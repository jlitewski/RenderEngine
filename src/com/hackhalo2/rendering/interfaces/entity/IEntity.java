package com.hackhalo2.rendering.interfaces.entity;

import org.lwjgl.util.vector.Vector3f;

public interface IEntity {
	
	/**
	 * Gets the Unique Entity ID (or UEID) from the Entity. This ID will never be the same for any <b><i>current</i></b> 
	 * Entity, but the ID may be reused if the Entity is destroyed. 
	 * 
	 * @return The Unique Entity ID
	 */
	public int getUEID();
	
	/**
	 * Sets the UEID of the Entity.
	 * <br />
	 * NOTE: This should not be called ANYWHERE other then the Tracker this entity is a part of. Changing the 
	 * UEID after the Tracker has set it will translate your documents into Swahilli, make your TV record 
	 * Gigg-Li, neuter your pets and give your laundry static cling. Don't say I didn't warn you.
	 */
	public void setUEID(final int ueid);
	
	/**
	 * Gets the position of this Entity. Event though the Entity may not be in 3D space, a Vector3f is used 
	 * for ease of use. 
	 * 
	 * @return the position of the Entity as a Vector3f
	 */
	public Vector3f getPosition();
	
	/**
	 * Gets the name of this Entity.
	 * 
	 * @return the Entity name
	 */
	public String getName();

}
