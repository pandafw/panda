package panda.mvc.view.tag;

import panda.lang.StringEscapes;

public class Escapes {
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
	
	public static String escape(String value, String escape) {
		String result = value;
		if (value != null) {
			if (ESCAPE_HTML.equals(escape)) {
				result = StringEscapes.escapeHtml(result);
			}
			else if (ESCAPE_PHTML.equals(escape)) {
				result = StringEscapes.escapePhtml(result);
			}
			else if (ESCAPE_JAVASCRIPT.equals(escape)) {
				result = StringEscapes.escapeJavaScript(result);
			}
			else if (ESCAPE_CSV.equals(escape)) {
				result = StringEscapes.escapeCsv(result);
			}
			else if (ESCAPE_XML.equals(escape)) {
				result = StringEscapes.escapeXml(result);
			}
		}
		return result;
	}
}
