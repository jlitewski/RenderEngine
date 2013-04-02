package com.hackhalo2.render;

public class Chassis {
	
	//The Chassis Components
	private RenderEngine renderEngine; //The RenderEngine, duh
	
	
	public Chassis(final Ignition ignition) {
		
	}
	
	/**
	 * Returns the RenderEngine reference the Chassis manages
	 * @return the RenderEngine reference
	 */
	public RenderEngine getRenderEngine() {
		return this.renderEngine;
	}

}
