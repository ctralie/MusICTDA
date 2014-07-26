classdef MarkovChainAll < handle   %for entire range of notes 
    properties(SetAccess = 'public', GetAccess = 'public')
        transitionMatrix;
        noteIndex;
    end

    methods(Access = 'public')

        function this = MarkovChain(varargin)
            narginchk(2,4);
            tmatrix = varargin{1};
            nindex = varargin{2};
            if nargin == 2
                this.transitionMatrix = tmatrix;
                this.noteIndex = nindex;
            elseif nargin == 4
                min = varargin{3};
                max = varargin{4};
                [ this.transitionMatrix, this.noteIndex ] = setTransitionMatrixRange(tmatrix, nindex, min, max);
            end
        end

        function [ transMatrix, prob ] = matrixPowers(this, numSteps, initState, finState)
            [transMatrix, prob] = matrixPowers(this.transitionMatrix, numSteps, initState, finState);            
        end

        function [ finalDistrib ] = finalProbDistrib(this, numSteps, initVec)
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

    end
end


