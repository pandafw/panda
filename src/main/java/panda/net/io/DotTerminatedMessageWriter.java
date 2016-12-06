package panda.net.io;

import java.io.IOException;
import java.io.Writer;

/***
 * DotTerminatedMessageWriter is a class used to write messages to a server that are terminated by a
 * single dot followed by a &lt;CR&gt;&lt;LF&gt; sequence and with double dots appearing at the
 * begining of lines which do not signal end of message yet start with a dot. Various Internet
 * protocols such as NNTP and POP3 produce messages of this type.
 * <p>
 * This class handles the doubling of line-starting periods, converts single linefeeds to NETASCII
 * newlines, and on closing will send the final message terminator dot and NETASCII newline
 * sequence.
 ***/

public final class DotTerminatedMessageWriter extends CRLFLineWriter {
	/***
	 * Creates a DotTerminatedMessageWriter that wraps an existing Writer output destination.
	 * 
	 * @param output The Writer output destination to write the message.
	 ***/
	public DotTerminatedMessageWriter(Writer output) {
		super(output);
	}

	/**
	 * write end mark
	 * @throws IOException
	 */
	protected void end() throws IOException {
		__output.write(".\r\n");
	}
}
