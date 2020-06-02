package panda.net.ntp;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

/**
 * Test class that validates assertions for the basic TimeStamp operations and comparisons.
 */
public class TimeStampTest extends TestCase {

	private static final String TIME1 = "c1a9ae1c.cf6ac48d"; // Tue, Dec 17 2002 14:07:24.810 UTC
	private static final String TIME2 = "c1a9ae1c.cf6ac48f"; // Tue, Dec 17 2002 14:07:24.810 UTC
	private static final String TIME3 = "c1a9ae1d.cf6ac48e"; // Tue, Dec 17 2002 14:07:25.810 UTC

	public void testCompare() {

		TimeStamp ts1 = new TimeStamp(TIME1); // Tue, Dec 17 2002 14:07:24.810 UTC
		TimeStamp ts2 = new TimeStamp(TIME1);
		TimeStamp ts3 = new TimeStamp(TIME2); // Tue, Dec 17 2002 14:07:24.810 UTC
		TimeStamp ts4 = new TimeStamp(TIME3); // Tue, Dec 17 2002 14:07:25.810 UTC

		// do assertion tests on TimeStamp class
		assertEquals("equals(1,2)", ts1, ts2);
		assertEquals("compareTo(1,2)", 0, ts1.compareTo(ts2));
		assertEquals("ntpValue(1,2)", ts1.ntpValue(), ts2.ntpValue());
		assertEquals("hashCode(1,2)", ts1.hashCode(), ts2.hashCode());
		assertEquals("ts1==ts1", ts1, ts1);

		// timestamps in ts1 (TIME1) and ts3 (TIME2) are only off by the smallest
		// fraction of a second (~200 picoseconds) so the times are not equal but
		// when converted to Java dates (in milliseconds) they will be equal.
		assertTrue("ts1 != ts3", !ts1.equals(ts3));
		assertEquals("compareTo(1,3)", -1, ts1.compareTo(ts3));
		assertEquals("seconds", ts1.getSeconds(), ts3.getSeconds());
		assertTrue("fraction", ts1.getFraction() != ts3.getFraction());
		assertTrue("ntpValue(1,3)", ts1.ntpValue() != ts3.ntpValue());
		assertTrue("hashCode(1,3)", ts1.hashCode() != ts3.hashCode());
		long time1 = ts1.getTime();
		long time3 = ts3.getTime();
		assertEquals("equals(time1,3)", time1, time3); // ntpTime1 != ntpTime3 but JavaTime(t1) ==
														// JavaTime(t3)...

		assertTrue("ts3 != ts4", !ts3.equals(ts4));
		assertTrue("time3 != ts4.time", time3 != ts4.getTime());
	}

	public void testUTCString() {
		TimeStamp ts1 = new TimeStamp(TIME1); // Tue, Dec 17 2002 14:07:24.810 UTC
		String actual = ts1.toUTCString();
		assertEquals("Tue, Dec 17 2002 14:07:24.810 UTC", actual);
	}

	public void testDateConversion() {
		// convert current date to NtpTimeStamp then compare Java date
		// computed from NTP timestamp with original Java date.
		Calendar refCal = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
		Date refDate = refCal.getTime();
		TimeStamp ts = new TimeStamp(refDate);
		assertEquals("refDate.getTime()", refDate.getTime(), ts.getTime());
		Date tsDate = ts.getDate();
		assertEquals(refDate, tsDate);
	}

	public void testNotSame() {
		TimeStamp time = TimeStamp.getCurrentTime();
		Object other = Integer.valueOf(0);
		if (time.equals(other)) {
			fail("TimeStamp cannot equal Date");
		}
		other = null;
		if (time.equals(other)) {
			fail("TimeStamp cannot equal null");
		}
	}

}
