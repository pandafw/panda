package panda.net;

import java.net.InetAddress;

import junit.framework.TestCase;

/**
 */
public class InetTest extends TestCase {

	/**
	 */
	public void testLocalhost() throws Exception {
		InetAddress localhost = InetAddress.getLocalHost();
		System.out.println(localhost);
	}

	/**
	 */
	public void testGetAllByName() throws Exception {
		InetAddress[] ias = InetAddress.getAllByName("www.google.com");
		for (InetAddress ia : ias) {
			System.out.println(ia);
		}
	}

}
