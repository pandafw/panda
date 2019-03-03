package panda.lang;

import org.junit.Assert;
import org.junit.Test;

import panda.log.Log;
import panda.log.Logs;

/**
 * test class for AsiaCharUtils
 */
public class JapanCharsTest {
	private static final Log log = Logs.getLog(JapanCharsTest.class);
	
	private void print(String name, String zen, String han) {
		Assert.assertEquals(name, zen.length(), han.length());
		
		log.debug(Strings.center(name, 40, '/'));
		for (int i = 0; i < zen.length(); i++) {
			char z = zen.charAt(i);
			char h = han.charAt(i);
			log.debug("{ 0x" + Strings.leftPad(Integer.toString(z, 16), 4, '0') 
				+ ", 0x" + Strings.leftPad(Integer.toString(h, 16), 4, '0') + " },  //"
				+ z + " --> " + h + ' ');
		}
	}
	
	@Test
	public void printLine() {
		log.debug("HANKAKU_DIGIT:    " + JapanChars.HANKAKU_DIGIT);
		log.debug("ZENKAKU_DIGIT:    " + JapanChars.ZENKAKU_DIGIT);
		log.debug("HANKAKU_LETTER:   " + JapanChars.HANKAKU_LETTER);
		log.debug("ZENKAKU_LETTER:   " + JapanChars.ZENKAKU_LETTER);
		log.debug("HANKAKU_SYMBOL:   " + JapanChars.HANKAKU_SYMBOL);
		log.debug("ZENKAKU_SYMBOL:   " + JapanChars.ZENKAKU_SYMBOL);
		log.debug("HANKAKU_ASCII:    " + JapanChars.HANKAKU_ASCII);
		log.debug("ZENKAKU_ASCII:    " + JapanChars.ZENKAKU_ASCII);
		log.debug("HANKAKU_MARK:     " + JapanChars.HANKAKU_MARK);
		log.debug("ZENKAKU_MARK:     " + JapanChars.ZENKAKU_MARK);
		log.debug("HANKAKU_AYATU:    " + JapanChars.HANKAKU_AYATU);
		log.debug("ZENKAKU_AYATU:    " + JapanChars.ZENKAKU_AYATU);
		log.debug("HANKAKU_ANAMAYARA:" + JapanChars.HANKAKU_ANAMAYARA);
		log.debug("ZENKAKU_ANAMAYARA:" + JapanChars.ZENKAKU_ANAMAYARA);
		log.debug("HANKAKU_KASATAHA: " + JapanChars.HANKAKU_KASATAHA);
		log.debug("ZENKAKU_KASATAHA: " + JapanChars.ZENKAKU_KASATAHA);
		log.debug("ZENKAKU_GAZADABA: " + JapanChars.ZENKAKU_GAZADABA);
		log.debug("HANKAKU_WAOU:     " + JapanChars.HANKAKU_WAOU);
		log.debug("ZENKAKU_WAOU:     " + JapanChars.ZENKAKU_WAOU);
		log.debug("ZENKAKU_VAVO:     " + JapanChars.ZENKAKU_VAVO);
		log.debug("HANKAKU_HANDAKU:  " + JapanChars.HANKAKU_HANDAKU);
		log.debug("ZENKAKU_HANDAKU:  " + JapanChars.ZENKAKU_HANDAKU);
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
