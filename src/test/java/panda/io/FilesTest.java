package panda.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.junit.Assert;

import panda.io.filter.NameFileFilter;
import panda.io.filter.WildcardFileFilter;
import panda.lang.Charsets;
import panda.lang.Numbers;

/**
 * This is used to test Files for correctness.
 * 
 * @see Files
 */
@SuppressWarnings("serial")
public class FilesTest extends FileBasedTestCase {

	// Test data

	/**
	 * Size of test directory.
	 */
	private static final int TEST_DIRECTORY_SIZE = 0;

	/**
	 * Size of test directory.
	 */
	private static final BigInteger TEST_DIRECTORY_SIZE_BI = BigInteger.ZERO;

	/**
	 * Size (greater of zero) of test file.
	 */
	private static final BigInteger TEST_DIRECTORY_SIZE_GT_ZERO_BI = BigInteger.valueOf(100);

	/**
	 * List files recursively
	 */
	private static final ListDirectoryWalker LIST_WALKER = new ListDirectoryWalker();

	/** Delay in milliseconds to make sure test for "last modified date" are accurate */
	// private static final int LAST_MODIFIED_DELAY = 600;

	private final File testFile1;
	private final File testFile2;

	private final int testFile1Size;
	private final int testFile2Size;

	public FilesTest(final String name) {
		super(name);

		testFile1 = new File(getTestDirectory(), "file1-test.txt");
		testFile2 = new File(getTestDirectory(), "file1a-test.txt");

		testFile1Size = (int)testFile1.length();
		testFile2Size = (int)testFile2.length();
	}

	/** @see junit.framework.TestCase#setUp() */
	@Override
	protected void setUp() throws Exception {
		getTestDirectory().mkdirs();
		createFile(testFile1, testFile1Size);
		createFile(testFile2, testFile2Size);
		Files.deleteDir(getTestDirectory());
		getTestDirectory().mkdirs();
		createFile(testFile1, testFile1Size);
		createFile(testFile2, testFile2Size);
	}

	/** @see junit.framework.TestCase#tearDown() */
	@Override
	protected void tearDown() throws Exception {
		Files.deleteDir(getTestDirectory());
	}

	// -----------------------------------------------------------------------
	/**
	 * Tests the {@link Files#getFile(String...)} method.
	 */
	public void testGetFile() {
		final File expected_A = new File("src");
		final File expected_B = new File(expected_A, "main");
		final File expected_C = new File(expected_B, "java");
		assertEquals("A", expected_A, Files.getFile("src"));
		assertEquals("B", expected_B, Files.getFile("src", "main"));
		assertEquals("C", expected_C, Files.getFile("src", "main", "java"));
		try {
			Files.getFile((String[])null);
			fail("Expected NullPointerException");
		}
		catch (final NullPointerException e) {
			// expected
		}
	}

	/**
	 * Tests the {@link Files#getFile(File, String...)} method.
	 */
	public void testGetFile_Parent() {
		final File parent = new File("parent");
		final File expected_A = new File(parent, "src");
		final File expected_B = new File(expected_A, "main");
		final File expected_C = new File(expected_B, "java");
		assertEquals("A", expected_A, Files.getFile(parent, "src"));
		assertEquals("B", expected_B, Files.getFile(parent, "src", "main"));
		assertEquals("C", expected_C, Files.getFile(parent, "src", "main", "java"));
		try {
			Files.getFile(parent, (String[])null);
			fail("Expected NullPointerException");
		}
		catch (final NullPointerException e) {
			// expected
		}
		try {
			Files.getFile((File)null, "src");
			fail("Expected NullPointerException");
		}
		catch (final NullPointerException e) {
			// expected
		}
	}

	/**
	 * Tests the {@link Files#getTempDirectoryPath()} method.
	 */
	public void testGetTempDirectoryPath() {
		assertEquals(System.getProperty("java.io.tmpdir"), Files.getTempDirectoryPath());
	}

	/**
	 * Tests the {@link Files#getTempDirectory()} method.
	 */
	public void testGetTempDirectory() {
		final File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
		assertEquals(tempDirectory, Files.getTempDirectory());
	}

	/**
	 * Tests the {@link Files#getUserDirectoryPath()} method.
	 */
	public void testGetUserDirectoryPath() {
		assertEquals(System.getProperty("user.home"), Files.getUserDirectoryPath());
	}

	/**
	 * Tests the {@link Files#getUserDirectory()} method.
	 */
	public void testGetUserDirectory() {
		final File userDirectory = new File(System.getProperty("user.home"));
		assertEquals(userDirectory, Files.getUserDirectory());
	}

	// -----------------------------------------------------------------------
	public void test_openInputStream_exists() throws Exception {
		final File file = new File(getTestDirectory(), "test.txt");
		createLineBasedFile(file, new String[] { "Hello" });
		FileInputStream in = null;
		try {
			in = Files.openInputStream(file);
			assertEquals('H', in.read());
		}
		finally {
			Streams.safeClose(in);
		}
	}

	public void test_openInputStream_existsButIsDirectory() throws Exception {
		final File directory = new File(getTestDirectory(), "subdir");
		directory.mkdirs();
		FileInputStream in = null;
		try {
			in = Files.openInputStream(directory);
			fail();
		}
		catch (final IOException ioe) {
			// expected
		}
		finally {
			Streams.safeClose(in);
		}
	}

	public void test_openInputStream_notExists() throws Exception {
		final File directory = new File(getTestDirectory(), "test.txt");
		FileInputStream in = null;
		try {
			in = Files.openInputStream(directory);
			fail();
		}
		catch (final IOException ioe) {
			// expected
		}
		finally {
			Streams.safeClose(in);
		}
	}

	// -----------------------------------------------------------------------
	void openOutputStream_noParent(final boolean createFile) throws Exception {
		final File file = new File("test.txt");
		assertNull(file.getParentFile());
		try {
			if (createFile) {
				createLineBasedFile(file, new String[] { "Hello" });
			}
			FileOutputStream out = null;
			try {
				out = Files.openOutputStream(file);
				out.write(0);
			}
			finally {
				Streams.safeClose(out);
			}
			assertTrue(file.exists());
		}
		finally {
			if (file.delete() == false) {
				file.deleteOnExit();
			}
		}
	}

	public void test_openOutputStream_noParentCreateFile() throws Exception {
		openOutputStream_noParent(true);
	}

	public void test_openOutputStream_noParentNoFile() throws Exception {
		openOutputStream_noParent(false);
	}

	public void test_openOutputStream_exists() throws Exception {
		final File file = new File(getTestDirectory(), "test.txt");
		createLineBasedFile(file, new String[] { "Hello" });
		FileOutputStream out = null;
		try {
			out = Files.openOutputStream(file);
			out.write(0);
		}
		finally {
			Streams.safeClose(out);
		}
		assertTrue(file.exists());
	}

	public void test_openOutputStream_existsButIsDirectory() throws Exception {
		final File directory = new File(getTestDirectory(), "subdir");
		directory.mkdirs();
		FileOutputStream out = null;
		try {
			out = Files.openOutputStream(directory);
			fail();
		}
		catch (final IOException ioe) {
			// expected
		}
		finally {
			Streams.safeClose(out);
		}
	}

	public void test_openOutputStream_notExists() throws Exception {
		final File file = new File(getTestDirectory(), "a/test.txt");
		FileOutputStream out = null;
		try {
			out = Files.openOutputStream(file);
			out.write(0);
		}
		finally {
			Streams.safeClose(out);
		}
		assertTrue(file.exists());
	}

	public void test_openOutputStream_notExistsCannotCreate() throws Exception {
		// according to Wikipedia, most filing systems have a 256 limit on filename
		final String longStr = "abcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyz"
				+ "abcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyz"
				+ "abcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyz"
				+ "abcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyz"
				+ "abcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyz"
				+ "abcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyzabcdevwxyz"; // 300 chars
		final File file = new File(getTestDirectory(), "a/" + longStr + "/test.txt");
		FileOutputStream out = null;
		try {
			out = Files.openOutputStream(file);
			fail();
		}
		catch (final IOException ioe) {
			// expected
		}
		finally {
			Streams.safeClose(out);
		}
	}

	// -----------------------------------------------------------------------
	// byteCountToDisplaySize
	public void testByteCountToDisplaySizeBigInteger() {
		final BigInteger b1023 = BigInteger.valueOf(1023);
		final BigInteger b1025 = BigInteger.valueOf(1025);
		final BigInteger KB1 = BigInteger.valueOf(1024);
		final BigInteger MB1 = KB1.multiply(KB1);
		final BigInteger GB1 = MB1.multiply(KB1);
		final BigInteger GB2 = GB1.add(GB1);
		final BigInteger TB1 = GB1.multiply(KB1);
		final BigInteger PB1 = TB1.multiply(KB1);
		final BigInteger EB1 = PB1.multiply(KB1);
		assertEquals(Files.toDisplaySize(BigInteger.ZERO), "0 bytes");
		assertEquals(Files.toDisplaySize(BigInteger.ONE), "1 bytes");
		assertEquals(Files.toDisplaySize(b1023), "1023 bytes");
		assertEquals(Files.toDisplaySize(KB1), "1 KB");
		assertEquals(Files.toDisplaySize(b1025), "1 KB");
		assertEquals(Files.toDisplaySize(MB1.subtract(KB1)), "1023 KB");
		assertEquals(Files.toDisplaySize(MB1), "1 MB");
		assertEquals(Files.toDisplaySize(MB1.add(BigInteger.ONE)), "1 MB");
		assertEquals(Files.toDisplaySize(GB1.subtract(KB1.multiply(BigInteger.valueOf(12)))), "1023.99 MB");
		assertEquals(Files.toDisplaySize(GB1), "1 GB");
		assertEquals(Files.toDisplaySize(GB1.add(BigInteger.ONE)), "1 GB");
		assertEquals(Files.toDisplaySize(GB2), "2 GB");
		assertEquals(Files.toDisplaySize(GB2.subtract(BigInteger.ONE)), "1.99 GB");
		assertEquals(Files.toDisplaySize(TB1), "1 TB");
		assertEquals(Files.toDisplaySize(PB1), "1 PB");
		assertEquals(Files.toDisplaySize(EB1), "1 EB");
		assertEquals(Files.toDisplaySize(Long.MAX_VALUE), "8 EB");
		// Other MAX_VALUEs
		assertEquals(Files.toDisplaySize(BigInteger.valueOf(Character.MAX_VALUE)), "63.99 KB");
		assertEquals(Files.toDisplaySize(BigInteger.valueOf(Short.MAX_VALUE)), "31.99 KB");
		assertEquals(Files.toDisplaySize(BigInteger.valueOf(Integer.MAX_VALUE)), "1.99 GB");
	}

