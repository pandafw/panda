package panda.roid.image;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import panda.image.ImageWrapper;
import panda.image.Images;

/**
 */
public class AndroidImages extends Images {

	@Override
	public ImageWrapper read(File file) throws IOException {
		Bitmap bm = BitmapFactory.decodeFile(file.getPath());
		if (bm == null) {
			throw new IllegalArgumentException("Invalid image file " + file);
		}
		return new AndroidImageWrapper(bm);
	}

	@Override
	public ImageWrapper read(InputStream is) {
		Bitmap bm = BitmapFactory.decodeStream(is);
		if (bm == null) {
			throw new IllegalArgumentException("Invalid image stream");
		}
		return new AndroidImageWrapper(bm);
	}
}
