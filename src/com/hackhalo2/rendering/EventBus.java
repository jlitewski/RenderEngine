package com.hackhalo2.rendering;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.interfaces.annotations.EventMethod;
import com.hackhalo2.rendering.interfaces.core.IManager;
import com.hackhalo2.rendering.interfaces.event.EventListener;
import com.hackhalo2.rendering.interfaces.event.IEvent;
import com.hackhalo2.rendering.util.Pair;
import com.hackhalo2.rendering.util.PrioritySorter;
import com.hackhalo2.rendering.util.UsedPair;

public class EventBus implements IManager {

	private static Map<IEvent, TreeMap<Priority, HashSet<Pair<Method, EventListener>>>> eventMap =
			new HashMap<IEvent, TreeMap<Priority, HashSet<Pair<Method, EventListener>>>>();
	private Map<EventListener, TreeSet<UsedPair<Method, EventMethod>>> eventMethodCache =
			new HashMap<EventListener, TreeSet<UsedPair<Method, EventMethod>>>();
	
	//Event Bus ID's
	private static int maxID = 0;
	private final int masterBusID = maxID++;

	private static final Dashboard logger = new Dashboard();

	protected EventBus() {

	}

	public boolean registerEventListener(final EventListener listener) {
		logger.debug("EventBus", "Registering "+listener.getClass().getName(), 0);
		try {
			for(Method method : listener.getClass().getMethods()) {
				EventMethod ema = method.getAnnotation(EventMethod.class);
				if(ema == null) continue;

				Class<?>[] parameters = method.getParameterTypes();
				if(parameters.length != 1) {
					logger.printException(new IllegalArgumentException(
							"Method "+method.getName()+" (EventListener "+listener.getClass().getCanonicalName()+
							") has the wrong amount of arguments ("+parameters.length+")! EventMethods require "+
							"one (1) argument. Please contact the dev to have this issue fixed."));
					return false;
				}
				
				Class<?> eventType = parameters[0];
				if(!IEvent.class.isAssignableFrom(eventType)) {
					logger.printException(new IllegalArgumentException(
							"Method "+method.getName()+"'s (EventListener "+listener.getClass().getCanonicalName()+
							") arguement does not implement the IEvent interface or extend a class that does so!"));
					return false;
				}
				
				return true;
			}
		} catch(Exception e) {
			logger.printException(e);
			return false;
		}
		return false;
	}

	public static boolean registerEvent(final IEvent event) {
		if(!eventMap.containsKey(event)) {
			//We need to set up the Map to not bitch about things
			HashSet<Pair<Method, EventListener>> set;
			TreeMap<Priority, HashSet<Pair<Method, EventListener>>> value =
					new TreeMap<Priority, HashSet<Pair<Method, EventListener>>>(new PrioritySorter());

			for(Priority p : Priority.values()) {
				set = new HashSet<Pair<Method, EventListener>>();
				value.put(p, set);
			}

			eventMap.put(event, value);
			return true;
		} else { //The Event already exists
			String classpath = event.getClass().getCanonicalName();
			if(classpath == null) classpath = "NULL";
			logger.err("EventBus", "Event "+event.getEventName()+" ("+classpath+"):", 0);
			logger.err("EventBus", "Event was already registered!", 1);
			return false;
		}
	}
	
	public static void queueEvent(IEvent event) {
		
	}

	@Override
	public void cleanup() {

	}

}
