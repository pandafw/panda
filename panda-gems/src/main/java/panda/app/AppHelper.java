package panda.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import panda.cast.Castors;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DaoException;
import panda.dao.DaoIterator;
import panda.dao.entity.Entity;
import panda.dao.sql.SqlDaoClient;
import panda.dao.sql.SqlIterator;
import panda.dao.sql.Sqls;
import panda.dao.sql.dbcp.SimpleDataSource;
import panda.dao.sql.executor.JdbcSqlExecutor;
import panda.dao.sql.executor.JdbcSqlExecutors;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 */
public abstract class AppHelper {
	private static Log log = Logs.getLog(AppHelper.class);
	
	public static void ddlTables(Appendable writer, Dao dao, Class<?> ... classes) {
		try {
			for (Class<?> c : classes) {
				Entity<?> e = dao.getEntity(c);
				writer.append("/* ").append(e.getTable()).append(" */");
				writer.append(Streams.EOL);
				writer.append(dao.ddl(c));
				writer.append(Streams.EOL);
				writer.append(Streams.EOL);
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

	public static void copyTables(final Dao sdao, final Dao ddao, Class<?>... classes) {
		for (final Class<?> c : classes) {
			log.info("Copy table for " + c);
			
			final DaoIterator di = sdao.iterate(c);
			try {
				ddao.exec(new Runnable() {
					@Override
					public void run() {
						int i = 0;
						while (di.hasNext()) {
							Object o = di.next();
							if (ddao.exists(o.getClass(), o)) {
								log.debug("Update [" + c.getSimpleName() + "]: " + o);
								ddao.update(o);
							}
							else {
								log.debug("Insert [" + c.getSimpleName() + "]: " + o);;
								ddao.insert(o);
								if (++i % 100 == 0) {
									ddao.commit();
								}
							}
						}
					}
				});
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
			Connection connection = ((SqlDaoClient)dc).getDataSource().getConnection();
			JdbcSqlExecutor se = JdbcSqlExecutors.getJdbcExecutor(connection);
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

	public static SqlDaoClient createSqlDaoClient(Map<String, String> props) throws SQLException {
		SimpleDataSource sds = new SimpleDataSource();
		Castors.scastTo(props, sds);

		SqlDaoClient sdc = new SqlDaoClient();
		sdc.setDataSource(sds);
		
		return sdc;
	}
}
