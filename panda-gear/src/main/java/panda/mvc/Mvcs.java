package panda.mvc;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

import panda.Panda;
import panda.bean.Beans;
import panda.bind.json.JsonArray;
import panda.bind.json.JsonObject;
import panda.cast.CastContext;
import panda.cast.Castors;
import panda.cast.castor.FileItemCastor;
import panda.el.EL;
import panda.el.ELTemplate;
import panda.io.Settings;
import panda.ioc.Ioc;
import panda.lang.Chars;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.impl.DefaultValidateHandler;
import panda.mvc.util.TextProvider;

/**
 * Mvc helper methods
 */
public abstract class Mvcs {
	public static final String PANDA_CDN = "//pandafw.github.io/repos";

	private static Beans beans = Beans.i();
	private static Castors castors = Castors.i();
	
	/**
	 * DATE_TIMEZONE = "date-timezone";
	 */
	public static final String DATE_TIMEZONE = "date-timezone";
	
	/**
	 * DATE_FORMAT_DEFAULT = "date-format";
	 */
	public static final String DATE_FORMAT_DEFAULT = "date-format";

	/**
	 * DATE_FORMAT_PREFIX = "date-format-";
	 */
	public static final String DATE_FORMAT_PREFIX = "date-format-";

	/**
	 * NUMBER_FORMAT_DEFAULT = "number-format";
	 */
	public static final String NUMBER_FORMAT_DEFAULT = "number-format";

	/**
	 * NUMBER_FORMAT_PREFIX = "number-format-";
	 */
	public static final String NUMBER_FORMAT_PREFIX = "number-format-";

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
	 * get action handler from servlet context
	 * @param sc servlet context
	 * @return action handler
	 */
	public static ActionHandler getActionHandler(ServletContext sc) {
		return (ActionHandler)sc.getAttribute(ActionHandler.class.getName());
	}
	
	/**
	 * set action handler to servlet context
	 * @param sc servlet context
	 * @param ah action handler
	 */
	public static void setActionHandler(ServletContext sc, ActionHandler ah) {
		if (ah == null) {
			sc.removeAttribute(ActionHandler.class.getName());
		}
		else {
			sc.setAttribute(ActionHandler.class.getName(), ah);
		}
	}
	
	/** 
	 * get Ioc from servlet context
	 * @param sc servlet context
	 * @return ioc
	 */
	public static Ioc getIoc(ServletContext sc) {
		ActionHandler handler = getActionHandler(sc);
		if (handler == null) {
			return null;
		}
		
		return handler.getConfig().getIoc();
	}
	
	/**
	 * get action context from servlet request
	 * @param req servlet request
	 * @return action context
	 */
	public static ActionContext getActionContext(ServletRequest req) {
		return (ActionContext)req.getAttribute(ActionContext.class.getName());
	}

	/**
	 * set action context to servlet request
	 * @param req servlet request
	 * @param ac action context
	 */
	public static void setActionContext(ServletRequest req, ActionContext ac) {
		if (ac == null) {
			req.removeAttribute(ActionContext.class.getName());
		}
		else {
			req.setAttribute(ActionContext.class.getName(), ac);
		}
	}
	
	/**
	 * @param ac the action context
	 * @param sb the string
	 * @return the static base path
	 */
	public static String getStaticBase(ActionContext ac, String sb) {
		if (Strings.isEmpty(sb)) {
			Settings ss = ac.getSettings();
			if (ss.getPropertyAsBoolean(SiteConstants.SITE_CDN)) {
				sb = PANDA_CDN;
			}
		}

		if (Strings.isEmpty(sb)) {
			sb = ac.getIoc().getIfExists(String.class, MvcConstants.UI_STATIC_BASE);
		}
		
		if (Strings.isEmpty(sb)) {
			sb = ac.getServlet().getContextPath() + "/static";
		}
		else if (sb.charAt(0) == '~') {
			sb = ac.getRequest().getContextPath() + sb.substring(1);
		}
		
		return sb + '/' + Panda.VERSION;
	}

	/**
	 * create a instance
	 * 
	 * @param ioc the IOC
	 * @param type the type
	 * @return ioc bean instance or new object instance
	 */
	public static <T> T born(Ioc ioc, Class<T> type) {
		T obj = ioc.getIfExists(type);
		if (obj == null) {
			obj = Classes.born(type);
		}
		return obj;
	}

	/**
	 * cast to string
	 * @param ac action context
	 * @param value object value
	 * @return casted string
	 */
	public static String castString(ActionContext ac, Object value) {
		return castValue(ac, value, String.class, null);
	}

	/**
	 * cast to string
	 * @param ac action context
	 * @param value object value
	 * @param format value format
	 * @return casted string
	 */
	public static String castString(ActionContext ac, Object value, String format) {
		return castValue(ac, value, String.class, format);
	}

