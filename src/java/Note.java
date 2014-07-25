import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

/**
 * Note object class, encapsulates a Note message.
 */

public class Note implements Comparable<Note> {

    private int trackNumber;
    private int channelNumber;
    private int noteNumber;
    private int velocity;
    private long noteOnTime;
    private long noteOffTime;
    private int noteOnIndex;
    private int noteOffIndex;
    private int PPQN;
    private long duration;
    private double type;

    public static final int C5 = 60;

    public static final double WHOLE = 4.0;
    public static final double HALF = 2.0;
    public static final double QUARTER = 1.0;
    public static final double EIGHTH = 1/2;
    public static final double SIXTEENTH = 1/4;

    public Note(int track, int chan, int nn, int v, long onT, long offT,
                int onIndex, int offIndex, int PPQN) {

        this.trackNumber = track;
        this.channelNumber = chan;
        this.noteNumber = nn;
        this.velocity = v;
        this.noteOnTime = onT;
        this.noteOffTime = offT;
        this.noteOnIndex = onIndex;
        this.noteOffIndex = offIndex;
        this.PPQN = PPQN;

        this.duration = noteOffTime - noteOnTime;
        this.type = this.duration / this.PPQN;
    }

    public int getTrackNumber() { return trackNumber; }
    public int getChannelNumber() { return channelNumber; }
    public int getNoteNumber() { return noteNumber; }
    public int getVelocity() { return velocity; }
    public long getNoteOnTime() { return noteOnTime; }
    public long getNoteOffTime() { return noteOffTime; }
    public int getNoteOnIndex() { return noteOnIndex; }
    public int getNoteOffIndex() { return noteOffIndex; }
    public int getPPQN() { return PPQN; }
    public long getDuration() { return duration; }
    public double getType() { return type; }
    public boolean isHalfNote() { return type == HALF; }
    public boolean isQuarterNote() { return type == QUARTER; }
    public boolean isEighthNote() { return type == EIGHTH; }

    public long[] getNoteVector() {
        return new long[] {
                this.trackNumber, this.channelNumber,
                this.noteNumber, this.velocity, this.noteOnTime,
                this.noteOffTime, this.noteOnIndex, this.noteOffIndex
        };
    }

    // transcribe the note by however many halfSteps.
    public Note transcribe(int halfSteps) {
        this.noteNumber += halfSteps;
        return this;
    }

    // returns key number, from C->0 to B->12.
    public int getKeyNumber() {
        return this.noteNumber % 12;
    }

    // outputs midi events corresponding to Midi note On and Off
    public MidiEvent[] getKeyEvents() {
        try {
            MidiMessage shortMessageOn = new ShortMessage(ShortMessage.NOTE_ON,
                    this.channelNumber, this.getKeyNumber() + C5, this.getVelocity());

            MidiMessage shortMessageOff = new ShortMessage(ShortMessage.NOTE_OFF,
                    this.channelNumber, this.getKeyNumber() + C5, this.getVelocity());
            MidiEvent midiEventOn = new MidiEvent(shortMessageOn, this.noteOnTime);
            MidiEvent midiEventOff = new MidiEvent(shortMessageOff, this.noteOffTime);

            return new MidiEvent[]{midiEventOn, midiEventOff};
        } catch (InvalidMidiDataException e) {
            throw new IllegalArgumentException("Error in getKeyEvents :(");
        }
    }

    @Override
    public int compareTo(Note o) {
        if (this.noteOnTime < o.noteOnTime)
            return -1;
        else if (this.noteOnTime > o.noteOnTime)
            return 1;
        else
            return 0;
    }
}
