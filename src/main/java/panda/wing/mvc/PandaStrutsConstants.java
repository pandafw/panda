package panda.wing.mvc;

/**
 * This class provides a central location for framework configuration keys
 * used to retrieve and store Struts configuration settings.
 */
public class PandaStrutsConstants {

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
	public static final String PANDA_FORM_LOADMASK = "panda.form.loadmask";

	/** The default includeParams method to generate Struts URLs */
	public static final String PANDA_URL_INCLUDEPARAMS = "panda.url.includeParams";

	/** The default encode method to generate Struts URLs */
	public static final String PANDA_URL_ENCODE = "panda.url.encode";
	
	/** The default escapeAmp method to generate Struts URLs */
	public static final String PANDA_URL_ESCAPEAMP = "panda.url.escapeAmp";

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
}
