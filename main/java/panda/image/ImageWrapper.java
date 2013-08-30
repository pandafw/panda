package panda.image;


/**
 * @author yf.frank.wang@gmail.com
 */
public interface ImageWrapper {
	
	Object getImage();

	byte[] getData();
	
	int getWidth();
	
	int getHeight();
	
	String getFormat();
	
	void setFormat(String format);

	int getQuality();

	void setQuality(int quality);
	
	ImageWrapper resize(int width, int height);
	
	ImageWrapper resize(int scale);
	
}
