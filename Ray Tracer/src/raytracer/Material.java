package raytracer;

public class Material {
	private Color color;
	private double specular;
	private double reflection;
	private boolean checkerBoard;
	private Color checkerBoardColorA = Color.BLACK;
	private Color checkerBoardColorB = Color.WHITE;

	public Material(Color materialColor, double reflection, double specular, boolean checkerBoard) {
		this.color = materialColor;
		this.reflection = reflection;
		this.specular = specular;
		this.checkerBoard = checkerBoard;
	}

	public Material(Color materialColor, double reflection, double specular) {
		this(materialColor, reflection, reflection, false);
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
		this.specular = specular;
	}

	public void setReflection(double reflection) {
		this.reflection = reflection;
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

}
