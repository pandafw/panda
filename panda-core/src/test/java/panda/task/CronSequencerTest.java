package panda.task;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;

public class CronSequencerTest {

	@Test
	public void testEvery10Sec1() throws Exception {
		FastDateFormat fdf = DateTimes.isoDatetimeFormat();
		
		Date d = fdf.parse("2000-01-01T01:01:01");
		CronSequencer cs = new CronSequencer("0 */10 * * * *");
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T01:10:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T01:20:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T01:30:00", fdf.format(d));
	}

	@Test
	public void testEvery10Sec2() throws Exception {
		FastDateFormat fdf = DateTimes.isoDatetimeFormat();
		
		Date d = fdf.parse("2000-01-01T01:01:01");
		CronSequencer cs = new CronSequencer("0 2/10 * * * *");
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T01:02:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T01:12:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T01:22:00", fdf.format(d));
	}

	@Test
	public void testHour() throws Exception {
		FastDateFormat fdf = DateTimes.isoDatetimeFormat();
		
		Date d = fdf.parse("2000-01-01T01:01:01");
		CronSequencer cs = new CronSequencer("0 0 * * * *");
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T02:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T03:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T04:00:00", fdf.format(d));
	}

	@Test
	public void testHourRange() throws Exception {
		FastDateFormat fdf = DateTimes.isoDatetimeFormat();
		
		Date d = fdf.parse("2000-01-01T01:01:01");
		CronSequencer cs = new CronSequencer("0 0 8-10 * * *");
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T08:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T09:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T10:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-02T08:00:00", fdf.format(d));
	}

	@Test
	public void testHourMinRange() throws Exception {
		FastDateFormat fdf = DateTimes.isoDatetimeFormat();
		
		Date d = fdf.parse("2000-01-01T01:01:01");
		CronSequencer cs = new CronSequencer("0 0/30 8-10 * * *");
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T08:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T08:30:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T09:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T09:30:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T10:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-01T10:30:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-02T08:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-02T08:30:00", fdf.format(d));
	}

	@Test
	public void testMonthRange() throws Exception {
		FastDateFormat fdf = DateTimes.isoDatetimeFormat();
		
		Date d = fdf.parse("2000-01-01T01:01:01");
		CronSequencer cs = new CronSequencer("0 0 9-10 * * MON-TUE");
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-03T09:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-03T10:00:00", fdf.format(d));

		d = cs.next(d);
		Assert.assertEquals("2000-01-04T09:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-04T10:00:00", fdf.format(d));

		d = cs.next(d);
		Assert.assertEquals("2000-01-10T09:00:00", fdf.format(d));
		
		d = cs.next(d);
		Assert.assertEquals("2000-01-10T10:00:00", fdf.format(d));
	}

}
