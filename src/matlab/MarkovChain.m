classdef MarkovChain < handle
    properties(SetAccess = 'public', GetAccess = 'public')
        transitionMatrix;
        noteIndex;
    end
    
    methods(Access = 'public')
        function this = MarkovChain(transitionMatrix, noteIndex)
            this.transitionMatrix = transitionMatrix;
            this.noteIndex = noteIndex;
        end
        
        function [transMatrix, prob] = nMatrix(this, numSteps, initState, finState)
            [transMatrix, prob] = matrixPowers(this.transitionMatrix, numSteps, initState, finState);            
        end
        
        function finalDistrib = finalProbDistrib(this, numSteps, initVec)
            finalDistrib = probDistribution(this.transitionMatrix, numSteps, initVec);
        end
        
        function [limVec, limMatrix] = limitingMatrix(this, numSteps)
            [limVec, limMatrix] = limiting(this.transitionMatrix, numSteps);
        end
        
        function [cTrans,tranStates,transAbsorb] = convert2canonical(this)
            [cTrans,tranStates,transAbsorb] = canonicalForm(this.transitionMatrix);
        end
        
        function fundTrans = fund(this)
            fundTrans = fundMatrix(this.transitionMatrix);
        end
        
        function fundTransErg = fundErg(this)
            fundTransErg = fundMatrixErg(this.transitionMatrix);
        end
        
        function absorbMatrix = absorb(this)
            absorbMatrix = absorbProb(this.transitionMatrix);          
        end
        
        function absorbed = absorbNumSteps(this)
            absorbed = absorbSteps(this.transitionMatrix); 
        end
        
        function meanFPMatrix = meanFP(this)
            meanFPMatrix = meanFirstPassage(this.transitionMatrix, numSteps);
        end
        
        function [meanRecVector, meanRecMatrix] = meanRT(this, numSteps)
            [meanRecVector, meanRecMatrix] = meanRecurrenceTime(this.transitionMatrix, numSteps);
        end
        
    end
end
