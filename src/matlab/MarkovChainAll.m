classdef MarkovChainAll < handle  
% MARKOVCHAINALL is class with series of helper function for an entire range
% of notes. Uses transitionMatrix of note transition probabilities and noteIndex
% mapping location in matrix to note values. 

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
                % Expands transition matrix to cover continuous range of
                % values from min to max 
                min = varargin{3};
                max = varargin{4};                
                [ this.transitionMatrix, this.noteIndex ] = setTransitionMatrixRange(tmatrix, nindex, min, max);
            end
        end

        % Finds transition matrix after certain number of timesteps 
        function [ transMatrix, prob ] = matrixPowers(this, numSteps, initState, finState)
            [transMatrix, prob] = matrixPowers(this.transitionMatrix, numSteps, initState, finState);            
        end

        % Finds vector final probability distribution after number of timesteps
        function [ finalDistrib ] = finalProbDistrib(this, numSteps, initVec)
            finalDistrib = probDistribution(this.transitionMatrix, numSteps, initVec);
        end

        % Finds limiting matrix for regular, ergodic chain 
        function [ limVec, limMatrix ] = limiting(this, numSteps)
            [limVec, limMatrix] = limiting(this.transitionMatrix, numSteps);
        end

         % Finds canonical form for transition matrix 
        function [ cTrans, tranStates, transAbsorb ] = canonicalForm(this)
            [cTrans, tranStates, transAbsorb] = canonicalForm(this.transitionMatrix);
        end

        % Finds fundamental matrix for absorbing chain 
        function [ fundTrans ] = fundMatrix(this)
            fundTrans = fundMatrix(this.transitionMatrix);
        end

        % Finds fundamental matrix for regular, ergodic chain 
        function [ fundTransErg ] = fundMatrixErg(this)
            fundTransErg = fundMatrixErg(this.transitionMatrix);
        end

        % Finds matrix of absorption probabilities for absorbing chain 
        function [ absorbMatrix ] = absorbProb(this)
            absorbMatrix = absorbProb(this.transitionMatrix);          
        end

        % Finds expected number of steps before chain is absorbed
        function [ absorbed ] = absorbSteps(this)
            absorbed = absorbSteps(this.transitionMatrix); 
        end

        % Finds number steps to first reach ending state for regular,
        % ergodic chain. [NOTE: still funky] 
        function [ meanFPMatrix ] = meanFirstPassage(this)
            meanFPMatrix = meanFirstPassage(this.transitionMatrix, numSteps);
        end

        % Finds avg time to first return to a state for regular, ergodic
        % chain
        function [ meanRecVector, meanRecMatrix ] = meanRecurrenceTime(this, numSteps)
            [meanRecVector, meanRecMatrix] = meanRecurrenceTime(this.transitionMatrix, numSteps);
        end

        % Finds feature vector for point cloud generation 
        function [ featureVector ] = getMarkovFeatures(this)
            featureVector = getMarkovFeatures(this.transitionMatrix);
        end

    end
end


