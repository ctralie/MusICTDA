function [ noteSequence ] = getSTSequence( STMatrix, notesperinterval, start )
%GETSTSEQUENCE create sequence of notes from short-time markov chain
%   [ noteSequence ] = getSTSequence( STMatrix, start, notesperinterval )
%   noteSequence is array of notes, STMatrix is short-time markov chain
%   transition matrix, start is the start note, notesperinterval is the #
%   of notes per each transition matrix in STMatrix.

noteSequence = NaN(1, notesperinterval*size(STMatrix,3));
noteSequence(1) = start;
for i=1:size(STMatrix, 3)
    curMatrix = STMatrix(:,:,i);
    for j=1:notesperinterval
        start = getNext(curMatrix, start);
        noteSequence((i-1)*notesperinterval + j) = start;
    end
end

end

