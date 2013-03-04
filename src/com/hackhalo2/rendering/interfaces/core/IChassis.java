package com.hackhalo2.rendering.interfaces.core;

import com.hackhalo2.rendering.CameraManager;
import com.hackhalo2.rendering.EventBus;
import com.hackhalo2.rendering.KeyboardBuffer;
import com.hackhalo2.rendering.RenderEngine;

public interface IChassis {
	
	//Sound System
	public void setSoundSystem(ISoundSystem soundSystem);
	public ISoundSystem getSoundSystem();
	
	//Entity System
	public void setEntityManager(IEntityManager entityManager);
	public IEntityManager getEntityManager();
	
	//Render System and Sub-systems
	public RenderEngine getRenderEngine();
	public CameraManager getCameraManager();
	public KeyboardBuffer getKeyboardBuffer();
	
	//Event System
	public EventBus getEventBus();
	
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
