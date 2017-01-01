package panda.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


/**
 */
public class Checksums {
	private static final int STREAM_BUFFER_LENGTH = 1024;

	//----------------------------------------------------------------------
	// Checksum
	//
	/**
	 * Reads through an InputStream and updates the checksum for the data
	 * 
	 * @param checksum The Checksum to use (e.g. CRC32)
	 * @param data Data to digest
	 * @return checksum
	 * @throws IOException On error reading from the stream
	 */
	public static Checksum updateChecksum(final Checksum checksum, final InputStream data) throws IOException {
		final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
		int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

		while (read > -1) {
			checksum.update(buffer, 0, read);
			read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
		}

		return checksum;
	}

	public static long crc32(final InputStream data) throws IOException {
		return updateChecksum(new CRC32(), data).getValue();
	}

	public static long adler32(final InputStream data) throws IOException {
		return updateChecksum(new Adler32(), data).getValue();
	}
}
