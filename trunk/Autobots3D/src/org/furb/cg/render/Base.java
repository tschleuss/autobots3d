package org.furb.cg.render;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public abstract class Base {

	protected GL gl;
	protected GLUT glut;

	protected float xS;
	protected float yS;
	protected float zS;
	
	protected float xT;
	protected float yT;
	protected float zT;
	
	protected float red;
	protected float green;
	protected float blue;
	
	public Base(GL gl, GLUT glut)
	{
		this.gl = gl;
		this.glut = glut;
		this.xS = 1;
		this.yS = 1;
		this.zS = 1;
		
		this.setXT(1);
		this.setYT(1);
		this.setZT(1);
		
		this.setRed(0);
		this.setGreen(0);
		this.setBlue(0);
	}
	
	public abstract void draw();

	public void setXS(float xS) {
		this.xS = xS;
	}

	public float getXS() {
		return xS;
	}

	public void setYS(float yS) {
		this.yS = yS;
	}

	public float getYS() {
		return yS;
	}

	public void setZS(float zS) {
		this.zS = zS;
	}

	public float getZS() {
		return zS;
	}

	public void setXT(float xT) {
		this.xT = xT;
	}

	public float getXT() {
		return xT;
	}

	public void setYT(float yT) {
		this.yT = yT;
	}

	public float getYT() {
		return yT;
	}

	public void setZT(float zT) {
		this.zT = zT;
	}

	public float getZT() {
		return zT;
	}
	
	public void setRed(float red){
		this.red = red;
	}

	public void setGreen(float green){
		this.green = green;
	}

	public void setBlue(float blue){
		this.blue = blue;
	}
}
