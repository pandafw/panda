package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.Asserts;
import panda.lang.CloneFailedException;
import panda.lang.Objects;
import panda.lang.mutable.MutableObject;

/**
 * Unit tests {@link Objects}.
 *
 */
public class ObjectsTest {
	private static final String FOO = "foo";
	private static final String BAR = "bar";

	// -----------------------------------------------------------------------
	@Test
	public void testIsNull() {
		final Object o = FOO;
		final Object dflt = BAR;
		assertSame("dflt was not returned when o was null", dflt, Objects.defaultIfNull(null, dflt));
		assertSame("dflt was returned when o was not null", o, Objects.defaultIfNull(o, dflt));
	}

	@Test
	public void testFirstNonNull() {
		assertEquals(null, Objects.firstNonNull(null, null));
		assertEquals("", Objects.firstNonNull(null, ""));
		final String firstNonNullGenerics = Objects.firstNonNull(null, null, "123", "456");
		assertEquals("123", firstNonNullGenerics);
		assertEquals("123", Objects.firstNonNull("123", null, "456", null));
		assertEquals(null, Objects.firstNonNull());
		assertSame(Boolean.TRUE, Objects.firstNonNull(Boolean.TRUE));
		assertNull(Objects.firstNonNull());
		assertNull(Objects.firstNonNull(null, null));
		// assertSame("123", Objects.firstNonNull(null, Objects.NULL, "123", "456"));
		// assertSame("456", Objects.firstNonNull(Objects.NULL, "456", "123", null));
		// assertNull(Objects.firstNonNull(null, null, Objects.NULL));
		assertNull(Objects.firstNonNull((Object)null));
		assertNull(Objects.firstNonNull((Object[])null));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testEquals() {
		assertTrue("Objects.equals(null, null) returned false", Objects.equals(null, null));
		assertTrue("Objects.equals(\"foo\", null) returned true", !Objects.equals(FOO, null));
		assertTrue("Objects.equals(null, \"bar\") returned true", !Objects.equals(null, BAR));
		assertTrue("Objects.equals(\"foo\", \"bar\") returned true", !Objects.equals(FOO, BAR));
		assertTrue("Objects.equals(\"foo\", \"foo\") returned false", Objects.equals(FOO, FOO));
	}

	@Test
	public void testNotEqual() {
		assertFalse("Objects.notEqual(null, null) returned false", Objects.notEqual(null, null));
		assertTrue("Objects.notEqual(\"foo\", null) returned true", Objects.notEqual(FOO, null));
		assertTrue("Objects.notEqual(null, \"bar\") returned true", Objects.notEqual(null, BAR));
		assertTrue("Objects.notEqual(\"foo\", \"bar\") returned true", Objects.notEqual(FOO, BAR));
		assertFalse("Objects.notEqual(\"foo\", \"foo\") returned false", Objects.notEqual(FOO, FOO));
	}

	@Test
	public void testHashCode() {
		assertEquals(0, Objects.hashCode(null));
		assertEquals("a".hashCode(), Objects.hashCode("a"));
	}

	@Test
	public void testHashCodeMulti_multiple_emptyArray() {
		final Object[] array = new Object[0];
		assertEquals(1, Objects.hashCodes(array));
	}

	@Test
	public void testHashCodeMulti_multiple_nullArray() {
		final Object[] array = null;
		assertEquals(0, Objects.hashCodes(array));
	}

	@Test
	public void testHashCodeMulti_multiple_likeList() {
		final List<Object> list0 = new ArrayList<Object>(Arrays.asList());
		assertEquals(list0.hashCode(), Objects.hashCodes());

		final List<Object> list1 = new ArrayList<Object>(Arrays.asList("a"));
		assertEquals(list1.hashCode(), Objects.hashCodes("a"));

		final List<Object> list2 = new ArrayList<Object>(Arrays.asList("a", "b"));
		assertEquals(list2.hashCode(), Objects.hashCodes("a", "b"));

		final List<Object> list3 = new ArrayList<Object>(Arrays.asList("a", "b", "c"));
		assertEquals(list3.hashCode(), Objects.hashCodes("a", "b", "c"));
	}

	// /**
	// * Show that java.util.Date and java.sql.Timestamp are apples and oranges.
	// * Prompted by an email discussion.
	// *
	// * The behavior is different b/w Sun Java 1.3.1_10 and 1.4.2_03.
	// */
	// public void testDateEqualsJava() {
	// long now = 1076957313284L; // Feb 16, 2004 10:49... PST
	// java.util.Date date = new java.util.Date(now);
	// java.sql.Timestamp realTimestamp = new java.sql.Timestamp(now);
	// java.util.Date timestamp = realTimestamp;
	// // sanity check 1:
	// assertEquals(284000000, realTimestamp.getNanos());
	// assertEquals(1076957313284L, date.getTime());
	// //
	// // On Sun 1.3.1_10:
	// //junit.framework.AssertionFailedError: expected:<1076957313284> but was:<1076957313000>
	// //
	// //assertEquals(1076957313284L, timestamp.getTime());
	// //
	// //junit.framework.AssertionFailedError: expected:<1076957313284> but was:<1076957313000>
	// //
	// //assertEquals(1076957313284L, realTimestamp.getTime());
	// // sanity check 2:
	// assertEquals(date.getDay(), realTimestamp.getDay());
	// assertEquals(date.getHours(), realTimestamp.getHours());
	// assertEquals(date.getMinutes(), realTimestamp.getMinutes());
	// assertEquals(date.getMonth(), realTimestamp.getMonth());
	// assertEquals(date.getSeconds(), realTimestamp.getSeconds());
	// assertEquals(date.getTimezoneOffset(), realTimestamp.getTimezoneOffset());
	// assertEquals(date.getYear(), realTimestamp.getYear());
	// //
	// // Time values are == and equals() on Sun 1.4.2_03 but NOT on Sun 1.3.1_10:
	// //
	// //assertFalse("Sanity check failed: date.getTime() == timestamp.getTime()", date.getTime() ==
	// timestamp.getTime());
	// //assertFalse("Sanity check failed: timestamp.equals(date)", timestamp.equals(date));
	// //assertFalse("Sanity check failed: date.equals(timestamp)", date.equals(timestamp));
	// // real test:
	// //assertFalse("java.util.Date and java.sql.Timestamp should be equal", Objects.equals(date,
	// timestamp));
	// }

	@Test
	public void testIdentityToStringStringBuffer() {
		final Integer i = Integer.valueOf(45);
		final String expected = "java.lang.Integer@" + Integer.toHexString(System.identityHashCode(i));

		final StringBuilder buffer = new StringBuilder();
		Objects.identityToString(buffer, i);
		assertEquals(expected, buffer.toString());

		try {
			Objects.identityToString((StringBuilder)null, "tmp");
			fail("NullPointerException expected");
		}
		catch (final NullPointerException npe) {
		}
		try {
			Objects.identityToString(new StringBuilder(), null);
			fail("NullPointerException expected");
		}
		catch (final NullPointerException npe) {
		}
	}

	@Test
	public void testIdentityToStringStringBuilder() {
		assertEquals(null, Objects.identityToString(null));
		assertEquals("java.lang.String@" + Integer.toHexString(System.identityHashCode(FOO)),
			Objects.identityToString(FOO));
		final Integer i = Integer.valueOf(90);
		final String expected = "java.lang.Integer@" + Integer.toHexString(System.identityHashCode(i));

		assertEquals(expected, Objects.identityToString(i));

		final StringBuilder builder = new StringBuilder();
		Objects.identityToString(builder, i);
		assertEquals(expected, builder.toString());

		try {
			Objects.identityToString((StringBuilder)null, "tmp");
			fail("NullPointerException expected");
		}
		catch (final NullPointerException npe) {
		}

		try {
			Objects.identityToString(new StringBuilder(), null);
			fail("NullPointerException expected");
		}
		catch (final NullPointerException npe) {
		}
	}

	@Test
	public void testIdentityToStringStrBuilder() {
		final Integer i = Integer.valueOf(102);
		final String expected = "java.lang.Integer@" + Integer.toHexString(System.identityHashCode(i));

		final StringBuilder builder = new StringBuilder();
		Objects.identityToString(builder, i);
		assertEquals(expected, builder.toString());

		try {
			Objects.identityToString((StringBuilder)null, "tmp");
			fail("NullPointerException expected");
		}
		catch (final NullPointerException npe) {
		}

		try {
			Objects.identityToString(new StringBuilder(), null);
			fail("NullPointerException expected");
		}
		catch (final NullPointerException npe) {
		}
	}

	@Test
	public void testToString_Object() {
		assertEquals("", Objects.toString((Object)null));
		assertEquals(Boolean.TRUE.toString(), Objects.toString(Boolean.TRUE));
	}

	@Test
	public void testToString_ObjectString() {
		assertEquals(BAR, Objects.toString((Object)null, BAR));
		assertEquals(Boolean.TRUE.toString(), Objects.toString(Boolean.TRUE, BAR));
	}

	@SuppressWarnings("cast")
	// 1 OK, because we are checking for code change
	@Test
	public void testNull() {
		assertNotNull(Objects.NULL);
		// 1 Check that NULL really is a Null i.e. the definition has not been changed
		assertTrue(Objects.NULL instanceof Objects.Null);
	}

	@Test
	public void testMax() {
		final Calendar calendar = Calendar.getInstance();
		final Date nonNullComparable1 = calendar.getTime();
		final Date nonNullComparable2 = calendar.getTime();
		final String[] nullAray = null;

		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
		final Date minComparable = calendar.getTime();

		assertNotSame(nonNullComparable1, nonNullComparable2);

		assertNull(Objects.max((String)null));
		assertNull(Objects.max(nullAray));
		assertSame(nonNullComparable1, Objects.max(null, nonNullComparable1));
		assertSame(nonNullComparable1, Objects.max(nonNullComparable1, null));
		assertSame(nonNullComparable1, Objects.max(null, nonNullComparable1, null));
		assertSame(nonNullComparable1, Objects.max(nonNullComparable1, nonNullComparable2));
		assertSame(nonNullComparable2, Objects.max(nonNullComparable2, nonNullComparable1));
		assertSame(nonNullComparable1, Objects.max(nonNullComparable1, minComparable));
		assertSame(nonNullComparable1, Objects.max(minComparable, nonNullComparable1));
		assertSame(nonNullComparable1, Objects.max(null, minComparable, null, nonNullComparable1));

		assertNull(Objects.max((String)null, (String)null));
	}

	@Test
	public void testMin() {
		final Calendar calendar = Calendar.getInstance();
		final Date nonNullComparable1 = calendar.getTime();
		final Date nonNullComparable2 = calendar.getTime();
		final String[] nullAray = null;

		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
		final Date minComparable = calendar.getTime();

		assertNotSame(nonNullComparable1, nonNullComparable2);

		assertNull(Objects.min((String)null));
		assertNull(Objects.min(nullAray));
		assertSame(nonNullComparable1, Objects.min(null, nonNullComparable1));
		assertSame(nonNullComparable1, Objects.min(nonNullComparable1, null));
		assertSame(nonNullComparable1, Objects.min(null, nonNullComparable1, null));
		assertSame(nonNullComparable1, Objects.min(nonNullComparable1, nonNullComparable2));
		assertSame(nonNullComparable2, Objects.min(nonNullComparable2, nonNullComparable1));
		assertSame(minComparable, Objects.min(nonNullComparable1, minComparable));
		assertSame(minComparable, Objects.min(minComparable, nonNullComparable1));
		assertSame(minComparable, Objects.min(null, nonNullComparable1, null, minComparable));

		assertNull(Objects.min((String)null, (String)null));
	}

	/**
	 * Tests {@link Objects#compare(Comparable, Comparable, boolean)}.
	 */
	@Test
	public void testCompare() {
		final Integer one = Integer.valueOf(1);
		final Integer two = Integer.valueOf(2);
		final Integer nullValue = null;

		assertEquals("Null Null false", 0, Objects.compare(nullValue, nullValue));
		assertEquals("Null Null true", 0, Objects.compare(nullValue, nullValue, true));

		assertEquals("Null one false", -1, Objects.compare(nullValue, one));
		assertEquals("Null one true", 1, Objects.compare(nullValue, one, true));

		assertEquals("one Null false", 1, Objects.compare(one, nullValue));
		assertEquals("one Null true", -1, Objects.compare(one, nullValue, true));

		assertEquals("one two false", -1, Objects.compare(one, two));
		assertEquals("one two true", -1, Objects.compare(one, two, true));
	}

	@Test
	public void testMedian() {
		assertEquals("foo", Objects.median("foo"));
		assertEquals("bar", Objects.median("foo", "bar"));
		assertEquals("baz", Objects.median("foo", "bar", "baz"));
		assertEquals("baz", Objects.median("foo", "bar", "baz", "blah"));
		assertEquals("blah", Objects.median("foo", "bar", "baz", "blah", "wah"));
		assertEquals(Integer.valueOf(5), Objects.median(Integer.valueOf(1), Integer.valueOf(5), Integer.valueOf(10)));
		assertEquals(
			Integer.valueOf(7),
			Objects.median(Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8),
				Integer.valueOf(9)));
		assertEquals(Integer.valueOf(6),
			Objects.median(Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8)));
	}

	@Test(expected = NullPointerException.class)
	public void testMedian_nullItems() {
		Objects.median((String[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMedian_emptyItems() {
		Objects.<String> median();
	}

	@Test
	public void testComparatorMedian() {
		final CharSequenceComparator cmp = new CharSequenceComparator();
		final NonComparableCharSequence foo = new NonComparableCharSequence("foo");
		final NonComparableCharSequence bar = new NonComparableCharSequence("bar");
		final NonComparableCharSequence baz = new NonComparableCharSequence("baz");
		final NonComparableCharSequence blah = new NonComparableCharSequence("blah");
		final NonComparableCharSequence wah = new NonComparableCharSequence("wah");
		assertSame(foo, Objects.median(cmp, foo));
		assertSame(bar, Objects.median(cmp, foo, bar));
		assertSame(baz, Objects.median(cmp, foo, bar, baz));
		assertSame(baz, Objects.median(cmp, foo, bar, baz, blah));
		assertSame(blah, Objects.median(cmp, foo, bar, baz, blah, wah));
	}

	@Test(expected = NullPointerException.class)
	public void testComparatorMedian_nullComparator() {
		Objects.median((Comparator<CharSequence>)null, new NonComparableCharSequence("foo"));
	}

	@Test(expected = NullPointerException.class)
	public void testComparatorMedian_nullItems() {
		Objects.median(new CharSequenceComparator(), (CharSequence[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testComparatorMedian_emptyItems() {
		Objects.median(new CharSequenceComparator());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMode() {
		assertNull(Objects.mode((Object[])null));
		assertNull(Objects.mode());
		assertNull(Objects.mode("foo", "bar", "baz"));
		assertNull(Objects.mode("foo", "bar", "baz", "foo", "bar"));
		assertEquals("foo", Objects.mode("foo", "bar", "baz", "foo"));
		assertEquals(Integer.valueOf(9),
			Objects.mode("foo", "bar", "baz", Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(9)));
	}

	/**
	 * Tests {@link Objects#clone(Object)} with a cloneable object.
	 */
	@Test
	public void testCloneOfCloneable() {
		final CloneableString string = new CloneableString("apache");
		final CloneableString stringClone = Objects.clone(string);
		assertEquals("apache", stringClone.getValue());
	}

	/**
	 * Tests {@link Objects#clone(Object)} with a not cloneable object.
	 */
	@Test
	public void testCloneOfNotCloneable() {
		final String string = new String("apache");
		assertNull(Objects.clone(string));
	}

	/**
	 * Tests {@link Objects#clone(Object)} with an uncloneable object.
	 * @throws Throwable if an error occurs
	 */
	@Test(expected = NoSuchMethodException.class)
	public void testCloneOfUncloneable() throws Throwable {
		final UncloneableString string = new UncloneableString("apache");
		try {
			Objects.clone(string);
			fail("Thrown " + CloneFailedException.class.getName() + " expected");
		}
		catch (final CloneFailedException e) {
			throw e.getCause();
		}
	}

	/**
	 * Tests {@link Objects#clone(Object)} with an object array.
	 */
	@Test
	public void testCloneOfStringArray() {
		assertTrue(Arrays.equals(new String[] { "string" }, Objects.clone(new String[] { "string" })));
	}

	/**
	 * Tests {@link Objects#clone(Object)} with an array of primitives.
	 */
	@Test
	public void testCloneOfPrimitiveArray() {
		assertTrue(Arrays.equals(new int[] { 1 }, Objects.clone(new int[] { 1 })));
	}

	/**
	 * Tests {@link Objects#cloneIfPossible(Object)} with a cloneable object.
	 */
	@Test
	public void testPossibleCloneOfCloneable() {
		final CloneableString string = new CloneableString("apache");
		final CloneableString stringClone = Objects.cloneIfPossible(string);
		assertEquals("apache", stringClone.getValue());
	}

	/**
	 * Tests {@link Objects#cloneIfPossible(Object)} with a not cloneable object.
	 */
	@Test
	public void testPossibleCloneOfNotCloneable() {
		final String string = new String("apache");
		assertSame(string, Objects.cloneIfPossible(string));
	}

	/**
	 * Tests {@link Objects#cloneIfPossible(Object)} with an uncloneable object.
	 * @throws Throwable if an error occurs
	 */
	@Test(expected = NoSuchMethodException.class)
	public void testPossibleCloneOfUncloneable() throws Throwable {
		final UncloneableString string = new UncloneableString("apache");
		try {
			Objects.cloneIfPossible(string);
			fail("Thrown " + CloneFailedException.class.getName() + " expected");
		}
		catch (final CloneFailedException e) {
			throw e.getCause();
		}
	}

	/**
	 * String that is cloneable.
	 */
	static final class CloneableString extends MutableObject<String> implements Cloneable {
		private static final long serialVersionUID = 1L;

		CloneableString(final String s) {
			super(s);
		}

		@Override
		public CloneableString clone() throws CloneNotSupportedException {
			return (CloneableString)super.clone();
		}
	}

	/**
	 * String that is not cloneable.
	 */
	static final class UncloneableString extends MutableObject<String> implements Cloneable {
		private static final long serialVersionUID = 1L;

		UncloneableString(final String s) {
			super(s);
		}
	}

	static final class NonComparableCharSequence implements CharSequence {
		final String value;

		/**
		 * Create a new NonComparableCharSequence instance.
		 * 
		 * @param value string
		 */
		public NonComparableCharSequence(final String value) {
			super();
			Asserts.notNull(value);
			this.value = value;
		}

		public char charAt(final int arg0) {
			return value.charAt(arg0);
		}

		public int length() {
			return value.length();
		}

		public CharSequence subSequence(final int arg0, final int arg1) {
			return value.subSequence(arg0, arg1);
		}

		public String toString() {
			return value;
		}
	}

	static final class CharSequenceComparator implements Comparator<CharSequence> {
		public int compare(final CharSequence o1, final CharSequence o2) {
			return o1.toString().compareTo(o2.toString());
		}

	}
}
