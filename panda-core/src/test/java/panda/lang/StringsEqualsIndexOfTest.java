package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.hamcrest.core.IsNot;
import org.junit.Test;

/**
 * Unit tests {@link Strings} - Substring methods
 *
 */
public class StringsEqualsIndexOfTest  {
	private static final String BAR = "bar";
	/**
	 * Supplementary character U+20000 See
	 * http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	private static final String CharU20000 = "\uD840\uDC00";
	/**
	 * Supplementary character U+20001 See
	 * http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	private static final String CharU20001 = "\uD840\uDC01";
	/**
	 * Incomplete supplementary character U+20000, high surrogate only. See
	 * http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	private static final String CharUSuppCharHigh = "\uDC00";

	/**
	 * Incomplete supplementary character U+20000, low surrogate only. See
	 * http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	private static final String CharUSuppCharLow = "\uD840";

	private static final String FOO = "foo";

	private static final String FOOBAR = "foobar";

	private static final String[] FOOBAR_SUB_ARRAY = new String[] { "ob", "ba" };

	@Test
	public void testContains_Char() {
		assertFalse(Strings.contains(null, ' '));
		assertFalse(Strings.contains("", ' '));
		assertFalse(Strings.contains("", null));
		assertFalse(Strings.contains(null, null));
		assertTrue(Strings.contains("abc", 'a'));
		assertTrue(Strings.contains("abc", 'b'));
		assertTrue(Strings.contains("abc", 'c'));
		assertFalse(Strings.contains("abc", 'z'));
	}

	@Test
	public void testContains_String() {
		assertFalse(Strings.contains(null, null));
		assertFalse(Strings.contains(null, ""));
		assertFalse(Strings.contains(null, "a"));
		assertFalse(Strings.contains("", null));
		assertTrue(Strings.contains("", ""));
		assertFalse(Strings.contains("", "a"));
		assertTrue(Strings.contains("abc", "a"));
		assertTrue(Strings.contains("abc", "b"));
		assertTrue(Strings.contains("abc", "c"));
		assertTrue(Strings.contains("abc", "abc"));
		assertFalse(Strings.contains("abc", "z"));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContains_StringWithBadSupplementaryChars() {
		// Test edge case: 1/2 of a (broken) supplementary char
		assertFalse(Strings.contains(CharUSuppCharHigh, CharU20001));
		assertFalse(Strings.contains(CharUSuppCharLow, CharU20001));
		assertFalse(Strings.contains(CharU20001, CharUSuppCharHigh));
		assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
		assertTrue(Strings.contains(CharU20001, CharUSuppCharLow));
		assertTrue(Strings.contains(CharU20001 + CharUSuppCharLow + "a", "a"));
		assertTrue(Strings.contains(CharU20001 + CharUSuppCharHigh + "a", "a"));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContains_StringWithSupplementaryChars() {
		assertTrue(Strings.contains(CharU20000 + CharU20001, CharU20000));
		assertTrue(Strings.contains(CharU20000 + CharU20001, CharU20001));
		assertTrue(Strings.contains(CharU20000, CharU20000));
		assertFalse(Strings.contains(CharU20000, CharU20001));
	}

	@Test
	public void testContainsAny_StringCharArray() {
		assertFalse(Strings.containsAny(null, (char[])null));
		assertFalse(Strings.containsAny(null, new char[0]));
		assertFalse(Strings.containsAny(null, new char[] { 'a', 'b' }));

		assertFalse(Strings.containsAny("", (char[])null));
		assertFalse(Strings.containsAny("", new char[0]));
		assertFalse(Strings.containsAny("", new char[] { 'a', 'b' }));

		assertFalse(Strings.containsAny("zzabyycdxx", (char[])null));
		assertFalse(Strings.containsAny("zzabyycdxx", new char[0]));
		assertTrue(Strings.containsAny("zzabyycdxx", new char[] { 'z', 'a' }));
		assertTrue(Strings.containsAny("zzabyycdxx", new char[] { 'b', 'y' }));
		assertFalse(Strings.containsAny("ab", new char[] { 'z' }));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContainsAny_StringCharArrayWithBadSupplementaryChars() {
		// Test edge case: 1/2 of a (broken) supplementary char
		assertFalse(Strings.containsAny(CharUSuppCharHigh, CharU20001.toCharArray()));
		assertFalse(Strings.containsAny("abc" + CharUSuppCharHigh + "xyz", CharU20001.toCharArray()));
		assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
		assertFalse(Strings.containsAny(CharUSuppCharLow, CharU20001.toCharArray()));
		assertFalse(Strings.containsAny(CharU20001, CharUSuppCharHigh.toCharArray()));
		assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
		assertTrue(Strings.containsAny(CharU20001, CharUSuppCharLow.toCharArray()));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContainsAny_StringCharArrayWithSupplementaryChars() {
		assertTrue(Strings.containsAny(CharU20000 + CharU20001, CharU20000.toCharArray()));
		assertTrue(Strings.containsAny("a" + CharU20000 + CharU20001, "a".toCharArray()));
		assertTrue(Strings.containsAny(CharU20000 + "a" + CharU20001, "a".toCharArray()));
		assertTrue(Strings.containsAny(CharU20000 + CharU20001 + "a", "a".toCharArray()));
		assertTrue(Strings.containsAny(CharU20000 + CharU20001, CharU20001.toCharArray()));
		assertTrue(Strings.containsAny(CharU20000, CharU20000.toCharArray()));
		// Sanity check:
		assertEquals(-1, CharU20000.indexOf(CharU20001));
		assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
		assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
		// Test:
		assertFalse(Strings.containsAny(CharU20000, CharU20001.toCharArray()));
		assertFalse(Strings.containsAny(CharU20001, CharU20000.toCharArray()));
	}

	@Test
	public void testContainsAny_StringString() {
		assertFalse(Strings.containsAny(null, (String)null));
		assertFalse(Strings.containsAny(null, ""));
		assertFalse(Strings.containsAny(null, "ab"));

		assertFalse(Strings.containsAny("", (String)null));
		assertFalse(Strings.containsAny("", ""));
		assertFalse(Strings.containsAny("", "ab"));

		assertFalse(Strings.containsAny("zzabyycdxx", (String)null));
		assertFalse(Strings.containsAny("zzabyycdxx", ""));
		assertTrue(Strings.containsAny("zzabyycdxx", "za"));
		assertTrue(Strings.containsAny("zzabyycdxx", "by"));
		assertFalse(Strings.containsAny("ab", "z"));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContainsAny_StringWithBadSupplementaryChars() {
		// Test edge case: 1/2 of a (broken) supplementary char
		assertFalse(Strings.containsAny(CharUSuppCharHigh, CharU20001));
		assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
		assertFalse(Strings.containsAny(CharUSuppCharLow, CharU20001));
		assertFalse(Strings.containsAny(CharU20001, CharUSuppCharHigh));
		assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
		assertTrue(Strings.containsAny(CharU20001, CharUSuppCharLow));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContainsAny_StringWithSupplementaryChars() {
		assertTrue(Strings.containsAny(CharU20000 + CharU20001, CharU20000));
		assertTrue(Strings.containsAny(CharU20000 + CharU20001, CharU20001));
		assertTrue(Strings.containsAny(CharU20000, CharU20000));
		// Sanity check:
		assertEquals(-1, CharU20000.indexOf(CharU20001));
		assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
		assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
		// Test:
		assertFalse(Strings.containsAny(CharU20000, CharU20001));
		assertFalse(Strings.containsAny(CharU20001, CharU20000));
	}

	@Test
	public void testContainsIgnoreCase_LocaleIndependence() {
		final Locale orig = Locale.getDefault();

		final Locale[] locales = { Locale.ENGLISH, new Locale("tr"), Locale.getDefault() };

		final String[][] tdata = { { "i", "I" }, { "I", "i" }, { "\u03C2", "\u03C3" }, { "\u03A3", "\u03C2" },
				{ "\u03A3", "\u03C3" }, };

		final String[][] fdata = { { "\u00DF", "SS" }, };

		try {
			for (final Locale locale : locales) {
				Locale.setDefault(locale);
				for (int j = 0; j < tdata.length; j++) {
					assertTrue(Locale.getDefault() + ": " + j + " " + tdata[j][0] + " " + tdata[j][1],
						Strings.containsIgnoreCase(tdata[j][0], tdata[j][1]));
				}
				for (int j = 0; j < fdata.length; j++) {
					assertFalse(Locale.getDefault() + ": " + j + " " + fdata[j][0] + " " + fdata[j][1],
						Strings.containsIgnoreCase(fdata[j][0], fdata[j][1]));
				}
			}
		}
		finally {
			Locale.setDefault(orig);
		}
	}

	@Test
	public void testContainsIgnoreCase_StringString() {
		assertFalse(Strings.containsIgnoreCase(null, null));

		// Null tests
		assertFalse(Strings.containsIgnoreCase(null, ""));
		assertFalse(Strings.containsIgnoreCase(null, "a"));
		assertFalse(Strings.containsIgnoreCase(null, "abc"));

		assertFalse(Strings.containsIgnoreCase("", null));
		assertFalse(Strings.containsIgnoreCase("a", null));
		assertFalse(Strings.containsIgnoreCase("abc", null));

		// Match len = 0
		assertTrue(Strings.containsIgnoreCase("", ""));
		assertTrue(Strings.containsIgnoreCase("a", ""));
		assertTrue(Strings.containsIgnoreCase("abc", ""));

		// Match len = 1
		assertFalse(Strings.containsIgnoreCase("", "a"));
		assertTrue(Strings.containsIgnoreCase("a", "a"));
		assertTrue(Strings.containsIgnoreCase("abc", "a"));
		assertFalse(Strings.containsIgnoreCase("", "A"));
		assertTrue(Strings.containsIgnoreCase("a", "A"));
		assertTrue(Strings.containsIgnoreCase("abc", "A"));

		// Match len > 1
		assertFalse(Strings.containsIgnoreCase("", "abc"));
		assertFalse(Strings.containsIgnoreCase("a", "abc"));
		assertTrue(Strings.containsIgnoreCase("xabcz", "abc"));
		assertFalse(Strings.containsIgnoreCase("", "ABC"));
		assertFalse(Strings.containsIgnoreCase("a", "ABC"));
		assertTrue(Strings.containsIgnoreCase("xabcz", "ABC"));
	}

	@Test
	public void testContainsNone_CharArray() {
		final String str1 = "a";
		final String str2 = "b";
		final String str3 = "ab.";
		final char[] chars1 = { 'b' };
		final char[] chars2 = { '.' };
		final char[] chars3 = { 'c', 'd' };
		final char[] emptyChars = new char[0];
		assertTrue(Strings.containsNone(null, (char[])null));
		assertTrue(Strings.containsNone("", (char[])null));
		assertTrue(Strings.containsNone(null, emptyChars));
		assertTrue(Strings.containsNone(str1, emptyChars));
		assertTrue(Strings.containsNone("", emptyChars));
		assertTrue(Strings.containsNone("", chars1));
		assertTrue(Strings.containsNone(str1, chars1));
		assertTrue(Strings.containsNone(str1, chars2));
		assertTrue(Strings.containsNone(str1, chars3));
		assertFalse(Strings.containsNone(str2, chars1));
		assertTrue(Strings.containsNone(str2, chars2));
		assertTrue(Strings.containsNone(str2, chars3));
		assertFalse(Strings.containsNone(str3, chars1));
		assertFalse(Strings.containsNone(str3, chars2));
		assertTrue(Strings.containsNone(str3, chars3));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContainsNone_CharArrayWithBadSupplementaryChars() {
		// Test edge case: 1/2 of a (broken) supplementary char
		assertTrue(Strings.containsNone(CharUSuppCharHigh, CharU20001.toCharArray()));
		assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
		assertTrue(Strings.containsNone(CharUSuppCharLow, CharU20001.toCharArray()));
		assertEquals(-1, CharU20001.indexOf(CharUSuppCharHigh));
		assertTrue(Strings.containsNone(CharU20001, CharUSuppCharHigh.toCharArray()));
		assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
		assertFalse(Strings.containsNone(CharU20001, CharUSuppCharLow.toCharArray()));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContainsNone_CharArrayWithSupplementaryChars() {
		assertFalse(Strings.containsNone(CharU20000 + CharU20001, CharU20000.toCharArray()));
		assertFalse(Strings.containsNone(CharU20000 + CharU20001, CharU20001.toCharArray()));
		assertFalse(Strings.containsNone(CharU20000, CharU20000.toCharArray()));
		// Sanity check:
		assertEquals(-1, CharU20000.indexOf(CharU20001));
		assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
		assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
		// Test:
		assertTrue(Strings.containsNone(CharU20000, CharU20001.toCharArray()));
		assertTrue(Strings.containsNone(CharU20001, CharU20000.toCharArray()));
	}

	@Test
	public void testContainsNone_String() {
		final String str1 = "a";
		final String str2 = "b";
		final String str3 = "ab.";
		final String chars1 = "b";
		final String chars2 = ".";
		final String chars3 = "cd";
		assertTrue(Strings.containsNone(null, (String)null));
		assertTrue(Strings.containsNone("", (String)null));
		assertTrue(Strings.containsNone(null, ""));
		assertTrue(Strings.containsNone(str1, ""));
		assertTrue(Strings.containsNone("", ""));
		assertTrue(Strings.containsNone("", chars1));
		assertTrue(Strings.containsNone(str1, chars1));
		assertTrue(Strings.containsNone(str1, chars2));
		assertTrue(Strings.containsNone(str1, chars3));
		assertFalse(Strings.containsNone(str2, chars1));
		assertTrue(Strings.containsNone(str2, chars2));
		assertTrue(Strings.containsNone(str2, chars3));
		assertFalse(Strings.containsNone(str3, chars1));
		assertFalse(Strings.containsNone(str3, chars2));
		assertTrue(Strings.containsNone(str3, chars3));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContainsNone_StringWithBadSupplementaryChars() {
		// Test edge case: 1/2 of a (broken) supplementary char
		assertTrue(Strings.containsNone(CharUSuppCharHigh, CharU20001));
		assertEquals(-1, CharUSuppCharLow.indexOf(CharU20001));
		assertTrue(Strings.containsNone(CharUSuppCharLow, CharU20001));
		assertEquals(-1, CharU20001.indexOf(CharUSuppCharHigh));
		assertTrue(Strings.containsNone(CharU20001, CharUSuppCharHigh));
		assertEquals(0, CharU20001.indexOf(CharUSuppCharLow));
		assertFalse(Strings.containsNone(CharU20001, CharUSuppCharLow));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testContainsNone_StringWithSupplementaryChars() {
		assertFalse(Strings.containsNone(CharU20000 + CharU20001, CharU20000));
		assertFalse(Strings.containsNone(CharU20000 + CharU20001, CharU20001));
		assertFalse(Strings.containsNone(CharU20000, CharU20000));
		// Sanity check:
		assertEquals(-1, CharU20000.indexOf(CharU20001));
		assertEquals(0, CharU20000.indexOf(CharU20001.charAt(0)));
		assertEquals(-1, CharU20000.indexOf(CharU20001.charAt(1)));
		// Test:
		assertTrue(Strings.containsNone(CharU20000, CharU20001));
		assertTrue(Strings.containsNone(CharU20001, CharU20000));
	}

	@Test
	public void testContainsOnly_CharArray() {
		final String str1 = "a";
		final String str2 = "b";
		final String str3 = "ab";
		final char[] chars1 = { 'b' };
		final char[] chars2 = { 'a' };
		final char[] chars3 = { 'a', 'b' };
		final char[] emptyChars = new char[0];
		assertFalse(Strings.containsOnly(null, (char[])null));
		assertFalse(Strings.containsOnly("", (char[])null));
		assertFalse(Strings.containsOnly(null, emptyChars));
		assertFalse(Strings.containsOnly(str1, emptyChars));
		assertTrue(Strings.containsOnly("", emptyChars));
		assertTrue(Strings.containsOnly("", chars1));
		assertFalse(Strings.containsOnly(str1, chars1));
		assertTrue(Strings.containsOnly(str1, chars2));
		assertTrue(Strings.containsOnly(str1, chars3));
		assertTrue(Strings.containsOnly(str2, chars1));
		assertFalse(Strings.containsOnly(str2, chars2));
		assertTrue(Strings.containsOnly(str2, chars3));
		assertFalse(Strings.containsOnly(str3, chars1));
		assertFalse(Strings.containsOnly(str3, chars2));
		assertTrue(Strings.containsOnly(str3, chars3));
	}

	@Test
	public void testContainsOnly_String() {
		final String str1 = "a";
		final String str2 = "b";
		final String str3 = "ab";
		final String chars1 = "b";
		final String chars2 = "a";
		final String chars3 = "ab";
		assertFalse(Strings.containsOnly(null, (String)null));
		assertFalse(Strings.containsOnly("", (String)null));
		assertFalse(Strings.containsOnly(null, ""));
		assertFalse(Strings.containsOnly(str1, ""));
		assertTrue(Strings.containsOnly("", ""));
		assertTrue(Strings.containsOnly("", chars1));
		assertFalse(Strings.containsOnly(str1, chars1));
		assertTrue(Strings.containsOnly(str1, chars2));
		assertTrue(Strings.containsOnly(str1, chars3));
		assertTrue(Strings.containsOnly(str2, chars1));
		assertFalse(Strings.containsOnly(str2, chars2));
		assertTrue(Strings.containsOnly(str2, chars3));
		assertFalse(Strings.containsOnly(str3, chars1));
		assertFalse(Strings.containsOnly(str3, chars2));
		assertTrue(Strings.containsOnly(str3, chars3));
	}

	@Test
	public void testContainsWhitespace() {
		assertFalse(Strings.containsWhitespace(""));
		assertTrue(Strings.containsWhitespace(" "));
		assertFalse(Strings.containsWhitespace("a"));
		assertTrue(Strings.containsWhitespace("a "));
		assertTrue(Strings.containsWhitespace(" a"));
		assertTrue(Strings.containsWhitespace("a\t"));
		assertTrue(Strings.containsWhitespace("\n"));
	}

	// The purpose of this class is to test Strings#equals(CharSequence, CharSequence)
	// with a CharSequence implementation whose equals(Object) override requires that the
	// other object be an instance of CustomCharSequence, even though, as char sequences,
	// `seq` may equal the other object.
	private static class CustomCharSequence implements CharSequence {
		private final CharSequence seq;

		public CustomCharSequence(final CharSequence seq) {
			this.seq = seq;
		}

		public char charAt(final int index) {
			return seq.charAt(index);
		}

		public int length() {
			return seq.length();
		}

		public CharSequence subSequence(final int start, final int end) {
			return new CustomCharSequence(seq.subSequence(start, end));
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == null || !(obj instanceof CustomCharSequence)) {
				return false;
			}
			final CustomCharSequence other = (CustomCharSequence)obj;
			return seq.equals(other.seq);
		}

		@Override
		public int hashCode() {
			return seq.hashCode();
		}

		@Override
		public String toString() {
			return seq.toString();
		}
	}

	@Test
	public void testCustomCharSequence() {
		assertThat(new CustomCharSequence(FOO), IsNot.<CharSequence> not(FOO));
		assertThat(FOO, IsNot.<CharSequence> not(new CustomCharSequence(FOO)));
		assertEquals(new CustomCharSequence(FOO), new CustomCharSequence(FOO));
	}

	@Test
	public void testEquals() {
		final CharSequence fooCs = FOO, barCs = BAR, foobarCs = FOOBAR;
		assertTrue(Strings.equals(null, null));
		assertTrue(Strings.equals(fooCs, fooCs));
		assertTrue(Strings.equals(fooCs, new StringBuilder(FOO)));
		assertTrue(Strings.equals(fooCs, new String(new char[] { 'f', 'o', 'o' })));
		assertTrue(Strings.equals(fooCs, new CustomCharSequence(FOO)));
		assertTrue(Strings.equals(new CustomCharSequence(FOO), fooCs));
		assertFalse(Strings.equals(fooCs, new String(new char[] { 'f', 'O', 'O' })));
		assertFalse(Strings.equals(fooCs, barCs));
		assertFalse(Strings.equals(fooCs, null));
		assertFalse(Strings.equals(null, fooCs));
		assertFalse(Strings.equals(fooCs, foobarCs));
		assertFalse(Strings.equals(foobarCs, fooCs));
	}

	@Test
	public void testEqualsOnStrings() {
		assertTrue(Strings.equals(null, null));
		assertTrue(Strings.equals(FOO, FOO));
		assertTrue(Strings.equals(FOO, new String(new char[] { 'f', 'o', 'o' })));
		assertFalse(Strings.equals(FOO, new String(new char[] { 'f', 'O', 'O' })));
		assertFalse(Strings.equals(FOO, BAR));
		assertFalse(Strings.equals(FOO, null));
		assertFalse(Strings.equals(null, FOO));
		assertFalse(Strings.equals(FOO, FOOBAR));
		assertFalse(Strings.equals(FOOBAR, FOO));
	}

	@Test
	public void testEqualsIgnoreCase() {
		assertTrue(Strings.equalsIgnoreCase(null, null));
		assertTrue(Strings.equalsIgnoreCase(FOO, FOO));
		assertTrue(Strings.equalsIgnoreCase(FOO, new String(new char[] { 'f', 'o', 'o' })));
		assertTrue(Strings.equalsIgnoreCase(FOO, new String(new char[] { 'f', 'O', 'O' })));
		assertFalse(Strings.equalsIgnoreCase(FOO, BAR));
		assertFalse(Strings.equalsIgnoreCase(FOO, null));
		assertFalse(Strings.equalsIgnoreCase(null, FOO));
		assertTrue(Strings.equalsIgnoreCase("", ""));
		assertFalse(Strings.equalsIgnoreCase("abcd", "abcd "));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIndexOf_char() {
		assertEquals(-1, Strings.indexOf(null, ' '));
		assertEquals(-1, Strings.indexOf("", ' '));
		assertEquals(0, Strings.indexOf("aabaabaa", 'a'));
		assertEquals(2, Strings.indexOf("aabaabaa", 'b'));

		assertEquals(2, Strings.indexOf(new StringBuilder("aabaabaa"), 'b'));
	}

	@Test
	public void testIndexOf_charInt() {
		assertEquals(-1, Strings.indexOf(null, ' ', 0));
		assertEquals(-1, Strings.indexOf(null, ' ', -1));
		assertEquals(-1, Strings.indexOf("", ' ', 0));
		assertEquals(-1, Strings.indexOf("", ' ', -1));
		assertEquals(0, Strings.indexOf("aabaabaa", 'a', 0));
		assertEquals(2, Strings.indexOf("aabaabaa", 'b', 0));
		assertEquals(5, Strings.indexOf("aabaabaa", 'b', 3));
		assertEquals(-1, Strings.indexOf("aabaabaa", 'b', 9));
		assertEquals(2, Strings.indexOf("aabaabaa", 'b', -1));

		assertEquals(5, Strings.indexOf(new StringBuilder("aabaabaa"), 'b', 3));
	}

	@Test
	public void testIndexOf_String() {
		assertEquals(-1, Strings.indexOf(null, null));
		assertEquals(-1, Strings.indexOf("", null));
		assertEquals(0, Strings.indexOf("", ""));
		assertEquals(0, Strings.indexOf("aabaabaa", "a"));
		assertEquals(2, Strings.indexOf("aabaabaa", "b"));
		assertEquals(1, Strings.indexOf("aabaabaa", "ab"));
		assertEquals(0, Strings.indexOf("aabaabaa", ""));

		assertEquals(2, Strings.indexOf(new StringBuilder("aabaabaa"), "b"));
	}

	@Test
	public void testIndexOf_StringInt() {
		assertEquals(-1, Strings.indexOf(null, null, 0));
		assertEquals(-1, Strings.indexOf(null, null, -1));
		assertEquals(-1, Strings.indexOf(null, "", 0));
		assertEquals(-1, Strings.indexOf(null, "", -1));
		assertEquals(-1, Strings.indexOf("", null, 0));
		assertEquals(-1, Strings.indexOf("", null, -1));
		assertEquals(0, Strings.indexOf("", "", 0));
		assertEquals(0, Strings.indexOf("", "", -1));
		assertEquals(0, Strings.indexOf("", "", 9));
		assertEquals(0, Strings.indexOf("abc", "", 0));
		assertEquals(0, Strings.indexOf("abc", "", -1));
		assertEquals(3, Strings.indexOf("abc", "", 9));
		assertEquals(3, Strings.indexOf("abc", "", 3));
		assertEquals(0, Strings.indexOf("aabaabaa", "a", 0));
		assertEquals(2, Strings.indexOf("aabaabaa", "b", 0));
		assertEquals(1, Strings.indexOf("aabaabaa", "ab", 0));
		assertEquals(5, Strings.indexOf("aabaabaa", "b", 3));
		assertEquals(-1, Strings.indexOf("aabaabaa", "b", 9));
		assertEquals(2, Strings.indexOf("aabaabaa", "b", -1));
		assertEquals(2, Strings.indexOf("aabaabaa", "", 2));

		// Test that startIndex works correctly, i.e. cannot match before startIndex
		assertEquals(7, Strings.indexOf("12345678", "8", 5));
		assertEquals(7, Strings.indexOf("12345678", "8", 6));
		assertEquals(7, Strings.indexOf("12345678", "8", 7)); // 7 is last index
		assertEquals(-1, Strings.indexOf("12345678", "8", 8));

		assertEquals(5, Strings.indexOf(new StringBuilder("aabaabaa"), "b", 3));
	}

	@Test
	public void testIndexOfAny_StringCharArray() {
		assertEquals(-1, Strings.indexOfAny(null, (char[])null));
		assertEquals(-1, Strings.indexOfAny(null, new char[0]));
		assertEquals(-1, Strings.indexOfAny(null, new char[] { 'a', 'b' }));

		assertEquals(-1, Strings.indexOfAny("", (char[])null));
		assertEquals(-1, Strings.indexOfAny("", new char[0]));
		assertEquals(-1, Strings.indexOfAny("", new char[] { 'a', 'b' }));

		assertEquals(-1, Strings.indexOfAny("zzabyycdxx", (char[])null));
		assertEquals(-1, Strings.indexOfAny("zzabyycdxx", new char[0]));
		assertEquals(0, Strings.indexOfAny("zzabyycdxx", new char[] { 'z', 'a' }));
		assertEquals(3, Strings.indexOfAny("zzabyycdxx", new char[] { 'b', 'y' }));
		assertEquals(-1, Strings.indexOfAny("ab", new char[] { 'z' }));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testIndexOfAny_StringCharArrayWithSupplementaryChars() {
		assertEquals(0, Strings.indexOfAny(CharU20000 + CharU20001, CharU20000.toCharArray()));
		assertEquals(2, Strings.indexOfAny(CharU20000 + CharU20001, CharU20001.toCharArray()));
		assertEquals(0, Strings.indexOfAny(CharU20000, CharU20000.toCharArray()));
		assertEquals(-1, Strings.indexOfAny(CharU20000, CharU20001.toCharArray()));
	}

	@Test
	public void testIndexOfAny_StringString() {
		assertEquals(-1, Strings.indexOfAny(null, (String)null));
		assertEquals(-1, Strings.indexOfAny(null, ""));
		assertEquals(-1, Strings.indexOfAny(null, "ab"));

		assertEquals(-1, Strings.indexOfAny("", (String)null));
		assertEquals(-1, Strings.indexOfAny("", ""));
		assertEquals(-1, Strings.indexOfAny("", "ab"));

		assertEquals(-1, Strings.indexOfAny("zzabyycdxx", (String)null));
		assertEquals(-1, Strings.indexOfAny("zzabyycdxx", ""));
		assertEquals(0, Strings.indexOfAny("zzabyycdxx", "za"));
		assertEquals(3, Strings.indexOfAny("zzabyycdxx", "by"));
		assertEquals(-1, Strings.indexOfAny("ab", "z"));
	}

	@Test
	public void testIndexOfAny_StringStringArray() {
		assertEquals(-1, Strings.indexOfAny(null, (String[])null));
		assertEquals(-1, Strings.indexOfAny(null, FOOBAR_SUB_ARRAY));
		assertEquals(-1, Strings.indexOfAny(FOOBAR, (String[])null));
		assertEquals(2, Strings.indexOfAny(FOOBAR, FOOBAR_SUB_ARRAY));
		assertEquals(-1, Strings.indexOfAny(FOOBAR, new String[0]));
		assertEquals(-1, Strings.indexOfAny(null, new String[0]));
		assertEquals(-1, Strings.indexOfAny("", new String[0]));
		assertEquals(-1, Strings.indexOfAny(FOOBAR, new String[] { "llll" }));
		assertEquals(0, Strings.indexOfAny(FOOBAR, new String[] { "" }));
		assertEquals(0, Strings.indexOfAny("", new String[] { "" }));
		assertEquals(-1, Strings.indexOfAny("", new String[] { "a" }));
		assertEquals(-1, Strings.indexOfAny("", new String[] { null }));
		assertEquals(-1, Strings.indexOfAny(FOOBAR, new String[] { null }));
		assertEquals(-1, Strings.indexOfAny(null, new String[] { null }));
	}

	/**
	 * See http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html
	 */
	@Test
	public void testIndexOfAny_StringStringWithSupplementaryChars() {
		assertEquals(0, Strings.indexOfAny(CharU20000 + CharU20001, CharU20000));
		assertEquals(2, Strings.indexOfAny(CharU20000 + CharU20001, CharU20001));
		assertEquals(0, Strings.indexOfAny(CharU20000, CharU20000));
		assertEquals(-1, Strings.indexOfAny(CharU20000, CharU20001));
	}

