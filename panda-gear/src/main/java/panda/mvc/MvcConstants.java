package panda.mvc;

public interface MvcConstants {

	/** The static resource path */
	public static final String STATIC_PATH = "static_path";

	public static final String TAGLIB_NAME = "panda.taglib.name";

	public static final String MVC_ACTION_CONTEXT_TYPE = "panda.action.context.type";

	public static final String MVC_ACTION_METHOD_SEPARATORS = "panda.action.method.separators";

	public static final String MVC_MAPPING_CASE_IGNORE = "panda.mvc.mapping.case.ignore";

	public static final String MVC_MAPPING_IGNORES = "panda.mvc.mapping.ignores";

	public static final String MVC_FILTERS = "panda.mvc.filters";

	public static final String MVC_CHAINS = "panda.mvc.chains";

	public static final String MVC_VIEWS = "panda.mvc.views";

	public static final String MVC_RESOURCES = "panda.mvc.resources";

	/* Crypto */
	public static final String CRYPTO_ALGORITHM = "panda.crypto.algorithm";

	public static final String CRYPTO_KEY_SECRET = "panda.crypto.key.secret";
	
	public static final String CRYPTO_KEY_ENCODE = "panda.crypto.key.encode";

	public static final String CRYPTO_KEY_DECODE = "panda.crypto.key.decode";

	/* File Store */
	public static final String FILESTORE_LOCATION = "panda.filestore.location";
	public static final String FILESTORE_DAO_BLOCK_SIZE = "panda.filestore.dao.block.size";
	public static final String FILESTORE_GCS_BUCKET = "panda.filestore.gcs.bucket";
	public static final String FILESTORE_GCS_PREFIX = "panda.filestore.gcs.prefix";

	/* MULTIPART */
	public static final String MULTIPART_BODY_MAXSIZE = "panda.multipart.body.maxsize";
	public static final String MULTIPART_FILE_MAXSIZE = "panda.multipart.file.maxsize";
	public static final String MULTIPART_DRAIN_TIMEOUT = "panda.multipart.drain.timeout";

	/* FILE UPLOAD */
	public static final String FILE_UPLOAD_TMPDIR = "panda.file.upload.tmpdir";
	public static final String FILE_UPLOAD_MAXAGE = "panda.file.upload.maxage";
	
	/* FREEMARKER */
	public static final String FREEMARKER_TEMPLATES = "panda.freemarker.templates";
	public static final String FREEMARKER_TEMPLATES_WEB_PATH = "panda.freemarker.templates.web.path";
	public static final String FREEMARKER_SETTINGS = "panda.freemarker.settings";

	public static final String FREEMARKER_WRAPPER_ALT_MAP = "panda.freemarker.wrapper.altMap";

	/** Cache model instances at BeanWrapper level */
	public static final String FREEMARKER_BEANWRAPPER_CACHE = "panda.freemarker.beanwrapper.cache";

	/** Request encoding */
	public static final String REQUEST_ENCODING = "panda.request.encoding";

	/** Parse empty parameters for http request */
	public static final String REQUEST_EMPTY_PARAMS = "panda.request.empty.params";

	/** The default url encodeing */
	public static final String URL_ENCODING = "panda.url.encoding";

	/** incude context path of url */
	public static final String URL_INCLUDE_CONTEXT = "panda.url.include.context";

	/** The default tag template theme */
	public static final String TAG_THEME = "panda.tag.theme";

	/** The custom tag themes */
	public static final String TAG_THEMES = "panda.tag.themes";

	/** JsonView Pretty Print */
	public static final String VIEW_JSON_PRETTY = "panda.view.json.pretty";

	/** XmlView Pretty Print */
	public static final String VIEW_XML_PRETTY = "panda.view.xml.pretty";

	/** Custom Servlet Error View */
	public static final String SERVLET_ERROR_VIEW = "panda.view.servlet.error";

	/** The locale header name */
	public static final String LOCALE_HEADER_NAME = "panda.locale.header.name";

	/** The locale parameter name */
	public static final String LOCALE_PARAMETER_NAME = "panda.locale.parameter.name";

	/** The locale request attribute */
	public static final String LOCALE_REQUEST_ATTR = "panda.locale.request.attr";

	/** The locale session attribute */
	public static final String LOCALE_SESSION_ATTR = "panda.locale.session.attr";

