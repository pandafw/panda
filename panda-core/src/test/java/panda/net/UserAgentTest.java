package panda.net;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Strings;
import panda.net.http.UserAgent;

public class UserAgentTest {

	@Test
	public void test01() {
		UserAgent ua = new UserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
		Assert.assertTrue(ua.isFirefox());
		
		ua = new UserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Safari/534.30");
		Assert.assertTrue(ua.isSafari());
		
		ua = new UserAgent("iPhone/4.2 Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30");
		Assert.assertTrue(ua.isChrome());
		Assert.assertFalse(ua.isSafari());
		Assert.assertTrue(ua.isIos());
		Assert.assertTrue(ua.isIphone());
		
		System.out.println(ua.toMajorString());
		System.out.println(ua.toSimpleString());
		Assert.assertTrue(Strings.contains(ua.toSimpleString(), "iphone"));
		Assert.assertTrue(Strings.contains(ua.toSimpleString(), "chrome"));
	}
	
	@Test
	public void test02() {
		UserAgent ua = new UserAgent("Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0 Mobile/15E148 Safari/604.1");

		Assert.assertTrue(ua.isSafari());
		Assert.assertTrue(ua.isIos());
		Assert.assertTrue(ua.isIphone());
		
		System.out.println(ua.toMajorString());
		System.out.println(ua.toSimpleString());
	}
}
