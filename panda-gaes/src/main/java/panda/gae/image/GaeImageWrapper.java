package panda.gae.image;

import java.io.IOException;
import java.io.OutputStream;

import panda.image.AbstractImageWrapper;
import panda.image.ImageWrapper;
import panda.io.Streams;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesService.OutputEncoding;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.OutputSettings;
import com.google.appengine.api.images.Transform;

/**
 */
public class GaeImageWrapper extends AbstractImageWrapper {
	private Image image;

	public GaeImageWrapper(Image image) {
		super();
		this.image = image;
		this.format = image.getFormat().toString();
	}

	/**
	 * @return the image
	 */
	@Override
	public Image getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
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

	@Override
	public byte[] getData() {
		return image.getImageData();
	}

	@Override
	public void write(OutputStream os) throws IOException {
		Streams.write(getData(), os);
	}
	
	@Override
	public ImageWrapper resize(int width, int height) {
		ImagesService imagesService = ImagesServiceFactory.getImagesService();

		Transform resize = ImagesServiceFactory.makeResize(width, height);

		OutputEncoding oe;
		if ("jpg".equalsIgnoreCase(format) || "jpeg".equalsIgnoreCase(format)) {
			oe = OutputEncoding.JPEG;
		}
		else {
			oe = OutputEncoding.PNG;
		}
		OutputSettings oss = new OutputSettings(oe);
		oss.setQuality(quality);

		Image ni = imagesService.applyTransform(resize, image, oss);

		ImageWrapper iw = new GaeImageWrapper(ni);
		iw.setQuality(quality);

		return iw;
	}

}
