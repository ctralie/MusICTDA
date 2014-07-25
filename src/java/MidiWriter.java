import com.sun.media.sound.StandardMidiFileWriter;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.midi.spi.MidiFileWriter;
import java.io.File;
import java.io.IOException;

public class MidiWriter {

    private MidiFileWriter midiFileWriter;
    private Sequence sequence;
    private Track track;
    private int PPQN;
    public static final int DEFAULT_VELOCITY = 64;

    public MidiWriter(int[] notes, int PPQN) {
        this.midiFileWriter = new StandardMidiFileWriter();
        this.PPQN = PPQN;

        // initialize sequence and track
        try {
            sequence = new Sequence(Sequence.PPQ, PPQN);
            track = sequence.createTrack();
        } catch (InvalidMidiDataException e) {
            throw new IllegalArgumentException("Error in MidiWriter :(");
        }

        // add notes to track
        long onTime = 0;
        for (int i : notes) {
            Note n = new Note(1, 1, i, DEFAULT_VELOCITY, onTime, onTime+PPQN, 0, 0, PPQN);
            MidiEvent[] midiEvents = n.getKeyEvents();
            track.add(midiEvents[0]);
            track.add(midiEvents[1]);
            onTime += PPQN;
        }
    }

    public MidiWriter(int[] notes) {
        this(notes, 960);
    }

    // writes note sequence to Midi File
    public void writeToFile(String fileName) throws IOException {
        File f = new File(fileName);
        midiFileWriter.write(sequence, 0, f);
    }

}
