package com.hackhalo2.rendering.textures;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.vector.Vector2f;

import com.hackhalo2.rendering.interfaces.ITexture;
import com.ra4king.opengl.util.PNGDecoder;
import com.ra4king.opengl.util.PNGDecoder.Format;

public class Texture2D implements ITexture {

	private int texID;
	private ByteBuffer texData;
	private PNGDecoder decoder;
	private DoubleBuffer texCoords;

	protected Texture2D(InputStream in, ByteBuffer texCoords) {
		this.texID = GL11.glGenTextures();
		this.texCoords = texCoords.asDoubleBuffer();
		try {
			this.decoder = new PNGDecoder(in); //Set up the decoder
			if(this.hasAlpha()) { //If the png file has an alpha channel or a tRNS chunk, use 4 (R,G,B,A)
				this.texData = BufferUtils.createByteBuffer((this.decoder.getHeight()*this.decoder.getWidth())*4); //set up the buffer to store the PNG
				this.decoder.decode(this.texData, this.decoder.getWidth()*4, Format.RGBA); //put the image in the buffer
			} else { //Otherwise, use 3 (R,G,B)
				this.texData = BufferUtils.createByteBuffer((this.decoder.getHeight()*this.decoder.getWidth())*3); //set up the buffer to store the PNG
				this.decoder.decode(this.texData, this.decoder.getWidth()*3, Format.RGB); //put the image in the buffer
			}
			this.texData.flip();

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texID);
			//Setup wrap mode
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			//Setup texture scaling filtering
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.decoder.getWidth(), this.decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, this.texData);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			
		} catch (Exception e) {
			System.err.println("Exception generated when using the InputStream!");
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.err.println("Exception generated closing the InputStream!");
					e.printStackTrace();
				}
			} else {
				throw new RuntimeException("BufferInputStream was null when it shouldn't of been!");
			}
		}
	}

	@Override
	public boolean hasAlpha() {
		return (this.decoder.hasAlpha() || this.decoder.hasAlphaChannel());
	}

	@Override
	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texID);
	}

	@Override
	public void unbind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	@Override
	public int getWidth() {
		return this.decoder.getWidth();
	}

	@Override
	public int getHeight() {
		return this.decoder.getHeight();
	}

	@Override
	public Vector2f getCenterPoint() {
		return new Vector2f(this.decoder.getWidth()/2, this.decoder.getHeight()/2);
	}

	@Override
	public int getID() {
		return this.texID;
	}
	
	@Override
	public void display() {
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		GL11.glTexCoordPointer(2, 0, this.texCoords);
	}

}
