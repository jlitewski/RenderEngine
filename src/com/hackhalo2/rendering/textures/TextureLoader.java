package com.hackhalo2.rendering.textures;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class TextureLoader {
	
	public static Texture2D load2DTexture(URI uri) {
		BufferedInputStream bis = null;
		ByteBuffer texCoords = null;
		try {
			File file = new File(uri);
			
			//Idiot checking
			if(!file.exists() || !file.isFile()) {
				System.err.println("File doesn't exist or was not a File, aborting!");
				return null;
			}
			
			//Set up the BufferedInputStream
			bis = new BufferedInputStream(new FileInputStream(file)); //get the InputStream
			
			//Set up the ByteBuffer
			texCoords = BufferUtils.createByteBuffer(12); //The default is 12, use an overloaded method to custom define this default
			texCoords.put(new byte[] {0,0, 1,0, 1,1, 1,1, 0,1, 0,0}).flip(); //The default. Again, Overloaded Constructor to deine this
			return new Texture2D(bis, texCoords); //Set up the Texture
		} catch(Exception e) {
			System.err.println("An Exception was thrown while loading the Texture!");
			e.printStackTrace();
			return null;
		}
	}
}
