package com.hackhalo2.rendering.interfaces.core;

public interface ILogger {
	//Exception API hook
	public void printException(Throwable t);
	
	//Error API hooks
	public void err(final String message);
	public void err(final String catagory, final String message);
	
	//Warning API hooks
	public void warn(final String message);
	public void warn(final String catagory, final String message);
	
	//Info API hooks
	public void info(final String message);
	public void info(final String catagory, final String message);
	
	//Debug API hooks
	public boolean isDebugModeEnabled(); //Mirrors RenderEngine._debug
	public void debug(final String message);
	public void debug(final String catagory, final String message);
}
