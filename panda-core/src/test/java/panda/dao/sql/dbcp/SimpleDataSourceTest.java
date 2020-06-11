package panda.dao.sql.dbcp;

import java.sql.Statement;
import java.util.LinkedList;
import java.util.Properties;

import panda.cast.Castors;
import panda.lang.Threads;
import panda.log.Log;
import panda.log.Logs;

import junit.framework.TestCase;


/**
 * SimpleDataSourceTest
 */
public class SimpleDataSourceTest extends TestCase {
	private static Log log = Logs.getLog(SimpleDataSourceTest.class);
	
	private static SimpleDataSource simpleDataSource;

	private Throwable lastError;
	
	static {
		try {
			Properties p = new Properties();
			p.load(SimpleDataSourceTest.class.getResourceAsStream("SimpleDataSourceTest.properties"));
			simpleDataSource = new SimpleDataSource();
			Castors.scastTo(p, simpleDataSource);
		}
		catch (Exception e) {
			log.error("exception", e);
			fail(e.getMessage());
		}
	}
	
	class TestThread extends Thread {
		int loops;
		long timeout;
		
		TestThread(int loops, long timeout) {
			this.loops = loops;
			this.timeout = timeout;
		}
		
		public void run() {
			try {
				log.debug(this.getName() + " start.");
				
				for (int i = 0; i < loops; i++) {
					SimplePooledConnection c = (SimplePooledConnection)simpleDataSource.getConnection();
					
					Statement s = c.createStatement();
					s.execute("SELECT * FROM INFORMATION_SCHEMA.TABLES");
					s.close();
					
					Thread.sleep(timeout);
					c.close();
				}
				log.debug(this.getName() + " end.");
			}
			catch (Exception e) {
				log.error("exception", e);
				lastError = e;
			}
		}
	}
	
	/**
	 * test01
	 */
	public void test01() {
		System.out.println("+++++++++++++test01+++++++++++++++");

		LinkedList<Thread> ts = new LinkedList<Thread>();
		for (int i = 0; i < simpleDataSource.getPool().getMaxActive() * 2; i++) {
			Thread t = (new TestThread(100, 10));
			t.start();
			ts.push(t);
		}
		
		while (ts.size() > 0) {
			Threads.safeJoin(ts.pop());
		}

		System.out.println(simpleDataSource.toString());
		
		if (lastError != null) {
			fail(lastError.getMessage());
		}
	}

	/**
	 * test02
	 */
	public void test02() {
		System.out.println("+++++++++++++test02+++++++++++++++");

		LinkedList<Thread> ts = new LinkedList<Thread>();
		for (int i = 0; i < simpleDataSource.getPool().getMaxActive() * 2; i++) {
			Thread t = (new TestThread(10, 100));
			t.start();
			ts.push(t);
		}
		
		while (ts.size() > 0) {
			Threads.safeJoin(ts.pop());
		}

		System.out.println(simpleDataSource.toString());

		if (lastError != null) {
			fail(lastError.getMessage());
		}
	}
}
