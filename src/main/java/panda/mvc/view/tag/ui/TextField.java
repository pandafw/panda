package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


/**
 * <!-- START SNIPPET: javadoc -->
 * Render an HTML input field of type text</p>
 * <!-- END SNIPPET: javadoc -->
 *
 * <p/> <b>Examples</b>
 * <p/>
 * <!-- START SNIPPET: exdescription -->
 * In this example, a text control for the "user" property is rendered. The label is also retrieved from a ResourceBundle via the key attribute.
 * <!-- END SNIPPET: exdescription -->
 * <pre>
 * <!-- START SNIPPET: example -->
 * &lt;r:textfield key="user" /&gt;
 * <!-- END SNIPPET: example -->
 * </pre>
 *
 * <pre>
 * <!-- START SNIPPET: example2 -->
 * &lt;r:textfield name="user" label="User Name" /&gt;
 * <!-- END SNIPPET: example -->
 * </pre>

 */
@IocBean(singleton=false)
public class TextField extends InputUIBean {
	protected Integer maxlength;
	protected Integer size;
	protected String format;
	protected String mask;
	protected String placeholder;

	/**
	 * @return the maxlength
	 */
	public Integer getMaxlength() {
		return maxlength;
	}

	/**
	 * @return the size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @return the mask
	 */
	public String getMask() {
		return mask;
	}

	/**
	 * @return the placeholder
	 */
	public String getPlaceholder() {
		return placeholder;
	}

	/**
	 * @param maxlength the maxlength to set
	 */
	public void setMaxlength(Integer maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * @param maxlength the maxlength to set
	 */
	public void setMaxLength(Integer maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @param mask the mask to set
	 */
	public void setMask(String mask) {
		this.mask = mask;
	}

	/**
	 * @param placeholder the placeholder to set
	 */
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
}