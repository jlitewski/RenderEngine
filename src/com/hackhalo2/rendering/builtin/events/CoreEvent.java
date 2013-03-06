package com.hackhalo2.rendering.builtin.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.TreeSet;

import com.hackhalo2.rendering.RenderLogger;
import com.hackhalo2.rendering.interfaces.annotations.EventMethod;
import com.hackhalo2.rendering.interfaces.event.EventListener;
import com.hackhalo2.rendering.interfaces.event.IEvent;
import com.hackhalo2.rendering.util.Pair;
import com.hackhalo2.rendering.util.UsedPair;
import com.hackhalo2.rendering.util.UsedPairSorter;

/**
 * The built in Core event for the RenderEngine. This should be used as a reference to any Libraries
 * implementing Events.
 * @author hackhalo2
 */
public class CoreEvent implements IEvent {

	/**
	 * The Map of Listeners this Event (and any Events extending this class) listens for
	 */
	protected Map<EventListener, TreeSet<UsedPair<Method, EventMethod>>> listenerMap =
			new HashMap<EventListener, TreeSet<UsedPair<Method, EventMethod>>>();
	/**
	 * The Logger, obviously
	 */
	protected RenderLogger logger = new RenderLogger();

	protected CoreEvent() { }

	@Override
	public String getEventName() {
		return "CoreEvent";
	}

	@Override
	public void addListener(EventListener parent, Pair<Method, EventMethod> methodPair) {
		//Firstly, synchronize the listenerMap.
		synchronized(this.listenerMap) {
			//Then we get the set of MethodPairs (this may return null because the map may not have the EventListener yet)
			TreeSet<UsedPair<Method, EventMethod>> set = this.listenerMap.get(parent);

			/*
			 * Null check
			 * I use the UsedPair class so I can sort based on the amount of times that pair has been used,
			 * sorta like a caching system. Most used are in the beginning of the set, with the lesser
			 * used behind them
			 */
			if(set == null) set = new TreeSet<UsedPair<Method, EventMethod>>(new UsedPairSorter());

			//Add the Pair to the set
			set.add((UsedPair<Method, EventMethod>)methodPair);

			//Then add it back into the Map
			this.listenerMap.put(parent, set);
		}
	}

	@Override
	public void invoke(IEvent event) {
		/*
		 * With the invoke(event) method, we need to not only invoke the Event the EventBus wants, but
		 * every Superclass Event that Event may have. We have to use some Reflection trickery to do this. 
		 */
		//Firstly, synchronize the listenerMap. We don't want an Event to be added while we are trying to add one
		synchronized(this.listenerMap) {
			Class<?> c = event.getClass(); //Get the class of the Event. We will use this later
			Set<EventListener> set = this.listenerMap.keySet(); //Get the set of keys in the listenerMap
			
			//Start looping through the superclasses and the listeners
			while(c != null) {
				//First loop through the EventListener set
				for(EventListener listener : set) {
					//Get the set of the method Pairs to use for Reflection
					TreeSet<UsedPair<Method, EventMethod>> methodSet = this.listenerMap.get(listener);
					
					//Then loop through the TreeSet of Pairs
					for(UsedPair<Method, EventMethod> methodPair : methodSet) {
						//Use some Relfection trickery to get the EventType of the Method
						
						/*
						 * This will get the first input Parameter, which we are going to use to check
						 * against the c variable
						 */
						Class<?> methodEventType = methodPair.getFirst().getParameterTypes()[0];
						
						//Check to see if the MethodType is assignable to the class of c
						if(methodEventType.isAssignableFrom(c)) {
							methodPair.incrementUsedCounter(); //Increment the used counter, since the use was successful
							
							try {
								//Invoke the Event listener
								methodPair.getFirst().invoke(listener, event);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								/*
								 * If anything should go wrong (which it very much will sooner or later),
								 * print the exception to the logger.
								 */
								this.logger.printException(e);
							}
						}
					}
				}
				c = c.getSuperclass(); //Get the superclass of the event and redo the loop until this is null
			}
		}
	}

}
