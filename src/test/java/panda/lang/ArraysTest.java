package panda.lang;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Unit tests {@link Arrays}.
 */
public class ArraysTest {

	// -----------------------------------------------------------------------
	@Test
	public void testToString() {
		assertEquals("null", Arrays.toString((Object)null));
		assertEquals("[]", Arrays.toString(new Object[0]));
		assertEquals("[]", Arrays.toString(new String[0]));
		assertEquals("[<null>]", Arrays.toString(new String[] { null }));
		assertEquals("[pink,blue]", Arrays.toString(new String[] { "pink", "blue" }));

		assertEquals("<empty>", Arrays.toString(null, "<empty>"));
		assertEquals("[]", Arrays.toString(new Object[0], "<empty>"));
		assertEquals("[]", Arrays.toString(new String[0], "<empty>"));
		assertEquals("[<null>]", Arrays.toString(new String[] { null }, "<empty>"));
		assertEquals("[pink,blue]", Arrays.toString(new String[] { "pink", "blue" }, "<empty>"));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testHashCode() {
		final long[][] array1 = new long[][] { { 2, 5 }, { 4, 5 } };
		final long[][] array2 = new long[][] { { 2, 5 }, { 4, 6 } };
		assertEquals(Arrays.hashCode(array1), Arrays.hashCode(array1));
		assertFalse(Arrays.hashCode(array1) == Arrays.hashCode(array2));

		final Object[] array3 = new Object[] { new String(new char[] { 'A', 'B' }) };
		final Object[] array4 = new Object[] { "AB" };
		assertEquals(Arrays.hashCode(array3), Arrays.hashCode(array3));
		assertTrue(Arrays.hashCode(array3) == Arrays.hashCode(array4));

		final Object[] arrayA = new Object[] { new boolean[] { true, false }, new int[] { 6, 7 } };
		final Object[] arrayB = new Object[] { new boolean[] { true, false }, new int[] { 6, 7 } };
		assertEquals(Arrays.deepHashCode(arrayB), Arrays.deepHashCode(arrayA));
	}

	// -----------------------------------------------------------------------
	private void assertIsEquals(final Object array1, final Object array2, final Object array3) {
		assertTrue(Arrays.equals(array1, array1));
		assertTrue(Arrays.equals(array2, array2));
		assertTrue(Arrays.equals(array3, array3));
		assertFalse(Arrays.equals(array1, array2));
		assertFalse(Arrays.equals(array2, array1));
		assertFalse(Arrays.equals(array1, array3));
		assertFalse(Arrays.equals(array3, array1));
		assertFalse(Arrays.equals(array1, array2));
		assertFalse(Arrays.equals(array2, array1));
	}

	@Test
	public void testIsEquals() {
		final long[][] larray1 = new long[][] { { 2, 5 }, { 4, 5 } };
		final long[][] larray2 = new long[][] { { 2, 5 }, { 4, 6 } };
		final long[] larray3 = new long[] { 2, 5 };
		this.assertIsEquals(larray1, larray2, larray3);

		final int[][] iarray1 = new int[][] { { 2, 5 }, { 4, 5 } };
		final int[][] iarray2 = new int[][] { { 2, 5 }, { 4, 6 } };
		final int[] iarray3 = new int[] { 2, 5 };
		this.assertIsEquals(iarray1, iarray2, iarray3);

		final short[][] sarray1 = new short[][] { { 2, 5 }, { 4, 5 } };
		final short[][] sarray2 = new short[][] { { 2, 5 }, { 4, 6 } };
		final short[] sarray3 = new short[] { 2, 5 };
		this.assertIsEquals(sarray1, sarray2, sarray3);

		final float[][] farray1 = new float[][] { { 2, 5 }, { 4, 5 } };
		final float[][] farray2 = new float[][] { { 2, 5 }, { 4, 6 } };
		final float[] farray3 = new float[] { 2, 5 };
		this.assertIsEquals(farray1, farray2, farray3);

		final double[][] darray1 = new double[][] { { 2, 5 }, { 4, 5 } };
		final double[][] darray2 = new double[][] { { 2, 5 }, { 4, 6 } };
		final double[] darray3 = new double[] { 2, 5 };
		this.assertIsEquals(darray1, darray2, darray3);

		final byte[][] byteArray1 = new byte[][] { { 2, 5 }, { 4, 5 } };
		final byte[][] byteArray2 = new byte[][] { { 2, 5 }, { 4, 6 } };
		final byte[] byteArray3 = new byte[] { 2, 5 };
		this.assertIsEquals(byteArray1, byteArray2, byteArray3);

		final char[][] charArray1 = new char[][] { { 2, 5 }, { 4, 5 } };
		final char[][] charArray2 = new char[][] { { 2, 5 }, { 4, 6 } };
		final char[] charArray3 = new char[] { 2, 5 };
		this.assertIsEquals(charArray1, charArray2, charArray3);

		final boolean[][] barray1 = new boolean[][] { { true, false }, { true, true } };
		final boolean[][] barray2 = new boolean[][] { { true, false }, { true, false } };
		final boolean[] barray3 = new boolean[] { false, true };
		this.assertIsEquals(barray1, barray2, barray3);

		final Object[] array3 = new Object[] { new String(new char[] { 'A', 'B' }) };
		final Object[] array4 = new Object[] { "AB" };
		assertTrue(Arrays.equals(array3, array3));
		assertTrue(Arrays.equals(array3, array4));

		assertTrue(Arrays.equals((Object)null, null));
		assertFalse(Arrays.equals(null, array4));
	}

	// -----------------------------------------------------------------------
	/**
	 * Tests generic array creation with parameters of same type.
	 */
	@Test
	public void testArrayCreation() {
		final String[] array = Arrays.toArray("foo", "bar");
		assertEquals(2, array.length);
		assertEquals("foo", array[0]);
		assertEquals("bar", array[1]);
	}

	/**
	 * Tests generic array creation with general return type.
	 */
	@Test
	public void testArrayCreationWithGeneralReturnType() {
		final Object obj = Arrays.toArray("foo", "bar");
		assertTrue(obj instanceof String[]);
	}

	/**
	 * Tests generic array creation with parameters of common base type.
	 */
	@Test
	public void testArrayCreationWithDifferentTypes() {
		final Number[] array = Arrays.<Number> toArray(Integer.valueOf(42), Double.valueOf(Math.PI));
		assertEquals(2, array.length);
		assertEquals(Integer.valueOf(42), array[0]);
		assertEquals(Double.valueOf(Math.PI), array[1]);
	}

	/**
	 * Tests generic array creation with generic type.
	 */
	@Test
	public void testIndirectArrayCreation() {
		final String[] array = toArrayPropagatingType("foo", "bar");
		assertEquals(2, array.length);
		assertEquals("foo", array[0]);
		assertEquals("bar", array[1]);
	}

	/**
	 * Tests generic empty array creation with generic type.
	 */
	@Test
	public void testEmptyArrayCreation() {
		final String[] array = Arrays.<String> toArray();
		assertEquals(0, array.length);
	}

	/**
	 * Tests indirect generic empty array creation with generic type.
	 */
	@Test
	public void testIndirectEmptyArrayCreation() {
		final String[] array = ArraysTest.<String> toArrayPropagatingType();
		assertEquals(0, array.length);
	}

	private static <T> T[] toArrayPropagatingType(final T... items) {
		return Arrays.toArray(items);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testToMap() {
		Map<?, ?> map = Arrays.toMap("foo", "bar", "hello", "world");

		assertEquals("bar", map.get("foo"));
		assertEquals("world", map.get("hello"));

		assertEquals(null, Arrays.toMap(null));
		try {
			Arrays.toMap("foo", "bar", "short");
			fail("exception expected");
		}
		catch (final ArrayIndexOutOfBoundsException ex) {
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testClone() {
		assertArrayEquals(null, Arrays.clone((Object[])null));
		Object[] original1 = new Object[0];
		Object[] cloned1 = Arrays.clone(original1);
		assertTrue(Arrays.equals(original1, cloned1));
		assertTrue(original1 != cloned1);

		final StringBuffer buf = new StringBuffer("pick");
		original1 = new Object[] { buf, "a", new String[] { "stick" } };
		cloned1 = Arrays.clone(original1);
		assertTrue(Arrays.equals(original1, cloned1));
		assertTrue(original1 != cloned1);
		assertSame(original1[0], cloned1[0]);
		assertSame(original1[1], cloned1[1]);
		assertSame(original1[2], cloned1[2]);
	}

	@Test
	public void testCloneBoolean() {
		assertEquals(null, Arrays.clone((boolean[])null));
		final boolean[] original = new boolean[] { true, false };
		final boolean[] cloned = Arrays.clone(original);
		assertTrue(Arrays.equals(original, cloned));
		assertTrue(original != cloned);
	}

	@Test
	public void testCloneLong() {
		assertEquals(null, Arrays.clone((long[])null));
		final long[] original = new long[] { 0L, 1L };
		final long[] cloned = Arrays.clone(original);
		assertTrue(Arrays.equals(original, cloned));
		assertTrue(original != cloned);
	}

	@Test
	public void testCloneInt() {
		assertEquals(null, Arrays.clone((int[])null));
		final int[] original = new int[] { 5, 8 };
		final int[] cloned = Arrays.clone(original);
		assertTrue(Arrays.equals(original, cloned));
		assertTrue(original != cloned);
	}

	@Test
	public void testCloneShort() {
		assertEquals(null, Arrays.clone((short[])null));
		final short[] original = new short[] { 1, 4 };
		final short[] cloned = Arrays.clone(original);
		assertTrue(Arrays.equals(original, cloned));
		assertTrue(original != cloned);
	}

	@Test
	public void testCloneChar() {
		assertEquals(null, Arrays.clone((char[])null));
		final char[] original = new char[] { 'a', '4' };
		final char[] cloned = Arrays.clone(original);
		assertTrue(Arrays.equals(original, cloned));
		assertTrue(original != cloned);
	}

	@Test
	public void testCloneByte() {
		assertEquals(null, Arrays.clone((byte[])null));
		final byte[] original = new byte[] { 1, 6 };
		final byte[] cloned = Arrays.clone(original);
		assertTrue(Arrays.equals(original, cloned));
		assertTrue(original != cloned);
	}

	@Test
	public void testCloneDouble() {
		assertEquals(null, Arrays.clone((double[])null));
		final double[] original = new double[] { 2.4d, 5.7d };
		final double[] cloned = Arrays.clone(original);
		assertTrue(Arrays.equals(original, cloned));
		assertTrue(original != cloned);
	}

	@Test
	public void testCloneFloat() {
		assertEquals(null, Arrays.clone((float[])null));
		final float[] original = new float[] { 2.6f, 6.4f };
		final float[] cloned = Arrays.clone(original);
		assertTrue(Arrays.equals(original, cloned));
		assertTrue(original != cloned);
	}

	// -----------------------------------------------------------------------

	@Test
	public void testNullToEmptyBoolean() {
		// Test null handling
		assertEquals(Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.nullToEmpty((boolean[])null));
		// Test valid array handling
		final boolean[] original = new boolean[] { true, false };
		assertEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final boolean[] empty = new boolean[] {};
		final boolean[] result = Arrays.nullToEmpty(empty);
		assertEquals(Arrays.EMPTY_BOOLEAN_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyLong() {
		// Test null handling
		assertEquals(Arrays.EMPTY_LONG_ARRAY, Arrays.nullToEmpty((long[])null));
		// Test valid array handling
		final long[] original = new long[] { 1L, 2L };
		assertEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final long[] empty = new long[] {};
		final long[] result = Arrays.nullToEmpty(empty);
		assertEquals(Arrays.EMPTY_LONG_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyInt() {
		// Test null handling
		assertEquals(Arrays.EMPTY_INT_ARRAY, Arrays.nullToEmpty((int[])null));
		// Test valid array handling
		final int[] original = new int[] { 1, 2 };
		assertEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final int[] empty = new int[] {};
		final int[] result = Arrays.nullToEmpty(empty);
		assertEquals(Arrays.EMPTY_INT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyShort() {
		// Test null handling
		assertEquals(Arrays.EMPTY_SHORT_ARRAY, Arrays.nullToEmpty((short[])null));
		// Test valid array handling
		final short[] original = new short[] { 1, 2 };
		assertEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final short[] empty = new short[] {};
		final short[] result = Arrays.nullToEmpty(empty);
		assertEquals(Arrays.EMPTY_SHORT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyChar() {
		// Test null handling
		assertEquals(Arrays.EMPTY_CHAR_ARRAY, Arrays.nullToEmpty((char[])null));
		// Test valid array handling
		final char[] original = new char[] { 'a', 'b' };
		assertEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final char[] empty = new char[] {};
		final char[] result = Arrays.nullToEmpty(empty);
		assertEquals(Arrays.EMPTY_CHAR_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyByte() {
		// Test null handling
		assertEquals(Arrays.EMPTY_BYTE_ARRAY, Arrays.nullToEmpty((byte[])null));
		// Test valid array handling
		final byte[] original = new byte[] { 0x0F, 0x0E };
		assertEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final byte[] empty = new byte[] {};
		final byte[] result = Arrays.nullToEmpty(empty);
		assertEquals(Arrays.EMPTY_BYTE_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyDouble() {
		// Test null handling
		assertEquals(Arrays.EMPTY_DOUBLE_ARRAY, Arrays.nullToEmpty((double[])null));
		// Test valid array handling
		final double[] original = new double[] { 1L, 2L };
		assertEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final double[] empty = new double[] {};
		final double[] result = Arrays.nullToEmpty(empty);
		assertEquals(Arrays.EMPTY_DOUBLE_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyFloat() {
		// Test null handling
		assertEquals(Arrays.EMPTY_FLOAT_ARRAY, Arrays.nullToEmpty((float[])null));
		// Test valid array handling
		final float[] original = new float[] { 2.6f, 3.8f };
		assertEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final float[] empty = new float[] {};
		final float[] result = Arrays.nullToEmpty(empty);
		assertEquals(Arrays.EMPTY_FLOAT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyObject() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_OBJECT_ARRAY, Arrays.nullToEmpty((Object[])null));
		// Test valid array handling
		final Object[] original = new Object[] { Boolean.TRUE, Boolean.FALSE };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final Object[] empty = new Object[] {};
		final Object[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_OBJECT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyString() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_STRING_ARRAY, Arrays.nullToEmpty((String[])null));
		// Test valid array handling
		final String[] original = new String[] { "abc", "def" };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final String[] empty = new String[] {};
		final String[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_STRING_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyBooleanObject() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_BOOLEAN_OBJECT_ARRAY, Arrays.nullToEmpty((Boolean[])null));
		// Test valid array handling
		final Boolean[] original = new Boolean[] { Boolean.TRUE, Boolean.FALSE };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final Boolean[] empty = new Boolean[] {};
		final Boolean[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_BOOLEAN_OBJECT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyLongObject() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_LONG_OBJECT_ARRAY, Arrays.nullToEmpty((Long[])null));
		// Test valid array handling
		@SuppressWarnings("boxing")
		final Long[] original = new Long[] { 1L, 2L };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final Long[] empty = new Long[] {};
		final Long[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_LONG_OBJECT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyIntObject() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_INTEGER_OBJECT_ARRAY, Arrays.nullToEmpty((Integer[])null));
		// Test valid array handling
		final Integer[] original = new Integer[] { 1, 2 };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final Integer[] empty = new Integer[] {};
		final Integer[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_INTEGER_OBJECT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyShortObject() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_SHORT_OBJECT_ARRAY, Arrays.nullToEmpty((Short[])null));
		// Test valid array handling
		@SuppressWarnings("boxing")
		final Short[] original = new Short[] { 1, 2 };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final Short[] empty = new Short[] {};
		final Short[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_SHORT_OBJECT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyCharObject() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_CHARACTER_OBJECT_ARRAY, Arrays.nullToEmpty((Character[])null));
		// Test valid array handling
		final Character[] original = new Character[] { 'a', 'b' };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final Character[] empty = new Character[] {};
		final Character[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_CHARACTER_OBJECT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyByteObject() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_BYTE_OBJECT_ARRAY, Arrays.nullToEmpty((Byte[])null));
		// Test valid array handling
		final Byte[] original = new Byte[] { 0x0F, 0x0E };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final Byte[] empty = new Byte[] {};
		final Byte[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_BYTE_OBJECT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyDoubleObject() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_DOUBLE_OBJECT_ARRAY, Arrays.nullToEmpty((Double[])null));
		// Test valid array handling
		final Double[] original = new Double[] { 1D, 2D };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final Double[] empty = new Double[] {};
		final Double[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_DOUBLE_OBJECT_ARRAY, result);
		assertTrue(empty != result);
	}

	@Test
	public void testNullToEmptyFloatObject() {
		// Test null handling
		assertArrayEquals(Arrays.EMPTY_FLOAT_OBJECT_ARRAY, Arrays.nullToEmpty((Float[])null));
		// Test valid array handling
		final Float[] original = new Float[] { 2.6f, 3.8f };
		assertArrayEquals(original, Arrays.nullToEmpty(original));
		// Test empty array handling
		final Float[] empty = new Float[] {};
		final Float[] result = Arrays.nullToEmpty(empty);
		assertArrayEquals(Arrays.EMPTY_FLOAT_OBJECT_ARRAY, result);
		assertTrue(empty != result);
	}

	// -----------------------------------------------------------------------

	@Test
	public void testSubarrayObject() {
		final Object[] nullArray = null;
		final Object[] objectArray = { "a", "b", "c", "d", "e", "f" };

		assertEquals("0 start, mid end", "abcd", Strings.join(Arrays.subarray(objectArray, 0, 4)));
		assertEquals("0 start, length end", "abcdef", Strings.join(Arrays.subarray(objectArray, 0, objectArray.length)));
		assertEquals("mid start, mid end", "bcd", Strings.join(Arrays.subarray(objectArray, 1, 4)));
		assertEquals("mid start, length end", "bcdef",
			Strings.join(Arrays.subarray(objectArray, 1, objectArray.length)));

		assertNull("null input", Arrays.subarray(nullArray, 0, 3));
		assertEquals("empty array", "", Strings.join(Arrays.subarray(Arrays.EMPTY_OBJECT_ARRAY, 1, 2)));
		assertEquals("start > end", "", Strings.join(Arrays.subarray(objectArray, 4, 2)));
		assertEquals("start == end", "", Strings.join(Arrays.subarray(objectArray, 3, 3)));
		assertEquals("start undershoot, normal end", "abcd", Strings.join(Arrays.subarray(objectArray, -2, 4)));
		assertEquals("start overshoot, any end", "", Strings.join(Arrays.subarray(objectArray, 33, 4)));
		assertEquals("normal start, end overshoot", "cdef", Strings.join(Arrays.subarray(objectArray, 2, 33)));
		assertEquals("start undershoot, end overshoot", "abcdef", Strings.join(Arrays.subarray(objectArray, -2, 12)));

		// array type tests
		final Date[] dateArray = { new java.sql.Date(new Date().getTime()), new Date(), new Date(), new Date(),
				new Date() };

		assertSame("Object type", Object.class, Arrays.subarray(objectArray, 2, 4).getClass().getComponentType());
		assertSame("java.util.Date type", java.util.Date.class, Arrays.subarray(dateArray, 1, 4).getClass()
			.getComponentType());
		assertNotSame("java.sql.Date type", java.sql.Date.class, Arrays.subarray(dateArray, 1, 4).getClass()
			.getComponentType());
		try {
			@SuppressWarnings("unused")
			final java.sql.Date[] dummy = (java.sql.Date[])Arrays.subarray(dateArray, 1, 3);
			fail("Invalid downcast");
		}
		catch (final ClassCastException e) {
		}
	}

	@Test
	public void testSubarrayLong() {
		final long[] nullArray = null;
		final long[] array = { 999910, 999911, 999912, 999913, 999914, 999915 };
		final long[] leftSubarray = { 999910, 999911, 999912, 999913 };
		final long[] midSubarray = { 999911, 999912, 999913, 999914 };
		final long[] rightSubarray = { 999912, 999913, 999914, 999915 };

		assertTrue("0 start, mid end", Arrays.equals(leftSubarray, Arrays.subarray(array, 0, 4)));

		assertTrue("0 start, length end", Arrays.equals(array, Arrays.subarray(array, 0, array.length)));

		assertTrue("mid start, mid end", Arrays.equals(midSubarray, Arrays.subarray(array, 1, 5)));

		assertTrue("mid start, length end", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, array.length)));

		assertNull("null input", Arrays.subarray(nullArray, 0, 3));

		assertEquals("empty array", Arrays.EMPTY_LONG_ARRAY, Arrays.subarray(Arrays.EMPTY_LONG_ARRAY, 1, 2));

		assertEquals("start > end", Arrays.EMPTY_LONG_ARRAY, Arrays.subarray(array, 4, 2));

		assertEquals("start == end", Arrays.EMPTY_LONG_ARRAY, Arrays.subarray(array, 3, 3));

		assertTrue("start undershoot, normal end", Arrays.equals(leftSubarray, Arrays.subarray(array, -2, 4)));

		assertEquals("start overshoot, any end", Arrays.EMPTY_LONG_ARRAY, Arrays.subarray(array, 33, 4));

		assertTrue("normal start, end overshoot", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, 33)));

		assertTrue("start undershoot, end overshoot", Arrays.equals(array, Arrays.subarray(array, -2, 12)));

		// empty-return tests

		assertSame("empty array, object test", Arrays.EMPTY_LONG_ARRAY, Arrays.subarray(Arrays.EMPTY_LONG_ARRAY, 1, 2));

		assertSame("start > end, object test", Arrays.EMPTY_LONG_ARRAY, Arrays.subarray(array, 4, 1));

		assertSame("start == end, object test", Arrays.EMPTY_LONG_ARRAY, Arrays.subarray(array, 3, 3));

		assertSame("start overshoot, any end, object test", Arrays.EMPTY_LONG_ARRAY, Arrays.subarray(array, 8733, 4));

		// array type tests

		assertSame("long type", long.class, Arrays.subarray(array, 2, 4).getClass().getComponentType());

	}

	@Test
	public void testSubarrayInt() {
		final int[] nullArray = null;
		final int[] array = { 10, 11, 12, 13, 14, 15 };
		final int[] leftSubarray = { 10, 11, 12, 13 };
		final int[] midSubarray = { 11, 12, 13, 14 };
		final int[] rightSubarray = { 12, 13, 14, 15 };

		assertTrue("0 start, mid end", Arrays.equals(leftSubarray, Arrays.subarray(array, 0, 4)));

		assertTrue("0 start, length end", Arrays.equals(array, Arrays.subarray(array, 0, array.length)));

		assertTrue("mid start, mid end", Arrays.equals(midSubarray, Arrays.subarray(array, 1, 5)));

		assertTrue("mid start, length end", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, array.length)));

		assertNull("null input", Arrays.subarray(nullArray, 0, 3));

		assertEquals("empty array", Arrays.EMPTY_INT_ARRAY, Arrays.subarray(Arrays.EMPTY_INT_ARRAY, 1, 2));

		assertEquals("start > end", Arrays.EMPTY_INT_ARRAY, Arrays.subarray(array, 4, 2));

		assertEquals("start == end", Arrays.EMPTY_INT_ARRAY, Arrays.subarray(array, 3, 3));

		assertTrue("start undershoot, normal end", Arrays.equals(leftSubarray, Arrays.subarray(array, -2, 4)));

		assertEquals("start overshoot, any end", Arrays.EMPTY_INT_ARRAY, Arrays.subarray(array, 33, 4));

		assertTrue("normal start, end overshoot", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, 33)));

		assertTrue("start undershoot, end overshoot", Arrays.equals(array, Arrays.subarray(array, -2, 12)));

		// empty-return tests

		assertSame("empty array, object test", Arrays.EMPTY_INT_ARRAY, Arrays.subarray(Arrays.EMPTY_INT_ARRAY, 1, 2));

		assertSame("start > end, object test", Arrays.EMPTY_INT_ARRAY, Arrays.subarray(array, 4, 1));

		assertSame("start == end, object test", Arrays.EMPTY_INT_ARRAY, Arrays.subarray(array, 3, 3));

		assertSame("start overshoot, any end, object test", Arrays.EMPTY_INT_ARRAY, Arrays.subarray(array, 8733, 4));

		// array type tests

		assertSame("int type", int.class, Arrays.subarray(array, 2, 4).getClass().getComponentType());

	}

	@Test
	public void testSubarrayShort() {
		final short[] nullArray = null;
		final short[] array = { 10, 11, 12, 13, 14, 15 };
		final short[] leftSubarray = { 10, 11, 12, 13 };
		final short[] midSubarray = { 11, 12, 13, 14 };
		final short[] rightSubarray = { 12, 13, 14, 15 };

		assertTrue("0 start, mid end", Arrays.equals(leftSubarray, Arrays.subarray(array, 0, 4)));

		assertTrue("0 start, length end", Arrays.equals(array, Arrays.subarray(array, 0, array.length)));

		assertTrue("mid start, mid end", Arrays.equals(midSubarray, Arrays.subarray(array, 1, 5)));

		assertTrue("mid start, length end", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, array.length)));

		assertNull("null input", Arrays.subarray(nullArray, 0, 3));

		assertEquals("empty array", Arrays.EMPTY_SHORT_ARRAY, Arrays.subarray(Arrays.EMPTY_SHORT_ARRAY, 1, 2));

		assertEquals("start > end", Arrays.EMPTY_SHORT_ARRAY, Arrays.subarray(array, 4, 2));

		assertEquals("start == end", Arrays.EMPTY_SHORT_ARRAY, Arrays.subarray(array, 3, 3));

		assertTrue("start undershoot, normal end", Arrays.equals(leftSubarray, Arrays.subarray(array, -2, 4)));

		assertEquals("start overshoot, any end", Arrays.EMPTY_SHORT_ARRAY, Arrays.subarray(array, 33, 4));

		assertTrue("normal start, end overshoot", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, 33)));

		assertTrue("start undershoot, end overshoot", Arrays.equals(array, Arrays.subarray(array, -2, 12)));

		// empty-return tests

		assertSame("empty array, object test", Arrays.EMPTY_SHORT_ARRAY,
			Arrays.subarray(Arrays.EMPTY_SHORT_ARRAY, 1, 2));

		assertSame("start > end, object test", Arrays.EMPTY_SHORT_ARRAY, Arrays.subarray(array, 4, 1));

		assertSame("start == end, object test", Arrays.EMPTY_SHORT_ARRAY, Arrays.subarray(array, 3, 3));

		assertSame("start overshoot, any end, object test", Arrays.EMPTY_SHORT_ARRAY, Arrays.subarray(array, 8733, 4));

		// array type tests

		assertSame("short type", short.class, Arrays.subarray(array, 2, 4).getClass().getComponentType());

	}

	@Test
	public void testSubarrChar() {
		final char[] nullArray = null;
		final char[] array = { 'a', 'b', 'c', 'd', 'e', 'f' };
		final char[] leftSubarray = { 'a', 'b', 'c', 'd', };
		final char[] midSubarray = { 'b', 'c', 'd', 'e', };
		final char[] rightSubarray = { 'c', 'd', 'e', 'f', };

		assertTrue("0 start, mid end", Arrays.equals(leftSubarray, Arrays.subarray(array, 0, 4)));

		assertTrue("0 start, length end", Arrays.equals(array, Arrays.subarray(array, 0, array.length)));

		assertTrue("mid start, mid end", Arrays.equals(midSubarray, Arrays.subarray(array, 1, 5)));

		assertTrue("mid start, length end", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, array.length)));

		assertNull("null input", Arrays.subarray(nullArray, 0, 3));

		assertEquals("empty array", Arrays.EMPTY_CHAR_ARRAY, Arrays.subarray(Arrays.EMPTY_CHAR_ARRAY, 1, 2));

		assertEquals("start > end", Arrays.EMPTY_CHAR_ARRAY, Arrays.subarray(array, 4, 2));

		assertEquals("start == end", Arrays.EMPTY_CHAR_ARRAY, Arrays.subarray(array, 3, 3));

		assertTrue("start undershoot, normal end", Arrays.equals(leftSubarray, Arrays.subarray(array, -2, 4)));

		assertEquals("start overshoot, any end", Arrays.EMPTY_CHAR_ARRAY, Arrays.subarray(array, 33, 4));

		assertTrue("normal start, end overshoot", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, 33)));

		assertTrue("start undershoot, end overshoot", Arrays.equals(array, Arrays.subarray(array, -2, 12)));

		// empty-return tests

		assertSame("empty array, object test", Arrays.EMPTY_CHAR_ARRAY, Arrays.subarray(Arrays.EMPTY_CHAR_ARRAY, 1, 2));

		assertSame("start > end, object test", Arrays.EMPTY_CHAR_ARRAY, Arrays.subarray(array, 4, 1));

		assertSame("start == end, object test", Arrays.EMPTY_CHAR_ARRAY, Arrays.subarray(array, 3, 3));

		assertSame("start overshoot, any end, object test", Arrays.EMPTY_CHAR_ARRAY, Arrays.subarray(array, 8733, 4));

		// array type tests

		assertSame("char type", char.class, Arrays.subarray(array, 2, 4).getClass().getComponentType());

	}

	@Test
	public void testSubarrayByte() {
		final byte[] nullArray = null;
		final byte[] array = { 10, 11, 12, 13, 14, 15 };
		final byte[] leftSubarray = { 10, 11, 12, 13 };
		final byte[] midSubarray = { 11, 12, 13, 14 };
		final byte[] rightSubarray = { 12, 13, 14, 15 };

		assertTrue("0 start, mid end", Arrays.equals(leftSubarray, Arrays.subarray(array, 0, 4)));

		assertTrue("0 start, length end", Arrays.equals(array, Arrays.subarray(array, 0, array.length)));

		assertTrue("mid start, mid end", Arrays.equals(midSubarray, Arrays.subarray(array, 1, 5)));

		assertTrue("mid start, length end", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, array.length)));

		assertNull("null input", Arrays.subarray(nullArray, 0, 3));

		assertEquals("empty array", Arrays.EMPTY_BYTE_ARRAY, Arrays.subarray(Arrays.EMPTY_BYTE_ARRAY, 1, 2));

		assertEquals("start > end", Arrays.EMPTY_BYTE_ARRAY, Arrays.subarray(array, 4, 2));

		assertEquals("start == end", Arrays.EMPTY_BYTE_ARRAY, Arrays.subarray(array, 3, 3));

		assertTrue("start undershoot, normal end", Arrays.equals(leftSubarray, Arrays.subarray(array, -2, 4)));

		assertEquals("start overshoot, any end", Arrays.EMPTY_BYTE_ARRAY, Arrays.subarray(array, 33, 4));

		assertTrue("normal start, end overshoot", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, 33)));

		assertTrue("start undershoot, end overshoot", Arrays.equals(array, Arrays.subarray(array, -2, 12)));

		// empty-return tests

		assertSame("empty array, object test", Arrays.EMPTY_BYTE_ARRAY, Arrays.subarray(Arrays.EMPTY_BYTE_ARRAY, 1, 2));

		assertSame("start > end, object test", Arrays.EMPTY_BYTE_ARRAY, Arrays.subarray(array, 4, 1));

		assertSame("start == end, object test", Arrays.EMPTY_BYTE_ARRAY, Arrays.subarray(array, 3, 3));

		assertSame("start overshoot, any end, object test", Arrays.EMPTY_BYTE_ARRAY, Arrays.subarray(array, 8733, 4));

		// array type tests

		assertSame("byte type", byte.class, Arrays.subarray(array, 2, 4).getClass().getComponentType());

	}

	@Test
	public void testSubarrayDouble() {
		final double[] nullArray = null;
		final double[] array = { 10.123, 11.234, 12.345, 13.456, 14.567, 15.678 };
		final double[] leftSubarray = { 10.123, 11.234, 12.345, 13.456, };
		final double[] midSubarray = { 11.234, 12.345, 13.456, 14.567, };
		final double[] rightSubarray = { 12.345, 13.456, 14.567, 15.678 };

		assertTrue("0 start, mid end", Arrays.equals(leftSubarray, Arrays.subarray(array, 0, 4)));

		assertTrue("0 start, length end", Arrays.equals(array, Arrays.subarray(array, 0, array.length)));

		assertTrue("mid start, mid end", Arrays.equals(midSubarray, Arrays.subarray(array, 1, 5)));

		assertTrue("mid start, length end", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, array.length)));

		assertNull("null input", Arrays.subarray(nullArray, 0, 3));

		assertEquals("empty array", Arrays.EMPTY_DOUBLE_ARRAY, Arrays.subarray(Arrays.EMPTY_DOUBLE_ARRAY, 1, 2));

		assertEquals("start > end", Arrays.EMPTY_DOUBLE_ARRAY, Arrays.subarray(array, 4, 2));

		assertEquals("start == end", Arrays.EMPTY_DOUBLE_ARRAY, Arrays.subarray(array, 3, 3));

		assertTrue("start undershoot, normal end", Arrays.equals(leftSubarray, Arrays.subarray(array, -2, 4)));

		assertEquals("start overshoot, any end", Arrays.EMPTY_DOUBLE_ARRAY, Arrays.subarray(array, 33, 4));

		assertTrue("normal start, end overshoot", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, 33)));

		assertTrue("start undershoot, end overshoot", Arrays.equals(array, Arrays.subarray(array, -2, 12)));

		// empty-return tests

		assertSame("empty array, object test", Arrays.EMPTY_DOUBLE_ARRAY,
			Arrays.subarray(Arrays.EMPTY_DOUBLE_ARRAY, 1, 2));

		assertSame("start > end, object test", Arrays.EMPTY_DOUBLE_ARRAY, Arrays.subarray(array, 4, 1));

		assertSame("start == end, object test", Arrays.EMPTY_DOUBLE_ARRAY, Arrays.subarray(array, 3, 3));

		assertSame("start overshoot, any end, object test", Arrays.EMPTY_DOUBLE_ARRAY, Arrays.subarray(array, 8733, 4));

		// array type tests

		assertSame("double type", double.class, Arrays.subarray(array, 2, 4).getClass().getComponentType());

	}

	@Test
	public void testSubarrayFloat() {
		final float[] nullArray = null;
		final float[] array = { 10, 11, 12, 13, 14, 15 };
		final float[] leftSubarray = { 10, 11, 12, 13 };
		final float[] midSubarray = { 11, 12, 13, 14 };
		final float[] rightSubarray = { 12, 13, 14, 15 };

		assertTrue("0 start, mid end", Arrays.equals(leftSubarray, Arrays.subarray(array, 0, 4)));

		assertTrue("0 start, length end", Arrays.equals(array, Arrays.subarray(array, 0, array.length)));

		assertTrue("mid start, mid end", Arrays.equals(midSubarray, Arrays.subarray(array, 1, 5)));

		assertTrue("mid start, length end", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, array.length)));

		assertNull("null input", Arrays.subarray(nullArray, 0, 3));

		assertEquals("empty array", Arrays.EMPTY_FLOAT_ARRAY, Arrays.subarray(Arrays.EMPTY_FLOAT_ARRAY, 1, 2));

		assertEquals("start > end", Arrays.EMPTY_FLOAT_ARRAY, Arrays.subarray(array, 4, 2));

		assertEquals("start == end", Arrays.EMPTY_FLOAT_ARRAY, Arrays.subarray(array, 3, 3));

		assertTrue("start undershoot, normal end", Arrays.equals(leftSubarray, Arrays.subarray(array, -2, 4)));

		assertEquals("start overshoot, any end", Arrays.EMPTY_FLOAT_ARRAY, Arrays.subarray(array, 33, 4));

		assertTrue("normal start, end overshoot", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, 33)));

		assertTrue("start undershoot, end overshoot", Arrays.equals(array, Arrays.subarray(array, -2, 12)));

		// empty-return tests

		assertSame("empty array, object test", Arrays.EMPTY_FLOAT_ARRAY,
			Arrays.subarray(Arrays.EMPTY_FLOAT_ARRAY, 1, 2));

		assertSame("start > end, object test", Arrays.EMPTY_FLOAT_ARRAY, Arrays.subarray(array, 4, 1));

		assertSame("start == end, object test", Arrays.EMPTY_FLOAT_ARRAY, Arrays.subarray(array, 3, 3));

		assertSame("start overshoot, any end, object test", Arrays.EMPTY_FLOAT_ARRAY, Arrays.subarray(array, 8733, 4));

		// array type tests

		assertSame("float type", float.class, Arrays.subarray(array, 2, 4).getClass().getComponentType());

	}

	@Test
	public void testSubarrayBoolean() {
		final boolean[] nullArray = null;
		final boolean[] array = { true, true, false, true, false, true };
		final boolean[] leftSubarray = { true, true, false, true };
		final boolean[] midSubarray = { true, false, true, false };
		final boolean[] rightSubarray = { false, true, false, true };

		assertTrue("0 start, mid end", Arrays.equals(leftSubarray, Arrays.subarray(array, 0, 4)));

		assertTrue("0 start, length end", Arrays.equals(array, Arrays.subarray(array, 0, array.length)));

		assertTrue("mid start, mid end", Arrays.equals(midSubarray, Arrays.subarray(array, 1, 5)));

		assertTrue("mid start, length end", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, array.length)));

		assertNull("null input", Arrays.subarray(nullArray, 0, 3));

		assertEquals("empty array", Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.subarray(Arrays.EMPTY_BOOLEAN_ARRAY, 1, 2));

		assertEquals("start > end", Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.subarray(array, 4, 2));

		assertEquals("start == end", Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.subarray(array, 3, 3));

		assertTrue("start undershoot, normal end", Arrays.equals(leftSubarray, Arrays.subarray(array, -2, 4)));

		assertEquals("start overshoot, any end", Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.subarray(array, 33, 4));

		assertTrue("normal start, end overshoot", Arrays.equals(rightSubarray, Arrays.subarray(array, 2, 33)));

		assertTrue("start undershoot, end overshoot", Arrays.equals(array, Arrays.subarray(array, -2, 12)));

		// empty-return tests

		assertSame("empty array, object test", Arrays.EMPTY_BOOLEAN_ARRAY,
			Arrays.subarray(Arrays.EMPTY_BOOLEAN_ARRAY, 1, 2));

		assertSame("start > end, object test", Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.subarray(array, 4, 1));

		assertSame("start == end, object test", Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.subarray(array, 3, 3));

		assertSame("start overshoot, any end, object test", Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.subarray(array, 8733, 4));

		// array type tests

		assertSame("boolean type", boolean.class, Arrays.subarray(array, 2, 4).getClass().getComponentType());

	}

	// -----------------------------------------------------------------------
	@Test
	public void testSameLength() {
		final Object[] nullArray = null;
		final Object[] emptyArray = new Object[0];
		final Object[] oneArray = new Object[] { "pick" };
		final Object[] twoArray = new Object[] { "pick", "stick" };

		assertTrue(Arrays.isSameLength(nullArray, nullArray));
		assertTrue(Arrays.isSameLength(nullArray, emptyArray));
		assertFalse(Arrays.isSameLength(nullArray, oneArray));
		assertFalse(Arrays.isSameLength(nullArray, twoArray));

		assertTrue(Arrays.isSameLength(emptyArray, nullArray));
		assertTrue(Arrays.isSameLength(emptyArray, emptyArray));
		assertFalse(Arrays.isSameLength(emptyArray, oneArray));
		assertFalse(Arrays.isSameLength(emptyArray, twoArray));

		assertFalse(Arrays.isSameLength(oneArray, nullArray));
		assertFalse(Arrays.isSameLength(oneArray, emptyArray));
		assertTrue(Arrays.isSameLength(oneArray, oneArray));
		assertFalse(Arrays.isSameLength(oneArray, twoArray));

		assertFalse(Arrays.isSameLength(twoArray, nullArray));
		assertFalse(Arrays.isSameLength(twoArray, emptyArray));
		assertFalse(Arrays.isSameLength(twoArray, oneArray));
		assertTrue(Arrays.isSameLength(twoArray, twoArray));
	}

	@Test
	public void testSameLengthBoolean() {
		final boolean[] nullArray = null;
		final boolean[] emptyArray = new boolean[0];
		final boolean[] oneArray = new boolean[] { true };
		final boolean[] twoArray = new boolean[] { true, false };

		assertTrue(Arrays.isSameLength(nullArray, nullArray));
		assertTrue(Arrays.isSameLength(nullArray, emptyArray));
		assertFalse(Arrays.isSameLength(nullArray, oneArray));
		assertFalse(Arrays.isSameLength(nullArray, twoArray));

		assertTrue(Arrays.isSameLength(emptyArray, nullArray));
		assertTrue(Arrays.isSameLength(emptyArray, emptyArray));
		assertFalse(Arrays.isSameLength(emptyArray, oneArray));
		assertFalse(Arrays.isSameLength(emptyArray, twoArray));

		assertFalse(Arrays.isSameLength(oneArray, nullArray));
		assertFalse(Arrays.isSameLength(oneArray, emptyArray));
		assertTrue(Arrays.isSameLength(oneArray, oneArray));
		assertFalse(Arrays.isSameLength(oneArray, twoArray));

		assertFalse(Arrays.isSameLength(twoArray, nullArray));
		assertFalse(Arrays.isSameLength(twoArray, emptyArray));
		assertFalse(Arrays.isSameLength(twoArray, oneArray));
		assertTrue(Arrays.isSameLength(twoArray, twoArray));
	}

	@Test
	public void testSameLengthLong() {
		final long[] nullArray = null;
		final long[] emptyArray = new long[0];
		final long[] oneArray = new long[] { 0L };
		final long[] twoArray = new long[] { 0L, 76L };

		assertTrue(Arrays.isSameLength(nullArray, nullArray));
		assertTrue(Arrays.isSameLength(nullArray, emptyArray));
		assertFalse(Arrays.isSameLength(nullArray, oneArray));
		assertFalse(Arrays.isSameLength(nullArray, twoArray));

		assertTrue(Arrays.isSameLength(emptyArray, nullArray));
		assertTrue(Arrays.isSameLength(emptyArray, emptyArray));
		assertFalse(Arrays.isSameLength(emptyArray, oneArray));
		assertFalse(Arrays.isSameLength(emptyArray, twoArray));

		assertFalse(Arrays.isSameLength(oneArray, nullArray));
		assertFalse(Arrays.isSameLength(oneArray, emptyArray));
		assertTrue(Arrays.isSameLength(oneArray, oneArray));
		assertFalse(Arrays.isSameLength(oneArray, twoArray));

		assertFalse(Arrays.isSameLength(twoArray, nullArray));
		assertFalse(Arrays.isSameLength(twoArray, emptyArray));
		assertFalse(Arrays.isSameLength(twoArray, oneArray));
		assertTrue(Arrays.isSameLength(twoArray, twoArray));
	}

	@Test
	public void testSameLengthInt() {
		final int[] nullArray = null;
		final int[] emptyArray = new int[0];
		final int[] oneArray = new int[] { 4 };
		final int[] twoArray = new int[] { 5, 7 };

		assertTrue(Arrays.isSameLength(nullArray, nullArray));
		assertTrue(Arrays.isSameLength(nullArray, emptyArray));
		assertFalse(Arrays.isSameLength(nullArray, oneArray));
		assertFalse(Arrays.isSameLength(nullArray, twoArray));

		assertTrue(Arrays.isSameLength(emptyArray, nullArray));
		assertTrue(Arrays.isSameLength(emptyArray, emptyArray));
		assertFalse(Arrays.isSameLength(emptyArray, oneArray));
		assertFalse(Arrays.isSameLength(emptyArray, twoArray));

		assertFalse(Arrays.isSameLength(oneArray, nullArray));
		assertFalse(Arrays.isSameLength(oneArray, emptyArray));
		assertTrue(Arrays.isSameLength(oneArray, oneArray));
		assertFalse(Arrays.isSameLength(oneArray, twoArray));

		assertFalse(Arrays.isSameLength(twoArray, nullArray));
		assertFalse(Arrays.isSameLength(twoArray, emptyArray));
		assertFalse(Arrays.isSameLength(twoArray, oneArray));
		assertTrue(Arrays.isSameLength(twoArray, twoArray));
	}

	@Test
	public void testSameLengthShort() {
		final short[] nullArray = null;
		final short[] emptyArray = new short[0];
		final short[] oneArray = new short[] { 4 };
		final short[] twoArray = new short[] { 6, 8 };

		assertTrue(Arrays.isSameLength(nullArray, nullArray));
		assertTrue(Arrays.isSameLength(nullArray, emptyArray));
		assertFalse(Arrays.isSameLength(nullArray, oneArray));
		assertFalse(Arrays.isSameLength(nullArray, twoArray));

		assertTrue(Arrays.isSameLength(emptyArray, nullArray));
		assertTrue(Arrays.isSameLength(emptyArray, emptyArray));
		assertFalse(Arrays.isSameLength(emptyArray, oneArray));
		assertFalse(Arrays.isSameLength(emptyArray, twoArray));

		assertFalse(Arrays.isSameLength(oneArray, nullArray));
		assertFalse(Arrays.isSameLength(oneArray, emptyArray));
		assertTrue(Arrays.isSameLength(oneArray, oneArray));
		assertFalse(Arrays.isSameLength(oneArray, twoArray));

		assertFalse(Arrays.isSameLength(twoArray, nullArray));
		assertFalse(Arrays.isSameLength(twoArray, emptyArray));
		assertFalse(Arrays.isSameLength(twoArray, oneArray));
		assertTrue(Arrays.isSameLength(twoArray, twoArray));
	}

	@Test
	public void testSameLengthChar() {
		final char[] nullArray = null;
		final char[] emptyArray = new char[0];
		final char[] oneArray = new char[] { 'f' };
		final char[] twoArray = new char[] { 'd', 't' };

		assertTrue(Arrays.isSameLength(nullArray, nullArray));
		assertTrue(Arrays.isSameLength(nullArray, emptyArray));
		assertFalse(Arrays.isSameLength(nullArray, oneArray));
		assertFalse(Arrays.isSameLength(nullArray, twoArray));

		assertTrue(Arrays.isSameLength(emptyArray, nullArray));
		assertTrue(Arrays.isSameLength(emptyArray, emptyArray));
		assertFalse(Arrays.isSameLength(emptyArray, oneArray));
		assertFalse(Arrays.isSameLength(emptyArray, twoArray));

		assertFalse(Arrays.isSameLength(oneArray, nullArray));
		assertFalse(Arrays.isSameLength(oneArray, emptyArray));
		assertTrue(Arrays.isSameLength(oneArray, oneArray));
		assertFalse(Arrays.isSameLength(oneArray, twoArray));

		assertFalse(Arrays.isSameLength(twoArray, nullArray));
		assertFalse(Arrays.isSameLength(twoArray, emptyArray));
		assertFalse(Arrays.isSameLength(twoArray, oneArray));
		assertTrue(Arrays.isSameLength(twoArray, twoArray));
	}

	@Test
	public void testSameLengthByte() {
		final byte[] nullArray = null;
		final byte[] emptyArray = new byte[0];
		final byte[] oneArray = new byte[] { 3 };
		final byte[] twoArray = new byte[] { 4, 6 };

		assertTrue(Arrays.isSameLength(nullArray, nullArray));
		assertTrue(Arrays.isSameLength(nullArray, emptyArray));
		assertFalse(Arrays.isSameLength(nullArray, oneArray));
		assertFalse(Arrays.isSameLength(nullArray, twoArray));

		assertTrue(Arrays.isSameLength(emptyArray, nullArray));
		assertTrue(Arrays.isSameLength(emptyArray, emptyArray));
		assertFalse(Arrays.isSameLength(emptyArray, oneArray));
		assertFalse(Arrays.isSameLength(emptyArray, twoArray));

		assertFalse(Arrays.isSameLength(oneArray, nullArray));
		assertFalse(Arrays.isSameLength(oneArray, emptyArray));
		assertTrue(Arrays.isSameLength(oneArray, oneArray));
		assertFalse(Arrays.isSameLength(oneArray, twoArray));

		assertFalse(Arrays.isSameLength(twoArray, nullArray));
		assertFalse(Arrays.isSameLength(twoArray, emptyArray));
		assertFalse(Arrays.isSameLength(twoArray, oneArray));
		assertTrue(Arrays.isSameLength(twoArray, twoArray));
	}

	@Test
	public void testSameLengthDouble() {
		final double[] nullArray = null;
		final double[] emptyArray = new double[0];
		final double[] oneArray = new double[] { 1.3d };
		final double[] twoArray = new double[] { 4.5d, 6.3d };

		assertTrue(Arrays.isSameLength(nullArray, nullArray));
		assertTrue(Arrays.isSameLength(nullArray, emptyArray));
		assertFalse(Arrays.isSameLength(nullArray, oneArray));
		assertFalse(Arrays.isSameLength(nullArray, twoArray));

		assertTrue(Arrays.isSameLength(emptyArray, nullArray));
		assertTrue(Arrays.isSameLength(emptyArray, emptyArray));
		assertFalse(Arrays.isSameLength(emptyArray, oneArray));
		assertFalse(Arrays.isSameLength(emptyArray, twoArray));

		assertFalse(Arrays.isSameLength(oneArray, nullArray));
		assertFalse(Arrays.isSameLength(oneArray, emptyArray));
		assertTrue(Arrays.isSameLength(oneArray, oneArray));
		assertFalse(Arrays.isSameLength(oneArray, twoArray));

		assertFalse(Arrays.isSameLength(twoArray, nullArray));
		assertFalse(Arrays.isSameLength(twoArray, emptyArray));
		assertFalse(Arrays.isSameLength(twoArray, oneArray));
		assertTrue(Arrays.isSameLength(twoArray, twoArray));
	}

	@Test
	public void testSameLengthFloat() {
		final float[] nullArray = null;
		final float[] emptyArray = new float[0];
		final float[] oneArray = new float[] { 2.5f };
		final float[] twoArray = new float[] { 6.4f, 5.8f };

		assertTrue(Arrays.isSameLength(nullArray, nullArray));
		assertTrue(Arrays.isSameLength(nullArray, emptyArray));
		assertFalse(Arrays.isSameLength(nullArray, oneArray));
		assertFalse(Arrays.isSameLength(nullArray, twoArray));

		assertTrue(Arrays.isSameLength(emptyArray, nullArray));
		assertTrue(Arrays.isSameLength(emptyArray, emptyArray));
		assertFalse(Arrays.isSameLength(emptyArray, oneArray));
		assertFalse(Arrays.isSameLength(emptyArray, twoArray));

		assertFalse(Arrays.isSameLength(oneArray, nullArray));
		assertFalse(Arrays.isSameLength(oneArray, emptyArray));
		assertTrue(Arrays.isSameLength(oneArray, oneArray));
		assertFalse(Arrays.isSameLength(oneArray, twoArray));

		assertFalse(Arrays.isSameLength(twoArray, nullArray));
		assertFalse(Arrays.isSameLength(twoArray, emptyArray));
		assertFalse(Arrays.isSameLength(twoArray, oneArray));
		assertTrue(Arrays.isSameLength(twoArray, twoArray));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSameType() {
		try {
			Arrays.isSameType(null, null);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			Arrays.isSameType(null, new Object[0]);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			Arrays.isSameType(new Object[0], null);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}

		assertTrue(Arrays.isSameType(new Object[0], new Object[0]));
		assertFalse(Arrays.isSameType(new String[0], new Object[0]));
		assertTrue(Arrays.isSameType(new String[0][0], new String[0][0]));
		assertFalse(Arrays.isSameType(new String[0], new String[0][0]));
		assertFalse(Arrays.isSameType(new String[0][0], new String[0]));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testReverse() {
		final StringBuffer str1 = new StringBuffer("pick");
		final String str2 = "a";
		final String[] str3 = new String[] { "stick" };
		final String str4 = "up";

		Object[] array = new Object[] { str1, str2, str3 };
		Arrays.reverse(array);
		assertEquals(array[0], str3);
		assertEquals(array[1], str2);
		assertEquals(array[2], str1);

		array = new Object[] { str1, str2, str3, str4 };
		Arrays.reverse(array);
		assertEquals(array[0], str4);
		assertEquals(array[1], str3);
		assertEquals(array[2], str2);
		assertEquals(array[3], str1);

		array = null;
		Arrays.reverse(array);
		assertArrayEquals(null, array);
	}

	@Test
	public void testReverseLong() {
		long[] array = new long[] { 1L, 2L, 3L };
		Arrays.reverse(array);
		assertEquals(array[0], 3L);
		assertEquals(array[1], 2L);
		assertEquals(array[2], 1L);

		array = null;
		Arrays.reverse(array);
		assertEquals(null, array);
	}

	@Test
	public void testReverseInt() {
		int[] array = new int[] { 1, 2, 3 };
		Arrays.reverse(array);
		assertEquals(array[0], 3);
		assertEquals(array[1], 2);
		assertEquals(array[2], 1);

		array = null;
		Arrays.reverse(array);
		assertEquals(null, array);
	}

	@Test
	public void testReverseShort() {
		short[] array = new short[] { 1, 2, 3 };
		Arrays.reverse(array);
		assertEquals(array[0], 3);
		assertEquals(array[1], 2);
		assertEquals(array[2], 1);

		array = null;
		Arrays.reverse(array);
		assertEquals(null, array);
	}

	@Test
	public void testReverseChar() {
		char[] array = new char[] { 'a', 'f', 'C' };
		Arrays.reverse(array);
		assertEquals(array[0], 'C');
		assertEquals(array[1], 'f');
		assertEquals(array[2], 'a');

		array = null;
		Arrays.reverse(array);
		assertEquals(null, array);
	}

	@Test
	public void testReverseByte() {
		byte[] array = new byte[] { 2, 3, 4 };
		Arrays.reverse(array);
		assertEquals(array[0], 4);
		assertEquals(array[1], 3);
		assertEquals(array[2], 2);

		array = null;
		Arrays.reverse(array);
		assertEquals(null, array);
	}

	@Test
	public void testReverseDouble() {
		double[] array = new double[] { 0.3d, 0.4d, 0.5d };
		Arrays.reverse(array);
		assertEquals(array[0], 0.5d, 0.0d);
		assertEquals(array[1], 0.4d, 0.0d);
		assertEquals(array[2], 0.3d, 0.0d);

		array = null;
		Arrays.reverse(array);
		assertEquals(null, array);
	}

	@Test
	public void testReverseFloat() {
		float[] array = new float[] { 0.3f, 0.4f, 0.5f };
		Arrays.reverse(array);
		assertEquals(array[0], 0.5f, 0.0f);
		assertEquals(array[1], 0.4f, 0.0f);
		assertEquals(array[2], 0.3f, 0.0f);

		array = null;
		Arrays.reverse(array);
		assertEquals(null, array);
	}

	@Test
	public void testReverseBoolean() {
		boolean[] array = new boolean[] { false, false, true };
		Arrays.reverse(array);
		assertTrue(array[0]);
		assertFalse(array[1]);
		assertFalse(array[2]);

		array = null;
		Arrays.reverse(array);
		assertEquals(null, array);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIndexOf() {
		final Object[] array = new Object[] { "0", "1", "2", "3", null, "0" };
		assertEquals(-1, Arrays.indexOf((Object[])null, null));
		assertEquals(-1, Arrays.indexOf(null, "0"));
		assertEquals(-1, Arrays.indexOf(new Object[0], "0"));
		assertEquals(0, Arrays.indexOf(array, "0"));
		assertEquals(1, Arrays.indexOf(array, "1"));
		assertEquals(2, Arrays.indexOf(array, "2"));
		assertEquals(3, Arrays.indexOf(array, "3"));
		assertEquals(4, Arrays.indexOf(array, null));
		assertEquals(-1, Arrays.indexOf(array, "notInArray"));
	}

	@Test
	public void testIndexOfWithStartIndex() {
		final Object[] array = new Object[] { "0", "1", "2", "3", null, "0" };
		assertEquals(-1, Arrays.indexOf((Object[])null, null, 2));
		assertEquals(-1, Arrays.indexOf(new Object[0], "0", 0));
		assertEquals(-1, Arrays.indexOf(null, "0", 2));
		assertEquals(5, Arrays.indexOf(array, "0", 2));
		assertEquals(-1, Arrays.indexOf(array, "1", 2));
		assertEquals(2, Arrays.indexOf(array, "2", 2));
		assertEquals(3, Arrays.indexOf(array, "3", 2));
		assertEquals(4, Arrays.indexOf(array, null, 2));
		assertEquals(-1, Arrays.indexOf(array, "notInArray", 2));

		assertEquals(4, Arrays.indexOf(array, null, -1));
		assertEquals(-1, Arrays.indexOf(array, null, 8));
		assertEquals(-1, Arrays.indexOf(array, "0", 8));
	}

	@Test
	public void testIndexOfByteArray() {
		byte[] outer = { 1, 2, 3, 4 };
		assertEquals(0, Arrays.indexOf(outer, new byte[] { 1, 2 }));
		assertEquals(1, Arrays.indexOf(outer, new byte[] { 2, 3 }));
		assertEquals(2, Arrays.indexOf(outer, new byte[] { 3, 4 }));
		assertEquals(-1, Arrays.indexOf(outer, new byte[] { 4, 4 }));
		assertEquals(-1, Arrays.indexOf(outer, new byte[] { 4, 5 }));
		assertEquals(-1, Arrays.indexOf(outer, new byte[] { 4, 5, 6, 7, 8 }));
	}
	
	@Test
	public void testLastIndexOf() {
		final Object[] array = new Object[] { "0", "1", "2", "3", null, "0" };
		assertEquals(-1, Arrays.lastIndexOf(null, null));
		assertEquals(-1, Arrays.lastIndexOf(null, "0"));
		assertEquals(5, Arrays.lastIndexOf(array, "0"));
		assertEquals(1, Arrays.lastIndexOf(array, "1"));
		assertEquals(2, Arrays.lastIndexOf(array, "2"));
		assertEquals(3, Arrays.lastIndexOf(array, "3"));
		assertEquals(4, Arrays.lastIndexOf(array, null));
		assertEquals(-1, Arrays.lastIndexOf(array, "notInArray"));
	}

	@Test
	public void testLastIndexOfWithStartIndex() {
		final Object[] array = new Object[] { "0", "1", "2", "3", null, "0" };
		assertEquals(-1, Arrays.lastIndexOf(null, null, 2));
		assertEquals(-1, Arrays.lastIndexOf(null, "0", 2));
		assertEquals(0, Arrays.lastIndexOf(array, "0", 2));
		assertEquals(1, Arrays.lastIndexOf(array, "1", 2));
		assertEquals(2, Arrays.lastIndexOf(array, "2", 2));
		assertEquals(-1, Arrays.lastIndexOf(array, "3", 2));
		assertEquals(-1, Arrays.lastIndexOf(array, "3", -1));
		assertEquals(4, Arrays.lastIndexOf(array, null, 5));
		assertEquals(-1, Arrays.lastIndexOf(array, null, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, "notInArray", 5));

		assertEquals(-1, Arrays.lastIndexOf(array, null, -1));
		assertEquals(5, Arrays.lastIndexOf(array, "0", 88));
	}

	@Test
	public void testContains() {
		final Object[] array = new Object[] { "0", "1", "2", "3", null, "0" };
		assertFalse(Arrays.contains(null, null));
		assertFalse(Arrays.contains(null, "1"));
		assertTrue(Arrays.contains(array, "0"));
		assertTrue(Arrays.contains(array, "1"));
		assertTrue(Arrays.contains(array, "2"));
		assertTrue(Arrays.contains(array, "3"));
		assertTrue(Arrays.contains(array, null));
		assertFalse(Arrays.contains(array, "notInArray"));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIndexOfLong() {
		long[] array = null;
		assertEquals(-1, Arrays.indexOf(array, 0));
		array = new long[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.indexOf(array, 0));
		assertEquals(1, Arrays.indexOf(array, 1));
		assertEquals(2, Arrays.indexOf(array, 2));
		assertEquals(3, Arrays.indexOf(array, 3));
		assertEquals(-1, Arrays.indexOf(array, 99));
	}

	@Test
	public void testIndexOfLongWithStartIndex() {
		long[] array = null;
		assertEquals(-1, Arrays.indexOf(array, 0, 2));
		array = new long[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.indexOf(array, 0, 2));
		assertEquals(-1, Arrays.indexOf(array, 1, 2));
		assertEquals(2, Arrays.indexOf(array, 2, 2));
		assertEquals(3, Arrays.indexOf(array, 3, 2));
		assertEquals(3, Arrays.indexOf(array, 3, -1));
		assertEquals(-1, Arrays.indexOf(array, 99, 0));
		assertEquals(-1, Arrays.indexOf(array, 0, 6));
	}

	@Test
	public void testLastIndexOfLong() {
		long[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, 0));
		array = new long[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.lastIndexOf(array, 0));
		assertEquals(1, Arrays.lastIndexOf(array, 1));
		assertEquals(2, Arrays.lastIndexOf(array, 2));
		assertEquals(3, Arrays.lastIndexOf(array, 3));
		assertEquals(-1, Arrays.lastIndexOf(array, 99));
	}

	@Test
	public void testLastIndexOfLongWithStartIndex() {
		long[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, 0, 2));
		array = new long[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.lastIndexOf(array, 0, 2));
		assertEquals(1, Arrays.lastIndexOf(array, 1, 2));
		assertEquals(2, Arrays.lastIndexOf(array, 2, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, 3, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, 3, -1));
		assertEquals(-1, Arrays.lastIndexOf(array, 99, 4));
		assertEquals(4, Arrays.lastIndexOf(array, 0, 88));
	}

	@Test
	public void testContainsLong() {
		long[] array = null;
		assertFalse(Arrays.contains(array, 1));
		array = new long[] { 0, 1, 2, 3, 0 };
		assertTrue(Arrays.contains(array, 0));
		assertTrue(Arrays.contains(array, 1));
		assertTrue(Arrays.contains(array, 2));
		assertTrue(Arrays.contains(array, 3));
		assertFalse(Arrays.contains(array, 99));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIndexOfInt() {
		int[] array = null;
		assertEquals(-1, Arrays.indexOf(array, 0));
		array = new int[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.indexOf(array, 0));
		assertEquals(1, Arrays.indexOf(array, 1));
		assertEquals(2, Arrays.indexOf(array, 2));
		assertEquals(3, Arrays.indexOf(array, 3));
		assertEquals(-1, Arrays.indexOf(array, 99));
	}

	@Test
	public void testIndexOfIntWithStartIndex() {
		int[] array = null;
		assertEquals(-1, Arrays.indexOf(array, 0, 2));
		array = new int[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.indexOf(array, 0, 2));
		assertEquals(-1, Arrays.indexOf(array, 1, 2));
		assertEquals(2, Arrays.indexOf(array, 2, 2));
		assertEquals(3, Arrays.indexOf(array, 3, 2));
		assertEquals(3, Arrays.indexOf(array, 3, -1));
		assertEquals(-1, Arrays.indexOf(array, 99, 0));
		assertEquals(-1, Arrays.indexOf(array, 0, 6));
	}

	@Test
	public void testLastIndexOfInt() {
		int[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, 0));
		array = new int[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.lastIndexOf(array, 0));
		assertEquals(1, Arrays.lastIndexOf(array, 1));
		assertEquals(2, Arrays.lastIndexOf(array, 2));
		assertEquals(3, Arrays.lastIndexOf(array, 3));
		assertEquals(-1, Arrays.lastIndexOf(array, 99));
	}

	@Test
	public void testLastIndexOfIntWithStartIndex() {
		int[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, 0, 2));
		array = new int[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.lastIndexOf(array, 0, 2));
		assertEquals(1, Arrays.lastIndexOf(array, 1, 2));
		assertEquals(2, Arrays.lastIndexOf(array, 2, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, 3, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, 3, -1));
		assertEquals(-1, Arrays.lastIndexOf(array, 99));
		assertEquals(4, Arrays.lastIndexOf(array, 0, 88));
	}

	@Test
	public void testContainsInt() {
		int[] array = null;
		assertFalse(Arrays.contains(array, 1));
		array = new int[] { 0, 1, 2, 3, 0 };
		assertTrue(Arrays.contains(array, 0));
		assertTrue(Arrays.contains(array, 1));
		assertTrue(Arrays.contains(array, 2));
		assertTrue(Arrays.contains(array, 3));
		assertFalse(Arrays.contains(array, 99));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIndexOfShort() {
		short[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (short)0));
		array = new short[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.indexOf(array, (short)0));
		assertEquals(1, Arrays.indexOf(array, (short)1));
		assertEquals(2, Arrays.indexOf(array, (short)2));
		assertEquals(3, Arrays.indexOf(array, (short)3));
		assertEquals(-1, Arrays.indexOf(array, (short)99));
	}

	@Test
	public void testIndexOfShortWithStartIndex() {
		short[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (short)0, 2));
		array = new short[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.indexOf(array, (short)0, 2));
		assertEquals(-1, Arrays.indexOf(array, (short)1, 2));
		assertEquals(2, Arrays.indexOf(array, (short)2, 2));
		assertEquals(3, Arrays.indexOf(array, (short)3, 2));
		assertEquals(3, Arrays.indexOf(array, (short)3, -1));
		assertEquals(-1, Arrays.indexOf(array, (short)99, 0));
		assertEquals(-1, Arrays.indexOf(array, (short)0, 6));
	}

	@Test
	public void testLastIndexOfShort() {
		short[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (short)0));
		array = new short[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.lastIndexOf(array, (short)0));
		assertEquals(1, Arrays.lastIndexOf(array, (short)1));
		assertEquals(2, Arrays.lastIndexOf(array, (short)2));
		assertEquals(3, Arrays.lastIndexOf(array, (short)3));
		assertEquals(-1, Arrays.lastIndexOf(array, (short)99));
	}

	@Test
	public void testLastIndexOfShortWithStartIndex() {
		short[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (short)0, 2));
		array = new short[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.lastIndexOf(array, (short)0, 2));
		assertEquals(1, Arrays.lastIndexOf(array, (short)1, 2));
		assertEquals(2, Arrays.lastIndexOf(array, (short)2, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, (short)3, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, (short)3, -1));
		assertEquals(-1, Arrays.lastIndexOf(array, (short)99));
		assertEquals(4, Arrays.lastIndexOf(array, (short)0, 88));
	}

	@Test
	public void testContainsShort() {
		short[] array = null;
		assertFalse(Arrays.contains(array, (short)1));
		array = new short[] { 0, 1, 2, 3, 0 };
		assertTrue(Arrays.contains(array, (short)0));
		assertTrue(Arrays.contains(array, (short)1));
		assertTrue(Arrays.contains(array, (short)2));
		assertTrue(Arrays.contains(array, (short)3));
		assertFalse(Arrays.contains(array, (short)99));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIndexOfChar() {
		char[] array = null;
		assertEquals(-1, Arrays.indexOf(array, 'a'));
		array = new char[] { 'a', 'b', 'c', 'd', 'a' };
		assertEquals(0, Arrays.indexOf(array, 'a'));
		assertEquals(1, Arrays.indexOf(array, 'b'));
		assertEquals(2, Arrays.indexOf(array, 'c'));
		assertEquals(3, Arrays.indexOf(array, 'd'));
		assertEquals(-1, Arrays.indexOf(array, 'e'));
	}

	@Test
	public void testIndexOfCharWithStartIndex() {
		char[] array = null;
		assertEquals(-1, Arrays.indexOf(array, 'a', 2));
		array = new char[] { 'a', 'b', 'c', 'd', 'a' };
		assertEquals(4, Arrays.indexOf(array, 'a', 2));
		assertEquals(-1, Arrays.indexOf(array, 'b', 2));
		assertEquals(2, Arrays.indexOf(array, 'c', 2));
		assertEquals(3, Arrays.indexOf(array, 'd', 2));
		assertEquals(3, Arrays.indexOf(array, 'd', -1));
		assertEquals(-1, Arrays.indexOf(array, 'e', 0));
		assertEquals(-1, Arrays.indexOf(array, 'a', 6));
	}

	@Test
	public void testLastIndexOfChar() {
		char[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, 'a'));
		array = new char[] { 'a', 'b', 'c', 'd', 'a' };
		assertEquals(4, Arrays.lastIndexOf(array, 'a'));
		assertEquals(1, Arrays.lastIndexOf(array, 'b'));
		assertEquals(2, Arrays.lastIndexOf(array, 'c'));
		assertEquals(3, Arrays.lastIndexOf(array, 'd'));
		assertEquals(-1, Arrays.lastIndexOf(array, 'e'));
	}

	@Test
	public void testLastIndexOfCharWithStartIndex() {
		char[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, 'a', 2));
		array = new char[] { 'a', 'b', 'c', 'd', 'a' };
		assertEquals(0, Arrays.lastIndexOf(array, 'a', 2));
		assertEquals(1, Arrays.lastIndexOf(array, 'b', 2));
		assertEquals(2, Arrays.lastIndexOf(array, 'c', 2));
		assertEquals(-1, Arrays.lastIndexOf(array, 'd', 2));
		assertEquals(-1, Arrays.lastIndexOf(array, 'd', -1));
		assertEquals(-1, Arrays.lastIndexOf(array, 'e'));
		assertEquals(4, Arrays.lastIndexOf(array, 'a', 88));
	}

	@Test
	public void testContainsChar() {
		char[] array = null;
		assertFalse(Arrays.contains(array, 'b'));
		array = new char[] { 'a', 'b', 'c', 'd', 'a' };
		assertTrue(Arrays.contains(array, 'a'));
		assertTrue(Arrays.contains(array, 'b'));
		assertTrue(Arrays.contains(array, 'c'));
		assertTrue(Arrays.contains(array, 'd'));
		assertFalse(Arrays.contains(array, 'e'));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIndexOfByte() {
		byte[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (byte)0));
		array = new byte[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.indexOf(array, (byte)0));
		assertEquals(1, Arrays.indexOf(array, (byte)1));
		assertEquals(2, Arrays.indexOf(array, (byte)2));
		assertEquals(3, Arrays.indexOf(array, (byte)3));
		assertEquals(-1, Arrays.indexOf(array, (byte)99));
	}

	@Test
	public void testIndexOfByteWithStartIndex() {
		byte[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (byte)0, 2));
		array = new byte[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.indexOf(array, (byte)0, 2));
		assertEquals(-1, Arrays.indexOf(array, (byte)1, 2));
		assertEquals(2, Arrays.indexOf(array, (byte)2, 2));
		assertEquals(3, Arrays.indexOf(array, (byte)3, 2));
		assertEquals(3, Arrays.indexOf(array, (byte)3, -1));
		assertEquals(-1, Arrays.indexOf(array, (byte)99, 0));
		assertEquals(-1, Arrays.indexOf(array, (byte)0, 6));
	}

	@Test
	public void testLastIndexOfByte() {
		byte[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (byte)0));
		array = new byte[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.lastIndexOf(array, (byte)0));
		assertEquals(1, Arrays.lastIndexOf(array, (byte)1));
		assertEquals(2, Arrays.lastIndexOf(array, (byte)2));
		assertEquals(3, Arrays.lastIndexOf(array, (byte)3));
		assertEquals(-1, Arrays.lastIndexOf(array, (byte)99));
	}

	@Test
	public void testLastIndexOfByteWithStartIndex() {
		byte[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (byte)0, 2));
		array = new byte[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.lastIndexOf(array, (byte)0, 2));
		assertEquals(1, Arrays.lastIndexOf(array, (byte)1, 2));
		assertEquals(2, Arrays.lastIndexOf(array, (byte)2, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, (byte)3, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, (byte)3, -1));
		assertEquals(-1, Arrays.lastIndexOf(array, (byte)99));
		assertEquals(4, Arrays.lastIndexOf(array, (byte)0, 88));
	}

	@Test
	public void testContainsByte() {
		byte[] array = null;
		assertFalse(Arrays.contains(array, (byte)1));
		array = new byte[] { 0, 1, 2, 3, 0 };
		assertTrue(Arrays.contains(array, (byte)0));
		assertTrue(Arrays.contains(array, (byte)1));
		assertTrue(Arrays.contains(array, (byte)2));
		assertTrue(Arrays.contains(array, (byte)3));
		assertFalse(Arrays.contains(array, (byte)99));
	}

	// -----------------------------------------------------------------------
	@SuppressWarnings("cast")
	@Test
	public void testIndexOfDouble() {
		double[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (double)0));
		array = new double[0];
		assertEquals(-1, Arrays.indexOf(array, (double)0));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.indexOf(array, (double)0));
		assertEquals(1, Arrays.indexOf(array, (double)1));
		assertEquals(2, Arrays.indexOf(array, (double)2));
		assertEquals(3, Arrays.indexOf(array, (double)3));
		assertEquals(3, Arrays.indexOf(array, (double)3, -1));
		assertEquals(-1, Arrays.indexOf(array, (double)99));
	}

	@SuppressWarnings("cast")
	@Test
	public void testIndexOfDoubleTolerance() {
		double[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (double)0, (double)0));
		array = new double[0];
		assertEquals(-1, Arrays.indexOf(array, (double)0, (double)0));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.indexOf(array, (double)0, (double)0.3));
		assertEquals(2, Arrays.indexOf(array, (double)2.2, (double)0.35));
		assertEquals(3, Arrays.indexOf(array, (double)4.15, (double)2.0));
		assertEquals(1, Arrays.indexOf(array, (double)1.00001324, (double)0.0001));
	}

	@SuppressWarnings("cast")
	@Test
	public void testIndexOfDoubleWithStartIndex() {
		double[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (double)0, 2));
		array = new double[0];
		assertEquals(-1, Arrays.indexOf(array, (double)0, 2));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.indexOf(array, (double)0, 2));
		assertEquals(-1, Arrays.indexOf(array, (double)1, 2));
		assertEquals(2, Arrays.indexOf(array, (double)2, 2));
		assertEquals(3, Arrays.indexOf(array, (double)3, 2));
		assertEquals(-1, Arrays.indexOf(array, (double)99, 0));
		assertEquals(-1, Arrays.indexOf(array, (double)0, 6));
	}

	@SuppressWarnings("cast")
	@Test
	public void testIndexOfDoubleWithStartIndexTolerance() {
		double[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (double)0, 2, (double)0));
		array = new double[0];
		assertEquals(-1, Arrays.indexOf(array, (double)0, 2, (double)0));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertEquals(-1, Arrays.indexOf(array, (double)0, 99, (double)0.3));
		assertEquals(0, Arrays.indexOf(array, (double)0, 0, (double)0.3));
		assertEquals(4, Arrays.indexOf(array, (double)0, 3, (double)0.3));
		assertEquals(2, Arrays.indexOf(array, (double)2.2, 0, (double)0.35));
		assertEquals(3, Arrays.indexOf(array, (double)4.15, 0, (double)2.0));
		assertEquals(1, Arrays.indexOf(array, (double)1.00001324, 0, (double)0.0001));
		assertEquals(3, Arrays.indexOf(array, (double)4.15, -1, (double)2.0));
		assertEquals(1, Arrays.indexOf(array, (double)1.00001324, -300, (double)0.0001));
	}

	@SuppressWarnings("cast")
	@Test
	public void testLastIndexOfDouble() {
		double[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (double)0));
		array = new double[0];
		assertEquals(-1, Arrays.lastIndexOf(array, (double)0));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.lastIndexOf(array, (double)0));
		assertEquals(1, Arrays.lastIndexOf(array, (double)1));
		assertEquals(2, Arrays.lastIndexOf(array, (double)2));
		assertEquals(3, Arrays.lastIndexOf(array, (double)3));
		assertEquals(-1, Arrays.lastIndexOf(array, (double)99));
	}

	@SuppressWarnings("cast")
	@Test
	public void testLastIndexOfDoubleTolerance() {
		double[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (double)0, (double)0));
		array = new double[0];
		assertEquals(-1, Arrays.lastIndexOf(array, (double)0, (double)0));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.lastIndexOf(array, (double)0, (double)0.3));
		assertEquals(2, Arrays.lastIndexOf(array, (double)2.2, (double)0.35));
		assertEquals(3, Arrays.lastIndexOf(array, (double)4.15, (double)2.0));
		assertEquals(1, Arrays.lastIndexOf(array, (double)1.00001324, (double)0.0001));
	}

	@SuppressWarnings("cast")
	@Test
	public void testLastIndexOfDoubleWithStartIndex() {
		double[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (double)0, 2));
		array = new double[0];
		assertEquals(-1, Arrays.lastIndexOf(array, (double)0, 2));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.lastIndexOf(array, (double)0, 2));
		assertEquals(1, Arrays.lastIndexOf(array, (double)1, 2));
		assertEquals(2, Arrays.lastIndexOf(array, (double)2, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, (double)3, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, (double)3, -1));
		assertEquals(-1, Arrays.lastIndexOf(array, (double)99));
		assertEquals(4, Arrays.lastIndexOf(array, (double)0, 88));
	}

	@SuppressWarnings("cast")
	@Test
	public void testLastIndexOfDoubleWithStartIndexTolerance() {
		double[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (double)0, 2, (double)0));
		array = new double[0];
		assertEquals(-1, Arrays.lastIndexOf(array, (double)0, 2, (double)0));
		array = new double[] { (double)3 };
		assertEquals(-1, Arrays.lastIndexOf(array, (double)1, 0, (double)0));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.lastIndexOf(array, (double)0, 99, (double)0.3));
		assertEquals(0, Arrays.lastIndexOf(array, (double)0, 3, (double)0.3));
		assertEquals(2, Arrays.lastIndexOf(array, (double)2.2, 3, (double)0.35));
		assertEquals(3, Arrays.lastIndexOf(array, (double)4.15, array.length, (double)2.0));
		assertEquals(1, Arrays.lastIndexOf(array, (double)1.00001324, array.length, (double)0.0001));
		assertEquals(-1, Arrays.lastIndexOf(array, (double)4.15, -200, (double)2.0));
	}

	@SuppressWarnings("cast")
	@Test
	public void testContainsDouble() {
		double[] array = null;
		assertFalse(Arrays.contains(array, (double)1));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertTrue(Arrays.contains(array, (double)0));
		assertTrue(Arrays.contains(array, (double)1));
		assertTrue(Arrays.contains(array, (double)2));
		assertTrue(Arrays.contains(array, (double)3));
		assertFalse(Arrays.contains(array, (double)99));
	}

	@SuppressWarnings("cast")
	@Test
	public void testContainsDoubleTolerance() {
		double[] array = null;
		assertFalse(Arrays.contains(array, (double)1, (double)0));
		array = new double[] { 0, 1, 2, 3, 0 };
		assertFalse(Arrays.contains(array, (double)4.0, (double)0.33));
		assertFalse(Arrays.contains(array, (double)2.5, (double)0.49));
		assertTrue(Arrays.contains(array, (double)2.5, (double)0.50));
		assertTrue(Arrays.contains(array, (double)2.5, (double)0.51));
	}

	// -----------------------------------------------------------------------
	@SuppressWarnings("cast")
	@Test
	public void testIndexOfFloat() {
		float[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (float)0));
		array = new float[0];
		assertEquals(-1, Arrays.indexOf(array, (float)0));
		array = new float[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.indexOf(array, (float)0));
		assertEquals(1, Arrays.indexOf(array, (float)1));
		assertEquals(2, Arrays.indexOf(array, (float)2));
		assertEquals(3, Arrays.indexOf(array, (float)3));
		assertEquals(-1, Arrays.indexOf(array, (float)99));
	}

	@SuppressWarnings("cast")
	@Test
	public void testIndexOfFloatWithStartIndex() {
		float[] array = null;
		assertEquals(-1, Arrays.indexOf(array, (float)0, 2));
		array = new float[0];
		assertEquals(-1, Arrays.indexOf(array, (float)0, 2));
		array = new float[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.indexOf(array, (float)0, 2));
		assertEquals(-1, Arrays.indexOf(array, (float)1, 2));
		assertEquals(2, Arrays.indexOf(array, (float)2, 2));
		assertEquals(3, Arrays.indexOf(array, (float)3, 2));
		assertEquals(3, Arrays.indexOf(array, (float)3, -1));
		assertEquals(-1, Arrays.indexOf(array, (float)99, 0));
		assertEquals(-1, Arrays.indexOf(array, (float)0, 6));
	}

	@SuppressWarnings("cast")
	@Test
	public void testLastIndexOfFloat() {
		float[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (float)0));
		array = new float[0];
		assertEquals(-1, Arrays.lastIndexOf(array, (float)0));
		array = new float[] { 0, 1, 2, 3, 0 };
		assertEquals(4, Arrays.lastIndexOf(array, (float)0));
		assertEquals(1, Arrays.lastIndexOf(array, (float)1));
		assertEquals(2, Arrays.lastIndexOf(array, (float)2));
		assertEquals(3, Arrays.lastIndexOf(array, (float)3));
		assertEquals(-1, Arrays.lastIndexOf(array, (float)99));
	}

	@SuppressWarnings("cast")
	@Test
	public void testLastIndexOfFloatWithStartIndex() {
		float[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, (float)0, 2));
		array = new float[0];
		assertEquals(-1, Arrays.lastIndexOf(array, (float)0, 2));
		array = new float[] { 0, 1, 2, 3, 0 };
		assertEquals(0, Arrays.lastIndexOf(array, (float)0, 2));
		assertEquals(1, Arrays.lastIndexOf(array, (float)1, 2));
		assertEquals(2, Arrays.lastIndexOf(array, (float)2, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, (float)3, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, (float)3, -1));
		assertEquals(-1, Arrays.lastIndexOf(array, (float)99));
		assertEquals(4, Arrays.lastIndexOf(array, (float)0, 88));
	}

	@SuppressWarnings("cast")
	@Test
	public void testContainsFloat() {
		float[] array = null;
		assertFalse(Arrays.contains(array, (float)1));
		array = new float[] { 0, 1, 2, 3, 0 };
		assertTrue(Arrays.contains(array, (float)0));
		assertTrue(Arrays.contains(array, (float)1));
		assertTrue(Arrays.contains(array, (float)2));
		assertTrue(Arrays.contains(array, (float)3));
		assertFalse(Arrays.contains(array, (float)99));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIndexOfBoolean() {
		boolean[] array = null;
		assertEquals(-1, Arrays.indexOf(array, true));
		array = new boolean[0];
		assertEquals(-1, Arrays.indexOf(array, true));
		array = new boolean[] { true, false, true };
		assertEquals(0, Arrays.indexOf(array, true));
		assertEquals(1, Arrays.indexOf(array, false));
		array = new boolean[] { true, true };
		assertEquals(-1, Arrays.indexOf(array, false));
	}

	@Test
	public void testIndexOfBooleanWithStartIndex() {
		boolean[] array = null;
		assertEquals(-1, Arrays.indexOf(array, true, 2));
		array = new boolean[0];
		assertEquals(-1, Arrays.indexOf(array, true, 2));
		array = new boolean[] { true, false, true };
		assertEquals(2, Arrays.indexOf(array, true, 1));
		assertEquals(-1, Arrays.indexOf(array, false, 2));
		assertEquals(1, Arrays.indexOf(array, false, 0));
		assertEquals(1, Arrays.indexOf(array, false, -1));
		array = new boolean[] { true, true };
		assertEquals(-1, Arrays.indexOf(array, false, 0));
		assertEquals(-1, Arrays.indexOf(array, false, -1));
	}

	@Test
	public void testLastIndexOfBoolean() {
		boolean[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, true));
		array = new boolean[0];
		assertEquals(-1, Arrays.lastIndexOf(array, true));
		array = new boolean[] { true, false, true };
		assertEquals(2, Arrays.lastIndexOf(array, true));
		assertEquals(1, Arrays.lastIndexOf(array, false));
		array = new boolean[] { true, true };
		assertEquals(-1, Arrays.lastIndexOf(array, false));
	}

	@Test
	public void testLastIndexOfBooleanWithStartIndex() {
		boolean[] array = null;
		assertEquals(-1, Arrays.lastIndexOf(array, true, 2));
		array = new boolean[0];
		assertEquals(-1, Arrays.lastIndexOf(array, true, 2));
		array = new boolean[] { true, false, true };
		assertEquals(2, Arrays.lastIndexOf(array, true, 2));
		assertEquals(0, Arrays.lastIndexOf(array, true, 1));
		assertEquals(1, Arrays.lastIndexOf(array, false, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, true, -1));
		array = new boolean[] { true, true };
		assertEquals(-1, Arrays.lastIndexOf(array, false, 2));
		assertEquals(-1, Arrays.lastIndexOf(array, true, -1));
	}

	@Test
	public void testContainsBoolean() {
		boolean[] array = null;
		assertFalse(Arrays.contains(array, true));
		array = new boolean[] { true, false, true };
		assertTrue(Arrays.contains(array, true));
		assertTrue(Arrays.contains(array, false));
		array = new boolean[] { true, true };
		assertTrue(Arrays.contains(array, true));
		assertFalse(Arrays.contains(array, false));
	}

	// testToPrimitive/Object for boolean
	// -----------------------------------------------------------------------
	@Test
	public void testToPrimitive_boolean() {
		final Boolean[] b = null;
		assertEquals(null, Arrays.toPrimitive(b));
		assertSame(Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.toPrimitive(new Boolean[0]));
		assertTrue(Arrays.equals(new boolean[] { true, false, true },
			Arrays.toPrimitive(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE })));

		try {
			Arrays.toPrimitive(new Boolean[] { Boolean.TRUE, null });
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	@Test
	public void testToPrimitive_boolean_boolean() {
		assertEquals(null, Arrays.toPrimitive(null, false));
		assertSame(Arrays.EMPTY_BOOLEAN_ARRAY, Arrays.toPrimitive(new Boolean[0], false));
		assertTrue(Arrays.equals(new boolean[] { true, false, true },
			Arrays.toPrimitive(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE }, false)));
		assertTrue(Arrays.equals(new boolean[] { true, false, false },
			Arrays.toPrimitive(new Boolean[] { Boolean.TRUE, null, Boolean.FALSE }, false)));
		assertTrue(Arrays.equals(new boolean[] { true, true, false },
			Arrays.toPrimitive(new Boolean[] { Boolean.TRUE, null, Boolean.FALSE }, true)));
	}

	@Test
	public void testToObject_boolean() {
		final boolean[] b = null;
		assertArrayEquals(null, Arrays.toObject(b));
		assertSame(Arrays.EMPTY_BOOLEAN_OBJECT_ARRAY, Arrays.toObject(new boolean[0]));
		assertTrue(Arrays.equals(new Boolean[] { Boolean.TRUE, Boolean.FALSE, Boolean.TRUE },
			Arrays.toObject(new boolean[] { true, false, true })));
	}

	// testToPrimitive/Object for byte
	// -----------------------------------------------------------------------
	@Test
	public void testToPrimitive_char() {
		final Character[] b = null;
		assertEquals(null, Arrays.toPrimitive(b));

		assertSame(Arrays.EMPTY_CHAR_ARRAY, Arrays.toPrimitive(new Character[0]));

		assertTrue(Arrays.equals(
			new char[] { Character.MIN_VALUE, Character.MAX_VALUE, '0' },
			Arrays.toPrimitive(new Character[] { new Character(Character.MIN_VALUE),
					new Character(Character.MAX_VALUE), new Character('0') })));

		try {
			Arrays.toPrimitive(new Character[] { new Character(Character.MIN_VALUE), null });
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	@Test
	public void testToPrimitive_char_char() {
		final Character[] b = null;
		assertEquals(null, Arrays.toPrimitive(b, Character.MIN_VALUE));

		assertSame(Arrays.EMPTY_CHAR_ARRAY, Arrays.toPrimitive(new Character[0], (char)0));

		assertTrue(Arrays.equals(
			new char[] { Character.MIN_VALUE, Character.MAX_VALUE, '0' },
			Arrays.toPrimitive(new Character[] { new Character(Character.MIN_VALUE),
					new Character(Character.MAX_VALUE), new Character('0') }, Character.MIN_VALUE)));

		assertTrue(Arrays.equals(new char[] { Character.MIN_VALUE, Character.MAX_VALUE, '0' }, Arrays.toPrimitive(
			new Character[] { new Character(Character.MIN_VALUE), null, new Character('0') }, Character.MAX_VALUE)));
	}

	@Test
	public void testToObject_char() {
		final char[] b = null;
		assertArrayEquals(null, Arrays.toObject(b));

		assertSame(Arrays.EMPTY_CHARACTER_OBJECT_ARRAY, Arrays.toObject(new char[0]));

		assertTrue(Arrays.equals(new Character[] { new Character(Character.MIN_VALUE),
				new Character(Character.MAX_VALUE), new Character('0') },
			Arrays.toObject(new char[] { Character.MIN_VALUE, Character.MAX_VALUE, '0' })));
	}

	// testToPrimitive/Object for byte
	// -----------------------------------------------------------------------
	@Test
	public void testToPrimitive_byte() {
		final Byte[] b = null;
		assertEquals(null, Arrays.toPrimitive(b));

		assertSame(Arrays.EMPTY_BYTE_ARRAY, Arrays.toPrimitive(new Byte[0]));

		assertTrue(Arrays.equals(
			new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE, (byte)9999999 },
			Arrays.toPrimitive(new Byte[] { Byte.valueOf(Byte.MIN_VALUE), Byte.valueOf(Byte.MAX_VALUE),
					Byte.valueOf((byte)9999999) })));

		try {
			Arrays.toPrimitive(new Byte[] { Byte.valueOf(Byte.MIN_VALUE), null });
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	@Test
	public void testToPrimitive_byte_byte() {
		final Byte[] b = null;
		assertEquals(null, Arrays.toPrimitive(b, Byte.MIN_VALUE));

		assertSame(Arrays.EMPTY_BYTE_ARRAY, Arrays.toPrimitive(new Byte[0], (byte)1));

		assertTrue(Arrays.equals(new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE, (byte)9999999 }, Arrays.toPrimitive(
			new Byte[] { Byte.valueOf(Byte.MIN_VALUE), Byte.valueOf(Byte.MAX_VALUE), Byte.valueOf((byte)9999999) },
			Byte.MIN_VALUE)));

		assertTrue(Arrays.equals(new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE, (byte)9999999 }, Arrays.toPrimitive(
			new Byte[] { Byte.valueOf(Byte.MIN_VALUE), null, Byte.valueOf((byte)9999999) }, Byte.MAX_VALUE)));
	}

	@Test
	public void testToObject_byte() {
		final byte[] b = null;
		assertArrayEquals(null, Arrays.toObject(b));

		assertSame(Arrays.EMPTY_BYTE_OBJECT_ARRAY, Arrays.toObject(new byte[0]));

		assertTrue(Arrays.equals(
			new Byte[] { Byte.valueOf(Byte.MIN_VALUE), Byte.valueOf(Byte.MAX_VALUE), Byte.valueOf((byte)9999999) },
			Arrays.toObject(new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE, (byte)9999999 })));
	}

	// testToPrimitive/Object for short
	// -----------------------------------------------------------------------
	@Test
	public void testToPrimitive_short() {
		final Short[] b = null;
		assertEquals(null, Arrays.toPrimitive(b));

		assertSame(Arrays.EMPTY_SHORT_ARRAY, Arrays.toPrimitive(new Short[0]));

		assertTrue(Arrays.equals(
			new short[] { Short.MIN_VALUE, Short.MAX_VALUE, (short)9999999 },
			Arrays.toPrimitive(new Short[] { Short.valueOf(Short.MIN_VALUE), Short.valueOf(Short.MAX_VALUE),
					Short.valueOf((short)9999999) })));

		try {
			Arrays.toPrimitive(new Short[] { Short.valueOf(Short.MIN_VALUE), null });
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	@Test
	public void testToPrimitive_short_short() {
		final Short[] s = null;
		assertEquals(null, Arrays.toPrimitive(s, Short.MIN_VALUE));

		assertSame(Arrays.EMPTY_SHORT_ARRAY, Arrays.toPrimitive(new Short[0], Short.MIN_VALUE));

		assertTrue(Arrays.equals(
			new short[] { Short.MIN_VALUE, Short.MAX_VALUE, (short)9999999 },
			Arrays.toPrimitive(
				new Short[] { Short.valueOf(Short.MIN_VALUE), Short.valueOf(Short.MAX_VALUE),
						Short.valueOf((short)9999999) }, Short.MIN_VALUE)));

		assertTrue(Arrays.equals(new short[] { Short.MIN_VALUE, Short.MAX_VALUE, (short)9999999 }, Arrays.toPrimitive(
			new Short[] { Short.valueOf(Short.MIN_VALUE), null, Short.valueOf((short)9999999) }, Short.MAX_VALUE)));
	}

	@Test
	public void testToObject_short() {
		final short[] b = null;
		assertArrayEquals(null, Arrays.toObject(b));

		assertSame(Arrays.EMPTY_SHORT_OBJECT_ARRAY, Arrays.toObject(new short[0]));

		assertTrue(Arrays
			.equals(
				new Short[] { Short.valueOf(Short.MIN_VALUE), Short.valueOf(Short.MAX_VALUE),
						Short.valueOf((short)9999999) },
				Arrays.toObject(new short[] { Short.MIN_VALUE, Short.MAX_VALUE, (short)9999999 })));
	}

	// testToPrimitive/Object for int
	// -----------------------------------------------------------------------
	@Test
	public void testToPrimitive_int() {
		final Integer[] b = null;
		assertEquals(null, Arrays.toPrimitive(b));
		assertSame(Arrays.EMPTY_INT_ARRAY, Arrays.toPrimitive(new Integer[0]));
		assertTrue(Arrays.equals(
			new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE, 9999999 },
			Arrays.toPrimitive(new Integer[] { Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(Integer.MAX_VALUE),
					Integer.valueOf(9999999) })));

		try {
			Arrays.toPrimitive(new Integer[] { Integer.valueOf(Integer.MIN_VALUE), null });
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	@Test
	public void testToPrimitive_int_int() {
		final Long[] l = null;
		assertEquals(null, Arrays.toPrimitive(l, Integer.MIN_VALUE));
		assertSame(Arrays.EMPTY_INT_ARRAY, Arrays.toPrimitive(new Integer[0], 1));
		assertTrue(Arrays.equals(
			new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE, 9999999 },
			Arrays.toPrimitive(new Integer[] { Integer.valueOf(Integer.MIN_VALUE), Integer.valueOf(Integer.MAX_VALUE),
					Integer.valueOf(9999999) }, 1)));
		assertTrue(Arrays.equals(new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE, 9999999 }, Arrays.toPrimitive(
			new Integer[] { Integer.valueOf(Integer.MIN_VALUE), null, Integer.valueOf(9999999) }, Integer.MAX_VALUE)));
	}

	@Test
	public void testToPrimitive_intNull() {
		final Integer[] iArray = null;
		assertEquals(null, Arrays.toPrimitive(iArray, Integer.MIN_VALUE));
	}

	@Test
	public void testToObject_int() {
		final int[] b = null;
		assertArrayEquals(null, Arrays.toObject(b));

		assertSame(Arrays.EMPTY_INTEGER_OBJECT_ARRAY, Arrays.toObject(new int[0]));

		assertTrue(Arrays.equals(new Integer[] { Integer.valueOf(Integer.MIN_VALUE),
				Integer.valueOf(Integer.MAX_VALUE), Integer.valueOf(9999999) },
			Arrays.toObject(new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE, 9999999 })));
	}

	// testToPrimitive/Object for long
	// -----------------------------------------------------------------------
	@Test
	public void testToPrimitive_long() {
		final Long[] b = null;
		assertEquals(null, Arrays.toPrimitive(b));

		assertSame(Arrays.EMPTY_LONG_ARRAY, Arrays.toPrimitive(new Long[0]));

		assertTrue(Arrays.equals(
			new long[] { Long.MIN_VALUE, Long.MAX_VALUE, 9999999 },
			Arrays.toPrimitive(new Long[] { Long.valueOf(Long.MIN_VALUE), Long.valueOf(Long.MAX_VALUE),
					Long.valueOf(9999999) })));

		try {
			Arrays.toPrimitive(new Long[] { Long.valueOf(Long.MIN_VALUE), null });
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	@Test
	public void testToPrimitive_long_long() {
		final Long[] l = null;
		assertEquals(null, Arrays.toPrimitive(l, Long.MIN_VALUE));

		assertSame(Arrays.EMPTY_LONG_ARRAY, Arrays.toPrimitive(new Long[0], 1));

		assertTrue(Arrays.equals(
			new long[] { Long.MIN_VALUE, Long.MAX_VALUE, 9999999 },
			Arrays.toPrimitive(
				new Long[] { Long.valueOf(Long.MIN_VALUE), Long.valueOf(Long.MAX_VALUE), Long.valueOf(9999999) }, 1)));

		assertTrue(Arrays.equals(new long[] { Long.MIN_VALUE, Long.MAX_VALUE, 9999999 }, Arrays.toPrimitive(new Long[] {
				Long.valueOf(Long.MIN_VALUE), null, Long.valueOf(9999999) }, Long.MAX_VALUE)));
	}

	@Test
	public void testToObject_long() {
		final long[] b = null;
		assertArrayEquals(null, Arrays.toObject(b));

		assertSame(Arrays.EMPTY_LONG_OBJECT_ARRAY, Arrays.toObject(new long[0]));

		assertTrue(Arrays.equals(
			new Long[] { Long.valueOf(Long.MIN_VALUE), Long.valueOf(Long.MAX_VALUE), Long.valueOf(9999999) },
			Arrays.toObject(new long[] { Long.MIN_VALUE, Long.MAX_VALUE, 9999999 })));
	}

	// testToPrimitive/Object for float
	// -----------------------------------------------------------------------
	@Test
	public void testToPrimitive_float() {
		final Float[] b = null;
		assertEquals(null, Arrays.toPrimitive(b));

		assertSame(Arrays.EMPTY_FLOAT_ARRAY, Arrays.toPrimitive(new Float[0]));

		assertTrue(Arrays.equals(
			new float[] { Float.MIN_VALUE, Float.MAX_VALUE, 9999999 },
			Arrays.toPrimitive(new Float[] { Float.valueOf(Float.MIN_VALUE), Float.valueOf(Float.MAX_VALUE),
					Float.valueOf(9999999) })));

		try {
			Arrays.toPrimitive(new Float[] { Float.valueOf(Float.MIN_VALUE), null });
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	@Test
	public void testToPrimitive_float_float() {
		final Float[] l = null;
		assertEquals(null, Arrays.toPrimitive(l, Float.MIN_VALUE));

		assertSame(Arrays.EMPTY_FLOAT_ARRAY, Arrays.toPrimitive(new Float[0], 1));

		assertTrue(Arrays.equals(new float[] { Float.MIN_VALUE, Float.MAX_VALUE, 9999999 }, Arrays.toPrimitive(
			new Float[] { Float.valueOf(Float.MIN_VALUE), Float.valueOf(Float.MAX_VALUE), Float.valueOf(9999999) }, 1)));

		assertTrue(Arrays.equals(new float[] { Float.MIN_VALUE, Float.MAX_VALUE, 9999999 }, Arrays.toPrimitive(
			new Float[] { Float.valueOf(Float.MIN_VALUE), null, Float.valueOf(9999999) }, Float.MAX_VALUE)));
	}

	@Test
	public void testToObject_float() {
		final float[] b = null;
		assertArrayEquals(null, Arrays.toObject(b));

		assertSame(Arrays.EMPTY_FLOAT_OBJECT_ARRAY, Arrays.toObject(new float[0]));

		assertTrue(Arrays.equals(
			new Float[] { Float.valueOf(Float.MIN_VALUE), Float.valueOf(Float.MAX_VALUE), Float.valueOf(9999999) },
			Arrays.toObject(new float[] { Float.MIN_VALUE, Float.MAX_VALUE, 9999999 })));
	}

	// testToPrimitive/Object for double
	// -----------------------------------------------------------------------
	@Test
	public void testToPrimitive_double() {
		final Double[] b = null;
		assertEquals(null, Arrays.toPrimitive(b));

		assertSame(Arrays.EMPTY_DOUBLE_ARRAY, Arrays.toPrimitive(new Double[0]));

		assertTrue(Arrays.equals(
			new double[] { Double.MIN_VALUE, Double.MAX_VALUE, 9999999 },
			Arrays.toPrimitive(new Double[] { Double.valueOf(Double.MIN_VALUE), Double.valueOf(Double.MAX_VALUE),
					Double.valueOf(9999999) })));

		try {
			Arrays.toPrimitive(new Float[] { Float.valueOf(Float.MIN_VALUE), null });
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	@Test
	public void testToPrimitive_double_double() {
		final Double[] l = null;
		assertEquals(null, Arrays.toPrimitive(l, Double.MIN_VALUE));

		assertSame(Arrays.EMPTY_DOUBLE_ARRAY, Arrays.toPrimitive(new Double[0], 1));

		assertTrue(Arrays.equals(
			new double[] { Double.MIN_VALUE, Double.MAX_VALUE, 9999999 },
			Arrays.toPrimitive(new Double[] { Double.valueOf(Double.MIN_VALUE), Double.valueOf(Double.MAX_VALUE),
					Double.valueOf(9999999) }, 1)));

		assertTrue(Arrays.equals(new double[] { Double.MIN_VALUE, Double.MAX_VALUE, 9999999 }, Arrays.toPrimitive(
			new Double[] { Double.valueOf(Double.MIN_VALUE), null, Double.valueOf(9999999) }, Double.MAX_VALUE)));
	}

	@Test
	public void testToObject_double() {
		final double[] b = null;
		assertArrayEquals(null, Arrays.toObject(b));

		assertSame(Arrays.EMPTY_DOUBLE_OBJECT_ARRAY, Arrays.toObject(new double[0]));

		assertTrue(Arrays.equals(new Double[] { Double.valueOf(Double.MIN_VALUE), Double.valueOf(Double.MAX_VALUE),
				Double.valueOf(9999999) },
			Arrays.toObject(new double[] { Double.MIN_VALUE, Double.MAX_VALUE, 9999999 })));
	}

	// -----------------------------------------------------------------------
	/**
	 * Test for {@link Arrays#isEmpty(java.lang.Object[])}.
	 */
	@Test
	public void testIsEmptyObject() {
		final Object[] emptyArray = new Object[] {};
		final Object[] notEmptyArray = new Object[] { new String("Value") };
		assertTrue(Arrays.isEmpty((Object[])null));
		assertTrue(Arrays.isEmpty(emptyArray));
		assertFalse(Arrays.isEmpty(notEmptyArray));
	}

	/**
	 * Tests for {@link Arrays#isEmpty(long[])}, {@link Arrays#isEmpty(int[])},
	 * {@link Arrays#isEmpty(short[])}, {@link Arrays#isEmpty(char[])},
	 * {@link Arrays#isEmpty(byte[])}, {@link Arrays#isEmpty(double[])},
	 * {@link Arrays#isEmpty(float[])} and {@link Arrays#isEmpty(boolean[])}.
	 */
	@Test
	public void testIsEmptyPrimitives() {
		final long[] emptyLongArray = new long[] {};
		final long[] notEmptyLongArray = new long[] { 1L };
		assertTrue(Arrays.isEmpty((long[])null));
		assertTrue(Arrays.isEmpty(emptyLongArray));
		assertFalse(Arrays.isEmpty(notEmptyLongArray));

		final int[] emptyIntArray = new int[] {};
		final int[] notEmptyIntArray = new int[] { 1 };
		assertTrue(Arrays.isEmpty((int[])null));
		assertTrue(Arrays.isEmpty(emptyIntArray));
		assertFalse(Arrays.isEmpty(notEmptyIntArray));

		final short[] emptyShortArray = new short[] {};
		final short[] notEmptyShortArray = new short[] { 1 };
		assertTrue(Arrays.isEmpty((short[])null));
		assertTrue(Arrays.isEmpty(emptyShortArray));
		assertFalse(Arrays.isEmpty(notEmptyShortArray));

		final char[] emptyCharArray = new char[] {};
		final char[] notEmptyCharArray = new char[] { 1 };
		assertTrue(Arrays.isEmpty((char[])null));
		assertTrue(Arrays.isEmpty(emptyCharArray));
		assertFalse(Arrays.isEmpty(notEmptyCharArray));

		final byte[] emptyByteArray = new byte[] {};
		final byte[] notEmptyByteArray = new byte[] { 1 };
		assertTrue(Arrays.isEmpty((byte[])null));
		assertTrue(Arrays.isEmpty(emptyByteArray));
		assertFalse(Arrays.isEmpty(notEmptyByteArray));

		final double[] emptyDoubleArray = new double[] {};
		final double[] notEmptyDoubleArray = new double[] { 1.0 };
		assertTrue(Arrays.isEmpty((double[])null));
		assertTrue(Arrays.isEmpty(emptyDoubleArray));
		assertFalse(Arrays.isEmpty(notEmptyDoubleArray));

		final float[] emptyFloatArray = new float[] {};
		final float[] notEmptyFloatArray = new float[] { 1.0F };
		assertTrue(Arrays.isEmpty((float[])null));
		assertTrue(Arrays.isEmpty(emptyFloatArray));
		assertFalse(Arrays.isEmpty(notEmptyFloatArray));

		final boolean[] emptyBooleanArray = new boolean[] {};
		final boolean[] notEmptyBooleanArray = new boolean[] { true };
		assertTrue(Arrays.isEmpty((boolean[])null));
		assertTrue(Arrays.isEmpty(emptyBooleanArray));
		assertFalse(Arrays.isEmpty(notEmptyBooleanArray));
	}

	/**
	 * Test for {@link Arrays#isNotEmpty(java.lang.Object[])}.
	 */
	@Test
	public void testIsNotEmptyObject() {
		final Object[] emptyArray = new Object[] {};
		final Object[] notEmptyArray = new Object[] { new String("Value") };
		assertFalse(Arrays.isNotEmpty((Object[])null));
		assertFalse(Arrays.isNotEmpty(emptyArray));
		assertTrue(Arrays.isNotEmpty(notEmptyArray));
	}

	/**
	 * Tests for {@link Arrays#isNotEmpty(long[])}, {@link Arrays#isNotEmpty(int[])},
	 * {@link Arrays#isNotEmpty(short[])}, {@link Arrays#isNotEmpty(char[])},
	 * {@link Arrays#isNotEmpty(byte[])}, {@link Arrays#isNotEmpty(double[])},
	 * {@link Arrays#isNotEmpty(float[])} and {@link Arrays#isNotEmpty(boolean[])}.
	 */
	@Test
	public void testIsNotEmptyPrimitives() {
		final long[] emptyLongArray = new long[] {};
		final long[] notEmptyLongArray = new long[] { 1L };
		assertFalse(Arrays.isNotEmpty((long[])null));
		assertFalse(Arrays.isNotEmpty(emptyLongArray));
		assertTrue(Arrays.isNotEmpty(notEmptyLongArray));

		final int[] emptyIntArray = new int[] {};
		final int[] notEmptyIntArray = new int[] { 1 };
		assertFalse(Arrays.isNotEmpty((int[])null));
		assertFalse(Arrays.isNotEmpty(emptyIntArray));
		assertTrue(Arrays.isNotEmpty(notEmptyIntArray));

		final short[] emptyShortArray = new short[] {};
		final short[] notEmptyShortArray = new short[] { 1 };
		assertFalse(Arrays.isNotEmpty((short[])null));
		assertFalse(Arrays.isNotEmpty(emptyShortArray));
		assertTrue(Arrays.isNotEmpty(notEmptyShortArray));

		final char[] emptyCharArray = new char[] {};
		final char[] notEmptyCharArray = new char[] { 1 };
		assertFalse(Arrays.isNotEmpty((char[])null));
		assertFalse(Arrays.isNotEmpty(emptyCharArray));
		assertTrue(Arrays.isNotEmpty(notEmptyCharArray));

		final byte[] emptyByteArray = new byte[] {};
		final byte[] notEmptyByteArray = new byte[] { 1 };
		assertFalse(Arrays.isNotEmpty((byte[])null));
		assertFalse(Arrays.isNotEmpty(emptyByteArray));
		assertTrue(Arrays.isNotEmpty(notEmptyByteArray));

		final double[] emptyDoubleArray = new double[] {};
		final double[] notEmptyDoubleArray = new double[] { 1.0 };
		assertFalse(Arrays.isNotEmpty((double[])null));
		assertFalse(Arrays.isNotEmpty(emptyDoubleArray));
		assertTrue(Arrays.isNotEmpty(notEmptyDoubleArray));

		final float[] emptyFloatArray = new float[] {};
		final float[] notEmptyFloatArray = new float[] { 1.0F };
		assertFalse(Arrays.isNotEmpty((float[])null));
		assertFalse(Arrays.isNotEmpty(emptyFloatArray));
		assertTrue(Arrays.isNotEmpty(notEmptyFloatArray));

		final boolean[] emptyBooleanArray = new boolean[] {};
		final boolean[] notEmptyBooleanArray = new boolean[] { true };
		assertFalse(Arrays.isNotEmpty((boolean[])null));
		assertFalse(Arrays.isNotEmpty(emptyBooleanArray));
		assertTrue(Arrays.isNotEmpty(notEmptyBooleanArray));
	}

	// ------------------------------------------------------------------------
	@Test
	public void testGetLength() {
		assertEquals(0, Arrays.getLength(null));

		final Object[] emptyObjectArray = new Object[0];
		final Object[] notEmptyObjectArray = new Object[] { "aValue" };
		assertEquals(0, Arrays.getLength((Object[])null));
		assertEquals(0, Arrays.getLength(emptyObjectArray));
		assertEquals(1, Arrays.getLength(notEmptyObjectArray));

		final int[] emptyIntArray = new int[] {};
		final int[] notEmptyIntArray = new int[] { 1 };
		assertEquals(0, Arrays.getLength((int[])null));
		assertEquals(0, Arrays.getLength(emptyIntArray));
		assertEquals(1, Arrays.getLength(notEmptyIntArray));

		final short[] emptyShortArray = new short[] {};
		final short[] notEmptyShortArray = new short[] { 1 };
		assertEquals(0, Arrays.getLength((short[])null));
		assertEquals(0, Arrays.getLength(emptyShortArray));
		assertEquals(1, Arrays.getLength(notEmptyShortArray));

		final char[] emptyCharArray = new char[] {};
		final char[] notEmptyCharArray = new char[] { 1 };
		assertEquals(0, Arrays.getLength((char[])null));
		assertEquals(0, Arrays.getLength(emptyCharArray));
		assertEquals(1, Arrays.getLength(notEmptyCharArray));

		final byte[] emptyByteArray = new byte[] {};
		final byte[] notEmptyByteArray = new byte[] { 1 };
		assertEquals(0, Arrays.getLength((byte[])null));
		assertEquals(0, Arrays.getLength(emptyByteArray));
		assertEquals(1, Arrays.getLength(notEmptyByteArray));

		final double[] emptyDoubleArray = new double[] {};
		final double[] notEmptyDoubleArray = new double[] { 1.0 };
		assertEquals(0, Arrays.getLength((double[])null));
		assertEquals(0, Arrays.getLength(emptyDoubleArray));
		assertEquals(1, Arrays.getLength(notEmptyDoubleArray));

		final float[] emptyFloatArray = new float[] {};
		final float[] notEmptyFloatArray = new float[] { 1.0F };
		assertEquals(0, Arrays.getLength((float[])null));
		assertEquals(0, Arrays.getLength(emptyFloatArray));
		assertEquals(1, Arrays.getLength(notEmptyFloatArray));

		final boolean[] emptyBooleanArray = new boolean[] {};
		final boolean[] notEmptyBooleanArray = new boolean[] { true };
		assertEquals(0, Arrays.getLength((boolean[])null));
		assertEquals(0, Arrays.getLength(emptyBooleanArray));
		assertEquals(1, Arrays.getLength(notEmptyBooleanArray));

		try {
			Arrays.getLength("notAnArray");
			fail("IllegalArgumentException should have been thrown");
		}
		catch (final IllegalArgumentException e) {
		}
	}

	// ------------------------------------------------------------------------
	@Test
	public void testToList() {
		List<String> l = new ArrayList<String>();
		l.add("1");
		l.add("2");
		
		assertEquals(l, Arrays.toList("1", "2"));
		assertEquals(l, Arrays.toList(new String[] { "1", "2" }));
	}

	// ------------------------------------------------------------------------
	@Test
	public void testToSet() {
		Set<String> l = new HashSet<String>();
		l.add("1");
		l.add("2");
		l.add("2");
		
		assertEquals(l, Arrays.toSet("1", "2", "2"));
		assertEquals(l, Arrays.toSet(new String[] { "1", "2", "2" }));
	}
}
