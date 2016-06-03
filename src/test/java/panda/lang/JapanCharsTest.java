package panda.lang;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.JapanChars;

import junit.framework.TestCase;

/**
 * test class for AsiaCharUtils
 */
public class JapanCharsTest extends TestCase {
	private void print(String name, String zen, String han) {
		Assert.assertEquals(zen.length(), han.length());
		
		System.out.println(Strings.center(name, 40, '/'));
		for (int i = 0; i < zen.length(); i++) {
			char z = zen.charAt(i);
			char h = han.charAt(i);
			System.out.println("{ 0x" + Strings.leftPad(Integer.toString(z, 16), 4, '0') 
				+ ", 0x" + Strings.leftPad(Integer.toString(h, 16), 4, '0') + " },  //"
				+ z + " --> " + h + ' ');
		}
	}
	
	@Test
	public void printLine() {
		System.out.println("HANKAKU_ASCII:   " + JapanChars.HANKAKU_ASCII);
		System.out.println("ZENKAKU_ASCII:   " + JapanChars.ZENKAKU_ASCII);
		System.out.println("HANKAKU_NORMAL:  " + JapanChars.HANKAKU_NORMAL);
		System.out.println("ZENKAKU_NORMAL:  " + JapanChars.ZENKAKU_NORMAL);
		System.out.println("HANKAKU_KASATAHA:" + JapanChars.HANKAKU_KASATAHA);
		System.out.println("ZENKAKU_KASATAHA:" + JapanChars.ZENKAKU_KASATAHA);
		System.out.println("ZENKAKU_GAZADABA:" + JapanChars.ZENKAKU_GAZADABA);
		System.out.println("HANKAKU_WAOU:    " + JapanChars.HANKAKU_WAOU);
		System.out.println("ZENKAKU_WAOU:    " + JapanChars.ZENKAKU_WAOU);
		System.out.println("ZENKAKU_VAVO:    " + JapanChars.ZENKAKU_VAVO);
		System.out.println("HANKAKU_HANDAKU: " + JapanChars.HANKAKU_HANDAKU);
		System.out.println("ZENKAKU_HANDAKU: " + JapanChars.ZENKAKU_HANDAKU);
	}

	@Test
	public void printPair() {
		print("HANKAKU_ASCII:   ", JapanChars.ZENKAKU_ASCII, JapanChars.HANKAKU_ASCII);
		print("HANKAKU_NORMAL:  ", JapanChars.ZENKAKU_NORMAL, JapanChars.HANKAKU_NORMAL);
		print("HANKAKU_KASATAHA:", JapanChars.ZENKAKU_KASATAHA, JapanChars.HANKAKU_KASATAHA);
		print("HANKAKU_WAOU:    ", JapanChars.ZENKAKU_WAOU, JapanChars.HANKAKU_WAOU);
		print("HANKAKU_HANDAKU: ", JapanChars.ZENKAKU_HANDAKU, JapanChars.HANKAKU_HANDAKU);
	}
	
	@Test
	public void testLength() {
		Assert.assertEquals(JapanChars.HANKAKU_ASCII.length(), JapanChars.ZENKAKU_ASCII.length());
		Assert.assertEquals(JapanChars.HANKAKU_NORMAL.length(), JapanChars.ZENKAKU_NORMAL.length());
		Assert.assertEquals(JapanChars.HANKAKU_KASATAHA.length(), JapanChars.ZENKAKU_KASATAHA.length());
		Assert.assertEquals(JapanChars.HANKAKU_KASATAHA.length(), JapanChars.ZENKAKU_GAZADABA.length());
		Assert.assertEquals(JapanChars.HANKAKU_WAOU.length(), JapanChars.ZENKAKU_WAOU.length());
		Assert.assertEquals(JapanChars.HANKAKU_WAOU.length(), JapanChars.ZENKAKU_VAVO.length());
		Assert.assertEquals(JapanChars.HANKAKU_DAKU.length(), JapanChars.ZENKAKU_DAKU.length());
		Assert.assertEquals(JapanChars.HANKAKU_HANDAKU.length(), JapanChars.ZENKAKU_HANDAKU.length());
		Assert.assertEquals(JapanChars.HANKAKU.length(), JapanChars.ZENKAKU.length());
	}

	/**
	 * test method: IsHankakuKatakanaChar
	 */
	public void testIsHankakuKatakanaChar() {
		assertFalse(JapanChars.isHankakuKatakana('a'));
		assertTrue(JapanChars.isHankakuKatakana('\uff88'));
	}

	/**
	 * test method: isZenkakuKatakanaChar
	 */
	public void testIsZenkakuKatakanaChar() {
		assertFalse(JapanChars.isZenkakuKatakana('a'));
		assertTrue(JapanChars.isZenkakuKatakana('\u30a1'));
	}

	/**
	 * test method: isHankakuChar
	 */
	public void testIsHankakuChar() {
		assertFalse(JapanChars.isHankaku('\250'));
		assertFalse(JapanChars.isHankaku('\u300b'));
		assertTrue(JapanChars.isHankaku('a'));
	}

	/**
	 * test method: isZenkakuChar
	 */
	public void testIsZenkakuChar() {
		assertFalse(JapanChars.isZenkaku('a'));
		assertTrue(JapanChars.isZenkaku('\u3011'));
	}

}
