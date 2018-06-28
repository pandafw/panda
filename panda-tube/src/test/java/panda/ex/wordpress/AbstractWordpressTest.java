package panda.ex.wordpress;

import panda.lang.Systems;

public class AbstractWordpressTest {
	public static String XMLRPCURL = Systems.getenv("WP_XMLRPCURL", "http://localhost/xmlrpc.php");
	public static String USERNAME = Systems.getenv("WP_USERNAME", "admin");
	public static String PASSWORD = Systems.getenv("WP_PASSWORD", "password");
	public static WordPress WP = new WordPress(XMLRPCURL, USERNAME, PASSWORD);

}
