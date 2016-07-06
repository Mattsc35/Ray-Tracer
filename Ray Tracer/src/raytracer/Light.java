package raytracer;

public class Light {
	Vector position;
	Color lightColor;
	
	public Light() {
		this(new Vector(0,0,0), Color.WHITE);
	}
	
	public Light(Vector position, Color lightColor) {
		this.position = position;
		this.lightColor = lightColor;
	}

	public Vector getPosition() {
		return position;
	}

	public Color getLightColor() {
		return lightColor;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public void setLightColor(Color lightColor) {
		this.lightColor = lightColor;
	}
}
