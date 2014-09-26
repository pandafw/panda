package panda.wing.action.tool;

import panda.exts.fileupload.UploadImage;
import panda.lang.time.DateTimes;
import panda.servlet.HttpServletSupport;
import panda.wing.mvc.CommonServletAction;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.vfs2.FileContent;

/**
 * Image download/upload for temporary image
 */
public class ImageServeAction extends CommonServletAction {
	protected boolean cache = true;
	protected int bufferSize = 4096;
	protected String contentDisposition;

	private UploadImage image = new UploadImage();
	private int width = 0;
	private int height = 0;
	private int scale = 0;
	
	/**
	 * @return the image
	 */
	public UploadImage getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(UploadImage image) {
		this.image = image;
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
	 * @return the cache
	 */
	public boolean isCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	public void setCache(boolean cache) {
		this.cache = cache;
	}

	/**
	 * @return the bufferSize
	 */
	public int getBufferSize() {
		return bufferSize;
	}

	/**
	 * @param bufferSize the bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @return the contentDisposition
	 */
	public String getContentDisposition() {
		return contentDisposition;
	}

	/**
	 * @param contentDisposition the contentDisposition to set
	 */
	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	/**
	 * @return the filename
	 * @throws IOException IOException
	 */
	public String getFn() throws IOException {
		return image.getSaveName();
	}

	/**
	 * @param filename the filename to set
	 * @throws IOException IOException
	 */
	public void setFn(String filename) throws IOException {
		image.setSaveName(filename);
	}

	/**
	 * constructor
	 */
	public ImageServeAction() {
	}

	/**
	 * execute
	 * 
	 * @return result name
	 * @throws Exception if an error occurs
	 */
	public String execute() throws Exception {
		return upload();
	}
	
	/**
	 * upload
	 * 
	 * @return NONE
	 * @throws Exception if an error occurs
	 */
	public String upload() throws Exception {
		if (width > 0 && height > 0) {
			if (image != null && image.getData() != null) {
				image.resize(width, height);
			}
		}
		else if (scale > 0) {
			if (image != null && image.getData() != null) {
				image.resize(scale);
			}
		}
		
		if (image != null) {
			log.debug("UploadImage: " + image.toString());
		}
		return SUCCESS;
	}
	
	/**
	 * download
	 * 
	 * @return NONE
	 * @throws Exception if an error occurs
	 */
	public String download() throws Exception {
		if (image.getFile() == null) {
			return NONE;
		}

		HttpServletRequest servletRequest = getServletRequest();
		HttpServletResponse servletResponse = getServletResponse();

		FileContent fc = image.getFile().getContent();
		String filename = image.getFile().getName().getBaseName();
		
		HttpServletSupport hss = new HttpServletSupport(servletRequest, servletResponse);
		hss.setContentLength(Integer.valueOf((int)fc.getSize()));
		hss.setFileName(filename);
		hss.setExpiry(cache ? DateTimes.SEC_WEEK : 0);

		hss.writeResponseHeader();
		hss.writeResponseData(fc.getInputStream(), bufferSize);
		
		return NONE;
	}
}
