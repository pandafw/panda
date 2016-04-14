package panda.net;

import java.net.InetAddress;

import org.junit.Assert;
import org.junit.Test;

/**
 */
public class IPsTest {

	@Test
	public void testIsIpV4() throws Exception {
		Assert.assertTrue(IPs.isIPv4("0.2.255.0"));
		Assert.assertTrue(IPs.isIPv4("255.2.255.0"));

		Assert.assertFalse(IPs.isIPv4("0.2"));
		Assert.assertFalse(IPs.isIPv4("0.2.3"));
		Assert.assertFalse(IPs.isIPv4("0.2.255 .0"));
		Assert.assertFalse(IPs.isIPv4("0 .2.255.0"));
		Assert.assertFalse(IPs.isIPv4("256.2.255.0"));
		Assert.assertFalse(IPs.isIPv4("a.2.255.0"));
	}

	@Test
	public void testLocalhost() throws Exception {
		InetAddress localhost = InetAddress.getLocalHost();
		System.out.println(localhost);
	}

	@Test
	public void testGetAllByName() throws Exception {
		InetAddress[] ias = InetAddress.getAllByName("www.google.com");
		for (InetAddress ia : ias) {
			System.out.println(ia);
		}
	}


	@Test
	public void testIsAnyLocalHost() throws Exception {
		// Class A: 10.0.0.0 ~ 10.255.255.255 （10.0.0.0/8）
		// Class B: 172.16.0.0 ~ 172.31.255.255 （172.16.0.0/12）
		// Class C: 192.168.0.0 ~ 192.168.255.255 （192.168.0.0/16）
		Assert.assertTrue("10.0.0.0", IPs.isIntranetAddr("10.0.0.0"));
		Assert.assertTrue("10.0.0.1", IPs.isIntranetAddr("10.0.0.1"));
		Assert.assertTrue("172.16.0.0", IPs.isIntranetAddr("172.16.0.0"));
		Assert.assertTrue("172.16.0.1", IPs.isIntranetAddr("172.16.0.1"));
		Assert.assertTrue("192.168.0.0", IPs.isIntranetAddr("192.168.0.0"));
		Assert.assertTrue("192.168.0.1", IPs.isIntranetAddr("192.168.0.1"));
		Assert.assertTrue("127.0.0.0", IPs.isIntranetAddr("127.0.0.0"));
		Assert.assertTrue("127.0.0.1", IPs.isIntranetAddr("127.0.0.1"));
		Assert.assertTrue("0:0:0:0:0:0:0:0", IPs.isIntranetAddr("0:0:0:0:0:0:0:0"));
		Assert.assertTrue("0:0:0:0:0:0:0:1", IPs.isIntranetAddr("0:0:0:0:0:0:0:1"));
	}
}
