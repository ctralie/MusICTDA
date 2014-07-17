function [ canTrans, tranStates, transAbsorb ] = canonicalForm( Trans ) 
%CANONICALFORM converts transition matrix to canonical form
%   [canTrans,tranStates,transAbsorb] = canonicalForm(Trans) turns 
%   transition matrix Trans into canonical form canTrans, also returning 
%   matrix of only transient states tranStates and matrix of transient
%   states going to absorbing states transAbsorb.  

length = size(Trans,2);
Trans = sparse(Trans);

diagM = logical(eye(size(Trans)));
zeroDiag = Trans(diagM) == 0;
oneDiag = Trans(diagM) == 1;
tranStates = Trans(:,zeroDiag);
tranStates = tranStates(~oneDiag,:);

transAbsorb = Trans(:,oneDiag);
transAbsorb = transAbsorb(~oneDiag,:);

% transient = length - size(transAbsorb,2);

zero = sparse(size(transAbsorb,2),size(tranStates,1));
eyeM = speye(size(transAbsorb,2)); 

canTrans = [tranStates,transAbsorb;zero,eyeM];
end
