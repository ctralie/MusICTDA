% load midi file
midimain1 = loadmidi('lib/music_examples/short_sample_3.mid');
% parse midi file
midiparser1 = midimain1.getMidiParser;
ppqn = midiparser1.getPPQN
bpm = midiparser1.getBPM
% set window/increment parameters
windowMicro = 1e7;
incrementMicro = 5e5;
% get ST markov chain
midimarkov = midimain1.getMidi2Markov;
tmatrix = midimarkov.getTemporalKeyTransitionMatrix(1, windowMicro, incrementMicro);
% make cool video
makeSynchronizedMCVideos(tmatrix, windowMicro, incrementMicro, 'lib/music_examples/short_sample_3.mp3', 'output.flv', 0);
% create new melody from markov chain
stmc = STMarkovChain(tmatrix);
notes = stmc.getSTSequence(1,0);
% write melody to output
midiwriter = midimain1.getMidiWriter;
midiwriter.setNotes(notes);
midiwriter.setPPQN(ppqn);
midiwriter.setBPM(bpm);
midiwriter.writeToFile('output.mid');

% load the output melody
midimain2 = loadmidi('output.mid');
midiparser2 = midimain2.getMidiParser;
ppqn2 = midiparser2.getPPQN
bpm2 = midiparser2.getBPM
midiplayer2 = midimain2.getMidiPlayer;
% play it
midiplayer2.play