package panda.dao.sql;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;

import panda.dao.DaoTestCase;
import panda.dao.entity.Score;
import panda.dao.entity.Student;
import panda.dao.entity.Teacher;
import panda.dao.query.DataQuery;
import panda.log.Log;
import panda.log.Logs;
import panda.mock.sql.MockDataSource;


/**
 */
public abstract class SqlDaoTestCase extends DaoTestCase {
	private static Log log = Logs.getLog(SqlDaoTestCase.class);

	protected SqlDaoClient getSqlDaoClient() {
		return (SqlDaoClient)getDaoClient();
	}
	
	protected static SqlDaoClient createSqlDaoClient(String db) {
		DataSource ds = TestHelper.getDataSource(db);
		if (!(ds instanceof MockDataSource)) {
			SqlDaoClient sdc = new SqlDaoClient();
			try {
				sdc.setDataSource(ds);
			}
			catch (SQLException e) {
				log.error("Failed to set data source", e);
				return null;
			}
			return sdc;
		}
		return null;
	}

	@Test
	public void testSelectEmptyAndEnd() {
		List<Teacher> expect = Teacher.creates(1, 5);
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);

		q.and().end();
		List<Teacher> actual = dao.select(q);
		Assert.assertEquals(expect, actual);

		expect.remove(4);
		expect.remove(3);
		expect.remove(1);

		q.clear().in("name", "T1", "T3").and().end();
		actual = dao.select(q);
		Assert.assertEquals(expect, actual);

		q.clear().and().in("name", "T1", "T3").and().end().end();
		actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSelectColumn() {
		List<Teacher> expect = Teacher.creates(1, 3);
		expect.remove(1);
		for (Teacher t : expect) {
			t.setMemo("m");
		}
		
		DataQuery<Teacher> q = new DataQuery<Teacher>(Teacher.class);

		q.in("name", "T1", "T3").column("memo", "'m'");
		List<Teacher> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}

	
	@Test
	public void testSelectSum() {
		List<Score> expect = Score.sums(1, 2);
		
		DataQuery<Score> q = new DataQuery<Score>(Score.class);

		q.in("student", 1, 2).include("student").column("score", "sum(" + getSqlDaoClient().getSqlExpert().escapeColumn("score") + ")").groupBy("student");
		List<Score> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testSelectQueryJoin() {
		List<Score> expect = Score.creates(1, 2);
		for (Score s : expect) {
			s.setStudentName("S" + s.getStudent());
		}

		DataQuery<Score> q = new DataQuery<Score>(Score.class);
		DataQuery<Student> j = new DataQuery<Student>(Student.class);

		q.in("student", 1, 2)
			.leftJoin(j, "st", "student = id")
			.column("studentName", getSqlDaoClient().getSqlExpert().escapeColumn("st", "STUDENT_NAME"))
			.orderBy("score");
		List<Score> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}

	protected String concatSql(String a, String b) {
		return "CONCAT(" + a + ", " + b + ")";
	}

	@Test
	public void testUpdatesColumnByQuery() {
		List<Teacher> expect = Teacher.creates(2, 3);
		expect.get(0).setMemo(expect.get(0).getMemo() + "+u");
		expect.get(1).setMemo(expect.get(1).getMemo() + "+u");

		Teacher t = new Teacher();
		t.setMemo("u");

		DataQuery<Teacher> q = new DataQuery<Teacher>(dao.getEntity(Teacher.class));
		q.in("name", "T2", "T3").excludeAll().column("memo", concatSql("memo", "'+u'"));
		
		Assert.assertEquals(expect.size(), dao.updates(t, q));
		
		Assert.assertEquals(expect.get(0), dao.fetch(Teacher.class, expect.get(0)));
		Assert.assertEquals(expect.get(1), dao.fetch(Teacher.class, expect.get(1)));
	}
}
