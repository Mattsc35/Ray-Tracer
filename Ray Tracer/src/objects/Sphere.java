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
		this(new Vector(0,0,0), 1, Color.BLACK);
	}
	
	@Override
	public Vector getNormal(Vector point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double findIntersection(Ray ray) {
		// TODO Auto-generated method stub
		return 0;
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
