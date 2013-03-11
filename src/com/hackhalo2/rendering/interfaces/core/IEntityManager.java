package com.hackhalo2.rendering.interfaces.core;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.hackhalo2.rendering.interfaces.entity.IEntity;
import com.hackhalo2.rendering.interfaces.entity.IEntityTracker;

public interface IEntityManager<ET extends IEntityTracker> {
	
	/**
	 * The internal ID counter. If an Entity is spawned, the Tracker will request an ID, either 
	 * from this AtomicInteger, or a List of Integers of past used ID's, if the Manager has such 
	 * a List implemented.
	 */
	public static final AtomicInteger ids = new AtomicInteger(1);
	
	/**
	 * Get the default Entity Tracker the Entity Manager provides.
	 * 
	 * @return the default Entity Tracker
	 */
	public ET getEntityTracker();
	
	/**
	 * Get the Entity Tracker from the given name. This method may or may not be used. If it is not used, 
	 * it should default to the same Entity Tracker as the {@link getEntityTracker()} method, for 
	 * consistency.
	 * 
	 * @param name The Entity Tracker name
	 * @return The Entity Tracker linked by the given name, or the Default if there is only one Tracker
	 */
	public ET getEntityTracker(final String name);
	
	/**
	 * Add a new Entity Tracker to the Entity Manager, if supported. If adding Entity Trackers is not 
	 * supported, this method should return false. If adding the Entity Tracker failed, this method should 
	 * return false.
	 * 
	 * @param tracker The Entity Tracker to add to the Manager
	 * @param name The name of the Tracker to be added
	 * @return true if adding the Tracker was successful, false otherwise
	 */
	public boolean addEntityTracker(final ET tracker, final String name);
	
	/**
	 * Remove an Entity Tracker from the Manager. You cannot remove the default Entity Tracker. Implementing 
	 * classes may have other guidelines on what else needs to be true in order for a Tracker to be 
	 * successfully removed from the Manager.
	 * 
	 * @param name The name of the Entity Tracker to be removed
	 * @return true if removing the Entity Tracker was successful, false otherwise
	 */
	public boolean removeEntityTracker(final String name);
	
	/**
	 * Gets all Entities across all Trackers. Depending on how many trackers this Manager manages, this 
	 * Collection could be huge. It is recommended to use {@link getAllEntitiesByClass(Class class)} 
	 * instead.
	 * 
	 * @return the Collection of every Entity across every tracker
	 */
	public Collection<IEntity> getAllEntities();
	
	/**
	 * Gets all Entities across all Trackers that either extend or implement the class supplied. 
	 * Depending on how many Trackers this Manager manages, this Collection could be huge. If no Entities 
	 * extend or implement the supplied class, null will be returned
	 * 
	 * @param clazz The class to be checked against
	 * @return the Collection of Entities that extend the Class provided, or null for no Entities found
	 */
	public Collection<IEntity> getAllEntitiesByClass(Class<? extends IEntity> clazz);
}
