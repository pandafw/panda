package panda.net.imap;

import org.junit.Assert;
import org.junit.Test;

public class IMAPTest {

	@Test
	public void checkGenerator() {
		// This test assumes:
		// - 26 letters in the generator alphabet
		// - the generator uses a fixed size tag
		IMAP imap = new IMAP();
		String initial = imap.generateCommandID();
		int expected = 1;
		for (int j = 0; j < initial.length(); j++) {
			expected *= 26; // letters in alphabet
		}
		int i = 0;
		boolean matched = false;
		while (i <= expected + 10) { // don't loop forever, but allow it to pass go!
			i++;
			String s = imap.generateCommandID();
			matched = initial.equals(s);
			if (matched) { // we've wrapped around completely
				break;
			}
		}
		Assert.assertEquals(expected, i);
		Assert.assertTrue("Expected to see the original value again", matched);
	}
}
