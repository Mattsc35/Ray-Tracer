package raytracer;

public class Camera {
	Vector cameraPosition;
	Vector cameraDirection;
	Vector cameraRight;
	Vector cameraDown;

	public Camera() {
		this(new Vector(0, 0, 0), new Vector(0, 0, 1));
	}

	public Camera(Vector position, Vector lookAt) {
		this.cameraPosition = position;

		orientCamera(lookAt);
	}

	/**
	 * Orients the camera to look at the point specified.
	 * 
	 * Sets up camera direction, camera relative right, and camera relative down.
	 * 
	 * Precondition: cameraPosition is valid.
	 * 
	 * @param lookAt
	 *            A vector detailing where the camera should be pointing.
	 */
	private void orientCamera(Vector lookAt) {
		if (cameraPosition == null) {
			throw new NullPointerException("Camera position not initialized");
		}
		Vector unitY = new Vector(0, 1, 0);

		Vector difference = cameraPosition.difference(lookAt); //Vector pointing from lookAt to cameraPosition
		this.cameraDirection = difference.negative().normalize();

		this.cameraRight = unitY.crossProduct(cameraDirection).normalize();

		this.cameraDown = cameraRight.crossProduct(cameraDirection);
	}
}
