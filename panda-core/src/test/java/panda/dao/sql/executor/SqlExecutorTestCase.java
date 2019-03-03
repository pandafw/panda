package panda.dao.sql.executor;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;

import panda.dao.sql.SqlExecutor;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.TimeZones;
import panda.lang.time.FastDateFormat;
import panda.log.Log;
import panda.log.Logs;
import panda.mock.sql.MockConnection;

/**
 * SqlExecutorTestCase
 */
public abstract class SqlExecutorTestCase {
	/**
	 * log
	 */
	protected static Log log = Logs.getLog(SqlExecutorTestCase.class);

	protected SqlExecutor executor;
	
	protected abstract Connection getConnection() throws Exception;

	protected abstract SqlExecutor createExecutor(Connection c) throws Exception;
	
	@Before
	public void setUp() throws Exception {
		Connection c = getConnection();
		if (c instanceof MockConnection) {
			log.debug("SKIP: " + this.getClass().getName());
			Assume.assumeTrue(false);
		}
		executor = createExecutor(c);
	}

	@After
	public void tearDown() throws Exception {
		if (executor != null) {
			((JdbcSqlExecutor)executor).getConnection().close();
		}
	}

	protected Object getExpectedBit(boolean b) {
		return b;
	}

	protected Object getExpectedBool(boolean b) {
		return b;
	}

	protected Object getExpectedInteger(int num) {
		return new Long(num);
	}

	protected Object getExpectedReal(String num) {
		return Double.parseDouble(num);
	}

	protected Object getExpectedFloat(String num) {
		return Double.parseDouble(num);
	}

	protected Object getExpectedDouble(String num) {
		return Double.parseDouble(num);
	}

	protected Object getExpectedDecimal(String num) {
		return new BigDecimal(num);
	}
	
	protected Object getExpectedDate(String date) {
		Date d = convertToDate(date);
		return new java.sql.Date(d.getTime());
	}
	
	protected Object getExpectedTime(String time) {
		Date d = convertToTime(time);
		return new java.sql.Time(d.getTime());
	}
	
	protected Object getExpectedTimestamp(String time) {
		Date d = convertToDate(time);
		return new Timestamp(d.getTime());
	}
	
	protected Date convertToTime(String time) {
		try {
			FastDateFormat sdf = FastDateFormat.getInstance("HH:mm:ss");
			Date d = sdf.parse(time);
			return d;
		}
		catch (Exception e) {
			log.error("exception", e);
			throw new RuntimeException(e);
		}
	}
	
	protected Date convertToGMTTime(String time) {
		try {
			FastDateFormat sdf = FastDateFormat.getInstance("HH:mm:ss", TimeZones.GMT);
			Date d = sdf.parse(time);
			return d;
		}
		catch (Exception e) {
			log.error("exception", e);
			throw new RuntimeException(e);
		}
	}
	
	protected Date convertToDate(String date) {
		try {
			FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd");
			Date d = sdf.parse(date);
			return d;
		}
		catch (Exception e) {
			log.error("exception", e);
			throw new RuntimeException(e);
		}
	}

	protected Date convertToGMTDate(String date) {
		try {
			FastDateFormat sdf = FastDateFormat.getInstance("yyyy-MM-dd", TimeZones.GMT);
			Date d = sdf.parse(date);
			return d;
		}
		catch (Exception e) {
			log.error("exception", e);
			throw new RuntimeException(e);
		}
	}

	protected Calendar convertToCalendar(String date) {
		Date d = convertToDate(date);
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c;
	}

	private static NumberFormat nf = new DecimalFormat("#.##");
	
	protected Double trimDouble(Double d) {
		if (d != null) {
			d = Double.parseDouble(nf.format(d));
		}
		return d;
	}
	
