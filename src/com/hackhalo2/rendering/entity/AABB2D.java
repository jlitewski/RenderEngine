package com.hackhalo2.rendering.entity;

import org.lwjgl.util.vector.Vector2f;

/**
 * This is a modular Axis Aligned Bounding Box Implementation
 * 
 * @author Jacob "HACKhalo2" Litewski
 */
public class AABB2D {
	
	protected Vector2f max; //The Top-Left set of coords
	protected Vector2f min; //The Bottom-Right set of coords
	
	/**
	 * Constructs an Axis Aligned Bounding Box with two Vector2f's<br />
	 * This constructor assumes that you know the coords you want for an AABB
	 * 
	 * @param min The Bottom Right set of coords
	 * @param max The Upper Left set of coords
	 */
	public AABB2D(final Vector2f max, final Vector2f min) {
		//Initialize the internal Vectors this way as a fail safe if the Vector is used somewhere else.
		//If it was initialized as this.max = max, and max was changed, the internal Vector would also change,
		//Producing undefined results
		this.max = new Vector2f(max);
		this.min = new Vector2f(min);
	}
	
	/**
	 * Constructs an Axis Aligned Bounding Box with four floats<br />
	 * This Constructor assumes that you know the coords you want for an AABB
	 * 
	 * @param minX the Bottom Right X
	 * @param minZ the Bottom Right Z
	 * @param maxX the Upper Right X
	 * @param maxZ the Upper Right Z
	 */
	public AABB2D(final float maxX, final float maxZ, final float minX, final float minZ) {
		this.max = new Vector2f(maxX, maxZ);
		this.min = new Vector2f(minX, minZ);
	}
	
	/**
	 * Sets the AABB Bounds from the passed in floats, and returns it
	 * 
	 * @param minX the Bottom Right X
	 * @param minZ the Bottom Right Z
	 * @param maxX the Upper Right X
	 * @param maxZ the Upper Right Z
	 * @return the AABB
	 */
	public AABB2D setBounds(final float maxX, final float maxZ, final float minX, final float minZ) {
		this.max = new Vector2f(maxX, maxZ);
		this.min = new Vector2f(minX, minZ);
		return this;
	}
	
	/**
	 * Sets the AABB Bounds from the passed in Vectors, and returns it
	 * 
	 * @param max the Upper Left coords
	 * @param min the Lower Right coords
	 * @return the AABB
	 */
	public AABB2D setBounds(final Vector2f max, final Vector2f min) {
		this.max = new Vector2f(max);
		this.min = new Vector2f(min);
		return this;
	}
	
	/**
	 * Sets the AABB bounds to the bounds of the passed in AABB, and returns it
	 * 
	 * @param box The AABB to copy
	 * @return the AABB
	 */
	public AABB2D setBounds(final AABB2D box) {
		this.max = new Vector2f(box.max);
		this.min = new Vector2f(box.min);
		return this;
	}
	
	@Override
	public String toString() {
		return "AABB2D["+this.max.x+", "+this.max.y+" -> "+this.min.x+", "+this.min.y+"]";
	}
}
