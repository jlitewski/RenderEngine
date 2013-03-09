package com.hackhalo2.rendering.interfaces.entity;

import org.lwjgl.util.vector.Vector3f;

public interface IEntity {
	
	/**
	 * Get the Unique Entity ID from the Entity. This ID will never be the same for any <b><i>current</i></b> 
	 * Entity, but the ID may be reused if the Entity is destroyed. 
	 * 
	 * @return The Unique Entity ID
	 */
	public int getUEID();
	
	/**
	 * Get the position of this Entity. 
	 * 
	 * @return
	 */
	public Vector3f getPosition();

}
