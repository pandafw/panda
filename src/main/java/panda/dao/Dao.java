package panda.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;
import panda.dao.entity.EntityField;
import panda.lang.Asserts;
import panda.lang.Objects;

/**
 * !! thread-unsafe !!
 * @author yf.frank.wang@gmail.com
 */
public abstract class Dao {
	protected final DaoClient daoClient;
	
	public Dao(DaoClient daoClient) {
		this.daoClient = daoClient;
	}

	/**
	 * @return the DaoClient
	 */
	public DaoClient getDaoClient() {
		return daoClient;
	}
	
	/**
	 * @return database meta
	 */
	public DatabaseMeta meta() {
		return daoClient.getDatabaseMeta();
	}

	/**
	 * @return beans
	 */
	public Beans getBeans() {
		return daoClient.getBeans();
	}

	protected void assertTable(String table) {
		Asserts.notEmpty(table, "The table is empty");
	}
	
	protected void assertTable(Entity<?> entity) {
		assertEntity(entity);
		Asserts.notEmpty(entity.getTableName(), "The table of [%s] is undefined", entity.getType().toString());
	}
	
	protected void assertType(Class<?> type) {
		Asserts.notNull(type, "The type is null");
	}

	protected void assertEntity(Entity<?> entity) {
		Asserts.notNull(entity, "The entity is null");
	}
	
	protected void assertEntity(Entity<?> entity, Class<?> type) {
		Asserts.notNull(entity, "The entity of [%s] is undefined", type.toString());
	}
	
	protected void assertCollection(Collection col) {
		Asserts.notNull(col, "The collection is null");
	}
	
	protected void assertObject(Object object) {
		Asserts.notNull(object, "The object is null");
	}
	
	protected void assertTransaction(Transaction transaction) {
		Asserts.notNull(transaction, "The transaction is null");
	}
	
	/**
	 * @param type record type
	 * @return the entity
	 */
	public <T> Entity<T> getEntity(Class<T> type) {
		assertType(type);
		return daoClient.getEntity(type);
	}

	/**
	 * @param type record type
	 * @return the entity
	 */
	public <T> EntityDao<T> getEntityDao(Class<T> type) {
		assertType(type);
		return new EntityDao<T>(this, type);
	}

	/**
	 * @param type record type
	 * @param param argument used for dynamic table
	 * @return the entity
	 */
	public <T> EntityDao<T> getEntityDao(Class<T> type, Object param) {
		assertType(type);
		return new EntityDao<T>(this, type, param);
	}

	/**
	 * drop a table if exists
	 * 
	 * @param type record type
	 */
	public void drop(Class<?> type) {
		drop(getEntity(type));
	}

	/**
	 * drop a table if exists
	 * 
	 * @param entity entity
	 */
	public abstract void drop(Entity<?> entity);

	/**
	 * drop a table if exists
	 * 
	 * @param table table name
	 */
	public abstract void drop(String table);

	/**
	 * create table
	 * 
	 * @param type record type
	 */
	public <T> void create(Class<T> type) {
		create(getEntity(type));
	}

	/**
	 * create table
	 * 
	 * @param entity entity
	 */
	public abstract void create(Entity<?> entity);

	/**
	 * check a table exists in the data store.
	 * 
	 * @param table table name
	 * @return true if the record or the table exists in the data store
	 */
	public abstract boolean exists(String table);

	/**
	 * check a record exists in the data store.
	 * if the keys is not supplied, then check the table existence.
	 * 
	 * @param type record type
	 * @param keys record keys (int, string or java bean with keys)
	 * @return true if the record or the table exists in the data store
	 */
	public boolean exists(Class<?> type, Object ... keys) {
		return exists(getEntity(type), keys);
	}

	/**
	 * check a record exists in the data store.
	 * if the keys is not supplied, then check the table existence.
	 * 
	 * @param entity entity
	 * @param keys record keys (int, string or java bean with keys)
	 * @return true if the record or the table exists in the data store
	 */
	public abstract boolean exists(Entity<?> entity, Object ... keys);

	/**
	 * get a record by the supplied keys
	 * 
	 * @param type record type
	 * @param keys record keys (int, string or java bean with keys)
	 */
	public <T> T fetch(Class<T> type, Object ... keys) {
		return fetch(getEntity(type), keys);
	}

