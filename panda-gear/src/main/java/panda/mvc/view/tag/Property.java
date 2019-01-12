package panda.mvc.view.tag;

import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

import panda.io.stream.StringBuilderWriter;
import panda.ioc.annotation.IocBean;
import panda.lang.Iterators;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.mvc.view.util.Escapes;

/**
 * <!-- START SNIPPET: javadoc -->
 *
 * Used to get the property of a <i>value</i>, which will default to the top of
 * the stack if none is specified.
 *
 * <!-- END SNIPPET: javadoc -->
 *
 * <p/>
 *
 *
 * <!-- START SNIPPET: params -->
 *
 * <ul>
 * <li>default (String) - The default value to be used if <u>value</u> attribute
 * is null</li>
 * <li>escape (Boolean) - Escape HTML. Default to true</li>
 * <li>value (Object) - value to be displayed</li>
 * </ul>
 *
 * <!-- END SNIPPET: params -->
 *
 *
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 *
 * &lt;s:push value=&quot;myBean&quot;&gt;
 *     &lt;!-- Example 1: --&gt;
 *     &lt;s:property value=&quot;myBeanProperty&quot; /&gt;
 *
 *     &lt;!-- Example 2: --&gt;TextUtils
 *     &lt;s:property value=&quot;myBeanProperty&quot; default=&quot;a default value&quot; /&gt;
 * &lt;/s:push&gt;
 *
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 *
 * <pre>
 * &lt;!-- START SNIPPET: exampledescription --&gt;
 *
 * Example 1 prints the result of myBean's getMyBeanProperty() method.
 * Example 2 prints the result of myBean's getMyBeanProperty() method and if it is null, print 'a default value' instead.
 *
 * &lt;!-- END SNIPPET: exampledescription --&gt;
 * </pre>
 *
 *
 * <pre>
 * &lt;!-- START SNIPPET: i18nExample --&gt;
 *
 * &lt;s:property value=&quot;getText('some.key')&quot; /&gt;
 *
 * &lt;!-- END SNIPPET: i18nExample --&gt;
 * </pre>
 *
 */
@IocBean(singleton=false)
public class Property extends ContextBean {
	/**
	 * PASSWORD_FORMAT = "password-format";
	 */
	public static final String PASSWORD_FORMAT = "password-format";

	/**
	 * DEFAULT_PASSWORD_FORMAT = "******";
	 */
	public static final String DEFAULT_PASSWORD_FORMAT = "******";

	private String defaultValue;
	private String name;
	private String format;
	private String pattern;
	private Object value;
	private String escape = Escapes.ESCAPE_HTML;
	
	private CBoolean cbool;
	private CDate cdate;
	private CNumber cnumber;

	/**
	 * @see panda.mvc.view.tag.TagBean#start(java.io.Writer)
	 */
	public boolean start(Writer writer) {
		boolean result = super.start(writer);

		String av = formatValue();
		if (av == null) {
			av = defaultValue;
		}
		
		writeOrSetVar(writer, av);

		return result;
	}

	public String formatValue() {
		if (name == null) {
			name = "top";
		}

		Object a = value != null ? value : findValue(name);
		if (a == null) {
			return null;
		}
		
		if (Iterators.isIterable(a)) {
			StringBuilder sb = new StringBuilder();
			for (Object v : Iterators.asIterable(a)) {
				String s = formatValue(v);
				if (Strings.isNotEmpty(s)) {
					if (sb.length() > 0) {
						sb.append(' ');
					}
					sb.append(s);
				}
			}
			return sb.toString();
		}

		return formatValue(a);
	}

	protected String formatValue(Object v) {
		if (v == null) {
			return null;
		}

		String s;
		if (Strings.isEmpty(format) && Strings.isEmpty(pattern)) {
			s = escape(castString(v));
		}
		else if ("password".equalsIgnoreCase(format)) {
			s = context.getText().getText(PASSWORD_FORMAT, DEFAULT_PASSWORD_FORMAT);
		}
		else if ("link".equalsIgnoreCase(format)) {
			s = StringEscapes.escapeHtml(v.toString());
			if (Strings.isNotEmpty(s)) {
				s = "<a href=\"" + s + "\">" + s + "</a>";
			}
		}
		else if ("extlink".equalsIgnoreCase(format)) {
			s = StringEscapes.escapeHtml(v.toString());
			if (Strings.isNotEmpty(s)) {
				s = "<a target=\"_blank\" href=\"" + s + "\">" + s + "</a>";
			}
		}
		else {
			if (v instanceof Boolean) {
				StringBuilderWriter sw = new StringBuilderWriter();
				if (cbool == null) {
					cbool = newComponent(CBoolean.class);
				}
				cbool.setValue((Boolean)v);
				cbool.setPattern(pattern);
				cbool.setFormat(format);
				cbool.start(sw);
				cbool.end(sw, "");
				s = sw.toString();
			}
			else if (v instanceof Date || v instanceof Calendar) {
				StringBuilderWriter sw = new StringBuilderWriter();
				if (cdate == null) {
					cdate = newComponent(CDate.class);
				}
				cdate.setValue(v instanceof Calendar ? ((Calendar)v).getTime() : (Date)v);
				cdate.setPattern(pattern);
				cdate.setFormat(format);
				cdate.start(sw);
				cdate.end(sw, "");
				s = sw.toString();
			}
			else if (v instanceof Number) {
				StringBuilderWriter sw = new StringBuilderWriter();
				if (cnumber == null) {
					cnumber = newComponent(CNumber.class);
				}
				cnumber.setValue((Number)v);
				cnumber.setPattern(pattern);
				cnumber.setFormat(format);
				cnumber.start(sw);
				cnumber.end(sw, "");
				s = sw.toString();
			}
			else if (v instanceof String) {
				s = escape((String)v);
			}
			else {
				s = escape(castString(v));
			}
		}
		return s;
	}

	private String escape(String value) {
		return Escapes.escape(value, escape);
	}

	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefault(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @param escape the escape to set
	 */
	public void setEscape(String escape) {
		this.escape = escape;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

}
