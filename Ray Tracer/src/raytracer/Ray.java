package raytracer;

public class Ray {
	private Vector origin;
	private Vector direction;

	public Ray(Vector origin, Vector direction) {
		setOrigin(origin);
		setDirection(direction);
	}

	public Ray() {
		this(new Vector(0, 0, 0), new Vector(1, 0, 0));
	}

	public Vector getOrigin() {
		return origin;
	}

	public Vector getDirection() {
		return direction;
	}

	public void setOrigin(Vector origin) {
		this.origin = origin;
	}

	public void setDirection(Vector direction) {
		this.direction = direction.normalize(); //TODO see if should normalize
	}

}
