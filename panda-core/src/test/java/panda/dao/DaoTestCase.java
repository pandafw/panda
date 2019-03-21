package panda.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.entity.Klass;
import panda.dao.entity.Score;
import panda.dao.entity.Student;
import panda.dao.entity.Teacher;
import panda.dao.query.DataQuery;
import panda.lang.Exceptions;
import panda.lang.Threads;
import panda.log.Log;
import panda.log.Logs;


/**
 */
public abstract class DaoTestCase {
	private static Log log = Logs.getLog(DaoTestCase.class);

	protected Dao dao;
	
	protected abstract DaoClient getDaoClient();
	
	@Before
	public void setUp() {
		if (getDaoClient() == null) {
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

	protected void init() {
		dao.drop(Score.class);
		dao.drop(Klass.class);
		dao.drop(Student.class);
		dao.drop(Teacher.class);

		dao.create(Teacher.class);
		dao.create(Student.class);
		dao.create(Klass.class);
		dao.create(Score.class);
		
		dao.inserts(Teacher.creates(1, 5));
		dao.inserts(Student.creates(1, 5));
		dao.inserts(Klass.creates(1, 5));
		dao.inserts(Score.creates(1, 5));
	}

	@Test
	public void testCount() {
		Assert.assertEquals(5,  dao.count(Teacher.class));
	}

	@Test
	public void testCountQuery() {
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.eq("name", "T1");
		Assert.assertEquals(1,  dao.count(q));
	}

	@Test
	public void testExists() {
		Assert.assertTrue(dao.exists(Teacher.class));
		Assert.assertTrue(dao.exists(Student.class));
		Assert.assertTrue(dao.exists(Klass.class));
		Assert.assertTrue(dao.exists(Score.class));

		Assert.assertTrue(dao.exists(Teacher.class, Teacher.create(1)));
		
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
		Assert.assertNotNull(dao.insert(expect));
		
		Assert.assertEquals(6, expect.getId());
		
		Student actual = dao.fetch(Student.class, expect);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSaveAutoId() {
		Student expect = Student.create(6);
		expect.setId(0);
		Assert.assertNotNull(dao.save(expect));
		
		Assert.assertEquals(6, expect.getId());
		
		Student actual = dao.fetch(Student.class, expect);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSaveUpdatePk() {
		Teacher expect = Teacher.create(2);
		expect.setMemo("save");

		Assert.assertNotNull(dao.save(expect));
		
		Teacher actual = dao.fetch(Teacher.class, expect);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSaveInsertPk() {
		Teacher expect = Teacher.create(8);
		expect.setMemo("save");

		Assert.assertNotNull(dao.save(expect));
		
		Teacher actual = dao.fetch(Teacher.class, expect);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSaveUpdateId() {
		Student expect = Student.create(2);
		expect.setName("save");

		Assert.assertNotNull(dao.save(expect));
		
		Student actual = dao.fetch(Student.class, expect);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSaveInsertId() {
		Student expect = Student.create(6);
		Assert.assertNotNull(dao.save(expect));
		
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
	public void testSelectCamel() {
		Klass expect = Klass.create(1);
		
		Klass actual = dao.fetch(Klass.class, expect);
		
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSelectInclude() {
		List<Teacher> expect = Teacher.creates(1, 3);
		expect.remove(1);
		for (Teacher t : expect) {
			t.setData(null);
		}
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);

		q.in("name", "T1", "T3").include("name").include("memo");
		List<Teacher> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}
	
		
	@Test
	public void testSelectExclude() {
		List<Teacher> expect = Teacher.creates(1, 3);
		expect.remove(1);
		for (Teacher t : expect) {
			t.setData(null);
		}
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);

		q.in("name", "T1", "T3").exclude("data");
		List<Teacher> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSelectOr() {
		List<Teacher> expect = Teacher.creates(1, 3);
		expect.remove(1);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.or().eq("name", "T1").eq("name", "T3").end();
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectIn() {
		List<Teacher> expect = Teacher.creates(1, 3);
		expect.remove(1);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.in("name", "T1", "T3");
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSelectNotIn() {
		List<Teacher> expect = Teacher.creates(4, 5);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.nin("name", new String[] { "T1", "T2", "T3" });
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectBetween() {
		List<Student> expect = Student.creates(1, 3);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.between("id", 1, 3);
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectNotBetween() {
		List<Student> expect = Student.creates(4, 5);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.nbetween("id", 1, 3);
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectLike() {
		List<Student> expect = Student.creates(1, 1);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.like("name", "%1");
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectNotLike() {
		List<Student> expect = Student.creates(2, 5);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.nlike("name", "%1");
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectContains() {
		List<Student> expect = Student.creates(1, 1);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.contains("name", "1");
		
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectNotContains() {
		List<Student> expect = Student.creates(2, 5);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.ncontains("name", "1");
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectStartsWith() {
		List<Student> expect = Student.creates(1, 5);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.startsWith("name", "S");
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectNotStartsWith() {
		List<Student> expect = new ArrayList<Student>();
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.nstartsWith("name", "S");
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectEndsWith() {
		List<Student> expect = Student.creates(1, 1);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.endsWith("name", "1");
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectNotEndsWith() {
		List<Student> expect = Student.creates(2, 5);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.nendsWith("name", "1");
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectLikeEscape() {
		List<Student> expect = Student.creates(1, 1);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.like("name", "SS%1", 'S');
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectNotLikeEscape() {
		List<Student> expect = Student.creates(2, 5);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.nlike("name", "SS%1", 'S');
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectContainsEscape() {
		List<Teacher> expect = Teacher.creates(2, 3);
		expect.get(0).setMemo(expect.get(0).getMemo() + "%u");
		expect.get(1).setMemo(expect.get(1).getMemo() + "%u");

		Assert.assertEquals(1, dao.update(expect.get(0)));
		Assert.assertEquals(1, dao.update(expect.get(1)));
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(dao.getEntity(Teacher.class));
		q.clear().contains("memo", "%");
		List<Teacher> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectStart() {
		List<Teacher> expect = Teacher.creates(4, 5);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.start(3);
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectLimit() {
		List<Teacher> expect = Teacher.creates(1, 2);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.limit(2);
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectStartLimit() {
		List<Teacher> expect = Teacher.creates(3, 4);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.start(2).limit(2);
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}


	@Test
	public void testSelectOrderStart() {
		List<Teacher> expect = Teacher.creates(4, 5);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.orderByAsc("name").start(3);
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectOrderLimit() {
		List<Teacher> expect = Teacher.creates(1, 2);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.orderByAsc("name").limit(2);
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectOrderStartLimit() {
		List<Teacher> expect = Teacher.creates(3, 4);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.orderByAsc("name").start(2).limit(2);
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectPage() {
		List<Teacher> expect = Teacher.creates(2, 3);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.start(1).limit(2);
		List<Teacher> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testSelectPageWhere() {
		List<Student> expect = Student.creates(2, 3);
		
		DataQuery<Student> q = new DataQuery<Student>(Student.class);
		q.gt("id", 0).start(1).limit(2);
		List<Student> actual = dao.select(q);
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testIterate() {
		List<Teacher> expect = Teacher.creates(1, 2);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);
		q.limit(2);

		List<Teacher> actual = new ArrayList<Teacher>();
		try (DaoIterator<Teacher> it = dao.iterate(q)) {
			while (it.hasNext()) {
				actual.add(it.next());
			}
		}
		
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testDelete() {
		Score expect = Score.create(2, 2, 2);
		Assert.assertEquals(1, dao.delete(expect));
		
		Score actual = dao.fetch(Score.class, expect);
		Assert.assertNull(actual);
	}

	@Test
	public void testDeletes() {
		List<Score> expect = Score.creates(1, 1);
		Assert.assertEquals(expect.size(), dao.deletes(expect));
		
		for (Score s : expect) {
			Assert.assertNull(dao.fetch(Score.class, s));
		}
	}

	@Test
	public void testDeleteAll() {
		Assert.assertTrue(dao.deletes(Score.class) > 0);
		Assert.assertEquals(0, dao.count(Score.class));
	}
	
	@Test
	public void testUpdate() {
		Teacher expect = Teacher.create(2);
		expect.setMemo("update");

		Assert.assertEquals(1, dao.update(expect));
		
		Teacher actual = dao.fetch(Teacher.class, expect);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testUpdateColumn() {
		Teacher expect = Teacher.create(2);
		expect.setMemo("update");

		Assert.assertEquals(1, dao.update(expect, "memo"));
		
		Teacher actual = dao.fetch(Teacher.class, expect);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testUpdateIgnoreNull() {
		Teacher expect = Teacher.create(2);
		expect.setMemo("update");
	
		Teacher update = Teacher.create(2);
		update.setMemo(expect.getMemo());
		update.setData(null);

		Assert.assertEquals(1, dao.updateIgnoreNull(update));
		
		Teacher actual = dao.fetch(Teacher.class, expect);
		Assert.assertEquals(expect, actual);
	}

	@Test
	public void testUpdates() {
		List<Teacher> expect = Teacher.creates(2, 3);
		expect.get(0).setMemo("update1");
		expect.get(1).setMemo("update2");

		Assert.assertEquals(expect.size(), dao.updates(expect));
		
		Assert.assertEquals(expect.get(0), dao.fetch(Teacher.class, expect.get(0)));
		Assert.assertEquals(expect.get(1), dao.fetch(Teacher.class, expect.get(1)));
	}

	@Test
	public void testUpdatesByQuery() {
		List<Teacher> expect = Teacher.creates(2, 3);
		expect.get(0).setMemo("u");
		expect.get(1).setMemo("u");

		Teacher t = new Teacher();
		t.setMemo("u");

		DataQuery<Teacher> q = new DataQuery<Teacher>(dao.getEntity(Teacher.class));
		q.in("name", "T2", "T3").excludeAll().include("memo");
		
		Assert.assertEquals(expect.size(), dao.updates(t, q));
		
		Assert.assertEquals(expect.get(0), dao.fetch(Teacher.class, expect.get(0)));
		Assert.assertEquals(expect.get(1), dao.fetch(Teacher.class, expect.get(1)));
	}

	@Test
	public void testExecDelete() {
		final Score expect = Score.create(2, 2, 2);
		dao.exec(new Runnable() {
			@Override
			public void run() {
				dao.delete(expect);
			}
		});

		Score actual = dao.fetch(Score.class, expect);
		Assert.assertNull(actual);
	}

	@Test
	public void testExecDelete2() {
		final Score expect1 = Score.create(2, 2, 2);
		final Score expect2 = Score.create(3, 3, 3);
		dao.exec(new Runnable() {
			@Override
			public void run() {
				dao.delete(expect1);
				dao.exec(new Runnable() {
					@Override
					public void run() {
						dao.delete(expect2);
					}
				});
			}
		});

		Score actual1 = dao.fetch(Score.class, expect1);
		Assert.assertNull(actual1);

		Score actual2 = dao.fetch(Score.class, expect2);
		Assert.assertNull(actual2);
	}
	
	class TeacherTask implements Runnable {
		public Exception error;

		@Override
		public void run() {
			Dao dao = getDaoClient().getDao();
			try {
				int base = (int)Thread.currentThread().getId() * 1000;
				for (int i = 0; i < 100; i++) {
					Teacher t = Teacher.create(base + i);
					log.debug("insert> " + t);
					dao.insert(t);
				}
			}
			catch (Exception e) {
				log.error("error", e);
				error = e;
			}
		}
	}

	@Test
	public void testMultithreads() {
		List<TeacherTask> ss = new ArrayList<TeacherTask>();
		List<Thread> ts = new ArrayList<Thread>();
		for (int i = 0; i < 5; i++) {
			TeacherTask s = new TeacherTask();
			ss.add(s);
			
			Thread t = new Thread(s);
			ts.add(t);
			log.debug("Start thread: " + t.getId());
			t.start();
		}
		
		for (Thread t : ts) {
			Threads.safeJoin(t);
		}
		
		for (TeacherTask s : ss) {
			if (s.error != null) {
				Assert.fail(Exceptions.getStackTrace(s.error));
			}
		}
	}
}
