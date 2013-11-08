package panda.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests {@link DateTimes}.
 */
public class DateTimesTest {

	private static final long MILLIS_TEST;
	static {
		final GregorianCalendar cal = new GregorianCalendar(2000, 6, 5, 4, 3, 2);
		cal.set(Calendar.MILLISECOND, 1);
		MILLIS_TEST = cal.getTime().getTime();
		System.out.println("DateUtilsTest: Default Locale=" + Locale.getDefault());
	}

	DateFormat dateParser = null;
	DateFormat dateTimeParser = null;
	DateFormat timeZoneDateParser = null;
	Date dateAmPm1 = null;
	Date dateAmPm2 = null;
	Date dateAmPm3 = null;
	Date dateAmPm4 = null;
	Date date0 = null;
	Date date1 = null;
	Date date2 = null;
	Date date3 = null;
	Date date4 = null;
	Date date5 = null;
	Date date6 = null;
	Date date7 = null;
	Date date8 = null;
	Calendar calAmPm1 = null;
	Calendar calAmPm2 = null;
	Calendar calAmPm3 = null;
	Calendar calAmPm4 = null;
	Calendar cal1 = null;
	Calendar cal2 = null;
	Calendar cal3 = null;
	Calendar cal4 = null;
	Calendar cal5 = null;
	Calendar cal6 = null;
	Calendar cal7 = null;
	Calendar cal8 = null;
	TimeZone zone = null;
	TimeZone defaultZone = null;

	@Before
	public void setUp() throws Exception {

		dateParser = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
		dateTimeParser = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);

