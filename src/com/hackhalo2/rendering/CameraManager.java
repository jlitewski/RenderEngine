package com.hackhalo2.rendering;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.glu.GLU;

import com.hackhalo2.rendering.RenderEngine.PlugMode.Priority;
import com.hackhalo2.rendering.RenderUtils.RefreshReason;
import com.hackhalo2.rendering.camera.GUIElement;
import com.hackhalo2.rendering.interfaces.annotations.Execute;
import com.hackhalo2.rendering.interfaces.camera.ICamera;
import com.hackhalo2.rendering.interfaces.core.IChassis;
import com.hackhalo2.rendering.interfaces.core.IManager;
import com.hackhalo2.rendering.interfaces.core.IPluggable;
import com.hackhalo2.rendering.interfaces.sound.IListenerLocation;
import com.hackhalo2.rendering.util.VBOContainer;
import com.hackhalo2.rendering.util.VBOContainer.ContainerType;

public class CameraManager implements IManager, IPluggable {

	private ICamera camera;
	private Map<String, ICamera> secondary;
	private Map<String, GUIElement> guiElements;
	private boolean hudEnabled = true;
	private final Priority priority = Priority.LOWEST;
	private boolean enabled = true;
	private RenderLogger log = new RenderLogger();

	public CameraManager() {
		this.secondary = new HashMap<String, ICamera>();
		this.guiElements = new HashMap<String, GUIElement>();
	}

	/* Register Events */
	public void register(ICamera camera) {
		if(this.camera == null) {
			this.camera = camera;
			this.log.debug("camera", "Registered new camera: "+camera.getName(), 0);
		} else {
			this.secondary.put(camera.getName(), camera);
			this.log.debug("camera", "Registered new secondary camera: "+camera.getName(), 0);
		}
	}

	public void register(GUIElement element) {
		if(!this.guiElements.containsKey(element.getName())) {
			this.guiElements.put(element.getName(), element);
		}
	}

	public void remove(GUIElement element) {
		this.removeElementByName(element.getName());
	}

	public void removeElementByName(String name) {
		if(this.guiElements.containsKey(name)) {
			this.guiElements.remove(name);
		}
	}

	/* Other Functions */
	public void switchCamera(ICamera camera) {
		//Safety check
		if(this.secondary.containsKey(camera.getName())) {
			this.secondary.remove(camera.getName());
		}

		ICamera temp = this.camera;
		this.camera = camera;
		this.camera.setPosition(temp.getPosition());
		this.secondary.put(temp.getName(), temp);
	}

	public void switchCamera(String name) {
		if(this.secondary.containsKey(name)) {
			this.switchCamera(this.secondary.get(name));
		}
	}

	public ICamera getActiveCamera() {
		return this.camera;
	}
	
