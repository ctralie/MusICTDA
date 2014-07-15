MusICTDA
========

Music Innovative Composition with Topological Data Analysis

Run setup.sh on command line to compile java files.

    sh setup.sh

In matlab, run setup to add necessary files to path.

    setup

Run the following commands to play a midi file.

    midimain = loadmidi('lib/music_examples/short_sample_3.mid');
    midiplayer = midimain.getMidiPlayer;
	midiplayer.play;

To stop the audio anytime:

    midiplayer.stop;

To get note information from the piece:

    midiparser = midimain.getMidiParser;
    noteMatrix = midiparser.getNoteMatrix;

To generate transition matrix for a first order Markov Chains of Notes:

    midimarkov = midimain.getMidi2Markov;
    transitionMatrix1 = midimarkov.getTransitionMatrix(1);

To generate transition matrix for a second order Markov Chain of Notes:

    transitionMatrix2 = midimarkov.getTransitionMatrix(2);
