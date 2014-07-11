import javax.sound.midi.*;
import java.util.ArrayList;

/**
 * Midi Parser. Only parses format 0 and 1 MIDI files.
 */

public class MidiParser {

    private Sequence sequence;

    public MidiParser(Sequence seq) {
        sequence = seq;
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

    public ArrayList<double[]> getNotes() {
        int vectorSize = 8;

        // get tracks from sequence
        Track[] tracks = sequence.getTracks();

        // initialize note info data structure
        ArrayList<double[]> noteData = new ArrayList<double[]>();

        // iterate through each track, retrieve all notes
        MidiEvent midiEvent;
        ShortMessage shortMessage;
        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];
            ArrayList<double[]> curTrackNoteData = new ArrayList<double[]>(track.size());
            for (int j = 0; j < track.size(); j++) {

                // check if a control event and a note message (note_on/note_off)
                if (isControlEvent(midiEvent = track.get(j)) &&
                        isNoteMessage(shortMessage = (ShortMessage) midiEvent.getMessage())) {

                    double[] note = new double[vectorSize];

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
                            double[] tempNote = curTrackNoteData.get(k);
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
            noteData.addAll(curTrackNoteData);
        }
        return noteData;
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
