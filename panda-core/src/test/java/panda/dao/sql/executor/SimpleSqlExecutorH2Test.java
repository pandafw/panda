package panda.dao.sql.executor;

import java.sql.Connection;
import java.util.Map;

import org.junit.Test;

/**
 */
public class SimpleSqlExecutorH2Test extends SimpleSqlExecutorTestCase {

	@Override
	protected Connection getConnection() throws Exception {
		return TestSupport.getH2Connection();
	}

	@Override
	protected void prepareActualBean(TestBean actual) {
		// fix for real precision error 
		Double n = trimDouble(actual.getFreal());
		actual.setFreal(n);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void prepareActualMap(Map actual) {
		super.prepareActualMap(actual);

		// fix for real precision error 
		Double d = (Double)actual.get("freal");
		if (d != null) {
			Double n = trimDouble(d);
			actual.put("freal", n);
		}
	}
	
	@Test
	public void testInsertIdAuto() throws Exception {
		super.testInsertIdAuto();
	}
}
