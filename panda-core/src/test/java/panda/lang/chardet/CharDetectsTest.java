package panda.lang.chardet;

import org.junit.Assert;
import org.junit.Test;

import panda.io.Streams;

/**
 * test class for ClassUtils
 */
public class CharDetectsTest {
	@Test
	public void testDetectCharsets() throws Exception {
		Assert.assertEquals("Shift_JIS", CharDetects.detectCharset(Streams.toByteArray(getClass().getResource("Shift-JIS.txt"))));
	}

}
