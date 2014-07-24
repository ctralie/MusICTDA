MusICTDA
========

Music Innovative Composition with Topological Data Analysis

### Setup
#### Linux/Mac
Run setup.sh on command line to compile java files.

    sh setup.sh

#### Windows
Run setup.bat on command line to compile java files.

    setup.bat

### Instructions
In matlab, run setup to add necessary files to path.

    setup

Run the following commands to play a midi file.

    midimain = loadmidi('lib/music_examples/short_sample_3.mid');
    midiplayer = midimain.getMidiPlayer;
	midiplayer.play;

To stop the audio anytime:

    midiplayer.stop;

To get original note information from the piece

    midimarkov1 = midimain.getMidi2Markov;
    noteMatrix1 = midimarkov1.getNoteMatrix;

To get note information transcribed to C Major or A Minor (Assumed to be so from this point on)

    midimarkov = midimain.getMidi2MarkovTranscribed;
    noteMatrix = midimarkov.getNoteMatrix;

To generate transition matrix for a first order Markov Chains of Notes:

    transitionMatrix1 = midimarkov.getNoteTransitionMatrix(1);

To generate transition matrix for a second order Markov Chain of Notes:

    transitionMatrix2 = midimarkov.getNoteTransitionMatrix(2);

To generate transition matrices for a Short-Time Markov Chain of first order,
with 10 second window and 1 second increment:

    stMatrix1 = midimarkov.getTemporalKeyTransitionMatrix(1, 1e7, 1e6);

To generate transition matrices for a Short-Time Markov Chain of second order,
with 10 second window and 1 second increment:

    stMatrix2 = midimarkov.getTemporalKeyTransitionMatrix(2, 1e7, 1e6);
