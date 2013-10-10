package panda.lang;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.Locale;

import org.junit.Test;

/**
 * test class for Strings
 */
public class StringsTest {

	static final String WHITESPACE;
	static final String NON_WHITESPACE;
	static final String TRIMMABLE;
	static final String NON_TRIMMABLE;
	static {
		String ws = "";
		String nws = "";
		String tr = "";
		String ntr = "";
		for (int i = 0; i < Character.MAX_VALUE; i++) {
			if (Character.isWhitespace((char)i)) {
				ws += String.valueOf((char)i);
				if (i > 32) {
					ntr += String.valueOf((char)i);
				}
			}
			else if (i < 40) {
				nws += String.valueOf((char)i);
			}
		}
		for (int i = 0; i <= 32; i++) {
			tr += String.valueOf((char)i);
		}
		WHITESPACE = ws;
		NON_WHITESPACE = nws;
		TRIMMABLE = tr;
		NON_TRIMMABLE = ntr;
	}

	private static final String[] ARRAY_LIST = { "foo", "bar", "baz" };
	private static final String[] EMPTY_ARRAY_LIST = {};
	private static final String[] NULL_ARRAY_LIST = { null };
	private static final Object[] NULL_TO_STRING_LIST = { new Object() {
		@Override
		public String toString() {
			return null;
		}
	} };
	private static final String[] MIXED_ARRAY_LIST = { null, "", "foo" };
	private static final Object[] MIXED_TYPE_LIST = { "foo", Long.valueOf(2L) };
	private static final long[] LONG_PRIM_LIST = { 1, 2 };
	private static final int[] INT_PRIM_LIST = { 1, 2 };
	private static final byte[] BYTE_PRIM_LIST = { 1, 2 };
	private static final short[] SHORT_PRIM_LIST = { 1, 2 };
	private static final char[] CHAR_PRIM_LIST = { '1', '2' };
	private static final float[] FLOAT_PRIM_LIST = { 1, 2 };
	private static final double[] DOUBLE_PRIM_LIST = { 1, 2 };

	private static final String SEPARATOR = ",";
	private static final char SEPARATOR_CHAR = ';';

	private static final String TEXT_LIST = "foo,bar,baz";
	private static final String TEXT_LIST_CHAR = "foo;bar;baz";
	private static final String TEXT_LIST_NOSEP = "foobarbaz";

	private static final String FOO_UNCAP = "foo";
	private static final String FOO_CAP = "Foo";

	private static final String SENTENCE_UNCAP = "foo bar baz";
	private static final String SENTENCE_CAP = "Foo Bar Baz";

