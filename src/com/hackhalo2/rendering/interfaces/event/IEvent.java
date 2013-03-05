package com.hackhalo2.rendering.interfaces.event;

import java.lang.reflect.Method;

import com.hackhalo2.rendering.interfaces.annotations.EventMethod;
import com.hackhalo2.rendering.util.Pair;

public interface IEvent {
	public String getEventName();
	public void addListener(EventListener parent, Pair<Method, EventMethod> methodPair);
	public void invoke(IEvent event);
}
