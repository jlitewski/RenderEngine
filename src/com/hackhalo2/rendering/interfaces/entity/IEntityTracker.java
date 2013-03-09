package com.hackhalo2.rendering.interfaces.entity;

public interface IEntityTracker {
	
	/**
	 * Adds an Entity to the Tracker.
	 * 
	 * @param entity the Entity to track
	 * @return true if the Entity is being tracked, false otherwise
	 */
	public boolean addEntityTracking(final IEntity entity);
	
	/**
	 * Removes an Entity from the Tracker.
	 * 
	 * @param entity The Entity to remove from the Tracker
	 * @return true if the entity was removed from tracking, false otherwise
	 */
	public boolean removeEntityTracking(final IEntity entity);
	
	/**
	 * Hook for gathering updates to tracked Entities. This method should not be used by outside code, 
	 * as it's ment to send update packets via a Network handler, which currently is not implemented.
	 */
	public void updateTrackedEntities();

}
