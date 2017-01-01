package panda.dao.nosql.mongo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DatabaseMeta;
import panda.lang.Exceptions;

public class MongoDaoClient extends DaoClient {
	private MongoClient mongo;
	private MongoClientURI mcu;
	private DatabaseMeta meta;
	private Map<Class<?>, MongoConverter> mcs;
	
	/**
	 * mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database[.collection]][?options]]
	 * @param uri mongo connection uri
	 * @see MongoClientURI
	 */
	public MongoDaoClient(String uri) {
		mcu = new MongoClientURI(uri);
		
		try {
			mongo = new MongoClient(mcu);
			String ver = getDb().command("buildInfo").getString("version");
			meta = new DatabaseMeta(panda.dao.DB.MONGO, "Mongo Java Driver", ver);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
		
		mcs = new ConcurrentHashMap<Class<?>, MongoConverter>();
	}

	public DB getDb() {
		return mongo.getDB(mcu.getDatabase());
	}
	
	@Override
	public DatabaseMeta getDatabaseMeta() {
		return meta;
	}

	@Override
	public Dao getDao() {
		return new MongoDao(this, getDb());
	}

	/**
	 * tokumx support transaction
	 * http://www.tokutek.com/2013/04/mongodb-multi-statement-transactions-yes-we-can/
	 */
	public boolean isTransactionSupport() {
		return false;
	}
	
	public void registerConverter(Class<?> cls, MongoConverter mc) {
		mcs.put(cls, mc);
	}
	
	public MongoConverter findConverter(Class<?> cls) {
		return mcs.get(cls);
	}
}
