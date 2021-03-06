package panda.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.cast.Castors;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;
import panda.dao.entity.EntityField;
import panda.dao.query.DataQuery;
import panda.dao.query.Join;
import panda.dao.query.Query;
import panda.lang.Arrays;
import panda.lang.Asserts;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Objects;

/**
 * !! thread-unsafe !!
 */
public abstract class AbstractDao implements Dao {
	protected final DaoClient daoClient;

	protected int timeout;
	
	public AbstractDao(DaoClient daoClient) {
		this.daoClient = daoClient;
		timeout = DEFAULT_TIMEOUT;
	}

	/**
	 * @return the DaoClient
	 */
	@Override
	public DaoClient getDaoClient() {
		return daoClient;
	}
	
	/**
	 * @return database meta
	 */
	@Override
	public DatabaseMeta meta() {
		return daoClient.getDatabaseMeta();
	}

	/**
	 * @return the timeout (seconds)
	 */
	@Override
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout (seconds) to set
	 */
	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	//---------------------------------------------------------------------------------
	/**
	 * @return beans
	 */
	protected Beans getBeans() {
		return daoClient.getBeans();
	}

	/**
	 * @return castors
	 */
	protected Castors getCastors() {
		return daoClient.getCastors();
	}

	//---------------------------------------------------------------------------------
	protected void assertTable(String table) {
		Asserts.notEmpty(table, "The table is empty");
	}
	
	protected void assertTable(Entity<?> entity) {
		assertEntity(entity);
		Asserts.notEmpty(entity.getTable(), "The table of [%s] is undefined", entity.getType().toString());
	}
	
	protected void assertType(Class<?> type) {
		Asserts.notNull(type, "The type is null");
	}

	protected void assertEntity(Entity<?> entity) {
		Asserts.notNull(entity, "The entity is null");
	}

	protected void assertEntityQuery(Query<?> query) {
		assertQuery(query);
		Asserts.notNull(query.getEntity(), "This entity of query is null");
	}

	protected void assertQuery(Query<?> query) {
		Asserts.notNull(query, "The query is null");
	}
	
	protected void assertEntity(Entity<?> entity, Class<?> type) {
		Asserts.notNull(entity, "The entity of [%s] is undefined", type.toString());
	}
	
	protected void assertCollection(Collection<?> col) {
		Asserts.notNull(col, "The collection is null");
	}
	
	protected void assertObject(Object object) {
		Asserts.notNull(object, "The object is null");
	}
	
	protected void assertTransaction(Runnable transaction) {
		Asserts.notNull(transaction, "The transaction is null");
	}

	@SuppressWarnings("unchecked")
	protected <T> T createEntityData(Entity<T> en) throws DaoException {
		try {
			return en == null ? (T)(new HashMap<String, Object>()) : Classes.newInstance(en.getType());
		}
		catch (Exception e) {
			throw new DaoException(e);
		}
	}

	//---------------------------------------------------------------------------------
	/**
	 * @param type record type
	 * @return the entity
	 */
	@Override
	public <T> Entity<T> getEntity(Class<T> type) {
		assertType(type);
		return daoClient.getEntity(type);
	}

	/**
	 * @param type record type
	 * @return the entity
	 */
	@Override
	public <T> EntityDao<T> getEntityDao(Class<T> type) {
		assertType(type);
		return new EntityDao<T>(this, type);
	}

	/**
	 * @param type record type
	 * @param param argument used for dynamic table
	 * @return the entity
	 */
	@Override
	public <T> EntityDao<T> getEntityDao(Class<T> type, Object param) {
		assertType(type);
		return new EntityDao<T>(this, type, param);
	}

	/**
	 * @param query query
	 * @return the table name of query
	 */
	@Override
	public String getTableName(Query<?> query) {
		return getDaoClient().getTableName(query);
	}

	/**
	 * @param entity entity
	 * @return the table name of entity
	 */
	@Override
	public String getTableName(Entity<?> entity) {
		return getDaoClient().getTableName(entity);
	}

	/**
	 * @param entity entity
	 * @return the view name of entity
	 */
	@Override
	public String getViewName(Entity<?> entity) {
		return getDaoClient().getViewName(entity);
	}

	//---------------------------------------------------------------------------------
	/**
	 * drop a table if exists
	 * 
	 * @param type record type
	 */
	@Override
	public void drop(Class<?> type) {
		drop(getEntity(type));
	}

	/**
	 * drop a table if exists
	 * 
	 * @param entity entity
	 */
	@Override
	public abstract void drop(Entity<?> entity);

	/**
	 * drop a table if exists
	 * 
	 * @param table table name
	 */
	@Override
	public abstract void drop(String table);

