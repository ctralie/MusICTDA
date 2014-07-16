function [ limTrans ] = limitMatrix( Trans, n )
%[limTrans] = limitMatrix(Trans) computes the limiting matrix limTrans
%   that input transition matrix Trans approaches after large steps n. 

limTrans = Trans^n;

end
