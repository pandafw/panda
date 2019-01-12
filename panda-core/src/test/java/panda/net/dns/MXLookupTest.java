package panda.net.dns;

import java.util.List;

import junit.framework.TestCase;
import panda.net.dns.MXLookup;

/**
 * test class for MXLookup
 * @see MXLookup
 */
public class MXLookupTest extends TestCase {

	/**
	 * @see MXLookup#lookup(String)
	 */
	public void testLookup() {
		String[] args = new String[] {
				"hotmail.com", "yahoo.com", "gmail.com"
		};
		for (int i = 0; i < args.length; i++) {
			try {
				List<String> hosts = MXLookup.lookup(args[i]);
				assertTrue(hosts.size() > 0);
				for (int j = 0; j < hosts.size(); j++) {
					System.out.println(args[i] + "[" + j + "]: " + hosts.get(j));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
				return;
			}
		}
	}

}
