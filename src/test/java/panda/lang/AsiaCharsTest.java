package panda.lang;

import panda.lang.AsiaChars;
import junit.framework.TestCase;

/**
 * test class for AsiaCharUtils
 */
public class AsiaCharsTest extends TestCase {

	/**
	 * test method: IsHankakuKatakanaChar
	 */
	public void testIsHankakuKatakanaChar() {
		assertFalse(AsiaChars.isHankakuKatakanaChar('a'));
		assertTrue(AsiaChars.isHankakuKatakanaChar('\uff88'));
	}

	/**
	 * test method: isZenkakuKatakanaChar
	 */
	public void testIsZenkakuKatakanaChar() {
		assertFalse(AsiaChars.isZenkakuKatakanaChar('a'));
		assertTrue(AsiaChars.isZenkakuKatakanaChar('\u30a1'));
	}

	/**
	 * test method: isHankakuChar
	 */
	public void testIsHankakuChar() {
		assertFalse(AsiaChars.isHankakuChar('\250'));
		assertFalse(AsiaChars.isHankakuChar('\u300b'));
		assertTrue(AsiaChars.isHankakuChar('a'));
	}

	/**
	 * test method: isZenkakuChar
	 */
	public void testIsZenkakuChar() {
		assertFalse(AsiaChars.isZenkakuChar('a'));
		assertTrue(AsiaChars.isZenkakuChar('\u3011'));
	}

}
