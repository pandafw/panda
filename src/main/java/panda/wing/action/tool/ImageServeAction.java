package panda.wing.action.tool;

import panda.filepool.FileItem;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;

/**
 * Image download/upload for temporary image
 */
@At("/image")
public class ImageServeAction extends FileServeAction {
	private static final Log log = Logs.getLog(ImageServeAction.class);
	
	private int width = 0;
	private int height = 0;
	private int scale = 0;
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
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
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the scale
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * upload
	 * 
	 * @return NONE
	 * @throws Exception if an error occurs
	 */
	@At
	public FileItem upload(@Param("file") FileItem file, @Param("width") int width, @Param("height") int height, @Param("scale") int scale) throws Exception {
		if (width > 0 && height > 0) {
			if (file != null && file.isExists()) {
				ImageWrapper iw = Images.i().read(file.getData());
				iw.resize(width, height);
				byte[] data = iw.getData();
				file.save(data);
			}
		}
		else if (scale > 0) {
			if (file != null && file.isExists()) {
				ImageWrapper iw = Images.i().read(file.getData());
				iw.resize(scale);
				byte[] data = iw.getData();
				file.save(data);
			}
		}
		
		if (file != null) {
			log.debug("UploadImage: " + file.getName());
		}
		return file;
	}
}
