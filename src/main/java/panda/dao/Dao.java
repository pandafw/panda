package panda.dao;

import java.util.List;
import java.util.Map;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;
import panda.dao.entity.EntityField;
import panda.lang.Asserts;

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
	 * @return datebase meta
	 */
	public DatabaseMeta meta() {
		return daoClient.getMeta();
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
	 * create table
	 * 
	 * @param type record type
	 * @param dropIfExists drop if table exists
	 * @return true if table exists or create successfully
	 */
	public <T> boolean create(Class<T> type, boolean dropIfExists) {
		assertType(type);

		Entity<T> en = getEntity(type);
		assertEntity(en, type);
		return create(en, dropIfExists);
	}

	/**
	 * create table
	 * 
	 * @param entity entity
	 * @param dropIfExists drop if table exists
	 * @return true if table exists or create successfully
	 */
	public abstract boolean create(Entity<?> entity, boolean dropIfExists);

	/**
	 * drop a table if exists
	 * 
	 * @param type record type
	 * @return true if drop successfully
	 */
	public boolean drop(Class<?> type) {
		return drop(getEntity(type));
	}

	/**
	 * drop a table if exists
	 * 
	 * @param entity entity
	 * @return true if drop successfully
	 */
	public abstract boolean drop(Entity<?> entity);

	/**
	 * drop a table if exists
	 * 
	 * @param table table name
	 * @return true if drop successfully
	 */
	public abstract boolean drop(String table);

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
	 * delete record by the supplied query
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	public int delete(Class<?> type, Query query) {
		return delete(getEntity(type), query);
	}

	/**
	 * delete record by the supplied query
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	public abstract int delete(Entity<?> entity, Query query);

	/**
	 * delete record by the supplied query
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	public abstract int delete(String table, Query query);

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
	 * @param obj the record to be inserted
	 * @return the inserted record
	 */
	public abstract <T> T insert(T obj);

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
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	public abstract int update(Object obj, Query query);

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	public abstract int updateIgnoreNull(Object obj, Query query);

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
	@SuppressWarnings("unchecked")
	protected void queryPrimaryKey(Entity<?> entity, Query query, Object ... keys) {
		if (keys == null || keys.length == 0) {
			return;
		}
		
		if (keys.length == 1 && entity.getType().isInstance(keys[0])) {
			BeanHandler bh = getBeans().getBeanHandler(entity.getType());

			for (EntityField ef : entity.getFields()) {
				if (ef.isPrimaryKey()) {
					Object k = bh.getPropertyValue(keys[0], ef.getName());
					query.equalTo(ef.getName(), k);
				}
			}
		}
		else {
			if (keys.length != entity.getPrimaryKeys().size()) {
				throw new IllegalArgumentException("Illegal primary keys for Entity [" + entity.getType() + "] ");
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
				query.addExclude(ef.getName());
			}
		}
	}

	protected void excludePrimaryKeys(Entity<?> entity, Query query) {
		for (EntityField ef : entity.getFields()) {
			if (ef.isPrimaryKey()) {
				query.addExclude(ef.getName());
			}
		}
	}

	protected void excludeNonPrimaryKeys(Entity<?> entity, Query query) {
		for (EntityField ef : entity.getFields()) {
			if (!ef.isPrimaryKey()) {
				query.addExclude(ef.getName());
			}
		}
	}

	protected void includePrimaryKeys(Entity<?> entity, Query query) {
		for (EntityField ef : entity.getFields()) {
			if (!ef.isPrimaryKey()) {
				query.addInclude(ef.getName());
			}
		}
	}
}
