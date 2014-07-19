function [ Y, eigs ] = pcafeatures( featureMatrix )
%PCAFEATURES

[Y, eigs] = cmdscale(pdist(featureMatrix));

end
