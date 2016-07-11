package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import raytracer.Vector;

public class VectorTest {

	final double EPSILON = .0000001;

	@Test
	public void testEquals() {
		Vector a = new Vector(1, 2, 3);
		String str = "test";
		assertFalse(a.equals(str));

		Vector b = new Vector(4, 5, 6);
		assertFalse(a.equals(b));

		b.setX(1);
		b.setY(2);
		b.setZ(3);
		assertTrue(a.equals(b));

		Vector c = new Vector(1, 2, 3);
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		assertTrue(b.equals(c));
		assertTrue(a.equals(c));

		assertEquals(a, b);
		assertEquals(b, c);
		assertEquals(a, c);
	}

	@Test
	public void testNegative() {
		Vector a = new Vector(1, 2, 3);
		Vector b = new Vector(-1, -2, -3);

		assertEquals(a.negative(), b);
		assertTrue((a.negative()).equals(new Vector(-1, -2, -3)));
	}

	@Test
	public void testMagnitude() {
		Vector a = new Vector(1, 0, 0);
		assertTrue(Math.abs(a.magnitude() - 1) < EPSILON);
		a = new Vector(0, 1, 0);
		assertTrue(Math.abs(a.magnitude() - 1) < EPSILON);
		a = new Vector(0, 0, 1);
		assertTrue(Math.abs(a.magnitude() - 1) < EPSILON);

		Vector b = new Vector(3, 4, 0);
		assertTrue(Math.abs(b.magnitude() - 5) < EPSILON);

		Vector c = new Vector(34.4, 23, 11.8);
		double magnitude = Math.sqrt((34.4 * 34.4) + (23 * 23) + (11.8 * 11.8));
		assertTrue(Math.abs(c.magnitude() - magnitude) < EPSILON);
	}

	@Test
	public void testNormalize() {
		Vector a = new Vector(1, 0, 0);
		assertTrue(a.normalize().equals(new Vector(1, 0, 0)));

		Vector b = new Vector(3, 2, -1);
		Vector norm = new Vector(3 / Math.sqrt(14), Math.sqrt(2.0 / 7.0), (-1 / Math.sqrt(14)));
		System.out.println(b.normalize().toString());
		System.out.println(norm.toString());
		assertTrue((b.normalize()).equals(norm));
	}

	@Test
	public void testDotProduct() {
		Vector a = new Vector(6.5, 5.25, 2.91);
		Vector b = new Vector(12, 4.32, 7.77);
		assertTrue(Math.abs(a.dotProduct(b) - 123.2907) < EPSILON);

	}

	@Test
	public void testCrossProduct() {
		Vector a = new Vector(6.5, 5.25, 2.91);
		Vector b = new Vector(12, 4.32, 7.77);
		Vector crossProduct = new Vector(28.2213, -15.585, -34.92);
		assertEquals(crossProduct, a.crossProduct(b));
	}

}
