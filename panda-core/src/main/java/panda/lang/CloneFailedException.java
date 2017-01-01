package panda.lang;

/**
 */
public class CloneFailedException extends RuntimeException {
	private static final long serialVersionUID = 0;

	/**
	 * Constructs a CloneFailedException.
	 * 
	 * @param message description of the exception
	 */
	public CloneFailedException(final String message) {
		super(message);
	}

	/**
	 * Constructs a CloneFailedException.
	 * 
	 * @param cause cause of the exception
	 */
	public CloneFailedException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a CloneFailedException.
	 * 
	 * @param message description of the exception
	 * @param cause cause of the exception
	 */
	public CloneFailedException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
