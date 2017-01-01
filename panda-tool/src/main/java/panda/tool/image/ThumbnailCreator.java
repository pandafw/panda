package panda.tool.image;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;

import panda.image.ImageWrapper;
import panda.image.Images;
import panda.image.JavaGraphics;
import panda.image.JavaImages;
import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Strings;
import panda.util.tool.AbstractCommandTool;
import panda.util.tool.AbstractFileTool;

/**
 * 
 */
public class ThumbnailCreator extends AbstractFileTool {
	/**
	 * Base main class for code generator. Parse basic command line options.
	 */
	public static class Main extends AbstractFileTool.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main m = new Main();
			
			ThumbnailCreator c = new ThumbnailCreator();

			m.execute(c, args);
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("o", "output", "Output directory");

			addCommandLineOption("f", "format", "Tumbnail format");
			
			addCommandLineOption("w", "width", "Thumbnail width");

			addCommandLineOption("h", "height", "Tumbnail height");

			addCommandLineOption("z", "size", "Thumbnail size");

			addCommandLineOption("q", "quality", "Thumbnail quality");

			addCommandLineFlag("ow", "overwrite", "Overwrite existing file");

			addCommandLineFlag("sw", "slow", "Use slow resize algorithm");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("o")) {
				setParameter("out", cl.getOptionValue("o").trim());
			}
			
			if (!cl.hasOption("w") && !cl.hasOption("h") && !cl.hasOption("z")) {
				errorRequired(options, "width/height/size");
			}
			
			if (cl.hasOption("f")) {
				setParameter("format", cl.getOptionValue("f").trim());
			}
			
			if (cl.hasOption("w")) {
				setParameter("width", Integer.parseInt(cl.getOptionValue("w").trim()));
			}
			if (cl.hasOption("h")) {
				setParameter("height", Integer.parseInt(cl.getOptionValue("h").trim()));
			}
			if (cl.hasOption("z")) {
				setParameter("size", Integer.parseInt(cl.getOptionValue("z").trim()));
			}
			if (cl.hasOption("q")) {
				setParameter("quality", Integer.parseInt(cl.getOptionValue("q").trim()));
			}
			if (cl.hasOption("ow")) {
				setParameter("overwrite", true);
			}
			if (cl.hasOption("sw")) {
				setParameter("slow", true);
			}
		}
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
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
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
		this.format = Strings.lowerCase(Strings.stripToNull(format));
	}

	/**
	 * @param outdir the outdir to set
	 */
	public void setOut(String outdir) {
		this.out = Strings.replaceChars(outdir, '\\', '/');
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(int verbose) {
		this.verbose = verbose;
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
	public void setSlow(boolean slow) {
		this.slow = slow;
	}

	protected void checkParameters() throws Exception {
		super.checkParameters();
		
		AbstractCommandTool.checkRequired(out, "out");

		if (width < 1 && height < 1 && size < 1) {
			throw new IllegalArgumentException("parameter [width/height/size] should > 0.");
		}
		if (includes != null && includes.length > 0) {
			for (int i = 0; i < includes.length; i++) {
				String s = includes[i];
				includes[i] = Strings.replaceChars(s, '/', File.separatorChar);
			}
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
