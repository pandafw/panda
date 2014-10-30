package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc --> Renders an HTML input element of type hidden, populated by the
 * specified property from the ValueStack. <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 * &lt;-- example one --&gt;
 * &lt;s:hidden name=&quot;foo&quot; /&gt;
 * &lt;-- example two --&gt;
 * &lt;s:hidden name=&quot;foo&quot; value=&quot;bar&quot; /&gt;
 * 
 * Example One Resulting HTML (if foo evaluates to bar):
 * &lt;input type=&quot;hidden&quot; name=&quot;foo&quot; value=&quot;bar&quot; /&gt;
 * Example Two Resulting HTML (if getBar method of the action returns 'bar')
 * &lt;input type=&quot;hidden&quot; name=&quot;foo&quot; value=&quot;bar&quot; /&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 */
@IocBean(singleton=false)
public class Hidden extends InputUIBean {
	protected String format;

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}
}
