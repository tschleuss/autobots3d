package org.furb.cg;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import org.furb.cg.engine.GameMap;
import org.furb.cg.loader.TextureLoader;
import org.furb.cg.render.Axis;
import org.furb.cg.render.Cube3D;
import org.furb.cg.render.Object3D;
import org.furb.cg.render.Robot;
import org.furb.cg.util.TipoTerreno;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;

public class CanvasGLListener implements GLEventListener, KeyListener, MouseMotionListener, MouseListener {

	private final static double SPEED			= 1.5;
	private final static double LOOK_AT_DIST	= 100.0;
	private final static double ANGLE_INCR		= 5.0;
	private final static double HEIGHT_STEP		= 1.0;
	private final static double Z_POS			= 9.0;
	private final static double DISTANCE_VIEW	= 500.0;
	
	private GLU 				glu			= null;
	private GameMap				gameMap 	= null;
	private Axis				axisRender	= null; 
	private ArrayList<Object3D> mapa3D		= null;
	
	private final double xUp = 0.0f;
	private final double yUp = 1.0f;
	private final double zUp = 0.0f;
	
	//variaveis de rotacao
	private double rotX;
	private double rotY;
	private double rotZ;

	//Movimentacao da camera
	private double xCamPos;
	private double yCamPos;
	private double zCamPos;
	private double xLookAt;
	private double yLookAt;
	private double zLookAt;
	private double xStep;
	private double zStep;
	private double viewAngle;
	
	private int prevMouseX;
	private int prevMouseY;

	/**
	 * Construtor padrao
	 */
	public CanvasGLListener()
	{
		this.gameMap = new GameMap();
		this.mapa3D = new ArrayList<Object3D>();
		this.axisRender = new Axis();
	}
	
	/**
	 * Metodo que inicializa o jogl, 
	 * o mapa, as texturas.
	 */
	public void init(GLAutoDrawable drawable) 
	{
		GL gl = drawable.getGL();
		glu = new GLU();
		
		TextureLoader.getInstance();
		initViewerPos();
		initMap(gl);

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}
	
	/**
	 * Metodo de callback do JOGL,
	 * chamado sempre que a alteracoes
	 * na tela.
	 */
	public void display(GLAutoDrawable drawable) 
	{
		GL gl = drawable.getGL();
		renderScene(gl);
	}
	
