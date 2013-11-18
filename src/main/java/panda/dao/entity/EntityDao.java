package panda.dao.entity;

import java.util.Collection;
import java.util.List;

import panda.dao.Dao;
import panda.dao.DaoClient;
import panda.dao.DataHandler;
import panda.dao.DatabaseMeta;
import panda.dao.Transaction;
import panda.dao.criteria.QueryWrapper;
import panda.dao.criteria.Query;
import panda.lang.Texts;



/**
 * @param <T> entity type
 * @author yf.frank.wang@gmail.com
 */
public class EntityDao<T> {
	//--------------------------------------------------------------
	private final Dao dao;
	private final Entity<T> entity;

	/**
	 * @param dao dao
	 * @param type entity type
	 */
	public EntityDao(Dao dao, Class<T> type) {
		this(dao, type, null);
	}
	
	/**
	 * @param dao dao
	 * @param type entity type
	 * @param param parameter used for dynamic table
	 */
	public EntityDao(Dao dao, Class<T> type, Object param) {
		this.dao = dao;
		Entity<T> en = dao.getEntity(type);
		if (param != null && isNeedCopyEntity(en)) {
			Entity<T> sen = new Entity<T>(en);
			sen.setTableName(Texts.transform(sen.getTableName(), param));
			sen.setViewName(Texts.transform(sen.getViewName(), param));
			for (EntityField ef : en.getFields()) {
				if (ef.hasDefaultValue()) {
					ef.setDefaultValue(Texts.transform(ef.getDefaultValue(), param));
				}
			}
			this.entity = sen;
		}
		else {
			this.entity = en;
		}
	}

