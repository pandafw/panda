package panda.mvc;

import javax.servlet.ServletContext;

/**
 * Mvc 相关帮助函数
 */
public abstract class Mvcs {
	private static String name;
	private static ServletContext servletContext;
	private static MvcConfig mvcConfig;
	
	/**
	 * @return the name
	 */
	public static String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public static void setName(String name) {
		Mvcs.name = name;
	}
	/**
	 * @return the servletContext
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}
	/**
	 * @param servletContext the servletContext to set
	 */
	public static void setServletContext(ServletContext servletContext) {
		Mvcs.servletContext = servletContext;
	}
	/**
	 * @return the mvcConfig
	 */
	public static MvcConfig getMvcConfig() {
		return mvcConfig;
	}
	/**
	 * @param mvcConfig the mvcConfig to set
	 */
	public static void setMvcConfig(MvcConfig mvcConfig) {
		Mvcs.mvcConfig = mvcConfig;
	}
}
