import javax.sound.midi.*;
import java.lang.Exception;
import java.io.File;

/**
 * Starting point for all other MIDI-related operations.
 */

public class MidiMain {

    private Sequence sequence;
    private MidiParser midiParser;
    private MidiPlayer midiPlayer;
    private Midi2Markov midi2Markov;
    private Midi2Markov midi2MarkovTranscribed;

    public MidiMain(String path) {
        try {
            sequence = MidiSystem.getSequence(new File(path));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        midiParser = new MidiParser(sequence);
        midiPlayer = MidiPlayer.getMidiPlayerInstance(sequence);
        midi2Markov = new Midi2Markov(midiParser);
        midi2MarkovTranscribed = new Midi2Markov(midiParser, true);
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

    public Midi2Markov getMidi2MarkovTranscribed() {
        return midi2MarkovTranscribed;
    }

    public static void main(String[] args) {
        MidiMain midi = new MidiMain("lib/music_examples/short_sample_3.mid");
        MidiParser midiParser1 = midi.getMidiParser();
        MidiPlayer midiPlayer1 = midi.getMidiPlayer();
        Midi2Markov midi2Markov1 = midi.getMidi2MarkovTranscribed();
        Midi2Markov midi2Markov2 = midi.getMidi2Markov();

//        double[][] tMatrix1 = midi2Markov1.getNoteTransitionMatrix(1);
//        int[] noteIndex1 = midi2Markov1.getNoteIndex();
//        System.out.println(tMatrix1.length + " | " + tMatrix1[0].length);
//        System.out.println(noteIndex1.length);
//
//        double[][] tMatrix2 = midi2Markov1.getNoteTransitionMatrix(2);
//        int[] noteIndex2 = midi2Markov1.getNoteIndex();
//        System.out.println(tMatrix2.length + " | " + tMatrix2[0].length);
//        System.out.println(noteIndex2.length);
//
//        for (int i = 0; i < midi2Markov1.getNoteMatrix().length; i++) {
//            System.out.println(midi2Markov1.getNoteMatrix()[i][2] == midi2Markov2.getNoteMatrix()[i][2]);
//        }
//        midiPlayer1.play();

        double[][][] tMatrix1 = midi2Markov1.getTemporalKeyTransitionMatrix(1, (long) 1e7, (long) 1e6);
    }

}
