package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JapanStringsTest {
	@Test
	public void TestJapanConvert() {
		int l = JapanCharsTest.zenkakuRunes.length;
		for (int i = 0; i < 100; i++) {
			StringBuilder src = new StringBuilder();
			StringBuilder han = new StringBuilder();
			StringBuilder zen = new StringBuilder();
			for (int n = 0; n < 100; n++) {
				int p = Randoms.randInt(l);
				char h = JapanCharsTest.hankakuRunes[p];
				char z = JapanCharsTest.zenkakuRunes[p];
				src.append(h);
				src.append(z);
				han.append(h);
				han.append(h);
				zen.append(z);
				zen.append(z);

				String s = src.toString();
				assertEquals("h:"+s, han.toString(), JapanStrings.toHankaku(s));
				assertEquals("z:"+s, zen.toString(), JapanStrings.toZenkaku(s));
			}
		}
	}

	@Test
	public void TestConvertDaku() {
		StringBuilder sb = new StringBuilder();
		for (char c : JapanCharsTest.hankakuDakuRunes) {
			sb.append(c);
			sb.append('ﾞ');
		}
		String han = sb.toString();
		assertEquals(han, JapanStrings.toHankaku(JapanCharsTest.zenkakuDaku));
		assertEquals(JapanCharsTest.zenkakuDaku, JapanStrings.toZenkaku(han));
	}

	@Test
	public void TestConvertHandaku() {
		StringBuilder sb = new StringBuilder();
		for (char c : JapanCharsTest.hankakuHandakuRunes) {
			sb.append(c);
			sb.append('ﾟ');
		}
		String han = sb.toString();
		assertEquals(han, JapanStrings.toHankaku(JapanCharsTest.zenkakuHandaku));
		assertEquals(JapanCharsTest.zenkakuHandaku, JapanStrings.toZenkaku(han));
	}

	@Test
	public void TestIsHankakuKatakana() {
		System.out.println(JapanCharsTest.hankakuKatakana);
		assertTrue(JapanCharsTest.hankakuKatakana, JapanStrings.isHankakuKatakana(JapanCharsTest.hankakuKatakana));
	}

	@Test
	public void TestIsZenkakuKatakana() {
		System.out.println(JapanCharsTest.zenkakuKatakana);
		assertTrue(JapanCharsTest.zenkakuKatakana, JapanStrings.isZenkakuKatakana(JapanCharsTest.zenkakuKatakana));
	}
}
