package panda.tool.image;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import panda.args.Option;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.image.JavaGraphics;
import panda.image.JavaImages;
import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Strings;
import panda.tool.AbstractCommandTool;
import panda.tool.AbstractFileTool;

/**
 * 
 */
public class ThumbnailCreator extends AbstractFileTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new ThumbnailCreator().execute(args);
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String out;
	protected int width = 0;
	protected int height = 0;
	protected int size = 0;
	protected int quality = 100;
	protected String format;
	protected boolean overwrite = false;
	protected boolean slow = false;
	
	/**
	 * Constructor
	 */
	public ThumbnailCreator() {
		out = ".thumbs";
		includes = new String[] { "**/*.jpg", "**/*.jpeg", "**/*.png", "**/*.gif" };
	}

	/**
	 * @return the out
	 */
	public String getOut() {
		return out;
	}

	/**
	 * @param outdir the outdir to set
	 */
	@Option(opt='o', option="output", arg="DIR", usage="Output directory")
	public void setOut(String outdir) {
		this.out = Strings.replaceChars(outdir, '\\', '/');
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	@Option(opt='w', option="width", arg="SIZE", usage="Thumbnail width")
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	@Option(opt='h', option="height", arg="SIZE", usage="Tumbnail height")
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	@Option(opt='z', option="size", arg="SIZE", usage="Thumbnail size")
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the quality
	 */
	public int getQuality() {
		return quality;
	}

	/**
	 * @param quality the quality to set
	 */
	@Option(opt='q', option="quality", arg="QUALITY", usage="Thumbnail quality")
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
	@Option(opt='f', option="format", arg="FORMAT", usage="Tumbnail format")
	public void setFormat(String format) {
		this.format = Strings.lowerCase(Strings.stripToNull(format));
	}

	/**
	 * @return the overwrite
	 */
	public boolean isOverwrite() {
		return overwrite;
	}

	/**
	 * @param overwrite the overwrite to set
	 */
	@Option(opt='O', option="overwrite", usage="Overwrite existing file")
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	/**
	 * @return the slow
	 */
	public boolean isSlow() {
		return slow;
	}

	/**
	 * @param slow the slow to set
	 */
	@Option(opt='S', option="slow", usage="Use slow resize algorithm")
	public void setSlow(boolean slow) {
		this.slow = slow;
	}

	protected void checkParameters() throws Exception {
		super.checkParameters();
		
		AbstractCommandTool.checkRequired(out, "out");

		if (width < 1 && height < 1 && size < 1) {
			throw new IllegalArgumentException("parameter [width/height/size] should > 0.");
		}
	}

	@Override
	protected void beforeProcess() throws Exception {
		super.beforeProcess();
		println0("Thumbnail create: " + source.getPath() + " -> " + out);
	}

	@Override
	protected void afterProcess() throws Exception {
		super.afterProcess();
		println0(cntFile + " thumbnail files created successfully.");
	}

	@Override
	protected void processFile(File file) throws Exception {
		File n;
		if (out.indexOf('/') >= 0) {
			String p = FileNames.removeLeadingPath(source, file);
			n = new File(out, p);
		}
		else {
			n = new File(new File(file.getParent(), out), file.getName());
		}
		if (Strings.isNotEmpty(format)) {
			n = new File(FileNames.removeExtension(n) + "." + format);
		}
		
		if (n.exists() && !overwrite) {
			try {
				ImageWrapper iw = Images.i().read(n);
				if (size > 0 && (iw.getWidth() == size || iw.getHeight() == size)) {
					return;
				}
				else if (width > 0 && height > 0
						&& width == iw.getWidth() 
						&& height == iw.getHeight()) {
					return;
				}
				else if (width > 0 && width == iw.getWidth()) {
					return;
				}
				else if (height > 0 && height == iw.getHeight()) {
					return;
				}
			}
			catch (Exception e) {
				//ignore
			}
		}

		println2("Creating thumbnail: " + file.getName() + " -> " + n.getName());
		n.getParentFile().mkdirs();

		if (slow) {
			ImageWrapper iw = Images.i().read(file);
			if (size > 0) {
				iw = iw.resize(size);
			}
			else {
				iw = iw.resize(width, height);
			}
			OutputStream os = null;
			try {
				os = new BufferedOutputStream(new FileOutputStream(n));
				if (Strings.isNotEmpty(format)) {
					iw.setFormat(format);
				}
				iw.setQuality(quality);
				iw.write(os);
			}
			finally {
				Streams.safeClose(os);
			}
		}
		else {
			InputStream is = new FileInputStream(file);
			BufferedImage bi = ImageIO.read(is);
			if (size > 0) {
				bi = JavaGraphics.createScaledImage(bi, size);
			}
			else {
				bi = JavaGraphics.createScaledImage(bi, width, height);
			}
	
			OutputStream os = null;
			try {
				os = new BufferedOutputStream(new FileOutputStream(n));
				JavaImages.write(bi, format, os, quality);
			}
			finally {
				Streams.safeClose(os);
			}
		}
	}
}