	@Test
	public void testIndexOfAnyBut_StringCharArray() {
		assertEquals(-1, Strings.indexOfAnyBut(null, (char[])null));
		assertEquals(-1, Strings.indexOfAnyBut(null, new char[0]));
		assertEquals(-1, Strings.indexOfAnyBut(null, new char[] { 'a', 'b' }));

		assertEquals(-1, Strings.indexOfAnyBut("", (char[])null));
		assertEquals(-1, Strings.indexOfAnyBut("", new char[0]));
		assertEquals(-1, Strings.indexOfAnyBut("", new char[] { 'a', 'b' }));

		assertEquals(-1, Strings.indexOfAnyBut("zzabyycdxx", (char[])null));
		assertEquals(-1, Strings.indexOfAnyBut("zzabyycdxx", new char[0]));
		assertEquals(3, Strings.indexOfAnyBut("zzabyycdxx", new char[] { 'z', 'a' }));
		assertEquals(0, Strings.indexOfAnyBut("zzabyycdxx", new char[] { 'b', 'y' }));
		assertEquals(-1, Strings.indexOfAnyBut("aba", new char[] { 'a', 'b' }));
		assertEquals(0, Strings.indexOfAnyBut("aba", new char[] { 'z' }));

	}

	@Test
	public void testIndexOfAnyBut_StringCharArrayWithSupplementaryChars() {
		assertEquals(2, Strings.indexOfAnyBut(CharU20000 + CharU20001, CharU20000.toCharArray()));
		assertEquals(0, Strings.indexOfAnyBut(CharU20000 + CharU20001, CharU20001.toCharArray()));
		assertEquals(-1, Strings.indexOfAnyBut(CharU20000, CharU20000.toCharArray()));
		assertEquals(0, Strings.indexOfAnyBut(CharU20000, CharU20001.toCharArray()));
	}

