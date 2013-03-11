package com.hackhalo2.rendering.interfaces.entity;

import java.util.Collection;

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
	
	/**
	 * Gets all Entities from this Tracker. Depending on how the Tracker is implemented, this list could 
	 * be huge. 
	 * 
	 * @return every Entity this Tracker knows of
	 */
	public Collection<IEntity> getEntities();
	
	/**
	 * Gets all Entities from this Tracker that either extend or implement the provided Class. Depending 
	 * on how this Tracker is implemented, this Collection could be huge. If no Entities are found that 
	 * extend or implement the provided Class, null will be returned
	 * 
	 * @param clazz The class to check against the Entities
	 * @return the Collection of Entities that extend or implement the provided class, or null for none found
	 */
	public Collection<IEntity> getEntitiesByClass(Class<? extends IEntity> clazz);

}