	public void testByteCountToDisplaySizeLong() {
		assertEquals(Files.toDisplaySize(0), "0 bytes");
		assertEquals(Files.toDisplaySize(1), "1 bytes");
		assertEquals(Files.toDisplaySize(1023), "1023 bytes");
		assertEquals(Files.toDisplaySize(1024), "1 KB");
		assertEquals(Files.toDisplaySize(1025), "1 KB");
		assertEquals(Files.toDisplaySize(1024 * 1023), "1023 KB");
		assertEquals(Files.toDisplaySize(1024 * 1024), "1 MB");
		assertEquals(Files.toDisplaySize(1024 * 1025), "1 MB");
		assertEquals(Files.toDisplaySize(1024 * 1024 * 1023), "1023 MB");
		assertEquals(Files.toDisplaySize(1024 * 1024 * 1024), "1 GB");
		assertEquals(Files.toDisplaySize(1024 * 1024 * 1025), "1 GB");
		assertEquals(Files.toDisplaySize(1024L * 1024 * 1024 * 2), "2 GB");
		assertEquals(Files.toDisplaySize(1024 * 1024 * 1024 * 2 - 1), "1.99 GB");
		assertEquals(Files.toDisplaySize(1024L * 1024 * 1024 * 1024), "1 TB");
		assertEquals(Files.toDisplaySize(1024L * 1024 * 1024 * 1024 * 1024), "1 PB");
		assertEquals(Files.toDisplaySize(1024L * 1024 * 1024 * 1024 * 1024 * 1024), "1 EB");
		assertEquals(Files.toDisplaySize(Long.MAX_VALUE), "8 EB");
		// Other MAX_VALUEs
		assertEquals(Files.toDisplaySize(Character.MAX_VALUE), "63.99 KB");
		assertEquals(Files.toDisplaySize(Short.MAX_VALUE), "31.99 KB");
		assertEquals(Files.toDisplaySize(Integer.MAX_VALUE), "1.99 GB");
	}

	// -----------------------------------------------------------------------
	// parseDisplaySize
	public void testParseDisplaySize() {
		final BigDecimal b1023 = BigDecimal.valueOf(1023);
		final BigDecimal b1025 = BigDecimal.valueOf(1025);
		final BigDecimal KB1 = BigDecimal.valueOf(1024);
		final BigDecimal MB1 = KB1.multiply(KB1);
		final BigDecimal GB1 = MB1.multiply(KB1);
		final BigDecimal GB2 = GB1.add(GB1);
		final BigDecimal TB1 = GB1.multiply(KB1);
		final BigDecimal PB1 = TB1.multiply(KB1);
		final BigDecimal EB1 = PB1.multiply(KB1);
		assertEquals(Files.parseDisplaySize("0 bytes"), BigDecimal.ZERO);
		assertEquals(Files.parseDisplaySize("1 bytes"), BigDecimal.ONE);
		assertEquals(Files.parseDisplaySize("1023 bytes"), b1023);
		assertEquals(Files.parseDisplaySize("1 KB"), KB1);
		assertEquals(Files.parseDisplaySize("1025 B"), b1025);
		assertEquals(Files.parseDisplaySize("1023 KB"), MB1.subtract(KB1));
		assertEquals(Files.parseDisplaySize("1 MB"), MB1);
		assertEquals(Files.parseDisplaySize("1023 MB"), GB1.subtract(MB1));
		assertEquals(Files.parseDisplaySize("1 GB"), GB1);
		assertEquals(Files.parseDisplaySize("2 GB"), GB2);
		assertEquals(Files.parseDisplaySize("1 TB"), TB1);
		assertEquals(Files.parseDisplaySize("1 PB"), PB1);
		assertEquals(Files.parseDisplaySize("1 EB"), EB1);
		assertEquals(Files.parseDisplaySize("7 EB"), EB1.multiply(BigDecimal.valueOf(7)));
		// Other MAX_VALUEs
		assertEquals(Files.parseDisplaySize("64 KB"), BigDecimal.valueOf(Character.MAX_VALUE).add(BigDecimal.ONE));
		assertEquals(Files.parseDisplaySize("32 KB"), BigDecimal.valueOf(Short.MAX_VALUE).add(BigDecimal.ONE));
		assertEquals(Files.parseDisplaySize("2 GB"), BigDecimal.valueOf(Integer.MAX_VALUE).add(BigDecimal.ONE));
	}

	// -----------------------------------------------------------------------
	public void testToFile1() throws Exception {
		final URL url = new URL("file", null, "a/b/c/file.txt");
		final File file = Files.toFile(url);
		assertTrue(file.toString().indexOf("file.txt") >= 0);
	}

	public void testToFile2() throws Exception {
		final URL url = new URL("file", null, "a/b/c/file%20n%61me%2520.tx%74");
		final File file = Files.toFile(url);
		assertTrue(file.toString().indexOf("file name%20.txt") >= 0);
	}

	public void testToFile3() throws Exception {
		assertEquals(null, Files.toFile((URL)null));
		assertEquals(null, Files.toFile(new URL("http://jakarta.apache.org")));
	}

	public void testToFile4() throws Exception {
		final URL url = new URL("file", null, "a/b/c/file%%20%me.txt%");
		final File file = Files.toFile(url);
		assertTrue(file.toString().indexOf("file% %me.txt%") >= 0);
	}

	/** IO-252 */
	public void testToFile5() throws Exception {
		final URL url = new URL("file", null, "both%20are%20100%20%25%20true");
		final File file = Files.toFile(url);
		assertEquals("both are 100 % true", file.toString());
	}

	public void testToFileUtf8() throws Exception {
		final URL url = new URL("file", null, "/home/%C3%A4%C3%B6%C3%BC%C3%9F");
		final File file = Files.toFile(url);
		assertTrue(file.toString().indexOf("\u00E4\u00F6\u00FC\u00DF") >= 0);
	}

	public void testDecodeUrl() {
		assertEquals("", Files.decodeUrl(""));
		assertEquals("foo", Files.decodeUrl("foo"));
		assertEquals("+", Files.decodeUrl("+"));
		assertEquals("% ", Files.decodeUrl("%25%20"));
		assertEquals("%20", Files.decodeUrl("%2520"));
		assertEquals("jar:file:/C:/dir/sub dir/1.0/foo-1.0.jar!/org/Bar.class",
			Files.decodeUrl("jar:file:/C:/dir/sub%20dir/1.0/foo-1.0.jar!/org/Bar.class"));
	}

	public void testDecodeUrlLenient() {
		assertEquals(" ", Files.decodeUrl(" "));
		assertEquals("\u00E4\u00F6\u00FC\u00DF", Files.decodeUrl("\u00E4\u00F6\u00FC\u00DF"));
		assertEquals("%", Files.decodeUrl("%"));
		assertEquals("% ", Files.decodeUrl("%%20"));
		assertEquals("%2", Files.decodeUrl("%2"));
		assertEquals("%2G", Files.decodeUrl("%2G"));
	}

	public void testDecodeUrlNullSafe() {
		assertNull(Files.decodeUrl(null));
	}

	public void testDecodeUrlEncodingUtf8() {
		assertEquals("\u00E4\u00F6\u00FC\u00DF", Files.decodeUrl("%C3%A4%C3%B6%C3%BC%C3%9F"));
	}

	// toFiles

	public void testToFiles1() throws Exception {
		final URL[] urls = new URL[] { new URL("file", null, "file1.txt"), new URL("file", null, "file2.txt"), };
		final File[] files = Files.toFiles(urls);

		assertEquals(urls.length, files.length);
		assertEquals("File: " + files[0], true, files[0].toString().indexOf("file1.txt") >= 0);
		assertEquals("File: " + files[1], true, files[1].toString().indexOf("file2.txt") >= 0);
	}

	public void testToFiles2() throws Exception {
		final URL[] urls = new URL[] { new URL("file", null, "file1.txt"), null, };
		final File[] files = Files.toFiles(urls);

		assertEquals(urls.length, files.length);
		assertEquals("File: " + files[0], true, files[0].toString().indexOf("file1.txt") >= 0);
		assertEquals("File: " + files[1], null, files[1]);
	}

	public void testToFiles3() throws Exception {
		final URL[] urls = null;
		final File[] files = Files.toFiles(urls);

		assertEquals(0, files.length);
	}

	public void testToFiles3a() throws Exception {
		final URL[] urls = new URL[0]; // empty array
		final File[] files = Files.toFiles(urls);

		assertEquals(0, files.length);
	}