	//-------------------------------------------------------------------------
	/**
	 * create table
	 * 
	 * @param type record type
	 */
	@Override
	public void create(Class<?> type) {
		create(getEntity(type));
	}

	/**
	 * create table
	 * 
	 * @param entity entity
	 */
	@Override
	public abstract void create(Entity<?> entity);

	//-------------------------------------------------------------------------
	/**
	 * create table ddl
	 * 
	 * @param type record type
	 */
	@Override
	public String ddl(Class<?> type) {
		return ddl(getEntity(type));
	}

	/**
	 * create table ddl
	 * 
	 * @param entity entity
	 */
	@Override
	public abstract String ddl(Entity<?> entity);

	//-------------------------------------------------------------------------
	/**
	 * check a table exists in the data store.
	 * 
	 * @param table table name
	 * @return true if the record or the table exists in the data store
	 */
	@Override
	public boolean exists(String table) {
		return existsByTable(table);
	}
	
	/**
	 * check a record exists in the data store.
	 * if the keys is not supplied, then check the table existence.
	 * 
	 * @param type record type
	 * @param keys record keys (int, string or java bean with keys)
	 * @return true if the record or the table exists in the data store
	 */
	@Override
	public boolean exists(Class<?> type, Object ... keys) {
		return existsByKeys(getEntity(type), keys);
	}
	
	/**
	 * check a record exists in the data store.
	 * if the keys is not supplied, then check the table existence.
	 * 
	 * @param entity entity
	 * @param keys record keys (int, string or java bean with keys)
	 * @return true if the record or the table exists in the data store
	 */
	@Override
	public boolean exists(Entity<?> entity, Object ... keys) {
		assertEntity(entity);
		return existsByKeys(entity, keys);
	}

	/**
	 * check a record exists in the data store.
	 * if the query is not supplied, then check the table existence.
	 * 
	 * @param query query
	 * @return true if the record or the table exists in the data store
	 */
	@Override
	public boolean exists(Query<?> query) {
		return existsByQuery(cloneQuery(query));
	}

	/**
	 * check a table exists in the data store.
	 * 
	 * @param table table name
	 * @return true if the record or the table exists in the data store
	 */
	protected abstract boolean existsByTable(String table);

	/**
	 * check a record exists in the data store.
	 * if the keys is not supplied, then check the table existence.
	 * 
	 * @param entity entity
	 * @param keys record keys (int, string or java bean with keys)
	 * @return true if the record or the table exists in the data store
	 */
	protected abstract boolean existsByKeys(Entity<?> entity, Object ... keys);

	/**
	 * check a record exists in the data store.
	 * if the query is not supplied, then check the table existence.
	 * 
	 * @param query query
	 * @return true if the record or the table exists in the data store
	 */
	protected abstract boolean existsByQuery(DataQuery<?> query);

	//-------------------------------------------------------------------------
	/**
	 * get a record by the supplied keys
	 * 
	 * @param type record type
	 * @param keys record keys (int, string or java bean with keys)
	 */
	@Override
	public <T> T fetch(Class<T> type, Object ... keys) {
		return fetchByKeys(getEntity(type), keys);
	}

	/**
	 * get a record by the supplied keys
	 * 
	 * @param entity entity
	 * @param keys record keys (int, string or java bean with keys)
	 */
	@Override
	public <T> T fetch(Entity<T> entity, Object ... keys) {
		assertEntity(entity);
		return fetchByKeys(entity, keys);
	}

	/**
	 * get a record by the supplied query
	 * 
	 * @param query query
	 * @return record
	 */
	@Override
	public <T> T fetch(Query<T> query) {
		return fetchByQuery(cloneQuery(query));
	}

	/**
	 * get a record by the supplied keys
	 * 
	 * @param entity entity
	 * @param keys record keys (int, string or java bean with keys)
	 * @return record
	 */
	protected <T> T fetchByKeys(Entity<T> entity, Object ... keys) {
		assertEntity(entity);

		DataQuery<T> query = createQuery(entity);
		queryPrimaryKey(query, keys);

		return fetchByQuery(query);
	}

	/**
	 * get a record by the supplied query
	 * 
	 * @param query query
	 * @return record
	 */
	protected abstract <T> T fetchByQuery(DataQuery<T> query);
	
	//-------------------------------------------------------------------------
	/**
	 * count all records.
	 * 
	 * @param type record type
	 * @return record count
	 */
	@Override
	public long count(Class<?> type) {
		return count(createQuery(type));
	}

	/**
	 * count all records.
	 * 
	 * @param entity entity
	 * @return record count
	 */
	@Override
	public long count(Entity<?> entity) {
		assertEntity(entity);
		return count(createQuery(entity));
	}

