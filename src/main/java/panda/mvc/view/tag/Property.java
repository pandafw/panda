package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

import panda.io.stream.StringBuilderWriter;
import panda.ioc.annotation.IocBean;
import panda.lang.StringEscapes;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

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
	private static final Log log = Logs.getLog(Property.class);

	/**
	 * PASSWORD_FORMAT = "password-format";
	 */
	public static final String PASSWORD_FORMAT = "password-format";

	/**
	 * ESCAPE_HTML = "html";
	 */
	public static final String ESCAPE_HTML = "html";
	
	/**
	 * ESCAPE_PHTML = "phtml";
	 */
	public static final String ESCAPE_PHTML = "phtml";
	
	/**
	 * ESCAPE_JAVASCRIPT = "js";
	 */
	public static final String ESCAPE_JAVASCRIPT = "js";
	
	/**
	 * ESCAPE_CSV = "csv";
	 */
	public static final String ESCAPE_CSV = "csv";
	
	/**
	 * ESCAPE_XML = "xml";
	 */
	public static final String ESCAPE_XML = "xml";
	
	/**
	 * DEFAULT_PASSWORD_FORMAT = "******";
	 */
	public static final String DEFAULT_PASSWORD_FORMAT = "******";

	private String defaultValue;
	private String name;
	private String format;
	private Object value;
	private String escape = ESCAPE_HTML;

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
			log.warn("Could not print out value '" + av + "'", e);
		}

		return result;
	}

	public String formatValue() {
		String actualValue = null;
		
		if (name == null) {
			name = "top";
		}

		if (format == null) {
			actualValue = prepare(getStringValue());
		}
		else {
			Object av = value != null ? value : eval(name);

			if (av == null) {
			}
			else if ("password".equalsIgnoreCase(format)) {
				actualValue = context.text(PASSWORD_FORMAT, DEFAULT_PASSWORD_FORMAT);
			}
			else if ("link".equalsIgnoreCase(format)) {
				String s = StringEscapes.escapeHtml(av.toString());
				if (Strings.isNotEmpty(s)) {
					actualValue = "<a href=\"" + s + "\">" + s + "</a>";
				}
			}
			else if ("extlink".equalsIgnoreCase(format)) {
				String s = StringEscapes.escapeHtml(av.toString());
				if (Strings.isNotEmpty(s)) {
					actualValue = "<a target=\"_blank\" href=\"" + s + "\">" + s + "</a>";
				}
			}
			else {
				if (av instanceof Boolean) {
					StringBuilderWriter sw = new StringBuilderWriter();
					CBoolean b = newComponent(CBoolean.class);
					b.setValue((Boolean)av);
					b.setFormat(format);
					b.start(sw);
					b.end(sw, "");
					actualValue = sw.toString();
				}
				else if (av instanceof Date || av instanceof Calendar) {
					StringBuilderWriter sw = new StringBuilderWriter();
					CDate d = newComponent(CDate.class);
					d.setValue(av instanceof Calendar ? ((Calendar)av).getTime() : (Date)av);
					d.setFormat(format);
					d.start(sw);
					d.end(sw, "");
					actualValue = sw.toString();
				}
				else if (av instanceof Number) {
					StringBuilderWriter sw = new StringBuilderWriter();
					CNumber n = newComponent(CNumber.class);
					n.setValue((Number)av);
					n.setFormat(format);
					n.start(sw);
					n.end(sw, "");
					actualValue = sw.toString();
				}
				else if (av instanceof String) {
					actualValue = prepare((String)av);
				}
				else {
					actualValue = prepare(getStringValue());
				}
			}
		}
		return actualValue;
	}
	
	private String getStringValue() {
		String av;
		// exception: don't call findString(), since we don't want the
		// expression parsed in this one case. it really
		// doesn't make sense, in fact.
		if (value != null) {
			av = value.toString();
		}
		else {
			av = evalString(name);
		}
		return av;
	}

	private void write(Writer writer, String value) throws IOException {
		if (getVar() == null) {
			writer.write(value);
		}
		else {
			putInVars(value);
		}
	}

	private String prepare(String value) {
		String result = value;
		if (value != null) {
			if (ESCAPE_HTML.equals(escape)) {
				result = StringEscapes.escapeHtml(result);
			}
			else if (ESCAPE_PHTML.equals(escape)) {
				result = StringEscapes.escapePhtml(result);
			}
			else if (ESCAPE_JAVASCRIPT.equals(escape)) {
				result = StringEscapes.escapeJavaScript(result);
			}
			else if (ESCAPE_CSV.equals(escape)) {
				result = StringEscapes.escapeCsv(result);
			}
			else if (ESCAPE_XML.equals(escape)) {
				result = StringEscapes.escapeXml(result);
			}
		}
		return result;
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

}
