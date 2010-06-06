package org.furb.cg.camera;

public abstract class Camera3D {
	
	/**
	 * Posi��o da camera
	 */
	protected double xCamPos;
	protected double yCamPos;
	protected double zCamPos;
	
	/**
	 * Ponto de observa��o
	 */
	private double xLookAt;
	protected double yLookAt;
	protected double zLookAt;
	
	/*
	 * Up vector
	 */
	private final double xUp = 0.0f;
	private final double yUp = 1.0f;
	private final double zUp = 0.0f;
	
	/**
	 * Move a camera de acordo com a tecla clicada
	 */
	public abstract void move(int key, boolean ctrlPressed);
	
	
	/**
	 * Posiciona a c�mera em sua localiza��o inicial
	 */
	protected abstract void initialCamPosition();
	
	/**
	 *Reseta a localiza��o inicial da c�mera
	 */
	public void resetPosition(){
		this.initialCamPosition();
	}
	
	public double getXCamPos() {
		return xCamPos;
	}
	public double getYCamPos() {
		return yCamPos;
	}

	public double getZCamPos() {
		return zCamPos;
	}

	public void setXLookAt(double xLookAt) {
		this.xLookAt = xLookAt;
	}
	
	public double getXLookAt() {
		return xLookAt;
	}

	public double getYLookAt() {
		return yLookAt;
	}

	public void setZLookAt(double zLookAt) {
		this.zLookAt = zLookAt;
	}	
	
	public double getZLookAt() {
		return zLookAt;
	}

	public double getXUp() {
		return xUp;
	}

	public double getYUp() {
		return yUp;
	}

	public double getZUp() {
		return zUp;
	}
}
