package panda.mvc;

public class MvcException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MvcException() {
		super();
	}

	/**
	 * @param message the message
	 * @param cause the cause error
	 */
	public MvcException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message the message
	 */
	public MvcException(String message) {
		super(message);
	}

	/**
	 * @param cause the cause error
	 */
	public MvcException(Throwable cause) {
		super(cause);
	}

}
