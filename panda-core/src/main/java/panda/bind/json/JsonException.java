package panda.bind.json;

/**
 * 
 *
 */
public class JsonException extends RuntimeException {
	private static final long serialVersionUID = 0;

	/**
	 * Constructs a JsonException.
	 * 
	 * @param message description of the exception
	 */
	public JsonException(final String message) {
		super(message);
	}

	/**
	 * Constructs a JsonException.
	 * 
	 * @param cause cause of the exception
	 */
	public JsonException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a JsonException.
	 * 
	 * @param message description of the exception
	 * @param cause cause of the exception
	 */
	public JsonException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
