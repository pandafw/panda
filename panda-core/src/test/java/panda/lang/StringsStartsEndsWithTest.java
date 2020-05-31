package panda.lang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests {@link Strings} - StartsWith/EndsWith methods
 *
 */
public class StringsStartsEndsWithTest {
	private static final String foo = "foo";
	private static final String bar = "bar";
	private static final String foobar = "foobar";
	private static final String FOO = "FOO";
	private static final String BAR = "BAR";
	private static final String FOOBAR = "FOOBAR";

	// -----------------------------------------------------------------------

	/**
	 * Test Strings.startsWith()
	 */
	@Test
	public void testStartsWith() {
		assertTrue("startsWith(null, null)", Strings.startsWith(null, (String)null));
		assertFalse("startsWith(FOOBAR, null)", Strings.startsWith(FOOBAR, (String)null));
		assertFalse("startsWith(null, FOO)", Strings.startsWith(null, FOO));
		assertTrue("startsWith(FOOBAR, \"\")", Strings.startsWith(FOOBAR, ""));

		assertTrue("startsWith(foobar, foo)", Strings.startsWith(foobar, foo));
		assertTrue("startsWith(FOOBAR, FOO)", Strings.startsWith(FOOBAR, FOO));
		assertFalse("startsWith(foobar, FOO)", Strings.startsWith(foobar, FOO));
		assertFalse("startsWith(FOOBAR, foo)", Strings.startsWith(FOOBAR, foo));

		assertFalse("startsWith(foo, foobar)", Strings.startsWith(foo, foobar));
		assertFalse("startsWith(foo, foobar)", Strings.startsWith(bar, foobar));

		assertFalse("startsWith(foobar, bar)", Strings.startsWith(foobar, bar));
		assertFalse("startsWith(FOOBAR, BAR)", Strings.startsWith(FOOBAR, BAR));
		assertFalse("startsWith(foobar, BAR)", Strings.startsWith(foobar, BAR));
		assertFalse("startsWith(FOOBAR, bar)", Strings.startsWith(FOOBAR, bar));
	}

	/**
	 * Test Strings.testStartsWithIgnoreCase()
	 */
	@Test
	public void testStartsWithIgnoreCase() {
		assertTrue("startsWithIgnoreCase(null, null)", Strings.startsWithIgnoreCase(null, (String)null));
		assertFalse("startsWithIgnoreCase(FOOBAR, null)", Strings.startsWithIgnoreCase(FOOBAR, (String)null));
		assertFalse("startsWithIgnoreCase(null, FOO)", Strings.startsWithIgnoreCase(null, FOO));
		assertTrue("startsWithIgnoreCase(FOOBAR, \"\")", Strings.startsWithIgnoreCase(FOOBAR, ""));

		assertTrue("startsWithIgnoreCase(foobar, foo)", Strings.startsWithIgnoreCase(foobar, foo));
		assertTrue("startsWithIgnoreCase(FOOBAR, FOO)", Strings.startsWithIgnoreCase(FOOBAR, FOO));
		assertTrue("startsWithIgnoreCase(foobar, FOO)", Strings.startsWithIgnoreCase(foobar, FOO));
		assertTrue("startsWithIgnoreCase(FOOBAR, foo)", Strings.startsWithIgnoreCase(FOOBAR, foo));

		assertFalse("startsWithIgnoreCase(foo, foobar)", Strings.startsWithIgnoreCase(foo, foobar));
		assertFalse("startsWithIgnoreCase(foo, foobar)", Strings.startsWithIgnoreCase(bar, foobar));

		assertFalse("startsWithIgnoreCase(foobar, bar)", Strings.startsWithIgnoreCase(foobar, bar));
		assertFalse("startsWithIgnoreCase(FOOBAR, BAR)", Strings.startsWithIgnoreCase(FOOBAR, BAR));
		assertFalse("startsWithIgnoreCase(foobar, BAR)", Strings.startsWithIgnoreCase(foobar, BAR));
		assertFalse("startsWithIgnoreCase(FOOBAR, bar)", Strings.startsWithIgnoreCase(FOOBAR, bar));
	}

