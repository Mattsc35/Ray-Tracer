package raytracer;

public class RayTracer {
	
	static final Vector X_VECTOR = new Vector(1, 0, 0);
	static final Vector Y_VECTOR = new Vector(0, 1, 0);
	static final Vector Z_VECTOR = new Vector(0, 0, 1);
	
	Camera mainCamera;

	public RayTracer(){
		
	}
	
	public void setUpScene(){
		Vector cameraStartPosition = new Vector(2,2,2);
		Vector cameraLookPosition = new Vector(0,0,0);
		
		mainCamera = new Camera(cameraStartPosition, cameraLookPosition);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
