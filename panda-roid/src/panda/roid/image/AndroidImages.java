package panda.roid.image;

import java.io.InputStream;

import panda.image.ImageWrapper;
import panda.image.Images;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 */
public class AndroidImages extends Images {
	@Override
	public ImageWrapper read(InputStream is) {
		Bitmap bm = BitmapFactory.decodeStream(is);
		if (bm == null) {
			throw new IllegalArgumentException("Invalid image stream");
		}
		return new AndroidImageWrapper(bm);
	}
}
