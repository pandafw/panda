package panda.mvc;

import panda.ioc.IocConstants;

public interface MvcConstants extends IocConstants {
	public static final String FILEPOOL_LOCAL_PATH = "ref:panda.filepool.local.path";
	public static final String FILEPOOL_DAO_BLOCK_SIZE = "ref:panda.filepool.dao.block.size";
	public static final String FREEMARKER_TEMPLATE_PATH = "ref:panda.freemarker.path";

	/** */
	public static final String PANDA_JSON_SHORT_NAME = "panda.json.shortname";

	/** */
	public static final String PANDA_XML_SHORT_NAME = "panda.xml.shortname";

	/**  */
	public static final String PANDA_WELCOME_TO_SLASH = "panda.welcome.to.slash";

	/**  */
	public static final String PANDA_WELCOME_IGNORE_DOT = "panda.welcome.ignoredot";

	/** the default package of struts configuration */
	public static final String PANDA_PACKAGE_DEFAULT = "panda.package.default";

	/**
	 * PANDA_ACTION_SUPPORT = "panda.action.support";
	 */
	public static final String PANDA_ACTION_SUPPORT = "panda.action.support";

	/** The welcome action */
	public static final String PANDA_ACTION_WELCOME = "panda.action.welcome";

	/** The action assist */
	public static final String PANDA_ACTION_ASSIST = "panda.action.assist";

	/** The action consts */
	public static final String PANDA_ACTION_CONSTS = "panda.action.consts";

	/** The fonts path */
	public static final String PANDA_FONTS_PATH = "panda.fonts.path";

	/** The valid locale */
	public static final String PANDA_LOCALE_VALID = "panda.locale.valid";

	/** The default loadmask option of form */
	public static final String PANDA_FORM_LOADMASK = "ref:panda.form.loadmask";

	/** The default encode method to generate Struts URLs */
	public static final String PANDA_URL_ENCODE = "panda.url.encode";

	/** The checkbox intercept */
	public static final String PANDA_CHECKBOX_INTERCEPT = "panda.checkbox.intercept";

	/** The multiselect intercept */
	public static final String PANDA_MULTISELECT_INTERCEPT = "panda.multiselect.intercept";

	/** The defaults of datepicker */
	public static final String PANDA_DATEPICKER_DEFAULTS = "panda.datepicker.defaults";

	/** The defaults of timepicker */
	public static final String PANDA_TIMEPICKER_DEFAULTS = "panda.timepicker.defaults";

	/** The defaults of datetimepicker */
	public static final String PANDA_DATETIMEPICKER_DEFAULTS = "panda.datetimepicker.defaults";

	/** The maximize size of a multipart request (file upload) */
	public static final String PANDA_MULTIPART_MAXSIZE = "panda.multipart.maxSize";

	/** The directory to use for storing uploaded files */
	public static final String PANDA_MULTIPART_SAVEDIR = "panda.multipart.saveDir";

	/** The static base url */
	public static final String PANDA_STATIC_BASE = "ref:panda.static.base";

	/** The default UI template theme */
	public static final String PANDA_UI_THEME = "ref:panda.ui.theme";
	
	/** The custom themes */
	public static final String PANDA_CUSTOM_THEMES = "ref:panda.mvc.themes.custom";
}
