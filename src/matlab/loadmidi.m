function [ midiaudio ] = loadmidi( path )

midiaudio = javaObjectEDT('MidiAudio', path);

end