	@Override
	public void setEnabled(final boolean flag) {
		this.enabled  = flag;
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	@Execute
	public void preLogic(IChassis chassis) {
		if(this.camera != null) this.camera.generateMatrices(); //Generate the primary camera matrices

		Iterator<GUIElement> it = this.guiElements.values().iterator();
		while(it.hasNext()) {
			GUIElement element = it.next();
			if(element.isActive()) element.preLogic(chassis);
		}
	}

	@Override
	@Execute
	public void preRender(IChassis chassis) {
		//Generate the VBO's for the GUI Elements
		Iterator<GUIElement> it = this.guiElements.values().iterator();
		while(it.hasNext()) {
			GUIElement element = it.next();
			if(element.isDirty() && element.isActive()) element.preRender(chassis);
		}
	}

	@Override
	@Execute
	public void render(IChassis chassis) {
		RenderEngine re = chassis.getRenderEngine();
		//Render the Elements
		//This only works if Vertices are enabled (aka if Rendering is enabled) and the HUD is enabled
		if(re.getVertexEnabled() && this.hudEnabled) {

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluOrtho2D(0f, Display.getWidth(), Display.getHeight(), 0f);

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			final boolean tex = GL11.glGetBoolean(GL11.GL_TEXTURE_2D); //Textures enabled
			final boolean ble = GL11.glGetBoolean(GL11.GL_BLEND); //Blending enabled

			re.setDepthTestingEnabled(false);
			re.setCullingEnabled(false);
			if(!tex) GL11.glEnable(GL11.GL_TEXTURE_2D); //Enable Textures
			if(!(ble && re.isWireframeEnabled())) { //Enable Alpha Blending
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			Iterator<GUIElement> it1 = this.guiElements.values().iterator();
			while(it1.hasNext()) {
				GUIElement element = it1.next();
				if(element.isActive()) {
					Set<VBOContainer> vbos = element.getVBOs();
					if(!vbos.isEmpty()) { //npe check
						Iterator<VBOContainer> it2 = vbos.iterator();
						while(it2.hasNext()) {
							VBOContainer vbo = it2.next();
							GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo.getHandle());
							switch(vbo.getType()) {
							case NORMAL:
								if(vbo.getType() != ContainerType.NORMAL) throw new RuntimeException("VBO type mismatch! (Expected NORMAL, got "+ vbo.getType().toString()+")");
								GL11.glNormalPointer(GL11.GL_FLOAT, vbo.getStride(0), 0L);
								break;
							case COLOR:
								if(vbo.getType() != ContainerType.COLOR) throw new RuntimeException("VBO type mismatch! (Expected COLOR, got "+ vbo.getType().toString()+")");
								GL11.glColorPointer(vbo.getSize(), GL11.GL_FLOAT, vbo.getStride(0), 0L);
								break;
							case VERTEX:
								if(vbo.getType() != ContainerType.VERTEX) throw new RuntimeException("VBO type mismatch! (Expected VERTEX, got "+ vbo.getType().toString()+")");
								GL11.glVertexPointer(vbo.getSize(), GL11.GL_FLOAT, vbo.getStride(0), 0L);
								break;
							default:
								throw new RuntimeException("Undefined or unsupported vbo type!");
							}
							GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
						}

						GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
						if(re.getColorEnabled()) GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
						if(re.getNormalEnabled()) GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);

						element.render(chassis);
						RenderUtils.checkGLErrors();

						if(re.getNormalEnabled()) GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
						if(re.getColorEnabled()) GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
						GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
					}
				}
			}

			//Reset the Matrix back to 3D
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluPerspective(this.camera.getFOV(), this.camera.getAspectRatio(), this.camera.getZNear(), this.camera.getZFar());
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();

			//TODO: Have a RenderEngine GLState keep track of this
			re.setDepthTestingEnabled(true);
			re.setCullingEnabled(true);
			if(!tex) GL11.glDisable(GL11.GL_TEXTURE_2D); //Disable Textures
			if(!(ble && re.isWireframeEnabled())) GL11.glDisable(GL11.GL_BLEND);

		}
	}

	@Override
	@Execute
	public void postRender(IChassis chassis) {
		Iterator<GUIElement> it = this.guiElements.values().iterator();
		while(it.hasNext()) {
			GUIElement element = it.next();
			element.postRender(chassis);
		}
	}

	@Override
	@Execute
	public void idleRender(IChassis chassis) {
		if(chassis.getSoundSystem() instanceof IListenerLocation) {
			((IListenerLocation)(chassis.getSoundSystem())).setListenerPosition(this.camera.getPosition());
		}

		Iterator<GUIElement> it = this.guiElements.values().iterator();
		while(it.hasNext()) {
			GUIElement element = it.next();
			if(element.isActive()) element.idleRender(chassis);
		}
	}

	@Override
	@Execute
	public void postLogic(IChassis chassis) {
		if(!GL11.glGetBoolean(GL11.GL_TEXTURE_2D)) GL11.glDisable(GL11.GL_TEXTURE_2D);
		if(this.camera != null) this.camera.applyMatrices();

		Iterator<GUIElement> it = this.guiElements.values().iterator();
		while(it.hasNext()) {
			GUIElement element = it.next();
			if(element.isActive()) element.postLogic(chassis);
		}
	}

	@Override
	public void refresh(IChassis chassis, RefreshReason reason) {
		switch(reason) {
		case DISPLAY_RESIZED:
			Iterator<GUIElement> i = this.guiElements.values().iterator();
			while(i.hasNext()) {
				GUIElement element = i.next();
				element.clearVBOs();
				element.setDirty(true);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public Priority getPriority() {
		return this.priority;
	}

	public boolean isHudEnabled() {
		return this.hudEnabled;
	}

	public void setHudEnabled(boolean hudEnabled) {
		this.hudEnabled = hudEnabled;
	}

	@Override
	public void cleanup() {
		this.camera = null;
		this.guiElements = null;
		this.secondary = null;
	}

	@Override
	public String getName() {
		return "CameraManager";
	}

}
