import java.util.ArrayList;

/**
 * Interface between Midi notes and Markov chains
 */

public class Midi2Markov {

    public static final int NUMNOTES = 128;
    private ArrayList<Note> notes;
    int[] noteCount;

    public Midi2Markov(ArrayList<Note> notes) {
        this.notes = notes;

        // save a count of all notes
        noteCount = new int[NUMNOTES];
        for (Note note: notes) {
            noteCount[note.getNoteNumber()]++;
        }

    }

    public double[][] getFirstOrderTransitionMatrix() {

        // Iterate over Notes array, fill in counts for note transitions
        int[][] nTP = new int[NUMNOTES][NUMNOTES];
        for (int i = 0; i < notes.size()-1; i++) {
            int curNote = notes.get(i).getNoteNumber();
            int nextNote = notes.get(i+1).getNoteNumber();
            nTP[curNote][nextNote]++;
        }

        // Normalize transition matrix (row stochastic)
        double[][] transitionMatrix = new double[NUMNOTES][NUMNOTES];
        for (int i = 0; i < NUMNOTES; i++) {
            int count = 0;
            for (int j = 0; j < NUMNOTES; j++) {
                count += nTP[i][j];
            }
            if (count > 0) {
                for (int j = 0; j < NUMNOTES; j++) {
                    transitionMatrix[i][j] = ((double) nTP[i][j]) / ((double) count);
                }
            }
        }
        return transitionMatrix;
    }

    public double[][] getSecondOrderTransitionMatrix() {
        // Iterate over Notes array, fill in counts for note transitions
        int[][] nTP = new int[NUMNOTES*NUMNOTES][NUMNOTES*NUMNOTES];
        for (int i = 0; i < notes.size()-2; i++) {
            int curNote = notes.get(i).getNoteNumber();
            int nextNote = notes.get(i+1).getNoteNumber();
            int nextnextNote = notes.get(i+2).getNoteNumber();
            nTP[curNote*(nextNote-1) + nextNote][nextnextNote]++;
        }

        // Normalize transition matrix (row stochastic)
        double[][] transitionMatrix = new double[NUMNOTES*NUMNOTES][NUMNOTES*NUMNOTES];
        for (int i = 0; i < NUMNOTES*NUMNOTES; i++) {
            int count = 0;
            for (int j = 0; j < NUMNOTES*NUMNOTES; j++) {
                count += nTP[i][j];
            }
            if (count > 0) {
                for (int j = 0; j < NUMNOTES * NUMNOTES; j++) {
                    transitionMatrix[i][j] = ((double) nTP[i][j]) / ((double) count);
                }
            }
        }
        return transitionMatrix;
    }
}
