package panda.mvc.view.tag;

import java.io.Writer;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.mvc.util.TextProvider;


/**
 * <!-- START SNIPPET: javadoc -->
 *
 * Format Boolean object.
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
 * <!-- END SNIPPET: javadoc -->
 *
 * <p/> <b>Examples</b>
 * <pre>
 *  <!-- START SNIPPET: example -->
 *  &lt;r:boolean name="person.birthday" /&gt;
 *  &lt;r:boolean value=true /&gt;
 *  <!-- END SNIPPET: example -->
 * </pre>
 *
 * <code>Boolean</code>
 *
 */
@IocBean(singleton=false)
public class CBoolean extends ContextBean {

	/**
	 * BOOLEAN_FORMAT_DEFAULT = "boolean-format";
	 */
	public static final String BOOLEAN_FORMAT_DEFAULT = "boolean-format";

	/**
	 * BOOLEAN_FORMAT_PREFIX = "boolean-format-";
	 */
	public static final String BOOLEAN_FORMAT_PREFIX = "boolean-format-";

	private Boolean value;

	private String format;
	
	private String pattern;

	/**
	 * @see panda.mvc.view.tag.TagBean#end(java.io.Writer, java.lang.String)
	 */
	public boolean end(Writer writer, String body) {
		if (value != null) {
			if (pattern == null) {
				TextProvider tp = context.getText();
	
				if (Strings.isNotEmpty(format)) {
					pattern = tp.getText(BOOLEAN_FORMAT_PREFIX + format, (String)null);
					if (pattern == null) {
						pattern = format;
					}
				}
				else {
					pattern = tp.getText(BOOLEAN_FORMAT_DEFAULT, (String)null);
				}
			}
			
			String msg = null;
			if (pattern != null) {
				msg = findString(pattern, value);
			}

			if (msg == null) {
				msg = value.toString();
			}
			
			writeOrSetVar(writer, msg);
		}
		return super.end(writer, "");
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Boolean value) {
		this.value = value;
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
