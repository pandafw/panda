package panda.lang;

import org.junit.Test;

/**
 * test class for Digests
 */
public class MacsTest {

	@Test
	public void testHmac256() {
		String secret = "4legyWDNoC59OZLpGGkxhwAJrBFWsDbncRZpPy04";
		String data = "GET\nlba2-api.jp-e1.cloudn-service.com\n/\nAWSAccessKeyId=3T1TRFY0K8ZJPABBR8G8&Action=DescribeLoadBalancers&SignatureMethod=HmacSHA256&SignatureVersion=2&Timestamp=2015-05-12T09%3A35%3A07%2B09%3A00&Version=2012-04-23";
		
		System.out.println(Macs.sha256Hex(secret, data));
		System.out.println(Macs.sha256Base64(secret, data));
	}
}
