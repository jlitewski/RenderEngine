package com.hackhalo2.rendering.interfaces.camera;

import org.lwjgl.util.vector.Vector3f;

public interface ICamera {
	public String getName();
	
	public float getZNear();
	public float getZFar();
	
	public float getAspectRatio();
	public float getFOV();
	
	public void generateMatrices(final int delta);
	public void applyMatrices();
	
	public void setPosition(Vector3f position);
	public Vector3f getPosition();
	
	public void setRotation(Vector3f rotation);
	public Vector3f getRotation();
	
	public void setX(float x);
	public void setY(float y);
	public void setZ(float z);
	
	public float x();
	public float y();
	public float z();
}
