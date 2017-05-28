package panda.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.entity.Entity;
import panda.dao.sql.SqlDaoClient;
import panda.dao.sql.SqlIterator;
import panda.dao.sql.Sqls;
import panda.dao.sql.executor.JdbcSqlExecutor;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import panda.app.entity.Resource;
import panda.app.entity.Template;
import panda.vfs.dao.DaoFileData;
import panda.vfs.dao.DaoFileItem;

/**
 */
public abstract class AppHelper {
	private static Log log = Logs.getLog(AppHelper.class);
	
	public static void ddlTables(Appendable writer, Dao dao, Class<?> ... classes) {
		try {
			for (Class<?> c : classes) {
				Entity<?> e = dao.getEntity(c);
				writer.append("/* ").append(e.getTable()).append(" */");
				writer.append(Streams.LINE_SEPARATOR);
				writer.append(dao.ddl(c));
				writer.append(Streams.LINE_SEPARATOR);
				writer.append(Streams.LINE_SEPARATOR);
			}
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static void createTables(Dao dao, Class<?> ... classes) {
		for (Class<?> c : classes) {
			if (!dao.exists(c)) {
				log.info("Create table for " + c);
				dao.create(c);
			}
		}
	}

	public static void dropTables(Dao dao, Class<?> ... classes) {
		for (Class<?> c : classes) {
			log.info("Drop table for " + c);
			dao.drop(c);
		}
	}

	public static void ddlDefaultTables(Appendable writer, Dao dao) {
		ddlTables(writer, dao,
			DaoFileItem.class,
			DaoFileData.class,
			Template.class,
			Resource.class
			);
	}

	public static void createDefaultTables(Dao dao) {
		createTables(dao,
			DaoFileItem.class,
			DaoFileData.class,
			Template.class,
			Resource.class
			);
	}
	
	public static void dropDefaultTables(Dao dao) {
		dropTables(dao,
			Template.class,
			Resource.class,
			DaoFileData.class,
			DaoFileItem.class
			);
	}
	
	public static void execSql(DaoClient dc, String file) throws SQLException {
		if (dc instanceof SqlDaoClient) {
			JdbcSqlExecutor se = ((SqlDaoClient)dc).getJdbcSqlExecutor();
			Connection connection = ((SqlDaoClient)dc).getDataSource().getConnection();
			se.setConnection(connection);
			try {
				connection.setAutoCommit(true);
				InputStream is = ClassLoaders.getResourceAsStream(file);
				Reader r = Streams.toReader(is, Charsets.UTF_8);
				SqlIterator si = new SqlIterator(r);
				while (si.hasNext()) {
					String sql = si.next();
					log.info("EXEC SQL: " + sql);
					se.execute(sql);
				}
				si.close();
			}
			finally {
				Sqls.safeClose(connection);
			}
		}
	}
}
