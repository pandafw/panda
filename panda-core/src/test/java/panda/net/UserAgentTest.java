package panda.net;

import org.junit.Assert;
import org.junit.Test;

import panda.log.Log;
import panda.log.Logs;
import panda.net.http.UserAgent;

public class UserAgentTest {

	protected static final Log log = Logs.getLog(UserAgentTest.class);
	
	@Test
	public void test() {
		Object[][] uas = {
				{
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0", 
					new Object[] { "Firefox", "5.0", 5, 0 },
					new Object[] { "Windows", "6.1", 6, 1 },
					new Object[] { "Windows", "6.1", 6, 1 },
					UserAgent.FLAG_DESKTOP | UserAgent.FLAG_WINDOWS | UserAgent.FLAG_64BIT
				},
				{
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Safari/534.30",
					new Object[] { "Safari", "534.30", 534, 30 },
					new Object[] { "Windows", "6.1", 6, 1 },
					new Object[] { "Windows", "6.1", 6, 1 },
					UserAgent.FLAG_DESKTOP | UserAgent.FLAG_WINDOWS | UserAgent.FLAG_64BIT
				},
				{
					"Mozilla/5.0 (iPhone; CPU iPhone OS 6_1_3 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10B329 Safari/8536.25",
					new Object[] { "Safari", "8536.25", 8536, 25 },
					new Object[] { "iPhone", "6.1.3", 6, 1 },
					new Object[] { "iPhone", "6.1.3", 6, 1 },
					UserAgent.FLAG_MOBILE | UserAgent.FLAG_IPHONE
				},
				{
					"Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
					new Object[] { "MSIE", "11.0", 11, 0 },
					new Object[] { "Windows", "10.0", 10, 0 },
					new Object[] { "Windows", "10.0", 10, 0 },
					UserAgent.FLAG_DESKTOP | UserAgent.FLAG_WINDOWS | UserAgent.FLAG_64BIT
				},
				{
					"Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19",
					new Object[] { "Chrome", "18.0.1025.133", 18, 0 },
					new Object[] { "Android", "4.0.4", 4, 0 },
					new Object[] { "Android", "4.0.4", 4, 0 },
					UserAgent.FLAG_MOBILE | UserAgent.FLAG_ANDROID
				},
				{
					"Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Safari/535.19",
					new Object[] { "Chrome", "18.0.1025.133", 18, 0 },
					new Object[] { "Android", "4.0.4", 4, 0 },
					new Object[] { "Android", "4.0.4", 4, 0 },
					UserAgent.FLAG_MOBILE | UserAgent.FLAG_ANDROID | UserAgent.FLAG_TABLET
				}
		};
		
		for (Object[] ts : uas) {
			//log.warn((String)ts[0]);

			UserAgent ua = new UserAgent((String)ts[0]);
			
			check(ua.getValue(), (Object[])ts[1], ua.getBrowser());
			check(ua.getValue(), (Object[])ts[2], ua.getDevice());
			check(ua.getValue(), (Object[])ts[3], ua.getPlatform());
			
			Assert.assertEquals(ua.getValue(), (int)ts[4], ua.getFlags());
		}
	}
	
	private void check(String v, Object[] o, UserAgent.Agent a) {
		Assert.assertEquals(v, (String)o[0], a.getName());
		Assert.assertEquals(v, (String)o[1], a.getVersion());
		Assert.assertEquals(v, (int)o[2], a.getMajor());
		Assert.assertEquals(v, (int)o[3], a.getMinor());
	}
}