	public void testToFiles4() throws Exception {
		final URL[] urls = new URL[] { new URL("file", null, "file1.txt"),
				new URL("http", "jakarta.apache.org", "file1.txt"), };
		try {
			Files.toFiles(urls);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	// toURLs

	public void testToURLs1() throws Exception {
		final File[] files = new File[] { new File(getTestDirectory(), "file1.txt"),
				new File(getTestDirectory(), "file2.txt"), new File(getTestDirectory(), "test file.txt"), };
		final URL[] urls = Files.toURLs(files);

		assertEquals(files.length, urls.length);
		assertTrue(urls[0].toExternalForm().startsWith("file:"));
		assertTrue(urls[0].toExternalForm().indexOf("file1.txt") >= 0);
		assertTrue(urls[1].toExternalForm().startsWith("file:"));
		assertTrue(urls[1].toExternalForm().indexOf("file2.txt") >= 0);

		// Test escaped char
		assertTrue(urls[2].toExternalForm().startsWith("file:"));
		assertTrue(urls[2].toExternalForm().indexOf("test%20file.txt") >= 0);
	}

	// public void testToURLs2() throws Exception {
	// File[] files = new File[] {
	// new File(getTestDirectory(), "file1.txt"),
	// null,
	// };
	// URL[] urls = Files.toURLs(files);
	//
	// assertEquals(files.length, urls.length);
	// assertTrue(urls[0].toExternalForm().startsWith("file:"));
	// assertTrue(urls[0].toExternalForm().indexOf("file1.txt") > 0);
	// assertEquals(null, urls[1]);
	// }
	//
	// public void testToURLs3() throws Exception {
	// File[] files = null;
	// URL[] urls = Files.toURLs(files);
	//
	// assertEquals(0, urls.length);
	// }

	public void testToURLs3a() throws Exception {
		final File[] files = new File[0]; // empty array
		final URL[] urls = Files.toURLs(files);

		assertEquals(0, urls.length);
	}

	// contentEquals

	public void testContentEquals() throws Exception {
		// Non-existent files
		final File file = new File(getTestDirectory(), getName());
		final File file2 = new File(getTestDirectory(), getName() + "2");
		// both don't exist
		assertTrue(Files.contentEquals(file, file));
		assertTrue(Files.contentEquals(file, file2));
		assertTrue(Files.contentEquals(file2, file2));
		assertTrue(Files.contentEquals(file2, file));

		// Directories
		try {
			Files.contentEquals(getTestDirectory(), getTestDirectory());
			fail("Comparing directories should fail with an IOException");
		}
		catch (final IOException ioe) {
			// expected
		}

		// Different files
		final File objFile1 = new File(getTestDirectory(), getName() + ".object");
		objFile1.deleteOnExit();
		Files.copyURLToFile(getClass().getResource("/java/lang/Object.class"), objFile1);

		final File objFile1b = new File(getTestDirectory(), getName() + ".object2");
		objFile1.deleteOnExit();
		Files.copyURLToFile(getClass().getResource("/java/lang/Object.class"), objFile1b);

		final File objFile2 = new File(getTestDirectory(), getName() + ".collection");
		objFile2.deleteOnExit();
		Files.copyURLToFile(getClass().getResource("/java/util/Collection.class"), objFile2);

		assertFalse(Files.contentEquals(objFile1, objFile2));
		assertFalse(Files.contentEquals(objFile1b, objFile2));
		assertTrue(Files.contentEquals(objFile1, objFile1b));

		assertTrue(Files.contentEquals(objFile1, objFile1));
		assertTrue(Files.contentEquals(objFile1b, objFile1b));
		assertTrue(Files.contentEquals(objFile2, objFile2));

		// Equal files
		file.createNewFile();
		file2.createNewFile();
		assertTrue(Files.contentEquals(file, file));
		assertTrue(Files.contentEquals(file, file2));
	}

	public void testContentEqualsIgnoreEOL() throws Exception {
		// Non-existent files
		final File file1 = new File(getTestDirectory(), getName());
		final File file2 = new File(getTestDirectory(), getName() + "2");
		// both don't exist
		assertTrue(Files.contentEqualsIgnoreEOL(file1, file1, null));
		assertTrue(Files.contentEqualsIgnoreEOL(file1, file2, null));
		assertTrue(Files.contentEqualsIgnoreEOL(file2, file2, null));
		assertTrue(Files.contentEqualsIgnoreEOL(file2, file1, null));

		// Directories
		try {
			Files.contentEqualsIgnoreEOL(getTestDirectory(), getTestDirectory(), null);
			fail("Comparing directories should fail with an IOException");
		}
		catch (final IOException ioe) {
			// expected
		}

		// Different files
		final File tfile1 = new File(getTestDirectory(), getName() + ".txt1");
		tfile1.deleteOnExit();
		Files.write(tfile1, "123\r");

		final File tfile2 = new File(getTestDirectory(), getName() + ".txt2");
		tfile1.deleteOnExit();
		Files.write(tfile2, "123\n");

		final File tfile3 = new File(getTestDirectory(), getName() + ".collection");
		tfile3.deleteOnExit();
		Files.write(tfile3, "123\r\n2");

		assertTrue(Files.contentEqualsIgnoreEOL(tfile1, tfile1, null));
		assertTrue(Files.contentEqualsIgnoreEOL(tfile2, tfile2, null));
		assertTrue(Files.contentEqualsIgnoreEOL(tfile3, tfile3, null));

		assertTrue(Files.contentEqualsIgnoreEOL(tfile1, tfile2, null));
		assertFalse(Files.contentEqualsIgnoreEOL(tfile1, tfile3, null));
		assertFalse(Files.contentEqualsIgnoreEOL(tfile2, tfile3, null));

		final File cr = new File(getTestDirectory(), "FilesTestDataCR.dat");
		Files.write(cr, new byte[] { 0x31, 0x0D, 0x32, 0x0D, 0x33, 0x0D });
		assertTrue(cr.exists());

		final File crlf = new File(getTestDirectory(), "FilesTestDataCRLF.dat");
		Files.write(crlf, new byte[] { 0x31, 0x0D, 0x0A, 0x32, 0x0D, 0x0A, 0x33, 0x0D, 0x0A });
		assertTrue(crlf.exists());

		final File lf = new File(getTestDirectory(), "FilesTestDataLF.dat");
		Files.write(lf, new byte[] { 0x31, 0x0A, 0x32, 0x0A, 0x33, 0x0A });
		assertTrue(lf.exists());

		assertTrue(Files.contentEqualsIgnoreEOL(cr, cr, null));
		assertTrue(Files.contentEqualsIgnoreEOL(crlf, crlf, null));
		assertTrue(Files.contentEqualsIgnoreEOL(lf, lf, null));

		assertTrue(Files.contentEqualsIgnoreEOL(cr, crlf, null));
		assertTrue(Files.contentEqualsIgnoreEOL(cr, lf, null));
		assertTrue(Files.contentEqualsIgnoreEOL(crlf, lf, null));

		// Check the files behave OK when EOL is not ignored
		assertTrue(Files.contentEquals(cr, cr));
		assertTrue(Files.contentEquals(crlf, crlf));
		assertTrue(Files.contentEquals(lf, lf));

		assertFalse(Files.contentEquals(cr, crlf));
		assertFalse(Files.contentEquals(cr, lf));
		assertFalse(Files.contentEquals(crlf, lf));

		// Equal files
		file1.createNewFile();
		file2.createNewFile();
		assertTrue(Files.contentEqualsIgnoreEOL(file1, file1, null));
		assertTrue(Files.contentEqualsIgnoreEOL(file1, file2, null));
	}

	// copyURLToFile

	public void testCopyURLToFile() throws Exception {
		// Creates file
		final File file = new File(getTestDirectory(), getName());
		file.deleteOnExit();

		// Loads resource
		final String resourceName = "/java/lang/Object.class";
		Files.copyURLToFile(getClass().getResource(resourceName), file);

		// Tests that resuorce was copied correctly
		final FileInputStream fis = new FileInputStream(file);
		try {
			assertTrue("Content is not equal.",
				Streams.contentEquals(getClass().getResourceAsStream(resourceName), fis));
		}
		finally {
			fis.close();
		}
		// TODO Maybe test copy to itself like for copyFile()
	}

	public void testCopyURLToFileWithTimeout() throws Exception {
		// Creates file
		final File file = new File(getTestDirectory(), "testCopyURLToFileWithTimeout");
		file.deleteOnExit();

		// Loads resource
		final String resourceName = "/java/lang/Object.class";
		Files.copyURLToFile(getClass().getResource(resourceName), file, 500, 500);

		// Tests that resuorce was copied correctly
		final FileInputStream fis = new FileInputStream(file);
		try {
			assertTrue("Content is not equal.",
				Streams.contentEquals(getClass().getResourceAsStream(resourceName), fis));
		}
		finally {
			fis.close();
		}
		// TODO Maybe test copy to itself like for copyFile()
	}

	// forceMkdir

	public void testForceMkdir() throws Exception {
		// Tests with existing directory
		Files.makeDirs(getTestDirectory());

		// Creates test file
		final File testFile = new File(getTestDirectory(), getName());
		testFile.deleteOnExit();
		testFile.createNewFile();
		assertTrue("Test file does not exist.", testFile.exists());

		// Tests with existing file
		try {
			Files.makeDirs(testFile);
			fail("Exception expected.");
		}
		catch (final IOException ex) {
		}

		testFile.delete();

		// Tests with non-existent directory
		Files.makeDirs(testFile);
		assertTrue("Directory was not created.", testFile.exists());
	}

	public void testForceMkdirParent() throws Exception {
		// Tests with existing directory
		assertTrue(getTestDirectory().exists());
		final File testParentDir = new File(getTestDirectory(), "testForceMkdirParent");
		try {
			testParentDir.delete();
			assertFalse(testParentDir.exists());
			final File testFile = new File(testParentDir, "test.txt");
			assertFalse(testParentDir.exists());
			assertFalse(testFile.exists());
			// Create
			Files.makeParents(testFile);
			assertTrue(testParentDir.exists());
			assertFalse(testFile.exists());
			// Again
			Files.makeParents(testFile);
			assertTrue(testParentDir.exists());
			assertFalse(testFile.exists());
		}
		finally {
			testParentDir.delete();
		}
	}

	// sizeOfDirectory

	public void testSizeOfDirectory() throws Exception {
		final File file = new File(getTestDirectory(), getName());

		// Non-existent file
		try {
			Files.sizeOfDir(file);
			fail("Exception expected.");
		}
		catch (final IllegalArgumentException ex) {
		}

		// Creates file
		file.createNewFile();
		file.deleteOnExit();

		// Existing file
		try {
			Files.sizeOfDir(file);
			fail("Exception expected.");
		}
		catch (final IllegalArgumentException ex) {
		}

		// Existing directory
		file.delete();
		file.mkdir();

		// Create a cyclic symlink
		this.createCircularSymLink(file);

		assertEquals("Unexpected directory size", TEST_DIRECTORY_SIZE, Files.sizeOfDir(file));
	}

	private void createCircularSymLink(final File file) throws IOException {
		if (!FileNames.isSystemWindows()) {
			Runtime.getRuntime().exec("ln -s " + file + "/.. " + file + "/cycle");
		}
		else {
			try {
				Runtime.getRuntime().exec("mklink /D " + file + "/cycle" + file + "/.. ");
			}
			catch (final IOException ioe) { // So that tests run in FAT filesystems
				// don't fail
			}
		}
	}

	public void testSizeOfDirectoryAsBigInteger() throws Exception {
		final File file = new File(getTestDirectory(), getName());

		// Non-existent file
		try {
			Files.sizeOfDirAsBigInteger(file);
			fail("Exception expected.");
		}
		catch (final IllegalArgumentException ex) {
		}

		// Creates file
		file.createNewFile();
		file.deleteOnExit();

		// Existing file
		try {
			Files.sizeOfDirAsBigInteger(file);
			fail("Exception expected.");
		}
		catch (final IllegalArgumentException ex) {
		}

		// Existing directory
		file.delete();
		file.mkdir();

		this.createCircularSymLink(file);

		assertEquals("Unexpected directory size", TEST_DIRECTORY_SIZE_BI, Files.sizeOfDirAsBigInteger(file));

		// Existing directory which size is greater than zero
		file.delete();
		file.mkdir();

		final File nonEmptyFile = new File(file, "nonEmptyFile" + System.nanoTime());
		this.createFile(nonEmptyFile, TEST_DIRECTORY_SIZE_GT_ZERO_BI.longValue());
		nonEmptyFile.deleteOnExit();

		assertEquals("Unexpected directory size", TEST_DIRECTORY_SIZE_GT_ZERO_BI,
			Files.sizeOfDirAsBigInteger(file));

		nonEmptyFile.delete();
		file.delete();
	}

	// Compare sizes of a directory tree using long and BigInteger methods
	public void testCompareSizeOf() {
		final File start = new File("src/test/java");
		final long sizeLong1 = Files.sizeOf(start);
		final BigInteger sizeBig = Files.sizeOfAsBigInteger(start);
		final long sizeLong2 = Files.sizeOf(start);
		assertEquals("Size should not change", sizeLong1, sizeLong2);
		assertEquals("longSize should equal BigSize", sizeLong1, sizeBig.longValue());
	}

	/**
	 * Tests the {@link Files#sizeOf(File)} method.
	 */
	public void testSizeOf() throws Exception {
		final File file = new File(getTestDirectory(), getName());

		// Null argument
		try {
			Files.sizeOf(null);
			fail("Exception expected.");
		}
		catch (final NullPointerException ex) {
		}

		// Non-existent file
		try {
			Files.sizeOf(file);
			fail("Exception expected.");
		}
		catch (final IllegalArgumentException ex) {
		}

		// Creates file
		file.createNewFile();
		file.deleteOnExit();

		// New file
		assertEquals(0, Files.sizeOf(file));
		file.delete();

		// Existing file
		assertEquals("Unexpected files size", testFile1Size, Files.sizeOf(testFile1));

		// Existing directory
		assertEquals("Unexpected directory size", TEST_DIRECTORY_SIZE, Files.sizeOf(getTestDirectory()));
	}

	/**
	 * Tests the {@link Files#sizeOf(File)} method.
	 */
	public void testSizeOfAsBigInteger() throws Exception {
		final File file = new File(getTestDirectory(), getName());

		// Null argument
		try {
			Files.sizeOfAsBigInteger(null);
			fail("Exception expected.");
		}
		catch (final NullPointerException ex) {
		}

		// Non-existent file
		try {
			Files.sizeOfAsBigInteger(file);
			fail("Exception expected.");
		}
		catch (final IllegalArgumentException ex) {
		}

		// Creates file
		file.createNewFile();
		file.deleteOnExit();

		// New file
		assertEquals(BigInteger.ZERO, Files.sizeOfAsBigInteger(file));
		file.delete();

		// Existing file
		assertEquals("Unexpected files size", BigInteger.valueOf(testFile1Size), Files.sizeOfAsBigInteger(testFile1));

		// Existing directory
		assertEquals("Unexpected directory size", TEST_DIRECTORY_SIZE_BI, Files.sizeOfAsBigInteger(getTestDirectory()));
	}

	// isFileNewer / isFileOlder
	public void testIsFileNewerOlder() throws Exception {
		final File reference = new File(getTestDirectory(), "Files-reference.txt");
		final File oldFile = new File(getTestDirectory(), "Files-old.txt");
		final File newFile = new File(getTestDirectory(), "Files-new.txt");
		final File invalidFile = new File(getTestDirectory(), "Files-invalid-file.txt");

		// Create Files
		createFile(oldFile, 0);

		do {
			try {
				Thread.sleep(1000);
			}
			catch (final InterruptedException ie) {
				// ignore
			}
			createFile(reference, 0);
		}
		while (oldFile.lastModified() == reference.lastModified());

		final Date date = new Date();
		final long now = date.getTime();

		do {
			try {
				Thread.sleep(1000);
			}
			catch (final InterruptedException ie) {
				// ignore
			}
			createFile(newFile, 0);
		}
		while (reference.lastModified() == newFile.lastModified());

		// Test isFileNewer()
		assertFalse("Old File - Newer - File", Files.isFileNewer(oldFile, reference));
		assertFalse("Old File - Newer - Date", Files.isFileNewer(oldFile, date));
		assertFalse("Old File - Newer - Mili", Files.isFileNewer(oldFile, now));
		assertTrue("New File - Newer - File", Files.isFileNewer(newFile, reference));
		assertTrue("New File - Newer - Date", Files.isFileNewer(newFile, date));
		assertTrue("New File - Newer - Mili", Files.isFileNewer(newFile, now));
		assertFalse("Invalid - Newer - File", Files.isFileNewer(invalidFile, reference));
		final String invalidFileName = invalidFile.getName();
		try {
			Files.isFileNewer(newFile, invalidFile);
			fail("Should have cause IllegalArgumentException");
		}
		catch (final IllegalArgumentException iae) {
			final String message = iae.getMessage();
			assertTrue("Message should contain: " + invalidFileName + " but was: " + message,
				message.contains(invalidFileName));
		}

		// Test isFileOlder()
		assertTrue("Old File - Older - File", Files.isFileOlder(oldFile, reference));
		assertTrue("Old File - Older - Date", Files.isFileOlder(oldFile, date));
		assertTrue("Old File - Older - Mili", Files.isFileOlder(oldFile, now));
		assertFalse("New File - Older - File", Files.isFileOlder(newFile, reference));
		assertFalse("New File - Older - Date", Files.isFileOlder(newFile, date));
		assertFalse("New File - Older - Mili", Files.isFileOlder(newFile, now));
		assertFalse("Invalid - Older - File", Files.isFileOlder(invalidFile, reference));
		try {
			Files.isFileOlder(newFile, invalidFile);
			fail("Should have cause IllegalArgumentException");
		}
		catch (final IllegalArgumentException iae) {
			final String message = iae.getMessage();
			assertTrue("Message should contain: " + invalidFileName + " but was: " + message,
				message.contains(invalidFileName));
		}

		// ----- Test isFileNewer() exceptions -----
		// Null File
		try {
			Files.isFileNewer(null, now);
			fail("Newer Null, expected IllegalArgumentExcepion");
		}
		catch (final IllegalArgumentException expected) {
			// expected result
		}

		// Null reference File
		try {
			Files.isFileNewer(oldFile, (File)null);
			fail("Newer Null reference, expected IllegalArgumentExcepion");
		}
		catch (final IllegalArgumentException expected) {
			// expected result
		}

		// Invalid reference File
		try {
			Files.isFileNewer(oldFile, invalidFile);
			fail("Newer invalid reference, expected IllegalArgumentExcepion");
		}
		catch (final IllegalArgumentException expected) {
			// expected result
		}

		// Null reference Date
		try {
			Files.isFileNewer(oldFile, (Date)null);
			fail("Newer Null date, expected IllegalArgumentExcepion");
		}
		catch (final IllegalArgumentException expected) {
			// expected result
		}

		// ----- Test isFileOlder() exceptions -----
		// Null File
		try {
			Files.isFileOlder(null, now);
			fail("Older Null, expected IllegalArgumentExcepion");
		}
		catch (final IllegalArgumentException expected) {
			// expected result
		}

		// Null reference File
		try {
			Files.isFileOlder(oldFile, (File)null);
			fail("Older Null reference, expected IllegalArgumentExcepion");
		}
		catch (final IllegalArgumentException expected) {
			// expected result
		}

		// Invalid reference File
		try {
			Files.isFileOlder(oldFile, invalidFile);
			fail("Older invalid reference, expected IllegalArgumentExcepion");
		}
		catch (final IllegalArgumentException expected) {
			// expected result
		}

		// Null reference Date
		try {
			Files.isFileOlder(oldFile, (Date)null);
			fail("Older Null date, expected IllegalArgumentExcepion");
		}
		catch (final IllegalArgumentException expected) {
			// expected result
		}

	}

	// // TODO Remove after debugging
	// private void log(Object obj) {
	// System.out.println(
	// FilesTestCase.class +" " + getName() + " " + obj);
	// }

	// copyFile

	public void testCopyFile1() throws Exception {
		final File destination = new File(getTestDirectory(), "copy1.txt");

		// Thread.sleep(LAST_MODIFIED_DELAY);
		// This is to slow things down so we can catch if
		// the lastModified date is not ok

		Files.copyFile(testFile1, destination);
		assertTrue("Check Exist", destination.exists());
		assertEquals("Check Full copy", testFile1Size, destination.length());
		/*
		 * disabled: Thread.sleep doesn't work reliantly for this case
		 * assertTrue("Check last modified date preserved", testFile1.lastModified() ==
		 * destination.lastModified());
		 */
	}

	public void testCopyFileToOutputStream() throws Exception {
		final ByteArrayOutputStream destination = new ByteArrayOutputStream();
		Files.copyFile(testFile1, destination);
		assertEquals("Check Full copy size", testFile1Size, destination.size());
		final byte[] expected = Files.readFileToByteArray(testFile1);
		Assert.assertArrayEquals("Check Full copy", expected, destination.toByteArray());
	}

	public void IGNOREtestCopyFileLarge() throws Exception {

		final File largeFile = new File(getTestDirectory(), "large.txt");
		final File destination = new File(getTestDirectory(), "copylarge.txt");

		System.out.println("START:   " + new java.util.Date());
		createFile(largeFile, Numbers.GB);
		System.out.println("CREATED: " + new java.util.Date());
		Files.copyFile(largeFile, destination);
		System.out.println("COPIED:  " + new java.util.Date());

		assertTrue("Check Exist", destination.exists());
		assertEquals("Check Full copy", largeFile.length(), destination.length());
	}

	public void testCopyFile2() throws Exception {
		final File destination = new File(getTestDirectory(), "copy2.txt");

		// Thread.sleep(LAST_MODIFIED_DELAY);
		// This is to slow things down so we can catch if
		// the lastModified date is not ok

		Files.copyFile(testFile1, destination);
		assertTrue("Check Exist", destination.exists());
		assertEquals("Check Full copy", testFile2Size, destination.length());
		/*
		 * disabled: Thread.sleep doesn't work reliably for this case
		 * assertTrue("Check last modified date preserved", testFile1.lastModified() ==
		 * destination.lastModified());
		 */
	}

	public void testCopyToSelf() throws Exception {
		final File destination = new File(getTestDirectory(), "copy3.txt");
		// Prepare a test file
		Files.copyFile(testFile1, destination);

		try {
			Files.copyFile(destination, destination);
			fail("file copy to self should not be possible");
		}
		catch (final IOException ioe) {
			// we want the exception, copy to self should be illegal
		}
	}

	public void testCopyFile2WithoutFileDatePreservation() throws Exception {
		final File destination = new File(getTestDirectory(), "copy2.txt");

		// Thread.sleep(LAST_MODIFIED_DELAY);
		// This is to slow things down so we can catch if
		// the lastModified date is not ok

		Files.copyFile(testFile1, destination, false);
		assertTrue("Check Exist", destination.exists());
		assertEquals("Check Full copy", testFile2Size, destination.length());
		/*
		 * disabled: Thread.sleep doesn't work reliantly for this case
		 * assertTrue("Check last modified date modified", testFile1.lastModified() !=
		 * destination.lastModified());
		 */
	}

	public void testCopyDirectoryToDirectory_NonExistingDest() throws Exception {
		createFile(testFile1, 1234);
		createFile(testFile2, 4321);
		final File srcDir = getTestDirectory();
		final File subDir = new File(srcDir, "sub");
		subDir.mkdir();
		final File subFile = new File(subDir, "A.txt");
		Files.write(subFile, "HELLO WORLD", "UTF8");
		final File destDir = new File(System.getProperty("java.io.tmpdir"), "tmp-FilesTestCase");
		Files.deleteDir(destDir);
		final File actualDestDir = new File(destDir, srcDir.getName());

		Files.copyDirToDir(srcDir, destDir);

		assertTrue("Check exists", destDir.exists());
		assertTrue("Check exists", actualDestDir.exists());
		final long srcSize = Files.sizeOfDir(srcDir);
		assertTrue("Size > 0", srcSize > 0);
		assertEquals("Check size", srcSize, Files.sizeOfDir(actualDestDir));
		assertTrue(new File(actualDestDir, "sub/A.txt").exists());
		Files.deleteDir(destDir);
	}

	public void testCopyDirectoryToNonExistingDest() throws Exception {
		createFile(testFile1, 1234);
		createFile(testFile2, 4321);
		final File srcDir = getTestDirectory();
		final File subDir = new File(srcDir, "sub");
		subDir.mkdir();
		final File subFile = new File(subDir, "A.txt");
		Files.write(subFile, "HELLO WORLD", "UTF8");
		final File destDir = new File(System.getProperty("java.io.tmpdir"), "tmp-FilesTestCase");
		Files.deleteDir(destDir);

		Files.copyDir(srcDir, destDir);

		assertTrue("Check exists", destDir.exists());
		final long sizeOfSrcDirectory = Files.sizeOfDir(srcDir);
		assertTrue("Size > 0", sizeOfSrcDirectory > 0);
		assertEquals("Check size", sizeOfSrcDirectory, Files.sizeOfDir(destDir));
		assertTrue(new File(destDir, "sub/A.txt").exists());
		Files.deleteDir(destDir);
	}

	public void testCopyDirectoryToExistingDest() throws Exception {
		createFile(testFile1, 1234);
		createFile(testFile2, 4321);
		final File srcDir = getTestDirectory();
		final File subDir = new File(srcDir, "sub");
		subDir.mkdir();
		final File subFile = new File(subDir, "A.txt");
		Files.write(subFile, "HELLO WORLD", "UTF8");
		final File destDir = new File(System.getProperty("java.io.tmpdir"), "tmp-FilesTestCase");
		Files.deleteDir(destDir);
		destDir.mkdirs();

		Files.copyDir(srcDir, destDir);

		final long srcSize = Files.sizeOfDir(srcDir);
		assertTrue("Size > 0", srcSize > 0);
		assertEquals(srcSize, Files.sizeOfDir(destDir));
		assertTrue(new File(destDir, "sub/A.txt").exists());
	}

	public void testCopyDirectoryFiltered() throws Exception {
		final File grandParentDir = new File(getTestDirectory(), "grandparent");
		final File parentDir = new File(grandParentDir, "parent");
		final File childDir = new File(parentDir, "child");
		createFilesForTestCopyDirectory(grandParentDir, parentDir, childDir);

		final NameFileFilter filter = new NameFileFilter(new String[] { "parent", "child", "file3.txt" });
		final File destDir = new File(getTestDirectory(), "copydest");

		Files.copyDir(grandParentDir, destDir, filter);
		final List<File> files = LIST_WALKER.list(destDir);
		assertEquals(3, files.size());
		assertEquals("parent", files.get(0).getName());
		assertEquals("child", files.get(1).getName());
		assertEquals("file3.txt", files.get(2).getName());
	}

	public void testCopyDirectoryPreserveDates() throws Exception {
		final File source = new File(getTestDirectory(), "source");
		final File sourceDirectory = new File(source, "directory");
		final File sourceFile = new File(sourceDirectory, "hello.txt");

		// Prepare source data
		source.mkdirs();
		sourceDirectory.mkdir();
		Files.write(sourceFile, "HELLO WORLD", "UTF8");
		// Set dates in reverse order to avoid overwriting previous values
		// Also, use full seconds (arguments are in ms) close to today
		// but still highly unlikely to occur in the real world
		sourceFile.setLastModified(1000000002000L);
		sourceDirectory.setLastModified(1000000001000L);
		source.setLastModified(1000000000000L);

		final File target = new File(getTestDirectory(), "target");
		final File targetDirectory = new File(target, "directory");
		final File targetFile = new File(targetDirectory, "hello.txt");

		// Test with preserveFileDate disabled
		Files.copyDir(source, target, false);
		assertTrue(1000000000000L != target.lastModified());
		assertTrue(1000000001000L != targetDirectory.lastModified());
		assertTrue(1000000002000L != targetFile.lastModified());
		Files.deleteDir(target);

		// Test with preserveFileDate enabled
		Files.copyDir(source, target, true);
		assertEquals(1000000000000L, target.lastModified());
		assertEquals(1000000001000L, targetDirectory.lastModified());
		assertEquals(1000000002000L, targetFile.lastModified());
		Files.deleteDir(target);

		// also if the target directory already exists (IO-190)
		target.mkdirs();
		Files.copyDir(source, target, true);
		assertEquals(1000000000000L, target.lastModified());
		assertEquals(1000000001000L, targetDirectory.lastModified());
		assertEquals(1000000002000L, targetFile.lastModified());
		Files.deleteDir(target);

		// also if the target subdirectory already exists (IO-190)
		targetDirectory.mkdirs();
		Files.copyDir(source, target, true);
		assertEquals(1000000000000L, target.lastModified());
		assertEquals(1000000001000L, targetDirectory.lastModified());
		assertEquals(1000000002000L, targetFile.lastModified());
		Files.deleteDir(target);
	}

	/** Test for IO-141 */
	public void testCopyDirectoryToChild() throws Exception {
		final File grandParentDir = new File(getTestDirectory(), "grandparent");
		final File parentDir = new File(grandParentDir, "parent");
		final File childDir = new File(parentDir, "child");
		createFilesForTestCopyDirectory(grandParentDir, parentDir, childDir);

		final long expectedCount = LIST_WALKER.list(grandParentDir).size() + LIST_WALKER.list(parentDir).size();
		final long expectedSize = Files.sizeOfDir(grandParentDir) + Files.sizeOfDir(parentDir);
		Files.copyDir(parentDir, childDir);
		assertEquals(expectedCount, LIST_WALKER.list(grandParentDir).size());
		assertEquals(expectedSize, Files.sizeOfDir(grandParentDir));
		assertTrue("Count > 0", expectedCount > 0);
		assertTrue("Size > 0", expectedSize > 0);
	}

	/** Test for IO-141 */
	public void testCopyDirectoryToGrandChild() throws Exception {
		final File grandParentDir = new File(getTestDirectory(), "grandparent");
		final File parentDir = new File(grandParentDir, "parent");
		final File childDir = new File(parentDir, "child");
		createFilesForTestCopyDirectory(grandParentDir, parentDir, childDir);

		final long expectedCount = LIST_WALKER.list(grandParentDir).size() * 2;
		final long expectedSize = Files.sizeOfDir(grandParentDir) * 2;
		Files.copyDir(grandParentDir, childDir);
		assertEquals(expectedCount, LIST_WALKER.list(grandParentDir).size());
		assertEquals(expectedSize, Files.sizeOfDir(grandParentDir));
		assertTrue("Size > 0", expectedSize > 0);
	}

	/** Test for IO-217 Files.copyDirectoryToDirectory makes infinite loops */
	public void testCopyDirectoryToItself() throws Exception {
		final File dir = new File(getTestDirectory(), "itself");
		dir.mkdirs();
		Files.copyDirToDir(dir, dir);
		assertEquals(1, LIST_WALKER.list(dir).size());
	}

	private void createFilesForTestCopyDirectory(final File grandParentDir, final File parentDir, final File childDir)
			throws Exception {
		final File childDir2 = new File(parentDir, "child2");
		final File grandChildDir = new File(childDir, "grandChild");
		final File grandChild2Dir = new File(childDir2, "grandChild2");
		final File file1 = new File(grandParentDir, "file1.txt");
		final File file2 = new File(parentDir, "file2.txt");
		final File file3 = new File(childDir, "file3.txt");
		final File file4 = new File(childDir2, "file4.txt");
		final File file5 = new File(grandChildDir, "file5.txt");
		final File file6 = new File(grandChild2Dir, "file6.txt");
		Files.deleteDir(grandParentDir);
		grandChildDir.mkdirs();
		grandChild2Dir.mkdirs();
		Files.write(file1, "File 1 in grandparent", "UTF8");
		Files.write(file2, "File 2 in parent", "UTF8");
		Files.write(file3, "File 3 in child", "UTF8");
		Files.write(file4, "File 4 in child2", "UTF8");
		Files.write(file5, "File 5 in grandChild", "UTF8");
		Files.write(file6, "File 6 in grandChild2", "UTF8");
	}

	public void testCopyDirectoryErrors() throws Exception {
		try {
			Files.copyDir(null, null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
		try {
			Files.copyDir(new File("a"), null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
		try {
			Files.copyDir(null, new File("a"));
			fail();
		}
		catch (final NullPointerException ex) {
		}
		try {
			Files.copyDir(new File("doesnt-exist"), new File("a"));
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			Files.copyDir(testFile1, new File("a"));
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			Files.copyDir(getTestDirectory(), testFile1);
			fail();
		}
		catch (final IOException ex) {
		}
		try {
			Files.copyDir(getTestDirectory(), getTestDirectory());
			fail();
		}
		catch (final IOException ex) {
		}
	}

	// forceDelete

	public void testForceDeleteAFile1() throws Exception {
		final File destination = new File(getTestDirectory(), "copy1.txt");
		destination.createNewFile();
		assertTrue("Copy1.txt doesn't exist to delete", destination.exists());
		Files.forceDelete(destination);
		assertTrue("Check No Exist", !destination.exists());
	}

	public void testForceDeleteAFile2() throws Exception {
		final File destination = new File(getTestDirectory(), "copy2.txt");
		destination.createNewFile();
		assertTrue("Copy2.txt doesn't exist to delete", destination.exists());
		Files.forceDelete(destination);
		assertTrue("Check No Exist", !destination.exists());
	}

	public void testForceDeleteAFile3() throws Exception {
		final File destination = new File(getTestDirectory(), "no_such_file");
		assertTrue("Check No Exist", !destination.exists());
		try {
			Files.forceDelete(destination);
			fail("Should generate FileNotFoundException");
		}
		catch (final FileNotFoundException ignored) {
		}
	}

	// copyFileToDirectory

	public void testCopyFile1ToDir() throws Exception {
		final File directory = new File(getTestDirectory(), "subdir");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		final File destination = new File(directory, testFile1.getName());

		// Thread.sleep(LAST_MODIFIED_DELAY);
		// This is to slow things down so we can catch if
		// the lastModified date is not ok

		Files.copyFileToDir(testFile1, directory);
		assertTrue("Check Exist", destination.exists());
		assertEquals("Check Full copy", testFile1Size, destination.length());
		/*
		 * disabled: Thread.sleep doesn't work reliantly for this case
		 * assertTrue("Check last modified date preserved", testFile1.lastModified() ==
		 * destination.lastModified());
		 */

		try {
			Files.copyFileToDir(destination, directory);
			fail("Should not be able to copy a file into the same directory as itself");
		}
		catch (final IOException ioe) {
			// we want that, cannot copy to the same directory as the original file
		}
	}

	public void testCopyFile2ToDir() throws Exception {
		final File directory = new File(getTestDirectory(), "subdir");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		final File destination = new File(directory, testFile1.getName());

		// Thread.sleep(LAST_MODIFIED_DELAY);
		// This is to slow things down so we can catch if
		// the lastModified date is not ok

		Files.copyFileToDir(testFile1, directory);
		assertTrue("Check Exist", destination.exists());
		assertEquals("Check Full copy", testFile2Size, destination.length());
		/*
		 * disabled: Thread.sleep doesn't work reliantly for this case
		 * assertTrue("Check last modified date preserved", testFile1.lastModified() ==
		 * destination.lastModified());
		 */
	}

	// forceDelete

	public void testForceDeleteDir() throws Exception {
		final File testDirectory = getTestDirectory();
		Files.forceDelete(testDirectory.getParentFile());
		assertTrue("Check No Exist", !testDirectory.getParentFile().exists());
	}

	/**
	 * Test the Files implementation.
	 */
	public void testFiles() throws Exception {
		// Loads file from classpath
		final File file1 = new File(getTestDirectory(), "test.txt");
		final String filename = file1.getAbsolutePath();

		// Create test file on-the-fly (used to be in CVS)
		final OutputStream out = new java.io.FileOutputStream(file1);
		try {
			out.write("This is a test".getBytes("UTF-8"));
		}
		finally {
			out.close();
		}

		final File file2 = new File(getTestDirectory(), "test2.txt");

		Files.write(file2, filename, "UTF-8");
		assertTrue(file2.exists());
		assertTrue(file2.length() > 0);

		final String file2contents = Files.readFileToString(file2, "UTF-8");
		assertTrue("Second file's contents correct", filename.equals(file2contents));

		assertTrue(file2.delete());

		final String contents = Files.readFileToString(new File(filename), "UTF-8");
		assertEquals("Files.fileRead()", "This is a test", contents);

	}

	public void testTouch() throws IOException {
		final File file = new File(getTestDirectory(), "touch.txt");
		if (file.exists()) {
			file.delete();
		}
		assertTrue("Bad test: test file still exists", !file.exists());
		Files.touch(file);
		assertTrue("Files.touch() created file", file.exists());
		final FileOutputStream out = new FileOutputStream(file);
		assertEquals("Created empty file.", 0, file.length());
		out.write(0);
		out.close();
		assertEquals("Wrote one byte to file", 1, file.length());
		final long y2k = new GregorianCalendar(2000, 0, 1).getTime().getTime();
		final boolean res = file.setLastModified(y2k); // 0L fails on Win98
		assertEquals("Bad test: set lastModified failed", true, res);
		assertEquals("Bad test: set lastModified set incorrect value", y2k, file.lastModified());
		final long now = System.currentTimeMillis();
		Files.touch(file);
		assertEquals("Files.touch() didn't empty the file.", 1, file.length());
		assertEquals("Files.touch() changed lastModified", false, y2k == file.lastModified());
		assertEquals("Files.touch() changed lastModified to more than now-3s", true, file.lastModified() >= now - 3000);
		assertEquals("Files.touch() changed lastModified to less than now+3s", true, file.lastModified() <= now + 3000);
	}

	public void testListFiles() throws Exception {
		final File srcDir = getTestDirectory();
		final File subDir = new File(srcDir, "list_test");
		subDir.mkdir();

		final File subDir2 = new File(subDir, "subdir");
		subDir2.mkdir();

		final String[] fileNames = { "a.txt", "b.txt", "c.txt", "d.txt", "e.txt", "f.txt" };
		final int[] fileSizes = { 123, 234, 345, 456, 678, 789 };

		for (int i = 0; i < fileNames.length; ++i) {
			final File theFile = new File(subDir, fileNames[i]);
			createFile(theFile, fileSizes[i]);
		}

		final Collection<File> files = Files.listFiles(subDir, new WildcardFileFilter("*.*"), new WildcardFileFilter(
			"*"));

		final int count = files.size();
		final Object[] fileObjs = files.toArray();

		assertEquals(fileNames.length, files.size());

		final Map<String, String> foundFileNames = new HashMap<String, String>();

		for (int i = 0; i < count; ++i) {
			boolean found = false;
			for (int j = 0; !found && j < fileNames.length; ++j) {
				if (fileNames[j].equals(((File)fileObjs[i]).getName())) {
					foundFileNames.put(fileNames[j], fileNames[j]);
					found = true;
				}
			}
		}

		assertEquals(foundFileNames.size(), fileNames.length);

		subDir.delete();
	}

	public void testListFilesWithDirs() throws IOException {
		final File srcDir = getTestDirectory();

		final File subDir1 = new File(srcDir, "subdir");
		subDir1.mkdir();

		final File subDir2 = new File(subDir1, "subdir2");
		subDir2.mkdir();

		final File someFile = new File(subDir2, "a.txt");
		createFile(someFile, 100);

		final File subDir3 = new File(subDir2, "subdir3");
		subDir3.mkdir();

		final Collection<File> files = Files.listFilesAndDirs(subDir1, new WildcardFileFilter("*.*"),
			new WildcardFileFilter("*"));

		assertEquals(4, files.size());
		assertTrue("Should contain the directory.", files.contains(subDir1));
		assertTrue("Should contain the directory.", files.contains(subDir2));
		assertTrue("Should contain the file.", files.contains(someFile));
		assertTrue("Should contain the directory.", files.contains(subDir3));

		subDir1.delete();
	}

	public void testIterateFiles() throws Exception {
		final File srcDir = getTestDirectory();
		final File subDir = new File(srcDir, "list_test");
		subDir.mkdir();

		final String[] fileNames = { "a.txt", "b.txt", "c.txt", "d.txt", "e.txt", "f.txt" };
		final int[] fileSizes = { 123, 234, 345, 456, 678, 789 };

		for (int i = 0; i < fileNames.length; ++i) {
			final File theFile = new File(subDir, fileNames[i]);
			createFile(theFile, fileSizes[i]);
		}

		final Iterator<File> files = Files.iterateFiles(subDir, new WildcardFileFilter("*.*"), new WildcardFileFilter(
			"*"));

		final Map<String, String> foundFileNames = new HashMap<String, String>();

		while (files.hasNext()) {
			boolean found = false;
			final String fileName = files.next().getName();

			for (int j = 0; !found && j < fileNames.length; ++j) {
				if (fileNames[j].equals(fileName)) {
					foundFileNames.put(fileNames[j], fileNames[j]);
					found = true;
				}
			}
		}

		assertEquals(foundFileNames.size(), fileNames.length);

		subDir.delete();
	}

	public void testIterateFilesAndDirs() throws IOException {
		final File srcDir = getTestDirectory();

		final File subDir1 = new File(srcDir, "subdir");
		subDir1.mkdir();

		final File subDir2 = new File(subDir1, "subdir2");
		subDir2.mkdir();

		final File someFile = new File(subDir2, "a.txt");
		createFile(someFile, 100);

		final File subDir3 = new File(subDir2, "subdir3");
		subDir3.mkdir();

		final Collection<File> filesAndDirs = Arrays.asList(subDir1, subDir2, someFile, subDir3);

		int filesCount = 0;
		final Iterator<File> files = Files.iterateFilesAndDirs(subDir1, new WildcardFileFilter("*.*"),
			new WildcardFileFilter("*"));
		while (files.hasNext()) {
			filesCount++;
			final File file = files.next();
			assertTrue("Should contain the directory/file", filesAndDirs.contains(file));
		}

		assertEquals(filesCount, filesAndDirs.size());
	}

	public void testReadFileToStringWithDefaultEncoding() throws Exception {
		final File file = new File(getTestDirectory(), "read.obj");
		final FileOutputStream out = new FileOutputStream(file);
		final byte[] text = "Hello /u1234".getBytes();
		out.write(text);
		out.close();

		final String data = Files.readFileToString(file);
		assertEquals("Hello /u1234", data);
	}

	public void testReadFileToStringWithEncoding() throws Exception {
		final File file = new File(getTestDirectory(), "read.obj");
		final FileOutputStream out = new FileOutputStream(file);
		final byte[] text = "Hello /u1234".getBytes("UTF8");
		out.write(text);
		out.close();

		final String data = Files.readFileToString(file, "UTF8");
		assertEquals("Hello /u1234", data);
	}

	public void testReadFileToByteArray() throws Exception {
		final File file = new File(getTestDirectory(), "read.txt");
		final FileOutputStream out = new FileOutputStream(file);
		out.write(11);
		out.write(21);
		out.write(31);
		out.close();

		final byte[] data = Files.readFileToByteArray(file);
		assertEquals(3, data.length);
		assertEquals(11, data[0]);
		assertEquals(21, data[1]);
		assertEquals(31, data[2]);
	}

	public void testReadLines() throws Exception {
		final File file = newFile("lines.txt");
		try {
			final String[] data = new String[] { "hello", "/u1234", "", "this is", "some text" };
			createLineBasedFile(file, data);

			final List<String> lines = Files.readLines(file, "UTF-8");
			assertEquals(Arrays.asList(data), lines);
		}
		finally {
			deleteFile(file);
		}
	}

	public void testWriteStringToFile1() throws Exception {
		final File file = new File(getTestDirectory(), "write.txt");
		Files.write(file, "Hello /u1234", "UTF8");
		final byte[] text = "Hello /u1234".getBytes("UTF8");
		assertEqualContent(text, file);
	}

	public void testWriteStringToFile2() throws Exception {
		final File file = new File(getTestDirectory(), "write.txt");
		Files.write(file, "Hello /u1234", (String)null);
		final byte[] text = "Hello /u1234".getBytes();
		assertEqualContent(text, file);
	}

	public void testWriteStringToFile3() throws Exception {
		final File file = new File(getTestDirectory(), "write.txt");
		Files.write(file, "Hello /u1234", (Charset)null);
		final byte[] text = "Hello /u1234".getBytes();
		assertEqualContent(text, file);
	}

	public void testWriteCharSequence1() throws Exception {
		final File file = new File(getTestDirectory(), "write.txt");
		Files.write(file, "Hello /u1234", "UTF8");
		final byte[] text = "Hello /u1234".getBytes("UTF8");
		assertEqualContent(text, file);
	}

	public void testWriteCharSequence2() throws Exception {
		final File file = new File(getTestDirectory(), "write.txt");
		Files.write(file, "Hello /u1234", (String)null);
		final byte[] text = "Hello /u1234".getBytes();
		assertEqualContent(text, file);
	}

	public void testWriteByteArrayToFile() throws Exception {
		final File file = new File(getTestDirectory(), "write.obj");
		final byte[] data = new byte[] { 11, 21, 31 };
		Files.write(file, data);
		assertEqualContent(data, file);
	}

	public void testWriteByteArrayToFile_WithOffsetAndLength() throws Exception {
		final File file = new File(getTestDirectory(), "write.obj");
		final byte[] data = new byte[] { 11, 21, 32, 41, 51 };
		final byte[] writtenData = new byte[3];
		System.arraycopy(data, 1, writtenData, 0, 3);
		Files.write(file, data, 1, 3);
		assertEqualContent(writtenData, file);
	}

	public void testWriteLines_4arg() throws Exception {
		final Object[] data = new Object[] { "hello", new StringBuffer("world"), "", "this is", null, "some text" };
		final List<Object> list = Arrays.asList(data);

		final File file = newFile("lines.txt");
		Files.writeLines(file, "US-ASCII", list, "*");

		final String expected = "hello*world**this is**some text*";
		final String actual = Files.readFileToString(file, "US-ASCII");
		assertEquals(expected, actual);
	}

	public void testWriteLines_4arg_Writer_nullData() throws Exception {
		final File file = newFile("lines.txt");
		Files.writeLines(file, "US-ASCII", (List<?>)null, "*");

		assertEquals("Sizes differ", 0, file.length());
	}

	public void testWriteLines_4arg_nullSeparator() throws Exception {
		final Object[] data = new Object[] { "hello", new StringBuffer("world"), "", "this is", null, "some text" };
		final List<Object> list = Arrays.asList(data);

		final File file = newFile("lines.txt");
		Files.writeLines(file, "US-ASCII", list, null);

		final String expected = "hello" + Streams.LINE_SEPARATOR + "world" + Streams.LINE_SEPARATOR
				+ Streams.LINE_SEPARATOR + "this is" + Streams.LINE_SEPARATOR + Streams.LINE_SEPARATOR + "some text"
				+ Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file, "US-ASCII");
		assertEquals(expected, actual);
	}

	public void testWriteLines_3arg_nullSeparator() throws Exception {
		final Object[] data = new Object[] { "hello", new StringBuffer("world"), "", "this is", null, "some text" };
		final List<Object> list = Arrays.asList(data);

		final File file = newFile("lines.txt");
		Files.writeLines(file, "US-ASCII", list);

		final String expected = "hello" + Streams.LINE_SEPARATOR + "world" + Streams.LINE_SEPARATOR
				+ Streams.LINE_SEPARATOR + "this is" + Streams.LINE_SEPARATOR + Streams.LINE_SEPARATOR + "some text"
				+ Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file, "US-ASCII");
		assertEquals(expected, actual);
	}

	public void testWriteLines_5argsWithAppendOptionTrue_ShouldNotDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final List<String> linesToAppend = Arrays.asList(new String[] { "my first line", "The second Line" });
		Files.writeLines(file, null, linesToAppend, null, true);

		final String expected = "This line was there before you..." + "my first line" + Streams.LINE_SEPARATOR
				+ "The second Line" + Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteLines_5argsWithAppendOptionFalse_ShouldDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final List<String> linesToAppend = Arrays.asList(new String[] { "my first line", "The second Line" });
		Files.writeLines(file, null, linesToAppend, null, false);

		final String expected = "my first line" + Streams.LINE_SEPARATOR + "The second Line" + Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteLines_4argsWithAppendOptionTrue_ShouldNotDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final List<String> linesToAppend = Arrays.asList(new String[] { "my first line", "The second Line" });
		Files.writeLines(file, linesToAppend, null, true);

		final String expected = "This line was there before you..." + "my first line" + Streams.LINE_SEPARATOR
				+ "The second Line" + Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteLines_4argsWithAppendOptionFalse_ShouldDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final List<String> linesToAppend = Arrays.asList(new String[] { "my first line", "The second Line" });
		Files.writeLines(file, linesToAppend, null, false);

		final String expected = "my first line" + Streams.LINE_SEPARATOR + "The second Line" + Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteLinesEncoding_WithAppendOptionTrue_ShouldNotDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final List<String> linesToAppend = Arrays.asList(new String[] { "my first line", "The second Line" });
		Files.writeLines(file, null, linesToAppend, true);

		final String expected = "This line was there before you..." + "my first line" + Streams.LINE_SEPARATOR
				+ "The second Line" + Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteLinesEncoding_WithAppendOptionFalse_ShouldDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final List<String> linesToAppend = Arrays.asList(new String[] { "my first line", "The second Line" });
		Files.writeLines(file, null, linesToAppend, false);

		final String expected = "my first line" + Streams.LINE_SEPARATOR + "The second Line" + Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteLines_3argsWithAppendOptionTrue_ShouldNotDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final List<String> linesToAppend = Arrays.asList(new String[] { "my first line", "The second Line" });
		Files.writeLines(file, linesToAppend, true);

		final String expected = "This line was there before you..." + "my first line" + Streams.LINE_SEPARATOR
				+ "The second Line" + Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteLines_3argsWithAppendOptionFalse_ShouldDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final List<String> linesToAppend = Arrays.asList(new String[] { "my first line", "The second Line" });
		Files.writeLines(file, linesToAppend, false);

		final String expected = "my first line" + Streams.LINE_SEPARATOR + "The second Line" + Streams.LINE_SEPARATOR;
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteStringToFileWithEncoding_WithAppendOptionTrue_ShouldNotDeletePreviousFileLines()
			throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data", (String)null, true);

		final String expected = "This line was there before you..." + "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteStringToFileWithEncoding_WithAppendOptionFalse_ShouldDeletePreviousFileLines()
			throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data", (String)null, false);

		final String expected = "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteStringToFile_WithAppendOptionTrue_ShouldNotDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data", true);

		final String expected = "This line was there before you..." + "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteStringToFile_WithAppendOptionFalse_ShouldDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data", false);

		final String expected = "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteWithEncoding_WithAppendOptionTrue_ShouldNotDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data", (String)null, true);

		final String expected = "This line was there before you..." + "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteWithEncoding_WithAppendOptionFalse_ShouldDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data", (String)null, false);

