package objects;

import raytracer.*;

public abstract class RenderableObject {
	Material objectMaterial;

	public abstract Vector getNormal(Vector point);

	public abstract double findIntersectionDistance(Ray ray);

	public abstract Vector findIntersection(Ray ray);

	public Material getObjectMaterial() {
		return objectMaterial;
	}

	public void setObjectMaterial(Material objectMaterial) {
		this.objectMaterial = objectMaterial;
	}

}
