package panda.mvc.view.tag.ui;

import panda.cast.castor.DateTypeCastor;
import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
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

	protected static final String DEFAULT_DATE_FORMAT = DateTypeCastor.DATE;

	protected static final String DEFAULT_DATE_PATTERN = DateTimes.ISO_DATE_FORMAT;

	protected String pattern;

	protected Boolean pickSeconds;

	//TODO
	//NOT USED
	protected String inline;

	public DatePicker() {
		ricon = "icon-calendar";
		
		// default add ptrigger=true to enable clear icon
		addParameter("dataPtrigger", "true");
	}

	/**
	 * evaluate parameters
	 */
	@Override
	public void evaluateParams() {
		super.evaluateParams();

		if (Strings.isEmpty(format)) {
			format = getDefaultFormat();
		}

		if (Strings.isEmpty(pattern)) {
			pattern = Mvcs.getDatePattern(context, format, getDefaultPattern());
		}
	}

	/**
	 * get default date format
	 * 
	 * @return date format
	 */
	protected String getDefaultFormat() {
		return DEFAULT_DATE_FORMAT;
	}

	/**
	 * get default date pattern
	 * 
	 * @return date pattern
	 */
	protected String getDefaultPattern() {
		return DEFAULT_DATE_PATTERN;
	}

	/**
	 * get date pattern from text
	 * 
	 * @return date pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the pickSeconds
	 */
	public Boolean getPickSeconds() {
		return pickSeconds;
	}

	/**
	 * @param pickSeconds the pickSeconds to set
	 */
	public void setPickSeconds(Boolean pickSeconds) {
		this.pickSeconds = pickSeconds;
	}
	
	/**
	 * @return the inline
	 */
	public String getInline() {
		return inline;
	}

	/**
	 * @param inline the inline to set
	 */
	public void setInline(String inline) {
		this.inline = inline;
	}
}