	/**
	 * cast value
	 * @param ac action context
	 * @param value object value
	 * @param type the type to cast
	 * @return casted object
	 */
	public static <T> T castValue(ActionContext ac, Object value, Type type) {
		return castValue(ac, value, type, null);
	}

	/**
	 * cast value
	 * @param ac action context
	 * @param value object value
	 * @param type the type to cast
	 * @param format value format
	 * @return casted object
	 */
	public static <T> T castValue(ActionContext ac, Object value, Type type, String format) {
		Castors cs = getCastors();
		CastContext cc = cs.newCastContext();
		
		cc.setSkipCastError(true);
		cc.set(FileItemCastor.KEY, ac.getFilePool());
		cc.setFormat(format);
		cc.setLocale(ac.getLocale());
		
		return cs.cast(value, type, cc);
	}

	/**
	 * cast value and save errors to action context
	 * @param ac action context
	 * @param value object value
	 * @param type the type to cast
	 * @param format value format
	 * @return casted object
	 */
	public static <T> T castValueWithErrors(ActionContext ac, Object value, Type type, String format) {
		return castValueWithErrors(ac, null, value, type, format);
	}

	/**
	 * cast value and save errors to action context
	 * @param ac action context
	 * @param name value name
	 * @param value object value
	 * @param type the type to cast
	 * @param format value format
	 * @return casted object
	 */
	public static <T> T castValueWithErrors(ActionContext ac, String name, Object value, Type type, String format) {
		Castors cs = Mvcs.getCastors();
		CastContext cc = cs.newCastContext();
		
		cc.setSkipCastError(true);
		cc.setPrefix(name);
		cc.set(FileItemCastor.KEY, ac.getFilePool());
		cc.setFormat(format);
		cc.setLocale(ac.getLocale());
		
		T o = cs.cast(value, type, cc);
		if (Collections.isNotEmpty(cc.getErrors())) {
			ac.addCastErrors(cc.getErrors());
		}
		return o;
	}

	/**
	 * find value in context
	 * @param ac action context
	 * @param expr expression
	 * @return value
	 */
	public static Object findValue(ActionContext ac, String expr) {
		return findValue(ac, expr, Objects.NULL);
	}
	
	/**
	 * find value in context with argument
	 * @param ac action context
	 * @param expr expression
	 * @param arg argument
	 * @return value
	 */
	public static Object findValue(ActionContext ac, String expr, Object arg) {
		if (Objects.NULL == arg) {
			return EL.eval(expr, ac);
		}

		try {
			ac.push(arg);
			return EL.eval(expr, ac);
		}
		finally {
			ac.pop();
		}
	}

	/**
	 * evaluate string value
	 * ${...}, %{...} : el eval
	 * !{...} : json object
	 * ![...] : json array
	 * #(...) : text
	 * @param ac action context
	 * @param expr expression
	 * @return evaluated object
	 */
	public static Object evaluate(ActionContext ac, Object expr) {
		if (expr instanceof String) {
			return evaluate(ac, (String)expr);
		}
		return expr;
	}
	
	/**
	 * evaluate string value
	 * ${...}, %{...} : el eval
	 * !{...} : json object
	 * ![...] : json array
	 * #(...) : text
	 * @param ac action context
	 * @param expr expression
	 * @param arg argument
	 * @return evaluated object
	 */
	public static Object evaluate(ActionContext ac, Object expr, Object arg) {
		if (expr instanceof String) {
			return evaluate(ac, (String)expr, arg);
		}
		return expr;
	}

	/**
	 * evaluate string value
	 * ${...}, %{...} : el eval
	 * !{...} : json object
	 * ![...] : json array
	 * #(...) : text
	 * @param ac action context
	 * @param expr expression
	 * @return evaluated object
	 */
	public static Object evaluate(ActionContext ac, String expr) {
		return evaluate(ac, expr, Objects.NULL);
	}
	
	/**
	 * evaluate string value
	 * ${...}, %{...} : el eval
	 * !{...} : json object
	 * ![...] : json array
	 * #(...) : text
	 * @param ac action context
	 * @param expr expression
	 * @param arg argument
	 * @return evaluated object
	 */
	public static Object evaluate(ActionContext ac, String expr, Object arg) {
		if (Strings.isEmpty(expr)) {
			return expr;
		}
		
		Object val = expr;
		if (expr.length() > 3) {
			char c0 = expr.charAt(0);
			char c1 = expr.charAt(1);
			char cx = expr.charAt(expr.length() - 1);
			if ((c0 == Chars.DOLLAR || c0 == Chars.PERCENT) && c1 == Chars.BRACES_LEFT && cx == Chars.BRACES_RIGHT) {
				val = findValue(ac, expr.substring(2, expr.length() - 1), arg);
			}
			else if (c0 == Chars.EXCLAMATION && c1 == Chars.BRACES_LEFT && cx == Chars.BRACES_RIGHT) {
				val = JsonObject.fromJson(expr.substring(1));
			}
			else if (c0 == Chars.EXCLAMATION && c1 == Chars.BRACKETS_LEFT && cx == Chars.BRACKETS_RIGHT) {
				val = JsonArray.fromJson(expr.substring(1));
			}
			else if (c0 == Chars.SHARP && c1 == Chars.PARENTHESES_LEFT && cx == Chars.PARENTHESES_RIGHT) {
				String k = expr.substring(2, expr.length() - 1);
				val = ac.getText().getText(k, "", arg);
			}
		}
		
		return val;
	}
	

