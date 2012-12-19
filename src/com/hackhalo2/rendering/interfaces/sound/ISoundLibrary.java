package com.hackhalo2.rendering.interfaces.sound;

import com.paulscode.sound.Library;

public interface ISoundLibrary<L extends Library> {
	public L getSoundLibrary();
}
