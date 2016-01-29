package panda.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.dao.entity.Entity;
import panda.dao.entity.EntityDao;
import panda.dao.query.Query;

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

	//--------------------------------------------------------------------
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

	//--------------------------------------------------------------------
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

	//--------------------------------------------------------------------
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
	 * @param query query
	 * @return record
	 */
	boolean exists(Query<?> query);

	//--------------------------------------------------------------------
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
	 * @param query query
	 * @return record
	 */
	<T> T fetch(Query<T> query);

	//--------------------------------------------------------------------
	/**
	 * count all records.
	 * 
	 * @param type record type
	 * @return record count
	 */
	long count(Class<?> type);

	/**
	 * count all records.
	 * 
	 * @param entity entity
	 * @return record count
	 */
	long count(Entity<?> entity);

	/**
	 * count all records.
	 * 
	 * @param table table name
	 * @return record count
	 */
	long count(String table);

	/**
	 * count records by the supplied query.
	 * 
	 * @param query query
	 * @return record count
	 */
	long count(Query<?> query);

	//--------------------------------------------------------------------
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
	 * @param query query, order, offset, limit and filters
	 * @return record list
	 */
	<T> List<T> select(Query<T> query);

	/**
	 * select all records.
	 * 
	 * @param type record type
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	<T> long select(Class<T> type, DataHandler<T> callback);

	/**
	 * select all records.
	 * 
	 * @param entity entity
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	<T> long select(Entity<T> entity, DataHandler<T> callback);

	/**
	 * select all records.
	 * 
	 * @param table table name
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	long select(String table, DataHandler<Map> callback);

	/**
	 * select records by the supplied query.
	 * 
	 * @param query query, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	<T> long select(Query<T> query, DataHandler<T> callback);

	//--------------------------------------------------------------------
	/**
	 * select all records to collection.
	 * 
	 * @param coll the collection to store the value
	 * @param type record type
	 * @param prop The property to be used as the value in the collection.
	 */
	Collection<?> coll(Collection<?> coll, Class<?> type, String prop);

	/**
	 * select all records to collection.
	 * 
	 * @param coll the collection to store the value
	 * @param entity entity
	 * @param prop The property to be used as the value in the collection.
	 */
	Collection<?> coll(Collection<?> coll, Entity<?> entity, String prop);

	/**
	 * select all records to collection.
	 * 
	 * @param coll the collection to store the value
	 * @param table table name
	 * @param prop The property to be used as the value in the collection.
	 */
	Collection<?> coll(Collection<?> coll, String table, String prop);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param coll the collection to store the value
	 * @param query query, order, offset, limit and filters
	 * @param prop The property to be used as the value in the collection.
	 */
	Collection<?> coll(Collection<?> coll, Query<?> query, String prop);

	//--------------------------------------------------------------------
	/**
	 * select all records to list.
	 * 
	 * @param type record type
	 * @param prop The property to be used as the value in the list.
	 * @return record value list
	 */
	List<?> list(Class<?> type, String prop);

	/**
	 * select all records to list.
	 * 
	 * @param entity entity
	 * @param prop The property to be used as the value in the list.
	 * @return record value list
	 */
	List<?> list(Entity<?> entity, String prop);

	/**
	 * select all records to list.
	 * 
	 * @param table table name
	 * @param prop The property to be used as the value in the list.
	 * @return record value list
	 */
	List<?> list(String table, String prop);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query query, order, offset, limit and filters
	 * @param prop The property to be used as the value in the list.
	 * @return record value list
	 */
	List<?> list(Query<?> query, String prop);

	//--------------------------------------------------------------------
	/**
	 * select all records to set.
	 * 
	 * @param type record type
	 * @param prop The property to be used as the value in the set.
	 * @return record value set
	 */
	Set<?> set(Class<?> type, String prop);

	/**
	 * select all records to set.
	 * 
	 * @param entity entity
	 * @param prop The property to be used as the value in the set.
	 * @return record value set
	 */
	Set<?> set(Entity<?> entity, String prop);

	/**
	 * select all records to set.
	 * 
	 * @param table table name
	 * @param prop The property to be used as the value in the set.
	 * @return record value set
	 */
	Set<?> set(String table, String prop);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query query, order, offset, limit and filters
	 * @param prop The property to be used as the value in the set.
	 * @return record value set
	 */
	Set<?> set(Query<?> query, String prop);

	//--------------------------------------------------------------------
	/**
	 * select all records to map.
	 * 
	 * @param type record type
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record map
	 */
	<T> Map<?, T> map(Class<T> type, String keyProp);

	/**
	 * select all records to map.
	 * 
	 * @param entity entity
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record map
	 */
	<T> Map<?, T> map(Entity<T> entity, String keyProp);

	/**
	 * select all records to map.
	 * 
	 * @param table table name
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record(a map) map
	 */
	Map<?, Map> map(String table, String keyProp);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query query, order, offset, limit and filters
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record map
	 */
	<T> Map<?, T> map(Query<T> query, String keyProp);

	//--------------------------------------------------------------------
	/**
	 * select all records to map.
	 * 
	 * @param type record type
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record map
	 */
	Map<?, ?> map(Class<?> type, String keyProp, String valProp);

	/**
	 * select all records to map.
	 * 
	 * @param entity entity
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record map
	 */
	Map<?, ?> map(Entity<?> entity, String keyProp, String valProp);

	/**
	 * select all records to map.
	 * 
	 * @param table table name
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record(a map) map
	 */
	Map<?, ?> map(String table, String keyProp, String valProp);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query query, order, offset, limit and filters
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record map
	 */
	Map<?, ?> map(Query<?> query, String keyProp, String valProp);

	//--------------------------------------------------------------------
	/**
	 * select all records to map.
	 * 
	 * @param type record type
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record map
	 */
	<T> Map<?, List<T>> group(Class<T> type, String keyProp);

	/**
	 * select all records to map.
	 * 
	 * @param entity entity
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record map
	 */
	<T> Map<?, List<T>> group(Entity<T> entity, String keyProp);

	/**
	 * select all records to map.
	 * 
	 * @param table table name
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record(a map) group
	 */
	Map<?, List<Map>> group(String table, String keyProp);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query query, order, offset, limit and filters
	 * @param keyProp The property to be used as the key in the Map.
	 * @return record group
	 */
	<T> Map<?, List<T>> group(Query<T> query, String keyProp);

	//--------------------------------------------------------------------
	/**
	 * select all records to map.
	 * 
	 * @param type record type
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record map
	 */
	Map<?, List<?>> group(Class<?> type, String keyProp, String valProp);

	/**
	 * select all records to map.
	 * 
	 * @param entity entity
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record map
	 */
	Map<?, List<?>> group(Entity<?> entity, String keyProp, String valProp);

	/**
	 * select all records to map.
	 * 
	 * @param table table name
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record(a map) group
	 */
	Map<?, List<?>> group(String table, String keyProp, String valProp);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query query, order, offset, limit and filters
	 * @param keyProp The property to be used as the key in the Map.
	 * @param valProp The property to be used as the value in the Map.
	 * @return record group
	 */
	Map<?, List<?>> group(Query<?> query, String keyProp, String valProp);

	//--------------------------------------------------------------------
	/**
	 * delete a object.
	 * 
	 * @param obj object to be deleted
	 * @return deleted count
	 */
	int delete(Object obj);

	/**
	 * delete records by the supplied keys.
	 * 
	 * @param type record type
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 * @throws IllegalArgumentException if the supplied keys is null
	 */
	int delete(Class<?> type, Object ... keys);

	/**
	 * delete records by the supplied keys.
	 * 
	 * @param entity entity
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 * @throws IllegalArgumentException if the supplied keys is null
	 */
	int delete(Entity<?> entity, Object ... keys);

	/**
	 * delete object collection
	 * 
	 * @param col object collection to be deleted
	 * @return deleted count
	 */
	int deletes(Collection<?> col);

	/**
	 * delete all records.
	 * 
	 * @param type record type
	 * @return deleted count
	 */
	int deletes(Class<?> type);

	/**
	 * delete all records.
	 * 
	 * @param table table name
	 * @return deleted count
	 */
	int deletes(String table);

	/**
	 * delete records by the supplied query
	 * 
	 * @param query query
	 * @return deleted count
	 */
	int deletes(Query<?> query);

	//--------------------------------------------------------------------
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

	//--------------------------------------------------------------------
	/**
	 * insert the object if not exists, 
	 * or update the record by the object.
	 * 
	 * @param obj object
	 * @return the saved record
	 */
	<T> T save(T obj);

	//--------------------------------------------------------------------
	/**
	 * update a record by the supplied object and fields. 
	 * 
	 * @param obj sample object
	 * @param fields the fields to update
	 * @return updated count
	 */
	int update(Object obj, String... fields);

	/**
	 * update a record by the supplied object and fields. 
	 * 
	 * @param obj sample object
	 * @param fields the fields to update
	 * @return updated count
	 */
	int update(Object obj, Collection<String> fields);

	/**
	 * update a record by the supplied object. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @return updated count
	 */
	int updateIgnoreNull(Object obj);

	/**
	 * update records by the supplied object collection and fields. 
	 * 
	 * @param col record collection
	 * @param fields the fields to update
	 * @return updated count
	 */
	int updates(Collection<?> col, String... fields);

	/**
	 * update records by the supplied object collection and fields. 
	 * 
	 * @param col record collection
	 * @param fields the fields to update
	 * @return updated count
	 */
	int updates(Collection<?> col, Collection<String> fields);

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
	int updates(Object obj, Query<?> query);

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	int updatesIgnoreNull(Object obj, Query<?> query);

	//--------------------------------------------------------------------
	/**
	 * execute a transaction
	 */
	void exec(Runnable transaction);
	
	/**
	 * execute a transaction
	 * @param level transaction level
	 */
	void exec(Runnable transaction, int level);
	
	//--------------------------------------------------------------------
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
