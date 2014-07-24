function makeSynchronizedMCVideos(transitionMatrix, secPerSample, filename, outname, plot3D)

featureMatrix = generateSTFeatureMatrix(transitionMatrix);
[Y, eigs] = cmdscale(pdist(featureMatrix));

C = colormap;
N = size(Y, 1);
Colors = C( ceil( (1:N)*64/N ), :);

TotalSamples = N;
TotalSeconds = TotalSamples/secPerSample;
FramesPerSecond = N/TotalSeconds;

figure(1);
for ii = 1:N
    if plot3D == 1
        scatter3(Y(1:ii, 1), Y(1:ii, 2), Y(1:ii, 3), 20, Colors(1:ii, :));
    else
        scatter(Y(1:ii, 1), Y(1:ii, 2), 20, Colors(1:ii, :));
    end
    xlim([min(Y(:, 1)), max(Y(:, 1))]);
    ylim([min(Y(:, 2)), max(Y(:, 2))]);
    if plot3D == 1
        zlim([min(Y(:, 3)), max(Y(:, 3))]);
        view(mod(ii/2, 360), 0);
    end
    print('-dpng', '-r100', sprintf('syncmovie%i.png', ii));
end

[X, Fs] = audioread(filename);

audiowrite('syncmoviesound.wav', X, Fs);


PATH = getenv('PATH');
setenv('PATH', [PATH ':/usr/local/bin']);


system(sprintf('avconv -r %g -i syncmovie%s.png -i syncmoviesound.wav -b 65536k -r %24 %s', FramesPerSecond, '%d', FramesPerSecond, outname));
system('rm syncmovie*.png');

end