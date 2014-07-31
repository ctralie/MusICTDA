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
    private int[] notes;
    private int[] velocities;
    private long[] noteOnTimes;
    private long[] noteOffTimes;
    private int PPQN;
    private double[][] BPM;
    public static final int DEFAULT_VELOCITY = 64;
    public static final int DEFAULT_PPQN = 96;
    public static final int TEMPO_TYPE = 0x51;

    private MidiWriter() {
        this.PPQN = DEFAULT_PPQN;
        this.BPM = new double[1][2];
        this.BPM[0] = new double[] {0, 100};
        this.notes = new int[0];
        this.velocities = new int[0];
        this.noteOnTimes = new long[0];
        this.noteOffTimes = new long[0];
    }
    public static MidiWriter getMidiPlayerInstance() {
        if (null == midiWriter) {
            midiWriter = new MidiWriter();
        }
        return midiWriter;
    }

    public int[] getNotes() {
        return this.notes;
    }

    public void setNoteOnTimes(long[] onTimes) {
        this.noteOnTimes = onTimes;
    }

    public void setNoteOffTimes(long[] offTimes) {
        this.noteOffTimes = offTimes;
    }

    public void setVelocity(int[] velocities) {
        this.velocities = velocities;
    }

    public void setNotes(int[] notes) {
        this.notes = notes;
    }

    public void setPPQN(int PPQN) {
        this.PPQN = PPQN;
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

    private Sequence initializeSequence() throws InvalidMidiDataException {
        this.midiFileWriter = new StandardMidiFileWriter();

        /**
         * Validate Sequence inputs and modify as appropriate.
         */
        // validate notes count
        if (this.notes.length == 0)
            throw new IllegalArgumentException("MidiWriter: No notes loaded in writer!");

        // validate velocities
        if (this.velocities.length == 0) {
            this.velocities = new int[this.notes.length];
            for (int i = 0; i < this.velocities.length; i++) {
                this.velocities[i] = DEFAULT_VELOCITY;
            }
        }
        if (this.velocities.length != this.notes.length)
            throw new IllegalArgumentException("MidiWriter: Velocity vector not the same size as that of notes!");

        // validate NoteOnTimes
        long startTime = 0;
        if (this.noteOnTimes.length == 0) {
            this.noteOnTimes = new long[this.notes.length];
            for (int i = 0; i < this.noteOnTimes.length; i++) {
                this.noteOnTimes[i] = startTime;
                startTime = startTime + this.PPQN;
            }
        }
        if (this.noteOnTimes.length != this.notes.length)
            throw new IllegalArgumentException("MidiWriter: NoteOnTime vector not the same size as that of notes!");

        // validate noteOffTimes
        long endTime = this.PPQN;
        if (this.noteOffTimes.length == 0) {
            this.noteOffTimes = new long[this.notes.length];
            for (int i = 0; i < this.noteOffTimes.length; i++) {
                this.noteOffTimes[i] = endTime;
                endTime = endTime + this.PPQN;
            }
        }
        if (this.noteOffTimes.length != this.notes.length)
            throw new IllegalArgumentException("MidiWriter: NoteOffTime vector not the same size as that of notes!");

        // initialize sequence and track
        Sequence sequence = new Sequence(Sequence.PPQ, this.PPQN);
        Track track = sequence.createTrack();

        // add tempo (BPM) metaevent to track
        for (double[] aBPM : this.BPM) {
            byte[] data = BPM2Tempo(aBPM[1]);
            try {
                MetaMessage metaMessage = new MetaMessage(TEMPO_TYPE, data, 3);
                MidiEvent midiEvent = new MidiEvent(metaMessage, (long) aBPM[0]);
                track.add(midiEvent);
            } catch (InvalidMidiDataException e) {
                throw new IllegalArgumentException("Error creating tempo MetaMessage in MidiWriter.");
            }
        }

        // add notes events to track
        for (int i = 0; i < this.notes.length; i++) {
            int nn = this.notes[i];
            int v = this.velocities[i];
            long noteOn = this.noteOnTimes[i];
            long noteOff = this.noteOffTimes[i];
            Note n = new Note(1, 1, nn, v , noteOn, noteOff, 0, 0, this.PPQN);
            MidiEvent[] midiEvents = n.getNoteEvents();
            track.add(midiEvents[0]);
            track.add(midiEvents[1]);
        }
        return sequence;
    }

    // writes note sequence to Midi File
    public void writeToFile(String fileName) throws IOException, InvalidMidiDataException {
        File f = new File(fileName);
        Sequence seq = initializeSequence();
        midiFileWriter.write(seq, 0, f);
    }

    public static void main(String[] args) {
        MidiWriter midiWriter1 = new MidiWriter();
        byte[] data = midiWriter1.BPM2Tempo(100);
        for (byte d : data) {
            System.out.println(d);
        }
    }

}
