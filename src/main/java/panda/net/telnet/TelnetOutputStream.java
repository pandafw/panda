package panda.net.telnet;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Wraps an output stream.
 * <p>
 * In binary mode, the only conversion is to double IAC.
 * <p>
 * In ASCII mode, if convertCRtoCRLF is true (currently always true), any CR is converted to CRLF.
 * IACs are doubled. Also a bare LF is converted to CRLF and a bare CR is converted to CR\0
 * <p>
 ***/

final class TelnetOutputStream extends OutputStream {
	private final TelnetClient __client;
	// TODO there does not appear to be any way to change this value - should it be a ctor
	// parameter?
	private final boolean __convertCRtoCRLF = true;
	private boolean __lastWasCR = false;

	TelnetOutputStream(TelnetClient client) {
		__client = client;
	}

	/***
	 * Writes a byte to the stream.
	 * <p>
	 * 
	 * @param ch The byte to write.
	 * @exception IOException If an error occurs while writing to the underlying stream.
	 ***/
	@Override
	public void write(int ch) throws IOException {

		synchronized (__client) {
			ch &= 0xff;

			if (__client._requestedWont(TelnetOption.BINARY)) // i.e. ASCII
			{
				if (__lastWasCR) {
					if (__convertCRtoCRLF) {
						__client._sendByte('\n');
						if (ch == '\n') // i.e. was CRLF anyway
						{
							__lastWasCR = false;
							return;
						}
					} // __convertCRtoCRLF
					else if (ch != '\n') {
						__client._sendByte('\0'); // RFC854 requires CR NUL for bare CR
					}
				}

				switch (ch) {
				case '\r':
					__client._sendByte('\r');
					__lastWasCR = true;
					break;
				case '\n':
					if (!__lastWasCR) { // convert LF to CRLF
						__client._sendByte('\r');
					}
					__client._sendByte(ch);
					__lastWasCR = false;
					break;
				case TelnetCommand.IAC:
					__client._sendByte(TelnetCommand.IAC);
					__client._sendByte(TelnetCommand.IAC);
					__lastWasCR = false;
					break;
				default:
					__client._sendByte(ch);
					__lastWasCR = false;
					break;
				}
			} // end ASCII
			else if (ch == TelnetCommand.IAC) {
				__client._sendByte(ch);
				__client._sendByte(TelnetCommand.IAC);
			}
			else {
				__client._sendByte(ch);
			}
		}
	}

	/***
	 * Writes a byte array to the stream.
	 * <p>
	 * 
	 * @param buffer The byte array to write.
	 * @exception IOException If an error occurs while writing to the underlying stream.
	 ***/
	@Override
	public void write(byte buffer[]) throws IOException {
		write(buffer, 0, buffer.length);
	}

	/***
	 * Writes a number of bytes from a byte array to the stream starting from a given offset.
	 * <p>
	 * 
	 * @param buffer The byte array to write.
	 * @param offset The offset into the array at which to start copying data.
	 * @param length The number of bytes to write.
	 * @exception IOException If an error occurs while writing to the underlying stream.
	 ***/
	@Override
	public void write(byte buffer[], int offset, int length) throws IOException {
		synchronized (__client) {
			while (length-- > 0) {
				write(buffer[offset++]);
			}
		}
	}

	/*** Flushes the stream. ***/
	@Override
	public void flush() throws IOException {
		__client._flushOutputStream();
	}

	/*** Closes the stream. ***/
	@Override
	public void close() throws IOException {
		__client._closeOutputStream();
	}
}
