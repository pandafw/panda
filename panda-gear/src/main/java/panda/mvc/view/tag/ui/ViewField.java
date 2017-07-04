package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;
import panda.mvc.view.util.Escapes;


@IocBean(singleton=false)
public class ViewField extends ListUIBean {
	protected String escape = Escapes.ESCAPE_PHTML;
	protected String fieldValue;
	protected String format;
	protected String icon;

	protected void evaluateDescriptionParam() {
		// do not get text from resource (x-dip or p.x-dip)
	}
	
	protected void evaluateTooltipParam() {
		// do not get text from resource (x-tip or p.x-tip)
	}

	/**
	 * @return the escape
	 */
	public String getEscape() {
		return escape;
	}

	/**
	 * @param escape the escape to set
	 */
	public void setEscape(String escape) {
		this.escape = escape;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
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
