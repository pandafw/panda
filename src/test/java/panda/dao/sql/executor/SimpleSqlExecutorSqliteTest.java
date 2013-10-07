package panda.dao.sql.executor;

import java.sql.Connection;
import java.util.Date;

import org.junit.Test;

/**
 */
public class SimpleSqlExecutorSqliteTest extends SimpleSqlExecutorTestCase {

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getSqliteConnection();
	}

	@Override
	protected Object getExpectedBit(boolean b) {
		return b ? 1L : 0L;
	}

	@Override
	protected Object getExpectedTime(String time) {
		Date d = convertToGMTTime(time);
		return d.getTime();
	}

	@Override
	protected Object getExpectedDate(String date) {
		Date d = convertToGMTDate(date);
		return d.getTime();
	}

	@Override
	protected Object getExpectedTimestamp(String time) {
		Date d = convertToGMTDate(time);
		return d.getTime();
	}
	
	@Override
	protected Date convertToTime(String time) {
		return convertToGMTTime(time);
	}
	
	@Override
	protected Date convertToDate(String date) {
		return convertToGMTDate(date);
	}

	@Test
	public void testInsertIdAuto() throws Exception {
		super.testInsertIdAuto();
	}
}
