package panda.wing.action.tool;

import java.io.InputStream;

import panda.filepool.FileItem;
import panda.filepool.FilePool;
import panda.io.Streams;
import panda.ioc.annotation.IocInject;
import panda.lang.time.DateTimes;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.HttpStatusView;
import panda.mvc.view.VoidView;
import panda.servlet.HttpServletSupport;
import panda.wing.action.AbstractAction;

/**
 * File download/upload for temporary image
 */
@At("/file")
public class FileServeAction extends AbstractAction {
	@IocInject
	protected FilePool filePool;
	
	protected int bufferSize = 4096;
	protected boolean cache = true;
	protected boolean attachment = true;

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
	 * @return the attachment
	 */
	public boolean isAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(boolean attachment) {
		this.attachment = attachment;
	}

	/**
	 * upload
	 * 
	 * @return file
	 * @throws Exception if an error occurs
	 */
	@At
	public FileItem upload(@Param("file") FileItem file) throws Exception {
		return file;
	}
	
	/**
	 * download
	 * 
	 * @throws Exception if an error occurs
	 */
	@At
	public Object download(@Param("id") Long id) throws Exception {
		FileItem file = filePool.findFile(id);
		if (file == null) {
			return HttpStatusView.HTTP_404;
		}

		String filename = file.getName();
		InputStream fis = file.getInputStream();
		
		try {
			HttpServletSupport hss = new HttpServletSupport(getRequest(), getResponse());
			hss.setContentLength(file.getSize());
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

}
