package panda.io;

import java.io.IOException;

public class SizeLimitExceededException extends IOException {

	private static final long serialVersionUID = 1L;

	/**
	 * The actual size of the request.
	 */
	private final long actual;

	/**
	 * The maximum permitted size of the request.
	 */
	private final long permitted;

	/**
	 * Constructs a <code>SizeExceededException</code> with the specified detail message, and
	 * actual and permitted sizes.
	 * 
	 * @param message The detail message.
	 * @param actual The actual request size.
	 * @param permitted The maximum permitted request size.
	 */
	public SizeLimitExceededException(String message, long actual, long permitted) {
		super(message);
		this.actual = actual;
		this.permitted = permitted;
	}

	/**
	 * Retrieves the actual size of the request.
	 * 
	 * @return The actual size of the request.
	 */
	public long getActualSize() {
		return actual;
	}

	/**
	 * Retrieves the permitted size of the request.
	 * 
	 * @return The permitted size of the request.
	 */
	public long getPermittedSize() {
		return permitted;
	}
}
