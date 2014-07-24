import java.util.*;

/**
 * Interface between Midi notes and Markov chains
 */

public class Midi2Markov {

    public static final int TOTALNOTES = 128;
    private MidiParser midiParser;
    private ArrayList<Note> notes;
    private int[] noteCount;    // count of all the available notes (0 to 127)
    private int[] noteIndex;

    private void initNotes(ArrayList<Note> notes) {
        this.notes = notes;

        // save a count of all notes
        noteCount = new int[TOTALNOTES];
        for (Note note : notes) {
            noteCount[note.getNoteNumber()]++;
        }
    }

    public Midi2Markov(MidiParser midiParser) {
        this(midiParser, false);
    }

    // constructor for transcribing any piece to C Major or A Minor
    public Midi2Markov(MidiParser midiParser, boolean transcribe) {
        this.midiParser = midiParser;

        ArrayList<Note> notes = midiParser.getNoteSequence();
        if (transcribe) {
            long[][] keySignature = midiParser.getKeySignature();
            if (keySignature.length == 0) {
                keySignature = new long[1][3];
                keySignature[0] = new long[] { 0, 0, 0 };
            }
            ArrayList<Note> transcribedNotes = new ArrayList<Note>();
            // iterate over all notes, transcribe to new key as specified in keySignature
            int index = 0;
            int keyTotalIndex = keySignature.length-1;
            int transposition = getTransposition((int) keySignature[index][1]);
            for (Note n: notes) {
                // update key Signature if necessary
                if (index < keyTotalIndex && keySignature[index+1][0] <= n.getNoteOnTime()) {
                    index = index+1;
                    transposition = getTransposition((int) keySignature[index][1]);
                }
                transcribedNotes.add(n.transcribe(transposition));
            }
            // initialize instance variables
            initNotes(transcribedNotes);
        } else {
            initNotes(notes);
        }
    }

    public MidiParser getMidiParser() {
        return midiParser;
    }

