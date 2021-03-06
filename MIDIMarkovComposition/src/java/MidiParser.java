import javax.sound.midi.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Midi Parser. Only parses format 0 and 1 MIDI files.
 */

public class MidiParser {

    private Sequence sequence;
    private int PPQN;
    private double[][] BPM; // [ tick #, BPM ]
    private long[][] timeSignature;  // [ tick #, numerator, denominator ]
    private long[][] keySignature;   // [tick #, sharp/flat count, major/minor]
    private double microsecondsPerTick;

    public MidiParser(Sequence seq) {

        // get sequence
        sequence = seq;

        // get Pulses Per Quarter Note (PPQN)
        PPQN = sequence.getResolution();

        // Parse all MIDI events
        ArrayList<double[]> BPMArray = new ArrayList<double[]>();
        ArrayList<long[]> timeSigArray = new ArrayList<long[]>();
        ArrayList<long[]> keySigArray = new ArrayList<long[]>();
        MidiEvent midiEvent;
        MetaMessage metaMessage;
        Track[] tracks = sequence.getTracks();
        for (Track track: tracks) {
            for (int j = 0; j < track.size(); j++) {
                if (isMetaEvent(midiEvent = track.get(j))) {
                    long tick = midiEvent.getTick();
                    if (isTempoMessage(metaMessage = (MetaMessage) midiEvent.getMessage())) {
                        byte[] bytes = metaMessage.getData();
                        int data = (bytes[0] & 0xff) << 16 | (bytes[1] & 0xff) << 8 | (bytes[2] & 0xff);
                        BPMArray.add(new double[] { (double) tick, 6e7 / data });
                    }
                    if (isTimeSigMessage(metaMessage = (MetaMessage) midiEvent.getMessage())) {
                        byte[] data = metaMessage.getData();
                        timeSigArray.add(new long[] { tick, (long) data[1], (long) data[2] });
                    }
                    if (isKeySigMessage(metaMessage = (MetaMessage) midiEvent.getMessage())) {
                        byte[] data = metaMessage.getData();
                        keySigArray.add(new long[] { tick, (long) data[0], (long) data[1] });
                    }
                }
            }
        }

        // convert from ArrayLists to array[][].
        if (BPMArray.size() == 0) {
            BPM = new double[0][0];
        } else {
            BPM = new double[BPMArray.size()][BPMArray.get(0).length];
            for (int i = 0; i < BPMArray.size(); i++)
                BPM[i] = BPMArray.get(i);
        }
        if (timeSigArray.size() == 0) {
            timeSignature = new long[0][0];
        } else {
            timeSignature = new long[timeSigArray.size()][timeSigArray.get(0).length];
            for (int i = 0; i < timeSigArray.size(); i++)
                timeSignature[i] = timeSigArray.get(i);
        }
        if (keySigArray.size() == 0) {
            keySignature = new long[0][0];
        } else {
            keySignature = new long[keySigArray.size()][keySigArray.get(0).length];
            for (int i = 0; i < keySigArray.size(); i++)
                keySignature[i] = keySigArray.get(i);
        }

        // calculate ticks per microsecond
        this.microsecondsPerTick = 6e7 / (this.getBPM()[0][1] * this.getPPQN());

    }

    public int getPPQN() {
        return PPQN;
    }

    public double[][] getBPM() {
        return BPM;
    }

    public long[][] getTimeSignature() {
        return timeSignature;
    }

    public long[][] getKeySignature() {
        return keySignature;
    }

    public double getMicrosecondsPerTick() {
        return microsecondsPerTick;
    }

    public Sequence getSequence() {
        return sequence;
    }

