package panda.io;

import java.io.File;
import java.util.Locale;

import junit.framework.TestCase;

public class FileNamesWildcardTest extends TestCase {

	private static final boolean WINDOWS = File.separatorChar == '\\';

	public FileNamesWildcardTest(final String name) {
		super(name);
	}

	// -----------------------------------------------------------------------
	// Testing:
	// FileNames.wildcardMatch(String,String)

	public void testMatch() {
		assertFalse(FileNames.wildcardMatch(null, "Foo"));
		assertFalse(FileNames.wildcardMatch("Foo", null));
		assertTrue(FileNames.wildcardMatch(null, null));
		assertTrue(FileNames.wildcardMatch("Foo", "Foo"));
		assertTrue(FileNames.wildcardMatch("", ""));
		assertTrue(FileNames.wildcardMatch("", "*"));
		assertFalse(FileNames.wildcardMatch("", "?"));
		assertTrue(FileNames.wildcardMatch("Foo", "Fo*"));
		assertTrue(FileNames.wildcardMatch("Foo", "Fo?"));
		assertTrue(FileNames.wildcardMatch("Foo Bar and Catflap", "Fo*"));
		assertTrue(FileNames.wildcardMatch("New Bookmarks", "N?w ?o?k??r?s"));
		assertFalse(FileNames.wildcardMatch("Foo", "Bar"));
		assertTrue(FileNames.wildcardMatch("Foo Bar Foo", "F*o Bar*"));
		assertTrue(FileNames.wildcardMatch("Adobe Acrobat Installer", "Ad*er"));
		assertTrue(FileNames.wildcardMatch("Foo", "*Foo"));
		assertTrue(FileNames.wildcardMatch("BarFoo", "*Foo"));
		assertTrue(FileNames.wildcardMatch("Foo", "Foo*"));
		assertTrue(FileNames.wildcardMatch("FooBar", "Foo*"));
		assertFalse(FileNames.wildcardMatch("FOO", "*Foo"));
		assertFalse(FileNames.wildcardMatch("BARFOO", "*Foo"));
		assertFalse(FileNames.wildcardMatch("FOO", "Foo*"));
		assertFalse(FileNames.wildcardMatch("FOOBAR", "Foo*"));
	}

	public void testMatchOnSystem() {
		assertFalse(FileNames.wildcardMatchOnSystem(null, "Foo"));
		assertFalse(FileNames.wildcardMatchOnSystem("Foo", null));
		assertTrue(FileNames.wildcardMatchOnSystem(null, null));
		assertTrue(FileNames.wildcardMatchOnSystem("Foo", "Foo"));
		assertTrue(FileNames.wildcardMatchOnSystem("", ""));
		assertTrue(FileNames.wildcardMatchOnSystem("Foo", "Fo*"));
		assertTrue(FileNames.wildcardMatchOnSystem("Foo", "Fo?"));
		assertTrue(FileNames.wildcardMatchOnSystem("Foo Bar and Catflap", "Fo*"));
		assertTrue(FileNames.wildcardMatchOnSystem("New Bookmarks", "N?w ?o?k??r?s"));
		assertFalse(FileNames.wildcardMatchOnSystem("Foo", "Bar"));
		assertTrue(FileNames.wildcardMatchOnSystem("Foo Bar Foo", "F*o Bar*"));
		assertTrue(FileNames.wildcardMatchOnSystem("Adobe Acrobat Installer", "Ad*er"));
		assertTrue(FileNames.wildcardMatchOnSystem("Foo", "*Foo"));
		assertTrue(FileNames.wildcardMatchOnSystem("BarFoo", "*Foo"));
		assertTrue(FileNames.wildcardMatchOnSystem("Foo", "Foo*"));
		assertTrue(FileNames.wildcardMatchOnSystem("FooBar", "Foo*"));
		assertEquals(WINDOWS, FileNames.wildcardMatchOnSystem("FOO", "*Foo"));
		assertEquals(WINDOWS, FileNames.wildcardMatchOnSystem("BARFOO", "*Foo"));
		assertEquals(WINDOWS, FileNames.wildcardMatchOnSystem("FOO", "Foo*"));
		assertEquals(WINDOWS, FileNames.wildcardMatchOnSystem("FOOBAR", "Foo*"));
	}

