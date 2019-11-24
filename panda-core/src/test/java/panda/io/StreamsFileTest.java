package panda.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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
}
