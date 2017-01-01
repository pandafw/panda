package panda.net.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * CRLFLineReader implements a readLine() method that requires exactly CRLF to terminate an input
 * line. This is required for IMAP, which allows bare CR and LF.
 */
public final class CRLFLineReader extends BufferedReader {
	private static final char LF = '\n';
	private static final char CR = '\r';

	/**
	 * Creates a CRLFLineReader that wraps an existing Reader input source.
	 * 
	 * @param reader The Reader input source.
	 */
	public CRLFLineReader(Reader reader) {
		super(reader);
	}

	/**
	 * Read a line of text. A line is considered to be terminated by carriage return followed
	 * immediately by a linefeed. This contrasts with BufferedReader which also allows other
	 * combinations.
	 */
	@Override
	public String readLine() throws IOException {
		StringBuilder sb = new StringBuilder();
		int intch;
		boolean prevWasCR = false;
		synchronized (lock) { // make thread-safe (hopefully!)
			while ((intch = read()) != -1) {
				if (prevWasCR && intch == LF) {
					return sb.substring(0, sb.length() - 1);
				}
				if (intch == CR) {
					prevWasCR = true;
				}
				else {
					prevWasCR = false;
				}
				sb.append((char)intch);
			}
		}
		String string = sb.toString();
		if (string.length() == 0) { // immediate EOF
			return null;
		}
		return string;
	}
}
