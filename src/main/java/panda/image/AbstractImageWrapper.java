package panda.image;

/**
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractImageWrapper implements ImageWrapper {
	protected String format = "PNG";
	protected int quality = 100;

	/**
	 * @return the quality
	 */
	public int getQuality() {
		return quality;
	}

	/**
	 * @param quality the quality to set
	 */
	public void setQuality(int quality) {
		this.quality = quality;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
}
