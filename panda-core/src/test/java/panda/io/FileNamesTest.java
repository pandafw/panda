package panda.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 */
public class FileNamesTest extends FileBasedTestCase {
	private static final String SEP = "" + File.separatorChar;
	private static final boolean WINDOWS = File.separatorChar == '\\';

	private final File testFile1;
	private final File testFile2;

	private final int testFile1Size;
	private final int testFile2Size;

	public FileNamesTest(final String name) {
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
	public void testNormalize() throws Exception {
		assertEquals(null, FileNames.normalize(null));
		assertEquals(null, FileNames.normalize(":"));
		assertEquals(null, FileNames.normalize("1:\\a\\b\\c.txt"));
		assertEquals(null, FileNames.normalize("1:"));
		assertEquals(null, FileNames.normalize("1:a"));
		assertEquals(null, FileNames.normalize("\\\\\\a\\b\\c.txt"));
		assertEquals(null, FileNames.normalize("\\\\a"));

		assertEquals("a" + SEP + "b" + SEP + "c.txt", FileNames.normalize("a\\b/c.txt"));
		assertEquals("" + SEP + "a" + SEP + "b" + SEP + "c.txt", FileNames.normalize("\\a\\b/c.txt"));
		assertEquals("C:" + SEP + "a" + SEP + "b" + SEP + "c.txt", FileNames.normalize("C:\\a\\b/c.txt"));
		assertEquals("" + SEP + "" + SEP + "server" + SEP + "a" + SEP + "b" + SEP + "c.txt",
			FileNames.normalize("\\\\server\\a\\b/c.txt"));
		assertEquals("~" + SEP + "a" + SEP + "b" + SEP + "c.txt", FileNames.normalize("~\\a\\b/c.txt"));
		assertEquals("~user" + SEP + "a" + SEP + "b" + SEP + "c.txt", FileNames.normalize("~user\\a\\b/c.txt"));

		assertEquals("a" + SEP + "c", FileNames.normalize("a/b/../c"));
		assertEquals("c", FileNames.normalize("a/b/../../c"));
		assertEquals("c" + SEP, FileNames.normalize("a/b/../../c/"));
		assertEquals(null, FileNames.normalize("a/b/../../../c"));
		assertEquals("a" + SEP, FileNames.normalize("a/b/.."));
		assertEquals("a" + SEP, FileNames.normalize("a/b/../"));
		assertEquals("", FileNames.normalize("a/b/../.."));
		assertEquals("", FileNames.normalize("a/b/../../"));
		assertEquals(null, FileNames.normalize("a/b/../../.."));
		assertEquals("a" + SEP + "d", FileNames.normalize("a/b/../c/../d"));
		assertEquals("a" + SEP + "d" + SEP, FileNames.normalize("a/b/../c/../d/"));
		assertEquals("a" + SEP + "b" + SEP + "d", FileNames.normalize("a/b//d"));
		assertEquals("a" + SEP + "b" + SEP, FileNames.normalize("a/b/././."));
		assertEquals("a" + SEP + "b" + SEP, FileNames.normalize("a/b/./././"));
		assertEquals("a" + SEP, FileNames.normalize("./a/"));
		assertEquals("a", FileNames.normalize("./a"));
		assertEquals("", FileNames.normalize("./"));
		assertEquals("", FileNames.normalize("."));
		assertEquals(null, FileNames.normalize("../a"));
		assertEquals(null, FileNames.normalize(".."));
		assertEquals("", FileNames.normalize(""));

		assertEquals(SEP + "a", FileNames.normalize("/a"));
		assertEquals(SEP + "a" + SEP, FileNames.normalize("/a/"));
		assertEquals(SEP + "a" + SEP + "c", FileNames.normalize("/a/b/../c"));
		assertEquals(SEP + "c", FileNames.normalize("/a/b/../../c"));
		assertEquals(null, FileNames.normalize("/a/b/../../../c"));
		assertEquals(SEP + "a" + SEP, FileNames.normalize("/a/b/.."));
		assertEquals(SEP + "", FileNames.normalize("/a/b/../.."));
		assertEquals(null, FileNames.normalize("/a/b/../../.."));
		assertEquals(SEP + "a" + SEP + "d", FileNames.normalize("/a/b/../c/../d"));
		assertEquals(SEP + "a" + SEP + "b" + SEP + "d", FileNames.normalize("/a/b//d"));
		assertEquals(SEP + "a" + SEP + "b" + SEP, FileNames.normalize("/a/b/././."));
		assertEquals(SEP + "a", FileNames.normalize("/./a"));
		assertEquals(SEP + "", FileNames.normalize("/./"));
		assertEquals(SEP + "", FileNames.normalize("/."));
		assertEquals(null, FileNames.normalize("/../a"));
		assertEquals(null, FileNames.normalize("/.."));
		assertEquals(SEP + "", FileNames.normalize("/"));

		assertEquals("~" + SEP + "a", FileNames.normalize("~/a"));
		assertEquals("~" + SEP + "a" + SEP, FileNames.normalize("~/a/"));
		assertEquals("~" + SEP + "a" + SEP + "c", FileNames.normalize("~/a/b/../c"));
		assertEquals("~" + SEP + "c", FileNames.normalize("~/a/b/../../c"));
		assertEquals(null, FileNames.normalize("~/a/b/../../../c"));
		assertEquals("~" + SEP + "a" + SEP, FileNames.normalize("~/a/b/.."));
		assertEquals("~" + SEP + "", FileNames.normalize("~/a/b/../.."));
		assertEquals(null, FileNames.normalize("~/a/b/../../.."));
		assertEquals("~" + SEP + "a" + SEP + "d", FileNames.normalize("~/a/b/../c/../d"));
		assertEquals("~" + SEP + "a" + SEP + "b" + SEP + "d", FileNames.normalize("~/a/b//d"));
		assertEquals("~" + SEP + "a" + SEP + "b" + SEP, FileNames.normalize("~/a/b/././."));
		assertEquals("~" + SEP + "a", FileNames.normalize("~/./a"));
		assertEquals("~" + SEP, FileNames.normalize("~/./"));
		assertEquals("~" + SEP, FileNames.normalize("~/."));
		assertEquals(null, FileNames.normalize("~/../a"));
		assertEquals(null, FileNames.normalize("~/.."));
		assertEquals("~" + SEP, FileNames.normalize("~/"));
		assertEquals("~" + SEP, FileNames.normalize("~"));

		assertEquals("~user" + SEP + "a", FileNames.normalize("~user/a"));
		assertEquals("~user" + SEP + "a" + SEP, FileNames.normalize("~user/a/"));
		assertEquals("~user" + SEP + "a" + SEP + "c", FileNames.normalize("~user/a/b/../c"));
		assertEquals("~user" + SEP + "c", FileNames.normalize("~user/a/b/../../c"));
		assertEquals(null, FileNames.normalize("~user/a/b/../../../c"));
		assertEquals("~user" + SEP + "a" + SEP, FileNames.normalize("~user/a/b/.."));
		assertEquals("~user" + SEP + "", FileNames.normalize("~user/a/b/../.."));
		assertEquals(null, FileNames.normalize("~user/a/b/../../.."));
		assertEquals("~user" + SEP + "a" + SEP + "d", FileNames.normalize("~user/a/b/../c/../d"));
		assertEquals("~user" + SEP + "a" + SEP + "b" + SEP + "d", FileNames.normalize("~user/a/b//d"));
		assertEquals("~user" + SEP + "a" + SEP + "b" + SEP, FileNames.normalize("~user/a/b/././."));
		assertEquals("~user" + SEP + "a", FileNames.normalize("~user/./a"));
		assertEquals("~user" + SEP + "", FileNames.normalize("~user/./"));
		assertEquals("~user" + SEP + "", FileNames.normalize("~user/."));
		assertEquals(null, FileNames.normalize("~user/../a"));
		assertEquals(null, FileNames.normalize("~user/.."));
		assertEquals("~user" + SEP, FileNames.normalize("~user/"));
		assertEquals("~user" + SEP, FileNames.normalize("~user"));

		assertEquals("C:" + SEP + "a", FileNames.normalize("C:/a"));
		assertEquals("C:" + SEP + "a" + SEP, FileNames.normalize("C:/a/"));
		assertEquals("C:" + SEP + "a" + SEP + "c", FileNames.normalize("C:/a/b/../c"));
		assertEquals("C:" + SEP + "c", FileNames.normalize("C:/a/b/../../c"));
		assertEquals(null, FileNames.normalize("C:/a/b/../../../c"));
		assertEquals("C:" + SEP + "a" + SEP, FileNames.normalize("C:/a/b/.."));
		assertEquals("C:" + SEP + "", FileNames.normalize("C:/a/b/../.."));
		assertEquals(null, FileNames.normalize("C:/a/b/../../.."));
		assertEquals("C:" + SEP + "a" + SEP + "d", FileNames.normalize("C:/a/b/../c/../d"));
		assertEquals("C:" + SEP + "a" + SEP + "b" + SEP + "d", FileNames.normalize("C:/a/b//d"));
		assertEquals("C:" + SEP + "a" + SEP + "b" + SEP, FileNames.normalize("C:/a/b/././."));
		assertEquals("C:" + SEP + "a", FileNames.normalize("C:/./a"));
		assertEquals("C:" + SEP + "", FileNames.normalize("C:/./"));
		assertEquals("C:" + SEP + "", FileNames.normalize("C:/."));
		assertEquals(null, FileNames.normalize("C:/../a"));
		assertEquals(null, FileNames.normalize("C:/.."));
		assertEquals("C:" + SEP + "", FileNames.normalize("C:/"));

		assertEquals("C:" + "a", FileNames.normalize("C:a"));
		assertEquals("C:" + "a" + SEP, FileNames.normalize("C:a/"));
		assertEquals("C:" + "a" + SEP + "c", FileNames.normalize("C:a/b/../c"));
		assertEquals("C:" + "c", FileNames.normalize("C:a/b/../../c"));
		assertEquals(null, FileNames.normalize("C:a/b/../../../c"));
		assertEquals("C:" + "a" + SEP, FileNames.normalize("C:a/b/.."));
		assertEquals("C:" + "", FileNames.normalize("C:a/b/../.."));
		assertEquals(null, FileNames.normalize("C:a/b/../../.."));
		assertEquals("C:" + "a" + SEP + "d", FileNames.normalize("C:a/b/../c/../d"));
		assertEquals("C:" + "a" + SEP + "b" + SEP + "d", FileNames.normalize("C:a/b//d"));
		assertEquals("C:" + "a" + SEP + "b" + SEP, FileNames.normalize("C:a/b/././."));
		assertEquals("C:" + "a", FileNames.normalize("C:./a"));
		assertEquals("C:" + "", FileNames.normalize("C:./"));
		assertEquals("C:" + "", FileNames.normalize("C:."));
		assertEquals(null, FileNames.normalize("C:../a"));
		assertEquals(null, FileNames.normalize("C:.."));
		assertEquals("C:" + "", FileNames.normalize("C:"));

		assertEquals(SEP + SEP + "server" + SEP + "a", FileNames.normalize("//server/a"));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP, FileNames.normalize("//server/a/"));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP + "c", FileNames.normalize("//server/a/b/../c"));
		assertEquals(SEP + SEP + "server" + SEP + "c", FileNames.normalize("//server/a/b/../../c"));
		assertEquals(null, FileNames.normalize("//server/a/b/../../../c"));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP, FileNames.normalize("//server/a/b/.."));
		assertEquals(SEP + SEP + "server" + SEP + "", FileNames.normalize("//server/a/b/../.."));
		assertEquals(null, FileNames.normalize("//server/a/b/../../.."));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP + "d", FileNames.normalize("//server/a/b/../c/../d"));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP + "b" + SEP + "d",
			FileNames.normalize("//server/a/b//d"));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP + "b" + SEP, FileNames.normalize("//server/a/b/././."));
		assertEquals(SEP + SEP + "server" + SEP + "a", FileNames.normalize("//server/./a"));
		assertEquals(SEP + SEP + "server" + SEP + "", FileNames.normalize("//server/./"));
		assertEquals(SEP + SEP + "server" + SEP + "", FileNames.normalize("//server/."));
		assertEquals(null, FileNames.normalize("//server/../a"));
		assertEquals(null, FileNames.normalize("//server/.."));
		assertEquals(SEP + SEP + "server" + SEP + "", FileNames.normalize("//server/"));
	}

	public void testNormalizeUnixWin() throws Exception {

		// Normalize (Unix Separator)
		assertEquals("/a/c/", FileNames.normalize("/a/b/../c/", true));
		assertEquals("/a/c/", FileNames.normalize("\\a\\b\\..\\c\\", true));

		// Normalize (Windows Separator)
		assertEquals("\\a\\c\\", FileNames.normalize("/a/b/../c/", false));
		assertEquals("\\a\\c\\", FileNames.normalize("\\a\\b\\..\\c\\", false));
	}

	// -----------------------------------------------------------------------
	public void testNormalizeNoEndSeparator() throws Exception {
		assertEquals(null, FileNames.normalizeNoEndSeparator(null));
		assertEquals(null, FileNames.normalizeNoEndSeparator(":"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("1:\\a\\b\\c.txt"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("1:"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("1:a"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("\\\\\\a\\b\\c.txt"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("\\\\a"));

		assertEquals("a" + SEP + "b" + SEP + "c.txt", FileNames.normalizeNoEndSeparator("a\\b/c.txt"));
		assertEquals("" + SEP + "a" + SEP + "b" + SEP + "c.txt", FileNames.normalizeNoEndSeparator("\\a\\b/c.txt"));
		assertEquals("C:" + SEP + "a" + SEP + "b" + SEP + "c.txt",
			FileNames.normalizeNoEndSeparator("C:\\a\\b/c.txt"));
		assertEquals("" + SEP + "" + SEP + "server" + SEP + "a" + SEP + "b" + SEP + "c.txt",
			FileNames.normalizeNoEndSeparator("\\\\server\\a\\b/c.txt"));
		assertEquals("~" + SEP + "a" + SEP + "b" + SEP + "c.txt",
			FileNames.normalizeNoEndSeparator("~\\a\\b/c.txt"));
		assertEquals("~user" + SEP + "a" + SEP + "b" + SEP + "c.txt",
			FileNames.normalizeNoEndSeparator("~user\\a\\b/c.txt"));

		assertEquals("a" + SEP + "c", FileNames.normalizeNoEndSeparator("a/b/../c"));
		assertEquals("c", FileNames.normalizeNoEndSeparator("a/b/../../c"));
		assertEquals("c", FileNames.normalizeNoEndSeparator("a/b/../../c/"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("a/b/../../../c"));
		assertEquals("a", FileNames.normalizeNoEndSeparator("a/b/.."));
		assertEquals("a", FileNames.normalizeNoEndSeparator("a/b/../"));
		assertEquals("", FileNames.normalizeNoEndSeparator("a/b/../.."));
		assertEquals("", FileNames.normalizeNoEndSeparator("a/b/../../"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("a/b/../../.."));
		assertEquals("a" + SEP + "d", FileNames.normalizeNoEndSeparator("a/b/../c/../d"));
		assertEquals("a" + SEP + "d", FileNames.normalizeNoEndSeparator("a/b/../c/../d/"));
		assertEquals("a" + SEP + "b" + SEP + "d", FileNames.normalizeNoEndSeparator("a/b//d"));
		assertEquals("a" + SEP + "b", FileNames.normalizeNoEndSeparator("a/b/././."));
		assertEquals("a" + SEP + "b", FileNames.normalizeNoEndSeparator("a/b/./././"));
		assertEquals("a", FileNames.normalizeNoEndSeparator("./a/"));
		assertEquals("a", FileNames.normalizeNoEndSeparator("./a"));
		assertEquals("", FileNames.normalizeNoEndSeparator("./"));
		assertEquals("", FileNames.normalizeNoEndSeparator("."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("../a"));
		assertEquals(null, FileNames.normalizeNoEndSeparator(".."));
		assertEquals("", FileNames.normalizeNoEndSeparator(""));

		assertEquals(SEP + "a", FileNames.normalizeNoEndSeparator("/a"));
		assertEquals(SEP + "a", FileNames.normalizeNoEndSeparator("/a/"));
		assertEquals(SEP + "a" + SEP + "c", FileNames.normalizeNoEndSeparator("/a/b/../c"));
		assertEquals(SEP + "c", FileNames.normalizeNoEndSeparator("/a/b/../../c"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("/a/b/../../../c"));
		assertEquals(SEP + "a", FileNames.normalizeNoEndSeparator("/a/b/.."));
		assertEquals(SEP + "", FileNames.normalizeNoEndSeparator("/a/b/../.."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("/a/b/../../.."));
		assertEquals(SEP + "a" + SEP + "d", FileNames.normalizeNoEndSeparator("/a/b/../c/../d"));
		assertEquals(SEP + "a" + SEP + "b" + SEP + "d", FileNames.normalizeNoEndSeparator("/a/b//d"));
		assertEquals(SEP + "a" + SEP + "b", FileNames.normalizeNoEndSeparator("/a/b/././."));
		assertEquals(SEP + "a", FileNames.normalizeNoEndSeparator("/./a"));
		assertEquals(SEP + "", FileNames.normalizeNoEndSeparator("/./"));
		assertEquals(SEP + "", FileNames.normalizeNoEndSeparator("/."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("/../a"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("/.."));
		assertEquals(SEP + "", FileNames.normalizeNoEndSeparator("/"));

		assertEquals("~" + SEP + "a", FileNames.normalizeNoEndSeparator("~/a"));
		assertEquals("~" + SEP + "a", FileNames.normalizeNoEndSeparator("~/a/"));
		assertEquals("~" + SEP + "a" + SEP + "c", FileNames.normalizeNoEndSeparator("~/a/b/../c"));
		assertEquals("~" + SEP + "c", FileNames.normalizeNoEndSeparator("~/a/b/../../c"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("~/a/b/../../../c"));
		assertEquals("~" + SEP + "a", FileNames.normalizeNoEndSeparator("~/a/b/.."));
		assertEquals("~" + SEP + "", FileNames.normalizeNoEndSeparator("~/a/b/../.."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("~/a/b/../../.."));
		assertEquals("~" + SEP + "a" + SEP + "d", FileNames.normalizeNoEndSeparator("~/a/b/../c/../d"));
		assertEquals("~" + SEP + "a" + SEP + "b" + SEP + "d", FileNames.normalizeNoEndSeparator("~/a/b//d"));
		assertEquals("~" + SEP + "a" + SEP + "b", FileNames.normalizeNoEndSeparator("~/a/b/././."));
		assertEquals("~" + SEP + "a", FileNames.normalizeNoEndSeparator("~/./a"));
		assertEquals("~" + SEP, FileNames.normalizeNoEndSeparator("~/./"));
		assertEquals("~" + SEP, FileNames.normalizeNoEndSeparator("~/."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("~/../a"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("~/.."));
		assertEquals("~" + SEP, FileNames.normalizeNoEndSeparator("~/"));
		assertEquals("~" + SEP, FileNames.normalizeNoEndSeparator("~"));

		assertEquals("~user" + SEP + "a", FileNames.normalizeNoEndSeparator("~user/a"));
		assertEquals("~user" + SEP + "a", FileNames.normalizeNoEndSeparator("~user/a/"));
		assertEquals("~user" + SEP + "a" + SEP + "c", FileNames.normalizeNoEndSeparator("~user/a/b/../c"));
		assertEquals("~user" + SEP + "c", FileNames.normalizeNoEndSeparator("~user/a/b/../../c"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("~user/a/b/../../../c"));
		assertEquals("~user" + SEP + "a", FileNames.normalizeNoEndSeparator("~user/a/b/.."));
		assertEquals("~user" + SEP + "", FileNames.normalizeNoEndSeparator("~user/a/b/../.."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("~user/a/b/../../.."));
		assertEquals("~user" + SEP + "a" + SEP + "d", FileNames.normalizeNoEndSeparator("~user/a/b/../c/../d"));
		assertEquals("~user" + SEP + "a" + SEP + "b" + SEP + "d", FileNames.normalizeNoEndSeparator("~user/a/b//d"));
		assertEquals("~user" + SEP + "a" + SEP + "b", FileNames.normalizeNoEndSeparator("~user/a/b/././."));
		assertEquals("~user" + SEP + "a", FileNames.normalizeNoEndSeparator("~user/./a"));
		assertEquals("~user" + SEP + "", FileNames.normalizeNoEndSeparator("~user/./"));
		assertEquals("~user" + SEP + "", FileNames.normalizeNoEndSeparator("~user/."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("~user/../a"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("~user/.."));
		assertEquals("~user" + SEP, FileNames.normalizeNoEndSeparator("~user/"));
		assertEquals("~user" + SEP, FileNames.normalizeNoEndSeparator("~user"));

		assertEquals("C:" + SEP + "a", FileNames.normalizeNoEndSeparator("C:/a"));
		assertEquals("C:" + SEP + "a", FileNames.normalizeNoEndSeparator("C:/a/"));
		assertEquals("C:" + SEP + "a" + SEP + "c", FileNames.normalizeNoEndSeparator("C:/a/b/../c"));
		assertEquals("C:" + SEP + "c", FileNames.normalizeNoEndSeparator("C:/a/b/../../c"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("C:/a/b/../../../c"));
		assertEquals("C:" + SEP + "a", FileNames.normalizeNoEndSeparator("C:/a/b/.."));
		assertEquals("C:" + SEP + "", FileNames.normalizeNoEndSeparator("C:/a/b/../.."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("C:/a/b/../../.."));
		assertEquals("C:" + SEP + "a" + SEP + "d", FileNames.normalizeNoEndSeparator("C:/a/b/../c/../d"));
		assertEquals("C:" + SEP + "a" + SEP + "b" + SEP + "d", FileNames.normalizeNoEndSeparator("C:/a/b//d"));
		assertEquals("C:" + SEP + "a" + SEP + "b", FileNames.normalizeNoEndSeparator("C:/a/b/././."));
		assertEquals("C:" + SEP + "a", FileNames.normalizeNoEndSeparator("C:/./a"));
		assertEquals("C:" + SEP + "", FileNames.normalizeNoEndSeparator("C:/./"));
		assertEquals("C:" + SEP + "", FileNames.normalizeNoEndSeparator("C:/."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("C:/../a"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("C:/.."));
		assertEquals("C:" + SEP + "", FileNames.normalizeNoEndSeparator("C:/"));

		assertEquals("C:" + "a", FileNames.normalizeNoEndSeparator("C:a"));
		assertEquals("C:" + "a", FileNames.normalizeNoEndSeparator("C:a/"));
		assertEquals("C:" + "a" + SEP + "c", FileNames.normalizeNoEndSeparator("C:a/b/../c"));
		assertEquals("C:" + "c", FileNames.normalizeNoEndSeparator("C:a/b/../../c"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("C:a/b/../../../c"));
		assertEquals("C:" + "a", FileNames.normalizeNoEndSeparator("C:a/b/.."));
		assertEquals("C:" + "", FileNames.normalizeNoEndSeparator("C:a/b/../.."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("C:a/b/../../.."));
		assertEquals("C:" + "a" + SEP + "d", FileNames.normalizeNoEndSeparator("C:a/b/../c/../d"));
		assertEquals("C:" + "a" + SEP + "b" + SEP + "d", FileNames.normalizeNoEndSeparator("C:a/b//d"));
		assertEquals("C:" + "a" + SEP + "b", FileNames.normalizeNoEndSeparator("C:a/b/././."));
		assertEquals("C:" + "a", FileNames.normalizeNoEndSeparator("C:./a"));
		assertEquals("C:" + "", FileNames.normalizeNoEndSeparator("C:./"));
		assertEquals("C:" + "", FileNames.normalizeNoEndSeparator("C:."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("C:../a"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("C:.."));
		assertEquals("C:" + "", FileNames.normalizeNoEndSeparator("C:"));

		assertEquals(SEP + SEP + "server" + SEP + "a", FileNames.normalizeNoEndSeparator("//server/a"));
		assertEquals(SEP + SEP + "server" + SEP + "a", FileNames.normalizeNoEndSeparator("//server/a/"));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP + "c",
			FileNames.normalizeNoEndSeparator("//server/a/b/../c"));
		assertEquals(SEP + SEP + "server" + SEP + "c", FileNames.normalizeNoEndSeparator("//server/a/b/../../c"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("//server/a/b/../../../c"));
		assertEquals(SEP + SEP + "server" + SEP + "a", FileNames.normalizeNoEndSeparator("//server/a/b/.."));
		assertEquals(SEP + SEP + "server" + SEP + "", FileNames.normalizeNoEndSeparator("//server/a/b/../.."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("//server/a/b/../../.."));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP + "d",
			FileNames.normalizeNoEndSeparator("//server/a/b/../c/../d"));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP + "b" + SEP + "d",
			FileNames.normalizeNoEndSeparator("//server/a/b//d"));
		assertEquals(SEP + SEP + "server" + SEP + "a" + SEP + "b",
			FileNames.normalizeNoEndSeparator("//server/a/b/././."));
		assertEquals(SEP + SEP + "server" + SEP + "a", FileNames.normalizeNoEndSeparator("//server/./a"));
		assertEquals(SEP + SEP + "server" + SEP + "", FileNames.normalizeNoEndSeparator("//server/./"));
		assertEquals(SEP + SEP + "server" + SEP + "", FileNames.normalizeNoEndSeparator("//server/."));
		assertEquals(null, FileNames.normalizeNoEndSeparator("//server/../a"));
		assertEquals(null, FileNames.normalizeNoEndSeparator("//server/.."));
		assertEquals(SEP + SEP + "server" + SEP + "", FileNames.normalizeNoEndSeparator("//server/"));
	}

	public void testNormalizeNoEndSeparatorUnixWin() throws Exception {

		// Normalize (Unix Separator)
		assertEquals("/a/c", FileNames.normalizeNoEndSeparator("/a/b/../c/", true));
		assertEquals("/a/c", FileNames.normalizeNoEndSeparator("\\a\\b\\..\\c\\", true));

		// Normalize (Windows Separator)
		assertEquals("\\a\\c", FileNames.normalizeNoEndSeparator("/a/b/../c/", false));
		assertEquals("\\a\\c", FileNames.normalizeNoEndSeparator("\\a\\b\\..\\c\\", false));
	}

	// -----------------------------------------------------------------------
	public void testConcat() {
		assertEquals(null, FileNames.concat("", null));
		assertEquals(null, FileNames.concat(null, null));
		assertEquals(null, FileNames.concat(null, ""));
		assertEquals(null, FileNames.concat(null, "a"));
		assertEquals(SEP + "a", FileNames.concat(null, "/a"));

		assertEquals(null, FileNames.concat("", ":")); // invalid prefix
		assertEquals(null, FileNames.concat(":", "")); // invalid prefix

		assertEquals("f" + SEP, FileNames.concat("", "f/"));
		assertEquals("f", FileNames.concat("", "f"));
		assertEquals("a" + SEP + "f" + SEP, FileNames.concat("a/", "f/"));
		assertEquals("a" + SEP + "f", FileNames.concat("a", "f"));
		assertEquals("a" + SEP + "b" + SEP + "f" + SEP, FileNames.concat("a/b/", "f/"));
		assertEquals("a" + SEP + "b" + SEP + "f", FileNames.concat("a/b", "f"));

		assertEquals("a" + SEP + "f" + SEP, FileNames.concat("a/b/", "../f/"));
		assertEquals("a" + SEP + "f", FileNames.concat("a/b", "../f"));
		assertEquals("a" + SEP + "c" + SEP + "g" + SEP, FileNames.concat("a/b/../c/", "f/../g/"));
		assertEquals("a" + SEP + "c" + SEP + "g", FileNames.concat("a/b/../c", "f/../g"));

		assertEquals("a" + SEP + "c.txt" + SEP + "f", FileNames.concat("a/c.txt", "f"));

		assertEquals(SEP + "f" + SEP, FileNames.concat("", "/f/"));
		assertEquals(SEP + "f", FileNames.concat("", "/f"));
		assertEquals(SEP + "f" + SEP, FileNames.concat("a/", "/f/"));
		assertEquals(SEP + "f", FileNames.concat("a", "/f"));

		assertEquals(SEP + "c" + SEP + "d", FileNames.concat("a/b/", "/c/d"));
		assertEquals("C:c" + SEP + "d", FileNames.concat("a/b/", "C:c/d"));
		assertEquals("C:" + SEP + "c" + SEP + "d", FileNames.concat("a/b/", "C:/c/d"));
		assertEquals("~" + SEP + "c" + SEP + "d", FileNames.concat("a/b/", "~/c/d"));
		assertEquals("~user" + SEP + "c" + SEP + "d", FileNames.concat("a/b/", "~user/c/d"));
		assertEquals("~" + SEP, FileNames.concat("a/b/", "~"));
		assertEquals("~user" + SEP, FileNames.concat("a/b/", "~user"));
	}

	// -----------------------------------------------------------------------
	public void testSeparatorsToUnix() {
		assertEquals(null, FileNames.separatorsToUnix(null));
		assertEquals("/a/b/c", FileNames.separatorsToUnix("/a/b/c"));
		assertEquals("/a/b/c.txt", FileNames.separatorsToUnix("/a/b/c.txt"));
		assertEquals("/a/b/c", FileNames.separatorsToUnix("/a/b\\c"));
		assertEquals("/a/b/c", FileNames.separatorsToUnix("\\a\\b\\c"));
		assertEquals("D:/a/b/c", FileNames.separatorsToUnix("D:\\a\\b\\c"));
	}

	public void testSeparatorsToWindows() {
		assertEquals(null, FileNames.separatorsToWindows(null));
		assertEquals("\\a\\b\\c", FileNames.separatorsToWindows("\\a\\b\\c"));
		assertEquals("\\a\\b\\c.txt", FileNames.separatorsToWindows("\\a\\b\\c.txt"));
		assertEquals("\\a\\b\\c", FileNames.separatorsToWindows("\\a\\b/c"));
		assertEquals("\\a\\b\\c", FileNames.separatorsToWindows("/a/b/c"));
		assertEquals("D:\\a\\b\\c", FileNames.separatorsToWindows("D:/a/b/c"));
	}

	public void testSeparatorsToSystem() {
		if (WINDOWS) {
			assertEquals(null, FileNames.separatorsToSystem(null));
			assertEquals("\\a\\b\\c", FileNames.separatorsToSystem("\\a\\b\\c"));
			assertEquals("\\a\\b\\c.txt", FileNames.separatorsToSystem("\\a\\b\\c.txt"));
			assertEquals("\\a\\b\\c", FileNames.separatorsToSystem("\\a\\b/c"));
			assertEquals("\\a\\b\\c", FileNames.separatorsToSystem("/a/b/c"));
			assertEquals("D:\\a\\b\\c", FileNames.separatorsToSystem("D:/a/b/c"));
		}
		else {
			assertEquals(null, FileNames.separatorsToSystem(null));
			assertEquals("/a/b/c", FileNames.separatorsToSystem("/a/b/c"));
			assertEquals("/a/b/c.txt", FileNames.separatorsToSystem("/a/b/c.txt"));
			assertEquals("/a/b/c", FileNames.separatorsToSystem("/a/b\\c"));
			assertEquals("/a/b/c", FileNames.separatorsToSystem("\\a\\b\\c"));
			assertEquals("D:/a/b/c", FileNames.separatorsToSystem("D:\\a\\b\\c"));
		}
	}

	// -----------------------------------------------------------------------
	public void testGetPrefixLength() {
		assertEquals(-1, FileNames.getPrefixLength(null));
		assertEquals(-1, FileNames.getPrefixLength(":"));
		assertEquals(-1, FileNames.getPrefixLength("1:\\a\\b\\c.txt"));
		assertEquals(-1, FileNames.getPrefixLength("1:"));
		assertEquals(-1, FileNames.getPrefixLength("1:a"));
		assertEquals(-1, FileNames.getPrefixLength("\\\\\\a\\b\\c.txt"));
		assertEquals(-1, FileNames.getPrefixLength("\\\\a"));

		assertEquals(0, FileNames.getPrefixLength(""));
		assertEquals(1, FileNames.getPrefixLength("\\"));
		assertEquals(2, FileNames.getPrefixLength("C:"));
		assertEquals(3, FileNames.getPrefixLength("C:\\"));
		assertEquals(9, FileNames.getPrefixLength("//server/"));
		assertEquals(2, FileNames.getPrefixLength("~"));
		assertEquals(2, FileNames.getPrefixLength("~/"));
		assertEquals(6, FileNames.getPrefixLength("~user"));
		assertEquals(6, FileNames.getPrefixLength("~user/"));

		assertEquals(0, FileNames.getPrefixLength("a\\b\\c.txt"));
		assertEquals(1, FileNames.getPrefixLength("\\a\\b\\c.txt"));
		assertEquals(2, FileNames.getPrefixLength("C:a\\b\\c.txt"));
		assertEquals(3, FileNames.getPrefixLength("C:\\a\\b\\c.txt"));
		assertEquals(9, FileNames.getPrefixLength("\\\\server\\a\\b\\c.txt"));

		assertEquals(0, FileNames.getPrefixLength("a/b/c.txt"));
		assertEquals(1, FileNames.getPrefixLength("/a/b/c.txt"));
		assertEquals(3, FileNames.getPrefixLength("C:/a/b/c.txt"));
		assertEquals(9, FileNames.getPrefixLength("//server/a/b/c.txt"));
		assertEquals(2, FileNames.getPrefixLength("~/a/b/c.txt"));
		assertEquals(6, FileNames.getPrefixLength("~user/a/b/c.txt"));

		assertEquals(0, FileNames.getPrefixLength("a\\b\\c.txt"));
		assertEquals(1, FileNames.getPrefixLength("\\a\\b\\c.txt"));
		assertEquals(2, FileNames.getPrefixLength("~\\a\\b\\c.txt"));
		assertEquals(6, FileNames.getPrefixLength("~user\\a\\b\\c.txt"));

		assertEquals(9, FileNames.getPrefixLength("//server/a/b/c.txt"));
		assertEquals(-1, FileNames.getPrefixLength("\\\\\\a\\b\\c.txt"));
		assertEquals(-1, FileNames.getPrefixLength("///a/b/c.txt"));
	}

	public void testIndexOfLastSeparator() {
		assertEquals(-1, FileNames.indexOfLastSeparator(null));
		assertEquals(-1, FileNames.indexOfLastSeparator("noseperator.inthispath"));
		assertEquals(3, FileNames.indexOfLastSeparator("a/b/c"));
		assertEquals(3, FileNames.indexOfLastSeparator("a\\b\\c"));
	}

	public void testIndexOfExtension() {
		assertEquals(-1, FileNames.indexOfExtension(null));
		assertEquals(-1, FileNames.indexOfExtension("file"));
		assertEquals(4, FileNames.indexOfExtension("file.txt"));
		assertEquals(13, FileNames.indexOfExtension("a.txt/b.txt/c.txt"));
		assertEquals(-1, FileNames.indexOfExtension("a/b/c"));
		assertEquals(-1, FileNames.indexOfExtension("a\\b\\c"));
		assertEquals(-1, FileNames.indexOfExtension("a/b.notextension/c"));
		assertEquals(-1, FileNames.indexOfExtension("a\\b.notextension\\c"));
	}

	// -----------------------------------------------------------------------
	public void testGetPrefix() {
		assertEquals(null, FileNames.getPrefix(null));
		assertEquals(null, FileNames.getPrefix(":"));
		assertEquals(null, FileNames.getPrefix("1:\\a\\b\\c.txt"));
		assertEquals(null, FileNames.getPrefix("1:"));
		assertEquals(null, FileNames.getPrefix("1:a"));
		assertEquals(null, FileNames.getPrefix("\\\\\\a\\b\\c.txt"));
		assertEquals(null, FileNames.getPrefix("\\\\a"));

		assertEquals("", FileNames.getPrefix(""));
		assertEquals("\\", FileNames.getPrefix("\\"));
		assertEquals("C:", FileNames.getPrefix("C:"));
		assertEquals("C:\\", FileNames.getPrefix("C:\\"));
		assertEquals("//server/", FileNames.getPrefix("//server/"));
		assertEquals("~/", FileNames.getPrefix("~"));
		assertEquals("~/", FileNames.getPrefix("~/"));
		assertEquals("~user/", FileNames.getPrefix("~user"));
		assertEquals("~user/", FileNames.getPrefix("~user/"));

		assertEquals("", FileNames.getPrefix("a\\b\\c.txt"));
		assertEquals("\\", FileNames.getPrefix("\\a\\b\\c.txt"));
		assertEquals("C:\\", FileNames.getPrefix("C:\\a\\b\\c.txt"));
		assertEquals("\\\\server\\", FileNames.getPrefix("\\\\server\\a\\b\\c.txt"));

		assertEquals("", FileNames.getPrefix("a/b/c.txt"));
		assertEquals("/", FileNames.getPrefix("/a/b/c.txt"));
		assertEquals("C:/", FileNames.getPrefix("C:/a/b/c.txt"));
		assertEquals("//server/", FileNames.getPrefix("//server/a/b/c.txt"));
		assertEquals("~/", FileNames.getPrefix("~/a/b/c.txt"));
		assertEquals("~user/", FileNames.getPrefix("~user/a/b/c.txt"));

		assertEquals("", FileNames.getPrefix("a\\b\\c.txt"));
		assertEquals("\\", FileNames.getPrefix("\\a\\b\\c.txt"));
		assertEquals("~\\", FileNames.getPrefix("~\\a\\b\\c.txt"));
		assertEquals("~user\\", FileNames.getPrefix("~user\\a\\b\\c.txt"));
	}

	public void testGetPath() {
		assertEquals(null, FileNames.getPath(null));
		assertEquals("", FileNames.getPath("noseperator.inthispath"));
		assertEquals("", FileNames.getPath("/noseperator.inthispath"));
		assertEquals("", FileNames.getPath("\\noseperator.inthispath"));
		assertEquals("a/b/", FileNames.getPath("a/b/c.txt"));
		assertEquals("a/b/", FileNames.getPath("a/b/c"));
		assertEquals("a/b/c/", FileNames.getPath("a/b/c/"));
		assertEquals("a\\b\\", FileNames.getPath("a\\b\\c"));

		assertEquals(null, FileNames.getPath(":"));
		assertEquals(null, FileNames.getPath("1:/a/b/c.txt"));
		assertEquals(null, FileNames.getPath("1:"));
		assertEquals(null, FileNames.getPath("1:a"));
		assertEquals(null, FileNames.getPath("///a/b/c.txt"));
		assertEquals(null, FileNames.getPath("//a"));

		assertEquals("", FileNames.getPath(""));
		assertEquals("", FileNames.getPath("C:"));
		assertEquals("", FileNames.getPath("C:/"));
		assertEquals("", FileNames.getPath("//server/"));
		assertEquals("", FileNames.getPath("~"));
		assertEquals("", FileNames.getPath("~/"));
		assertEquals("", FileNames.getPath("~user"));
		assertEquals("", FileNames.getPath("~user/"));

		assertEquals("a/b/", FileNames.getPath("a/b/c.txt"));
		assertEquals("a/b/", FileNames.getPath("/a/b/c.txt"));
		assertEquals("", FileNames.getPath("C:a"));
		assertEquals("a/b/", FileNames.getPath("C:a/b/c.txt"));
		assertEquals("a/b/", FileNames.getPath("C:/a/b/c.txt"));
		assertEquals("a/b/", FileNames.getPath("//server/a/b/c.txt"));
		assertEquals("a/b/", FileNames.getPath("~/a/b/c.txt"));
		assertEquals("a/b/", FileNames.getPath("~user/a/b/c.txt"));
	}

	public void testGetPathNoEndSeparator() {
		assertEquals(null, FileNames.getPath(null));
		assertEquals("", FileNames.getPath("noseperator.inthispath"));
		assertEquals("", FileNames.getPathNoEndSeparator("/noseperator.inthispath"));
		assertEquals("", FileNames.getPathNoEndSeparator("\\noseperator.inthispath"));
		assertEquals("a/b", FileNames.getPathNoEndSeparator("a/b/c.txt"));
		assertEquals("a/b", FileNames.getPathNoEndSeparator("a/b/c"));
		assertEquals("a/b/c", FileNames.getPathNoEndSeparator("a/b/c/"));
		assertEquals("a\\b", FileNames.getPathNoEndSeparator("a\\b\\c"));

		assertEquals(null, FileNames.getPathNoEndSeparator(":"));
		assertEquals(null, FileNames.getPathNoEndSeparator("1:/a/b/c.txt"));
		assertEquals(null, FileNames.getPathNoEndSeparator("1:"));
		assertEquals(null, FileNames.getPathNoEndSeparator("1:a"));
		assertEquals(null, FileNames.getPathNoEndSeparator("///a/b/c.txt"));
		assertEquals(null, FileNames.getPathNoEndSeparator("//a"));

		assertEquals("", FileNames.getPathNoEndSeparator(""));
		assertEquals("", FileNames.getPathNoEndSeparator("C:"));
		assertEquals("", FileNames.getPathNoEndSeparator("C:/"));
		assertEquals("", FileNames.getPathNoEndSeparator("//server/"));
		assertEquals("", FileNames.getPathNoEndSeparator("~"));
		assertEquals("", FileNames.getPathNoEndSeparator("~/"));
		assertEquals("", FileNames.getPathNoEndSeparator("~user"));
		assertEquals("", FileNames.getPathNoEndSeparator("~user/"));

		assertEquals("a/b", FileNames.getPathNoEndSeparator("a/b/c.txt"));
		assertEquals("a/b", FileNames.getPathNoEndSeparator("/a/b/c.txt"));
		assertEquals("", FileNames.getPathNoEndSeparator("C:a"));
		assertEquals("a/b", FileNames.getPathNoEndSeparator("C:a/b/c.txt"));
		assertEquals("a/b", FileNames.getPathNoEndSeparator("C:/a/b/c.txt"));
		assertEquals("a/b", FileNames.getPathNoEndSeparator("//server/a/b/c.txt"));
		assertEquals("a/b", FileNames.getPathNoEndSeparator("~/a/b/c.txt"));
		assertEquals("a/b", FileNames.getPathNoEndSeparator("~user/a/b/c.txt"));
	}

	public void testGetFullPath() {
		assertEquals(null, FileNames.getFullPath(null));
		assertEquals("", FileNames.getFullPath("noseperator.inthispath"));
		assertEquals("a/b/", FileNames.getFullPath("a/b/c.txt"));
		assertEquals("a/b/", FileNames.getFullPath("a/b/c"));
		assertEquals("a/b/c/", FileNames.getFullPath("a/b/c/"));
		assertEquals("a\\b\\", FileNames.getFullPath("a\\b\\c"));

		assertEquals(null, FileNames.getFullPath(":"));
		assertEquals(null, FileNames.getFullPath("1:/a/b/c.txt"));
		assertEquals(null, FileNames.getFullPath("1:"));
		assertEquals(null, FileNames.getFullPath("1:a"));
		assertEquals(null, FileNames.getFullPath("///a/b/c.txt"));
		assertEquals(null, FileNames.getFullPath("//a"));

		assertEquals("", FileNames.getFullPath(""));
		assertEquals("C:", FileNames.getFullPath("C:"));
		assertEquals("C:/", FileNames.getFullPath("C:/"));
		assertEquals("//server/", FileNames.getFullPath("//server/"));
		assertEquals("~/", FileNames.getFullPath("~"));
		assertEquals("~/", FileNames.getFullPath("~/"));
		assertEquals("~user/", FileNames.getFullPath("~user"));
		assertEquals("~user/", FileNames.getFullPath("~user/"));

		assertEquals("a/b/", FileNames.getFullPath("a/b/c.txt"));
		assertEquals("/a/b/", FileNames.getFullPath("/a/b/c.txt"));
		assertEquals("C:", FileNames.getFullPath("C:a"));
		assertEquals("C:a/b/", FileNames.getFullPath("C:a/b/c.txt"));
		assertEquals("C:/a/b/", FileNames.getFullPath("C:/a/b/c.txt"));
		assertEquals("//server/a/b/", FileNames.getFullPath("//server/a/b/c.txt"));
		assertEquals("~/a/b/", FileNames.getFullPath("~/a/b/c.txt"));
		assertEquals("~user/a/b/", FileNames.getFullPath("~user/a/b/c.txt"));
	}

	public void testGetFullPathNoEndSeparator() {
		assertEquals(null, FileNames.getFullPathNoEndSeparator(null));
		assertEquals("", FileNames.getFullPathNoEndSeparator("noseperator.inthispath"));
		assertEquals("a/b", FileNames.getFullPathNoEndSeparator("a/b/c.txt"));
		assertEquals("a/b", FileNames.getFullPathNoEndSeparator("a/b/c"));
		assertEquals("a/b/c", FileNames.getFullPathNoEndSeparator("a/b/c/"));
		assertEquals("a\\b", FileNames.getFullPathNoEndSeparator("a\\b\\c"));

		assertEquals(null, FileNames.getFullPathNoEndSeparator(":"));
		assertEquals(null, FileNames.getFullPathNoEndSeparator("1:/a/b/c.txt"));
		assertEquals(null, FileNames.getFullPathNoEndSeparator("1:"));
		assertEquals(null, FileNames.getFullPathNoEndSeparator("1:a"));
		assertEquals(null, FileNames.getFullPathNoEndSeparator("///a/b/c.txt"));
		assertEquals(null, FileNames.getFullPathNoEndSeparator("//a"));

		assertEquals("", FileNames.getFullPathNoEndSeparator(""));
		assertEquals("C:", FileNames.getFullPathNoEndSeparator("C:"));
		assertEquals("C:/", FileNames.getFullPathNoEndSeparator("C:/"));
		assertEquals("//server/", FileNames.getFullPathNoEndSeparator("//server/"));
		assertEquals("~", FileNames.getFullPathNoEndSeparator("~"));
		assertEquals("~/", FileNames.getFullPathNoEndSeparator("~/"));
		assertEquals("~user", FileNames.getFullPathNoEndSeparator("~user"));
		assertEquals("~user/", FileNames.getFullPathNoEndSeparator("~user/"));

		assertEquals("a/b", FileNames.getFullPathNoEndSeparator("a/b/c.txt"));
		assertEquals("/a/b", FileNames.getFullPathNoEndSeparator("/a/b/c.txt"));
		assertEquals("C:", FileNames.getFullPathNoEndSeparator("C:a"));
		assertEquals("C:a/b", FileNames.getFullPathNoEndSeparator("C:a/b/c.txt"));
		assertEquals("C:/a/b", FileNames.getFullPathNoEndSeparator("C:/a/b/c.txt"));
		assertEquals("//server/a/b", FileNames.getFullPathNoEndSeparator("//server/a/b/c.txt"));
		assertEquals("~/a/b", FileNames.getFullPathNoEndSeparator("~/a/b/c.txt"));
		assertEquals("~user/a/b", FileNames.getFullPathNoEndSeparator("~user/a/b/c.txt"));
	}

	/**
	 * Test for https://issues.apache.org/jira/browse/IO-248
	 */
	public void testGetFullPathNoEndSeparator_IO_248() {

		// Test single separator
		assertEquals("/", FileNames.getFullPathNoEndSeparator("/"));
		assertEquals("\\", FileNames.getFullPathNoEndSeparator("\\"));

		// Test one level directory
		assertEquals("/", FileNames.getFullPathNoEndSeparator("/abc"));
		assertEquals("\\", FileNames.getFullPathNoEndSeparator("\\abc"));

		// Test one level directory
		assertEquals("/abc", FileNames.getFullPathNoEndSeparator("/abc/xyz"));
		assertEquals("\\abc", FileNames.getFullPathNoEndSeparator("\\abc\\xyz"));
	}

	public void testGetName() {
		assertEquals(null, FileNames.getName((String)null));
		assertEquals("noseperator.inthispath", FileNames.getName("noseperator.inthispath"));
		assertEquals("c.txt", FileNames.getName("a/b/c.txt"));
		assertEquals("c", FileNames.getName("a/b/c"));
		assertEquals("", FileNames.getName("a/b/c/"));
		assertEquals("c", FileNames.getName("a\\b\\c"));
	}

	public void testGetBaseName() {
		assertEquals(null, FileNames.getBaseName((String)null));
		assertEquals("noseperator", FileNames.getBaseName("noseperator.inthispath"));
		assertEquals("c", FileNames.getBaseName("a/b/c.txt"));
		assertEquals("c", FileNames.getBaseName("a/b/c"));
		assertEquals("", FileNames.getBaseName("a/b/c/"));
		assertEquals("c", FileNames.getBaseName("a\\b\\c"));
		assertEquals("file.txt", FileNames.getBaseName("file.txt.bak"));
	}

	public void testGetExtension() {
		assertEquals(null, FileNames.getExtension((String)null));
		assertEquals("ext", FileNames.getExtension("file.ext"));
		assertEquals("", FileNames.getExtension("README"));
		assertEquals("com", FileNames.getExtension("domain.dot.com"));
		assertEquals("jpeg", FileNames.getExtension("image.jpeg"));
		assertEquals("", FileNames.getExtension("a.b/c"));
		assertEquals("txt", FileNames.getExtension("a.b/c.txt"));
		assertEquals("", FileNames.getExtension("a/b/c"));
		assertEquals("", FileNames.getExtension("a.b\\c"));
		assertEquals("txt", FileNames.getExtension("a.b\\c.txt"));
		assertEquals("", FileNames.getExtension("a\\b\\c"));
		assertEquals("", FileNames.getExtension("C:\\temp\\foo.bar\\README"));
		assertEquals("ext", FileNames.getExtension("../filename.ext"));
	}

	public void testRemoveExtension() {
		assertEquals(null, FileNames.removeExtension((String)null));
		assertEquals("file", FileNames.removeExtension("file.ext"));
		assertEquals("README", FileNames.removeExtension("README"));
		assertEquals("domain.dot", FileNames.removeExtension("domain.dot.com"));
		assertEquals("image", FileNames.removeExtension("image.jpeg"));
		assertEquals("a.b/c", FileNames.removeExtension("a.b/c"));
		assertEquals("a.b/c", FileNames.removeExtension("a.b/c.txt"));
		assertEquals("a/b/c", FileNames.removeExtension("a/b/c"));
		assertEquals("a.b\\c", FileNames.removeExtension("a.b\\c"));
		assertEquals("a.b\\c", FileNames.removeExtension("a.b\\c.txt"));
		assertEquals("a\\b\\c", FileNames.removeExtension("a\\b\\c"));
		assertEquals("C:\\temp\\foo.bar\\README", FileNames.removeExtension("C:\\temp\\foo.bar\\README"));
		assertEquals("../filename", FileNames.removeExtension("../filename.ext"));
	}

	// -----------------------------------------------------------------------
	public void testEquals() {
		assertTrue(FileNames.equals(null, null));
		assertFalse(FileNames.equals(null, ""));
		assertFalse(FileNames.equals("", null));
		assertTrue(FileNames.equals("", ""));
		assertTrue(FileNames.equals("file.txt", "file.txt"));
		assertFalse(FileNames.equals("file.txt", "FILE.TXT"));
		assertFalse(FileNames.equals("a\\b\\file.txt", "a/b/file.txt"));
	}

	public void testEqualsOnSystem() {
		assertTrue(FileNames.equalsOnSystem(null, null));
		assertFalse(FileNames.equalsOnSystem(null, ""));
		assertFalse(FileNames.equalsOnSystem("", null));
		assertTrue(FileNames.equalsOnSystem("", ""));
		assertTrue(FileNames.equalsOnSystem("file.txt", "file.txt"));
		assertEquals(WINDOWS, FileNames.equalsOnSystem("file.txt", "FILE.TXT"));
		assertFalse(FileNames.equalsOnSystem("a\\b\\file.txt", "a/b/file.txt"));
	}

	// -----------------------------------------------------------------------
	public void testEqualsNormalized() {
		assertTrue(FileNames.equalsNormalized(null, null));
		assertFalse(FileNames.equalsNormalized(null, ""));
		assertFalse(FileNames.equalsNormalized("", null));
		assertTrue(FileNames.equalsNormalized("", ""));
		assertTrue(FileNames.equalsNormalized("file.txt", "file.txt"));
		assertFalse(FileNames.equalsNormalized("file.txt", "FILE.TXT"));
		assertTrue(FileNames.equalsNormalized("a\\b\\file.txt", "a/b/file.txt"));
		assertFalse(FileNames.equalsNormalized("a/b/", "a/b"));
	}

	public void testEqualsNormalizedOnSystem() {
		assertTrue(FileNames.equalsNormalizedOnSystem(null, null));
		assertFalse(FileNames.equalsNormalizedOnSystem(null, ""));
		assertFalse(FileNames.equalsNormalizedOnSystem("", null));
		assertTrue(FileNames.equalsNormalizedOnSystem("", ""));
		assertTrue(FileNames.equalsNormalizedOnSystem("file.txt", "file.txt"));
		assertEquals(WINDOWS, FileNames.equalsNormalizedOnSystem("file.txt", "FILE.TXT"));
		assertTrue(FileNames.equalsNormalizedOnSystem("a\\b\\file.txt", "a/b/file.txt"));
		assertFalse(FileNames.equalsNormalizedOnSystem("a/b/", "a/b"));
	}

	/**
	 * Test for https://issues.apache.org/jira/browse/IO-128
	 */
	public void testEqualsNormalizedError_IO_128() {
		try {
			FileNames.equalsNormalizedOnSystem("//file.txt", "file.txt");
			fail("Invalid normalized first file");
		}
		catch (final NullPointerException e) {
			// expected result
		}
		try {
			FileNames.equalsNormalizedOnSystem("file.txt", "//file.txt");
			fail("Invalid normalized second file");
		}
		catch (final NullPointerException e) {
			// expected result
		}
		try {
			FileNames.equalsNormalizedOnSystem("//file.txt", "//file.txt");
			fail("Invalid normalized both filse");
		}
		catch (final NullPointerException e) {
			// expected result
		}
	}

	public void testEquals_fullControl() {
		assertFalse(FileNames.equals("file.txt", "FILE.TXT", true, IOCase.SENSITIVE));
		assertTrue(FileNames.equals("file.txt", "FILE.TXT", true, IOCase.INSENSITIVE));
		assertEquals(WINDOWS, FileNames.equals("file.txt", "FILE.TXT", true, IOCase.SYSTEM));
		assertFalse(FileNames.equals("file.txt", "FILE.TXT", true, null));
	}

	// -----------------------------------------------------------------------
	public void testIsExtension() {
		assertFalse(FileNames.isExtension(null, (String)null));
		assertFalse(FileNames.isExtension("file.txt", (String)null));
		assertTrue(FileNames.isExtension("file", (String)null));
		assertFalse(FileNames.isExtension("file.txt", ""));
		assertTrue(FileNames.isExtension("file", ""));
		assertTrue(FileNames.isExtension("file.txt", "txt"));
		assertFalse(FileNames.isExtension("file.txt", "rtf"));

		assertFalse(FileNames.isExtension("a/b/file.txt", (String)null));
		assertFalse(FileNames.isExtension("a/b/file.txt", ""));
		assertTrue(FileNames.isExtension("a/b/file.txt", "txt"));
		assertFalse(FileNames.isExtension("a/b/file.txt", "rtf"));

		assertFalse(FileNames.isExtension("a.b/file.txt", (String)null));
		assertFalse(FileNames.isExtension("a.b/file.txt", ""));
		assertTrue(FileNames.isExtension("a.b/file.txt", "txt"));
		assertFalse(FileNames.isExtension("a.b/file.txt", "rtf"));

		assertFalse(FileNames.isExtension("a\\b\\file.txt", (String)null));
		assertFalse(FileNames.isExtension("a\\b\\file.txt", ""));
		assertTrue(FileNames.isExtension("a\\b\\file.txt", "txt"));
		assertFalse(FileNames.isExtension("a\\b\\file.txt", "rtf"));

		assertFalse(FileNames.isExtension("a.b\\file.txt", (String)null));
		assertFalse(FileNames.isExtension("a.b\\file.txt", ""));
		assertTrue(FileNames.isExtension("a.b\\file.txt", "txt"));
		assertFalse(FileNames.isExtension("a.b\\file.txt", "rtf"));

		assertFalse(FileNames.isExtension("a.b\\file.txt", "TXT"));
	}

	public void testIsExtensionArray() {
		assertFalse(FileNames.isExtension(null, (String[])null));
		assertFalse(FileNames.isExtension("file.txt", (String[])null));
		assertTrue(FileNames.isExtension("file", (String[])null));
		assertFalse(FileNames.isExtension("file.txt", new String[0]));
		assertTrue(FileNames.isExtension("file.txt", new String[] { "txt" }));
		assertFalse(FileNames.isExtension("file.txt", new String[] { "rtf" }));
		assertTrue(FileNames.isExtension("file", new String[] { "rtf", "" }));
		assertTrue(FileNames.isExtension("file.txt", new String[] { "rtf", "txt" }));

		assertFalse(FileNames.isExtension("a/b/file.txt", (String[])null));
		assertFalse(FileNames.isExtension("a/b/file.txt", new String[0]));
		assertTrue(FileNames.isExtension("a/b/file.txt", new String[] { "txt" }));
		assertFalse(FileNames.isExtension("a/b/file.txt", new String[] { "rtf" }));
		assertTrue(FileNames.isExtension("a/b/file.txt", new String[] { "rtf", "txt" }));

		assertFalse(FileNames.isExtension("a.b/file.txt", (String[])null));
		assertFalse(FileNames.isExtension("a.b/file.txt", new String[0]));
		assertTrue(FileNames.isExtension("a.b/file.txt", new String[] { "txt" }));
		assertFalse(FileNames.isExtension("a.b/file.txt", new String[] { "rtf" }));
		assertTrue(FileNames.isExtension("a.b/file.txt", new String[] { "rtf", "txt" }));

		assertFalse(FileNames.isExtension("a\\b\\file.txt", (String[])null));
		assertFalse(FileNames.isExtension("a\\b\\file.txt", new String[0]));
		assertTrue(FileNames.isExtension("a\\b\\file.txt", new String[] { "txt" }));
		assertFalse(FileNames.isExtension("a\\b\\file.txt", new String[] { "rtf" }));
		assertTrue(FileNames.isExtension("a\\b\\file.txt", new String[] { "rtf", "txt" }));

		assertFalse(FileNames.isExtension("a.b\\file.txt", (String[])null));
		assertFalse(FileNames.isExtension("a.b\\file.txt", new String[0]));
		assertTrue(FileNames.isExtension("a.b\\file.txt", new String[] { "txt" }));
		assertFalse(FileNames.isExtension("a.b\\file.txt", new String[] { "rtf" }));
		assertTrue(FileNames.isExtension("a.b\\file.txt", new String[] { "rtf", "txt" }));

		assertFalse(FileNames.isExtension("a.b\\file.txt", new String[] { "TXT" }));
		assertFalse(FileNames.isExtension("a.b\\file.txt", new String[] { "TXT", "RTF" }));
	}

	public void testIsExtensionCollection() {
		assertFalse(FileNames.isExtension(null, (Collection<String>)null));
		assertFalse(FileNames.isExtension("file.txt", (Collection<String>)null));
		assertTrue(FileNames.isExtension("file", (Collection<String>)null));
		assertFalse(FileNames.isExtension("file.txt", new ArrayList<String>()));
		assertTrue(FileNames.isExtension("file.txt", new ArrayList<String>(Arrays.asList(new String[] { "txt" }))));
		assertFalse(FileNames.isExtension("file.txt", new ArrayList<String>(Arrays.asList(new String[] { "rtf" }))));
		assertTrue(FileNames.isExtension("file", new ArrayList<String>(Arrays.asList(new String[] { "rtf", "" }))));
		assertTrue(FileNames.isExtension("file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "rtf", "txt" }))));

		assertFalse(FileNames.isExtension("a/b/file.txt", (Collection<String>)null));
		assertFalse(FileNames.isExtension("a/b/file.txt", new ArrayList<String>()));
		assertTrue(FileNames.isExtension("a/b/file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "txt" }))));
		assertFalse(FileNames.isExtension("a/b/file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "rtf" }))));
		assertTrue(FileNames.isExtension("a/b/file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "rtf", "txt" }))));

		assertFalse(FileNames.isExtension("a.b/file.txt", (Collection<String>)null));
		assertFalse(FileNames.isExtension("a.b/file.txt", new ArrayList<String>()));
		assertTrue(FileNames.isExtension("a.b/file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "txt" }))));
		assertFalse(FileNames.isExtension("a.b/file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "rtf" }))));
		assertTrue(FileNames.isExtension("a.b/file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "rtf", "txt" }))));

		assertFalse(FileNames.isExtension("a\\b\\file.txt", (Collection<String>)null));
		assertFalse(FileNames.isExtension("a\\b\\file.txt", new ArrayList<String>()));
		assertTrue(FileNames.isExtension("a\\b\\file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "txt" }))));
		assertFalse(FileNames.isExtension("a\\b\\file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "rtf" }))));
		assertTrue(FileNames.isExtension("a\\b\\file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "rtf", "txt" }))));

		assertFalse(FileNames.isExtension("a.b\\file.txt", (Collection<String>)null));
		assertFalse(FileNames.isExtension("a.b\\file.txt", new ArrayList<String>()));
		assertTrue(FileNames.isExtension("a.b\\file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "txt" }))));
		assertFalse(FileNames.isExtension("a.b\\file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "rtf" }))));
		assertTrue(FileNames.isExtension("a.b\\file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "rtf", "txt" }))));

		assertFalse(FileNames.isExtension("a.b\\file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "TXT" }))));
		assertFalse(FileNames.isExtension("a.b\\file.txt",
			new ArrayList<String>(Arrays.asList(new String[] { "TXT", "RTF" }))));
	}

	/**
	 * test method: GetContentTypeForName
	 */
	public void testGetContentTypeForName() {
		assertEquals(MimeTypes.IMG_GIF, FileNames.getContentTypeFor("/a/s1.gif"));
		assertEquals(MimeTypes.IMG_PNG, FileNames.getContentTypeFor("a.png"));
		assertEquals(MimeTypes.IMG_JPEG, FileNames.getContentTypeFor("a.jpg"));
		assertEquals(null, FileNames.getContentTypeFor("a.js"));
		assertEquals(null, FileNames.getContentTypeFor("a.css"));
	}

	/**
	 * test method: isAbsolutePath
	 */
	public void testIsAbsolutePath() {
		assertTrue(FileNames.isAbsolutePath("/a/s1.gif"));
		assertTrue(FileNames.isAbsolutePath("C:/1.js"));
		assertTrue(FileNames.isAbsolutePath("C:\\1.js"));
		assertFalse(FileNames.isAbsolutePath("c.css"));
	}

}