	private boolean isNeedCopyEntity(Entity<?> en) {
		if (en.getTableName().indexOf('$') >= 0) {
			return true;
		}
		if (en.getViewName().indexOf('$') >= 0) {
			return true;
		}
		
		for (EntityField ef : en.getFields()) {
			if (ef.hasDefaultValue() && ef.getDefaultValue().indexOf('$') >= 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the DaoClient
	 */
	public DaoClient getDaoClient() {
		return dao.getDaoClient();
	}

	/**
	 * @return datease meta
	 */
	public DatabaseMeta meta() {
		return dao.meta();
	}

	public Dao getDao() {
		return dao;
	}

	public Entity<T> getEntity() {
		return entity;
	}

	/**
	 * drop a table if exists
	 */
	public void drop() {
		dao.drop(entity);
	}

	/**
	 * create table
	 */
	public void create() {
		dao.create(entity);
	}

	/**
	 * check a table exists in the data store.
	 * 
	 * @return true if the record or the table exists in the data store
	 */
	public boolean exists() {
		return dao.exists(entity);
	}

	/**
	 * check a record exists in the data store.
	 * if the keys is not supplied, then check the table existence.
	 * 
	 * @param keys record keys (int, string or java bean with keys)
	 * @return true if the record or the table exists in the data store
	 */
	public boolean exists(Object ... keys) {
		return dao.exists(entity, keys);
	}

	/**
	 * get a record by the supplied keys
	 * 
	 * @param keys record keys (int, string or java bean with keys)
	 */
	public T fetch(Object ... keys) {
		return dao.fetch(entity, keys);
	}

	/**
	 * get a record by the supplied query
	 * 
	 * @param query WHERE conditions
	 * @return record
	 */
	public T fetch(QueryWrapper query) {
		return dao.fetch(entity, query);
	}

	/**
	 * get a record by the supplied query
	 * 
	 * @param query WHERE conditions
	 * @return record
	 */
	public T fetch(Query query) {
		return dao.fetch(entity, query);
	}

	/**
	 * delete a object.
	 * 
	 * @param obj object to be deleted
	 * @return deleted count
	 */
	public int delete(T obj) {
		return dao.delete(obj);
	}

	/**
	 * delete object collection
	 * 
	 * @param col object collection to be deleted
	 * @return deleted count
	 */
	public int deletes(Collection<T> col) {
		return dao.deletes(col);
	}

	/**
	 * delete records by the supplied keys.
	 * if the supplied keys is null, all records will be deleted.
	 * 
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 */
	public int delete(Object ... keys) {
		return dao.delete(keys);
	}

	/**
	 * delete records by the supplied query
	 * 
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	public int deletes(QueryWrapper query) {
		return dao.deletes(entity, query);
	}

	/**
	 * delete records by the supplied query
	 * 
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	public int deletes(Query query) {
		return dao.deletes(entity, query);
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
	 * @param obj the record to be inserted
	 * @return the inserted record
	 */
	public T insert(T obj) {
		return dao.insert(obj);
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
	@SuppressWarnings("unchecked")
	public Collection<T> inserts(Collection<T> col) {
		return (Collection<T>)dao.inserts(col);
	}

	/**
	 * update a record by the supplied object. 
	 * 
	 * @param obj sample object
	 * @return updated count
	 */
	public int update(T obj) {
		return dao.update(obj);
	}

	/**
	 * update records by the supplied object collection. 
	 * 
	 * @param col record collection
	 * @return updated count
	 */
	public int updates(Collection<T> col) {
		return dao.updates(col);
	}

	/**
	 * update a record by the supplied object. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @return updated count
	 */
	public int updateIgnoreNull(T obj) {
		return dao.updateIgnoreNull(obj);
	}

	/**
	 * update records by the supplied object collection. 
	 * 
	 * @param col record collection
	 * @return updated count
	 */
	public int updatesIgnoreNull(Collection<T> col) {
		return dao.updatesIgnoreNull(col);
	}

	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	public int updates(T obj, QueryWrapper query) {
		return dao.updates(obj, query);
	}

	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	public int updates(T obj, Query query) {
		return dao.updates(obj, query);
	}

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	public int updatesIgnoreNull(T obj, QueryWrapper query) {
		return dao.updatesIgnoreNull(obj, query);
	}

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	public int updatesIgnoreNull(T obj, Query query) {
		return dao.updatesIgnoreNull(obj, query);
	}

	/**
	 * select all records.
	 * 
	 * @return record list
	 */
	public List<T> select() {
		return dao.select(entity);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record list
	 */
	public List<T> select(QueryWrapper query) {
		return dao.select(entity, query);
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record list
	 */
	public List<T> select(Query query) {
		return dao.select(entity, query);
	}

	/**
	 * select all records.
	 * 
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	public int select(DataHandler<T> callback) {
		return dao.select(entity, callback);
	}

	/**
	 * select records by the supplied query.
	 * 
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	public int select(QueryWrapper query, DataHandler<T> callback) {
		return dao.select(entity, query, callback);
	}

	/**
	 * select records by the supplied query.
	 * 
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	public int select(Query query, DataHandler<T> callback) {
		return dao.select(entity, query, callback);
	}

	/**
	 * count all records.
	 * 
	 * @return record count
	 */
	public int count() {
		return dao.count(entity);
	}

	/**
	 * count records by the supplied query.
	 * 
	 * @param query WHERE conditions
	 * @return record count
	 */
	public int count(QueryWrapper query) {
		return dao.count(entity, query);
	}

	/**
	 * count records by the supplied query.
	 * 
	 * @param query WHERE conditions
	 * @return record count
	 */
	public int count(Query query) {
		return dao.count(entity, query);
	}

	/**
	 * execute a transaction
	 */
	public void exec(Transaction transaction) {
		dao.exec(transaction);
	}
	/**
	 * execute a transaction
	 * @param level transaction level
	 */
	public void exec(Transaction transaction, int level) {
		dao.exec(transaction, level);
	}
	
	/**
	 * commit a transaction
	 */
	public void commit() {
		dao.commit();
	}

	/**
	 * rollback a transaction
	 */
	public void rollback() {
		dao.rollback();
	}

	/**
	 * check the identity value
	 * @param id identity
	 * @return true if id != null && id != 0
	 */
	public boolean isValidIdentity(Object id) {
		return dao.isValidIdentity(id);
	}
}
