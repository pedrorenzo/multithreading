package thread.optimization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The idea of ​​this example is to take the original image of flowers, where we
 * have purple and white flowers and make them all purple, using single thread
 * and also using multithreads.
 * 
 * It is important to note that if the number of threads (in the multithreaded
 * scenario) value is equal to 1, it will actually be slower than if I run with
 * single thread. This is due to the overhead of creating the thread. We should
 * also note that adding a large number of threads (again, in the multithreaded
 * scenario) decreases performance, as we may not use the hardware in the best
 * possible way, causing context changes (one thread having to share CPU with
 * another).
 * 
 * @author pedrorenzo
 */
public class ThreadOptimizationForLatency {
	public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
	public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

	public static void main(String[] args) throws IOException {

		final BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
		final BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		// Single thread:
		long startTime = System.currentTimeMillis();

		recolorSingleThreaded(originalImage, resultImage);

		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;

		final File outputFile = new File(DESTINATION_FILE);
		ImageIO.write(resultImage, "jpg", outputFile);
		System.out.println("The duration for recolorSingleThreaded was: " + String.valueOf(duration));

		// Multithread:
		startTime = System.currentTimeMillis();

		// If you will change the numberOfThreads value, please look at the class comment :)
		final int numberOfThreads = 4;
		recolorMultithreaded(originalImage, resultImage, numberOfThreads);

		endTime = System.currentTimeMillis();
		duration = endTime - startTime;

		ImageIO.write(resultImage, "jpg", outputFile);
		System.out.println("The duration for recolorMultithreaded was: " + String.valueOf(duration));
	}

	/**
	 * Re-color the image, dividing it by height according to the number of threads,
	 * where each thread is responsible for re-coloring its slice.
	 * 
	 * @param originalImage
	 * @param resultImage
	 * @param numberOfThreads
	 */
	public static void recolorMultithreaded(final BufferedImage originalImage, final BufferedImage resultImage,
			final int numberOfThreads) {
		final List<Thread> threads = new ArrayList<>();
		final int width = originalImage.getWidth();
		final int height = originalImage.getHeight() / numberOfThreads;

		for (int i = 0; i < numberOfThreads; i++) {
			final int threadMultiplier = i;

			final Thread thread = new Thread(() -> {
				final int xOrigin = 0;
				// The height to start re-coloring varies according to the slice that this
				// thread is responsible for.
				final int yOrigin = height * threadMultiplier;

				recolorImage(originalImage, resultImage, xOrigin, yOrigin, width, height);
			});

			threads.add(thread);
		}

		for (Thread thread : threads) {
			thread.start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Re-color the image starting from the left top using single thread.
	 * 
	 * @param originalImage
	 * @param resultImage
	 */
	public static void recolorSingleThreaded(final BufferedImage originalImage, final BufferedImage resultImage) {
		recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
	}

	/**
	 * Goes through the image like a matrix and re-color each pixel.
	 * 
	 * @param originalImage
	 * @param resultImage
	 * @param leftCorner
	 * @param topCorner
	 * @param width
	 * @param height
	 */
	public static void recolorImage(final BufferedImage originalImage, final BufferedImage resultImage,
			final int leftCorner, final int topCorner, final int width, final int height) {
		for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
			for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
				recolorPixel(originalImage, resultImage, x, y);
			}
		}
	}

	/**
	 * Re-color the pixel x,y from the original image to result image, trying to
	 * make this pixel turn purple.
	 * 
	 * @param originalImage
	 * @param resultImage
	 * @param x
	 * @param y
	 */
	public static void recolorPixel(final BufferedImage originalImage, final BufferedImage resultImage, final int x,
			final int y) {
		final int rgb = originalImage.getRGB(x, y);

		final int red = getRed(rgb);
		final int green = getGreen(rgb);
		final int blue = getBlue(rgb);

		final int newRed;
		final int newGreen;
		final int newBlue;

		if (isShadeOfGray(red, green, blue)) {
			// Purple is a combination of red and blue
			newRed = Math.min(255, red + 10);
			newGreen = Math.max(0, green - 80);
			newBlue = Math.max(0, blue - 20);
		} else {
			newRed = red;
			newGreen = green;
			newBlue = blue;
		}
		final int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
		setRGB(resultImage, x, y, newRGB);
	}

	/**
	 * Assign the color value to the image.
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param rgb
	 */
	public static void setRGB(final BufferedImage image, final int x, final int y, final int rgb) {
		image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
	}

	/**
	 * Validate if the colors are shade of gray. In other words, if no one is
	 * stronger than the rest.
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @return <code>true</code> if it is shade of gray and <code>false</code> if
	 *         not.
	 */
	public static boolean isShadeOfGray(final int red, final int green, final int blue) {
		return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;
	}

	/**
	 * Create a RGB from the colors.
	 * 
	 * @param red
	 * @param green
	 * @param blue
	 * @return the RGB created.
	 */
	public static int createRGBFromColors(final int red, final int green, final int blue) {
		int rgb = 0;

		// Bit-wise OR
		rgb |= blue;
		rgb |= green << 8;
		rgb |= red << 16;

		// 255 in hexadecimal
		rgb |= 0xFF000000;

		return rgb;
	}

	/**
	 * Takes a RGB value and extracts just the blue value out of the pixel applying
	 * a bit mask on the pixel making all the components 0 except for the rightmost
	 * byte, which is exactly the blue component.
	 * 
	 * @param rgb
	 * @return blue component.
	 */
	public static int getBlue(final int rgb) {
		// Bit-wise AND
		return rgb & 0x000000FF;
	}

	/**
	 * Takes a RGB value and extracts just the red value out of the pixel applying a
	 * bit mask on the pixel making all the components 0 except for the first byte
	 * from the left, which is exactly the red component and then shifting the value
	 * 16 bits to the right.
	 * 
	 * @param rgb
	 * @return red component.
	 */
	public static int getRed(final int rgb) {
		return (rgb & 0x00FF0000) >> 16;
	}

	/**
	 * Takes a RGB value and extracts just the green value out of the pixel applying
	 * a bit mask on the pixel making all the components 0 except for the second
	 * byte from the right, which is exactly the green component and then shifting
	 * the value 8 bits to the right.
	 * 
	 * @param rgb
	 * @return green component.
	 */
	public static int getGreen(final int rgb) {
		return (rgb & 0x0000FF00) >> 8;
	}

}
