package com.hackhalo2.rendering;

import com.hackhalo2.rendering.interfaces.core.ILogger;

public class RenderLogger implements ILogger {
	//TODO: use a Logger instead of sysout and syserr
	
	public RenderLogger() { }

	@Override
	public void printException(Throwable t) {
		if(t == null) return; //If the Throwable is null, ignore it
		int indent = 0;
		
		//Print exceptions only if DebugMode is on or if the Throwable is an instance of a RuntimeException
		if(this.isDebugModeEnabled() || (t instanceof RuntimeException)) {
			this.exception(t.getMessage(), indent);
			System.err.println(this.indentString("Stack Trace:", indent));
			indent++;
			
			StackTraceElement[] stack = t.getStackTrace();
			if(stack == null || stack.length == 0) {
				//Return if there were no StackTraceElements  (or if it was null)
				System.err.println(this.indentString("(No StackTraceElements Available)", indent));
				return;
			}
			indent++;
			
			for(StackTraceElement line : stack) {
				if(line != null) System.err.println(this.indentString(line.toString(), indent));
			}
		}
	}
	
	@Override
	public void exception(String message, final int indent) {
		String msg = "[EXCEPTION] ";
		if(message == null) msg += "(none)";
		else msg += message;
		
		System.err.println(this.indentString(msg, indent));
	}
	
	@Override
	public void err(String message, final int indent) {
		this.err(null, message, indent);
	}
	
	@Override
	public void err(String category, String message, final int indent) {
		String header = this.buildHeader("ERROR", category);
		
		System.err.println(this.indentString(header+message, indent));
	}

	@Override
	public void warn(String message, final int indent) {
		this.warn(null, message, indent);
	}
	
	@Override
	public void warn(String category, String message, final int indent) {
		String header = this.buildHeader("WARN", category);
		
		System.out.println(this.indentString(header+message, indent));
	}

	@Override
	public void info(String message, final int indent) {
		this.info(null, message, indent);
	}
	
	@Override
	public void info(String category, String message, final int indent) {
		String header = this.buildHeader("info", category);
		
		System.out.println(this.indentString(header+message, indent));
	}

	/**
	 * Returns if the RenderEngine's debug mode is enabled.<br />
	 * If debug mode is <b>true</b>, Exceptions and debug messages will be printed to the console,<br />
	 * else they will be suppressed. Info, Warning, and Error messages will still print with debug mode on.<br />
	 * 
	 */
	@Override
	public boolean isDebugModeEnabled() {
		return RenderEngine._debug;
	}

	@Override
	public void debug(String message, final int indent) {
		this.debug(null, message, indent);
	}

	@Override
	public void debug(String category, String message, final int indent) {
		if(this.isDebugModeEnabled()) {
			String header = this.buildHeader("debug", category);
			
			System.out.println(this.indentString(header+message, indent));
		}
	}
	
	/**
	 * Indents a String, similar to Paul's
	 * 
	 * @param message The message to indent
	 * @param indent the amount to indent
	 * @return the indented message
	 */
	private String indentString(final String message, final int indent) {
		String spacer = "";
		for(int i = 0; i < indent; i++) {
			spacer += "  ";
		}
		
		return new String(spacer+message);
	}
	
	/**
	 * Builds the header to the Message String
	 * 
	 * @param header The header to use
	 * @param category the optional category to use (use <b>null</b> for none)
	 * @return the Built header
	 */
	private String buildHeader(final String header, final String category) {
		String temp = "["+header;
		if(category != null) temp += "-"+category+"] ";
		else temp += "] ";
		
		return temp;
	}
	
}
