package panda.net;

import panda.net.http.UserAgent;
import junit.framework.TestCase;

/**
 */
public class UserAgentTest extends TestCase {

	/**
	 */
	public void test01() {
		UserAgent b = new UserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
		System.out.println(b.toSimpleString());

		b = new UserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30");
		System.out.println(b.toSimpleString());

		b = new UserAgent("iPhone/4.2 Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30");
		System.out.println(b.toSimpleString());
	}
}
