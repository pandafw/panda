package panda.lang;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * test class for Classes
 */
public class ClassesTest {
	// -------------------------------------------------------------------------
	@Test
	public void testScan() {
		String[] pkgs = new String[] { "org.apache.log4j.jdbc", "panda.log.impl" };
		
		List<Class<?>> clss = Classes.scan(pkgs);
		Assert.assertNotNull(clss);
		Assert.assertTrue(clss.size() > 0);
		
		for (Class<?> cls : clss) {
			boolean m = false;
			for (String pkg : pkgs) {
				if (cls.getName().startsWith(pkg)) {
					m = true;
					break;
				}
			}
			Assert.assertTrue(m);
		}
	}
	
	// -------------------------------------------------------------------------
	/**
	 * test method: getCastableClassName
	 * 
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testGetCastableClassName() throws Exception {
		assertEquals("int[][]", Classes.getCastableClassName(int[][].class));
		assertEquals("java.lang.Boolean[][]", Classes.getCastableClassName(Boolean[][].class));
	}

	private static class Inner {
		private class DeeplyNested {
		}
	}

	// -------------------------------------------------------------------------
	@Test
	public void test_getShortClassName_Class() {
		assertEquals("Classes", Classes.getShortClassName(Classes.class));
		assertEquals("Map.Entry", Classes.getShortClassName(Map.Entry.class));
		assertEquals(null, Classes.getShortClassName((Class<?>)null));

		assertEquals("String[]", Classes.getShortClassName(String[].class));
		assertEquals("Map.Entry[]", Classes.getShortClassName(Map.Entry[].class));

		// Primitives
		assertEquals("boolean", Classes.getShortClassName(boolean.class));
		assertEquals("byte", Classes.getShortClassName(byte.class));
		assertEquals("char", Classes.getShortClassName(char.class));
		assertEquals("short", Classes.getShortClassName(short.class));
		assertEquals("int", Classes.getShortClassName(int.class));
		assertEquals("long", Classes.getShortClassName(long.class));
		assertEquals("float", Classes.getShortClassName(float.class));
		assertEquals("double", Classes.getShortClassName(double.class));

		// Primitive Arrays
		assertEquals("boolean[]", Classes.getShortClassName(boolean[].class));
		assertEquals("byte[]", Classes.getShortClassName(byte[].class));
		assertEquals("char[]", Classes.getShortClassName(char[].class));
		assertEquals("short[]", Classes.getShortClassName(short[].class));
		assertEquals("int[]", Classes.getShortClassName(int[].class));
		assertEquals("long[]", Classes.getShortClassName(long[].class));
		assertEquals("float[]", Classes.getShortClassName(float[].class));
		assertEquals("double[]", Classes.getShortClassName(double[].class));

		// Arrays of arrays of ...
		assertEquals("String[][]", Classes.getShortClassName(String[][].class));
		assertEquals("String[][][]", Classes.getShortClassName(String[][][].class));
		assertEquals("String[][][][]", Classes.getShortClassName(String[][][][].class));

		// Inner types
		class Named extends Object {
		}
		assertEquals("ClassesTest.1", Classes.getShortClassName(new Object() {
		}.getClass()));
		assertEquals("ClassesTest.1Named", Classes.getShortClassName(Named.class));
		assertEquals("ClassesTest.Inner", Classes.getShortClassName(Inner.class));
	}

	@Test
	public void test_getShortClassName_String() {
		assertEquals("Classes", Classes.getShortClassName(Classes.class.getName()));
		assertEquals("Map.Entry", Classes.getShortClassName(Map.Entry.class.getName()));
		assertEquals(null, Classes.getShortClassName((String)null));
		assertEquals("", Classes.getShortClassName(""));
	}

	@Test
	public void test_getSimpleClassName_Class() {
		assertEquals("Classes", Classes.getSimpleClassName(Classes.class));
		assertEquals("Entry", Classes.getSimpleClassName(Map.Entry.class));
		assertEquals(null, Classes.getSimpleClassName((Class<?>)null));

		assertEquals("String[]", Classes.getSimpleClassName(String[].class));
		assertEquals("Entry[]", Classes.getSimpleClassName(Map.Entry[].class));

		// Primitives
		assertEquals("boolean", Classes.getSimpleClassName(boolean.class));
		assertEquals("byte", Classes.getSimpleClassName(byte.class));
		assertEquals("char", Classes.getSimpleClassName(char.class));
		assertEquals("short", Classes.getSimpleClassName(short.class));
		assertEquals("int", Classes.getSimpleClassName(int.class));
		assertEquals("long", Classes.getSimpleClassName(long.class));
		assertEquals("float", Classes.getSimpleClassName(float.class));
		assertEquals("double", Classes.getSimpleClassName(double.class));

		// Primitive Arrays
		assertEquals("boolean[]", Classes.getSimpleClassName(boolean[].class));
		assertEquals("byte[]", Classes.getSimpleClassName(byte[].class));
		assertEquals("char[]", Classes.getSimpleClassName(char[].class));
		assertEquals("short[]", Classes.getSimpleClassName(short[].class));
		assertEquals("int[]", Classes.getSimpleClassName(int[].class));
		assertEquals("long[]", Classes.getSimpleClassName(long[].class));
		assertEquals("float[]", Classes.getSimpleClassName(float[].class));
		assertEquals("double[]", Classes.getSimpleClassName(double[].class));

		// Arrays of arrays of ...
		assertEquals("String[][]", Classes.getSimpleClassName(String[][].class));
		assertEquals("String[][][]", Classes.getSimpleClassName(String[][][].class));
		assertEquals("String[][][][]", Classes.getSimpleClassName(String[][][][].class));

		// On-the-fly types
		class Named extends Object {
		}
		assertEquals("", Classes.getSimpleClassName(new Object() {
		}.getClass()));
		assertEquals("Named", Classes.getSimpleClassName(Named.class));
	}

	// -------------------------------------------------------------------------
	@Test
	public void test_getPackageName_Object() {
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageName(new ClassesTest(), "<null>"));
		assertEquals(ClassesTest.class.getPackage().getName(), Classes.getPackageName(new Inner(), "<null>"));
		assertEquals("<null>", Classes.getPackageName(null, "<null>"));
	}

	@Test
	public void test_getPackageName_Class() {
		assertEquals("java.lang", Classes.getPackageName(String.class));
		assertEquals("java.util", Classes.getPackageName(Map.Entry.class));
		assertEquals("", Classes.getPackageName((Class<?>)null));

		// LANG-535
		assertEquals("java.lang", Classes.getPackageName(String[].class));

		// Primitive Arrays
		assertEquals("", Classes.getPackageName(boolean[].class));
		assertEquals("", Classes.getPackageName(byte[].class));
		assertEquals("", Classes.getPackageName(char[].class));
		assertEquals("", Classes.getPackageName(short[].class));
		assertEquals("", Classes.getPackageName(int[].class));
		assertEquals("", Classes.getPackageName(long[].class));
		assertEquals("", Classes.getPackageName(float[].class));
		assertEquals("", Classes.getPackageName(double[].class));

		// Arrays of arrays of ...
		assertEquals("java.lang", Classes.getPackageName(String[][].class));
		assertEquals("java.lang", Classes.getPackageName(String[][][].class));
		assertEquals("java.lang", Classes.getPackageName(String[][][][].class));

		// On-the-fly types
		class Named extends Object {
		}
		assertEquals(ClassesTest.class.getPackage().getName(), Classes.getPackageName(new Object() {
		}.getClass()));
		assertEquals(ClassesTest.class.getPackage().getName(), Classes.getPackageName(Named.class));
	}

	@Test
	public void test_getPackageName_String() {
		assertEquals(ClassesTest.class.getPackage().getName(), Classes.getPackageName(Classes.class.getName()));
		assertEquals("java.util", Classes.getPackageName(Map.Entry.class.getName()));
		assertEquals("", Classes.getPackageName((String)null));
		assertEquals("", Classes.getPackageName(""));
	}

	// -------------------------------------------------------------------------
	@Test
	public void test_getAllSuperclasses_Class() {
		final List<?> list = Classes.getAllSuperclasses(CY.class);
		assertEquals(2, list.size());
		assertEquals(CX.class, list.get(0));
		assertEquals(Object.class, list.get(1));

		assertEquals(null, Classes.getAllSuperclasses(null));
	}

	@Test
	public void test_getAllInterfaces_Class() {
		final List<?> list = Classes.getAllInterfaces(CY.class);
		assertEquals(6, list.size());
		assertEquals(IB.class, list.get(0));
		assertEquals(IC.class, list.get(1));
		assertEquals(ID.class, list.get(2));
		assertEquals(IE.class, list.get(3));
		assertEquals(IF.class, list.get(4));
		assertEquals(IA.class, list.get(5));

		assertEquals(null, Classes.getAllInterfaces(null));
	}

	private static interface IA {
	}

	private static interface IB {
	}

	private static interface IC extends ID, IE {
	}

	private static interface ID {
	}

	private static interface IE extends IF {
	}

	private static interface IF {
	}

	private static class CX implements IB, IA, IE {
	}

	private static class CY extends CX implements IB, IC {
	}

	// -------------------------------------------------------------------------
	@Test
	public void test_convertClassNamesToClasses_List() {
		final List<String> list = new ArrayList<String>();
		List<Class<?>> result = Classes.convertClassNamesToClasses(list);
		assertEquals(0, result.size());

		list.add("java.lang.String");
		list.add("java.lang.xxx");
		list.add("java.lang.Object");
		result = Classes.convertClassNamesToClasses(list);
		assertEquals(3, result.size());
		assertEquals(String.class, result.get(0));
		assertEquals(null, result.get(1));
		assertEquals(Object.class, result.get(2));

		@SuppressWarnings("unchecked")
		// test what happens when non-generic code adds wrong type of element
		final List<Object> olist = (List<Object>)(List<?>)list;
		olist.add(new Object());
		try {
			Classes.convertClassNamesToClasses(list);
			fail("Should not have been able to convert list");
		}
		catch (final ClassCastException expected) {
		}
		assertEquals(null, Classes.convertClassNamesToClasses(null));
	}

	@Test
	public void test_convertClassesToClassNames_List() {
		final List<Class<?>> list = new ArrayList<Class<?>>();
		List<String> result = Classes.convertClassesToClassNames(list);
		assertEquals(0, result.size());

		list.add(String.class);
		list.add(null);
		list.add(Object.class);
		result = Classes.convertClassesToClassNames(list);
		assertEquals(3, result.size());
		assertEquals("java.lang.String", result.get(0));
		assertEquals(null, result.get(1));
		assertEquals("java.lang.Object", result.get(2));

		@SuppressWarnings("unchecked")
		// test what happens when non-generic code adds wrong type of element
		final List<Object> olist = (List<Object>)(List<?>)list;
		olist.add(new Object());
		try {
			Classes.convertClassesToClassNames(list);
			fail("Should not have been able to convert list");
		}
		catch (final ClassCastException expected) {
		}
		assertEquals(null, Classes.convertClassesToClassNames(null));
	}

	// -------------------------------------------------------------------------
	@Test
	public void test_isInnerClass_Class() {
		assertTrue(Classes.isInnerClass(Inner.class));
		assertTrue(Classes.isInnerClass(Map.Entry.class));
		assertTrue(Classes.isInnerClass(new Cloneable() {
		}.getClass()));
		assertFalse(Classes.isInnerClass(this.getClass()));
		assertFalse(Classes.isInnerClass(String.class));
		assertFalse(Classes.isInnerClass(null));
	}

	// -------------------------------------------------------------------------
	@Test
	public void test_isAssignable_ClassArray_ClassArray() throws Exception {
		final Class<?>[] array2 = new Class[] { Object.class, Object.class };
		final Class<?>[] array1 = new Class[] { Object.class };
		final Class<?>[] array1s = new Class[] { String.class };
		final Class<?>[] array0 = new Class[] {};
		final Class<?>[] arrayPrimitives = { Integer.TYPE, Boolean.TYPE };
		final Class<?>[] arrayWrappers = { Integer.class, Boolean.class };

		assertFalse(Classes.isAssignable(array1, array2));
		assertFalse(Classes.isAssignable(null, array2));
		assertTrue(Classes.isAssignable(null, array0));
		assertTrue(Classes.isAssignable(array0, array0));
		// assertTrue(Classes.isAssignable(array0, null));
		assertTrue(Classes.isAssignable(array0, (Class<?>[])null)); // explicit cast to avoid
																	// warning
		assertTrue(Classes.isAssignable((Class[])null, (Class[])null));

		assertFalse(Classes.isAssignable(array1, array1s));
		assertTrue(Classes.isAssignable(array1s, array1s));
		assertTrue(Classes.isAssignable(array1s, array1));

		final boolean autoboxing = true;

		assertEquals(autoboxing, Classes.isAssignable(arrayPrimitives, arrayWrappers));
		assertEquals(autoboxing, Classes.isAssignable(arrayWrappers, arrayPrimitives));
		assertFalse(Classes.isAssignable(arrayPrimitives, array1));
		assertFalse(Classes.isAssignable(arrayWrappers, array1));
		assertEquals(autoboxing, Classes.isAssignable(arrayPrimitives, array2));
		assertTrue(Classes.isAssignable(arrayWrappers, array2));
	}

	@Test
	public void test_isAssignable_ClassArray_ClassArray_Autoboxing() throws Exception {
		final Class<?>[] array2 = new Class[] { Object.class, Object.class };
		final Class<?>[] array1 = new Class[] { Object.class };
		final Class<?>[] array1s = new Class[] { String.class };
		final Class<?>[] array0 = new Class[] {};
		final Class<?>[] arrayPrimitives = { Integer.TYPE, Boolean.TYPE };
		final Class<?>[] arrayWrappers = { Integer.class, Boolean.class };

		assertFalse(Classes.isAssignable(array1, array2, true));
		assertFalse(Classes.isAssignable(null, array2, true));
		assertTrue(Classes.isAssignable(null, array0, true));
		assertTrue(Classes.isAssignable(array0, array0, true));
		assertTrue(Classes.isAssignable(array0, null, true));
		assertTrue(Classes.isAssignable((Class[])null, (Class[])null, true));

		assertFalse(Classes.isAssignable(array1, array1s, true));
		assertTrue(Classes.isAssignable(array1s, array1s, true));
		assertTrue(Classes.isAssignable(array1s, array1, true));

		assertTrue(Classes.isAssignable(arrayPrimitives, arrayWrappers, true));
		assertTrue(Classes.isAssignable(arrayWrappers, arrayPrimitives, true));
		assertFalse(Classes.isAssignable(arrayPrimitives, array1, true));
		assertFalse(Classes.isAssignable(arrayWrappers, array1, true));
		assertTrue(Classes.isAssignable(arrayPrimitives, array2, true));
		assertTrue(Classes.isAssignable(arrayWrappers, array2, true));
	}

	@Test
	public void test_isAssignable_ClassArray_ClassArray_NoAutoboxing() throws Exception {
		final Class<?>[] array2 = new Class[] { Object.class, Object.class };
		final Class<?>[] array1 = new Class[] { Object.class };
		final Class<?>[] array1s = new Class[] { String.class };
		final Class<?>[] array0 = new Class[] {};
		final Class<?>[] arrayPrimitives = { Integer.TYPE, Boolean.TYPE };
		final Class<?>[] arrayWrappers = { Integer.class, Boolean.class };

		assertFalse(Classes.isAssignable(array1, array2, false));
		assertFalse(Classes.isAssignable(null, array2, false));
		assertTrue(Classes.isAssignable(null, array0, false));
		assertTrue(Classes.isAssignable(array0, array0, false));
		assertTrue(Classes.isAssignable(array0, null, false));
		assertTrue(Classes.isAssignable((Class[])null, (Class[])null, false));

		assertFalse(Classes.isAssignable(array1, array1s, false));
		assertTrue(Classes.isAssignable(array1s, array1s, false));
		assertTrue(Classes.isAssignable(array1s, array1, false));

		assertFalse(Classes.isAssignable(arrayPrimitives, arrayWrappers, false));
		assertFalse(Classes.isAssignable(arrayWrappers, arrayPrimitives, false));
		assertFalse(Classes.isAssignable(arrayPrimitives, array1, false));
		assertFalse(Classes.isAssignable(arrayWrappers, array1, false));
		assertTrue(Classes.isAssignable(arrayWrappers, array2, false));
		assertFalse(Classes.isAssignable(arrayPrimitives, array2, false));
	}

	@Test
	public void test_isAssignable() throws Exception {
		assertTrue(Classes.isAssignable((Class<?>)null, null));
		assertFalse(Classes.isAssignable(String.class, null));

		assertTrue(Classes.isAssignable(null, Object.class));
		assertTrue(Classes.isAssignable(null, Integer.class));
		assertFalse(Classes.isAssignable(null, Integer.TYPE));
		assertTrue(Classes.isAssignable(String.class, Object.class));
		assertTrue(Classes.isAssignable(String.class, String.class));
		assertFalse(Classes.isAssignable(Object.class, String.class));

		final boolean autoboxing = true;

		assertEquals(autoboxing, Classes.isAssignable(Integer.TYPE, Integer.class));
		assertEquals(autoboxing, Classes.isAssignable(Integer.TYPE, Object.class));
		assertEquals(autoboxing, Classes.isAssignable(Integer.class, Integer.TYPE));
		assertEquals(autoboxing, Classes.isAssignable(Integer.class, Object.class));
		assertTrue(Classes.isAssignable(Integer.TYPE, Integer.TYPE));
		assertTrue(Classes.isAssignable(Integer.class, Integer.class));
		assertEquals(autoboxing, Classes.isAssignable(Boolean.TYPE, Boolean.class));
		assertEquals(autoboxing, Classes.isAssignable(Boolean.TYPE, Object.class));
		assertEquals(autoboxing, Classes.isAssignable(Boolean.class, Boolean.TYPE));
		assertEquals(autoboxing, Classes.isAssignable(Boolean.class, Object.class));
		assertTrue(Classes.isAssignable(Boolean.TYPE, Boolean.TYPE));
		assertTrue(Classes.isAssignable(Boolean.class, Boolean.class));
	}

	@Test
	public void test_isAssignable_Autoboxing() throws Exception {
		assertTrue(Classes.isAssignable((Class<?>)null, null, true));
		assertFalse(Classes.isAssignable(String.class, null, true));

		assertTrue(Classes.isAssignable(null, Object.class, true));
		assertTrue(Classes.isAssignable(null, Integer.class, true));
		assertFalse(Classes.isAssignable(null, Integer.TYPE, true));
		assertTrue(Classes.isAssignable(String.class, Object.class, true));
		assertTrue(Classes.isAssignable(String.class, String.class, true));
		assertFalse(Classes.isAssignable(Object.class, String.class, true));
		assertTrue(Classes.isAssignable(Integer.TYPE, Integer.class, true));
		assertTrue(Classes.isAssignable(Integer.TYPE, Object.class, true));
		assertTrue(Classes.isAssignable(Integer.class, Integer.TYPE, true));
		assertTrue(Classes.isAssignable(Integer.class, Object.class, true));
		assertTrue(Classes.isAssignable(Integer.TYPE, Integer.TYPE, true));
		assertTrue(Classes.isAssignable(Integer.class, Integer.class, true));
		assertTrue(Classes.isAssignable(Boolean.TYPE, Boolean.class, true));
		assertTrue(Classes.isAssignable(Boolean.class, Boolean.TYPE, true));
		assertTrue(Classes.isAssignable(Boolean.class, Object.class, true));
		assertTrue(Classes.isAssignable(Boolean.TYPE, Boolean.TYPE, true));
		assertTrue(Classes.isAssignable(Boolean.class, Boolean.class, true));
	}

	@Test
	public void test_isAssignable_NoAutoboxing() throws Exception {
		assertTrue(Classes.isAssignable((Class<?>)null, null, false));
		assertFalse(Classes.isAssignable(String.class, null, false));

		assertTrue(Classes.isAssignable(null, Object.class, false));
		assertTrue(Classes.isAssignable(null, Integer.class, false));
		assertFalse(Classes.isAssignable(null, Integer.TYPE, false));
		assertTrue(Classes.isAssignable(String.class, Object.class, false));
		assertTrue(Classes.isAssignable(String.class, String.class, false));
		assertFalse(Classes.isAssignable(Object.class, String.class, false));
		assertFalse(Classes.isAssignable(Integer.TYPE, Integer.class, false));
		assertFalse(Classes.isAssignable(Integer.TYPE, Object.class, false));
		assertFalse(Classes.isAssignable(Integer.class, Integer.TYPE, false));
		assertTrue(Classes.isAssignable(Integer.TYPE, Integer.TYPE, false));
		assertTrue(Classes.isAssignable(Integer.class, Integer.class, false));
		assertFalse(Classes.isAssignable(Boolean.TYPE, Boolean.class, false));
		assertFalse(Classes.isAssignable(Boolean.TYPE, Object.class, false));
		assertFalse(Classes.isAssignable(Boolean.class, Boolean.TYPE, false));
		assertTrue(Classes.isAssignable(Boolean.class, Object.class, false));
		assertTrue(Classes.isAssignable(Boolean.TYPE, Boolean.TYPE, false));
		assertTrue(Classes.isAssignable(Boolean.class, Boolean.class, false));
	}

	@Test
	public void test_isAssignable_Widening() throws Exception {
		// test byte conversions
		assertFalse("byte -> char", Classes.isAssignable(Byte.TYPE, Character.TYPE));
		assertTrue("byte -> byte", Classes.isAssignable(Byte.TYPE, Byte.TYPE));
		assertTrue("byte -> short", Classes.isAssignable(Byte.TYPE, Short.TYPE));
		assertTrue("byte -> int", Classes.isAssignable(Byte.TYPE, Integer.TYPE));
		assertTrue("byte -> long", Classes.isAssignable(Byte.TYPE, Long.TYPE));
		assertTrue("byte -> float", Classes.isAssignable(Byte.TYPE, Float.TYPE));
		assertTrue("byte -> double", Classes.isAssignable(Byte.TYPE, Double.TYPE));
		assertFalse("byte -> boolean", Classes.isAssignable(Byte.TYPE, Boolean.TYPE));

		// test short conversions
		assertFalse("short -> char", Classes.isAssignable(Short.TYPE, Character.TYPE));
		assertFalse("short -> byte", Classes.isAssignable(Short.TYPE, Byte.TYPE));
		assertTrue("short -> short", Classes.isAssignable(Short.TYPE, Short.TYPE));
		assertTrue("short -> int", Classes.isAssignable(Short.TYPE, Integer.TYPE));
		assertTrue("short -> long", Classes.isAssignable(Short.TYPE, Long.TYPE));
		assertTrue("short -> float", Classes.isAssignable(Short.TYPE, Float.TYPE));
		assertTrue("short -> double", Classes.isAssignable(Short.TYPE, Double.TYPE));
		assertFalse("short -> boolean", Classes.isAssignable(Short.TYPE, Boolean.TYPE));

		// test char conversions
		assertTrue("char -> char", Classes.isAssignable(Character.TYPE, Character.TYPE));
		assertFalse("char -> byte", Classes.isAssignable(Character.TYPE, Byte.TYPE));
		assertFalse("char -> short", Classes.isAssignable(Character.TYPE, Short.TYPE));
		assertTrue("char -> int", Classes.isAssignable(Character.TYPE, Integer.TYPE));
		assertTrue("char -> long", Classes.isAssignable(Character.TYPE, Long.TYPE));
		assertTrue("char -> float", Classes.isAssignable(Character.TYPE, Float.TYPE));
		assertTrue("char -> double", Classes.isAssignable(Character.TYPE, Double.TYPE));
		assertFalse("char -> boolean", Classes.isAssignable(Character.TYPE, Boolean.TYPE));

		// test int conversions
		assertFalse("int -> char", Classes.isAssignable(Integer.TYPE, Character.TYPE));
		assertFalse("int -> byte", Classes.isAssignable(Integer.TYPE, Byte.TYPE));
		assertFalse("int -> short", Classes.isAssignable(Integer.TYPE, Short.TYPE));
		assertTrue("int -> int", Classes.isAssignable(Integer.TYPE, Integer.TYPE));
		assertTrue("int -> long", Classes.isAssignable(Integer.TYPE, Long.TYPE));
		assertTrue("int -> float", Classes.isAssignable(Integer.TYPE, Float.TYPE));
		assertTrue("int -> double", Classes.isAssignable(Integer.TYPE, Double.TYPE));
		assertFalse("int -> boolean", Classes.isAssignable(Integer.TYPE, Boolean.TYPE));

		// test long conversions
		assertFalse("long -> char", Classes.isAssignable(Long.TYPE, Character.TYPE));
		assertFalse("long -> byte", Classes.isAssignable(Long.TYPE, Byte.TYPE));
		assertFalse("long -> short", Classes.isAssignable(Long.TYPE, Short.TYPE));
		assertFalse("long -> int", Classes.isAssignable(Long.TYPE, Integer.TYPE));
		assertTrue("long -> long", Classes.isAssignable(Long.TYPE, Long.TYPE));
		assertTrue("long -> float", Classes.isAssignable(Long.TYPE, Float.TYPE));
		assertTrue("long -> double", Classes.isAssignable(Long.TYPE, Double.TYPE));
		assertFalse("long -> boolean", Classes.isAssignable(Long.TYPE, Boolean.TYPE));

		// test float conversions
		assertFalse("float -> char", Classes.isAssignable(Float.TYPE, Character.TYPE));
		assertFalse("float -> byte", Classes.isAssignable(Float.TYPE, Byte.TYPE));
		assertFalse("float -> short", Classes.isAssignable(Float.TYPE, Short.TYPE));
		assertFalse("float -> int", Classes.isAssignable(Float.TYPE, Integer.TYPE));
		assertFalse("float -> long", Classes.isAssignable(Float.TYPE, Long.TYPE));
		assertTrue("float -> float", Classes.isAssignable(Float.TYPE, Float.TYPE));
		assertTrue("float -> double", Classes.isAssignable(Float.TYPE, Double.TYPE));
		assertFalse("float -> boolean", Classes.isAssignable(Float.TYPE, Boolean.TYPE));

		// test double conversions
		assertFalse("double -> char", Classes.isAssignable(Double.TYPE, Character.TYPE));
		assertFalse("double -> byte", Classes.isAssignable(Double.TYPE, Byte.TYPE));
		assertFalse("double -> short", Classes.isAssignable(Double.TYPE, Short.TYPE));
		assertFalse("double -> int", Classes.isAssignable(Double.TYPE, Integer.TYPE));
		assertFalse("double -> long", Classes.isAssignable(Double.TYPE, Long.TYPE));
		assertFalse("double -> float", Classes.isAssignable(Double.TYPE, Float.TYPE));
		assertTrue("double -> double", Classes.isAssignable(Double.TYPE, Double.TYPE));
		assertFalse("double -> boolean", Classes.isAssignable(Double.TYPE, Boolean.TYPE));

		// test boolean conversions
		assertFalse("boolean -> char", Classes.isAssignable(Boolean.TYPE, Character.TYPE));
		assertFalse("boolean -> byte", Classes.isAssignable(Boolean.TYPE, Byte.TYPE));
		assertFalse("boolean -> short", Classes.isAssignable(Boolean.TYPE, Short.TYPE));
		assertFalse("boolean -> int", Classes.isAssignable(Boolean.TYPE, Integer.TYPE));
		assertFalse("boolean -> long", Classes.isAssignable(Boolean.TYPE, Long.TYPE));
		assertFalse("boolean -> float", Classes.isAssignable(Boolean.TYPE, Float.TYPE));
		assertFalse("boolean -> double", Classes.isAssignable(Boolean.TYPE, Double.TYPE));
		assertTrue("boolean -> boolean", Classes.isAssignable(Boolean.TYPE, Boolean.TYPE));
	}

	@Test
	public void test_isAssignable_DefaultUnboxing_Widening() throws Exception {
		final boolean autoboxing = true;

		// test byte conversions
		assertFalse("byte -> char", Classes.isAssignable(Byte.class, Character.TYPE));
		assertEquals("byte -> byte", autoboxing, Classes.isAssignable(Byte.class, Byte.TYPE));
		assertEquals("byte -> short", autoboxing, Classes.isAssignable(Byte.class, Short.TYPE));
		assertEquals("byte -> int", autoboxing, Classes.isAssignable(Byte.class, Integer.TYPE));
		assertEquals("byte -> long", autoboxing, Classes.isAssignable(Byte.class, Long.TYPE));
		assertEquals("byte -> float", autoboxing, Classes.isAssignable(Byte.class, Float.TYPE));
		assertEquals("byte -> double", autoboxing, Classes.isAssignable(Byte.class, Double.TYPE));
		assertFalse("byte -> boolean", Classes.isAssignable(Byte.class, Boolean.TYPE));

		// test short conversions
		assertFalse("short -> char", Classes.isAssignable(Short.class, Character.TYPE));
		assertFalse("short -> byte", Classes.isAssignable(Short.class, Byte.TYPE));
		assertEquals("short -> short", autoboxing, Classes.isAssignable(Short.class, Short.TYPE));
		assertEquals("short -> int", autoboxing, Classes.isAssignable(Short.class, Integer.TYPE));
		assertEquals("short -> long", autoboxing, Classes.isAssignable(Short.class, Long.TYPE));
		assertEquals("short -> float", autoboxing, Classes.isAssignable(Short.class, Float.TYPE));
		assertEquals("short -> double", autoboxing, Classes.isAssignable(Short.class, Double.TYPE));
		assertFalse("short -> boolean", Classes.isAssignable(Short.class, Boolean.TYPE));

		// test char conversions
		assertEquals("char -> char", autoboxing, Classes.isAssignable(Character.class, Character.TYPE));
		assertFalse("char -> byte", Classes.isAssignable(Character.class, Byte.TYPE));
		assertFalse("char -> short", Classes.isAssignable(Character.class, Short.TYPE));
		assertEquals("char -> int", autoboxing, Classes.isAssignable(Character.class, Integer.TYPE));
		assertEquals("char -> long", autoboxing, Classes.isAssignable(Character.class, Long.TYPE));
		assertEquals("char -> float", autoboxing, Classes.isAssignable(Character.class, Float.TYPE));
		assertEquals("char -> double", autoboxing, Classes.isAssignable(Character.class, Double.TYPE));
		assertFalse("char -> boolean", Classes.isAssignable(Character.class, Boolean.TYPE));

		// test int conversions
		assertFalse("int -> char", Classes.isAssignable(Integer.class, Character.TYPE));
		assertFalse("int -> byte", Classes.isAssignable(Integer.class, Byte.TYPE));
		assertFalse("int -> short", Classes.isAssignable(Integer.class, Short.TYPE));
		assertEquals("int -> int", autoboxing, Classes.isAssignable(Integer.class, Integer.TYPE));
		assertEquals("int -> long", autoboxing, Classes.isAssignable(Integer.class, Long.TYPE));
		assertEquals("int -> float", autoboxing, Classes.isAssignable(Integer.class, Float.TYPE));
		assertEquals("int -> double", autoboxing, Classes.isAssignable(Integer.class, Double.TYPE));
		assertFalse("int -> boolean", Classes.isAssignable(Integer.class, Boolean.TYPE));

		// test long conversions
		assertFalse("long -> char", Classes.isAssignable(Long.class, Character.TYPE));
		assertFalse("long -> byte", Classes.isAssignable(Long.class, Byte.TYPE));
		assertFalse("long -> short", Classes.isAssignable(Long.class, Short.TYPE));
		assertFalse("long -> int", Classes.isAssignable(Long.class, Integer.TYPE));
		assertEquals("long -> long", autoboxing, Classes.isAssignable(Long.class, Long.TYPE));
		assertEquals("long -> float", autoboxing, Classes.isAssignable(Long.class, Float.TYPE));
		assertEquals("long -> double", autoboxing, Classes.isAssignable(Long.class, Double.TYPE));
		assertFalse("long -> boolean", Classes.isAssignable(Long.class, Boolean.TYPE));

		// test float conversions
		assertFalse("float -> char", Classes.isAssignable(Float.class, Character.TYPE));
		assertFalse("float -> byte", Classes.isAssignable(Float.class, Byte.TYPE));
		assertFalse("float -> short", Classes.isAssignable(Float.class, Short.TYPE));
		assertFalse("float -> int", Classes.isAssignable(Float.class, Integer.TYPE));
		assertFalse("float -> long", Classes.isAssignable(Float.class, Long.TYPE));
		assertEquals("float -> float", autoboxing, Classes.isAssignable(Float.class, Float.TYPE));
		assertEquals("float -> double", autoboxing, Classes.isAssignable(Float.class, Double.TYPE));
		assertFalse("float -> boolean", Classes.isAssignable(Float.class, Boolean.TYPE));

		// test double conversions
		assertFalse("double -> char", Classes.isAssignable(Double.class, Character.TYPE));
		assertFalse("double -> byte", Classes.isAssignable(Double.class, Byte.TYPE));
		assertFalse("double -> short", Classes.isAssignable(Double.class, Short.TYPE));
		assertFalse("double -> int", Classes.isAssignable(Double.class, Integer.TYPE));
		assertFalse("double -> long", Classes.isAssignable(Double.class, Long.TYPE));
		assertFalse("double -> float", Classes.isAssignable(Double.class, Float.TYPE));
		assertEquals("double -> double", autoboxing, Classes.isAssignable(Double.class, Double.TYPE));
		assertFalse("double -> boolean", Classes.isAssignable(Double.class, Boolean.TYPE));

		// test boolean conversions
		assertFalse("boolean -> char", Classes.isAssignable(Boolean.class, Character.TYPE));
		assertFalse("boolean -> byte", Classes.isAssignable(Boolean.class, Byte.TYPE));
		assertFalse("boolean -> short", Classes.isAssignable(Boolean.class, Short.TYPE));
		assertFalse("boolean -> int", Classes.isAssignable(Boolean.class, Integer.TYPE));
		assertFalse("boolean -> long", Classes.isAssignable(Boolean.class, Long.TYPE));
		assertFalse("boolean -> float", Classes.isAssignable(Boolean.class, Float.TYPE));
		assertFalse("boolean -> double", Classes.isAssignable(Boolean.class, Double.TYPE));
		assertEquals("boolean -> boolean", autoboxing, Classes.isAssignable(Boolean.class, Boolean.TYPE));
	}

	@Test
	public void test_isAssignable_Unboxing_Widening() throws Exception {
		// test byte conversions
		assertFalse("byte -> char", Classes.isAssignable(Byte.class, Character.TYPE, true));
		assertTrue("byte -> byte", Classes.isAssignable(Byte.class, Byte.TYPE, true));
		assertTrue("byte -> short", Classes.isAssignable(Byte.class, Short.TYPE, true));
		assertTrue("byte -> int", Classes.isAssignable(Byte.class, Integer.TYPE, true));
		assertTrue("byte -> long", Classes.isAssignable(Byte.class, Long.TYPE, true));
		assertTrue("byte -> float", Classes.isAssignable(Byte.class, Float.TYPE, true));
		assertTrue("byte -> double", Classes.isAssignable(Byte.class, Double.TYPE, true));
		assertFalse("byte -> boolean", Classes.isAssignable(Byte.class, Boolean.TYPE, true));

		// test short conversions
		assertFalse("short -> char", Classes.isAssignable(Short.class, Character.TYPE, true));
		assertFalse("short -> byte", Classes.isAssignable(Short.class, Byte.TYPE, true));
		assertTrue("short -> short", Classes.isAssignable(Short.class, Short.TYPE, true));
		assertTrue("short -> int", Classes.isAssignable(Short.class, Integer.TYPE, true));
		assertTrue("short -> long", Classes.isAssignable(Short.class, Long.TYPE, true));
		assertTrue("short -> float", Classes.isAssignable(Short.class, Float.TYPE, true));
		assertTrue("short -> double", Classes.isAssignable(Short.class, Double.TYPE, true));
		assertFalse("short -> boolean", Classes.isAssignable(Short.class, Boolean.TYPE, true));

		// test char conversions
		assertTrue("char -> char", Classes.isAssignable(Character.class, Character.TYPE, true));
		assertFalse("char -> byte", Classes.isAssignable(Character.class, Byte.TYPE, true));
		assertFalse("char -> short", Classes.isAssignable(Character.class, Short.TYPE, true));
		assertTrue("char -> int", Classes.isAssignable(Character.class, Integer.TYPE, true));
		assertTrue("char -> long", Classes.isAssignable(Character.class, Long.TYPE, true));
		assertTrue("char -> float", Classes.isAssignable(Character.class, Float.TYPE, true));
		assertTrue("char -> double", Classes.isAssignable(Character.class, Double.TYPE, true));
		assertFalse("char -> boolean", Classes.isAssignable(Character.class, Boolean.TYPE, true));

		// test int conversions
		assertFalse("int -> char", Classes.isAssignable(Integer.class, Character.TYPE, true));
		assertFalse("int -> byte", Classes.isAssignable(Integer.class, Byte.TYPE, true));
		assertFalse("int -> short", Classes.isAssignable(Integer.class, Short.TYPE, true));
		assertTrue("int -> int", Classes.isAssignable(Integer.class, Integer.TYPE, true));
		assertTrue("int -> long", Classes.isAssignable(Integer.class, Long.TYPE, true));
		assertTrue("int -> float", Classes.isAssignable(Integer.class, Float.TYPE, true));
		assertTrue("int -> double", Classes.isAssignable(Integer.class, Double.TYPE, true));
		assertFalse("int -> boolean", Classes.isAssignable(Integer.class, Boolean.TYPE, true));

		// test long conversions
		assertFalse("long -> char", Classes.isAssignable(Long.class, Character.TYPE, true));
		assertFalse("long -> byte", Classes.isAssignable(Long.class, Byte.TYPE, true));
		assertFalse("long -> short", Classes.isAssignable(Long.class, Short.TYPE, true));
		assertFalse("long -> int", Classes.isAssignable(Long.class, Integer.TYPE, true));
		assertTrue("long -> long", Classes.isAssignable(Long.class, Long.TYPE, true));
		assertTrue("long -> float", Classes.isAssignable(Long.class, Float.TYPE, true));
		assertTrue("long -> double", Classes.isAssignable(Long.class, Double.TYPE, true));
		assertFalse("long -> boolean", Classes.isAssignable(Long.class, Boolean.TYPE, true));

		// test float conversions
		assertFalse("float -> char", Classes.isAssignable(Float.class, Character.TYPE, true));
		assertFalse("float -> byte", Classes.isAssignable(Float.class, Byte.TYPE, true));
		assertFalse("float -> short", Classes.isAssignable(Float.class, Short.TYPE, true));
		assertFalse("float -> int", Classes.isAssignable(Float.class, Integer.TYPE, true));
		assertFalse("float -> long", Classes.isAssignable(Float.class, Long.TYPE, true));
		assertTrue("float -> float", Classes.isAssignable(Float.class, Float.TYPE, true));
		assertTrue("float -> double", Classes.isAssignable(Float.class, Double.TYPE, true));
		assertFalse("float -> boolean", Classes.isAssignable(Float.class, Boolean.TYPE, true));

		// test double conversions
		assertFalse("double -> char", Classes.isAssignable(Double.class, Character.TYPE, true));
		assertFalse("double -> byte", Classes.isAssignable(Double.class, Byte.TYPE, true));
		assertFalse("double -> short", Classes.isAssignable(Double.class, Short.TYPE, true));
		assertFalse("double -> int", Classes.isAssignable(Double.class, Integer.TYPE, true));
		assertFalse("double -> long", Classes.isAssignable(Double.class, Long.TYPE, true));
		assertFalse("double -> float", Classes.isAssignable(Double.class, Float.TYPE, true));
		assertTrue("double -> double", Classes.isAssignable(Double.class, Double.TYPE, true));
		assertFalse("double -> boolean", Classes.isAssignable(Double.class, Boolean.TYPE, true));

		// test boolean conversions
		assertFalse("boolean -> char", Classes.isAssignable(Boolean.class, Character.TYPE, true));
		assertFalse("boolean -> byte", Classes.isAssignable(Boolean.class, Byte.TYPE, true));
		assertFalse("boolean -> short", Classes.isAssignable(Boolean.class, Short.TYPE, true));
		assertFalse("boolean -> int", Classes.isAssignable(Boolean.class, Integer.TYPE, true));
		assertFalse("boolean -> long", Classes.isAssignable(Boolean.class, Long.TYPE, true));
		assertFalse("boolean -> float", Classes.isAssignable(Boolean.class, Float.TYPE, true));
		assertFalse("boolean -> double", Classes.isAssignable(Boolean.class, Double.TYPE, true));
		assertTrue("boolean -> boolean", Classes.isAssignable(Boolean.class, Boolean.TYPE, true));
	}

	@Test
	public void testIsPrimitiveOrWrapper() {

		// test primitive wrapper classes
		assertTrue("Boolean.class", Classes.isPrimitiveOrWrapper(Boolean.class));
		assertTrue("Byte.class", Classes.isPrimitiveOrWrapper(Byte.class));
		assertTrue("Character.class", Classes.isPrimitiveOrWrapper(Character.class));
		assertTrue("Short.class", Classes.isPrimitiveOrWrapper(Short.class));
		assertTrue("Integer.class", Classes.isPrimitiveOrWrapper(Integer.class));
		assertTrue("Long.class", Classes.isPrimitiveOrWrapper(Long.class));
		assertTrue("Double.class", Classes.isPrimitiveOrWrapper(Double.class));
		assertTrue("Float.class", Classes.isPrimitiveOrWrapper(Float.class));
		assertTrue("Void.class", Classes.isPrimitiveOrWrapper(Void.class));

		// test primitive classes
		assertTrue("boolean", Classes.isPrimitiveOrWrapper(Boolean.TYPE));
		assertTrue("byte", Classes.isPrimitiveOrWrapper(Byte.TYPE));
		assertTrue("char", Classes.isPrimitiveOrWrapper(Character.TYPE));
		assertTrue("short", Classes.isPrimitiveOrWrapper(Short.TYPE));
		assertTrue("int", Classes.isPrimitiveOrWrapper(Integer.TYPE));
		assertTrue("long", Classes.isPrimitiveOrWrapper(Long.TYPE));
		assertTrue("double", Classes.isPrimitiveOrWrapper(Double.TYPE));
		assertTrue("float", Classes.isPrimitiveOrWrapper(Float.TYPE));
		assertTrue("Void.TYPE", Classes.isPrimitiveOrWrapper(Void.TYPE));

		// others
		assertFalse("null", Classes.isPrimitiveOrWrapper(null));
		assertFalse("String.class", Classes.isPrimitiveOrWrapper(String.class));
		assertFalse("this.getClass()", Classes.isPrimitiveOrWrapper(this.getClass()));
	}

	@Test
	public void testIsPrimitiveWrapper() {

		// test primitive wrapper classes
		assertTrue("Boolean.class", Classes.isPrimitiveWrapper(Boolean.class));
		assertTrue("Byte.class", Classes.isPrimitiveWrapper(Byte.class));
		assertTrue("Character.class", Classes.isPrimitiveWrapper(Character.class));
		assertTrue("Short.class", Classes.isPrimitiveWrapper(Short.class));
		assertTrue("Integer.class", Classes.isPrimitiveWrapper(Integer.class));
		assertTrue("Long.class", Classes.isPrimitiveWrapper(Long.class));
		assertTrue("Double.class", Classes.isPrimitiveWrapper(Double.class));
		assertTrue("Float.class", Classes.isPrimitiveWrapper(Float.class));
		assertTrue("Void.class", Classes.isPrimitiveWrapper(Void.class));

		// test primitive classes
		assertFalse("boolean", Classes.isPrimitiveWrapper(Boolean.TYPE));
		assertFalse("byte", Classes.isPrimitiveWrapper(Byte.TYPE));
		assertFalse("char", Classes.isPrimitiveWrapper(Character.TYPE));
		assertFalse("short", Classes.isPrimitiveWrapper(Short.TYPE));
		assertFalse("int", Classes.isPrimitiveWrapper(Integer.TYPE));
		assertFalse("long", Classes.isPrimitiveWrapper(Long.TYPE));
		assertFalse("double", Classes.isPrimitiveWrapper(Double.TYPE));
		assertFalse("float", Classes.isPrimitiveWrapper(Float.TYPE));

		// others
		assertFalse("null", Classes.isPrimitiveWrapper(null));
		assertFalse("Void.TYPE", Classes.isPrimitiveWrapper(Void.TYPE));
		assertFalse("String.class", Classes.isPrimitiveWrapper(String.class));
		assertFalse("this.getClass()", Classes.isPrimitiveWrapper(this.getClass()));
	}

	@Test
	public void testPrimitiveToWrapper() {

		// test primitive classes
		assertEquals("boolean -> Boolean.class", Boolean.class, Classes.primitiveToWrapper(Boolean.TYPE));
		assertEquals("byte -> Byte.class", Byte.class, Classes.primitiveToWrapper(Byte.TYPE));
		assertEquals("char -> Character.class", Character.class, Classes.primitiveToWrapper(Character.TYPE));
		assertEquals("short -> Short.class", Short.class, Classes.primitiveToWrapper(Short.TYPE));
		assertEquals("int -> Integer.class", Integer.class, Classes.primitiveToWrapper(Integer.TYPE));
		assertEquals("long -> Long.class", Long.class, Classes.primitiveToWrapper(Long.TYPE));
		assertEquals("double -> Double.class", Double.class, Classes.primitiveToWrapper(Double.TYPE));
		assertEquals("float -> Float.class", Float.class, Classes.primitiveToWrapper(Float.TYPE));
		assertEquals("Void -> Void.class", Void.class, Classes.primitiveToWrapper(Void.TYPE));

		// test a few other classes
		assertEquals("String.class -> String.class", String.class, Classes.primitiveToWrapper(String.class));
		assertEquals("Classes.class -> Classes.class", Classes.class, Classes.primitiveToWrapper(Classes.class));

		// test null
		assertNull("null -> null", Classes.primitiveToWrapper(null));
	}

	@Test
	public void testPrimitivesToWrappers() {
		// test null
		// assertNull("null -> null", Classes.primitivesToWrappers(null)); // generates warning
		assertNull("null -> null", Classes.primitivesToWrappers((Class<?>[])null)); // equivalent
																					// cast to avoid
																					// warning
		// Other possible casts for null
		assertTrue("empty -> empty", Arrays.equals(Arrays.EMPTY_CLASS_ARRAY, Classes.primitivesToWrappers()));
		final Class<?>[] castNull = Classes.primitivesToWrappers((Class<?>)null); // == new
																					// Class<?>[]{null}
		assertTrue("(Class<?>)null -> [null]", Arrays.equals(new Class<?>[] { null }, castNull));
		// test empty array is returned unchanged
		// TODO this is not documented
		assertArrayEquals("empty -> empty", Arrays.EMPTY_CLASS_ARRAY,
			Classes.primitivesToWrappers(Arrays.EMPTY_CLASS_ARRAY));

		// test an array of various classes
		final Class<?>[] primitives = new Class[] { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE,
				Long.TYPE, Double.TYPE, Float.TYPE, String.class, Classes.class };
		final Class<?>[] wrappers = Classes.primitivesToWrappers(primitives);

		for (int i = 0; i < primitives.length; i++) {
			// test each returned wrapper
			final Class<?> primitive = primitives[i];
			final Class<?> expectedWrapper = Classes.primitiveToWrapper(primitive);

			assertEquals(primitive + " -> " + expectedWrapper, expectedWrapper, wrappers[i]);
		}

		// test an array of no primitive classes
		final Class<?>[] noPrimitives = new Class[] { String.class, Classes.class, Void.TYPE };
		// This used to return the exact same array, but no longer does.
		Assert.assertNotSame("unmodified", noPrimitives, Classes.primitivesToWrappers(noPrimitives));
	}

	@Test
	public void testWrapperToPrimitive() {
		// an array with classes to convert
		final Class<?>[] primitives = { Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE,
				Float.TYPE, Double.TYPE };
		for (final Class<?> primitive : primitives) {
			final Class<?> wrapperCls = Classes.primitiveToWrapper(primitive);
			assertFalse("Still primitive", wrapperCls.isPrimitive());
			assertEquals(wrapperCls + " -> " + primitive, primitive, Classes.wrapperToPrimitive(wrapperCls));
		}
	}

	@Test
	public void testWrapperToPrimitiveNoWrapper() {
		assertNull("Wrong result for non wrapper class", Classes.wrapperToPrimitive(String.class));
	}

	@Test
	public void testWrapperToPrimitiveNull() {
		assertNull("Wrong result for null class", Classes.wrapperToPrimitive(null));
	}

	@Test
	public void testWrappersToPrimitives() {
		// an array with classes to test
		final Class<?>[] classes = { Boolean.class, Byte.class, Character.class, Short.class, Integer.class,
				Long.class, Float.class, Double.class, String.class, Classes.class, null };

		final Class<?>[] primitives = Classes.wrappersToPrimitives(classes);
		// now test the result
		assertEquals("Wrong length of result array", classes.length, primitives.length);
		for (int i = 0; i < classes.length; i++) {
			final Class<?> expectedPrimitive = Classes.wrapperToPrimitive(classes[i]);
			assertEquals(classes[i] + " -> " + expectedPrimitive, expectedPrimitive, primitives[i]);
		}
	}

	@Test
	public void testWrappersToPrimitivesNull() {
		// assertNull("Wrong result for null input", Classes.wrappersToPrimitives(null)); //
		// generates warning
		assertNull("Wrong result for null input", Classes.wrappersToPrimitives((Class<?>[])null)); // equivalent
																									// cast
		// Other possible casts for null
		assertTrue("empty -> empty", Arrays.equals(Arrays.EMPTY_CLASS_ARRAY, Classes.wrappersToPrimitives()));
		final Class<?>[] castNull = Classes.wrappersToPrimitives((Class<?>)null); // == new
																					// Class<?>[]{null}
		assertTrue("(Class<?>)null -> [null]", Arrays.equals(new Class<?>[] { null }, castNull));
	}

	@Test
	public void testWrappersToPrimitivesEmpty() {
		final Class<?>[] empty = new Class[0];
		assertArrayEquals("Wrong result for empty input", empty, Classes.wrappersToPrimitives(empty));
	}

	@Test
	public void testGetClassClassNotFound() throws Exception {
		assertGetClassThrowsClassNotFound("bool");
		assertGetClassThrowsClassNotFound("bool[]");
		assertGetClassThrowsClassNotFound("integer[]");
	}

	@Test
	public void testGetClassInvalidArguments() throws Exception {
		assertGetClassThrowsNullPointerException(null);
		assertGetClassThrowsClassNotFound("[][][]");
		assertGetClassThrowsClassNotFound("[[]");
		assertGetClassThrowsClassNotFound("[");
		assertGetClassThrowsClassNotFound("java.lang.String][");
		assertGetClassThrowsClassNotFound(".hello.world");
		assertGetClassThrowsClassNotFound("hello..world");
	}

	@Test
	public void testWithInterleavingWhitespace() throws ClassNotFoundException {
		assertEquals(int[].class, Classes.getClass(" int [ ] "));
		assertEquals(long[].class, Classes.getClass("\rlong\t[\n]\r"));
		assertEquals(short[].class, Classes.getClass("\tshort                \t\t[]"));
		assertEquals(byte[].class, Classes.getClass("byte[\t\t\n\r]   "));
	}

	@Test
	public void testGetInnerClass() throws ClassNotFoundException {
		assertEquals(Inner.DeeplyNested.class,
			Classes.getClass(ClassesTest.class.getName() + ".Inner.DeeplyNested"));
		assertEquals(Inner.DeeplyNested.class,
			Classes.getClass(ClassesTest.class.getName() + ".Inner$DeeplyNested"));
		assertEquals(Inner.DeeplyNested.class,
			Classes.getClass(ClassesTest.class.getName() + "$Inner$DeeplyNested"));
		assertEquals(Inner.DeeplyNested.class,
			Classes.getClass(ClassesTest.class.getName() + "$Inner.DeeplyNested"));
	}

	@Test
	public void testGetClassByNormalNameArrays() throws ClassNotFoundException {
		assertEquals(int[].class, Classes.getClass("int[]"));
		assertEquals(long[].class, Classes.getClass("long[]"));
		assertEquals(short[].class, Classes.getClass("short[]"));
		assertEquals(byte[].class, Classes.getClass("byte[]"));
		assertEquals(char[].class, Classes.getClass("char[]"));
		assertEquals(float[].class, Classes.getClass("float[]"));
		assertEquals(double[].class, Classes.getClass("double[]"));
		assertEquals(boolean[].class, Classes.getClass("boolean[]"));
		assertEquals(String[].class, Classes.getClass("java.lang.String[]"));
		assertEquals(java.util.Map.Entry[].class, Classes.getClass("java.util.Map.Entry[]"));
		assertEquals(java.util.Map.Entry[].class, Classes.getClass("java.util.Map$Entry[]"));
		assertEquals(java.util.Map.Entry[].class, Classes.getClass("[Ljava.util.Map.Entry;"));
		assertEquals(java.util.Map.Entry[].class, Classes.getClass("[Ljava.util.Map$Entry;"));
	}

	@Test
	public void testGetClassByNormalNameArrays2D() throws ClassNotFoundException {
		assertEquals(int[][].class, Classes.getClass("int[][]"));
		assertEquals(long[][].class, Classes.getClass("long[][]"));
		assertEquals(short[][].class, Classes.getClass("short[][]"));
		assertEquals(byte[][].class, Classes.getClass("byte[][]"));
		assertEquals(char[][].class, Classes.getClass("char[][]"));
		assertEquals(float[][].class, Classes.getClass("float[][]"));
		assertEquals(double[][].class, Classes.getClass("double[][]"));
		assertEquals(boolean[][].class, Classes.getClass("boolean[][]"));
		assertEquals(String[][].class, Classes.getClass("java.lang.String[][]"));
	}

	@Test
	public void testGetClassWithArrayClasses2D() throws Exception {
		assertGetClassReturnsClass(String[][].class);
		assertGetClassReturnsClass(int[][].class);
		assertGetClassReturnsClass(long[][].class);
		assertGetClassReturnsClass(short[][].class);
		assertGetClassReturnsClass(byte[][].class);
		assertGetClassReturnsClass(char[][].class);
		assertGetClassReturnsClass(float[][].class);
		assertGetClassReturnsClass(double[][].class);
		assertGetClassReturnsClass(boolean[][].class);
	}

	@Test
	public void testGetClassWithArrayClasses() throws Exception {
		assertGetClassReturnsClass(String[].class);
		assertGetClassReturnsClass(int[].class);
		assertGetClassReturnsClass(long[].class);
		assertGetClassReturnsClass(short[].class);
		assertGetClassReturnsClass(byte[].class);
		assertGetClassReturnsClass(char[].class);
		assertGetClassReturnsClass(float[].class);
		assertGetClassReturnsClass(double[].class);
		assertGetClassReturnsClass(boolean[].class);
	}

	@Test
	public void testGetClassRawPrimitives() throws ClassNotFoundException {
		assertEquals(int.class, Classes.getClass("int"));
		assertEquals(long.class, Classes.getClass("long"));
		assertEquals(short.class, Classes.getClass("short"));
		assertEquals(byte.class, Classes.getClass("byte"));
		assertEquals(char.class, Classes.getClass("char"));
		assertEquals(float.class, Classes.getClass("float"));
		assertEquals(double.class, Classes.getClass("double"));
		assertEquals(boolean.class, Classes.getClass("boolean"));
	}

	private void assertGetClassReturnsClass(final Class<?> c) throws Exception {
		assertEquals(c, Classes.getClass(c.getName()));
	}

	private void assertGetClassThrowsException(final String className, final Class<?> exceptionType) throws Exception {
		try {
			Classes.getClass(className);
			fail("Classes.getClass() should fail with an exception of type " + exceptionType.getName()
					+ " when given class name \"" + className + "\".");
		}
		catch (final Exception e) {
			assertTrue(exceptionType.isAssignableFrom(e.getClass()));
		}
	}

	private void assertGetClassThrowsNullPointerException(final String className) throws Exception {
		assertGetClassThrowsException(className, NullPointerException.class);
	}

	private void assertGetClassThrowsClassNotFound(final String className) throws Exception {
		assertGetClassThrowsException(className, ClassNotFoundException.class);
	}

	// Show the Java bug: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4071957
	// We may have to delete this if a JDK fixes the bug.
	@Test
	public void testShowJavaBug() throws Exception {
		// Tests with Collections$UnmodifiableSet
		final Set<?> set = Collections.unmodifiableSet(new HashSet<Object>());
		final Method isEmptyMethod = set.getClass().getMethod("isEmpty", new Class[0]);
		try {
			isEmptyMethod.invoke(set, new Object[0]);
			fail("Failed to throw IllegalAccessException as expected");
		}
		catch (final IllegalAccessException iae) {
			// expected
		}
	}

	@Test
	public void testGetPublicMethod() throws Exception {
		// Tests with Collections$UnmodifiableSet
		final Set<?> set = Collections.unmodifiableSet(new HashSet<Object>());
		final Method isEmptyMethod = Classes.getPublicMethod(set.getClass(), "isEmpty", new Class[0]);
		assertTrue(Modifier.isPublic(isEmptyMethod.getDeclaringClass().getModifiers()));

		try {
			isEmptyMethod.invoke(set, new Object[0]);
		}
		catch (final java.lang.IllegalAccessException iae) {
			fail("Should not have thrown IllegalAccessException");
		}

		// Tests with a public Class
		final Method toStringMethod = Classes.getPublicMethod(Object.class, "toString", new Class[0]);
		assertEquals(Object.class.getMethod("toString", new Class[0]), toStringMethod);
	}

	@Test
	public void testToClass_object() {
		// assertNull(Classes.toClass(null)); // generates warning
		assertNull(Classes.toClass((Object[])null)); // equivalent explicit cast

		// Additional varargs tests
		assertTrue("empty -> empty", Arrays.equals(Arrays.EMPTY_CLASS_ARRAY, Classes.toClass()));
		final Class<?>[] castNull = Classes.toClass((Object)null); // == new Object[]{null}
		assertTrue("(Object)null -> [null]", Arrays.equals(new Class<?>[] { null }, castNull));

		Assert.assertSame(Arrays.EMPTY_CLASS_ARRAY, Classes.toClass(Arrays.EMPTY_OBJECT_ARRAY));

		assertTrue(Arrays.equals(new Class[] { String.class, Integer.class, Double.class },
			Classes.toClass(new Object[] { "Test", Integer.valueOf(1), Double.valueOf(99d) })));

		assertTrue(Arrays.equals(new Class[] { String.class, null, Double.class },
			Classes.toClass(new Object[] { "Test", null, Double.valueOf(99d) })));
	}

	@Test
	public void test_getPackageCanonicalName_Object() {
		assertEquals("<null>", Classes.getPackageCanonicalName(null, "<null>"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(new ClassesTest(), "<null>"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(new ClassesTest[0], "<null>"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(new ClassesTest[0][0], "<null>"));
		assertEquals("", Classes.getPackageCanonicalName(new int[0], "<null>"));
		assertEquals("", Classes.getPackageCanonicalName(new int[0][0], "<null>"));

		// Inner types
		class Named extends Object {
		}
		assertEquals(ClassesTest.class.getPackage().getName(), Classes.getPackageCanonicalName(new Object() {
		}, "<null>"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(new Named(), "<null>"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(new Inner(), "<null>"));
	}

	@Test
	public void test_getPackageCanonicalName_Class() {
		assertEquals(ClassesTest.class.getPackage().getName(), Classes.getPackageCanonicalName(Classes.class));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(ClassesTest[].class));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(ClassesTest[][].class));
		assertEquals("", Classes.getPackageCanonicalName(int[].class));
		assertEquals("", Classes.getPackageCanonicalName(int[][].class));

		// Inner types
		class Named extends Object {
		}
		assertEquals(ClassesTest.class.getPackage().getName(), Classes.getPackageCanonicalName(new Object() {
		}.getClass()));
		assertEquals(ClassesTest.class.getPackage().getName(), Classes.getPackageCanonicalName(Named.class));
		assertEquals(ClassesTest.class.getPackage().getName(), Classes.getPackageCanonicalName(Inner.class));
	}

	@Test
	public void test_getPackageCanonicalName_String() {
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(Classes.class.getName()));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName("[L" + Classes.class.getName() + ";"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName("[[L" + Classes.class.getName() + ";"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(ClassesTest.class.getName() + "[]"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(ClassesTest.class.getName() + "[][]"));
		assertEquals("", Classes.getPackageCanonicalName("[I"));
		assertEquals("", Classes.getPackageCanonicalName("[[I"));
		assertEquals("", Classes.getPackageCanonicalName("int[]"));
		assertEquals("", Classes.getPackageCanonicalName("int[][]"));

		// Inner types
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(ClassesTest.class.getName() + "$6"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(ClassesTest.class.getName() + "$5Named"));
		assertEquals(ClassesTest.class.getPackage().getName(),
			Classes.getPackageCanonicalName(ClassesTest.class.getName() + "$Inner"));
	}
}
