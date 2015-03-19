package panda.mvc;

import panda.bean.Beans;
import panda.bind.json.JsonArray;
import panda.bind.json.JsonObject;
import panda.cast.Castors;
import panda.el.El;
import panda.el.ElTemplate;
import panda.filepool.FileItemCastor;
import panda.ioc.Ioc;
import panda.lang.Classes;
import panda.lang.Objects;
import panda.lang.Strings;

/**
 * Mvc 相关帮助函数
 */
public abstract class Mvcs {
	private static Beans beans = Beans.i();
	private static Castors castors = Castors.i();
	
	static {
		castors.register(new FileItemCastor());
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
	 * @return static base path
	 */
	public static String getStaticBase(ActionContext ac, String sb) {
		if (sb == null) {
			sb = ac.getIoc().getIfExists(String.class, MvcConstants.UI_STATIC_BASE);
		}

		if (Strings.isEmpty(sb)) {
			sb = ac.getServlet().getContextPath() + "/static";
		}
		else if (sb.charAt(0) == '~') {
			sb = ac.getRequest().getContextPath() + sb.substring(1);
		}
		return sb;
	}

	/**
	 * create a instance
	 */
	public static <T> T born(Ioc ioc, Class<T> type) {
		T obj = ioc.getIfExists(type);
		if (obj == null) {
			obj = Classes.born(type);
		}
		return obj;
	}

	/**
	 * find value in context
	 */
	public static Object findValue(String expr, ActionContext context) {
		return findValue(expr, context, Objects.NULL);
	}
	
	/**
	 * find value in context with argument
	 */
	public static Object findValue(String expr, ActionContext ac, Object arg) {
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

	/**
	 * evaluate string value
	 * ${...}, %{...} : el eval
	 * #{...} : json object
	 * #[...] : json array
	 * #(...) : text
	 */
	public static Object evaluate(Object val, ActionContext ac) {
		return evaluate(val, ac, Objects.NULL);
	}
	
	/**
	 * evaluate string value
	 * ${...}, %{...} : el eval
	 * #{...} : json object
	 * #[...] : json array
	 * #(...) : text
	 */
	public static Object evaluate(Object val, ActionContext ac, Object arg) {
		if (val instanceof String) {
			String s = (String)val;
			if (s.length() > 3) {
				char c0 = s.charAt(0);
				char c1 = s.charAt(1);
				char cx = s.charAt(s.length() - 1);
				if ((c0 == '$' || c0 == '%') && c1 == '{' && cx == '}') {
					val = findValue(s.substring(2, s.length() - 1), ac, arg);
				}
				else if (c0 == '!' && c1 == '{' && cx == '}') {
					val = JsonObject.fromJson(s.substring(1));
				}
				else if (c0 == '!' && c1 == '[' && cx == ']') {
					val = JsonArray.fromJson(s.substring(1));
				}
				else if (c0 == '#' && c1 == '(' && cx == ')') {
					val = ac.text(s.substring(2, s.length() - 1));
				}
			}
		}
		
		return val;
	}
	

	/**
	 * translate expression
	 */
	public static String translate(String expr, ActionContext ac) {
		return translate(expr, ac, Objects.NULL);
	}
	
	/**
	 * translate expression
	 */
	public static String translate(String expr, ActionContext ac, Object arg) {
		if (arg != Objects.NULL) {
			try {
				ac.push(arg);
				return ElTemplate.evaluate(expr, ac);
			}
			finally {
				ac.pop();
			}
		}
		return ElTemplate.evaluate(expr, ac);
	}
	
	/**
	 * translate expression
	 */
	public static String translate(String expr, Object arg) {
		return ElTemplate.evaluate(expr, arg);
	}
}

