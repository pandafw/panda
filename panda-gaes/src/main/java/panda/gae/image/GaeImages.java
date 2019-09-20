package panda.gae.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import panda.image.ImageWrapper;
import panda.image.Images;
import panda.io.Streams;
import panda.io.stream.ByteArrayOutputStream;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;


/**
 */
public class GaeImages extends Images {
	@Override
	public ImageWrapper read(File file) {
		Image im = ImagesServiceFactory.makeImageFromFilename(file.getPath());
		return new GaeImageWrapper(im);
	}

	@Override
	public ImageWrapper read(byte[] data) {
		Image im = ImagesServiceFactory.makeImage(data);
		return new GaeImageWrapper(im);
	}
	
	@Override
	public ImageWrapper read(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Streams.copy(is, baos);
		Image im = ImagesServiceFactory.makeImage(baos.toByteArray());
		return new GaeImageWrapper(im);
	}
}