	// -----------------------------------------------------------------------
	@Test
	public void testConstructor() {
		assertNotNull(new Strings());
		final Constructor<?>[] cons = Strings.class.getDeclaredConstructors();
		assertEquals(1, cons.length);
		assertTrue(Modifier.isPublic(cons[0].getModifiers()));
		assertTrue(Modifier.isPublic(Strings.class.getModifiers()));
		assertFalse(Modifier.isFinal(Strings.class.getModifiers()));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testCaseFunctions() {
		assertEquals(null, Strings.upperCase(null));
		assertEquals(null, Strings.upperCase(null, Locale.ENGLISH));
		assertEquals(null, Strings.lowerCase(null));
		assertEquals(null, Strings.lowerCase(null, Locale.ENGLISH));
		assertEquals(null, Strings.capitalize(null));
		assertEquals(null, Strings.uncapitalize(null));

		assertEquals("capitalize(empty-string) failed", "", Strings.capitalize(""));
		assertEquals("capitalize(single-char-string) failed", "X", Strings.capitalize("x"));
		assertEquals("capitalize(String) failed", FOO_CAP, Strings.capitalize(FOO_CAP));
		assertEquals("capitalize(string) failed", FOO_CAP, Strings.capitalize(FOO_UNCAP));

		assertEquals("uncapitalize(String) failed", FOO_UNCAP, Strings.uncapitalize(FOO_CAP));
		assertEquals("uncapitalize(string) failed", FOO_UNCAP, Strings.uncapitalize(FOO_UNCAP));
		assertEquals("uncapitalize(empty-string) failed", "", Strings.uncapitalize(""));
		assertEquals("uncapitalize(single-char-string) failed", "x", Strings.uncapitalize("X"));

		// reflection type of tests: Sentences.
		assertEquals("uncapitalize(capitalize(String)) failed", SENTENCE_UNCAP,
			Strings.uncapitalize(Strings.capitalize(SENTENCE_UNCAP)));
		assertEquals("capitalize(uncapitalize(String)) failed", SENTENCE_CAP,
			Strings.capitalize(Strings.uncapitalize(SENTENCE_CAP)));

		// reflection type of tests: One word.
		assertEquals("uncapitalize(capitalize(String)) failed", FOO_UNCAP,
			Strings.uncapitalize(Strings.capitalize(FOO_UNCAP)));
		assertEquals("capitalize(uncapitalize(String)) failed", FOO_CAP,
			Strings.capitalize(Strings.uncapitalize(FOO_CAP)));

		assertEquals("upperCase(String) failed", "FOO TEST THING", Strings.upperCase("fOo test THING"));
		assertEquals("upperCase(empty-string) failed", "", Strings.upperCase(""));
		assertEquals("lowerCase(String) failed", "foo test thing", Strings.lowerCase("fOo test THING"));
		assertEquals("lowerCase(empty-string) failed", "", Strings.lowerCase(""));

		assertEquals("upperCase(String, Locale) failed", "FOO TEST THING",
			Strings.upperCase("fOo test THING", Locale.ENGLISH));
		assertEquals("upperCase(empty-string, Locale) failed", "", Strings.upperCase("", Locale.ENGLISH));
		assertEquals("lowerCase(String, Locale) failed", "foo test thing",
			Strings.lowerCase("fOo test THING", Locale.ENGLISH));
		assertEquals("lowerCase(empty-string, Locale) failed", "", Strings.lowerCase("", Locale.ENGLISH));
	}

	@Test
	public void testSwapCase_String() {
		assertEquals(null, Strings.swapCase(null));
		assertEquals("", Strings.swapCase(""));
		assertEquals("  ", Strings.swapCase("  "));

		assertEquals("i", Texts.swapCase("I"));
		assertEquals("I", Texts.swapCase("i"));
		assertEquals("I AM HERE 123", Strings.swapCase("i am here 123"));
		assertEquals("i aM hERE 123", Strings.swapCase("I Am Here 123"));
		assertEquals("I AM here 123", Strings.swapCase("i am HERE 123"));
		assertEquals("i am here 123", Strings.swapCase("I AM HERE 123"));

		final String test = "This String contains a TitleCase character: \u01C8";
		final String expect = "tHIS sTRING CONTAINS A tITLEcASE CHARACTER: \u01C9";
		assertEquals(expect, Texts.swapCase(test));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testJoin_Objects() {
		assertEquals("abc", Strings.join("a", "b", "c"));
		assertEquals("a", Strings.join(null, "", "a"));
		assertEquals(null, Strings.join((Object[])null));
	}

	@Test
	public void testJoin_Objectarray() {
		// assertEquals(null, Strings.join(null)); // generates warning
		assertEquals(null, Strings.join((Object[])null)); // equivalent explicit cast
		// test additional varargs calls
		assertEquals("", Strings.join()); // empty array
		assertEquals("", Strings.join((Object)null)); // => new Object[]{null}

		assertEquals("", Strings.join(EMPTY_ARRAY_LIST));
		assertEquals("", Strings.join(NULL_ARRAY_LIST));
		assertEquals("null", Strings.join(NULL_TO_STRING_LIST));
		assertEquals("abc", Strings.join(new String[] { "a", "b", "c" }));
		assertEquals("a", Strings.join(new String[] { null, "a", "" }));
		assertEquals("foo", Strings.join(MIXED_ARRAY_LIST));
		assertEquals("foo2", Strings.join(MIXED_TYPE_LIST));
	}

	@Test
	public void testJoin_ArrayCharSeparator() {
		assertEquals(null, Strings.join((Object[])null, ','));
		assertEquals(TEXT_LIST_CHAR, Strings.join(ARRAY_LIST, SEPARATOR_CHAR));
		assertEquals("", Strings.join(EMPTY_ARRAY_LIST, SEPARATOR_CHAR));
		assertEquals(";;foo", Strings.join(MIXED_ARRAY_LIST, SEPARATOR_CHAR));
		assertEquals("foo;2", Strings.join(MIXED_TYPE_LIST, SEPARATOR_CHAR));

		assertEquals("/", Strings.join(MIXED_ARRAY_LIST, '/', 0, MIXED_ARRAY_LIST.length - 1));
		assertEquals("foo", Strings.join(MIXED_TYPE_LIST, '/', 0, 1));
		assertEquals("null", Strings.join(NULL_TO_STRING_LIST, '/', 0, 1));
		assertEquals("foo/2", Strings.join(MIXED_TYPE_LIST, '/', 0, 2));
		assertEquals("2", Strings.join(MIXED_TYPE_LIST, '/', 1, 2));
		assertEquals("", Strings.join(MIXED_TYPE_LIST, '/', 2, 1));
	}

	@Test
	public void testJoin_ArrayOfChars() {
		assertEquals(null, Strings.join((char[])null, ','));
		assertEquals("1;2", Strings.join(CHAR_PRIM_LIST, SEPARATOR_CHAR));
		assertEquals("2", Strings.join(CHAR_PRIM_LIST, SEPARATOR_CHAR, 1, 2));
	}

	@Test
	public void testJoin_ArrayOfBytes() {
		assertEquals(null, Strings.join((byte[])null, ','));
		assertEquals("1;2", Strings.join(BYTE_PRIM_LIST, SEPARATOR_CHAR));
		assertEquals("2", Strings.join(BYTE_PRIM_LIST, SEPARATOR_CHAR, 1, 2));
	}

	@Test
	public void testJoin_ArrayOfInts() {
		assertEquals(null, Strings.join((int[])null, ','));
		assertEquals("1;2", Strings.join(INT_PRIM_LIST, SEPARATOR_CHAR));
		assertEquals("2", Strings.join(INT_PRIM_LIST, SEPARATOR_CHAR, 1, 2));
	}

	@Test
	public void testJoin_ArrayOfLongs() {
		assertEquals(null, Strings.join((long[])null, ','));
		assertEquals("1;2", Strings.join(LONG_PRIM_LIST, SEPARATOR_CHAR));
		assertEquals("2", Strings.join(LONG_PRIM_LIST, SEPARATOR_CHAR, 1, 2));
	}

	@Test
	public void testJoin_ArrayOfFloats() {
		assertEquals(null, Strings.join((float[])null, ','));
		assertEquals("1.0;2.0", Strings.join(FLOAT_PRIM_LIST, SEPARATOR_CHAR));
		assertEquals("2.0", Strings.join(FLOAT_PRIM_LIST, SEPARATOR_CHAR, 1, 2));
	}

	@Test
	public void testJoin_ArrayOfDoubles() {
		assertEquals(null, Strings.join((double[])null, ','));
		assertEquals("1.0;2.0", Strings.join(DOUBLE_PRIM_LIST, SEPARATOR_CHAR));
		assertEquals("2.0", Strings.join(DOUBLE_PRIM_LIST, SEPARATOR_CHAR, 1, 2));
	}

	@Test
	public void testJoin_ArrayOfShorts() {
		assertEquals(null, Strings.join((short[])null, ','));
		assertEquals("1;2", Strings.join(SHORT_PRIM_LIST, SEPARATOR_CHAR));
		assertEquals("2", Strings.join(SHORT_PRIM_LIST, SEPARATOR_CHAR, 1, 2));
	}

	@Test
	public void testJoin_ArrayString() {
		assertEquals(null, Strings.join((Object[])null, null));
		assertEquals(TEXT_LIST_NOSEP, Strings.join(ARRAY_LIST, null));
		assertEquals(TEXT_LIST_NOSEP, Strings.join(ARRAY_LIST, ""));

		assertEquals("", Strings.join(NULL_ARRAY_LIST, null));

		assertEquals("", Strings.join(EMPTY_ARRAY_LIST, null));
		assertEquals("", Strings.join(EMPTY_ARRAY_LIST, ""));
		assertEquals("", Strings.join(EMPTY_ARRAY_LIST, SEPARATOR));

		assertEquals(TEXT_LIST, Strings.join(ARRAY_LIST, SEPARATOR));
		assertEquals(",,foo", Strings.join(MIXED_ARRAY_LIST, SEPARATOR));
		assertEquals("foo,2", Strings.join(MIXED_TYPE_LIST, SEPARATOR));

		assertEquals("/", Strings.join(MIXED_ARRAY_LIST, "/", 0, MIXED_ARRAY_LIST.length - 1));
		assertEquals("", Strings.join(MIXED_ARRAY_LIST, "", 0, MIXED_ARRAY_LIST.length - 1));
		assertEquals("foo", Strings.join(MIXED_TYPE_LIST, "/", 0, 1));
		assertEquals("foo/2", Strings.join(MIXED_TYPE_LIST, "/", 0, 2));
		assertEquals("2", Strings.join(MIXED_TYPE_LIST, "/", 1, 2));
		assertEquals("", Strings.join(MIXED_TYPE_LIST, "/", 2, 1));
	}

	@Test
	public void testJoin_IteratorChar() {
		assertEquals(null, Strings.join((Iterator<?>)null, ','));
		assertEquals(TEXT_LIST_CHAR, Strings.join(Arrays.asList(ARRAY_LIST).iterator(), SEPARATOR_CHAR));
		assertEquals("", Strings.join(Arrays.asList(NULL_ARRAY_LIST).iterator(), SEPARATOR_CHAR));
		assertEquals("", Strings.join(Arrays.asList(EMPTY_ARRAY_LIST).iterator(), SEPARATOR_CHAR));
		assertEquals("foo", Strings.join(Collections.singleton("foo").iterator(), 'x'));
	}

	@Test
	public void testJoin_IteratorString() {
		assertEquals(null, Strings.join((Iterator<?>)null, null));
		assertEquals(TEXT_LIST_NOSEP, Strings.join(Arrays.asList(ARRAY_LIST).iterator(), null));
		assertEquals(TEXT_LIST_NOSEP, Strings.join(Arrays.asList(ARRAY_LIST).iterator(), ""));
		assertEquals("foo", Strings.join(Collections.singleton("foo").iterator(), "x"));
		assertEquals("foo", Strings.join(Collections.singleton("foo").iterator(), null));

		assertEquals("", Strings.join(Arrays.asList(NULL_ARRAY_LIST).iterator(), null));

		assertEquals("", Strings.join(Arrays.asList(EMPTY_ARRAY_LIST).iterator(), null));
		assertEquals("", Strings.join(Arrays.asList(EMPTY_ARRAY_LIST).iterator(), ""));
		assertEquals("", Strings.join(Arrays.asList(EMPTY_ARRAY_LIST).iterator(), SEPARATOR));

		assertEquals(TEXT_LIST, Strings.join(Arrays.asList(ARRAY_LIST).iterator(), SEPARATOR));
	}

	@Test
	public void testJoin_IterableChar() {
		assertEquals(null, Strings.join((Iterable<?>)null, ','));
		assertEquals(TEXT_LIST_CHAR, Strings.join(Arrays.asList(ARRAY_LIST), SEPARATOR_CHAR));
		assertEquals("", Strings.join(Arrays.asList(NULL_ARRAY_LIST), SEPARATOR_CHAR));
		assertEquals("", Strings.join(Arrays.asList(EMPTY_ARRAY_LIST), SEPARATOR_CHAR));
		assertEquals("foo", Strings.join(Collections.singleton("foo"), 'x'));
	}

	@Test
	public void testJoin_IterableString() {
		assertEquals(null, Strings.join((Iterable<?>)null, null));
		assertEquals(TEXT_LIST_NOSEP, Strings.join(Arrays.asList(ARRAY_LIST), null));
		assertEquals(TEXT_LIST_NOSEP, Strings.join(Arrays.asList(ARRAY_LIST), ""));
		assertEquals("foo", Strings.join(Collections.singleton("foo"), "x"));
		assertEquals("foo", Strings.join(Collections.singleton("foo"), null));

		assertEquals("", Strings.join(Arrays.asList(NULL_ARRAY_LIST), null));

		assertEquals("", Strings.join(Arrays.asList(EMPTY_ARRAY_LIST), null));
		assertEquals("", Strings.join(Arrays.asList(EMPTY_ARRAY_LIST), ""));
		assertEquals("", Strings.join(Arrays.asList(EMPTY_ARRAY_LIST), SEPARATOR));

		assertEquals(TEXT_LIST, Strings.join(Arrays.asList(ARRAY_LIST), SEPARATOR));
	}

	@Test
	public void testSplit_String() {
		assertArrayEquals(null, Strings.split(null));
		assertEquals(0, Strings.split("").length);

		String str = "a b  .c";
		String[] res = Strings.split(str);
		assertEquals(3, res.length);
		assertEquals("a", res[0]);
		assertEquals("b", res[1]);
		assertEquals(".c", res[2]);

		str = " a ";
		res = Strings.split(str);
		assertEquals(1, res.length);
		assertEquals("a", res[0]);

		str = "a" + WHITESPACE + "b" + NON_WHITESPACE + "c";
		res = Strings.split(str);
		assertEquals(2, res.length);
		assertEquals("a", res[0]);
		assertEquals("b" + NON_WHITESPACE + "c", res[1]);
	}

	@Test
	public void testSplit_StringChar() {
		assertArrayEquals(null, Strings.split(null, '.'));
		assertEquals(0, Strings.split("", '.').length);

		String str = "a.b.. c";
		String[] res = Strings.split(str, '.');
		assertEquals(3, res.length);
		assertEquals("a", res[0]);
		assertEquals("b", res[1]);
		assertEquals(" c", res[2]);

		str = ".a.";
		res = Strings.split(str, '.');
		assertEquals(1, res.length);
		assertEquals("a", res[0]);

		str = "a b c";
		res = Strings.split(str, ' ');
		assertEquals(3, res.length);
		assertEquals("a", res[0]);
		assertEquals("b", res[1]);
		assertEquals("c", res[2]);
	}

	@Test
	public void testSplit_StringString_StringStringInt() {
		assertArrayEquals(null, Strings.split(null, "."));
		assertArrayEquals(null, Strings.split(null, ".", 3));

		assertEquals(0, Strings.split("", ".").length);
		assertEquals(0, Strings.split("", ".", 3).length);

		innerTestSplit('.', ".", ' ');
		innerTestSplit('.', ".", ',');
		innerTestSplit('.', ".,", 'x');
		for (int i = 0; i < WHITESPACE.length(); i++) {
			for (int j = 0; j < NON_WHITESPACE.length(); j++) {
				innerTestSplit(WHITESPACE.charAt(i), null, NON_WHITESPACE.charAt(j));
				innerTestSplit(WHITESPACE.charAt(i), String.valueOf(WHITESPACE.charAt(i)), NON_WHITESPACE.charAt(j));
			}
		}

		String[] results;
		final String[] expectedResults = { "ab", "de fg" };
		results = Strings.split("ab   de fg", null, 2);
		assertEquals(expectedResults.length, results.length);
		for (int i = 0; i < expectedResults.length; i++) {
			assertEquals(expectedResults[i], results[i]);
		}

		final String[] expectedResults2 = { "ab", "cd:ef" };
		results = Strings.split("ab:cd:ef", ":", 2);
		assertEquals(expectedResults2.length, results.length);
		for (int i = 0; i < expectedResults2.length; i++) {
			assertEquals(expectedResults2[i], results[i]);
		}
	}

	private void innerTestSplit(final char separator, final String sepStr, final char noMatch) {
		final String msg = "Failed on separator hex(" + Integer.toHexString(separator) + "), noMatch hex("
				+ Integer.toHexString(noMatch) + "), sepStr(" + sepStr + ")";

		final String str = "a" + separator + "b" + separator + separator + noMatch + "c";
		String[] res;
		// (str, sepStr)
		res = Strings.split(str, sepStr);
		assertEquals(msg, 3, res.length);
		assertEquals(msg, "a", res[0]);
		assertEquals(msg, "b", res[1]);
		assertEquals(msg, noMatch + "c", res[2]);

		final String str2 = separator + "a" + separator;
		res = Strings.split(str2, sepStr);
		assertEquals(msg, 1, res.length);
		assertEquals(msg, "a", res[0]);

		res = Strings.split(str, sepStr, -1);
		assertEquals(msg, 3, res.length);
		assertEquals(msg, "a", res[0]);
		assertEquals(msg, "b", res[1]);
		assertEquals(msg, noMatch + "c", res[2]);

		res = Strings.split(str, sepStr, 0);
		assertEquals(msg, 3, res.length);
		assertEquals(msg, "a", res[0]);
		assertEquals(msg, "b", res[1]);
		assertEquals(msg, noMatch + "c", res[2]);

		res = Strings.split(str, sepStr, 1);
		assertEquals(msg, 1, res.length);
		assertEquals(msg, str, res[0]);

		res = Strings.split(str, sepStr, 2);
		assertEquals(msg, 2, res.length);
		assertEquals(msg, "a", res[0]);
		assertEquals(msg, str.substring(2), res[1]);
	}

	@Test
	public void testSplitByWholeString_StringStringBoolean() {
		assertArrayEquals(null, Strings.splitByWholeSeparator(null, "."));

		assertEquals(0, Strings.splitByWholeSeparator("", ".").length);

		final String stringToSplitOnNulls = "ab   de fg";
		final String[] splitOnNullExpectedResults = { "ab", "de", "fg" };

		final String[] splitOnNullResults = Strings.splitByWholeSeparator(stringToSplitOnNulls, null);
		assertEquals(splitOnNullExpectedResults.length, splitOnNullResults.length);
		for (int i = 0; i < splitOnNullExpectedResults.length; i += 1) {
			assertEquals(splitOnNullExpectedResults[i], splitOnNullResults[i]);
		}

		final String stringToSplitOnCharactersAndString = "abstemiouslyaeiouyabstemiously";

		final String[] splitOnStringExpectedResults = { "abstemiously", "abstemiously" };
		final String[] splitOnStringResults = Strings.splitByWholeSeparator(stringToSplitOnCharactersAndString,
			"aeiouy");
		assertEquals(splitOnStringExpectedResults.length, splitOnStringResults.length);
		for (int i = 0; i < splitOnStringExpectedResults.length; i += 1) {
			assertEquals(splitOnStringExpectedResults[i], splitOnStringResults[i]);
		}

		final String[] splitWithMultipleSeparatorExpectedResults = { "ab", "cd", "ef" };
		final String[] splitWithMultipleSeparator = Strings.splitByWholeSeparator("ab:cd::ef", ":");
		assertEquals(splitWithMultipleSeparatorExpectedResults.length, splitWithMultipleSeparator.length);
		for (int i = 0; i < splitWithMultipleSeparatorExpectedResults.length; i++) {
			assertEquals(splitWithMultipleSeparatorExpectedResults[i], splitWithMultipleSeparator[i]);
		}
	}

	@Test
	public void testSplitByWholeString_StringStringBooleanInt() {
		assertArrayEquals(null, Strings.splitByWholeSeparator(null, ".", 3));

		assertEquals(0, Strings.splitByWholeSeparator("", ".", 3).length);

		final String stringToSplitOnNulls = "ab   de fg";
		final String[] splitOnNullExpectedResults = { "ab", "de fg" };
		// String[] splitOnNullExpectedResults = { "ab", "de" } ;

		final String[] splitOnNullResults = Strings.splitByWholeSeparator(stringToSplitOnNulls, null, 2);
		assertEquals(splitOnNullExpectedResults.length, splitOnNullResults.length);
		for (int i = 0; i < splitOnNullExpectedResults.length; i += 1) {
			assertEquals(splitOnNullExpectedResults[i], splitOnNullResults[i]);
		}

		final String stringToSplitOnCharactersAndString = "abstemiouslyaeiouyabstemiouslyaeiouyabstemiously";

		final String[] splitOnStringExpectedResults = { "abstemiously", "abstemiouslyaeiouyabstemiously" };
		// String[] splitOnStringExpectedResults = { "abstemiously", "abstemiously" } ;
		final String[] splitOnStringResults = Strings.splitByWholeSeparator(stringToSplitOnCharactersAndString,
			"aeiouy", 2);
		assertEquals(splitOnStringExpectedResults.length, splitOnStringResults.length);
		for (int i = 0; i < splitOnStringExpectedResults.length; i++) {
			assertEquals(splitOnStringExpectedResults[i], splitOnStringResults[i]);
		}
	}

	@Test
	public void testSplitByWholeSeparatorPreserveAllTokens_StringStringInt() {
		assertArrayEquals(null, Strings.splitByWholeSeparatorPreserveAllTokens(null, ".", -1));

		assertEquals(0, Strings.splitByWholeSeparatorPreserveAllTokens("", ".", -1).length);

		// test whitespace
		String input = "ab   de fg";
		String[] expected = new String[] { "ab", "", "", "de", "fg" };

		String[] actual = Strings.splitByWholeSeparatorPreserveAllTokens(input, null, -1);
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < actual.length; i += 1) {
			assertEquals(expected[i], actual[i]);
		}

		// test delimiter singlechar
		input = "1::2:::3::::4";
		expected = new String[] { "1", "", "2", "", "", "3", "", "", "", "4" };

		actual = Strings.splitByWholeSeparatorPreserveAllTokens(input, ":", -1);
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < actual.length; i += 1) {
			assertEquals(expected[i], actual[i]);
		}

		// test delimiter multichar
		input = "1::2:::3::::4";
		expected = new String[] { "1", "2", ":3", "", "4" };

		actual = Strings.splitByWholeSeparatorPreserveAllTokens(input, "::", -1);
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < actual.length; i += 1) {
			assertEquals(expected[i], actual[i]);
		}

		// test delimiter char with max
		input = "1::2::3:4";
		expected = new String[] { "1", "", "2", ":3:4" };

