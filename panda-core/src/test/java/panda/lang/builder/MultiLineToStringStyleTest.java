package panda.lang.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import panda.lang.Systems;
import panda.lang.builder.ToStringStyleTest.Person;

/**
 * Unit tests {@link MultiLineToStringStyleTest}.
 *
 */
public class MultiLineToStringStyleTest {

	private final Integer base = Integer.valueOf(5);
	private final String baseStr = base.getClass().getName() + "@"
			+ Integer.toHexString(System.identityHashCode(base));

	@Before
	public void setUp() throws Exception {
		ToStringBuilder.setDefaultStyle(ToStringStyle.MULTI_LINE_STYLE);
	}

	@After
	public void tearDown() throws Exception {
		ToStringBuilder.setDefaultStyle(ToStringStyle.DEFAULT_STYLE);
	}

	// ----------------------------------------------------------------

	private static final String contentStart = "{";
	private static final String contentEnd = "}";
	@Test
	public void testBlank() {
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + contentEnd,
			new ToStringBuilder(base).toString());
	}

	@Test
	public void testAppendSuper() {
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base)
			.appendSuper("Integer@8888{" + Systems.LINE_SEPARATOR + contentEnd).toString());
		assertEquals(
			baseStr + contentStart + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR + contentEnd,
			new ToStringBuilder(base).appendSuper(
				"Integer@8888{" + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
						+ contentEnd).toString());

		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=hello" + Systems.LINE_SEPARATOR
				+ contentEnd,
			new ToStringBuilder(base).appendSuper("Integer@8888{" + Systems.LINE_SEPARATOR + contentEnd)
				.append("a", "hello").toString());
		assertEquals(
			baseStr + contentStart + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
					+ "  a=hello" + Systems.LINE_SEPARATOR + contentEnd,
			new ToStringBuilder(base)
				.appendSuper(
					"Integer@8888{" + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
							+ contentEnd).append("a", "hello").toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=hello" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).appendSuper(null).append("a", "hello").toString());
	}

	@Test
	public void testObject() {
		final Integer i3 = Integer.valueOf(3);
		final Integer i4 = Integer.valueOf(4);
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append((Object)null).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  3" + Systems.LINE_SEPARATOR + contentEnd,
			new ToStringBuilder(base).append(i3).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=<null>" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append("a", (Object)null).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=3" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append("a", i3).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=3" + Systems.LINE_SEPARATOR
				+ "  b=4" + Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base).append("a", i3)
			.append("b", i4).toString());
//		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=<Integer>"
//				+ Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base).append("a", i3, false)
//			.toString());
//		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=<size=0>"
//				+ Systems.LINE_SEPARATOR + contentEnd,
//			new ToStringBuilder(base).append("a", new ArrayList<Object>(), false).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=[]" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append("a", new ArrayList<Object>(), true)
			.toString());
//		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=<size=0>"
//				+ Systems.LINE_SEPARATOR + contentEnd,
//			new ToStringBuilder(base).append("a", new HashMap<Object, Object>(), false).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a={}" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append("a", new HashMap<Object, Object>(), true)
			.toString());
//		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=<size=0>"
//				+ Systems.LINE_SEPARATOR + contentEnd,
//			new ToStringBuilder(base).append("a", (Object)new String[0], false).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=[]" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append("a", (Object)new String[0], true)
			.toString());
	}

	@Test
	public void testPerson() {
		final Person p = new Person();
		p.name = "Jane Doe";
		p.age = 25;
		p.smoker = true;
		final String pBaseStr = p.getClass().getName() + "@"
				+ Integer.toHexString(System.identityHashCode(p));
		assertEquals(pBaseStr + contentStart + Systems.LINE_SEPARATOR + "  name=Jane Doe"
				+ Systems.LINE_SEPARATOR + "  age=25" + Systems.LINE_SEPARATOR + "  smoker=true"
				+ Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(p).append("name", p.name)
			.append("age", p.age).append("smoker", p.smoker).toString());
	}

	@Test
	public void testLong() {
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  3" + Systems.LINE_SEPARATOR + contentEnd,
			new ToStringBuilder(base).append(3L).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=3" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append("a", 3L).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  a=3" + Systems.LINE_SEPARATOR
				+ "  b=4" + Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base).append("a", 3L)
			.append("b", 4L).toString());
	}

	@Test
	public void testObjectArray() {
		Object[] array = new Object[] { null, base, new int[] { 3, 6 } };
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  [<null>,5,[3,6]]"
				+ Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  [<null>,5,[3,6]]"
				+ Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base).append((Object)array)
			.toString());
		array = null;
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append((Object)array).toString());
	}

	@Test
	public void testLongArray() {
		long[] array = new long[] { 1, 2, -3, 4 };
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  [1,2,-3,4]"
				+ Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  [1,2,-3,4]"
				+ Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base).append((Object)array)
			.toString());
		array = null;
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append((Object)array).toString());
	}

	@Test
	public void testLongArrayArray() {
		long[][] array = new long[][] { { 1, 2 }, null, { 5 } };
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  [[1,2],<null>,[5]]"
				+ Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  [[1,2],<null>,[5]]"
				+ Systems.LINE_SEPARATOR + contentEnd, new ToStringBuilder(base).append((Object)array)
			.toString());
		array = null;
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append(array).toString());
		assertEquals(baseStr + contentStart + Systems.LINE_SEPARATOR + "  <null>" + Systems.LINE_SEPARATOR
				+ contentEnd, new ToStringBuilder(base).append((Object)array).toString());
	}

}
