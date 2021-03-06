package panda.app.action.base;

import panda.app.action.BaseAction;
import panda.image.ImageWrapper;
import panda.image.Images;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.servlet.HttpServletResponser;
import panda.vfs.FileItem;
import panda.vfs.FileStore;

/**
 * file download/upload Action
 */
public abstract class BaseFileAction extends BaseAction {
	@IocInject
	protected FileStore fileStore;
	
	protected int bufferSize = 4096;
	protected boolean cache = true;
	protected boolean attachment = true;

	/**
	 * @return the cache
	 */
	protected boolean isCache() {
		return cache;
	}

	/**
	 * @param cache the cache to set
	 */
	protected void setCache(boolean cache) {
		this.cache = cache;
	}

	/**
	 * @return the bufferSize
	 */
	protected int getBufferSize() {
		return bufferSize;
	}

	/**
	 * @param bufferSize the bufferSize to set
	 */
	protected void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * @return the attachment
	 */
	protected boolean isAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	protected void setAttachment(boolean attachment) {
		this.attachment = attachment;
	}

	/**
	 * download
	 * @param file file name
	 * @return view
	 * 
	 * @throws Exception if an error occurs
	 */
	@At
	@To(Views.RAW)
	public Object download(@Param("file") String file) throws Exception {
		if (Strings.isEmpty(file)) {
			return Views.scNotFound(context);
		}

		FileItem fi = fileStore.getFile(file);
		if (fi == null || !fi.isExists()) {
			return Views.scNotFound(context);
		}

		HttpServletResponser hss = new HttpServletResponser(getRequest(), getResponse());
		hss.setFile(fi);
		hss.setMaxAge(cache ? DateTimes.SEC_WEEK : 0);
		hss.setBufferSize(bufferSize);
	
		return hss;
	}

	/**
	 * upload
	 * 
	 * @param file the upload file
	 * @return file
	 * @throws Exception if an error occurs
	 */
	@At
	@To(Views.SJSON)
	public FileItem upload(@Param("file") FileItem file) throws Exception {
		return file;
	}

	/**
	 * upload
	 * 
	 * @param files the upload files
	 * @return file
	 * @throws Exception if an error occurs
	 */
	@At
	@To(Views.SJSON)
	public FileItem[] uploads(@Param("files") FileItem[] files) throws Exception {
		return files;
	}

	/**
	 * image upload
	 * 
	 * @param file the input file
	 * @param width image width
	 * @param height image height
	 * @param scale resize scale
	 * 
	 * @return uploaded file item
	 * @throws Exception if an error occurs
	 */
	@At
	@To(Views.SJSON)
	public FileItem iupload(@Param("file") FileItem file, @Param("width") int width, @Param("height") int height, @Param("scale") int scale) throws Exception {
		if (file != null && file.isExists()) {
			if (width > 0 && height > 0) {
				ImageWrapper iw = Images.i().read(file.data());
				iw.resize(width, height);
				byte[] data = iw.getData();
				file.save(data);
			}
			else if (scale > 0) {
				ImageWrapper iw = Images.i().read(file.data());
				iw.resize(scale);
				byte[] data = iw.getData();
				file.save(data);
			}
		}
		return file;
	}
}
