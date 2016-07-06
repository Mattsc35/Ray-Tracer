package objects;

import raytracer.*;

public class Plane extends RenderableObject {

	Vector normal;
	double distance;

	@Override
	public Vector getNormal(Vector point) {
		// TODO Auto-generated method stub
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
	public double findIntersection(Ray ray) {
		Vector rayDirection = ray.getDirection();
		Vector rayOrigin = ray.getOrigin();

		double rayDotNormal = rayDirection.dotProduct(normal);

		if (rayDotNormal == 0) { //Is parallel and will never intersect
			return -1;
		}
		else {
			Vector normalScaledAndNegative = normal.multiplyVector(distance).negative();
			double result = normal.dotProduct(rayOrigin.addVector(normalScaledAndNegative));
			return -1 * (rayDotNormal * result);
		}

	}

}
