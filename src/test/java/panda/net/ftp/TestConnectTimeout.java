package panda.net.ftp;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import panda.net.ftp.FTPClient;

import junit.framework.TestCase;

/**
 * Test the socket connect timeout functionality
 */
public class TestConnectTimeout extends TestCase {

	public void testConnectTimeout() throws SocketException, IOException {
		FTPClient client = new FTPClient();
		client.setConnectTimeout(1000);

		try {
			// Connect to a valid host on a bogus port
			// TODO use a local server if possible
			client.connect("www.apache.org", 1234);
			fail("Expecting an Exception");
		}
		catch (ConnectException se) {
			assertTrue(true);
		}
		catch (SocketTimeoutException se) {
			assertTrue(true);
		}
		catch (UnknownHostException ue) {
			// Not much we can do about this, we may be firewalled
			assertTrue(true);
		}

	}
}