		final String expected = "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWrite_WithAppendOptionTrue_ShouldNotDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data", true);

		final String expected = "This line was there before you..." + "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWrite_WithAppendOptionFalse_ShouldDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data", false);

		final String expected = "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteByteArrayToFile_WithAppendOptionTrue_ShouldNotDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data".getBytes(), true);

		final String expected = "This line was there before you..." + "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteByteArrayToFile_WithAppendOptionFalse_ShouldDeletePreviousFileLines() throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		Files.write(file, "this is brand new data".getBytes(), false);

		final String expected = "this is brand new data";
		final String actual = Files.readFileToString(file);
		assertEquals(expected, actual);
	}

	public void testWriteByteArrayToFile_WithOffsetAndLength_WithAppendOptionTrue_ShouldNotDeletePreviousFileLines()
			throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final byte[] data = "SKIP_THIS_this is brand new data_AND_SKIP_THIS".getBytes(Charsets.UTF_8);
		Files.write(file, data, 10, 22, true);

		final String expected = "This line was there before you..." + "this is brand new data";
		final String actual = Files.readFileToString(file, Charsets.UTF_8);
		assertEquals(expected, actual);
	}

	public void testWriteByteArrayToFile_WithOffsetAndLength_WithAppendOptionTrue_ShouldDeletePreviousFileLines()
			throws Exception {
		final File file = newFile("lines.txt");
		Files.write(file, "This line was there before you...");

		final byte[] data = "SKIP_THIS_this is brand new data_AND_SKIP_THIS".getBytes(Charsets.UTF_8);
		Files.write(file, data, 10, 22, false);

		final String expected = "this is brand new data";
		final String actual = Files.readFileToString(file, Charsets.UTF_8);
		assertEquals(expected, actual);
	}

	// -----------------------------------------------------------------------
	public void testChecksumCRC32() throws Exception {
		// create a test file
		final String text = "Imagination is more important than knowledge - Einstein";
		final File file = new File(getTestDirectory(), "checksum-test.txt");
		Files.write(file, text, "US-ASCII");

		// compute the expected checksum
		final Checksum expectedChecksum = new CRC32();
		expectedChecksum.update(text.getBytes("US-ASCII"), 0, text.length());
		final long expectedValue = expectedChecksum.getValue();

		// compute the checksum of the file
		final long resultValue = Files.checksumCRC32(file);

		assertEquals(expectedValue, resultValue);
	}

	public void testChecksum() throws Exception {
		// create a test file
		final String text = "Imagination is more important than knowledge - Einstein";
		final File file = new File(getTestDirectory(), "checksum-test.txt");
		Files.write(file, text, "US-ASCII");

		// compute the expected checksum
		final Checksum expectedChecksum = new CRC32();
		expectedChecksum.update(text.getBytes("US-ASCII"), 0, text.length());
		final long expectedValue = expectedChecksum.getValue();

		// compute the checksum of the file
		final Checksum testChecksum = new CRC32();
		final Checksum resultChecksum = Files.checksum(file, testChecksum);
		final long resultValue = resultChecksum.getValue();

		assertSame(testChecksum, resultChecksum);
		assertEquals(expectedValue, resultValue);
	}

	public void testChecksumOnNullFile() throws Exception {
		try {
			Files.checksum((File)null, new CRC32());
			fail();
		}
		catch (final NullPointerException ex) {
			// expected
		}
	}

	public void testChecksumOnNullChecksum() throws Exception {
		// create a test file
		final String text = "Imagination is more important than knowledge - Einstein";
		final File file = new File(getTestDirectory(), "checksum-test.txt");
		Files.write(file, text, "US-ASCII");
		try {
			Files.checksum(file, (Checksum)null);
			fail();
		}
		catch (final NullPointerException ex) {
			// expected
		}
	}

	public void testChecksumOnDirectory() throws Exception {
		try {
			Files.checksum(new File("."), new CRC32());
			fail();
		}
		catch (final IllegalArgumentException ex) {
			// expected
		}
	}

	public void testChecksumDouble() throws Exception {
		// create a test file
		final String text1 = "Imagination is more important than knowledge - Einstein";
		final File file1 = new File(getTestDirectory(), "checksum-test.txt");
		Files.write(file1, text1, "US-ASCII");

		// create a second test file
		final String text2 = "To be or not to be - Shakespeare";
		final File file2 = new File(getTestDirectory(), "checksum-test2.txt");
		Files.write(file2, text2, "US-ASCII");

		// compute the expected checksum
		final Checksum expectedChecksum = new CRC32();
		expectedChecksum.update(text1.getBytes("US-ASCII"), 0, text1.length());
		expectedChecksum.update(text2.getBytes("US-ASCII"), 0, text2.length());
		final long expectedValue = expectedChecksum.getValue();

		// compute the checksum of the file
		final Checksum testChecksum = new CRC32();
		Files.checksum(file1, testChecksum);
		Files.checksum(file2, testChecksum);
		final long resultValue = testChecksum.getValue();

		assertEquals(expectedValue, resultValue);
	}

	public void testDeleteQuietlyForNull() {
		try {
			Files.safeDelete(null);
		}
		catch (final Exception ex) {
			fail(ex.getMessage());
		}
	}

	public void testDeleteQuietlyDir() throws IOException {
		final File testDirectory = new File(getTestDirectory(), "testDeleteQuietlyDir");
		final File testFile = new File(testDirectory, "testDeleteQuietlyFile");
		testDirectory.mkdirs();
		createFile(testFile, 0);

		assertTrue(testDirectory.exists());
		assertTrue(testFile.exists());
		Files.safeDelete(testDirectory);
		assertFalse("Check No Exist", testDirectory.exists());
		assertFalse("Check No Exist", testFile.exists());
	}

	public void testDeleteQuietlyFile() throws IOException {
		final File testFile = new File(getTestDirectory(), "testDeleteQuietlyFile");
		createFile(testFile, 0);

		assertTrue(testFile.exists());
		Files.safeDelete(testFile);
		assertFalse("Check No Exist", testFile.exists());
	}

	public void testDeleteQuietlyNonExistent() {
		final File testFile = new File("testDeleteQuietlyNonExistent");
		assertFalse(testFile.exists());

		try {
			Files.safeDelete(testFile);
		}
		catch (final Exception ex) {
			fail(ex.getMessage());
		}
	}

	public void testMoveFile_Rename() throws Exception {
		final File destination = new File(getTestDirectory(), "move1.txt");

		Files.moveFile(testFile1, destination);
		assertTrue("Check Exist", destination.exists());
		assertTrue("Original deleted", !testFile1.exists());
	}

	public void testMoveFile_CopyDelete() throws Exception {
		final File destination = new File(getTestDirectory(), "move2.txt");
		final File src = new File(testFile1.getAbsolutePath()) {
			// Force renameTo to fail, as if destination is on another
			// filesystem
			@Override
			public boolean renameTo(final File f) {
				return false;
			}
		};
		Files.moveFile(src, destination);
		assertTrue("Check Exist", destination.exists());
		assertTrue("Original deleted", !src.exists());
	}

	public void testMoveFile_CopyDelete_Failed() throws Exception {
		final File destination = new File(getTestDirectory(), "move3.txt");
		final File src = new File(testFile1.getAbsolutePath()) {
			// Force renameTo to fail, as if destination is on another
			// filesystem
			@Override
			public boolean renameTo(final File f) {
				return false;
			}

			// Force delete failure
			@Override
			public boolean delete() {
				return false;
			}

		};
		try {
			Files.moveFile(src, destination);
			fail("move should have failed as src has not been deleted");
		}
		catch (final IOException e) {
			// exepected
			assertTrue("Check Rollback", !destination.exists());
			assertTrue("Original exists", src.exists());
		}
	}

	public void testMoveFile_Errors() throws Exception {
		try {
			Files.moveFile(null, new File("foo"));
			fail("Expected NullPointerException when source is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		try {
			Files.moveFile(new File("foo"), null);
			fail("Expected NullPointerException when destination is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		try {
			Files.moveFile(new File("nonexistant"), new File("foo"));
			fail("Expected FileNotFoundException for source");
		}
		catch (final FileNotFoundException e) {
			// expected
		}
		try {
			Files.moveFile(getTestDirectory(), new File("foo"));
			fail("Expected IOException when source is a directory");
		}
		catch (final IOException e) {
			// expected
		}
		final File testSourceFile = new File(getTestDirectory(), "testMoveFileSource");
		final File testDestFile = new File(getTestDirectory(), "testMoveFileSource");
		createFile(testSourceFile, 0);
		createFile(testDestFile, 0);
		try {
			Files.moveFile(testSourceFile, testDestFile);
			fail("Expected FileExistsException when dest already exists");
		}
		catch (final FileExistsException e) {
			// expected
		}

	}

	public void testMoveFileToDirectory() throws Exception {
		final File destDir = new File(getTestDirectory(), "moveFileDestDir");
		final File movedFile = new File(destDir, testFile1.getName());
		assertFalse("Check Exist before", destDir.exists());
		assertFalse("Check Exist before", movedFile.exists());

		Files.moveFileToDiry(testFile1, destDir, true);
		assertTrue("Check Exist after", movedFile.exists());
		assertTrue("Original deleted", !testFile1.exists());
	}

	public void testMoveFileToDirectory_Errors() throws Exception {
		try {
			Files.moveFileToDiry(null, new File("foo"), true);
			fail("Expected NullPointerException when source is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		try {
			Files.moveFileToDiry(new File("foo"), null, true);
			fail("Expected NullPointerException when destination is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		final File testFile1 = new File(getTestDirectory(), "testMoveFileFile1");
		final File testFile2 = new File(getTestDirectory(), "testMoveFileFile2");
		createFile(testFile1, 0);
		createFile(testFile2, 0);
		try {
			Files.moveFileToDiry(testFile1, testFile2, true);
			fail("Expected IOException when dest not a directory");
		}
		catch (final IOException e) {
			// expected
		}

		final File nonexistant = new File(getTestDirectory(), "testMoveFileNonExistant");
		try {
			Files.moveFileToDiry(testFile1, nonexistant, false);
			fail("Expected IOException when dest does not exist and create=false");
		}
		catch (final IOException e) {
			// expected
		}
	}

	public void testMoveDirectory_Rename() throws Exception {
		final File dir = getTestDirectory();
		final File src = new File(dir, "testMoveDirectory1Source");
		final File testDir = new File(src, "foo");
		final File testFile = new File(testDir, "bar");
		testDir.mkdirs();
		createFile(testFile, 0);
		final File destination = new File(dir, "testMoveDirectory1Dest");
		Files.deleteDir(destination);

		// Move the directory
		Files.moveDir(src, destination);

		// Check results
		assertTrue("Check Exist", destination.exists());
		assertTrue("Original deleted", !src.exists());
		final File movedDir = new File(destination, testDir.getName());
		final File movedFile = new File(movedDir, testFile.getName());
		assertTrue("Check dir moved", movedDir.exists());
		assertTrue("Check file moved", movedFile.exists());
	}

	public void testMoveDirectory_CopyDelete() throws Exception {

		final File dir = getTestDirectory();
		final File src = new File(dir, "testMoveDirectory2Source") {

			// Force renameTo to fail
			@Override
			public boolean renameTo(final File dest) {
				return false;
			}
		};
		final File testDir = new File(src, "foo");
		final File testFile = new File(testDir, "bar");
		testDir.mkdirs();
		createFile(testFile, 0);
		final File destination = new File(dir, "testMoveDirectory1Dest");
		Files.deleteDir(destination);

		// Move the directory
		Files.moveDir(src, destination);

		// Check results
		assertTrue("Check Exist", destination.exists());
		assertTrue("Original deleted", !src.exists());
		final File movedDir = new File(destination, testDir.getName());
		final File movedFile = new File(movedDir, testFile.getName());
		assertTrue("Check dir moved", movedDir.exists());
		assertTrue("Check file moved", movedFile.exists());
	}

	public void testMoveDirectory_Errors() throws Exception {
		try {
			Files.moveDir(null, new File("foo"));
			fail("Expected NullPointerException when source is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		try {
			Files.moveDir(new File("foo"), null);
			fail("Expected NullPointerException when destination is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		try {
			Files.moveDir(new File("nonexistant"), new File("foo"));
			fail("Expected FileNotFoundException for source");
		}
		catch (final FileNotFoundException e) {
			// expected
		}
		final File testFile = new File(getTestDirectory(), "testMoveDirectoryFile");
		createFile(testFile, 0);
		try {
			Files.moveDir(testFile, new File("foo"));
			fail("Expected IOException when source is not a directory");
		}
		catch (final IOException e) {
			// expected
		}
		final File testSrcFile = new File(getTestDirectory(), "testMoveDirectorySource");
		final File testDestFile = new File(getTestDirectory(), "testMoveDirectoryDest");
		testSrcFile.mkdir();
		testDestFile.mkdir();
		try {
			Files.moveDir(testSrcFile, testDestFile);
			fail("Expected FileExistsException when dest already exists");
		}
		catch (final FileExistsException e) {
			// expected
		}

	}

	public void testMoveDirectoryToDirectory() throws Exception {
		final File dir = getTestDirectory();
		final File src = new File(dir, "testMoveDirectory1Source");
		final File testChildDir = new File(src, "foo");
		final File testFile = new File(testChildDir, "bar");
		testChildDir.mkdirs();
		createFile(testFile, 0);
		final File destDir = new File(dir, "testMoveDirectory1Dest");
		Files.deleteDir(destDir);
		assertFalse("Check Exist before", destDir.exists());

		// Move the directory
		Files.moveDirToDir(src, destDir, true);

		// Check results
		assertTrue("Check Exist after", destDir.exists());
		assertTrue("Original deleted", !src.exists());
		final File movedDir = new File(destDir, src.getName());
		final File movedChildDir = new File(movedDir, testChildDir.getName());
		final File movedFile = new File(movedChildDir, testFile.getName());
		assertTrue("Check dir moved", movedDir.exists());
		assertTrue("Check child dir moved", movedChildDir.exists());
		assertTrue("Check file moved", movedFile.exists());
	}

	public void testMoveDirectoryToDirectory_Errors() throws Exception {
		try {
			Files.moveDirToDir(null, new File("foo"), true);
			fail("Expected NullPointerException when source is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		try {
			Files.moveDirToDir(new File("foo"), null, true);
			fail("Expected NullPointerException when destination is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		final File testFile1 = new File(getTestDirectory(), "testMoveFileFile1");
		final File testFile2 = new File(getTestDirectory(), "testMoveFileFile2");
		createFile(testFile1, 0);
		createFile(testFile2, 0);
		try {
			Files.moveDirToDir(testFile1, testFile2, true);
			fail("Expected IOException when dest not a directory");
		}
		catch (final IOException e) {
			// expected
		}

		final File nonexistant = new File(getTestDirectory(), "testMoveFileNonExistant");
		try {
			Files.moveDirToDir(testFile1, nonexistant, false);
			fail("Expected IOException when dest does not exist and create=false");
		}
		catch (final IOException e) {
			// expected
		}
	}

	public void testMoveToDirectory() throws Exception {
		final File destDir = new File(getTestDirectory(), "testMoveToDirectoryDestDir");
		final File testDir = new File(getTestDirectory(), "testMoveToDirectoryTestDir");
		final File testFile = new File(getTestDirectory(), "testMoveToDirectoryTestFile");
		testDir.mkdirs();
		createFile(testFile, 0);
		final File movedFile = new File(destDir, testFile.getName());
		final File movedDir = new File(destDir, testFile.getName());

		assertFalse("Check File Doesnt exist", movedFile.exists());
		assertFalse("Check Dir Doesnt exist", movedDir.exists());

		// Test moving a file
		Files.moveToDir(testFile, destDir, true);
		assertTrue("Check File exists", movedFile.exists());
		assertFalse("Check Original File doesn't exist", testFile.exists());

		// Test moving a directory
		Files.moveToDir(testDir, destDir, true);
		assertTrue("Check Dir exists", movedDir.exists());
		assertFalse("Check Original Dir doesn't exist", testDir.exists());
	}

	public void testMoveToDirectory_Errors() throws Exception {
		try {
			Files.moveDirToDir(null, new File("foo"), true);
			fail("Expected NullPointerException when source is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		try {
			Files.moveDirToDir(new File("foo"), null, true);
			fail("Expected NullPointerException when destination is null");
		}
		catch (final NullPointerException e) {
			// expected
		}
		final File nonexistant = new File(getTestDirectory(), "nonexistant");
		final File destDir = new File(getTestDirectory(), "MoveToDirectoryDestDir");
		try {
			Files.moveToDir(nonexistant, destDir, true);
			fail("Expected IOException when source does not exist");
		}
		catch (final IOException e) {
			// expected
		}
	}

	public void testIO300() throws Exception {
		final File testDirectory = getTestDirectory();
		final File src = new File(testDirectory, "dir1");
		final File dest = new File(src, "dir2");
		assertTrue(dest.mkdirs());
		assertTrue(src.exists());
		try {
			Files.moveDirToDir(src, dest, false);
			fail("expected IOException");
		}
		catch (final IOException ioe) {
			// expected
		}
		assertTrue(src.exists());
	}

	public void testIO276() throws Exception {
		final File testDirectory = getTestDirectory();
		final File src = new File(testDirectory, "target");
		final File dir = new File(src, "IO276");
		assertTrue(dir + " should not be present", dir.mkdirs());
		final File file = new File(dir, "IO276.txt");
		assertTrue(file + " should not be present", file.createNewFile());
		Files.forceDeleteOnExit(dir);
		// If this does not work, test will fail next time (assuming target is not cleaned)
	}

	// Test helper class to pretend a file is shorter than it is
	private static class ShorterFile extends File {
		public ShorterFile(File parent, String child) {
			super(parent, child);
		}

		public ShorterFile(String parent, String child) {
			super(parent, child);
		}

		public ShorterFile(String pathname) {
			super(pathname);
		}

		@Override
		public long length() {
			return super.length() - 1;
		}
	}

	// This test relies on Files.copyFile using File.length to check the output size
	public void testIncorrectOutputSize() throws Exception {
		File inFile = new File("build.xml");
		File outFile = new ShorterFile(getTestDirectory(), "target/build.tmp"); // it will report a shorter file
		try {
			Files.copyFile(inFile, outFile);
			fail("Expected IOException");
		}
		catch (Exception e) {
			final String msg = e.toString();
			assertTrue(msg, msg.contains("Failed to copy full contents"));
		}
		finally {
			outFile.delete(); // tidy up
		}
	}

	/**
	 * DirectoryWalker implementation that recursively lists all files and directories.
	 */
	static class ListDirectoryWalker extends DirectoryWalker<File> {
		ListDirectoryWalker() {
			super();
		}

		List<File> list(final File startDirectory) throws IOException {
			final ArrayList<File> files = new ArrayList<File>();
			walk(startDirectory, files);
			return files;
		}

		@Override
		protected void handleDirectoryStart(final File directory, final int depth, final Collection<File> results)
				throws IOException {
			// Add all directories except the starting directory
			if (depth > 0) {
				results.add(directory);
			}
		}

		@Override
		protected void handleFile(final File file, final int depth, final Collection<File> results) throws IOException {
			results.add(file);
		}
	}

}
