package org.furb.cg;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import org.furb.cg.engine.GameMap;
import org.furb.cg.render.Axis;
import org.furb.cg.render.Cube;


import com.sun.opengl.util.GLUT;

public class Canvas implements GLEventListener, KeyListener  {
	
	private GameMap gameMap = null;
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private double xEye, yEye, zEye;
	private double xCenter, yCenter, zCenter;
	private final double xUp = 0.0f, yUp = 1.0f, zUp = 0.0f;
	private float aspectRatio;
	
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
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		this.axisRender.draw();
		
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
		
		gl.glFlush();
	}

	public void keyPressed(KeyEvent e) 
	{
		switch (e.getKeyCode()) 
		{
			case KeyEvent.VK_ESCAPE: {
				System.exit(1);
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

	public GameMap getGameMap() {
		return gameMap;
	}

	public void setGameMap(GameMap gameMap) {
		this.gameMap = gameMap;
	}
}
