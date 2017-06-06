package panda.dao.nosql.mongo;

import java.io.IOException;
import java.util.Properties;

import panda.dao.DaoClient;
import panda.dao.DaoTestCase;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;



/**
 */
public class MongoDaoTest extends DaoTestCase {
	private static final Log log = Logs.getLog(MongoDaoTest.class);
	
	private static MongoDaoClient client = createMongoDaoClient();

	protected static MongoDaoClient createMongoDaoClient() {
		Properties properties = new Properties();
		try {
			properties.load(MongoDaoTest.class.getResourceAsStream("jdbc.properties"));
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}

		String url = properties.getProperty("mongo.url");
		try {
			log.debug("Connnect " + url);
			return new MongoDaoClient(url);
		}
		catch (Exception e) {
			log.warn("Failed to connect " + url, e);
			return null;
		}
	}

	protected DaoClient getDaoClient() {
		return client;
	}
}
