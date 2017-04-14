package panda.dao.sql;

import java.io.File;
import java.io.IOException;

import panda.dao.DaoClient;
import panda.io.Files;
import panda.lang.Exceptions;


public class SqlDaoDerbyTest extends SqlDaoTestCase {
	private static SqlDaoClient client = createSqlDaoClient();

	protected static SqlDaoClient createSqlDaoClient() {
		try {
			Files.deleteDir(new File("out/test-classes/panda/derby.ptest"));
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return createSqlDaoClient("derby");
	}
	
	
	protected DaoClient getDaoClient() {
		return client;
	}

	protected String concatSql(String a, String b) {
		return "(" + a + " || " + b + ")";
	}
}
