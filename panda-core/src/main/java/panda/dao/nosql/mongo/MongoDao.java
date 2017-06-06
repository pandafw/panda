package panda.dao.nosql.mongo;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

import panda.dao.AbstractDao;
import panda.dao.DaoException;
import panda.dao.DataHandler;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.entity.EntityIndex;
import panda.dao.query.Filter.ComboFilter;
import panda.dao.query.Filter.ReferFilter;
import panda.dao.query.Filter.SimpleFilter;
import panda.dao.query.Filter.ValueFilter;
import panda.dao.query.GenericQuery;
import panda.dao.query.Operator;
import panda.dao.query.Order;
import panda.dao.query.Query;
import panda.io.Streams;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.reflect.Types;
import panda.log.Log;
import panda.log.Logs;

public class MongoDao extends AbstractDao {
	private static final Log log = Logs.getLog(MongoDao.class);
	
	private static final String ID = "_id";

	/**
	 * auto start count
	 */
	private int autoCount;
	
	private DB db;
	
	public MongoDao(MongoDaoClient daoClient, DB db) {
		super(daoClient);
		this.db = db;
	}

	//---------------------------------------------------------------------
	protected MongoDaoClient getMongoDaoClient() {
		return (MongoDaoClient)getDaoClient();
	}

	//---------------------------------------------------------------------
	private boolean isTransactionSupport() {
		return getMongoDaoClient().isTransactionSupport();
	}
	
	@Override
	protected void autoStart() {
		if (autoCount == 0) {
			start();
		}

		autoCount++;
	}

	@Override
	protected void autoCommit() {
		if (autoCount == 1) {
			commit();
		}
	}

	@Override
	protected void autoClose() {
		autoCount--;
	}

	//--------------------------------------------------------------------
	/**
	 * start a transaction
	 */
	private void start() {
		if (isTransactionSupport()) {
			try {
				BasicDBObject dbo = new BasicDBObject("beginTransaction", true).append("isolation", "mvcc");
				db.command(dbo);
			}
			catch (Exception e) {
				throw new DaoException("Failed to start transaction", e);
			}
		}
	}

	/**
	 * commit a transaction
	 */
	@Override
	public void commit() {
		if (isTransactionSupport()) {
			try {
				db.command("commitTransaction");
			}
			catch (Exception e) {
				throw new DaoException("Failed to commit transaction", e);
			}
		}
	}
	
	/**
	 * rollback a transaction
	 */
	@Override
	public void rollback() {
		if (isTransactionSupport()) {
			try {
				db.command("rollbackTransaction");
			}
			catch (Exception e) {
				throw new DaoException("Failed to rollback transaction", e);
			}
		}
	}

	//--------------------------------------------------------------------
	/**
	 * execute a transaction
	 */
	@Override
	public void exec(Runnable transaction) {
		exec(transaction, 0);
	}
	
	/**
	 * execute a transaction
	 * @param level transaction level
	 */
	@Override
	public void exec(Runnable transaction, int level) {
		assertTransaction(transaction);

		try {
			autoStart();
			transaction.run();
			autoCommit();
		}
		catch (Throwable e) {
			rollback();
			throw Exceptions.wrapThrow(e);
		}
		finally {
			autoClose();
		}
	}
	//-----------------------------------------------------------------------------
	/**
	 * drop a table if exists
	 * 
	 * @param entity entity
	 */
	@Override
	public void drop(Entity<?> entity) {
		drop(getTableName(entity));
	}

	/**
	 * drop a table if exists
	 * 
	 * @param table table name
	 */
	@Override
	public void drop(String table) {
		db.getCollection(table).drop();
	}