	@Test
	public void testStartsWithAny() {
		assertFalse(Strings.startsWithAny(null, (String[])null));
		assertFalse(Strings.startsWithAny(null, "abc"));
		assertFalse(Strings.startsWithAny("abcxyz", (String[])null));
		assertFalse(Strings.startsWithAny("abcxyz"));
		assertTrue(Strings.startsWithAny("abcxyz", "abc"));
		assertTrue(Strings.startsWithAny("abcxyz", null, "xyz", "abc"));
		assertFalse(Strings.startsWithAny("abcxyz", null, "xyz", "abcd"));

		assertTrue("Strings.startsWithAny(abcxyz, StringBuilder(xyz), StringBuffer(abc))",
			Strings.startsWithAny("abcxyz", new StringBuilder("xyz"), new StringBuffer("abc")));
		assertTrue("Strings.startsWithAny( StrBuilder(abcxyz), StringBuilder(xyz), StringBuffer(abc))",
			Strings.startsWithAny(new StringBuilder("abcxyz"), new StringBuilder("xyz"), new StringBuffer("abc")));
	}

	/**
	 * Test Strings.endsWith()
	 */
	@Test
	public void testEndsWith() {
		assertTrue("endsWith(null, null)", Strings.endsWith(null, (String)null));
		assertFalse("endsWith(FOOBAR, null)", Strings.endsWith(FOOBAR, (String)null));
		assertFalse("endsWith(null, FOO)", Strings.endsWith(null, FOO));
		assertTrue("endsWith(FOOBAR, \"\")", Strings.endsWith(FOOBAR, ""));

		assertFalse("endsWith(foobar, foo)", Strings.endsWith(foobar, foo));
		assertFalse("endsWith(FOOBAR, FOO)", Strings.endsWith(FOOBAR, FOO));
		assertFalse("endsWith(foobar, FOO)", Strings.endsWith(foobar, FOO));
		assertFalse("endsWith(FOOBAR, foo)", Strings.endsWith(FOOBAR, foo));

		assertFalse("endsWith(foo, foobar)", Strings.endsWith(foo, foobar));
		assertFalse("endsWith(foo, foobar)", Strings.endsWith(bar, foobar));

		assertTrue("endsWith(foobar, bar)", Strings.endsWith(foobar, bar));
		assertTrue("endsWith(FOOBAR, BAR)", Strings.endsWith(FOOBAR, BAR));
		assertFalse("endsWith(foobar, BAR)", Strings.endsWith(foobar, BAR));
		assertFalse("endsWith(FOOBAR, bar)", Strings.endsWith(FOOBAR, bar));

		// "alpha,beta,gamma,delta".endsWith("delta")
		assertTrue("endsWith(\u03B1\u03B2\u03B3\u03B4, \u03B4)", Strings.endsWith("\u03B1\u03B2\u03B3\u03B4", "\u03B4"));
		// "alpha,beta,gamma,delta".endsWith("gamma,DELTA")
		assertFalse("endsWith(\u03B1\u03B2\u03B3\u03B4, \u03B3\u0394)",
			Strings.endsWith("\u03B1\u03B2\u03B3\u03B4", "\u03B3\u0394"));
	}

