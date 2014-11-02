import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import java.io.File;

/**
 * Custom Midi audio player
 */

public class MidiPlayer {

    private static MidiPlayer midiPlayer;
    private Sequence sequence;
    private PlayRunnable pr;

    // Ensures only ONE MidiPlayer object
    private MidiPlayer() {}
    public static MidiPlayer getMidiPlayerInstance(Sequence seq) {
        if (null == midiPlayer) {
            midiPlayer = new MidiPlayer();
            midiPlayer.setSequence(seq);
        }
        return midiPlayer;
    }
    public static MidiPlayer getMidiPlayerInstance(String path) {
        try {
            Sequence seq = MidiSystem.getSequence(new File(path));
            resetPlayer();
            return getMidiPlayerInstance(seq);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    // resets the MidiPlayer to null.
    public static boolean resetPlayer() {
        if (midiPlayer == null) return false;
        else midiPlayer = null;
        return true;
    }

    private void setSequence(Sequence seq) {
        this.sequence = seq;
    }

    // Play the midi audio on MIDI compatible speakers
    public void play() {
        if (null != pr && pr.isRunning()) {
            pr.kill();
        }
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
                while (running) {
                    if (player.isRunning()) {
                        try {
                            Thread.sleep(1000); // Check every second
                        } catch (InterruptedException ignore) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                // Close resources
                player.stop();
                player.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        public void kill() {
            running = false;
        }

        public boolean isRunning() {
            return running;
        }
    }

    public static void main(String[] args) {
        MidiPlayer mp = MidiPlayer.getMidiPlayerInstance("lib/music_examples/short_sample_1.mid");
        mp.play();
    }

}