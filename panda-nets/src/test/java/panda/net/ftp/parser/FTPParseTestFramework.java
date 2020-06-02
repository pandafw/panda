package panda.net.ftp.parser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import panda.net.ftp.FTPFile;
import panda.net.ftp.FTPFileEntryParser;

import junit.framework.TestCase;

/**
 * @version $Id: FTPParseTestFramework.java 1697293 2015-08-24 01:01:00Z sebb $
 */
public abstract class FTPParseTestFramework extends TestCase {
	private FTPFileEntryParser parser = null;
	protected SimpleDateFormat df = null;

	/**
	 * @see junit.framework.TestCase#TestCase(String)
	 */
	public FTPParseTestFramework(String name) {
		super(name);
	}

	public void testBadListing() throws Exception {

		String[] badsamples = getBadListing();
		for (String test : badsamples) {

			FTPFile f = parser.parseFTPEntry(test);
			assertNull("Should have Failed to parse <" + test + ">", nullFileOrNullDate(f));

			doAdditionalBadTests(test, f);
		}
	}

	public void testGoodListing() throws Exception {

		String[] goodsamples = getGoodListing();
		for (String test : goodsamples) {

			FTPFile f = parser.parseFTPEntry(test);
			assertNotNull("Failed to parse " + test, f);

			doAdditionalGoodTests(test, f);
		}
	}

	/**
	 * during processing you could hook here to do additional tests
	 * 
	 * @param test raw entry
	 * @param f parsed entry
	 */
	protected void doAdditionalGoodTests(String test, FTPFile f) {
	}

	/**
	 * during processing you could hook here to do additional tests
	 * 
	 * @param test raw entry
	 * @param f parsed entry
	 */
	protected void doAdditionalBadTests(String test, FTPFile f) {
	}

	/**
	 * Method getBadListing. Implementors must provide a listing that contains failures.
	 * 
	 * @return String[]
	 */
	protected abstract String[] getBadListing();

	/**
	 * Method getGoodListing. Implementors must provide a listing that passes.
	 * 
	 * @return String[]
	 */
	protected abstract String[] getGoodListing();

	/**
	 * Method getParser. Provide the parser to use for testing.
	 * 
	 * @return FTPFileEntryParser
	 */
	protected abstract FTPFileEntryParser getParser();

	/**
	 * Method testParseFieldsOnDirectory. Provide a test to show that fields on a directory entry
	 * are parsed correctly.
	 * 
	 * @throws Exception on error
	 */
	public abstract void testParseFieldsOnDirectory() throws Exception;

	/**
	 * Method testParseFieldsOnFile. Provide a test to show that fields on a file entry are parsed
	 * correctly.
	 * 
	 * @throws Exception on error
	 */
	public abstract void testParseFieldsOnFile() throws Exception;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		parser = getParser();
		df = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US);
	}

	/**
	 * Check if FTPFile entry parsing failed; i.e. if entry is null or date is null.
	 * 
	 * @param f FTPFile entry - may be null
	 * @return null if f is null or the date is null
	 */
	protected FTPFile nullFileOrNullDate(FTPFile f) {
		if (f == null) {
			return null;
		}
		if (f.getTimestamp() == null) {
			return null;
		}
		return f;
	}

	// associate Calendar unit ints with a readable string
	// MUST be listed least significant first, as the routine needs to
	// find the previous - less significant - entry
	protected enum CalendarUnit {
		MILLISECOND(Calendar.MILLISECOND),
		SECOND(Calendar.SECOND),
		MINUTE(Calendar.MINUTE),
		HOUR_OF_DAY(Calendar.HOUR_OF_DAY),
		DAY_OF_MONTH(Calendar.DAY_OF_MONTH),
		MONTH(Calendar.MONTH),
		YEAR(Calendar.YEAR), ;
		final int unit;

		CalendarUnit(int calUnit) {
			unit = calUnit;
		};
	}

	protected void testPrecision(String listEntry, CalendarUnit expectedPrecision) {
		FTPFile file = getParser().parseFTPEntry(listEntry);
		assertNotNull("Could not parse " + listEntry, file);
		Calendar stamp = file.getTimestamp();
		assertNotNull("Failed to parse time in " + listEntry, stamp);
		final int ordinal = expectedPrecision.ordinal();
		final CalendarUnit[] values = CalendarUnit.values();
		// Check expected unit and all more significant ones are set
		// This is needed for FTPFile.toFormattedString() to work correctly
		for (int i = ordinal; i < values.length; i++) {
			CalendarUnit unit = values[i];
			assertTrue("Expected set " + unit + " in " + listEntry, stamp.isSet(unit.unit));
		}
		// Check previous entry (if any) is not set
		// This is also needed for FTPFile.toFormattedString() to work correctly
		if (ordinal > 0) {
			final CalendarUnit prevUnit = values[ordinal - 1];
			assertFalse("Expected not set " + prevUnit + " in " + listEntry, stamp.isSet(prevUnit.unit));
		}
	}

	// Force subclasses to test precision
	public abstract void testDefaultPrecision();

	public abstract void testRecentPrecision();
}
