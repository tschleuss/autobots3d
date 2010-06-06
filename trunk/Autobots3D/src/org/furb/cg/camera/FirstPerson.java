package org.furb.cg.camera;

import java.awt.event.KeyEvent;

public class FirstPerson  extends Camera3D {

	
	private final static double SPEED			= 1.5;
	private final static double LOOK_AT_DIST	= 100.0;
	private final static double ANGLE_INCR		= 5.0;
	private final static double HEIGHT_STEP		= 1.0;
	private final static double Z_POS			= 9.0;
	
	private double viewAngle;
	
	private double xStep;
	private double zStep;
	
	public FirstPerson()
	{
		this.initialCamPosition();
	}
	
	@Override
	protected void initialCamPosition() 
	{
		xCamPos = 0;
		yCamPos = 1;
		zCamPos = Z_POS;
		
		viewAngle = -90.0;
		xStep = Math.cos( Math.toRadians(viewAngle) );
		zStep = Math.sin( Math.toRadians(viewAngle) );
		
		xLookAt = (xCamPos + ( LOOK_AT_DIST * xStep ));
		yLookAt = 0;
		zLookAt = zCamPos + ( LOOK_AT_DIST * zStep );
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
			
			case KeyEvent.VK_ESCAPE: 
			{
				System.exit(0);
				break;
			}
		}
		
		xLookAt = (xCamPos + (xStep * LOOK_AT_DIST));
		zLookAt = zCamPos + (zStep * LOOK_AT_DIST);
	}
}
