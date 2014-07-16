function [ absorbed ] = absorbSteps( Trans )
%[absorbed] = absorbSteps(Trans) calculates the expected number of steps
%   before a chain is absorbed, given that the chain starts in state i. 

fundTrans = fundMatrix(Trans);
length = size(fundTrans);
absorbed = fundTrans*ones(length,1); 

end
