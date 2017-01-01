package panda.wing;

import panda.mvc.MvcConstants;

public interface AppConstants extends MvcConstants {
	/** The cache */
	public static final String PANDA_CACHE = "panda.cache";

	/** The wkhtmltopdf exe path */
	public static final String PANDA_WKHTML2PDF_PATH = "panda.wkhtml2pdf.path";

	/** The wkhtmltopdf processor wait timeout */
	public static final String PANDA_WKHTML2PDF_TIMEOUT = "panda.wkhtml2pdf.timeout";

	/** The fonts path used by flyingsaucer */
	public static final String PANDA_FONTS_PATH = "panda.fonts.path";

	/** The load task keys */
	public static final String PANDA_TASK_LOAD_KEYS = "panda.task.load.keys";

	/** The secret */
	public static final String PANDA_SECRET_AUTH_KEY = "panda.secret.auth.key";
	
	public static final String PANDA_SECRET_AUTH_CIPHER = "panda.secret.auth.cipher";
	
	/** The authenticate */
	public static final String PANDA_AUTH_ALLOW_UNKNOWN_URL = "panda.auth.allow.unknown.url";
	
	public static final String PANDA_AUTH_UNLOGIN_VIEW = "panda.auth.unlogin.view";
	
	public static final String PANDA_AUTH_UNSECURE_VIEW = "panda.auth.unsecure.view";
	
	public static final String PANDA_AUTH_UNLOGIN_URL = "panda.auth.unlogin.url";
	
	public static final String PANDA_AUTH_UNSECURE_URL = "panda.auth.unsecure.url";
	
	public static final String PANDA_AUTH_SECURE_USER_AGE = "panda.auth.secure.session.age";
	
	public static final String PANDA_AUTH_USER_TYPE = "panda.auth.user.type";
	
	public static final String PANDA_AUTH_TICKET_COOKIE_AGE = "panda.auth.ticket.cookie.age";
}
