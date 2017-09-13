package panda.net.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import panda.lang.Arrays;

public class SSLSocketFactoryEx extends SSLSocketFactory {
	private SSLSocketFactory sslSocketFactory;
	private String[] enabledProtocols;
	
	
	/**
	 * @param sslSocketFactory
	 */
	public SSLSocketFactoryEx(SSLSocketFactory sslSocketFactory) {
		this.sslSocketFactory = sslSocketFactory;
	}

	/**
	 * @param sslSocketFactory
	 * @param enabledProtocols
	 */
	public SSLSocketFactoryEx(SSLSocketFactory sslSocketFactory, String... enabledProtocols) {
		this.sslSocketFactory = sslSocketFactory;
		this.enabledProtocols = enabledProtocols;
	}

	/**
	 * @return the enabledProtocols
	 */
	public String[] getEnabledProtocols() {
		return enabledProtocols;
	}

	/**
	 * @param enabledProtocols the enabledProtocols to set
	 */
	public void setEnabledProtocols(String[] enabledProtocols) {
		this.enabledProtocols = enabledProtocols;
	}

	private Socket disableProtocols(Socket socket) {
		if (Arrays.isNotEmpty(enabledProtocols) && socket instanceof SSLSocket) {
			SSLSocket sslSocket = (SSLSocket)socket;
			sslSocket.setEnabledProtocols(enabledProtocols);
		}
		return socket;
	}
	
	@Override
	public Socket createSocket() throws IOException {
		return disableProtocols(sslSocketFactory.createSocket());
	}

	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
			throws IOException {
		return disableProtocols(sslSocketFactory.createSocket(address, port, localAddress, localPort));
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return disableProtocols(sslSocketFactory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
			throws IOException, UnknownHostException {
		return disableProtocols(sslSocketFactory.createSocket(host, port, localHost, localPort));
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return disableProtocols(sslSocketFactory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
		return disableProtocols(sslSocketFactory.createSocket(s, host, port, autoClose));
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return sslSocketFactory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return sslSocketFactory.getSupportedCipherSuites();
	}

}
