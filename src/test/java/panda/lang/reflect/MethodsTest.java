package panda.lang.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.Numbers;
import panda.lang.mutable.Mutable;
import panda.lang.mutable.MutableObject;
import panda.lang.reflect.Methods;

/**
 * Unit tests Methods
 */
public class MethodsTest {

	private static interface PrivateInterface {
	}

	static class TestBeanWithInterfaces implements PrivateInterface {
		public String foo() {
			return "foo()";
		}
	}

	public static class TestBean {

		public static String bar() {
			return "bar()";
		}

		public static String bar(final int i) {
			return "bar(int)";
		}

		public static String bar(final Integer i) {
			return "bar(Integer)";
		}

		public static String bar(final double d) {
			return "bar(double)";
		}

		public static String bar(final String s) {
			return "bar(String)";
		}

		public static String bar(final Object o) {
			return "bar(Object)";
		}

		public static void oneParameterStatic(final String s) {
			// empty
		}

		@SuppressWarnings("unused")
		private void privateStuff() {
		}

		public String foo() {
			return "foo()";
		}

		public String foo(final int i) {
			return "foo(int)";
		}

		public String foo(final Integer i) {
			return "foo(Integer)";
		}

		public String foo(final double d) {
			return "foo(double)";
		}

		public String foo(final String s) {
			return "foo(String)";
		}

		public String foo(final Object o) {
			return "foo(Object)";
		}

		public void oneParameter(final String s) {
			// empty
		}
	}

	private static class TestMutable implements Mutable<Object> {
		@Override
		public Object getValue() {
			return null;
		}

		@Override
		public void setValue(final Object value) {
		}
	}

	private TestBean testBean;
	private final Map<Class<?>, Class<?>[]> classCache = new HashMap<Class<?>, Class<?>[]>();

	@Before
	public void setUp() throws Exception {
		testBean = new TestBean();
		classCache.clear();
	}

	@Test
	public void testGetDeclaredMethods() throws Exception {
		Methods.getDeclaredMethodsWithoutTop(Map.class);
	}
	
	@Test
	public void testInvokeMethod() throws Exception {
		assertEquals("foo()", Methods.invokeMethod(testBean, "foo", (Object[])Arrays.EMPTY_CLASS_ARRAY));
		assertEquals("foo()", Methods.invokeMethod(testBean, "foo", (Object[])null));
		assertEquals("foo()", Methods.invokeMethod(testBean, "foo", (Object[])null, (Class<?>[])null));
		assertEquals("foo(String)", Methods.invokeMethod(testBean, "foo", ""));
		assertEquals("foo(Object)", Methods.invokeMethod(testBean, "foo", new Object()));
		assertEquals("foo(Object)", Methods.invokeMethod(testBean, "foo", Boolean.TRUE));
		assertEquals("foo(Integer)", Methods.invokeMethod(testBean, "foo", Numbers.INTEGER_ONE));
		assertEquals("foo(int)", Methods.invokeMethod(testBean, "foo", Numbers.BYTE_ONE));
		assertEquals("foo(double)", Methods.invokeMethod(testBean, "foo", Numbers.LONG_ONE));
		assertEquals("foo(double)", Methods.invokeMethod(testBean, "foo", Numbers.DOUBLE_ONE));
	}

	@Test
	public void testInvokeExactMethod() throws Exception {
		testBean.getClass().getMethod("foo", new Class[] { Double.TYPE });
		
		assertEquals("foo()", Methods.invokeExactMethod(testBean, "foo", (Object[])Arrays.EMPTY_CLASS_ARRAY));
		assertEquals("foo()", Methods.invokeExactMethod(testBean, "foo", (Object[])null));
		assertEquals("foo()", Methods.invokeExactMethod(testBean, "foo", (Object[])null, (Class<?>[])null));
		assertEquals("foo(String)", Methods.invokeExactMethod(testBean, "foo", ""));
		assertEquals("foo(Object)", Methods.invokeExactMethod(testBean, "foo", new Object()));
		assertEquals("foo(Integer)", Methods.invokeExactMethod(testBean, "foo", Numbers.INTEGER_ONE));
		assertEquals("foo(double)", Methods.invokeExactMethod(testBean, "foo",
			new Object[] { Numbers.DOUBLE_ONE }, new Class[] { Double.TYPE }));

		try {
			Methods.invokeExactMethod(testBean, "foo", Numbers.BYTE_ONE);
			fail("should throw NoSuchMethodException");
		}
		catch (final NoSuchMethodException e) {
		}
		try {
			Methods.invokeExactMethod(testBean, "foo", Numbers.LONG_ONE);
			fail("should throw NoSuchMethodException");
		}
		catch (final NoSuchMethodException e) {
		}
		try {
			Methods.invokeExactMethod(testBean, "foo", Boolean.TRUE);
			fail("should throw NoSuchMethodException");
		}
		catch (final NoSuchMethodException e) {
		}
	}

