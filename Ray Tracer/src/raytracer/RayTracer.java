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
		Vector cameraStartPosition = new Vector(0, 1, -4);
		Vector cameraLookPosition = new Vector(0, 0, 0);

		mainCamera = new Camera(cameraStartPosition, cameraLookPosition);
	}

	public void setUpScene() {
		sceneToRender = new Scene();

		Vector initialLightPosition = new Vector(-3, 7, -3);
		Color initialLightColor = Color.WHITE;
		Light initialLight = new Light(initialLightPosition, initialLightColor);
		sceneToRender.setSceneLight(initialLight);

		Vector initialSpherePosition = new Vector(1.75, 2.25, 0);
		double initialSphereRadius = 1.0;
		Color initialSphereColor = Color.RED;
		Sphere theSphere = new Sphere(initialSpherePosition, initialSphereRadius, initialSphereColor);
		sceneToRender.addObject(theSphere);
		/////////////
		Vector initialPlaneNormal = Y_VECTOR.getCopy();
		double initialPlaneDisance = -6;
		Color initialPlaneColor = Color.GREEN;
		Plane thePlane = new Plane(initialPlaneNormal, initialPlaneDisance, initialPlaneColor);
	//	sceneToRender.addObject(thePlane);
	}

	public void renderImage() {
		System.out.println("starting render");
		int width = 640;
		int height = 480;
		int numPixels = width * height;
		double aspectRatio = ((double) width) / ((double) height);
		int intersectedCount = 0;
		int nonIntersectedCount = 0;
		Color[] pixels = new Color[numPixels];
		int pixelIndex = 0;
		final Color DEFAULT_COLOR = Color.GRAY;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				//Creates a ray
				double xOffset = getXOffset(x, width, height, aspectRatio);
				double yOffset = getYOffset(y, width, height, aspectRatio);
				Ray generatedRay = generateRay(xOffset, yOffset);

				//Gets list of intersections
				ArrayList<RenderableObject> sceneObjects = sceneToRender.getSceneObjects();
				ArrayList<Double> intersections = new ArrayList<Double>(sceneObjects.size());
				for (int i = 0; i < sceneObjects.size(); i++) {
					double currentIntersection = sceneObjects.get(i).findIntersectionDistance(generatedRay);
					intersections.add(currentIntersection);
				}
				int closestObjectIndex = getClosestObjctIndex(intersections);
				if (closestObjectIndex != -1) {
					pixels[pixelIndex] = sceneObjects.get(closestObjectIndex).getColor();
				}
				else {
					pixels[pixelIndex] = DEFAULT_COLOR;
				}
				if(y == 50){
					pixels[pixelIndex] = Color.YELLOW;
				}
				/*
				 * if (closestObjectIndex != -1) { intersectedCount++; } else{ nonIntersectedCount++; }
				 */
				pixelIndex++;
			}
		}
		if (BitmapSaver.saveBitmap(width, height, pixels)) {
			System.out.println("Image rendered");
		}
		else {
			System.out.println("rendering failed");
		}

		//System.out.println("intersected " + intersectedCount);
		//	System.out.println(
		//			"Percent intersected: " + (((double) intersectedCount) / (intersectedCount + nonIntersectedCount)));
	}

	private int getClosestObjctIndex(ArrayList<Double> intersections) {
		int index = -1;
		double closestIntersection = Double.MAX_VALUE;
		for (int i = 0; i < intersections.size(); i++) {
			double currentIntersection = intersections.get(i);
			if ((currentIntersection > 0) && (currentIntersection < closestIntersection)) {
				closestIntersection = currentIntersection;
				index = i;
			}
		}
		return index;
	}

	private Ray generateRay(double xOffset, double yOffset) {
		Vector cameraRight = mainCamera.cameraRight;
		Vector cameraDown = mainCamera.cameraDown;
		Vector cameraDirection = mainCamera.cameraDirection;
		//Vector cam_ray_direction = cameraDirection.addVector(cameraRight.multiplyVector(xOffset - 0.5).addVector(cameraDown.multiplyVector(yOffset - 0.5))).normalize();

		Vector adjustedXVector = cameraRight.multiplyVector(xOffset - 0.5);
		Vector adjustedYVector = cameraDown.multiplyVector(yOffset - 0.5);

		Vector rayOrigin = mainCamera.cameraPosition.getCopy();
		Vector rayDirection = cameraDirection.addVector(adjustedXVector).addVector(adjustedYVector).normalize();

		return new Ray(rayOrigin, rayDirection);
	}

	private double getXOffset(int x, int width, int height, double aspectRatio) {
		double dX = (double) x;
		double dWidth = (double) width;
		double dHeight = (double) height;
		if (height < width) {
			//Landscape
			return ((dX + 0.5) / dWidth * aspectRatio) - ((((dWidth - height) / ((double) height)) / 2));
		}
		else if (height > dWidth) {
			//Portrait
			return (dX + 0.5) / dWidth;
		}
		else {
			//Square
			return (dX + 0.5) / dWidth;
		}
	}

	private double getYOffset(int y, int width, int height, double aspectRatio) {
		double dY = (double) y;
		double dWidth = (double) width;
		double dHeight = (double) height;
		if (height < width) {
			//Landscape
			return ((dHeight - dY) + 0.5) / dHeight;
		}
		else if (height > width) {
			//Portrait
			return (((dHeight - dY) + 0.5) / dHeight) / aspectRatio;
		}
		else {
			//Square
			return ((dHeight - dY) + 0.5) / dHeight;
		}
	}

}
