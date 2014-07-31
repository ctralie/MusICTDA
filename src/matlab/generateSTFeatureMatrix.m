function [ featureMatrix ] = generateSTFeatureMatrix( transitionMatrix )
%GENERATESTFEATUREMATRIX generate short-time feature vectors for one song.

ysize = size(transitionMatrix, 1);
xsize = size(transitionMatrix, 2);
zsize = size(transitionMatrix, 3);
featureMatrix = NaN(zsize, xsize*(ysize-1));

for i=1:zsize
    mc = MarkovChain(transitionMatrix(:,:,i));
    featureMatrix(i,:) = mc.getMarkovFeatures();
end

end
