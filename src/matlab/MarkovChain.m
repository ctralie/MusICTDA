classdef MarkovChain < handle
    properties(SetAccess = 'public', GetAccess = 'public')
        Trans;
        numSteps;
        rowState;
        colState;
        initVec; 
    end
    
    methods(Access = 'public')
        function this = MarkovChain(trans, n, i, j, initial)
            this.Trans = trans;
            this.numSteps = n;
            this.rowState = i;
            this.colState = j;
            this.initVec = initial;
        end
        
        function [transMatrix, prob] = nMatrix(this)
            [transMatrix, prob] = matrixPowers(this.Trans, this.numSteps, this.rowState, this.colState);            
        end   
        
        function finalDistrib = finalProbDistrib(this)
            finalDistrib = probDistribution(this.Trans, this.numSteps, this.initVec);
        end
        
        function [limVec, limMatrix] = limitingMatrix(this)
            [limVec, limMatrix] = limiting(this.Trans, this.numSteps);
        end
        
        function [cTrans,tranStates,transAbsorb] = convert2canonical(this)
            [cTrans,tranStates,transAbsorb] = canonicalForm(this.Trans);
        end
        
        function fundTrans = fund(this)
            fundTrans = fundMatrix(this.Trans);
        end
        
        function fundTransErg = fundErg(this)
            fundTransErg = fundMatrixErg(this.Trans);
        end
        
        function absorbMatrix = absorb(this)
            absorbMatrix = absorbProb(this.Trans);
        end
        
        function meanFPMatrix = meanFP(this)
            meanFPMatrix = meanFirstPassage(this.Trans, this.numSteps);
        end
        
        function [meanRecVector, meanRecMatrix] = meanRT(this)
            [meanRecVector, meanRecMatrix] = meanRecurrenceTime(this.Trans, this.numSteps);
        end
        
    end
end
