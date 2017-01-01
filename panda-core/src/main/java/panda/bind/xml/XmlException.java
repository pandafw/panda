package panda.bind.xml;

/**
 * 
 *
 */
public class XmlException extends RuntimeException {
	private static final long serialVersionUID = 1;

	/**
	 * Constructs a JsonException.
	 * 
	 * @param message description of the exception
	 */
	public XmlException(final String message) {
		super(message);
	}

	/**
	 * Constructs a JsonException.
	 * 
	 * @param cause cause of the exception
	 */
	public XmlException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a JsonException.
	 * 
	 * @param message description of the exception
	 * @param cause cause of the exception
	 */
	public XmlException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
