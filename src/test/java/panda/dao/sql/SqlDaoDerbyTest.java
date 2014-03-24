package panda.dao.sql;

import panda.dao.entity.Score;
import panda.dao.query.GenericQuery;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;



/**
 */
public class SqlDaoDerbyTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient("derby");
	
	protected SqlDaoClient getDaoClient() {
		return client;
	}

	@Test
	public void testSelectSum() {
		List<Score> expect = Score.sums(1, 2);
		
		GenericQuery<Score> q = new GenericQuery<Score>(Score.class);

		q.in("student", new Object[] { 1, 2 }).include("student").column("score", "sum(\"score\")").groupBy("student");
		List<Score> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}
	
}
