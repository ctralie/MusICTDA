function [ absorbed ] = absorbSteps( Trans )
%ABSORBSTEPS computes expected number of steps before chain is absorbed
%   [absorbed] = absorbSteps(Trans) returns absorbed, a vector detailing
%   the expected number of steps, for each starting state, before a chain 
%   is absorbed.

fundTrans = fundMatrix(Trans);
length = size(fundTrans);
absorbed = fundTrans*ones(length,1); 

end
