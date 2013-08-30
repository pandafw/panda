package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import panda.lang.Strings;

/**
 * Unit tests {@link Strings} - Substring methods
 *
 */
public class StringsSubstringTest  {
	private static final String FOO = "foo";
	private static final String BAR = "bar";
	private static final String BAZ = "baz";
	private static final String FOOBAR = "foobar";
	private static final String SENTENCE = "foo bar baz";

	// -----------------------------------------------------------------------

	@Test
	public void testSubstring_StringInt() {
		assertEquals(null, Strings.substring(null, 0));
		assertEquals("", Strings.substring("", 0));
		assertEquals("", Strings.substring("", 2));

		assertEquals("", Strings.substring(SENTENCE, 80));
		assertEquals(BAZ, Strings.substring(SENTENCE, 8));
		assertEquals(BAZ, Strings.substring(SENTENCE, -3));
		assertEquals(SENTENCE, Strings.substring(SENTENCE, 0));
		assertEquals("abc", Strings.substring("abc", -4));
		assertEquals("abc", Strings.substring("abc", -3));
		assertEquals("bc", Strings.substring("abc", -2));
		assertEquals("c", Strings.substring("abc", -1));
		assertEquals("abc", Strings.substring("abc", 0));
		assertEquals("bc", Strings.substring("abc", 1));
		assertEquals("c", Strings.substring("abc", 2));
		assertEquals("", Strings.substring("abc", 3));
		assertEquals("", Strings.substring("abc", 4));
	}

	@Test
	public void testSubstring_StringIntInt() {
		assertEquals(null, Strings.substring(null, 0, 0));
		assertEquals(null, Strings.substring(null, 1, 2));
		assertEquals("", Strings.substring("", 0, 0));
		assertEquals("", Strings.substring("", 1, 2));
		assertEquals("", Strings.substring("", -2, -1));

		assertEquals("", Strings.substring(SENTENCE, 8, 6));
		assertEquals(FOO, Strings.substring(SENTENCE, 0, 3));
		assertEquals("o", Strings.substring(SENTENCE, -9, 3));
		assertEquals(FOO, Strings.substring(SENTENCE, 0, -8));
		assertEquals("o", Strings.substring(SENTENCE, -9, -8));
		assertEquals(SENTENCE, Strings.substring(SENTENCE, 0, 80));
		assertEquals("", Strings.substring(SENTENCE, 2, 2));
		assertEquals("b", Strings.substring("abc", -2, -1));
	}

	@Test
	public void testLeft_String() {
		assertSame(null, Strings.left(null, -1));
		assertSame(null, Strings.left(null, 0));
		assertSame(null, Strings.left(null, 2));

		assertEquals("", Strings.left("", -1));
		assertEquals("", Strings.left("", 0));
		assertEquals("", Strings.left("", 2));

		assertEquals("", Strings.left(FOOBAR, -1));
		assertEquals("", Strings.left(FOOBAR, 0));
		assertEquals(FOO, Strings.left(FOOBAR, 3));
		assertSame(FOOBAR, Strings.left(FOOBAR, 80));
	}

	@Test
	public void testRight_String() {
		assertSame(null, Strings.right(null, -1));
		assertSame(null, Strings.right(null, 0));
		assertSame(null, Strings.right(null, 2));

		assertEquals("", Strings.right("", -1));
		assertEquals("", Strings.right("", 0));
		assertEquals("", Strings.right("", 2));

		assertEquals("", Strings.right(FOOBAR, -1));
		assertEquals("", Strings.right(FOOBAR, 0));
		assertEquals(BAR, Strings.right(FOOBAR, 3));
		assertSame(FOOBAR, Strings.right(FOOBAR, 80));
	}

