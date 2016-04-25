package panda.net.ftp.parser;

import panda.net.ftp.FTPClientConfig;
import panda.net.ftp.FTPFile;
import panda.net.ftp.FTPFileEntryParser;
import panda.net.ftp.parser.CompositeFileEntryParser;
import panda.net.ftp.parser.OS400FTPEntryParser;
import panda.net.ftp.parser.UnixFTPEntryParser;

import java.util.Calendar;

/**
 * @version $Id: OS400FTPEntryParserTest.java 1697289 2015-08-24 00:32:19Z sebb $
 */

public class OS400FTPEntryParserTest extends CompositeFTPParseTestFramework {
	private static final String[][] badsamples = {
			{ "PEP              4019 04/03/18 18:58:16 STMF       einladung.zip",
					"PEP               422 03/24 14:06:26 *STMF      readme",
					"PEP              6409 04/03/24 30:06:29 *STMF      build.xml",
					"PEP USR         36864 04/03/24 14:06:34 *DIR       dir1/",
					"PEP             3686404/03/24 14:06:47 *DIR       zdir2/" },

			{ "----rwxr-x   1PEP       0           4019 Mar 18 18:58 einladung.zip",
					"----rwxr-x   1 PEP      0  xx        422 Mar 24 14:06 readme",
					"----rwxr-x   1 PEP      0           8492 Apr 07 30:13 build.xml",
					"d---rwxr-x   2 PEP      0          45056Mar 24 14:06 zdir2" } };

	private static final String[][] goodsamples = {
			{ "PEP              4019 04/03/18 18:58:16 *STMF      einladung.zip",
					"PEP               422 04/03/24 14:06:26 *STMF      readme",
					"PEP              6409 04/03/24 14:06:29 *STMF      build.xml",
					"PEP             36864 04/03/24 14:06:34 *DIR       dir1/",
					"PEP             36864 04/03/24 14:06:47 *DIR       zdir2/" },
			{ "----rwxr-x   1 PEP      0           4019 Mar 18 18:58 einladung.zip",
					"----rwxr-x   1 PEP      0            422 Mar 24 14:06 readme",
					"----rwxr-x   1 PEP      0           8492 Apr 07 07:13 build.xml",
					"d---rwxr-x   2 PEP      0          45056 Mar 24 14:06 dir1",
					"d---rwxr-x   2 PEP      0          45056 Mar 24 14:06 zdir2" } };

	/**
	 * @see junit.framework.TestCase#TestCase(String)
	 */
	public OS400FTPEntryParserTest(String name) {
		super(name);
	}

	/**
	 * @see FTPParseTestFramework#getBadListing()
	 */
	@Override
	protected String[][] getBadListings() {
		return badsamples;
	}

	/**
	 * @see FTPParseTestFramework#getGoodListing()
	 */
	@Override
	protected String[][] getGoodListings() {
		return goodsamples;
	}

	/**
	 * @see FTPParseTestFramework#getParser()
	 */
	@Override
	protected FTPFileEntryParser getParser() {
		return new CompositeFileEntryParser(new FTPFileEntryParser[] { new OS400FTPEntryParser(),
				new UnixFTPEntryParser() });
	}

	/**
	 * @see FTPParseTestFramework#testParseFieldsOnDirectory()
	 */
	@Override
	public void testParseFieldsOnDirectory() throws Exception {
		FTPFile f = getParser().parseFTPEntry("PEP             36864 04/03/24 14:06:34 *DIR       dir1/");
		assertNotNull("Could not parse entry.", f);
		assertTrue("Should have been a directory.", f.isDirectory());
		assertEquals("PEP", f.getUser());
		assertEquals("dir1", f.getName());
		assertEquals(36864, f.getSize());

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.MARCH);

		cal.set(Calendar.YEAR, 2004);
		cal.set(Calendar.DAY_OF_MONTH, 24);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 6);
		cal.set(Calendar.SECOND, 34);

		assertEquals(df.format(cal.getTime()), df.format(f.getTimestamp().getTime()));
	}

	@Override
	protected void doAdditionalGoodTests(String test, FTPFile f) {
		if (test.startsWith("d")) {
			assertEquals("directory.type", FTPFile.DIRECTORY_TYPE, f.getType());
		}
	}

	/**
	 * @see FTPParseTestFramework#testParseFieldsOnFile()
	 */
	@Override
	public void testParseFieldsOnFile() throws Exception {
		FTPFile f = getParser().parseFTPEntry("PEP              5000000000 04/03/24 14:06:29 *STMF      build.xml");
		assertNotNull("Could not parse entry.", f);
		assertTrue("Should have been a file.", f.isFile());
		assertEquals("PEP", f.getUser());
		assertEquals("build.xml", f.getName());
		assertEquals(5000000000L, f.getSize());

		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.DAY_OF_MONTH, 24);
		cal.set(Calendar.MONTH, Calendar.MARCH);
		cal.set(Calendar.YEAR, 2004);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 6);
		cal.set(Calendar.SECOND, 29);
		assertEquals(df.format(cal.getTime()), df.format(f.getTimestamp().getTime()));
	}

	@Override
	public void testDefaultPrecision() {
		testPrecision("PEP              4019 04/03/18 18:58:16 *STMF      einladung.zip", CalendarUnit.SECOND);
	}

	@Override
	public void testRecentPrecision() {
		testPrecision("----rwxr-x   1 PEP      0           4019 Mar 18 18:58 einladung.zip", CalendarUnit.MINUTE);
	}

	public void testNET573() throws Exception {
		final FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_AS400);
		conf.setDefaultDateFormatStr("MM/dd/yy HH:mm:ss");
		final FTPFileEntryParser parser = new OS400FTPEntryParser(conf);

		FTPFile f = parser
			.parseFTPEntry("ZFTPDEV 9069 05/20/15 15:36:52 *STMF /DRV/AUDWRKSHET/AUDWRK0204232015114625.PDF");
		assertNotNull("Could not parse entry.", f);
		assertNotNull("Could not parse timestamp.", f.getTimestamp());
		assertFalse("Should not have been a directory.", f.isDirectory());
		assertEquals("ZFTPDEV", f.getUser());
		assertEquals("AUDWRK0204232015114625.PDF", f.getName());
		assertEquals(9069, f.getSize());

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2015);
		cal.set(Calendar.MONTH, Calendar.MAY);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		cal.set(Calendar.HOUR_OF_DAY, 15);
		cal.set(Calendar.MINUTE, 36);
		cal.set(Calendar.SECOND, 52);

		assertEquals(df.format(cal.getTime()), df.format(f.getTimestamp().getTime()));
	}

}
