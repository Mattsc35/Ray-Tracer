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
		Vector cameraStartPosition = new Vector(0, 5, -5);
		Vector cameraLookPosition = new Vector(0, 1, 0);

		mainCamera = new Camera(cameraStartPosition, cameraLookPosition);
	}

	public void setUpScene() {
		sceneToRender = new Scene();

		Vector initialLightPosition = new Vector(2,2, 6);
		Color initialLightColor = Color.WHITE;
		Light initialLight = new Light(initialLightPosition, initialLightColor);
		sceneToRender.addSceneLight(initialLight);

		Vector initialSpherePosition = new Vector(0, 0, 5);
		double initialSphereRadius = 1;
		Color initialSphereColor = new Color(1, 0, 0);
		Sphere theSphere = new Sphere(initialSpherePosition, initialSphereRadius, initialSphereColor);
		sceneToRender.addObject(theSphere);

		initialSpherePosition = new Vector(2, 0, 5);
		initialSphereRadius = 1.0;
		initialSphereColor = Color.GREEN;
		theSphere = new Sphere(initialSpherePosition, initialSphereRadius, initialSphereColor);
		sceneToRender.addObject(theSphere);

		initialSpherePosition = new Vector(0, 2, 5);
		initialSphereRadius = 1.0;
		initialSphereColor = Color.BLUE;
		theSphere = new Sphere(initialSpherePosition, initialSphereRadius, initialSphereColor);
		sceneToRender.addObject(theSphere);
		/*
		 * 
		 * initialSpherePosition = new Vector(1, 0, -6); initialSphereRadius = .50; initialSphereColor = Color.GREEN; theSphere = new
		 * Sphere(initialSpherePosition, initialSphereRadius, initialSphereColor); sceneToRender.addObject(theSphere);
		 */
		/////////////
		Vector initialPlaneNormal = Y_VECTOR.getCopy();
		double initialPlaneDisance = -1;
		Color initialPlaneColor = new Color(0, 1, 0, 2);
		Plane thePlane = new Plane(initialPlaneNormal, initialPlaneDisance, initialPlaneColor);
		sceneToRender.addObject(thePlane);
	}

	public void renderImage() {
		System.out.println("starting render");
		int width = 640;
		int height = 480;
		int numPixels = width * height;
		Color[] pixels = new Color[numPixels];
		int pixelIndex = 0;
		final Color DEFAULT_COLOR = Color.GRAY;

		for (int y = 0; y < height; y++) {
			//for (int x = width - 1; x >= 0; x--) {
			for (int x = 0; x < width; x++) {
				//Creates a ray
				Ray generatedRay = generateRay(x, y, width, height);

				//Gets list of intersections
				ArrayList<RenderableObject> sceneObjects = sceneToRender.getSceneObjects();
				ArrayList<Double> intersections = getIntersections(sceneObjects, generatedRay);
				//Find index of scene Object that intersects the closest

				Color finalColor = determineColor(generatedRay, intersections);
				pixels[pixelIndex] = finalColor;// sceneObjects.get(closestObjectIndex).getColor();

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

	private Color determineColor(Ray generatedRay, ArrayList<Double> intersections) {
		// TODO Auto-generated method stub
		final Color DEFAULT_COLOR = Color.GRAY;

		int closestObjectIndex = getClosestObjctIndex(intersections);
		if (closestObjectIndex == -1) {
			return DEFAULT_COLOR;
		}

		double ambientLight = .1;
		ArrayList<RenderableObject> sceneObjects = sceneToRender.getSceneObjects();
		double closestIntersectionDistance = intersections.get(closestObjectIndex);
		Vector rayOrigin = generatedRay.getOrigin();
		Vector rayDirection = generatedRay.getDirection();
		Vector intersectionCoord = rayOrigin.addVector(rayDirection.multiplyVector(closestIntersectionDistance));

		RenderableObject closestObject = sceneToRender.getSceneObjects().get(closestObjectIndex);
		Vector closestObjectNormal = closestObject.getNormal(intersectionCoord);

		Color objectColor = closestObject.getColor();
		if (objectColor.getAlpha() == 2) {
			objectColor = getCheckerBoardColor(intersectionCoord);
		}

		Color returnColor = objectColor.scalarColor(1);

		int numLights = sceneToRender.getSceneLights().size();
		for (int lightIndex = 0; lightIndex < numLights; lightIndex++) {
			Light currentLight = sceneToRender.getSceneLights().get(lightIndex);
			Vector lightDirection = currentLight.getPosition().addVector(intersectionCoord.negative()).normalize();
			double cosineOfAngle = closestObjectNormal.dotProduct(lightDirection);

			if (cosineOfAngle > 0) {
				boolean isShadowed = false;
				Vector vectorToLight = currentLight.getPosition().addVector(intersectionCoord.negative()).normalize();
				double distanceToLight = vectorToLight.magnitude();
				Ray shadowRay = new Ray(intersectionCoord, vectorToLight);

				ArrayList<Double> shadowIntersections = getIntersections(sceneObjects, shadowRay);
				shadowIntersections.set(closestObjectIndex, -1.0); //TODO see if needed to prevent shadowing with self

				for (int sRayIndex = 0; isShadowed && sRayIndex < shadowIntersections.size(); sRayIndex++) {
					double currentShadowIntersectionDistance = shadowIntersections.get(sRayIndex);
					if (currentShadowIntersectionDistance > 0 && currentShadowIntersectionDistance < distanceToLight) {
						isShadowed = true;
					}
				}

				if (!isShadowed) {
					returnColor = returnColor.multiplyColor(currentLight.getLightColor()).scalarColor(cosineOfAngle);

					if (objectColor.getAlpha() > 0 && objectColor.getAlpha() <= 1) {
						double specular = getSpecular(rayDirection, closestObjectNormal, vectorToLight);
						if (specular > 0) {
							specular = Math.pow(specular, 10);
							returnColor = returnColor.addColor(
									currentLight.getLightColor().scalarColor(specular * objectColor.getAlpha()));
						}
					}
				}
			}
		}

		return returnColor;
	}

	private double getSpecular(Vector rayDirection, Vector closestObjectNormal, Vector vectorToLight) {
		double dot1 = closestObjectNormal.dotProduct(rayDirection.negative());
		Vector scalar1 = closestObjectNormal.multiplyVector(dot1);
		Vector add1 = scalar1.addVector(rayDirection);
		Vector scalar2 = add1.multiplyVector(2);
		Vector add2 = rayDirection.negative().addVector(scalar2);
		Vector reflection_direction = add2.normalize();

		double specular = reflection_direction.dotProduct(vectorToLight);
		return specular;
	}

	private Color getCheckerBoardColor(Vector intersectionCoord) {
		int square = (int) Math.floor(intersectionCoord.getX()) + (int) Math.floor(intersectionCoord.getZ());
		Color returnColor = new Color(0, 0, 0, 2);
		if ((square % 2) == 0) {
			// black tile
			returnColor.setRed(0);
			returnColor.setGreen(0);
			returnColor.setBlue(0);
		}
		else {
			// white tile
			returnColor.setRed(1);
			returnColor.setGreen(1);
			returnColor.setBlue(1);
		}
		return returnColor;
	}

	private ArrayList<Double> getIntersections(ArrayList<RenderableObject> sceneObjects, Ray generatedRay) {
		ArrayList<Double> intersections = new ArrayList<Double>(sceneObjects.size());
		for (int i = 0; i < sceneObjects.size(); i++) {
			double currentIntersection = sceneObjects.get(i).findIntersectionDistance(generatedRay);
			intersections.add(currentIntersection);
		}
		return intersections;
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

	private Ray generateRay(int x, int y, int width, int height) {
		double aspectRatio = ((double) width) / ((double) height);
		double xOffset = getXOffset(x, width, height, aspectRatio);
		double yOffset = getYOffset(y, width, height, aspectRatio);

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
		double dX = (double) x;
		double dWidth = (double) width;
		double dHeight = (double) height;
		if (height < width) {
			//Landscape
			return ((dX + 0.5) / dWidth * aspectRatio) - ((((dWidth - dHeight) / ((double) dHeight)) / 2));
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
