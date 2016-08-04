package raytracer;

public class Material {
	private Color color;
	private double specular;
	private double reflection;
	private boolean checkerBoard;
	private Color checkerBoardColorA = Color.BLACK;
	private Color checkerBoardColorB = Color.WHITE;

	public Material(Color materialColor, double reflection, double specular, boolean checkerBoard) {
		setColor(materialColor);
		setReflection(reflection);
		setSpecular(specular);
		setCheckerBoard(checkerBoard);
	}

	public Material(Color materialColor, double reflection, double specular) {
		this(materialColor, reflection, specular, false);
	}

	public Material(Color materialColor) {
		this(materialColor, 0, 0, false);
	}

	public Material() {
		this(Color.WHITE, 0, 0, false);
	}

	public Color getColor() {
		return color;
	}

	public double getSpecular() {
		return specular;
	}

	public double getReflection() {
		return reflection;
	}

	public boolean isCheckerBoard() {
		return checkerBoard;
	}

	public Color getCheckerBoardColorA() {
		return checkerBoardColorA;
	}

	public Color getCheckerBoardColorB() {
		return checkerBoardColorB;
	}

	public void setColor(Color materialColor) {
		this.color = materialColor;
	}

	public void setSpecular(double specular) {
		this.specular = clamp(specular, 0, 1);
	}

	public void setReflection(double reflection) {
		this.reflection = clamp(reflection, 0, 1);
	}

	public void setCheckerBoard(boolean checkerBoard) {
		this.checkerBoard = checkerBoard;
	}

	public void setCheckerBoardColorA(Color checkerBoardColorA) {
		this.checkerBoardColorA = checkerBoardColorA;
	}

	public void setCheckerBoardColorB(Color checkerBoardColorB) {
		this.checkerBoardColorB = checkerBoardColorB;
	}

	private double clamp(double val, double min, double max) {
		return Math.max(min, Math.min(max, val));
	}

}
