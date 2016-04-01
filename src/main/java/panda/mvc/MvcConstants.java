package panda.mvc;

import panda.ioc.IocConstants;

public interface MvcConstants extends IocConstants {
	public static final String TAGLIB_NAME = "panda.taglib.name";

	public static final String MVC_CHAINS = "panda.mvc.chains";
	
	public static final String CRONS = "panda.crons";
	
	public static final String DEFAULT_RESOURCE_BUNDLES = "panda.resources.default";
	
	public static final String FILEPOOL_EXPIRES = "panda.filepool.expires";
	public static final String FILEPOOL_LOCAL_PATH = "panda.filepool.local.path";
	public static final String FILEPOOL_DAO_BLOCK_SIZE = "panda.filepool.dao.block.size";

	public static final String MULTIPART_BODY_SIZE_MAX = "panda.multipart.body.size.max";
	public static final String MULTIPART_FILE_SIZE_MAX = "panda.multipart.file.size.max";
	
	public static final String FREEMARKER_TEMPLATES = "panda.freemarker.templates";
	public static final String FREEMARKER_TEMPLATES_WEB_PATH = "panda.freemarker.templates.web.path";
	public static final String FREEMARKER_SETTINGS = "panda.freemarker.settings";

	public static final String FREEMARKER_WRAPPER_ALT_MAP = "panda.freemarker.wrapper.altMap";

	/** Cache model instances at BeanWrapper level */
	public static final String FREEMARKER_BEANWRAPPER_CACHE = "panda.freemarker.beanwrapper.cache";

	/** Input encoding */
	public static final String IO_INPUT_ENCODING = "panda.io.input.encoding";

	/** The default encode method to generate Struts URLs */
	public static final String UI_URL_ENCODE = "panda.ui.url.encode";

	/** incude context path of url */
	public static final String UI_URL_INCLUDE_CONTEXT = "panda.url.include.context";

	/** The defaults of datepicker */
	public static final String UI_DATEPICKER_DEFAULTS = "panda.ui.datepicker.defaults";

	/** The defaults of timepicker */
	public static final String UI_TIMEPICKER_DEFAULTS = "panda.ui.timepicker.defaults";

	/** The defaults of datetimepicker */
	public static final String UI_DATETIMEPICKER_DEFAULTS = "panda.ui.datetimepicker.defaults";

	/** The static base url */
	public static final String UI_STATIC_BASE = "panda.ui.static.base";

	/** The default UI template theme */
	public static final String UI_THEME = "panda.ui.theme";

	/** The custom themes */
	public static final String UI_CUSTOM_THEMES = "panda.ui.themes";

	/** Custom Servlet Error View */
	public static final String SERVLET_ERROR_VIEW = "panda.view.servlet.error";

	/** The locale domains */
	public static final String LOCALE_DOMAINS = "panda.locale.domains";

	/** The allowed locale */
	public static final String LOCALE_ALLOWED = "panda.locale.allowed";

	/** The default locale */
	public static final String LOCALE_DEFAULT = "panda.locale.default";

	/** The sitemesh configuration */
	public static final String SITEMESH = "panda.sitemesh";
}
