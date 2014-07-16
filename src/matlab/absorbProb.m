function [ absorbMatrix ] = absorbProb( Trans )
%[absorbMatrix] = absorbProb(Trans) calculates the matrix of absorption 
%   probabilities absorbMatrix going from row to column state. 

[~,~,transAbsorb] = canonicalForm(Trans);
fundTrans = fundMatrix(Trans);
absorbMatrix = fundTrans*transAbsorb; 

end
