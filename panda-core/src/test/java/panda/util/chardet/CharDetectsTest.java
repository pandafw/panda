package panda.util.chardet;

import org.junit.Assert;
import org.junit.Test;

import panda.io.Streams;
import panda.lang.Strings;

/**
 * test class for ClassUtils
 */
public class CharDetectsTest {
	@Test
	public void testDetectCharsets() throws Exception {
		Assert.assertEquals("Shift_JIS,GB18030", Strings.join(CharDetects.detectCharsets(Streams.toByteArray(getClass().getResource("Shift-JIS.txt"))), ","));
	}

}
