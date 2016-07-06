package objects;
import raytracer.*;

public abstract class RenderableObject {
	Color objectColor;
	
	public abstract Vector getNormal(Vector point);
	public abstract double findIntersection(Ray ray);
	
	public void setColor(Color color){
		objectColor = color;
	}
	public Color getColor(){
		return objectColor;
	}
}
