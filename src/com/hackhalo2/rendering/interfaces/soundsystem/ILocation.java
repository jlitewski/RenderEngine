package com.hackhalo2.rendering.interfaces.soundsystem;

import org.lwjgl.util.vector.Vector3f;

/**
 * The Location API for the SoundSystem. This class was modeled after Paul's SoundSystem API,
 * but it should be vague enough to plug into any other Sound API.
 * 
 * @author Jacob "HACKhalo2" Litewski
 *
 */
public interface ILocation {
	
	/**
	 * Location API Version
	 */
	public final int _locationAPIVersion = 1;
	
	/**
	 * Sets the Viewers position to the underlying SoundSystem
	 * @param position The LWJGL Vector of the listener's position
	 */
	public void setListenerPosition(Vector3f position);
	
	/**
	 * Sets the Viewers Camera Angle to the underlying SoundSystem
	 * @param angle The LWJGL Vector of the listener's camera angle
	 */
	public void setListenerAngle(Vector3f angle);
	
	/**
	 * Sets the Viewers Look and Up Orientations to the underlying SoundSystem.<br />
	 * Note: The Vector's may need to be normalized before passing them into the SoundSystem.<br />
	 * Please check with the Implementation to see if they need to be.
	 * @param look The LWJGL Vector of the listener's camera's look angle
	 * @param up The LWJGL Vector of the listener's camera's up angle
	 */
	public void setListenerOrientation(Vector3f look, Vector3f up);
	
	/**
	 * 
	 * @param velocity 
	 */
	public void setListenerVelocity(Vector3f velocity);
}
