function [ final ] = probDistribution( Trans, n, initial )
%[final] = probDistribution(Trans,n,initial) takes in transition matrix Trans, 
%   n steps, and row probability vector, initial, of starting distribution and
%   returns final, ending distribution vector. To examine behavior of chain 
%   assuming it starts in state i, set ith entry of initial to 1 and all
%   other entries equal to 0. 

Trans = sparse(Trans); 
nTrans = matrixPowers(Trans,n); 
final = initial*nTrans; 

end
