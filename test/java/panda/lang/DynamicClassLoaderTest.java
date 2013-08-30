package panda.lang;

import junit.framework.TestCase;
import panda.io.Streams;
import panda.lang.Classes;
import panda.lang.DynamicClassLoader;

/**
 * DynamicClassLoaderTest
 */
public class DynamicClassLoaderTest extends TestCase {
	private Class loadClass(DynamicClassLoader loader, String className, String sourceName) throws Exception {
		String source = Streams.toString(this.getClass().getResourceAsStream(sourceName));
		
		className = this.getClass().getPackage().getName() + "." + className;
		loader.defineClass(className, source);
		
		Class c = loader.loadClass(className);
		assertNotNull(c);
		assertEquals(className, c.getName());
		
		return c;
	}

	private void loadDynamicTest(DynamicClassLoader loader) throws Exception {
		Class c = loadClass(loader, "DynamicTest", "DynamicTest.java.txt");
		Object o = c.newInstance();
		assertEquals("1", o.toString());

		String classNameI = c.getName() + "$PublicInner"; 
		Class ci = Classes.getClass(loader, classNameI);
		assertNotNull(ci);
		assertEquals(classNameI, ci.getName());
	}

	private void loadDynamicTest2(DynamicClassLoader loader) throws Exception {
		Class c = loadClass(loader, "DynamicTest2", "DynamicTest2.java.txt");
		Object o = c.newInstance();
		assertEquals("2", o.toString());

		String classNameI = c.getName() + "$PublicInner"; 
		Class ci = Classes.getClass(loader, classNameI);
		assertNotNull(ci);
		assertEquals(classNameI, ci.getName());
	}
	
	private void loadDynamicTestChild(DynamicClassLoader loader) throws Exception {
		Class c = loadClass(loader, "DynamicTestChild", "DynamicTestChild.java.txt");

		Object o = c.newInstance();
		assertEquals("child", o.toString());
	}

	/**
	 * test simple
	 * @throws Exception if an error occurs
	 */
	public void testSimple() throws Exception {
		DynamicClassLoader loader = new DynamicClassLoader();
		loadDynamicTest(loader);
	}
	
	/**
	 * test recompile
	 * @throws Exception if an error occurs
	 */
	public void testRecompile() throws Exception {
		DynamicClassLoader loader = new DynamicClassLoader();

		loadDynamicTest(loader);
		
		Class c = loadClass(loader, "DynamicTest", "DynamicTest.java.2.txt");

		String classNameI = c.getName() + "$PublicInner"; 
		Class ci = Classes.getClass(loader, classNameI);
		assertNotNull(ci);
		assertEquals(classNameI, ci.getName());

		Object o = c.newInstance();
		assertEquals("-1", o.toString());
	}

	/**
	 * test two class
	 * @throws Exception if an error occurs
	 */
	public void testTwoClass() throws Exception {
		DynamicClassLoader loader = new DynamicClassLoader();

		loadDynamicTest(loader);
		loadDynamicTest2(loader);
	}
	
	/**
	 * test inherit
	 * @throws Exception if an error occurs
	 */
	public void testInherit() throws Exception {
		DynamicClassLoader loader = new DynamicClassLoader();

		try {
			loadDynamicTestChild(loader);
			fail("loadDynamicTestChild should failed!");
		}
		catch (Throwable e) {
		}
		
		loadDynamicTest(loader);
		loadDynamicTestChild(loader);
	}
	
}
