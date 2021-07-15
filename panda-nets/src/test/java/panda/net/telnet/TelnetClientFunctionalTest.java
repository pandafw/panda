package panda.net.telnet;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assume;
import org.junit.Test;

import panda.io.Streams;

/***
 * JUnit functional test for TelnetClient. Connects to the weather forecast service
 * rainmaker.wunderground.com and asks for Los Angeles forecast.
 ***/
public class TelnetClientFunctionalTest {
	/*
	 * Do the functional test: - connect to the weather service - press return on the first menu -
	 * send LAX on the second menu - send X to exit*
	 */
	@Test
	public void testFunctionalTest() throws Exception {
		TelnetClient tc1 = new TelnetClient();
		try {
			tc1.connect("rainmaker.wunderground.com", 3000);
		}
		catch (Exception e) {
			System.out.println("SKIP testFunctionalTest(rainmaker.wunderground.com): " + e.getMessage());
			Assume.assumeTrue(false);
		}

		boolean testresult = false;
		InputStream is = tc1.getInputStream();
		OutputStream os = tc1.getOutputStream();

		boolean cont = waitForString(is, "Return to continue:", 30000);
		if (cont) {
			os.write("\n".getBytes());
			os.flush();
			cont = waitForString(is, "city code--", 30000);
		}
		if (cont) {
			os.write("LAX\n".getBytes());
			os.flush();
			cont = waitForString(is, "Los Angeles", 30000);
		}
		if (cont) {
			cont = waitForString(is, "X to exit:", 30000);
		}
		if (cont) {
			os.write("X\n".getBytes());
			os.flush();
			tc1.disconnect();
			testresult = true;
		}

		assertTrue(testresult);
		Streams.safeClose(os);
		Streams.safeClose(is);
	}

	/*
	 * Helper method. waits for a string with timeout
	 */
	public boolean waitForString(InputStream is, String end, long timeout) throws Exception {
		byte buffer[] = new byte[32];
		long starttime = System.currentTimeMillis();

		String readbytes = "";
		while ((readbytes.indexOf(end) < 0) && ((System.currentTimeMillis() - starttime) < timeout)) {
			if (is.available() > 0) {
				int ret_read = is.read(buffer);
				readbytes = readbytes + new String(buffer, 0, ret_read);
			}
			else {
				Thread.sleep(500);
			}
		}

		if (readbytes.indexOf(end) >= 0) {
			return (true);
		}
		else {
			return (false);
		}
	}
}
