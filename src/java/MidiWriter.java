import com.sun.media.sound.StandardMidiFileWriter;

import javax.sound.midi.*;
import javax.sound.midi.spi.MidiFileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MidiWriter {

    private static MidiWriter midiWriter;

    private MidiFileWriter midiFileWriter;
    private Sequence sequence;
    private int[] notes;
    private int PPQN;
    private double[][] BPM;
    public static final int DEFAULT_VELOCITY = 64;
    public static final int DEFAULT_PPQN = 96;
    public static final int TEMPO_TYPE = 0x51;

    private MidiWriter() {
        this.PPQN = DEFAULT_PPQN;
        this.BPM = new double[1][2];
        this.notes = new int[0];
    }
    public static MidiWriter getMidiPlayerInstance() {
        if (null == midiWriter) {
            midiWriter = new MidiWriter();
        }
        return midiWriter;
    }

    public void setNotes(int[] notes) {
        this.notes = notes;
        initialize();
    }

    public void setPPQN(int PPQN) {
        this.PPQN = PPQN;
        initialize();
    }

    public void setBPM(double[][] BPM) {
        this.BPM = BPM;
    }

    private byte[] BPM2Tempo(double BPM) {
        int msqb = (int) (6e7 / BPM);
        byte[] data = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN).putInt(msqb).array();
        byte[] newdata = new byte[3];
        System.arraycopy(data, 1, newdata, 0, newdata.length);
        return newdata;
    }

    private void initialize() {
        this.midiFileWriter = new StandardMidiFileWriter();

        // initialize sequence and track
        try {
            sequence = new Sequence(Sequence.PPQ, PPQN);
        } catch (InvalidMidiDataException e) {
            throw new IllegalArgumentException("Error creating Sequence in MidiWriter :(");
        }
        Track track = sequence.createTrack();

        // add tempo metaevent to track
        byte[] data = BPM2Tempo(this.BPM[0][1]);
        try {
            MetaMessage metaMessage = new MetaMessage(TEMPO_TYPE, data, 3);
            MidiEvent midiEvent = new MidiEvent(metaMessage, (long) this.BPM[0][0]);
            track.add(midiEvent);
        } catch (InvalidMidiDataException e) {
            throw new IllegalArgumentException("Error creating tempo MetaMessage in MidiWriter.");
        }

        // add notes events to track
        long onTime = 0;
        for (int i : notes) {
            Note n = new Note(1, 1, i, DEFAULT_VELOCITY, onTime, onTime+PPQN, 0, 0, PPQN);
            MidiEvent[] midiEvents = n.getKeyEvents();
            track.add(midiEvents[0]);
            track.add(midiEvents[1]);
            onTime += PPQN;
        }
    }

    // writes note sequence to Midi File
    public void writeToFile(String fileName) throws IOException {
        File f = new File(fileName);
        midiFileWriter.write(sequence, 0, f);
    }

    public static void main(String[] args) {
        MidiWriter midiWriter1 = new MidiWriter();
        byte[] data = midiWriter1.BPM2Tempo(100);
        for (byte d : data) {
            System.out.println(d);
        }
    }

}
