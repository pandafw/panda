package panda.mvc.view;

import panda.mvc.View;

public abstract class AbstractView implements View {

	protected String description;
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + description;
	}
}
