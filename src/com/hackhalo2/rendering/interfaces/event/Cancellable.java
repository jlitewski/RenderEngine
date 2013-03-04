package com.hackhalo2.rendering.interfaces.event;

public interface Cancellable {
	public void setEventCancelled(boolean cancelled);
	public boolean isCancelled();
}
