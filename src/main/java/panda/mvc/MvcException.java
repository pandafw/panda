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
	 * @param message
	 * @param cause
	 */
	public MvcException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public MvcException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public MvcException(Throwable cause) {
		super(cause);
	}

}
