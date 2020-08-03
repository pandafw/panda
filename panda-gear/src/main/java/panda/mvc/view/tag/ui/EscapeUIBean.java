package panda.mvc.view.tag.ui;

import panda.mvc.view.util.Escapes;

public abstract class EscapeUIBean extends UIBean {

	protected String escape = Escapes.ESCAPE_HTML;

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
}
