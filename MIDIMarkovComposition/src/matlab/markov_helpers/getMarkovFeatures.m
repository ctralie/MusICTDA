function [ featureVector ] = getMarkovFeatures( transitionMatrix )
%GETMARKOVFEATURES Obtain feature vector from Markov Chain transition
%matrix.
%   [ featureVector ] = getMarkovFeatures( transitionMatrix ) Convert
%   transition matrix to feature vector for point cloud.

diagmat = speye(size(transitionMatrix));
diagremoved = transitionMatrix(~diagmat);
featureVector = diagremoved(:);

end
