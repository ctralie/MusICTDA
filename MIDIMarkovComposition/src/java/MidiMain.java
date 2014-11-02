import javax.sound.midi.*;
import java.io.IOException;
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
    private MidiWriter midiWriter;

    public MidiMain(String path) {
        try {
            sequence = MidiSystem.getSequence(new File(path));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        midiParser = new MidiParser(sequence);
        midiPlayer = MidiPlayer.getMidiPlayerInstance(path);
        midi2Markov = new Midi2Markov(midiParser);
        midi2MarkovTranscribed = new Midi2Markov(midiParser, true);
        midiWriter = MidiWriter.getMidiPlayerInstance();
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

    public MidiWriter getMidiWriter() {
        return midiWriter;
    }

    public static void main(String[] args) {
        MidiMain midi = new MidiMain("lib/music_examples/short_sample_3.mid");
        MidiParser midiParser1 = midi.getMidiParser();
        double[][] bpm = midiParser1.getBPM();
        int PPQN = midiParser1.getPPQN();
        System.out.println("bpm: " + bpm[0][1]);
        System.out.println("PPQN: " + PPQN);
        MidiWriter midiWriter1 = midi.getMidiWriter();
        int[] chromscale = {0,1,2,3,4,5,6,7,8,9,10,11,11,10,9,8,7,6,5,4,3,2,1,0};
        midiWriter1.setNotes(chromscale);
        midiWriter1.setBPM(bpm);
        midiWriter1.setPPQN(PPQN);
        try {
            midiWriter1.writeToFile("output.mid");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        MidiMain midiMain2 = new MidiMain("output.mid");
        MidiParser midiParser2 = midiMain2.getMidiParser();
        double[][] bpm2 = midiParser2.getBPM();
        int PPQN2 = midiParser2.getPPQN();
        System.out.println("bpm2: " + bpm2[0][1]);
        System.out.println("PPQN2: " + PPQN2);
        MidiPlayer midiPlayer1 = midiMain2.getMidiPlayer();
        midiPlayer1.play();
    }

}
