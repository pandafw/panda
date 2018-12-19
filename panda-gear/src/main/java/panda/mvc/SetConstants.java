package panda.mvc;

public interface SetConstants {

	/**
	 * debug setting
	 */
	public static final String APP_DEBUG = "app.debug";

	/**
	 * web directory
	 */
	public static final String WEB_DIR = "web.dir";

	/**
	 * mvc mapping ignores
	 */
	public static final String MVC_MAPPING_IGNORES = "mvc.mapping.ignores";

	/** Access log name */
	public static final String MVC_ACCESS_LOG_NAME = "mvc.access.log.name";

	/** Access log format */
	public static final String MVC_ACCESS_LOG_FORMAT = "mvc.access.log.format";

	/**
	 * mvc http dump path
	 */
	public static final String MVC_HTTP_DUMP_PATH = "mvc.http.dump.path";

	/**
	 * mvc http dump request
	 */
	public static final String MVC_HTTP_DUMP_REQUEST = "mvc.http.dump.request";

	/**
	 * mvc http dump response
	 */
	public static final String MVC_HTTP_DUMP_RESPONSE = "mvc.http.dump.response";

	/**
	 * disable sitemesh?
	 */
	public static final String MVC_SITEMESH_DISABLE = "mvc.sitemesh.disable";

	/**
	 * use cdn for js/css
	 */
	public static final String MVC_LINK_USECDN = "mvc.link.usecdn";

	/** html editor (summernote or cleditor) */
	public static final String MVC_TAG_HTML_EDITOR = "mvc.tag.html.editor";

	/** JsonView Pretty Print */
	public static final String MVC_VIEW_JSON_PRETTY = "mvc.view.json.pretty";

	/** XmlView Pretty Print */
	public static final String MVC_VIEW_XML_PRETTY = "mvc.view.xml.pretty";

	/** The static resource version */
	public static final String MVC_STATIC_VERSION = "mvc.static.version";

	/** The static resource browser cache max age (seconds) */
	public static final String MVC_STATIC_MAXAGE = "mvc.static.maxage";

	/** The static resource charset */
	public static final String MVC_STATIC_CHARSET = "mvc.static.charset";
}
