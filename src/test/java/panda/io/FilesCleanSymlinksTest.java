package panda.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for Files.cleanDirectory() method that involve symlinks. & Files.isSymlink(File file)
 */
public class FilesCleanSymlinksTest extends FileBasedTestCase {

	final File top = getTestDirectory();

	public FilesCleanSymlinksTest(final String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		top.mkdirs();
	}

	@Override
	protected void tearDown() throws Exception {
		Files.deleteDir(top);
	}

	public void testCleanDirWithSymlinkFile() throws Exception {
		if (System.getProperty("os.name").startsWith("Win")) {
			// cant create symlinks in windows.
			return;
		}

		final File realOuter = new File(top, "realouter");
		assertTrue(realOuter.mkdirs());

		final File realInner = new File(realOuter, "realinner");
		assertTrue(realInner.mkdirs());

		final File realFile = new File(realInner, "file1");
		Files.touch(realFile);
		assertEquals(1, realInner.list().length);

		final File randomFile = new File(top, "randomfile");
		Files.touch(randomFile);

		final File symlinkFile = new File(realInner, "fakeinner");
		setupSymlink(randomFile, symlinkFile);

		assertEquals(2, realInner.list().length);

		// assert contents of the real directory were removed including the symlink
		Files.cleanDir(realOuter);
		assertEquals(0, realOuter.list().length);

		// ensure that the contents of the symlink were NOT removed.
		assertTrue(randomFile.exists());
		assertFalse(symlinkFile.exists());
	}

	public void testCleanDirWithASymlinkDir() throws Exception {
		if (System.getProperty("os.name").startsWith("Win")) {
			// cant create symlinks in windows.
			return;
		}

		final File realOuter = new File(top, "realouter");
		assertTrue(realOuter.mkdirs());

		final File realInner = new File(realOuter, "realinner");
		assertTrue(realInner.mkdirs());

		Files.touch(new File(realInner, "file1"));
		assertEquals(1, realInner.list().length);

		final File randomDirectory = new File(top, "randomDir");
		assertTrue(randomDirectory.mkdirs());

		Files.touch(new File(randomDirectory, "randomfile"));
		assertEquals(1, randomDirectory.list().length);

		final File symlinkDirectory = new File(realOuter, "fakeinner");
		setupSymlink(randomDirectory, symlinkDirectory);

		assertEquals(1, symlinkDirectory.list().length);

		// assert contents of the real directory were removed including the symlink
		Files.cleanDir(realOuter);
		assertEquals(0, realOuter.list().length);

		// ensure that the contents of the symlink were NOT removed.
		assertEquals("Contents of sym link should not have been removed", 1, randomDirectory.list().length);
	}

	public void testCleanDirWithParentSymlinks() throws Exception {
		if (System.getProperty("os.name").startsWith("Win")) {
			// cant create symlinks in windows.
			return;
		}

		final File realParent = new File(top, "realparent");
		assertTrue(realParent.mkdirs());

		final File realInner = new File(realParent, "realinner");
		assertTrue(realInner.mkdirs());

		Files.touch(new File(realInner, "file1"));
		assertEquals(1, realInner.list().length);

		final File randomDirectory = new File(top, "randomDir");
		assertTrue(randomDirectory.mkdirs());

		Files.touch(new File(randomDirectory, "randomfile"));
		assertEquals(1, randomDirectory.list().length);

		final File symlinkDirectory = new File(realParent, "fakeinner");
		setupSymlink(randomDirectory, symlinkDirectory);

		assertEquals(1, symlinkDirectory.list().length);

		final File symlinkParentDirectory = new File(top, "fakeouter");
		setupSymlink(realParent, symlinkParentDirectory);

		// assert contents of the real directory were removed including the symlink
		Files.cleanDir(symlinkParentDirectory);// should clean the contents of this but not
														// recurse into other links
		assertEquals(0, symlinkParentDirectory.list().length);
		assertEquals(0, realParent.list().length);

		// ensure that the contents of the symlink were NOT removed.
		assertEquals("Contents of sym link should not have been removed", 1, randomDirectory.list().length);
	}

	public void testStillClearsIfGivenDirectoryIsASymlink() throws Exception {
		if (System.getProperty("os.name").startsWith("Win")) {
			// cant create symlinks in windows.
			return;
		}

		final File randomDirectory = new File(top, "randomDir");
		assertTrue(randomDirectory.mkdirs());

		Files.touch(new File(randomDirectory, "randomfile"));
		assertEquals(1, randomDirectory.list().length);

		final File symlinkDirectory = new File(top, "fakeDir");
		setupSymlink(randomDirectory, symlinkDirectory);

		Files.cleanDir(symlinkDirectory);
		assertEquals(0, symlinkDirectory.list().length);
		assertEquals(0, randomDirectory.list().length);
	}

	public void testIdentifiesSymlinkDir() throws Exception {
		if (System.getProperty("os.name").startsWith("Win")) {
			// cant create symlinks in windows.
			return;
		}

		final File randomDirectory = new File(top, "randomDir");
		assertTrue(randomDirectory.mkdirs());

		final File symlinkDirectory = new File(top, "fakeDir");
		setupSymlink(randomDirectory, symlinkDirectory);

		assertTrue(Files.isSymlink(symlinkDirectory));
		assertFalse(Files.isSymlink(randomDirectory));
	}

	public void testIdentifiesSymlinkFile() throws Exception {
		if (System.getProperty("os.name").startsWith("Win")) {
			// cant create symlinks in windows.
			return;
		}

		final File randomFile = new File(top, "randomfile");
		Files.touch(randomFile);

		final File symlinkFile = new File(top, "fakeinner");
		setupSymlink(randomFile, symlinkFile);

		assertTrue(Files.isSymlink(symlinkFile));
		assertFalse(Files.isSymlink(randomFile));
	}

	public void testCorrectlyIdentifySymlinkWithParentSymLink() throws Exception {
		if (System.getProperty("os.name").startsWith("Win")) {
			// cant create symlinks in windows.
			return;
		}

		final File realParent = new File(top, "realparent");
		assertTrue(realParent.mkdirs());

		final File symlinkParentDirectory = new File(top, "fakeparent");
		setupSymlink(realParent, symlinkParentDirectory);

		final File realChild = new File(symlinkParentDirectory, "realChild");
		assertTrue(realChild.mkdirs());

		final File symlinkChild = new File(symlinkParentDirectory, "fakeChild");
		setupSymlink(realChild, symlinkChild);

		assertTrue(Files.isSymlink(symlinkChild));
		assertFalse(Files.isSymlink(realChild));
	}

	private void setupSymlink(final File res, final File link) throws Exception {
		// create symlink
		final List<String> args = new ArrayList<String>();
		args.add("ln");
		args.add("-s");

		args.add(res.getAbsolutePath());
		args.add(link.getAbsolutePath());

		Process proc;

		proc = Runtime.getRuntime().exec(args.toArray(new String[args.size()]));
		proc.waitFor();
	}

}
