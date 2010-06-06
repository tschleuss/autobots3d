package org.furb.cg.camera;

public abstract class Camera3D {
	
	/**
	 * Posição da camera
	 */
	protected double xCamPos;
	protected double yCamPos;
	protected double zCamPos;
	
	/**
	 * Ponto de observação
	 */
	protected double xLookAt;
	protected double yLookAt;
	protected double zLookAt;
	
	/*
	 * Up vector
	 */
	protected final double xUp = 0.0f;
	protected final double yUp = 1.0f;
	protected final double zUp = 0.0f;
	
	//variaveis de rotacao
	protected double rotX;
	protected double rotY;
	protected double rotZ;
	protected int prevMouseX;
	protected int prevMouseY;
	
	/**
	 * Move a camera de acordo com a tecla clicada
	 */
	public abstract void move(int key, boolean ctrlPressed);
	
	
	/**
	 * Posiciona a câmera em sua localização inicial
	 */
	protected abstract void initialCamPosition();
	
	/**
	 *Reseta a localização inicial da câmera
	 */
	public void resetPosition(){
		this.initialCamPosition();
	}

	public double getXCamPos() {
		return xCamPos;
	}

	public void setXCamPos(double xCamPos) {
		this.xCamPos = xCamPos;
	}

	public double getYCamPos() {
		return yCamPos;
	}

	public void setYCamPos(double yCamPos) {
		this.yCamPos = yCamPos;
	}

	public double getZCamPos() {
		return zCamPos;
	}

	public void setZCamPos(double zCamPos) {
		this.zCamPos = zCamPos;
	}

	public double getXLookAt() {
		return xLookAt;
	}

	public void setXLookAt(double xLookAt) {
		this.xLookAt = xLookAt;
	}

	public double getYLookAt() {
		return yLookAt;
	}

	public void setYLookAt(double yLookAt) {
		this.yLookAt = yLookAt;
	}

	public double getZLookAt() {
		return zLookAt;
	}

	public void setZLookAt(double zLookAt) {
		this.zLookAt = zLookAt;
	}

	public double getRotX() {
		return rotX;
	}

	public void setRotX(double rotX) {
		this.rotX = rotX;
	}

	public double getRotY() {
		return rotY;
	}

	public void setRotY(double rotY) {
		this.rotY = rotY;
	}

	public double getRotZ() {
		return rotZ;
	}

	public void setRotZ(double rotZ) {
		this.rotZ = rotZ;
	}

	public int getPrevMouseX() {
		return prevMouseX;
	}

	public void setPrevMouseX(int prevMouseX) {
		this.prevMouseX = prevMouseX;
	}

	public int getPrevMouseY() {
		return prevMouseY;
	}

	public void setPrevMouseY(int prevMouseY) {
		this.prevMouseY = prevMouseY;
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
