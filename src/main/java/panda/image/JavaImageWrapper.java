package panda.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import panda.io.stream.ByteArrayOutputStream;


/**
 * @author yf.frank.wang@gmail.com
 */
public class JavaImageWrapper extends AbstractImageWrapper {
	private BufferedImage image;

	public JavaImageWrapper(BufferedImage image) {
		super();
		this.image = image;
	}

	public JavaImageWrapper(BufferedImage image, String format) {
		super();
		this.image = image;
		this.format = format;
	}

	public JavaImageWrapper(BufferedImage image, String format, int quality) {
		super();
		this.image = image;
		this.format = format;
		this.quality = quality;
	}

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}

	public byte[] getData() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			write(baos);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		return baos.toByteArray();
	}
	
	public void write(OutputStream os) throws IOException {
		JavaImages.write(image, format, os, quality);
	}
	
	public ImageWrapper resize(int width, int height) {
		BufferedImage bi = JavaGraphics.createScaledImageSlow(image, width, height);
		ImageWrapper iw = new JavaImageWrapper(bi, format, quality);
		return iw;
	}
	
	public ImageWrapper resize(int scale) {
		BufferedImage bi = JavaGraphics.createScaledImageSlow(image, scale);
		ImageWrapper iw = new JavaImageWrapper(bi, format, quality);
		return iw;
	}
}
