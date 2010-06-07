package org.furb.cg.camera;

import java.awt.event.KeyEvent;

public class FreeCam extends Camera3D {
	
	private final static double SPEED			= 1.5;
	private final static double LOOK_AT_DIST	= 100.0;
	private final static double ANGLE_INCR		= 5.0;
	private final static double HEIGHT_STEP		= 1.0;
	private final static double Z_POS			= 9.0;
	
	private double viewAngle;
	private double xStep;
	private double zStep;
	
	public FreeCam()
	{
		this.initialCamPosition();
	}
	
	@Override
	protected void initialCamPosition() 
	{
		rotX		= 17.999998927116394;
		rotY		= -225.00000250339508;
		rotZ		= 0.0;
		xCamPos 	= -5.87830463590729E-15;
		yCamPos 	= 12.0;
		zCamPos 	= 105.0;
		xLookAt 	= 2.449293598294758E-16;
		yLookAt 	= 11.0;
		zLookAt 	= 5.0;
		xStep		= 6.123233995736766E-17;
		zStep		= -1.0;
		viewAngle	= -90.0;
		prevMouseX	= 553;
		prevMouseY	= 459;
	}
	
	@Override
	public void move(int key, boolean ctrlPressed) {

		switch ( key ) 
		{
			case KeyEvent.VK_LEFT: 
			{
				if( ctrlPressed) {
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
				if( ctrlPressed) {
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
				if( ctrlPressed ) {
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
				if( ctrlPressed ) {
					yCamPos -= HEIGHT_STEP;
					yLookAt -= HEIGHT_STEP;
 				}
				else {
					xCamPos -= xStep * SPEED;
					zCamPos -= zStep * SPEED;
				}
				
				break;
			}
		}
		
		xLookAt = xCamPos + (xStep * LOOK_AT_DIST);
		zLookAt = zCamPos + (zStep * LOOK_AT_DIST);
	}
}
