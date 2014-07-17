function [ limVec, limMatrix ] = limiting( Trans, n )
%LIMITING computes limiting matrix the transition matrix approaches
%   [limVect,limMatrix] = limiting(Trans) returns limiting matrix limTrans
%   and a row limVec that transition matrix Trans tends to after n large 
%   steps. 

limMatrix = sparse(Trans^n);
limVec = limMatrix(1,:); 

end

