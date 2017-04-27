package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

import panda.io.stream.StringBuilderWriter;
import panda.ioc.annotation.IocBean;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.mvc.MvcException;

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
	 * @see panda.mvc.view.tag.Component#start(java.io.Writer)
	 */
	public boolean start(Writer writer) {
		boolean result = super.start(writer);

		String av = formatValue();

		try {
			if (av == null) {
				av = defaultValue;
			}

			if (av != null) {
				write(writer, av);
			}
		}
		catch (IOException e) {
			throw new MvcException("Failed to print out value '" + av + "'", e);
		}

		return result;
	}

	public String formatValue() {
		String ev = null;
		
		if (name == null) {
			name = "top";
		}

		if (Strings.isEmpty(format) && Strings.isEmpty(pattern)) {
			ev = escape(getStringValue());
		}
		else {
			Object av = value != null ? value : findValue(name);

			if (av == null) {
			}
			else if ("password".equalsIgnoreCase(format)) {
				ev = context.getText().getText(PASSWORD_FORMAT, DEFAULT_PASSWORD_FORMAT);
			}
			else if ("link".equalsIgnoreCase(format)) {
				String s = StringEscapes.escapeHtml(av.toString());
				if (Strings.isNotEmpty(s)) {
					ev = "<a href=\"" + s + "\">" + s + "</a>";
				}
			}
			else if ("extlink".equalsIgnoreCase(format)) {
				String s = StringEscapes.escapeHtml(av.toString());
				if (Strings.isNotEmpty(s)) {
					ev = "<a target=\"_blank\" href=\"" + s + "\">" + s + "</a>";
				}
			}
			else {
				if (av instanceof Boolean) {
					StringBuilderWriter sw = new StringBuilderWriter();
					if (cbool == null) {
						cbool = newComponent(CBoolean.class);
					}
					cbool.setValue((Boolean)av);
					cbool.setPattern(pattern);
					cbool.setFormat(format);
					cbool.start(sw);
					cbool.end(sw, "");
					ev = sw.toString();
				}
				else if (av instanceof Date || av instanceof Calendar) {
					StringBuilderWriter sw = new StringBuilderWriter();
					if (cdate == null) {
						cdate = newComponent(CDate.class);
					}
					cdate.setValue(av instanceof Calendar ? ((Calendar)av).getTime() : (Date)av);
					cdate.setPattern(pattern);
					cdate.setFormat(format);
					cdate.start(sw);
					cdate.end(sw, "");
					ev = sw.toString();
				}
				else if (av instanceof Number) {
					StringBuilderWriter sw = new StringBuilderWriter();
					if (cnumber == null) {
						cnumber = newComponent(CNumber.class);
					}
					cnumber.setValue((Number)av);
					cnumber.setPattern(pattern);
					cnumber.setFormat(format);
					cnumber.start(sw);
					cnumber.end(sw, "");
					ev = sw.toString();
				}
				else if (av instanceof String) {
					ev = escape((String)av);
				}
				else {
					ev = escape(castString(av));
				}
			}
		}
		return ev;
	}
	
	private String getStringValue() {
		if (value != null) {
			return castString(value);
		}

		return Strings.defaultString(findString(name));
	}

	private void write(Writer writer, String value) throws IOException {
		if (getVar() == null) {
			writer.write(value);
		}
		else {
			putInVars(value);
		}
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
