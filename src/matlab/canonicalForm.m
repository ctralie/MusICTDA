function [ canTrans, tranStates, transAbsorb ] = canonicalForm( Trans ) 
%CANONICALFORM converts transition matrix to canonical form
%   [canTrans,tranStates,transAbsorb] = canonicalForm(Trans) turns 
%   transition matrix Trans into canonical form canTrans, also returning 
%   matrix of only transient states tranStates and matrix of transient
%   states going to absorbing states transAbsorb.  

Trans = sparse(Trans);

diagM = logical(eye(size(Trans)));
Diag = Trans(diagM) ~= 1;
oneDiag = Trans(diagM) == 1;
tranStates = Trans(:,Diag);
tranStates = tranStates(~oneDiag,:);

transAbsorb = Trans(:,oneDiag);
transAbsorb = transAbsorb(~oneDiag,:);

zero = sparse(size(transAbsorb,2),size(tranStates,1));
eyeM = speye(size(transAbsorb,2));

canTrans = [tranStates,transAbsorb;zero,eyeM];
