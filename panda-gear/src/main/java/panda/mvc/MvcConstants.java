package panda.mvc;

import panda.ioc.IocConstants;

public interface MvcConstants extends IocConstants {
	public static final String TAGLIB_NAME = "panda.taglib.name";

	public static final String MVC_CHAINS = "panda.mvc.chains";

	public static final String MVC_VALIDATORS = "panda.mvc.validators";

	public static final String MVC_VIEWS = "panda.mvc.views";

	public static final String DEFAULT_RESOURCE_BUNDLES = "panda.resources.default";

	/* Crypto */
	public static final String CRYPTO_ALGORITHM = "panda.crypto.algorithm";

	public static final String CRYPTO_KEY_SECRET = "panda.crypto.key.secret";
	
	public static final String CRYPTO_KEY_ENCODE = "panda.crypto.key.encode";

	public static final String CRYPTO_KEY_DECODE = "panda.crypto.key.decode";

	/* File POOL */
	public static final String FILEPOOL_MAXAGE = "panda.filepool.maxage";
	public static final String FILEPOOL_LOCAL_PATH = "panda.filepool.local.path";
	public static final String FILEPOOL_DAO_BLOCK_SIZE = "panda.filepool.dao.block.size";

	public static final String MULTIPART_BODY_SIZE_MAX = "panda.multipart.body.size.max";
	public static final String MULTIPART_FILE_SIZE_MAX = "panda.multipart.file.size.max";
	public static final String MULTIPART_DRAIN_TIMEOUT = "panda.multipart.drain.timeout";
	
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

	/** The defaults of datepicker */
	public static final String UI_DATEPICKER_DEFAULTS = "panda.ui.datepicker.defaults";

	/** The defaults of timepicker */
	public static final String UI_TIMEPICKER_DEFAULTS = "panda.ui.timepicker.defaults";

	/** The defaults of datetimepicker */
	public static final String UI_DATETIMEPICKER_DEFAULTS = "panda.ui.datetimepicker.defaults";

	/** The static path */
	public static final String STATIC_PATH = "static_path";

	/** The default UI template theme */
	public static final String UI_THEME = "panda.ui.theme";

	/** The custom themes */
	public static final String UI_CUSTOM_THEMES = "panda.ui.themes";

	/** Custom Servlet Error View */
	public static final String SERVLET_ERROR_VIEW = "panda.view.servlet.error";

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

	/** The sitemesh configuration */
	public static final String SITEMESH = "panda.sitemesh";
}
