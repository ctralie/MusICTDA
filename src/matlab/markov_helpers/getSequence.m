function noteSequence = getSequence( Trans, start, count )
%GETSEQUENCE returns array of notes in sequence
%   noteSequence = getSequence(Trans,start,count) takes transition matrix
%   Trans, starting note start, and entire sequence's note duration count 
%   to determine sequence of notes noteSequence. start ranges from 0 to 11, 
%   with 0 being C.

noteSequence = NaN(1,count); 
noteSequence(1,1) = start; 

for i = 2:count
    nextNote = getNext(Trans, start);
    noteSequence(1,i) = nextNote; 
    start = nextNote;
end

end
