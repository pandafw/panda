package panda.roid.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * Collection of utility functions used in this package.
 */
public class Bitmaps {
	public static final int UNCONSTRAINED = -1;

	/**
	 * Rotates the bitmap by the specified degree. If a new bitmap is created, the original bitmap is recycled.
	 * @param b bitmap object
	 * @param degrees rotate degrees
	 */
	public static Bitmap rotate(Bitmap b, int degrees) {
		return rotate(b, degrees, null);
	}
	
	/**
	 * Rotates the bitmap by the specified degree. If a new bitmap is created, the original bitmap is recycled.
	 * @param b bitmap object
	 * @param degrees rotate degrees
	 * @param m matrix object
	 */
	public static Bitmap rotate(Bitmap b, int degrees, Matrix m) {
		if (degrees != 0 && b != null) {
			if (m == null) {
				m = new Matrix();
			}

			m.setRotate(degrees, (float)b.getWidth() / 2, (float)b.getHeight() / 2);
			Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
			if (b != b2) {
				b.recycle();
				b = b2;
			}
		}
		return b;
	}

	/**
	 * Compute the sample size as a function of minSideLength and maxNumOfPixels. minSideLength is
	 * used to specify that minimal width or height of a bitmap. maxNumOfPixels is used to specify
	 * the maximal size in pixels that is tolerable in terms of memory usage. The function returns a
	 * sample size based on the constraints. Both size and minSideLength can be passed in as
	 * IImage.UNCONSTRAINED, which indicates no care of the corresponding constraint. The functions
	 * prefers returning a sample size that generates a smaller bitmap, unless minSideLength =
	 * IImage.UNCONSTRAINED. Also, the function rounds up the sample size to a power of 2 or
	 * multiple of 8 because BitmapFactory only honors sample size this way. For example,
	 * BitmapFactory downsamples an image by 2 even though the request is 3. So we round up the
	 * sample size to avoid OOM.
	 * 
	 * @param options bitmap factory options
	 * @param minSideLength minimum side length
	 * @param maxNumOfPixels max numbers of pixels
	 * @return size
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		}
		else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int)Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int)Math.min(Math.floor(w / minSideLength),
			Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED) && (minSideLength == UNCONSTRAINED)) {
			return 1;
		}
		else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		}
		else {
			return upperBound;
		}
	}

	public static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, boolean scaleUp,
			boolean recycle) {
		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension, than the target.
			 * Transform it by placing as much of the image as possible into the target and leaving
			 * the top/bottom or left/right (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf + Math.min(targetWidth, source.getWidth()),
				deltaYHalf + Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float)targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			}
			else {
				scaler = null;
			}
		}
		else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			}
			else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
		}
		else {
			b1 = source;
		}

		if (recycle && b1 != source) {
			source.recycle();
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);

		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}

		return b2;
	}

	/**
	 * Make a bitmap from a given Uri.
	 * @param minSideLength side length
	 * @param maxNumOfPixels max number of pixels
	 * @param pathName path name
	 * @param options bitmap factory options
	 * @return bitmap object
	 */
	public static Bitmap makeBitmap(int minSideLength, int maxNumOfPixels, String pathName,
			BitmapFactory.Options options) {
		if (options == null)
			options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		if (options.outWidth == -1 || options.outHeight == -1) {
			return null;
		}

		options.inSampleSize = computeSampleSize(options, minSideLength, maxNumOfPixels);
		options.inJustDecodeBounds = false;
		// options.inDither = false;
		// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return BitmapFactory.decodeFile(pathName, options);
	}
}
