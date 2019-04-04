package panda.bean;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.bean.TestA.TestB;
import panda.lang.Arrays;
import panda.lang.Systems;
import panda.lang.reflect.Types;
import panda.log.Log;
import panda.log.Logs;


/**
 * JavaBeanHandlerTest
 */
@SuppressWarnings("unchecked")
public class JavaBeanHandlerTest {

	private static Log log = Logs.getLog(JavaBeanHandlerTest.class);

	private static Beans beans = new Beans();
	
	protected BeanHandler getBeanHandler(Class type) {
		return beans.getBeanHandler(type);
	}
	
	protected void testGetTypeStr(Object beanObject, String propertyName, Type expected) {
		log.debug("testGetTypeStr: " + beanObject.getClass().getName() + " - " + propertyName + " [ "
				+ expected + " ]");

		Type actual;
		try {
			BeanHandler bh = getBeanHandler(beanObject.getClass());
			actual = bh.getBeanType(beanObject, propertyName);
		}
		catch (RuntimeException e) {
			log.error("exception", e);
			throw e;
		}
		log.debug("expect: " + Types.getCastableClassName(expected));
		log.debug("actual: " + Types.getCastableClassName(actual));
		Assert.assertEquals(Types.getCastableClassName(expected), Types.getCastableClassName(actual));
	}

	
	protected void testGetType(Object beanObject, String propertyName, Type expected) {
		log.debug("testGetType: " + beanObject.getClass().getName() + " - " + propertyName + " [ "
				+ expected + " ]");

		Type actual;
		try {
			BeanHandler bh = getBeanHandler(beanObject.getClass());
			actual = bh.getBeanType(beanObject, propertyName);
		}
		catch (RuntimeException e) {
			log.error("exception", e);
			throw e;
		}
		Assert.assertEquals(expected, actual);
	}

	
	protected void testGetRawType(Object beanObject, String propertyName, Class expected) {
		log.debug("testGetRawType: " + beanObject.getClass().getName() + " - " + propertyName + " [ "
				+ expected + " ]");

		Type actual;
		try {
			BeanHandler bh = getBeanHandler(beanObject.getClass());
			actual = bh.getBeanType(beanObject, propertyName);
			actual = Types.getRawType(actual);
		}
		catch (RuntimeException e) {
			log.error("exception", e);
			throw e;
		}
		Assert.assertEquals(expected, actual);
	}

	protected void testGetElemType(Object beanObject, String propertyName, Type expected) {
		log.debug("testGetElemType: " + beanObject.getClass().getName() + " - " + propertyName + " [ "
				+ expected + " ]");

		Type actual;
		try {
			BeanHandler bh = getBeanHandler(beanObject.getClass());
			Type pt = bh.getBeanType(beanObject, propertyName);
			actual = Types.getArrayElementType(pt);
		}
		catch (RuntimeException e) {
			log.error("exception", e);
			throw e;
		}
		Assert.assertEquals(expected, actual);
	}

	protected void testSetGetValue(Object beanObject, String propertyName, Object expected) {
		log.debug("testSetGetValue: " + beanObject.getClass().getName() + " - " + propertyName
				+ " - " + expected + " [ " + expected.getClass().getName() + " ]");

		Object actual;
		try {
			BeanHandler bh = getBeanHandler(beanObject.getClass());
			bh.setBeanValue(beanObject, propertyName, expected);
			actual = bh.getBeanValue(beanObject, propertyName);
		}
		catch (RuntimeException e) {
			log.error("exception", e);
			throw e;
		}
		Assert.assertEquals(expected, actual);
	}

	/**
	 * testCreateObject
	 */
	@Test
	public void testCreateObject() {
		TestA actual;
		try {
			BeanHandler bh = getBeanHandler(TestA.class);
			actual = (TestA) bh.createObject();
		}
		catch (RuntimeException e) {
			log.error("exception", e);
			throw e;
		}
		Assert.assertNotNull(actual);
	}

	@Test
	public void testGetGenericType() {
		TestH h = new TestH();
		testGetRawType(h, "obj", TestA.class);
		testGetElemType(h, "lst", TestA.class);
	}
	
