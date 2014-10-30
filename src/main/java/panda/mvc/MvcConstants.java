package panda.mvc;

import panda.ioc.IocConstants;

public interface MvcConstants extends IocConstants {
	public static final String FILEPOOL_LOCAL_PATH = "ref:panda.filepool.local.path";
	public static final String FILEPOOL_DAO_BLOCK_SIZE = "ref:panda.filepool.dao.block.size";
	public static final String FREEMARKER_TEMPLATE_PATH = "ref:panda.freemarker.path";

	/** The default loadmask option of form */
	public static final String UI_FORM_LOADMASK = "ref:panda.ui.form.loadmask";

	/** The default encode method to generate Struts URLs */
	public static final String UI_URL_ENCODE = "ref:panda.ui.url.encode";

	/** The defaults of datepicker */
	public static final String UI_DATEPICKER_DEFAULTS = "ref:panda.ui.datepicker.defaults";

	/** The defaults of timepicker */
	public static final String UI_TIMEPICKER_DEFAULTS = "ref:panda.ui.timepicker.defaults";

	/** The defaults of datetimepicker */
	public static final String UI_DATETIMEPICKER_DEFAULTS = "ref:panda.ui.datetimepicker.defaults";

	/** The static base url */
	public static final String UI_STATIC_BASE = "ref:panda.ui.static.base";

	/** The default UI template theme */
	public static final String UI_THEME = "ref:panda.ui.theme";
	
	/** The custom themes */
	public static final String UI_CUSTOM_THEMES = "ref:panda.ui.themes";
}
