package panda.net;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Strings;
import panda.net.http.UserAgent;

public class UserAgentTest {

	@Test
	public void test01() {
		UserAgent b = new UserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
		Assert.assertTrue(b.isFirefox());
		
		b = new UserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30");
		Assert.assertTrue(b.isSafari());
		
		b = new UserAgent("iPhone/4.2 Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30");
		Assert.assertTrue(b.isChrome());
		Assert.assertTrue(b.isIos());
		Assert.assertTrue(b.isIphone());
		
		System.out.println(b.toSimpleString());
		Assert.assertTrue(Strings.contains(b.toSimpleString(), "windows"));
		Assert.assertTrue(Strings.contains(b.toSimpleString(), "iphone"));
		Assert.assertTrue(Strings.contains(b.toSimpleString(), "chrome"));
		Assert.assertTrue(Strings.contains(b.toSimpleString(), "safari"));
	}
}
