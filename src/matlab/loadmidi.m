function [ midimain ] = loadmidi( path )

midimain = javaObjectEDT('MidiMain', path);

end
