package com.hackhalo2.rendering.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.hackhalo2.rendering.interfaces.entity.IEntity;
import com.hackhalo2.rendering.interfaces.entity.IEntityTracker;

public class BasicEntityTracker implements IEntityTracker {
	
	private List<IEntity> trackingEntities;
	
	public BasicEntityTracker() {
		this.trackingEntities = new ArrayList<IEntity>();
	}

	@Override
	public boolean addEntityTracking(IEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEntityTracking(IEntity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateTrackedEntities() {
		List<IEntity> remove = new ArrayList<IEntity>();
		for(IEntity entity : this.trackingEntities) {
			//Death check
			if(entity.isDead()) {
				remove.add(entity);
				continue;
			}
			
			//TODO: this
		}
	}

	@Override
	public Collection<IEntity> getEntities() {
		return this.trackingEntities;
	}

	@Override
	public Collection<IEntity> getEntitiesByClass(Class<? extends IEntity> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
