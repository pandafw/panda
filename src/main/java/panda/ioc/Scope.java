package panda.ioc;

/**
 * 对象生命周期范围
 */
public interface Scope {
	/**
	 * ServletContext 级别
	 */
	public static final String APP = "app";

	/**
	 * Session 级别
	 */
	public static final String SESSION = "session";
	/**
	 * Request 级别
	 */
	public static final String REQUEST = "request";

}
