package com.hackhalo2.rendering.interfaces.sound;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public interface IDimensionalSound {
	public boolean play2D(int soundID, Vector2f vector);
	public boolean play3D(int soundID, Vector3f vector);
}
