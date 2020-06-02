package panda.tube.wordpress;

import org.junit.Assume;
import org.junit.Before;

import panda.lang.Strings;
import panda.lang.Systems;
import panda.log.Logs;

public class AbstractWordpressTest {
	public static WordPress WP;

	@Before
	public void setUp() {
		if (WP != null) {
			return;
		}
		
		// "http://localhost/xmlrpc.php"
		String XMLRPCURL = Systems.getenv("WP_XMLRPCURL");
		if (Strings.isEmpty(XMLRPCURL)) {
			Logs.getLog(getClass()).warn("SKIP: " + this.getClass().getName());
			Assume.assumeTrue(false);
		}

		String USERNAME = Systems.getenv("WP_USERNAME", "admin");
		String PASSWORD = Systems.getenv("WP_PASSWORD", "password");
		WP = new WordPress(XMLRPCURL, USERNAME, PASSWORD);
	}

}
