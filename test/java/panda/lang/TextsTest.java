package panda.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import panda.lang.Texts;
import junit.framework.TestCase;

/**
 * test class for StringUtils
 */
public class TextsTest extends TestCase {
	/**
	 * test method: transform
	 * @throws Exception if an error occurs
	 */
	public void testTransform() throws Exception {
		Map<String, String> m = new HashMap<String, String>();
		m.put("a", "1");
		m.put("a.b", "2");
		m.put("a.b.c", "3");
		
		assertEquals("1.23-${8}-xx", Texts.transform("${a}.${a.b}${a.b.c}-${8}-xx", m));
	}

	/**
	 * test method: parseKeywords
	 * @throws Exception if an error occurs
	 */
	public void testParseKeyword() throws Exception {
		String text = "abc1 adf - <>/ @ おあい";
		
		Set<String> kws = Texts.parseKeywords(text);
		for (String s : kws) {
			System.out.println(s);
		}
		System.out.println(Character.isLetter('あ'));
		System.out.println(Character.isDigit('あ'));
		System.out.println(Character.isDigit('９'));
		System.out.println(Character.isLetterOrDigit('@'));
		System.out.println(Character.isLetterOrDigit('/'));
	}
}
