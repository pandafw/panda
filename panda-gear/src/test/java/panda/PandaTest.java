package panda;

import org.junit.Assert;
import org.junit.Test;


public class PandaTest {
	@Test
	public void testVersion() {
		Assert.assertNotNull(Panda.VERSION);
	}
}
