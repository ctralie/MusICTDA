function [ newMatrix, newNoteIndex ] = setTransitionMatrixRange( oldMatrix, noteIndex, min, max )
%SETTRANSITIONMATRIXRANGE Expands the transition matrix to cover a
%continuous range of values.
%   [ newMatrix, newNoteIndex ] = setTransitionMatrixRange( oldMatrix, noteIndex, min, max )
%   oldMatrix is the previous condensed transition matrix, with associated
%   note indices. min and max specify the new range for the transition
%   matrix to be in. 

newNoteIndex = min:max;
newmembers = ismember(newNoteIndex, noteIndex);
oldmembers = ismember(noteIndex, newNoteIndex);
newMatrix = sparse(length(newmembers), length(newmembers));
newMatrix(newmembers) = oldMatrix(oldmembers);

end
