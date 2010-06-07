package org.furb.cg.camera;


public class CameraManager {

	private Camera3D[] cameras;
	private static final int MAX = 3;
	private int atual = 0;
	
	public CameraManager() 
	{
		super();
		cameras = new Camera3D[MAX];
		cameras[0] = new FreeCam();
		cameras[1] = new FirstPerson();
		cameras[2] = new ThirdPerson();
	}
	
	public Camera3D atual()
	{
		return cameras[atual];
	}
	
	public void changeCamera()
	{
		atual = (atual + 1) % MAX;
	}
}
