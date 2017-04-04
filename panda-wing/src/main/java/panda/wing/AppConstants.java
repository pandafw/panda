package panda.wing;

import panda.mvc.MvcConstants;

public interface AppConstants extends MvcConstants {
	//------------------------------------------------
	// reloadable settings
	//
	/** The reloadable settings path */
	public static final String SETTINGS_RELOAD_PATH = "panda.settings.reload.path";

	/** The reloadable settings check interval */
	public static final String SETTINGS_RELOAD_INTERVAL = "panda.settings.reload.interval";

	//------------------------------------------------
	// cache
	//
	/** The cache ioc bean name */
	public static final String CACHE_IOCBEAN = "panda.cache";

	public static final String CACHE_PROVIDER = "panda.cache.provider";

	public static final String CACHE_NAME = "panda.cache.name";

	public static final String CACHE_MAXAGE = "panda.cache.maxage";
	
	//------------------------------------------------
	// task executor
	//
	public static final String EXECUTOR_ENABLE = "panda.executor.enable";

	public static final String EXECUTOR_NAME = "panda.executor.name";

	public static final String EXECUTOR_CORE_POOL_SIZE = "panda.executor.pool.size.core";

	public static final String EXECUTOR_MAX_POOL_SIZE = "panda.executor.pool.size.max";

	//------------------------------------------------
	// task scheduler
	//
	public static final String SCHEDULER_ENABLE = "panda.scheduler.enable";

	public static final String SCHEDULER_NAME = "panda.scheduler.name";

	public static final String SCHEDULER_POOL_SIZE = "panda.scheduler.pool.size";

	/** The task scheduler crons */
	public static final String SCHEDULER_CRONS = "panda.scheduler.crons";

	/** The task action scheme */
	public static final String TASK_ACTION_SCHEME = "panda.task.action.scheme";
	
	/** The load task keys */
	public static final String TASK_LOAD_KEYS = "panda.task.load.keys";

	//------------------------------------------------
	// html to pdf
	//
	/** The wkhtmltopdf exe path */
	public static final String WKHTML2PDF_PATH = "panda.wkhtml2pdf.path";

	/** The wkhtmltopdf processor wait timeout */
	public static final String WKHTML2PDF_TIMEOUT = "panda.wkhtml2pdf.timeout";

	//------------------------------------------------
	// The authenticate
	//
	public static final String AUTH_ALLOW_UNKNOWN_URL = "panda.auth.allow.unknown.url";
	
	public static final String AUTH_UNLOGIN_VIEW = "panda.auth.unlogin.view";
	
	public static final String AUTH_UNSECURE_VIEW = "panda.auth.unsecure.view";
	
	public static final String AUTH_UNLOGIN_URL = "panda.auth.unlogin.url";
	
	public static final String AUTH_UNSECURE_URL = "panda.auth.unsecure.url";
	
	public static final String AUTH_SECURE_USER_AGE = "panda.auth.secure.session.age";
	
	public static final String AUTH_USER_TYPE = "panda.auth.user.type";
	
	public static final String AUTH_TICKET_PARAM_NAME = "panda.auth.ticket.param.name";
	
	public static final String AUTH_TICKET_COOKIE_NAME = "panda.auth.ticket.cookie.name";

	public static final String AUTH_TICKET_COOKIE_AGE = "panda.auth.ticket.cookie.age";

	public static final String AUTH_SECRET_KEY = "panda.secret.auth.key";
	
	public static final String AUTH_SECRET_CIPHER = "panda.secret.auth.cipher";

	//------------------------------------------------
	// The lucene settings
	//
	public static final String LUCENE_LOCATION = "panda.lucene.location";

	public static final String LUCENE_ANALYZER = "panda.lucene.analyzer";

	//------------------------------------------------
	// The database settings
	//
	public static final String DATABASE_TEMPLATE = "panda.database.template";

	public static final String DATABASE_RESOURCE = "panda.database.resource";

	public static final String DATABASE_PROPERTY = "panda.database.property";

}
