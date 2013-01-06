package com.hackhalo2.rendering;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.Color;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.RenderUtils.RefreshReason;
import com.hackhalo2.rendering.interfaces.annotations.Executable;
import com.hackhalo2.rendering.interfaces.annotations.PriorityOverride;
import com.hackhalo2.rendering.interfaces.core.IChassis;
import com.hackhalo2.rendering.interfaces.core.IManager;
import com.hackhalo2.rendering.interfaces.core.IPluggable;
import com.hackhalo2.rendering.plugs.RenderPlugable;
import com.hackhalo2.rendering.util.PlugModeSorter;
import com.hackhalo2.rendering.util.PrioritySorter;
import com.hackhalo2.rendering.util.TreeSetPlugableSorter;
import com.hackhalo2.rendering.util.VBOContainer;
import com.hackhalo2.rendering.util.VBOContainer.ContainerType;
import com.hackhalo2.util.Pair;

public class RenderEngine {

	private static Logger _log = Logger.getLogger("RenderEngine");
	private IChassis chassis = null;
	public static final boolean _debug = true;
	private Map<PlugMode, TreeSet<IPluggable>> oldPlugableMap =
			new HashMap<PlugMode, TreeSet<IPluggable>>();
	private Map<Priority, TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>>> plugableMap =
			new TreeMap<Priority, TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>>>(new PrioritySorter());
	private int glClearBit = 0;
	private boolean vboEnabled = false, colorEnabled = false, normalEnabled = false;
	private Color clearColor = new Color(Color.BLACK);
	private final byte floppeldidoppelin = 0;
	private static boolean running = false;
	private boolean wireframe = false, culling = false;

	private int cullingMode = GL11.GL_BACK;

	protected RenderEngine(IChassis chassis) {
		//Initialize the plugable map
		PlugMode[] modes = PlugMode.values();
		for(int i = 0; i < modes.length; i++) {
			TreeSet<IPluggable> temp = this.oldPlugableMap.get(modes[i]);
			temp = new TreeSet<IPluggable>(new TreeSetPlugableSorter());
			this.oldPlugableMap.put(modes[i], temp);
		}
		
		for(Priority priority : Priority.values()) {
			TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>> map = this.plugableMap.get(priority);
			map = new TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>>(new PlugModeSorter());
			for(PlugMode mode : PlugMode.getAllModes()) {
				HashSet<Pair<Method, IPluggable>> set = map.get(mode);
				set = new HashSet<Pair<Method, IPluggable>>();
				map.put(mode, set);
			}
			this.plugableMap.put(priority, map);
		}

		this.chassis = chassis;
	}