    /**
     *  Outputs all the midi events into a Nx8 matrix such that the structure is:
     *      0. track number
     *      1. channel number
     *      2. note number
     *      3. note_on velocity
     *      4. note_on time (tick)
     *      5. note_off time (tick)
     *      6. note_on event index
     *      7. note_off event index
     * @return matrix of note on/off data for MIDI sequence.
     */
    public ArrayList<Note> getNoteSequence() {
        final int vectorSize = 8;

        // get tracks from sequence
        Track[] tracks = sequence.getTracks();

        // initialize note info data structure
        ArrayList<Note> noteData = new ArrayList<Note>();

        // iterate through each track, retrieve all notes
        MidiEvent midiEvent;
        ShortMessage shortMessage;
        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];
            ArrayList<long[]> curTrackNoteData = new ArrayList<long[]>(track.size());
            for (int j = 0; j < track.size(); j++) {

                // check if a control event and a note message (note_on/note_off)
                if (isControlEvent(midiEvent = track.get(j)) &&
                        isNoteMessage(shortMessage = (ShortMessage) midiEvent.getMessage())) {

                    long[] note = new long[vectorSize];

                    // 0. Track Number
                    note[0] = i+1;
                    // 1. Channel number
                    note[1] = shortMessage.getChannel();
                    // 2. Note number
                    note[2] = shortMessage.getData1();

                    // if NOTE_ON
                    if (isNoteOnMessage(shortMessage)) {

                        // 3. note_on velocity
                        note[3] = shortMessage.getData2();
                        // 4. note_on time
                        note[4] = midiEvent.getTick();
                        // 6. note_on event index
                        note[6] = j;

                        // add new event to track
                        curTrackNoteData.add(note);

                    // if NOTE_OFF
                    } else if (isNoteOffMessage(shortMessage)) {

                        // look back until reaching the identical note_on event on the same channel
                        int index = 0;
                        for (int k = curTrackNoteData.size() - 1; k >= 0; k--) {
                            long[] tempNote = curTrackNoteData.get(k);
                            if (tempNote[2] == note[2]) {
                                note = tempNote;
                                index = k;
                                break;
                            }
                        }

                        // 5. note_off time;
                        note[5] = midiEvent.getTick();
                        // 7. note_off event index
                        note[7] = j;

                        // update note_on event
                        curTrackNoteData.set(index, note);
                    }
                }
            }
            // Create note object and add to noteData
            for (long[] nV: curTrackNoteData) {
                Note note = new Note((int)nV[0], (int)nV[1], (int)nV[2], (int)nV[3],
                        nV[4], nV[5], (int)nV[6], (int)nV[7], PPQN);
                noteData.add(note);
            }
        }
        Collections.sort(noteData);
        return noteData;
    }

    private boolean isMetaEvent(MidiEvent event) {
        return (event.getMessage().getStatus() == MetaMessage.META);
    }

    private boolean isSysExEvent(MidiEvent event) {
        return (event.getMessage().getStatus() == SysexMessage.SYSTEM_EXCLUSIVE);
    }

    private boolean isControlEvent(MidiEvent event) {
        return (!isMetaEvent(event) && !isSysExEvent(event));
    }

    private boolean isTimeSigMessage(MetaMessage metaMessage) {
        return metaMessage.getType() == 0x58;
    }

    private boolean isTempoMessage(MetaMessage metaMessage) {
        return metaMessage.getType() == 0x51;
    }

    private boolean isKeySigMessage(MetaMessage metaMessage) {
        return metaMessage.getType() == 0x59;
    }

    private boolean isNoteMessage(ShortMessage shortMessage) {
        return isNoteOffMessage(shortMessage) || isNoteOnMessage(shortMessage);
    }

    private boolean isNoteOnMessage(ShortMessage shortMessage) {
        return shortMessage.getCommand() == ShortMessage.NOTE_ON && shortMessage.getData2() > 0;
    }
    private boolean isNoteOffMessage(ShortMessage shortMessage) {
        return shortMessage.getCommand() == ShortMessage.NOTE_OFF ||
                (shortMessage.getCommand() == ShortMessage.NOTE_ON &&
                        shortMessage.getData2() == 0);
    }

}
