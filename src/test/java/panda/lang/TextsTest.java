package panda.lang;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import panda.lang.time.StopWatch;

/**
 * test class for Texts
 */
public class TextsTest {
	// -----------------------------------------------------------------------
	private static class CLD {
		String lhs;
		String rhs;
		int dis;
	}
	
	@Test
	public void testComputeLevenshteinDistance() {
		assertEquals(1, Texts.computeLevenshteinDistance("abcdefg", "abcefg"));

		int max = 1000;
		int cnt = 1;
		int len = 10;

		List<CLD> clds = new ArrayList<CLD>();
		for (int i = 0; i < max; i++) {
			CLD cld = new CLD();
			cld.lhs = Randoms.randString(len);
			cld.rhs = Randoms.randString(len);
			cld.dis = Texts.computeLevenshteinDistance(cld.lhs, cld.rhs);
			clds.add(cld);
		}

		StopWatch sw = new StopWatch();
		for (int i = 0; i < cnt; i++) {
			for (CLD cld : clds) {
				assertEquals(cld.dis, Texts.computeLevenshteinDistance(cld.lhs, cld.rhs));
			}
		}
		System.out.println(sw);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testWrap_StringInt() {
		assertEquals(null, Texts.wrap(null, 20));
		assertEquals(null, Texts.wrap(null, -1));

		assertEquals("", Texts.wrap("", 20));
		assertEquals("", Texts.wrap("", -1));

		// normal
		final String systemNewLine = System.getProperty("line.separator");
		String input = "Here is one line of text that is going to be wrapped after 20 columns.";
		String expected = "Here is one line of" + systemNewLine + "text that is going" + systemNewLine
				+ "to be wrapped after" + systemNewLine + "20 columns.";
		assertEquals(expected, Texts.wrap(input, 20));

		// long word at end
		input = "Click here to jump to the jakarta website - http://jakarta.apache.org";
		expected = "Click here to jump" + systemNewLine + "to the jakarta" + systemNewLine + "website -"
				+ systemNewLine + "http://jakarta.apache.org";
		assertEquals(expected, Texts.wrap(input, 20));

		// long word in middle
		input = "Click here, http://jakarta.apache.org, to jump to the jakarta website";
		expected = "Click here," + systemNewLine + "http://jakarta.apache.org," + systemNewLine + "to jump to the"
				+ systemNewLine + "jakarta website";
		assertEquals(expected, Texts.wrap(input, 20));
	}

	@Test
	public void testWrap_StringIntStringBoolean() {
		assertEquals(null, Texts.wrap(null, 20, "\n", false));
		assertEquals(null, Texts.wrap(null, 20, "\n", true));
		assertEquals(null, Texts.wrap(null, 20, null, true));
		assertEquals(null, Texts.wrap(null, 20, null, false));
		assertEquals(null, Texts.wrap(null, -1, null, true));
		assertEquals(null, Texts.wrap(null, -1, null, false));

		assertEquals("", Texts.wrap("", 20, "\n", false));
		assertEquals("", Texts.wrap("", 20, "\n", true));
		assertEquals("", Texts.wrap("", 20, null, false));
		assertEquals("", Texts.wrap("", 20, null, true));
		assertEquals("", Texts.wrap("", -1, null, false));
		assertEquals("", Texts.wrap("", -1, null, true));

		// normal
		String input = "Here is one line of text that is going to be wrapped after 20 columns.";
		String expected = "Here is one line of\ntext that is going\nto be wrapped after\n20 columns.";
		assertEquals(expected, Texts.wrap(input, 20, "\n", false));
		assertEquals(expected, Texts.wrap(input, 20, "\n", true));

		// unusual newline char
		input = "Here is one line of text that is going to be wrapped after 20 columns.";
		expected = "Here is one line of<br />text that is going<br />to be wrapped after<br />20 columns.";
		assertEquals(expected, Texts.wrap(input, 20, "<br />", false));
		assertEquals(expected, Texts.wrap(input, 20, "<br />", true));

		// short line length
		input = "Here is one line";
		expected = "Here\nis one\nline";
		assertEquals(expected, Texts.wrap(input, 6, "\n", false));
		expected = "Here\nis\none\nline";
		assertEquals(expected, Texts.wrap(input, 2, "\n", false));
		assertEquals(expected, Texts.wrap(input, -1, "\n", false));

		// system newline char
		final String systemNewLine = System.getProperty("line.separator");
		input = "Here is one line of text that is going to be wrapped after 20 columns.";
		expected = "Here is one line of" + systemNewLine + "text that is going" + systemNewLine + "to be wrapped after"
				+ systemNewLine + "20 columns.";
		assertEquals(expected, Texts.wrap(input, 20, null, false));
		assertEquals(expected, Texts.wrap(input, 20, null, true));

		// with extra spaces
		input = " Here:  is  one  line  of  text  that  is  going  to  be  wrapped  after  20  columns.";
		expected = "Here:  is  one  line\nof  text  that  is \ngoing  to  be \nwrapped  after  20 \ncolumns.";
		assertEquals(expected, Texts.wrap(input, 20, "\n", false));
		assertEquals(expected, Texts.wrap(input, 20, "\n", true));

		// with tab
		input = "Here is\tone line of text that is going to be wrapped after 20 columns.";
		expected = "Here is\tone line of\ntext that is going\nto be wrapped after\n20 columns.";
		assertEquals(expected, Texts.wrap(input, 20, "\n", false));
		assertEquals(expected, Texts.wrap(input, 20, "\n", true));

		// with tab at wrapColumn
		input = "Here is one line of\ttext that is going to be wrapped after 20 columns.";
		expected = "Here is one line\nof\ttext that is\ngoing to be wrapped\nafter 20 columns.";
		assertEquals(expected, Texts.wrap(input, 20, "\n", false));
		assertEquals(expected, Texts.wrap(input, 20, "\n", true));

		// difference because of long word
		input = "Click here to jump to the jakarta website - http://jakarta.apache.org";
		expected = "Click here to jump\nto the jakarta\nwebsite -\nhttp://jakarta.apache.org";
		assertEquals(expected, Texts.wrap(input, 20, "\n", false));
		expected = "Click here to jump\nto the jakarta\nwebsite -\nhttp://jakarta.apach\ne.org";
		assertEquals(expected, Texts.wrap(input, 20, "\n", true));

		// difference because of long word in middle
		input = "Click here, http://jakarta.apache.org, to jump to the jakarta website";
		expected = "Click here,\nhttp://jakarta.apache.org,\nto jump to the\njakarta website";
		assertEquals(expected, Texts.wrap(input, 20, "\n", false));
		expected = "Click here,\nhttp://jakarta.apach\ne.org, to jump to\nthe jakarta website";
		assertEquals(expected, Texts.wrap(input, 20, "\n", true));
		// System.err.println(expected);
		// System.err.println(Texts.wrap(input, 20, "\n", false));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testCapitalize_String() {
		assertEquals(null, Texts.capitalize(null));
		assertEquals("", Texts.capitalize(""));
		assertEquals("  ", Texts.capitalize("  "));

		assertEquals("I", Texts.capitalize("I"));
		assertEquals("I", Texts.capitalize("i"));
		assertEquals("I Am Here 123", Texts.capitalize("i am here 123"));
		assertEquals("I Am Here 123", Texts.capitalize("I Am Here 123"));
		assertEquals("I Am HERE 123", Texts.capitalize("i am HERE 123"));
		assertEquals("I AM HERE 123", Texts.capitalize("I AM HERE 123"));
	}

	@Test
	public void testCapitalizeWithDelimiters_String() {
		assertEquals(null, Texts.capitalize(null, null));
		assertEquals("", Texts.capitalize("", new char[0]));
		assertEquals("  ", Texts.capitalize("  ", new char[0]));

		char[] chars = new char[] { '-', '+', ' ', '@' };
		assertEquals("I", Texts.capitalize("I", chars));
		assertEquals("I", Texts.capitalize("i", chars));
		assertEquals("I-Am Here+123", Texts.capitalize("i-am here+123", chars));
		assertEquals("I Am+Here-123", Texts.capitalize("I Am+Here-123", chars));
		assertEquals("I+Am-HERE 123", Texts.capitalize("i+am-HERE 123", chars));
		assertEquals("I-AM HERE+123", Texts.capitalize("I-AM HERE+123", chars));
		chars = new char[] { '.' };
		assertEquals("I aM.Fine", Texts.capitalize("i aM.fine", chars));
		assertEquals("I Am.fine", Texts.capitalize("i am.fine", null));
	}

	@Test
	public void testCapitalizeFully_String() {
		assertEquals(null, Texts.capitalizeFully(null));
		assertEquals("", Texts.capitalizeFully(""));
		assertEquals("  ", Texts.capitalizeFully("  "));

		assertEquals("I", Texts.capitalizeFully("I"));
		assertEquals("I", Texts.capitalizeFully("i"));
		assertEquals("I Am Here 123", Texts.capitalizeFully("i am here 123"));
		assertEquals("I Am Here 123", Texts.capitalizeFully("I Am Here 123"));
		assertEquals("I Am Here 123", Texts.capitalizeFully("i am HERE 123"));
		assertEquals("I Am Here 123", Texts.capitalizeFully("I AM HERE 123"));
	}

	@Test
	public void testCapitalizeFullyWithDelimiters_String() {
		assertEquals(null, Texts.capitalizeFully(null, null));
		assertEquals("", Texts.capitalizeFully("", new char[0]));
		assertEquals("  ", Texts.capitalizeFully("  ", new char[0]));

		char[] chars = new char[] { '-', '+', ' ', '@' };
		assertEquals("I", Texts.capitalizeFully("I", chars));
		assertEquals("I", Texts.capitalizeFully("i", chars));
		assertEquals("I-Am Here+123", Texts.capitalizeFully("i-am here+123", chars));
		assertEquals("I Am+Here-123", Texts.capitalizeFully("I Am+Here-123", chars));
		assertEquals("I+Am-Here 123", Texts.capitalizeFully("i+am-HERE 123", chars));
		assertEquals("I-Am Here+123", Texts.capitalizeFully("I-AM HERE+123", chars));
		chars = new char[] { '.' };
		assertEquals("I am.Fine", Texts.capitalizeFully("i aM.fine", chars));
		assertEquals("I Am.fine", Texts.capitalizeFully("i am.fine", null));
	}

	@Test
	public void testUncapitalize_String() {
		assertEquals(null, Texts.uncapitalize(null));
		assertEquals("", Texts.uncapitalize(""));
		assertEquals("  ", Texts.uncapitalize("  "));

		assertEquals("i", Texts.uncapitalize("I"));
		assertEquals("i", Texts.uncapitalize("i"));
		assertEquals("i am here 123", Texts.uncapitalize("i am here 123"));
		assertEquals("i am here 123", Texts.uncapitalize("I Am Here 123"));
		assertEquals("i am hERE 123", Texts.uncapitalize("i am HERE 123"));
		assertEquals("i aM hERE 123", Texts.uncapitalize("I AM HERE 123"));
	}

	@Test
	public void testUncapitalizeWithDelimiters_String() {
		assertEquals(null, Texts.uncapitalize(null, null));
		assertEquals("", Texts.uncapitalize("", new char[0]));
		assertEquals("  ", Texts.uncapitalize("  ", new char[0]));

		char[] chars = new char[] { '-', '+', ' ', '@' };
		assertEquals("i", Texts.uncapitalize("I", chars));
		assertEquals("i", Texts.uncapitalize("i", chars));
		assertEquals("i am-here+123", Texts.uncapitalize("i am-here+123", chars));
		assertEquals("i+am here-123", Texts.uncapitalize("I+Am Here-123", chars));
		assertEquals("i-am+hERE 123", Texts.uncapitalize("i-am+HERE 123", chars));
		assertEquals("i aM-hERE+123", Texts.uncapitalize("I AM-HERE+123", chars));
		chars = new char[] { '.' };
		assertEquals("i AM.fINE", Texts.uncapitalize("I AM.FINE", chars));
		assertEquals("i aM.FINE", Texts.uncapitalize("I AM.FINE", null));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testInitials_String() {
		assertEquals(null, Texts.initials(null));
		assertEquals("", Texts.initials(""));
		assertEquals("", Texts.initials("  "));

		assertEquals("I", Texts.initials("I"));
		assertEquals("i", Texts.initials("i"));
		assertEquals("BJL", Texts.initials("Ben John Lee"));
		assertEquals("BJ", Texts.initials("Ben J.Lee"));
		assertEquals("BJ.L", Texts.initials(" Ben   John  . Lee"));
		assertEquals("iah1", Texts.initials("i am here 123"));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testInitials_String_charArray() {
		char[] array = null;
		assertEquals(null, Texts.initials(null, array));
		assertEquals("", Texts.initials("", array));
		assertEquals("", Texts.initials("  ", array));
		assertEquals("I", Texts.initials("I", array));
		assertEquals("i", Texts.initials("i", array));
		assertEquals("S", Texts.initials("SJC", array));
		assertEquals("BJL", Texts.initials("Ben John Lee", array));
		assertEquals("BJ", Texts.initials("Ben J.Lee", array));
		assertEquals("BJ.L", Texts.initials(" Ben   John  . Lee", array));
		assertEquals("KO", Texts.initials("Kay O'Murphy", array));
		assertEquals("iah1", Texts.initials("i am here 123", array));

		array = new char[0];
		assertEquals(null, Texts.initials(null, array));
		assertEquals("", Texts.initials("", array));
		assertEquals("", Texts.initials("  ", array));
		assertEquals("", Texts.initials("I", array));
		assertEquals("", Texts.initials("i", array));
		assertEquals("", Texts.initials("SJC", array));
		assertEquals("", Texts.initials("Ben John Lee", array));
		assertEquals("", Texts.initials("Ben J.Lee", array));
		assertEquals("", Texts.initials(" Ben   John  . Lee", array));
		assertEquals("", Texts.initials("Kay O'Murphy", array));
		assertEquals("", Texts.initials("i am here 123", array));

		array = " ".toCharArray();
		assertEquals(null, Texts.initials(null, array));
		assertEquals("", Texts.initials("", array));
		assertEquals("", Texts.initials("  ", array));
		assertEquals("I", Texts.initials("I", array));
		assertEquals("i", Texts.initials("i", array));
		assertEquals("S", Texts.initials("SJC", array));
		assertEquals("BJL", Texts.initials("Ben John Lee", array));
		assertEquals("BJ", Texts.initials("Ben J.Lee", array));
		assertEquals("BJ.L", Texts.initials(" Ben   John  . Lee", array));
		assertEquals("KO", Texts.initials("Kay O'Murphy", array));
		assertEquals("iah1", Texts.initials("i am here 123", array));

		array = " .".toCharArray();
		assertEquals(null, Texts.initials(null, array));
		assertEquals("", Texts.initials("", array));
		assertEquals("", Texts.initials("  ", array));
		assertEquals("I", Texts.initials("I", array));
		assertEquals("i", Texts.initials("i", array));
		assertEquals("S", Texts.initials("SJC", array));
		assertEquals("BJL", Texts.initials("Ben John Lee", array));
		assertEquals("BJL", Texts.initials("Ben J.Lee", array));
		assertEquals("BJL", Texts.initials(" Ben   John  . Lee", array));
		assertEquals("KO", Texts.initials("Kay O'Murphy", array));
		assertEquals("iah1", Texts.initials("i am here 123", array));

		array = " .'".toCharArray();
		assertEquals(null, Texts.initials(null, array));
		assertEquals("", Texts.initials("", array));
		assertEquals("", Texts.initials("  ", array));
		assertEquals("I", Texts.initials("I", array));
		assertEquals("i", Texts.initials("i", array));
		assertEquals("S", Texts.initials("SJC", array));
		assertEquals("BJL", Texts.initials("Ben John Lee", array));
		assertEquals("BJL", Texts.initials("Ben J.Lee", array));
		assertEquals("BJL", Texts.initials(" Ben   John  . Lee", array));
		assertEquals("KOM", Texts.initials("Kay O'Murphy", array));
		assertEquals("iah1", Texts.initials("i am here 123", array));

		array = "SIJo1".toCharArray();
		assertEquals(null, Texts.initials(null, array));
		assertEquals("", Texts.initials("", array));
		assertEquals(" ", Texts.initials("  ", array));
		assertEquals("", Texts.initials("I", array));
		assertEquals("i", Texts.initials("i", array));
		assertEquals("C", Texts.initials("SJC", array));
		assertEquals("Bh", Texts.initials("Ben John Lee", array));
		assertEquals("B.", Texts.initials("Ben J.Lee", array));
		assertEquals(" h", Texts.initials(" Ben   John  . Lee", array));
		assertEquals("K", Texts.initials("Kay O'Murphy", array));
		assertEquals("i2", Texts.initials("i am here 123", array));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSwapCase_String() {
		assertEquals(null, Texts.swapCase(null));
		assertEquals("", Texts.swapCase(""));
		assertEquals("  ", Texts.swapCase("  "));

		assertEquals("i", Texts.swapCase("I"));
		assertEquals("I", Texts.swapCase("i"));
		assertEquals("I AM HERE 123", Texts.swapCase("i am here 123"));
		assertEquals("i aM hERE 123", Texts.swapCase("I Am Here 123"));
		assertEquals("I AM here 123", Texts.swapCase("i am HERE 123"));
		assertEquals("i am here 123", Texts.swapCase("I AM HERE 123"));

		final String test = "This String contains a TitleCase character: \u01C8";
		final String expect = "tHIS sTRING CONTAINS A tITLEcASE CHARACTER: \u01C9";
		assertEquals(expect, Texts.swapCase(test));
	}

	// -----------------------------------------------------------------------
	/**
	 * test method: transform
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testTransform() throws Exception {
		Map<String, String> m = new HashMap<String, String>();
		m.put("a", "1");
		m.put("a.b", "2");
		m.put("a.b.c", "3");
		
		assertEquals("1.23-${8}-xx", Texts.translate("${a}.${a.b}${a.b.c}-${8}-xx", m));
	}

	// -----------------------------------------------------------------------
	/**
	 * test method: camelWord
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testCamelWord() throws Exception {
		assertEquals(null, Texts.camelWord(null, '-'));
		assertEquals("", Texts.camelWord("", '-'));
		assertEquals("helloWorld", Texts.camelWord("hello-world", '-'));
		assertEquals("helloWorld", Texts.camelWord("hello--world", '-'));
		assertEquals("HelloWorld", Texts.camelWord("-hello-world", '-'));
		assertEquals("HelloWorld", Texts.camelWord("--hello-world", '-'));
		assertEquals("helloWorld", Texts.camelWord("hello-world-", '-'));
		assertEquals("helloWorld", Texts.camelWord("hello-world--", '-'));
	}
	
	/**
	 * test method: uncamelWord
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testuncamelWord() throws Exception {
		assertEquals(null, Texts.uncamelWord(null, '-'));
		assertEquals("", Texts.uncamelWord("", '-'));
		assertEquals("hello-world", Texts.uncamelWord("helloWorld", '-'));
		assertEquals("hello-w-orld", Texts.uncamelWord("helloWOrld", '-'));
		assertEquals("hello-world", Texts.uncamelWord("hello-world", '-'));
		assertEquals("hello-worl-d", Texts.uncamelWord("helloWorlD", '-'));
	}
	
	// -----------------------------------------------------------------------
//	/**
//	 * test method: formatFileSize
//	 * @throws Exception if an error occurs
//	 */
//	@Test
//	public void testFormatSize() throws Exception {
//		assertEquals("", Texts.formatFileSize((Integer)null));
//		assertEquals("", Texts.formatFileSize((Long)null));
//		assertEquals("0B", Texts.formatFileSize(0));
//		assertEquals("1023B", Texts.formatFileSize(1023));
//		assertEquals("1KB", Texts.formatFileSize(1024));
//		assertEquals("1023KB", Texts.formatFileSize(1024*1024-1));
//		assertEquals("1MB", Texts.formatFileSize(1024*1024));
//		assertEquals("1023MB", Texts.formatFileSize(1024*1024*1024-1));
//		assertEquals("1GB", Texts.formatFileSize(1024*1024*1024));
//		assertEquals("1023GB", Texts.formatFileSize(1024L*1024*1024*1024-1));
//		assertEquals("1TB", Texts.formatFileSize(1024L*1024*1024*1024));
//		assertEquals("1023TB", Texts.formatFileSize(1024L*1024*1024*1024*1024-1));
//		assertEquals("1PB", Texts.formatFileSize(1024L*1024*1024*1024*1024));
//	}
	
	// -----------------------------------------------------------------------
	/**
	 * test method: ellipsis
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testEllipsis() throws Exception {
		assertEquals(null, Texts.ellipsis(null, 3));
		assertEquals("", Texts.ellipsis("", 3));
		assertEquals("", Texts.ellipsis("", 0));
		assertEquals("123456789", Texts.ellipsis("123456789", 9));
		assertEquals("12345...", Texts.ellipsis("123456789", 8));
		assertEquals("1234...", Texts.ellipsis("123456789", 7));
		assertEquals("123...", Texts.ellipsis("123456789", 6));
		assertEquals("12...", Texts.ellipsis("123456789", 5));
		assertEquals("1...", Texts.ellipsis("123456789", 4));
		assertEquals("...", Texts.ellipsis("123456789", 3));
		assertEquals("..", Texts.ellipsis("123456789", 2));
		assertEquals(".", Texts.ellipsis("123456789", 1));
		assertEquals("", Texts.ellipsis("123456789", 0));
	}
	
	/**
	 * test method: ellipsiz
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testEllipsiz() throws Exception {
		assertEquals(null, Texts.ellipsiz(null, 3));
		assertEquals("", Texts.ellipsiz("", 3));
		assertEquals("", Texts.ellipsiz("", 0));
		assertEquals("あいうえお", Texts.ellipsiz("あいうえお", 10));
		assertEquals("あいう...", Texts.ellipsiz("あいうえお", 9));
		assertEquals("あい...", Texts.ellipsiz("あいうえお", 8));
		assertEquals("あい...", Texts.ellipsiz("あいうえお", 7));
		assertEquals("あ...", Texts.ellipsiz("あいうえお", 6));
		assertEquals("あ...", Texts.ellipsiz("あいうえお", 5));
		assertEquals("あ...", Texts.ellipsiz("あいうえお", 4));
		assertEquals("...", Texts.ellipsiz("あいうえお", 3));
		assertEquals("..", Texts.ellipsiz("あいうえお", 2));
		assertEquals(".", Texts.ellipsiz("あいうえお", 1));
		assertEquals("", Texts.ellipsiz("あいうえお", 0));
	}
	
	// -----------------------------------------------------------------------
	/**
	 * test method: prettifyXml
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testPrettifyXml() throws Exception {
		assertEquals("<a>1</a>\r\n<b>2</b>\r\n", Texts.prettifyXml("<a>1</a><b>2</b>"));
		//assertEquals("<a>\r\n  <b>2</b>\r\n</a>\r\n", Texts.prettifyXml("<a><b>2</b></a>"));
	}
	
}
