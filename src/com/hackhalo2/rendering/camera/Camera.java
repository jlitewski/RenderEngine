package com.hackhalo2.rendering.camera;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.hackhalo2.rendering.KeyboardBuffer;
import com.hackhalo2.rendering.interfaces.ICamera;

public abstract class Camera implements ICamera {
	
	protected Vector3f position = new Vector3f(0,0,0);
	protected KeyboardBuffer kb = null;
	protected float zNear = 0.3f;
	protected float zFar = 100f;
	protected float aspectRatio = ((float)Display.getWidth()/Display.getHeight());
	protected float fov = 45f;

	protected Camera(KeyboardBuffer kb) {
		this.kb = kb;
	}
	
	@Override
	public abstract String getName();

	@Override
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	@Override
	public Vector3f getPosition() {
		return this.position;
	}

	@Override
	public abstract void setRotation(Vector3f rotation);

	@Override
	public abstract Vector3f getRotation();
	
	@Override
	public float getZNear() {
		return this.zNear;
	}

	@Override
	public float getZFar() {
		return this.zFar;
	}
	
	@Override
	public float getAspectRatio() {
		return this.aspectRatio;
	}
	
	@Override
	public float getFOV() {
		return this.fov;
	}

	@Override
	public void setX(float x) {
		this.position.setX(x);
	}

	@Override
	public void setY(float y) {
		this.position.setY(y);
	}

	@Override
	public void setZ(float z) {
		this.position.setZ(z);
	}

	@Override
	public float x() {
		return this.position.x;
	}

	@Override
	public float y() {
		return this.position.y;
	}

	@Override
	public float z() {
		return this.position.z;
	}
	
	protected void registerKBInputs(int... inputs) {
		for(int i = 0; i < inputs.length; i++) {
			this.kb.registerKey(inputs[i]);
		}
	}

	@Override
	public abstract void generateMatrices();

	@Override
	public abstract void applyMatrices();

}