	/* Render code */
	/**
	 * @deprecated Kept for compatibility. Once newStart is done, switch to that.
	 */
	@Deprecated
	public void start() {
		System.out.println("Initializing the RenderEngine...");

		//set up the reusable iterators
		Iterator<IPluggable> it1;
		Iterator<VBOContainer> it2;

		running = true;
		while(!Display.isCloseRequested() && !RenderUtils.error) {

			GL11.glClearColor(this.clearColor.getRed(), this.clearColor.getGreen(), this.clearColor.getBlue(), this.clearColor.getAlpha());

			//Clear the bits set to be cleared
			GL11.glClear(this.glClearBit);

			//Execute the pre-render logic code
			it1 = this.oldPlugableMap.get(PlugMode.PRE_LOGIC).descendingIterator();
			while(it1.hasNext()) (it1.next()).preLogic(this.chassis);

			//Execute the render code per vbo
			it1 = this.oldPlugableMap.get(PlugMode.PRE_RENDER).descendingIterator();
			while(it1.hasNext()) {
				IPluggable plug = it1.next();
				Set<VBOContainer> vbos = null;
				if(plug instanceof IManager) {
					//Pass the render logic directly to the manager
					plug.preRender(this.chassis);
					plug.render(this.chassis);
					continue;
				} else if(plug instanceof RenderPlugable) {
					RenderPlugable rPlug = (RenderPlugable)plug; //cast it into a RenderPlugable
					vbos = rPlug.getVBOs(); //set the temporary variable to the VBO's contained in the plug
					if(rPlug.isDirty()) rPlug.preRender(this.chassis); //Prerender if it's dirty
				} else {
					plug.preRender(this.chassis);
				}

				if(this.vboEnabled && !vbos.isEmpty()) { //Failsafe check
					it2 = vbos.iterator();
					while(it2.hasNext()) {
						VBOContainer vbo = it2.next();
						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo.getHandle());
						switch(vbo.getType()) {
						case NORMAL:
							if(vbo.getType() != ContainerType.NORMAL) throw new RuntimeException("VBO type mismatch! (Expected NORMAL, got "+ vbo.getType().toString()+")");
							GL11.glNormalPointer(GL11.GL_FLOAT, vbo.getStride(0), 0L);
							break;
						case COLOR:
							if(vbo.getType() != ContainerType.COLOR) throw new RuntimeException("VBO type mismatch! (Expected COLOR, got "+ vbo.getType().toString()+")");
							GL11.glColorPointer(vbo.getSize(), GL11.GL_FLOAT, vbo.getStride(0), 0L);
							break;
						case VERTEX:
							if(vbo.getType() != ContainerType.VERTEX) throw new RuntimeException("VBO type mismatch! (Expected VERTEX, got "+ vbo.getType().toString()+")");
							GL11.glVertexPointer(vbo.getSize(), GL11.GL_FLOAT, vbo.getStride(0), 0L);
							break;
						default:
							throw new RuntimeException("Undefined or unsupported vbo type!");
						}
						GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
					}

					GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
					if(this.colorEnabled) GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
					if(this.normalEnabled) GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

					plug.render(this.chassis);
					RenderUtils.checkGLErrors();

					if(this.normalEnabled) GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
					if(this.colorEnabled) GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
					GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
				}
			}

			//Execute the post-render code
			it1 = this.oldPlugableMap.get(PlugMode.POST_RENDER).descendingIterator();
			while(it1.hasNext()) (it1.next()).postRender(this.chassis);

			//Update the display (switch the buffers and such)

			if(Display.wasResized()) {
				if(_debug) {
					it1 = RenderUtils.getIteratorFromComplexMap(this.oldPlugableMap);
					while(it1.hasNext()) {
						IPluggable plug = it1.next();
						plug.refresh(this.chassis, RefreshReason.DISPLAY_RESIZED);
					}
				}
				GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			}

			Display.update();

			//Execute the post-render logic code
			it1 = this.oldPlugableMap.get(PlugMode.POST_LOGIC).descendingIterator();
			while(it1.hasNext()) (it1.next()).postLogic(this.chassis);

			//Execute the render idle code
			it1 = this.oldPlugableMap.get(PlugMode.IDLE).descendingIterator();
			while(it1.hasNext()) (it1.next()).idleRender(this.chassis);

			RenderUtils.updateFPS();
			Display.sync(RenderUtils.fps);
		}
		System.out.println("Shutting down the RenderEngine...");

		//TODO: Internal clean up code here
		it1 = null;
		it2 = null;

		//Clean up the Chassis
		this.chassis.cleanup();
		this.chassis = null;

