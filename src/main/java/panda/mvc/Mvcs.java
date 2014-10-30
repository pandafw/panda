package panda.mvc;

import javax.servlet.ServletContext;

import panda.bean.Beans;
import panda.bind.json.JsonArray;
import panda.bind.json.JsonObject;
import panda.cast.Castors;
import panda.el.El;
import panda.filepool.FileItemCastor;
import panda.lang.Objects;

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
	
	/**
	 * eval parameter
	 */
	public static Object eval(String expr, ActionContext context) {
		return eval(expr, context, Objects.NULL);
	}
	
	/**
	 * eval parameter
	 */
	public static Object eval(String expr, ActionContext ac, Object arg) {
		if (Objects.NULL == arg) {
			return El.eval(expr, ac);
		}

		try {
			ac.push(arg);
			return El.eval(expr, ac);
		}
		finally {
			ac.pop();
		}
	}
	
	public static Object translate(Object val, ActionContext ac) {
		return translate(val, ac, Objects.NULL);
	}
	
	public static Object translate(Object val, ActionContext ac, Object arg) {
		if (val instanceof String) {
			String s = (String)val;
			if (s.length() > 3) {
				char c0 = s.charAt(0);
				char c1 = s.charAt(1);
				char cx = s.charAt(s.length() - 1);
				if ((c0 == '$' || c0 == '%') && c1 == '{' && cx == '}') {
					val = eval(s.substring(2, s.length() - 1), ac, arg);
				}
				else if (c0 == '#' && c1 == '{' && cx == '}') {
					val = JsonObject.fromJson(s.substring(1));
				}
				else if (c0 == '#' && c1 == '[' && cx == ']') {
					val = JsonArray.fromJson(s.substring(1));
				}
			}
		}
		
		return val;
	}
}

