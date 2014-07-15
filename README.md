MusICTDA
========

Music Innovative Composition with Topological Data Analysis

Run setup.sh on command line to compile java files.

    sh setup.sh

In matlab, run setup to add necessary files to path.

    setup

Run the following commands to play a midi file.

    midiaudio = loadmidi('lib/music_examples/short_sample_1.mid');
	midiaudio.play();

To stop the audio anytime:

    midiaudio.stop();

To get note information from the piece:

    noteMatrix = midiaudio.getNoteMatrix;

To generate transition matrix for a first order Markov Chains of Notes:

    transitionMatrix = midiaudio.getFirstOrderMarkovTransitionMatrix;
