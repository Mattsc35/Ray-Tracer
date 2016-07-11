package raytracer;

import java.util.ArrayList;

import objects.RenderableObject;

public class Scene {
	ArrayList<RenderableObject> sceneObjects;
	ArrayList<Light> sceneLights;

	public Scene(Light sceneLight) {
		sceneObjects = new ArrayList<RenderableObject>();
		sceneLights = new ArrayList<Light>();
		sceneLights.add(sceneLight);
	}

	public Scene() {
		sceneObjects = new ArrayList<RenderableObject>();
		sceneLights = new ArrayList<Light>();
	}

	public void addObject(RenderableObject newObject) {
		sceneObjects.add(newObject);
	}

	public ArrayList<RenderableObject> getSceneObjects() {
		return sceneObjects;
	}

	public ArrayList<Light> getSceneLights() {
		return sceneLights;
	}

	public void setSceneLights(ArrayList<Light> sceneLights) {
		this.sceneLights = sceneLights;
	}

	public void addSceneLight(Light sceneLight) {
		sceneLights.add(sceneLight);
	}

}
