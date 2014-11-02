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

        % Generate short-time feature vectors for entire song
        function [ featureMatrix ] = getMarkovFeatureMatrix(this)
            ysize = size(this.transitionMatrix, 1);
            xsize = size(this.transitionMatrix, 2);
            zsize = size(this.transitionMatrix, 3);
            featureMatrix = NaN(zsize, xsize*(ysize-1));
            for i=1:zsize
                featureMatrix(i,:) = this.getMarkovFeatures(i);
            end
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
            % convert to middle C
            noteSTSequence = noteSTSequence + 60;
            % "phase unwrapping"
            for i=1:length(noteSTSequence)-1
                curN = noteSTSequence(i);
                nextN = noteSTSequence(i+1);
                if abs((nextN+12)-curN) < abs(nextN-curN)
                    noteSTSequence(i+1:end) = noteSTSequence(i+1:end)+12;
                end
                if abs((nextN-12)-curN) < abs(nextN-curN)
                    noteSTSequence(i+1:end) = noteSTSequence(i+1:end)-12;
                end
            end
        end
        
    end
end

