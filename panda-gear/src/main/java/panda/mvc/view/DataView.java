package panda.mvc.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import panda.io.MimeTypes;
import panda.ioc.annotation.IocBean;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.view.util.HttpRangeDownloader;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServletResponser;


@IocBean(singleton=false)
public class DataView implements View {
	private static final Log log = Logs.getLog(DataView.class);

	protected boolean rangeDownload;
	
	protected int maxAge = 0;

	protected String contentType = MimeTypes.TEXT_PLAIN;

	protected String encoding = Charsets.UTF_8;

	protected boolean attachment;
	
	protected String fileName;
	
	protected boolean bom;

	protected Integer contentLength;

	protected Object result;
	
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the maxAge
	 */
	public int getMaxAge() {
		return maxAge;
	}

	/**
	 * @param maxAge the maxAge to set
	 */
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
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
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the file name to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the bom
	 */
	public boolean isBom() {
		return bom;
	}

	/**
	 * @param bom the bom to set
	 */
	public void setBom(boolean bom) {
		this.bom = bom;
	}

	/**
	 * @return the contentLength
	 */
	public Integer getContentLength() {
		return contentLength;
	}

	/**
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(Integer contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public void setArgument(String argument) {
		if (Strings.isNotEmpty(argument)) {
			setFileName(argument);
			setAttachment(true);
		}
	}

	/**
	 * write response header
	 * @param ac action context
	 * @throws IOException
	 */
	protected void writeHeader(ActionContext ac) throws IOException {
		HttpServletResponser hsr = new HttpServletResponser(ac.getRequest(), ac.getResponse());
		writeHeader(hsr);
	}

	/**
	 * write response header
	 * @param hsr http servlet responser
	 * @throws IOException
	 */
	protected void writeHeader(HttpServletResponser hsr) throws IOException {
		hsr.setMaxAge(maxAge);
		hsr.setCharset(encoding);
		hsr.setContentType(contentType);
		hsr.setAttachment(attachment);
		hsr.setFileName(fileName);
		hsr.setBom(bom);
		hsr.setContentLength(contentLength);
		hsr.writeHeader();
	}
	
	@Override
	public void render(ActionContext ac) {
		if (result == null) {
			writeResult(ac, ac.getResult());
		}
		else {
			writeResult(ac, result);
		}
	}

	/**
	 * write result
	 * @param ac action context
	 * @param result result object
	 */
	protected void writeResult(ActionContext ac, Object result) {
		try {
			if (result == null) {
				writeHeader(ac);
				return;
			}

			if (result instanceof File) {
				writeFile(ac, (File)result);
				return;
			}
		
			if (result instanceof HttpServletResponser) {
				HttpServletResponser hsr = (HttpServletResponser)result;
				hsr.writeHeader();
				hsr.writeBody();
				hsr.flush();
				return;
			}
			
			if (result instanceof byte[]) {
				setBom(false);
				setContentLength(((byte[])result).length);
				writeResponse(ac, result);
				return;
			}
			
			if (result instanceof char[] || result instanceof Reader || result instanceof InputStream) {
				writeResponse(ac, result);
				return;
			}

			writeResponse(ac, String.valueOf(result));
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	protected void writeResponse(ActionContext ac, Object body) throws IOException {
		HttpServletResponser hsr = new HttpServletResponser(ac.getRequest(), ac.getResponse());
		hsr.setBody(body);
		writeHeader(hsr);
		hsr.writeBody();
		hsr.flush();
	}

	protected void writeFile(ActionContext ac, File file) throws IOException {
		if (!file.exists() || file.isDirectory()) {
			log.debug("File downloading ... Not Exist : " + file.getAbsolutePath());
			ac.getResponse().sendError(HttpStatus.SC_NOT_FOUND);
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("File downloading ... " + file.getAbsolutePath());
		}

		String range = ac.getRequest().getHeader("Range");

		long fileSize = file.length();
		if (!rangeDownload || fileSize == 0
				|| (range == null || !range.startsWith("bytes=") || range.length() < "bytes=1".length())) {
			setContentLength((int)fileSize);
			writeResponse(ac, file);
			return;
		}
		
		HttpRangeDownloader rd = ac.getIoc().get(HttpRangeDownloader.class);
		rd.download(ac.getRequest(), ac.getResponse(), file);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName() + ": " + fileName;
	}
}

