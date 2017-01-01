package panda.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 */
public class JavaImages extends Images {
	@Override
	public ImageWrapper read(InputStream is) {
		BufferedImage bi;
		try {
			bi = ImageIO.read(is);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (bi == null) {
			throw new IllegalArgumentException("Invalid image stream");
		}
		return new JavaImageWrapper(bi);
	}
	
	//-------------------------------------------------------------------------------
	/**
	 * Writes an image using an arbitrary <code>ImageWriter</code> that supports the given format to
	 * a <code>File</code>. If there is already a <code>File</code> present, its contents are
	 * discarded.
	 * 
	 * @param im a <code>RenderedImage</code> to be written.
	 * @param format a <code>String</code> containg the informal name of the format.
	 * @param output a <code>File</code> to be written to.
	 * @param quality image quality
	 * @exception IOException if an error occurs during writing.
	 */
	public static void write(RenderedImage im, String format, File output, int quality)
			throws IOException {
		if (output == null) {
			throw new IllegalArgumentException("output == null!");
		}
		ImageOutputStream stream = null;
		output.delete();
		stream = ImageIO.createImageOutputStream(output);

		try {
			write(im, format, stream, quality);
		}
		finally {
			stream.close();
		}
	}

	/**
	 * Writes an image using an arbitrary <code>ImageWriter</code> that supports the given format to
	 * an <code>OutputStream</code>.
	 * <p>
	 * This method <em>does not</em> close the provided <code>OutputStream</code> after the write
	 * operation has completed; it is the responsibility of the caller to close the stream, if
	 * desired.
	 * <p>
	 * The current cache settings from <code>getUseCache</code>and <code>getCacheDirectory</code>
	 * will be used to control caching.
	 * 
	 * @param im a <code>RenderedImage</code> to be written.
	 * @param format a <code>String</code> containg the informal name of the format.
	 * @param output an <code>OutputStream</code> to be written to.
	 * @param quality image quality
	 * @exception IOException if an error occurs during writing.
	 */
	public static void write(RenderedImage im, String format, OutputStream output, int quality)
			throws IOException {
		ImageOutputStream stream = ImageIO.createImageOutputStream(output);
		write(im, format, stream, quality);
	}

	/**
	 * Writes an image using the an arbitrary <code>ImageWriter</code> that supports the given
	 * format to an <code>ImageOutputStream</code>. The image is written to the
	 * <code>ImageOutputStream</code> starting at the current stream pointer, overwriting existing
	 * stream data from that point forward, if present.
	 * <p>
	 * This method <em>does not</em> close the provided <code>ImageOutputStream</code> after the
	 * write operation has completed; it is the responsibility of the caller to close the stream, if
	 * desired.
	 * 
	 * @param im a <code>RenderedImage</code> to be written.
	 * @param format a <code>String</code> containg the informal name of the format.
	 * @param output an <code>ImageOutputStream</code> to be written to.
	 * @param quality image quality
	 * @exception IOException if an error occurs during writing.
	 */
	public static void write(RenderedImage im, String format, ImageOutputStream output, int quality)
			throws IOException {
		if (quality < 0 || quality > 100) {
			throw new IllegalArgumentException("Illegal quality(0-100): " + quality);
		}

		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
		if (!writers.hasNext()) {
			throw new IllegalArgumentException("No writers for [" + format + "]");
		}
		ImageWriter writer = writers.next();

		if (im instanceof BufferedImage) {
			BufferedImage bi = (BufferedImage)im;
			if (JavaGraphics.isTransparency(bi) 
					&& !"png".equalsIgnoreCase(format)
					&& !"gif".equalsIgnoreCase(format)) {
				BufferedImage nbi = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = nbi.createGraphics();
				g2.drawImage(bi, 0, 0, Color.WHITE, null);
				g2.dispose();
				im = nbi;
			}
		}

		writer.setOutput(output);
		ImageWriteParam param = writer.getDefaultWriteParam();
		if (quality > 0) {
			try {
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(quality / 100);
			}
			catch (UnsupportedOperationException e) {
				// skip;
			}
		}
		writer.write(null, new IIOImage(im, null, null), param);
	}
}