	@Test
	public void testIndexOfAnyBut_StringString() {
		assertEquals(-1, Strings.indexOfAnyBut(null, (String)null));
		assertEquals(-1, Strings.indexOfAnyBut(null, ""));
		assertEquals(-1, Strings.indexOfAnyBut(null, "ab"));

		assertEquals(-1, Strings.indexOfAnyBut("", (String)null));
		assertEquals(-1, Strings.indexOfAnyBut("", ""));
		assertEquals(-1, Strings.indexOfAnyBut("", "ab"));

		assertEquals(-1, Strings.indexOfAnyBut("zzabyycdxx", (String)null));
		assertEquals(-1, Strings.indexOfAnyBut("zzabyycdxx", ""));
		assertEquals(3, Strings.indexOfAnyBut("zzabyycdxx", "za"));
		assertEquals(0, Strings.indexOfAnyBut("zzabyycdxx", "by"));
		assertEquals(0, Strings.indexOfAnyBut("ab", "z"));
	}

	@Test
	public void testIndexOfAnyBut_StringStringWithSupplementaryChars() {
		assertEquals(2, Strings.indexOfAnyBut(CharU20000 + CharU20001, CharU20000));
		assertEquals(0, Strings.indexOfAnyBut(CharU20000 + CharU20001, CharU20001));
		assertEquals(-1, Strings.indexOfAnyBut(CharU20000, CharU20000));
		assertEquals(0, Strings.indexOfAnyBut(CharU20000, CharU20001));
	}

