package panda.lang.time;

import panda.lang.time.TimeSpan;
import junit.framework.TestCase;

/**
 */
public class TimeSpanTest extends TestCase {

	/**
	 * TimeSpan(1)  "00.001"
	 *  Days               0       TotalDays          1.1574074074074074E-8
		Hours              0       TotalHours         2.7777777777777776E-7
		Minutes            0       TotalMinutes       1.6666666666666667E-5
		Seconds            0       TotalSeconds       0.0010
		Milliseconds       0       TotalMilliseconds  1
	 */
	public void test01() {
		TimeSpan ts = new TimeSpan(1);
		
		assertEquals("00:00:00.001", ts.toString());
		
		assertEquals(0, ts.getDays());
		assertEquals(0, ts.getHours());
		assertEquals(0, ts.getMinutes());
		assertEquals(0, ts.getSeconds());
		assertEquals(1, ts.getMilliseconds());

		assertEquals("1.1574074074074074E-8", String.valueOf(ts.totalDays()));
		assertEquals("2.7777777777777776E-7", String.valueOf(ts.totalHours()));
		assertEquals("1.6666666666666667E-5", String.valueOf(ts.totalMinutes()));
		assertEquals("0.0010", String.valueOf(ts.totalSeconds()));
		assertEquals(1L, ts.totalMilliseconds());
	}

	/**
		TimeSpan( 11122233344L )   "128.17:30:33.344"
		Days             128       TotalDays              128.7295525925926
		Hours             17       TotalHours             3089.5092622222223
		Minutes           30       TotalMinutes           185370.55573333334
		Seconds           33       TotalSeconds           1.1122233344E7
		Milliseconds     344       TotalMilliseconds      11122233344
	*/
	public void test02() {
		TimeSpan ts = new TimeSpan( 11122233344L );
		
		assertEquals("3089:30:33.344", ts.toString());
		
		assertEquals(128, ts.getDays());
		assertEquals(17, ts.getHours());
		assertEquals(30, ts.getMinutes());
		assertEquals(33, ts.getSeconds());
		assertEquals(344, ts.getMilliseconds());

		assertEquals("128.7295525925926", String.valueOf(ts.totalDays()));
		assertEquals("3089.5092622222223", String.valueOf(ts.totalHours()));
		assertEquals("185370.55573333334", String.valueOf(ts.totalMinutes()));
		assertEquals("1.1122233344E7", String.valueOf(ts.totalSeconds()));
		assertEquals(11122233344L, ts.totalMilliseconds());
	}

	/**
		TimeSpan( 10, 20, 30, 40, 50 )  10.20:30:40.050
		Days              10       TotalDays              10.854630208333333
		Hours             20       TotalHours                   260.511125
		Minutes           30       TotalMinutes                 15630.6675
		Seconds           40       TotalSeconds                  937840.05
		Milliseconds      50       TotalMilliseconds             937840050
	*/
	public void test03() {
		TimeSpan ts = new TimeSpan(10, 20, 30, 40, 50);
		
		assertEquals("260:30:40.050", ts.toString());
		
		assertEquals(10, ts.getDays());
		assertEquals(20, ts.getHours());
		assertEquals(30, ts.getMinutes());
		assertEquals(40, ts.getSeconds());
		assertEquals(50, ts.getMilliseconds());

		assertEquals("10.854630208333333", String.valueOf(ts.totalDays()));
		assertEquals("260.511125", String.valueOf(ts.totalHours()));
		assertEquals("15630.6675", String.valueOf(ts.totalMinutes()));
		assertEquals("937840.05", String.valueOf(ts.totalSeconds()));
		
		assertEquals(937840050, ts.totalMilliseconds());
	}
	
	/**
	TimeSpan( 1111, 2222, 3333, 4444, 5555 )  1205.22:47:09.555
	Days            1205       TotalDays              1205.9494161458333
	Hours             22       TotalHours                28942.7859875
	Minutes           47       TotalMinutes              1736567.15925
	Seconds            9       TotalSeconds              1.04194029555E8
	Milliseconds     555       TotalMilliseconds          104194029555
	*/
	public void test04() {
		TimeSpan ts = new TimeSpan(1111, 2222, 3333, 4444, 5555);
		
		assertEquals("28942:47:09.555", ts.toString());
		
		assertEquals(1205, ts.getDays());
		assertEquals(22, ts.getHours());
		assertEquals(47, ts.getMinutes());
		assertEquals(9, ts.getSeconds());
		assertEquals(555, ts.getMilliseconds());

		assertEquals("1205.9494161458333", String.valueOf(ts.totalDays()));
		assertEquals("28942.7859875", String.valueOf(ts.totalHours()));
		assertEquals("1736567.15925", String.valueOf(ts.totalMinutes()));
		assertEquals("1.04194029555E8", String.valueOf(ts.totalSeconds()));
		
		assertEquals(104194029555L, ts.totalMilliseconds());
	}
	
	/**
	FromDays( 20.84745602 )    20.20:20:20.200
	Days              20       TotalDays              20.847456018518518
	Hours             20       TotalHours             500.33894444444445
	Minutes           20       TotalMinutes           30020.336666666666
	Seconds           20       TotalSeconds                  1801220.2
	Milliseconds     200       TotalMilliseconds            1801220200
	 */
	public void test05() {
		TimeSpan ts = TimeSpan.fromDays(20.84745602);
		
		assertEquals("500:20:20.200", ts.toString());
		
		assertEquals(20, ts.getDays());
		assertEquals(20, ts.getHours());
		assertEquals(20, ts.getMinutes());
		assertEquals(20, ts.getSeconds());
		assertEquals(200, ts.getMilliseconds());

		assertEquals("20.847456018518518", String.valueOf(ts.totalDays()));
		assertEquals("500.33894444444445", String.valueOf(ts.totalHours()));
		assertEquals("30020.336666666666", String.valueOf(ts.totalMinutes()));
		assertEquals("1801220.2", String.valueOf(ts.totalSeconds()));
		
		assertEquals(1801220200L, ts.totalMilliseconds());
	}
}