	/**
	 * Metodo que inicializa as
	 * os atributos iniciais de
	 * camera e movimentacao.
	 */
	private void initViewerPos()
	{
		rotX = 0.0f;
		rotY = 0.0f;
		rotZ = 0.0f;
		
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
	
	/**
	 * Metodo que instancia o mapa.
	 * @param gl
	 */
	private void initMap(GL gl)
	{
		Robot robot = null;
		Cube3D cube3D = null;
		TipoTerreno tp = null;
		
		for (int y = 0; y < gameMap.getTerrain().length; y++ ) 
		{
			final int[] row = gameMap.getTerrain()[y];
			
			for( int x = 0; x < row.length; x++ )
			{
				final int unit = gameMap.getUnit(y, x);
				
				if( unit == TipoTerreno.ROBOT.getType() )
				{
					robot = new Robot(y*2, 2, x*2);
					robot.setTipoTerreno( TipoTerreno.ROBOT );
					robot.setMapXY(y, x);
					this.mapa3D.add(robot);
				}
				else {
					
					tp = TipoTerreno.valueOf( row[x] );
					cube3D = new Cube3D(y*2, 0, x*2);
					cube3D = new Cube3D(y*2, 0, x*2);
					cube3D.setMapXY(y, x);
					cube3D.setTipoTerreno(tp);
					this.mapa3D.add(cube3D);	
				}
			}
		}
	}
	
	/**
	 * Metodo que desenha o mapa
	 * na tela do usuario.
	 * @param gl
	 */
	private void drawnMap(GL gl)
	{
		TextureCoords tc = null;
		Texture currentTexture = null;
		
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL.GL_ALPHA_TEST); 
		gl.glAlphaFunc(GL.GL_GREATER, 0);
		gl.glEnable(GL.GL_TEXTURE_2D);
		
		for( Object3D obj3D : mapa3D )
		{
			switch (obj3D.getTipoTerreno()) 
			{
				case GRASS: {
					gl.glEnable(GL.GL_TEXTURE_2D);
					currentTexture = TextureLoader.getInstance().getGrassTex();
					break;
				}
				
				case TREES: {
					gl.glEnable(GL.GL_TEXTURE_2D);
					currentTexture = TextureLoader.getInstance().getGroundTex();
					break;
				}
				
				case WATER: {
					gl.glEnable(GL.GL_TEXTURE_2D);
					currentTexture = TextureLoader.getInstance().getWaterTex();
					break;
				}
				
				default: {
					gl.glDisable(GL.GL_TEXTURE_2D);
					break;
				}
			}
			
			if( currentTexture != null ) 
			{
				currentTexture.bind();
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
				tc = currentTexture.getImageTexCoords();
			}
			
			obj3D.draw(gl, tc);
		}

		gl.glDisable(GL.GL_TEXTURE_2D);
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE); 
		gl.glDisable(GL.GL_ALPHA); 
		gl.glDisable(GL.GL_BLEND);
	}
	
	/**
	 * Metodo chamado pelo metodo display,
	 * sempre que a alteracoes para
	 * serem renderizadas na tela do usuario.
	 * @param gl
	 */
	private void renderScene(GL gl)
	{
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		glu.gluLookAt(xCamPos, yCamPos, zCamPos, xLookAt, yLookAt, zLookAt, xUp, yUp,zUp);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		axisRender.draw(gl, null);

		//aplica as rotacoes para os eixos x,y,z
	    gl.glPushMatrix();
	    gl.glRotated(rotX, 1.0f, 0.0f, 0.0f);
	    gl.glRotated(rotY, 0.0f, 1.0f, 0.0f);
	    gl.glRotated(rotZ, 0.0f, 0.0f, 1.0f);
	    gl.glPushMatrix();
		
	    drawnMap(gl);
		
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glFlush();
	}

	/**
	 * Metodo de callback do JOGL
	 * chamado sempre que o usuario
	 * redimensiona a janela.
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
	{
		GL gl = drawable.getGL();
		
		if( height == 0 ) {
			height = 1;
		}
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		
		final float aspectRatio = (float) height / (float) width;
		gl.glFrustum(-1.0f, 1.0f, -aspectRatio, aspectRatio, 5.0f, DISTANCE_VIEW);
		
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/**
	 * Metodo de callback chamado
	 * sempre que o usuario pressiona
	 * alguma tecla do teclado.
	 * Utilizado para movimentar a camera.
	 */
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
					yCamPos += HEIGHT_STEP;
					yLookAt += HEIGHT_STEP;
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
					yCamPos -= HEIGHT_STEP;
					yLookAt -= HEIGHT_STEP;
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
		
		xLookAt = xCamPos + (xStep * LOOK_AT_DIST);
		zLookAt = zCamPos + (zStep * LOOK_AT_DIST);
	}

	/**
	 * Metodo de callback chamado
	 * sempre que o usuario clica e 
	 * arrasta o mouse, utilizado
	 * para movimentar a camera.
	 */
	public void mouseDragged(MouseEvent e) 
	{
		int x = e.getX();
	    int y = e.getY();
	    Dimension size = e.getComponent().getSize();

	    float thetaY = 360.0f * ( (float)(x-prevMouseX)/(float)size.width);
	    float thetaX = 360.0f * ( (float)(prevMouseY-y)/(float)size.height);
	    
	    prevMouseX = x;
	    prevMouseY = y;

	    rotX += thetaX;
	    rotY += thetaY;
	}

	/**
	 * Metodo de callback chamado
	 * sempre que o usuario preciona o mouse.
	 * Utilizado para guardar a posicao
	 * inicial do mouse antes do metodo
	 * mouseDragged ser chamado.
	 */
	public void mousePressed(MouseEvent e) 
	{
		prevMouseX = e.getX();
	    prevMouseY = e.getY();
	}
	
	public void displayChanged(GLAutoDrawable drawable, boolean arg1, boolean arg2) {
		return;
	}
	
	public void keyReleased(KeyEvent e) {
		return;
	}

	public void keyTyped(KeyEvent e) {
		return;
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

	public void mouseReleased(MouseEvent e) {
		return;
	}
}
