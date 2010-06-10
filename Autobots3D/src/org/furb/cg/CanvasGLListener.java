package org.furb.cg;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import org.furb.cg.camera.Camera3D;
import org.furb.cg.camera.CameraManager;
import org.furb.cg.camera.FirstPerson;
import org.furb.cg.camera.ThirdPerson;
import org.furb.cg.engine.GameMap;
import org.furb.cg.engine.PickModel;
import org.furb.cg.engine.structs.Caminho;
import org.furb.cg.engine.structs.Passo;
import org.furb.cg.loader.TextureLoader;
import org.furb.cg.render.Axis;
import org.furb.cg.render.Cube3D;
import org.furb.cg.render.Object3D;
import org.furb.cg.render.model.GLModel;
import org.furb.cg.util.ResourceUtil;
import org.furb.cg.util.TipoTerreno;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;

public class CanvasGLListener implements GLEventListener, KeyListener, MouseMotionListener, MouseListener {
	
	private final static double DISTANCE_VIEW	= 500.0;
	
	private GL					gl			= null;
	private GLU 				glu			= null;
	private GameMap				gameMap 	= null;
	private Axis				axisRender	= null; 
	private List<Object3D>		mapa3D		= null;
	private PickModel			pickModel	= null;
	
	//Robo, alvo e caminho entre eles
	private GLModel				r2d2 		= null;
	private GLModel				cone = null;
	
	private float 				robotRotateAngle;
	private float 				targetRotateAngle;
	
	private boolean				lookUp;
	private boolean 			lookRight;
	private boolean 			lookLeft;
	private boolean				rotateTarget;
	
	private Thread				threadRobot = null;
	private Thread				threadTarget = null;
	
	//Camera
	private CameraManager cameraManager;
	private Camera3D camera;
	
	//Arvores
	private List<Object3D> models = null;
	private GLModel tree;
	
	/**
	 * Construtor padrao
	 */
	public CanvasGLListener()
	{
		this.axisRender = new Axis();
		this.cameraManager = new CameraManager();
		this.camera = cameraManager.atual();
	}
	
	/**
	 * Metodo que inicializa o jogl, 
	 * o mapa, as texturas.
	 */
	public void init(GLAutoDrawable drawable) 
	{
		gl			= drawable.getGL();
		glu			= new GLU();
		pickModel	= new PickModel(mapa3D, gl, glu);
		
		TextureLoader.getInstance();
		initModels();
		initMap();
		
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
		renderScene();
	}
	
	/**
	 * Metodo que instancia o mapa.
	 * @param gl
	 */
	private void initMap()
	{
		this.lookUp = true;
		this.lookLeft = false;
		this.lookRight = false;
		this.robotRotateAngle = 0;
		this.targetRotateAngle = 0;
		this.rotateTarget = false;
		
		this.gameMap = new GameMap();
		this.mapa3D = new ArrayList<Object3D>();
		this.models = new ArrayList<Object3D>();

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
					r2d2.setTipoTerreno( TipoTerreno.ROBOT );
					r2d2.setMapXY(y, x);
					r2d2.setObjectID(objectID++);
				}

				if( unit == TipoTerreno.TARGET.getType() )
				{
					cone.setTipoTerreno( TipoTerreno.TARGET );
					cone.setMapXY(y, x);
					cone.setObjectID(objectID++);
				}
				
				tp = TipoTerreno.valueOf( row[x] );
				cube3D = new Cube3D(newX, 0,newZ);
				cube3D.setMapXY(y, x);
				cube3D.setTipoTerreno(tp);
				cube3D.setObjectID(objectID++);
				this.mapa3D.add(cube3D);
				