	/**
	 * testGetPropertyType
	 */
	@Test
	public void testGetPropertyType() {
		TestA a = new TestA();

		testGetType(a, "boolField", boolean.class);
		testGetType(a, "byteField", byte.class);
		testGetType(a, "charField", char.class);
		testGetType(a, "doubleField", double.class);
		testGetType(a, "floatField", float.class);
		testGetType(a, "intField", int.class);
		testGetType(a, "longField", long.class);
		testGetType(a, "shortField", short.class);

		testGetType(a, "boolArray", boolean[].class);
		testGetType(a, "byteArray", byte[].class);
		testGetType(a, "charArray", char[].class);
		testGetType(a, "doubleArray", double[].class);
		testGetType(a, "floatArray", float[].class);
		testGetType(a, "intArray", int[].class);
		testGetType(a, "longArray", long[].class);
		testGetType(a, "shortArray", short[].class);

		testGetType(a, "boolWrap", Boolean.class);
		testGetType(a, "byteWrap", Byte.class);
		testGetType(a, "charWrap", Character.class);
		testGetType(a, "doubleWrap", Double.class);
		testGetType(a, "floatWrap", Float.class);
		testGetType(a, "intWrap", Integer.class);
		testGetType(a, "longWrap", Long.class);
		testGetType(a, "shortWrap", Short.class);

		testGetType(a, "stringField", String.class);
		testGetType(a, "stringArray", String[].class);
		testGetType(a, "stringList", Types.paramTypeOf(List.class, String.class));

		testGetType(a, "dateField", Date.class);
		testGetType(a, "dateArray", Date[].class);
		testGetType(a, "dateMap", Types.paramTypeOf(Map.class, String.class, Date.class));

		testGetType(a, "dateMapList", 
			Types.paramTypeOf(List.class, 
				Types.paramTypeOfOwner(
			null, Map.class, String.class, Date.class)));

		testGetType(a, "numExtendMap", 
			Types.paramTypeOf(Map.class, 
				Types.subTypeOf(Object.class), 
				Types.subTypeOf(Number.class)));

		if (Systems.JAVA_MAJOR_VERSION == 6) {
			testGetType(a, "intArrayMap", 
				Types.paramTypeOf(Map.class, 
					Types.subTypeOf(Object.class), 
					Types.arrayTypeOf(int.class)));
		}
		else if (Systems.JAVA_MAJOR_VERSION == 7) {
			//FIXME
			testGetTypeStr(a, "intArrayMap", 
				Types.paramTypeOf(Map.class, 
					Types.subTypeOf(Object.class), 
					Types.arrayTypeOf(int.class)));
		}

		testGetType(a, "testB", TestB.class);

		testGetType(a, "testB.boolField", boolean.class);
		testGetType(a, "testB.byteField", byte.class);
		testGetType(a, "testB.charField", char.class);
		testGetType(a, "testB.doubleField", double.class);
		testGetType(a, "testB.floatField", float.class);
		testGetType(a, "testB.intField", int.class);
		testGetType(a, "testB.longField", long.class);
		testGetType(a, "testB.shortField", short.class);

		testGetType(a, "testB.boolArray", boolean[].class);
		testGetType(a, "testB.byteArray", byte[].class);
		testGetType(a, "testB.charArray", char[].class);
		testGetType(a, "testB.doubleArray", double[].class);
		testGetType(a, "testB.floatArray", float[].class);
		testGetType(a, "testB.intArray", int[].class);
		testGetType(a, "testB.longArray", long[].class);
		testGetType(a, "testB.shortArray", short[].class);

		testGetType(a, "testB.boolWrap", Boolean.class);
		testGetType(a, "testB.byteWrap", Byte.class);
		testGetType(a, "testB.charWrap", Character.class);
		testGetType(a, "testB.doubleWrap", Double.class);
		testGetType(a, "testB.floatWrap", Float.class);
		testGetType(a, "testB.intWrap", Integer.class);
		testGetType(a, "testB.longWrap", Long.class);
		testGetType(a, "testB.shortWrap", Short.class);

		testGetType(a, "testB.stringField", String.class);
		testGetType(a, "testB.dateField", Date.class);
		testGetType(a, "testB.stringArray", String[].class);
		testGetType(a, "testB.dateArray", Date[].class);

		testGetType(a, "testB.testA", TestA.class);
	}

