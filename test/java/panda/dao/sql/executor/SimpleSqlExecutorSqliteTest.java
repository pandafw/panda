package panda.dao.sql.executor;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import panda.dao.sql.TestSupport;
import panda.lang.TimeZones;

/**
 */
public class SimpleSqlExecutorSqliteTest extends SimpleSqlExecutorTestCase {

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getSqliteConnection();
	}

	@Override
	protected Object getExpectedTimestamp(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZones.GMT);
			Long t = sdf.parse(time).getTime();
			return t;
		}
		catch (Exception e) {
			log.error("exception", e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected Calendar convertToCalendar(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setTimeZone(TimeZones.GMT);
			Date d = sdf.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			return c;
		}
		catch (Exception e) {
			log.error("exception", e);
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testInsertIdAuto() throws Exception {
		super.testInsertIdAuto();
	}
}
