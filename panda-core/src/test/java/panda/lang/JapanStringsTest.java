package panda.lang;

import org.junit.Assert;
import org.junit.Test;

public class JapanStringsTest {

	@Test
	public void testAsciiZenkakuToHankaku() {
		Assert.assertEquals("82.7~~10", JapanStrings.asciiZenkakuToHankaku("８２.７～〜１０"));
	}
}
