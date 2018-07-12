package panda.io;

import java.io.File;

import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.Logs;



public class MimeTypes {
	public static final String SYSPROP = "panda.mimetypes";
	public static final String CONFIG = MimeTypes.class.getPackage().getName().replace('.', '/') + "/mimetypes.properties";

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
	public static final String TEXT_TSV = "text/tab-separated-values";
	public static final String TEXT_JAVASCRIPT = "text/javascript";
	public static final String TEXT_HTML = "text/html";
	public static final String TEXT_PLAIN = "text/plain";
	public static final String TEXT_XML = "text/xml";
	
	/** not standard */
	public static final String TEXT_JSON = "text/json";

	/** mime type mapping */
	private static Settings mimetypes;

	static {
		loadMimeTypes();
	}

	private static void loadMimeTypes() {
		mimetypes = new Settings();

		String file = Systems.getProperty(SYSPROP, CONFIG);
		try {
			// load settings
			mimetypes.load(file);
		}
		catch (Throwable e) {
			Logs.getLog(MimeTypes.class).warn("Failed to load mime types: " + file, e);
		}
	}
	
	/**
	 * @param filename file name
	 * @return mime type by file extension
	 */
	public static String getMimeType(String filename) {
		return Strings.defaultString(mimetypes.get(Strings.lowerCase(FileNames.getExtension(filename))), APP_STREAM);
	}

	/**
	 * @param file file
	 * @return mime type by file extension
	 */
	public static String getMimeType(File file) {
		return Strings.defaultString(mimetypes.get(Strings.lowerCase(FileNames.getExtension(file))), APP_STREAM);
	}
}
