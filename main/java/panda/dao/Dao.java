package panda.dao;

import java.util.List;
import java.util.Map;

/**
 * @author yf.frank.wang@gmail.com
 */
public interface Dao {
	/**
	 * @return 数据源的元数据
	 */
	DatabaseMeta meta();

	/**
	 * @return the DaoClient
	 */
	DaoClient getDaoClient();

	/**
	 * @return the DaoSession
	 */
	DaoSession getDaoSession();

	/**
	 * @param type record type
	 * @return 实体描述
	 */
//	<T> Entity<T> getEntity(Class<T> type);

	/**
	 * 根据一个实体的配置信息为其创建一张表
	 * 
	 * @param type 实体类型
	 * @param dropIfExists 如果表存在是否强制移除
	 * @return 实体对象
	 */
//	<T> Entity<T> create(Class<T> type, boolean dropIfExists);

	/**
	 * drop a table if exists
	 * 
	 * @param type record type
	 * @return true if drop successfully
	 */
	boolean drop(Class<?> type);

	/**
	 * drop a table if exists
	 * 
	 * @param table table name
	 * @return true if drop successfully
	 */
	boolean drop(String table);

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
	 * get a record by the supplied keys
	 * 
	 * @param type record type
	 * @param keys record keys (int, string or java bean with keys)
	 */
	<T> T fetch(Class<T> type, Object ... keys);

	/**
	 * get a record by the supplied query
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return record
	 */
	<T> T fetch(Class<T> type, QueryParameter qp);

	/**
	 * get a record by the supplied query
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record
	 */
	Map fetch(String table, QueryParameter query);

	/**
	 * delete a object.
	 * 
	 * @param obj object to be deleted
	 * @return deleted count
	 */
	int delete(Object obj);

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
	 * delete record by the supplied query
	 * 
	 * @param type record type
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	int delete(Class<?> type, QueryParameter query);

	/**
	 * delete record by the supplied query
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	int delete(String table, QueryParameter query);

	/**
	 * insert a record.
	 * <p>
	 * 声明了 '@Id'的字段会在插入数据库时被忽略，因为数据库会自动为其设值。如果想手动设置，请设置 '@Id(auto=false)'
	 * <p>
	 * <b>插入之前</b>，会检查声明了 '@Prep(@SQL("SELECT ..."))' 的字段，预先执行 SQL 为字段设置。
	 * <p>
	 * <b>插入之后</b>，会检查声明了 '@Post(@SQL("SELECT ..."))' 的字段，通过执行 SQL 将值取回
	 * <p>
	 * 如果你的字段仅仅声明了 '@Id(auto=true)'，没有声明 '@Post'，则认为你还是想取回插入后最新的 ID 值，因为 自动为你添加类似
	 * @Post(@SQL("SELECT MAX(id) FROM table")) 的设置
	 * 
	 * @param obj the record to be inserted
	 * @return the inserted record
	 */
	<T> T insert(T obj);

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
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	int update(Object obj, QueryParameter query);

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	int updateIgnoreNull(Object obj, QueryParameter query);

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
	<T> List<T> select(Class<T> type, QueryParameter query);

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record(a map) list
	 */
	List<Map> select(String table, QueryParameter query);

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
	<T> int select(Class<T> type, QueryParameter query, DataHandler<T> callback);

	/**
	 * select records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	int select(String table, QueryParameter query, DataHandler<Map> callback);

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
	int count(Class<?> type, QueryParameter query);

	/**
	 * count records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record count
	 */
	int count(String table, QueryParameter query);

}
