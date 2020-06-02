package panda.net.ftp.parser;

import java.util.Calendar;

import panda.net.ftp.FTPFile;
import panda.net.ftp.FTPFileEntryParser;

/**
 * @version $Id: OS400FTPEntryParserAdditionalTest.java 1644697 2014-12-11 17:00:57Z sebb $
 */

public class OS400FTPEntryParserAdditionalTest extends CompositeFTPParseTestFramework {
	private static final String[][] badsamples = { { "QPGMR          135168 04/03/18 13:18:19 *FILE",
			"QPGMR          135168    03/24 13:18:19 *FILE", "QPGMR          135168 04/03/18 30:06:29 *FILE",
			"QPGMR                 04/03/18 13:18:19 *FILE      RPGUNITC1.FILE",
			"QPGMR          135168    03/24 13:18:19 *FILE      RPGUNITC1.FILE",
			"QPGMR          135168 04/03/18 30:06:29 *FILE      RPGUNITC1.FILE",
			"QPGMR                                   *MEM       ",
			"QPGMR          135168 04/03/18 13:18:19 *MEM       RPGUNITC1.FILE/RUCALLTST.MBR",
			"QPGMR          135168                   *MEM       RPGUNITC1.FILE/RUCALLTST.MBR",
			"QPGMR                 04/03/18 13:18:19 *MEM       RPGUNITC1.FILE/RUCALLTST.MBR",
			"QPGMR USR                               *MEM       RPGUNITC1.FILE/RUCALLTST.MBR" } };

	private static final String[][] goodsamples = { {
			"QPGMR                                   *MEM       RPGUNITC1.FILE/RUCALLTST.MBR",
			"QPGMR        16347136 29.06.13 15:45:09 *FILE      RPGUNIT.SAVF" } };

	public OS400FTPEntryParserAdditionalTest(String name) {
		super(name);
	}

	@Override
	protected String[][] getBadListings() {
		return badsamples;
	}

	@Override
	protected String[][] getGoodListings() {
		return goodsamples;
	}

	@Override
	protected FTPFileEntryParser getParser() {
		return new CompositeFileEntryParser(new FTPFileEntryParser[] { new OS400FTPEntryParser(),
				new UnixFTPEntryParser() });
	}

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
		// Done in other class
	}

	@Override
	public void testRecentPrecision() {
		// Done in other class
	}
}
