package com.hackhalo2.rendering.builtin.entity;

import java.util.Collection;

import com.hackhalo2.rendering.interfaces.entity.IEntity;
import com.hackhalo2.rendering.interfaces.entity.IEntityTracker;

public class BasicEntityTracker implements IEntityTracker {

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<IEntity> getEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IEntity> getEntitiesByClass(Class<? extends IEntity> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
