package panda.io;

import panda.io.Files;
import junit.framework.TestCase;

/**
 * @author yf.frank.wang@gmail.com
 */
public class FilesTest extends TestCase {

	/**
	 * test method: GetFileName
	 */
	public void testGetFileName() {
		assertEquals("1.js", Files.getFileName("/a/1.js"));
	}

	/**
	 * test method: GetFileNameExtension
	 */
	public void testGetFileNameExtension() {
		assertEquals("js", Files.getFileNameExtension("/a/1.js"));
		assertEquals("", Files.getFileNameExtension("/a/2"));
	}

	/**
	 * test method: GetContentTypeForName
	 */
	public void testGetContentTypeForName() {
		assertEquals("image/gif", Files.getContentTypeFor("/a/s1.gif"));
		System.out.println(Files.getContentTypeFor("1.js"));
		System.out.println(Files.getContentTypeFor("c.css"));
	}

	/**
	 * test method: isAbsolutePath
	 */
	public void testIsAbsolutePath() {
		assertTrue(Files.isAbsolutePath("/a/s1.gif"));
		assertTrue(Files.isAbsolutePath("C:/1.js"));
		assertTrue(Files.isAbsolutePath("C:\\1.js"));
		assertFalse(Files.isAbsolutePath("c.css"));
	}

}
