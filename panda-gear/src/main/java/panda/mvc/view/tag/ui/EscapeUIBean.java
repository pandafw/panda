package panda.mvc.view.tag.ui;


public abstract class EscapeUIBean extends UIBean {

	protected boolean escape = true;

	/**
	 * @return the escape
	 */
	public boolean isEscape() {
		return escape;
	}

	/** Whether to escape HTML */
	public void setEscape(boolean escape) {
		this.escape = escape;
	}
}
