package raytracer;

import java.util.ArrayList;

import objects.Plane;
import objects.RenderableObject;
import objects.Sphere;

public class RayTracer {

	static final Vector X_VECTOR = new Vector(1, 0, 0);
	static final Vector Y_VECTOR = new Vector(0, 1, 0);
	static final Vector Z_VECTOR = new Vector(0, 0, 1);

	Camera mainCamera;
	Scene sceneToRender;

	public RayTracer() {
		setUpRayTracer();
		setUpScene();
	}

	public void setUpRayTracer() {
		Vector cameraStartPosition = new Vector(2, 2, 2);
		Vector cameraLookPosition = new Vector(0, 0, 0);

		mainCamera = new Camera(cameraStartPosition, cameraLookPosition);
	}

	public void setUpScene() {
		sceneToRender = new Scene();

		Vector initialLightPosition = new Vector(-3, 7, -3);
		Color initialLightColor = Color.WHITE;
		Light initialLight = new Light(initialLightPosition, initialLightColor);
		sceneToRender.setSceneLight(initialLight);

		Vector initialSpherePosition = new Vector(2, 1, 2);
		double initialSphereRadius = 1.0;
		Color initialSphereColor = Color.RED;
		Sphere theSphere = new Sphere(initialSpherePosition, initialSphereRadius, initialSphereColor);
		sceneToRender.addObject(theSphere);

		Vector initialPlaneNormal = Y_VECTOR.getCopy();
		double initialPlaneDisance = -1;
		Color initialPlaneColor = Color.GRAY;
		Plane thePlane = new Plane(initialPlaneNormal, initialPlaneDisance, initialPlaneColor);
		sceneToRender.addObject(thePlane);
	}

	public void renderImage() {
		int width = 640;
		int height = 480;
		int numPixels = width * height;
		double aspectRatio = ((double) width) / ((double) height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				
				double xOffset = getXOffset(x, width, height, aspectRatio);
				double yOffset = getYOffset(y, width, height, aspectRatio);
				Ray generatedRay = generateRay(xOffset, yOffset);
				
				ArrayList<RenderableObject> sceneObjects = sceneToRender.getSceneObjects();
				ArrayList<Double> intersections = new ArrayList<Double>(sceneObjects.size());
				
				for (int i = 0; i < sceneObjects.size(); i++) {
					double currentIntersection = sceneObjects.get(i).findIntersection(generatedRay);
					intersections.add(currentIntersection);
				}

			}
		}
	}

	private Ray generateRay(double xOffset, double yOffset) {
		Vector cameraRight = mainCamera.cameraRight;
		Vector cameraDown = mainCamera.cameraDown;
		Vector cameraDirection = mainCamera.cameraDirection;

		Vector adjustedXVector = cameraRight.multiplyVector(xOffset - 0.5);
		Vector adjustedYVector = cameraDown.multiplyVector(yOffset - 0.5);
		
		Vector rayOrigin = mainCamera.cameraPosition.getCopy();
		Vector rayDirection = cameraDirection.addVector(adjustedXVector).addVector(adjustedYVector).normalize();

		return new Ray(rayOrigin, rayDirection);
	}

	private double getXOffset(int x, int width, int height, double aspectRatio) {
		if (height < width) {
			//Landscape
			return ((x + 0.5) / width * aspectRatio) - ((((width - height) / ((double) height)) / 2));
		}
		else if (height > width) {
			//Portrait
			return (x + 0.5) / width;
		}
		else {
			//Square
			return (x + 0.5) / width;
		}
	}

	private double getYOffset(int y, int width, int height, double aspectRatio) {
		if (height < width) {
			//Landscape
			return ((height - y) + 0.5) / height;
		}
		else if (height > width) {
			//Portrait
			return (((height - y) + 0.5) / height) / aspectRatio;
		}
		else {
			//Square
			return ((height - y) + 0.5) / height;
		}
	}

}
