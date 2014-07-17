function [ meanFPMatrix ] = meanFirstPassage ( Trans, n )
%MEANFIRSTPASSAGE computes expected number of steps to reach ending state
%for first time for ergodic Markov chain 
%   [meanFPMatrix] = meanFirstPassage(Trans) returns meanFPMatrix, the 
%   matrix of mean times to go from every starting state to every ending 
%   state, assuming passage time is trivial if start/end states are the same. 

[~,meanRecMatrix] = meanRecurrenceTime(Trans);
fundTransErg = fundMatrixErg(Trans);
limTrans = limitMatrix(Trans,n);

difMatrix = ones(size(meanRecMatrix)) - fundTransErg*meanRecMatrix;
dif2Matrix = eye(size(limTrans)) - limTrans;  
meanFPMatrix = difMatrix*inv(dif2Matrix);

end

% M = C - ZD + WM 
% M(I-W) = C - ZD
% M = (C - ZD)*(I - W)^-1
% Z = fundmatrix, I = identity, W = lim matrix, C =
% ones(size(meanRecMatrix)), D = meanRecMatrix
%{
[~,meanRecMatrix] = meanRecurrenceTime(Trans);
difMatrix = ones(size(meanRecMatrix)) - meanRecMatrix;
Matrix = eye(size(Trans)) - Trans;
meanFPMatrix = Matrix\difMatrix;
%}
