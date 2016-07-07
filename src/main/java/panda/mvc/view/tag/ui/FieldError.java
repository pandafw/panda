package panda.mvc.view.tag.ui;

import java.util.ArrayList;
import java.util.List;

import panda.ioc.annotation.IocBean;

/**
 * <!-- START SNIPPET: javadoc --> Render field errors if they exists. Specific layout depends on
 * the particular theme. <!-- END SNIPPET: javadoc -->
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: example --&gt;
 *    &lt;!-- example 1 --&gt;
 *    &lt;s:fielderror /&gt;
 *    &lt;!-- example 2 --&gt;
 *    &lt;s:fielderror&gt;
 *         &lt;s:param&gt;field1&lt;/s:param&gt;
 *         &lt;s:param&gt;field2&lt;/s:param&gt;
 *    &lt;/s:fielderror&gt;
 *    &lt;s:form .... &gt;
 *       ....
 *    &lt;/s:form&gt;
 *    OR
 *    &lt;s:fielderror&gt;
 *          &lt;s:param value=&quot;%{'field1'}&quot; /&gt;
 *          &lt;s:param value=&quot;%{'field2'}&quot; /&gt;
 *    &lt;/s:fielderror&gt;
 *    &lt;s:form .... &gt;
 *       ....
 *    &lt;/s:form&gt;
 * &lt;!-- END SNIPPET: example --&gt;
 * </pre>
 * <p/>
 * <b>Description</b>
 * <p/>
 * 
 * <pre>
 * &lt;!-- START SNIPPET: description --&gt;
 * Example 1: display all field errors&lt;p/&gt;
 * Example 2: display field errors only for 'field1' and 'field2'&lt;p/&gt;
 * &lt;!-- END SNIPPET: description --&gt;
 * </pre>
 */
@IocBean(singleton=false)
public class FieldError extends EscapeUIBean {

	private boolean showLabel;
	private boolean hideEmptyLabel;
	private String labelSeparator = ": ";
	private List<String> fieldNames = new ArrayList<String>();


	/**
	 * @return the showLabel
	 */
	public boolean isShowLabel() {
		return showLabel;
	}

	/**
	 * @param showLabel the showLabel to set
	 */
	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	/**
	 * @return the hideEmptyLabel
	 */
	public boolean isHideEmptyLabel() {
		return hideEmptyLabel;
	}

	/**
	 * @param hideEmptyLabel the hideEmptyLabel to set
	 */
	public void setHideEmptyLabel(boolean hideEmptyLabel) {
		this.hideEmptyLabel = hideEmptyLabel;
	}

	/**
	 * @return the labelSeparator
	 */
	public String getLabelSeparator() {
		return labelSeparator;
	}

	/**
	 * @param labelSeparator the labelSeparator to set
	 */
	public void setLabelSeparator(String labelSeparator) {
		this.labelSeparator = labelSeparator;
	}

	/**
	 * @return the fieldNames
	 */
	public List<String> getFieldNames() {
		return fieldNames;
	}

	/**
	 * @param fieldNames the fieldNames to set
	 */
	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public void setFieldName(String fieldName) {
		fieldNames.add(fieldName);
	}
}
