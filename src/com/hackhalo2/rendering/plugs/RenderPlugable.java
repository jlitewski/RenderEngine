package com.hackhalo2.rendering.plugs;

import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Set;

import org.lwjgl.opengl.GL15;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.RenderUtils.RefreshReason;
import com.hackhalo2.rendering.interfaces.core.IChassis;
import com.hackhalo2.rendering.interfaces.core.IPluggable;
import com.hackhalo2.rendering.util.VBOContainer;

public abstract class RenderPlugable implements IPluggable {

	protected int vertexHandle; //The Vertex Buffer Object handle
	protected int colorHandle; //The Color handle
	protected int normalHandle; //The Normal handle

	protected FloatBuffer vertexData; //The VBO FloatBuffer
	protected FloatBuffer colorData; //The Color FloatBuffer
	protected FloatBuffer normalData; //The Normals FloatBuffer

	private boolean isDirty = true; //The Dirty bit
	private Priority priority = Priority.NORMAL;

	private Set<VBOContainer> vbo;

	protected RenderPlugable() {
		this.vertexHandle = GL15.glGenBuffers();
		this.colorHandle = GL15.glGenBuffers();
		this.normalHandle = GL15.glGenBuffers();
	}

	protected void setPriority(Priority priority) {
		this.priority = priority;
	}

	@Override
	public Priority getPriority() {
		return this.priority;
	}

	@Override
	public void preLogic(IChassis chassis) { }

	@Override
	public abstract void preRender(IChassis chassis);

	@Override
	public abstract void render(IChassis chassis);

	@Override
	public void postRender(IChassis chassis) { };

	@Override
	public void idleRender(IChassis chassis) { }

	@Override
	public void postLogic(IChassis chassis) { }
	
	@Override
	public void refresh(IChassis chassis, RefreshReason reason) {
		switch(reason) {
		case DISPLAY_RESIZED:
			this.isDirty = true;
			this.vbo = Collections.emptySet();
			break;
		default:
			break;
		}
	}

	protected void register(Set<VBOContainer> vbo) { this.vbo = vbo; }

	public Set<VBOContainer> getVBOs() {
		if(this.vbo != null) return Collections.unmodifiableSet(this.vbo);
		else return Collections.emptySet();
	}

	public boolean isDirty() { return this.isDirty; }

	public void setDirty(boolean isDirty) { this.isDirty = isDirty; }

}
