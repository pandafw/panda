package panda.io;

import java.util.HashMap;
import java.util.Map;

import panda.lang.Strings;



public class MimeTypes {
	public static final String MULTIPART_PREFIX = "multipart/";
	
	/**
	 * HTTP content type header for multipart forms.
	 */
	public static final String MULTIPART_FORM_DATA = "multipart/form-data";

	/**
	 * HTTP content type header for multiple uploads.
	 */
	public static final String MULTIPART_MIXED = "multipart/mixed";

	public static final String X_WWW_FORM_URLECODED = "application/x-www-form-urlencoded";

	public static final String APP_JAVASCRIPT = "application/javascript";
	public static final String APP_JSON = "application/json";
	public static final String APP_PDF = "application/pdf";
	public static final String APP_STREAM = "application/octet-stream";
	public static final String APP_XLS = "application/vnd.ms-excel";
	public static final String APP_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static final String IMG_GIF = "image/gif";
	public static final String IMG_JPEG = "image/jpeg";
	public static final String IMG_PNG = "image/png";
	public static final String IMG_WEBP = "image/webp";

	public static final String TEXT_CSS = "text/css";
	public static final String TEXT_CSV = "text/comma-separated-values";
	public static final String TEXT_JAVASCRIPT = "text/javascript";
	public static final String TEXT_HTML = "text/html";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String TEXT_TSV = "text/tab-separated-values";
	public static final String TEXT_XML = "text/xml";
	
	/** not standard */
	public static final String TEXT_JSON = "text/json";

	
	private static final Map<String, String> aliases = new HashMap<String, String>();

	static {
		aliases.put("text", TEXT_PLAIN);
		aliases.put("xml", TEXT_XML);
		aliases.put("html", TEXT_HTML);
		aliases.put("htm", TEXT_HTML);
		aliases.put("stream", APP_STREAM);
		aliases.put("js", TEXT_JAVASCRIPT);
		aliases.put("json", APP_JSON);
		aliases.put("jpg", IMG_JPEG);
		aliases.put("jpeg", IMG_JPEG);
		aliases.put("png", IMG_PNG);
		aliases.put("webp", IMG_WEBP);
	}

	/**
	 * @param alias alias
	 * @return mime type from alias
	 */
	public static String getMimeType(String alias) {
		return Strings.defaultString(aliases.get(Strings.lowerCase(alias)), alias);
	}
}
