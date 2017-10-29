package panda.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import panda.app.entity.Resource;
import panda.app.entity.Template;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DaoException;
import panda.dao.DaoIterator;
import panda.dao.entity.Entity;
import panda.dao.sql.SqlDaoClient;
import panda.dao.sql.SqlIterator;
import panda.dao.sql.Sqls;
import panda.dao.sql.executor.JdbcSqlExecutor;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;
import panda.vfs.dao.DaoFileData;
import panda.vfs.dao.DaoFileItem;

/**
 */
public abstract class AppHelper {
	private static Log log = Logs.getLog(AppHelper.class);
	
	public static final Class[] APP_ENTITIES = {
			DaoFileItem.class,
			DaoFileData.class,
			Template.class,
			Resource.class
		};
		

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
		ddlTables(writer, dao, APP_ENTITIES);
	}

	public static void createDefaultTables(Dao dao) {
		createTables(dao, APP_ENTITIES);
	}
	
	public static void dropDefaultTables(Dao dao) {
		Class[] es = APP_ENTITIES.clone();
		Arrays.reverse(es);
		dropTables(dao, es);
	}

	public static void copyTables(Dao sdao, Dao ddao, Class<?>... classes) {
		for (Class<?> c : classes) {
			log.info("Copy table for " + c);
			
			DaoIterator di = sdao.iterate(c);
			try {
				while (di.hasNext()) {
					Object o = di.next();
					if (ddao.exists(o.getClass(), o)) {
						log.debug("Update: " + o);
						ddao.update(o);
					}
					else {
						log.debug("Insert: " + o);;
						ddao.insert(o);
					}
				}
			}
			catch (DaoException e) {
				log.error("Failed to copy table for " + c, e);
			}
			finally {
				Streams.safeClose(di);
			}
		}
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
