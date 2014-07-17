function [ limVec, limMatrix ] = limiting( Trans, n )
%LIMITING computes limiting matrix the transition matrix approaches for
%regular, ergodic Markov chain
%   [limVect,limMatrix] = limiting(Trans,n) returns limiting matrix limTrans
%   and a row limVec that regular transition matrix Trans tends to after n large 
%   steps. 

limMatrix = sparse(Trans^n);
limVec = limMatrix(1,:); 

end
