import javax.sound.midi.*;
import java.lang.Exception;
import java.io.File;
import java.util.ArrayList;

public class MidiAudio {

    private Sequence sequence;
    private PlayRunnable pr;

    public MidiAudio(String path) {
        try {
            sequence = MidiSystem.getSequence(new File(path));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public Sequence getSequence() {
        return sequence;
    }

    public double[][] getNotesMatrix() {
        MidiParser midiParser = new MidiParser(sequence);
        ArrayList<double[]> noteData = midiParser.getNotes();

        // convert to double[][] for easier matlab integration :(
        double[][] noteMatrixData = new double[noteData.size()][noteData.get(0).length];
        for (int i = 0; i < noteData.size(); i++)
            for (int j = 0; j < noteData.get(i).length; j++)
                noteMatrixData[i][j] = noteData.get(i)[j];

        return noteMatrixData;
    }

    // Play the midi audio on MIDI compatible speakers
    public void play() {
            pr = new PlayRunnable(sequence);
            Thread th = new Thread(pr);
            th.start();
    }

    // Stop the midi audio (assumed that the audio is already playing)
    public void stop() {
        pr.kill();
    }

    // Separate thread to play the audio
    public class PlayRunnable implements Runnable {

        private volatile boolean running;
        private Sequence seq;

        public PlayRunnable(Sequence s) {
            running = true;
            seq = s;
        }

        public void run() {
            try {
                Sequencer player = MidiSystem.getSequencer();
                player.setSequence(seq);
                player.open();
                player.start();
                while(running) {
                    if(player.isRunning()) {
                        try {
                            Thread.sleep(1000); // Check every second
                        } catch(InterruptedException ignore) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                // Close resources
                player.stop();
                player.close();
            } catch(Exception e) {
                System.out.println(e.toString());
            }
        }

        public void kill() {
            running = false;
        }

    }

    public static void main(String[] args) {
        MidiAudio midi = new MidiAudio("lib/music_examples/short_sample_1.mid");
        midi.play();
        midi.getNotesMatrix();
    }

}
