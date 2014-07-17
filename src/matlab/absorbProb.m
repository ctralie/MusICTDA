function [ absorbMatrix ] = absorbProb( Trans )
%ABSORBPROB computes matrix of absorption proabilities for absorbing chain
%   [absorbMatrix] = absorbProb(Trans) calculates the matrix of absorption 
%   probabilities, absorbMatrix, of going from each transient state to each
%   absorbing state using transition matrix Trans. 

[~,~,transAbsorb] = canonicalForm(Trans);
absorbMatrix = fundMatrix(Trans)*transAbsorb; 

end