	/** The locale cookie name */
	public static final String LOCALE_COOKIE_NAME = "panda.locale.cookie.name";

	/** The locale cookie domain */
	public static final String LOCALE_COOKIE_DOMAIN = "panda.locale.cookie.domain";

	/** The locale cookie path */
	public static final String LOCALE_COOKIE_PATH = "panda.locale.cookie.path";

	/** The locale cookie max age */
	public static final String LOCALE_COOKIE_MAXAGE = "panda.locale.cookie.maxage";

	/** The locale domains */
	public static final String LOCALE_DOMAINS = "panda.locale.domains";

	/** The acceptable locale */
	public static final String LOCALE_ACCEPTS = "panda.locale.accepts";

	/** The default locale */
	public static final String LOCALE_DEFAULT = "panda.locale.default";

	/** The locale save to session */
	public static final String LOCALE_SAVE_TO_SESSION = "panda.locale.save.to.session";

	/** The locale save to cookie */
	public static final String LOCALE_SAVE_TO_COOKIE = "panda.locale.save.to.cookie";

	/** The locale from accept language */
	public static final String LOCALE_FROM_ACCEPT_LANGUAGE = "panda.locale.from.accept.language";

	/** The redirect parameter name */
	public static final String REDIRECT_PARAMETER = "panda.redirect.parameter";

	/** The redirect query name */
	public static final String REDIRECT_QUERY_NAME = "panda.redirect.query.name";

	/** The layout header name */
	public static final String LAYOUT_HEADER_NAME = "panda.layout.header.name";

	/** The layout parameter name */
	public static final String LAYOUT_PARAMETER_NAME = "panda.layout.parameter.name";

	/** The layout request attribute */
	public static final String LAYOUT_REQUEST_ATTR = "panda.layout.request.attr";

	/** The layout session attribute */
	public static final String LAYOUT_SESSION_ATTR = "panda.layout.session.attr";

	/** The layout cookie name */
	public static final String LAYOUT_COOKIE_NAME = "panda.layout.cookie.name";

	/** The layout cookie domain */
	public static final String LAYOUT_COOKIE_DOMAIN = "panda.layout.cookie.domain";

	/** The layout cookie path */
	public static final String LAYOUT_COOKIE_PATH = "panda.layout.cookie.path";

	/** The layout cookie max age */
	public static final String LAYOUT_COOKIE_MAXAGE = "panda.layout.cookie.maxage";

	/** The default layout */
	public static final String LAYOUT_DEFAULT = "panda.layout.default";

	/** The layout save to session */
	public static final String LAYOUT_SAVE_TO_SESSION = "panda.layout.save.to.session";

	/** The layout save to cookie */
	public static final String LAYOUT_SAVE_TO_COOKIE = "panda.layout.save.to.cookie";

	/** The layout from user agent */
	public static final String LAYOUT_FROM_USER_AGENT = "panda.layout.from.useragent";

	/** The token header name */
	public static final String TOKEN_HEADER_NAME = "panda.token.header.name";

	/** The token parameter name */
	public static final String TOKEN_PARAMETER_NAME = "panda.token.parameter.name";

	/** The token request attribute */
	public static final String TOKEN_REQUEST_ATTR = "panda.token.request.attr";

	/** The token session attribute */
	public static final String TOKEN_SESSION_ATTR = "panda.token.session.attr";

	/** The token cookie name */
	public static final String TOKEN_COOKIE_NAME = "panda.token.cookie.name";

	/** The token cookie domain */
	public static final String TOKEN_COOKIE_DOMAIN = "panda.token.cookie.domain";

	/** The token cookie path */
	public static final String TOKEN_COOKIE_PATH = "panda.token.cookie.path";

	/** The token cookie max age */
	public static final String TOKEN_COOKIE_MAXAGE = "panda.token.cookie.maxage";

	/** The token save to session */
	public static final String TOKEN_SAVE_TO_SESSION = "panda.token.save.to.session";

	/** The token save to cookie */
	public static final String TOKEN_SAVE_TO_COOKIE = "panda.token.save.to.cookie";

	/** The sitemesh configuration */
	public static final String SITEMESH = "panda.sitemesh";

	/** The sitemesh disable */
	public static final String SITEMESH_DISABLE = "panda.sitemesh.disable";
}
