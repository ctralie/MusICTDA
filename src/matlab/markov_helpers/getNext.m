function nextNote = getNext( Trans, start )
%GETNEXT finds next note in sequence
%   nextNote = getNext(Trans,start) uses transition matrix Trans and
%   starting note start to pick nextNote, following note of highest
%   probability. start ranges from 0 to 11, with 0 being C. 

start = start + 1; 
noteProb = Trans(start,:);
[~,nextNote] = max(noteProb); 
nextNote = nextNote - 1; 

end
