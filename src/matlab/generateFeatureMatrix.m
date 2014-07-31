function [ featureMatrix, noteIndex ] = generateFeatureMatrix( rootpath, filter )

x = dir([rootpath, '/',filter]);

% iterate through each piece, get count of notes
notecount = zeros(1, 128);
MidiMain = cell(numel(x), 1);
for i=1:numel(x)
    path = [rootpath, x(i).name];
    MidiMain(i) = loadmidi(path);
    midimarkov = MidiMain{i}.getMidi2MarkovTranscribed;
    notecount = notecount + double(midimarkov.getNoteCount)';
end

% limit range to note range of piece
notenum = 0:127;
notenum(notecount==0) = [];
minrange = min(notenum);
maxrange = max(notenum);
noteIndex = minrange:maxrange;

featureMatrix = NaN(numel(x), length(noteIndex)*(length(noteIndex)-1));
% iterate through each piece, compute featureVectors
for i=1:length(MidiMain)
   midimarkov = MidiMain{i}.getMidi2MarkovTranscribed;
   noteMatrix = sparse(midimarkov.getNoteTransitionMatrix(1));
   noteIndex = midimarkov.getNoteIndex;
   mc = MusicMarkovChain(noteMatrix, noteIndex, minrange, maxrange);
   featureMatrix(i,:) = mc.getMarkovFeatures;
end

end