	//--------------------------------------------------------------------
	/**
	 * create table
	 * 
	 * @param entity entity
	 */
	@Override
	public void create(Entity<?> entity) {
		if (log.isDebugEnabled()) {
			log.debug("create: " + getTableName(entity));
		}
		
		autoStart();
		try {
			// create collection
			BasicDBObject dbo = new BasicDBObject();
			if (Collections.isNotEmpty(entity.getOptions())) {
				for (Entry<String, Object> en : entity.getOptions().entrySet()) {
					dbo.put(en.getKey(), en.getValue());
				}
			}
			db.createCollection(getTableName(entity), dbo);
			
			// create indexes
			Collection<EntityIndex> indexs = entity.getIndexes();
			if (Collections.isNotEmpty(indexs)) {
				DBCollection dbc = db.getCollection(getTableName(entity));
				for (EntityIndex index : indexs) {
					if (!index.isReal()) {
						continue;
					}

					BasicDBObject opts = new BasicDBObject();
					if (index.isUnique()) {
						opts.append("unique", true);
					}
					opts.append("sparse", true);
					opts.append("name", (index.isUnique() ? "ux_" : "ix_") + index.getName());

					BasicDBObject keys = new BasicDBObject();
					for (EntityField ef : index.getFields()) {
						keys.append(ef.getColumn(), 1);
					}
					
					dbc.createIndex(keys, opts);
				}
			}
		}
		catch (Exception e) {
			throw new DaoException("Failed to create index for table " + getTableName(entity), e);
		}
		finally {
			autoClose();
		}
	}
	
	/**
	 * create table ddl
	 * 
	 * @param entity entity
	 */
	@Override
	public String ddl(Entity<?> entity) {
		log.info("ddl(entity) method is unsupported for Mongo database.");
		return Strings.EMPTY;
	}

