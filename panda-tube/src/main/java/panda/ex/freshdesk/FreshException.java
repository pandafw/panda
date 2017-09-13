package panda.ex.freshdesk;

import java.io.IOException;

public class FreshException extends IOException {
	
	private static final long serialVersionUID = 5512289367718068512L;

	private ErrorResult error;

	/**
	 * 
	 */
	public FreshException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FreshException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public FreshException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public FreshException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param error
	 */
	public FreshException(ErrorResult error) {
		super(error.getDescription());
		this.error = error;
	}

	/**
	 * @param error
	 */
	public FreshException(String message, ErrorResult error) {
		super(message);
		this.error = error;
	}

	/**
	 * @return the error
	 */
	public ErrorResult getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ErrorResult error) {
		this.error = error;
	}

}
