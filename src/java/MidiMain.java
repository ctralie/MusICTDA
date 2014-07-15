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
    private Midi2Markov midi2Markov;

    public MidiMain(String path) {
        try {
            sequence = MidiSystem.getSequence(new File(path));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        midiParser = new MidiParser(sequence);
        midiPlayer = MidiPlayer.getMidiPlayerInstance(sequence);
        midi2Markov = new Midi2Markov(midiParser.getNoteSequence());
    }

    public Sequence getSequence() {
        return sequence;
    }

    public MidiParser getMidiParser() {
        return midiParser;
    }

    public MidiPlayer getMidiPlayer() {
        return midiPlayer;
    }

    public Midi2Markov getMidi2Markov() {
        return midi2Markov;
    }

    public static void main(String[] args) {
        MidiMain midi = new MidiMain("lib/music_examples/short_sample_1.mid");
        MidiParser midiParser1 = midi.getMidiParser();
        MidiPlayer midiPlayer1 = midi.getMidiPlayer();
        Midi2Markov midi2Markov1 = midi.getMidi2Markov();

        double[][] tMatrix1 = midi2Markov1.getTransitionMatrix(1);
        int[] noteIndex1 = midi2Markov1.getNoteIndex();
        System.out.println(tMatrix1.length + " | " + tMatrix1[0].length);
        System.out.println(noteIndex1.length);

        double[][] tMatrix2 = midi2Markov1.getTransitionMatrix(2);
        int[] noteIndex2 = midi2Markov1.getNoteIndex();
        System.out.println(tMatrix2.length + " | " + tMatrix2[0].length);
        System.out.println(noteIndex2.length);

        long[][] noteMatrix = midiParser1.getNoteMatrix();
        midiPlayer1.play();
    }

}