		actual = Strings.splitByWholeSeparatorPreserveAllTokens(input, ":", 4);
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < actual.length; i += 1) {
			assertEquals(expected[i], actual[i]);
		}
	}

	@Test
	public void testSplitPreserveAllTokens_String() {
		assertArrayEquals(null, Strings.splitPreserveAllTokens(null));
		assertEquals(0, Strings.splitPreserveAllTokens("").length);

		String str = "abc def";
		String[] res = Strings.splitPreserveAllTokens(str);
		assertEquals(2, res.length);
		assertEquals("abc", res[0]);
		assertEquals("def", res[1]);

		str = "abc  def";
		res = Strings.splitPreserveAllTokens(str);
		assertEquals(3, res.length);
		assertEquals("abc", res[0]);
		assertEquals("", res[1]);
		assertEquals("def", res[2]);

		str = " abc ";
		res = Strings.splitPreserveAllTokens(str);
		assertEquals(3, res.length);
		assertEquals("", res[0]);
		assertEquals("abc", res[1]);
		assertEquals("", res[2]);

		str = "a b .c";
		res = Strings.splitPreserveAllTokens(str);
		assertEquals(3, res.length);
		assertEquals("a", res[0]);
		assertEquals("b", res[1]);
		assertEquals(".c", res[2]);

		str = " a b .c";
		res = Strings.splitPreserveAllTokens(str);
		assertEquals(4, res.length);
		assertEquals("", res[0]);
		assertEquals("a", res[1]);
		assertEquals("b", res[2]);
		assertEquals(".c", res[3]);

		str = "a  b  .c";
		res = Strings.splitPreserveAllTokens(str);
		assertEquals(5, res.length);
		assertEquals("a", res[0]);
		assertEquals("", res[1]);
		assertEquals("b", res[2]);
		assertEquals("", res[3]);
		assertEquals(".c", res[4]);

		str = " a  ";
		res = Strings.splitPreserveAllTokens(str);
		assertEquals(4, res.length);
		assertEquals("", res[0]);
		assertEquals("a", res[1]);
		assertEquals("", res[2]);
		assertEquals("", res[3]);

		str = " a  b";
		res = Strings.splitPreserveAllTokens(str);
		assertEquals(4, res.length);
		assertEquals("", res[0]);
		assertEquals("a", res[1]);
		assertEquals("", res[2]);
		assertEquals("b", res[3]);

		str = "a" + WHITESPACE + "b" + NON_WHITESPACE + "c";
		res = Strings.splitPreserveAllTokens(str);
		assertEquals(WHITESPACE.length() + 1, res.length);
		assertEquals("a", res[0]);
		for (int i = 1; i < WHITESPACE.length() - 1; i++) {
			assertEquals("", res[i]);
		}
		assertEquals("b" + NON_WHITESPACE + "c", res[WHITESPACE.length()]);
	}

	@Test
	public void testSplitPreserveAllTokens_StringChar() {
		assertArrayEquals(null, Strings.splitPreserveAllTokens(null, '.'));
		assertEquals(0, Strings.splitPreserveAllTokens("", '.').length);

		String str = "a.b. c";
		String[] res = Strings.splitPreserveAllTokens(str, '.');
		assertEquals(3, res.length);
		assertEquals("a", res[0]);
		assertEquals("b", res[1]);
		assertEquals(" c", res[2]);

		str = "a.b.. c";
		res = Strings.splitPreserveAllTokens(str, '.');
		assertEquals(4, res.length);
		assertEquals("a", res[0]);
		assertEquals("b", res[1]);
		assertEquals("", res[2]);
		assertEquals(" c", res[3]);

		str = ".a.";
		res = Strings.splitPreserveAllTokens(str, '.');
		assertEquals(3, res.length);
		assertEquals("", res[0]);
		assertEquals("a", res[1]);
		assertEquals("", res[2]);

		str = ".a..";
		res = Strings.splitPreserveAllTokens(str, '.');
		assertEquals(4, res.length);
		assertEquals("", res[0]);
		assertEquals("a", res[1]);
		assertEquals("", res[2]);
		assertEquals("", res[3]);

		str = "..a.";
		res = Strings.splitPreserveAllTokens(str, '.');
		assertEquals(4, res.length);
		assertEquals("", res[0]);
		assertEquals("", res[1]);
		assertEquals("a", res[2]);
		assertEquals("", res[3]);

		str = "..a";
		res = Strings.splitPreserveAllTokens(str, '.');
		assertEquals(3, res.length);
		assertEquals("", res[0]);
		assertEquals("", res[1]);
		assertEquals("a", res[2]);

		str = "a b c";
		res = Strings.splitPreserveAllTokens(str, ' ');
		assertEquals(3, res.length);
		assertEquals("a", res[0]);
		assertEquals("b", res[1]);
		assertEquals("c", res[2]);

		str = "a  b  c";
		res = Strings.splitPreserveAllTokens(str, ' ');
		assertEquals(5, res.length);
		assertEquals("a", res[0]);
		assertEquals("", res[1]);
		assertEquals("b", res[2]);
		assertEquals("", res[3]);
		assertEquals("c", res[4]);

		str = " a b c";
		res = Strings.splitPreserveAllTokens(str, ' ');
		assertEquals(4, res.length);
		assertEquals("", res[0]);
		assertEquals("a", res[1]);
		assertEquals("b", res[2]);
		assertEquals("c", res[3]);

		str = "  a b c";
		res = Strings.splitPreserveAllTokens(str, ' ');
		assertEquals(5, res.length);
		assertEquals("", res[0]);
		assertEquals("", res[1]);
		assertEquals("a", res[2]);
		assertEquals("b", res[3]);
		assertEquals("c", res[4]);

		str = "a b c ";
		res = Strings.splitPreserveAllTokens(str, ' ');
		assertEquals(4, res.length);
		assertEquals("a", res[0]);
		assertEquals("b", res[1]);
		assertEquals("c", res[2]);
		assertEquals("", res[3]);

		str = "a b c  ";
		res = Strings.splitPreserveAllTokens(str, ' ');
		assertEquals(5, res.length);
		assertEquals("a", res[0]);
		assertEquals("b", res[1]);
		assertEquals("c", res[2]);
		assertEquals("", res[3]);
		assertEquals("", res[3]);

		// Match example in javadoc
		{
			String[] results;
			final String[] expectedResults = { "a", "", "b", "c" };
			results = Strings.splitPreserveAllTokens("a..b.c", '.');
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}
	}

	@Test
	public void testSplitPreserveAllTokens_StringString_StringStringInt() {
		assertArrayEquals(null, Strings.splitPreserveAllTokens(null, "."));
		assertArrayEquals(null, Strings.splitPreserveAllTokens(null, ".", 3));

		assertEquals(0, Strings.splitPreserveAllTokens("", ".").length);
		assertEquals(0, Strings.splitPreserveAllTokens("", ".", 3).length);

		innerTestSplitPreserveAllTokens('.', ".", ' ');
		innerTestSplitPreserveAllTokens('.', ".", ',');
		innerTestSplitPreserveAllTokens('.', ".,", 'x');
		for (int i = 0; i < WHITESPACE.length(); i++) {
			for (int j = 0; j < NON_WHITESPACE.length(); j++) {
				innerTestSplitPreserveAllTokens(WHITESPACE.charAt(i), null, NON_WHITESPACE.charAt(j));
				innerTestSplitPreserveAllTokens(WHITESPACE.charAt(i), String.valueOf(WHITESPACE.charAt(i)),
					NON_WHITESPACE.charAt(j));
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "ab", "de fg" };
			results = Strings.splitPreserveAllTokens("ab de fg", null, 2);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "ab", "  de fg" };
			results = Strings.splitPreserveAllTokens("ab   de fg", null, 2);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "ab", "::de:fg" };
			results = Strings.splitPreserveAllTokens("ab:::de:fg", ":", 2);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "ab", "", " de fg" };
			results = Strings.splitPreserveAllTokens("ab   de fg", null, 3);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "ab", "", "", "de fg" };
			results = Strings.splitPreserveAllTokens("ab   de fg", null, 4);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			final String[] expectedResults = { "ab", "cd:ef" };
			String[] results;
			results = Strings.splitPreserveAllTokens("ab:cd:ef", ":", 2);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "ab", ":cd:ef" };
			results = Strings.splitPreserveAllTokens("ab::cd:ef", ":", 2);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "ab", "", ":cd:ef" };
			results = Strings.splitPreserveAllTokens("ab:::cd:ef", ":", 3);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "ab", "", "", "cd:ef" };
			results = Strings.splitPreserveAllTokens("ab:::cd:ef", ":", 4);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "", "ab", "", "", "cd:ef" };
			results = Strings.splitPreserveAllTokens(":ab:::cd:ef", ":", 5);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

		{
			String[] results;
			final String[] expectedResults = { "", "", "ab", "", "", "cd:ef" };
			results = Strings.splitPreserveAllTokens("::ab:::cd:ef", ":", 6);
			assertEquals(expectedResults.length, results.length);
			for (int i = 0; i < expectedResults.length; i++) {
				assertEquals(expectedResults[i], results[i]);
			}
		}

	}

	private void innerTestSplitPreserveAllTokens(final char separator, final String sepStr, final char noMatch) {
		final String msg = "Failed on separator hex(" + Integer.toHexString(separator) + "), noMatch hex("
				+ Integer.toHexString(noMatch) + "), sepStr(" + sepStr + ")";

		final String str = "a" + separator + "b" + separator + separator + noMatch + "c";
		String[] res;
		// (str, sepStr)
		res = Strings.splitPreserveAllTokens(str, sepStr);
		assertEquals(msg, 4, res.length);
		assertEquals(msg, "a", res[0]);
		assertEquals(msg, "b", res[1]);
		assertEquals(msg, "", res[2]);
		assertEquals(msg, noMatch + "c", res[3]);

		final String str2 = separator + "a" + separator;
		res = Strings.splitPreserveAllTokens(str2, sepStr);
		assertEquals(msg, 3, res.length);
		assertEquals(msg, "", res[0]);
		assertEquals(msg, "a", res[1]);
		assertEquals(msg, "", res[2]);

		res = Strings.splitPreserveAllTokens(str, sepStr, -1);
		assertEquals(msg, 4, res.length);
		assertEquals(msg, "a", res[0]);
		assertEquals(msg, "b", res[1]);
		assertEquals(msg, "", res[2]);
		assertEquals(msg, noMatch + "c", res[3]);

		res = Strings.splitPreserveAllTokens(str, sepStr, 0);
		assertEquals(msg, 4, res.length);
		assertEquals(msg, "a", res[0]);
		assertEquals(msg, "b", res[1]);
		assertEquals(msg, "", res[2]);
		assertEquals(msg, noMatch + "c", res[3]);

		res = Strings.splitPreserveAllTokens(str, sepStr, 1);
		assertEquals(msg, 1, res.length);
		assertEquals(msg, str, res[0]);

		res = Strings.splitPreserveAllTokens(str, sepStr, 2);
		assertEquals(msg, 2, res.length);
		assertEquals(msg, "a", res[0]);
		assertEquals(msg, str.substring(2), res[1]);
	}

	@Test
	public void testSplitByCharacterType() {
		assertNull(Strings.splitByCharacterType(null));
		assertEquals(0, Strings.splitByCharacterType("").length);

		assertTrue(Arrays.equals(new String[] { "ab", " ", "de", " ", "fg" }, Strings.splitByCharacterType("ab de fg")));

		assertTrue(Arrays.equals(new String[] { "ab", "   ", "de", " ", "fg" },
			Strings.splitByCharacterType("ab   de fg")));

		assertTrue(Arrays.equals(new String[] { "ab", ":", "cd", ":", "ef" }, Strings.splitByCharacterType("ab:cd:ef")));

		assertTrue(Arrays.equals(new String[] { "number", "5" }, Strings.splitByCharacterType("number5")));

		assertTrue(Arrays.equals(new String[] { "foo", "B", "ar" }, Strings.splitByCharacterType("fooBar")));

		assertTrue(Arrays.equals(new String[] { "foo", "200", "B", "ar" }, Strings.splitByCharacterType("foo200Bar")));

		assertTrue(Arrays.equals(new String[] { "ASFR", "ules" }, Strings.splitByCharacterType("ASFRules")));
	}

	@Test
	public void testSplitByCharacterTypeCamelCase() {
		assertNull(Strings.splitByCharacterTypeCamelCase(null));
		assertEquals(0, Strings.splitByCharacterTypeCamelCase("").length);

		assertTrue(Arrays.equals(new String[] { "ab", " ", "de", " ", "fg" },
			Strings.splitByCharacterTypeCamelCase("ab de fg")));

		assertTrue(Arrays.equals(new String[] { "ab", "   ", "de", " ", "fg" },
			Strings.splitByCharacterTypeCamelCase("ab   de fg")));

		assertTrue(Arrays.equals(new String[] { "ab", ":", "cd", ":", "ef" },
			Strings.splitByCharacterTypeCamelCase("ab:cd:ef")));

		assertTrue(Arrays.equals(new String[] { "number", "5" }, Strings.splitByCharacterTypeCamelCase("number5")));

		assertTrue(Arrays.equals(new String[] { "foo", "Bar" }, Strings.splitByCharacterTypeCamelCase("fooBar")));

		assertTrue(Arrays.equals(new String[] { "foo", "200", "Bar" },
			Strings.splitByCharacterTypeCamelCase("foo200Bar")));

		assertTrue(Arrays.equals(new String[] { "ASF", "Rules" }, Strings.splitByCharacterTypeCamelCase("ASFRules")));
	}

	@Test
	public void testDeleteWhitespace_String() {
		assertEquals(null, Strings.deleteWhitespace(null));
		assertEquals("", Strings.deleteWhitespace(""));
		assertEquals("", Strings.deleteWhitespace("  \u000C  \t\t\u001F\n\n \u000B  "));
		assertEquals("", Strings.deleteWhitespace(StringsTest.WHITESPACE));
		assertEquals(StringsTest.NON_WHITESPACE, Strings.deleteWhitespace(StringsTest.NON_WHITESPACE));
		// Note: u-2007 and u-000A both cause problems in the source code
		// it should ignore 2007 but delete 000A
		assertEquals("\u00A0\u202F", Strings.deleteWhitespace("  \u00A0  \t\t\n\n \u202F  "));
		assertEquals("\u00A0\u202F", Strings.deleteWhitespace("\u00A0\u202F"));
		assertEquals("test", Strings.deleteWhitespace("\u000Bt  \t\n\u0009e\rs\n\n   \tt"));
	}

	@Test
	public void testLang623() {
		assertEquals("t", Strings.replaceChars("\u00DE", '\u00DE', 't'));
		assertEquals("t", Strings.replaceChars("\u00FE", '\u00FE', 't'));
	}

	@Test
	public void testReplace_StringStringString() {
		assertEquals(null, Strings.replace(null, null, null));
		assertEquals(null, Strings.replace(null, null, "any"));
		assertEquals(null, Strings.replace(null, "any", null));
		assertEquals(null, Strings.replace(null, "any", "any"));

		assertEquals("", Strings.replace("", null, null));
		assertEquals("", Strings.replace("", null, "any"));
		assertEquals("", Strings.replace("", "any", null));
		assertEquals("", Strings.replace("", "any", "any"));

		assertEquals("FOO", Strings.replace("FOO", "", "any"));
		assertEquals("FOO", Strings.replace("FOO", null, "any"));
		assertEquals("FOO", Strings.replace("FOO", "F", null));
		assertEquals("FOO", Strings.replace("FOO", null, null));

		assertEquals("", Strings.replace("foofoofoo", "foo", ""));
		assertEquals("barbarbar", Strings.replace("foofoofoo", "foo", "bar"));
		assertEquals("farfarfar", Strings.replace("foofoofoo", "oo", "ar"));
	}

	@Test
	public void testReplacePattern() {
		assertEquals("X", Strings.replacePattern("<A>\nxy\n</A>", "<A>.*</A>", "X"));
	}

	@Test
	public void testRemovePattern() {
		assertEquals("", Strings.removePattern("<A>x\\ny</A>", "<A>.*</A>"));
	}

	@Test
	public void testReplace_StringStringStringInt() {
		assertEquals(null, Strings.replace(null, null, null, 2));
		assertEquals(null, Strings.replace(null, null, "any", 2));
		assertEquals(null, Strings.replace(null, "any", null, 2));
		assertEquals(null, Strings.replace(null, "any", "any", 2));

		assertEquals("", Strings.replace("", null, null, 2));
		assertEquals("", Strings.replace("", null, "any", 2));
		assertEquals("", Strings.replace("", "any", null, 2));
		assertEquals("", Strings.replace("", "any", "any", 2));

		final String str = new String(new char[] { 'o', 'o', 'f', 'o', 'o' });
		assertSame(str, Strings.replace(str, "x", "", -1));

		assertEquals("f", Strings.replace("oofoo", "o", "", -1));
		assertEquals("oofoo", Strings.replace("oofoo", "o", "", 0));
		assertEquals("ofoo", Strings.replace("oofoo", "o", "", 1));
		assertEquals("foo", Strings.replace("oofoo", "o", "", 2));
		assertEquals("fo", Strings.replace("oofoo", "o", "", 3));
		assertEquals("f", Strings.replace("oofoo", "o", "", 4));

		assertEquals("f", Strings.replace("oofoo", "o", "", -5));
		assertEquals("f", Strings.replace("oofoo", "o", "", 1000));
	}

	@Test
	public void testReplaceOnce_StringStringString() {
		assertEquals(null, Strings.replaceOnce(null, null, null));
		assertEquals(null, Strings.replaceOnce(null, null, "any"));
		assertEquals(null, Strings.replaceOnce(null, "any", null));
		assertEquals(null, Strings.replaceOnce(null, "any", "any"));

		assertEquals("", Strings.replaceOnce("", null, null));
		assertEquals("", Strings.replaceOnce("", null, "any"));
		assertEquals("", Strings.replaceOnce("", "any", null));
		assertEquals("", Strings.replaceOnce("", "any", "any"));

		assertEquals("FOO", Strings.replaceOnce("FOO", "", "any"));
		assertEquals("FOO", Strings.replaceOnce("FOO", null, "any"));
		assertEquals("FOO", Strings.replaceOnce("FOO", "F", null));
		assertEquals("FOO", Strings.replaceOnce("FOO", null, null));

		assertEquals("foofoo", Strings.replaceOnce("foofoofoo", "foo", ""));
	}

	/**
	 * Test method for 'Strings.replaceEach(String, String[], String[])'
	 */
	@Test
	public void testReplace_StringStringArrayStringArray() {
		// JAVADOC TESTS START
		assertNull(Strings.replaceEach(null, new String[] { "a" }, new String[] { "b" }));
		assertEquals(Strings.replaceEach("", new String[] { "a" }, new String[] { "b" }), "");
		assertEquals(Strings.replaceEach("aba", null, null), "aba");
		assertEquals(Strings.replaceEach("aba", new String[0], null), "aba");
		assertEquals(Strings.replaceEach("aba", null, new String[0]), "aba");
		assertEquals(Strings.replaceEach("aba", new String[] { "a" }, null), "aba");

		assertEquals(Strings.replaceEach("aba", new String[] { "a" }, new String[] { "" }), "b");
		assertEquals(Strings.replaceEach("aba", new String[] { null }, new String[] { "a" }), "aba");
		assertEquals(Strings.replaceEach("abcde", new String[] { "ab", "d" }, new String[] { "w", "t" }), "wcte");
		assertEquals(Strings.replaceEach("abcde", new String[] { "ab", "d" }, new String[] { "d", "t" }), "dcte");
		// JAVADOC TESTS END

		assertEquals("bcc", Strings.replaceEach("abc", new String[] { "a", "b" }, new String[] { "b", "c" }));
		assertEquals(
			"q651.506bera",
			Strings.replaceEach("d216.102oren", new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
					"l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E",
					"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
					"Z", "1", "2", "3", "4", "5", "6", "7", "8", "9" }, new String[] { "n", "o", "p", "q", "r", "s",
					"t", "u", "v", "w", "x", "y", "z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
					"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "A", "B", "C", "D", "E", "F", "G",
					"H", "I", "J", "K", "L", "M", "5", "6", "7", "8", "9", "1", "2", "3", "4" }));

		// Test null safety inside arrays - LANG-552
		assertEquals(Strings.replaceEach("aba", new String[] { "a" }, new String[] { null }), "aba");
		assertEquals(Strings.replaceEach("aba", new String[] { "a", "b" }, new String[] { "c", null }), "cbc");
	}

	/**
	 * Test method for 'Strings.replaceEachRepeatedly(String, String[], String[])'
	 */
	@Test
	public void testReplace_StringStringArrayStringArrayBoolean() {
		// JAVADOC TESTS START
		assertNull(Strings.replaceEachRepeatedly(null, new String[] { "a" }, new String[] { "b" }));
		assertEquals(Strings.replaceEachRepeatedly("", new String[] { "a" }, new String[] { "b" }), "");
		assertEquals(Strings.replaceEachRepeatedly("aba", null, null), "aba");
		assertEquals(Strings.replaceEachRepeatedly("aba", new String[0], null), "aba");
		assertEquals(Strings.replaceEachRepeatedly("aba", null, new String[0]), "aba");
		assertEquals(Strings.replaceEachRepeatedly("aba", new String[0], null), "aba");

		assertEquals(Strings.replaceEachRepeatedly("aba", new String[] { "a" }, new String[] { "" }), "b");
		assertEquals(Strings.replaceEachRepeatedly("aba", new String[] { null }, new String[] { "a" }), "aba");
		assertEquals(Strings.replaceEachRepeatedly("abcde", new String[] { "ab", "d" }, new String[] { "w", "t" }),
			"wcte");
		assertEquals(Strings.replaceEachRepeatedly("abcde", new String[] { "ab", "d" }, new String[] { "d", "t" }),
			"tcte");

		try {
			Strings.replaceEachRepeatedly("abcde", new String[] { "ab", "d" }, new String[] { "d", "ab" });
			fail("Should be a circular reference");
		}
		catch (final IllegalStateException e) {
		}

		// JAVADOC TESTS END
	}

	@Test
	public void testReplaceChars_StringCharChar() {
		assertEquals(null, Strings.replaceChars(null, 'b', 'z'));
		assertEquals("", Strings.replaceChars("", 'b', 'z'));
		assertEquals("azcza", Strings.replaceChars("abcba", 'b', 'z'));
		assertEquals("abcba", Strings.replaceChars("abcba", 'x', 'z'));
	}

	@Test
	public void testReplaceChars_StringStringString() {
		assertEquals(null, Strings.replaceChars(null, null, null));
		assertEquals(null, Strings.replaceChars(null, "", null));
		assertEquals(null, Strings.replaceChars(null, "a", null));
		assertEquals(null, Strings.replaceChars(null, null, ""));
		assertEquals(null, Strings.replaceChars(null, null, "x"));

		assertEquals("", Strings.replaceChars("", null, null));
		assertEquals("", Strings.replaceChars("", "", null));
		assertEquals("", Strings.replaceChars("", "a", null));
		assertEquals("", Strings.replaceChars("", null, ""));
		assertEquals("", Strings.replaceChars("", null, "x"));

		assertEquals("abc", Strings.replaceChars("abc", null, null));
		assertEquals("abc", Strings.replaceChars("abc", null, ""));
		assertEquals("abc", Strings.replaceChars("abc", null, "x"));

		assertEquals("abc", Strings.replaceChars("abc", "", null));
		assertEquals("abc", Strings.replaceChars("abc", "", ""));
		assertEquals("abc", Strings.replaceChars("abc", "", "x"));

		assertEquals("ac", Strings.replaceChars("abc", "b", null));
		assertEquals("ac", Strings.replaceChars("abc", "b", ""));
		assertEquals("axc", Strings.replaceChars("abc", "b", "x"));

		assertEquals("ayzya", Strings.replaceChars("abcba", "bc", "yz"));
		assertEquals("ayya", Strings.replaceChars("abcba", "bc", "y"));
		assertEquals("ayzya", Strings.replaceChars("abcba", "bc", "yzx"));

		assertEquals("abcba", Strings.replaceChars("abcba", "z", "w"));
		assertSame("abcba", Strings.replaceChars("abcba", "z", "w"));

		// Javadoc examples:
		assertEquals("jelly", Strings.replaceChars("hello", "ho", "jy"));
		assertEquals("ayzya", Strings.replaceChars("abcba", "bc", "yz"));
		assertEquals("ayya", Strings.replaceChars("abcba", "bc", "y"));
		assertEquals("ayzya", Strings.replaceChars("abcba", "bc", "yzx"));

		// From http://issues.apache.org/bugzilla/show_bug.cgi?id=25454
		assertEquals("bcc", Strings.replaceChars("abc", "ab", "bc"));
		assertEquals("q651.506bera", Strings.replaceChars("d216.102oren",
			"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789",
			"nopqrstuvwxyzabcdefghijklmNOPQRSTUVWXYZABCDEFGHIJKLM567891234"));
	}

	@Test
	public void testOverlay_StringStringIntInt() {
		assertEquals(null, Strings.overlay(null, null, 2, 4));
		assertEquals(null, Strings.overlay(null, null, -2, -4));

		assertEquals("", Strings.overlay("", null, 0, 0));
		assertEquals("", Strings.overlay("", "", 0, 0));
		assertEquals("zzzz", Strings.overlay("", "zzzz", 0, 0));
		assertEquals("zzzz", Strings.overlay("", "zzzz", 2, 4));
		assertEquals("zzzz", Strings.overlay("", "zzzz", -2, -4));

		assertEquals("abef", Strings.overlay("abcdef", null, 2, 4));
		assertEquals("abef", Strings.overlay("abcdef", null, 4, 2));
		assertEquals("abef", Strings.overlay("abcdef", "", 2, 4));
		assertEquals("abef", Strings.overlay("abcdef", "", 4, 2));
		assertEquals("abzzzzef", Strings.overlay("abcdef", "zzzz", 2, 4));
		assertEquals("abzzzzef", Strings.overlay("abcdef", "zzzz", 4, 2));

		assertEquals("zzzzef", Strings.overlay("abcdef", "zzzz", -1, 4));
		assertEquals("zzzzef", Strings.overlay("abcdef", "zzzz", 4, -1));
		assertEquals("zzzzabcdef", Strings.overlay("abcdef", "zzzz", -2, -1));
		assertEquals("zzzzabcdef", Strings.overlay("abcdef", "zzzz", -1, -2));
		assertEquals("abcdzzzz", Strings.overlay("abcdef", "zzzz", 4, 10));
		assertEquals("abcdzzzz", Strings.overlay("abcdef", "zzzz", 10, 4));
		assertEquals("abcdefzzzz", Strings.overlay("abcdef", "zzzz", 8, 10));
		assertEquals("abcdefzzzz", Strings.overlay("abcdef", "zzzz", 10, 8));
	}

	@Test
	public void testRepeat_StringInt() {
		assertEquals(null, Strings.repeat(null, 2));
		assertEquals("", Strings.repeat("ab", 0));
		assertEquals("", Strings.repeat("", 3));
		assertEquals("aaa", Strings.repeat("a", 3));
		assertEquals("ababab", Strings.repeat("ab", 3));
		assertEquals("abcabcabc", Strings.repeat("abc", 3));
		final String str = Strings.repeat("a", 10000); // bigger than pad limit
		assertEquals(10000, str.length());
		assertTrue(Strings.containsOnly(str, new char[] { 'a' }));
	}

	@Test
	public void testRepeat_StringStringInt() {
		assertEquals(null, Strings.repeat(null, null, 2));
		assertEquals(null, Strings.repeat(null, "x", 2));
		assertEquals("", Strings.repeat("", null, 2));

		assertEquals("", Strings.repeat("ab", "", 0));
		assertEquals("", Strings.repeat("", "", 2));

		assertEquals("xx", Strings.repeat("", "x", 3));

		assertEquals("?, ?, ?", Strings.repeat("?", ", ", 3));
	}

	@Test
	public void testChop() {

		final String[][] chopCases = { { FOO_UNCAP + "\r\n", FOO_UNCAP }, { FOO_UNCAP + "\n", FOO_UNCAP },
				{ FOO_UNCAP + "\r", FOO_UNCAP }, { FOO_UNCAP + " \r", FOO_UNCAP + " " }, { "foo", "fo" },
				{ "foo\nfoo", "foo\nfo" }, { "\n", "" }, { "\r", "" }, { "\r\n", "" }, { null, null }, { "", "" },
				{ "a", "" }, };
		for (final String[] chopCase : chopCases) {
			final String original = chopCase[0];
			final String expectedResult = chopCase[1];
			assertEquals("chop(String) failed", expectedResult, Strings.chop(original));
		}
	}

	@SuppressWarnings("deprecation")
	// intentional test of deprecated method
	@Test
	public void testChomp() {

		final String[][] chompCases = { { FOO_UNCAP + "\r\n", FOO_UNCAP }, { FOO_UNCAP + "\n", FOO_UNCAP },
				{ FOO_UNCAP + "\r", FOO_UNCAP }, { FOO_UNCAP + " \r", FOO_UNCAP + " " }, { FOO_UNCAP, FOO_UNCAP },
				{ FOO_UNCAP + "\n\n", FOO_UNCAP + "\n" }, { FOO_UNCAP + "\r\n\r\n", FOO_UNCAP + "\r\n" },
				{ "foo\nfoo", "foo\nfoo" }, { "foo\n\rfoo", "foo\n\rfoo" }, { "\n", "" }, { "\r", "" }, { "a", "a" },
				{ "\r\n", "" }, { "", "" }, { null, null }, { FOO_UNCAP + "\n\r", FOO_UNCAP + "\n" } };
		for (final String[] chompCase : chompCases) {
			final String original = chompCase[0];
			final String expectedResult = chompCase[1];
			assertEquals("chomp(String) failed", expectedResult, Strings.chomp(original));
		}

		assertEquals("chomp(String, String) failed", "foo", Strings.chomp("foobar", "bar"));
		assertEquals("chomp(String, String) failed", "foobar", Strings.chomp("foobar", "baz"));
		assertEquals("chomp(String, String) failed", "foo", Strings.chomp("foo", "foooo"));
		assertEquals("chomp(String, String) failed", "foobar", Strings.chomp("foobar", ""));
		assertEquals("chomp(String, String) failed", "foobar", Strings.chomp("foobar", null));
		assertEquals("chomp(String, String) failed", "", Strings.chomp("", "foo"));
		assertEquals("chomp(String, String) failed", "", Strings.chomp("", null));
		assertEquals("chomp(String, String) failed", "", Strings.chomp("", ""));
		assertEquals("chomp(String, String) failed", null, Strings.chomp(null, "foo"));
		assertEquals("chomp(String, String) failed", null, Strings.chomp(null, null));
		assertEquals("chomp(String, String) failed", null, Strings.chomp(null, ""));
		assertEquals("chomp(String, String) failed", "", Strings.chomp("foo", "foo"));
		assertEquals("chomp(String, String) failed", " ", Strings.chomp(" foo", "foo"));
		assertEquals("chomp(String, String) failed", "foo ", Strings.chomp("foo ", "foo"));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testRightPad_StringInt() {
		assertEquals(null, Strings.rightPad(null, 5));
		assertEquals("     ", Strings.rightPad("", 5));
		assertEquals("abc  ", Strings.rightPad("abc", 5));
		assertEquals("abc", Strings.rightPad("abc", 2));
		assertEquals("abc", Strings.rightPad("abc", -1));
	}

	@Test
	public void testRightPad_StringIntChar() {
		assertEquals(null, Strings.rightPad(null, 5, ' '));
		assertEquals("     ", Strings.rightPad("", 5, ' '));
		assertEquals("abc  ", Strings.rightPad("abc", 5, ' '));
		assertEquals("abc", Strings.rightPad("abc", 2, ' '));
		assertEquals("abc", Strings.rightPad("abc", -1, ' '));
		assertEquals("abcxx", Strings.rightPad("abc", 5, 'x'));
		final String str = Strings.rightPad("aaa", 10000, 'a'); // bigger than pad length
		assertEquals(10000, str.length());
		assertTrue(Strings.containsOnly(str, new char[] { 'a' }));
	}

	@Test
	public void testRightPad_StringIntString() {
		assertEquals(null, Strings.rightPad(null, 5, "-+"));
		assertEquals("     ", Strings.rightPad("", 5, " "));
		assertEquals(null, Strings.rightPad(null, 8, null));
		assertEquals("abc-+-+", Strings.rightPad("abc", 7, "-+"));
		assertEquals("abc-+~", Strings.rightPad("abc", 6, "-+~"));
		assertEquals("abc-+", Strings.rightPad("abc", 5, "-+~"));
		assertEquals("abc", Strings.rightPad("abc", 2, " "));
		assertEquals("abc", Strings.rightPad("abc", -1, " "));
		assertEquals("abc  ", Strings.rightPad("abc", 5, null));
		assertEquals("abc  ", Strings.rightPad("abc", 5, ""));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testLeftPad_StringInt() {
		assertEquals(null, Strings.leftPad(null, 5));
		assertEquals("     ", Strings.leftPad("", 5));
		assertEquals("  abc", Strings.leftPad("abc", 5));
		assertEquals("abc", Strings.leftPad("abc", 2));
	}

	@Test
	public void testLeftPad_StringIntChar() {
		assertEquals(null, Strings.leftPad(null, 5, ' '));
		assertEquals("     ", Strings.leftPad("", 5, ' '));
		assertEquals("  abc", Strings.leftPad("abc", 5, ' '));
		assertEquals("xxabc", Strings.leftPad("abc", 5, 'x'));
		assertEquals("\uffff\uffffabc", Strings.leftPad("abc", 5, '\uffff'));
		assertEquals("abc", Strings.leftPad("abc", 2, ' '));
		final String str = Strings.leftPad("aaa", 10000, 'a'); // bigger than pad length
		assertEquals(10000, str.length());
		assertTrue(Strings.containsOnly(str, new char[] { 'a' }));
	}

	@Test
	public void testLeftPad_StringIntString() {
		assertEquals(null, Strings.leftPad(null, 5, "-+"));
		assertEquals(null, Strings.leftPad(null, 5, null));
		assertEquals("     ", Strings.leftPad("", 5, " "));
		assertEquals("-+-+abc", Strings.leftPad("abc", 7, "-+"));
		assertEquals("-+~abc", Strings.leftPad("abc", 6, "-+~"));
		assertEquals("-+abc", Strings.leftPad("abc", 5, "-+~"));
		assertEquals("abc", Strings.leftPad("abc", 2, " "));
		assertEquals("abc", Strings.leftPad("abc", -1, " "));
		assertEquals("  abc", Strings.leftPad("abc", 5, null));
		assertEquals("  abc", Strings.leftPad("abc", 5, ""));
	}

	@Test
	public void testLengthString() {
		assertEquals(0, Strings.length(null));
		assertEquals(0, Strings.length(""));
		assertEquals(0, Strings.length(Strings.EMPTY));
		assertEquals(1, Strings.length("A"));
		assertEquals(1, Strings.length(" "));
		assertEquals(8, Strings.length("ABCDEFGH"));
	}

	@Test
	public void testLengthStringBuffer() {
		assertEquals(0, Strings.length(new StringBuffer("")));
		assertEquals(0, Strings.length(new StringBuffer(Strings.EMPTY)));
		assertEquals(1, Strings.length(new StringBuffer("A")));
		assertEquals(1, Strings.length(new StringBuffer(" ")));
		assertEquals(8, Strings.length(new StringBuffer("ABCDEFGH")));
	}

	@Test
	public void testLengthStringBuilder() {
		assertEquals(0, Strings.length(new StringBuilder("")));
		assertEquals(0, Strings.length(new StringBuilder(Strings.EMPTY)));
		assertEquals(1, Strings.length(new StringBuilder("A")));
		assertEquals(1, Strings.length(new StringBuilder(" ")));
		assertEquals(8, Strings.length(new StringBuilder("ABCDEFGH")));
	}

	@Test
	public void testLength_CharBuffer() {
		assertEquals(0, Strings.length(CharBuffer.wrap("")));
		assertEquals(1, Strings.length(CharBuffer.wrap("A")));
		assertEquals(1, Strings.length(CharBuffer.wrap(" ")));
		assertEquals(8, Strings.length(CharBuffer.wrap("ABCDEFGH")));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testCenter_StringInt() {
		assertEquals(null, Strings.center(null, -1));
		assertEquals(null, Strings.center(null, 4));
		assertEquals("    ", Strings.center("", 4));
		assertEquals("ab", Strings.center("ab", 0));
		assertEquals("ab", Strings.center("ab", -1));
		assertEquals("ab", Strings.center("ab", 1));
		assertEquals("    ", Strings.center("", 4));
		assertEquals(" ab ", Strings.center("ab", 4));
		assertEquals("abcd", Strings.center("abcd", 2));
		assertEquals(" a  ", Strings.center("a", 4));
		assertEquals("  a  ", Strings.center("a", 5));
	}

	@Test
	public void testCenter_StringIntChar() {
		assertEquals(null, Strings.center(null, -1, ' '));
		assertEquals(null, Strings.center(null, 4, ' '));
		assertEquals("    ", Strings.center("", 4, ' '));
		assertEquals("ab", Strings.center("ab", 0, ' '));
		assertEquals("ab", Strings.center("ab", -1, ' '));
		assertEquals("ab", Strings.center("ab", 1, ' '));
		assertEquals("    ", Strings.center("", 4, ' '));
		assertEquals(" ab ", Strings.center("ab", 4, ' '));
		assertEquals("abcd", Strings.center("abcd", 2, ' '));
		assertEquals(" a  ", Strings.center("a", 4, ' '));
		assertEquals("  a  ", Strings.center("a", 5, ' '));
		assertEquals("xxaxx", Strings.center("a", 5, 'x'));
	}

	@Test
	public void testCenter_StringIntString() {
		assertEquals(null, Strings.center(null, 4, null));
		assertEquals(null, Strings.center(null, -1, " "));
		assertEquals(null, Strings.center(null, 4, " "));
		assertEquals("    ", Strings.center("", 4, " "));
		assertEquals("ab", Strings.center("ab", 0, " "));
		assertEquals("ab", Strings.center("ab", -1, " "));
		assertEquals("ab", Strings.center("ab", 1, " "));
		assertEquals("    ", Strings.center("", 4, " "));
		assertEquals(" ab ", Strings.center("ab", 4, " "));
		assertEquals("abcd", Strings.center("abcd", 2, " "));
		assertEquals(" a  ", Strings.center("a", 4, " "));
		assertEquals("yayz", Strings.center("a", 4, "yz"));
		assertEquals("yzyayzy", Strings.center("a", 7, "yz"));
		assertEquals("  abc  ", Strings.center("abc", 7, null));
		assertEquals("  abc  ", Strings.center("abc", 7, ""));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testReverse_String() {
		assertEquals(null, Strings.reverse(null));
		assertEquals("", Strings.reverse(""));
		assertEquals("sdrawkcab", Strings.reverse("backwards"));
	}

	@Test
	public void testReverseDelimited_StringChar() {
		assertEquals(null, Strings.reverseDelimited(null, '.'));
		assertEquals("", Strings.reverseDelimited("", '.'));
		assertEquals("c.b.a", Strings.reverseDelimited("a.b.c", '.'));
		assertEquals("a b c", Strings.reverseDelimited("a b c", '.'));
		assertEquals("", Strings.reverseDelimited("", '.'));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testDefault_String() {
		assertEquals("", Strings.defaultString(null));
		assertEquals("", Strings.defaultString(""));
		assertEquals("abc", Strings.defaultString("abc"));
	}

	@Test
	public void testDefault_StringString() {
		assertEquals("NULL", Strings.defaultString(null, "NULL"));
		assertEquals("", Strings.defaultString("", "NULL"));
		assertEquals("abc", Strings.defaultString("abc", "NULL"));
	}

	@Test
	public void testDefaultIfEmpty_StringString() {
		assertEquals("NULL", Strings.defaultIfEmpty(null, "NULL"));
		assertEquals("NULL", Strings.defaultIfEmpty("", "NULL"));
		assertEquals("abc", Strings.defaultIfEmpty("abc", "NULL"));
		assertNull(Strings.defaultIfEmpty("", null));
		// Tests compatibility for the API return type
		final String s = Strings.defaultIfEmpty("abc", "NULL");
		assertEquals("abc", s);
	}

	@Test
	public void testDefaultIfBlank_StringString() {
		assertEquals("NULL", Strings.defaultIfBlank(null, "NULL"));
		assertEquals("NULL", Strings.defaultIfBlank("", "NULL"));
		assertEquals("NULL", Strings.defaultIfBlank(" ", "NULL"));
		assertEquals("abc", Strings.defaultIfBlank("abc", "NULL"));
		assertNull(Strings.defaultIfBlank("", null));
		// Tests compatibility for the API return type
		final String s = Strings.defaultIfBlank("abc", "NULL");
		assertEquals("abc", s);
	}

	@Test
	public void testDefaultIfEmpty_StringBuilders() {
		assertEquals("NULL", Strings.defaultIfEmpty(new StringBuilder(""), new StringBuilder("NULL")).toString());
		assertEquals("abc", Strings.defaultIfEmpty(new StringBuilder("abc"), new StringBuilder("NULL")).toString());
		assertNull(Strings.defaultIfEmpty(new StringBuilder(""), null));
		// Tests compatibility for the API return type
		final StringBuilder s = Strings.defaultIfEmpty(new StringBuilder("abc"), new StringBuilder("NULL"));
		assertEquals("abc", s.toString());
	}

	@Test
	public void testDefaultIfBlank_StringBuilders() {
		assertEquals("NULL", Strings.defaultIfBlank(new StringBuilder(""), new StringBuilder("NULL")).toString());
		assertEquals("NULL", Strings.defaultIfBlank(new StringBuilder(" "), new StringBuilder("NULL")).toString());
		assertEquals("abc", Strings.defaultIfBlank(new StringBuilder("abc"), new StringBuilder("NULL")).toString());
		assertNull(Strings.defaultIfBlank(new StringBuilder(""), null));
		// Tests compatibility for the API return type
		final StringBuilder s = Strings.defaultIfBlank(new StringBuilder("abc"), new StringBuilder("NULL"));
		assertEquals("abc", s.toString());
	}

	@Test
	public void testDefaultIfEmpty_StringBuffers() {
		assertEquals("NULL", Strings.defaultIfEmpty(new StringBuffer(""), new StringBuffer("NULL")).toString());
		assertEquals("abc", Strings.defaultIfEmpty(new StringBuffer("abc"), new StringBuffer("NULL")).toString());
		assertNull(Strings.defaultIfEmpty(new StringBuffer(""), null));
		// Tests compatibility for the API return type
		final StringBuffer s = Strings.defaultIfEmpty(new StringBuffer("abc"), new StringBuffer("NULL"));
		assertEquals("abc", s.toString());
	}

	@Test
	public void testDefaultIfBlank_StringBuffers() {
		assertEquals("NULL", Strings.defaultIfBlank(new StringBuffer(""), new StringBuffer("NULL")).toString());
		assertEquals("NULL", Strings.defaultIfBlank(new StringBuffer(" "), new StringBuffer("NULL")).toString());
		assertEquals("abc", Strings.defaultIfBlank(new StringBuffer("abc"), new StringBuffer("NULL")).toString());
		assertNull(Strings.defaultIfBlank(new StringBuffer(""), null));
		// Tests compatibility for the API return type
		final StringBuffer s = Strings.defaultIfBlank(new StringBuffer("abc"), new StringBuffer("NULL"));
		assertEquals("abc", s.toString());
	}

	@Test
	public void testDefaultIfEmpty_CharBuffers() {
		assertEquals("NULL", Strings.defaultIfEmpty(CharBuffer.wrap(""), CharBuffer.wrap("NULL")).toString());
		assertEquals("abc", Strings.defaultIfEmpty(CharBuffer.wrap("abc"), CharBuffer.wrap("NULL")).toString());
		assertNull(Strings.defaultIfEmpty(CharBuffer.wrap(""), null));
		// Tests compatibility for the API return type
		final CharBuffer s = Strings.defaultIfEmpty(CharBuffer.wrap("abc"), CharBuffer.wrap("NULL"));
		assertEquals("abc", s.toString());
	}

	@Test
	public void testDefaultIfBlank_CharBuffers() {
		assertEquals("NULL", Strings.defaultIfBlank(CharBuffer.wrap(""), CharBuffer.wrap("NULL")).toString());
		assertEquals("NULL", Strings.defaultIfBlank(CharBuffer.wrap(" "), CharBuffer.wrap("NULL")).toString());
		assertEquals("abc", Strings.defaultIfBlank(CharBuffer.wrap("abc"), CharBuffer.wrap("NULL")).toString());
		assertNull(Strings.defaultIfBlank(CharBuffer.wrap(""), null));
		// Tests compatibility for the API return type
		final CharBuffer s = Strings.defaultIfBlank(CharBuffer.wrap("abc"), CharBuffer.wrap("NULL"));
		assertEquals("abc", s.toString());
	}

	// -----------------------------------------------------------------------
	@Test
	public void testAbbreviate_StringInt() {
		assertEquals(null, Strings.abbreviate(null, 10));
		assertEquals("", Strings.abbreviate("", 10));
		assertEquals("short", Strings.abbreviate("short", 10));
		assertEquals("Now is ...",
			Strings.abbreviate("Now is the time for all good men to come to the aid of their party.", 10));

		final String raspberry = "raspberry peach";
		assertEquals("raspberry p...", Strings.abbreviate(raspberry, 14));
		assertEquals("raspberry peach", Strings.abbreviate("raspberry peach", 15));
		assertEquals("raspberry peach", Strings.abbreviate("raspberry peach", 16));
		assertEquals("abc...", Strings.abbreviate("abcdefg", 6));
		assertEquals("abcdefg", Strings.abbreviate("abcdefg", 7));
		assertEquals("abcdefg", Strings.abbreviate("abcdefg", 8));
		assertEquals("a...", Strings.abbreviate("abcdefg", 4));
		assertEquals("", Strings.abbreviate("", 4));

		try {
			@SuppressWarnings("unused")
			final String res = Strings.abbreviate("abc", 3);
			fail("Strings.abbreviate expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			// empty
		}
	}

	@Test
	public void testAbbreviate_StringIntInt() {
		assertEquals(null, Strings.abbreviate(null, 10, 12));
		assertEquals("", Strings.abbreviate("", 0, 10));
		assertEquals("", Strings.abbreviate("", 2, 10));

		try {
			@SuppressWarnings("unused")
			final String res = Strings.abbreviate("abcdefghij", 0, 3);
			fail("Strings.abbreviate expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			// empty
		}
		try {
			@SuppressWarnings("unused")
			final String res = Strings.abbreviate("abcdefghij", 5, 6);
			fail("Strings.abbreviate expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			// empty
		}

		final String raspberry = "raspberry peach";
		assertEquals("raspberry peach", Strings.abbreviate(raspberry, 11, 15));

		assertEquals(null, Strings.abbreviate(null, 7, 14));
		assertAbbreviateWithOffset("abcdefg...", -1, 10);
		assertAbbreviateWithOffset("abcdefg...", 0, 10);
		assertAbbreviateWithOffset("abcdefg...", 1, 10);
		assertAbbreviateWithOffset("abcdefg...", 2, 10);
		assertAbbreviateWithOffset("abcdefg...", 3, 10);
		assertAbbreviateWithOffset("abcdefg...", 4, 10);
		assertAbbreviateWithOffset("...fghi...", 5, 10);
		assertAbbreviateWithOffset("...ghij...", 6, 10);
		assertAbbreviateWithOffset("...hijk...", 7, 10);
		assertAbbreviateWithOffset("...ijklmno", 8, 10);
		assertAbbreviateWithOffset("...ijklmno", 9, 10);
		assertAbbreviateWithOffset("...ijklmno", 10, 10);
		assertAbbreviateWithOffset("...ijklmno", 10, 10);
		assertAbbreviateWithOffset("...ijklmno", 11, 10);
		assertAbbreviateWithOffset("...ijklmno", 12, 10);
		assertAbbreviateWithOffset("...ijklmno", 13, 10);
		assertAbbreviateWithOffset("...ijklmno", 14, 10);
		assertAbbreviateWithOffset("...ijklmno", 15, 10);
		assertAbbreviateWithOffset("...ijklmno", 16, 10);
		assertAbbreviateWithOffset("...ijklmno", Integer.MAX_VALUE, 10);
	}

	private void assertAbbreviateWithOffset(final String expected, final int offset, final int maxWidth) {
		final String abcdefghijklmno = "abcdefghijklmno";
		final String message = "abbreviate(String,int,int) failed";
		final String actual = Strings.abbreviate(abcdefghijklmno, offset, maxWidth);
		if (offset >= 0 && offset < abcdefghijklmno.length()) {
			assertTrue(message + " -- should contain offset character", actual.indexOf((char)('a' + offset)) != -1);
		}
		assertTrue(message + " -- should not be greater than maxWidth", actual.length() <= maxWidth);
		assertEquals(message, expected, actual);
	}

	@Test
	public void testAbbreviateMiddle() {
		// javadoc examples
		assertNull(Strings.abbreviateMiddle(null, null, 0));
		assertEquals("abc", Strings.abbreviateMiddle("abc", null, 0));
		assertEquals("abc", Strings.abbreviateMiddle("abc", ".", 0));
		assertEquals("abc", Strings.abbreviateMiddle("abc", ".", 3));
		assertEquals("ab.f", Strings.abbreviateMiddle("abcdef", ".", 4));

		// JIRA issue (LANG-405) example (slightly different than actual expected result)
		assertEquals("A very long text with un...f the text is complete.", Strings.abbreviateMiddle(
			"A very long text with unimportant stuff in the middle but interesting start and "
					+ "end to see if the text is complete.", "...", 50));

		// Test a much longer text :)
		final String longText = "Start text" + Strings.repeat("x", 10000) + "Close text";
		assertEquals("Start text->Close text", Strings.abbreviateMiddle(longText, "->", 22));

		// Test negative length
		assertEquals("abc", Strings.abbreviateMiddle("abc", ".", -1));

		// Test boundaries
		// Fails to change anything as method ensures first and last char are kept
		assertEquals("abc", Strings.abbreviateMiddle("abc", ".", 1));
		assertEquals("abc", Strings.abbreviateMiddle("abc", ".", 2));

		// Test length of n=1
		assertEquals("a", Strings.abbreviateMiddle("a", ".", 1));

		// Test smallest length that can lead to success
		assertEquals("a.d", Strings.abbreviateMiddle("abcd", ".", 3));

		// More from LANG-405
		assertEquals("a..f", Strings.abbreviateMiddle("abcdef", "..", 4));
		assertEquals("ab.ef", Strings.abbreviateMiddle("abcdef", ".", 5));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testDifference_StringString() {
		assertEquals(null, Strings.difference(null, null));
		assertEquals("", Strings.difference("", ""));
		assertEquals("abc", Strings.difference("", "abc"));
		assertEquals("", Strings.difference("abc", ""));
		assertEquals("i am a robot", Strings.difference(null, "i am a robot"));
		assertEquals("i am a machine", Strings.difference("i am a machine", null));
		assertEquals("robot", Strings.difference("i am a machine", "i am a robot"));
		assertEquals("", Strings.difference("abc", "abc"));
		assertEquals("you are a robot", Strings.difference("i am a robot", "you are a robot"));
	}

	@Test
	public void testDifferenceAt_StringString() {
		assertEquals(-1, Strings.indexOfDifference(null, null));
		assertEquals(0, Strings.indexOfDifference(null, "i am a robot"));
		assertEquals(-1, Strings.indexOfDifference("", ""));
		assertEquals(0, Strings.indexOfDifference("", "abc"));
		assertEquals(0, Strings.indexOfDifference("abc", ""));
		assertEquals(0, Strings.indexOfDifference("i am a machine", null));
		assertEquals(7, Strings.indexOfDifference("i am a machine", "i am a robot"));
		assertEquals(-1, Strings.indexOfDifference("foo", "foo"));
		assertEquals(0, Strings.indexOfDifference("i am a robot", "you are a robot"));
		// System.out.println("indexOfDiff: " + Strings.indexOfDifference("i am a robot",
		// "not machine"));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testGetLevenshteinDistance_StringString() {
		assertEquals(0, Strings.getLevenshteinDistance("", ""));
		assertEquals(1, Strings.getLevenshteinDistance("", "a"));
		assertEquals(7, Strings.getLevenshteinDistance("aaapppp", ""));
		assertEquals(1, Strings.getLevenshteinDistance("frog", "fog"));
		assertEquals(3, Strings.getLevenshteinDistance("fly", "ant"));
		assertEquals(7, Strings.getLevenshteinDistance("elephant", "hippo"));
		assertEquals(7, Strings.getLevenshteinDistance("hippo", "elephant"));
		assertEquals(8, Strings.getLevenshteinDistance("hippo", "zzzzzzzz"));
		assertEquals(8, Strings.getLevenshteinDistance("zzzzzzzz", "hippo"));
		assertEquals(1, Strings.getLevenshteinDistance("hello", "hallo"));
		try {
			@SuppressWarnings("unused")
			final int d = Strings.getLevenshteinDistance("a", null);
			fail("expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			// empty
		}
		try {
			@SuppressWarnings("unused")
			final int d = Strings.getLevenshteinDistance(null, "a");
			fail("expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			// empty
		}
	}

	@Test
	public void testGetLevenshteinDistance_StringStringInt() {
		// empty strings
		assertEquals(0, Strings.getLevenshteinDistance("", "", 0));
		assertEquals(7, Strings.getLevenshteinDistance("aaapppp", "", 8));
		assertEquals(7, Strings.getLevenshteinDistance("aaapppp", "", 7));
		assertEquals(-1, Strings.getLevenshteinDistance("aaapppp", "", 6));

		// unequal strings, zero threshold
		assertEquals(-1, Strings.getLevenshteinDistance("b", "a", 0));
		assertEquals(-1, Strings.getLevenshteinDistance("a", "b", 0));

		// equal strings
		assertEquals(0, Strings.getLevenshteinDistance("aa", "aa", 0));
		assertEquals(0, Strings.getLevenshteinDistance("aa", "aa", 2));

		// same length
		assertEquals(-1, Strings.getLevenshteinDistance("aaa", "bbb", 2));
		assertEquals(3, Strings.getLevenshteinDistance("aaa", "bbb", 3));

		// big stripe
		assertEquals(6, Strings.getLevenshteinDistance("aaaaaa", "b", 10));

		// distance less than threshold
		assertEquals(7, Strings.getLevenshteinDistance("aaapppp", "b", 8));
		assertEquals(3, Strings.getLevenshteinDistance("a", "bbb", 4));

		// distance equal to threshold
		assertEquals(7, Strings.getLevenshteinDistance("aaapppp", "b", 7));
		assertEquals(3, Strings.getLevenshteinDistance("a", "bbb", 3));

		// distance greater than threshold
		assertEquals(-1, Strings.getLevenshteinDistance("a", "bbb", 2));
		assertEquals(-1, Strings.getLevenshteinDistance("bbb", "a", 2));
		assertEquals(-1, Strings.getLevenshteinDistance("aaapppp", "b", 6));

		// stripe runs off array, strings not similar
		assertEquals(-1, Strings.getLevenshteinDistance("a", "bbb", 1));
		assertEquals(-1, Strings.getLevenshteinDistance("bbb", "a", 1));

		// stripe runs off array, strings are similar
		assertEquals(-1, Strings.getLevenshteinDistance("12345", "1234567", 1));
		assertEquals(-1, Strings.getLevenshteinDistance("1234567", "12345", 1));

		// old getLevenshteinDistance test cases
		assertEquals(1, Strings.getLevenshteinDistance("frog", "fog", 1));
		assertEquals(3, Strings.getLevenshteinDistance("fly", "ant", 3));
		assertEquals(7, Strings.getLevenshteinDistance("elephant", "hippo", 7));
		assertEquals(-1, Strings.getLevenshteinDistance("elephant", "hippo", 6));
		assertEquals(7, Strings.getLevenshteinDistance("hippo", "elephant", 7));
		assertEquals(-1, Strings.getLevenshteinDistance("hippo", "elephant", 6));
		assertEquals(8, Strings.getLevenshteinDistance("hippo", "zzzzzzzz", 8));
		assertEquals(8, Strings.getLevenshteinDistance("zzzzzzzz", "hippo", 8));
		assertEquals(1, Strings.getLevenshteinDistance("hello", "hallo", 1));

		// exceptions
		try {
			@SuppressWarnings("unused")
			final int d = Strings.getLevenshteinDistance("a", null, 0);
			fail("expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			// empty
		}
		try {
			@SuppressWarnings("unused")
			final int d = Strings.getLevenshteinDistance(null, "a", 0);
			fail("expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			// empty
		}

		try {
			@SuppressWarnings("unused")
			final int d = Strings.getLevenshteinDistance("a", "a", -1);
			fail("expecting IllegalArgumentException");
		}
		catch (final IllegalArgumentException ex) {
			// empty
		}
	}

	/**
	 * A sanity check for {@link Strings#EMPTY}.
	 */
	@Test
	public void testEMPTY() {
		assertNotNull(Strings.EMPTY);
		assertEquals("", Strings.EMPTY);
		assertEquals(0, Strings.EMPTY.length());
	}

	/**
	 * Test for {@link Strings#isAllLowerCase(CharSequence)}.
	 */
	@Test
	public void testIsAllLowerCase() {
		assertFalse(Strings.isAllLowerCase(null));
		assertFalse(Strings.isAllLowerCase(Strings.EMPTY));
		assertTrue(Strings.isAllLowerCase("abc"));
		assertFalse(Strings.isAllLowerCase("abc "));
		assertFalse(Strings.isAllLowerCase("abC"));
	}

	/**
	 * Test for {@link Strings#isAllUpperCase(CharSequence)}.
	 */
	@Test
	public void testIsAllUpperCase() {
		assertFalse(Strings.isAllUpperCase(null));
		assertFalse(Strings.isAllUpperCase(Strings.EMPTY));
		assertTrue(Strings.isAllUpperCase("ABC"));
		assertFalse(Strings.isAllUpperCase("ABC "));
		assertFalse(Strings.isAllUpperCase("aBC"));
	}

	@Test
	public void testRemoveStart() {
		// Strings.removeStart("", *) = ""
		assertNull(Strings.removeStart(null, null));
		assertNull(Strings.removeStart(null, ""));
		assertNull(Strings.removeStart(null, "a"));

		// Strings.removeStart(*, null) = *
		assertEquals(Strings.removeStart("", null), "");
		assertEquals(Strings.removeStart("", ""), "");
		assertEquals(Strings.removeStart("", "a"), "");

		// All others:
		assertEquals(Strings.removeStart("www.domain.com", "www."), "domain.com");
		assertEquals(Strings.removeStart("domain.com", "www."), "domain.com");
		assertEquals(Strings.removeStart("domain.com", ""), "domain.com");
		assertEquals(Strings.removeStart("domain.com", null), "domain.com");
	}

	@Test
	public void testRemoveStartIgnoreCase() {
		// Strings.removeStart("", *) = ""
		assertNull("removeStartIgnoreCase(null, null)", Strings.removeStartIgnoreCase(null, null));
		assertNull("removeStartIgnoreCase(null, \"\")", Strings.removeStartIgnoreCase(null, ""));
		assertNull("removeStartIgnoreCase(null, \"a\")", Strings.removeStartIgnoreCase(null, "a"));

		// Strings.removeStart(*, null) = *
		assertEquals("removeStartIgnoreCase(\"\", null)", Strings.removeStartIgnoreCase("", null), "");
		assertEquals("removeStartIgnoreCase(\"\", \"\")", Strings.removeStartIgnoreCase("", ""), "");
		assertEquals("removeStartIgnoreCase(\"\", \"a\")", Strings.removeStartIgnoreCase("", "a"), "");

		// All others:
		assertEquals("removeStartIgnoreCase(\"www.domain.com\", \"www.\")",
			Strings.removeStartIgnoreCase("www.domain.com", "www."), "domain.com");
		assertEquals("removeStartIgnoreCase(\"domain.com\", \"www.\")",
			Strings.removeStartIgnoreCase("domain.com", "www."), "domain.com");
		assertEquals("removeStartIgnoreCase(\"domain.com\", \"\")", Strings.removeStartIgnoreCase("domain.com", ""),
			"domain.com");
		assertEquals("removeStartIgnoreCase(\"domain.com\", null)", Strings.removeStartIgnoreCase("domain.com", null),
			"domain.com");

		// Case insensitive:
		assertEquals("removeStartIgnoreCase(\"www.domain.com\", \"WWW.\")",
			Strings.removeStartIgnoreCase("www.domain.com", "WWW."), "domain.com");
	}

	@Test
	public void testRemoveEnd() {
		// Strings.removeEnd("", *) = ""
		assertNull(Strings.removeEnd(null, null));
		assertNull(Strings.removeEnd(null, ""));
		assertNull(Strings.removeEnd(null, "a"));

		// Strings.removeEnd(*, null) = *
		assertEquals(Strings.removeEnd("", null), "");
		assertEquals(Strings.removeEnd("", ""), "");
		assertEquals(Strings.removeEnd("", "a"), "");

		// All others:
		assertEquals(Strings.removeEnd("www.domain.com.", ".com"), "www.domain.com.");
		assertEquals(Strings.removeEnd("www.domain.com", ".com"), "www.domain");
		assertEquals(Strings.removeEnd("www.domain", ".com"), "www.domain");
		assertEquals(Strings.removeEnd("domain.com", ""), "domain.com");
		assertEquals(Strings.removeEnd("domain.com", null), "domain.com");
	}

	@Test
	public void testRemoveEndIgnoreCase() {
		// Strings.removeEndIgnoreCase("", *) = ""
		assertNull("removeEndIgnoreCase(null, null)", Strings.removeEndIgnoreCase(null, null));
		assertNull("removeEndIgnoreCase(null, \"\")", Strings.removeEndIgnoreCase(null, ""));
		assertNull("removeEndIgnoreCase(null, \"a\")", Strings.removeEndIgnoreCase(null, "a"));

		// Strings.removeEnd(*, null) = *
		assertEquals("removeEndIgnoreCase(\"\", null)", Strings.removeEndIgnoreCase("", null), "");
		assertEquals("removeEndIgnoreCase(\"\", \"\")", Strings.removeEndIgnoreCase("", ""), "");
		assertEquals("removeEndIgnoreCase(\"\", \"a\")", Strings.removeEndIgnoreCase("", "a"), "");

		// All others:
		assertEquals("removeEndIgnoreCase(\"www.domain.com.\", \".com\")",
			Strings.removeEndIgnoreCase("www.domain.com.", ".com"), "www.domain.com.");
		assertEquals("removeEndIgnoreCase(\"www.domain.com\", \".com\")",
			Strings.removeEndIgnoreCase("www.domain.com", ".com"), "www.domain");
		assertEquals("removeEndIgnoreCase(\"www.domain\", \".com\")",
			Strings.removeEndIgnoreCase("www.domain", ".com"), "www.domain");
		assertEquals("removeEndIgnoreCase(\"domain.com\", \"\")", Strings.removeEndIgnoreCase("domain.com", ""),
			"domain.com");
		assertEquals("removeEndIgnoreCase(\"domain.com\", null)", Strings.removeEndIgnoreCase("domain.com", null),
			"domain.com");

		// Case insensitive:
		assertEquals("removeEndIgnoreCase(\"www.domain.com\", \".COM\")",
			Strings.removeEndIgnoreCase("www.domain.com", ".COM"), "www.domain");
		assertEquals("removeEndIgnoreCase(\"www.domain.COM\", \".com\")",
			Strings.removeEndIgnoreCase("www.domain.COM", ".com"), "www.domain");
	}

	@Test
	public void testRemove_String() {
		// Strings.remove(null, *) = null
		assertEquals(null, Strings.remove(null, null));
		assertEquals(null, Strings.remove(null, ""));
		assertEquals(null, Strings.remove(null, "a"));

		// Strings.remove("", *) = ""
		assertEquals("", Strings.remove("", null));
		assertEquals("", Strings.remove("", ""));
		assertEquals("", Strings.remove("", "a"));

		// Strings.remove(*, null) = *
		assertEquals(null, Strings.remove(null, null));
		assertEquals("", Strings.remove("", null));
		assertEquals("a", Strings.remove("a", null));

		// Strings.remove(*, "") = *
		assertEquals(null, Strings.remove(null, ""));
		assertEquals("", Strings.remove("", ""));
		assertEquals("a", Strings.remove("a", ""));

		// Strings.remove("queued", "ue") = "qd"
		assertEquals("qd", Strings.remove("queued", "ue"));

		// Strings.remove("queued", "zz") = "queued"
		assertEquals("queued", Strings.remove("queued", "zz"));
	}

	@Test
	public void testRemove_char() {
		// Strings.remove(null, *) = null
		assertEquals(null, Strings.remove(null, 'a'));
		assertEquals(null, Strings.remove(null, 'a'));
		assertEquals(null, Strings.remove(null, 'a'));

		// Strings.remove("", *) = ""
		assertEquals("", Strings.remove("", 'a'));
		assertEquals("", Strings.remove("", 'a'));
		assertEquals("", Strings.remove("", 'a'));

		// Strings.remove("queued", 'u') = "qeed"
		assertEquals("qeed", Strings.remove("queued", 'u'));

		// Strings.remove("queued", 'z') = "queued"
		assertEquals("queued", Strings.remove("queued", 'z'));
	}

	@Test
	public void testDifferenceAt_StringArray() {
		assertEquals(-1, Strings.indexOfDifference((String[])null));
		assertEquals(-1, Strings.indexOfDifference(new String[] {}));
		assertEquals(-1, Strings.indexOfDifference(new String[] { "abc" }));
		assertEquals(-1, Strings.indexOfDifference(new String[] { null, null }));
		assertEquals(-1, Strings.indexOfDifference(new String[] { "", "" }));
		assertEquals(0, Strings.indexOfDifference(new String[] { "", null }));
		assertEquals(0, Strings.indexOfDifference(new String[] { "abc", null, null }));
		assertEquals(0, Strings.indexOfDifference(new String[] { null, null, "abc" }));
		assertEquals(0, Strings.indexOfDifference(new String[] { "", "abc" }));
		assertEquals(0, Strings.indexOfDifference(new String[] { "abc", "" }));
		assertEquals(-1, Strings.indexOfDifference(new String[] { "abc", "abc" }));
		assertEquals(1, Strings.indexOfDifference(new String[] { "abc", "a" }));
		assertEquals(2, Strings.indexOfDifference(new String[] { "ab", "abxyz" }));
		assertEquals(2, Strings.indexOfDifference(new String[] { "abcde", "abxyz" }));
		assertEquals(0, Strings.indexOfDifference(new String[] { "abcde", "xyz" }));
		assertEquals(0, Strings.indexOfDifference(new String[] { "xyz", "abcde" }));
		assertEquals(7, Strings.indexOfDifference(new String[] { "i am a machine", "i am a robot" }));
	}

	@Test
	public void testGetCommonPrefix_StringArray() {
		assertEquals("", Strings.getCommonPrefix((String[])null));
		assertEquals("", Strings.getCommonPrefix());
		assertEquals("abc", Strings.getCommonPrefix("abc"));
		assertEquals("", Strings.getCommonPrefix(null, null));
		assertEquals("", Strings.getCommonPrefix("", ""));
		assertEquals("", Strings.getCommonPrefix("", null));
		assertEquals("", Strings.getCommonPrefix("abc", null, null));
		assertEquals("", Strings.getCommonPrefix(null, null, "abc"));
		assertEquals("", Strings.getCommonPrefix("", "abc"));
		assertEquals("", Strings.getCommonPrefix("abc", ""));
		assertEquals("abc", Strings.getCommonPrefix("abc", "abc"));
		assertEquals("a", Strings.getCommonPrefix("abc", "a"));
		assertEquals("ab", Strings.getCommonPrefix("ab", "abxyz"));
		assertEquals("ab", Strings.getCommonPrefix("abcde", "abxyz"));
		assertEquals("", Strings.getCommonPrefix("abcde", "xyz"));
		assertEquals("", Strings.getCommonPrefix("xyz", "abcde"));
		assertEquals("i am a ", Strings.getCommonPrefix("i am a machine", "i am a robot"));
	}

	@Test
	public void testNormalizeSpace() {
		assertEquals(null, Strings.normalizeSpace(null));
		assertEquals("", Strings.normalizeSpace(""));
		assertEquals("", Strings.normalizeSpace(" "));
		assertEquals("", Strings.normalizeSpace("\t"));
		assertEquals("", Strings.normalizeSpace("\n"));
		assertEquals("", Strings.normalizeSpace("\u0009"));
		assertEquals("", Strings.normalizeSpace("\u000B"));
		assertEquals("", Strings.normalizeSpace("\u000C"));
		assertEquals("", Strings.normalizeSpace("\u001C"));
		assertEquals("", Strings.normalizeSpace("\u001D"));
		assertEquals("", Strings.normalizeSpace("\u001E"));
		assertEquals("", Strings.normalizeSpace("\u001F"));
		assertEquals("", Strings.normalizeSpace("\f"));
		assertEquals("", Strings.normalizeSpace("\r"));
		assertEquals("a", Strings.normalizeSpace("  a  "));
		assertEquals("a b c", Strings.normalizeSpace("  a  b   c  "));
		assertEquals("a b c", Strings.normalizeSpace("a\t\f\r  b\u000B   c\n"));
	}

	@Test
	public void testLANG666() {
		assertEquals("12", Strings.stripEnd("120.00", ".0"));
		assertEquals("121", Strings.stripEnd("121.00", ".0"));
	}

	/**
	 * Tests {@link Strings#toString(byte[], String)}
	 * 
	 * @throws UnsupportedEncodingException
	 * @see Strings#toString(byte[], String)
	 */
	@Test
	public void testToString() throws UnsupportedEncodingException {
		final String expectedString = "The quick brown fox jumped over the lazy dog.";
		String encoding = Systems.FILE_ENCODING;
		byte[] expectedBytes = expectedString.getBytes(encoding);
		// sanity check start
		assertArrayEquals(expectedBytes, expectedString.getBytes());
		// sanity check end
		assertEquals(expectedString, Strings.toString(expectedBytes, null));
		assertEquals(expectedString, Strings.toString(expectedBytes, encoding));
		encoding = "UTF-16";
		expectedBytes = expectedString.getBytes(encoding);
		assertEquals(expectedString, Strings.toString(expectedBytes, encoding));
	}

	@Test
	public void testEscapeSurrogatePairs() throws Exception {
		assertEquals("\uD83D\uDE30", StringEscapes.escapeCsv("\uD83D\uDE30"));
		// Examples from https://en.wikipedia.org/wiki/UTF-16
		assertEquals("\uD800\uDC00", StringEscapes.escapeCsv("\uD800\uDC00"));
		assertEquals("\uD834\uDD1E", StringEscapes.escapeCsv("\uD834\uDD1E"));
		assertEquals("\uDBFF\uDFFD", StringEscapes.escapeCsv("\uDBFF\uDFFD"));
		assertEquals("\uDBFF\uDFFD", StringEscapes.escapeHtml3("\uDBFF\uDFFD"));
		assertEquals("\uDBFF\uDFFD", StringEscapes.escapeHtml4("\uDBFF\uDFFD"));
		assertEquals("\uDBFF\uDFFD", StringEscapes.escapeXml("\uDBFF\uDFFD"));
	}

	/**
	 * Tests LANG-858.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testEscapeSurrogatePairsLang858() throws Exception {
		assertEquals("\\uDBFF\\uDFFD", StringEscapes.escapeJava("\uDBFF\uDFFD")); // fail LANG-858
		assertEquals("\\uDBFF\\uDFFD", StringEscapes.escapeEcmaScript("\uDBFF\uDFFD")); // fail
																						// LANG-858
	}

	@Test
	public void testUnescapeSurrogatePairs() throws Exception {
		assertEquals("\uD83D\uDE30", StringEscapes.unescapeCsv("\uD83D\uDE30"));
		// Examples from https://en.wikipedia.org/wiki/UTF-16
		assertEquals("\uD800\uDC00", StringEscapes.unescapeCsv("\uD800\uDC00"));
		assertEquals("\uD834\uDD1E", StringEscapes.unescapeCsv("\uD834\uDD1E"));
		assertEquals("\uDBFF\uDFFD", StringEscapes.unescapeCsv("\uDBFF\uDFFD"));
		assertEquals("\uDBFF\uDFFD", StringEscapes.unescapeHtml3("\uDBFF\uDFFD"));
		assertEquals("\uDBFF\uDFFD", StringEscapes.unescapeHtml4("\uDBFF\uDFFD"));
	}

	/**
	 * Tests {@code appendIfMissing}.
	 */
	@Test
	public void testAppendIfMissing() {
		assertEquals("appendIfMissing(null,null)", null, Strings.appendIfMissing(null, null));
		assertEquals("appendIfMissing(abc,null)", "abc", Strings.appendIfMissing("abc", null));
		assertEquals("appendIfMissing(\"\",xyz)", "xyz", Strings.appendIfMissing("", "xyz"));
		assertEquals("appendIfMissing(abc,xyz)", "abcxyz", Strings.appendIfMissing("abc", "xyz"));
		assertEquals("appendIfMissing(abcxyz,xyz)", "abcxyz", Strings.appendIfMissing("abcxyz", "xyz"));
		assertEquals("appendIfMissing(aXYZ,xyz)", "aXYZxyz", Strings.appendIfMissing("aXYZ", "xyz"));

		assertEquals("appendIfMissing(null,null,null)", null, Strings.appendIfMissing(null, null, (CharSequence[])null));
		assertEquals("appendIfMissing(abc,null,null)", "abc",
			Strings.appendIfMissing("abc", null, (CharSequence[])null));
		assertEquals("appendIfMissing(\"\",xyz,null))", "xyz", Strings.appendIfMissing("", "xyz", (CharSequence[])null));
		assertEquals("appendIfMissing(abc,xyz,{null})", "abcxyz",
			Strings.appendIfMissing("abc", "xyz", new CharSequence[] { null }));
		assertEquals("appendIfMissing(abc,xyz,\"\")", "abc", Strings.appendIfMissing("abc", "xyz", ""));
		assertEquals("appendIfMissing(abc,xyz,mno)", "abcxyz", Strings.appendIfMissing("abc", "xyz", "mno"));
		assertEquals("appendIfMissing(abcxyz,xyz,mno)", "abcxyz", Strings.appendIfMissing("abcxyz", "xyz", "mno"));
		assertEquals("appendIfMissing(abcmno,xyz,mno)", "abcmno", Strings.appendIfMissing("abcmno", "xyz", "mno"));
		assertEquals("appendIfMissing(abcXYZ,xyz,mno)", "abcXYZxyz", Strings.appendIfMissing("abcXYZ", "xyz", "mno"));
		assertEquals("appendIfMissing(abcMNO,xyz,mno)", "abcMNOxyz", Strings.appendIfMissing("abcMNO", "xyz", "mno"));
	}

	/**
	 * Tests {@code appendIfMissingIgnoreCase}.
	 */
	@Test
	public void testAppendIfMissingIgnoreCase() {
		assertEquals("appendIfMissingIgnoreCase(null,null)", null, Strings.appendIfMissingIgnoreCase(null, null));
		assertEquals("appendIfMissingIgnoreCase(abc,null)", "abc", Strings.appendIfMissingIgnoreCase("abc", null));
		assertEquals("appendIfMissingIgnoreCase(\"\",xyz)", "xyz", Strings.appendIfMissingIgnoreCase("", "xyz"));
		assertEquals("appendIfMissingIgnoreCase(abc,xyz)", "abcxyz", Strings.appendIfMissingIgnoreCase("abc", "xyz"));
		assertEquals("appendIfMissingIgnoreCase(abcxyz,xyz)", "abcxyz",
			Strings.appendIfMissingIgnoreCase("abcxyz", "xyz"));
		assertEquals("appendIfMissingIgnoreCase(abcXYZ,xyz)", "abcXYZ",
			Strings.appendIfMissingIgnoreCase("abcXYZ", "xyz"));

		assertEquals("appendIfMissingIgnoreCase(null,null,null)", null,
			Strings.appendIfMissingIgnoreCase(null, null, (CharSequence[])null));
		assertEquals("appendIfMissingIgnoreCase(abc,null,null)", "abc",
			Strings.appendIfMissingIgnoreCase("abc", null, (CharSequence[])null));
		assertEquals("appendIfMissingIgnoreCase(\"\",xyz,null)", "xyz",
			Strings.appendIfMissingIgnoreCase("", "xyz", (CharSequence[])null));
		assertEquals("appendIfMissingIgnoreCase(abc,xyz,{null})", "abcxyz",
			Strings.appendIfMissingIgnoreCase("abc", "xyz", new CharSequence[] { null }));
		assertEquals("appendIfMissingIgnoreCase(abc,xyz,\"\")", "abc",
			Strings.appendIfMissingIgnoreCase("abc", "xyz", ""));
		assertEquals("appendIfMissingIgnoreCase(abc,xyz,mno)", "abcxyz",
			Strings.appendIfMissingIgnoreCase("abc", "xyz", "mno"));
		assertEquals("appendIfMissingIgnoreCase(abcxyz,xyz,mno)", "abcxyz",
			Strings.appendIfMissingIgnoreCase("abcxyz", "xyz", "mno"));
		assertEquals("appendIfMissingIgnoreCase(abcmno,xyz,mno)", "abcmno",
			Strings.appendIfMissingIgnoreCase("abcmno", "xyz", "mno"));
		assertEquals("appendIfMissingIgnoreCase(abcXYZ,xyz,mno)", "abcXYZ",
			Strings.appendIfMissingIgnoreCase("abcXYZ", "xyz", "mno"));
		assertEquals("appendIfMissingIgnoreCase(abcMNO,xyz,mno)", "abcMNO",
			Strings.appendIfMissingIgnoreCase("abcMNO", "xyz", "mno"));
	}

	/**
	 * Tests {@code prependIfMissing}.
	 */
	@Test
	public void testPrependIfMissing() {
		assertEquals("prependIfMissing(null,null)", null, Strings.prependIfMissing(null, null));
		assertEquals("prependIfMissing(abc,null)", "abc", Strings.prependIfMissing("abc", null));
		assertEquals("prependIfMissing(\"\",xyz)", "xyz", Strings.prependIfMissing("", "xyz"));
		assertEquals("prependIfMissing(abc,xyz)", "xyzabc", Strings.prependIfMissing("abc", "xyz"));
		assertEquals("prependIfMissing(xyzabc,xyz)", "xyzabc", Strings.prependIfMissing("xyzabc", "xyz"));
		assertEquals("prependIfMissing(XYZabc,xyz)", "xyzXYZabc", Strings.prependIfMissing("XYZabc", "xyz"));

		assertEquals("prependIfMissing(null,null null)", null,
			Strings.prependIfMissing(null, null, (CharSequence[])null));
		assertEquals("prependIfMissing(abc,null,null)", "abc",
			Strings.prependIfMissing("abc", null, (CharSequence[])null));
		assertEquals("prependIfMissing(\"\",xyz,null)", "xyz",
			Strings.prependIfMissing("", "xyz", (CharSequence[])null));
		assertEquals("prependIfMissing(abc,xyz,{null})", "xyzabc",
			Strings.prependIfMissing("abc", "xyz", new CharSequence[] { null }));
		assertEquals("prependIfMissing(abc,xyz,\"\")", "abc", Strings.prependIfMissing("abc", "xyz", ""));
		assertEquals("prependIfMissing(abc,xyz,mno)", "xyzabc", Strings.prependIfMissing("abc", "xyz", "mno"));
		assertEquals("prependIfMissing(xyzabc,xyz,mno)", "xyzabc", Strings.prependIfMissing("xyzabc", "xyz", "mno"));
		assertEquals("prependIfMissing(mnoabc,xyz,mno)", "mnoabc", Strings.prependIfMissing("mnoabc", "xyz", "mno"));
		assertEquals("prependIfMissing(XYZabc,xyz,mno)", "xyzXYZabc", Strings.prependIfMissing("XYZabc", "xyz", "mno"));
		assertEquals("prependIfMissing(MNOabc,xyz,mno)", "xyzMNOabc", Strings.prependIfMissing("MNOabc", "xyz", "mno"));
	}

	/**
	 * Tests {@code prependIfMissingIgnoreCase}.
	 */
	@Test
	public void testPrependIfMissingIgnoreCase() {
		assertEquals("prependIfMissingIgnoreCase(null,null)", null, Strings.prependIfMissingIgnoreCase(null, null));
		assertEquals("prependIfMissingIgnoreCase(abc,null)", "abc", Strings.prependIfMissingIgnoreCase("abc", null));
		assertEquals("prependIfMissingIgnoreCase(\"\",xyz)", "xyz", Strings.prependIfMissingIgnoreCase("", "xyz"));
		assertEquals("prependIfMissingIgnoreCase(abc,xyz)", "xyzabc", Strings.prependIfMissingIgnoreCase("abc", "xyz"));
		assertEquals("prependIfMissingIgnoreCase(xyzabc,xyz)", "xyzabc",
			Strings.prependIfMissingIgnoreCase("xyzabc", "xyz"));
		assertEquals("prependIfMissingIgnoreCase(XYZabc,xyz)", "XYZabc",
			Strings.prependIfMissingIgnoreCase("XYZabc", "xyz"));

		assertEquals("prependIfMissingIgnoreCase(null,null null)", null,
			Strings.prependIfMissingIgnoreCase(null, null, (CharSequence[])null));
		assertEquals("prependIfMissingIgnoreCase(abc,null,null)", "abc",
			Strings.prependIfMissingIgnoreCase("abc", null, (CharSequence[])null));
		assertEquals("prependIfMissingIgnoreCase(\"\",xyz,null)", "xyz",
			Strings.prependIfMissingIgnoreCase("", "xyz", (CharSequence[])null));
		assertEquals("prependIfMissingIgnoreCase(abc,xyz,{null})", "xyzabc",
			Strings.prependIfMissingIgnoreCase("abc", "xyz", new CharSequence[] { null }));
		assertEquals("prependIfMissingIgnoreCase(abc,xyz,\"\")", "abc",
			Strings.prependIfMissingIgnoreCase("abc", "xyz", ""));
		assertEquals("prependIfMissingIgnoreCase(abc,xyz,mno)", "xyzabc",
			Strings.prependIfMissingIgnoreCase("abc", "xyz", "mno"));
		assertEquals("prependIfMissingIgnoreCase(xyzabc,xyz,mno)", "xyzabc",
			Strings.prependIfMissingIgnoreCase("xyzabc", "xyz", "mno"));
		assertEquals("prependIfMissingIgnoreCase(mnoabc,xyz,mno)", "mnoabc",
			Strings.prependIfMissingIgnoreCase("mnoabc", "xyz", "mno"));
		assertEquals("prependIfMissingIgnoreCase(XYZabc,xyz,mno)", "XYZabc",
			Strings.prependIfMissingIgnoreCase("XYZabc", "xyz", "mno"));
		assertEquals("prependIfMissingIgnoreCase(MNOabc,xyz,mno)", "MNOabc",
			Strings.prependIfMissingIgnoreCase("MNOabc", "xyz", "mno"));
	}

	/**
	 * test method: startsWith
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testStartsWithChars() throws Exception {
		assertTrue(Strings.startsWithChars("adga", "abc"));
	}
	
}