	@Test
	public void testInvokeStaticMethod() throws Exception {
		assertEquals("bar()",
			Methods.invokeStaticMethod(TestBean.class, "bar", (Object[])Arrays.EMPTY_CLASS_ARRAY));
		assertEquals("bar()", Methods.invokeStaticMethod(TestBean.class, "bar", (Object[])null));
		assertEquals("bar()", Methods.invokeStaticMethod(TestBean.class, "bar", (Object[])null, (Class<?>[])null));
		assertEquals("bar(String)", Methods.invokeStaticMethod(TestBean.class, "bar", ""));
		assertEquals("bar(Object)", Methods.invokeStaticMethod(TestBean.class, "bar", new Object()));
		assertEquals("bar(Object)", Methods.invokeStaticMethod(TestBean.class, "bar", Boolean.TRUE));
		assertEquals("bar(Integer)", Methods.invokeStaticMethod(TestBean.class, "bar", Numbers.INTEGER_ONE));
		assertEquals("bar(int)", Methods.invokeStaticMethod(TestBean.class, "bar", Numbers.BYTE_ONE));
		assertEquals("bar(double)", Methods.invokeStaticMethod(TestBean.class, "bar", Numbers.LONG_ONE));
		assertEquals("bar(double)", Methods.invokeStaticMethod(TestBean.class, "bar", Numbers.DOUBLE_ONE));

		try {
			Methods.invokeStaticMethod(TestBean.class, "does_not_exist");
			fail("should throw NoSuchMethodException");
		}
		catch (final NoSuchMethodException e) {
		}
	}

	@Test
	public void testInvokeExactStaticMethod() throws Exception {
		assertEquals("bar()",
			Methods.invokeExactStaticMethod(TestBean.class, "bar", (Object[])Arrays.EMPTY_CLASS_ARRAY));
		assertEquals("bar()", Methods.invokeExactStaticMethod(TestBean.class, "bar", (Object[])null));
		assertEquals("bar()",
			Methods.invokeExactStaticMethod(TestBean.class, "bar", (Object[])null, (Class<?>[])null));
		assertEquals("bar(String)", Methods.invokeExactStaticMethod(TestBean.class, "bar", ""));
		assertEquals("bar(Object)", Methods.invokeExactStaticMethod(TestBean.class, "bar", new Object()));
		assertEquals("bar(Integer)",
			Methods.invokeExactStaticMethod(TestBean.class, "bar", Numbers.INTEGER_ONE));
		assertEquals("bar(double)", Methods.invokeExactStaticMethod(TestBean.class, "bar",
			new Object[] { Numbers.DOUBLE_ONE }, new Class[] { Double.TYPE }));

		try {
			Methods.invokeExactStaticMethod(TestBean.class, "bar", Numbers.BYTE_ONE);
			fail("should throw NoSuchMethodException");
		}
		catch (final NoSuchMethodException e) {
		}
		try {
			Methods.invokeExactStaticMethod(TestBean.class, "bar", Numbers.LONG_ONE);
			fail("should throw NoSuchMethodException");
		}
		catch (final NoSuchMethodException e) {
		}
		try {
			Methods.invokeExactStaticMethod(TestBean.class, "bar", Boolean.TRUE);
			fail("should throw NoSuchMethodException");
		}
		catch (final NoSuchMethodException e) {
		}
	}

	@Test
	public void testGetAccessibleInterfaceMethod() throws Exception {
		final Class<?>[][] p = { Arrays.EMPTY_CLASS_ARRAY, null };
		for (final Class<?>[] element : p) {
			final Method method = TestMutable.class.getMethod("getValue", element);
			final Method accessibleMethod = Methods.getAccessibleMethod(method);
			assertNotSame(accessibleMethod, method);
			assertSame(Mutable.class, accessibleMethod.getDeclaringClass());
		}
	}

	@Test
	public void testGetAccessibleMethodPrivateInterface() throws Exception {
		final Method expected = TestBeanWithInterfaces.class.getMethod("foo");
		assertNotNull(expected);
		final Method actual = Methods.getAccessibleMethod(TestBeanWithInterfaces.class, "foo");
		assertNull(actual);
	}

