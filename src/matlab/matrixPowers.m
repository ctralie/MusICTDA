function [ nTrans, prob ] = matrixPowers( Trans, n, i, j )
%[nTrans,prob] = matrixPowers(Trans,n,i,j) takes transition matrix Trans and 
%   computes nTrans after n steps. Optional input args i, j range from 
%   1:size(:,nTrans) and return probability Markov chain goes from
%   state i to j after n steps.  

Trans = sparse(Trans); 
nTrans = Trans^n;

if nargin < 3 
prob = 0; 
else
prob = nTrans(i,j); 
end

end
