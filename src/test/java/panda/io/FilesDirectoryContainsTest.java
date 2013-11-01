package panda.io;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * This class ensure the correctness of {@link Files#directoryContains(File,File)}.
 * 
 * @see Files#directoryContains(File, File)
 */
public class FilesDirectoryContainsTest extends FileBasedTestCase {

	private File directory1;
	private File directory2;
	private File directory3;
	private File file1;
	private File file1ByRelativeDirectory2;
	private File file2;
	private File file2ByRelativeDirectory1;
	private File file3;
	final File top = getTestDirectory();

	public FilesDirectoryContainsTest(final String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		top.mkdirs();

		directory1 = new File(top, "directory1");
		directory2 = new File(top, "directory2");
		directory3 = new File(directory2, "directory3");

		directory1.mkdir();
		directory2.mkdir();
		directory3.mkdir();

		file1 = new File(directory1, "file1");
		file2 = new File(directory2, "file2");
		file3 = new File(top, "file3");

		// Tests case with relative path
		file1ByRelativeDirectory2 = new File(getTestDirectory(), "directory2/../directory1/file1");
		file2ByRelativeDirectory1 = new File(getTestDirectory(), "directory1/../directory2/file2");

		Files.touch(file1);
		Files.touch(file2);
		Files.touch(file3);
	}

	@Override
	protected void tearDown() throws Exception {
		Files.deleteDir(top);
	}

	@Test
	public void testCanonicalPath() throws IOException {
		assertTrue(Files.directoryContains(directory1, file1ByRelativeDirectory2));
		assertTrue(Files.directoryContains(directory2, file2ByRelativeDirectory1));

		assertFalse(Files.directoryContains(directory1, file2ByRelativeDirectory1));
		assertFalse(Files.directoryContains(directory2, file1ByRelativeDirectory2));
	}

	@Test
	public void testDirectoryContainsDirectory() throws IOException {
		assertTrue(Files.directoryContains(top, directory1));
		assertTrue(Files.directoryContains(top, directory2));
		assertTrue(Files.directoryContains(top, directory3));
		assertTrue(Files.directoryContains(directory2, directory3));
	}

	@Test
	public void testDirectoryContainsFile() throws IOException {
		assertTrue(Files.directoryContains(directory1, file1));
		assertTrue(Files.directoryContains(directory2, file2));
	}

	@Test
	public void testDirectoryDoesNotContainFile() throws IOException {
		assertFalse(Files.directoryContains(directory1, file2));
		assertFalse(Files.directoryContains(directory2, file1));

		assertFalse(Files.directoryContains(directory1, file3));
		assertFalse(Files.directoryContains(directory2, file3));
	}

	@Test
	public void testDirectoryDoesNotContainsDirectory() throws IOException {
		assertFalse(Files.directoryContains(directory1, top));
		assertFalse(Files.directoryContains(directory2, top));
		assertFalse(Files.directoryContains(directory3, top));
		assertFalse(Files.directoryContains(directory3, directory2));
	}

	@Test
	public void testDirectoryDoesNotExist() throws IOException {
		final File dir = new File("DOESNOTEXIST");
		assertFalse(dir.exists());
		try {
			assertFalse(Files.directoryContains(dir, file1));
			fail("Expected " + IllegalArgumentException.class.getName());
		}
		catch (final IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testSameFile() throws IOException {
		try {
			assertTrue(Files.directoryContains(file1, file1));
			fail("Expected " + IllegalArgumentException.class.getName());
		}
		catch (final IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testFileDoesNotExist() throws IOException {
		assertFalse(Files.directoryContains(top, null));
		final File file = new File("DOESNOTEXIST");
		assertFalse(file.exists());
		assertFalse(Files.directoryContains(top, file));
	}

	/**
	 * Test to demonstrate a file which does not exist returns false
	 */
	@Test
	public void testFileDoesNotExistBug() throws IOException {
		final File file = new File(top, "DOESNOTEXIST");
		assertTrue("Check directory exists", top.exists());
		assertFalse("Check file does not exist", file.exists());
		assertFalse("Direcory does not contain unrealized file", Files.directoryContains(top, file));
	}

	@Test
	public void testUnrealizedContainment() throws IOException {
		final File dir = new File("DOESNOTEXIST");
		final File file = new File(dir, "DOESNOTEXIST2");
		assertFalse(dir.exists());
		assertFalse(file.exists());
		try {
			assertTrue(Files.directoryContains(dir, file));
		}
		catch (final IllegalArgumentException e) {
			// expected
		}
	}
}
