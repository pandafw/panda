package panda.lang.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import panda.lang.builder.ToStringStyleTest.Person;

/**
 * Unit tests {@link ToStringStyle#SHORT_PREFIX_STYLE}.
 * 
 */
public class ShortPrefixToStringStyleTest {

	private final Integer base = Integer.valueOf(5);
	private final String baseStr = "Integer";

	@Before
	public void setUp() throws Exception {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@After
	public void tearDown() throws Exception {
		ToStringBuilder.setDefaultStyle(ToStringStyle.DEFAULT_STYLE);
	}

	// ----------------------------------------------------------------

	@Test
	public void testBlank() {
		assertEquals(baseStr + "{}", new ToStringBuilder(base).toString());
	}

	@Test
	public void testAppendSuper() {
		assertEquals(baseStr + "{}", new ToStringBuilder(base).appendSuper("Integer@8888{}")
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
	public void testPerson() {
		final Person p = new Person();
		p.name = "John Q. Public";
		p.age = 45;
		p.smoker = true;
		final String pBaseStr = "ToStringStyleTest.Person";
		assertEquals(pBaseStr + "{name=John Q. Public,age=45,smoker=true}", new ToStringBuilder(p)
			.append("name", p.name).append("age", p.age).append("smoker", p.smoker).toString());
	}

	@Test
	public void testLong() {
		assertEquals(baseStr + "{3}", new ToStringBuilder(base).append(3L).toString());
		assertEquals(baseStr + "{a=3}", new ToStringBuilder(base).append("a", 3L).toString());
		assertEquals(baseStr + "{a=3,b=4}",
			new ToStringBuilder(base).append("a", 3L).append("b", 4L).toString());
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

}
