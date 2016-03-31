package panda.wing.action.base;

import java.io.InputStream;

import panda.image.ImageWrapper;
import panda.image.Images;
import panda.io.Streams;
import panda.ioc.annotation.IocInject;
import panda.lang.time.DateTimes;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Fatal;
import panda.mvc.annotation.view.Ok;
import panda.mvc.view.HttpStatusView;
import panda.mvc.view.VoidView;
import panda.servlet.HttpServletSupport;
import panda.vfs.FileItem;
import panda.vfs.FilePool;
import panda.wing.action.AbstractAction;

/**
 * download/upload Action
 */
public abstract class BaseTempFileAction extends AbstractAction {
	@IocInject
	protected FilePool filePool;
	
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
	 * 
	 * @throws Exception if an error occurs
	 */
	@At
	public Object download(@Param("id") Long id) throws Exception {
		if (id == null) {
			return HttpStatusView.NOT_FOUND;
		}

		FileItem file = filePool.findFile(id);
		if (file == null || !file.isExists()) {
			return HttpStatusView.NOT_FOUND;
		}

		String filename = file.getName();
		InputStream fis = file.getInputStream();
		
		try {
			HttpServletSupport hss = new HttpServletSupport(getRequest(), getResponse());
			hss.setContentLength(file.getSize());
			hss.setContentType(file.getContentType());
			hss.setFileName(filename);
			hss.setExpiry(cache ? DateTimes.SEC_WEEK : 0);
	
			hss.writeResponseHeader();
			hss.writeResponseData(fis, bufferSize);
		}
		finally {
			Streams.safeClose(fis);
		}
		return VoidView.INSTANCE;
	}

	/**
	 * upload
	 * 
	 * @return file
	 * @throws Exception if an error occurs
	 */
	@At
	@Ok(View.JSON)
	@Err(View.JSON)
	@Fatal(View.JSON)
	public FileItem upload(@Param("file") FileItem file) throws Exception {
		return file;
	}

	/**
	 * upload
	 * 
	 * @return uploaded file item
	 * @throws Exception if an error occurs
	 */
	@At
	@Ok(View.JSON)
	@Err(View.JSON)
	@Fatal(View.JSON)
	public FileItem iupload(@Param("file") FileItem file, @Param("width") int width, @Param("height") int height, @Param("scale") int scale) throws Exception {
		if (file != null && file.isExists()) {
			if (width > 0 && height > 0) {
				ImageWrapper iw = Images.i().read(file.getData());
				iw.resize(width, height);
				byte[] data = iw.getData();
				file.save(data);
			}
			else if (scale > 0) {
				ImageWrapper iw = Images.i().read(file.getData());
				iw.resize(scale);
				byte[] data = iw.getData();
				file.save(data);
			}
		}
		return file;
	}
}