	/**
	 * count all records.
	 * 
	 * @param table table name
	 * @return record count
	 */
	@Override
	public long count(String table) {
		return count(createQuery(table));
	}

	/**
	 * count records by the supplied query.
	 * 
	 * @param query query
	 * @return record count
	 */
	@Override
	public long count(Query<?> query) {
		return countByQuery(cloneQuery(query));
	}

	/**
	 * count records by the supplied query.
	 * 
	 * @param query query
	 * @return record count
	 */
	protected abstract long countByQuery(Query<?> query);

	//-------------------------------------------------------------------------
	/**
	 * select all records.
	 * 
	 * @param type record type
	 * @return record list
	 */
	@Override
	public <T> List<T> select(Class<T> type) {
		return selectByQuery(createQuery(type));
	}

	/**
	 * select all records.
	 * 
	 * @param entity entity
	 * @return record list
	 */
	@Override
	public <T> List<T> select(Entity<T> entity) {
		assertEntity(entity);
		return selectByQuery(createQuery(entity));
	}

	/**
	 * select all records.
	 * 
	 * @param table table name
	 * @return record(a map) list
	 */
	@Override
	public List<Map<String, ?>> select(String table) {
		return selectByQuery(createQuery(table));
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query query
	 * @return record list
	 */
	@Override
	public <T> List<T> select(Query<T> query) {
		return selectByQuery(cloneQuery(query));
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query query
	 * @return record list
	 */
	protected abstract <T> List<T> selectByQuery(DataQuery<T> query);

	/**
	 * select all records.
	 * 
	 * @param type record type
	 * @return data iterator
	 */
	@Override
	public <T> DaoIterator<T> iterate(Class<T> type) {
		return iterateByQuery(createQuery(type));
	}

	/**
	 * select all records.
	 * 
	 * @param entity entity
	 * @return data iterator
	 */
	@Override
	public <T> DaoIterator<T> iterate(Entity<T> entity) {
		assertEntity(entity);
		return iterateByQuery(createQuery(entity));
	}

	/**
	 * select all records.
	 * 
	 * @param table table name
	 * @return data iterator
	 */
	@Override
	public DaoIterator<Map<String, ?>> iterate(String table) {
		return iterateByQuery(createQuery(table));
	}

	/**
	 * select records by the supplied query.
	 * 
	 * @param query query
	 * @return data iterator
	 */
	@Override
	public <T> DaoIterator<T> iterate(Query<T> query) {
		return iterateByQuery(cloneQuery(query));
	}

	/**
	 * select records by the supplied query.
	 * 
	 * @param query query
	 * @return data iterator
	 */
	protected abstract <T> DaoIterator<T> iterateByQuery(DataQuery<T> query);

	//--------------------------------------------------------------------
	/**
	 * select all records to collection.
	 * 
	 * @param coll the collection to store the value
	 * @param type record type
	 * @param prop The property to be used as the value in the collection.
	 */
	@Override
	public Collection<?> coll(Collection<?> coll, Class<?> type, String prop) {
		return collByQuery(coll, createQuery(type), prop);
	}

	/**
	 * select all records to collection.
	 * 
	 * @param coll the collection to store the value
	 * @param entity entity
	 * @param prop The property to be used as the value in the collection.
	 */
	@Override
	public Collection<?> coll(Collection<?> coll, Entity<?> entity, String prop) {
		assertEntity(entity);
		return collByQuery(coll, createQuery(entity), prop);
	}

	/**
	 * select all records to collection.
	 * 
	 * @param coll the collection to store the value
	 * @param table table name
	 * @param prop The property to be used as the value in the collection.
	 */
	@Override
	public Collection<?> coll(Collection<?> coll, String table, String prop) {
		return collByQuery(coll, createQuery(table), prop);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param coll the collection to store the value
	 * @param prop The property to be used as the value in the collection.
	 * @param query query
	 */
	@Override
	public Collection<?> coll(Collection<?> coll, Query<?> query, String prop) {
		return collByQuery(coll, cloneQuery(query), prop);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Collection<?> collByQuery(Collection coll, DataQuery<?> query, String prop) {
		query.includeOnly(prop);

		BeanHandler bh = getDaoClient().getBeans().getBeanHandler(query.getType());

		DaoIterator<?> it = iterateByQuery(query);
		try {
			while (it.hasNext()) {
				Object o = it.next();
				Object v = bh.getPropertyValue(o, prop);
				coll.add(v);
			}
		}
		finally {
			it.close();
		}
		return coll;
	}

	//--------------------------------------------------------------------
	/**
	 * select all records to list.
	 * 
	 * @param type record type
	 * @param prop The property to be used as the value in the list.
	 * @return record value list
	 */
	@Override
	public List<?> list(Class<?> type, String prop) {
		return listByQuery(createQuery(type), prop);
	}

	/**
	 * select all records to list.
	 * 
	 * @param entity entity
	 * @param prop The property to be used as the value in the list.
	 * @return record value list
	 */
	@Override
	public List<?> list(Entity<?> entity, String prop) {
		assertEntity(entity);
		return listByQuery(createQuery(entity), prop);
	}

	/**
	 * select all records to list.
	 * 
	 * @param table table name
	 * @param prop The property to be used as the value in the list.
	 * @return record value list
	 */
	@Override
	public List<?> list(String table, String prop) {
		return listByQuery(createQuery(table), prop);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param prop The property to be used as the value in the list.
	 * @param query query
	 * @return record value list
	 */
	@Override
	public List<?> list(Query<?> query, String prop) {
		return listByQuery(cloneQuery(query), prop);
	}

	protected List<?> listByQuery(DataQuery<?> query, String prop) {
		List<?> list = new ArrayList<Object>();
		collByQuery(list, query, prop);
		return list;
	}

	//--------------------------------------------------------------------
	/**
	 * select all records to set.
	 * 
	 * @param type record type
	 * @param prop The property to be used as the value in the set.
	 * @return record value set
	 */
	@Override
	public Set<?> set(Class<?> type, String prop) {
		return setByQuery(createQuery(type), prop);
	}

	/**
	 * select all records to set.
	 * 
	 * @param entity entity
	 * @param prop The property to be used as the value in the set.
	 * @return record value set
	 */
	@Override
	public Set<?> set(Entity<?> entity, String prop) {
		assertEntity(entity);
		return setByQuery(createQuery(entity), prop);
	}

	/**
	 * select all records to set.
	 * 
	 * @param table table name
	 * @param prop The property to be used as the value in the set.
	 * @return record value set
	 */
	@Override
	public Set<?> set(String table, String prop) {
		return setByQuery(createQuery(table), prop);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param prop The property to be used as the value in the set.
	 * @param query query
	 * @return record value set
	 */
	@Override
	public Set<?> set(Query<?> query, String prop) {
		return setByQuery(cloneQuery(query), prop);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param prop The property to be used as the value in the set.
	 * @param query query
	 * @return record value set
	 */
	protected Set<?> setByQuery(DataQuery<?> query, String prop) {
		Set<?> set = new HashSet<Object>();
		collByQuery(set, query, prop);
		return set;
	}

	//--------------------------------------------------------------------
	/**
	 * select all records to map.
	 * 
	 * @param type record type
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record map
	 */
	@Override
	public <T> Map<?, T> map(Class<T> type, String keyProp) {
		return mapByQuery(createQuery(type), keyProp);
	}

	/**
	 * select all records to map.
	 * 
	 * @param entity entity
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record map
	 */
	@Override
	public <T> Map<?, T> map(Entity<T> entity, String keyProp) {
		assertEntity(entity);
		return mapByQuery(createQuery(entity), keyProp);
	}

	/**
	 * select all records to map.
	 * 
	 * @param table table name
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record(a map) map
	 */
	@Override
	public Map<?, Map<String, ?>> map(String table, String keyProp) {
		return mapByQuery(createQuery(table), keyProp);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param query query
	 * @return record map
	 */
	@Override
	public <T> Map<?, T> map(Query<T> query, String keyProp) {
		return mapByQuery(cloneQuery(query), keyProp);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param query query
	 * @return record map
	 */
	protected <T> Map<?, T> mapByQuery(DataQuery<T> query, String keyProp) {
		Map<Object, T> map = new LinkedHashMap<Object, T>();

		BeanHandler<T> bh = getDaoClient().getBeans().getBeanHandler(query.getType());

		DaoIterator<T> it = iterateByQuery(query);
		try {
			while (it.hasNext()) {
				T o = it.next();
				Object kv = bh.getPropertyValue(o, keyProp);
				map.put(kv, o);
			}
		}
		finally {
			it.close();
		}
		return map;
	}

	//--------------------------------------------------------------------
	/**
	 * select all records to map.
	 * 
	 * @param type record type
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record map
	 */
	@Override
	public Map<?, ?> map(Class<?> type, String keyProp, String valProp) {
		return mapByQuery(createQuery(type), keyProp, valProp);
	}

	/**
	 * select all records to map.
	 * 
	 * @param entity entity
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record map
	 */
	@Override
	public Map<?, ?> map(Entity<?> entity, String keyProp, String valProp) {
		assertEntity(entity);
		return mapByQuery(createQuery(entity), keyProp, valProp);
	}

	/**
	 * select all records to map.
	 * 
	 * @param table table name
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record(a map) map
	 */
	@Override
	public Map<?, ?> map(String table, String keyProp, String valProp) {
		return mapByQuery(createQuery(table), keyProp, valProp);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @param query query
	 * @return record map
	 */
	@Override
	public Map<?, ?> map(Query<?> query, String keyProp, String valProp) {
		return mapByQuery(cloneQuery(query), keyProp, valProp);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @param query query
	 * @return record map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map<?, ?> mapByQuery(DataQuery<?> query, String keyProp, String valProp) {
		query.includeOnly(keyProp, valProp);

		BeanHandler bh = getDaoClient().getBeans().getBeanHandler(query.getType());

		Map<Object, Object> map = new LinkedHashMap<Object, Object>();

		DaoIterator it = iterateByQuery(query);
		try {
			while (it.hasNext()) {
				Object o = it.next();
				Object kv = bh.getPropertyValue(o, keyProp);
				Object vv = bh.getPropertyValue(o, valProp);
				map.put(kv, vv);
			}
		}
		finally {
			it.close();
		}
		return map;
	}

	//--------------------------------------------------------------------
	/**
	 * select all records to map.
	 * 
	 * @param type record type
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record map
	 */
	@Override
	public <T> Map<?, List<T>> group(Class<T> type, String keyProp) {
		return groupByQuery(createQuery(type), keyProp);
	}

	/**
	 * select all records to map.
	 * 
	 * @param entity entity
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record map
	 */
	@Override
	public <T> Map<?, List<T>> group(Entity<T> entity, String keyProp) {
		assertEntity(entity);
		return groupByQuery(createQuery(entity), keyProp);
	}

	/**
	 * select all records to map.
	 * 
	 * @param table table name
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record(a map) group
	 */
	@Override
	public Map<?, List<Map<String, ?>>> group(String table, String keyProp) {
		return groupByQuery(createQuery(table), keyProp);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param query query
	 * @return record group
	 */
	@Override
	public <T> Map<?, List<T>> group(Query<T> query, String keyProp) {
		return groupByQuery(cloneQuery(query), keyProp);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param query query
	 * @return record group
	 */
	protected <T> Map<?, List<T>> groupByQuery(DataQuery<T> query, String keyProp) {
		BeanHandler<T> bh = getDaoClient().getBeans().getBeanHandler(query.getType());

		Map<Object, List<T>> map = new LinkedHashMap<Object, List<T>>();

		DaoIterator<T> it = iterateByQuery(query);
		try {
			while (it.hasNext()) {
				T o = it.next();
				Object kv = bh.getPropertyValue(o, keyProp);

				List<T> vs = map.get(kv);
				if (vs == null) {
					vs = new ArrayList<T>();
					map.put(kv, vs);
				}
				vs.add(o);
			}
		}
		finally {
			it.close();
		}
		return map;
	}

	//--------------------------------------------------------------------
	/**
	 * select all records to map.
	 * 
	 * @param type record type
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record map
	 */
	@Override
	public Map<?, List<?>> group(Class<?> type, String keyProp, String valProp) {
		return groupByQuery(createQuery(type), keyProp, valProp);
	}

	/**
	 * select all records to map.
	 * 
	 * @param entity entity
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record map
	 */
	@Override
	public Map<?, List<?>> group(Entity<?> entity, String keyProp, String valProp) {
		return groupByQuery(createQuery(entity), keyProp, valProp);
	}

	/**
	 * select all records to map.
	 * 
	 * @param table table name
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record(a map) group
	 */
	@Override
	public Map<?, List<?>> group(String table, String keyProp, String valProp) {
		return group(createQuery(table), keyProp, valProp);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @param query query
	 * @return record group
	 */
	@Override
	public Map<?, List<?>> group(Query<?> query, String keyProp, String valProp) {
		return groupByQuery(cloneQuery(query), keyProp, valProp);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @param query query
	 * @return record group
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<?, List<?>> groupByQuery(DataQuery<?> query, String keyProp, String valProp) {
		query.includeOnly(keyProp, valProp);
		
		BeanHandler bh = getDaoClient().getBeans().getBeanHandler(query.getType());

		Map<Object, List<?>> map = new LinkedHashMap<Object, List<?>>();

		DaoIterator it = iterateByQuery(query);
		try {
			while (it.hasNext()) {
				Object o = it.next();
				Object kv = bh.getPropertyValue(o, keyProp);
				Object vv = bh.getPropertyValue(o, valProp);

				List vs = map.get(kv);
				if (vs == null) {
					vs = new ArrayList();
					map.put(kv, vs);
				}
				vs.add(vv);
			}
		}
		finally {
			it.close();
		}
		return map;
	}

	//-------------------------------------------------------------------------
	/**
	 * delete a object.
	 * 
	 * @param obj object to be deleted
	 * @return deleted count
	 */
	@Override
	public int delete(Object obj) {
		assertObject(obj);

		Entity<?> entity = getEntity(obj.getClass());
		assertTable(entity);
		
		DataQuery<?> query = createQuery(entity);
		queryPrimaryKey(query, obj);

		return deletes(query);
	}

	/**
	 * delete records by the supplied keys.
	 * 
	 * @param type record type
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 * @throws IllegalArgumentException if the supplied keys is null
	 */
	@Override
	public int delete(Class<?> type, Object ... keys) {
		Entity<?> entity = getEntity(type);
		return delete(entity, keys);
	}

	/**
	 * delete records by the supplied keys.
	 * 
	 * @param entity entity
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 * @throws IllegalArgumentException if the supplied keys is null
	 */
	@Override
	public int delete(Entity<?> entity, Object ... keys) {
		if (keys == null || keys.length == 0) {
			throw new IllegalArgumentException("Illegal primary keys for [" + entity.getType() + "]: " + Objects.toString(keys));
		}

		assertTable(entity);

		DataQuery<?> query = createQuery(entity);
		queryPrimaryKey(query, keys);
		
		return deletesByQuery(query);
	}

	/**
	 * delete object collection
	 * 
	 * @param col object collection to be deleted
	 * @return deleted count
	 */
	@Override
	public int deletes(Collection<?> col) {
		assertCollection(col);

		autoStart();
		try {
			int cnt = 0;
			for (Object obj : col) {
				if (obj == null) {
					continue;
				}
				cnt += delete(obj);
			}

			autoCommit();
			return cnt;
		}
		finally {
			autoClose();
		}
	}

	/**
	 * delete all records.
	 * 
	 * @param type record type
	 * @return deleted count
	 */
	@Override
	public int deletes(Class<?> type) {
		return deletesByQuery(createQuery(type));
	}

	/**
	 * delete all records.
	 * 
	 * @param entity entity
	 * @return deleted count
	 */
	@Override
	public int deletes(Entity<?> entity) {
		return deletesByQuery(createQuery(entity));
	}

	/**
	 * delete all records.
	 * 
	 * @param table table name
	 * @return deleted count
	 */
	@Override
	public int deletes(String table) {
		return deletesByQuery(createQuery(table));
	}

	/**
	 * delete records by the supplied query
	 * 
	 * @param query query
	 * @return deleted count
	 */
	@Override
	public int deletes(Query<?> query) {
		return deletesByQuery(cloneQuery(query));
	}

	/**
	 * delete records by the supplied query.
	 * if query is empty, all records will be deleted.
	 * 
	 * @param query query
	 * @return deleted count
	 */
	protected abstract int deletesByQuery(Query<?> query);

	//-------------------------------------------------------------------------
	protected abstract <T> T insertData(Entity<T> entity, T obj);

	/**
	 * insert a record.
	 * <p>
	 * a '@Id' field will be set after insert. 
	 * set '@Id(auto=false)' to disable retrieving the primary key of the newly inserted row.
	 * <p>
	 * the '@Prep("SELECT ...")' sql will be executed before insert.
	 * <p>
	 * the '@Post("SELECT ...")' sql will be executed after insert.
	 * 
	 * @param obj the record to be inserted (@Id property will be setted)
	 * @return the inserted record
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T insert(T obj) {
		assertObject(obj);

		Entity<T> entity = getEntity((Class<T>)obj.getClass());
		return insert(entity, obj);
	}

	/**
	 * insert a record.
	 * <p>
	 * a '@Id' field will be set after insert. 
	 * set '@Id(auto=false)' to disable retrieving the primary key of the newly inserted row.
	 * <p>
	 * the '@Prep("SELECT ...")' sql will be executed before insert.
	 * <p>
	 * the '@Post("SELECT ...")' sql will be executed after insert.
	 * 
	 * @param entity the Entity of the obj
	 * @param obj the record to be inserted (@Id property will be setted)
	 * @return the inserted record
	 */
	public <T> T insert(Entity<T> entity, T obj) {
		assertObject(obj);

		assertTable(entity);
		
		autoStart();
		try {
			T d = insertData(entity, obj);
			autoCommit();
			return d;
		}
		catch (Exception e) {
			rollback();
			throw new DaoException("Failed to insert " + getTableName(entity) + ": " + obj, e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * insert record collections.
	 * <p>
	 * a '@Id' field will be set after insert. 
	 * set '@Id(auto=false)' to disable retrieving the primary key of the newly inserted row.
	 * <p>
	 * the '@Prep("SELECT ...")' sql will be executed before insert.
	 * <p>
	 * the '@Post("SELECT ...")' sql will be executed after insert.
	 * 
	 * @param col the record collection to be inserted
	 * @return the inserted record collection
	 */
	@Override
	public <T> Collection<T> inserts(Collection<T> col) {
		assertCollection(col);

		autoStart();
		try {
			for (T obj : col) {
				if (obj == null) {
					continue;
				}
				insert(obj);
			}

			autoCommit();
			return col;
		}
		finally {
			autoClose();
		}
	}

	/**
	 * insert record collections.
	 * <p>
	 * a '@Id' field will be set after insert. 
	 * set '@Id(auto=false)' to disable retrieving the primary key of the newly inserted row.
	 * <p>
	 * the '@Prep("SELECT ...")' sql will be executed before insert.
	 * <p>
	 * the '@Post("SELECT ...")' sql will be executed after insert.
	 * 
	 * @param entity the Entity of the obj
	 * @param col the record collection to be inserted
	 * @return the inserted record collection
	 */
	public <T> Collection<T> inserts(Entity<T> entity, Collection<T> col) {
		assertCollection(col);

		autoStart();
		try {
			for (T obj : col) {
				if (obj == null) {
					continue;
				}
				insert(entity, obj);
			}

			autoCommit();
			return col;
		}
		finally {
			autoClose();
		}
	}

	//-------------------------------------------------------------------------
	/**
	 * insert the object if not exists, 
	 * or update the record by the object.
	 * 
	 * @param obj object
	 * @return the saved record
	 */
	@Override
	public <T> T save(T obj) {
		assertObject(obj);

		Entity<?> entity = getEntity(obj.getClass());
		assertTable(entity);

		EntityField eid = entity.getIdentity();
		if (eid != null) {
			Object iid = eid.getValue(obj);
			if (!isValidIdentity(iid)) {
				return insert(obj);
			}
		}
		
		if (update(obj) == 0) {
			return insert(obj);
		}
		return obj;
	}

	//-------------------------------------------------------------------------
	/**
	 * update a record by the supplied object and fields. 
	 * 
	 * @param obj sample object
	 * @param fields the fields to update
	 * @return updated count
	 */
	@Override
	public int update(Object obj, String... fields) {
		return update(obj, Arrays.asList(fields));
	}
	
	/**
	 * update a record by the supplied object and fields. 
	 * 
	 * @param obj sample object
	 * @param fields the fields to update
	 * @return updated count
	 */
	@Override
	public int update(Object obj, Collection<String> fields) {
		assertObject(obj);

		Entity<?> entity = getEntity(obj.getClass());
		assertTable(entity);
		
		DataQuery<?> query = createQuery(entity);
		queryPrimaryKey(query, obj);
		if (Collections.isNotEmpty(fields)) {
			query.excludeAll().include(fields);
		}

		return updatesByQuery(obj, query, 1);
	}

	/**
	 * update a record by the supplied object. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @return updated count
	 */
	@Override
	public int updateIgnoreNull(Object obj) {
		assertObject(obj);

		Entity<?> entity = getEntity(obj.getClass());
		assertTable(entity);
		
		DataQuery<?> query = createQuery(entity);
		queryPrimaryKey(query, obj);
		excludeNullProperties(query, obj);

		return updatesByQuery(obj, query, 1);
	}

	/**
	 * update records by the supplied object collection and fields. 
	 * 
	 * @param col record collection
	 * @param fields the fields to update
	 * @return updated count
	 */
	@Override
	public int updates(Collection<?> col, String... fields) {
		return updates(col, Arrays.asList(fields));
	}
	
	/**
	 * update records by the supplied object collection and fields. 
	 * 
	 * @param col record collection
	 * @param fields the fields to update
	 * @return updated count
	 */
	@Override
	public int updates(Collection<?> col, Collection<String> fields) {
		assertCollection(col);

		autoStart();
		try {
			int cnt = 0;
			for (Object obj : col) {
				if (obj == null) {
					continue;
				}
				cnt += update(obj, fields);
			}

			autoCommit();
			return cnt;
		}
		finally {
			autoClose();
		}
	}

	/**
	 * update records by the supplied object collection. 
	 * 
	 * @param col record collection
	 * @return updated count
	 */
	@Override
	public int updatesIgnoreNull(Collection<?> col) {
		assertCollection(col);

		autoStart();
		try {
			int cnt = 0;
			for (Object obj : col) {
				if (obj == null) {
					continue;
				}
				cnt += updateIgnoreNull(obj);
			}

			autoCommit();
			return cnt;
		}
		finally {
			autoClose();
		}
	}

	/**
	 * update records by the supplied query
	 * 
	 * @param query query
	 * @return updated count
	 */
	@Override
	public int updates(Query<?> query) {
		return updatesByQuery(null, cloneQuery(query), Integer.MAX_VALUE);
	}

	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query query
	 * @return updated count
	 */
	@Override
	public int updates(Object obj, Query<?> query) {
		return updatesByQuery(obj, cloneQuery(query), Integer.MAX_VALUE);
	}

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query query
	 * @return updated count
	 */
	@Override
	public int updatesIgnoreNull(Object obj, Query<?> query) {
		assertObject(obj);

		Entity<?> entity = getEntity(obj.getClass());
		assertTable(entity);
		
		DataQuery<?> gq = cloneQuery(query);
		excludeNullProperties(gq, obj);

		return updatesByQuery(obj, gq, Integer.MAX_VALUE);
	}

	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query query
	 * @param limit limit count of updated items
	 * @return updated count
	 */
	protected abstract int updatesByQuery(Object obj, DataQuery<?> query, int limit);
	
	//-------------------------------------------------------------------------
	/**
	 * execute a transaction
	 */
	@Override
	public abstract void exec(Runnable transaction);
	
	/**
	 * execute a transaction
	 * @param level transaction level
	 */
	@Override
	public abstract void exec(Runnable transaction, int level);
	
	/**
	 * commit a transaction
	 */
	@Override
	public abstract void commit();

	/**
	 * rollback a transaction
	 */
	@Override
	public abstract void rollback();

	//---------------------------------------------------------------------
	protected abstract void autoStart();

	protected abstract void autoCommit();

	protected abstract void autoClose();

	//--------------------------------------------------------------------
	/**
	 * check the identity value
	 * @param id identity
	 * @return true if id != null && id != ''
	 */
	@Override
	public boolean isValidIdentity(Object id) {
		if (id == null) {
			return false;
		}
		
		if (id instanceof Number) {
			return ((Number)id).longValue() > 0;
		}

		if (id instanceof CharSequence) {
			return ((CharSequence)id).length() > 0;
		}

		return false;
	}
	
	//--------------------------------------------------------------------
	protected <T> DataQuery<T> createQuery(Class<T> type) {
		return new DataQuery<T>(getEntity(type));
	}
	
	protected <T> DataQuery<T> createQuery(Entity<T> entity) {
		return new DataQuery<T>(entity);
	}
	
	protected DataQuery<Map<String, ?>> createQuery(String table) {
		return new DataQuery<Map<String, ?>>(table);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> DataQuery<T> cloneQuery(Query<T> query) {
		assertQuery(query);
		DataQuery<T> q = new DataQuery<T>(query);
		if (q.getEntity() == null && q.getType() != null) {
			Entity<T> entity = getEntity(q.getType());
			q.setEntity(entity);
		}
		if (q.hasJoins()) {
			for (Entry<String, Join> en : q.getJoins().entrySet()) {
				Join join = en.getValue();
				Query<?> jq = join.getQuery();
				if (jq.getEntity() == null && jq.getType() != null) {
					DataQuery<?> gq = new DataQuery(jq);
					Entity entity = getEntity(gq.getType());
					gq.setEntity(entity);

					Join nj = new Join(join.getType(), gq, join.getConditions());
					en.setValue(nj);
				}
			}
		}
		
		return q;
	}
	
	//--------------------------------------------------------------------
	protected void queryPrimaryKey(DataQuery<?> query, Object ... keys) {
		query.equalToPrimaryKeys(keys);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void excludeNullProperties(DataQuery<?> query, Object obj) {
		Entity<?> entity = query.getEntity();
		BeanHandler bh = getBeans().getBeanHandler(obj.getClass());

		if (query.hasColumns()) {
			for (EntityField ef : entity.getFields()) {
				if (bh.getPropertyValue(obj, ef.getName()) == null) {
					query.exclude(ef.getName());
				}
			}
		}
		else {
			for (EntityField ef : entity.getFields()) {
				if (bh.getPropertyValue(obj, ef.getName()) != null) {
					query.include(ef.getName());
				}
			}
		}
	}

	protected void excludePrimaryKeys(DataQuery<?> query) {
		if (query.hasColumns()) {
			query.excludePrimaryKeys();
		}
		else {
			query.includeNotPrimaryKeys();
		}
	}

	protected void selectPrimaryKeys(DataQuery<?> query) {
		query.clearColumns();
		query.includePrimayKeys();
	}
}
