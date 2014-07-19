import java.util.*;

/**
 * Interface between Midi notes and Markov chains
 */

public class Midi2Markov {

    public static final int TOTALNOTES = 128;
    private ArrayList<Note> notes;
    private int[] noteCount;    // count of all the available notes (0 to 127)
    private int[] noteIndex;

    private void init(ArrayList<Note> notes) {
        this.notes = notes;

        // save a count of all notes
        noteCount = new int[TOTALNOTES];
        for (Note note : notes) {
            noteCount[note.getNoteNumber()]++;
        }
    }

    public Midi2Markov(ArrayList<Note> notes) {
        init(notes);
    }

    // constructor for transcribing any piece to C Major or A Minor
    public Midi2Markov(ArrayList<Note> notes, long[][] keySignature) {
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
        init(transcribedNotes);
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
