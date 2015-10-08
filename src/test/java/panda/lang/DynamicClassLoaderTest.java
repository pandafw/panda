package panda.lang;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import panda.io.Streams;

/**
 * DynamicClassLoaderTest
 */
public class DynamicClassLoaderTest {
	DynamicClassLoader loader;

	@Before
	public void setUp() {
		loader = new DynamicClassLoader();
	}

	@After
	public void tearDown() {
		if (loader != null) {
			loader.clear();
		}
	}
	
	private Class loadClass(DynamicClassLoader loader, String className, String sourceName) throws Exception {
		String source = Streams.toStringBom(this.getClass().getResourceAsStream(sourceName));
		
		className = this.getClass().getPackage().getName() + "." + className;
		loader.defineClass(className, source);
		
		Class c = loader.loadClass(className);
		Assert.assertNotNull(c);
		Assert.assertEquals(className, c.getName());
		
		return c;
	}

	private void loadDynamicTest(DynamicClassLoader loader) throws Exception {
		Class c = loadClass(loader, "DynamicTest", "DynamicTest.java.txt");
		Object o = c.newInstance();
		Assert.assertEquals("1", o.toString());

		String classNameI = c.getName() + "$PublicInner"; 
		Class ci = Classes.getClass(loader, classNameI);
		Assert.assertNotNull(ci);
		Assert.assertEquals(classNameI, ci.getName());
	}

	private void loadDynamicTest2(DynamicClassLoader loader) throws Exception {
		Class c = loadClass(loader, "DynamicTest2", "DynamicTest2.java.txt");
		Object o = c.newInstance();
		Assert.assertEquals("2", o.toString());

		String classNameI = c.getName() + "$PublicInner"; 
		Class ci = Classes.getClass(loader, classNameI);
		Assert.assertNotNull(ci);
		Assert.assertEquals(classNameI, ci.getName());
	}
	
	private void loadDynamicTestChild(DynamicClassLoader loader) throws Exception {
		Class c = loadClass(loader, "DynamicTestChild", "DynamicTestChild.java.txt");

		Object o = c.newInstance();
		Assert.assertEquals("child", o.toString());
	}

	/**
	 * test simple
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testSimple() throws Exception {
		loadDynamicTest(loader);
	}
	
	/**
	 * test recompile
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testRecompile() throws Exception {
		loadDynamicTest(loader);
		
		Class c = loadClass(loader, "DynamicTest", "DynamicTest.java.2.txt");

		String classNameI = c.getName() + "$PublicInner"; 
		Class ci = Classes.getClass(loader, classNameI);
		Assert.assertNotNull(ci);
		Assert.assertEquals(classNameI, ci.getName());

		Object o = c.newInstance();
		Assert.assertEquals("-1", o.toString());
	}

	/**
	 * test two class
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testTwoClass() throws Exception {
		loadDynamicTest(loader);
		loadDynamicTest2(loader);
	}
	
	/**
	 * test inherit
	 * @throws Exception if an error occurs
	 */
	@Test
	public void testInherit() throws Exception {
		try {
			loadDynamicTestChild(loader);
			Assert.fail("loadDynamicTestChild should failed!");
		}
		catch (Throwable e) {
		}
		
		loadDynamicTest(loader);
		loadDynamicTestChild(loader);
	}
	
}