	/**
	 * get a record by the supplied keys
	 * 
	 * @param entity entity
	 * @param keys record keys (int, string or java bean with keys)
	 */
	public abstract <T> T fetch(Entity<T> entity, Object ... keys);

	/**
	 * get a record by the supplied query
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return record
	 */
	public <T> T fetch(Class<T> type, Query query) {
		return fetch(getEntity(type), query);
	}

	/**
	 * get a record by the supplied query
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record
	 */
	public abstract <T> T fetch(Entity<T> entity, Query query);

	/**
	 * get a record by the supplied query
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record
	 */
	public abstract Map fetch(String table, Query query);

	/**
	 * delete a object.
	 * 
	 * @param obj object to be deleted
	 * @return deleted count
	 */
	public abstract int delete(Object obj);

	/**
	 * delete all records.
	 * 
	 * @param type record type
	 * @return deleted count
	 */
	public <T> int deletes(Class<T> type) {
		return deletes(getEntity(type));
	}

	/**
	 * delete records by the supplied keys.
	 * if the supplied keys is null, all records will be deleted.
	 * 
	 * @param type record type
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 */
	public <T> int delete(Class<T> type, Object ... keys) {
		return delete(getEntity(type), keys);
	}

	/**
	 * delete records by the supplied keys.
	 * if the supplied keys is null, all records will be deleted.
	 * 
	 * @param entity entity
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 */
	public abstract <T> int delete(Entity<T> entity, Object ... keys);

	/**
	 * delete object collection
	 * 
	 * @param col object collection to be deleted
	 * @return deleted count
	 */
	public abstract int deletes(Collection<?> col);

	/**
	 * delete records by the supplied query
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	public int deletes(Class<?> type, Query query) {
		return deletes(getEntity(type), query);
	}

	/**
	 * delete all records.
	 * 
	 * @param entity entity
	 * @return deleted count
	 */
	public int deletes(Entity<?> entity) {
		return deletes(entity, null);
	}

	/**
	 * delete records by the supplied query.
	 * if query is empty, all records will be deleted.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	public abstract int deletes(Entity<?> entity, Query query);

	/**
	 * delete all records.
	 * 
	 * @param table table name
	 * @return deleted count
	 */
	public int deletes(String table) {
		return deletes(table, null);
	}

	/**
	 * delete records by the supplied query.
	 * if query is empty, all records will be deleted.
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	public abstract int deletes(String table, Query query);

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
	public abstract <T> T insert(T obj);

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
	public abstract Collection<?> inserts(Collection<?> col);

	/**
	 * insert the object if not exists, 
	 * or update the record by the object.
	 * 
	 * @param obj object
	 * @return the saved record
	 */
	public abstract <T> T save(T obj);

	/**
	 * update a record by the supplied object. 
	 * 
	 * @param obj sample object
	 * @return updated count
	 */
	public abstract int update(Object obj);

	/**
	 * update a record by the supplied object. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @return updated count
	 */
	public abstract int updateIgnoreNull(Object obj);

	/**
	 * update records by the supplied object collection. 
	 * 
	 * @param col record collection
	 * @return updated count
	 */
	public abstract int updates(Collection<?> col);

	/**
	 * update records by the supplied object collection. 
	 * 
	 * @param col record collection
	 * @return updated count
	 */
	public abstract int updatesIgnoreNull(Collection<?> col);

	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	public abstract int updates(Object obj, Query query);

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	public abstract int updatesIgnoreNull(Object obj, Query query);

	/**
	 * select all records.
	 * 
	 * @param type record type
	 * @return record list
	 */
	public <T> List<T> select(Class<T> type) {
		return select(getEntity(type));
	}

	/**
	 * select all records.
	 * 
	 * @param entity entity
	 * @return record list
	 */
	public <T> List<T> select(Entity<T> entity) {
		return select(entity, (Query)null);
	}

