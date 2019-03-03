package panda.dao.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import panda.dao.sql.dbcp.AbstractDataSource;
import panda.dao.sql.dbcp.SimpleDataSource;
import panda.dao.sql.dbcp.ThreadLocalDataSource;
import panda.io.Settings;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import panda.mock.sql.MockConnection;
import panda.mock.sql.MockDataSource;


/**
 * TestHelper
 */
public class TestHelper {
	private static Log log = Logs.getLog(TestHelper.class);
	private static Settings settings;
	private static Map<String, DataSource> sources = new HashMap<String, DataSource>();

	/**
	 * execSQL
	 * @param connection connection
	 * @param name resource name
	 * @param splitter splitter
	 * @throws Exception if an error occurs
	 */
	public static void execSQL(Connection connection, String name, String splitter) throws Exception {
		execSQL(TestHelper.class, connection, name, splitter);
	}
	
	/**
	 * execSQL
	 * @param clazz class
	 * @param connection connection
	 * @param name resource name
	 * @param splitter splitter
	 * @throws Exception if an error occurs
	 */
	public static void execSQL(Class clazz, Connection connection, String name, String splitter) throws Exception {
		if (connection instanceof MockConnection) {
			return;
		}
		
		InputStream is = clazz.getResourceAsStream(name);

		StringBuilder sqls = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.length() < 1 || line.startsWith("--")) {
					sqls.append(" ");
				}
				else {
					sqls.append(" ").append(line);
				}
			}
		}
		finally {
			Streams.safeClose(is);
		}

		String all = sqls.toString();
		String[] ss = all.split(splitter);
		
		Statement stm = connection.createStatement();
		for (String sql : ss) {
			sql = sql.trim();
			if (sql.length() > 0) {
				try {
					log.debug("Execute SQL: " + sql);
					stm.executeUpdate(sql);
				}
				catch (Exception e) {
					log.error("Error: " + e.getMessage());
				}
			}
		}
		Sqls.safeClose(stm);
	}
	
	public static synchronized DataSource getDataSource(String name) {
		if (settings == null) {
			settings = new Settings();
			try {
				settings.load(TestHelper.class.getResourceAsStream("jdbc.properties"), "test");
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
		}

		DataSource ds = sources.get(name);
		if (ds == null) {
			DriverManager.setLoginTimeout(10);

			AbstractDataSource ads = null;
			try {
				Map<String, String> dps = new TreeMap<String, String>(Collections.subMap(settings, name + '.'));

				String driver = dps.get("jdbc.driver");
				if ("org.sqlite.JDBC".equals(driver)) {
					ads = new ThreadLocalDataSource();
				}
				else {
					ads = new SimpleDataSource();
				}
				ads.initialize(dps);

				log.debug("Connect " + ads.getJdbc().getUrl() + " - " + ads.getJdbc().getUsername());
				ads.getConnection();
				
				ds = ads;
			}
			catch (Throwable e) {
				log.warn("Failed to connect " + ads.getJdbc().getUrl() + " - " + ads.getJdbc().getUsername() + ": " + e.getMessage());
				ds = new MockDataSource();
			}
			sources.put(name, ds);
		}
		return ds;
	}
	
	public static Connection getConnection(String name) throws Exception {
		DataSource ds = getDataSource(name);
		return ds.getConnection();
	}
}
