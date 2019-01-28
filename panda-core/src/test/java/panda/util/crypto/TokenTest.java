package panda.util.crypto;

import org.junit.Assert;
import org.junit.Test;

/**
 * test class for Token
 */
public class TokenTest {
	@Test
	public void testSalt() {
		for (int i = 0; i < 100; i++) {
			Token token = new Token();

			String salted = Token.salt(token.getSecret(), token.getSalt());
			Assert.assertEquals(token.getSecret().length(), salted.length());
			
			String unsalted = Token.unsalt(salted,token.getSalt());
			Assert.assertEquals(token.getSecret(), unsalted);
		}
	}

	@Test
	public void testParse() {
		for (int i = 0; i < 100; i++) {
			Token t1 = new Token();

			Token t2 = Token.parse(t1.getToken());

			Assert.assertNotNull(t2);
			Assert.assertEquals(t1, t2);
			Assert.assertEquals(t1.getSalt(), t2.getSalt());
			Assert.assertEquals(t1.getSecret(), t2.getSecret());
			Assert.assertEquals(t1.getTimestamp(), t2.getTimestamp());
		}
	}
}
