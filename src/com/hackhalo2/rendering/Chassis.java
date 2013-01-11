package com.hackhalo2.rendering;

import com.hackhalo2.rendering.builtin.MIDISoundSystem;
import com.hackhalo2.rendering.interfaces.core.IChassis;
import com.hackhalo2.rendering.interfaces.core.IEntityManager;
import com.hackhalo2.rendering.interfaces.core.IManager;
import com.hackhalo2.rendering.interfaces.core.INetworkManager;
import com.hackhalo2.rendering.interfaces.core.ISettingsManager;
import com.hackhalo2.rendering.interfaces.core.ISoundSystem;
import com.hackhalo2.rendering.interfaces.core.IThreadManager;

public class Chassis implements IChassis {
	
	//The customizable systems
	private ISoundSystem soundSystem = null;
	private IEntityManager entityManager = null;
	private INetworkManager networkManager = null;
	private ISettingsManager settingsManager = null;
	private IThreadManager threadManager = null;
	
	//The built in systems
	private RenderEngine renderEngine = null;
	private CameraManager cameraManager = null;
	private KeyboardBuffer keyboardBuffer = null;
	
	//the finalized boolean
	private boolean finalized = false;

	public Chassis() {
		this.cameraManager = new CameraManager();
		this.keyboardBuffer = new KeyboardBuffer();
	}

	@Override
	public void setSoundSystem(ISoundSystem soundSystem) {
		if(!this.finalized) this.soundSystem = soundSystem;
		else throw new UnsupportedOperationException("The Chassis has been finalized, you cannot swap systems now");
	}

	@Override
	public ISoundSystem getSoundSystem() {
		return this.soundSystem;
	}

	@Override
	public void setEntityManager(IEntityManager entityManager) {
		if(!this.finalized) this.entityManager = entityManager;
		else throw new UnsupportedOperationException("The Chassis has been finalized, you cannot swap systems now");
	}

	@Override
	public IEntityManager getEntityManager() {
		return this.entityManager;
	}

	@Override
	public RenderEngine getRenderEngine() {
		return this.renderEngine;
	}

	@Override
	public CameraManager getCameraManager() {
		return this.cameraManager;
	}

	@Override
	public KeyboardBuffer getKeyboardBuffer() {
		return this.keyboardBuffer;
	}

	@Override
	public void setNetworkManager(INetworkManager networkManager) {
		if(!this.finalized) this.networkManager = networkManager;
		else throw new UnsupportedOperationException("The Chassis has been finalized, you cannot swap systems now");
	}

	@Override
	public INetworkManager getNetworkManager() {
		return this.networkManager;
	}

	@Override
	public void setSettingsManager(ISettingsManager settingsManager) {
		if(!this.finalized) this.settingsManager = settingsManager;
		else throw new UnsupportedOperationException("The Chassis has been finalized, you cannot swap systems now");
	}

	@Override
	public ISettingsManager getSettingsManager() {
		return this.settingsManager;
	}
	
	@Override
	public void setThreadManager(IThreadManager threadManager) {
		if(!this.finalized) this.threadManager = threadManager;
		else throw new UnsupportedOperationException("The Chassis has been finalized, you cannot swap systems now");
	}

	@Override
	public IThreadManager getThreadManager() {
		return this.threadManager;
	}

	@Override
	public void initialize() {
		//Finalize the Chassis
		this.finalized = true;
		//TODO initialize any null objects here with built in implementations
		
		//Initialize the SoundSystem
		if(this.soundSystem == null) this.soundSystem = new MIDISoundSystem(); //Fallback if no SoundSystem was supplied
		this.soundSystem.initialize();
		
		//Instance the RenderEngine
		this.renderEngine = new RenderEngine(this);
		
		//Register things with the RenderEngine
		this.renderEngine.register(this.cameraManager);
		this.renderEngine.register(this.keyboardBuffer);
	}

	@Override
	public void cleanup() {
		// TODO Add cleanup methods to all the underlying interfaces and call it here
		((IManager)this.soundSystem).cleanup();
		this.keyboardBuffer.cleanup();
		this.cameraManager.cleanup();
	}

}
