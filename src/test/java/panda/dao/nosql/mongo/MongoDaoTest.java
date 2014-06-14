package panda.dao.nosql.mongo;

import java.io.IOException;
import java.util.Properties;

import panda.dao.DaoClient;
import panda.dao.DaoTestCase;
import panda.lang.Exceptions;



/**
 */
public class MongoDaoTest extends DaoTestCase {
	private static MongoDaoClient client = createMongoDaoClient();

	protected static MongoDaoClient createMongoDaoClient() {
		Properties properties = new Properties();
		try {
			properties.load(MongoDaoTest.class.getResourceAsStream("jdbc.properties"));
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}

		return new MongoDaoClient(properties.getProperty("mongo.url"));
	}

	protected DaoClient getDaoClient() {
		return client;
	}
}
