package panda.image;

import panda.io.stream.ByteArrayOutputStream;

import java.io.IOException;

/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractImageWrapper implements ImageWrapper {
	protected String format = "PNG";
	protected int quality = 100;

	/**
	 * @return the quality
	 */
	@Override
	public int getQuality() {
		return quality;
	}

	/**
	 * @param quality the quality to set
	 */
	@Override
	public void setQuality(int quality) {
		this.quality = quality;
	}

	/**
	 * @return the format
	 */
	@Override
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	@Override
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
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
	
	@Override
	public ImageWrapper resize(int scale) {
		int width = getWidth();
		int height = getHeight();

		if (width > height) {
			if (scale == width) {
				return this;
			}

			float ratio = (float)width / (float)height;
			width = scale;
			height = (int)(width / ratio);
		}
		else {
			if (scale == height) {
				return this;
			}

			float ratio = (float)height / (float)width;
			height = scale;
			width = (int)(height / ratio);
		}

		return resize(width, height);
	}
}
