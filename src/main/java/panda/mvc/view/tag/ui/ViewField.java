package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;


@IocBean(singleton=false)
public class ViewField extends ListUIBean {
	protected String format;
	protected String icon;

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
}
