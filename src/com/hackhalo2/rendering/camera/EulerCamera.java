package com.hackhalo2.rendering.camera;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBDepthClamp;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import com.hackhalo2.rendering.KeyboardBuffer;

import static java.lang.Math.*;

public class EulerCamera extends Camera {

	private Vector3f rotation;
	private float speed = 1.0f;
	private float movementSpeed = 0.003f;
	private float maxDown = -85f, maxUp = 85f;

	public EulerCamera(KeyboardBuffer kb, Vector3f position, Vector3f rotation) {
		super(kb);
		
		this.position = position;
		this.rotation = rotation;

		// Enable Depth clamping if supported
		if (GLContext.getCapabilities().GL_ARB_depth_clamp) {
			GL11.glEnable(ARBDepthClamp.GL_DEPTH_CLAMP);
		}

		//Instance the Mouse object if it does not already exist
		//TODO: Move this to the InputManager
		try {
			if(!Mouse.isCreated()) Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//Register the input keys
		this.registerKBInputs(new int[] {
			Keyboard.KEY_W,
			Keyboard.KEY_A,
			Keyboard.KEY_S,
			Keyboard.KEY_D,
			Keyboard.KEY_SPACE,
			Keyboard.KEY_LSHIFT,
			Keyboard.KEY_ESCAPE
		});
	}

	@Override
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	@Override
	public Vector3f getRotation() {
		return this.rotation;
	}

	private void moveFromLook(float dx, float dy, float dz) {
		this.position.z += dx * (float) cos(toRadians(this.rotation.x - 90)) + dz * cos(toRadians(this.rotation.x));
		this.position.x -= dx * (float) sin(toRadians(this.rotation.x - 90)) + dz * sin(toRadians(this.rotation.x));
		this.position.y += dy * (float) sin(toRadians(this.rotation.y - 90)) + dz * sin(toRadians(this.rotation.y));
	}

	@Override
	public String getName() {
		return "Euler";
	}

	@Override
	public void generateMatrices(int delta) {
		//Local Keyboard variables
		boolean keyUp     = this.kb.getState(Keyboard.KEY_W);
		boolean keyDown   = this.kb.getState(Keyboard.KEY_S);
		boolean keyLeft   = this.kb.getState(Keyboard.KEY_A);
		boolean keyRight  = this.kb.getState(Keyboard.KEY_D);
		boolean flyUp     = this.kb.getState(Keyboard.KEY_SPACE);
		boolean flyDown   = this.kb.getState(Keyboard.KEY_LSHIFT);
		boolean grabMouse = this.kb.getState(Keyboard.KEY_ESCAPE);

		//float yaw = this.rotation.getYaw(), pitch = this.rotation.getPitch();

		//Process Mouse Movements
		if(Mouse.isGrabbed()) { //Make sure the Mouse is grabbed before processing Mouse Events
			float mouseDX = Mouse.getDX() * this.speed * 0.16f;
			float mouseDY = Mouse.getDY() * this.speed * 0.16f;

			if (this.rotation.x + mouseDX >= 360) {
				this.rotation.x = this.rotation.x + mouseDX - 360;
			} else if (this.rotation.x + mouseDX < 0) {
				this.rotation.x = 360 - this.rotation.x + mouseDX;
			} else {
				this.rotation.x += mouseDX;
			}
			if (this.rotation.y - mouseDY >= this.maxDown
					&& this.rotation.y - mouseDY <= this.maxUp) {
				this.rotation.y += -mouseDY;
			} else if (this.rotation.y - mouseDY < this.maxDown) {
				this.rotation.y = this.maxDown;
			} else if (this.rotation.y - mouseDY > this.maxUp) {
				this.rotation.y = this.maxUp;
			}
		}

		//Process Keyboard Events
		if(keyUp && keyRight && !keyLeft && !keyDown) {
			this.moveFromLook(this.speed * delta * this.movementSpeed, 0, -this.speed * delta * this.movementSpeed);
		}
		if(keyUp && keyLeft && !keyRight && !keyDown) {
			this.moveFromLook(-this.speed * delta * this.movementSpeed, 0, -this.speed * delta * this.movementSpeed);
		}
		if(keyUp && !keyLeft && !keyRight && !keyDown) {
			this.moveFromLook(0, 0, -this.speed * delta * this.movementSpeed);
		}
		if(keyDown && keyLeft && !keyRight && !keyUp) {
			this.moveFromLook(-this.speed * delta * this.movementSpeed, 0, this.speed * delta * this.movementSpeed);
		}
		if(keyDown && keyRight && !keyLeft && !keyUp) {
			this.moveFromLook(this.speed * delta * this.movementSpeed, 0, this.speed * delta * this.movementSpeed);
		}
		if(keyDown && !keyUp && !keyLeft && !keyRight) {
			this.moveFromLook(0, 0, this.speed * delta * this.movementSpeed);
		}
		if(keyLeft && !keyRight && !keyUp && !keyDown) {
			this.moveFromLook(-this.speed * delta * this.movementSpeed, 0, 0);
		}
		if(keyRight && !keyLeft && !keyUp && !keyDown) {
			this.moveFromLook(this.speed * delta * this.movementSpeed, 0, 0);
		}
		if(flyUp && !flyDown) {
			this.position.setY(this.position.y + (this.speed * delta * this.movementSpeed));
		}
		if(flyDown && !flyUp) {
			this.position.setY(this.position.y - (this.speed * delta * this.movementSpeed));
		}

		if(grabMouse && !this.kb.getLastState(Keyboard.KEY_ESCAPE)) {
			if(Mouse.isInsideWindow()) { //Make use the mouse is inside the window before trying to grab it
				Mouse.setCursorPosition(Display.getWidth()/2, Display.getHeight()/2);
				Mouse.setGrabbed(!Mouse.isGrabbed());
				this.log.debug("camera_event", "Mouse grabbed: "+Mouse.isGrabbed(), 0);
			}
		}
	}

	@Override
	public void applyMatrices() {
		int previousMatrixMode = GL11.glGetInteger(GL11.GL_MATRIX_MODE);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glRotatef(this.rotation.y, 1, 0, 0);
		GL11.glRotatef(this.rotation.x, 0, 1, 0);
		GL11.glRotatef(this.rotation.z, 0, 0, 1);
		GL11.glTranslatef(-this.position.x, -this.position.y, -this.position.z);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(this.fov, this.aspectRatio, this.zNear, this.zFar);
		GL11.glMatrixMode(previousMatrixMode);
	}

}
