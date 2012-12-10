package com.hackhalo2.rendering.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Model {
	
	private String objectName; //The Object Name
	
	private boolean finalized = false; //Defines if the Model has be finalized (I.E. Read Only)
	
	private List<Vector3f> vertices;
	private List<Vector3f> normals;
	private List<Face> faces;
	
	public Model() {
		this.normals = new ArrayList<Vector3f>();
		this.vertices = new ArrayList<Vector3f>();
		this.faces = new ArrayList<Face>();
	}
	
	public void setName(String name) {
		if(!this.finalized) this.objectName = name;
		else throw new UnsupportedOperationException("Model was finalized");
	}
	
	public void finalize() {
		if(!this.finalized) this.finalized = true;
		else throw new UnsupportedOperationException("Model was already finalized");
	}
	
	public void addFace(Face face) {
		if(!this.finalized) this.faces.add(face);
		else throw new UnsupportedOperationException("Model was finalized");
	}
	
	public void addNormal(Vector3f normal) {
		if(!this.finalized) this.normals.add(normal);
		else throw new UnsupportedOperationException("Model was finalized");
	}
	
	public void addVertex(Vector3f vertex) {
		if(!this.finalized) this.vertices.add(vertex);
		else throw new UnsupportedOperationException("Model was finalized");
	}
	
	public String getName() {
		return this.objectName;
	}
	
	public int getVertexSize() {
		return this.vertices.size();
	}
	
	public Vector3f getVertex(int index) {
		return this.vertices.get(index);
	}
	
	public int getNormalSize() {
		return this.normals.size();
	}
	
	public Vector3f getNormal(int index) {
		return this.normals.get(index);
	}
	
	public List<Face> getFaces() {
		return this.faces;
	}
	
	public int bufferSize() {
		return (this.faces.size()*9);
	}

}
