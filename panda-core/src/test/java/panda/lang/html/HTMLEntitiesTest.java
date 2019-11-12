package panda.lang.html;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.escape.EntityArrays;
import panda.lang.html.HTMLEntities;

/**
 * Unit tests for {@link HTMLEntities}.
 */
public class HTMLEntitiesTest  {

	@Test
	public void testConstructorExists() {
		new EntityArrays();
	}

	// check arrays for duplicate entries
	@Test
	public void testHTML40_EXTENDED_ESCAPE() {
		Assert.assertEquals(HTMLEntities.HTML40_EXTENDED_ESCAPE.size(), HTMLEntities.HTML40_EXTENDED_UNESCAPE.size());
		final Set<String> c = new HashSet<String>();
		final Map<String, String> m = HTMLEntities.HTML40_EXTENDED_ESCAPE;
		for (String s : m.values()) {
			Assert.assertNotNull(s);
			Assert.assertTrue(c.add(s));
		}
	}

	// check arrays for duplicate entries
	@Test
	public void testISO8859_1_ESCAPE() {
		Assert.assertEquals(HTMLEntities.ISO8859_1_ESCAPE.size(), HTMLEntities.ISO8859_1_UNESCAPE.size());
		final Set<String> c = new HashSet<String>();
		final Map<String, String> m = HTMLEntities.ISO8859_1_ESCAPE;
		for (String s : m.values()) {
			Assert.assertNotNull(s);
			Assert.assertTrue(c.add(s));
		}
	}

}
