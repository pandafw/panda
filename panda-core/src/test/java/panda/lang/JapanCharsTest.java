package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * test class for AsiaCharUtils
 */
public class JapanCharsTest {
	// hankakuMark 半角: ｡｢｣､･ﾞﾟ
	protected final static String hankakuMark = ("｡｢｣､･ﾞﾟ");

	// zenkakuMark 全角: 。「」、・゛゜
	protected final static String zenkakuMark = ("。「」、・゛゜");

	// hankakuAyatu 半角: ｧｨｩｪｫｬｭｮｯｰ
	protected final static String hankakuAyatu = ("ｧｨｩｪｫｬｭｮｯｰ");

	// zenkakuAyatu 全角: ァィゥェォャュョッー
	protected final static String zenkakuAyatu = ("ァィゥェォャュョッー");

	// hankakuAnamayara 半角: ｱｲｴｵﾅﾆﾇﾈﾉﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝ
	protected final static String hankakuAnamayara = ("ｱｲｴｵﾅﾆﾇﾈﾉﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾝ");

	// zenkakuAnamayara 全角: アイエオナニヌネノマミムメモヤユヨラリルレロン
	protected final static String zenkakuAnamayara = ("アイエオナニヌネノマミムメモヤユヨラリルレロン");

	// hankakuKasataha 半角　かさたは　行: ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ
	protected final static String hankakuKasataha = ("ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳ");

	// zenkakuKasataha 全角　かさたは　行: カキクケコサシスセソタチツテトハヒフヘホウ
	protected final static String zenkakuKasataha = ("カキクケコサシスセソタチツテトハヒフヘホウ");

	// zenkakuGazadaba 全角　がざだば　行: ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ
	protected final static String zenkakuGazadaba = ("ガギグゲゴザジズゼゾダヂヅデドバビブベボヴ");

	// hankakuWaou 半角　わ　行: ﾜｦ
	protected final static String hankakuWaou = ("ﾜｦ");

	// zenkakuWaou 全角　わ　行: ワヲ
	protected final static String zenkakuWaou = ("ワヲ");

	// zenkakuVavo 全角　わ　行: ヷヺ
	protected final static String zenkakuVavo = ("ヷヺ");

	// hankakuVive 半角　: ｲｴ
	protected final static String hankakuVive = ("ｲｴ");

	// zenkakuVavo 全角　: ヸヹ
	protected final static String zenkakuVive = ("ヸヹ");

	// hankakuDaku 半角　濁文字: ｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾊﾋﾌﾍﾎｳﾜｦｲｴ
	protected final static String hankakuDaku      = hankakuKasataha + hankakuWaou + hankakuVive;
	protected final static char[] hankakuDakuRunes = hankakuDaku.toCharArray();

	// zenkakuDaku 全角　濁文字: ガギグゲゴザジズゼゾダヂヅデドバビブベボヴヷヺヸヹ
	protected final static String zenkakuDaku      = zenkakuGazadaba + zenkakuVavo + zenkakuVive;
	protected final static char[] zenkakuDakuRunes = zenkakuDaku.toCharArray();

	// hankakuHandaku 半角　半濁文字: ﾊﾋﾌﾍﾎ
	protected final static String hankakuHandaku      = ("ﾊﾋﾌﾍﾎ");
	protected final static char[] hankakuHandakuRunes = hankakuHandaku.toCharArray();

	// zenkakuHandaku 全角　半濁文字: パピプペポ
	protected final static String zenkakuHandaku     = ("パピプペポ");
	protected final static char[] zenkakuHadakuRunes = zenkakuHandaku.toCharArray();

	// hankakuKanakana 半角
	protected final static String hankakuKatakana = hankakuAnamayara + hankakuAyatu + hankakuDaku + hankakuHandaku + hankakuKasataha + hankakuWaou + hankakuVive;

	// zenkakuKanakana 全角
	protected final static String zenkakuKatakana = zenkakuAnamayara + zenkakuAyatu + zenkakuDaku + zenkakuHandaku + zenkakuKasataha + zenkakuWaou + zenkakuVive;

	// hankaku 半角
	protected final static String hankaku      = hankakuMark + hankakuAyatu + hankakuAnamayara + hankakuKasataha + hankakuWaou;
	protected final static char[] hankakuRunes = hankaku.toCharArray();

	// zenkaku 全角
	protected final static String zenkaku      = zenkakuMark + zenkakuAyatu + zenkakuAnamayara + zenkakuKasataha + zenkakuWaou;
	protected final static char[] zenkakuRunes = zenkaku.toCharArray();

	private void testJapan(String name, String zens, String hans) {
		char[] zen = zens.toCharArray();
		char[] han = hans.toCharArray();

		assertEquals(name, zen.length, han.length);

		System.out.println("// " + name);
		for (int i = 0; i < zen.length && i < han.length; i++) {
			char z = zen[i];
			char h = han[i];

			System.out.printf("'\\u%04X': '\\u%04X', // %s => %s\n", (int)z, (int)h, String.valueOf(z), String.valueOf(h));
		}
	}

	@Test
	public void TestJapanPair() {
		testJapan("Mark:     ", zenkakuMark, hankakuMark);
		testJapan("Ayatu:    ", zenkakuAyatu, hankakuAyatu);
		testJapan("Anamayara:", zenkakuAnamayara, hankakuAnamayara);
		testJapan("Kasataha: ", zenkakuKasataha, hankakuKasataha);
		testJapan("Waou:     ", zenkakuWaou, hankakuWaou);

		testJapan("Handaku:  ", zenkakuHandaku, hankakuHandaku);
		testJapan("Daku:  ", zenkakuDaku, hankakuDaku);
	}

	@Test
	public void TestIsHankakuKatakanaRune() {
		System.out.println(hankakuKatakana);
		for (char c : JapanCharsTest.hankakuKatakana.toCharArray()) {
			assertTrue(String.format("%04X %s", (int)c, String.valueOf(c)), JapanChars.isHankakuKatakana(c));
		}
	}

	@Test
	public void TestIsZenkakuKatakanaRune() {
		System.out.println(zenkakuKatakana);
		for (char c : zenkakuKatakana.toCharArray()) {
			assertTrue(String.format("%04X %s", (int)c, String.valueOf(c)), JapanChars.isZenkakuKatakana(c));
		}
	}

}
