package panda.net.dns;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import panda.log.Log;
import panda.log.Logs;

/**
 * test class for MXLookup
 * @see MXLookup
 */
public class MXLookupTest {
	private static final Log log = Logs.getLog(MXLookupTest.class);
	
	@Test
	public void testLookup() {
		String[] args = new String[] {
				"hotmail.com", "yahoo.com", "gmail.com"
		};
		for (int i = 0; i < args.length; i++) {
			try {
				List<String> hosts = MXLookup.lookup(args[i]);
				assertTrue(hosts.size() > 0);
//				for (int j = 0; j < hosts.size(); j++) {
//					log.debug(args[i] + "[" + j + "]: " + hosts.get(j));
//				}
			}
			catch (Exception e) {
				log.error("error", e);
				fail(e.getMessage());
				return;
			}
		}
	}

}
