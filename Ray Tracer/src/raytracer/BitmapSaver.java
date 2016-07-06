package raytracer;

import java.io.FileOutputStream;

public class BitmapSaver {

	//File Header
	private final static int FILE_HEADER_SIZE = 14; // Size of file header in bytes
	private final static int INFO_HEADER_SIZE = 40; // Size of info header in bytes
	private static int totalHeaderSize = FILE_HEADER_SIZE + INFO_HEADER_SIZE;
	private static byte bitmapHeaderConstants[] = { 'B', 'M' }; // Constants
	private static int totalFileSize = 0; // Total size of file in bytes
	private static int res1 = 0; // Reserved, constant
	private static int res2 = 0; // Reserved, constant

	//Info Header
	private static int planes = 1; // Number of planes. Keeps at 1
	private static int imageWidth = 0;// Image width in pixels
	private static int imageHeight = 0;// Image height in pixels
	private static int bitCount = 24; // Bits per pixels
	private static int compression = 0; // Leave 0 for 24 bit pixels
	private static int sizeImage = 0; // Size of image in bytes
	private static int xPixelsPerMeter = 0x0; // Horizontal pixels per meter
	private static int yPixelsPerMeter = 0x0; // Vertical pixels per meter
	private static int colorIndex = 0; //color indexes used
	private static int importantColors = 0; // Important colors. 0 == all colors important

	public static boolean saveBitmap(int width, int height, Color[] pixels) {
		return saveBitmap("RenderedImage.bmp", width, height, pixels);
	}

	public static boolean saveBitmap(Color[][] pixels) {
		return saveBitmap("RenderedImage.bmp", pixels);
	}

	private static boolean saveBitmap(String fileName, Color[][] pixels) {
		Color[] pixelArray = matrixToArray(pixels);
		return saveBitmap(fileName, pixels.length, pixels[0].length, pixelArray);
	}

	
	
	public static boolean saveBitmap(String fileName, int width, int height, Color[] pixels) {
		imageWidth = width;
		imageHeight = height;
		int padding = (4 - ((width * 3) % 4)) * height;
		sizeImage = ((width * height) * 3) + padding;
		totalFileSize = sizeImage + totalHeaderSize;
		
		try {
			FileOutputStream outputStream = new FileOutputStream(fileName);
			writeHeader(outputStream);
			for (int i = 0; i < pixels.length; i++) {
				outputStream.write((byte) ((int) (pixels[i].getBlue() * 255)));
				outputStream.write((byte) ((int) (pixels[i].getGreen() * 255)));
				outputStream.write((byte) ((int) (pixels[i].getRed() * 255)));
			}

			outputStream.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private static void writeHeader(FileOutputStream outputStream) throws Exception {
		writeBitmapFileHeader(outputStream);
		writeBitmapInfoHeader(outputStream);
	}

	private static void writeBitmapFileHeader(FileOutputStream outputStream) throws Exception {
		outputStream.write(bitmapHeaderConstants);
		outputStream.write(intToFourBytes(totalFileSize));
		outputStream.write(intToTwoBytes(res1));
		outputStream.write(intToTwoBytes(res2));
		outputStream.write(intToFourBytes(totalHeaderSize));
	}

	private static void writeBitmapInfoHeader(FileOutputStream outputStream) throws Exception {
		outputStream.write(intToFourBytes(INFO_HEADER_SIZE));
		outputStream.write(intToFourBytes(imageWidth));
		outputStream.write(intToFourBytes(imageHeight));
		outputStream.write(intToTwoBytes(planes));
		outputStream.write(intToTwoBytes(bitCount));
		outputStream.write(intToFourBytes(compression));
		outputStream.write(intToFourBytes(sizeImage));
		outputStream.write(intToFourBytes(xPixelsPerMeter));
		outputStream.write(intToFourBytes(yPixelsPerMeter));
		outputStream.write(intToFourBytes(colorIndex));
		outputStream.write(intToFourBytes(importantColors));
	}

	private static byte[] intToTwoBytes(int val) {
		byte returnArray[] = new byte[2];
		returnArray[0] = (byte) (val & 0x00FF);
		returnArray[1] = (byte) ((val >> 8) & 0x00FF);

		return returnArray;
	}

	private static byte[] intToFourBytes(int val) {
		byte returnArray[] = new byte[4];
		returnArray[0] = (byte) (val & 0x00FF);
		returnArray[1] = (byte) ((val >> 8) & 0x000000FF);
		returnArray[2] = (byte) ((val >> 16) & 0x000000FF);
		returnArray[3] = (byte) ((val >> 24) & 0x000000FF);

		return returnArray;
	}

	private static Color[] matrixToArray(Color[][] matrix) {
		Color[] returnArray = new Color[matrix.length * matrix[0].length];
		int index = 0;
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				returnArray[index] = matrix[row][col];
				index++;
			}
		}

		return returnArray;
	}
}
