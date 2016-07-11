package objects;

import raytracer.Color;
import raytracer.Ray;
import raytracer.Vector;

public class Sphere extends RenderableObject {

	private Vector center;
	private double radius;

	public Sphere(Vector center, double radius, Color objectColor) {
		this.center = center;
		this.radius = radius;
		this.objectColor = objectColor;
	}

	public Sphere() {
		this(new Vector(0, 0, 0), 1, Color.BLACK);
	}

	@Override
	public Vector getNormal(Vector point) {
		Vector normalVector = point.addVector(center.negative()).normalize();
		return normalVector;
	}

	@Override
	public double findIntersectionDistance(Ray ray) {
		Vector rayOrigin = ray.getOrigin();
		double rayOriginX = rayOrigin.getX();
		double rayOriginY = rayOrigin.getY();
		double rayOriginZ = rayOrigin.getZ();

		Vector rayDirection = ray.getDirection();
		double rayDirectionX = rayDirection.getX();
		double rayDirectionY = rayDirection.getY();
		double rayDirectionZ = rayDirection.getZ();

		double centerX = center.getX();
		double centerY = center.getY();
		double centerZ = center.getZ();

		double a = 1;
		double b = (2 * (rayOriginX - centerX) * rayDirectionX) + (2 * (rayOriginY - centerY) * rayDirectionY)
				+ (2 * (rayOriginZ - centerZ) * rayDirectionZ);
		double c = (Math.pow(rayOriginX - centerX, 2) + Math.pow(rayOriginY - centerY, 2)
				+ Math.pow(rayOriginZ - centerZ, 2)) - (radius * radius);

		double discriminant = b * b - 4 * c;

		if (discriminant >= 0) {
			/// the ray intersects the sphere
			// the first root
			double root_1 = ((-1 * b - Math.sqrt(discriminant)) / (2 * a));//- 0.000001;

			if (root_1 > 0) {
				// the first root is the smallest positive root
				return root_1;
			}
			else {
				double root_2 = ((-1 * b + Math.sqrt(discriminant)) / (2 * a));//- 0.000001;
				return root_2;
			}
		}
		else {
			// the ray missed the sphere
			return -1;
		}
	}

	public Vector findIntersection(Ray ray) {
		double distance = findIntersectionDistance(ray);
		
		return ray.getOrigin().addVector(ray.getDirection().multiplyVector(distance));
	}

	public Vector getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	public void setCenter(Vector center) {
		this.center = center;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}
