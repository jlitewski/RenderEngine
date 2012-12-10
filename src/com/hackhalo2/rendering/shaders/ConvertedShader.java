package com.hackhalo2.rendering.shaders;

import java.net.URI;

public class ConvertedShader implements ShaderObject {
	
	private CharSequence vertexShader;
	private CharSequence fragmentShader;

	protected ConvertedShader(URI vertex, URI fragment) {
		this.vertexShader = ShaderUtils.getTextFromURI(vertex);
		this.fragmentShader = ShaderUtils.getTextFromURI(fragment);
	}

	@Override
	public CharSequence vertex() {
		return this.vertexShader;
	}

	@Override
	public CharSequence fragment() {
		return this.fragmentShader;
	}

}