				if( tp == TipoTerreno.TREES )
				{
					this.models.add(cube3D);
				}
			}
		}
		
		ajustCameraVision();
	}
	
	/**
	 * Inicializa os modelos
	 * a serem importados, ou seja,
	 * carrega o arquivo OBJ para a 
	 * memoria, parseando linha por linha.
	 */
	private void initModels()
	{
		try {
			
			InputStream is = ResourceUtil.getResource("/org/furb/cg/resources/obj/palmtree.obj", CanvasGLListener.class);
	        BufferedReader treeBuf = new BufferedReader( new InputStreamReader(is) );
	        tree = new GLModel(treeBuf, true, "/org/furb/cg/resources/obj/palmtree.mtl", gl);
	        
	        if( treeBuf != null )
	        {
	        	treeBuf.close();
	        }
	        
	        if( is != null )
	        {
	        	is.close();
	        }

			is = ResourceUtil.getResource("/org/furb/cg/resources/obj/r2d2.obj", CanvasGLListener.class);
	        BufferedReader r2dBuf = new BufferedReader( new InputStreamReader(is) );
	        r2d2 = new GLModel(r2dBuf, true, "/org/furb/cg/resources/obj/r2d2.mtl", gl);

	        if( r2dBuf != null )
	        {
	        	r2dBuf.close();
	        }
	        
	        if( is != null )
	        {
	        	is.close();
	        }
	        
			is = ResourceUtil.getResource("/org/furb/cg/resources/obj/cone.obj", CanvasGLListener.class);
	        BufferedReader coneBuf = new BufferedReader( new InputStreamReader(is) );
	        cone = new GLModel(coneBuf, true, "/org/furb/cg/resources/obj/cone.mtl", gl);

	        if( coneBuf != null )
	        {
	        	coneBuf.close();
	        }
	        
	        if( is != null )
	        {
	        	is.close();
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo chamado pelo metodo display,
	 * sempre que a alteracoes para
	 * serem renderizadas na tela do usuario.
	 * @param gl
	 */
	private void renderScene()
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
		drawnMap();
		pickModel.tryEndPicking();
		
		//Desenha os modelos na tela
		drawnModels();
		
		gl.glPopMatrix();
		gl.glPopMatrix();
		gl.glFlush();
	}
	
	/**
	 * Desenha as arvores.
	 */
	private void drawnModels()
	{
		for(Object3D obj3D : models)
		{
	        gl.glPushMatrix();
	        gl.glTranslatef(obj3D.getMapX()*2, 3, obj3D.getMapY()*2);
	        gl.glScalef(0.2f, 0.2f, 0.2f);
	        tree.draw(gl);
	        gl.glPopMatrix();
		}
		
        gl.glPushMatrix();
        gl.glTranslatef(r2d2.getMapX() * 2, 3, r2d2.getMapY()*2);
        gl.glScalef(0.8f, 0.8f, 0.8f);
		gl.glRotatef(this.robotRotateAngle, 0.0f, 1.0f, 0.0f);
        r2d2.draw(gl);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
        
        //robo alcançou o alvo
        if(this.rotateTarget)
        {
            gl.glTranslatef(cone.getMapX() * 2, 6, cone.getMapY()*2);
            gl.glRotatef(this.targetRotateAngle, 0.0f, 1.0f, 0.0f);
        }
        else
        {
            gl.glTranslatef(cone.getMapX() * 2, 2.5f, cone.getMapY()*2);
        }

        gl.glScalef(0.4f, 0.4f, 0.4f);
        cone.draw(gl);
        gl.glPopMatrix();
	}
	
	/**
	 * Metodo que desenha o mapa
	 * na tela do usuario.
	 */
	private void drawnMap()
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
				
				case DIRT_GRASS: {
					gl.glEnable(GL.GL_TEXTURE_2D);
					currentTexture = TextureLoader.getInstance().getDirtGrassText();
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
		//target.draw(gl);
		
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
		this.threadRobot = new Thread( new Runnable() 
		{
			public void run() 
			{
				Caminho path = gameMap.getFasterPath( 
					r2d2.getMapX(), 
					r2d2.getMapY(), 
					cone.getMapX(), 
					cone.getMapY() 
				);
				
				try {
					
					if( path != null && path.getSteps() != null )
					{
						int newX= 0, newZ= 0;
						int oldX= -1, oldZ= -1;

						for(Passo step : path.getSteps())
						{

							newX = step.getX();
							newZ = step.getY();
							
							ajustRobotDirection(newX, oldX, newZ, oldZ);

							oldX = newX;
							oldZ = newZ;

							r2d2.setMapXY(step.getX(), step.getY());
							changeToDirtGrass(step.getX(), step.getY());
							
							//Teste para camera em primeira pessoa
							ajustCameraVision();
							
							Thread.sleep(200);
						}
						
						if( camera instanceof FirstPerson )
						{
							camera.setXLookAt(0);
							camera.setZLookAt(0);
						}
						
						threadTarget = new Thread( new Runnable() 
							{
								public void run() 
								{
									rotateTarget = true;
									
									while(rotateTarget)
									{
										if(targetRotateAngle == 360)
										{
											targetRotateAngle = 0;
										}
										else
										{
											targetRotateAngle++;
										}
									}
								}
							}
						);
						
						threadTarget.start();
						
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		this.threadRobot.start();
	}
	
/**
	 * Ajusta a direção do robô no mapa, de acordo
	 * com sua posição antiga e sua posição nova
	 */
	private void ajustRobotDirection(int newX, int oldX, int newZ, int oldZ)
	{
		if(oldX !=-1 && oldZ != -1)
		{
			if(oldZ > newZ)
			{
				if(lookRight)
				{
					robotRotateAngle -= 90;
					lookRight = false;
				}
				else if(lookLeft)
				{
					robotRotateAngle += 90;
					lookLeft = false;
				}
				else if(lookUp)
				{
					robotRotateAngle += 180;
					lookUp = false;
				}
			}
			else if(oldZ < newZ)
			{
				if(lookRight)
				{
					robotRotateAngle += 90;
					lookRight = false;
				}
				else if(lookLeft)
				{
					robotRotateAngle -= 90;
					lookLeft = false;
				}
				else if(!lookUp)
				{
					robotRotateAngle -= 180;
					lookUp = true;
				}
			}
			
			if(oldX > newX && !lookRight)
			{
				if(lookUp)
				{
					robotRotateAngle -= 90;
				}
				else
				{
					robotRotateAngle += 90;
				}
				lookRight = true;
				lookLeft = false;
			}
			else if(oldX < newX && !lookLeft)
			{
				if(lookUp)
				{
					robotRotateAngle += 90;
				}
				else
				{
					robotRotateAngle -= 90;
				}
				lookRight = false;
				lookLeft = true;
			}
			/*
			else if(oldX == newX && (lookLeft || lookRight))
			{
				if(lookRight)
				{
					robotRotateAngle -= 90;
				}
				else
				{
					robotRotateAngle += 90;
				}
			}
			*/
		}
		
		if( camera instanceof ThirdPerson )
		{
			camera.setXLookAt(r2d2.getMapX() * 2);
			camera.setZLookAt(r2d2.getMapY() * 2);
		}
	}

	/**
	 * Ajusta o angulo da camera de acordo
	 * com o tipo de camera
	 */
	private void ajustCameraVision()
	{
		if( camera instanceof FirstPerson )
		{
			//Posicao do robo
			camera.setXCamPos( r2d2.getMapX() * 2 );
			camera.setYCamPos(2);
			camera.setZCamPos( r2d2.getMapY() * 2 );
			
			//Alvo da camera e o target
			camera.setXLookAt( cone.getMapX() * 2 );
			camera.setYLookAt(2);
			camera.setZLookAt( cone.getMapY() * 2 );
		}
		else if(camera instanceof ThirdPerson )
		{
			camera.setXLookAt( r2d2.getMapX() * 2 );
			camera.setYLookAt(2);
			camera.setZLookAt( r2d2.getMapY() * 2 );
		}
	}
	
	/**
	 * Altera a grama para grama suja, como
	 * se o robo tivesse a estragado
	 * @param x
	 * @param y
	 */
	public void changeToDirtGrass(int x, int y)
	{
		for(Object3D obj3D : mapa3D)
		{
			if( obj3D.getMapX() == x && obj3D.getMapY() == y )
			{
				obj3D.setTipoTerreno( TipoTerreno.DIRT_GRASS );
			}
		}
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
			
			case KeyEvent.VK_C:
			{
				cameraManager.changeCamera();
				camera = cameraManager.atual();
				camera.resetPosition();
				
				if( camera instanceof ThirdPerson )
				{
					camera.setXLookAt(r2d2.getMapX() * 2);
					camera.setZLookAt(r2d2.getMapY() * 2);
				}
				
				break;
			}
			
			case KeyEvent.VK_N:
			{
				if( e.isControlDown() )
				{

					if(this.threadRobot != null){
						this.threadRobot.stop();
					}
					
					if(rotateTarget)
					{
						rotateTarget = false;
					}
					
					initMap();
				}
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
	    	pickModel.setClickX( camera.getPrevMouseX() );
	    	pickModel.setClickY( camera.getPrevMouseY() );
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
