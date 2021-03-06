function [ fundTransErg ] = fundMatrixErg( Trans, n )
%FUNDMATRIXERG converts transition matrix for regular, ergodic Markov chains 
%to fundamental matrix 
%   [fundTransErg] = fundMatrixErg(Trans) computes fundamental matrix
%   fundTransErg of ergodic, regular chain from transition matrix Trans. 

[~,limMatrix] = limiting(Trans,n);
fundTransErg = inv(eye(size(Trans)) - Trans + limMatrix);

end
