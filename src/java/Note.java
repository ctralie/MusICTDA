
public class Note {

    private int PPQN;
    private int value;
    private int duration;
    private int type;

    public static final double WHOLE = 4.0;
    public static final double HALF = 2.0;
    public static final double QUARTER = 1.0;
    public static final double EIGHTH = 1/2;
    public static final double SIXTEENTH = 1/4;

    public Note(int PPQN, int noteValue, int noteDuration) {
        this.PPQN = PPQN;
        this.value = noteValue;
        this.duration = noteDuration;
        this.type = this.duration / this.PPQN;
    }

    public int getPPQN() {
        return PPQN;
    }

    public int getValue() {
        return value;
    }

    public int duration() {
        return duration;
    }

    public int getType() {
        return type;
    }

}
