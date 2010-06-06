package org.furb.cg;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import org.furb.cg.camera.Camera3D;
import org.furb.cg.camera.FirstPerson;
import org.furb.cg.engine.GameMap;
import org.furb.cg.engine.PickModel;
import org.furb.cg.engine.structs.Caminho;
import org.furb.cg.engine.structs.Passo;
import org.furb.cg.loader.TextureLoader;
import org.furb.cg.render.Axis;
import org.furb.cg.render.Cube3D;
import org.furb.cg.render.Object3D;
import org.furb.cg.render.Robot;
import org.furb.cg.render.Target;
import org.furb.cg.util.TipoTerreno;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;

public class CanvasGLListener implements GLEventListener, KeyListener, MouseMotionListener, MouseListener {
	
	private final static double DISTANCE_VIEW	= 500.0;
	
	private GLAutoDrawable		glDrawable	= null;
	private GLU 				glu			= null;
	private GameMap				gameMap 	= null;
	private Axis				axisRender	= null; 
	private List<Object3D>		mapa3D		= null;
	private PickModel			pickModel	= null;
	
	//Robo, alvo e caminho entre eles
	private Robot				robot		= null;
	private Target				target		= null;

	private Camera3D camera;
	
	/**
	 * Construtor padrao
	 */
	public CanvasGLListener()
	{
		this.gameMap = new GameMap();
		this.mapa3D = new ArrayList<Object3D>();
		this.axisRender = new Axis();
		
		//this.camera = new ThirdPerson();
		this.camera = new FirstPerson();
	}
	
	/**
	 * Metodo que inicializa o jogl, 
	 * o mapa, as texturas.
	 */
	public void init(GLAutoDrawable drawable) 
	{
		GL gl		= drawable.getGL();
		glDrawable	= drawable;
		glu			= new GLU();
		pickModel	= new PickModel(mapa3D, gl, glu);
		
		TextureLoader.getInstance();
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
	 * Metodo que instancia o mapa.
	 * @param gl
	 */
	private void initMap(GL gl)
	{
		int objectID = 0;
		Cube3D cube3D = null;
		TipoTerreno tp = null;
		
		int newX, newZ;
		
		for (int y = 0; y < gameMap.getTerrain().length; y++ ) 
		{
			final int[] row = gameMap.getTerrain()[y];
			
			for( int x = 0; x < row.length; x++ )
			{
				final int unit = gameMap.getUnit(y, x);
				
				newX = y*2;
				newZ = x*2;
				
				if( unit == TipoTerreno.ROBOT.getType() )
				{
					robot = new Robot(newX, 2, newZ);
					robot.setTipoTerreno( TipoTerreno.ROBOT );
					robot.setMapXY(y, x);
					robot.setObjectID(objectID++);
				}

				if( unit == TipoTerreno.TARGET.getType() )
				{
					target = new Target(newX, 2, newZ);
					target.setTipoTerreno( TipoTerreno.TARGET );
					target.setMapXY(y, x);
					target.setObjectID(objectID++);
				}
				
				tp = TipoTerreno.valueOf( row[x] );
				cube3D = new Cube3D(newX, 0,newZ);
				cube3D.setMapXY(y, x);
				cube3D.setTipoTerreno(tp);
				cube3D.setObjectID(objectID++);
				this.mapa3D.add(cube3D);
			}
		}
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
		
		glu.gluLookAt(
			camera.getXCamPos(),	camera.getYCamPos(),	camera.getZCamPos(),
			camera.getXLookAt(),	camera.getYLookAt(),	camera.getZLookAt(),
			camera.getXUp(), 		camera.getYUp(),		camera.getZUp()
		 );
		
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		axisRender.draw(gl, null);

		//aplica as rotacoes para os eixos x,y,z
	    gl.glPushMatrix();
	    gl.glRotated(camera.getRotX(), 1.0f, 0.0f, 0.0f);
	    gl.glRotated(camera.getRotY(), 0.0f, 1.0f, 0.0f);
	    gl.glRotated(camera.getRotZ(), 0.0f, 0.0f, 1.0f);
	    gl.glPushMatrix();
		
    	pickModel.tryStartPicking();
		drawnMap(gl);
		pickModel.tryEndPicking();
		
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glFlush();
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
			pickModel.tryPushObject(obj3D);
			
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
			
			if( currentTexture != null && obj3D.isBindTexture() ) 
			{
				currentTexture.bind();
				gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
				tc = currentTexture.getImageTexCoords();
				obj3D.draw(gl, tc);
			}
			else
			{
				gl.glDisable(GL.GL_TEXTURE_2D);
				obj3D.draw(gl);
			}
			
			
			pickModel.tryPopObject();
			tc = null;
		}

		gl.glDisable(GL.GL_TEXTURE_2D);
		
		//Desenha depois de desabilitar a textura.
		robot.draw(gl);
		target.draw(gl);
		
		gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE); 
		gl.glDisable(GL.GL_ALPHA); 
		gl.glDisable(GL.GL_BLEND);
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
	 * Metodo que faz o robo calcular
	 * o caminho ate o alvo e andar ate la.
	 */
	private void walkToTarget()
	{
		new Thread( new Runnable() 
		{
			Caminho path = gameMap.getFasterPath( 
				robot.getMapX(), 
				robot.getMapY(), 
				target.getMapX(), 
				target.getMapY() 
			);
			
			public void run() 
			{
				try {
					
					if( path != null && path.getSteps() != null )
					{
						
						int newX, newZ;
						
						for(Passo step : path.getSteps())
						{
							
							newX = step.getX()*2;
							newZ = step.getY()*2;
							
							robot.setMapXY(step.getX(), step.getY());
							robot.moveTo(newX, 2, newZ);
							
							camera.setXLookAt(newX);
							camera.setZLookAt(newZ);
							
							glDrawable.display();
							Thread.sleep(500);
						}	
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}).start();
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
			
			case KeyEvent.VK_R:
			{
				camera.resetPosition();
				
				break;
			}
			
			case KeyEvent.VK_SPACE:
			{
				walkToTarget();
				break;
			}
			
			default:
			{
				camera.move(keyCode, e.isControlDown());	
				break;
			}
		}
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

	    float thetaY = 360.0f * ( (float)(x-camera.getPrevMouseX())/(float)size.width);
	    float thetaX = 360.0f * ( (float)(camera.getPrevMouseY()-y)/(float)size.height);
	    
	    camera.setPrevMouseX(x);
	    camera.setPrevMouseY(y);

	    camera.setRotX( camera.getRotX() + thetaX );
	    camera.setRotY( camera.getRotY() + thetaY );
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
		camera.setPrevMouseX(e.getX());
		camera.setPrevMouseY(e.getY());
	    
	    if( e.getButton() == MouseEvent.BUTTON3 || e.getButton() == MouseEvent.BUTTON2 )
	    {
	    	pickModel.setInSelectionMode(true);
	    	pickModel.setClickX(camera.getPrevMouseX());
	    	pickModel.setClickY(camera.getPrevMouseY());
	    }
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
