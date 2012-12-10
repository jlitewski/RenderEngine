package com.hackhalo2.rendering.exceptions;

public class GLException extends RuntimeException {

	private static final long serialVersionUID = 8116075270416838128L;
	private String message = "";

	public GLException() {
		super();
		this.message = "GLERR: Unknown OpenGL Exception";
	}
	
	public GLException(String message){
		super(message);
		this.message = "GLERR: "+message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

}
