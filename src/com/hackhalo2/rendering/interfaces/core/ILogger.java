package com.hackhalo2.rendering.interfaces.core;

public interface ILogger {
	//Exception API hook
	public void printException(Throwable t);
	public void exception(final String message, final int indent);
	
	//Error API hooks
	public void err(final String message, final int indent);
	public void err(final String category, final String message, final int indent);
	
	//Warning API hooks
	public void warn(final String message, final int indent);
	public void warn(final String category, final String message, final int indent);
	
	//Info API hooks
	public void info(final String message, final int indent);
	public void info(final String category, final String message, final int indent);
	
	//Debug API hooks
	public boolean isDebugModeEnabled(); //Mirrors RenderEngine._debug
	public void debug(final String message, final int indent);
	public void debug(final String category, final String message, final int indent);
}
