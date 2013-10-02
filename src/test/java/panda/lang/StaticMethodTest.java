package panda.lang;

import junit.framework.TestCase;

/**
 */
public class StaticMethodTest extends TestCase {
	public static class T1 {
		public static void print0(String text) {
			print("000" + text);
		}
		
		public static void print(String text) {
			System.out.println(text);
		}
	}
	
	public static class T2 extends T1 {
		public static void print(String text) {
			System.out.println("111" + text);
		}
	}
	
	/**
	 * @throws Exception if an error occurs
	 */
	public void testStatic01() throws Exception {
		T1.print0("test");
	}
	
	/**
	 * @throws Exception if an error occurs
	 */
	public void testStatic02() throws Exception {
		T2.print0("test");
	}

}