	@Test
	public void testIndexOfIgnoreCase_String() {
		assertEquals(-1, Strings.indexOfIgnoreCase(null, null));
		assertEquals(-1, Strings.indexOfIgnoreCase(null, ""));
		assertEquals(-1, Strings.indexOfIgnoreCase("", null));
		assertEquals(0, Strings.indexOfIgnoreCase("", ""));
		assertEquals(0, Strings.indexOfIgnoreCase("aabaabaa", "a"));
		assertEquals(0, Strings.indexOfIgnoreCase("aabaabaa", "A"));
		assertEquals(2, Strings.indexOfIgnoreCase("aabaabaa", "b"));
		assertEquals(2, Strings.indexOfIgnoreCase("aabaabaa", "B"));
		assertEquals(1, Strings.indexOfIgnoreCase("aabaabaa", "ab"));
		assertEquals(1, Strings.indexOfIgnoreCase("aabaabaa", "AB"));
		assertEquals(0, Strings.indexOfIgnoreCase("aabaabaa", ""));
	}

	@Test
	public void testIndexOfIgnoreCase_StringInt() {
		assertEquals(1, Strings.indexOfIgnoreCase("aabaabaa", "AB", -1));
		assertEquals(1, Strings.indexOfIgnoreCase("aabaabaa", "AB", 0));
		assertEquals(1, Strings.indexOfIgnoreCase("aabaabaa", "AB", 1));
		assertEquals(4, Strings.indexOfIgnoreCase("aabaabaa", "AB", 2));
		assertEquals(4, Strings.indexOfIgnoreCase("aabaabaa", "AB", 3));
		assertEquals(4, Strings.indexOfIgnoreCase("aabaabaa", "AB", 4));
		assertEquals(-1, Strings.indexOfIgnoreCase("aabaabaa", "AB", 5));
		assertEquals(-1, Strings.indexOfIgnoreCase("aabaabaa", "AB", 6));
		assertEquals(-1, Strings.indexOfIgnoreCase("aabaabaa", "AB", 7));
		assertEquals(-1, Strings.indexOfIgnoreCase("aabaabaa", "AB", 8));
		assertEquals(1, Strings.indexOfIgnoreCase("aab", "AB", 1));
		assertEquals(5, Strings.indexOfIgnoreCase("aabaabaa", "", 5));
		assertEquals(-1, Strings.indexOfIgnoreCase("ab", "AAB", 0));
		assertEquals(-1, Strings.indexOfIgnoreCase("aab", "AAB", 1));
	}

