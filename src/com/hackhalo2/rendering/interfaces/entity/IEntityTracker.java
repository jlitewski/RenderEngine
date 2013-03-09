package com.hackhalo2.rendering.interfaces.entity;

public interface IEntityTracker {
	
	/**
	 * Adds an Entity to be Tracked
	 * 
	 * @param entity the Entity to track
	 * @return true if the Entity is being tracked, false otherwise
	 */
	public boolean addEntityTracking(final IEntity entity);
	
	
	public boolean removeEntityTracking(final int entityID);

}