	/**
	 * select all records.
	 * 
	 * @param table table name
	 * @return record(a map) list
	 */
	public List<Map> select(String table) {
		return select(table, (Query)null);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param type record type
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record list
	 */
	public <T> List<T> select(Class<T> type, Query query) {
		return select(getEntity(type), query);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record list
	 */
	public abstract <T> List<T> select(Entity<T> entity, Query query);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record(a map) list
	 */
	public abstract List<Map> select(String table, Query query);

	/**
	 * select all records.
	 * 
	 * @param type record type
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	public <T> int select(Class<T> type, DataHandler<T> callback) {
		return select(getEntity(type), callback);
	}

	/**
	 * select all records.
	 * 
	 * @param entity entity
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	public <T> int select(Entity<T> entity, DataHandler<T> callback) {
		return select(entity, null, callback);
	}

	/**
	 * select all records.
	 * 
	 * @param table table name
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	public int select(String table, DataHandler<Map> callback) {
		return select(table, null, callback);
	}

	/**
	 * select records by the supplied query.
	 * 
	 * @param type record type
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	public <T> int select(Class<T> type, Query query, DataHandler<T> callback) {
		return select(getEntity(type), query, callback);
	}

	/**
	 * select records by the supplied query.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	public abstract <T> int select(Entity<T> entity, Query query, DataHandler<T> callback);

	/**
	 * select records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	public abstract int select(String table, Query query, DataHandler<Map> callback);

	/**
	 * count all records.
	 * 
	 * @param type record type
	 * @return record count
	 */
	public int count(Class<?> type) {
		return count(getEntity(type));
	}

	/**
	 * count all records.
	 * 
	 * @param entity entity
	 * @return record count
	 */
	public int count(Entity<?> entity) {
		return count(entity, null);
	}

	/**
	 * count all records.
	 * 
	 * @param table table name
	 * @return record count
	 */
	public int count(String table) {
		return count(table, null);
	}

	/**
	 * count records by the supplied query.
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return record count
	 */
	public int count(Class<?> type, Query query) {
		return count(getEntity(type), query);
	}

	/**
	 * count records by the supplied query.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record count
	 */
	public abstract int count(Entity<?> entity, Query query);

	/**
	 * count records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record count
	 */
	public abstract int count(String table, Query query);

	/**
	 * execute a transaction
	 */
	public abstract void exec(Transaction transaction);
	
	/**
	 * execute a transaction
	 * @param level transaction level
	 */
	public abstract void exec(Transaction transaction, int level);
	
	/**
	 * commit a transaction
	 */
	public abstract void commit();

	/**
	 * rollback a transaction
	 */
	public abstract void rollback();

	//--------------------------------------------------------------------
	/**
	 * check the identity value
	 * @param id identity
	 * @return true if id != null && id != 0
	 */
	public boolean isValidIdentity(Object id) {
		return id != null && !id.equals(0);
	}
	
	//--------------------------------------------------------------------
	protected void queryPrimaryKey(Entity<?> entity, Query query, Object ... keys) {
		if (keys == null || keys.length == 0) {
			throw new IllegalArgumentException("Illegal primary keys for Entity [" + entity.getType() + "]: " + Objects.toString(keys));
		}
		
		if (keys.length == 1 && entity.getType().isInstance(keys[0])) {
			for (EntityField ef : entity.getPrimaryKeys()) {
				Object k = ef.getValue(keys[0]);
				if (k == null) {
					throw new IllegalArgumentException("Null primary keys for Entity [" + entity.getType() + "]: " + Objects.toString(keys));
				}
				query.equalTo(ef.getName(), k);
			}
		}
		else {
			if (keys.length != entity.getPrimaryKeys().size()) {
				throw new IllegalArgumentException("Illegal primary keys for Entity [" + entity.getType() + "]: " + Objects.toString(keys));
			}
			
			int i = 0;
			for (EntityField ef : entity.getFields()) {
				if (ef.isPrimaryKey()) {
					query.equalTo(ef.getName(), keys[i++]);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void excludeNullProperties(Entity<?> entity, Query query, Object obj) {
		BeanHandler bh = getBeans().getBeanHandler(obj.getClass());
		for (EntityField ef : entity.getFields()) {
			if (bh.getPropertyValue(obj, ef.getName()) == null) {
				query.exclude(ef.getName());
			}
		}
	}

	protected void excludePrimaryKeys(Entity<?> entity, Query query) {
		for (EntityField ef : entity.getFields()) {
			if (ef.isPrimaryKey()) {
				query.exclude(ef.getName());
			}
		}
	}

	protected void excludeNonPrimaryKeys(Entity<?> entity, Query query) {
		for (EntityField ef : entity.getFields()) {
			if (!ef.isPrimaryKey()) {
				query.exclude(ef.getName());
			}
		}
	}

	protected void includePrimaryKeys(Entity<?> entity, Query query) {
		for (EntityField ef : entity.getFields()) {
			if (!ef.isPrimaryKey()) {
				query.include(ef.getName());
			}
		}
	}
}
