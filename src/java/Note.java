
public class Note {

    private int PPQN;
    private int value;
    private int duration

    public Note(int PPQN, int noteValue, int noteDuration) {
        this.PPQN = PPQN;
        this.value = noteValue;
        this.duration = noteDuration;
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

    

}
