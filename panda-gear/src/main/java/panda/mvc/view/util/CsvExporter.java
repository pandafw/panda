package panda.mvc.view.util;

import java.io.IOException;
import java.io.Writer;

import panda.io.stream.CsvWriter;
import panda.ioc.annotation.IocBean;

@IocBean(singleton=false)
public class CsvExporter extends ListExporter {
	
	protected Character separator;
	protected Character quotechar;
	protected Character escapechar;
	
	/**
	 * @return the separator
	 */
	public Character getSeparator() {
		return separator;
	}

	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(Character separator) {
		this.separator = separator;
	}

	/**
	 * @return the quotechar
	 */
	public Character getQuotechar() {
		return quotechar;
	}

	/**
	 * @param quotechar the quotechar to set
	 */
	public void setQuotechar(Character quotechar) {
		this.quotechar = quotechar;
	}

	/**
	 * @return the escapechar
	 */
	public Character getEscapechar() {
		return escapechar;
	}

	/**
	 * @param escapechar the escapechar to set
	 */
	public void setEscapechar(Character escapechar) {
		this.escapechar = escapechar;
	}

	/**
	 * export list to writer
	 * @param writer writer
	 * @throws IOException if a I/O error occurs
	 */
	public void export(Writer writer) throws IOException {
		CsvWriter cw = new CsvWriter(writer);
		if (separator != null) {
			cw.setSeparator(separator);
		}
		if (quotechar != null) {
			cw.setQuotechar(quotechar);
		}
		if (escapechar != null) {
			cw.setEscapechar(escapechar);
		}
		export(cw);
		cw.flush();
	}

}
