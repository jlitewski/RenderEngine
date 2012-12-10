package com.hackhalo2.rendering.math;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Quaternion {

	private float w, x, y, z;
	private final float tolerance = 0.0001f;

	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	//Convert from Axis Angle
	public Quaternion(Vector3f vector, float angle) {
		angle *= 0.5f;
		Vector3f vn = new Vector3f(vector);
		vn.normalise();

		float sinAngle = (float)Math.sin(angle);

		this.x = (vn.x * sinAngle);
		this.y = (vn.y * sinAngle);
		this.z = (vn.z * sinAngle);
		this.w = (float)Math.cos(angle);
	}

	public Quaternion(Matrix4f matrix) {
		float tr = matrix.m00 + matrix.m11 + matrix.m22;

		if (tr > 0) { 
			float S = (float)Math.sqrt(tr+1.0) * 2; // S=4*qw 
			this.w = 0.25f * S;
			this.x = (matrix.m21 - matrix.m12) / S;
			this.y = (matrix.m02 - matrix.m20) / S; 
			this.z = (matrix.m10 - matrix.m01) / S; 
		} else if ((matrix.m00 > matrix.m11)&(matrix.m00 > matrix.m22)) { 
			float S = (float)Math.sqrt(1.0 + matrix.m00 - matrix.m11 - matrix.m22) * 2; // S=4*qx 
			this.w = (matrix.m21 - matrix.m12) / S;
			this.x = 0.25f * S;
			this.y = (matrix.m01 + matrix.m10) / S; 
			this.z = (matrix.m02 + matrix.m20) / S; 
		} else if (matrix.m11 > matrix.m22) { 
			float S = (float)Math.sqrt(1.0 + matrix.m11 - matrix.m00 - matrix.m22) * 2; // S=4*qy
			this.w = (matrix.m02 - matrix.m20) / S;
			this.x = (matrix.m01 + matrix.m10) / S; 
			this.y = 0.25f * S;
			this.z = (matrix.m12 + matrix.m21) / S; 
		} else {
			float S = (float)Math.sqrt(1.0 + matrix.m22 - matrix.m00 - matrix.m11) * 2; // S=4*qz
			this.w = (matrix.m10 - matrix.m01) / S;
			this.x = (matrix.m02 + matrix.m20) / S;
			this.y = (matrix.m12 + matrix.m21) / S;
			this.z = 0.25f * S;
		}

	}

	public float w() {
		return this.w;
	}
	
	public void setW(float w) {
		this.w = w;
	}

	public float x() {
		return this.x;
	}
	
	public void setX(float x) {
		this.x = x;
	}

	public float y() {
		return this.y;
	}
	
	public void setY(float y) {
		this.y = y;
	}

	public float z() {
		return this.z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}

	public Matrix4f getMatrix() {
		Matrix4f matrix = new Matrix4f();
		Matrix4f.setZero(matrix);

		float x2 = this.x * this.x;
		float y2 = this.y * this.y;
		float z2 = this.z * this.z;
		float xy = this.x * this.y;
		float xz = this.x * this.z;
		float yz = this.y * this.z;
		float wx = this.w * this.x;
		float wy = this.w * this.y;
		float wz = this.w * this.z;

		matrix.m00 = 1.0f - 2.0f * (y2 + z2);
		matrix.m01 = 2.0f * (xy - wz);
		matrix.m02 = 2.0f * (xz + wy);
		matrix.m03 = 0.0f;

		matrix.m10 = 2.0f * (xy + wz);
		matrix.m11 = 1.0f - 2.0f * (x2 + z2);
		matrix.m12 = 2.0f * (yz - wx);
		matrix.m13 = 0.0f;

		matrix.m20 = 2.0f * (xz - wy);
		matrix.m21 = 2.0f * (yz + wx);
		matrix.m22 = 1.0f - 2.0f * (x2 + y2);
		matrix.m23 = 0.0f;

		matrix.m30 = 0.0f;
		matrix.m31 = 0.0f;
		matrix.m32 = 0.0f;
		matrix.m33 = 1.0f;

		return matrix;
	}

	public Quaternion multiply(Quaternion q) {
		return new Quaternion(
				this.w * q.x + this.x * q.w + this.y * q.z - this.z * q.y,
				this.w * q.y + this.y * q.w + this.z * q.x - this.x * q.z,
				this.w * q.z + this.z * q.w + this.x * q.y - this.y * q.x,
				this.w * q.w - this.x * q.x - this.y * q.y - this.z * q.z
				);
	}

	public Vector3f multiply(Vector3f v) {
		Vector3f vn = new Vector3f(v);
		vn.normalise();

		Quaternion vecQuat = new Quaternion(vn.x, vn.y, vn.z, 0.0f);
		Quaternion resQuat = vecQuat.multiply(this.getConjugate());
		resQuat = this.multiply(resQuat);

		return (Vector3f) new Vector3f(resQuat.x(), resQuat.y(), resQuat.z()).scale(v.length());
	}

	public Quaternion getConjugate() {
		return new Quaternion(-this.x, -this.y, -this.z, this.w);
	}

	public Quaternion normalize() {
		float mag = this.w*this.w+this.x*this.x+this.y*this.y+this.z*this.z;
		if(Math.abs(mag) > this.tolerance && Math.abs(mag - 1.0f) > this.tolerance) {
			float mag2 = (float)Math.sqrt(mag);
			this.w /= mag2;
			this.x /= mag2;
			this.y /= mag2;
			this.z /= mag2;
		}
		return this;
	}

}