	/**
	 * Test Strings.endsWithIgnoreCase()
	 */
	@Test
	public void testEndsWithIgnoreCase() {
		assertTrue("endsWithIgnoreCase(null, null)", Strings.endsWithIgnoreCase(null, (String)null));
		assertFalse("endsWithIgnoreCase(FOOBAR, null)", Strings.endsWithIgnoreCase(FOOBAR, (String)null));
		assertFalse("endsWithIgnoreCase(null, FOO)", Strings.endsWithIgnoreCase(null, FOO));
		assertTrue("endsWithIgnoreCase(FOOBAR, \"\")", Strings.endsWithIgnoreCase(FOOBAR, ""));

		assertFalse("endsWithIgnoreCase(foobar, foo)", Strings.endsWithIgnoreCase(foobar, foo));
		assertFalse("endsWithIgnoreCase(FOOBAR, FOO)", Strings.endsWithIgnoreCase(FOOBAR, FOO));
		assertFalse("endsWithIgnoreCase(foobar, FOO)", Strings.endsWithIgnoreCase(foobar, FOO));
		assertFalse("endsWithIgnoreCase(FOOBAR, foo)", Strings.endsWithIgnoreCase(FOOBAR, foo));

		assertFalse("endsWithIgnoreCase(foo, foobar)", Strings.endsWithIgnoreCase(foo, foobar));
		assertFalse("endsWithIgnoreCase(foo, foobar)", Strings.endsWithIgnoreCase(bar, foobar));

		assertTrue("endsWithIgnoreCase(foobar, bar)", Strings.endsWithIgnoreCase(foobar, bar));
		assertTrue("endsWithIgnoreCase(FOOBAR, BAR)", Strings.endsWithIgnoreCase(FOOBAR, BAR));
		assertTrue("endsWithIgnoreCase(foobar, BAR)", Strings.endsWithIgnoreCase(foobar, BAR));
		assertTrue("endsWithIgnoreCase(FOOBAR, bar)", Strings.endsWithIgnoreCase(FOOBAR, bar));

		// javadoc
		assertTrue(Strings.endsWithIgnoreCase("abcdef", "def"));
		assertTrue(Strings.endsWithIgnoreCase("ABCDEF", "def"));
		assertFalse(Strings.endsWithIgnoreCase("ABCDEF", "cde"));

		// "alpha,beta,gamma,delta".endsWith("DELTA")
		assertTrue("endsWith(\u03B1\u03B2\u03B3\u03B4, \u0394)",
			Strings.endsWithIgnoreCase("\u03B1\u03B2\u03B3\u03B4", "\u0394"));
		// "alpha,beta,gamma,delta".endsWith("GAMMA")
		assertFalse("endsWith(\u03B1\u03B2\u03B3\u03B4, \u0393)",
			Strings.endsWithIgnoreCase("\u03B1\u03B2\u03B3\u03B4", "\u0393"));
	}

	@Test
	public void testEndsWithAny() {
		assertFalse("Strings.endsWithAny(null, null)", Strings.endsWithAny(null, (String)null));
		assertFalse("Strings.endsWithAny(null, new String[] {abc})", Strings.endsWithAny(null, new String[] { "abc" }));
		assertFalse("Strings.endsWithAny(abcxyz, null)", Strings.endsWithAny("abcxyz", (String)null));
		assertTrue("Strings.endsWithAny(abcxyz, new String[] {\"\"})",
			Strings.endsWithAny("abcxyz", new String[] { "" }));
		assertTrue("Strings.endsWithAny(abcxyz, new String[] {xyz})",
			Strings.endsWithAny("abcxyz", new String[] { "xyz" }));
		assertTrue("Strings.endsWithAny(abcxyz, new String[] {null, xyz, abc})",
			Strings.endsWithAny("abcxyz", new String[] { null, "xyz", "abc" }));
		assertFalse("Strings.endsWithAny(defg, new String[] {null, xyz, abc})",
			Strings.endsWithAny("defg", new String[] { null, "xyz", "abc" }));

		assertTrue("Strings.endsWithAny(abcxyz, StringBuilder(abc), StringBuffer(xyz))",
			Strings.endsWithAny("abcxyz", new StringBuilder("abc"), new StringBuffer("xyz")));
		assertTrue("Strings.endsWithAny( StrBuilder(abcxyz), StringBuilder(abc), StringBuffer(xyz))",
			Strings.endsWithAny(new StringBuilder("abcxyz"), new StringBuilder("abc"), new StringBuffer("xyz")));
	}

}