	//--------------------------------------------------------------------
	/**
	 * check a table exists in the data store.
	 * 
	 * @param table table name
	 * @return true if the record or the table exists in the data store
	 */
	@Override
	protected boolean existsByTable(String table) {
		return true;
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
	protected boolean existsByKeys(Entity<?> entity, Object ... keys) {
		assertEntity(entity);

		if (keys == null || keys.length == 0) {
			return existsByTable(getTableName(entity));
		}

		GenericQuery<?> query = createQuery(entity);
		query.setLimit(1);
		queryPrimaryKey(query, keys);
		
		return existsByQuery(query);
	}

	/**
	 * check a record exists in the data store.
	 * if the query is not supplied, then check the table existence.
	 * 
	 * @param query query
	 * @return true if the record or the table exists in the data store
	 */
	@Override
	protected boolean existsByQuery(GenericQuery<?> query) {
		if (!query.hasFilters()) {
			return existsByTable(getTableName(query));
		}

		if (log.isDebugEnabled()) {
			log.debug("existsByQuery: " + query);
		}
		
		DBCollection dbc = db.getCollection(getTableName(query));
		DBObject dbq = where(query);
		DBObject dbo = dbc.findOne(dbq, new BasicDBObject().append(ID, 1));
		return dbo != null;
	}

	//--------------------------------------------------------------------
	/**
	 * get a record by the supplied query
	 * 
	 * @param query query
	 * @return record
	 */
	@Override
	protected <T> T fetchByQuery(GenericQuery<T> query) {
		if (log.isDebugEnabled()) {
			log.debug("fetchByQuery: " + query);
		}
		
		DBCursor cursor = null;
		
		autoStart();
		try {
			cursor = createCursor(query);
			
			if (cursor.hasNext()) {
				DBObject dbo = cursor.next();
				T data = convertBsonToData(query, dbo);
				return data;
			}
			return null;
		}
		catch (Exception e) {
			throw new DaoException("Failed to select entity " + getTableName(query) + ": " + query, e);
		}
		finally {
			Streams.safeClose(cursor);
			autoClose();
		}
	}

	//--------------------------------------------------------------------
	/**
	 * count records by the supplied query.
	 * 
	 * @param query query
	 * @return record count
	 */
	@Override
	protected long countByQuery(Query<?> query) {
		if (log.isDebugEnabled()) {
			log.debug("countByQuery: " + query);
		}
		
		autoStart();
		try {
			DBObject dbq = where(query);
			DBCollection dbc = db.getCollection(getTableName(query));
			long cnt = dbc.count(dbq);
			return cnt;
		}
		catch (Exception e) {
			throw new DaoException("Failed to count entity " + getTableName(query) + ": " + query, e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	* @param query query
	 * @return record list
	 */
	@Override
	protected <T> List<T> selectByQuery(GenericQuery<T> query) {
		if (log.isDebugEnabled()) {
			log.debug("selectByQuery: " + query);
		}
		
		DBCursor cursor = null;

		autoStart();
		try {
			cursor = createCursor(query);
			
			List<T> list = new ArrayList<T>();
			while (cursor.hasNext()) {
				DBObject dbo = cursor.next();
				T data = convertBsonToData(query, dbo);
				list.add(data);
			}
			return list;
		}
		catch (Exception e) {
			throw new DaoException("Failed to select entity " + getTableName(query) + ": " + query, e);
		}
		finally {
			Streams.safeClose(cursor);
			autoClose();
		}
	}

	/**
	 * select records by the supplied query.
	 * 
	* @param query query
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	@Override
	protected <T> long selectByQuery(GenericQuery <T>query, DataHandler<T> callback) {
		if (log.isDebugEnabled()) {
			log.debug("selectByQuery: " + query);
		}
		
		DBCursor cursor = null;
		
		autoStart();
		try {
			cursor = createCursor(query);

			long count = 0;
			while (cursor.hasNext()) {
				DBObject dbo = cursor.next();
				T data = convertBsonToData(query, dbo);
				if (!callback(callback, data, count++)) {
					break;
				}
			}
			return count;
		}
		catch (Exception e) {
			throw new DaoException("Failed to select entity " + getTableName(query) + ": " + query, e);
		}
		finally {
			Streams.safeClose(cursor);
			autoClose();
		}
	}

	//--------------------------------------------------------------------
	/**
	 * delete record by the supplied query
	 * 
	 * @param query query
	 * @return deleted count
	 */
	@Override
	protected int deletesByQuery(Query<?> query) {
		if (log.isDebugEnabled()) {
			log.debug("deletesByQuery: " + query);
		}
		
		autoStart();
		try {
			DBObject dbq = where(query);
			
			DBCollection dbc = db.getCollection(getTableName(query));
			WriteResult wr = dbc.remove(dbq);
			autoCommit();
			return wr.getN();
		}
		catch (Exception e) {
			rollback();
			throw new DaoException("Failed to delete entity " + getTableName(query) + ": " + query, e);
		}
		finally {
			autoClose();
		}
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
	 * @param entity the Entity of the data
	 * @param data the record to be inserted (@Id property will be setted)
	 * @return the inserted record
	 */
	@Override
	protected <T> T insertData(Entity<T> entity, T data) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("insert: " + data);
		}
		
		EntityField idef = entity.getIdentity();
		if (idef == null) {
			writeData(entity, data);
			return data;
		}

		Object oid = idef.getValue(data);
		if (isValidIdentity(oid)) {
			writeData(entity, data);
			return data;
		}

		DBCollection dbc = db.getCollection(getTableName(entity));
		DBObject dbo = convertDataToBson(entity, data);

		for (int i = 0; i < 100; i++) {
			Object gid;
			
			if (idef.isNumberIdentity()) {
				gid = 1L;
				DBObject o = dbc.findOne(new BasicDBObject(), new BasicDBObject().append(ID, 1), new BasicDBObject().append(ID, -1));
				if (o != null) {
					Object id = o.get(ID);
					gid = (Long)cast(id, long.class) + 1;
				}
			}
			else {
				gid = ObjectId.get();
			}

			if (!idef.setValue(data, cast(gid, idef.getType()))) {
				throw new DaoException("Failed to set identity to entity: " + entity.getType());
			}
			
			dbo.put(ID, gid);
			try {
				dbc.insert(dbo);
				return data;
			}
			catch (MongoException me) {
				if (me.getCode() == 11000) {
					// duplicate key
					continue;
				}
				throw me;
			}
		}

		throw new DaoException("Failed to insert " + entity.getType() + " for too many duplicated id failure");
	}

	//--------------------------------------------------------------------
	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	protected int updatesByQuery(Object obj, GenericQuery<?> query, int limit) {
		if (log.isDebugEnabled()) {
			log.debug("updatesByQuery: " + query);
		}
		
		excludePrimaryKeys(query);

		DBObject dbq = where(query);
		DBObject dbo = convertDataToBson(query, obj);
		DBObject dbs = new BasicDBObject().append("$set", dbo);
		
		autoStart();
		try {
			DBCollection dbc = db.getCollection(getTableName(query));
			WriteResult wr = dbc.updateMulti(dbq, dbs);
			int cnt = wr.getN();
			if (cnt > limit) {
				throw new Exception("Too many (" + cnt + ") records updated.");
			}
			autoCommit();
			return cnt;
		}
		catch (Exception e) {
			rollback();
			throw new DaoException("Failed to update entity " + getTableName(query) + ": " + query, e);
		}
		finally {
			autoClose();
		}
	}

	//--------------------------------------------------------------------
	private DBObject field(GenericQuery<?> query) {
		Entity<?> entity = query.getEntity();
		if (entity != null) {
			BasicDBObject dbo = new BasicDBObject();

			for (EntityField ef : entity.getFields()) {
				if (query.shouldExclude(ef.getName())) {
					continue;
				}
				
				String col = query.getColumn(ef.getName());
				if (Strings.isEmpty(col)) {
					col = ef.getColumn();
					// skip join column which has not joined
					if (Strings.isEmpty(col)) {
						continue;
					}
				}
				
				dbo.append(col, 1);
			}
			if (dbo.isEmpty()) {
				throw new IllegalArgumentException("Nothing to SELECT!");
			}
			
			return dbo;
		}

		if (!query.hasColumns()) {
			return null;
		}
			
		BasicDBObject dbo = new BasicDBObject();
		for (Entry<String, String> en : query.getColumns().entrySet()) {
			String col = en.getValue();
			if (col == null) {
				dbo.append(en.getKey(), 0);
				continue;
			}
			
			if (col.length() == 0) {
				col = en.getKey();
			}
			
			dbo.append(col, 1);
		}
		return dbo;
	}
	
	private DBCursor createCursor(GenericQuery<?> query) {
		DBCollection dbc = db.getCollection(getTableName(query));
		
		DBObject dbq = where(query);
		DBObject dbf = field(query);
		DBObject dbo = order(query);

		DBCursor cursor = dbc.find(dbq, dbf);
		cursor.sort(dbo);
		
		if (query.getStart() > 0) {
			cursor.skip((int)query.getStart());
		}
		if (query.getLimit() > 0) {
			cursor.limit((int)query.getLimit());
		}
		
		return cursor;
	}
	
	//-----------------------------------------------------------------------------
	private EntityField getEntityField(Entity<?> entity, String field, String name) {
		EntityField ef = entity.getField(field);
		if (ef == null) {
			throw new IllegalArgumentException("invalid " + name + " field '" + field + "' of entity " + entity.getType());
		}
		return ef;
	}
	
	private BasicDBObject where(Query<?> query) {
		BasicDBObject dbq = new BasicDBObject();
		if (query.hasFilters()) {
			ComboFilter cf = query.getFilters();
			Object w = where(query.getEntity(), cf);
			dbq.append("$" + cf.getLogical().toString().toLowerCase(), w);
		}
		return dbq;
	}

	private BasicDBList where(Entity<?> entity, ComboFilter cf) {
		BasicDBList qs = new BasicDBList();
		for (Object f : cf.getFilters()) {
			if (f instanceof ValueFilter) {
				ValueFilter vf = (ValueFilter)f;
				if (entity == null) {
					whereValueFilter(qs, null, vf.getField(), vf);
				}
				else {
					EntityField ef = getEntityField(entity, vf.getField(), "where");
					whereValueFilter(qs, ef, ef.getColumn(), vf);
				}
			}
			else if (f instanceof ReferFilter) {
				throw Exceptions.unsupported("Field compare is not supported by MongoDB.");
			}
			else if (f instanceof SimpleFilter) {
				SimpleFilter sf = (SimpleFilter)f;
				if (entity == null) {
					whereSimpleFilter(qs, sf.getField(), sf);
				}
				else {
					EntityField ef = getEntityField(entity, sf.getField(), "simple");
					whereSimpleFilter(qs, ef.getColumn(), sf);
				}
			}
			else if (f instanceof ComboFilter) {
				ComboFilter ccf = (ComboFilter)f;
				Object w = where(entity, ccf);
				qs.add(new BasicDBObject().append("$" + ccf.getLogical().toString().toLowerCase(), w));
			}
		}
		return qs;
	}
	
	private void whereSimpleFilter(BasicDBList qs, String column, SimpleFilter sf) {
		Operator op = sf.getOperator();
		if (op == Operator.IS_NULL) {
			qs.add(new BasicDBObject(column, new BasicDBObject("$exists", false)));
			return;
		}
		
		if (op == Operator.IS_NOT_NULL) {
			qs.add(new BasicDBObject(column, new BasicDBObject("$exists", true)));
			return;
		}
		
		throw Exceptions.unsupported("Operator " + op + " is not supported by MongoDB.");
	}
	
	private void whereValueFilter(BasicDBList qs, EntityField ef, String column, ValueFilter vf) {
		if (ef != null && ef.isIdentity()) {
			column = ID;
		}
		
		Operator op = vf.getOperator();
		if (op == Operator.BETWEEN) {
			Object v0 = convertValueToBson(ef, vf.getValue(0));
			Object v1 = convertValueToBson(ef, vf.getValue(1));
			qs.add(new BasicDBObject(column, new BasicDBObject("$gte", v0).append("$lte", v1)));
			return;
		}
		
		if (op == Operator.NOT_BETWEEN) {
			Object v0 = convertValueToBson(ef, vf.getValue(0));
			Object v1 = convertValueToBson(ef, vf.getValue(1));
			BasicDBList nb = new BasicDBList();
			nb.add(new BasicDBObject(column, new BasicDBObject("$lt", v0)));
			nb.add(new BasicDBObject(column, new BasicDBObject("$gt", v1)));
			qs.add(new BasicDBObject("$or", nb));
			return;
		}
		
		if (op == Operator.EQUAL) {
			Object v = convertValueToBson(ef, vf.getValue());
			qs.add(new BasicDBObject(column, v));
			return;
		}
		
		if (op == Operator.NOT_BETWEEN) {
			Object v0 = convertValueToBson(ef, vf.getValue(0));
			Object v1 = convertValueToBson(ef, vf.getValue(1));
			qs.add(new BasicDBObject(column, new BasicDBObject("$lt", v0).append("$gt", v1)));
			return;
		}
		
		if (op == Operator.LIKE) {
			// FIXME: escape value
			String reg = Strings.replace(Strings.replace(vf.getValue().toString(), "%", ".*"), "_", "?*");
			Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
			qs.add(new BasicDBObject(column, p));
			return;
		}
		
		if (op == Operator.NOT_LIKE) {
			// FIXME: escape value
			String reg = Strings.replace(Strings.replace(vf.getValue().toString(), "%", ".*"), "_", "?*");
			Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
			qs.add(new BasicDBObject(column, new BasicDBObject("$not", p)));
			return;
		}
		
		if (op == Operator.MATCH) {
			// FIXME: escape value
			Pattern p = Pattern.compile(".*" + vf.getValue() + ".*", Pattern.CASE_INSENSITIVE);
			qs.add(new BasicDBObject(column, p));
			return;
		}
		
		if (op == Operator.NOT_MATCH) {
			// FIXME: escape value
			Pattern p = Pattern.compile(".*" + vf.getValue() + ".*", Pattern.CASE_INSENSITIVE);
			qs.add(new BasicDBObject(column, new BasicDBObject("$not", p)));
			return;
		}
		
		if (op == Operator.LEFT_MATCH) {
			// FIXME: escape value
			Pattern p = Pattern.compile("^" + vf.getValue() + ".*", Pattern.CASE_INSENSITIVE);
			qs.add(new BasicDBObject(column, p));
			return;
		}
		
		if (op == Operator.NOT_LEFT_MATCH) {
			// FIXME: escape value
			Pattern p = Pattern.compile("^" + vf.getValue() + ".*", Pattern.CASE_INSENSITIVE);
			qs.add(new BasicDBObject(column, new BasicDBObject("$not", p)));
			return;
		}
		
		if (op == Operator.RIGHT_MATCH) {
			// FIXME: escape value
			Pattern p = Pattern.compile(".*" + vf.getValue() + "$", Pattern.CASE_INSENSITIVE);
			qs.add(new BasicDBObject(column, p));
			return;
		}
		
		if (op == Operator.NOT_RIGHT_MATCH) {
			// FIXME: escape value
			Pattern p = Pattern.compile(".*" + vf.getValue() + "$", Pattern.CASE_INSENSITIVE);
			qs.add(new BasicDBObject(column, new BasicDBObject("$not", p)));
			return;
		}
		
		String fo = null;
		if (op == Operator.LESS_THAN) {
			fo = "$lt";
		}
		else if (op == Operator.LESS_EQUAL) {
			fo = "$lte";
		}
		else if (op == Operator.GREATER_THAN) {
			fo = "$gt";
		}
		else if (op == Operator.GREATER_EQUAL) {
			fo = "$gte";
		}
		else if (op == Operator.NOT_EQUAL) {
			fo = "ne";
		}
		else if (op == Operator.IN) {
			fo = "$in";
		}
		else if (op == Operator.NOT_IN) {
			fo = "$nin";
		}

		if (fo == null) {
			throw Exceptions.unsupported("Operator " + op + " is not supported by MongoDB.");
		}
		
		Object v = convertValueToBson(ef, vf.getValue());
		qs.add(new BasicDBObject(column, new BasicDBObject(fo, v)));
	}

	//-----------------------------------------------------------------------------
	private int getSortDirection(Order order) {
		if (Order.DESC.equals(order)) {
			return -1;
		}
		else if (Order.ASC.equals(order)) {
			return 1;
		}
		else {
			throw new IllegalArgumentException("Illegal sort direction: " + order);
		}
	}

	private BasicDBObject order(Query<?> query) {
		if (!query.hasOrders()) {
			return null;
		}
		
		BasicDBObject dbo = new BasicDBObject();

		Entity<?> entity = query.getEntity();
		if (entity == null) {
			for (Entry<String, Order> en : query.getOrders().entrySet()) {
				dbo.append(en.getKey(), getSortDirection(en.getValue()));
			}
		}
		else {
			for (Entry<String, Order> en : query.getOrders().entrySet()) {
				EntityField ef = getEntityField(entity, en.getKey(), "order");
				dbo.append(ef.getColumn(), getSortDirection(en.getValue()));
			}
		}
		return dbo;
	}
	
	//--------------------------------------------------------------------
	private WriteResult writeData(Entity<?> entity, Object obj) {
		DBObject dbo = convertDataToBson(entity, obj);
		DBCollection dbc = db.getCollection(getTableName(entity));
		return dbc.insert(dbo);
	}

	//--------------------------------------------------------------------
	private DBObject convertDataToBson(Entity<?> entity, Object data) {
		Query<?> query = createQuery(entity);
		return convertDataToBson(query, data);
	}
	
	private DBObject convertDataToBson(Query<?> query, Object data) {
		BasicDBObject dbo = new BasicDBObject();
		
		Entity<?> entity = query.getEntity();
		for (EntityField ef : entity.getFields()) {
			if (ef.isReadonly() || query.shouldExclude(ef.getName())) {
				continue;
			}

			Object v = ef.getValue(data);
			v = convertValueToBson(ef, v);
			
			String fn = ef.isIdentity() ? ID : ef.getColumn();
			dbo.put(fn, v);
		}

		return dbo;
	}
	
	private Object convertValueToBson(EntityField ef, Object value) throws DaoException {
		if (value == null) {
			return value;
		}
		
		MongoConverter mc = getMongoDaoClient().findConverter(value.getClass());
		if (mc != null) {
			return mc.convert(value);
		}
		
		if (value instanceof CharSequence) {
			if (ef != null && ef.isIdentity()) {
				value = new ObjectId(value.toString());
			}
			else {
				value = value.toString();
			}
		}
		else if (value instanceof Calendar) {
			value = ((Calendar)value).getTime();
		}
		else if (value instanceof BigInteger) {
			value = ((BigInteger)value).longValue();
		}
		else if (value instanceof BigDecimal) {
			value = ((BigDecimal)value).doubleValue();
		}
		
		return value;
	}

	@SuppressWarnings("unchecked")
	private <T> T convertBsonToData(Query<T> query, DBObject dbo) {
		Entity<T> en = query.getEntity();
		EntityField eid = (en == null ? null : en.getIdentity());
		
		T data = createEntityData(en);
		
		if (en == null) {
			Map<String, Object> map = (Map<String, Object>)data;
			for (String fn : dbo.keySet()) {
				if (query.shouldExclude(fn)) {
					continue;
				}

				Object v = dbo.get(fn);
				v = convertValueFromBson(null, v);
				map.put(fn, v);
			}
		}
		else {
			for (String k : dbo.keySet()) {
				String fn = k;

				EntityField ef = null;
				if (eid != null && ID.equals(fn)) {
					fn = eid.getName();
					ef = eid;
				}
				else {
					ef = en.getColumn(fn);
					if (ef == null || ef.isNotPersistent() || query.shouldExclude(ef.getName())) {
						continue;
					}
				}

				Object v = dbo.get(k);
				v = convertValueFromBson(ef, v);
				ef.setValue(data, v);
			}
		}
		return data;
	}

	private Object convertValueFromBson(EntityField ef, Object value) throws DaoException {
		if (value == null) {
			return value;
		}
		
		if (ef != null) {
			Type toType = ef.getType();
			if (!Types.isInstance(value, toType)) {
				value = cast(value, toType);
			}
		}

		return value;
	}

	private <T> T cast(Object value, Type toType) {
		return getDaoClient().getCastors().cast(value, toType);
	}
}
