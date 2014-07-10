
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import java.lang.Exception;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;

public class MidiAudio {

    private File songpath;
    private Sequence sequence;
    private PlayRunnable pr;

    public MidiAudio(String path) {
        songpath = new File(path);
        try {
            sequence = MidiSystem.getSequence(songpath);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    // Play the midi audio
    public void play() {
            pr = new PlayRunnable(sequence);
            Thread th = new Thread(pr);
            th.start();
    }

    // Stop the midi audio
    public void stop() {
        pr.kill();
    }

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

}
