package com.hackhalo2.rendering.camera;

import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Set;

import org.lwjgl.opengl.GL15;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.RenderUtils.RefreshReason;
import com.hackhalo2.rendering.interfaces.core.IChassis;
import com.hackhalo2.rendering.interfaces.core.IPluggable;
import com.hackhalo2.rendering.util.VBOContainer;

public abstract class GUIElement implements IPluggable {

	protected int vertexHandle; //The Vertex Buffer Object handle
	protected int colorHandle; //The Color handle
	protected int normalHandle; //The Normal handle

	protected FloatBuffer vertexData; //The VBO FloatBuffer
	protected FloatBuffer colorData; //The Color FloatBuffer
	protected FloatBuffer normalData; //The Normals FloatBuffer

	private boolean dirty = true; //The dirty boolean
	private boolean active = false; //the active boolean

	private Priority priority = Priority.NORMAL; //The default priority level

	private String uiName; //The GUIElement name

	private Set<VBOContainer> vbo; //The set of VBOContainers

	//Constructor
	protected GUIElement(String name, Priority priority) {
		this.uiName = name;
		this.priority = priority;

		//Generate the VBO handles
		this.vertexHandle = GL15.glGenBuffers();
		this.colorHandle = GL15.glGenBuffers();
		this.normalHandle = GL15.glGenBuffers();
	}

	/* IPlugable methods */
	@Override
	public void preLogic(IChassis chassis) { }

	@Override
	public abstract void preRender(IChassis chassis);

	@Override
	public abstract void render(IChassis chassis);

	@Override
	public void postRender(IChassis chassis) { }

	@Override
	public void idleRender(IChassis chassis) { }

	@Override
	public void postLogic(IChassis chassis) { }
	
	@Override
	public void refresh(IChassis chassis, RefreshReason reason) { }

	public boolean isActive() {
		return this.active;
	}

	/* Custom defined base functions */
	public void activate() {
		this.active = true;
	}
	
	public void setActive(boolean flag) {
		this.active = flag;
	}

	public void toggleActive() {
		this.active = !this.active;
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public void setDirty(boolean flag) {
		this.dirty = flag;
	}

	public void toggleDirty() {
		this.dirty = !this.dirty;
	}

	public String getName() {
		return this.uiName;
	}

	protected void register(Set<VBOContainer> set) {
		this.vbo = set;
	}

	public Set<VBOContainer> getVBOs() {
		if(this.vbo != null) return Collections.unmodifiableSet(this.vbo);
		else return Collections.emptySet();
	}
	
	public void clearVBOs() {
		if(this.vbo != null) this.vbo = Collections.emptySet();
	}

	@Override
	public Priority getPriority() {
		return this.priority;
	}
}
