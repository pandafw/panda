package panda.lang;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.JapanChars;

/**
 * test class for AsiaCharUtils
 */
public class JapanCharsTest {
	private void print(String name, String zen, String han) {
		Assert.assertEquals(name, zen.length(), han.length());
		
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
		System.out.println("HANKAKU_DIGIT:    " + JapanChars.HANKAKU_DIGIT);
		System.out.println("ZENKAKU_DIGIT:    " + JapanChars.ZENKAKU_DIGIT);
		System.out.println("HANKAKU_LETTER:   " + JapanChars.HANKAKU_LETTER);
		System.out.println("ZENKAKU_LETTER:   " + JapanChars.ZENKAKU_LETTER);
		System.out.println("HANKAKU_SYMBOL:   " + JapanChars.HANKAKU_SYMBOL);
		System.out.println("ZENKAKU_SYMBOL:   " + JapanChars.ZENKAKU_SYMBOL);
		System.out.println("HANKAKU_ASCII:    " + JapanChars.HANKAKU_ASCII);
		System.out.println("ZENKAKU_ASCII:    " + JapanChars.ZENKAKU_ASCII);
		System.out.println("HANKAKU_MARK:     " + JapanChars.HANKAKU_MARK);
		System.out.println("ZENKAKU_MARK:     " + JapanChars.ZENKAKU_MARK);
		System.out.println("HANKAKU_AYATU:    " + JapanChars.HANKAKU_AYATU);
		System.out.println("ZENKAKU_AYATU:    " + JapanChars.ZENKAKU_AYATU);
		System.out.println("HANKAKU_ANAMAYARA:" + JapanChars.HANKAKU_ANAMAYARA);
		System.out.println("ZENKAKU_ANAMAYARA:" + JapanChars.ZENKAKU_ANAMAYARA);
		System.out.println("HANKAKU_KASATAHA: " + JapanChars.HANKAKU_KASATAHA);
		System.out.println("ZENKAKU_KASATAHA: " + JapanChars.ZENKAKU_KASATAHA);
		System.out.println("ZENKAKU_GAZADABA: " + JapanChars.ZENKAKU_GAZADABA);
		System.out.println("HANKAKU_WAOU:     " + JapanChars.HANKAKU_WAOU);
		System.out.println("ZENKAKU_WAOU:     " + JapanChars.ZENKAKU_WAOU);
		System.out.println("ZENKAKU_VAVO:     " + JapanChars.ZENKAKU_VAVO);
		System.out.println("HANKAKU_HANDAKU:  " + JapanChars.HANKAKU_HANDAKU);
		System.out.println("ZENKAKU_HANDAKU:  " + JapanChars.ZENKAKU_HANDAKU);
	}

	@Test
	public void testPair() {
		print("HANKAKU_DIGIT:    ", JapanChars.ZENKAKU_DIGIT, JapanChars.HANKAKU_DIGIT);
		print("HANKAKU_LETTER:   ", JapanChars.ZENKAKU_LETTER, JapanChars.HANKAKU_LETTER);
		print("HANKAKU_SYMBOL:   ", JapanChars.ZENKAKU_SYMBOL, JapanChars.HANKAKU_SYMBOL);
		print("HANKAKU_ASCII:    ", JapanChars.ZENKAKU_ASCII, JapanChars.HANKAKU_ASCII);
		print("HANKAKU_MARK:     ", JapanChars.ZENKAKU_MARK, JapanChars.HANKAKU_MARK);
		print("HANKAKU_AYATU:    ", JapanChars.ZENKAKU_AYATU, JapanChars.HANKAKU_AYATU);
		print("HANKAKU_ANAMAYARA:", JapanChars.ZENKAKU_ANAMAYARA, JapanChars.HANKAKU_ANAMAYARA);
		print("HANKAKU_KASATAHA: ", JapanChars.ZENKAKU_KASATAHA, JapanChars.HANKAKU_KASATAHA);
		print("HANKAKU_WAOU:     ", JapanChars.ZENKAKU_WAOU, JapanChars.HANKAKU_WAOU);
		print("HANKAKU_HANDAKU:  ", JapanChars.ZENKAKU_HANDAKU, JapanChars.HANKAKU_HANDAKU);
	}
	
	/**
	 * test method: IsHankakuKatakanaChar
	 */
	@Test
	public void testIsHankakuKatakanaChar() {
		Assert.assertFalse(JapanChars.isHankakuKatakana('a'));
		Assert.assertTrue(JapanChars.isHankakuKatakana('\uff88'));
	}

	/**
	 * test method: isZenkakuKatakanaChar
	 */
	@Test
	public void testIsZenkakuKatakanaChar() {
		Assert.assertFalse(JapanChars.isZenkakuKatakana('a'));
		Assert.assertTrue(JapanChars.isZenkakuKatakana('\u30a1'));
	}

	/**
	 * test method: isHankakuChar
	 */
	@Test
	public void testIsHankakuChar() {
		Assert.assertFalse(JapanChars.isHankaku('\250'));
		Assert.assertFalse(JapanChars.isHankaku('\u300b'));
		Assert.assertTrue(JapanChars.isHankaku('a'));
	}

	/**
	 * test method: isZenkakuChar
	 */
	@Test
	public void testIsZenkakuChar() {
		Assert.assertFalse(JapanChars.isZenkaku('a'));
		Assert.assertTrue(JapanChars.isZenkaku('\u3011'));
	}

}
