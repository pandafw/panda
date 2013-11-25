package panda.tool.image;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;

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

			addCommandLineOption("w", "width", "Thumbnal width");

			addCommandLineOption("h", "height", "Tumbnail height");

			addCommandLineOption("z", "size", "Thumbnal size");

			addCommandLineOption("q", "quality", "Thumbnal quality");
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
				setParameter("quality", Integer.parseInt(cl.getOptionValue("1").trim()));
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
	
	/**
	 * Constructor
	 */
	public ThumbnailCreator() {
		out = ".thumbs";
		includes = new String[] { "**/*.jpg", "**/*.jpeg" };
		excludes =  new String[] { "**/.*" };
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
		print0("Thumbnail create: " + source.getPath());
	}

	@Override
	protected void afterProcess() throws Exception {
		super.afterProcess();
		print0(cntFile + " thumbnail files created successfully.");
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

		if (n.exists()) {
			try {
				BufferedImage bi = JavaImages.read(n);
				if (size > 0 && (bi.getWidth() == size || bi.getHeight() == size)) {
					return;
				}
				else if (width > 0 && height > 0
						&& width == bi.getWidth() 
						&& height == bi.getHeight()) {
					return;
				}
				else if (width > 0 && width == bi.getWidth()) {
					return;
				}
				else if (height > 0 && height == bi.getHeight()) {
					return;
				}
			}
			catch (Exception e) {
				//ignore
			}
		}

		print2("Creating thumbnail: " + file.getName());
		BufferedImage bi = JavaImages.read(file);
		if (size > 0) {
			bi = JavaGraphics.createScaledImageSlow(bi, size);
		}
		else {
			bi = JavaGraphics.createScaledImageSlow(bi, width, height);
		}

		n.getParentFile().mkdirs();
		
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(n));
			JavaImages.write(bi, "jpg", os, quality);
		}
		finally {
			Streams.safeClose(os);
		}
		cntFile++;
	}
}
