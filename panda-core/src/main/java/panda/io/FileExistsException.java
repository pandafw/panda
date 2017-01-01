package panda.io;

import java.io.File;
import java.io.IOException;

/**
 * Indicates that a file already exists.
 */
public class FileExistsException extends IOException {

	/**
	 * Defines the serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor.
	 */
	public FileExistsException() {
		super();
	}

	/**
	 * Construct an instance with the specified message.
	 * 
	 * @param message The error message
	 */
	public FileExistsException(String message) {
		super(message);
	}

	/**
	 * Construct an instance with the specified file.
	 * 
	 * @param file The file that exists
	 */
	public FileExistsException(File file) {
		super("File " + file + " exists");
	}

}
