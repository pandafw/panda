package panda.image;


/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class Images {
	private static Images me = new JavaImages();
	
	/**
	 * @return the instance
	 */
	public static Images me() {
		return me;
	}

	/**
	 * @return the instance
	 */
	public static Images getMe() {
		return me;
	}

	/**
	 * @param instance the instance to set
	 */
	public static void setMe(Images instance) {
		Images.me = instance;
	}

	public abstract ImageWrapper makeImage(byte[] data) throws Exception;
}