	@Test
	public void testLastIndexOf_char() {
		assertEquals(-1, Strings.lastIndexOf(null, ' '));
		assertEquals(-1, Strings.lastIndexOf("", ' '));
		assertEquals(7, Strings.lastIndexOf("aabaabaa", 'a'));
		assertEquals(5, Strings.lastIndexOf("aabaabaa", 'b'));

		assertEquals(5, Strings.lastIndexOf(new StringBuilder("aabaabaa"), 'b'));
	}

	@Test
	public void testLastIndexOf_charInt() {
		assertEquals(-1, Strings.lastIndexOf(null, ' ', 0));
		assertEquals(-1, Strings.lastIndexOf(null, ' ', -1));
		assertEquals(-1, Strings.lastIndexOf("", ' ', 0));
		assertEquals(-1, Strings.lastIndexOf("", ' ', -1));
		assertEquals(7, Strings.lastIndexOf("aabaabaa", 'a', 8));
		assertEquals(5, Strings.lastIndexOf("aabaabaa", 'b', 8));
		assertEquals(2, Strings.lastIndexOf("aabaabaa", 'b', 3));
		assertEquals(5, Strings.lastIndexOf("aabaabaa", 'b', 9));
		assertEquals(-1, Strings.lastIndexOf("aabaabaa", 'b', -1));
		assertEquals(0, Strings.lastIndexOf("aabaabaa", 'a', 0));

		assertEquals(2, Strings.lastIndexOf(new StringBuilder("aabaabaa"), 'b', 2));
	}

