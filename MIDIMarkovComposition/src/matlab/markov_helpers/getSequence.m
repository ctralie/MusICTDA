function [ note_sequence ] = getSequence( tMatrix, start, count )
%GETSEQUENCE returns array of notes in sequence
%   [ note_sequence ] = getSequence( tMatrix, start, count ) takes
%   transition matrix tMatrix, starting note start, and entire sequence's
%   note duration count to determine sequence of notes note_sequence.

note_sequence = NaN(1, count);
note_sequence(1,1) = start;

for i = 2:count
    start = getNext(tMatrix, start);
    note_sequence(1,i) = start;
end

end
