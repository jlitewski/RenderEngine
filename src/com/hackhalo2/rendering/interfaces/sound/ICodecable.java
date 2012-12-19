package com.hackhalo2.rendering.interfaces.sound;

import com.paulscode.sound.ICodec;
import com.paulscode.sound.SoundSystemException;

public interface ICodecable {
	public void installCodec(String format, ICodec codec) throws SoundSystemException;
	public boolean hasCustomMIDICodec();
	
}
