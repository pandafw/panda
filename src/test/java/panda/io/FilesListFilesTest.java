package panda.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import panda.io.filter.FileFilters;
import panda.io.filter.IOFileFilter;

/**
 * Test cases for Files.listFiles() methods.
 */
public class FilesListFilesTest extends FileBasedTestCase {

	public FilesListFilesTest(final String name) {
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
		File dir = getLocalTestDirectory();
		if (dir.exists()) {
			Files.deleteDir(dir);
		}
		dir.mkdirs();
		File file = new File(dir, "dummy-build.xml");
		Files.touch(file);
		file = new File(dir, "README");
		Files.touch(file);

		dir = new File(dir, "subdir1");
		dir.mkdirs();
		file = new File(dir, "dummy-build.xml");
		Files.touch(file);
		file = new File(dir, "dummy-readme.txt");
		Files.touch(file);

		dir = new File(dir, "subsubdir1");
		dir.mkdirs();
		file = new File(dir, "dummy-file.txt");
		Files.touch(file);
		file = new File(dir, "dummy-index.html");
		Files.touch(file);

		dir = dir.getParentFile();
		dir = new File(dir, "CVS");
		dir.mkdirs();
		file = new File(dir, "Entries");
		Files.touch(file);
		file = new File(dir, "Repository");
		Files.touch(file);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		final File dir = getLocalTestDirectory();
		Files.deleteDir(dir);
	}

	private Collection<String> filesToFilenames(final Collection<File> files) {
		final Collection<String> filenames = new ArrayList<String>(files.size());
		for (final File file : files) {
			filenames.add(file.getName());
		}
		return filenames;
	}

	private Collection<String> filesToFilenames(final Iterator<File> files) {
		final Collection<String> filenames = new ArrayList<String>();
		while (files.hasNext()) {
			filenames.add(files.next().getName());
		}
		return filenames;
	}

	public void testIterateFilesByExtension() throws Exception {
		final String[] extensions = { "xml", "txt" };

		Iterator<File> files = Files.iterateFiles(getLocalTestDirectory(), extensions, false);
		Collection<String> filenames = filesToFilenames(files);
		assertEquals(1, filenames.size());
		assertTrue(filenames.contains("dummy-build.xml"));
		assertFalse(filenames.contains("README"));
		assertFalse(filenames.contains("dummy-file.txt"));

		files = Files.iterateFiles(getLocalTestDirectory(), extensions, true);
		filenames = filesToFilenames(files);
		assertEquals(4, filenames.size());
		assertTrue(filenames.contains("dummy-file.txt"));
		assertFalse(filenames.contains("dummy-index.html"));

		files = Files.iterateFiles(getLocalTestDirectory(), (String[])null, false);
		filenames = filesToFilenames(files);
		assertEquals(2, filenames.size());
		assertTrue(filenames.contains("dummy-build.xml"));
		assertTrue(filenames.contains("README"));
		assertFalse(filenames.contains("dummy-file.txt"));
	}

	public void testListFilesByExtension() throws Exception {
		final String[] extensions = { "xml", "txt" };

		Collection<File> files = Files.listFiles(getLocalTestDirectory(), extensions, false);
		assertEquals(1, files.size());
		Collection<String> filenames = filesToFilenames(files);
		assertTrue(filenames.contains("dummy-build.xml"));
		assertFalse(filenames.contains("README"));
		assertFalse(filenames.contains("dummy-file.txt"));

		files = Files.listFiles(getLocalTestDirectory(), extensions, true);
		filenames = filesToFilenames(files);
		assertEquals(4, filenames.size());
		assertTrue(filenames.contains("dummy-file.txt"));
		assertFalse(filenames.contains("dummy-index.html"));

		files = Files.listFiles(getLocalTestDirectory(), (String[])null, false);
		assertEquals(2, files.size());
		filenames = filesToFilenames(files);
		assertTrue(filenames.contains("dummy-build.xml"));
		assertTrue(filenames.contains("README"));
		assertFalse(filenames.contains("dummy-file.txt"));
	}

	public void testListFiles() throws Exception {
		Collection<File> files;
		Collection<String> filenames;
		IOFileFilter fileFilter;
		IOFileFilter dirFilter;

		// First, find non-recursively
		fileFilter = FileFilters.trueFileFilter();
		files = Files.listFiles(getLocalTestDirectory(), fileFilter, (IOFileFilter)null);
		filenames = filesToFilenames(files);
		assertTrue("'dummy-build.xml' is missing", filenames.contains("dummy-build.xml"));
		assertFalse("'dummy-index.html' shouldn't be found", filenames.contains("dummy-index.html"));
		assertFalse("'Entries' shouldn't be found", filenames.contains("Entries"));

		// Second, find recursively
		fileFilter = FileFilters.trueFileFilter();
		dirFilter = FileFilters.notFileFilter(FileFilters.nameFileFilter("CVS"));
		files = Files.listFiles(getLocalTestDirectory(), fileFilter, dirFilter);
		filenames = filesToFilenames(files);
		assertTrue("'dummy-build.xml' is missing", filenames.contains("dummy-build.xml"));
		assertTrue("'dummy-index.html' is missing", filenames.contains("dummy-index.html"));
		assertFalse("'Entries' shouldn't be found", filenames.contains("Entries"));

		// Do the same as above but now with the filter coming from FileFilterUtils
		fileFilter = FileFilters.trueFileFilter();
		dirFilter = FileFilters.makeCVSAware(null);
		files = Files.listFiles(getLocalTestDirectory(), fileFilter, dirFilter);
		filenames = filesToFilenames(files);
		assertTrue("'dummy-build.xml' is missing", filenames.contains("dummy-build.xml"));
		assertTrue("'dummy-index.html' is missing", filenames.contains("dummy-index.html"));
		assertFalse("'Entries' shouldn't be found", filenames.contains("Entries"));

		// Again with the CVS filter but now with a non-null parameter
		fileFilter = FileFilters.trueFileFilter();
		dirFilter = FileFilters.prefixFileFilter("sub");
		dirFilter = FileFilters.makeCVSAware(dirFilter);
		files = Files.listFiles(getLocalTestDirectory(), fileFilter, dirFilter);
		filenames = filesToFilenames(files);
		assertTrue("'dummy-build.xml' is missing", filenames.contains("dummy-build.xml"));
		assertTrue("'dummy-index.html' is missing", filenames.contains("dummy-index.html"));
		assertFalse("'Entries' shouldn't be found", filenames.contains("Entries"));

		try {
			Files.listFiles(getLocalTestDirectory(), (IOFileFilter)null, (IOFileFilter)null);
			fail("Expected error about null parameter");
		}
		catch (final NullPointerException e) {
			// expected
		}
	}

}