	protected TestBean createBean(int id) {
		TestBean a = new TestBean();

		a.setId(1000 + id);
		a.setFbit(true);
		a.setFbool(true);
		a.setFchar(String.valueOf(id).charAt(0));
		a.setFstr("NAME 100" + id);
		a.setFtinyint((byte)(10 + id));
		a.setFsmallint((short)(100 + id));
		a.setFinteger(1000 + id);
		a.setFbigint(10000 + id);
		a.setFreal(Double.parseDouble(id + ".0" + id));
		a.setFfloat(Float.parseFloat("1" + id + ".0" + id));
		a.setFdouble(Double.parseDouble("10" + id + ".0" + id));
		a.setFdecimal(new BigDecimal("100" + id + ".0" + id));
		a.setFnumeric(new BigDecimal("1000" + id + ".0" + id));
		a.setFdate(convertToDate("2009-0" + id + "-0" + id));
		a.setFtime(convertToTime("00:0" + id + ":0" + id));
		a.setFtimestamp(convertToCalendar("2009-0" + id + "-0" + id));
	
		return a;
	}

	protected Map<String, Object> createMap(int id) {
		Map<String, Object> a = new LinkedHashMap<String, Object>();

		a.put("id", getExpectedInteger(1000 + id));
		a.put("fbit", getExpectedBit(true));
		a.put("fbool", getExpectedBool(true));
		a.put("fchar", String.valueOf(id));
		a.put("fstr", "NAME 100" + id);
		a.put("ftinyint", getExpectedInteger(10 + id));
		a.put("fsmallint", getExpectedInteger(100 + id));
		a.put("finteger", getExpectedInteger(1000 + id));
		a.put("fbigint", getExpectedInteger(10000 + id));
		a.put("freal", getExpectedReal(id + ".0" + id));
		a.put("ffloat", getExpectedFloat("1" + id + ".0" + id));
		a.put("fdouble", getExpectedDouble("10" + id + ".0" + id));
		a.put("fdecimal", getExpectedDecimal("100" + id + ".0" + id));
		a.put("fnumeric", getExpectedDecimal("1000" + id + ".0" + id));
		a.put("fdate", getExpectedDate("2009-0" + id + "-0" + id));
		a.put("ftime", getExpectedTime("00:0" + id + ":0" + id));
		a.put("ftimestamp", getExpectedTimestamp("2009-0" + id + "-0" + id));
		a.put("fclob", null);
		a.put("flongvarchar", null);
		a.put("fblob", null);
		a.put("fbinary", null);
		a.put("fvarbinary", null);
		a.put("flongvarbinary", null);
		
		return a;
	}

	protected List<Object> createList(int id, boolean update) {
		List<Object> a = new ArrayList<Object>();

		if (!update) {
			a.add(getExpectedInteger(1000 + id));
		}
		a.add(getExpectedBit(true));
		a.add(getExpectedBool(true));
		a.add(String.valueOf(id));
		a.add("NAME 100" + id);
		a.add(getExpectedInteger(10 + id));
		a.add(getExpectedInteger(100 + id));
		a.add(getExpectedInteger(1000 + id));
		a.add(getExpectedInteger(10000 + id));
		a.add(getExpectedReal(id + ".0" + id));
		a.add(getExpectedFloat("1" + id + ".0" + id));
		a.add(getExpectedDouble("10" + id + ".0" + id));
		a.add(getExpectedDecimal("100" + id + ".0" + id));
		a.add(getExpectedDecimal("1000" + id + ".0" + id));
		a.add(getExpectedDate("2009-0" + id + "-0" + id));
		a.add(getExpectedTime("00:0" + id + ":0" + id));
		a.add(getExpectedTimestamp("2009-0" + id + "-0" + id));
		if (update) {
			a.add(getExpectedInteger(1000 + id));
		}
		
		return a;
	}

	protected TestBean createBlobBean(int id) {
		TestBean a = new TestBean();
		a.setId(1000 + id);
		a.setFblob(("blob" + id).getBytes());
		return a;
	}

	protected Map<String, Object> createBlobMap(int id) {
		Map<String, Object> a = new LinkedHashMap<String, Object>();
		a.put("id", getExpectedInteger(1000 + id));
		a.put("fblob", ("blob" + id).getBytes());
		return a;
	}

	protected void logTestMethod() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		StackTraceElement ste = stack[1];
		String mn = this.getClass().getSimpleName() + "." + ste.getMethodName() + "()";
		
