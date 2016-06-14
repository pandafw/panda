package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.io.MimeType;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.mvc.ActionContext;
import panda.servlet.HttpServletSupport;


public abstract class AbstractDataView extends AbstractView {
	protected int maxAge = 0;

	protected String contentType = MimeType.TEXT_PLAIN;

	protected String encoding = Charsets.UTF_8;

	protected boolean attachment;
	
	protected String filename;
	
	protected boolean bom = true;

	/**
	 * Constructor.
	 */
	public AbstractDataView(String location) {
		super(location);
	}

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
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
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
	 * write result
	 * @param writer response writer
	 * @param result result object
	 * @throws IOException
	 */
	protected void writeResult(PrintWriter writer, Object result) throws IOException {
		if (result != null) {
			writer.write(result.toString());
		}
	}
	
	/**
	 * write result
	 * @param ac action context
	 * @param result result object
	 */
	protected void writeResult(ActionContext ac, Object result) {
		try {
			HttpServletRequest request = ac.getRequest();
			HttpServletResponse response = ac.getResponse();
	
			HttpServletSupport hss = new HttpServletSupport(request, response);
			hss.setMaxAge(maxAge);
			hss.setCharset(encoding);
			hss.setContentType(contentType);
			hss.setAttachment(attachment);
			hss.setFileName(filename);
			hss.setBom(bom);
			hss.writeResponseHeader();
	
			PrintWriter writer = response.getWriter();
			writeResult(writer, result);
			writer.flush();
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}

