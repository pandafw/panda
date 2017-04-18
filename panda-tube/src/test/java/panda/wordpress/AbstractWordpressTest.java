package panda.wordpress;

import java.net.MalformedURLException;
import java.util.Map;

import panda.bean.Beans;

public class AbstractWordpressTest {
	public static String SITEURL = "http://wptest.foolite.com";
	public static String USERNAME = "admin";
	public static String PASSWORD = "admin";
	public static String XMLRPCURL = null;
	public static Wordpress WP;

	static {
		try {
			Beans.setInstance(new Beans());

			Map<String, String> env = System.getenv();
			if (env.containsKey("TOPURL")) {
				SITEURL = env.get("TOPURL");
			}
			
			if (env.containsKey("USERNAME")) {
				USERNAME = env.get("USERNAME");
			}
			
			if (env.containsKey("PASSWORD")) {
				PASSWORD = env.get("PASSWORD");
			}
			
			if (env.containsKey("XMLRPCURL")) {
				XMLRPCURL = env.get("XMLRPCURL");
			}
			else {
				XMLRPCURL = SITEURL + "/xmlrpc.php";
			}
			WP = new Wordpress(USERNAME, PASSWORD, XMLRPCURL);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
