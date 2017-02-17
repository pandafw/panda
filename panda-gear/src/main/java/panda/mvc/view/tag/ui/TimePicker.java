package panda.mvc.view.tag.ui;

import panda.cast.castor.DateTypeCastor;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConstants;
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
 *     &lt;r:timepicker name=&quot;order.date&quot; label=&quot;Order Date&quot; /&gt;
 * Example 2:
 *     &lt;r:timepicker name=&quot;delivery.date&quot; label=&quot;Delivery Date&quot; format=&quot;date&quot;  /&gt;
 * 
 * &lt;!-- END SNIPPET: expl1 --&gt;
 * </pre>
 * <p/>
 * 
 */
@IocBean(singleton=false)
public class TimePicker extends DatePicker {
	protected static final Log log = Logs.getLog(TimePicker.class);

	protected static final String DEFAULT_TIME_FORMAT = DateTypeCastor.TIME;

	protected static final String DEFAULT_TIME_PATTERN = DateTimes.ISO_TIME_FORMAT;

	
	public TimePicker() {
		ricon = "icon-clock";
	}

	/**
	 * get time format from text
	 * 
	 * @return time format
	 */
	@Override
	public String getFormat() {
		if (format == null) {
			format = DEFAULT_TIME_FORMAT;
		}		
		return format;
	}

	/**
	 * get time pattern from text
	 * 
	 * @return time pattern
	 */
	@Override
	public String getPattern() {
		if (pattern == null) {
			pattern = Mvcs.getDatePattern(context, getFormat(), DEFAULT_TIME_PATTERN);
		}
		return pattern;
	}

	/**
	 * @param defaults defaults
	 */
	@IocInject(value = MvcConstants.UI_TIMEPICKER_DEFAULTS, required = false)
	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}
}
