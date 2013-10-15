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
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import panda.mock.sql.MockDataSource;


/**
 */
public abstract class SqlDaoTestCase {
	private static Log log = Logs.getLog(SqlDaoTestCase.class);

	protected Dao dao;
	
	protected abstract SqlDaoClient getDaoClient();
	
	protected static SqlDaoClient createSqlDaoClient(String db) {
		DataSource ds = TestHelper.getDataSource(db);
		if (!(ds instanceof MockDataSource)) {
			SqlDaoClient sdc = new SqlDaoClient();
			sdc.setDataSource(ds);
			return sdc;
		}
		return null;
	}

	@Before
	public void setUp() {
		if (getDaoClient() == null) {
			log.warn("SKIP: " + this.getClass().getName());
			Assume.assumeTrue(false);
		}
		dao = getDaoClient().getDao();
		try {
			init();
		}
		catch (Exception e) {
			log.error("init error", e);
			throw Exceptions.wrapThrow(e);
		}
	}

	protected void drop(Class clazz) {
		try {
			dao.drop(clazz);
		}
		catch (Exception e) {
			log.warn("failed to drop " + clazz + ": " + e);
		}
	}

	protected void init() {
		drop(Score.class);
		drop(Klass.class);
		drop(Student.class);
		drop(Teacher.class);

		dao.create(Teacher.class, true);
		dao.create(Student.class, true);
		dao.create(Klass.class, true);
		dao.create(Score.class, true);
		
		dao.inserts(Teacher.creates(1, 5));
		dao.inserts(Student.creates(1, 5));
		dao.inserts(Klass.creates(1, 5));
		dao.inserts(Score.creates(1, 5));
	}
	
	@Test
	public void testExists() {
		Assert.assertTrue(dao.exists(Teacher.class));
		Assert.assertTrue(dao.exists(Student.class));
		Assert.assertTrue(dao.exists(Klass.class));
		Assert.assertTrue(dao.exists(Score.class));
	}
	
	@Test
	public void testDrop() {
		dao.drop(Score.class);
		dao.drop(Klass.class);
		dao.drop(Teacher.class);
		dao.drop(Student.class);
	}
	
	@Test
	public void testInsertAutoId() {
		Student expect = Student.create(6);
		expect.setId(0);
		dao.insert(expect);
		
		Assert.assertEquals(6, expect.getId());
		
		Student actual = dao.fetch(Student.class, expect);
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
		List<Student> expect = Student.creates(1, 3);
		
		List<Student> actual = dao.select(Student.class, Query.create().between("id", 1, 3));
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectStart() {
		List<Teacher> expect = Teacher.creates(4, 5);
		
		List<Teacher> actual = dao.select(Teacher.class, Query.create().start(3));
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectLimit() {
		List<Teacher> expect = Teacher.creates(1, 2);
		
		List<Teacher> actual = dao.select(Teacher.class, Query.create().limit(2));
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectPage() {
		List<Teacher> expect = Teacher.creates(2, 3);
		
		List<Teacher> actual = dao.select(Teacher.class, Query.create().start(1).limit(2));
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectPageWhere() {
		List<Student> expect = Student.creates(2, 3);
		
		List<Student> actual = dao.select(Student.class, Query.create().greaterThan("id", 0).start(1).limit(2));
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testDelete() {
		Score expect = Score.create(2, 2, 2);
		dao.delete(expect);
		
		Score actual = dao.fetch(Score.class, expect);
		Assert.assertNull(actual);
	}

	@Test
	public void testDeletes() {
		List<Score> expect = Score.creates(1, 1);
		dao.deletes(expect);
		
		for (Score s : expect) {
			Assert.assertNull(dao.fetch(Score.class, s));
		}
	}

	@Test
	public void testUpdate() {
		Teacher expect = Teacher.create(2);
		expect.setMemo("update");

		dao.update(expect);
		
		Teacher actual = dao.fetch(Teacher.class, expect);
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testUpdates() {
		List<Teacher> expect = Teacher.creates(2, 3);
		expect.get(0).setMemo("update1");
		expect.get(1).setMemo("update2");

		dao.updates(expect);
		
		Assert.assertEquals(expect.get(0), dao.fetch(Teacher.class, expect.get(0)));
		Assert.assertEquals(expect.get(1), dao.fetch(Teacher.class, expect.get(1)));
	}
}
