function [ fundTrans ] = fundMatrix( Trans ) 
%FUNDMATRIX converts transition matrix for absorbing Markov chains to 
%fundamental matrix 
%   [fundTrans] = fundMatrix(Trans) computes fundamental matrix fundTrans
%   for absorbing chains using canonical form of transition matrix Trans. 
%   Entries in fundTrans give expected number of times process is in
%   absorbing state if started in transient state, or the expected time 
%   required before absorption. 
 
[~,tranStates,~] = canonicalForm(Trans);
length = size(tranStates,1);
fundTrans = inv(speye(length) - sparse(tranStates)); 

end
