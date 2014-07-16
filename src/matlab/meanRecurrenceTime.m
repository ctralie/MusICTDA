function [ meanRecVector, meanRecMatrix ] = meanRecurrenceTime( Trans, n ) 
%[meanRecVector] = meanRecurrenceTime(Trans,n) returns mean recurrence times
%   in vector meanRecVector and diagonalizes those entries in meanRecMatrix.

limVect = limitVector(Trans,n);
meanRecVector = 1./limVect; 
meanRecMatrix = diag(meanRecVector);

end
