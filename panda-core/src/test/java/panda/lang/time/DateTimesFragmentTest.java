package panda.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DateTimesFragmentTest {

	private static final int months = 7; // second final prime before 12
	private static final int days = 23; // second final prime before 31 (and valid)
	private static final int hours = 19; // second final prime before 24
	private static final int minutes = 53; // second final prime before 60
	private static final int seconds = 47; // third final prime before 60
	private static final int millis = 991; // second final prime before 1000

	private Date aDate;
	private Calendar aCalendar;

	@Before
	public void setUp() {
		aCalendar = Calendar.getInstance();
		aCalendar.set(2005, months, days, hours, minutes, seconds);
		aCalendar.set(Calendar.MILLISECOND, millis);
		aDate = aCalendar.getTime();
	}

	@Test
	public void testNullDate() {
		try {
			DateTimes.getFragmentInMilliseconds((Date)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInSeconds((Date)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInMinutes((Date)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInHours((Date)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInDays((Date)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}
	}

	@Test
	public void testNullCalendar() {
		try {
			DateTimes.getFragmentInMilliseconds((Calendar)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInSeconds((Calendar)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInMinutes((Calendar)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInHours((Calendar)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInDays((Calendar)null, Calendar.MILLISECOND);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}
	}

	@Test
	public void testInvalidFragmentWithDate() {
		try {
			DateTimes.getFragmentInMilliseconds(aDate, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInSeconds(aDate, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInMinutes(aDate, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInHours(aDate, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInDays(aDate, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}
	}

	@Test
	public void testInvalidFragmentWithCalendar() {
		try {
			DateTimes.getFragmentInMilliseconds(aCalendar, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInSeconds(aCalendar, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInMinutes(aCalendar, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInHours(aCalendar, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			DateTimes.getFragmentInDays(aCalendar, 0);
			fail();
		}
		catch (final IllegalArgumentException iae) {
		}
	}

	@Test
	public void testMillisecondFragmentInLargerUnitWithDate() {
		assertEquals(0, DateTimes.getFragmentInMilliseconds(aDate, Calendar.MILLISECOND));
		assertEquals(0, DateTimes.getFragmentInSeconds(aDate, Calendar.MILLISECOND));
		assertEquals(0, DateTimes.getFragmentInMinutes(aDate, Calendar.MILLISECOND));
		assertEquals(0, DateTimes.getFragmentInHours(aDate, Calendar.MILLISECOND));
		assertEquals(0, DateTimes.getFragmentInDays(aDate, Calendar.MILLISECOND));
	}

	@Test
	public void testMillisecondFragmentInLargerUnitWithCalendar() {
		assertEquals(0, DateTimes.getFragmentInMilliseconds(aCalendar, Calendar.MILLISECOND));
		assertEquals(0, DateTimes.getFragmentInSeconds(aCalendar, Calendar.MILLISECOND));
		assertEquals(0, DateTimes.getFragmentInMinutes(aCalendar, Calendar.MILLISECOND));
		assertEquals(0, DateTimes.getFragmentInHours(aCalendar, Calendar.MILLISECOND));
		assertEquals(0, DateTimes.getFragmentInDays(aCalendar, Calendar.MILLISECOND));
	}

	@Test
	public void testSecondFragmentInLargerUnitWithDate() {
		assertEquals(0, DateTimes.getFragmentInSeconds(aDate, Calendar.SECOND));
		assertEquals(0, DateTimes.getFragmentInMinutes(aDate, Calendar.SECOND));
		assertEquals(0, DateTimes.getFragmentInHours(aDate, Calendar.SECOND));
		assertEquals(0, DateTimes.getFragmentInDays(aDate, Calendar.SECOND));
	}

	@Test
	public void testSecondFragmentInLargerUnitWithCalendar() {
		assertEquals(0, DateTimes.getFragmentInSeconds(aCalendar, Calendar.SECOND));
		assertEquals(0, DateTimes.getFragmentInMinutes(aCalendar, Calendar.SECOND));
		assertEquals(0, DateTimes.getFragmentInHours(aCalendar, Calendar.SECOND));
		assertEquals(0, DateTimes.getFragmentInDays(aCalendar, Calendar.SECOND));
	}

	@Test
	public void testMinuteFragmentInLargerUnitWithDate() {
		assertEquals(0, DateTimes.getFragmentInMinutes(aDate, Calendar.MINUTE));
		assertEquals(0, DateTimes.getFragmentInHours(aDate, Calendar.MINUTE));
		assertEquals(0, DateTimes.getFragmentInDays(aDate, Calendar.MINUTE));
	}

	@Test
	public void testMinuteFragmentInLargerUnitWithCalendar() {
		assertEquals(0, DateTimes.getFragmentInMinutes(aCalendar, Calendar.MINUTE));
		assertEquals(0, DateTimes.getFragmentInHours(aCalendar, Calendar.MINUTE));
		assertEquals(0, DateTimes.getFragmentInDays(aCalendar, Calendar.MINUTE));
	}

	@Test
	public void testHourOfDayFragmentInLargerUnitWithDate() {
		assertEquals(0, DateTimes.getFragmentInHours(aDate, Calendar.HOUR_OF_DAY));
		assertEquals(0, DateTimes.getFragmentInDays(aDate, Calendar.HOUR_OF_DAY));
	}

	@Test
	public void testHourOfDayFragmentInLargerUnitWithCalendar() {
		assertEquals(0, DateTimes.getFragmentInHours(aCalendar, Calendar.HOUR_OF_DAY));
		assertEquals(0, DateTimes.getFragmentInDays(aCalendar, Calendar.HOUR_OF_DAY));
	}

	@Test
	public void testDayOfYearFragmentInLargerUnitWithDate() {
		assertEquals(0, DateTimes.getFragmentInDays(aDate, Calendar.DAY_OF_YEAR));
	}

	@Test
	public void testDayOfYearFragmentInLargerUnitWithCalendar() {
		assertEquals(0, DateTimes.getFragmentInDays(aCalendar, Calendar.DAY_OF_YEAR));
	}

	@Test
	public void testDateFragmentInLargerUnitWithDate() {
		assertEquals(0, DateTimes.getFragmentInDays(aDate, Calendar.DATE));
	}

	@Test
	public void testDateFragmentInLargerUnitWithCalendar() {
		assertEquals(0, DateTimes.getFragmentInDays(aCalendar, Calendar.DATE));
	}

	// Calendar.SECOND as useful fragment

	@Test
	public void testMillisecondsOfSecondWithDate() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aDate, Calendar.SECOND);
		assertEquals(millis, testResult);
	}

	@Test
	public void testMillisecondsOfSecondWithCalendar() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aCalendar, Calendar.SECOND);
		assertEquals(millis, testResult);
		assertEquals(aCalendar.get(Calendar.MILLISECOND), testResult);
	}

	// Calendar.MINUTE as useful fragment

	@Test
	public void testMillisecondsOfMinuteWithDate() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aDate, Calendar.MINUTE);
		assertEquals(millis + (seconds * DateTimes.MS_SECOND), testResult);
	}

	@Test
	public void testMillisecondsOfMinuteWithCalender() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aCalendar, Calendar.MINUTE);
		assertEquals(millis + (seconds * DateTimes.MS_SECOND), testResult);
	}

	@Test
	public void testSecondsofMinuteWithDate() {
		final long testResult = DateTimes.getFragmentInSeconds(aDate, Calendar.MINUTE);
		assertEquals(seconds, testResult);
	}

	@Test
	public void testSecondsofMinuteWithCalendar() {
		final long testResult = DateTimes.getFragmentInSeconds(aCalendar, Calendar.MINUTE);
		assertEquals(seconds, testResult);
		assertEquals(aCalendar.get(Calendar.SECOND), testResult);
	}

	// Calendar.HOUR_OF_DAY as useful fragment

	@Test
	public void testMillisecondsOfHourWithDate() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aDate, Calendar.HOUR_OF_DAY);
		assertEquals(millis + (seconds * DateTimes.MS_SECOND) + (minutes * DateTimes.MS_MINUTE),
			testResult);
	}

	@Test
	public void testMillisecondsOfHourWithCalendar() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aCalendar, Calendar.HOUR_OF_DAY);
		assertEquals(millis + (seconds * DateTimes.MS_SECOND) + (minutes * DateTimes.MS_MINUTE),
			testResult);
	}

	@Test
	public void testSecondsofHourWithDate() {
		final long testResult = DateTimes.getFragmentInSeconds(aDate, Calendar.HOUR_OF_DAY);
		assertEquals(seconds + (minutes * DateTimes.MS_MINUTE / DateTimes.MS_SECOND), testResult);
	}

	@Test
	public void testSecondsofHourWithCalendar() {
		final long testResult = DateTimes.getFragmentInSeconds(aCalendar, Calendar.HOUR_OF_DAY);
		assertEquals(seconds + (minutes * DateTimes.MS_MINUTE / DateTimes.MS_SECOND), testResult);
	}

	@Test
	public void testMinutesOfHourWithDate() {
		final long testResult = DateTimes.getFragmentInMinutes(aDate, Calendar.HOUR_OF_DAY);
		assertEquals(minutes, testResult);
	}

	@Test
	public void testMinutesOfHourWithCalendar() {
		final long testResult = DateTimes.getFragmentInMinutes(aCalendar, Calendar.HOUR_OF_DAY);
		assertEquals(minutes, testResult);
	}

	// Calendar.DATE and Calendar.DAY_OF_YEAR as useful fragment
	@Test
	public void testMillisecondsOfDayWithDate() {
		long testresult = DateTimes.getFragmentInMilliseconds(aDate, Calendar.DATE);
		final long expectedValue = millis + (seconds * DateTimes.MS_SECOND)
				+ (minutes * DateTimes.MS_MINUTE) + (hours * DateTimes.MS_HOUR);
		assertEquals(expectedValue, testresult);
		testresult = DateTimes.getFragmentInMilliseconds(aDate, Calendar.DAY_OF_YEAR);
		assertEquals(expectedValue, testresult);
	}

	@Test
	public void testMillisecondsOfDayWithCalendar() {
		long testresult = DateTimes.getFragmentInMilliseconds(aCalendar, Calendar.DATE);
		final long expectedValue = millis + (seconds * DateTimes.MS_SECOND)
				+ (minutes * DateTimes.MS_MINUTE) + (hours * DateTimes.MS_HOUR);
		assertEquals(expectedValue, testresult);
		testresult = DateTimes.getFragmentInMilliseconds(aCalendar, Calendar.DAY_OF_YEAR);
		assertEquals(expectedValue, testresult);
	}

	@Test
	public void testSecondsOfDayWithDate() {
		long testresult = DateTimes.getFragmentInSeconds(aDate, Calendar.DATE);
		final long expectedValue = seconds
				+ ((minutes * DateTimes.MS_MINUTE) + (hours * DateTimes.MS_HOUR))
				/ DateTimes.MS_SECOND;
		assertEquals(expectedValue, testresult);
		testresult = DateTimes.getFragmentInSeconds(aDate, Calendar.DAY_OF_YEAR);
		assertEquals(expectedValue, testresult);
	}

	@Test
	public void testSecondsOfDayWithCalendar() {
		long testresult = DateTimes.getFragmentInSeconds(aCalendar, Calendar.DATE);
		final long expectedValue = seconds
				+ ((minutes * DateTimes.MS_MINUTE) + (hours * DateTimes.MS_HOUR))
				/ DateTimes.MS_SECOND;
		assertEquals(expectedValue, testresult);
		testresult = DateTimes.getFragmentInSeconds(aCalendar, Calendar.DAY_OF_YEAR);
		assertEquals(expectedValue, testresult);
	}

	@Test
	public void testMinutesOfDayWithDate() {
		long testResult = DateTimes.getFragmentInMinutes(aDate, Calendar.DATE);
		final long expectedValue = minutes + ((hours * DateTimes.MS_HOUR)) / DateTimes.MS_MINUTE;
		assertEquals(expectedValue, testResult);
		testResult = DateTimes.getFragmentInMinutes(aDate, Calendar.DAY_OF_YEAR);
		assertEquals(expectedValue, testResult);
	}

	@Test
	public void testMinutesOfDayWithCalendar() {
		long testResult = DateTimes.getFragmentInMinutes(aCalendar, Calendar.DATE);
		final long expectedValue = minutes + ((hours * DateTimes.MS_HOUR)) / DateTimes.MS_MINUTE;
		assertEquals(expectedValue, testResult);
		testResult = DateTimes.getFragmentInMinutes(aCalendar, Calendar.DAY_OF_YEAR);
		assertEquals(expectedValue, testResult);
	}

	@Test
	public void testHoursOfDayWithDate() {
		long testResult = DateTimes.getFragmentInHours(aDate, Calendar.DATE);
		final long expectedValue = hours;
		assertEquals(expectedValue, testResult);
		testResult = DateTimes.getFragmentInHours(aDate, Calendar.DAY_OF_YEAR);
		assertEquals(expectedValue, testResult);
	}

	@Test
	public void testHoursOfDayWithCalendar() {
		long testResult = DateTimes.getFragmentInHours(aCalendar, Calendar.DATE);
		final long expectedValue = hours;
		assertEquals(expectedValue, testResult);
		testResult = DateTimes.getFragmentInHours(aCalendar, Calendar.DAY_OF_YEAR);
		assertEquals(expectedValue, testResult);
	}

	// Calendar.MONTH as useful fragment
	@Test
	public void testMillisecondsOfMonthWithDate() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aDate, Calendar.MONTH);
		assertEquals(millis + (seconds * DateTimes.MS_SECOND) + (minutes * DateTimes.MS_MINUTE)
				+ (hours * DateTimes.MS_HOUR) + (days * DateTimes.MS_DAY), testResult);
	}

	@Test
	public void testMillisecondsOfMonthWithCalendar() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aCalendar, Calendar.MONTH);
		assertEquals(millis + (seconds * DateTimes.MS_SECOND) + (minutes * DateTimes.MS_MINUTE)
				+ (hours * DateTimes.MS_HOUR) + (days * DateTimes.MS_DAY), testResult);
	}

	@Test
	public void testSecondsOfMonthWithDate() {
		final long testResult = DateTimes.getFragmentInSeconds(aDate, Calendar.MONTH);
		assertEquals(
			seconds
					+ ((minutes * DateTimes.MS_MINUTE) + (hours * DateTimes.MS_HOUR) + (days * DateTimes.MS_DAY))
					/ DateTimes.MS_SECOND, testResult);
	}

	@Test
	public void testSecondsOfMonthWithCalendar() {
		final long testResult = DateTimes.getFragmentInSeconds(aCalendar, Calendar.MONTH);
		assertEquals(
			seconds
					+ ((minutes * DateTimes.MS_MINUTE) + (hours * DateTimes.MS_HOUR) + (days * DateTimes.MS_DAY))
					/ DateTimes.MS_SECOND, testResult);
	}

	@Test
	public void testMinutesOfMonthWithDate() {
		final long testResult = DateTimes.getFragmentInMinutes(aDate, Calendar.MONTH);
		assertEquals(minutes + ((hours * DateTimes.MS_HOUR) + (days * DateTimes.MS_DAY))
				/ DateTimes.MS_MINUTE, testResult);
	}

	@Test
	public void testMinutesOfMonthWithCalendar() {
		final long testResult = DateTimes.getFragmentInMinutes(aCalendar, Calendar.MONTH);
		assertEquals(minutes + ((hours * DateTimes.MS_HOUR) + (days * DateTimes.MS_DAY))
				/ DateTimes.MS_MINUTE, testResult);
	}

	@Test
	public void testHoursOfMonthWithDate() {
		final long testResult = DateTimes.getFragmentInHours(aDate, Calendar.MONTH);
		assertEquals(hours + ((days * DateTimes.MS_DAY)) / DateTimes.MS_HOUR, testResult);
	}

	@Test
	public void testHoursOfMonthWithCalendar() {
		final long testResult = DateTimes.getFragmentInHours(aCalendar, Calendar.MONTH);
		assertEquals(hours + ((days * DateTimes.MS_DAY)) / DateTimes.MS_HOUR, testResult);
	}

	// Calendar.YEAR as useful fragment
	@Test
	public void testMillisecondsOfYearWithDate() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aDate, Calendar.YEAR);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(aDate);
		assertEquals(millis + (seconds * DateTimes.MS_SECOND) + (minutes * DateTimes.MS_MINUTE)
				+ (hours * DateTimes.MS_HOUR) + (cal.get(Calendar.DAY_OF_YEAR) * DateTimes.MS_DAY),
			testResult);
	}

	@Test
	public void testMillisecondsOfYearWithCalendar() {
		final long testResult = DateTimes.getFragmentInMilliseconds(aCalendar, Calendar.YEAR);
		assertEquals(millis + (seconds * DateTimes.MS_SECOND) + (minutes * DateTimes.MS_MINUTE)
				+ (hours * DateTimes.MS_HOUR)
				+ (aCalendar.get(Calendar.DAY_OF_YEAR) * DateTimes.MS_DAY), testResult);
	}

	@Test
	public void testSecondsOfYearWithDate() {
		final long testResult = DateTimes.getFragmentInSeconds(aDate, Calendar.YEAR);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(aDate);
		assertEquals(
			seconds
					+ ((minutes * DateTimes.MS_MINUTE) + (hours * DateTimes.MS_HOUR) + (cal.get(Calendar.DAY_OF_YEAR) * DateTimes.MS_DAY))
					/ DateTimes.MS_SECOND, testResult);
	}

	@Test
	public void testSecondsOfYearWithCalendar() {
		final long testResult = DateTimes.getFragmentInSeconds(aCalendar, Calendar.YEAR);
		assertEquals(
			seconds
					+ ((minutes * DateTimes.MS_MINUTE) + (hours * DateTimes.MS_HOUR) + (aCalendar.get(Calendar.DAY_OF_YEAR) * DateTimes.MS_DAY))
					/ DateTimes.MS_SECOND, testResult);
	}

	@Test
	public void testMinutesOfYearWithDate() {
		final long testResult = DateTimes.getFragmentInMinutes(aDate, Calendar.YEAR);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(aDate);
		assertEquals(minutes
				+ ((hours * DateTimes.MS_HOUR) + (cal.get(Calendar.DAY_OF_YEAR) * DateTimes.MS_DAY))
				/ DateTimes.MS_MINUTE, testResult);
	}

	@Test
	public void testMinutesOfYearWithCalendar() {
		final long testResult = DateTimes.getFragmentInMinutes(aCalendar, Calendar.YEAR);
		assertEquals(
			minutes
					+ ((hours * DateTimes.MS_HOUR) + (aCalendar.get(Calendar.DAY_OF_YEAR) * DateTimes.MS_DAY))
					/ DateTimes.MS_MINUTE, testResult);
	}

	@Test
	public void testHoursOfYearWithDate() {
		final long testResult = DateTimes.getFragmentInHours(aDate, Calendar.YEAR);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(aDate);
		assertEquals(hours + ((cal.get(Calendar.DAY_OF_YEAR) * DateTimes.MS_DAY)) / DateTimes.MS_HOUR,
			testResult);
	}

	@Test
	public void testHoursOfYearWithCalendar() {
		final long testResult = DateTimes.getFragmentInHours(aCalendar, Calendar.YEAR);
		assertEquals(hours + ((aCalendar.get(Calendar.DAY_OF_YEAR) * DateTimes.MS_DAY))
				/ DateTimes.MS_HOUR, testResult);
	}
}
