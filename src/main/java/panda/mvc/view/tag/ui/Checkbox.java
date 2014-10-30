package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;

/**
 * <!-- START SNIPPET: javadoc --> Renders an HTML input element of type
 * checkbox, populated by the specified property from the ValueStack. <!-- END
 * SNIPPET: javadoc -->
 * 
 * <p/>
 * <b>Examples</b>
 * 
 * <pre>
 * <!-- START SNIPPET: example -->
 * JSP:
 * &lt;s:checkbox label="checkbox test" name="checkboxField1" value="aBoolean" fieldValue="true"/&gt;
 * 
 * Velocity:
 * #tag( Checkbox "label=checkbox test" "name=checkboxField1" "value=aBoolean" )
 * 
 * Resulting HTML (simple template, aBoolean == true):
 * &lt;input type="checkbox" name="checkboxField1" value="true" checked="checked" /&gt;
 * 
 * <!-- END SNIPPET: example -->
 * </pre>
 * 
 */
@IocBean(singleton=false)
public class Checkbox extends InputUIBean {
	protected String fieldLabel;
	protected String fieldValue;

	/**
	 * @return the fieldLabel
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}

	/**
	 * @param fieldLabel the fieldLabel to set
	 */
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	/**
	 * @return the fieldValue
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	/**
	 * The actual HTML value attribute of the checkbox
	 */
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
}
