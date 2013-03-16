package com.hackhalo2.rendering.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.RenderUtils.RefreshReason;
import com.hackhalo2.rendering.interfaces.core.IChassis;
import com.hackhalo2.rendering.interfaces.core.IEntityManager;
import com.hackhalo2.rendering.interfaces.core.IManager;
import com.hackhalo2.rendering.interfaces.core.IPluggable;
import com.hackhalo2.rendering.interfaces.entity.IEntity;

public class BasicEntityManager implements IManager, IEntityManager<BasicEntityTracker>, IPluggable {

	//The Tracker. Obviously.
	private BasicEntityTracker defaultTracker;

	//The List of past used IDs.
	public static final List<Integer> recycledIDs = new ArrayList<Integer>();
	
	private final String name = "BasicEntityManager";
	

	public BasicEntityManager() {
		this.defaultTracker = new BasicEntityTracker();
	}

	@Override
	public BasicEntityTracker getEntityTracker() {
		return this.defaultTracker;
	}

	@Override
	public BasicEntityTracker getEntityTracker(String name) {
		// Since we only have one tracker, return that one
		return this.defaultTracker;
	}

	@Override
	public boolean addEntityTracker(BasicEntityTracker tracker, String name) {
		//We Don't allow more then one Tracker, so return false
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
			//Assume the ID list is always dirty
			Collections.sort(recycledIDs);

			int id = recycledIDs.get(0);
			recycledIDs.remove(0);
			return id;
		}
	}

	@Override
	public void preLogic(IChassis chassis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preRender(IChassis chassis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(IChassis chassis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postRender(IChassis chassis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postLogic(IChassis chassis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void idleRender(IChassis chassis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(IChassis chassis, RefreshReason reason) { } //Ignore for now

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Priority getPriority() {
		return Priority.NORMAL;
	}

	@Override
	public void setEnabled(boolean flag) {
		//Do Nothing, you can't disable this
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