		dateAmPm1 = dateTimeParser.parse("February 3, 2002 01:10:00.000");
		dateAmPm2 = dateTimeParser.parse("February 3, 2002 11:10:00.000");
		dateAmPm3 = dateTimeParser.parse("February 3, 2002 13:10:00.000");
		dateAmPm4 = dateTimeParser.parse("February 3, 2002 19:10:00.000");
		date0 = dateTimeParser.parse("February 3, 2002 12:34:56.789");
		date1 = dateTimeParser.parse("February 12, 2002 12:34:56.789");
		date2 = dateTimeParser.parse("November 18, 2001 1:23:11.321");
		defaultZone = TimeZone.getDefault();
		zone = TimeZone.getTimeZone("MET");
		TimeZone.setDefault(zone);
		dateTimeParser.setTimeZone(zone);
		date3 = dateTimeParser.parse("March 30, 2003 05:30:45.000");
		date4 = dateTimeParser.parse("March 30, 2003 01:10:00.000");
		date5 = dateTimeParser.parse("March 30, 2003 01:40:00.000");
		date6 = dateTimeParser.parse("March 30, 2003 02:10:00.000");
		date7 = dateTimeParser.parse("March 30, 2003 02:40:00.000");
		date8 = dateTimeParser.parse("October 26, 2003 05:30:45.000");
		dateTimeParser.setTimeZone(defaultZone);
		TimeZone.setDefault(defaultZone);
		calAmPm1 = Calendar.getInstance();
		calAmPm1.setTime(dateAmPm1);
		calAmPm2 = Calendar.getInstance();
		calAmPm2.setTime(dateAmPm2);
		calAmPm3 = Calendar.getInstance();
		calAmPm3.setTime(dateAmPm3);
		calAmPm4 = Calendar.getInstance();
		calAmPm4.setTime(dateAmPm4);
		cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		TimeZone.setDefault(zone);
		cal3 = Calendar.getInstance();
		cal3.setTime(date3);
		cal4 = Calendar.getInstance();
		cal4.setTime(date4);
		cal5 = Calendar.getInstance();
		cal5.setTime(date5);
		cal6 = Calendar.getInstance();
		cal6.setTime(date6);
		cal7 = Calendar.getInstance();
		cal7.setTime(date7);
		cal8 = Calendar.getInstance();
		cal8.setTime(date8);
		TimeZone.setDefault(defaultZone);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIsSameDay_Date() {
		Date datea = new GregorianCalendar(2004, 6, 9, 13, 45).getTime();
		Date dateb = new GregorianCalendar(2004, 6, 9, 13, 45).getTime();
		assertTrue(DateTimes.isSameDay(datea, dateb));
		dateb = new GregorianCalendar(2004, 6, 10, 13, 45).getTime();
		assertFalse(DateTimes.isSameDay(datea, dateb));
		datea = new GregorianCalendar(2004, 6, 10, 13, 45).getTime();
		assertTrue(DateTimes.isSameDay(datea, dateb));
		dateb = new GregorianCalendar(2005, 6, 10, 13, 45).getTime();
		assertFalse(DateTimes.isSameDay(datea, dateb));
		try {
			DateTimes.isSameDay((Date)null, (Date)null);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIsSameDay_Cal() {
		final GregorianCalendar cala = new GregorianCalendar(2004, 6, 9, 13, 45);
		final GregorianCalendar calb = new GregorianCalendar(2004, 6, 9, 13, 45);
		assertTrue(DateTimes.isSameDay(cala, calb));
		calb.add(Calendar.DAY_OF_YEAR, 1);
		assertFalse(DateTimes.isSameDay(cala, calb));
		cala.add(Calendar.DAY_OF_YEAR, 1);
		assertTrue(DateTimes.isSameDay(cala, calb));
		calb.add(Calendar.YEAR, 1);
		assertFalse(DateTimes.isSameDay(cala, calb));
		try {
			DateTimes.isSameDay((Calendar)null, (Calendar)null);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIsSameInstant_Date() {
		Date datea = new GregorianCalendar(2004, 6, 9, 13, 45).getTime();
		Date dateb = new GregorianCalendar(2004, 6, 9, 13, 45).getTime();
		assertTrue(DateTimes.isSameInstant(datea, dateb));
		dateb = new GregorianCalendar(2004, 6, 10, 13, 45).getTime();
		assertFalse(DateTimes.isSameInstant(datea, dateb));
		datea = new GregorianCalendar(2004, 6, 10, 13, 45).getTime();
		assertTrue(DateTimes.isSameInstant(datea, dateb));
		dateb = new GregorianCalendar(2005, 6, 10, 13, 45).getTime();
		assertFalse(DateTimes.isSameInstant(datea, dateb));
		try {
			DateTimes.isSameInstant((Date)null, (Date)null);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIsSameInstant_Cal() {
		final GregorianCalendar cala = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
		final GregorianCalendar calb = new GregorianCalendar(TimeZone.getTimeZone("GMT-1"));
		cala.set(2004, 6, 9, 13, 45, 0);
		cala.set(Calendar.MILLISECOND, 0);
		calb.set(2004, 6, 9, 13, 45, 0);
		calb.set(Calendar.MILLISECOND, 0);
		assertFalse(DateTimes.isSameInstant(cala, calb));

		calb.set(2004, 6, 9, 11, 45, 0);
		assertTrue(DateTimes.isSameInstant(cala, calb));
		try {
			DateTimes.isSameInstant((Calendar)null, (Calendar)null);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testIsSameLocalTime_Cal() {
		final GregorianCalendar cala = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
		final GregorianCalendar calb = new GregorianCalendar(TimeZone.getTimeZone("GMT-1"));
		cala.set(2004, 6, 9, 13, 45, 0);
		cala.set(Calendar.MILLISECOND, 0);
		calb.set(2004, 6, 9, 13, 45, 0);
		calb.set(Calendar.MILLISECOND, 0);
		assertTrue(DateTimes.isSameLocalTime(cala, calb));

		final Calendar calc = Calendar.getInstance();
		final Calendar cald = Calendar.getInstance();
		calc.set(2004, 6, 9, 4, 0, 0);
		cald.set(2004, 6, 9, 16, 0, 0);
		calc.set(Calendar.MILLISECOND, 0);
		cald.set(Calendar.MILLISECOND, 0);
		assertFalse("LANG-677", DateTimes.isSameLocalTime(calc, cald));

		calb.set(2004, 6, 9, 11, 45, 0);
		assertFalse(DateTimes.isSameLocalTime(cala, calb));
		try {
			DateTimes.isSameLocalTime((Calendar)null, (Calendar)null);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testParseDate() throws Exception {
		final GregorianCalendar cal = new GregorianCalendar(1972, 11, 3);
		String dateStr = "1972-12-03";
		final String[] parsers = new String[] { "yyyy'-'DDD", "yyyy'-'MM'-'dd", "yyyyMMdd" };
		
		//TODO: bugfix
		Date date = DateTimes.parse(dateStr, parsers);
		//assertEquals(cal.getTime(), date);

		dateStr = "1972-338";
		date = DateTimes.parse(dateStr, parsers);
		assertEquals(cal.getTime(), date);

		dateStr = "19721203";
		date = DateTimes.parse(dateStr, parsers);
		assertEquals(cal.getTime(), date);

		try {
			DateTimes.parse("PURPLE", parsers);
			fail();
		}
		catch (final ParseException ex) {
		}
		try {
			DateTimes.parse("197212AB", parsers);
			fail();
		}
		catch (final ParseException ex) {
		}
		try {
			DateTimes.parse(null, parsers);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.parse(dateStr, (String[])null);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.parse(dateStr, new String[0]);
			fail();
		}
		catch (final ParseException ex) {
		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testAddYears() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.addYears(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

		result = DateTimes.addYears(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2001, 6, 5, 4, 3, 2, 1);

		result = DateTimes.addYears(base, -1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 1999, 6, 5, 4, 3, 2, 1);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testAddMonths() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.addMonths(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

		result = DateTimes.addMonths(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 7, 5, 4, 3, 2, 1);

		result = DateTimes.addMonths(base, -1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 5, 5, 4, 3, 2, 1);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testAddWeeks() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.addWeeks(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

		result = DateTimes.addWeeks(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 12, 4, 3, 2, 1);

		result = DateTimes.addWeeks(base, -1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1); // july
		assertDate(result, 2000, 5, 28, 4, 3, 2, 1); // june
	}

	// -----------------------------------------------------------------------
	@Test
	public void testAddDays() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.addDays(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

		result = DateTimes.addDays(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 6, 4, 3, 2, 1);

		result = DateTimes.addDays(base, -1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 4, 4, 3, 2, 1);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testAddHours() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.addHours(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

		result = DateTimes.addHours(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 5, 3, 2, 1);

		result = DateTimes.addHours(base, -1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 3, 3, 2, 1);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testAddMinutes() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.addMinutes(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

		result = DateTimes.addMinutes(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 4, 2, 1);

		result = DateTimes.addMinutes(base, -1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 2, 2, 1);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testAddSeconds() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.addSeconds(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

		result = DateTimes.addSeconds(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 3, 1);

		result = DateTimes.addSeconds(base, -1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 1, 1);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testAddMilliseconds() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.addMilliseconds(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

		result = DateTimes.addMilliseconds(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 2);

		result = DateTimes.addMilliseconds(base, -1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 0);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSetYears() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.setYears(base, 2000);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

		result = DateTimes.setYears(base, 2008);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2008, 6, 5, 4, 3, 2, 1);

		result = DateTimes.setYears(base, 2005);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2005, 6, 5, 4, 3, 2, 1);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSetMonths() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.setMonths(base, 5);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 5, 5, 4, 3, 2, 1);

		result = DateTimes.setMonths(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 1, 5, 4, 3, 2, 1);

		try {
			result = DateTimes.setMonths(base, 12);
			fail("DateTimes.setMonths did not throw an expected IllegalArguementException.");
		}
		catch (final IllegalArgumentException e) {

		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSetDays() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.setDays(base, 1);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 1, 4, 3, 2, 1);

		result = DateTimes.setDays(base, 29);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 29, 4, 3, 2, 1);

		try {
			result = DateTimes.setDays(base, 32);
			fail("DateTimes.setDays did not throw an expected IllegalArguementException.");
		}
		catch (final IllegalArgumentException e) {

		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSetHours() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.setHours(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 0, 3, 2, 1);

		result = DateTimes.setHours(base, 23);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 23, 3, 2, 1);

		try {
			result = DateTimes.setHours(base, 24);
			fail("DateTimes.setHours did not throw an expected IllegalArguementException.");
		}
		catch (final IllegalArgumentException e) {

		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSetMinutes() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.setMinutes(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 0, 2, 1);

		result = DateTimes.setMinutes(base, 59);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 59, 2, 1);

		try {
			result = DateTimes.setMinutes(base, 60);
			fail("DateTimes.setMinutes did not throw an expected IllegalArguementException.");
		}
		catch (final IllegalArgumentException e) {

		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSetSeconds() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.setSeconds(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 0, 1);

		result = DateTimes.setSeconds(base, 59);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 59, 1);

		try {
			result = DateTimes.setSeconds(base, 60);
			fail("DateTimes.setSeconds did not throw an expected IllegalArguementException.");
		}
		catch (final IllegalArgumentException e) {

		}
	}

	// -----------------------------------------------------------------------
	@Test
	public void testSetMilliseconds() throws Exception {
		final Date base = new Date(MILLIS_TEST);
		Date result = DateTimes.setMilliseconds(base, 0);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 0);

		result = DateTimes.setMilliseconds(base, 999);
		assertNotSame(base, result);
		assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
		assertDate(result, 2000, 6, 5, 4, 3, 2, 999);

		try {
			result = DateTimes.setMilliseconds(base, 1000);
			fail("DateTimes.setMilliseconds did not throw an expected IllegalArguementException.");
		}
		catch (final IllegalArgumentException e) {

		}
	}

	// -----------------------------------------------------------------------
	private void assertDate(final Date date, final int year, final int month, final int day, final int hour,
			final int min, final int sec, final int mil) throws Exception {
		final GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		assertEquals(year, cal.get(Calendar.YEAR));
		assertEquals(month, cal.get(Calendar.MONTH));
		assertEquals(day, cal.get(Calendar.DAY_OF_MONTH));
		assertEquals(hour, cal.get(Calendar.HOUR_OF_DAY));
		assertEquals(min, cal.get(Calendar.MINUTE));
		assertEquals(sec, cal.get(Calendar.SECOND));
		assertEquals(mil, cal.get(Calendar.MILLISECOND));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testToCalendar() {
		assertEquals("Failed to convert to a Calendar and back", date1, DateTimes.toCalendar(date1).getTime());
		try {
			DateTimes.toCalendar(null);
			fail("Expected NullPointerException to be thrown");
		}
		catch (final NullPointerException npe) {
			// expected
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Tests various values with the round method
	 */
	@Test
	public void testRound() throws Exception {
		// tests for public static Date round(Date date, int field)
		assertEquals("round year-1 failed", dateParser.parse("January 1, 2002"), DateTimes.round(date1, Calendar.YEAR));
		assertEquals("round year-2 failed", dateParser.parse("January 1, 2002"), DateTimes.round(date2, Calendar.YEAR));
		assertEquals("round month-1 failed", dateParser.parse("February 1, 2002"),
			DateTimes.round(date1, Calendar.MONTH));
		assertEquals("round month-2 failed", dateParser.parse("December 1, 2001"),
			DateTimes.round(date2, Calendar.MONTH));
		assertEquals("round semimonth-0 failed", dateParser.parse("February 1, 2002"),
			DateTimes.round(date0, DateTimes.SEMI_MONTH));
		assertEquals("round semimonth-1 failed", dateParser.parse("February 16, 2002"),
			DateTimes.round(date1, DateTimes.SEMI_MONTH));
		assertEquals("round semimonth-2 failed", dateParser.parse("November 16, 2001"),
			DateTimes.round(date2, DateTimes.SEMI_MONTH));

		assertEquals("round date-1 failed", dateParser.parse("February 13, 2002"),
			DateTimes.round(date1, Calendar.DATE));
		assertEquals("round date-2 failed", dateParser.parse("November 18, 2001"),
			DateTimes.round(date2, Calendar.DATE));
		assertEquals("round hour-1 failed", dateTimeParser.parse("February 12, 2002 13:00:00.000"),
			DateTimes.round(date1, Calendar.HOUR));
		assertEquals("round hour-2 failed", dateTimeParser.parse("November 18, 2001 1:00:00.000"),
			DateTimes.round(date2, Calendar.HOUR));
		assertEquals("round minute-1 failed", dateTimeParser.parse("February 12, 2002 12:35:00.000"),
			DateTimes.round(date1, Calendar.MINUTE));
		assertEquals("round minute-2 failed", dateTimeParser.parse("November 18, 2001 1:23:00.000"),
			DateTimes.round(date2, Calendar.MINUTE));
		assertEquals("round second-1 failed", dateTimeParser.parse("February 12, 2002 12:34:57.000"),
			DateTimes.round(date1, Calendar.SECOND));
		assertEquals("round second-2 failed", dateTimeParser.parse("November 18, 2001 1:23:11.000"),
			DateTimes.round(date2, Calendar.SECOND));
		assertEquals("round ampm-1 failed", dateTimeParser.parse("February 3, 2002 00:00:00.000"),
			DateTimes.round(dateAmPm1, Calendar.AM_PM));
		assertEquals("round ampm-2 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.round(dateAmPm2, Calendar.AM_PM));
		assertEquals("round ampm-3 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.round(dateAmPm3, Calendar.AM_PM));
		assertEquals("round ampm-4 failed", dateTimeParser.parse("February 4, 2002 00:00:00.000"),
			DateTimes.round(dateAmPm4, Calendar.AM_PM));

		// tests for public static Date round(Object date, int field)
		assertEquals("round year-1 failed", dateParser.parse("January 1, 2002"),
			DateTimes.round((Object)date1, Calendar.YEAR));
		assertEquals("round year-2 failed", dateParser.parse("January 1, 2002"),
			DateTimes.round((Object)date2, Calendar.YEAR));
		assertEquals("round month-1 failed", dateParser.parse("February 1, 2002"),
			DateTimes.round((Object)date1, Calendar.MONTH));
		assertEquals("round month-2 failed", dateParser.parse("December 1, 2001"),
			DateTimes.round((Object)date2, Calendar.MONTH));
		assertEquals("round semimonth-1 failed", dateParser.parse("February 16, 2002"),
			DateTimes.round((Object)date1, DateTimes.SEMI_MONTH));
		assertEquals("round semimonth-2 failed", dateParser.parse("November 16, 2001"),
			DateTimes.round((Object)date2, DateTimes.SEMI_MONTH));
		assertEquals("round date-1 failed", dateParser.parse("February 13, 2002"),
			DateTimes.round((Object)date1, Calendar.DATE));
		assertEquals("round date-2 failed", dateParser.parse("November 18, 2001"),
			DateTimes.round((Object)date2, Calendar.DATE));
		assertEquals("round hour-1 failed", dateTimeParser.parse("February 12, 2002 13:00:00.000"),
			DateTimes.round((Object)date1, Calendar.HOUR));
		assertEquals("round hour-2 failed", dateTimeParser.parse("November 18, 2001 1:00:00.000"),
			DateTimes.round((Object)date2, Calendar.HOUR));
		assertEquals("round minute-1 failed", dateTimeParser.parse("February 12, 2002 12:35:00.000"),
			DateTimes.round((Object)date1, Calendar.MINUTE));
		assertEquals("round minute-2 failed", dateTimeParser.parse("November 18, 2001 1:23:00.000"),
			DateTimes.round((Object)date2, Calendar.MINUTE));
		assertEquals("round second-1 failed", dateTimeParser.parse("February 12, 2002 12:34:57.000"),
			DateTimes.round((Object)date1, Calendar.SECOND));
		assertEquals("round second-2 failed", dateTimeParser.parse("November 18, 2001 1:23:11.000"),
			DateTimes.round((Object)date2, Calendar.SECOND));
		assertEquals("round calendar second-1 failed", dateTimeParser.parse("February 12, 2002 12:34:57.000"),
			DateTimes.round((Object)cal1, Calendar.SECOND));
		assertEquals("round calendar second-2 failed", dateTimeParser.parse("November 18, 2001 1:23:11.000"),
			DateTimes.round((Object)cal2, Calendar.SECOND));
		assertEquals("round ampm-1 failed", dateTimeParser.parse("February 3, 2002 00:00:00.000"),
			DateTimes.round((Object)dateAmPm1, Calendar.AM_PM));
		assertEquals("round ampm-2 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.round((Object)dateAmPm2, Calendar.AM_PM));
		assertEquals("round ampm-3 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.round((Object)dateAmPm3, Calendar.AM_PM));
		assertEquals("round ampm-4 failed", dateTimeParser.parse("February 4, 2002 00:00:00.000"),
			DateTimes.round((Object)dateAmPm4, Calendar.AM_PM));

		try {
			DateTimes.round((Date)null, Calendar.SECOND);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.round((Calendar)null, Calendar.SECOND);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.round((Object)null, Calendar.SECOND);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.round("", Calendar.SECOND);
			fail();
		}
		catch (final ClassCastException ex) {
		}
		try {
			DateTimes.round(date1, -9999);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}

		assertEquals("round ampm-1 failed", dateTimeParser.parse("February 3, 2002 00:00:00.000"),
			DateTimes.round((Object)calAmPm1, Calendar.AM_PM));
		assertEquals("round ampm-2 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.round((Object)calAmPm2, Calendar.AM_PM));
		assertEquals("round ampm-3 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.round((Object)calAmPm3, Calendar.AM_PM));
		assertEquals("round ampm-4 failed", dateTimeParser.parse("February 4, 2002 00:00:00.000"),
			DateTimes.round((Object)calAmPm4, Calendar.AM_PM));

		// Fix for http://issues.apache.org/bugzilla/show_bug.cgi?id=25560 / LANG-13
		// Test rounding across the beginning of daylight saving time
		TimeZone.setDefault(zone);
		dateTimeParser.setTimeZone(zone);
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.round(date4, Calendar.DATE));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.round((Object)cal4, Calendar.DATE));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.round(date5, Calendar.DATE));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.round((Object)cal5, Calendar.DATE));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.round(date6, Calendar.DATE));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.round((Object)cal6, Calendar.DATE));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.round(date7, Calendar.DATE));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.round((Object)cal7, Calendar.DATE));

		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 01:00:00.000"),
			DateTimes.round(date4, Calendar.HOUR_OF_DAY));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 01:00:00.000"),
			DateTimes.round((Object)cal4, Calendar.HOUR_OF_DAY));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 03:00:00.000"),
			DateTimes.round(date5, Calendar.HOUR_OF_DAY));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 03:00:00.000"),
			DateTimes.round((Object)cal5, Calendar.HOUR_OF_DAY));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 03:00:00.000"),
			DateTimes.round(date6, Calendar.HOUR_OF_DAY));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 03:00:00.000"),
			DateTimes.round((Object)cal6, Calendar.HOUR_OF_DAY));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 04:00:00.000"),
			DateTimes.round(date7, Calendar.HOUR_OF_DAY));
		assertEquals("round MET date across DST change-over", dateTimeParser.parse("March 30, 2003 04:00:00.000"),
			DateTimes.round((Object)cal7, Calendar.HOUR_OF_DAY));
		TimeZone.setDefault(defaultZone);
		dateTimeParser.setTimeZone(defaultZone);
	}

	/**
	 * Tests the Changes Made by LANG-346 to the DateTimes.modify() private method invoked by
	 * DateTimes.round().
	 */
	@Test
	public void testRoundLang346() throws Exception {
		TimeZone.setDefault(defaultZone);
		dateTimeParser.setTimeZone(defaultZone);
		final Calendar testCalendar = Calendar.getInstance();
		testCalendar.set(2007, 6, 2, 8, 8, 50);
		Date date = testCalendar.getTime();
		assertEquals("Minute Round Up Failed", dateTimeParser.parse("July 2, 2007 08:09:00.000"),
			DateTimes.round(date, Calendar.MINUTE));

		testCalendar.set(2007, 6, 2, 8, 8, 20);
		date = testCalendar.getTime();
		assertEquals("Minute No Round Failed", dateTimeParser.parse("July 2, 2007 08:08:00.000"),
			DateTimes.round(date, Calendar.MINUTE));

		testCalendar.set(2007, 6, 2, 8, 8, 50);
		testCalendar.set(Calendar.MILLISECOND, 600);
		date = testCalendar.getTime();

		assertEquals("Second Round Up with 600 Milli Seconds Failed",
			dateTimeParser.parse("July 2, 2007 08:08:51.000"), DateTimes.round(date, Calendar.SECOND));

		testCalendar.set(2007, 6, 2, 8, 8, 50);
		testCalendar.set(Calendar.MILLISECOND, 200);
		date = testCalendar.getTime();
		assertEquals("Second Round Down with 200 Milli Seconds Failed",
			dateTimeParser.parse("July 2, 2007 08:08:50.000"), DateTimes.round(date, Calendar.SECOND));

		testCalendar.set(2007, 6, 2, 8, 8, 20);
		testCalendar.set(Calendar.MILLISECOND, 600);
		date = testCalendar.getTime();
		assertEquals("Second Round Up with 200 Milli Seconds Failed",
			dateTimeParser.parse("July 2, 2007 08:08:21.000"), DateTimes.round(date, Calendar.SECOND));

		testCalendar.set(2007, 6, 2, 8, 8, 20);
		testCalendar.set(Calendar.MILLISECOND, 200);
		date = testCalendar.getTime();
		assertEquals("Second Round Down with 200 Milli Seconds Failed",
			dateTimeParser.parse("July 2, 2007 08:08:20.000"), DateTimes.round(date, Calendar.SECOND));

		testCalendar.set(2007, 6, 2, 8, 8, 50);
		date = testCalendar.getTime();
		assertEquals("Hour Round Down Failed", dateTimeParser.parse("July 2, 2007 08:00:00.000"),
			DateTimes.round(date, Calendar.HOUR));

		testCalendar.set(2007, 6, 2, 8, 31, 50);
		date = testCalendar.getTime();
		assertEquals("Hour Round Up Failed", dateTimeParser.parse("July 2, 2007 09:00:00.000"),
			DateTimes.round(date, Calendar.HOUR));
	}

	/**
	 * Tests various values with the trunc method
	 */
	@Test
	public void testTruncate() throws Exception {
		// tests public static Date truncate(Date date, int field)
		assertEquals("truncate year-1 failed", dateParser.parse("January 1, 2002"),
			DateTimes.truncate(date1, Calendar.YEAR));
		assertEquals("truncate year-2 failed", dateParser.parse("January 1, 2001"),
			DateTimes.truncate(date2, Calendar.YEAR));
		assertEquals("truncate month-1 failed", dateParser.parse("February 1, 2002"),
			DateTimes.truncate(date1, Calendar.MONTH));
		assertEquals("truncate month-2 failed", dateParser.parse("November 1, 2001"),
			DateTimes.truncate(date2, Calendar.MONTH));
		assertEquals("truncate semimonth-1 failed", dateParser.parse("February 1, 2002"),
			DateTimes.truncate(date1, DateTimes.SEMI_MONTH));
		assertEquals("truncate semimonth-2 failed", dateParser.parse("November 16, 2001"),
			DateTimes.truncate(date2, DateTimes.SEMI_MONTH));
		assertEquals("truncate date-1 failed", dateParser.parse("February 12, 2002"),
			DateTimes.truncate(date1, Calendar.DATE));
		assertEquals("truncate date-2 failed", dateParser.parse("November 18, 2001"),
			DateTimes.truncate(date2, Calendar.DATE));
		assertEquals("truncate hour-1 failed", dateTimeParser.parse("February 12, 2002 12:00:00.000"),
			DateTimes.truncate(date1, Calendar.HOUR));
		assertEquals("truncate hour-2 failed", dateTimeParser.parse("November 18, 2001 1:00:00.000"),
			DateTimes.truncate(date2, Calendar.HOUR));
		assertEquals("truncate minute-1 failed", dateTimeParser.parse("February 12, 2002 12:34:00.000"),
			DateTimes.truncate(date1, Calendar.MINUTE));
		assertEquals("truncate minute-2 failed", dateTimeParser.parse("November 18, 2001 1:23:00.000"),
			DateTimes.truncate(date2, Calendar.MINUTE));
		assertEquals("truncate second-1 failed", dateTimeParser.parse("February 12, 2002 12:34:56.000"),
			DateTimes.truncate(date1, Calendar.SECOND));
		assertEquals("truncate second-2 failed", dateTimeParser.parse("November 18, 2001 1:23:11.000"),
			DateTimes.truncate(date2, Calendar.SECOND));
		assertEquals("truncate ampm-1 failed", dateTimeParser.parse("February 3, 2002 00:00:00.000"),
			DateTimes.truncate(dateAmPm1, Calendar.AM_PM));
		assertEquals("truncate ampm-2 failed", dateTimeParser.parse("February 3, 2002 00:00:00.000"),
			DateTimes.truncate(dateAmPm2, Calendar.AM_PM));
		assertEquals("truncate ampm-3 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.truncate(dateAmPm3, Calendar.AM_PM));
		assertEquals("truncate ampm-4 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.truncate(dateAmPm4, Calendar.AM_PM));

		// tests public static Date truncate(Object date, int field)
		assertEquals("truncate year-1 failed", dateParser.parse("January 1, 2002"),
			DateTimes.truncate((Object)date1, Calendar.YEAR));
		assertEquals("truncate year-2 failed", dateParser.parse("January 1, 2001"),
			DateTimes.truncate((Object)date2, Calendar.YEAR));
		assertEquals("truncate month-1 failed", dateParser.parse("February 1, 2002"),
			DateTimes.truncate((Object)date1, Calendar.MONTH));
		assertEquals("truncate month-2 failed", dateParser.parse("November 1, 2001"),
			DateTimes.truncate((Object)date2, Calendar.MONTH));
		assertEquals("truncate semimonth-1 failed", dateParser.parse("February 1, 2002"),
			DateTimes.truncate((Object)date1, DateTimes.SEMI_MONTH));
		assertEquals("truncate semimonth-2 failed", dateParser.parse("November 16, 2001"),
			DateTimes.truncate((Object)date2, DateTimes.SEMI_MONTH));
		assertEquals("truncate date-1 failed", dateParser.parse("February 12, 2002"),
			DateTimes.truncate((Object)date1, Calendar.DATE));
		assertEquals("truncate date-2 failed", dateParser.parse("November 18, 2001"),
			DateTimes.truncate((Object)date2, Calendar.DATE));
		assertEquals("truncate hour-1 failed", dateTimeParser.parse("February 12, 2002 12:00:00.000"),
			DateTimes.truncate((Object)date1, Calendar.HOUR));
		assertEquals("truncate hour-2 failed", dateTimeParser.parse("November 18, 2001 1:00:00.000"),
			DateTimes.truncate((Object)date2, Calendar.HOUR));
		assertEquals("truncate minute-1 failed", dateTimeParser.parse("February 12, 2002 12:34:00.000"),
			DateTimes.truncate((Object)date1, Calendar.MINUTE));
		assertEquals("truncate minute-2 failed", dateTimeParser.parse("November 18, 2001 1:23:00.000"),
			DateTimes.truncate((Object)date2, Calendar.MINUTE));
		assertEquals("truncate second-1 failed", dateTimeParser.parse("February 12, 2002 12:34:56.000"),
			DateTimes.truncate((Object)date1, Calendar.SECOND));
		assertEquals("truncate second-2 failed", dateTimeParser.parse("November 18, 2001 1:23:11.000"),
			DateTimes.truncate((Object)date2, Calendar.SECOND));
		assertEquals("truncate ampm-1 failed", dateTimeParser.parse("February 3, 2002 00:00:00.000"),
			DateTimes.truncate((Object)dateAmPm1, Calendar.AM_PM));
		assertEquals("truncate ampm-2 failed", dateTimeParser.parse("February 3, 2002 00:00:00.000"),
			DateTimes.truncate((Object)dateAmPm2, Calendar.AM_PM));
		assertEquals("truncate ampm-3 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.truncate((Object)dateAmPm3, Calendar.AM_PM));
		assertEquals("truncate ampm-4 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.truncate((Object)dateAmPm4, Calendar.AM_PM));

		assertEquals("truncate calendar second-1 failed", dateTimeParser.parse("February 12, 2002 12:34:56.000"),
			DateTimes.truncate((Object)cal1, Calendar.SECOND));
		assertEquals("truncate calendar second-2 failed", dateTimeParser.parse("November 18, 2001 1:23:11.000"),
			DateTimes.truncate((Object)cal2, Calendar.SECOND));

		assertEquals("truncate ampm-1 failed", dateTimeParser.parse("February 3, 2002 00:00:00.000"),
			DateTimes.truncate((Object)calAmPm1, Calendar.AM_PM));
		assertEquals("truncate ampm-2 failed", dateTimeParser.parse("February 3, 2002 00:00:00.000"),
			DateTimes.truncate((Object)calAmPm2, Calendar.AM_PM));
		assertEquals("truncate ampm-3 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.truncate((Object)calAmPm3, Calendar.AM_PM));
		assertEquals("truncate ampm-4 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.truncate((Object)calAmPm4, Calendar.AM_PM));

		try {
			DateTimes.truncate((Date)null, Calendar.SECOND);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.truncate((Calendar)null, Calendar.SECOND);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.truncate((Object)null, Calendar.SECOND);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.truncate("", Calendar.SECOND);
			fail();
		}
		catch (final ClassCastException ex) {
		}

		// Fix for http://issues.apache.org/bugzilla/show_bug.cgi?id=25560
		// Test truncate across beginning of daylight saving time
		TimeZone.setDefault(zone);
		dateTimeParser.setTimeZone(zone);
		assertEquals("truncate MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.truncate(date3, Calendar.DATE));
		assertEquals("truncate MET date across DST change-over", dateTimeParser.parse("March 30, 2003 00:00:00.000"),
			DateTimes.truncate((Object)cal3, Calendar.DATE));
		// Test truncate across end of daylight saving time
		assertEquals("truncate MET date across DST change-over", dateTimeParser.parse("October 26, 2003 00:00:00.000"),
			DateTimes.truncate(date8, Calendar.DATE));
		assertEquals("truncate MET date across DST change-over", dateTimeParser.parse("October 26, 2003 00:00:00.000"),
			DateTimes.truncate((Object)cal8, Calendar.DATE));
		TimeZone.setDefault(defaultZone);
		dateTimeParser.setTimeZone(defaultZone);

		// Bug 31395, large dates
		final Date endOfTime = new Date(Long.MAX_VALUE); // fyi: Sun Aug 17 07:12:55 CET 292278994
															// -- 807 millis
		final GregorianCalendar endCal = new GregorianCalendar();
		endCal.setTime(endOfTime);
		try {
			DateTimes.truncate(endCal, Calendar.DATE);
			fail();
		}
		catch (final ArithmeticException ex) {
		}
		endCal.set(Calendar.YEAR, 280000001);
		try {
			DateTimes.truncate(endCal, Calendar.DATE);
			fail();
		}
		catch (final ArithmeticException ex) {
		}
		endCal.set(Calendar.YEAR, 280000000);
		final Calendar cal = DateTimes.truncate(endCal, Calendar.DATE);
		assertEquals(0, cal.get(Calendar.HOUR));
	}

	/**
	 * Tests for LANG-59 see http://issues.apache.org/jira/browse/LANG-59
	 */
	@Test
	public void testTruncateLang59() throws Exception {
		// Set TimeZone to Mountain Time
		final TimeZone MST_MDT = TimeZone.getTimeZone("MST7MDT");
		TimeZone.setDefault(MST_MDT);
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
		format.setTimeZone(MST_MDT);

		final Date oct31_01MDT = new Date(1099206000000L);

		final Date oct31MDT = new Date(oct31_01MDT.getTime() - 3600000L); // - 1 hour
		final Date oct31_01_02MDT = new Date(oct31_01MDT.getTime() + 120000L); // + 2 minutes
		final Date oct31_01_02_03MDT = new Date(oct31_01_02MDT.getTime() + 3000L); // + 3 seconds
		final Date oct31_01_02_03_04MDT = new Date(oct31_01_02_03MDT.getTime() + 4L); // + 4
																						// milliseconds

		assertEquals("Check 00:00:00.000", "2004-10-31 00:00:00.000 MDT", format.format(oct31MDT));
		assertEquals("Check 01:00:00.000", "2004-10-31 01:00:00.000 MDT", format.format(oct31_01MDT));
		assertEquals("Check 01:02:00.000", "2004-10-31 01:02:00.000 MDT", format.format(oct31_01_02MDT));
		assertEquals("Check 01:02:03.000", "2004-10-31 01:02:03.000 MDT", format.format(oct31_01_02_03MDT));
		assertEquals("Check 01:02:03.004", "2004-10-31 01:02:03.004 MDT", format.format(oct31_01_02_03_04MDT));

		// ------- Demonstrate Problem -------
		final Calendar gval = Calendar.getInstance();
		gval.setTime(new Date(oct31_01MDT.getTime()));
		gval.set(Calendar.MINUTE, gval.get(Calendar.MINUTE)); // set minutes to the same value
		assertEquals("Demonstrate Problem", gval.getTime().getTime(), oct31_01MDT.getTime() + 3600000L);

		// ---------- Test Truncate ----------
		assertEquals("Truncate Calendar.MILLISECOND", oct31_01_02_03_04MDT,
			DateTimes.truncate(oct31_01_02_03_04MDT, Calendar.MILLISECOND));

		assertEquals("Truncate Calendar.SECOND", oct31_01_02_03MDT,
			DateTimes.truncate(oct31_01_02_03_04MDT, Calendar.SECOND));

		assertEquals("Truncate Calendar.MINUTE", oct31_01_02MDT,
			DateTimes.truncate(oct31_01_02_03_04MDT, Calendar.MINUTE));

		assertEquals("Truncate Calendar.HOUR_OF_DAY", oct31_01MDT,
			DateTimes.truncate(oct31_01_02_03_04MDT, Calendar.HOUR_OF_DAY));

		assertEquals("Truncate Calendar.HOUR", oct31_01MDT, DateTimes.truncate(oct31_01_02_03_04MDT, Calendar.HOUR));

		assertEquals("Truncate Calendar.DATE", oct31MDT, DateTimes.truncate(oct31_01_02_03_04MDT, Calendar.DATE));

		// ---------- Test Round (down) ----------
		assertEquals("Round Calendar.MILLISECOND", oct31_01_02_03_04MDT,
			DateTimes.round(oct31_01_02_03_04MDT, Calendar.MILLISECOND));

		assertEquals("Round Calendar.SECOND", oct31_01_02_03MDT, DateTimes.round(oct31_01_02_03_04MDT, Calendar.SECOND));

		assertEquals("Round Calendar.MINUTE", oct31_01_02MDT, DateTimes.round(oct31_01_02_03_04MDT, Calendar.MINUTE));

		assertEquals("Round Calendar.HOUR_OF_DAY", oct31_01MDT,
			DateTimes.round(oct31_01_02_03_04MDT, Calendar.HOUR_OF_DAY));

		assertEquals("Round Calendar.HOUR", oct31_01MDT, DateTimes.round(oct31_01_02_03_04MDT, Calendar.HOUR));

		assertEquals("Round Calendar.DATE", oct31MDT, DateTimes.round(oct31_01_02_03_04MDT, Calendar.DATE));

		// restore default time zone
		TimeZone.setDefault(defaultZone);
	}

	// http://issues.apache.org/jira/browse/LANG-530
	@Test
	public void testLang530() throws ParseException {
		final Date d = new Date();
		final String isoDateStr = DateTimes.ISO_DATETIME_TIME_ZONE_FORMAT.format(d);
		final Date d2 = DateTimes.parse(isoDateStr,
			new String[] { DateTimes.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern() });
		// the format loses milliseconds so have to reintroduce them
		assertEquals("Date not equal to itself ISO formatted and parsed", d.getTime(), d2.getTime() + d.getTime()
				% 1000);
	}

	/**
	 * Tests various values with the ceiling method
	 */
	@Test
	public void testCeil() throws Exception {
		// test javadoc
		assertEquals("ceiling javadoc-1 failed", dateTimeParser.parse("March 28, 2002 14:00:00.000"),
			DateTimes.ceiling(dateTimeParser.parse("March 28, 2002 13:45:01.231"), Calendar.HOUR));
		assertEquals("ceiling javadoc-2 failed", dateTimeParser.parse("April 1, 2002 00:00:00.000"),
			DateTimes.ceiling(dateTimeParser.parse("March 28, 2002 13:45:01.231"), Calendar.MONTH));

		// tests public static Date ceiling(Date date, int field)
		assertEquals("ceiling year-1 failed", dateParser.parse("January 1, 2003"),
			DateTimes.ceiling(date1, Calendar.YEAR));
		assertEquals("ceiling year-2 failed", dateParser.parse("January 1, 2002"),
			DateTimes.ceiling(date2, Calendar.YEAR));
		assertEquals("ceiling month-1 failed", dateParser.parse("March 1, 2002"),
			DateTimes.ceiling(date1, Calendar.MONTH));
		assertEquals("ceiling month-2 failed", dateParser.parse("December 1, 2001"),
			DateTimes.ceiling(date2, Calendar.MONTH));
		assertEquals("ceiling semimonth-1 failed", dateParser.parse("February 16, 2002"),
			DateTimes.ceiling(date1, DateTimes.SEMI_MONTH));
		assertEquals("ceiling semimonth-2 failed", dateParser.parse("December 1, 2001"),
			DateTimes.ceiling(date2, DateTimes.SEMI_MONTH));
		assertEquals("ceiling date-1 failed", dateParser.parse("February 13, 2002"),
			DateTimes.ceiling(date1, Calendar.DATE));
		assertEquals("ceiling date-2 failed", dateParser.parse("November 19, 2001"),
			DateTimes.ceiling(date2, Calendar.DATE));
		assertEquals("ceiling hour-1 failed", dateTimeParser.parse("February 12, 2002 13:00:00.000"),
			DateTimes.ceiling(date1, Calendar.HOUR));
		assertEquals("ceiling hour-2 failed", dateTimeParser.parse("November 18, 2001 2:00:00.000"),
			DateTimes.ceiling(date2, Calendar.HOUR));
		assertEquals("ceiling minute-1 failed", dateTimeParser.parse("February 12, 2002 12:35:00.000"),
			DateTimes.ceiling(date1, Calendar.MINUTE));
		assertEquals("ceiling minute-2 failed", dateTimeParser.parse("November 18, 2001 1:24:00.000"),
			DateTimes.ceiling(date2, Calendar.MINUTE));
		assertEquals("ceiling second-1 failed", dateTimeParser.parse("February 12, 2002 12:34:57.000"),
			DateTimes.ceiling(date1, Calendar.SECOND));
		assertEquals("ceiling second-2 failed", dateTimeParser.parse("November 18, 2001 1:23:12.000"),
			DateTimes.ceiling(date2, Calendar.SECOND));
		assertEquals("ceiling ampm-1 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.ceiling(dateAmPm1, Calendar.AM_PM));
		assertEquals("ceiling ampm-2 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.ceiling(dateAmPm2, Calendar.AM_PM));
		assertEquals("ceiling ampm-3 failed", dateTimeParser.parse("February 4, 2002 00:00:00.000"),
			DateTimes.ceiling(dateAmPm3, Calendar.AM_PM));
		assertEquals("ceiling ampm-4 failed", dateTimeParser.parse("February 4, 2002 00:00:00.000"),
			DateTimes.ceiling(dateAmPm4, Calendar.AM_PM));

		// tests public static Date ceiling(Object date, int field)
		assertEquals("ceiling year-1 failed", dateParser.parse("January 1, 2003"),
			DateTimes.ceiling((Object)date1, Calendar.YEAR));
		assertEquals("ceiling year-2 failed", dateParser.parse("January 1, 2002"),
			DateTimes.ceiling((Object)date2, Calendar.YEAR));
		assertEquals("ceiling month-1 failed", dateParser.parse("March 1, 2002"),
			DateTimes.ceiling((Object)date1, Calendar.MONTH));
		assertEquals("ceiling month-2 failed", dateParser.parse("December 1, 2001"),
			DateTimes.ceiling((Object)date2, Calendar.MONTH));
		assertEquals("ceiling semimonth-1 failed", dateParser.parse("February 16, 2002"),
			DateTimes.ceiling((Object)date1, DateTimes.SEMI_MONTH));
		assertEquals("ceiling semimonth-2 failed", dateParser.parse("December 1, 2001"),
			DateTimes.ceiling((Object)date2, DateTimes.SEMI_MONTH));
		assertEquals("ceiling date-1 failed", dateParser.parse("February 13, 2002"),
			DateTimes.ceiling((Object)date1, Calendar.DATE));
		assertEquals("ceiling date-2 failed", dateParser.parse("November 19, 2001"),
			DateTimes.ceiling((Object)date2, Calendar.DATE));
		assertEquals("ceiling hour-1 failed", dateTimeParser.parse("February 12, 2002 13:00:00.000"),
			DateTimes.ceiling((Object)date1, Calendar.HOUR));
		assertEquals("ceiling hour-2 failed", dateTimeParser.parse("November 18, 2001 2:00:00.000"),
			DateTimes.ceiling((Object)date2, Calendar.HOUR));
		assertEquals("ceiling minute-1 failed", dateTimeParser.parse("February 12, 2002 12:35:00.000"),
			DateTimes.ceiling((Object)date1, Calendar.MINUTE));
		assertEquals("ceiling minute-2 failed", dateTimeParser.parse("November 18, 2001 1:24:00.000"),
			DateTimes.ceiling((Object)date2, Calendar.MINUTE));
		assertEquals("ceiling second-1 failed", dateTimeParser.parse("February 12, 2002 12:34:57.000"),
			DateTimes.ceiling((Object)date1, Calendar.SECOND));
		assertEquals("ceiling second-2 failed", dateTimeParser.parse("November 18, 2001 1:23:12.000"),
			DateTimes.ceiling((Object)date2, Calendar.SECOND));
		assertEquals("ceiling ampm-1 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.ceiling((Object)dateAmPm1, Calendar.AM_PM));
		assertEquals("ceiling ampm-2 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.ceiling((Object)dateAmPm2, Calendar.AM_PM));
		assertEquals("ceiling ampm-3 failed", dateTimeParser.parse("February 4, 2002 00:00:00.000"),
			DateTimes.ceiling((Object)dateAmPm3, Calendar.AM_PM));
		assertEquals("ceiling ampm-4 failed", dateTimeParser.parse("February 4, 2002 00:00:00.000"),
			DateTimes.ceiling((Object)dateAmPm4, Calendar.AM_PM));

		assertEquals("ceiling calendar second-1 failed", dateTimeParser.parse("February 12, 2002 12:34:57.000"),
			DateTimes.ceiling((Object)cal1, Calendar.SECOND));
		assertEquals("ceiling calendar second-2 failed", dateTimeParser.parse("November 18, 2001 1:23:12.000"),
			DateTimes.ceiling((Object)cal2, Calendar.SECOND));

		assertEquals("ceiling ampm-1 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.ceiling((Object)calAmPm1, Calendar.AM_PM));
		assertEquals("ceiling ampm-2 failed", dateTimeParser.parse("February 3, 2002 12:00:00.000"),
			DateTimes.ceiling((Object)calAmPm2, Calendar.AM_PM));
		assertEquals("ceiling ampm-3 failed", dateTimeParser.parse("February 4, 2002 00:00:00.000"),
			DateTimes.ceiling((Object)calAmPm3, Calendar.AM_PM));
		assertEquals("ceiling ampm-4 failed", dateTimeParser.parse("February 4, 2002 00:00:00.000"),
			DateTimes.ceiling((Object)calAmPm4, Calendar.AM_PM));

		try {
			DateTimes.ceiling((Date)null, Calendar.SECOND);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.ceiling((Calendar)null, Calendar.SECOND);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.ceiling((Object)null, Calendar.SECOND);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.ceiling("", Calendar.SECOND);
			fail();
		}
		catch (final ClassCastException ex) {
		}
		try {
			DateTimes.ceiling(date1, -9999);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}

		// Fix for http://issues.apache.org/bugzilla/show_bug.cgi?id=25560
		// Test ceiling across the beginning of daylight saving time
		TimeZone.setDefault(zone);
		dateTimeParser.setTimeZone(zone);

		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 31, 2003 00:00:00.000"),
			DateTimes.ceiling(date4, Calendar.DATE));
		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 31, 2003 00:00:00.000"),
			DateTimes.ceiling((Object)cal4, Calendar.DATE));
		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 31, 2003 00:00:00.000"),
			DateTimes.ceiling(date5, Calendar.DATE));
		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 31, 2003 00:00:00.000"),
			DateTimes.ceiling((Object)cal5, Calendar.DATE));
		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 31, 2003 00:00:00.000"),
			DateTimes.ceiling(date6, Calendar.DATE));
		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 31, 2003 00:00:00.000"),
			DateTimes.ceiling((Object)cal6, Calendar.DATE));
		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 31, 2003 00:00:00.000"),
			DateTimes.ceiling(date7, Calendar.DATE));
		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 31, 2003 00:00:00.000"),
			DateTimes.ceiling((Object)cal7, Calendar.DATE));

		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 30, 2003 03:00:00.000"),
			DateTimes.ceiling(date4, Calendar.HOUR_OF_DAY));
		assertEquals("ceiling MET date across DST change-over", dateTimeParser.parse("March 30, 2003 03:00:00.000"),
			DateTimes.ceiling((Object)cal4, Calendar.HOUR_OF_DAY));
		assertEquals("ceiling MET date across DST change-over",
			dateTimeParser.parse("March 30, 2003 03:00:00.000"), DateTimes.ceiling(date5, Calendar.HOUR_OF_DAY));
		assertEquals("ceiling MET date across DST change-over",
			dateTimeParser.parse("March 30, 2003 03:00:00.000"),
			DateTimes.ceiling((Object)cal5, Calendar.HOUR_OF_DAY));
		assertEquals("ceiling MET date across DST change-over",
			dateTimeParser.parse("March 30, 2003 04:00:00.000"), DateTimes.ceiling(date6, Calendar.HOUR_OF_DAY));
		assertEquals("ceiling MET date across DST change-over",
			dateTimeParser.parse("March 30, 2003 04:00:00.000"),
			DateTimes.ceiling((Object)cal6, Calendar.HOUR_OF_DAY));
		assertEquals("ceiling MET date across DST change-over",
			dateTimeParser.parse("March 30, 2003 04:00:00.000"), DateTimes.ceiling(date7, Calendar.HOUR_OF_DAY));
		assertEquals("ceiling MET date across DST change-over",
			dateTimeParser.parse("March 30, 2003 04:00:00.000"),
			DateTimes.ceiling((Object)cal7, Calendar.HOUR_OF_DAY));
		TimeZone.setDefault(defaultZone);
		dateTimeParser.setTimeZone(defaultZone);

		// Bug 31395, large dates
		final Date endOfTime = new Date(Long.MAX_VALUE); // fyi: Sun Aug 17 07:12:55 CET 292278994
															// -- 807 millis
		final GregorianCalendar endCal = new GregorianCalendar();
		endCal.setTime(endOfTime);
		try {
			DateTimes.ceiling(endCal, Calendar.DATE);
			fail();
		}
		catch (final ArithmeticException ex) {
		}
		endCal.set(Calendar.YEAR, 280000001);
		try {
			DateTimes.ceiling(endCal, Calendar.DATE);
			fail();
		}
		catch (final ArithmeticException ex) {
		}
		endCal.set(Calendar.YEAR, 280000000);
		final Calendar cal = DateTimes.ceiling(endCal, Calendar.DATE);
		assertEquals(0, cal.get(Calendar.HOUR));
	}

	/**
	 * Tests the iterator exceptions
	 */
	@Test
	public void testIteratorEx() throws Exception {
		try {
			DateTimes.iterator(Calendar.getInstance(), -9999);
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.iterator((Date)null, DateTimes.RANGE_WEEK_CENTER);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.iterator((Calendar)null, DateTimes.RANGE_WEEK_CENTER);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.iterator((Object)null, DateTimes.RANGE_WEEK_CENTER);
			fail();
		}
		catch (final IllegalArgumentException ex) {
		}
		try {
			DateTimes.iterator("", DateTimes.RANGE_WEEK_CENTER);
			fail();
		}
		catch (final ClassCastException ex) {
		}
	}

	/**
	 * Tests the calendar iterator for week ranges
	 */
	@Test
	public void testWeekIterator() throws Exception {
		final Calendar now = Calendar.getInstance();
		for (int i = 0; i < 7; i++) {
			final Calendar today = DateTimes.truncate(now, Calendar.DATE);
			final Calendar sunday = DateTimes.truncate(now, Calendar.DATE);
			sunday.add(Calendar.DATE, 1 - sunday.get(Calendar.DAY_OF_WEEK));
			final Calendar monday = DateTimes.truncate(now, Calendar.DATE);
			if (monday.get(Calendar.DAY_OF_WEEK) == 1) {
				// This is sunday... roll back 6 days
				monday.add(Calendar.DATE, -6);
			}
			else {
				monday.add(Calendar.DATE, 2 - monday.get(Calendar.DAY_OF_WEEK));
			}
			final Calendar centered = DateTimes.truncate(now, Calendar.DATE);
			centered.add(Calendar.DATE, -3);

			Iterator<?> it = DateTimes.iterator(now, DateTimes.RANGE_WEEK_SUNDAY);
			assertWeekIterator(it, sunday);
			it = DateTimes.iterator(now, DateTimes.RANGE_WEEK_MONDAY);
			assertWeekIterator(it, monday);
			it = DateTimes.iterator(now, DateTimes.RANGE_WEEK_RELATIVE);
			assertWeekIterator(it, today);
			it = DateTimes.iterator(now, DateTimes.RANGE_WEEK_CENTER);
			assertWeekIterator(it, centered);

			it = DateTimes.iterator((Object)now, DateTimes.RANGE_WEEK_CENTER);
			assertWeekIterator(it, centered);
			it = DateTimes.iterator((Object)now.getTime(), DateTimes.RANGE_WEEK_CENTER);
			assertWeekIterator(it, centered);
			try {
				it.next();
				fail();
			}
			catch (final NoSuchElementException ex) {
			}
			it = DateTimes.iterator(now, DateTimes.RANGE_WEEK_CENTER);
			it.next();
			try {
				it.remove();
			}
			catch (final UnsupportedOperationException ex) {
			}

			now.add(Calendar.DATE, 1);
		}
	}

	/**
	 * Tests the calendar iterator for month-based ranges
	 */
	@Test
	public void testMonthIterator() throws Exception {
		Iterator<?> it = DateTimes.iterator(date1, DateTimes.RANGE_MONTH_SUNDAY);
		assertWeekIterator(it, dateParser.parse("January 27, 2002"), dateParser.parse("March 2, 2002"));

		it = DateTimes.iterator(date1, DateTimes.RANGE_MONTH_MONDAY);
		assertWeekIterator(it, dateParser.parse("January 28, 2002"), dateParser.parse("March 3, 2002"));

		it = DateTimes.iterator(date2, DateTimes.RANGE_MONTH_SUNDAY);
		assertWeekIterator(it, dateParser.parse("October 28, 2001"), dateParser.parse("December 1, 2001"));

		it = DateTimes.iterator(date2, DateTimes.RANGE_MONTH_MONDAY);
		assertWeekIterator(it, dateParser.parse("October 29, 2001"), dateParser.parse("December 2, 2001"));
	}

	@Test
	public void testLANG799_EN_OK() throws ParseException {
		final Locale dflt = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		try {
			DateTimes.parse("Wed, 09 Apr 2008 23:55:38 GMT", "EEE, dd MMM yyyy HH:mm:ss zzz");
		}
		finally {
			Locale.setDefault(dflt);
		}
	}

	// Parse German date with English Locale
	@Test(expected = ParseException.class)
	public void testLANG799_EN_FAIL() throws ParseException {
		final Locale dflt = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		try {
			DateTimes.parse("Mi, 09 Apr 2008 23:55:38 GMT", "EEE, dd MMM yyyy HH:mm:ss zzz");
		}
		finally {
			Locale.setDefault(dflt);
		}
	}

	@Test
	public void testLANG799_DE_OK() throws ParseException {
		final Locale dflt = Locale.getDefault();
		Locale.setDefault(Locale.GERMAN);
		try {
			DateTimes.parse("Mi, 09 Apr 2008 23:55:38 GMT", "EEE, dd MMM yyyy HH:mm:ss zzz");
		}
		finally {
			Locale.setDefault(dflt);
		}
	}

	// Parse English date with German Locale
	@Test(expected = ParseException.class)
	public void testLANG799_DE_FAIL() throws ParseException {
		final Locale dflt = Locale.getDefault();
		Locale.setDefault(Locale.GERMAN);
		try {
			DateTimes.parse("Wed, 09 Apr 2008 23:55:38 GMT", "EEE, dd MMM yyyy HH:mm:ss zzz");
		}
		finally {
			Locale.setDefault(dflt);
		}
	}

	// Parse German date with English Locale, specifying German Locale override
	@Test
	public void testLANG799_EN_WITH_DE_LOCALE() throws ParseException {
		final Locale dflt = Locale.getDefault();
		Locale.setDefault(Locale.ENGLISH);
		try {
			DateTimes.parse("Mi, 09 Apr 2008 23:55:38 GMT", Locale.GERMAN, "EEE, dd MMM yyyy HH:mm:ss zzz");
		}
		finally {
			Locale.setDefault(dflt);
		}
	}

	/**
	 * This checks that this is a 7 element iterator of Calendar objects that are dates (no time),
	 * and exactly 1 day spaced after each other.
	 */
	private static void assertWeekIterator(final Iterator<?> it, final Calendar start) {
		final Calendar end = (Calendar)start.clone();
		end.add(Calendar.DATE, 6);

		assertWeekIterator(it, start, end);
	}

	/**
	 * Convenience method for when working with Date objects
	 */
	private static void assertWeekIterator(final Iterator<?> it, final Date start, final Date end) {
		final Calendar calStart = Calendar.getInstance();
		calStart.setTime(start);
		final Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(end);

		assertWeekIterator(it, calStart, calEnd);
	}

	/**
	 * This checks that this is a 7 divisble iterator of Calendar objects that are dates (no time),
	 * and exactly 1 day spaced after each other (in addition to the proper start and stop dates)
	 */
	private static void assertWeekIterator(final Iterator<?> it, final Calendar start, final Calendar end) {
		Calendar cal = (Calendar)it.next();
		assertCalendarsEquals("", start, cal, 0);
		Calendar last = null;
		int count = 1;
		while (it.hasNext()) {
			// Check this is just a date (no time component)
			assertCalendarsEquals("", cal, DateTimes.truncate(cal, Calendar.DATE), 0);

			last = cal;
			cal = (Calendar)it.next();
			count++;

			// Check that this is one day more than the last date
			last.add(Calendar.DATE, 1);
			assertCalendarsEquals("", last, cal, 0);
		}
		if (count % 7 != 0) {
			throw new AssertionFailedError("There were " + count + " days in this iterator");
		}
		assertCalendarsEquals("", end, cal, 0);
	}

	/**
	 * Used to check that Calendar objects are close enough delta is in milliseconds
	 */
	private static void assertCalendarsEquals(final String message, final Calendar cal1, final Calendar cal2,
			final long delta) {
		if (Math.abs(cal1.getTime().getTime() - cal2.getTime().getTime()) > delta) {
			throw new AssertionFailedError(message + " expected " + cal1.getTime() + " but got " + cal2.getTime());
		}
	}

	void warn(final String msg) {
		System.err.println(msg);
	}

	// -----------------------------------------------------------------------
	@Test
	public void testFormat() {
		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.set(2005, 0, 1, 12, 0, 0);
		c.setTimeZone(TimeZone.getDefault());
		final StringBuilder buffer = new StringBuilder();
		final int year = c.get(Calendar.YEAR);
		final int month = c.get(Calendar.MONTH) + 1;
		final int day = c.get(Calendar.DAY_OF_MONTH);
		final int hour = c.get(Calendar.HOUR_OF_DAY);
		buffer.append(year);
		buffer.append(month);
		buffer.append(day);
		buffer.append(hour);
		assertEquals(buffer.toString(), DateTimes.format(c.getTime(), "yyyyMdH"));

		assertEquals(buffer.toString(), DateTimes.format(c.getTime().getTime(), "yyyyMdH"));

		assertEquals(buffer.toString(), DateTimes.format(c.getTime(), "yyyyMdH", Locale.US));

		assertEquals(buffer.toString(), DateTimes.format(c.getTime().getTime(), "yyyyMdH", Locale.US));
	}

	// -----------------------------------------------------------------------
	@Test
	public void testFormatCalendar() {
		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.set(2005, 0, 1, 12, 0, 0);
		c.setTimeZone(TimeZone.getDefault());
		final StringBuilder buffer = new StringBuilder();
		final int year = c.get(Calendar.YEAR);
		final int month = c.get(Calendar.MONTH) + 1;
		final int day = c.get(Calendar.DAY_OF_MONTH);
		final int hour = c.get(Calendar.HOUR_OF_DAY);
		buffer.append(year);
		buffer.append(month);
		buffer.append(day);
		buffer.append(hour);
		assertEquals(buffer.toString(), DateTimes.format(c, "yyyyMdH"));

		assertEquals(buffer.toString(), DateTimes.format(c.getTime(), "yyyyMdH"));

		assertEquals(buffer.toString(), DateTimes.format(c, "yyyyMdH", Locale.US));

		assertEquals(buffer.toString(), DateTimes.format(c.getTime(), "yyyyMdH", Locale.US));
	}

	@Test
	public void testFormatUTC() {
		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.set(2005, 0, 1, 12, 0, 0);
		assertEquals("2005-01-01T12:00:00",
			DateTimes.formatUTC(c.getTime(), DateTimes.ISO_DATETIME_FORMAT.getPattern()));

		assertEquals("2005-01-01T12:00:00",
			DateTimes.formatUTC(c.getTime().getTime(), DateTimes.ISO_DATETIME_FORMAT.getPattern()));

		assertEquals("2005-01-01T12:00:00",
			DateTimes.formatUTC(c.getTime(), DateTimes.ISO_DATETIME_FORMAT.getPattern(), Locale.US));

		assertEquals("2005-01-01T12:00:00",
			DateTimes.formatUTC(c.getTime().getTime(), DateTimes.ISO_DATETIME_FORMAT.getPattern(), Locale.US));
	}

	@Test
	public void testDateTimeISO() {
		final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
		final Calendar cal = Calendar.getInstance(timeZone);
		cal.set(2002, 1, 23, 9, 11, 12);
		String text = DateTimes.format(cal.getTime(), DateTimes.ISO_DATETIME_FORMAT.getPattern(), timeZone);
		assertEquals("2002-02-23T09:11:12", text);
		text = DateTimes.format(cal.getTime().getTime(), DateTimes.ISO_DATETIME_FORMAT.getPattern(), timeZone);
		assertEquals("2002-02-23T09:11:12", text);
		text = DateTimes.ISO_DATETIME_FORMAT.format(cal);
		assertEquals("2002-02-23T09:11:12", text);

		text = DateTimes.format(cal.getTime(), DateTimes.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(), timeZone);
		assertEquals("2002-02-23T09:11:12-03:00", text);
		text = DateTimes
			.format(cal.getTime().getTime(), DateTimes.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern(), timeZone);
		assertEquals("2002-02-23T09:11:12-03:00", text);
		text = DateTimes.ISO_DATETIME_TIME_ZONE_FORMAT.format(cal);
		assertEquals("2002-02-23T09:11:12-03:00", text);
	}

	@Test
	public void testDateISO() {
		final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
		final Calendar cal = Calendar.getInstance(timeZone);
		cal.set(2002, 1, 23, 10, 11, 12);
		String text = DateTimes.format(cal.getTime(), DateTimes.ISO_DATE_FORMAT.getPattern(), timeZone);
		assertEquals("2002-02-23", text);
		text = DateTimes.format(cal.getTime().getTime(), DateTimes.ISO_DATE_FORMAT.getPattern(), timeZone);
		assertEquals("2002-02-23", text);
		text = DateTimes.ISO_DATE_FORMAT.format(cal);
		assertEquals("2002-02-23", text);

		text = DateTimes.format(cal.getTime(), DateTimes.ISO_DATE_TIME_ZONE_FORMAT.getPattern(), timeZone);
		assertEquals("2002-02-23-03:00", text);
		text = DateTimes.format(cal.getTime().getTime(), DateTimes.ISO_DATE_TIME_ZONE_FORMAT.getPattern(), timeZone);
		assertEquals("2002-02-23-03:00", text);
		text = DateTimes.ISO_DATE_TIME_ZONE_FORMAT.format(cal);
		assertEquals("2002-02-23-03:00", text);
	}

	@Test
	public void testTimeISO() {
		final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
		final Calendar cal = Calendar.getInstance(timeZone);
		cal.set(2002, 1, 23, 10, 11, 12);
		String text = DateTimes.format(cal.getTime(), DateTimes.ISO_TIME_FORMAT.getPattern(), timeZone);
		assertEquals("T10:11:12", text);
		text = DateTimes.format(cal.getTime().getTime(), DateTimes.ISO_TIME_FORMAT.getPattern(), timeZone);
		assertEquals("T10:11:12", text);
		text = DateTimes.ISO_TIME_FORMAT.format(cal);
		assertEquals("T10:11:12", text);

		text = DateTimes.format(cal.getTime(), DateTimes.ISO_TIME_TIME_ZONE_FORMAT.getPattern(), timeZone);
		assertEquals("T10:11:12-03:00", text);
		text = DateTimes.format(cal.getTime().getTime(), DateTimes.ISO_TIME_TIME_ZONE_FORMAT.getPattern(), timeZone);
		assertEquals("T10:11:12-03:00", text);
		text = DateTimes.ISO_TIME_TIME_ZONE_FORMAT.format(cal);
		assertEquals("T10:11:12-03:00", text);
	}

	@Test
	public void testTimeNoTISO() {
		final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
		final Calendar cal = Calendar.getInstance(timeZone);
		cal.set(2002, 1, 23, 10, 11, 12);
		String text = DateTimes.format(cal.getTime(), DateTimes.ISO_TIME_NO_T_FORMAT.getPattern(), timeZone);
		assertEquals("10:11:12", text);
		text = DateTimes.format(cal.getTime().getTime(), DateTimes.ISO_TIME_NO_T_FORMAT.getPattern(), timeZone);
		assertEquals("10:11:12", text);
		text = DateTimes.ISO_TIME_NO_T_FORMAT.format(cal);
		assertEquals("10:11:12", text);

		text = DateTimes.format(cal.getTime(), DateTimes.ISO_TIME_NO_T_TIME_ZONE_FORMAT.getPattern(), timeZone);
		assertEquals("10:11:12-03:00", text);
		text = DateTimes.format(cal.getTime().getTime(), DateTimes.ISO_TIME_NO_T_TIME_ZONE_FORMAT.getPattern(),
			timeZone);
		assertEquals("10:11:12-03:00", text);
		text = DateTimes.ISO_TIME_NO_T_TIME_ZONE_FORMAT.format(cal);
		assertEquals("10:11:12-03:00", text);
	}

	@Test
	public void testSMTP() {
		final TimeZone timeZone = TimeZone.getTimeZone("GMT-3");
		final Calendar cal = Calendar.getInstance(timeZone);
		cal.set(2003, 5, 8, 10, 11, 12);
		String text = DateTimes.format(cal.getTime(), DateTimes.SMTP_DATETIME_FORMAT.getPattern(), timeZone,
			DateTimes.SMTP_DATETIME_FORMAT.getLocale());
		assertEquals("Sun, 08 Jun 2003 10:11:12 -0300", text);
		text = DateTimes.format(cal.getTime().getTime(), DateTimes.SMTP_DATETIME_FORMAT.getPattern(), timeZone,
			DateTimes.SMTP_DATETIME_FORMAT.getLocale());
		assertEquals("Sun, 08 Jun 2003 10:11:12 -0300", text);
		text = DateTimes.SMTP_DATETIME_FORMAT.format(cal);
		assertEquals("Sun, 08 Jun 2003 10:11:12 -0300", text);

		// format UTC
		text = DateTimes.formatUTC(cal.getTime().getTime(), DateTimes.SMTP_DATETIME_FORMAT.getPattern(),
			DateTimes.SMTP_DATETIME_FORMAT.getLocale());
		assertEquals("Sun, 08 Jun 2003 13:11:12 +0000", text);
	}

}