	public void testMatchCaseSpecified() {
		assertFalse(FileNames.wildcardMatch(null, "Foo", IOCase.SENSITIVE));
		assertFalse(FileNames.wildcardMatch("Foo", null, IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch(null, null, IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Foo", "Foo", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("", "", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Foo", "Fo*", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Foo", "Fo?", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Foo Bar and Catflap", "Fo*", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("New Bookmarks", "N?w ?o?k??r?s", IOCase.SENSITIVE));
		assertFalse(FileNames.wildcardMatch("Foo", "Bar", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Foo Bar Foo", "F*o Bar*", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Adobe Acrobat Installer", "Ad*er", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Foo", "*Foo", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Foo", "Foo*", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Foo", "*Foo", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("BarFoo", "*Foo", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("Foo", "Foo*", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("FooBar", "Foo*", IOCase.SENSITIVE));

		assertFalse(FileNames.wildcardMatch("FOO", "*Foo", IOCase.SENSITIVE));
		assertFalse(FileNames.wildcardMatch("BARFOO", "*Foo", IOCase.SENSITIVE));
		assertFalse(FileNames.wildcardMatch("FOO", "Foo*", IOCase.SENSITIVE));
		assertFalse(FileNames.wildcardMatch("FOOBAR", "Foo*", IOCase.SENSITIVE));
		assertTrue(FileNames.wildcardMatch("FOO", "*Foo", IOCase.INSENSITIVE));
		assertTrue(FileNames.wildcardMatch("BARFOO", "*Foo", IOCase.INSENSITIVE));
		assertTrue(FileNames.wildcardMatch("FOO", "Foo*", IOCase.INSENSITIVE));
		assertTrue(FileNames.wildcardMatch("FOOBAR", "Foo*", IOCase.INSENSITIVE));
		assertEquals(WINDOWS, FileNames.wildcardMatch("FOO", "*Foo", IOCase.SYSTEM));
		assertEquals(WINDOWS, FileNames.wildcardMatch("BARFOO", "*Foo", IOCase.SYSTEM));
		assertEquals(WINDOWS, FileNames.wildcardMatch("FOO", "Foo*", IOCase.SYSTEM));
		assertEquals(WINDOWS, FileNames.wildcardMatch("FOOBAR", "Foo*", IOCase.SYSTEM));
	}

	public void testSplitOnTokens() {
		assertArrayEquals(new String[] { "Ad", "*", "er" }, FileNames.splitOnTokens("Ad*er"));
		assertArrayEquals(new String[] { "Ad", "?", "er" }, FileNames.splitOnTokens("Ad?er"));
		assertArrayEquals(new String[] { "Test", "*", "?", "One" }, FileNames.splitOnTokens("Test*?One"));
		assertArrayEquals(new String[] { "Test", "?", "*", "One" }, FileNames.splitOnTokens("Test?*One"));
		assertArrayEquals(new String[] { "*" }, FileNames.splitOnTokens("****"));
		assertArrayEquals(new String[] { "*", "?", "?", "*" }, FileNames.splitOnTokens("*??*"));
		assertArrayEquals(new String[] { "*", "?", "*", "?", "*" }, FileNames.splitOnTokens("*?**?*"));
		assertArrayEquals(new String[] { "*", "?", "*", "?", "*" }, FileNames.splitOnTokens("*?***?*"));
		assertArrayEquals(new String[] { "h", "?", "?", "*" }, FileNames.splitOnTokens("h??*"));
		assertArrayEquals(new String[] { "" }, FileNames.splitOnTokens(""));
	}

	private void assertArrayEquals(final Object[] a1, final Object[] a2) {
		assertEquals(a1.length, a2.length);
		for (int i = 0; i < a1.length; i++) {
			assertEquals(a1[i], a2[i]);
		}
	}

	private void assertMatch(final String text, final String wildcard, final boolean expected) {
		assertEquals(text + " " + wildcard, expected, FileNames.wildcardMatch(text, wildcard));
	}

	// A separate set of tests, added to this batch
	public void testMatch2() {
		assertMatch("log.txt", "log.txt", true);
		assertMatch("log.txt1", "log.txt", false);

		assertMatch("log.txt", "log.txt*", true);
		assertMatch("log.txt", "log.txt*1", false);
		assertMatch("log.txt", "*log.txt*", true);

		assertMatch("log.txt", "*.txt", true);
		assertMatch("txt.log", "*.txt", false);
		assertMatch("config.ini", "*.ini", true);

		assertMatch("config.txt.bak", "con*.txt", false);

		assertMatch("log.txt9", "*.txt?", true);
		assertMatch("log.txt", "*.txt?", false);

		assertMatch("progtestcase.java~5~", "*test*.java~*~", true);
		assertMatch("progtestcase.java;5~", "*test*.java~*~", false);
		assertMatch("progtestcase.java~5", "*test*.java~*~", false);

		assertMatch("log.txt", "log.*", true);

		assertMatch("log.txt", "log?*", true);

		assertMatch("log.txt12", "log.txt??", true);

		assertMatch("log.log", "log**log", true);
		assertMatch("log.log", "log**", true);
		assertMatch("log.log", "log.**", true);
		assertMatch("log.log", "**.log", true);
		assertMatch("log.log", "**log", true);

		assertMatch("log.log", "log*log", true);
		assertMatch("log.log", "log*", true);
		assertMatch("log.log", "log.*", true);
		assertMatch("log.log", "*.log", true);
		assertMatch("log.log", "*log", true);

		assertMatch("log.log", "*log?", false);
		assertMatch("log.log", "*log?*", true);
		assertMatch("log.log.abc", "*log?abc", true);
		assertMatch("log.log.abc.log.abc", "*log?abc", true);
		assertMatch("log.log.abc.log.abc.d", "*log?abc?d", true);
	}

	/**
	 * See https://issues.apache.org/jira/browse/IO-246
	 */
	public void test_IO_246() {

		// Tests for "*?"
		assertMatch("aaa", "aa*?", true);
		// these ought to work as well, but "*?" does not work properly at present
		// assertMatch("aaa", "a*?", true);
		// assertMatch("aaa", "*?", true);

		// Tests for "?*"
		assertMatch("", "?*", false);
		assertMatch("a", "a?*", false);
		assertMatch("aa", "aa?*", false);
		assertMatch("a", "?*", true);
		assertMatch("aa", "?*", true);
		assertMatch("aaa", "?*", true);

		// Test ending on "?"
		assertMatch("", "?", false);
		assertMatch("a", "a?", false);
		assertMatch("aa", "aa?", false);
		assertMatch("aab", "aa?", true);
		assertMatch("aaa", "*a", true);
	}

	public void testLocaleIndependence() {
		final Locale orig = Locale.getDefault();

		final Locale[] locales = Locale.getAvailableLocales();

		final String[][] data = { { "I", "i" }, { "i", "I" }, { "i", "\u0130" }, { "i", "\u0131" },
				{ "\u03A3", "\u03C2" }, { "\u03A3", "\u03C3" }, { "\u03C2", "\u03C3" }, };

		try {
			for (int i = 0; i < data.length; i++) {
				for (final Locale locale : locales) {
					Locale.setDefault(locale);
					assertTrue("Test data corrupt: " + i, data[i][0].equalsIgnoreCase(data[i][1]));
					final boolean match = FileNames.wildcardMatch(data[i][0], data[i][1], IOCase.INSENSITIVE);
					assertTrue(Locale.getDefault().toString() + ": " + i, match);
				}
			}
		}
		finally {
			Locale.setDefault(orig);
		}
	}

}
