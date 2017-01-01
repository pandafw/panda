package panda.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for Files.cleanDirectory() method.
 */
public class FilesCleanDirectoryTest extends FileBasedTestCase {
	final File top = getLocalTestDirectory();

	public FilesCleanDirectoryTest(final String name) {
		super(name);
	}

	private File getLocalTestDirectory() {
		return new File(getTestDirectory(), "list-files");
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		top.mkdirs();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		chmod(top, 775, true);
		Files.deleteDir(top);
	}

	// -----------------------------------------------------------------------
	public void testCleanEmpty() throws Exception {
		assertEquals(0, top.list().length);

		Files.cleanDir(top);

		assertEquals(0, top.list().length);
	}

	public void testDeletesRegular() throws Exception {
		Files.touch(new File(top, "regular"));
		Files.touch(new File(top, ".hidden"));

		assertEquals(2, top.list().length);

		Files.cleanDir(top);

		assertEquals(0, top.list().length);
	}

	public void testDeletesNested() throws Exception {
		final File nested = new File(top, "nested");

		assertTrue(nested.mkdirs());

		Files.touch(new File(nested, "file"));

		assertEquals(1, top.list().length);

		Files.cleanDir(top);

		assertEquals(0, top.list().length);
	}

	public void testThrowsOnNullList() throws Exception {
		if (System.getProperty("os.name").startsWith("Win") || !chmod(top, 0, false)) {
			// test wont work if we can't restrict permissions on the
			// directory, so skip it.
			return;
		}

		try {
			Files.cleanDir(top);
			fail("expected IOException");
		}
		catch (final IOException e) {
			assertEquals("Failed to list contents of " + top.getAbsolutePath(), e.getMessage());
		}
	}

	public void testThrowsOnCannotDeleteFile() throws Exception {
		final File file = new File(top, "restricted");
		Files.touch(file);

		if (System.getProperty("os.name").startsWith("Win") || !chmod(top, 500, false)) {
			// test wont work if we can't restrict permissions on the
			// directory, so skip it.
			return;
		}

		try {
			Files.cleanDir(top);
			fail("expected IOException");
		}
		catch (final IOException e) {
			assertEquals("Unable to delete file: " + file.getAbsolutePath(), e.getMessage());
		}
	}

	private boolean chmod(final File file, final int mode, final boolean recurse) throws InterruptedException {
		// TODO: Refactor this to FileSystemUtils
		final List<String> args = new ArrayList<String>();
		args.add("chmod");

		if (recurse) {
			args.add("-R");
		}

		args.add(Integer.toString(mode));
		args.add(file.getAbsolutePath());

		Process proc;

		try {
			proc = Runtime.getRuntime().exec(args.toArray(new String[args.size()]));
		}
		catch (final IOException e) {
			return false;
		}
		final int result = proc.waitFor();
		return result == 0;
	}

}
