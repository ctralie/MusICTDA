function [ fundTrans ] = fundMatrix( Trans ) 
%[fundTrans] = fundMatrix(Trans) computes the fundamental matrix fundTrans
%   for absorbing chains from the canonical form of a transition matrix Trans. 
%   Entries in fundTrans give expected number of times from row state to 
%   column state until absorption. 
 
[~,tranStates,~] = canonicalForm(Trans);
length = size(tranStates,1);
fundTrans = inv(speye(length) - sparse(tranStates)); 

end
