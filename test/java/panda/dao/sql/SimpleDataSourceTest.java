package panda.dao.sql;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import panda.dao.sql.SimpleDataSource;
import panda.dao.sql.SimplePooledConnection;
import panda.log.Log;
import panda.log.Logs;
import junit.framework.TestCase;


/**
 * SimpleDataSourceTest
 */
public class SimpleDataSourceTest extends TestCase {
	private static Log log = Logs.getLog(SimpleDataSourceTest.class);
	
	private static SimpleDataSource simpleDataSource;

	static {
		try {
			Properties p = new Properties();
			p.load(SimpleDataSourceTest.class.getResourceAsStream("SimpleDataSourceTest.properties"));
			simpleDataSource = new SimpleDataSource(p);
			Connection c = simpleDataSource.getConnection();
			TestSupport.initHsqldbTestData(c);
			c.close();
		}
		catch (Exception e) {
			log.error("exception", e);
			fail(e.getMessage());
		}
	}
	
	class TestThread extends Thread {
		int timeout;
		
		TestThread(int timeout) {
			this.timeout = timeout;
		}
		
		public void run() {
			try {
				log.debug(this.getName() + " start.");

				SimplePooledConnection c = (SimplePooledConnection)simpleDataSource.getConnection();
				
				Statement s = c.createStatement();
				s.execute("select * from TEST");
				s.close();
				
				Thread.sleep(timeout);
				c.close();
				
				log.debug(this.getName() + " end.");
			}
			catch (Exception e) {
				log.error("exception", e);
				fail(e.getMessage());
			}
		}
	}
	
	private void printStatus() {
		do {
			try {
				Thread.sleep(1000);
				log.debug(simpleDataSource.getStatus());
			}
			catch (InterruptedException e) {
			}
		} 
		while (simpleDataSource.getActiveConnections() > 0);
	}

	/**
	 * test01
	 */
	public void test01() {
		log.debug("+++++++++++++test01+++++++++++++++");
		try {
			for (int i = 0; i < simpleDataSource.getPoolMaximumActiveConnections() * 2; i++) {
				(new TestThread(simpleDataSource.getPoolMaximumCheckoutTime() - 500)).start();
			}
			printStatus();
		}
		catch (Exception e) {
			log.error("exception", e);
			fail(e.getMessage());
		}
	}

	/**
	 * test02
	 */
	public void test02() {
		log.debug("+++++++++++++test02+++++++++++++++");
		try {
			for (int i = 0; i < simpleDataSource.getPoolMaximumActiveConnections() * 2; i++) {
				(new TestThread(simpleDataSource.getPoolMaximumCheckoutTime() + 500)).start();
			}
			printStatus();
		}
		catch (Exception e) {
			log.error("exception", e);
			fail(e.getMessage());
		}
	}
}
