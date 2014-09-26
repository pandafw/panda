package panda.wing.action.tool;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.filepool.FileItem;
import panda.filepool.FilePool;
import panda.ioc.annotation.IocInject;
import panda.lang.time.DateTimes;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.servlet.HttpServletSupport;
import panda.wing.mvc.AbstractAction;

/**
 * File download/upload for temporary image
 */
public class FileServeAction extends AbstractAction {
	@IocInject
	protected FilePool pool;
	
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
	public void download(@Param("id") String id) throws Exception {
		if (pool.openFile(id) == null) {
			return NONE;
		}

		HttpServletRequest servletRequest = getServletRequest();
		HttpServletResponse servletResponse = getServletResponse();

		FileContent fc = file.getFile().getContent();
		String filename = file.getFile().getName().getBaseName();
		
		HttpServletSupport hss = new HttpServletSupport(servletRequest, servletResponse);
		hss.setContentLength(Integer.valueOf((int)fc.getSize()));
		hss.setFileName(filename);
		hss.setExpiry(cache ? DateTimes.SEC_WEEK : 0);

		hss.writeResponseHeader();
		hss.writeResponseData(fc.getInputStream(), bufferSize);
		
		return NONE;
	}

}
