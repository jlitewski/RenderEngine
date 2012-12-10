package com.hackhalo2.rendering.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

public class GLState {

	private Map<Integer, Boolean> states = new HashMap<Integer, Boolean>();
	
	public GLState() { }
	
	public void getState(int glInteger) {
		this.states.put(glInteger, GL11.glGetBoolean(glInteger));
	}
	
	public void getStates(int... glIntegers) {
		for(int glInteger : glIntegers)
			this.states.put(glInteger, GL11.glGetBoolean(glInteger));
	}
	
	public void recall() {
		Iterator<Integer> it = this.states.keySet().iterator();
		while(it.hasNext()) {
			Integer i = it.next();
			Boolean b = this.states.get(i);
			
			if(b) {
				if(!GL11.glGetBoolean(i)) GL11.glEnable(i);
			} else {
				if(GL11.glGetBoolean(i)) GL11.glDisable(i);
			}
		}
	}

}
