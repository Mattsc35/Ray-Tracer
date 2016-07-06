package raytracer;

class Vector{
	double x;
	double y;
	double z;

	public Vector(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	public Vector() {
		this(0, 0, 0);
	}

	public Vector normalize() {
		double magnitude = this.magnitude();

		return new Vector((x / magnitude), (y / magnitude), (z / magnitude));
	}

	public double magnitude() {
		return Math.sqrt((x * x) + (y * y) + (z * z));
	}

	public Vector negative() {
		return new Vector(-x, -y, -z);
	}

	public double dotProduct(Vector vec) {
		return (x * vec.getX()) + (y * vec.getY()) + (z * vec.getZ());
	}

	public Vector crossProduct(Vector vec) {
		double newX = (y * vec.getZ()) - (z * vec.getY());
		double newY = (z * vec.getX()) - (x * vec.getZ());
		double newZ = (x * vec.getY()) - (y * vec.getX());
		return new Vector(newX, newY, newZ);
	}

	public Vector addVector(Vector vec) {
		return new Vector((x + vec.getX()), (y + vec.getY()), (z + vec.getZ()));
	}

	public Vector difference(Vector vec) {
		return new Vector((x - vec.getX()), (y - vec.getY()), (z - vec.getZ()));
	}

	public Vector multiplyVector(double scalar) {
		return new Vector(x * scalar, y * scalar, z * scalar);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append('[');
		//X
		sb.append('(');
		sb.append(x);
		sb.append(')');
		sb.append(", ");

		//Y
		sb.append('(');
		sb.append(y);
		sb.append(')');
		sb.append(", ");

		//Z
		sb.append('(');
		sb.append(z);
		sb.append(')');

		sb.append(']');
		return sb.toString();
	}
}
