package org.furb.cg.engine;

import java.nio.IntBuffer;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import org.furb.cg.render.Object3D;

import com.sun.opengl.util.BufferUtil;

public class PickModel {

	private static final double SCREEN_WIDTH	= 1000;
	private static final double SCREEN_HEIGHT	= 700;
	private final static double DISTANCE_VIEW	= 500.0;
	private static final int	BUFSIZE			= 512;

	private List<Object3D> mapa3D;
	private boolean inSelectionMode;
	private IntBuffer selectBuffer;
	private int clickX;
	private int clickY;
	private GL gl;
	private GLU glu;
	
	public PickModel(List<Object3D> mapa3D, GL gl, GLU glu)
	{
		super();
		this.gl = gl;
		this.glu = glu;
		this.mapa3D = mapa3D;
	}
	
	public void tryStartPicking()
	{
		if( inSelectionMode )
		{
			selectBuffer = BufferUtil.newIntBuffer(BUFSIZE);
			gl.glSelectBuffer(BUFSIZE, selectBuffer);
			
			gl.glRenderMode(GL.GL_SELECT);
			gl.glInitNames();
			
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glPushMatrix();
			gl.glLoadIdentity();
			
			int viewPort[] = new int[4];
			gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort, 0);
			
			glu.gluPickMatrix((double) clickX, 
							  (double) (viewPort[3] - clickY),
							  1.0, 1.0, viewPort, 0);
			
			final float aspectRatio = (float) SCREEN_WIDTH / (float) SCREEN_HEIGHT;
			gl.glFrustum(-1.0f, 1.0f, -aspectRatio, aspectRatio, 5.0f, DISTANCE_VIEW);
			
			gl.glMatrixMode(GL.GL_MODELVIEW);	
		}
	}
	
	public void tryEndPicking()
	{
		if( inSelectionMode )
		{
			gl.glMatrixMode(GL.GL_PROJECTION);
			gl.glPopMatrix();
			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glFlush();
			
			int numHist = gl.glRenderMode(GL.GL_RENDER);
			processHits(numHist);
			
			inSelectionMode = false;
		}
	}
	
	private void processHits(int numHits)
	{
		if(numHits == 0)
		{
			return;
		}
		
		int selectedNameID = -1;
		float smallestZ = -1.0f;
		boolean isFirstLoop = true;
		int offset = 0;
		
		for( int i = 0; i < numHits; i++)
		{
			int numNames = selectBuffer.get(offset);
			offset++;
			
			float minZ = getDepth(offset);
			offset++;
			
			if( isFirstLoop )
			{
				smallestZ = minZ;
				isFirstLoop = false;
			}
			else {
				
				if(minZ < smallestZ)
				{
					smallestZ = minZ;
				}
			}
			
			float maxZ = getDepth(offset);
			offset++;

			int nameID;
			
			for(int j = 0; j < numNames; j++)
			{
				nameID = selectBuffer.get(offset);
				
				if( j == (numNames-1))
				{
					if(smallestZ == minZ)
					{
						selectedNameID = nameID;
					}
				}

				offset++;
			}
		}
		
		pickObject3D(selectedNameID);
	}
	
	private float getDepth(int offset)
	{
		long depth = (long) selectBuffer.get(offset);
		return (1.0f + ((float) depth / 0x7fffffff));
	}
	
	private void pickObject3D(int nameID)
	{
		for(Object3D obj : mapa3D)
		{
			if( obj.getObjectID() == nameID )
			{
				obj.setBindTexture(false);
			}
		}
	}

	public void tryPushObject(Object3D obj3D)
	{
		if( inSelectionMode )
		{
			gl.glPushName(obj3D.getObjectID());
		}
	}
	
	public void tryPopObject()
	{
		if( inSelectionMode )
		{
			gl.glPopName();
		}
	}
	
	public boolean isInSelectionMode() {
		return inSelectionMode;
	}

	public void setInSelectionMode(boolean inSelectionMode) {
		this.inSelectionMode = inSelectionMode;
	}

	public int getClickX() {
		return clickX;
	}

	public void setClickX(int clickX) {
		this.clickX = clickX;
	}

	public int getClickY() {
		return clickY;
	}

	public void setClickY(int clickY) {
		this.clickY = clickY;
	}
}
