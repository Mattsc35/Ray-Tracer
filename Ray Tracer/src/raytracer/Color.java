package raytracer;

public class Color {

	private double red;
	private double green;
	private double blue;

	public final static Color RED = new Color(1, 0, 0);
	public final static Color GREEN = new Color(0, 1, 0);
	public final static Color BLUE = new Color(0, 0, 1);
	public final static Color BLACK = new Color(0, 0, 0);
	public final static Color WHITE = new Color(1, 1, 1);
	public final static Color YELLOW = new Color(1, 1, 0);
	public static final Color GRAY = new Color(.5, .5, .5);

	public Color(double red, double green, double blue) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
	}

	public double getRed() {
		return red;
	}

	public double getGreen() {
		return green;
	}

	public double getBlue() {
		return blue;
	}

	public double brightness() {
		return ((red + blue + green) / 3.0);
	}

	public Color addColor(Color c) {
		return new Color(c.getRed() + red, c.getGreen() + green, c.getBlue() + blue);
	}

	public Color multiplyColor(Color c) {
		return new Color(c.getRed() * red, c.getGreen() * green, c.getBlue() * blue);
	}

	public Color scalarColor(double scalar) {
		return new Color(red * scalar, green * scalar, blue * scalar);
	}

	public Color averageColor(Color c) {
		return new Color((c.getRed() + red) / 2, (c.getGreen() + green) / 2, (c.getBlue() + blue) / 2);
	}

	public void setRed(double value) {
		this.red = clamp(value, 0, 1);
	}

	public void setGreen(double value) {
		this.green = clamp(value, 0, 1);
	}

	public void setBlue(double value) {
		this.blue = clamp(value, 0, 1);
	}

	private double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}
}