		System.out.println("RenderEngine successfully shut down.");
		running = false;
	}
	
	public void newStart() {
		
	}

	public void setWireframeModeEnabled(boolean enabled) {
		this.wireframe = enabled;
		if(enabled) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		} else {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
	}

	public boolean isWireframeEnabled() {
		return this.wireframe;
	}

	public void setCullingMode(int cullingMode) {
		this.cullingMode = cullingMode;
	}

	public void setCullingEnabled(boolean enabled) {
		this.culling = enabled;
		if(enabled) {
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(this.cullingMode);
		} else {
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
	}

	public boolean isCullingEnabled() {
		return this.culling;
	}

	public void refreshCullingMode() {
		GL11.glCullFace(this.cullingMode);
	}

	public void setDepthTestingEnabled(boolean enabled) {
		if(enabled) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
		} else {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		}
	}

	public void setVertexEnabled(boolean enabled) {
		this.vboEnabled = enabled;
	}

	public boolean getVertexEnabled() {
		return this.vboEnabled;
	}

	public void setColorEnabled(boolean enabled) {
		this.colorEnabled = enabled;
	}

	public boolean getColorEnabled() {
		return this.colorEnabled;
	}

	public void setNormalEnabled(boolean enabled) {
		this.normalEnabled = enabled;
	}

	public boolean getNormalEnabled() {
		return this.normalEnabled;
	}

	public void appendClearBit(int glClearInteger) {
		this.glClearBit = this.glClearBit | glClearInteger;
	}

	/* Register functions */
	@Deprecated
	public boolean register(IPluggable object, PlugMode... states) {
		List<PlugMode> modes = new ArrayList<PlugMode>();
		//Idiot checking
		for(PlugMode state : states) {
			if(state == PlugMode.ALL) { //Add ALL the modes!
				for(PlugMode mode : PlugMode.getAllModes()) {
					if(!modes.contains(mode)) modes.add(mode);
				}
				break; //Since we just added all the modes, break out of this loop
			} else if(state == PlugMode.GROUP_LOGIC) { //Add the Logic modes
				for(PlugMode mode : PlugMode.getLogicModes()) {
					if(!modes.contains(mode)) modes.add(mode);
				}
			} else if(state == PlugMode.GROUP_RENDER) { //Add the Render Modes
				for(PlugMode mode : PlugMode.getRenderModes()) {
					if(!modes.contains(mode)) modes.add(mode);
				}
			} else {
				if(!modes.contains(state)) modes.add(state);
			}
		}

		for(PlugMode mode : modes) {
			TreeSet<IPluggable> temp = this.oldPlugableMap.get(mode);
			if(!temp.contains(object)) { //Don't try adding the object if it already exists
				//This is also just a sanity check, since it should never happen
				boolean success = temp.add(object);
				if(!success) System.err.println("Could not register the Pluggable with PlugMode "+mode.name());
				this.oldPlugableMap.put(mode, temp);
			}
		}

		return true;
	}
	
	public boolean registerNew(IPluggable object) {
		try {
			Method[] methods = Class.forName(object.getClass().getName()).getMethods();
			for(Method method : methods) {
				Executable exe = method.getAnnotation(Executable.class);
				if(exe == null) continue;
				else if(exe.execute()) {
					PlugMode mode = PlugMode.getByName(method.getName());
					if(mode == null) continue;
					
					Priority priority = object.getPriority();
					PriorityOverride override = method.getAnnotation(PriorityOverride.class);
					if(override != null) priority = override.priority();
					
					//Tear apart the Map and set the Pair
					TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>> map = this.plugableMap.get(priority);
					HashSet<Pair<Method, IPluggable>> set = map.get(mode);
					Pair<Method, IPluggable> pair = new Pair<Method, IPluggable>(method, object);
					
					//Build the Map
					set.add(pair);
					map.put(mode, set);
					this.plugableMap.put(priority, map);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/* Other Functions */

	public byte getFloppeldidoppelin() {
		return this.floppeldidoppelin;
	}

	public static synchronized Logger getLogger() {
		return _log;
	}

	public void setClearColor(Color color) {
		this.clearColor = color;
	}

	protected static boolean isRunning() {
		return running;
	}

	public enum PlugMode {
		//Selective enums
		PRE_LOGIC("preLogic", Priority.HIGHEST),
		PRE_RENDER("preRender", Priority.HIGHER),
		RENDER("render", Priority.HIGH),
		POST_RENDER("postRender", Priority.MEDHIGH),
		POST_LOGIC("postLogic", Priority.MEDIUM),
		IDLE("idleRender", Priority.LOWEST),

		//Group enums
		GROUP_LOGIC,
		GROUP_RENDER,

		//All the enums!
		ALL;
		
		private Priority priority;
		private String name;
		
		private PlugMode(String name, Priority p) {
			this.name = name;
			this.priority = p;
		}
		
		private PlugMode(String name) {
			this.name = name;
			this.priority = Priority.ROCK_BOTTOM;
		}
		
		private PlugMode() {
			this.name = "";
			this.priority = Priority.ROCK_BOTTOM;
		}
		
		public Priority getPriority() {
			return this.priority;
		}
		
		public static PlugMode getByName(String name) {
			for(PlugMode mode : PlugMode.getAllModes()) {
				if(mode.name.equals(name)) return mode;
			}
			return null;
		}

		public static PlugMode[] getAllModes() {
			return new PlugMode[] {PRE_LOGIC, PRE_RENDER, RENDER, POST_RENDER, POST_LOGIC, IDLE};
		}

		public static PlugMode[] getRenderModes() {
			return new PlugMode[] {PRE_RENDER, RENDER, POST_RENDER};
		}

		public static PlugMode[] getLogicModes() {
			return new PlugMode[] {PRE_LOGIC, POST_LOGIC};
		}

		public enum Priority {
			HIGHEST(100),
			HIGHER(90),
			HIGH(80),
			MEDHIGH(70),
			MEDIUM(60),
			NORMAL(50),
			MEDLOW(40),
			LOW(30),
			LOWER(20),
			LOWEST(10),
			ROCK_BOTTOM(0);

			private int level;

			private Priority(int level) {
				this.level = level;
			}

			public int getLevel() {
				return this.level;
			}
		}
	}

}
