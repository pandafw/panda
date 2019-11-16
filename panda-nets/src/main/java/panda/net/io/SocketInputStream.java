package panda.net.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/***
 * This class wraps an input stream, storing a reference to its originating socket. When the stream
 * is closed, it will also close the socket immediately afterward. This class is useful for
 * situations where you are dealing with a stream originating from a socket, but do not have a
 * reference to the socket, and want to make sure it closes when the stream closes.
 * 
 * @see SocketOutputStream
 ***/

public class SocketInputStream extends FilterInputStream {
	private final Socket __socket;

	/***
	 * Creates a SocketInputStream instance wrapping an input stream and storing a reference to a
	 * socket that should be closed on closing the stream.
	 * 
	 * @param socket The socket to close on closing the stream.
	 * @param stream The input stream to wrap.
	 ***/
	public SocketInputStream(Socket socket, InputStream stream) {
		super(stream);
		__socket = socket;
	}

	/***
	 * Closes the stream and immediately afterward closes the referenced socket.
	 * 
	 * @exception IOException If there is an error in closing the stream or socket.
	 ***/
	@Override
	public void close() throws IOException {
		super.close();
		__socket.close();
	}
}
