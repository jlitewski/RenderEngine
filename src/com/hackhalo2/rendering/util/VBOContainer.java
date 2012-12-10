package com.hackhalo2.rendering.util;

import java.nio.FloatBuffer;

public class VBOContainer {
	
	//Initialize some of the variables to prevent !!FUN!!
	private int handle;
	private ContainerType type;
	private int size = 0;
	private FloatBuffer normalBuffer = null;
	private int[] stride;
	
	//The Vertex and Color constructor
	public VBOContainer(ContainerType type, int handle, int size, int stride) {
		this.type = type;
		this.handle = handle;
		this.size = size;
		this.stride = new int[1]; 
		this.stride[0] = stride;
	}
	
	//The Normal constructor
	public VBOContainer(ContainerType type, int handle, FloatBuffer normalBuffer, int stride) {
		this.type = type;
		this.handle = handle;
		this.normalBuffer = normalBuffer;
		this.stride = new int[1]; 
		this.stride[0] = stride;
	}
	
	public ContainerType getType() {
		return this.type;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getHandle() {
		return this.handle;
	}
	
	public int getStride(int offset) {
		return this.stride[offset];
	}
	
	public FloatBuffer getPointer() {
		return this.normalBuffer;
	}
	
	public enum ContainerType {
		VERTEX,
		COLOR,
		NORMAL,
		CLUMPED
	}

}
