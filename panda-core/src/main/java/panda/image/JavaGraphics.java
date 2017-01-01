package panda.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * <p>
 * a set of tools to perform common graphics operations
 * easily. These operations are divided into several themes, listed below.
 * </p>
 * <h2>Compatible Images</h2>
 * <p>
 * Compatible images can, and should, be used to increase drawing performance. This class provides a
 * number of methods to load compatible images directly from files or to convert existing images to
 * compatibles images.
 * </p>
 * <h2>Creating Thumbnails</h2>
 * <p>
 * This class provides a number of methods to easily scale down images. Some of these methods offer
 * a trade-off between speed and result quality and shouuld be used all the time. They also offer
 * the advantage of producing compatible images, thus automatically resulting into better runtime
 * performance.
 * </p>
 * <p>
 * All these methodes are both faster than {@link java.awt.Image#getScaledInstance(int, int, int)}
 * and produce better-looking results than the various <code>drawImage()</code> methods in
 * {@link java.awt.Graphics}, which can be used for image scaling.
 * </p>
 * <h2>Image Manipulation</h2>
 * <p>
 * This class provides two methods to get and set pixels in a buffered image. These methods try to
 * avoid unmanaging the image in order to keep good performance.
 * </p>
 * 
 * @author yf.frank.wang@gmail.com
 */
public abstract class JavaGraphics {

	// Returns the graphics configuration for the primary screen
	private static GraphicsConfiguration getGraphicsConfiguration() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
			.getDefaultConfiguration();
	}

	private static boolean isHeadless() {
		return GraphicsEnvironment.isHeadless();
	}

	/**
	 * <p>
	 * Returns a new <code>BufferedImage</code> using the same color model as the image passed as a
	 * parameter. The returned image is only compatible with the image passed as a parameter. This
	 * does not mean the returned image is compatible with the hardware.
	 * </p>
	 * 
	 * @param image the reference image from which the color model of the new image is obtained
	 * @return a new <code>BufferedImage</code>, compatible with the color model of
	 *         <code>image</code>
	 */
	public static BufferedImage createColorModelCompatibleImage(BufferedImage image) {
		ColorModel cm = image.getColorModel();
		return new BufferedImage(cm, cm.createCompatibleWritableRaster(image.getWidth(), image
			.getHeight()), cm.isAlphaPremultiplied(), null);
	}

	/**
	 * <p>
	 * Returns a new compatible image with the same width, height and transparency as the image
	 * specified as a parameter. That is, the returned BufferedImage will be compatible with the
	 * graphics hardware. If this method is called in a headless environment, then the returned
	 * BufferedImage will be compatible with the source image.
	 * </p>
	 * 
	 * @see java.awt.Transparency
	 * @see #createCompatibleImage(int, int)
	 * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
	 * @see #createCompatibleTranslucentImage(int, int)
	 * @see #loadCompatibleImage(java.net.URL)
	 * @see #toCompatibleImage(java.awt.image.BufferedImage)
	 * @param image the reference image from which the dimension and the transparency of the new
	 *            image are obtained
	 * @return a new compatible <code>BufferedImage</code> with the same dimension and transparency
	 *         as <code>image</code>
	 */
	public static BufferedImage createCompatibleImage(BufferedImage image) {
		return createCompatibleImage(image, image.getWidth(), image.getHeight());
	}

	/**
	 * <p>
	 * Returns a new compatible image of the specified width and height, and the same transparency
	 * setting as the image specified as a parameter. That is, the returned
	 * <code>BufferedImage</code> is compatible with the graphics hardware. If the method is called
	 * in a headless environment, then the returned BufferedImage will be compatible with the source
	 * image.
	 * </p>
	 * 
	 * @see java.awt.Transparency
	 * @see #createCompatibleImage(java.awt.image.BufferedImage)
	 * @see #createCompatibleImage(int, int)
	 * @see #createCompatibleTranslucentImage(int, int)
	 * @see #loadCompatibleImage(java.net.URL)
	 * @see #toCompatibleImage(java.awt.image.BufferedImage)
	 * @param width the width of the new image
	 * @param height the height of the new image
	 * @param image the reference image from which the transparency of the new image is obtained
	 * @return a new compatible <code>BufferedImage</code> with the same transparency as
	 *         <code>image</code> and the specified dimension
	 */
	public static BufferedImage createCompatibleImage(BufferedImage image, int width, int height) {
		return isHeadless() ? new BufferedImage(width, height, image.getType()) : getGraphicsConfiguration()
			.createCompatibleImage(width, height, image.getTransparency());
	}

	/**
	 * <p>
	 * Returns a new opaque compatible image of the specified width and height. That is, the
	 * returned <code>BufferedImage</code> is compatible with the graphics hardware. If the method
	 * is called in a headless environment, then the returned BufferedImage will be compatible with
	 * the source image.
	 * </p>
	 * 
	 * @see #createCompatibleImage(java.awt.image.BufferedImage)
	 * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
	 * @see #createCompatibleTranslucentImage(int, int)
	 * @see #loadCompatibleImage(java.net.URL)
	 * @see #toCompatibleImage(java.awt.image.BufferedImage)
	 * @param width the width of the new image
	 * @param height the height of the new image
	 * @return a new opaque compatible <code>BufferedImage</code> of the specified width and height
	 */
	public static BufferedImage createCompatibleImage(int width, int height) {
		return isHeadless() ? new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB) 
			: getGraphicsConfiguration().createCompatibleImage(width, height);
	}

	/**
	 * <p>
	 * Returns a new translucent compatible image of the specified width and height. That is, the
	 * returned <code>BufferedImage</code> is compatible with the graphics hardware. If the method
	 * is called in a headless environment, then the returned BufferedImage will be compatible with
	 * the source image.
	 * </p>
	 * 
	 * @see #createCompatibleImage(java.awt.image.BufferedImage)
	 * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
	 * @see #createCompatibleImage(int, int)
	 * @see #loadCompatibleImage(java.net.URL)
	 * @see #toCompatibleImage(java.awt.image.BufferedImage)
	 * @param width the width of the new image
	 * @param height the height of the new image
	 * @return a new translucent compatible <code>BufferedImage</code> of the specified width and
	 *         height
	 */
	public static BufferedImage createCompatibleTranslucentImage(int width, int height) {
		return isHeadless() ? new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB) : getGraphicsConfiguration()
			.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
	}

	/**
	 * <p>
	 * Returns a new compatible image from a URL. The image is loaded from the specified location
	 * and then turned, if necessary into a compatible image.
	 * </p>
	 * 
	 * @see #createCompatibleImage(java.awt.image.BufferedImage)
	 * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
	 * @see #createCompatibleImage(int, int)
	 * @see #createCompatibleTranslucentImage(int, int)
	 * @see #toCompatibleImage(java.awt.image.BufferedImage)
	 * @param resource the URL of the picture to load as a compatible image
	 * @return a new translucent compatible <code>BufferedImage</code> of the specified width and
	 *         height
	 * @throws java.io.IOException if the image cannot be read or loaded
	 */
	public static BufferedImage loadCompatibleImage(URL resource) throws IOException {
		BufferedImage image = ImageIO.read(resource);
		return toCompatibleImage(image);
	}

	/**
	 * <p>
	 * Return a new compatible image that contains a copy of the specified image. This method
	 * ensures an image is compatible with the hardware, and therefore optimized for fast blitting
	 * operations.
	 * </p>
	 * <p>
	 * If the method is called in a headless environment, then the returned
	 * <code>BufferedImage</code> will be the source image.
	 * </p>
	 * 
	 * @see #createCompatibleImage(java.awt.image.BufferedImage)
	 * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
	 * @see #createCompatibleImage(int, int)
	 * @see #createCompatibleTranslucentImage(int, int)
	 * @see #loadCompatibleImage(java.net.URL)
	 * @param image the image to copy into a new compatible image
	 * @return a new compatible copy, with the same width and height and transparency and content,
	 *         of <code>image</code>
	 */
	public static BufferedImage toCompatibleImage(BufferedImage image) {
		if (isHeadless()) {
			return image;
		}

		if (image.getColorModel().equals(getGraphicsConfiguration().getColorModel())) {
			return image;
		}

		BufferedImage compatibleImage = getGraphicsConfiguration().createCompatibleImage(
			image.getWidth(), image.getHeight(), image.getTransparency());
		Graphics g = compatibleImage.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return compatibleImage;
	}

	/**
	 * <p>
	 * Returns a scaled image of a source image. <code>newSize</code> defines the length of the
	 * longest dimension of the new image. The other dimension is then computed according to the
	 * dimensions ratio of the original picture.
	 * </p>
	 * <p>
	 * This method favors speed over quality. When the new size is less than half the longest
	 * dimension of the source image, {@link #createScaledImageSlow(BufferedImage, int)} or
	 * {@link #createScaledImageSlow(BufferedImage, int, int)} should be used instead to ensure the
	 * quality of the result without sacrificing too much performance.
	 * </p>
	 * 
	 * @see #createScaledImage(java.awt.image.BufferedImage, int, int)
	 * @see #createScaledImageSlow(java.awt.image.BufferedImage, int)
	 * @see #createScaledImageSlow(java.awt.image.BufferedImage, int, int)
	 * @param image the source image
	 * @param newSize the length of the largest dimension
	 * @return a new compatible <code>BufferedImage</code>
	 * @throws IllegalArgumentException if <code>newSize</code> is &lt;= 0
	 */
	public static BufferedImage createScaledImage(BufferedImage image, int newSize) {
		if (newSize <= 0) {
			throw new IllegalArgumentException("new size must be greater than 0");
		}

		int width = image.getWidth();
		int height = image.getHeight();

		if (width > height) {
			if (newSize == width) {
				return image;
			}

			float ratio = (float)width / (float)height;
			width = newSize;
			height = (int)(width / ratio);
		}
		else {
			if (newSize == height) {
				return image;
			}

			float ratio = (float)height / (float)width;
			height = newSize;
			width = (int)(height / ratio);
		}

		BufferedImage temp = createCompatibleImage(image, width, height);
		Graphics2D g2 = temp.createGraphics();
		// g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);
		g2.dispose();

		return temp;
	}

	/**
	 * <p>
	 * Returns a scaled image of a source image. (ratio keeped)
	 * </p>
	 * 
	 * @see #createScaledImage(java.awt.image.BufferedImage, int, int, boolean)
	 */
	public static BufferedImage createScaledImage(BufferedImage image,
			int newWidth, int newHeight) {
		return createScaledImage(image, newWidth, newHeight, true);
	}
	
	/**
	 * <p>
	 * Returns a scaled image of a source image.
	 * </p>
	 * <p>
	 * This method favors speed over quality. When the new size is less than half the longest
	 * dimension of the source image, {@link #createScaledImageSlow(BufferedImage, int)} or
	 * {@link #createScaledImageSlow(BufferedImage, int, int)} should be used instead to ensure the
	 * quality of the result without sacrificing too much performance.
	 * </p>
	 * 
	 * @see #createScaledImage(java.awt.image.BufferedImage, int)
	 * @see #createScaledImageSlow(java.awt.image.BufferedImage, int)
	 * @see #createScaledImageSlow(java.awt.image.BufferedImage, int, int)
	 * @param image the source image
	 * @param newWidth the width of the scaled image
	 * @param newHeight the height of the scaled image
	 * @param keepRatio keep ratio of image
	 * @return a new compatible <code>BufferedImage</code> containing a scaled <code>image</code>
	 * @throws IllegalArgumentException if <code>newWidth</code> is &lt;= 0 and
	 *             <code>newHeight</code> is &lt;= 0
	 */
	public static BufferedImage createScaledImage(BufferedImage image,
			int newWidth, int newHeight, boolean keepRatio) {
		if (newWidth <= 0 && newHeight <= 0) {
			throw new IllegalArgumentException("new width/height must be greater than 0");
		}

		if (newWidth == image.getWidth() && newHeight == image.getHeight()) {
			return image;
		}

		int width = image.getWidth();
		int height = image.getHeight();

		if (newHeight <= 0) {
			if (newWidth == width) {
				return image;
			}

			float ratio = (float)width / (float)height;
			newHeight = (int)(newWidth / ratio);
		}
		else if (newWidth <= 0) {
			if (newHeight == height) {
				return image;
			}

			float ratio = (float)height / (float)width;
			newWidth = (int)(newHeight / ratio);
		}
		else if (keepRatio) {
			float ratiow = (float)newWidth / (float)width;
			float ratioh = (float)newHeight / (float)height;
			
			if (ratiow < ratioh) {
				newWidth = (int)(width * ratiow);
			}
			else if (ratioh < ratiow) {
				newHeight = (int)(height * ratioh);
			}
		}

		BufferedImage temp = createCompatibleImage(image, newWidth, newHeight);
		Graphics2D g2 = temp.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);
		g2.dispose();

		return temp;
	}

	public static boolean isTransparency(BufferedImage image) {
		return image.getTransparency() != Transparency.OPAQUE;
	}

	/**
	 * <p>
	 * Returns a scaled image of a source image. <code>newSize</code> defines the length of the
	 * longest dimension of the scaled image. The other dimension is then computed according to the
	 * dimensions ratio of the original picture.
	 * </p>
	 * <p>
	 * This method offers a good trade-off between speed and quality. The result looks better than
	 * {@link #createScaledImage(java.awt.image.BufferedImage, int)} when the new size is less than
	 * half the longest dimension of the source image, yet the rendering speed is almost similar.
	 * </p>
	 * 
	 * @see #createScaledImage(java.awt.image.BufferedImage, int, int)
	 * @see #createScaledImage(java.awt.image.BufferedImage, int)
	 * @see #createScaledImageSlow(java.awt.image.BufferedImage, int, int)
	 * @param image the source image
	 * @param newSize the length of the largest dimension of the scaled image
	 * @return a new compatible <code>BufferedImage</code> containing a scaled image of
	 *         <code>image</code>
	 * @throws IllegalArgumentException if <code>newSize</code> is &lt;= 0
	 */
	public static BufferedImage createScaledImageSlow(BufferedImage image, int newSize) {
		if (newSize <= 0) {
			throw new IllegalArgumentException("new size must be greater than 0");
		}

		int width = image.getWidth();
		int height = image.getHeight();

		float ratio;
		boolean expand = false;
		boolean widther = width > height;

		if (widther) {
			if (newSize == width) {
				return image;
			}
			if (newSize > width) {
				expand = true;
			}
			ratio = (float)width / (float)height;
		}
		else {
			if (newSize == height) {
				return image;
			}
			if (newSize > height) {
				expand = true;
			}
			ratio = (float)height / (float)width;
		}

		boolean transparent = isTransparency(image);

		BufferedImage scale = image;
		BufferedImage temp = null;

		Graphics2D g2 = null;

		int previousWidth = width;
		int previousHeight = height;

		do {
			if (widther) {
				if (expand) {
					width *= 2;
					if (width > newSize) {
						width = newSize;
					}
				}
				else {
					width /= 2;
					if (width < newSize) {
						width = newSize;
					}
				}
				height = (int)(width / ratio);
			}
			else {
				if (expand) {
					height *= 2;
					if (height > newSize) {
						height = newSize;
					}
				}
				else {
					height /= 2;
					if (height < newSize) {
						height = newSize;
					}
				}
				width = (int)(height / ratio);
			}

			if (temp == null || transparent) {
				temp = createCompatibleImage(image, width, height);
				g2 = temp.createGraphics();
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			}
			g2.drawImage(scale, 0, 0, width, height, 0, 0, previousWidth, previousHeight, null);

			previousWidth = width;
			previousHeight = height;

			scale = temp;
		}
		while (newSize != (widther ? width : height));

		g2.dispose();

		if (width != scale.getWidth() || height != scale.getHeight()) {
			temp = createCompatibleImage(image, width, height);
			g2 = temp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(scale, 0, 0, width, height, 0, 0, width, height, null);
			g2.dispose();
			scale = temp;
		}

		return scale;
	}
	/**
	 * <p>
	 * Returns a scaled image (ratio keep) of a source image.
	 * </p>
	 * 
	 * @see #createScaledImageSlow(java.awt.image.BufferedImage, int, int, boolean)
	 */
	public static BufferedImage createScaledImageSlow(BufferedImage image,
			int newWidth, int newHeight) {
		return createScaledImageSlow(image, newWidth, newHeight, true);
	}
	
	/**
	 * <p>
	 * Returns a scaled image of a source image.
	 * </p>
	 * <p>
	 * This method offers a good trade-off between speed and quality. The result looks better than
	 * {@link #createScaledImage(java.awt.image.BufferedImage, int)} when the new size is less than
	 * half the longest dimension of the source image, yet the rendering speed is almost similar.
	 * </p>
	 * 
	 * @see #createScaledImage(java.awt.image.BufferedImage, int)
	 * @see #createScaledImage(java.awt.image.BufferedImage, int, int)
	 * @see #createScaledImageSlow(java.awt.image.BufferedImage, int)
	 * @param image the source image
	 * @param newWidth the width of the scaled image
	 * @param newHeight the height of the scaled image
	 * @param keepRatio keep ratio of image
	 * @return a new compatible <code>BufferedImage</code> containing a scaled image of
	 *         <code>image</code>
	 * @throws IllegalArgumentException if <code>newWidth</code> is &lt;= 0 or if
	 *             code>newHeight</code> is &lt;= 0</code>
	 */
	public static BufferedImage createScaledImageSlow(BufferedImage image,
			int newWidth, int newHeight, boolean keepRatio) {
		if (newWidth <= 0 && newHeight <= 0) {
			throw new IllegalArgumentException("new width/height must be greater than 0");
		}

		if (newWidth == image.getWidth() && newHeight == image.getHeight()) {
			return image;
		}

		int width = image.getWidth();
		int height = image.getHeight();

		if (newHeight <= 0) {
			if (newWidth == width) {
				return image;
			}

			float ratio = (float)width / (float)height;
			newHeight = (int)(newWidth / ratio);
		}
		else if (newWidth <= 0) {
			if (newHeight == height) {
				return image;
			}

			float ratio = (float)height / (float)width;
			newWidth = (int)(newHeight / ratio);
		}
		else if (keepRatio) {
			float ratiow = (float)newWidth / (float)width;
			float ratioh = (float)newHeight / (float)height;
			
			if (ratiow < ratioh) {
				newWidth = (int)(width * ratiow);
			}
			else if (ratioh < ratiow) {
				newHeight = (int)(height * ratioh);
			}
		}

		boolean transparent = isTransparency(image);

		boolean wexpand = newWidth > width;
		boolean hexpand = newHeight > height;

		BufferedImage scale = image;
		BufferedImage temp = null;

		Graphics2D g2 = null;

		int previousWidth = width;
		int previousHeight = height;

		do {
			if (wexpand) {
				if (width < newWidth) {
					width *= 2;
					if (width > newWidth) {
						width = newWidth;
					}
				}
			}
			else {
				if (width > newWidth) {
					width /= 2;
					if (width < newWidth) {
						width = newWidth;
					}
				}
			}

			if (hexpand) {
				if (height < newHeight) {
					height *= 2;
					if (height > newHeight) {
						height = newHeight;
					}
				}
			}
			else {
				if (height > newHeight) {
					height /= 2;
					if (height < newHeight) {
						height = newHeight;
					}
				}
			}

			if (temp == null || transparent) {
				temp = createCompatibleImage(image, width, height);
				g2 = temp.createGraphics();
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			}
			g2.drawImage(scale, 0, 0, width, height, 0, 0, previousWidth, previousHeight, null);

			previousWidth = width;
			previousHeight = height;

			scale = temp;
		}
		while (width != newWidth || height != newHeight);

		g2.dispose();

		if (width != scale.getWidth() || height != scale.getHeight()) {
			temp = createCompatibleImage(image, width, height);
			g2 = temp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(scale, 0, 0, width, height, 0, 0, width, height, null);
			g2.dispose();
			scale = temp;
		}

		return scale;
	}

	/**
	 * <p>
	 * Returns an array of pixels, stored as integers, from a <code>BufferedImage</code>. The pixels
	 * are grabbed from a rectangular area defined by a location and two dimensions. Calling this
	 * method on an image of type different from <code>BufferedImage.TYPE_INT_ARGB</code> and
	 * <code>BufferedImage.TYPE_INT_RGB</code> will unmanage the image.
	 * </p>
	 * 
	 * @param img the source image
	 * @param x the x location at which to start grabbing pixels
	 * @param y the y location at which to start grabbing pixels
	 * @param w the width of the rectangle of pixels to grab
	 * @param h the height of the rectangle of pixels to grab
	 * @param pixels a pre-allocated array of pixels of size w*h; can be null
	 * @return <code>pixels</code> if non-null, a new array of integers otherwise
	 * @throws IllegalArgumentException is <code>pixels</code> is non-null and of length &lt; w*h
	 */
	public static int[] getPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
		if (w == 0 || h == 0) {
			return new int[0];
		}

		if (pixels == null) {
			pixels = new int[w * h];
		}
		else if (pixels.length < w * h) {
			throw new IllegalArgumentException("pixels array must have a length" + " >= w*h");
		}

		int imageType = img.getType();
		if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB) {
			Raster raster = img.getRaster();
			return (int[])raster.getDataElements(x, y, w, h, pixels);
		}

		// Unmanages the image
		return img.getRGB(x, y, w, h, pixels, 0, w);
	}

	/**
	 * <p>
	 * Writes a rectangular area of pixels in the destination <code>BufferedImage</code>. Calling
	 * this method on an image of type different from <code>BufferedImage.TYPE_INT_ARGB</code> and
	 * <code>BufferedImage.TYPE_INT_RGB</code> will unmanage the image.
	 * </p>
	 * 
	 * @param img the destination image
	 * @param x the x location at which to start storing pixels
	 * @param y the y location at which to start storing pixels
	 * @param w the width of the rectangle of pixels to store
	 * @param h the height of the rectangle of pixels to store
	 * @param pixels an array of pixels, stored as integers
	 * @throws IllegalArgumentException is <code>pixels</code> is non-null and of length &lt; w*h
	 */
	public static void setPixels(BufferedImage img, int x, int y, int w, int h, int[] pixels) {
		if (pixels == null || w == 0 || h == 0) {
			return;
		}
		else if (pixels.length < w * h) {
			throw new IllegalArgumentException("pixels array must have a length" + " >= w*h");
		}

		int imageType = img.getType();
		if (imageType == BufferedImage.TYPE_INT_ARGB || imageType == BufferedImage.TYPE_INT_RGB) {
			WritableRaster raster = img.getRaster();
			raster.setDataElements(x, y, w, h, pixels);
		}
		else {
			// Unmanages the image
			img.setRGB(x, y, w, h, pixels, 0, w);
		}
	}
}
