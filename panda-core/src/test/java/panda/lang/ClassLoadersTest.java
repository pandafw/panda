package panda.lang;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests {@link panda.lang.ClassLoaders}.
 */
public class ClassLoadersTest {
	@Test
	public void testGetResourceAsURL() {
		Assert.assertNotNull(ClassLoaders.getResourceAsURL("log.properties"));
	}

}