	/**
	 * testSetGetPropertyValue
	 */
	@Test
	public void testSetGetPropertyValue() {
		TestA a = new TestA();

		testSetGetValue(a, "boolField", true);
		testSetGetValue(a, "byteField", (byte)10);
		testSetGetValue(a, "charField", 'c');
		testSetGetValue(a, "doubleField", (double)101.001);
		testSetGetValue(a, "floatField", (float)102.102);
		testSetGetValue(a, "intField", (int)1000);
		testSetGetValue(a, "longField", (long)10000);
		testSetGetValue(a, "shortField", (short)100);

		testSetGetValue(a, "boolArray", new boolean[] { true, false });
		testSetGetValue(a, "byteArray", new byte[] { 1, 2 });
		testSetGetValue(a, "charArray", new char[] { 'a', 'b' });
		testSetGetValue(a, "doubleArray", new double[] { 1.1, 2.2 });
		testSetGetValue(a, "floatArray", new float[] { 3.3F, 4.4F });
		testSetGetValue(a, "shortArray", new short[] { 11, 12, 13 });
		testSetGetValue(a, "longArray", new long[] { 1001L, 1002L });
		testSetGetValue(a, "intArray", new int[] { 101, 102, 103 });
		testSetGetValue(a, "intArray:0", 21);
		testSetGetValue(a, "intArray(1)", 22);
		testSetGetValue(a, "intArray[2]", 23);

		testSetGetValue(a, "boolWrap", Boolean.TRUE);
		testSetGetValue(a, "byteWrap", new Byte((byte)10));
		testSetGetValue(a, "charWrap", new Character('C'));
		testSetGetValue(a, "doubleWrap", new Double(101.101));
		testSetGetValue(a, "floatWrap", new Float(102.102));
		testSetGetValue(a, "intWrap", new Integer(1000));
		testSetGetValue(a, "longWrap", new Long(10000));
		testSetGetValue(a, "shortWrap", new Short((short)100));

		testSetGetValue(a, "stringField", "String");
		testSetGetValue(a, "stringArray", new String[] { "abc", "efd" });
		testSetGetValue(a, "stringList", Arrays.toList(new String[] { "xxx", "yyy", "zzz" }));
		testSetGetValue(a, "stringList:0", "z");
		testSetGetValue(a, "stringList(1)", "y");
		testSetGetValue(a, "stringList[2]", "x");
		testSetGetValue(a, "stringList[10]", "a");

		testSetGetValue(a, "dateField", Calendar.getInstance().getTime());
		testSetGetValue(a, "dateArray", new Date[] { Calendar.getInstance().getTime() });
		Map<String, Date> m = new HashMap<String, Date>();
		m.put("1", new Date(1));
		m.put("2", new Date(2));
		testSetGetValue(a, "dateMap", m);

		testSetGetValue(a, "testB", new TestB());
	}

	/**
	 * testSetGetPropertyValue02
	 */
	@Test
	public void testSetGetPropertyValue02() {
		TestA a = new TestA();

		testSetGetValue(a, "testB.boolField", true);
		testSetGetValue(a, "testB.byteField", (byte)10);
		testSetGetValue(a, "testB.charField", 'c');
		testSetGetValue(a, "testB.doubleField", (double)101.001);
		testSetGetValue(a, "testB.floatField", (float)102.102);
		testSetGetValue(a, "testB.intField", (int)1000);
		testSetGetValue(a, "testB.longField", (long)10000);
		testSetGetValue(a, "testB.shortField", (short)100);

		testSetGetValue(a, "testB.boolArray", new boolean[] { true, false });
		testSetGetValue(a, "testB.byteArray", new byte[] { 1, 2 });
		testSetGetValue(a, "testB.charArray", new char[] { 'a', 'b' });
		testSetGetValue(a, "testB.doubleArray", new double[] { 1.1, 2.2 });
		testSetGetValue(a, "testB.floatArray", new float[] { 3.3F, 4.4F });
		testSetGetValue(a, "testB.intArray", new int[] { 101, 102 });
		testSetGetValue(a, "testB.longArray", new long[] { 1001L, 1002L });
		testSetGetValue(a, "testB.shortArray", new short[] { 11, 12 });

		testSetGetValue(a, "testB.boolWrap", Boolean.TRUE);
		testSetGetValue(a, "testB.byteWrap", new Byte((byte)10));
		testSetGetValue(a, "testB.charWrap", new Character('C'));
		testSetGetValue(a, "testB.doubleWrap", new Double(101.101));
		testSetGetValue(a, "testB.floatWrap", new Float(102.102));
		testSetGetValue(a, "testB.intWrap", new Integer(1000));
		testSetGetValue(a, "testB.longWrap", new Long(10000));
		testSetGetValue(a, "testB.shortWrap", new Short((short)100));

		testSetGetValue(a, "testB.stringField", "String");
		testSetGetValue(a, "testB.dateField", Calendar.getInstance().getTime());
		testSetGetValue(a, "testB.stringArray", new String[] { "abc", "efd" });
		testSetGetValue(a, "testB.dateArray", new Date[] { Calendar.getInstance().getTime() });

		testSetGetValue(a, "testB.testA", new TestA());
	}

	/**
	 * testGetPropertyValueOnly
	 */
	@Test
	public void testGetPropertyValueOnly() {
		String expected = "test getField ";
		Object actual;
		try {
			TestA testA = new TestA();

			BeanHandler bh = getBeanHandler(testA.getClass());

			testA.getField = expected;

			actual = bh.getBeanValue(testA, "getField");
		}
		catch (RuntimeException e) {
			log.error("exception", e);
			throw e;
		}
		Assert.assertEquals(expected, actual);
	}

	/**
	 * testSetPropertyValueOnly
	 */
	@Test
	public void testSetPropertyValueOnly() {
		String expected = "test getField ";
		Object actual;
		try {
			TestA testA = new TestA();

			BeanHandler bh = getBeanHandler(testA.getClass());

			bh.setBeanValue(testA, "setField", expected);
			
			actual = testA.setField;
		}
		catch (RuntimeException e) {
			log.error("exception", e);
			throw e;
		}
		Assert.assertEquals(expected, actual);
	}
}
