package com.hackhalo2.rendering.model;

import org.lwjgl.util.vector.Vector3f;

public class Face {
		private Vector3f vertex;
		private Vector3f normal;

		public Face(Vector3f vertex, Vector3f normal) {
			this.normal = normal;
			this.vertex = vertex;
		}
		
		public Vector3f getVertex() {
			return this.vertex;
		}
		
		public Vector3f getNormal() {
			return this.normal;
		}
}
