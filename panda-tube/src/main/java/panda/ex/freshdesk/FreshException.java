package panda.ex.freshdesk;

import panda.net.http.HttpException;

public class FreshException extends HttpException {
	
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
	 * @param message error message
	 * @param error error result
	 */
	public FreshException(String message, ErrorResult error) {
		super(message);
		this.error = error;
	}

	public FreshException(int status, String message, ErrorResult error) {
		super(status, message);
		this.error = error;
	}

	public FreshException(int status, String message, String content) {
		super(status, message, content);
	}

	public FreshException(int status, String message) {
		super(status, message);
	}

	public FreshException(int status) {
		super(status);
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
