function [ nTrans, prob ] = matrixPowers( Trans, n, i, j )
%MATRIXPOWERS computes transition matrix after n steps 
%   [nTrans,prob] = matrixPowers(Trans,n,i,j) computes matrix nTrans of
%   transition matrix Trans after n steps. Optional args starting state i,
%   ending state j return prob, probability Markov chain goes from state i 
%   to j after n steps.  

Trans = sparse(Trans); 
nTrans = Trans^n;

if nargin < 3 
prob = 0; 
else
prob = nTrans(i,j); 
end

end
