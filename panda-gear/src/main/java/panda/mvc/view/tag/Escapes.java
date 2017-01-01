package panda.mvc.view.tag;

import panda.lang.StringEscapes;
import panda.lang.Strings;

public class Escapes {
	/**
	 * ESCAPE_NONE = "";
	 */
	public static final String ESCAPE_NONE = "";

	/**
	 * ESCAPE_HTML = "html";
	 */
	public static final String ESCAPE_HTML = "html";
	
	/**
	 * ESCAPE_PHTML = "phtml";
	 */
	public static final String ESCAPE_PHTML = "phtml";
	
	/**
	 * ESCAPE_JAVASCRIPT = "js";
	 */
	public static final String ESCAPE_JAVASCRIPT = "js";
	
	/**
	 * ESCAPE_CSV = "csv";
	 */
	public static final String ESCAPE_CSV = "csv";
	
	/**
	 * ESCAPE_XML = "xml";
	 */
	public static final String ESCAPE_XML = "xml";
	
	public static String escape(CharSequence value, String escape) {
		if (Strings.isEmpty(value) || Strings.isEmpty(escape)) {
			return Strings.EMPTY;
		}

		switch (escape.charAt(0)) {
		case 'h':
			return StringEscapes.escapeHtml(value);
		case 'p':
			return StringEscapes.escapePhtml(value);
		case 'j':
			return StringEscapes.escapeJavaScript(value);
		case 'c':
			return StringEscapes.escapeCsv(value);
		case 'x':
			return StringEscapes.escapeXml(value);
		default:
			return value.toString();
		}
	}
}
