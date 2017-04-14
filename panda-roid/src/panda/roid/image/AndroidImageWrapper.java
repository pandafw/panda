package panda.roid.image;

import java.io.IOException;
import java.io.OutputStream;

import panda.image.AbstractImageWrapper;
import panda.image.ImageWrapper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;


/**
 */
public class AndroidImageWrapper extends AbstractImageWrapper {
	private Bitmap image;

	public AndroidImageWrapper(Bitmap image) {
		super();
		this.image = image;
	}

	public AndroidImageWrapper(Bitmap image, String format) {
		super();
		this.image = image;
		this.format = format;
	}

	public AndroidImageWrapper(Bitmap image, String format, int quality) {
		super();
		this.image = image;
		this.format = format;
		this.quality = quality;
	}

	/**
	 * @return the image
	 */
	public Bitmap getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Bitmap image) {
		this.image = image;
	}

	@Override
	public int getWidth() {
		return image.getWidth();
	}
	
	@Override
	public int getHeight() {
		return image.getHeight();
	}

	private CompressFormat getCompressFormat() {
		if ("png".equalsIgnoreCase(format)) {
			return CompressFormat.PNG;
		}
		return CompressFormat.JPEG;
	}
	
	@Override
	public void write(OutputStream os) throws IOException {
		image.compress(getCompressFormat(), quality, os);
	}
	
	@Override
	public ImageWrapper resize(int width, int height) {
		Bitmap bm = Bitmap.createScaledBitmap(image, width, height, false);
		ImageWrapper iw = new AndroidImageWrapper(bm, format, quality);
		return iw;
	}
}
