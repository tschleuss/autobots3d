package org.furb.cg;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import org.furb.cg.engine.GameMap;
import org.furb.cg.render.Axis;
import org.furb.cg.render.Cube3D;
import org.furb.cg.render.Object3D;
import org.furb.cg.render.Robot;
import org.furb.cg.util.TipoTerreno;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;

public class Canvas implements GLEventListener, KeyListener, MouseMotionListener, MouseListener  {
	
	private GameMap		gameMap		= null;
	private Animator	animator	= null;
	
	private GLAutoDrawable	glDrawable	= null;
	private GLUT			glut		= null;
	private GLU				glu			= null;
	private GL				gl			= null;

	private final double xUp = 0.0f;
	private final double yUp = 1.0f;
	private final double zUp = 0.0f;
	
	private final static int FLOOR_LEN = 100;
	private final static double LOOK_AT_DIST = 100.0;
	private final static double Z_POS = 9.0;
	private final static double SPEED = 1.5;
	private final static double ANGLE_INCR = 5.0;
	private final static double HEIGHT_STEP = 1.0;
	private final static double DISTANCE_VIEW = 500.0f;
	
	private double xCamPos, yCamPos, zCamPos;
	private double xLookAt, yLookAt, zLookAt;

	private double xStep, zStep;
	private double viewAngle;
	private float aspectRatio;
	
	private float view_rotx, view_roty, view_rotz = 0.0f;
	private int prevMouseX, prevMouseY;
	private Axis axisRender = null;    
	
	private ArrayList<Object3D> mapa3D;
	
	
	public void init(GLAutoDrawable drawable) 
	{
		this.initEngine(drawable);
		this.initConfig();
		this.initRenders();
		this.initMap();
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
		gl.glClearColor(0, 0, 0, 1.0f);
		this.initViewerPos();
	}
	
	private void initRenders()
	{
		this.axisRender = new Axis(gl, glut);
		this.mapa3D =  new ArrayList<Object3D>();
	}
	
	private void initMap()
	{
		Cube3D cube3D = null;
		
		for (int y = 0; y < gameMap.getTerrain().length; y++ ) 
		{
			final int[] row = gameMap.getTerrain()[y];
			
			for( int x = 0; x < row.length; x++ )
			{
				cube3D = new Cube3D(this.gl, x * 3 , 0 , y * 3 );
				cube3D = new Cube3D(this.gl, x * 3,0,y * 3);
				cube3D.setMapXY(x, y);
				cube3D.setTipoTerreno( TipoTerreno.valueOf( row[x] ) );
				this.mapa3D.add(cube3D);
			}
		}
		
		Robot robot = new Robot(this.gl, 10 * 3 , 2 , 10 * 3 );
		robot.setTipoTerreno(TipoTerreno.ROBOT);
		this.mapa3D.add(robot);
	}
	
	private void initViewerPos()
	{
		view_rotx = 0.0f;
		view_roty = 0.0f;
		view_rotz = 0.0f;
		xCamPos = 0;
		yCamPos = 1;
		zCamPos = Z_POS;
		
		viewAngle = -90.0;
		xStep = Math.cos( Math.toRadians(viewAngle) );
		zStep = Math.sin( Math.toRadians(viewAngle) );
		
		xLookAt = xCamPos + ( LOOK_AT_DIST * xStep );
		yLookAt = 0;
		zLookAt = zCamPos + ( LOOK_AT_DIST * zStep );
	}

	public void display(GLAutoDrawable drawable) 
	{
		this.refreshRender();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		this.axisRender.draw();

	    gl.glPushMatrix();
	    gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
	    gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
	    gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);
	    gl.glPushMatrix();
	    
	    for (Object3D casa: this.mapa3D) {
			casa.draw();
		}
	    
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glFlush();
	}

	public void keyPressed(KeyEvent e) 
	{
		final int keyCode = e.getKeyCode();
		
		switch ( keyCode ) 
		{
			case KeyEvent.VK_ESCAPE: 
			{
				System.exit(0);
				break;
			}
			
			case KeyEvent.VK_LEFT: 
			{
				if( e.isControlDown()) {
					xCamPos += zStep * SPEED;
					zCamPos -= xStep * SPEED;
				}
				else {
					viewAngle -= ANGLE_INCR;
					xStep = Math.cos( Math.toRadians(viewAngle) );
					zStep = Math.sin( Math.toRadians(viewAngle) );
				}
				
				break;
			}
			
			case KeyEvent.VK_RIGHT: 
			{
				if( e.isControlDown()) {
					xCamPos -= zStep * SPEED;
					zCamPos += xStep * SPEED;
				}
				else {
					viewAngle += ANGLE_INCR;
					xStep = Math.cos( Math.toRadians(viewAngle) );
					zStep = Math.sin( Math.toRadians(viewAngle) );
				}
				
				break;
			}
			
			case KeyEvent.VK_UP: 
			{
				if( e.isControlDown() ) {
					if( (yCamPos + HEIGHT_STEP) < FLOOR_LEN/2 ) {
						yCamPos += HEIGHT_STEP;
						yLookAt += HEIGHT_STEP;
					}
 				}
				else {
					xCamPos += xStep * SPEED;
					zCamPos += zStep * SPEED;
				}
				
				break;
			}
			
			case KeyEvent.VK_DOWN: 
			{
				if( e.isControlDown() ) {
					if( (yCamPos - HEIGHT_STEP) > 0 ) {
						yCamPos -= HEIGHT_STEP;
						yLookAt -= HEIGHT_STEP;
					}
 				}
				else {
					xCamPos -= xStep * SPEED;
					zCamPos -= zStep * SPEED;
				}
				
				break;
			}
			
			case KeyEvent.VK_R:
			{
				this.initViewerPos();
				break;
			}
		}
		
		if( xCamPos < -FLOOR_LEN/2 ) {
			xCamPos = -FLOOR_LEN/2;
		}
		else if( xCamPos > FLOOR_LEN/2 ) {
			xCamPos = FLOOR_LEN/2;
		}
		
		if( zCamPos < -FLOOR_LEN/2 ) {
			zCamPos = -FLOOR_LEN/2;
		}
		else if( zCamPos > FLOOR_LEN/2 ) {
			zCamPos = FLOOR_LEN/2;
		}
		
		xLookAt = xCamPos + (xStep * LOOK_AT_DIST);
		zLookAt = zCamPos + (zStep * LOOK_AT_DIST);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
	{
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		aspectRatio = (float) height / (float) width;
		gl.glFrustum(-1.0f, 1.0f, -aspectRatio, aspectRatio, 5.0f, DISTANCE_VIEW);
		
		this.refreshRender();
	}

	/**
	 * Atualiza a tela.
	 * @param width
	 * @param height
	 */
	private void refreshRender() 
	{
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(xCamPos, yCamPos, zCamPos, xLookAt, yLookAt, zLookAt, xUp, yUp,zUp);
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

	public Animator getAnimator() {
		return animator;
	}

	public void setAnimator(Animator animator) {
		this.animator = animator;
	}
}
