package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Mvcs;

/**
 * <!-- START SNIPPET: javadoc -->
 * <p>
 * Renders a jquery date picker.
 * </p>
 * 
 * <!-- END SNIPPET: javadoc -->
 * 
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: expl1 --&gt;
 * 
 * Example 1:
 *     &lt;r:datepicker name=&quot;order.date&quot; label=&quot;Order Date&quot; /&gt;
 * Example 2:
 *     &lt;r:datepicker name=&quot;delivery.date&quot; label=&quot;Delivery Date&quot; format=&quot;date&quot;  /&gt;
 * 
 * &lt;!-- END SNIPPET: expl1 --&gt;
 * </pre>
 * <p/>
 * 
 */
@IocBean(singleton=false)
public class DatePicker extends TriggerField {
	protected static final Log log = Logs.getLog(DatePicker.class);

	protected static final String DEFAULT_DATE_FORMAT = "date";

	protected static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	protected String pattern;
	protected String options;

	//NOT USED
	protected String inline;
	protected String defaults;
	
	public DatePicker() {
		ricon = "icon-calendar";
		addParameter("dataTrigger", "true");
	}

	/**
	 * @return the inline
	 */
	public String getInline() {
		return inline;
	}

	/**
	 * @return the options
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * @return the defaults
	 */
	public String getDefaults() {
		return defaults;
	}

	/**
	 * get date format from text
	 * 
	 * @return date format
	 */
	@Override
	public String getFormat() {
		if (format == null) {
			format = DEFAULT_DATE_FORMAT;
		}
		return format;
	}

	/**
	 * get date pattern from text
	 * 
	 * @return date pattern
	 */
	public String getPattern() {
		if (pattern == null) {
			pattern = Mvcs.getDatePattern(context, getFormat(), DEFAULT_DATE_PATTERN);
		}
		return pattern;
	}

	/**
	 * @param defaults defaults
	 */
	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}

	/**
	 * @param inline the inline to set
	 */
	public void setInline(String inline) {
		this.inline = inline;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(String options) {
		this.options = options;
	}
}