	@Test
	public void testLastIndexOf_String() {
		assertEquals(-1, Strings.lastIndexOf(null, null));
		assertEquals(-1, Strings.lastIndexOf("", null));
		assertEquals(-1, Strings.lastIndexOf("", "a"));
		assertEquals(0, Strings.lastIndexOf("", ""));
		assertEquals(8, Strings.lastIndexOf("aabaabaa", ""));
		assertEquals(7, Strings.lastIndexOf("aabaabaa", "a"));
		assertEquals(5, Strings.lastIndexOf("aabaabaa", "b"));
		assertEquals(4, Strings.lastIndexOf("aabaabaa", "ab"));

		assertEquals(4, Strings.lastIndexOf(new StringBuilder("aabaabaa"), "ab"));
	}

	@Test
	public void testLastIndexOf_StringInt() {
		assertEquals(-1, Strings.lastIndexOf(null, null, 0));
		assertEquals(-1, Strings.lastIndexOf(null, null, -1));
		assertEquals(-1, Strings.lastIndexOf(null, "", 0));
		assertEquals(-1, Strings.lastIndexOf(null, "", -1));
		assertEquals(-1, Strings.lastIndexOf("", null, 0));
		assertEquals(-1, Strings.lastIndexOf("", null, -1));
		assertEquals(0, Strings.lastIndexOf("", "", 0));
		assertEquals(-1, Strings.lastIndexOf("", "", -1));
		assertEquals(0, Strings.lastIndexOf("", "", 9));
		assertEquals(0, Strings.lastIndexOf("abc", "", 0));
		assertEquals(-1, Strings.lastIndexOf("abc", "", -1));
		assertEquals(3, Strings.lastIndexOf("abc", "", 9));
		assertEquals(7, Strings.lastIndexOf("aabaabaa", "a", 8));
		assertEquals(5, Strings.lastIndexOf("aabaabaa", "b", 8));
		assertEquals(4, Strings.lastIndexOf("aabaabaa", "ab", 8));
		assertEquals(2, Strings.lastIndexOf("aabaabaa", "b", 3));
		assertEquals(5, Strings.lastIndexOf("aabaabaa", "b", 9));
		assertEquals(-1, Strings.lastIndexOf("aabaabaa", "b", -1));
		assertEquals(-1, Strings.lastIndexOf("aabaabaa", "b", 0));
		assertEquals(0, Strings.lastIndexOf("aabaabaa", "a", 0));
		assertEquals(-1, Strings.lastIndexOf("aabaabaa", "a", -1));

		// Test that fromIndex works correctly, i.e. cannot match after fromIndex
		assertEquals(7, Strings.lastIndexOf("12345678", "8", 9));
		assertEquals(7, Strings.lastIndexOf("12345678", "8", 8));
		assertEquals(7, Strings.lastIndexOf("12345678", "8", 7)); // 7 is last index
		assertEquals(-1, Strings.lastIndexOf("12345678", "8", 6));

		assertEquals(-1, Strings.lastIndexOf("aabaabaa", "b", 1));
		assertEquals(2, Strings.lastIndexOf("aabaabaa", "b", 2));
		assertEquals(2, Strings.lastIndexOf("aabaabaa", "ba", 2));
		assertEquals(2, Strings.lastIndexOf("aabaabaa", "ba", 3));

		assertEquals(2, Strings.lastIndexOf(new StringBuilder("aabaabaa"), "b", 3));
	}

