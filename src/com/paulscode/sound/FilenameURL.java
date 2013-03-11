package com.paulscode.sound;

import java.net.URL;

import com.hackhalo2.rendering.Dashboard;

/**
 * The FilenameURL class is designed to associate a String filename/identifier
 * with a URL. Handles either case where user supplies a String path or user
 * supplies a URL instance. <br>
 * <br>
 * <b><i> SoundSystem License:</b></i><br>
 * <b><br>
 * You are free to use this library for any purpose, commercial or otherwise.
 * You may modify this library or source code, and distribute it any way you
 * like, provided the following conditions are met: <br>
 * 1) You may not falsely claim to be the author of this library or any
 * unmodified portion of it. <br>
 * 2) You may not copyright this library or a modified version of it and then
 * sue me for copyright infringement. <br>
 * 3) If you modify the source code, you must clearly document the changes made
 * before redistributing the modified source code, so other users know it is not
 * the original code. <br>
 * 4) You are not required to give me credit for this library in any derived
 * work, but if you do, you must also mention my website:
 * http://www.paulscode.com <br>
 * 5) I the author will not be responsible for any damages (physical, financial,
 * or otherwise) caused by the use if this library or any part of it. <br>
 * 6) I the author do not guarantee, warrant, or make any representations,
 * either expressed or implied, regarding the use of this library or any part of
 * it. <br>
 * <br>
 * Author: Paul Lamb <br>
 * http://www.paulscode.com </b>
 */
public class FilenameURL {
	/**
	 * Processes status messages, warnings, and error messages.
	 */
	private Dashboard logger = SoundSystemConfig.getLogger();

	/**
	 * Filename or identifier for the file.
	 */
	private String filename = null;

	/**
	 * URL interface to the file.
	 */
	private URL url = null;

	/**
	 * Constructor: Saves handles to the url and identifier. The identifier
	 * should look like a filename, and it must have the correct extension so
	 * SoundSystem knows what format to use for the file referenced by the URL
	 * instance.
	 * 
	 * @param url
	 *            URL interface to a file.
	 * @param identifier
	 *            Identifier (filename) for the file.
	 */
	public FilenameURL(URL url, String identifier) {
		this.filename = identifier;
		this.url = url;
	}

	/**
	 * Constructor: Saves a handle to the filename (used later to generate a URL
	 * instance). The file may either be located within the JAR or at an online
	 * location. If the file is online, filename must begin with "http://",
	 * since that is how SoundSystem recognizes URL names.
	 * 
	 * @param filename
	 *            Name of the file.
	 */
	public FilenameURL(String filename) {
		this.filename = filename;
		this.url = null;
	}

	/**
	 * Returns the filename/identifier.
	 * 
	 * @return Filename or identifier for the file.
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * Returns the URL interface to the file. If a URL was not originally
	 * specified in the constructor, then the first time this method is called
	 * it creates a URL instance using the previously specified filename.
	 * 
	 * @return URL interface to the file.
	 */
	public URL getURL() {
		if (this.url == null) {
			// Check if the file is online or inside the JAR:
			if (this.filename.matches(SoundSystemConfig.PREFIX_URL)) {
				// Online
				try {
					this.url = new URL(this.filename);
				} catch (Exception e) {
					this.logger.err("FilenameURL", "Unable to access online URL", 0);
					this.logger.printException(e);
					return null;
				}
			} else {
				// Inside the JAR
				this.url = getClass().getClassLoader().getResource(
						SoundSystemConfig.getSoundFilesPackage() + this.filename);
			}
		}
		return this.url;
	}
}
