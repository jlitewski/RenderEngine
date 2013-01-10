package com.hackhalo2.rendering;

import com.hackhalo2.rendering.interfaces.core.ILogger;

public class RenderLogger implements ILogger {
	
	protected RenderLogger() { }

	@Override
	public void printException(Throwable t) {
		
	}

	@Override
	public void err(String message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void err(String catagory, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(String message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void warn(String catagory, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(String message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void info(String catagory, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDebugModeEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void debug(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(String catagory, String message) {
		// TODO Auto-generated method stub
		
	}
	
}
