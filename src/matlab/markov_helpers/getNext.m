function nextNote = getNext( Trans, start )
%GETNEXT finds next note in sequence
%   nextNote = getNext(Trans,start) uses transition matrix Trans and
%   starting note start to pick nextNote, following note of highest
%   probability. start ranges from 0 to 11, with 0 being C. 

noteProb = Trans(start+1,:);
[~,nextNote] = max(noteProb); 

end
