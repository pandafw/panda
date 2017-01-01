package panda.net.discard;

import java.io.OutputStream;

import panda.net.SocketClient;

/***
 * The DiscardTCPClient class is a TCP implementation of a client for the Discard protocol described
 * in RFC 863. To use the class, merely establish a connection with
 * {@link panda.net.SocketClient#connect connect } and call {@link #getOutputStream
 * getOutputStream() } to retrieve the discard output stream. Don't close the output stream when
 * you're done writing to it. Rather, call {@link panda.net.SocketClient#disconnect disconnect } to
 * clean up properly.
 * 
 * @see DiscardUDPClient
 ***/

public class DiscardTCPClient extends SocketClient {
	/*** The default discard port. It is set to 9 according to RFC 863. ***/
	public static final int DEFAULT_PORT = 9;

	/***
	 * The default DiscardTCPClient constructor. It merely sets the default port to
	 * <code> DEFAULT_PORT </code>.
	 ***/
	public DiscardTCPClient() {
		setDefaultPort(DEFAULT_PORT);
	}

	/***
	 * Returns an OutputStream through which you may write data to the server. You should NOT close
	 * the OutputStream when you're finished reading from it. Rather, you should call
	 * {@link panda.net.SocketClient#disconnect disconnect } to clean up properly.
	 * 
	 * @return An OutputStream through which you can write data to the server.
	 ***/
	public OutputStream getOutputStream() {
		return _output_;
	}
}