	/**
	 * translate expression
	 * @param ac action context
	 * @param expr expression
	 * @return translated string
	 */
	public static String translate(ActionContext ac, String expr) {
		return translate(ac, expr, Objects.NULL);
	}
	
	/**
	 * translate expression
	 * @param ac action context
	 * @param expr expression
	 * @param arg argument
	 * @return translated string
	 */
	public static String translate(ActionContext ac, String expr, Object arg) {
		if (Strings.isEmpty(expr)) {
			return expr;
		}
		
		if (arg != Objects.NULL) {
			try {
				ac.push(arg);
				return ELTemplate.evaluate(expr, ac);
			}
			finally {
				ac.pop();
			}
		}
		return ELTemplate.evaluate(expr, ac);
	}
	
	/**
	 * translate expression
	 * @param expr expression
	 * @param arg argument
	 * @return translated string
	 */
	public static String translate(String expr, Object arg) {
		return ELTemplate.evaluate(expr, arg);
	}

	//----------------------------------------------
	/**
	 * get date pattern from text
	 * @param ac action context
	 * @param format date format
	 * @return date pattern
	 */
	public static String getDatePattern(ActionContext ac, String format) {
		return getDatePattern(ac, format, null);
	}
	
	/**
	 * get date pattern from text
	 * @param ac action context
	 * @param format date format
	 * @param defv default value
	 * @return date pattern
	 */
	public static String getDatePattern(ActionContext ac, String format, String defv) {
		TextProvider tp = ac.getText();
		String pattern = null;

		if (Strings.isNotEmpty(format)) {
			pattern = tp.getText(DATE_FORMAT_PREFIX + format, (String)null);
			if (pattern == null) {
				pattern = format;
			}
		}
		else {
			pattern = tp.getText(DATE_FORMAT_DEFAULT, defv);
		}

		return pattern;
	}

	/**
	 * get date timezone from text
	 * @param ac action context
	 * @return date timezone
	 */
	public static TimeZone getDateTimeZone(ActionContext ac) {
		TextProvider tp = ac.getText();
		String tz = null;

		tz = tp.getText(DATE_TIMEZONE, (String)null);
		if (tz != null) {
			return TimeZone.getTimeZone(tz);
		}

		return null;
	}

	/**
	 * get number pattern from text
	 * @param ac action context
	 * @param format number format
	 * @return number pattern
	 */
	public static String getNumberPattern(ActionContext ac, String format) {
		TextProvider tp = ac.getText();
		String pattern = null;

		if (Strings.isNotEmpty(format)) {
			pattern = tp.getText(NUMBER_FORMAT_PREFIX + format, (String)null);
			if (pattern == null) {
				pattern = format;
			}
		}
		else {
			pattern = tp.getText(NUMBER_FORMAT_DEFAULT, (String)null);
		}

		return pattern;
	}

	/**
	 * get text from code map
	 * @param cm code map
	 * @param k key
	 * @return text
	 */
	public static String getCodeText(Map cm, Object k) {
		Object v = cm.get(k);
		if (v == null && k != null && !(k instanceof String)) {
			v = cm.get(k.toString());
		}
		return (v == null ? Strings.EMPTY : v.toString());
	}

	/**
	 * create validate handler
	 * @param context action context
	 * @return validate handler instance
	 */
	public static ValidateHandler getValidateHandler(ActionContext context) {
		ValidateHandler vh = context.getIoc().getIfExists(ValidateHandler.class);
		if (vh == null) {
			vh = new DefaultValidateHandler();
		}
		return vh;
	}

	/**
	 * Use validators to validate object
	 * @param context action context
	 * @param value validate value
	 * @return true if no validation error
	 */
	public static boolean validate(ActionContext context, Object value) {
		return validate(context, value, "");
	}

	/**
	 * Use validate handler to validate object
	 * @param context action context
	 * @param value validate value
	 * @param name object name
	 * @return true if no validation error
	 */
	public static boolean validate(ActionContext context, Object value, String name) {
		ValidateHandler vh = getValidateHandler(context);
		return vh.validate(context, name, value);
	}
}

