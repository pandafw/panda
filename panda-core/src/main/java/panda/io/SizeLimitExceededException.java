package panda.io;

import java.io.IOException;

import panda.lang.Numbers;

public class SizeLimitExceededException extends IOException {

	private static final long serialVersionUID = 1L;

	/**
	 * The actual size of the request.
	 */
	private final long actual;

	/**
	 * The maximum limited size of the request.
	 */
	private final long limited;

	/**
	 * Constructs a <code>SizeExceededException</code> with the specified detail message, and
	 * actual and limited sizes.
	 * 
	 * @param message The detail message.
	 * @param actual The actual request size.
	 * @param limited The maximum limited request size.
	 */
	public SizeLimitExceededException(String message, long actual, long limited) {
		super(message);
		this.actual = actual;
		this.limited = limited;
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
	 * Retrieves the limited size of the request.
	 * 
	 * @return The limited size of the request.
	 */
	public long getLimitedSize() {
		return limited;
	}

	/**
	 * Retrieves the actual size of the request.
	 * 
	 * @return The actual size of the request.
	 */
	public String getDisplayActualSize() {
		return Numbers.humanSize(actual);
	}

	/**
	 * Retrieves the limited size of the request.
	 * 
	 * @return The limited size of the request.
	 */
	public String getDisplayLimitedSize() {
		return Numbers.humanSize(limited);
	}
}
