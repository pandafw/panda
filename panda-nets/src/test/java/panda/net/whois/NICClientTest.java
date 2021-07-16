package panda.net.whois;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Strings;


public class NICClientTest {

	private NICClient nicc = new NICClient();

	private void test(String domain, String result) {
		String r = nicc.whois(domain);
		if (!Strings.contains(r, result)) {
			System.out.println(Strings.center(domain, 60, '='));
			System.out.println(r);
		}
		Assert.assertTrue(Strings.contains(r, result));
	}

	@Test
	public void testGoogleCom() {
		test("Google.com", "Country: US");
	}

//	@Test
//	public void test163net() {
//		test("163.net", "Country: CN");
//	}

	@Test
	public void testDocomo() {
		test("nttdocomo.co.jp", "株式会社NTTドコモ");
	}

	@Test
	public void testSimpleUnicodeDomain() {
		test("お名前.com", "Country: JP");
	}
}
