package com.hackhalo2.rendering.builtin.entity;

import org.lwjgl.util.vector.Vector3f;

import com.hackhalo2.rendering.interfaces.entity.IEntity;

public class BasicEntity implements IEntity {
	
	private String entityName = "Entity";
	private int ueid = -1; //-1 is "no UEID"
	
	public BasicEntity() { }

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.entityName;
	}
	
	protected void setName(String name) {
		this.entityName = name;
	}

}
