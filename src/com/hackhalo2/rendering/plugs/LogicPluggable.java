package com.hackhalo2.rendering.plugs;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.RenderUtils.RefreshReason;
import com.hackhalo2.rendering.interfaces.core.IChassis;
import com.hackhalo2.rendering.interfaces.core.IPluggable;

public abstract class LogicPluggable implements IPluggable {
	private Priority priority = Priority.NORMAL;

	protected LogicPluggable() { }

	@Override
	public Priority getPriority() {
		return priority;
	}

	protected void setPriority(Priority priority) {
		this.priority = priority;
	}

	@Override
	public abstract void preLogic(IChassis chassis);

	@Override
	public void preRender(IChassis chassis) { }

	@Override
	public void render(IChassis chassis) { }

	@Override
	public void postRender(IChassis chassis) { }

	@Override
	public void idleRender(IChassis chassis) { }

	@Override
	public abstract void postLogic(IChassis chassis);

	@Override
	public abstract String getName();

	@Override
	public void refresh(IChassis chassis, RefreshReason reason) { }

}
