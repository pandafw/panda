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
import panda.dao.query.GenericQuery;
import panda.log.Log;
import panda.log.Logs;
import panda.mock.sql.MockDataSource;


/**
 */
public abstract class SqlDaoTestCase extends DaoTestCase {
	private static Log log = Logs.getLog(SqlDaoTestCase.class);

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
		
		GenericQuery<Teacher> q = new GenericQuery<Teacher>(Teacher.class);

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
		
		GenericQuery<Teacher> q = new GenericQuery<Teacher>(Teacher.class);

		q.in("name", "T1", "T3").column("memo", "'m'");
		List<Teacher> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}

	
	@Test
	public void testSelectSum() {
		List<Score> expect = Score.sums(1, 2);
		
		GenericQuery<Score> q = new GenericQuery<Score>(Score.class);

		q.in("student", 1, 2).include("student").column("score", "sum(" + escapeColumn("score") + ")").groupBy("student");
		List<Score> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSelectQueryJoin() {
		List<Score> expect = Score.creates(1, 2);
		for (Score s : expect) {
			s.setStudentName("S" + s.getStudent());
		}

		GenericQuery<Score> q = new GenericQuery<Score>(Score.class);
		GenericQuery<Student> j = new GenericQuery<Student>(Student.class);

		q.in("student", 1, 2)
			.leftJoin(j, "s", "student = id")
			.column("studentName", "s.name")
			.orderBy("score");
		List<Score> actual = dao.select(q);
		Assert.assertEquals(expect, actual);
	}
}
