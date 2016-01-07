package panda.mvc.view.tag.ui;

import panda.ioc.annotation.IocBean;

@IocBean(singleton=false)
public class Icon extends UIBean {
	protected String icon;

	public boolean usesBody() {
		return true;
	}
	
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
