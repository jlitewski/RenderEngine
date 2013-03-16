package com.hackhalo2.rendering.interfaces.core;

import com.hackhalo2.rendering.CameraManager;
import com.hackhalo2.rendering.KeyboardBuffer;
import com.hackhalo2.rendering.RenderEngine;
import com.hackhalo2.rendering.interfaces.entity.IEntityTracker;

public interface IChassis {
	
	//Sound System
	public void setSoundSystem(ISoundSystem soundSystem);
	public ISoundSystem getSoundSystem();
	
	//Entity System
	public void setEntityManager(IEntityManager<? extends IEntityTracker> entityManager);
	public IEntityManager<? extends IEntityTracker> getEntityManager();
	
	//Render System and Sub-systems
	public RenderEngine getRenderEngine();
	public CameraManager getCameraManager();
	public KeyboardBuffer getKeyboardBuffer();
	
	//Network System
	public void setNetworkManager(INetworkManager networkManager);
	public INetworkManager getNetworkManager();
	
	//Settings System
	public void setSettingsManager(ISettingsManager settingsManager);
	public ISettingsManager getSettingsManager();
	
	//Threading System
	public void setThreadManager(IThreadManager threadManager);
	public IThreadManager getThreadManager();
	
	//initialize
	public void initialize();
	
	//cleanup
	public void cleanup();
	
}
