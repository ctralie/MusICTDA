function nextNote = getNext( Trans, start )
%GETNEXT finds next note in sequence
%   nextNote = getNext(Trans,start) uses transition matrix Trans and
%   start index (WITH ZERO INDEXING) to find index of nextNote.

% convert to one indexing
start = start + 1;
% get note transition probabilities
noteProb = Trans(start, :);
if sum(noteProb)==0
    noteProb = ones(size(noteProb));
end
% convert to note transition counts
nozeros = noteProb(noteProb~=0);
noteCount = round(noteProb/min(nozeros));
% create array of notes weighted by frequency
noteFreq = [];
for i=1:length(noteCount)
    noteFreq = [ noteFreq, ones(1,noteCount(i))*i ];
end
% randomly pick from noteFreq
nextNote = noteFreq(randi(length(noteFreq))) - 1;

end
