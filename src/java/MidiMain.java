import javax.sound.midi.*;
import java.lang.Exception;
import java.io.File;
import java.util.ArrayList;

/**
 * Starting point for all other MIDI-related operations.
 */

public class MidiMain {

    private Sequence sequence;
    private MidiParser midiParser;
    private MidiPlayer midiPlayer;

    public MidiMain(String path) {
        try {
            sequence = MidiSystem.getSequence(new File(path));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        midiParser = new MidiParser(sequence);
        midiPlayer = MidiPlayer.getMidiPlayerInstance(sequence);
    }

    public Sequence getSequence() {
        return sequence;
    }

    // Return an ArrayList of ordered Notes
    public ArrayList<Note> getNoteSequence() {
        return midiParser.getNoteSequence();
    }

    // Return a matrix of note information, primary for matlab matrix manipulations
    public long[][] getNoteMatrix() {
        MidiParser midiParser = new MidiParser(sequence);
        ArrayList<Note> noteData = midiParser.getNoteSequence();

        // convert to double[][] for easier matlab integration :(
        long[][] noteMatrix = new long[noteData.size()][noteData.get(0).getNoteVector().length];
        for (int i = 0; i < noteData.size(); i++) {
            long[] noteVector = noteData.get(i).getNoteVector();
            System.arraycopy(noteVector, 0, noteMatrix[i], 0, noteVector.length);
        }
        return noteMatrix;
    }

    // Play the midi audio on MIDI compatible speakers
    public void play() {
        midiPlayer.play();
    }

    // Stop the midi audio (assumed that the audio is already playing)
    public void stop() {
        midiPlayer.stop();
    }

    public static void main(String[] args) {
        MidiMain midi = new MidiMain("lib/music_examples/short_sample_1.mid");
        midi.getNoteMatrix();
        midi.play();
    }

}
