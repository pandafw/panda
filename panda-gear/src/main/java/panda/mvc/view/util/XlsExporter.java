package panda.mvc.view.util;

import java.io.IOException;
import java.io.OutputStream;

import panda.io.stream.XlsWriter;
import panda.ioc.annotation.IocBean;

@IocBean(singleton=false)
public class XlsExporter extends ListExporter {
	
	/** xls all string cell */
	protected boolean allString;

	/** xls date cell fomat */
	protected String dateFormat;
	
	/**
	 * @return the allString
	 */
	public boolean isAllString() {
		return allString;
	}

	/**
	 * @param allString the allString to set
	 */
	public void setAllString(boolean allString) {
		this.allString = allString;
	}

	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * export list to stream
	 * @param os output stream
	 * @throws IOException
	 */
	public void export(OutputStream os) throws IOException {
		XlsWriter xw = newXlsWriter(os);
		xw.setAllString(allString);
		xw.setDateFormat(dateFormat);
		export(xw);
		xw.flush();
	}
	
	/**
	 * create a XlsWriter
	 * @param os output stream
	 * @return XlsWriter
	 * @throws IOException
	 */
	protected XlsWriter newXlsWriter(OutputStream os) throws IOException {
		return new XlsWriter(os);
	}
}
