package panda.gae.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.appengine.api.datastore.DatastoreServiceFactory;

import panda.dao.DB;
import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DatabaseMeta;

public class GaeDaoClient extends DaoClient {
	private static final DatabaseMeta meta = new DatabaseMeta(DB.GAE, "Google App Engine", "1.0");
	
	private static GaeDaoClient instance = new GaeDaoClient();
	
	public static GaeDaoClient i() {
		return instance;
	}

	private Map<Class<?>, GaeConverter> gcs;

	private GaeDaoClient() {
		this.name = "GAE";
		gcs = new ConcurrentHashMap<Class<?>, GaeConverter>();
	}

	@Override
	public DatabaseMeta getDatabaseMeta() {
		return meta;
	}

	@Override
	public Dao getDao() {
		return new GaeDao(this, DatastoreServiceFactory.getDatastoreService());
	}

	public void registerConverter(Class<?> cls, GaeConverter gc) {
		gcs.put(cls, gc);
	}
	
	public GaeConverter findConverter(Class<?> cls) {
		return gcs.get(cls);
	}
}
