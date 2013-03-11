package com.hackhalo2.rendering.builtin.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.hackhalo2.rendering.interfaces.core.IEntityManager;
import com.hackhalo2.rendering.interfaces.core.IManager;
import com.hackhalo2.rendering.interfaces.entity.IEntity;

public class BasicEntityManager implements IManager, IEntityManager<BasicEntityTracker> {

	//The Tracker. Obviously.
	private BasicEntityTracker defaultTracker;

	//The List of past used IDs.
	public static final List<Integer> recycledIDs = new ArrayList<Integer>();

	//The flag used to tell if the recycledIDs List is dirty
	private static boolean recycledListDirty = false;

	public BasicEntityManager() {
		this.defaultTracker = new BasicEntityTracker();
	}

	@Override
	public BasicEntityTracker getEntityTracker() {
		return this.defaultTracker;
	}

	@Override
	public BasicEntityTracker getEntityTracker(String name) {
		// Since we only have 
		return this.defaultTracker;
	}

	@Override
	public boolean addEntityTracker(BasicEntityTracker tracker, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEntityTracker(String name) {
		// This Entity Manager only contains one Entity Tracker, the Default, so it can never be removed
		return false;
	}

	@Override
	public Collection<IEntity> getAllEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IEntity> getAllEntitiesByClass(Class<? extends IEntity> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	public static int getEntityID() {
		if(recycledIDs.isEmpty()) return ids.getAndIncrement();
		else {
			if(recycledListDirty) {
				//If the recycledIDs List is "dirty" (ie recently added to), sort it into ascending order
				recycledListDirty = false;
				Collections.sort(recycledIDs);
			}

			int id = recycledIDs.get(0);
			recycledIDs.remove(0);
			return id;
		}
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
