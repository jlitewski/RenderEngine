package com.hackhalo2.rendering.interfaces;

import org.lwjgl.util.vector.Vector;

public interface ITexture {
	
	public boolean hasAlpha();
	
	public void bind();
	public void unbind();
	
	public int getWidth();
	public int getHeight();
	
	public Vector getCenterPoint();
	
	public int getID();
	
	public void display();

}
