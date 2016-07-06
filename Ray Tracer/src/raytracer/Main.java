package raytracer;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		testBitmapWriting();
	}

	private static void testBitmapWriting() {
		int width = 100;
		int height = 100;
		Color[][] pixels = new Color[width][height];
		Random rand = new Random();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double red = (x * (((double)x)/width))/100;
				double green = (x * (((double)y)/height))/100;
				double blue = rand.nextDouble();

				pixels[x][y] = new Color(red, green, blue);
				pixels[x][y] = new Color(1,0, 1);
			}
		}

		if (BitmapSaver.saveBitmap(pixels)) {
			System.out.println("Saved Bitmap.");
		}
		else {
			System.out.println("Error saving bitmap.");
		}
	}

}
