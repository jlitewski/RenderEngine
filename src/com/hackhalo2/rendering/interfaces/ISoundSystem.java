package com.hackhalo2.rendering.interfaces;

import java.net.URI;

import org.lwjgl.util.vector.Vector3f;

public interface ISoundSystem {

	/**
	 * ISoundSystem API version.<br />
	 * This should not to be confused with the per-implementation API version.
	 */
	public final int _coreAPIVersion = 1;

	/**
	 * The minimum supported ISoundSystem API version this API version supports.<br />
	 * This should not to be confused with the minimum per-implementation API version.
	 */
	public final int _minimumSupportedCoreAPIVersion = 1;

	/**
	 * Initializes the implementation (if it needs to be). This function should be called in<br />
	 * any implementation of {@link IChassis#initialize()}, so it shouldn't be called outside of that.
	 */
	public void initialize();

	/**
	 * Queue the sound from the given URI.<br />
	 * <br />
	 * Depending on the implementation, this method may fail silently if an exception is thrown.<br />
	 * This method, however, will always return <b>-1</b> if it failed.<br />
	 * Please take any appropriate actions according to the implementation.
	 * 
	 * @param soundPath the path to the sound
	 * @param filename the filename of the sound (i.e. <i>"hit1.wav"</i>). Depending on the implementation, 
	 * you may or may not need to include the file extension
	 * @param isLooped the boolean if the sound should be looped or not
	 * @return the unique soundID, or <b>-1</b> for a failure
	 */
	public int queue(URI soundPath, String filename, boolean isLooped, boolean priority);

	/**
	 * Dequeue the sound from the given unique sound ID.<br />
	 * Depending on the implementation, this method may fail silently if an exception is thrown.<br />
	 * Please take any appropriate actions according to the implementation.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 */
	public void dequeue(int soundID);

	/**
	 * Checks to see if the sound from the given unique sound ID is looping.<br />
	 * Some implementations do not support looping of sounds, so this will always<br />
	 * return <b>false</b>.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return <b>true</b> is the sound is looping, <b>false</b> otherwise
	 */
	public boolean isLooping(int soundID);

	/**
	 * Play the sound from the unique sound ID given.<br />
	 * Depending on the implementation, this method may fail silently if an exception is thrown.<br />
	 * This method, however, will always return <b>false</b> if it failed.<br />
	 * Please take any appropriate actions according to the implementation.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return <b>true</b> is the sound started playing, <b>false</b> otherwise
	 */
	public boolean play(int soundID);

	/**
	 * Check to see if the sound is currently playing. If the unique ID is not valid,<br />
	 * this function will return <b>false</b>.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return <b>true</b> if the sound is playing, <b>false</b> otherwise
	 */
	public boolean isPlaying(int soundID);

	/**
	 * Pause the sound by the given unique sound ID.<br />
	 * <br />
	 * Some implementations may not support the ability to pause sounds, so <b>0</b> will return.<br />
	 * If the supplied sound ID is invaild, <b>-1</b> will be returned.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return <b>1</b> if the sound was paused, <b>0</b> if it wasn't or if it's unsupported, and <b>-1</b><br />
	 * for invalid soundID.
	 */
	public byte pause(int soundID);

	/**
	 * Check to see if the sound from the given unique sound ID is paused. If the unique ID is not valid,<br />
	 * this function will return <b>false</b>.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return <b>true</b> if the sound is paused, <b>false</b> otherwise
	 */
	public boolean isPaused(int soundID);

	/**
	 * Resume a paused sound by the given unique sound ID. Some implementations may not support the ability<br />
	 * to pause sounds, so this will return <b>-1</b> if this is the case.<br />
	 * 
	 * @param soundID the unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return <b>1</b> if the sound was resumed, <b>0</b> if it wasn't or if is was currently playing, and<br />
	 * <b>-1</b> for unsupported function.
	 */
	public byte resume(int soundID);

	/**
	 * Mute the sound from the given unique sound ID. If the soundID was invalid,<br />
	 * this will return <b>-1</b>.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return <b>1</b> if the sound was muted, <b>0</b> if it wasn't, and <b>-1</b> for an invalid soundID.
	 */
	public byte mute(int soundID);

	/**
	 * Checks to see if the sound from the given unique sound ID. Some implementations may not support the<br />
	 * ability to mute sounds, so this will return <b>false</b> if this is the case.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return <b>true</b> if the sound is muted, <b>false</b> otherwise.
	 */
	public boolean isMuted(int soundID);

	/**
	 * Unmute the sound from the given unique sound ID. If the soundID was invalid,<br />
	 * this will return <b>-1</b>.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return <b>1</b> if the sound was unmuted, <b>0</b> if it wasn't, and <b>-1</b> for an invalid soundID.
	 */
	public byte unmute(int soundID);

	/**
	 * Get's the current volume of the sound given by the unique sound ID. 
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @return the current volume of the sound, or <b>-1</b> if the soundID was invalid
	 */
	public int getVolume(int soundID);

	/**
	 * Set the volume of the sound given by the unique sound ID. Please note that different implementations<br />
	 * will have different ways to clamp the minimum and maximum volume. Some implementations may throw<br />
	 * exceptions if the volume is out of bounds.
	 * 
	 * @param soundID The unique sound ID returned from {@link #queue(URI, boolean)}
	 * @param volume the new volume value the sound should have
	 */
	public void setVolume(int soundID, int volume);

	/**
	 * Stops the first playing sound. If there are no sounds playing when this function is called,<br />
	 * it fails silently.
	 */
	public void stop();

	/**
	 * Stop the sound with the unique ID given. If the unique ID is not valid, or the sound is not<br />
	 * playing, it fails silently.
	 * 
	 * @param soundID the unique sound ID returned from {@link #queue(URI, boolean)}
	 */
	public void stop(int soundID);

	/**
	 * Updates the camera's position for the underlying sound library.<br />
	 * Note that some implementations may not support this function, which <b>false</b> will be returned<br />
	 * or an exception thrown, dependng on the implementation.
	 * 
	 * @param position The xyz position of the viewer, using LWJGL's Vector3f class
	 * @return <b>true</b> if successful, <b>false</b> otherwise
	 */
	public boolean setViewerPosition(Vector3f position);

}
