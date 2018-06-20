package panda.dao.sql;

import java.sql.Statement;
import java.util.Properties;

import junit.framework.TestCase;
import panda.log.Log;
import panda.log.Logs;


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
				s.execute("SELECT * FROM INFORMATION_SCHEMA.TABLES");
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
		while (simpleDataSource.getActives() > 0);
	}

	/**
	 * test01
	 */
	public void test01() {
		log.debug("+++++++++++++test01+++++++++++++++");
		try {
			for (int i = 0; i < simpleDataSource.getPool().getMaximumActiveConnections() * 2; i++) {
				(new TestThread(simpleDataSource.getPool().getMaximumCheckoutTime() - 500)).start();
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
			for (int i = 0; i < simpleDataSource.getPool().getMaximumActiveConnections() * 2; i++) {
				(new TestThread(simpleDataSource.getPool().getMaximumCheckoutTime() + 500)).start();
			}
			printStatus();
		}
		catch (Exception e) {
			log.error("exception", e);
			fail(e.getMessage());
		}
	}
}
