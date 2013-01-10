package com.hackhalo2.rendering;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.RenderUtils.RefreshReason;
import com.hackhalo2.rendering.interfaces.annotations.Execute;
import com.hackhalo2.rendering.interfaces.core.IChassis;
import com.hackhalo2.rendering.interfaces.core.IManager;
import com.hackhalo2.rendering.interfaces.core.IPluggable;

public class KeyboardBuffer implements IManager, IPluggable {

	private BitSet bufferedIn = null;
	private BitSet bufferedOut = null;
	private BitSet bufferedLast = null;
	private Set<Integer> registeredKeys = null;
	private Map<Integer, Integer> keyMap = null;
	private boolean enabled = true;

	//Prevent Initialization from outside the classpath
	protected KeyboardBuffer() {
		//Mouse Initialization code
		try {
			if(!Mouse.isCreated()) { //Check to see if the LWJGL mouse has been created
				Mouse.create();
			}
		} catch(LWJGLException e) {
			System.out.println("Unable to initialize the Mouse!");
		}
		
		//Keyboard Initialization code
		try {
			if(!Keyboard.isCreated()) { //Check to see if the LWJGL keyboard has been created
				Keyboard.create();
			}
		} catch(LWJGLException e) {
			System.out.println("Unable to initialize the Keyboard!");
		}
		
		//Other Initialization code
		this.bufferedIn = new BitSet();
		this.bufferedOut = new BitSet();
		this.bufferedLast = new BitSet();
		this.registeredKeys = new TreeSet<Integer>();
		this.keyMap = new TreeMap<Integer, Integer>();
	}
	
	@Override
	public void setEnabled(final boolean flag) {
		this.enabled  = flag;
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void preLogic(IChassis chassis) { }
	
	@Override
	public void preRender(IChassis chassis) { }
	
	@Override
	public void render(IChassis chassis) { }
	
	@Override
	public void postRender(IChassis chassis) { }
	
	@Override
	@Execute
	public void idleRender(IChassis chassis) {
		//Poll the registered keys for updates
		Iterator<Integer> i = this.registeredKeys.iterator(); //Get the registered keyboard keys
		while(i.hasNext()) { //Loop through them
			int key = i.next();
			this.bufferedIn.set(this.keyMap.get(key), Keyboard.isKeyDown(key)); //store the current state in the buffered map
		}
	}
	
	@Override
	@Execute
	public void postLogic(IChassis chassis) {
		//Swap the BitSets
		BitSet tempOut = this.bufferedOut;
		BitSet tempLast = this.bufferedLast;
		this.bufferedOut = this.bufferedIn; //In to Out
		this.bufferedLast = tempOut; //Out to Last
		this.bufferedIn = tempLast; //Last to In
		tempOut = null;
		tempLast = null;
	}
	
	public boolean getState(int key) {
		if(this.registeredKeys.contains(key)) {
			return this.bufferedOut.get(this.keyMap.get(key));
		} else {
			return false;
		}
	}
	
	public boolean getLastState(int key) {
		if(this.registeredKeys.contains(key)) {
			return this.bufferedLast.get(this.keyMap.get(key));
		} else {
			return false;
		}
	}
	
	public void registerKey(int key) {
		if(!this.registeredKeys.contains(key)) { //Make sure the key doesn't get registered again
			this.keyMap.put(key, this.keyMap.size());
			this.registeredKeys.add(key); //Initialize the map entry
		}
	}
	
	@Override
	public Priority getPriority() {
		return Priority.HIGHEST;
	}

	@Override
	public void refresh(IChassis chassis, RefreshReason reason) { }

	@Override
	public void cleanup() {
		this.bufferedIn = null;
		this.bufferedLast = null;
		this.bufferedOut = null;
		this.keyMap = null;
		this.registeredKeys = null;
	}

	@Override
	public String getName() {
		return "KeyboardBuffer";
	}

}
