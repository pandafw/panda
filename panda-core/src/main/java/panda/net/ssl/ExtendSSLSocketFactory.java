package panda.net.ssl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import panda.lang.Arrays;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.reflect.Methods;

public class ExtendSSLSocketFactory extends SSLSocketFactory {
	private SSLSocketFactory sslSocketFactory;
	private String[] enabledProtocols;
	private String[] enabledCipherSuites;
	private boolean sniExtensionDisabled;
	
	protected ExtendSSLSocketFactory() {
	}

	/**
	 * @param sslSocketFactory
	 */
	public ExtendSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		this.sslSocketFactory = sslSocketFactory;
	}

	/**
	 * @return the sslSocketFactory
	 */
	protected SSLSocketFactory getSslSocketFactory() {
		return sslSocketFactory;
	}

	/**
	 * @param sslSocketFactory the sslSocketFactory to set
	 */
	protected void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
		this.sslSocketFactory = sslSocketFactory;
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

	/**
	 * @return the enabledCipherSuites
	 */
	public String[] getEnabledCipherSuites() {
		return enabledCipherSuites;
	}

	/**
	 * @param enabledCipherSuites the enabledCipherSuites to set
	 */
	public void setEnabledCipherSuites(String[] enabledCipherSuites) {
		this.enabledCipherSuites = enabledCipherSuites;
	}

	/**
	 * @return the sniExtensionDisabled
	 */
	public boolean isSniExtensionDisabled() {
		return sniExtensionDisabled;
	}

	/**
	 * @param sniExtensionDisabled the sniExtensionDisabled to set
	 */
	public void setSniExtensionDisabled(boolean sniExtensionDisabled) {
		this.sniExtensionDisabled = sniExtensionDisabled;
	}

	protected void initSSLSocketFactory(TrustManager tm) {
		try {
			SSLContext sslctx = SSLContexts.createSSLContext("SSL", null, tm);
			ExtendSSLSocketFactory sslsf = new ExtendSSLSocketFactory(sslctx.getSocketFactory());
			setSslSocketFactory(sslsf);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	private Socket initSocketSettings(Socket socket) {
		if (socket instanceof SSLSocket) {
			SSLSocket sslSocket = (SSLSocket)socket;
			if (sniExtensionDisabled) {
				SSLParameters params = sslSocket.getSSLParameters();
				// java 1.7 does not support setServerNames()
				Method ssn = Methods.getAccessibleMethod(SSLParameters.class, "setServerNames", List.class);
				if (ssn != null) {
					try {
						ssn.invoke(params, Collections.EMPTY_LIST);
					}
					catch (Exception e) {
						throw Exceptions.wrapThrow(e);
					}
				}
				sslSocket.setSSLParameters(params);
			}
			if (Arrays.isNotEmpty(enabledProtocols)) {
				sslSocket.setEnabledProtocols(enabledProtocols);
			}
			if (Arrays.isNotEmpty(enabledCipherSuites)) {
				sslSocket.setEnabledCipherSuites(enabledCipherSuites);
			}
		}
		return socket;
	}
	
	@Override
	public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
			throws IOException {
		return initSocketSettings(sslSocketFactory.createSocket(address, port, localAddress, localPort));
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		return initSocketSettings(sslSocketFactory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
			throws IOException, UnknownHostException {
		return initSocketSettings(sslSocketFactory.createSocket(host, port, localHost, localPort));
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return initSocketSettings(sslSocketFactory.createSocket(host, port));
	}

	@Override
	public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
		return initSocketSettings(sslSocketFactory.createSocket(s, host, port, autoClose));
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
