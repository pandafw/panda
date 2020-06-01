package panda.lang.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

/**
 * Unit tests for {@ToStringBuilder}.
 *
 */
public class ToStringBuilderTest {

	private final Integer base = Integer.valueOf(5);
	private final String baseStr = base.getClass().getName() + "@"
			+ Integer.toHexString(System.identityHashCode(base));

	/*
	 * All tests should leave the registry empty.
	 */
	@After
	public void after() {
		validateNullToStringStyleRegistry();
	}

	// -----------------------------------------------------------------------

	@Test
	public void testConstructorEx0() {
		assertEquals("", new ToStringBuilder(null).toString());
	}

	@Test
	public void testConstructorEx1() {
		assertEquals("", new ToStringBuilder(null).toString());
	}

	@Test
	public void testConstructorEx2() {
		assertEquals("", new ToStringBuilder(null, null).toString());
		new ToStringBuilder(this.base, null).toString();
	}

	@Test
	public void testConstructorEx3() {
		assertEquals("", new ToStringBuilder(null, null, null).toString());
		new ToStringBuilder(this.base, null, null).toString();
		new ToStringBuilder(this.base, ToStringStyle.DEFAULT_STYLE, null).toString();
	}

	@Test
	public void testGetSetDefault() {
		try {
			ToStringBuilder.setDefaultStyle(ToStringStyle.NO_FIELD_NAMES_STYLE);
			assertSame(ToStringStyle.NO_FIELD_NAMES_STYLE, ToStringBuilder.getDefaultStyle());
		}
		finally {
			// reset for other tests
			ToStringBuilder.setDefaultStyle(ToStringStyle.DEFAULT_STYLE);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetDefaultEx() {
		ToStringBuilder.setDefaultStyle(null);
	}

	@Test
	public void testBlank() {
		assertEquals(baseStr + "{}", new ToStringBuilder(base).toString());
	}

	/**
	 * Create the same toString() as Object.toString().
	 * 
	 * @param o the object to create the string for.
	 * @return a String in the Object.toString format.
	 */
	private String toBaseString(final Object o) {
		return o.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(o));
	}

	static class ReflectionTestFixtureA {
		@SuppressWarnings("unused")
		private final char a = 'a';
		@SuppressWarnings("unused")
		private transient char transientA = 't';
	}

	static class ReflectionTestFixtureB extends ReflectionTestFixtureA {
		@SuppressWarnings("unused")
		private final char b = 'b';
		@SuppressWarnings("unused")
		private transient char transientB = 't';
	}

	void validateNullToStringStyleRegistry() {
		final Map<Object, Object> registry = ToStringStyle.getRegistry();
		assertNull("Expected null, actual: " + registry, registry);
	}

	// End: Reflection cycle tests

	@Test
	public void testAppendSuper() {
		assertEquals(baseStr + "{}", new ToStringBuilder(base).appendSuper("Integer@8888[]")
			.toString());
		assertEquals(baseStr + "{<null>}",
			new ToStringBuilder(base).appendSuper("Integer@8888{<null>}").toString());

		assertEquals(baseStr + "{a=hello}", new ToStringBuilder(base).appendSuper("Integer@8888{}")
			.append("a", "hello").toString());
		assertEquals(baseStr + "{<null>,a=hello}",
			new ToStringBuilder(base).appendSuper("Integer@8888{<null>}").append("a", "hello")
				.toString());
		assertEquals(baseStr + "{a=hello}",
			new ToStringBuilder(base).appendSuper(null).append("a", "hello").toString());
	}

	@Test
	public void testAppendToString() {
		assertEquals(baseStr + "{}", new ToStringBuilder(base).appendToString("Integer@8888[]")
			.toString());
		assertEquals(baseStr + "{<null>}",
			new ToStringBuilder(base).appendToString("Integer@8888{<null>}").toString());

		assertEquals(baseStr + "{a=hello}",
			new ToStringBuilder(base).appendToString("Integer@8888{}").append("a", "hello")
				.toString());
		assertEquals(baseStr + "{<null>,a=hello}",
			new ToStringBuilder(base).appendToString("Integer@8888{<null>}").append("a", "hello")
				.toString());
		assertEquals(baseStr + "{a=hello}",
			new ToStringBuilder(base).appendToString(null).append("a", "hello").toString());
	}

	@Test
	public void testObject() {
		final Integer i3 = Integer.valueOf(3);
		final Integer i4 = Integer.valueOf(4);
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)null)
			.toString());
		assertEquals(baseStr + "{3}", new ToStringBuilder(base).append(i3).toString());
		assertEquals(baseStr + "{a=<null>}", new ToStringBuilder(base).append("a", (Object)null)
			.toString());
		assertEquals(baseStr + "{a=3}", new ToStringBuilder(base).append("a", i3).toString());
		assertEquals(baseStr + "{a=3,b=4}",
			new ToStringBuilder(base).append("a", i3).append("b", i4).toString());
