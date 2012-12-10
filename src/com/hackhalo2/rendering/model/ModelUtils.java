package com.hackhalo2.rendering.model;

import org.lwjgl.util.vector.Vector3f;

public class ModelUtils {

	private ModelUtils() { }
	
	public static float[] asFloats(Vector3f v) {
		return new float[]{v.x, v.y, v.z};
	}

}
