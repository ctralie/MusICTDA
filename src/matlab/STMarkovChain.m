classdef STMarkovChain < handle 
% STMARKOVCHAIN Short-time markov chain class

    properties(SetAccess = 'public', GetAccess = 'public')
        transitionMatrix;
    end

    methods(Access = 'public')

        function this = STMarkovChain(tmatrix)
            this.transitionMatrix = tmatrix;
        end

        % Finds feature vector for point cloud generation 
        function [ featureVector ] = getMarkovFeatures(this, index)
            featureVector = getMarkovFeatures(this.transitionMatrix(:,:,index));
        end

        % Finds next note given starting note 
        function nextNote = getNext(this, start, index)
            nextNote = getNext(this.transitionMatrix(:,:,index), start); 
        end
        
        % Finds sequence of notes given starting note 
        function noteSequence = getSequence(this, start, count, i)
            noteSequence = getSequence(this.transitionMatrix(:,:,i), start, count);
        end
        
        % return sequence of notes given starting note and increment time
        % in microseconds
        function noteSTSequence = getSTSequence(this, notesperinterval, start)
            noteSTSequence = getSTSequence(this.transitionMatrix, notesperinterval, start);
        end
        
    end
end

