package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.TimeZone;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.util.TextProvider;


/**
 * <!-- START SNIPPET: javadoc -->
 *
 * Format Date object in different ways.
 *
 * <p/>
 *
 * Configurable attributes are :-
 * <ul>
 *    <li>name</li>
 *    <li>value</li>
 *    <li>format</li>
 * </ul>
 *
 * <p/>
 *
 * <table border="1">
 *   <tr>
 *      <td>date-format-date</td>
 *      <td>yyyy/MM/dd</td>
 *   </tr>
 *   <tr>
 *      <td>date-format-time</td>
 *      <td>HH:mm:ss</td>
 *   </tr>
 *   <tr>
 *      <td>date-format-datetime</td>
 *      <td>yyyy/MM/dd HH:mm:ss</td>
 *   </tr>
 *   <tr>
 *      <td>date-format</td>
 *      <td>yyyy/MM/dd HH:mm:ss.SSS</td>
 *   </tr>
 *   <tr>
 *      <td>else</td>
 *      <td>String.valueOf(date.getTime()) will be used</td>
 *   </tr>
 * </table>
 *
 * <p/>
 *
 * <!-- END SNIPPET: javadoc -->
 *
 * <p/> <b>Examples</b>
 * <pre>
 *  <!-- START SNIPPET: example -->
 *  &lt;r:date name="person.birthday" format="date" /&gt;
 *  &lt;r:date name="person.birthday" format="time" /&gt;
 *  &lt;r:date name="person.birthday" format="datetime" /&gt;
 *  &lt;r:date name="person.birthday" /&gt;
 *  <!-- END SNIPPET: example -->
 * </pre>
 *
 * <code>Date</code>
 *
 */
@IocBean(singleton=false)
public class CDate extends ContextBean {

	private static final Log log = Logs.getLog(CDate.class);

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

	private Date value;

	private String format;
	
	private String pattern;

	/**
	 * @see panda.mvc.view.tag.Component#end(java.io.Writer, java.lang.String)
	 */
	public boolean end(Writer writer, String body) {
		if (value != null) {
			if (pattern == null) {
				pattern = getDatePattern(format);
			}

			String msg = null;
			if (pattern == null) {
				msg = String.valueOf(value.getTime());
			}
			else {
				TimeZone timezone = getDateTimeZone();
				msg = DateTimes.format(value, pattern, timezone, context.getLocale());
			}

			if (msg != null) {
				try {
					if (getVar() == null) {
						writer.write(msg);
					}
					else {
						putInVars(msg);
					}
				}
				catch (IOException e) {
					log.warn("Could not write out Date tag", e);
				}
			}
		}
		return super.end(writer, "");
	}

	/**
	 * get date pattern from text
	 * @param format date format
	 * @return date pattern
	 */
	private String getDatePattern(String format) {
		TextProvider tp = context.getText();
		String pattern = null;

		if (Strings.isNotEmpty(format)) {
			pattern = tp.getText(DATE_FORMAT_PREFIX + format, (String)null);
			if (pattern == null) {
				pattern = format;
			}
		}
		else {
			pattern = tp.getText(DATE_FORMAT_DEFAULT, (String)null);
		}

		return pattern;
	}
	
	/**
	 * get date timezone from text
	 * @return date timezone
	 */
	private TimeZone getDateTimeZone() {
		TextProvider tp = context.getText();
		String tz = null;

		tz = tp.getText(DATE_TIMEZONE, (String)null);
		if (tz != null) {
			return TimeZone.getTimeZone(tz);
		}

		return null;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Date value) {
		this.value = value;
	}

}
