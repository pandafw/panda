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
import java.util.Properties;

import javax.sql.DataSource;

import panda.dao.sql.dbcp.SimpleDataSource;
import panda.io.Streams;
import panda.lang.Charsets;
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
	private static Properties properties;
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
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(TestHelper.class.getResourceAsStream("jdbc.properties"));
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
		}

		DataSource ds = sources.get(name);
		if (ds == null) {
			DriverManager.setLoginTimeout(5);

			SimpleDataSource sds = new SimpleDataSource();
			try {
				sds.getJdbc().setDriver(properties.getProperty(name + ".driver"));
				sds.getJdbc().setUrl(properties.getProperty(name + ".url"));
				sds.getJdbc().setUsername(properties.getProperty(name + ".username"));
				sds.getJdbc().setPassword(properties.getProperty(name + ".password"));
				sds.initialize();

				log.debug("Connect " + sds.getJdbc().getUrl() + " - " + sds.getJdbc().getUsername());

				sds.getConnection();

				ds = sds;
			}
			catch (Throwable e) {
				log.warn("Failed to connect " + sds.getJdbc().getUrl() + " - " + sds.getJdbc().getUsername(), e);
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
