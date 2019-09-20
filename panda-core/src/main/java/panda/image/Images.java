package panda.image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import panda.lang.Classes;
import panda.lang.Systems;


/**
 */
public abstract class Images {
	private static Images i = initInstance();
	
	private static Images initInstance() {
		if (Systems.IS_OS_ANDROID) {
			return (Images)Classes.born("panda.roid.image.AndroidImages");
		}
		
		if (Systems.IS_OS_APPENGINE) {
			return (Images)Classes.born("panda.gae.image.GaeImages");
		}

		return new JavaImages();
	}
	
	/**
	 * @return the instance
	 */
	public static Images i() {
		return i;
	}

	/**
	 * @return the instance
	 */
	public static Images getInstance() {
		return i;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setInstance(Images instance) {
		Images.i = instance;
	}

	/**
	 * @param file image file
	 * @return image wrapper
	 * @throws FileNotFoundException if file not found
	 * @throws IOException if read error
	 */
	public abstract ImageWrapper read(File file) throws IOException;

	/**
	 * @param data image data
	 * @return image wrapper
	 * @throws IOException if the data is not a image
	 */
	public ImageWrapper read(byte[] data) throws IOException {
		return read(new ByteArrayInputStream(data));
	}

	/**
	 * @param is image input stream
	 * @return image wrapper
	 * @throws RuntimeException if the data is not a image
	 * @throws IOException if read error
	 */
	public abstract ImageWrapper read(InputStream is) throws IOException;
}
