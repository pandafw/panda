package panda.lang;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests {@link panda.lang.MultiByteChars}.
 */
public class MultiByteCharsTest {

	// halfNumber 半角数字: 0123456789
	protected static final String halfNumber = ("0123456789");

	// fullNumber 全角数字: ０１２３４５６７８９
	protected static final String fullNumber = ("０１２３４５６７８９");

	// halfLetter 半角英字: ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz
	protected static final String halfLetter = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

	// fullLetter 全角英字: ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ
	protected static final String fullLetter = ("ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ");

	// halfSymbol 半角記号: !""""#$%&'()*+,----./:;<=>?@[\]^_`{|}~~
	protected static final String halfSymbol = (" !\"\"\"\"#$%&'()*+,----./:;<=>?@[\\]^_`{|}~~");

	// fullSymbol 全角記号: ！″＂“”＃＄％＆’（）＊＋，－ー‐−．／：；＜＝＞？＠［￥］＾＿｀｛｜｝～〜
	protected static final String fullSymbol = ("　！″＂”“＃＄％＆’（）＊＋，－ー‐−．／：；＜＝＞？＠［￥］＾＿｀｛｜｝～〜");

	// halfASCII 半角: !"#$%&'()*+,-./0123456789:<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}~~
	protected static final String halfASCII      = halfNumber + halfLetter + halfSymbol;
	protected static final char[] halfASCIIChars = halfASCII.toCharArray();

	// fullASCII 全角: ！“”＃＄％＆’（）＊＋，－．／０１２３４５６７８９：；＜＝＞？＠ＡＢＣＤＥＦＧＨＩＪＫＬＭＮＯＰＱＲＳＴＵＶＷＸＹＺ［￥］＾＿｀ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ｛｜｝～〜
	protected static final String fullASCII      = fullNumber + fullLetter + fullSymbol;
	protected static final char[] fullASCIIChars = fullASCII.toCharArray();

	private void testASCII(String name, String zen, String han) {
		testPrint(name, zen, han);
	}

	protected static char specialZ(char c) {
		switch (c) {
		case '“':
		case '”':
		case '″':
			return '＂';
		case '〜':
			return '～';
		case '’':
			return '＇';
		case '￥':
			return '＼';
		case 'ー':
		case '‐':
		case '−':
			return '－';
		default:
			return c;
		}
	}

	private void testPrint(String name, String zens, String hans) {
		char[] zen = zens.toCharArray();
		char[] han = hans.toCharArray();

		assertEquals(name, zen.length, han.length);

		System.out.println("// " + name);
		for (int i = 0; i < zen.length && i < han.length; i++) {
			char z = zen[i];
			char h = han[i];

			char z2h = MultiByteChars.toHalfChar(z);
			char h2z = MultiByteChars.toFullChar(h);
			assertEquals(h+" <> "+z2h, h, z2h);
			char sz = specialZ(z);
			assertEquals(sz+" <> "+h2z, sz, h2z);

			System.out.printf("'\\u%04X': '\\u%04X', // %s => %s\n", (int)z, (int)h, String.valueOf(z), String.valueOf(h));
		}
	}

	@Test
	public void TestASCIIPair() {
		testASCII("Number", fullNumber, halfNumber);
		testASCII("Letter", fullLetter, halfLetter);
		testASCII("Symbol", fullSymbol, halfSymbol);
		testASCII("ASCII", fullASCII, halfASCII);
		testASCII("ASCII", fullASCII, halfASCII);
	}
}
