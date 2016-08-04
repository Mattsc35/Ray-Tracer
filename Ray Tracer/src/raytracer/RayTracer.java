package raytracer;

import java.util.ArrayList;

import objects.Plane;
import objects.RenderableObject;
import objects.Sphere;

public class RayTracer {
	static final Vector X_VECTOR = new Vector(1, 0, 0);
	static final Vector Y_VECTOR = new Vector(0, 1, 0);
	static final Vector Z_VECTOR = new Vector(0, 0, 1);

	int width = 640;
	int height = 480;
	Camera mainCamera;
	Scene sceneToRender;

	public RayTracer() {
		setUpRayTracer();
		setUpScene();
	}

	public void setUpRayTracer() {
		Vector cameraStartPosition = new Vector(0, 2, -4);
		Vector cameraLookPosition = new Vector(0, 1, 2);

		mainCamera = new Camera(cameraStartPosition, cameraLookPosition);
	}

	public void setUpScene() {
		sceneToRender = new Scene();

		Vector initialLightPosition = new Vector(-1, 2.5, 0);
		Color initialLightColor = new Color(1, 1, 1);
		Light initialLight = new Light(initialLightPosition, initialLightColor);
		sceneToRender.addSceneLight(initialLight);

		initialLightColor = new Color(.6, .6, .6);
		initialLight = new Light(initialLightPosition, initialLightColor);
		//		sceneToRender.addSceneLight(initialLight);

		Vector initialSpherePosition = new Vector(0, 1, 2);
		double initialSphereRadius = 1.0;
		Color initialSphereColor = new Color(1, 0, 0);
		Material initialSphereMaterial = new Material(initialSphereColor, .99, 0);
		Sphere theSphere = new Sphere(initialSpherePosition, initialSphereRadius, initialSphereMaterial);
		sceneToRender.addObject(theSphere);

		initialSpherePosition = new Vector(-2, 1, 1);
		initialSphereRadius = 1.0;
		initialSphereColor = Color.BLUE;
		theSphere = new Sphere(initialSpherePosition, initialSphereRadius, initialSphereColor);
		sceneToRender.addObject(theSphere);

		initialSpherePosition = new Vector(2.5, 2, 3);
		initialSphereRadius = .3;
		initialSphereColor = Color.GREEN;
		theSphere = new Sphere(initialLightPosition, initialSphereRadius, initialSphereColor);
		sceneToRender.addObject(theSphere);

		Vector initialPlaneNormal = Y_VECTOR.getCopy();
		double initialPlaneDisance = -1;
		Color initialPlaneColor = new Color(0, 1, 0);
		Material initialPlaneMaterial = new Material(initialPlaneColor, 0, 0, true);
		Plane thePlane = new Plane(initialPlaneNormal, initialPlaneDisance, initialPlaneMaterial);
		//	sceneToRender.addObject(thePlane);
	}

	public void renderImage() {
		System.out.println("starting render");
		int numPixels = width * height;
		Color[] pixels = new Color[numPixels];
		int pixelIndex = 0;

		for (int y = 0; y < height; y++) {
			//for (int x = width - 1; x >= 0; x--) {
			for (int x = 0; x < width; x++) {
				Ray generatedRay = generateRay(x, y);
				Color finalColor = traceRay(generatedRay, 1);

				pixels[pixelIndex] = finalColor;
				pixelIndex++;
			}
		}
		if (BitmapSaver.saveBitmap(width, height, pixels)) {
			System.out.println("Image rendered");
		}
		else {
			System.out.println("rendering failed");
		}
	}

	private Color traceRay(Ray ray, int depth) {
		//Gets list of intersections
		ArrayList<RenderableObject> sceneObjects = sceneToRender.getSceneObjects();
		ArrayList<Double> intersections = getIntersections(sceneObjects, ray);

		Color finalColor = determineColor(ray, intersections, depth);
		return finalColor;
	}

