package panda.ex.freshdesk;

import java.util.List;

public class ErrorResult {
	private String description;
	
	private List<Error> errors;

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

	/**
	 * @return the errors
	 */
	public List<Error> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
	
}
