package panda.mvc.adaptor.multipart;

import java.io.IOException;


/**
 * Exception for errors encountered while processing the request.
 */
public class FileUploadException extends IOException {

	/**
	 * Serial version UID, being used, if the exception is serialized.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new <code>FileUploadException</code> without message.
	 */
	public FileUploadException() {
		this(null, null);
	}

	/**
	 * Constructs a new <code>FileUploadException</code> with specified detail message.
	 * 
	 * @param msg the error message.
	 */
	public FileUploadException(final String msg) {
		this(msg, null);
	}

	/**
	 * Creates a new <code>FileUploadException</code> with the given detail message and cause.
	 * 
	 * @param msg The exceptions detail message.
	 * @param cause The exceptions cause.
	 */
	public FileUploadException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
