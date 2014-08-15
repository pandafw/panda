package panda.net.http;



public class HttpContentType {
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
	public static final String TEXT_JAVASCRIPT = "text/javascript";
	/** not standard */
	public static final String TEXT_JSON = "text/json";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String TEXT_XML = "text/xml";
	public static final String APP_JSON = "application/json";
	public static final String APP_STREAM = "application/octet-stream";
}
