package org.furb.cg.camera;

import java.awt.event.KeyEvent;

public class ThirdPerson extends Camera3D 
{
	private double angle;	
	private double radius;
	
	public ThirdPerson()
	{
		this.angle = Math.PI / 2.0f;
		this.initialCamPosition();
		this.setRadius(100);
	}
	
	/**
	 * Define o raio da rotação
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	protected void initialCamPosition() {

		xCamPos 	= 0;
		yCamPos 	= 20;
		zCamPos 	= 80;
		
		setXLookAt(0);
		yLookAt 	= 0;
		zLookAt 	= 0;
	}
	
	@Override
	public void move(int key, boolean ctrlPressed) 
	{

		switch (key) {
			case KeyEvent.VK_RIGHT:
			{
				this.angle +=0.5f;
				break;
			}
			
			case KeyEvent.VK_LEFT: 
			{
				this.angle -= 0.5f;
				break;
			}
			
		}
		
		this.calcRotation();
	}
	
	private void calcRotation(){
		
		this.xCamPos = this.radius * Math.cos(this.angle);
		this.zCamPos = this.radius * Math.sin(this.angle);
		
	}

}
