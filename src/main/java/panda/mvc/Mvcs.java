package panda.mvc;

import javax.servlet.ServletContext;

import panda.bean.Beans;
import panda.castor.Castors;
import panda.filepool.FileItemCastor;

/**
 * Mvc 相关帮助函数
 */
public abstract class Mvcs {
	private static String name;
	private static ServletContext servletContext;
	private static MvcConfig mvcConfig;
	private static Beans beans = Beans.i();
	private static Castors castors = Castors.i();
	
	static {
		castors.register(new FileItemCastor());
	}
	
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

	/**
	 * @return the beans
	 */
	public static Beans getBeans() {
		return beans;
	}

	/**
	 * @param beans the beans to set
	 */
	public static void setBeans(Beans beans) {
		Mvcs.beans = beans;
	}

	/**
	 * @return the castors
	 */
	public static Castors getCastors() {
		return castors;
	}

	/**
	 * @param castors the castors to set
	 */
	public static void setCastors(Castors castors) {
		Mvcs.castors = castors;
	}
	
}
