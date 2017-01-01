package panda.dao.sql.expert;

import org.junit.Assert;
import org.junit.Test;

/**
 */
public class SqlExpertConfigTestCase {
	@Test
	public void testInit() {
		SqlExpertConfig sec = new SqlExpertConfig();
		Assert.assertNotNull(sec);
	}
}
