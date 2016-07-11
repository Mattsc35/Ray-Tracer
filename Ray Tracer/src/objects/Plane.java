package objects;

import raytracer.*;

public class Plane extends RenderableObject {

	Vector normal;
	double distance;

	@Override
	public Vector getNormal(Vector point) {
		return normal;
	}

	public Vector getNormal() {
		return normal;
	}

	public Plane(Vector normal, double distance, Color color) {
		this.normal = normal;
		this.distance = distance;
		this.objectColor = color;
	}

	public Plane() {
		this(new Vector(1, 0, 0), 1, Color.BLUE);
	}

	@Override
	public double findIntersectionDistance(Ray ray) {
		Vector point = normal.normalize().multiplyVector(distance);
		Vector rayOrigin = ray.getOrigin();
		Vector rayDirection = ray.getDirection();
		double denom = normal.negative().dotProduct(rayDirection);
		if (denom > .0001) {
			Vector aaaaa = point.difference(rayOrigin);
			double t = Math.abs(aaaaa.dotProduct(normal) / denom);
			if (t >= 0) {
				return t;
			}
			else {
				return t;
			}
		}
		return -1;
	}

	public Vector findIntersection(Ray ray) {
		double distance = findIntersectionDistance(ray);
		return ray.getOrigin().addVector(ray.getDirection().multiplyVector(distance));
	}

}
