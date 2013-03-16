package com.hackhalo2.rendering.entity.util;

import com.hackhalo2.rendering.interfaces.entity.IEntity;

public class EntityUtils {
	
	private EntityUtils() { }
	
	/**
	 * Returns the square root distance between two Entities on a 2D plane
	 * <br />
	 * This was inspired from the 'Nuclear Pizza War' source
	 * 
	 * @param one The first Entity (usually the Entity that is tracking)
	 * @param two The second Entity (The Target)
	 * @return The square root distance between the entities
	 */
	public static float distanceToSqrt2D(IEntity one, IEntity two) {
		final float dX = one.getPosition().x - two.getPosition().x;
		final float dZ = one.getPosition().z - two.getPosition().z;
		return ((dX * dX) + (dZ * dZ));
	}
	/**
	 * Returns the square root distance between two Entities on a 3D plane
	 * 
	 @param one The first Entity (usually the Entity that is tracking)
	 * @param two The second Entity (The Target)
	 * @return The square root distance between the entities
	 */
	public static float distanceToSqrt3D(IEntity one, IEntity two) {
		final float dX = one.getPosition().x - two.getPosition().x;
		final float dY = one.getPosition().y - two.getPosition().y;
		final float dZ = one.getPosition().z - two.getPosition().z;
		return ((dX * dX) + (dY * dY) + (dZ * dZ));
	}
	
	/**
	 * Checks to see if two entities Bounding Boxes are colliding with one another on a 2D plane
	 * 
	 * @param one The First Entity
	 * @param two The Second Entity
	 * @return true if the two Entities are colliding, else otherwise
	 */
	public static boolean collides2D(IEntity one, IEntity two) {
		//TODO: this
		return false;
	}
	
	
}
