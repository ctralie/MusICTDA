function [ canTrans, tranStates, transAbsorb ] = canonicalForm( Trans ) 
%[cTrans,tranStates,transAbsorb] = canonicalForm(Trans) turns transition matrix Trans 
%   with absorbing and transient states into canonical form canTrans and 
%   transient matrix tranStates and transAbsorb. 

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