	@Test
	public void testLastIndexOfAny_StringStringArray() {
		assertEquals(-1, Strings.lastIndexOfAny(null, (CharSequence)null)); // test both types of
																			// ...
		assertEquals(-1, Strings.lastIndexOfAny(null, (CharSequence[])null)); // ... varargs
																				// invocation
		assertEquals(-1, Strings.lastIndexOfAny(null)); // Missing varag
		assertEquals(-1, Strings.lastIndexOfAny(null, FOOBAR_SUB_ARRAY));
		assertEquals(-1, Strings.lastIndexOfAny(FOOBAR, (CharSequence)null)); // test both types of
																				// ...
		assertEquals(-1, Strings.lastIndexOfAny(FOOBAR, (CharSequence[])null)); // ... varargs
																				// invocation
		assertEquals(-1, Strings.lastIndexOfAny(FOOBAR)); // Missing vararg
		assertEquals(3, Strings.lastIndexOfAny(FOOBAR, FOOBAR_SUB_ARRAY));
		assertEquals(-1, Strings.lastIndexOfAny(FOOBAR, new String[0]));
		assertEquals(-1, Strings.lastIndexOfAny(null, new String[0]));
		assertEquals(-1, Strings.lastIndexOfAny("", new String[0]));
		assertEquals(-1, Strings.lastIndexOfAny(FOOBAR, new String[] { "llll" }));
		assertEquals(6, Strings.lastIndexOfAny(FOOBAR, new String[] { "" }));
		assertEquals(0, Strings.lastIndexOfAny("", new String[] { "" }));
		assertEquals(-1, Strings.lastIndexOfAny("", new String[] { "a" }));
		assertEquals(-1, Strings.lastIndexOfAny("", new String[] { null }));
		assertEquals(-1, Strings.lastIndexOfAny(FOOBAR, new String[] { null }));
		assertEquals(-1, Strings.lastIndexOfAny(null, new String[] { null }));
	}

