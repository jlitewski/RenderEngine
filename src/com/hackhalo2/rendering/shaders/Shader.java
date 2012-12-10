package com.hackhalo2.rendering.shaders;

import org.lwjgl.opengl.GL20;

public class Shader {
	private int program;

	protected Shader(int program) {
		this.program = program;
	}
	
	public void start() {
		GL20.glUseProgram(this.program);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void setAttributes(String... attributes) {
		
	}

}
