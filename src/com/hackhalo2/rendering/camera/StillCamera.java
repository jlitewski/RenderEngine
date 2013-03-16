package com.hackhalo2.rendering.camera;

import org.lwjgl.util.vector.Vector3f;

import com.hackhalo2.rendering.KeyboardBuffer;

/**
 * The most basic non-movable camera the RenderEngine can implement
 * 
 * @author Jacob "HACKhalo2" Litewski
 */
public class StillCamera extends Camera {

	public StillCamera(KeyboardBuffer kb) {
		super(kb);
	}

	@Override
	public String getName() {
		return "Still";
	}

	@Override
	public void setRotation(Vector3f rotation) { }

	@Override
	public Vector3f getRotation() {
		return new Vector3f(0,0,0);
	}

	@Override
	public void generateMatrices(int delta) { }

	@Override
	public void applyMatrices() { }

}
