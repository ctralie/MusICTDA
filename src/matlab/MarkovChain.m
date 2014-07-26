classdef MarkovChain < handle  %for one octave of notes 
    properties(SetAccess = 'public', GetAccess = 'public')
        transitionMatrix;
    end

    methods(Access = 'public')

        function this = MarkovChain(tmatrix)
            this.transitionMatrix = tmatrix;
        end

        function [ transMatrix, prob ] = matrixPowers(this, numSteps, initState, finState)
            [transMatrix, prob] = matrixPowers(this.transitionMatrix, numSteps, initState, finState);            
        end

        function [ finalDistrib ] = probDistribution(this, numSteps, initVec)
            finalDistrib = probDistribution(this.transitionMatrix, numSteps, initVec);
        end

        function [ limVec, limMatrix ] = limiting(this, numSteps)
            [limVec, limMatrix] = limiting(this.transitionMatrix, numSteps);
        end

        function [ cTrans, tranStates, transAbsorb ] = canonicalForm(this)
            [cTrans, tranStates, transAbsorb] = canonicalForm(this.transitionMatrix);
        end

        function [ fundTrans ] = fundMatrix(this)
            fundTrans = fundMatrix(this.transitionMatrix);
        end

        function [ fundTransErg ] = fundMatrixErg(this)
            fundTransErg = fundMatrixErg(this.transitionMatrix);
        end

        function [ absorbMatrix ] = absorbProb(this)
            absorbMatrix = absorbProb(this.transitionMatrix);          
        end

        function [ absorbed ] = absorbSteps(this)
            absorbed = absorbSteps(this.transitionMatrix); 
        end

        function [ meanFPMatrix ] = meanFirstPassage(this)
            meanFPMatrix = meanFirstPassage(this.transitionMatrix, numSteps);
        end

        function [ meanRecVector, meanRecMatrix ] = meanRecurrenceTime(this, numSteps)
            [meanRecVector, meanRecMatrix] = meanRecurrenceTime(this.transitionMatrix, numSteps);
        end

        function [ featureVector ] = getMarkovFeatures(this)
            featureVector = getMarkovFeatures(this.transitionMatrix);
        end

        function nextNote = getNext(this, start)
            nextNote = getNext(this.transitionMatrix, start); 
        end
        
        function noteSequence = getSequence(this, start, count)
            noteSequence = getSequence(this.transitionMatrix, start, count)
        end
        
    end
end
