function [ limVect ] = limitVector( Trans ) 
%[limVect] = limitVector(Trans) computes the limiting vector from Trans.

limTrans = limitMatrix(Trans,n); 
limVect = limTrans(1,:); 

end
