package panda.mvc;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TimeZone;

import panda.bean.Beans;
import panda.bind.json.JsonArray;
import panda.bind.json.JsonObject;
import panda.cast.CastContext;
import panda.cast.Castors;
import panda.cast.castor.FileItemCastor;
import panda.el.El;
import panda.el.ElTemplate;
import panda.ioc.Ioc;
import panda.lang.Classes;
import panda.lang.Marks;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.util.TextProvider;
import panda.mvc.validation.DefaultValidators;
import panda.mvc.validation.Validators;

/**
 * Mvc helper methods
 */
public abstract class Mvcs {
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
	 * cast to string
	 */
	public static String castString(ActionContext ac, Object value) {
		return castValue(ac, value, String.class, null);
	}

	/**
	 * cast to string
	 */
	public static String castString(ActionContext ac, Object value, String format) {
		return castValue(ac, value, String.class, format);
	}

	/**
	 * cast value
	 */
	public static <T> T castValue(ActionContext ac, Object value, Type type) {
		return castValue(ac, value, type, null);
	}

	/**
	 * cast value
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
	 * find value in context
	 */
	public static Object findValue(ActionContext context, String expr) {
		return findValue(context, expr, Objects.NULL);
	}
	
	/**
	 * find value in context with argument
	 */
	public static Object findValue(ActionContext ac, String expr, Object arg) {
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
	 * !{...} : json object
	 * ![...] : json array
	 * #(...) : text
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
	 */
	public static Object evaluate(ActionContext ac, String expr, Object arg) {
		Object val = expr;
		if (expr.length() > 3) {
			char c0 = expr.charAt(0);
			char c1 = expr.charAt(1);
			char cx = expr.charAt(expr.length() - 1);
			if ((c0 == Marks.DOLLAR || c0 == Marks.PERCENT) && c1 == Marks.BRACES_LEFT && cx == Marks.BRACES_RIGHT) {
				val = findValue(ac, expr.substring(2, expr.length() - 1), arg);
			}
			else if (c0 == Marks.EXCLAMATION && c1 == Marks.BRACES_LEFT && cx == Marks.BRACES_RIGHT) {
				val = JsonObject.fromJson(expr.substring(1));
			}
			else if (c0 == Marks.EXCLAMATION && c1 == Marks.BRACKETS_LEFT && cx == Marks.BRACKETS_RIGHT) {
				val = JsonArray.fromJson(expr.substring(1));
			}
			else if (c0 == Marks.SHARP && c1 == Marks.PARENTHESES_LEFT && cx == Marks.PARENTHESES_RIGHT) {
				String k = expr.substring(2, expr.length() - 1);
				val = ac.getText().getText(k, "", arg);
			}
		}
		
		return val;
	}
	

	/**
	 * translate expression
	 */
	public static String translate(ActionContext ac, String expr) {
		return translate(ac, expr, Objects.NULL);
	}
	
	/**
	 * translate expression
	 */
	public static String translate(ActionContext ac, String expr, Object arg) {
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

	//----------------------------------------------
	/**
	 * get date pattern from text
	 * @param format date format
	 * @return date pattern
	 */
	public static String getDatePattern(ActionContext context, String format) {
		return getDatePattern(context, format, null);
	}
	
	/**
	 * get date pattern from text
	 * @param format date format
	 * @return date pattern
	 */
	public static String getDatePattern(ActionContext context, String format, String defv) {
		TextProvider tp = context.getText();
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
	 * @return date timezone
	 */
	public static TimeZone getDateTimeZone(ActionContext context) {
		TextProvider tp = context.getText();
		String tz = null;

		tz = tp.getText(DATE_TIMEZONE, (String)null);
		if (tz != null) {
			return TimeZone.getTimeZone(tz);
		}

		return null;
	}

	/**
	 * get number pattern from text
	 * @param format number format
	 * @return number pattern
	 */
	public static String getNumberPattern(ActionContext context, String format) {
		TextProvider tp = context.getText();
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
	 * create validators
	 * @param context action context
	 * @return validators instance
	 */
	public static Validators getValidators(ActionContext context) {
		Validators validators = context.getIoc().getIfExists(Validators.class);
		if (validators == null) {
			validators = new DefaultValidators();
		}
		return validators;
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
	 * Use validators to validate object
	 * @param context action context
	 * @param value validate value
	 * @param name object name
	 * @return true if no validation error
	 */
	public static boolean validate(ActionContext context, Object value, String name) {
		Validators vs = getValidators(context);
		return vs.validate(context, name, value);
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
}

