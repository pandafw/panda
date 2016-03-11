package panda.wing;

import java.io.IOException;

import panda.dao.Dao;
import panda.dao.entity.Entity;
import panda.filepool.dao.DaoFileData;
import panda.filepool.dao.DaoFileItem;
import panda.io.Streams;
import panda.log.Log;
import panda.log.Logs;
import panda.wing.entity.Resource;
import panda.wing.entity.Template;

/**
 */
public abstract class AppHelper {
	private static Log log = Logs.getLog(AppHelper.class);
	
	public static void ddlTables(Appendable writer, Dao dao, Class<?> ... classes) throws IOException {
		for (Class<?> c : classes) {
			Entity<?> e = dao.getEntity(c);
			writer.append("/* ").append(e.getTable()).append(" */");
			writer.append(Streams.LINE_SEPARATOR);
			writer.append(dao.ddl(c));
			writer.append(Streams.LINE_SEPARATOR);
			writer.append(Streams.LINE_SEPARATOR);
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

	public static void ddlDefaultTables(Appendable writer, Dao dao) throws IOException {
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
}
