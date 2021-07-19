package panda.net;

import java.net.InetSocketAddress;
import java.net.Proxy;

import panda.net.ftp.FTPClient;

/**
 * A simple functional test class for SocketClients. Requires a Java-compatible SOCK proxy server on
 * 127.0.0.1:9050 and access to ftp.gnu.org.
 */
public class SocketClientFunctionalTest {
	// any subclass will do, but it should be able to connect to the destination host
	SocketClient sc = new FTPClient();
	private static final String PROXY_HOST = "127.0.0.1";
	private static final int PROXY_PORT = 9050;
	private static final String DEST_HOST = "ftp.gnu.org";
	private static final int DEST_PORT = 21;

	public static void main(String[] argv) throws Exception {
		SocketClientFunctionalTest scft = new SocketClientFunctionalTest();
		scft.testProxySettings();
	}

	/**
	 * A simple test to verify that the Proxy settings work.
	 * 
	 * @throws Exception in case of connection errors
	 */
	public void testProxySettings() throws Exception {
		// NOTE: HTTP Proxies seem to be invalid for raw sockets
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
		sc.setProxy(proxy);
		sc.connect(DEST_HOST, DEST_PORT);
		System.out.println("Proxy connect: " + sc.isConnected());
		sc.disconnect();
	}
}