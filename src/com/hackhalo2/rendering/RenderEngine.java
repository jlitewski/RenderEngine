package com.hackhalo2.rendering;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.Color;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.interfaces.annotations.Execute;
import com.hackhalo2.rendering.interfaces.annotations.PriorityOverride;
import com.hackhalo2.rendering.interfaces.core.IChassis;
import com.hackhalo2.rendering.interfaces.core.IManager;
import com.hackhalo2.rendering.interfaces.core.IPluggable;
import com.hackhalo2.rendering.plugs.RenderPluggable;
import com.hackhalo2.rendering.util.PlugModeSorter;
import com.hackhalo2.rendering.util.PrioritySorter;
import com.hackhalo2.rendering.util.VBOContainer;
import com.hackhalo2.rendering.util.VBOContainer.ContainerType;
import com.hackhalo2.util.Pair;

public class RenderEngine {
	
	private IChassis chassis = null;
	private RenderLogger log = new RenderLogger();
	public static final boolean _debug = true;
	private Map<Priority, TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>>> pluggableMap =
			new TreeMap<Priority, TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>>>(new PrioritySorter());
	private Set<IPluggable> enabledPlugs = new HashSet<IPluggable>();
	private int glClearBit = 0;
	private boolean vboEnabled = false, colorEnabled = false, normalEnabled = false;
	private Color clearColor = new Color(Color.BLACK);
	private final byte floppeldidoppelin = 0;
	private static boolean running = false;
	private boolean wireframe = false, culling = false;

	private int cullingMode = GL11.GL_BACK;

	protected RenderEngine(IChassis chassis) {
		if(_debug) System.out.println("Debug Mode Active");
		//Initialize the plugable map
		for(Priority priority : Priority.values()) {
			TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>> map = this.pluggableMap.get(priority);
			map = new TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>>(new PlugModeSorter());
			for(PlugMode mode : PlugMode.getAllModes()) {
				HashSet<Pair<Method, IPluggable>> set = map.get(mode);
				set = new HashSet<Pair<Method, IPluggable>>();
				map.put(mode, set);
			}
			this.pluggableMap.put(priority, map);
		}

		this.chassis = chassis;
	}

	/* Render code */
	public void start() {
		this.log.info("loop", "Initializing the RenderEngine...", 0);

		Iterator<Pair<Method, IPluggable>> it1;
		Iterator<VBOContainer> it2;

		running = true;
		while(!Display.isCloseRequested() && !RenderUtils.error) {
			GL11.glClearColor(this.clearColor.getRed(), this.clearColor.getGreen(), this.clearColor.getBlue(), this.clearColor.getAlpha());

			//Clear the bits set to be cleared
			GL11.glClear(this.glClearBit);

			for(Priority priority : Priority.values()) {
				TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>> map = this.pluggableMap.get(priority);
				if(map.isEmpty()) continue; //Skip processing this Priority Level if the Map is empty
				for(PlugMode mode : PlugMode.getAllModes()) {
					HashSet<Pair<Method, IPluggable>> set = map.get(mode);
					if(set.isEmpty()) continue; //Skip processing this PlugMode if the Set is empty
					it1 = set.iterator();

					while(it1.hasNext()) {
						Pair<Method, IPluggable> pair = it1.next();
						if(!pair.getSecond().isEnabled()) continue; //TODO Move this up the for loop chain

						try {
							//Pass the call to the Manager and process the next pair, if any are left
							if(pair.getSecond() instanceof IManager) {
								pair.getFirst().invoke(pair.getSecond(), this.chassis);
								continue;
							}

							switch(mode) {
							case PRE_LOGIC:
							case POST_RENDER:
							case POST_LOGIC:
							case IDLE:
								//All of these don't have special processing requirements, so pass the call
								pair.getFirst().invoke(pair.getSecond(), this.chassis);
								break;

							case PRE_RENDER:

								if(pair.getSecond() instanceof RenderPluggable) {
									RenderPluggable rPlug = (RenderPluggable)pair.getSecond();
									if(rPlug.isDirty()) pair.getFirst().invoke(pair.getSecond(), this.chassis);
								} else pair.getFirst().invoke(pair.getSecond(), this.chassis);
								break;

							case RENDER:
								//Render the object directly
								Set<VBOContainer> vbos = null;
								if(pair.getSecond() instanceof RenderPluggable) {
									RenderPluggable rPlug = (RenderPluggable)pair.getSecond();
									vbos = rPlug.getVBOs(); //Get the VBOs
									if(this.vboEnabled && !vbos.isEmpty()) { //Failsafe check
										it2 = vbos.iterator();
										//Cycle through all the VBO types
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

										//Enable the client states
										GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
										if(this.colorEnabled) GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
										if(this.normalEnabled) GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

										//Render
										pair.getFirst().invoke(pair.getSecond(), this.chassis);

										//Disable the client states
										if(this.normalEnabled) GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
										if(this.colorEnabled) GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
										GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
									}
								} else pair.getFirst().invoke(pair.getSecond(), this.chassis);
								break;

							default:
								pair.getFirst().invoke(pair.getSecond(), this.chassis);
								break;
							}
							RenderUtils.checkGLErrors();
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}

			//Resize the viewport if the Display was resized
			if(Display.wasResized()) GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());

			Display.update();
			RenderUtils.updateFPS();
			Display.sync(RenderUtils.fps);
		}
		this.log.info("loop", "Shutting down the RenderEngine...", 0);

		it1 = null;
		it2 = null;

		//Clean up the Chassis
		this.chassis.cleanup();
		this.chassis = null;

		this.log.info("loop", "RenderEngine has shut down!", 0);
		running = false;
	}
	
	//Plug Functions
	//XXX Better API functions for this?
	public void setPlugEnabled(String name, boolean enabled) {
		for(IPluggable plug : this.enabledPlugs) {
			if(plug.getName().equals(name)) {
				plug.setEnabled(enabled);
				break;
			}
		}
	}

	//OpenGL functions
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

	public void setRenderEnabled(boolean enabled) {
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
	public boolean register(IPluggable object) {
		this.log.debug("register()", "Registering new Plug '"+object.getName()+"'...", 0);
		try {
			Method[] methods = Class.forName(object.getClass().getName()).getMethods();
			for(Method method : methods) {
				Execute exe = method.getAnnotation(Execute.class);
				if(exe == null) continue;
				else if(exe.executable()) {
					PlugMode mode = PlugMode.getByName(method.getName());
					if(mode == null) continue;

					Priority priority = object.getPriority();
					PriorityOverride override = method.getAnnotation(PriorityOverride.class);
					if(override != null) priority = override.priority();
					this.log.debug("register()", "Registering PlugMode '"+mode.name +"' with "+priority.name()+" Priority", 1);

					//Tear apart the Map and set the Pair
					TreeMap<PlugMode, HashSet<Pair<Method, IPluggable>>> map = this.pluggableMap.get(priority);
					HashSet<Pair<Method, IPluggable>> set = map.get(mode);
					Pair<Method, IPluggable> pair = new Pair<Method, IPluggable>(method, object);

					//Add the IPluggable to the enabledPlugs HashSet
					this.enabledPlugs.add(object);

					//Build the Map
					set.add(pair);
					map.put(mode, set);
					this.pluggableMap.put(priority, map);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}

		this.log.debug("register()", "Plug '"+object.getName()+"' registered sucessfully.", 0);

		return true;
	}

	/* Other Functions */
	public byte getFloppeldidoppelin() {
		return this.floppeldidoppelin;
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
