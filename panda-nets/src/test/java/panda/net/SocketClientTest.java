package panda.net;

import junit.framework.TestCase;

import java.net.InetSocketAddress;
import java.net.Proxy;

import panda.net.SocketClient;
import panda.net.ftp.FTPClient;

/**
 * A simple test class for SocketClient settings.
 */
public class SocketClientTest extends TestCase {
	private static final String PROXY_HOST = "127.0.0.1";
	private static final int PROXY_PORT = 1080;

	/**
	 * A simple test to verify that the Proxy is being set.
	 */
	public void testProxySettings() {
		SocketClient socketClient = new FTPClient();
		assertNull(socketClient.getProxy());
		Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
		socketClient.setProxy(proxy);
		assertEquals(proxy, socketClient.getProxy());
		assertFalse(socketClient.isConnected());
	}
}
