package panda.net.ftp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

/**
 * Server socket factory for FTPS connections.
 * 
 */
public class FTPSServerSocketFactory extends ServerSocketFactory {

	/** Factory for secure socket factories */
	private final SSLContext context;

	public FTPSServerSocketFactory(SSLContext context) {
		this.context = context;
	}

	// Override the default superclass method
	@Override
	public ServerSocket createServerSocket() throws IOException {
		return init(this.context.getServerSocketFactory().createServerSocket());
	}

	@Override
	public ServerSocket createServerSocket(int port) throws IOException {
		return init(this.context.getServerSocketFactory().createServerSocket(port));
	}

	@Override
	public ServerSocket createServerSocket(int port, int backlog) throws IOException {
		return init(this.context.getServerSocketFactory().createServerSocket(port, backlog));
	}

	@Override
	public ServerSocket createServerSocket(int port, int backlog, InetAddress ifAddress) throws IOException {
		return init(this.context.getServerSocketFactory().createServerSocket(port, backlog, ifAddress));
	}

	/**
	 * Sets the socket so newly accepted connections will use SSL client mode.
	 * 
	 * @param socket the SSLServerSocket to initialise
	 * @return the socket
	 * @throws ClassCastException if socket is not an instance of SSLServerSocket
	 */
	public ServerSocket init(ServerSocket socket) {
		((SSLServerSocket)socket).setUseClientMode(true);
		return socket;
	}
}
