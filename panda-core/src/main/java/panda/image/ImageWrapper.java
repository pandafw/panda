package panda.image;

import java.io.IOException;
import java.io.OutputStream;


/**
 * @author yf.frank.wang@gmail.com
 */
public interface ImageWrapper {
	
	Object getImage();

	int getWidth();
	
	int getHeight();
	
	String getFormat();
	
	void setFormat(String format);

	int getQuality();

	void setQuality(int quality);

	byte[] getData();
	
	void write(OutputStream os) throws IOException;
	
	ImageWrapper resize(int width, int height);
	
	ImageWrapper resize(int scale);
}
