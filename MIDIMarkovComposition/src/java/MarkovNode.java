import java.util.Arrays;

public class MarkovNode {

    private int order;
    private int[] noteNumbers;
    private int index;

    public MarkovNode(int order, int[] noteNumbers) {
        if (order == 1 || order == 2)
            this.order = order;
        else {
            throw new IllegalArgumentException("Model must be of order 1 or 2!");
        }
        this.noteNumbers = noteNumbers;
        this.index = computeIndex(order, noteNumbers);
    }
    public MarkovNode(int order, int index) {
        if (order == 1 || order == 2)
            this.order = order;
        else {
            throw new IllegalArgumentException("Model must be of order 1 or 2!");
        }
        this.index = index;
        this.noteNumbers = computeNoteNumbers(order, index);
    }

    // Determine note index (NOTE: index starts at 1)
    private static int computeIndex(int order, int[]noteNumbers) {
        int result = 0;
        if (order == 1) {
            result =  noteNumbers[0] + 1;
        } else if (order == 2) {
            int a = noteNumbers[0];
            int b = noteNumbers[1];
            result =  a*128 + b + 1;
        }
        return result;
    }

    // Determine note number from linear index (NOTE: index starts at 1)
    private static int[] computeNoteNumbers(int order, int index) {
        int[] result = new int[order];
        if (order == 1) {
            result[0] = index - 1;
        } else if (order == 2) {
            int c = index;
            int a = (c-1)/128;
            int b = (c-1)%128;
            result[0] = a;
            result[1] = b;
        }
        return result;
    }

    public int getOrder() {
        return order;
    }

    public int getIndex() {
        return index;
    }

    public int[] getNoteNumbers() {
        return noteNumbers;
    }

    public static int getIndex(int order, int[] noteNumbers) {
        return computeIndex(order, noteNumbers);
    }

    public static int[] getNoteNumbers(int order, int index) {
        return computeNoteNumbers(order, index);
    }

    @Override
    public int hashCode() {
        return 7*order + 13*index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarkovNode that = (MarkovNode) o;

        if (index != that.index) return false;
        if (order != that.order) return false;
        if (!Arrays.equals(noteNumbers, that.noteNumbers)) return false;

        return this.hashCode() == o.hashCode();
    }

}
