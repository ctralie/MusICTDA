function [ meanRecVector, meanRecMatrix ] = meanRecurrenceTime( Trans, n ) 
%MEANRECURRENCETIME computes the mean time to return to a state for the
%first time for an ergodic chain 
%   [meanRecVector] = meanRecurrenceTime(Trans,n) gives mean recurrence times
%   in vector meanRecVector and diagonalizes those entries in meanRecMatrix.

limVect = limitVector(Trans,n);
meanRecVector = 1./limVect; 
meanRecMatrix = diag(meanRecVector);

end
