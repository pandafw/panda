package panda.bind.json;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class JsonException extends RuntimeException {
	private static final long serialVersionUID = 0;

	/**
	 * Constructs a JsonException.
	 * 
	 * @param message description of the exception
	 * @since upcoming
	 */
	public JsonException(final String message) {
		super(message);
	}

	/**
	 * Constructs a JsonException.
	 * 
	 * @param cause cause of the exception
	 * @since upcoming
	 */
	public JsonException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a JsonException.
	 * 
	 * @param message description of the exception
	 * @param cause cause of the exception
	 * @since upcoming
	 */
	public JsonException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
