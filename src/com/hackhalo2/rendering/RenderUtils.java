package com.hackhalo2.rendering;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import com.hackhalo2.rendering.RenderEngine.PlugMode;
import com.hackhalo2.rendering.exceptions.GLException;
import com.hackhalo2.rendering.interfaces.core.IPluggable;

public class RenderUtils {
	public static int fps = 60; //The Frames per Second the Display will run at
	public static boolean error = false;
	public static int counter = 0;
	private static long lastFrame = 0L;
	private static long lastFPS = getTime();
	private static BitSet supportedFlags = new BitSet();
	private static int multisamplingSamples;

	//Prevent Initialization
	private RenderUtils() { }

	public static ClassLoader getClassLoader() {
		return RenderUtils.class.getClassLoader();
	}

	public static void checkGLErrors() {
		int glError = GL11.glGetError();
		if (glError != GL11.GL_NO_ERROR) {
			error = true;
			throw new GLException(GLU.gluErrorString(glError)+" caused by "+GLU.gluGetString(glError));
		}
	}

	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static int getDelta() {
		long time = getTime();
		int delta = (int)(time-lastFrame);
		lastFrame = time;

		return delta;
	}

	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("Sandbox: "+counter+" FPS");
			counter = 0;
			lastFPS += 1000;
		}
		counter++;
	}

	public static void setFPS(int newFPS) {
		fps = newFPS;
	}

	public static boolean isMultisamplingSupported() {
		return supportedFlags.get(0);
	}

	public static void setMultisamplingSupported(boolean flag) {
		supportedFlags.set(0, flag);
	}

	public static int getMSSamples() {
		return multisamplingSamples;
	}

	public static void setMSSamples(int samples) {
		multisamplingSamples = samples;
	}
	
	@Deprecated
	protected static Iterator<IPluggable> getIteratorFromComplexMap(Map<PlugMode, TreeSet<IPluggable>> map) {
		Iterator<IPluggable> it;
		PlugMode[] modes = PlugMode.getAllModes();
		Set<IPluggable> set = new HashSet<IPluggable>();
		
		for(PlugMode mode : modes) {
			it = map.get(mode).iterator();
			while(it.hasNext()) {
				IPluggable plug = it.next();
				if(!set.contains(plug)) set.add(plug);
			}
		}
		
		return set.iterator();
	}
	
	public enum RefreshReason {
		DISPLAY_RESIZED,
		OTHER
	}
}
