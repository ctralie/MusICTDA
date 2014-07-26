classdef MarkovChain < handle 
% MARKOVCHAIN is class with all the same functions as in MarkovChainAll,
% plus getNext and getSequence, but for single octave of notes.
% transitionMatrix is matrix detailing transition probabilities from one
% note to another, including itself, within chosen octave. 

    properties(SetAccess = 'public', GetAccess = 'public')
        transitionMatrix;
    end

    methods(Access = 'public')

        function this = MarkovChain(tmatrix)
            this.transitionMatrix = tmatrix;
        end

        % Finds transition matrix after certain number of timesteps 
        function [ transMatrix, prob ] = matrixPowers(this, numSteps, initState, finState)
            [transMatrix, prob] = matrixPowers(this.transitionMatrix, numSteps, initState, finState);            
        end

        % Finds vector final probability distribution after number of timesteps
        function [ finalDistrib ] = probDistribution(this, numSteps, initVec)
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

        % Finds next note given starting note 
        function nextNote = getNext(this, start)
            nextNote = getNext(this.transitionMatrix, start); 
        end
        
        % Finds sequence of notes given starting note 
        function noteSequence = getSequence(this, start, count)
            noteSequence = getSequence(this.transitionMatrix, start, count)
        end
        
    end
end

