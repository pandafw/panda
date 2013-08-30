package panda.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import panda.lang.Classes;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class AbstractDataAccessClient implements DaoClient {
	public static final String META_PREFIX = "meta.";
	
	protected String name;
	
	protected Properties properties;
	
	protected Map<String, ModelMetaData> metadataMap;

	/**
	 */
	public AbstractDataAccessClient() {
		super();
	}

	/**
	 * @param properties the properties to set
	 */
	public AbstractDataAccessClient(Properties properties) {
		super();
		setProperties(properties);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;

		metadataMap = new HashMap<String, ModelMetaData>();

		try {
			for (Entry<Object, Object> e : properties.entrySet()) {
				String n = e.getKey().toString();
				if (n.startsWith(META_PREFIX)) {
					n = n.substring(META_PREFIX.length());
					metadataMap.put(n, (ModelMetaData)Classes.newInstance(e.getValue().toString()));
				}
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @return model meta data map
	 */
	public Map<String, ModelMetaData> getMetaDataMap() {
		return metadataMap;
	}

	/**
	 * @param name model name
	 * @return model meta data
	 */
	public ModelMetaData getMetaData(String name) {
		ModelMetaData mmd = metadataMap.get(name);
		if (mmd == null) {
			throw new IllegalArgumentException("unknown metadata of [" + name + "].");
		}

		return mmd;
	}

	/**
	 * @param name model name
	 * @param session DataAccessSession
	 * @return modelDao
	 */
	public ModelDAO getModelDAO(String name, DaoSession session) {
		ModelMetaData mmd = getMetaData(name);
		if (mmd == null) {
			throw new IllegalArgumentException("unknown metadata of [" + name + "].");
		}
		
		Class cls = mmd.getModelDAOClass();
		if (cls == null) {
			throw new IllegalArgumentException("unknown dao class of [" + name + "].");
		}

		try {
			return (ModelDAO)Classes.newInstance(cls, session, DaoSession.class);
		}
		catch (Exception e) {
			throw new RuntimeException("failed to create dao instance (" + cls.getName() + ") of [" + name + "].");
		}
	}

	/**
	 * open session
	 */
	public DaoSession openSession() throws DaoException {
		return openSession(false);
	}
}
