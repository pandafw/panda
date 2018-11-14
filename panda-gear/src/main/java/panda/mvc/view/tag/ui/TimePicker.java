package panda.mvc.view.tag.ui;

import panda.cast.castor.DateTypeCastor;
import panda.ioc.annotation.IocBean;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;

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
	 * get default time format
	 * 
	 * @return time format
	 */
	@Override
	protected String getDefaultFormat() {
		return DEFAULT_TIME_FORMAT;
	}

	/**
	 * get default time pattern
	 * 
	 * @return time pattern
	 */
	@Override
	protected String getDefaultPattern() {
		return DEFAULT_TIME_PATTERN;
	}
}
