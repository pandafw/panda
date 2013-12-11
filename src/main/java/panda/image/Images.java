package panda.image;


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
	 * @param data image data
	 * @return image wrapper
	 * @throws IllegalArgumentException if the data is not a image
	 */
	public abstract ImageWrapper makeImage(byte[] data);
}
