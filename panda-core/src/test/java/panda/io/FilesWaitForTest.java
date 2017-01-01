package panda.io;

import java.io.File;

/**
 * This is used to test Files.waitFor() method for correctness.
 */
public class FilesWaitForTest extends FileBasedTestCase {
	// This class has been broken out from FilesTestCase
	// to solve issues as per BZ 38927

	public FilesWaitForTest(final String name) {
		super(name);
	}

	/** @see junit.framework.TestCase#setUp() */
	@Override
	protected void setUp() throws Exception {
		getTestDirectory().mkdirs();
	}

	/** @see junit.framework.TestCase#tearDown() */
	@Override
	protected void tearDown() throws Exception {
		Files.deleteDir(getTestDirectory());
	}

	// -----------------------------------------------------------------------
	public void testWaitFor() {
		Files.waitFor(new File(""), -1);
		Files.waitFor(new File(""), 2);
	}

}
