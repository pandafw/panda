package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;

/**
 * <!-- START SNIPPET: javadoc -->
 * <p>
 * Renders a jquery datetime picker.
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
 *     &lt;r:datetimepicker name=&quot;order.date&quot; label=&quot;Order Date&quot; /&gt;
 * Example 2:
 *     &lt;r:datetimepicker name=&quot;delivery.date&quot; label=&quot;Delivery Date&quot; format=&quot;date&quot;  /&gt;
 * 
 * &lt;!-- END SNIPPET: expl1 --&gt;
 * </pre>
 * <p/>
 * 
 */
@IocBean(singleton=false)
public class DateTimePicker extends DatePicker {
	protected static final String DEFAULT_DATETIME_FORMAT = "datetime";

	protected static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * get date time format from text
	 * 
	 * @return date time format
	 */
	@Override
	public String getFormat() {
		if (format == null) {
			format = DEFAULT_DATETIME_FORMAT;
		}
		return format;
	}

	/**
	 * get date time pattern from text
	 * 
	 * @return date time pattern
	 */
	@Override
	public String getPattern() {
		if (pattern == null) {
			pattern = DEFAULT_DATETIME_PATTERN;
		}
		return pattern;
	}

	/**
	 * @param defaults defaults
	 */
	@IocInject(value = MvcConstants.UI_DATETIMEPICKER_DEFAULTS, required = false)
	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}
}
