package panda.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/***
 * DefaultDatagramSocketFactory implements the DatagramSocketFactory interface by simply wrapping
 * the java.net.DatagramSocket constructors. It is the default DatagramSocketFactory used by
 * {@link panda.net.DatagramSocketClient} implementations.
 * 
 * @see DatagramSocketFactory
 * @see DatagramSocketClient
 * @see DatagramSocketClient#setDatagramSocketFactory
 ***/

public class DefaultDatagramSocketFactory implements DatagramSocketFactory {

	/***
	 * Creates a DatagramSocket on the local host at the first available port.
	 * 
	 * @return a new DatagramSocket
	 * @exception SocketException If the socket could not be created.
	 ***/
	// @Override
	public DatagramSocket createDatagramSocket() throws SocketException {
		return new DatagramSocket();
	}

	/***
	 * Creates a DatagramSocket on the local host at a specified port.
	 * 
	 * @param port The port to use for the socket.
	 * @return a new DatagramSocket
	 * @exception SocketException If the socket could not be created.
	 ***/
	// @Override
	public DatagramSocket createDatagramSocket(int port) throws SocketException {
		return new DatagramSocket(port);
	}

	/***
	 * Creates a DatagramSocket at the specified address on the local host at a specified port.
	 * 
	 * @param port The port to use for the socket.
	 * @param laddr The local address to use.
	 * @return a new DatagramSocket
	 * @exception SocketException If the socket could not be created.
	 ***/
	// @Override
	public DatagramSocket createDatagramSocket(int port, InetAddress laddr) throws SocketException {
		return new DatagramSocket(port, laddr);
	}
}