	@Test
	public void testGetAccessibleInterfaceMethodFromDescription() throws Exception {
		final Class<?>[][] p = { Arrays.EMPTY_CLASS_ARRAY, null };
		for (final Class<?>[] element : p) {
			final Method accessibleMethod = Methods.getAccessibleMethod(TestMutable.class, "getValue", element);
			assertSame(Mutable.class, accessibleMethod.getDeclaringClass());
		}
	}

	@Test
	public void testGetAccessiblePublicMethod() throws Exception {
		assertSame(MutableObject.class,
			Methods.getAccessibleMethod(MutableObject.class.getMethod("getValue", Arrays.EMPTY_CLASS_ARRAY))
				.getDeclaringClass());
	}

	@Test
	public void testGetAccessiblePublicMethodFromDescription() throws Exception {
		assertSame(MutableObject.class,
			Methods.getAccessibleMethod(MutableObject.class, "getValue", Arrays.EMPTY_CLASS_ARRAY)
				.getDeclaringClass());
	}

	@Test
	public void testGetAccessibleMethodInaccessible() throws Exception {
		final Method expected = TestBean.class.getDeclaredMethod("privateStuff");
		final Method actual = Methods.getAccessibleMethod(expected);
		assertNull(actual);
	}

	@Test
	public void testGetMatchingAccessibleMethod() throws Exception {
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", Arrays.EMPTY_CLASS_ARRAY,
			Arrays.EMPTY_CLASS_ARRAY);
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", null, Arrays.EMPTY_CLASS_ARRAY);
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(String.class),
			singletonArray(String.class));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Object.class),
			singletonArray(Object.class));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Boolean.class),
			singletonArray(Object.class));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Byte.class),
			singletonArray(Integer.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Byte.TYPE),
			singletonArray(Integer.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Short.class),
			singletonArray(Integer.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Short.TYPE),
			singletonArray(Integer.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Character.class),
			singletonArray(Integer.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Character.TYPE),
			singletonArray(Integer.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Integer.class),
			singletonArray(Integer.class));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Integer.TYPE),
			singletonArray(Integer.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Long.class),
			singletonArray(Double.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Long.TYPE),
			singletonArray(Double.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Float.class),
			singletonArray(Double.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Float.TYPE),
			singletonArray(Double.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Double.class),
			singletonArray(Double.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Double.TYPE),
			singletonArray(Double.TYPE));
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "foo", singletonArray(Double.TYPE),
			singletonArray(Double.TYPE));
		expectMatchingAccessibleMethodParameterTypes(InheritanceBean.class, "testOne",
			singletonArray(ParentObject.class), singletonArray(ParentObject.class));
		expectMatchingAccessibleMethodParameterTypes(InheritanceBean.class, "testOne",
			singletonArray(ChildObject.class), singletonArray(ParentObject.class));
		expectMatchingAccessibleMethodParameterTypes(InheritanceBean.class, "testTwo",
			singletonArray(ParentObject.class), singletonArray(GrandParentObject.class));
		expectMatchingAccessibleMethodParameterTypes(InheritanceBean.class, "testTwo",
			singletonArray(ChildObject.class), singletonArray(ChildInterface.class));
	}

	@Test
	public void testNullArgument() {
		expectMatchingAccessibleMethodParameterTypes(TestBean.class, "oneParameter", singletonArray(null),
			singletonArray(String.class));
	}

	private void expectMatchingAccessibleMethodParameterTypes(final Class<?> cls, final String methodName,
			final Class<?>[] requestTypes, final Class<?>[] actualTypes) {
		final Method m = Methods.getMatchingAccessibleMethod(cls, methodName, requestTypes);
		assertTrue(toString(m.getParameterTypes()) + " not equals " + toString(actualTypes),
			Arrays.equals(actualTypes, m.getParameterTypes()));
	}

	private String toString(final Class<?>[] c) {
		return Arrays.asList(c).toString();
	}

	private Class<?>[] singletonArray(final Class<?> c) {
		Class<?>[] result = classCache.get(c);
		if (result == null) {
			result = new Class[] { c };
			classCache.put(c, result);
		}
		return result;
	}

	public static class InheritanceBean {
		public void testOne(final Object obj) {
		}

		public void testOne(final GrandParentObject obj) {
		}

		public void testOne(final ParentObject obj) {
		}

		public void testTwo(final Object obj) {
		}

		public void testTwo(final GrandParentObject obj) {
		}

		public void testTwo(final ChildInterface obj) {
		}
	}

	interface ChildInterface {
	}

	public static class GrandParentObject {
	}

	public static class ParentObject extends GrandParentObject {
	}

	public static class ChildObject extends ParentObject implements ChildInterface {
	}
}
