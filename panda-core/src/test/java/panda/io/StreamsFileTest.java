package panda.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Assert;

/**
 * This is used to test Files for correctness.
 * 
 * @see Files
 */
public class StreamsFileTest extends FileBasedTestCase {
	private final File testFile1;
	private final File testFile2;

	private final int testFile1Size;
	private final int testFile2Size;


	public StreamsFileTest(final String name) {
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
	public void test_openInputStream_exists() throws Exception {
		final File file = new File(getTestDirectory(), "test.txt");
		createLineBasedFile(file, new String[] { "Hello" });
		FileInputStream in = null;
		try {
			in = Streams.openInputStream(file);
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
			in = Streams.openInputStream(directory);
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
			in = Streams.openInputStream(directory);
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
				out = Streams.openOutputStream(file);
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
			out = Streams.openOutputStream(file);
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
			out = Streams.openOutputStream(directory);
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
			out = Streams.openOutputStream(file);
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
			out = Streams.openOutputStream(file);
			fail();
		}
		catch (final IOException ioe) {
			// expected
		}
		finally {
			Streams.safeClose(out);
		}
	}


	public void testCopyFileToOutputStream() throws Exception {
		final ByteArrayOutputStream destination = new ByteArrayOutputStream();
		Files.copyFile(testFile1, destination);
		assertEquals("Check Full copy size", testFile1Size, destination.size());
		final byte[] expected = Streams.toByteArray(testFile1);
		Assert.assertArrayEquals("Check Full copy", expected, destination.toByteArray());
	}


	public void testReadFileToStringWithDefaultEncoding() throws Exception {
		final File file = new File(getTestDirectory(), "read.obj");
		final FileOutputStream out = new FileOutputStream(file);
		final byte[] text = "Hello /u1234".getBytes();
		out.write(text);
		out.close();

		final String data = Streams.toString(file);
		assertEquals("Hello /u1234", data);
	}

	public void testReadFileToStringWithEncoding() throws Exception {
		final File file = new File(getTestDirectory(), "read.obj");
		final FileOutputStream out = new FileOutputStream(file);
		final byte[] text = "Hello /u1234".getBytes("UTF8");
		out.write(text);
		out.close();

		final String data = Streams.toString(file, "UTF8");
		assertEquals("Hello /u1234", data);
	}

	public void testReadFileToByteArray() throws Exception {
		final File file = new File(getTestDirectory(), "read.txt");
		final FileOutputStream out = new FileOutputStream(file);
		out.write(11);
		out.write(21);
		out.write(31);
		out.close();

		final byte[] data = Streams.toByteArray(file);
		assertEquals(3, data.length);
		assertEquals(11, data[0]);
		assertEquals(21, data[1]);
		assertEquals(31, data[2]);
	}
}
