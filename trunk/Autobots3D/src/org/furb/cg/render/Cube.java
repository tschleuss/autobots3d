package org.furb.cg.render;


import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class Cube extends Base {

	private float size;	
	private boolean solid;
	
	public Cube(GL gl, GLUT glut) {
		super(gl, glut);
		this.size = 1.0f;
		this.setSolid(false);
	}
	
	@Override
	public void draw() 
	{
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		gl.glPushMatrix();
			gl.glTranslatef(xT, yT, zT);
			gl.glScalef(xS,yS,zS);
			
			if(this.solid){
				glut.glutSolidCube(this.size);
			}else{
				glut.glutWireCube(this.size);
			}
		gl.glPopMatrix();
	}

	public void setSize(float size) {
		this.size = size;
	}

	public float getSize() {
		return size;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isSolid() {
		return solid;
	}
}
