function [ final ] = probDistribution( Trans, n, initial )
%PROBDISTRIBUTION computes final distribution vector given initial vector
%   [final] = probDistribution(Trans,n,initial) returns ending distribution
%   vector, final, using transition matrix Trans, n steps, and row
%   probability vector of starting distribution, initial. 

Trans = sparse(Trans); 
nTrans = matrixPowers(Trans,n); 
final = initial*nTrans; 

end
