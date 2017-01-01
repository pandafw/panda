package panda.mvc.adaptor.multipart;

public class InvalidContentTypeException extends RuntimeException {

	/**
	 * The exceptions UID, for serializing an instance.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a <code>InvalidContentTypeException</code> with no detail message.
	 */
	public InvalidContentTypeException() {
		super();
	}

	/**
	 * Constructs an <code>InvalidContentTypeException</code> with the specified detail message.
	 * 
	 * @param message The detail message.
	 */
	public InvalidContentTypeException(String message) {
		super(message);
	}

	/**
	 * Constructs an <code>InvalidContentTypeException</code> with the specified detail message
	 * and cause.
	 * 
	 * @param msg The detail message.
	 * @param cause the original cause
	 * @since 1.3.1
	 */
	public InvalidContentTypeException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
