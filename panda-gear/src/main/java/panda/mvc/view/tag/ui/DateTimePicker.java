package panda.mvc.view.tag.ui;

import panda.cast.castor.DateTypeCastor;
import panda.ioc.annotation.IocBean;
import panda.lang.time.DateTimes;

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
	protected static final String DEFAULT_DATETIME_FORMAT = DateTypeCastor.DATETIME;

	protected static final String DEFAULT_DATETIME_PATTERN = DateTimes.ISO_DATETIME_NO_T_FORMAT;

	/**
	 * get default date time format
	 * 
	 * @return date time format
	 */
	@Override
	protected String getDefaultFormat() {
		return DEFAULT_DATETIME_FORMAT;
	}

	/**
	 * get default date time pattern
	 * 
	 * @return date time pattern
	 */
	@Override
	protected String getDefaultPattern() {
		return DEFAULT_DATETIME_PATTERN;
	}
}
