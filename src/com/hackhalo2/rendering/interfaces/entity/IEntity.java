package com.hackhalo2.rendering.interfaces.entity;

import org.lwjgl.util.vector.Vector3f;

import com.hackhalo2.rendering.interfaces.core.IChassis;

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
	 * 
	 * @param ueid The UEID this Entity should have
	 */
	public void setUEID(final int ueid);
	
	/**
	 * Gets the position of this Entity. Even though the Entity may not be in 3D space, a Vector3f is used.
	 * 
	 * @return The position of the Entity as a Vector3f
	 */
	public Vector3f getPosition();
	
	/**
	 * Sets the Position of the Entity. Even though the Entity may not be in 3D space, a Vector3f is used.
	 * 
	 * @param position The Position to set this Entity to.
	 */
	public void setPosition(final Vector3f position);
	
	/**
	 * Gets the name of this Entity.
	 * 
	 * @return the Entity name
	 */
	public String getName();
	
	/**
	 * Gets the Height of the Entity.
	 * 
	 * @return the Entity Height
	 */
	public float getHeight();
	
	/**
	 * Gets the Width of the Entity.
	 * 
	 * @return the Entity Width
	 */
	public float getWidth();
	
	
	/**
	 * Gets the Length of the Entity
	 * 
	 * @return the Entity Length
	 */
	public float getLength();
	
	/**
	 * Checks to see if the Entity is dead or not
	 * 
	 * @return true if the entity is dead, false otherwise
	 */
	public boolean isDead();
	
	/**
	 * Tick (update) the Entity.
	 * 
	 * @param chassis The Chassis framework, defined at game initialization
	 */
	abstract void tick(final IChassis chassis);

}
