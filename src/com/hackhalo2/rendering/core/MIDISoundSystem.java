package com.hackhalo2.rendering.core;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.util.vector.Vector3f;

import com.hackhalo2.rendering.interfaces.ISoundSystem;
import com.paulscode.sound.FilenameURL;
import com.paulscode.sound.SoundSystem;
import com.paulscode.sound.SoundSystemConfig;

/**
 * The core MIDI SoundSystem provided with the RenderEngine.<br />
 * <br />
 * This class uses Paul's SoundSystem with no plug-ins, codecs, or libraries internally.<br />
 * This class should be used as a reference for any other implementations of the {@link ISoundSystem}<br />
 * interface, as it follows the API correctly.
 * 
 * @author Jacob "HACKhalo2" Litewski
 */
public class MIDISoundSystem implements ISoundSystem {

	/**
	 * The API Version of the implementation
	 */
	public final int apiVersion = 1;

	private final int midiChannels = 8; //The maximum amount of MIDI Channels supported

	private SoundSystem soundSystem = null; //The SoundSystem reference
	private Map<Integer, FilenameURL> soundMap = null; //The unique sound ID map
	private Map<Integer, Float> mutedMap = null; //The muted map

	public MIDISoundSystem() {
		this.soundMap = new TreeMap<Integer, FilenameURL>();
		this.mutedMap = new TreeMap<Integer, Float>();
	}

	@Override
	public void initialize() {
		this.soundSystem = new SoundSystem();
	}

	@Override
	public int queue(URI soundPath, String filename, boolean isLooped, boolean priority) {
		FilenameURL url = null;
		try {
			url = new FilenameURL(soundPath.toURL(), filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return -1;
		}
		
		for(int i = 0; i < this.midiChannels; i++) {
			if(!this.soundMap.containsKey(i)) {
				this.soundMap.put(i, url);
				this.soundSystem.loadSound(url.getURL(), url.getFilename()); //Load the sound into memory
				this.soundSystem.newStreamingSource(priority, url.getFilename(), url.getURL(), url.getFilename(),
						isLooped, 0, 0, 0, SoundSystemConfig.ATTENUATION_ROLLOFF, 0.9f); //Set it as a streaming source
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public void dequeue(int soundID) {
		if(this.soundMap.containsKey(soundID)) {
			this.soundSystem.unloadSound(this.soundMap.get(soundID).getFilename());
			this.soundMap.remove(soundID);
		}
	}

	@Override
	public boolean isLooping(int soundID) {
		//Loop Detection isn't supported natively in Paul's SoundSystem
		return false;
	}

	@Override
	public boolean play(int soundID) {
		if(this.soundMap.containsKey(soundID)) {
			if(this.isPlaying(soundID)) this.stop(soundID);
			
			this.soundSystem.play(this.soundMap.get(soundID).getFilename());
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isPlaying(int soundID) {
		return this.soundSystem.playing(this.soundMap.get(soundID).getFilename());
	}

	@Override
	public byte pause(int soundID) {
		if(this.soundMap.containsKey(soundID)) {
			if(this.isPlaying(soundID)) {
				this.soundSystem.pause(this.soundMap.get(soundID).getFilename());
				return 1;
			} else return 0;
		}
		
		return -1;
	}

	@Override
	public boolean isPaused(int soundID) {
		return !this.soundSystem.playing(this.soundMap.get(soundID).getFilename());
	}

	@Override
	public byte resume(int soundID) {
		if(this.soundMap.containsKey(soundID)) {
			if(this.isPaused(soundID)) {
				this.soundSystem.play(this.soundMap.get(soundID).getFilename());
				return 1;
			} else return 0;
		}
		
		return -1;
	}

	@Override
	public byte mute(int soundID) {
		if(this.soundMap.containsKey(soundID)) {
			if(this.isMuted(soundID)) {
				float volume = this.soundSystem.getVolume(this.soundMap.get(soundID).getFilename());
				this.mutedMap.put(soundID, volume);
				this.soundSystem.setVolume(this.soundMap.get(soundID).getFilename(), 0f);
				return 1;
			} return 0;
		}
		
		return -1;
	}

	@Override
	public boolean isMuted(int soundID) {
		return (this.getVolume(soundID) == 0 && this.mutedMap.containsKey(soundID));
	}

	@Override
	public byte unmute(int soundID) {
		if(this.soundMap.containsKey(soundID)) {
			if(this.isPlaying(soundID) && this.isMuted(soundID)) {
				float previousVolume = this.mutedMap.get(soundID);
				this.setVolumeChecked(soundID, previousVolume);
				this.mutedMap.remove(soundID);
				
				return 1;
			} else {
				return 0;
			}
		} else {
			return -1;
		}
	}

	@Override
	public int getVolume(int soundID) {
		if(this.soundMap.containsKey(soundID)) {
			float volume = this.soundSystem.getVolume(this.soundMap.get(soundID).getFilename());
			int convertedVolume = ((int)(volume*100));

			return convertedVolume;
		} else  {
			return -1;
		}
	}
	
	private void setVolumeChecked(int soundID, float volume) {
		this.soundSystem.setVolume(this.soundMap.get(soundID).getFilename(), volume);
	}

	@Override
	public void setVolume(int soundID, int volume) {
		//Clamp the volume between 0-100
		if(volume > 100) volume = 100;
		if(volume < 0) volume = 0;

		//Convert it to a float between 0.0f and 1.0f
		float convertedVolume = ((float)(volume/100));

		//Set the volume
		if(this.soundMap.containsKey(soundID)) {
			this.setVolumeChecked(soundID, convertedVolume);
		}
	}

	@Override
	public void stop() {
		//Stop the first playing sound
		for(int i = 0; i < this.midiChannels; i++) {
			if(this.soundMap.containsKey(i)) {
				if(this.isPlaying(i)) {
					this.soundSystem.stop(this.soundMap.get(i).getFilename());
					break;
				}
			}
		}
	}

	@Override
	public void stop(int soundID) {
		if(this.soundMap.containsKey(soundID)) {
			this.soundSystem.stop(this.soundMap.get(soundID).getFilename());
		}
	}
	
	@Override
	public boolean setViewerPosition(Vector3f position) {
		this.soundSystem.setListenerPosition(position.x, position.y, position.z);
		return true;
	}

	@Override
	public void cleanup() {
		//Force stop all the sounds and remove them from the soundMap
		for(int i = 0; i < this.midiChannels; i++) this.stop(i);

		this.soundSystem.cleanup(); //Clean up the underlying SoundSystem

		//Null out all the class variables
		this.soundMap = null;
		this.mutedMap = null;
		this.soundSystem = null;
	}

}