    // perform transcription from key signature to # of half step shifts
    private int getTransposition(int key) {
        return (key * 7) % 12;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    // 1x128 array of the count of notes in this markov chain
    public int[] getNoteCount() {
        return noteCount;
    }

    // 1xn array such that the ith value indicates the value of the note
    // in the ith index of the transition probability matrix
    public int[] getNoteIndex() {
        return noteIndex;
    }

    /**
     * Constructs transition matrices of temporal Markov Chains with sliding window.
     * @param order specifies the order of the Markov Chain
     * @param interval specifies the number of microseconds in the window
     * @param increment specifies the number of microseconds to move forward between each iteration
     * @return ArrayList of the transition matrices for the Markov Chain in each temporal window.
     * The structure is as follow: rows -> start markov node; cols -> end markov node, z -> window interval
     */
    public double[][][] getTemporalKeyTransitionMatrix(int order, long interval, long increment) {
        // validate interval and increment inputs
        int tickInterval = (int) (interval / midiParser.getMicrosecondsPerTick());
        int tickIncrement = (int) (increment / midiParser.getMicrosecondsPerTick());

        // validate order inputs
        int iterationEnd;
        if (order == 1) {
            iterationEnd = notes.size() - 1;
        } else if (order == 2) {
            iterationEnd = notes.size() - 2;
        } else {
            throw new IllegalArgumentException("Order must be 1 or 2!");
        }

        // Iterate over Notes array, fill in counts for each iteration
        ArrayList<double[][]> matrixList = new ArrayList<double[][]>();
        ArrayList<Note> notesInterval = new ArrayList<Note>();
        long startTick = 0;
        long endTick = startTick + tickInterval;
        for (int i = 0; i < iterationEnd; i++) {
            Note n = notes.get(i);
            if (n.getNoteOnTime() >= startTick && n.getNoteOnTime() <= endTick) {
                notesInterval.add(n);
            }
            // calculate note transition frequency, update start and end times
            if (n.getNoteOnTime() > endTick) {
                if (order == 1) {
                    double[][] transitionMatrix = new double[12][12];
                    for (int j = 0; j < notesInterval.size()-1; j++) {
                        int n1 = notesInterval.get(j).getKeyNumber();
                        int n2 = notesInterval.get(j+1).getKeyNumber();
                        transitionMatrix[n1][n2]++;
                    }
                    matrixList.add(transitionMatrix);
                } else {
                    double[][] transitionMatrix = new double[144][144];
                    for (int j = 0; j < notesInterval.size()-2; j++) {
                        int n1 = notesInterval.get(j).getKeyNumber();
                        int n2 = notesInterval.get(j+1).getKeyNumber();
                        int n3 = notesInterval.get(j+2).getKeyNumber();
                        transitionMatrix[n1*12+n2][n2*12+n3]++;
                    }
                    matrixList.add(transitionMatrix);
                }
                startTick += tickIncrement;
                endTick = startTick + tickInterval;
                i = 0;
                notesInterval.clear();
            }
        }

        // convert from ArrayList<double[][]> to double[][][], mainly for matlab integration
        int xsize = matrixList.get(0).length;
        int ysize = matrixList.get(0)[0].length;
        int zsize = matrixList.size();
        double[][][] matrixArray = new double[xsize][ysize][zsize];
        for (int i = 0; i < zsize; i++) {
            double[][] tMatrix = matrixList.get(i);

            // make matrix row stochastic
            for (int j = 0; j < xsize; j++) {
                int count = 0;
                for (int k = 0; k < ysize; k++) {
                    count += tMatrix[j][k];
                }
                if (count > 0) {
                    for (int k = 0; k < ysize; k++) {
                        matrixArray[j][k][i] = tMatrix[j][k] / (double) count;
                    }
                }
            }
        }
        return matrixArray;
    }

    // Transition matrix for 1st or 2nd order markov chain
    public double[][] getNoteTransitionMatrix(int order) {
        // validate order inputs
        int iterationEnd;
        if (order == 1) {
            iterationEnd = notes.size() - 1;
        } else if (order == 2) {
            iterationEnd = notes.size() - 2;
        } else {
            throw new IllegalArgumentException("Order must be 1 or 2!");
        }

        // Iterate over Notes array, fill in counts for note transitions
        Set<MarkovNode> markovNodeSet = new HashSet<MarkovNode>();
        HashMap<MarkovNode, ArrayList<MarkovNode>> nTPMap = new HashMap<MarkovNode, ArrayList<MarkovNode>>();
        for (int i = 0; i < iterationEnd; i++) {
            int[] curNoteNum = {notes.get(i).getNoteNumber()};
            int[] nextNoteNum = {notes.get(i + 1).getNoteNumber()};
            MarkovNode curNode = new MarkovNode(1, curNoteNum);
            MarkovNode nextNode = new MarkovNode(1, nextNoteNum);
            if (order == 2) {
                curNoteNum = new int[2];
                nextNoteNum = new int[2];
                curNoteNum[0] = notes.get(i).getNoteNumber();
                curNoteNum[1] = notes.get(i+1).getNoteNumber();
                nextNoteNum[0] = notes.get(i+1).getNoteNumber();
                nextNoteNum[1] = notes.get(i+2).getNoteNumber();
                curNode = new MarkovNode(2, curNoteNum);
                nextNode = new MarkovNode(2, nextNoteNum);
            }
            if (!nTPMap.containsKey(curNode)) {
                nTPMap.put(curNode, new ArrayList<MarkovNode>());
            }
            ArrayList<MarkovNode> noteArray = nTPMap.get(curNode);
            noteArray.add(nextNode);

            // add to markovnodeset
            markovNodeSet.add(curNode);
            markovNodeSet.add(nextNode);
        }

        // Set mapping from matrix indices to note linear indices
        int numnotes = markovNodeSet.size();
        noteIndex = new int[numnotes];
        int counter = 0;
        for (MarkovNode mn : markovNodeSet) {
            noteIndex[counter] = mn.getIndex();
            counter++;
        }
        Arrays.sort(noteIndex);

        // Add values to transition matrix
        int[][] nTP = new int[numnotes][numnotes];
        Set<MarkovNode> keys = nTPMap.keySet();
        for (MarkovNode key : keys) {
            ArrayList<MarkovNode> nodeList = nTPMap.get(key);
            for (MarkovNode val : nodeList) {
                int curIndex = Arrays.binarySearch(noteIndex, key.getIndex());
                int nextIndex = Arrays.binarySearch(noteIndex, val.getIndex());
                nTP[curIndex][nextIndex]++;
            }
        }

        // Normalize transition matrix (row stochastic)
        double[][] transitionMatrix = new double[numnotes][numnotes];
        for (int i = 0; i < numnotes; i++) {
            int count = 0;
            for (int j = 0; j < numnotes; j++) {
                count += nTP[i][j];
            }
            if (count > 0) {
                for (int j = 0; j < numnotes; j++) {
                    transitionMatrix[i][j] = ((double) nTP[i][j]) / ((double) count);
                }
            }
        }
        return transitionMatrix;
    }

    // Return a matrix of note information, primary for matlab matrix manipulations
    public long[][] getNoteMatrix() {
        // convert to double[][] for easier matlab integration :(
        long[][] noteMatrix = new long[notes.size()][notes.get(0).getNoteVector().length];
        for (int i = 0; i < notes.size(); i++) {
            long[] noteVector = notes.get(i).getNoteVector();
            System.arraycopy(noteVector, 0, noteMatrix[i], 0, noteVector.length);
        }
        return noteMatrix;
    }
}
