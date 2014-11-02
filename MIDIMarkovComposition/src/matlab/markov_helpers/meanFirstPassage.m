function [ meanFPMatrix ] = meanFirstPassage ( Trans, n )
%MEANFIRSTPASSAGE computes expected number of steps to reach ending state
%for first time for regular, ergodic Markov chain 
%   [meanFPMatrix] = meanFirstPassage(Trans,n) returns meanFPMatrix, the 
%   matrix of mean times to go from every starting state to every ending 
%   state, assuming passage time is trivial if start/end states are the same. 

%transition matrix for regular chain  
[~,meanRecMatrix] = meanRecurrenceTime(Trans,n);
fundTransErg = fundMatrixErg(Trans,n);
[~,limMatrix] = limiting(Trans,n);

difMatrix = ones(size(meanRecMatrix)) - fundTransErg*meanRecMatrix;
dif2Matrix = eye(size(full(limMatrix))) - full(limMatrix);  
meanFPMatrix = dif2Matrix\difMatrix;



% M = C - ZD + WM 
% (I-W)M = C - ZD
% M = (I-W)\(C-ZD) 
% Z = fundmatrix, I = identity, W = lim matrix, C =
% ones(size(meanRecMatrix)), D = meanRecMatrix
% (I-P)M = C - D
%{
[~,meanRecMatrix] = meanRecurrenceTime(Trans,n);
difMatrix = ones(size(meanRecMatrix)) - meanRecMatrix;
[canTrans,~,~] = canonicalForm(Trans); %prob w canonicalForm
Matrix = eye(size(canTrans)) - canTrans;
meanFPMatrix = Matrix\difMatrix;
%}
end
