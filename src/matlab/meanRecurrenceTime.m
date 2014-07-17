function [ meanRecVector, meanRecMatrix ] = meanRecurrenceTime( Trans, n ) 
%MEANRECURRENCETIME computes the mean time to return to a state for the
%first time for an ergodic chain 
%   [meanRecVector] = meanRecurrenceTime(Trans,n) gives mean recurrence times
%   in vector meanRecVector and diagonalizes those entries in meanRecMatrix.

[limVec,~] = limiting(Trans,n);
meanRecVector = 1./limVec; 
meanRecMatrix = diag(meanRecVector);

end
