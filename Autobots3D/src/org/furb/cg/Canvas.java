package org.furb.cg;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import org.furb.cg.engine.GameMap;
import org.furb.cg.render.Axis;
import org.furb.cg.render.Cube;

import com.sun.opengl.util.GLUT;

public class Canvas implements GLEventListener, KeyListener, MouseMotionListener, MouseListener  {
	
	private GameMap gameMap = null;
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private double xEye, yEye, zEye;
	private double xCenter, yCenter, zCenter;
	private final double xUp = 0.0f, yUp = 1.0f, zUp = 0.0f;
	private float aspectRatio;
	
	private float view_rotx = 0.0f;
	private float view_roty = 0.0f;
	private float view_rotz = 0.0f;
	private float angle = 0.0f;
	private int prevMouseX;
	private int prevMouseY;
	
	private Cube cubeRender = null;
	private Axis axisRender = null;
	
	public void init(GLAutoDrawable drawable) 
	{
		this.initEngine(drawable);
		this.initConfig();
		this.initRenders();	
	}
	
	private void initEngine(GLAutoDrawable drawable)
	{
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));
	}
	
	private void initConfig()
	{
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		xEye = 10.0f;
		yEye = 10.0f;
		zEye = 20.0f;
		xCenter = 0.0f;
		yCenter = 0.0f;
		zCenter = 0.0f;
	}
	
	private void initRenders(){
		this.cubeRender = new Cube(this.gl, this.glut);
		this.axisRender = new Axis(this.gl, this.glut);
	}

	public void display(GLAutoDrawable drawable) 
	{
		angle += 2.0f;
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		this.axisRender.draw();

	    gl.glPushMatrix();
	    gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
	    gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
	    gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);
	    gl.glPushMatrix();
	    
		for (int y = 0; y < gameMap.getTerrain().length; y++ ) 
		{
			final int[] row = gameMap.getTerrain()[y];
			
			for( int x = 0; x < row.length; x++ )
			{
				this.cubeRender.setZT(y);
				this.cubeRender.setXT(x);
				this.cubeRender.setYT(0);
				this.cubeRender.setSolid(false);
				this.cubeRender.draw();
			}
		}
		
		gl.glPopMatrix();
		gl.glPopMatrix();
		
		gl.glFlush();
	}

	public void keyPressed(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{
			case KeyEvent.VK_ESCAPE: {
				System.exit(0);
			}
		}
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
	{
		aspectRatio = (float) height / (float) width;
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustum(-1.0f, 1.0f, -aspectRatio, aspectRatio, 5.0f, 60.0f);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(xEye, yEye, zEye, xCenter, yCenter, zCenter, xUp, yUp,zUp);
	}
	
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
		return;
	}

	public void keyTyped(KeyEvent arg0) {
		return;
	}

	public void mouseDragged(MouseEvent e) 
	{
		int x = e.getX();
	    int y = e.getY();
	    Dimension size = e.getComponent().getSize();

	    float thetaY = 360.0f * ( (float)(x-prevMouseX)/(float)size.width);
	    float thetaX = 360.0f * ( (float)(prevMouseY-y)/(float)size.height);
	    
	    prevMouseX = x;
	    prevMouseY = y;

	    view_rotx += thetaX;
	    view_roty += thetaY;
	}

	public void mouseMoved(MouseEvent e) {
		return;
	}

	public void mouseClicked(MouseEvent e) {
		return;
	}

	public void mouseEntered(MouseEvent e) {
		return;
	}

	public void mouseExited(MouseEvent e) {
		return;
	}

	public void mousePressed(MouseEvent e) 
	{
		prevMouseX = e.getX();
	    prevMouseY = e.getY();
	}

	public void mouseReleased(MouseEvent e) 
	{
		return;
	}
	
	public GameMap getGameMap() {
		return gameMap;
	}

	public void setGameMap(GameMap gameMap) {
		this.gameMap = gameMap;
	}
}
