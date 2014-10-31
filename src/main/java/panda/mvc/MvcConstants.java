package panda.mvc;

import panda.ioc.IocConstants;

public interface MvcConstants extends IocConstants {
	public static final String FILEPOOL_LOCAL_PATH = "panda.filepool.local.path";
	public static final String FILEPOOL_DAO_BLOCK_SIZE = "panda.filepool.dao.block.size";

	public static final String FREEMARKER_TEMPLATES = "panda.freemarker.templates";
	public static final String FREEMARKER_SETTINGS = "panda.freemarker.settings";

	public static final String FREEMARKER_WRAPPER_ALT_MAP = "panda.freemarker.wrapper.altMap";

	/** Cache model instances at BeanWrapper level */
	public static final String FREEMARKER_BEANWRAPPER_CACHE = "panda.freemarker.beanwrapper.cache";

	/** The default loadmask option of form */
	public static final String UI_FORM_LOADMASK = "panda.ui.form.loadmask";

	/** The default encode method to generate Struts URLs */
	public static final String UI_URL_ENCODE = "panda.ui.url.encode";

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
}
