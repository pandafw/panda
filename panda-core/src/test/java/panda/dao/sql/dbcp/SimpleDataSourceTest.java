package panda.dao.sql.dbcp;

import java.sql.Statement;
import java.util.Properties;

import junit.framework.TestCase;
import panda.cast.Castors;
import panda.dao.sql.dbcp.SimpleDataSource;
import panda.dao.sql.dbcp.SimplePooledConnection;
import panda.log.Log;
import panda.log.Logs;


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
		long timeout;
		
		TestThread(long timeout) {
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
				lastError = e;
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
			for (int i = 0; i < simpleDataSource.getPool().getMaxActive() * 2; i++) {
				(new TestThread(simpleDataSource.getPool().getMaxCheckoutMillis() - 1000)).start();
			}
			printStatus();
		}
		catch (Exception e) {
			log.error("exception", e);
			fail(e.getMessage());
		}
		
		if (lastError != null) {
			fail(lastError.getMessage());
		}
	}

	/**
	 * test02
	 */
	public void test02() {
		log.debug("+++++++++++++test02+++++++++++++++");
		try {
			for (int i = 0; i < simpleDataSource.getPool().getMaxActive() * 2; i++) {
				(new TestThread(simpleDataSource.getPool().getMaxCheckoutMillis() + 1000)).start();
			}
			printStatus();
		}
		catch (Exception e) {
			log.error("exception", e);
			fail(e.getMessage());
		}
		
		if (lastError != null) {
			fail(lastError.getMessage());
		}
	}
}