	@Test
	public void testLastIndexOfIgnoreCase_String() {
		assertEquals(-1, Strings.lastIndexOfIgnoreCase(null, null));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase("", null));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase(null, ""));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase("", "a"));
		assertEquals(0, Strings.lastIndexOfIgnoreCase("", ""));
		assertEquals(8, Strings.lastIndexOfIgnoreCase("aabaabaa", ""));
		assertEquals(7, Strings.lastIndexOfIgnoreCase("aabaabaa", "a"));
		assertEquals(7, Strings.lastIndexOfIgnoreCase("aabaabaa", "A"));
		assertEquals(5, Strings.lastIndexOfIgnoreCase("aabaabaa", "b"));
		assertEquals(5, Strings.lastIndexOfIgnoreCase("aabaabaa", "B"));
		assertEquals(4, Strings.lastIndexOfIgnoreCase("aabaabaa", "ab"));
		assertEquals(4, Strings.lastIndexOfIgnoreCase("aabaabaa", "AB"));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase("ab", "AAB"));
		assertEquals(0, Strings.lastIndexOfIgnoreCase("aab", "AAB"));
	}

	@Test
	public void testLastIndexOfIgnoreCase_StringInt() {
		assertEquals(-1, Strings.lastIndexOfIgnoreCase(null, null, 0));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase(null, null, -1));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase(null, "", 0));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase(null, "", -1));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase("", null, 0));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase("", null, -1));
		assertEquals(0, Strings.lastIndexOfIgnoreCase("", "", 0));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase("", "", -1));
		assertEquals(0, Strings.lastIndexOfIgnoreCase("", "", 9));
		assertEquals(0, Strings.lastIndexOfIgnoreCase("abc", "", 0));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase("abc", "", -1));
		assertEquals(3, Strings.lastIndexOfIgnoreCase("abc", "", 9));
		assertEquals(7, Strings.lastIndexOfIgnoreCase("aabaabaa", "A", 8));
		assertEquals(5, Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 8));
		assertEquals(4, Strings.lastIndexOfIgnoreCase("aabaabaa", "AB", 8));
		assertEquals(2, Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 3));
		assertEquals(5, Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 9));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase("aabaabaa", "B", -1));
		assertEquals(-1, Strings.lastIndexOfIgnoreCase("aabaabaa", "B", 0));
		assertEquals(0, Strings.lastIndexOfIgnoreCase("aabaabaa", "A", 0));
		assertEquals(1, Strings.lastIndexOfIgnoreCase("aab", "AB", 1));
	}

	@Test
	public void testLastOrdinalIndexOf() {
		assertEquals(-1, Strings.lastOrdinalIndexOf(null, "*", 42));
		assertEquals(-1, Strings.lastOrdinalIndexOf("*", null, 42));
		assertEquals(0, Strings.lastOrdinalIndexOf("", "", 42));
		assertEquals(7, Strings.lastOrdinalIndexOf("aabaabaa", "a", 1));
		assertEquals(6, Strings.lastOrdinalIndexOf("aabaabaa", "a", 2));
		assertEquals(5, Strings.lastOrdinalIndexOf("aabaabaa", "b", 1));
		assertEquals(2, Strings.lastOrdinalIndexOf("aabaabaa", "b", 2));
		assertEquals(4, Strings.lastOrdinalIndexOf("aabaabaa", "ab", 1));
		assertEquals(1, Strings.lastOrdinalIndexOf("aabaabaa", "ab", 2));
		assertEquals(8, Strings.lastOrdinalIndexOf("aabaabaa", "", 1));
		assertEquals(8, Strings.lastOrdinalIndexOf("aabaabaa", "", 2));
	}

	@Test
	public void testOrdinalIndexOf() {
		assertEquals(-1, Strings.ordinalIndexOf(null, null, Integer.MIN_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("", null, Integer.MIN_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("", "", Integer.MIN_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "a", Integer.MIN_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "b", Integer.MIN_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "ab", Integer.MIN_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "", Integer.MIN_VALUE));

		assertEquals(-1, Strings.ordinalIndexOf(null, null, -1));
		assertEquals(-1, Strings.ordinalIndexOf("", null, -1));
		assertEquals(-1, Strings.ordinalIndexOf("", "", -1));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "a", -1));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "b", -1));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "ab", -1));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "", -1));

		assertEquals(-1, Strings.ordinalIndexOf(null, null, 0));
		assertEquals(-1, Strings.ordinalIndexOf("", null, 0));
		assertEquals(-1, Strings.ordinalIndexOf("", "", 0));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "a", 0));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "b", 0));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "ab", 0));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "", 0));

		assertEquals(-1, Strings.ordinalIndexOf(null, null, 1));
		assertEquals(-1, Strings.ordinalIndexOf("", null, 1));
		assertEquals(0, Strings.ordinalIndexOf("", "", 1));
		assertEquals(0, Strings.ordinalIndexOf("aabaabaa", "a", 1));
		assertEquals(2, Strings.ordinalIndexOf("aabaabaa", "b", 1));
		assertEquals(1, Strings.ordinalIndexOf("aabaabaa", "ab", 1));
		assertEquals(0, Strings.ordinalIndexOf("aabaabaa", "", 1));

		assertEquals(-1, Strings.ordinalIndexOf(null, null, 2));
		assertEquals(-1, Strings.ordinalIndexOf("", null, 2));
		assertEquals(0, Strings.ordinalIndexOf("", "", 2));
		assertEquals(1, Strings.ordinalIndexOf("aabaabaa", "a", 2));
		assertEquals(5, Strings.ordinalIndexOf("aabaabaa", "b", 2));
		assertEquals(4, Strings.ordinalIndexOf("aabaabaa", "ab", 2));
		assertEquals(0, Strings.ordinalIndexOf("aabaabaa", "", 2));

		assertEquals(-1, Strings.ordinalIndexOf(null, null, Integer.MAX_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("", null, Integer.MAX_VALUE));
		assertEquals(0, Strings.ordinalIndexOf("", "", Integer.MAX_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "a", Integer.MAX_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "b", Integer.MAX_VALUE));
		assertEquals(-1, Strings.ordinalIndexOf("aabaabaa", "ab", Integer.MAX_VALUE));
		assertEquals(0, Strings.ordinalIndexOf("aabaabaa", "", Integer.MAX_VALUE));

		assertEquals(-1, Strings.ordinalIndexOf("aaaaaaaaa", "a", 0));
		assertEquals(0, Strings.ordinalIndexOf("aaaaaaaaa", "a", 1));
		assertEquals(1, Strings.ordinalIndexOf("aaaaaaaaa", "a", 2));
		assertEquals(2, Strings.ordinalIndexOf("aaaaaaaaa", "a", 3));
		assertEquals(3, Strings.ordinalIndexOf("aaaaaaaaa", "a", 4));
		assertEquals(4, Strings.ordinalIndexOf("aaaaaaaaa", "a", 5));
		assertEquals(5, Strings.ordinalIndexOf("aaaaaaaaa", "a", 6));
		assertEquals(6, Strings.ordinalIndexOf("aaaaaaaaa", "a", 7));
		assertEquals(7, Strings.ordinalIndexOf("aaaaaaaaa", "a", 8));
		assertEquals(8, Strings.ordinalIndexOf("aaaaaaaaa", "a", 9));
		assertEquals(-1, Strings.ordinalIndexOf("aaaaaaaaa", "a", 10));
	}

}
