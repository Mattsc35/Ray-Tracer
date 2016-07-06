package raytracer;

import java.util.ArrayList;

import objects.RenderableObject;

public class Scene {
	ArrayList<RenderableObject> sceneObjects;
	Light sceneLight;

	public Scene(Light sceneLight) {
		sceneObjects = new ArrayList<RenderableObject>();
		this.sceneLight = sceneLight;
	}

	public Scene() {
		this(new Light(new Vector(5, 5, 5), Color.WHITE));
	}

	public void addObject(RenderableObject newObject) {
		sceneObjects.add(newObject);
	}

	public ArrayList<RenderableObject> getSceneObjects() {
		return sceneObjects;
	}

	public Light getSceneLight() {
		return sceneLight;
	}

	public void setSceneLight(Light sceneLight) {
		this.sceneLight = sceneLight;
	}

}