	@Test
	public void testMid_String() {
		assertSame(null, Strings.mid(null, -1, 0));
		assertSame(null, Strings.mid(null, 0, -1));
		assertSame(null, Strings.mid(null, 3, 0));
		assertSame(null, Strings.mid(null, 3, 2));

		assertEquals("", Strings.mid("", 0, -1));
		assertEquals("", Strings.mid("", 0, 0));
		assertEquals("", Strings.mid("", 0, 2));

		assertEquals("", Strings.mid(FOOBAR, 3, -1));
		assertEquals("", Strings.mid(FOOBAR, 3, 0));
		assertEquals("b", Strings.mid(FOOBAR, 3, 1));
		assertEquals(FOO, Strings.mid(FOOBAR, 0, 3));
		assertEquals(BAR, Strings.mid(FOOBAR, 3, 3));
		assertEquals(FOOBAR, Strings.mid(FOOBAR, 0, 80));
		assertEquals(BAR, Strings.mid(FOOBAR, 3, 80));
		assertEquals("", Strings.mid(FOOBAR, 9, 3));
		assertEquals(FOO, Strings.mid(FOOBAR, -1, 3));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSubstringBefore_StringString() {
		assertEquals("foo", Strings.substringBefore("fooXXbarXXbaz", "XX"));

		assertEquals(null, Strings.substringBefore(null, null));
		assertEquals(null, Strings.substringBefore(null, ""));
		assertEquals(null, Strings.substringBefore(null, "XX"));
		assertEquals("", Strings.substringBefore("", null));
		assertEquals("", Strings.substringBefore("", ""));
		assertEquals("", Strings.substringBefore("", "XX"));

		assertEquals("foo", Strings.substringBefore("foo", null));
		assertEquals("foo", Strings.substringBefore("foo", "b"));
		assertEquals("f", Strings.substringBefore("foot", "o"));
		assertEquals("", Strings.substringBefore("abc", "a"));
		assertEquals("a", Strings.substringBefore("abcba", "b"));
		assertEquals("ab", Strings.substringBefore("abc", "c"));
		assertEquals("", Strings.substringBefore("abc", ""));
	}

	@Test
	public void testSubstringAfter_StringString() {
		assertEquals("barXXbaz", Strings.substringAfter("fooXXbarXXbaz", "XX"));

		assertEquals(null, Strings.substringAfter(null, null));
		assertEquals(null, Strings.substringAfter(null, ""));
		assertEquals(null, Strings.substringAfter(null, "XX"));
		assertEquals("", Strings.substringAfter("", null));
		assertEquals("", Strings.substringAfter("", ""));
		assertEquals("", Strings.substringAfter("", "XX"));

		assertEquals("", Strings.substringAfter("foo", null));
		assertEquals("ot", Strings.substringAfter("foot", "o"));
		assertEquals("bc", Strings.substringAfter("abc", "a"));
		assertEquals("cba", Strings.substringAfter("abcba", "b"));
		assertEquals("", Strings.substringAfter("abc", "c"));
		assertEquals("abc", Strings.substringAfter("abc", ""));
		assertEquals("", Strings.substringAfter("abc", "d"));
	}

	@Test
	public void testSubstringBeforeLast_StringString() {
		assertEquals("fooXXbar", Strings.substringBeforeLast("fooXXbarXXbaz", "XX"));

		assertEquals(null, Strings.substringBeforeLast(null, null));
		assertEquals(null, Strings.substringBeforeLast(null, ""));
		assertEquals(null, Strings.substringBeforeLast(null, "XX"));
		assertEquals("", Strings.substringBeforeLast("", null));
		assertEquals("", Strings.substringBeforeLast("", ""));
		assertEquals("", Strings.substringBeforeLast("", "XX"));

		assertEquals("foo", Strings.substringBeforeLast("foo", null));
		assertEquals("foo", Strings.substringBeforeLast("foo", "b"));
		assertEquals("fo", Strings.substringBeforeLast("foo", "o"));
		assertEquals("abc\r\n", Strings.substringBeforeLast("abc\r\n", "d"));
		assertEquals("abc", Strings.substringBeforeLast("abcdabc", "d"));
		assertEquals("abcdabc", Strings.substringBeforeLast("abcdabcd", "d"));
		assertEquals("a", Strings.substringBeforeLast("abc", "b"));
		assertEquals("abc ", Strings.substringBeforeLast("abc \n", "\n"));
		assertEquals("a", Strings.substringBeforeLast("a", null));
		assertEquals("a", Strings.substringBeforeLast("a", ""));
		assertEquals("", Strings.substringBeforeLast("a", "a"));
	}

	@Test
	public void testSubstringAfterLast_StringString() {
		assertEquals("baz", Strings.substringAfterLast("fooXXbarXXbaz", "XX"));

		assertEquals(null, Strings.substringAfterLast(null, null));
		assertEquals(null, Strings.substringAfterLast(null, ""));
		assertEquals(null, Strings.substringAfterLast(null, "XX"));
		assertEquals("", Strings.substringAfterLast("", null));
		assertEquals("", Strings.substringAfterLast("", ""));
		assertEquals("", Strings.substringAfterLast("", "a"));

		assertEquals("", Strings.substringAfterLast("foo", null));
		assertEquals("", Strings.substringAfterLast("foo", "b"));
		assertEquals("t", Strings.substringAfterLast("foot", "o"));
		assertEquals("bc", Strings.substringAfterLast("abc", "a"));
		assertEquals("a", Strings.substringAfterLast("abcba", "b"));
		assertEquals("", Strings.substringAfterLast("abc", "c"));
		assertEquals("", Strings.substringAfterLast("", "d"));
		assertEquals("", Strings.substringAfterLast("abc", ""));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSubstringBetween_StringString() {
		assertEquals(null, Strings.substringBetween(null, "tag"));
		assertEquals("", Strings.substringBetween("", ""));
		assertEquals(null, Strings.substringBetween("", "abc"));
		assertEquals("", Strings.substringBetween("    ", " "));
		assertEquals(null, Strings.substringBetween("abc", null));
		assertEquals("", Strings.substringBetween("abc", ""));
		assertEquals(null, Strings.substringBetween("abc", "a"));
		assertEquals("bc", Strings.substringBetween("abca", "a"));
		assertEquals("bc", Strings.substringBetween("abcabca", "a"));
		assertEquals("bar", Strings.substringBetween("\nbar\n", "\n"));
	}

	@Test
	public void testSubstringBetween_StringStringString() {
		assertEquals(null, Strings.substringBetween(null, "", ""));
		assertEquals(null, Strings.substringBetween("", null, ""));
		assertEquals(null, Strings.substringBetween("", "", null));
		assertEquals("", Strings.substringBetween("", "", ""));
		assertEquals("", Strings.substringBetween("foo", "", ""));
		assertEquals(null, Strings.substringBetween("foo", "", "]"));
		assertEquals(null, Strings.substringBetween("foo", "[", "]"));
		assertEquals("", Strings.substringBetween("    ", " ", "  "));
		assertEquals("bar", Strings.substringBetween("<foo>bar</foo>", "<foo>", "</foo>"));
	}

	/**
	 * Tests the substringsBetween method that returns an String Array of substrings.
	 */
	@Test
	public void testSubstringsBetween_StringStringString() {

		String[] results = Strings.substringsBetween("[one], [two], [three]", "[", "]");
		assertEquals(3, results.length);
		assertEquals("one", results[0]);
		assertEquals("two", results[1]);
		assertEquals("three", results[2]);

		results = Strings.substringsBetween("[one], [two], three", "[", "]");
		assertEquals(2, results.length);
		assertEquals("one", results[0]);
		assertEquals("two", results[1]);

		results = Strings.substringsBetween("[one], [two], three]", "[", "]");
		assertEquals(2, results.length);
		assertEquals("one", results[0]);
		assertEquals("two", results[1]);

		results = Strings.substringsBetween("[one], two], three]", "[", "]");
		assertEquals(1, results.length);
		assertEquals("one", results[0]);

		results = Strings.substringsBetween("one], two], [three]", "[", "]");
		assertEquals(1, results.length);
		assertEquals("three", results[0]);

		// 'ab hello ba' will match, but 'ab non ba' won't
		// this is because the 'a' is shared between the two and can't be matched twice
		results = Strings.substringsBetween("aabhellobabnonba", "ab", "ba");
		assertEquals(1, results.length);
		assertEquals("hello", results[0]);

		results = Strings.substringsBetween("one, two, three", "[", "]");
		assertNull(results);

		results = Strings.substringsBetween("[one, two, three", "[", "]");
		assertNull(results);

		results = Strings.substringsBetween("one, two, three]", "[", "]");
		assertNull(results);

		results = Strings.substringsBetween("[one], [two], [three]", "[", null);
		assertNull(results);

		results = Strings.substringsBetween("[one], [two], [three]", null, "]");
		assertNull(results);

		results = Strings.substringsBetween("[one], [two], [three]", "", "");
		assertNull(results);

		results = Strings.substringsBetween(null, "[", "]");
		assertNull(results);

		results = Strings.substringsBetween("", "[", "]");
		assertEquals(0, results.length);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testCountMatches_String() {
		assertEquals(0, Strings.countMatches(null, null));
		assertEquals(0, Strings.countMatches("blah", null));
		assertEquals(0, Strings.countMatches(null, "DD"));

		assertEquals(0, Strings.countMatches("x", ""));
		assertEquals(0, Strings.countMatches("", ""));

		assertEquals(3, Strings.countMatches("one long someone sentence of one", "one"));
		assertEquals(0, Strings.countMatches("one long someone sentence of one", "two"));
		assertEquals(4, Strings.countMatches("oooooooooooo", "ooo"));
	}

}