//		assertEquals(baseStr + "{a=<Integer>}", new ToStringBuilder(base).append("a", i3, false)
//			.toString());
//		assertEquals(baseStr + "{a=<size=0>}",
//			new ToStringBuilder(base).append("a", new ArrayList<Object>(), false).toString());
		assertEquals(baseStr + "{a=[]}",
			new ToStringBuilder(base).append("a", new ArrayList<Object>(), true).toString());
//		assertEquals(baseStr + "{a=<size=0>}",
//			new ToStringBuilder(base).append("a", new HashMap<Object, Object>(), false).toString());
		assertEquals(baseStr + "{a={}}",
			new ToStringBuilder(base).append("a", new HashMap<Object, Object>(), true).toString());
//		assertEquals(baseStr + "{a=<size=0>}",
//			new ToStringBuilder(base).append("a", (Object)new String[0], false).toString());
		assertEquals(baseStr + "{a=[]}",
			new ToStringBuilder(base).append("a", (Object)new String[0], true).toString());
	}

	@Test
	public void testObjectBuild() {
		final Integer i3 = Integer.valueOf(3);
		final Integer i4 = Integer.valueOf(4);
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)null).build());
		assertEquals(baseStr + "{3}", new ToStringBuilder(base).append(i3).build());
		assertEquals(baseStr + "{a=<null>}", new ToStringBuilder(base).append("a", (Object)null)
			.build());
		assertEquals(baseStr + "{a=3}", new ToStringBuilder(base).append("a", i3).build());
		assertEquals(baseStr + "{a=3,b=4}",
			new ToStringBuilder(base).append("a", i3).append("b", i4).build());
//		assertEquals(baseStr + "{a=<Integer>}", new ToStringBuilder(base).append("a", i3, false)
//			.build());
//		assertEquals(baseStr + "{a=<size=0>}",
//			new ToStringBuilder(base).append("a", new ArrayList<Object>(), false).build());
		assertEquals(baseStr + "{a=[]}",
			new ToStringBuilder(base).append("a", new ArrayList<Object>(), true).build());
//		assertEquals(baseStr + "{a=<size=0>}",
//			new ToStringBuilder(base).append("a", new HashMap<Object, Object>(), false).build());
		assertEquals(baseStr + "{a={}}",
			new ToStringBuilder(base).append("a", new HashMap<Object, Object>(), true).build());
