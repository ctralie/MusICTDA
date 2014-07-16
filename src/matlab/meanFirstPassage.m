function [ meanFPMatrix ] = meanFirstPassage ( Trans, n )
%[meanFPMatrix] = meanFirstPassage(Trans) returns the matrix listing the
% mean first passage times to go from row state to column state, excluding 
% diagonals: meanFPMatrix.

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
