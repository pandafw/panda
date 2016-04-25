package panda.net.io;

import java.io.IOException;

/**
 * The CopyStreamException class is thrown by the org.apache.commons.io.Util copyStream() methods.
 * It stores the number of bytes confirmed to have been transferred before an I/O error as well as
 * the IOException responsible for the failure of a copy operation.
 * 
 * @see Util
 */
public class CopyStreamException extends IOException {
	private static final long serialVersionUID = -2602899129433221532L;

	private final long totalBytesTransferred;

	/**
	 * Creates a new CopyStreamException instance.
	 * 
	 * @param message A message describing the error.
	 * @param bytesTransferred The total number of bytes transferred before an exception was thrown
	 *            in a copy operation.
	 * @param exception The IOException thrown during a copy operation.
	 */
	public CopyStreamException(String message, long bytesTransferred, IOException exception) {
		super(message);
		initCause(exception); // merge this into super() call once we need 1.6+
		totalBytesTransferred = bytesTransferred;
	}

	/**
	 * Returns the total number of bytes confirmed to have been transferred by a failed copy
	 * operation.
	 * 
	 * @return The total number of bytes confirmed to have been transferred by a failed copy
	 *         operation.
	 */
	public long getTotalBytesTransferred() {
		return totalBytesTransferred;
	}

	/**
	 * Returns the IOException responsible for the failure of a copy operation.
	 * 
	 * @return The IOException responsible for the failure of a copy operation.
	 */
	public IOException getIOException() {
		return (IOException)getCause(); // cast is OK because it was initialised with an IOException
	}
}
