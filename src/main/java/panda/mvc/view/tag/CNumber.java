package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import panda.ioc.annotation.IocBean;
import panda.lang.Numbers;
import panda.mvc.MvcException;
import panda.mvc.Mvcs;


/**
 * <!-- START SNIPPET: javadoc -->
 *
 * Format Number object in different ways.
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
 *      <td>number-format-*</td>
 *      <td>#</td>
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
 *  &lt;r:number name="person.age" format="decimal" /&gt;
 *  <!-- END SNIPPET: example -->
 * </pre>
 *
 * <code>Number</code>
 *
 */
@IocBean(singleton=false)
public class CNumber extends ContextBean {

	private Number value;

	private String format;
	
	private String pattern;

	/**
	 * get number pattern from text
	 * @param format number format
	 * @return number pattern
	 */
	private String getNumberPattern(String format) {
		return Mvcs.getNumberPattern(context, format);
	}
	
	/**
	 * @see panda.mvc.view.tag.Component#end(java.io.Writer, java.lang.String)
	 */
	public boolean end(Writer writer, String body) {
		if (value != null) {
			if (pattern == null) {
				pattern = getNumberPattern(format);
			}

			String msg = null;
			if (pattern == null) {
				msg = Numbers.format(value);
			}
			else {
				NumberFormat df = new DecimalFormat(pattern, new DecimalFormatSymbols(context.getLocale()));
				msg = df.format(value);
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
					throw new MvcException("Failed to write out Number tag", e);
				}
			}
		}
		return super.end(writer, "");
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
	public void setValue(Number value) {
		this.value = value;
	}

}
