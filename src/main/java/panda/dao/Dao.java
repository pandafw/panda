package panda.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import panda.dao.criteria.Query;
import panda.dao.criteria.QueryWrapper;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;

/**
 * !! thread-unsafe !!
 * @author yf.frank.wang@gmail.com
 */
public interface Dao {
	/**
	 * @return the DaoClient
	 */
	DaoClient getDaoClient();
	
	/**
	 * @return database meta
	 */
	DatabaseMeta meta();

	/**
	 * @param type record type
	 * @return the entity
	 */
	<T> Entity<T> getEntity(Class<T> type);

	/**
	 * @param type record type
	 * @return the entity
	 */
	<T> EntityDao<T> getEntityDao(Class<T> type);

	/**
	 * @param type record type
	 * @param param argument used for dynamic table
	 * @return the entity
	 */
	<T> EntityDao<T> getEntityDao(Class<T> type, Object param);

	/**
	 * drop a table if exists
	 * 
	 * @param type record type
	 */
	void drop(Class<?> type);

	/**
	 * drop a table if exists
	 * 
	 * @param entity entity
	 */
	void drop(Entity<?> entity);

	/**
	 * drop a table if exists
	 * 
	 * @param table table name
	 */
	void drop(String table);

	/**
	 * create table
	 * 
	 * @param type record type
	 */
	void create(Class<?> type);

	/**
	 * create table
	 * 
	 * @param entity entity
	 */
	void create(Entity<?> entity);

	/**
	 * check a table exists in the data store.
	 * 
	 * @param table table name
	 * @return true if the record or the table exists in the data store
	 */
	boolean exists(String table);

	/**
	 * check a record exists in the data store.
	 * if the keys is not supplied, then check the table existence.
	 * 
	 * @param type record type
	 * @param keys record keys (int, string or java bean with keys)
	 * @return true if the record or the table exists in the data store
	 */
	boolean exists(Class<?> type, Object ... keys);

	/**
	 * check a record exists in the data store.
	 * if the keys is not supplied, then check the table existence.
	 * 
	 * @param entity entity
	 * @param keys record keys (int, string or java bean with keys)
	 * @return true if the record or the table exists in the data store
	 */
	boolean exists(Entity<?> entity, Object ... keys);

	/**
	 * check a record exists in the data store.
	 * if the query is not supplied, then check the table existence.
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return record
	 */
	boolean exists(Class<?> type, QueryWrapper query);

	/**
	 * check a record exists in the data store.
	 * if the query is not supplied, then check the table existence.
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return record
	 */
	boolean exists(Class<?> type, Query query);

	/**
	 * check a record exists in the data store.
	 * if the query is not supplied, then check the table existence.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return true if the record or the table exists in the data store
	 */
	boolean exists(Entity<?> entity, QueryWrapper query);

	/**
	 * check a record exists in the data store.
	 * if the query is not supplied, then check the table existence.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return true if the record or the table exists in the data store
	 */
	boolean exists(Entity<?> entity, Query query);

	/**
	 * get a record by the supplied keys
	 * 
	 * @param type record type
	 * @param keys record keys (int, string or java bean with keys)
	 */
	<T> T fetch(Class<T> type, Object ... keys);

	/**
	 * get a record by the supplied keys
	 * 
	 * @param entity entity
	 * @param keys record keys (int, string or java bean with keys)
	 */
	<T> T fetch(Entity<T> entity, Object ... keys);

	/**
	 * get a record by the supplied query
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return record
	 */
	<T> T fetch(Class<T> type, QueryWrapper query);

	/**
	 * get a record by the supplied query
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return record
	 */
	<T> T fetch(Class<T> type, Query query);

	/**
	 * get a record by the supplied query
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record
	 */
	<T> T fetch(Entity<T> entity, QueryWrapper query);

	/**
	 * get a record by the supplied query
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record
	 */
	<T> T fetch(Entity<T> entity, Query query);

	/**
	 * get a record by the supplied query
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record
	 */
	Map fetch(String table, QueryWrapper query);
	
	/**
	 * get a record by the supplied query
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record
	 */
	Map fetch(String table, Query query);

	/**
	 * delete a object.
	 * 
	 * @param obj object to be deleted
	 * @return deleted count
	 */
	int delete(Object obj);

	/**
	 * delete all records.
	 * 
	 * @param type record type
	 * @return deleted count
	 */
	<T> int deletes(Class<T> type);

	/**
	 * delete records by the supplied keys.
	 * if the supplied keys is null, all records will be deleted.
	 * 
	 * @param type record type
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 */
	<T> int delete(Class<T> type, Object ... keys);

	/**
	 * delete records by the supplied keys.
	 * if the supplied keys is null, all records will be deleted.
	 * 
	 * @param entity entity
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 */
	<T> int delete(Entity<T> entity, Object ... keys);

	/**
	 * delete object collection
	 * 
	 * @param col object collection to be deleted
	 * @return deleted count
	 */
	int deletes(Collection<?> col);

	/**
	 * delete records by the supplied query
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	int deletes(Class<?> type, QueryWrapper query);

	/**
	 * delete records by the supplied query
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	int deletes(Class<?> type, Query query);

	/**
	 * delete all records.
	 * 
	 * @param entity entity
	 * @return deleted count
	 */
	int deletes(Entity<?> entity);