//		assertEquals(baseStr + "{a=<size=0>}",
//			new ToStringBuilder(base).append("a", (Object)new String[0], false).build());
		assertEquals(baseStr + "{a=[]}",
			new ToStringBuilder(base).append("a", (Object)new String[0], true).build());
	}

	@Test
	public void testLong() {
		assertEquals(baseStr + "{3}", new ToStringBuilder(base).append(3L).toString());
		assertEquals(baseStr + "{a=3}", new ToStringBuilder(base).append("a", 3L).toString());
		assertEquals(baseStr + "{a=3,b=4}",
			new ToStringBuilder(base).append("a", 3L).append("b", 4L).toString());
	}

	@SuppressWarnings("cast")
	// cast is not really needed, keep for consistency
	@Test
	public void testInt() {
		assertEquals(baseStr + "{3}", new ToStringBuilder(base).append((int)3).toString());
		assertEquals(baseStr + "{a=3}", new ToStringBuilder(base).append("a", (int)3).toString());
		assertEquals(baseStr + "{a=3,b=4}",
			new ToStringBuilder(base).append("a", (int)3).append("b", (int)4).toString());
	}

	@Test
	public void testShort() {
		assertEquals(baseStr + "{3}", new ToStringBuilder(base).append((short)3).toString());
		assertEquals(baseStr + "{a=3}", new ToStringBuilder(base).append("a", (short)3).toString());
		assertEquals(baseStr + "{a=3,b=4}",
			new ToStringBuilder(base).append("a", (short)3).append("b", (short)4).toString());
	}

	@Test
	public void testChar() {
		assertEquals(baseStr + "{A}", new ToStringBuilder(base).append((char)65).toString());
		assertEquals(baseStr + "{a=A}", new ToStringBuilder(base).append("a", (char)65).toString());
		assertEquals(baseStr + "{a=A,b=B}",
			new ToStringBuilder(base).append("a", (char)65).append("b", (char)66).toString());
	}

	@Test
	public void testByte() {
		assertEquals(baseStr + "{3}", new ToStringBuilder(base).append((byte)3).toString());
		assertEquals(baseStr + "{a=3}", new ToStringBuilder(base).append("a", (byte)3).toString());
		assertEquals(baseStr + "{a=3,b=4}",
			new ToStringBuilder(base).append("a", (byte)3).append("b", (byte)4).toString());
	}

	@SuppressWarnings("cast")
	@Test
	public void testDouble() {
		assertEquals(baseStr + "{3.2}", new ToStringBuilder(base).append((double)3.2).toString());
		assertEquals(baseStr + "{a=3.2}", new ToStringBuilder(base).append("a", (double)3.2)
			.toString());
		assertEquals(baseStr + "{a=3.2,b=4.3}", new ToStringBuilder(base).append("a", (double)3.2)
			.append("b", (double)4.3).toString());
	}

	@Test
	public void testFloat() {
		assertEquals(baseStr + "{3.2}", new ToStringBuilder(base).append((float)3.2).toString());
		assertEquals(baseStr + "{a=3.2}", new ToStringBuilder(base).append("a", (float)3.2)
			.toString());
		assertEquals(baseStr + "{a=3.2,b=4.3}", new ToStringBuilder(base).append("a", (float)3.2)
			.append("b", (float)4.3).toString());
	}

	@Test
	public void testBoolean() {
		assertEquals(baseStr + "{true}", new ToStringBuilder(base).append(true).toString());
		assertEquals(baseStr + "{a=true}", new ToStringBuilder(base).append("a", true).toString());
		assertEquals(baseStr + "{a=true,b=false}", new ToStringBuilder(base).append("a", true)
			.append("b", false).toString());
	}

	@Test
	public void testObjectArray() {
		Object[] array = new Object[] { null, base, new int[] { 3, 6 } };
		assertEquals(baseStr + "{[<null>,5,[3,6]]}", new ToStringBuilder(base).append(array)
			.toString());
		assertEquals(baseStr + "{[<null>,5,[3,6]]}", new ToStringBuilder(base)
			.append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testLongArray() {
		long[] array = new long[] { 1, 2, -3, 4 };
		assertEquals(baseStr + "{[1,2,-3,4]}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[1,2,-3,4]}", new ToStringBuilder(base).append((Object)array)
			.toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testIntArray() {
		int[] array = new int[] { 1, 2, -3, 4 };
		assertEquals(baseStr + "{[1,2,-3,4]}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[1,2,-3,4]}", new ToStringBuilder(base).append((Object)array)
			.toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testShortArray() {
		short[] array = new short[] { 1, 2, -3, 4 };
		assertEquals(baseStr + "{[1,2,-3,4]}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[1,2,-3,4]}", new ToStringBuilder(base).append((Object)array)
			.toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testByteArray() {
		byte[] array = new byte[] { 1, 2, -3, 4 };
		assertEquals(baseStr + "{[1,2,-3,4]}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[1,2,-3,4]}", new ToStringBuilder(base).append((Object)array)
			.toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testCharArray() {
		char[] array = new char[] { 'A', '2', '_', 'D' };
		assertEquals(baseStr + "{[A,2,_,D]}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[A,2,_,D]}", new ToStringBuilder(base).append((Object)array)
			.toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testDoubleArray() {
		double[] array = new double[] { 1.0, 2.9876, -3.00001, 4.3 };
		assertEquals(baseStr + "{[1.0,2.9876,-3.00001,4.3]}",
			new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[1.0,2.9876,-3.00001,4.3]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testFloatArray() {
		float[] array = new float[] { 1.0f, 2.9876f, -3.00001f, 4.3f };
		assertEquals(baseStr + "{[1.0,2.9876,-3.00001,4.3]}",
			new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[1.0,2.9876,-3.00001,4.3]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testBooleanArray() {
		boolean[] array = new boolean[] { true, false, false };
		assertEquals(baseStr + "{[true,false,false]}", new ToStringBuilder(base).append(array)
			.toString());
		assertEquals(baseStr + "{[true,false,false]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testLongArrayArray() {
		long[][] array = new long[][] { { 1, 2 }, null, { 5 } };
		assertEquals(baseStr + "{[[1,2],<null>,[5]]}", new ToStringBuilder(base).append(array)
			.toString());
		assertEquals(baseStr + "{[[1,2],<null>,[5]]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testIntArrayArray() {
		int[][] array = new int[][] { { 1, 2 }, null, { 5 } };
		assertEquals(baseStr + "{[[1,2],<null>,[5]]}", new ToStringBuilder(base).append(array)
			.toString());
		assertEquals(baseStr + "{[[1,2],<null>,[5]]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testShortArrayArray() {
		short[][] array = new short[][] { { 1, 2 }, null, { 5 } };
		assertEquals(baseStr + "{[[1,2],<null>,[5]]}", new ToStringBuilder(base).append(array)
			.toString());
		assertEquals(baseStr + "{[[1,2],<null>,[5]]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testByteArrayArray() {
		byte[][] array = new byte[][] { { 1, 2 }, null, { 5 } };
		assertEquals(baseStr + "{[[1,2],<null>,[5]]}", new ToStringBuilder(base).append(array)
			.toString());
		assertEquals(baseStr + "{[[1,2],<null>,[5]]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testCharArrayArray() {
		char[][] array = new char[][] { { 'A', 'B' }, null, { 'p' } };
		assertEquals(baseStr + "{[[A,B],<null>,[p]]}", new ToStringBuilder(base).append(array)
			.toString());
		assertEquals(baseStr + "{[[A,B],<null>,[p]]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testDoubleArrayArray() {
		double[][] array = new double[][] { { 1.0, 2.29686 }, null, { Double.NaN } };
		assertEquals(baseStr + "{[[1.0,2.29686],<null>,[NaN]]}",
			new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[[1.0,2.29686],<null>,[NaN]]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testFloatArrayArray() {
		float[][] array = new float[][] { { 1.0f, 2.29686f }, null, { Float.NaN } };
		assertEquals(baseStr + "{[[1.0,2.29686],<null>,[NaN]]}",
			new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[[1.0,2.29686],<null>,[NaN]]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testBooleanArrayArray() {
		boolean[][] array = new boolean[][] { { true, false }, null, { false } };
		assertEquals(baseStr + "{[[true,false],<null>,[false]]}",
			new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{[[true,false],<null>,[false]]}",
			new ToStringBuilder(base).append((Object)array).toString());
		array = null;
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + "{<null>}", new ToStringBuilder(base).append((Object)array)
			.toString());
	}

	@Test
	public void testObjectCycle() {
		final ObjectCycle a = new ObjectCycle();
		final ObjectCycle b = new ObjectCycle();
		a.obj = b;
		b.obj = a;

		final String expected = toBaseString(a) + "{{" + toBaseString(b) + "{" + toBaseString(a)
				+ "}}}";
		assertEquals(expected, a.toString());
	}

	static class ObjectCycle {
		Object obj;

		@Override
		public String toString() {
			return new ToStringBuilder(this).append(obj).toString();
		}
	}

	/**
	 * Tests ReflectionToStringBuilder.toString() for statics.
	 */
	class ReflectionStaticFieldsFixture {
		static final String staticString = "staticString";
		static final int staticInt = 12345;
		static final transient String staticTransientString = "staticTransientString";
		static final transient int staticTransientInt = 54321;
		String instanceString = "instanceString";
		int instanceInt = 67890;
		transient String transientString = "transientString";
		transient int transientInt = 98765;
	}

	/**
	 * Test fixture for ReflectionToStringBuilder.toString() for statics.
	 */
	class SimpleReflectionStaticFieldsFixture {
		static final String staticString = "staticString";
		static final int staticInt = 12345;
	}

	/**
	 * Test fixture for ReflectionToStringBuilder.toString() for statics.
	 */
	class InheritedReflectionStaticFieldsFixture extends SimpleReflectionStaticFieldsFixture {
		static final String staticString2 = "staticString2";
		static final int staticInt2 = 67890;
	}

	/**
	 * Points out failure to print anything from appendToString methods using MULTI_LINE_STYLE. See
	 * issue LANG-372.
	 */
	class MultiLineTestObject {
		Integer i = Integer.valueOf(31337);

		@Override
		public String toString() {
			return new ToStringBuilder(this).append("testInt", i).toString();
		}
	}

	@Test
	public void testAppendToStringUsingMultiLineStyle() {
		final MultiLineTestObject obj = new MultiLineTestObject();
		final ToStringBuilder testBuilder = new ToStringBuilder(this,
			ToStringStyle.MULTI_LINE_STYLE).appendToString(obj.toString());
		assertEquals(testBuilder.toString().indexOf("testInt=31337"), -1);
	}

}
