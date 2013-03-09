package com.hackhalo2.rendering.interfaces.core;

import com.hackhalo2.rendering.interfaces.entity.IEntityTracker;

public interface IEntityManager<ET extends IEntityTracker> {
	
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
	
	
}
