package panda.dao.sql;

import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import panda.dao.Dao;
import panda.dao.criteria.Query;
import panda.dao.entity.Klass;
import panda.dao.entity.Score;
import panda.dao.entity.Student;
import panda.dao.entity.Teacher;
import panda.log.Log;
import panda.log.Logs;
import panda.mock.sql.MockDataSource;


/**
 */
public class SqlDaoHsqldbTest {
	private static Log log = Logs.getLog(SqlDaoHsqldbTest.class);

	private static SqlDaoClient client;
	
	static {
		DataSource ds = TestHelper.getDataSource("hsqldb");
		if (!(ds instanceof MockDataSource)) {
			SqlDaoClient sdc = new SqlDaoClient();
			sdc.setDataSource(ds);
			client = sdc;
		}
	}
	
	private Dao dao;
	
	@Before
	public void setUp() {
		if (client == null) {
			log.warn("SKIP: " + this.getClass().getName());
			Assume.assumeTrue(false);
		}
		dao = client.getDao();
		init();
	}

	protected void init() {
		dao.create(Teacher.class, true);
		dao.create(Student.class, true);
		dao.create(Klass.class, true);
		dao.create(Score.class, true);
		
		dao.inserts(Teacher.creates(0, 5));
	}
	
	@Test
	public void testExists() {
		Assert.assertTrue(dao.exists(Teacher.class));
		Assert.assertTrue(dao.exists(Student.class));
		Assert.assertTrue(dao.exists(Klass.class));
		Assert.assertTrue(dao.exists(Score.class));
		dao.drop(Teacher.class);
	}
	
	@Test
	public void testDrop() {
		dao.drop(Teacher.class);
		dao.drop(Student.class);
		dao.drop(Klass.class);
		dao.drop(Score.class);
	}
	
	@Test
	public void testInsert() {
		Teacher expect = Teacher.create(7);
		dao.insert(expect);
		
		Teacher actual = dao.fetch(Teacher.class, expect);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testFetch() {
		Assert.assertEquals(Teacher.create(1), dao.fetch(Teacher.class, "T1"));
		Assert.assertEquals(Teacher.create(2), dao.fetch(Teacher.class, Teacher.create(2)));
	}
	
	@Test
	public void testSelectIn() {
		List<Teacher> expect = Teacher.creates(1, 3);
		expect.remove(1);
		
		List<Teacher> actual = dao.select(Teacher.class, Query.create().in("name", new String[] { "T1", "T3" }));
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectBetween() {
		List<Teacher> expect = Teacher.creates(1, 3);
		
		List<Teacher> actual = dao.select(Teacher.class, Query.create().between("name", "T1", "T3"));
		
		Assert.assertEquals(expect, actual);
	}
}
