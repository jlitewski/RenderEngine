package com.hackhalo2.rendering;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.interfaces.core.IManager;
import com.hackhalo2.rendering.interfaces.event.EventListener;
import com.hackhalo2.rendering.interfaces.event.IEvent;
import com.hackhalo2.rendering.util.PrioritySorter;
import com.hackhalo2.util.Pair;

public class EventBus implements IManager {
	
	private Map<IEvent, TreeMap<Priority, HashSet<Pair<Method, EventListener>>>> eventMap =
			new HashMap<IEvent, TreeMap<Priority, HashSet<Pair<Method, EventListener>>>>();
	
	private final RenderLogger logger = new RenderLogger();
	
	protected EventBus() {
		
	}
	
	public boolean registerEvent(final IEvent event) {
		if(!this.eventMap.containsKey(event)) {
			//We need to set up the Map to not bitch about things
			HashSet<Pair<Method, EventListener>> set;
			TreeMap<Priority, HashSet<Pair<Method, EventListener>>> value =
					new TreeMap<Priority, HashSet<Pair<Method, EventListener>>>(new PrioritySorter());
			
			for(Priority p : Priority.values()) {
				set = new HashSet<Pair<Method, EventListener>>();
				value.put(p, set);
			}
			
			this.eventMap.put(event, value);
			return true;
		} else {
			String classpath = event.getClass().getCanonicalName();
			if(classpath == null) classpath = "NULL";
			this.logger.err("EventBus", "Event "+event.getEventName()+" ("+classpath+"):", 0);
			this.logger.err("EventBus", "Event was already registered!", 1);
			return false;
		}
	}

	@Override
	public void cleanup() {
		
	}

}
