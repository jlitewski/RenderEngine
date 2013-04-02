package com.hackhalo2.render;

public class RenderEngine {
	//Debug boolean
	public static final boolean _debug = true;
	private final Chassis chassis;
	
	protected RenderEngine(final Ignition ignition, final Chassis chassis) {
		this.chassis = chassis;
	}
}
