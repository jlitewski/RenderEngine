package com.hackhalo2.rendering.entity;

import org.lwjgl.util.vector.Vector3f;

import com.hackhalo2.rendering.interfaces.entity.IEntity;

public abstract class BasicEntity implements IEntity {
	
	private String entityName = "Entity";
	private int ueid = -1; //-1 is "no UEID"
	private boolean dead;
	
	/*
	 * Now, we use a Vector3f as a standard, even if the Entity isn't a 3D one. The reasoning behind this is
	 * simple: we could use the unused variable as a couple of bytes of data storage.
	 */
	private Vector3f position = new Vector3f(0,0,0);
	
	public BasicEntity() {
		this.dead = false;
	}

	@Override
	public int getUEID() {
		return this.ueid;
	}
	
	@Override
	public void setUEID(int ueid) {
		//Built in check to make sure no rouge code tries to fuck with my entities
		if(this.ueid == -1) this.ueid = ueid;
	}

	@Override
	public Vector3f getPosition() {
		return this.position;
	}
	
	@Override
	public void setPosition(Vector3f position) {
		
	}
	
	@Override
	public boolean isDead() {
		return this.dead;
	}

	@Override
	public String getName() {
		return this.entityName;
	}
	
	protected void setName(String name) {
		this.entityName = name;
	}

}