	private Color determineColor(Ray generatedRay, ArrayList<Double> intersections, int depth) {
		final Color DEFAULT_COLOR = Color.GRAY;
		int closestObjectIndex = getClosestObjectIndex(intersections);
		if (closestObjectIndex == -1) {
			return DEFAULT_COLOR;
		}

		double ambientLight = .3;
		ArrayList<RenderableObject> sceneObjects = sceneToRender.getSceneObjects();
		double closestIntersectionDistance = intersections.get(closestObjectIndex);
		Vector rayOrigin = generatedRay.getOrigin();
		Vector rayDirection = generatedRay.getDirection();
		Vector intersectionCoord = rayOrigin.addVector(rayDirection.multiplyVector(closestIntersectionDistance));
		RenderableObject closestObject = sceneToRender.getSceneObjects().get(closestObjectIndex);
		Vector closestObjectNormal = closestObject.getNormal(intersectionCoord);
		Material closestObjectMaterial = closestObject.getObjectMaterial();
		Color closestObjectColor = closestObjectMaterial.getColor();
		if (closestObjectMaterial.isCheckerBoard()) {
			closestObjectColor = getCheckerBoardColor(intersectionCoord, closestObjectMaterial);
		}
		Color returnColor = closestObjectColor.scalarColor(ambientLight);
		/*
		 * //Reflection from reflectivity if (closestObjectMaterial.getReflection() > 0) { Vector specularReflectionDirection =
		 * getReflectionDirection(closestObjectNormal, rayDirection); Ray reflectionRay = new Ray(intersectionCoord, specularReflectionDirection);
		 * ArrayList<Double> specularReflectionIntersections = getIntersections(sceneObjects, reflectionRay); int closestSpecularReflectedIndex =
		 * getClosestObjectIndex(specularReflectionIntersections); // specularReflectionIntersections.set(closestObjectIndex, -1.0); //TODO see if needed to
		 * prevent shadowing with self
		 * 
		 * if (closestSpecularReflectedIndex != -1) { if (specularReflectionIntersections.get(closestSpecularReflectedIndex) > 0.00) { // determine the position
		 * and direction at the point of intersection with the reflection ray // the ray only affects the color if it reflected off something
		 * 
		 * Vector reflection_intersection_position = intersectionCoord.addVector(specularReflectionDirection
		 * .multiplyVector(specularReflectionIntersections.get(closestSpecularReflectedIndex)));
		 * 
		 * Color reflection_intersection_color = traceRay(reflectionRay, depth - 1); returnColor = returnColor
		 * .addColor(reflection_intersection_color.scalarColor(closestObjectMaterial.getReflection())); } } }
		 */
		int numLights = sceneToRender.getSceneLights().size();
		for (int lightIndex = 0; lightIndex < numLights; lightIndex++) {
			Light currentLight = sceneToRender.getSceneLights().get(lightIndex);
			Vector lightDirection = currentLight.getPosition().addVector(intersectionCoord.negative()).normalize();
			double cosineOfAngle = closestObjectNormal.dotProduct(currentLight.getPosition());

			if (cosineOfAngle > 0) {

				boolean isShadowed = false;
				Vector vectorToLight = currentLight.getPosition().addVector(intersectionCoord.negative()).normalize();
				double distanceToLight = vectorToLight.magnitude();
				Ray shadowRay = new Ray(intersectionCoord, vectorToLight);

				ArrayList<Double> shadowIntersections = getIntersections(sceneObjects, shadowRay);
				shadowIntersections.set(closestObjectIndex, -1.0); //TODO see if needed to prevent shadowing with self

				for (int sRayIndex = 0; !isShadowed && sRayIndex < shadowIntersections.size(); sRayIndex++) {
					double currentShadowIntersectionDistance = shadowIntersections.get(sRayIndex);
					if (currentShadowIntersectionDistance > 0 && currentShadowIntersectionDistance < distanceToLight) {
						isShadowed = true;
					}
				}

				if (!isShadowed) {

					//returnColor = closestObjectColor; //TODO Change
					if (closestObjectMaterial.getSpecular() > 0 && closestObjectMaterial.getSpecular() <= 1) {
						double specular = getSpecular(rayDirection, closestObjectNormal, vectorToLight);
						if (specular > 0) {
							specular = Math.pow(specular, 10);
							returnColor = returnColor.addColor(currentLight.getLightColor()
									.scalarColor(specular * closestObjectMaterial.getSpecular()));
						}
					}
					returnColor = returnColor.multiplyColor(currentLight.getLightColor())
							.scalarColor(ambientLight + ((1.0 - ambientLight) * Math.max(cosineOfAngle, ambientLight)));
					//returnColor.scalarColor(ambientLight + ((1.0 - ambientLight) * cosineOfAngle));

				}
			}
		}

		return returnColor;
	}

	private double getSpecular(Vector rayDirection, Vector closestObjectNormal, Vector vectorToLight) {
		Vector reflection_direction = getReflectionDirection(rayDirection, closestObjectNormal);
		double specular = reflection_direction.dotProduct(vectorToLight);
		return specular;
	}

	private Vector getReflectionDirection(Vector rayDirection, Vector closestObjectNormal) {
		double dot1 = closestObjectNormal.dotProduct(rayDirection.negative());
		Vector scalar1 = closestObjectNormal.multiplyVector(dot1);
		Vector add1 = scalar1.addVector(rayDirection);
		Vector scalar2 = add1.multiplyVector(2);
		Vector add2 = rayDirection.negative().addVector(scalar2);
		Vector reflectionDirection = add2.normalize();
		return reflectionDirection;
	}

	private Color getCheckerBoardColor(Vector intersectionCoord, Material closestObjectMaterial) {
		int square = (int) Math.floor(intersectionCoord.getX()) + (int) Math.floor(intersectionCoord.getZ());
		if ((square % 2) == 0) {
			return closestObjectMaterial.getCheckerBoardColorA();
		}
		else {
			return closestObjectMaterial.getCheckerBoardColorB();
		}
	}

	private ArrayList<Double> getIntersections(ArrayList<RenderableObject> sceneObjects, Ray generatedRay) {
		ArrayList<Double> intersections = new ArrayList<Double>(sceneObjects.size());
		for (int i = 0; i < sceneObjects.size(); i++) {
			double currentIntersection = sceneObjects.get(i).findIntersectionDistance(generatedRay);
			intersections.add(currentIntersection);
		}
		return intersections;
	}

	private int getClosestObjectIndex(ArrayList<Double> intersections) {
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

	private Ray generateRay(int x, int y) {
		double aspectRatio = ((double) width) / ((double) height);
		double xOffset = getXOffset(x, aspectRatio);
		double yOffset = getYOffset(y, aspectRatio);

		Vector cameraRight = mainCamera.cameraRight;
		Vector cameraDown = mainCamera.cameraDown;
		Vector cameraDirection = mainCamera.cameraDirection;

		Vector adjustedXVector = cameraRight.multiplyVector(xOffset - 0.5);
		Vector adjustedYVector = cameraDown.multiplyVector(yOffset - 0.5);

		Vector rayOrigin = mainCamera.cameraPosition.getCopy();
		Vector rayDirection = cameraDirection.addVector(adjustedXVector).addVector(adjustedYVector).normalize();

		return new Ray(rayOrigin, rayDirection);
	}

	private double getXOffset(int x, double aspectRatio) {
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

	private double getYOffset(int y, double aspectRatio) {
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
