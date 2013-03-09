package com.hackhalo2.rendering.builtin;

import com.hackhalo2.rendering.interfaces.core.IEntityManager;
import com.hackhalo2.rendering.interfaces.core.IManager;

public class BasicEntityManager implements IManager, IEntityManager<BasicEntityTracker> {

	@Override
	public BasicEntityTracker getEntityTracker() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasicEntityTracker getEntityTracker(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addEntityTracker(BasicEntityTracker tracker, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEntityTracker(String name) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

}