	/**
	 * delete records by the supplied query.
	 * if query is empty, all records will be deleted.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	int deletes(Entity<?> entity, QueryWrapper query);

	/**
	 * delete records by the supplied query.
	 * if query is empty, all records will be deleted.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	int deletes(Entity<?> entity, Query query);

	/**
	 * delete all records.
	 * 
	 * @param table table name
	 * @return deleted count
	 */
	int deletes(String table);

	/**
	 * delete records by the supplied query.
	 * if query is empty, all records will be deleted.
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	int deletes(String table, Query query);

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
	<T> T insert(T obj);

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
	Collection<?> inserts(Collection<?> col);

	/**
	 * insert the object if not exists, 
	 * or update the record by the object.
	 * 
	 * @param obj object
	 * @return the saved record
	 */
	<T> T save(T obj);

	/**
	 * update a record by the supplied object. 
	 * 
	 * @param obj sample object
	 * @return updated count
	 */
	int update(Object obj);

	/**
	 * update a record by the supplied object. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @return updated count
	 */
	int updateIgnoreNull(Object obj);

	/**
	 * update records by the supplied object collection. 
	 * 
	 * @param col record collection
	 * @return updated count
	 */
	int updates(Collection<?> col);

	/**
	 * update records by the supplied object collection. 
	 * 
	 * @param col record collection
	 * @return updated count
	 */
	int updatesIgnoreNull(Collection<?> col);

	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	int updates(Object obj, QueryWrapper query);

	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	int updates(Object obj, Query query);

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	int updatesIgnoreNull(Object obj, QueryWrapper query);

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	int updatesIgnoreNull(Object obj, Query query);

	/**
	 * select all records.
	 * 
	 * @param type record type
	 * @return record list
	 */
	<T> List<T> select(Class<T> type);

	/**
	 * select all records.
	 * 
	 * @param entity entity
	 * @return record list
	 */
	<T> List<T> select(Entity<T> entity);

	/**
	 * select all records.
	 * 
	 * @param table table name
	 * @return record(a map) list
	 */
	List<Map> select(String table);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param type record type
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record list
	 */
	<T> List<T> select(Class<T> type, QueryWrapper query);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param type record type
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record list
	 */
	<T> List<T> select(Class<T> type, Query query);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record list
	 */
	<T> List<T> select(Entity<T> entity, QueryWrapper query);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record list
	 */
	<T> List<T> select(Entity<T> entity, Query query);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record(a map) list
	 */
	List<Map> select(String table, QueryWrapper query);
	
	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record(a map) list
	 */
	List<Map> select(String table, Query query);

	/**
	 * select all records.
	 * 
	 * @param type record type
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	<T> int select(Class<T> type, DataHandler<T> callback);

	/**
	 * select all records.
	 * 
	 * @param entity entity
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	<T> int select(Entity<T> entity, DataHandler<T> callback);

	/**
	 * select all records.
	 * 
	 * @param table table name
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	int select(String table, DataHandler<Map> callback);

	/**
	 * select records by the supplied query.
	 * 
	 * @param type record type
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	<T> int select(Class<T> type, QueryWrapper query, DataHandler<T> callback);

	/**
	 * select records by the supplied query.
	 * 
	 * @param type record type
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	<T> int select(Class<T> type, Query query, DataHandler<T> callback);

	/**
	 * select records by the supplied query.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	<T> int select(Entity<T> entity, QueryWrapper query, DataHandler<T> callback);

	/**
	 * select records by the supplied query.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	<T> int select(Entity<T> entity, Query query, DataHandler<T> callback);

	/**
	 * select records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	int select(String table, QueryWrapper query, DataHandler<Map> callback);

	/**
	 * select records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	int select(String table, Query query, DataHandler<Map> callback);

	/**
	 * count all records.
	 * 
	 * @param type record type
	 * @return record count
	 */
	int count(Class<?> type);

	/**
	 * count all records.
	 * 
	 * @param entity entity
	 * @return record count
	 */
	int count(Entity<?> entity);

	/**
	 * count all records.
	 * 
	 * @param table table name
	 * @return record count
	 */
	int count(String table);

	/**
	 * count records by the supplied query.
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return record count
	 */
	int count(Class<?> type, QueryWrapper query);

	/**
	 * count records by the supplied query.
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return record count
	 */
	int count(Class<?> type, Query query);

	/**
	 * count records by the supplied query.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record count
	 */
	int count(Entity<?> entity, QueryWrapper query);

	/**
	 * count records by the supplied query.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record count
	 */
	int count(Entity<?> entity, Query query);

	/**
	 * count records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record count
	 */
	int count(String table, QueryWrapper query);

	/**
	 * count records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record count
	 */
	int count(String table, Query query);

	/**
	 * execute a transaction
	 */
	void exec(Transaction transaction);
	
	/**
	 * execute a transaction
	 * @param level transaction level
	 */
	void exec(Transaction transaction, int level);
	
	/**
	 * commit a transaction
	 */
	void commit();

	/**
	 * rollback a transaction
	 */
	void rollback();

	/**
	 * check the identity value
	 * @param id identity
	 * @return true if id != null && id != 0
	 */
	boolean isValidIdentity(Object id);
}
