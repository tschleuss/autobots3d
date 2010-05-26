package org.furb.cg.render;

public class Ponto {

	private float x;
	private float y;
	private float z;
	private float w;
	
	public Ponto() {
		this(0, 0, 0, 1);
	}
	
	public Ponto(float x, float y) {
		this(x, y, 0, 1);
	}
	
	public Ponto(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
	
	public double getW() {
		return w;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setZ(float z) {
		this.z = z;
	}

	public void setW(float w) {
		this.w = w;
	}
}
