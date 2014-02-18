package panda.image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class Images {
	private static Images i = new JavaImages();
	
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
	public ImageWrapper read(File file) throws IOException {
		return read(new FileInputStream(file));
	}

	/**
	 * @param data image data
	 * @return image wrapper
	 * @throws RuntimeException if the data is not a image
	 */
	public ImageWrapper read(byte[] data) {
		try {
			return read(new ByteArrayInputStream(data));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param is image input stream
	 * @return image wrapper
	 * @throws RuntimeException if the data is not a image
	 * @throws IOException if read error
	 */
	public abstract ImageWrapper read(InputStream is) throws IOException;
}