		log.debug("");
		log.debug(mn);
	}

	protected void logTestMethodName() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		StackTraceElement ste = stack[2];
		String mn = this.getClass().getSimpleName() + "." + ste.getMethodName() + "()";
		
		log.debug("");
		log.debug(mn);
	}

	protected void compareResult(Map expect, Map actual) {
		if (expect == null) {
			Assert.assertNull(actual);
			return;
		}

		Assert.assertNotNull(actual);
		prepareExpectMap(expect);
		prepareActualMap(actual);
		
		Set eks = expect.keySet();
		Set aks = actual.keySet();
		
		log.debug("expect map keys: " + eks);
		log.debug("actual map keys: " + aks);
		Assert.assertEquals(eks, aks);

		StringBuilder sb;
		
		sb = new StringBuilder();
		sb.append("expect map value Class: [");
		for (Iterator it = eks.iterator(); it.hasNext(); ) {
			Object k = it.next();
			Object v = expect.get(k);
			sb.append(v == null ? "<NULL>" : v.getClass().getName());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		log.debug(sb.toString());

		sb = new StringBuilder();
		sb.append("actual map value Class: [");
		for (Iterator it = eks.iterator(); it.hasNext(); ) {
			Object k = it.next();
			Object v = actual.get(k);
			sb.append(v == null ? "<NULL>" : v.getClass().getName());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		log.debug(sb.toString());

		sb = new StringBuilder();
		sb.append("expect map values: [");
		for (Iterator it = eks.iterator(); it.hasNext(); ) {
			Object k = it.next();
			Object v = expect.get(k);
			sb.append(v);
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		log.debug(sb.toString());

		sb = new StringBuilder();
		sb.append("actual map values: [");
		for (Iterator it = eks.iterator(); it.hasNext(); ) {
			Object k = it.next();
			Object v = actual.get(k);
			sb.append(v);
			if (it.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		log.debug(sb.toString());

		Assert.assertEquals(expect, actual);
	}

	protected void testQueryForInt(String sql, Object param, int expectedResult) {
		logTestMethodName();

		int actualResult = 0;
		try {
			actualResult = executor.fetch(sql, param, int.class);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	protected void testQueryForObject(String sql, Object param, Object expectedResult) {
		logTestMethodName();

		Object actualResult = null;
		try {
			actualResult = executor.fetch(sql, param, expectedResult.getClass());
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	protected void testQueryForObject(String sql, Object param, Map expectedResult) {
		logTestMethodName();

		Map actualResult = null;
		try {
			actualResult = executor.fetch(sql, param, Map.class);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}
		
		compareResult(expectedResult, actualResult);
	}

	protected void compareResult(TestBean expect, TestBean actual) {
		if (expect == null) {
			Assert.assertNull(actual);
			return;
		}

		Assert.assertNotNull(actual);
		prepareExpectBean(expect);
		prepareActualBean(actual);
		
		log.debug("expect: " + expect);
		log.debug("actual: " + actual);
		Assert.assertEquals(expect, actual);
	}
	
	protected void testQueryForObject(String sql, Object param, TestBean expectedResult) {
		logTestMethodName();

		TestBean actualResult = null;
		try {
			actualResult = executor.fetch(sql, param, TestBean.class);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}
		
		compareResult(expectedResult, actualResult);
	}

	protected void prepareExpectBean(TestBean actual) {
	}
	
	protected void prepareActualBean(TestBean actual) {
	}
	
	@SuppressWarnings("unchecked")
	protected void prepareExpectMap(Map actual) {
		for (Iterator<Entry> it = actual.entrySet().iterator(); it.hasNext();) {
			Entry e = it.next();
			Object val = e.getValue();
			if (val instanceof byte[]) {
				actual.put(e.getKey(), Strings.newStringUtf8((byte[])val));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void prepareActualMap(Map actual) {
		for (Iterator<Entry> it = actual.entrySet().iterator(); it.hasNext();) {
			Entry e = it.next();
			Object val = e.getValue();
			if (val instanceof Blob) {
				try {
					byte[] b = ((Blob)val).getBytes(1L, (int)((Blob)val).length());
					actual.put(e.getKey(), Strings.newStringUtf8(b));
				}
				catch (SQLException ex) {
					log.error(ex.getMessage());
				}
			}
		}
	}
	
	protected void compareListResult(List expect, List actual) {
		if (expect == null) {
			Assert.assertNull(actual);
			return;
		}

		Assert.assertNotNull(actual);
		
		for (Object o : expect) {
			if (o instanceof Map) {
				prepareExpectMap((Map)o);
			}
			else if (o instanceof TestBean) {
				prepareExpectBean((TestBean)o);
			}
		}
		for (Object o : actual) {
			if (o instanceof Map) {
				prepareActualMap((Map)o);
			}
			else if (o instanceof TestBean) {
				prepareActualBean((TestBean)o);
			}
		}
		Assert.assertEquals(expect, actual);
	}
	
	@SuppressWarnings("unchecked")
	protected void testQueryForList(String sql, Object param, Class type, List expectedResult) {
		logTestMethodName();

		List actualResult = null;
		try {
			actualResult = executor.selectList(sql, param, type);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}

		compareListResult(expectedResult, actualResult);
	}

	@SuppressWarnings("unchecked")
	protected void testQueryForList(String sql, Object param, int start, int limit, Class type, List expectedResult) {
		logTestMethodName();

		List actualResult = null;
		try {
			actualResult = executor.selectList(sql, param, type, start, limit);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}

		compareListResult(expectedResult, actualResult);
	}

	protected void compareMapResult(Map expect, Map actual) {
		if (expect == null) {
			Assert.assertNull(actual);
			return;
		}

		Assert.assertNotNull(actual);
		
		for (Object o : expect.values()) {
			if (o instanceof Map) {
				prepareExpectMap((Map)o);
			}
			else if (o instanceof TestBean) {
				prepareExpectBean((TestBean)o);
			}
		}
		for (Object o : actual.values()) {
			if (o instanceof Map) {
				prepareActualMap((Map)o);
			}
			else if (o instanceof TestBean) {
				prepareActualBean((TestBean)o);
			}
		}
		Assert.assertEquals(expect, actual);
	}
	
	@SuppressWarnings("unchecked")
	protected void testQueryForMap(String sql, Object param, Class type, Map expectedResult) {
		logTestMethodName();

		Map actualResult = null;
		try {
			actualResult = executor.selectMap(sql, param, type, "id", null);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}

		compareMapResult(expectedResult, actualResult);
	}

	protected void testExecuteUpdate(String updateSql, Object uparam, String selectSql, Object sparam, Map expectedResult) {
		logTestMethodName();

		try {
			int cnt = executor.update(updateSql, uparam);
			Assert.assertEquals(cnt, 1);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}

		Map actualResult = null;
		try {
			actualResult = executor.fetch(selectSql, sparam, Map.class);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}
		
		if (expectedResult == null) {
			Assert.assertNull(actualResult);
		}
		else {
			Assert.assertNotNull(actualResult);
			compareResult(expectedResult, actualResult);
		}
	}

	protected void testExecuteUpdate(String updateSql, Object uparam, String selectSql, Object sparam, TestBean expectedResult) {
		logTestMethodName();

		try {
			int cnt = executor.update(updateSql, uparam);
			Assert.assertEquals(cnt, 1);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}

		TestBean actualResult = null;
		try {
			actualResult = executor.fetch(selectSql, sparam, TestBean.class);
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}
		
		if (expectedResult == null) {
			Assert.assertNull(actualResult);
		}
		else {
			Assert.assertNotNull(actualResult);
			compareResult(expectedResult, actualResult);
		}
	}

	protected void testExecuteInsertAuto(String insertSql, Object param, String selectSql, Object expect) {
		logTestMethodName();

		Map key = null;
		try {
			key = executor.insert(insertSql, param, Map.class, "id");
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}
		
		Object id = null;
		// id is generated by DB
		Assert.assertNotNull(key);
		if (expect instanceof TestBean) {
			id = ((TestBean)expect).getId();
		}
		else {
			id = ((Map)expect).get("id");
		}
		Assert.assertEquals(id.toString(), key.get("id").toString());

		Object actualResult = null;
		try {
			if (executor instanceof SimpleSqlExecutor) {
				actualResult = executor.fetch(selectSql, expect, expect.getClass());
			}
			else {
				actualResult = executor.fetch(selectSql, Arrays.asList(id), expect.getClass());
			}
		}
		catch (Exception e) {
			log.error("exception", e);
			throw Exceptions.wrapThrow(e);
		}
		
		Assert.assertNotNull(actualResult);
		if (expect instanceof TestBean) {
			compareResult((TestBean)expect, (TestBean)actualResult);
		}
		else {
			compareResult((Map)expect, (Map)actualResult);
		}
	}
}
