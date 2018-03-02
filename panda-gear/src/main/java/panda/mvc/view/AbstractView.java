package panda.mvc.view;

import panda.mvc.View;

public abstract class AbstractView implements View {

	protected String argument;
	
	/**
	 * @return the argument
	 */
	public String getArgument() {
		return argument;
	}

	/**
	 * @param argument the argument to set
	 */
	public void setArgument(String argument) {
		this.argument = argument;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + argument;
	}
}
