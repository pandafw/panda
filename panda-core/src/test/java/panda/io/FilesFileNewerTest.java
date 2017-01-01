package panda.io;

import java.io.File;
import java.util.Date;

/**
 * This is used to test Files for correctness.
 */
public class FilesFileNewerTest extends FileBasedTestCase {

	// Test data
	private static final int FILE1_SIZE = 1;
	private static final int FILE2_SIZE = 1024 * 4 + 1;

	private final File m_testFile1;
	private final File m_testFile2;

	public FilesFileNewerTest(final String name) {
		super(name);

		m_testFile1 = new File(getTestDirectory(), "file1-test.txt");
		m_testFile2 = new File(getTestDirectory(), "file2-test.txt");
	}

	/** @see junit.framework.TestCase#setUp() */
	@Override
	protected void setUp() throws Exception {
		getTestDirectory().mkdirs();
		createFile(m_testFile1, FILE1_SIZE);
		createFile(m_testFile2, FILE2_SIZE);
	}

	/** @see junit.framework.TestCase#tearDown() */
	@Override
	protected void tearDown() throws Exception {
		m_testFile1.delete();
		m_testFile2.delete();
	}

	/**
	 * Tests the <code>isFileNewer(File, *)</code> methods which a "normal" file.
	 * 
	 * @see Files#isFileNewer(File, long)
	 * @see Files#isFileNewer(File, Date)
	 * @see Files#isFileNewer(File, File)
	 */
	public void testIsFileNewer() {
		if (!m_testFile1.exists()) {
			throw new IllegalStateException("The m_testFile1 should exist");
		}

		final long fileLastModified = m_testFile1.lastModified();
		final long TWO_SECOND = 2000;

		testIsFileNewer("two second earlier is not newer", m_testFile1, fileLastModified + TWO_SECOND, false);
		testIsFileNewer("same time is not newer", m_testFile1, fileLastModified, false);
		testIsFileNewer("two second later is newer", m_testFile1, fileLastModified - TWO_SECOND, true);
	}

	/**
	 * Tests the <code>isFileNewer(File, *)</code> methods which a not existing file.
	 * 
	 * @see Files#isFileNewer(File, long)
	 * @see Files#isFileNewer(File, Date)
	 * @see Files#isFileNewer(File, File)
	 */
	public void testIsFileNewerImaginaryFile() {
		final File imaginaryFile = new File(getTestDirectory(), "imaginaryFile");
		if (imaginaryFile.exists()) {
			throw new IllegalStateException("The imaginary File exists");
		}

		testIsFileNewer("imaginary file can be newer", imaginaryFile, m_testFile2.lastModified(), false);
	}

	/**
	 * Tests the <code>isFileNewer(File, *)</code> methods which the specified conditions.
	 * <p/>
	 * Creates :
	 * <ul>
	 * <li>a <code>Date</code> which represents the time reference</li>
	 * <li>a temporary file with the same last modification date than the time reference</li>
	 * </ul>
	 * Then compares (with the needed <code>isFileNewer</code> method) the last modification date of
	 * the specified file with the specified time reference, the created <code>Date</code> and the
	 * temporary file. <br/>
	 * The test is successfull if the three comparaisons return the specified wanted result.
	 * 
	 * @param description describes the tested situation
	 * @param file the file of which the last modification date is compared
	 * @param time the time reference measured in milliseconds since the epoch
	 * @see Files#isFileNewer(File, long)
	 * @see Files#isFileNewer(File, Date)
	 * @see Files#isFileNewer(File, File)
	 */
	protected void testIsFileNewer(final String description, final File file, final long time,
			final boolean wantedResult) {
		assertEquals(description + " - time", wantedResult, Files.isFileNewer(file, time));
		assertEquals(description + " - date", wantedResult, Files.isFileNewer(file, new Date(time)));

		final File temporaryFile = m_testFile2;

		temporaryFile.setLastModified(time);
		assertEquals("The temporary file hasn't the right last modification date", time, temporaryFile.lastModified());
		assertEquals(description + " - file", wantedResult, Files.isFileNewer(file, temporaryFile));
	}

	/**
	 * Tests the <code>isFileNewer(File, long)</code> method without specifying a <code>File</code>. <br/>
	 * The test is successfull if the method throws an <code>IllegalArgumentException</code>.
	 */
	public void testIsFileNewerNoFile() {
		try {
			Files.isFileNewer(null, 0);
			fail("File not specified");
		}
		catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests the <code>isFileNewer(File, Date)</code> method without specifying a <code>Date</code>. <br/>
	 * The test is successfull if the method throws an <code>IllegalArgumentException</code>.
	 */
	public void testIsFileNewerNoDate() {
		try {
			Files.isFileNewer(m_testFile1, (Date)null);
			fail("Date not specified");
		}
		catch (final IllegalArgumentException e) {
		}
	}

	/**
	 * Tests the <code>isFileNewer(File, File)</code> method without specifying a reference
	 * <code>File</code>. <br/>
	 * The test is successfull if the method throws an <code>IllegalArgumentException</code>.
	 */
	public void testIsFileNewerNoFileReference() {
		try {
			Files.isFileNewer(m_testFile1, (File)null);
			fail("Reference file not specified");
		}
		catch (final IllegalArgumentException e) {
		}
	}
}
