package panda.dao.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.dao.AbstractDao;
import panda.dao.DB;
import panda.dao.DaoException;
import panda.dao.DataHandler;
import panda.dao.Transaction;
import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.sql.executor.JdbcSqlExecutor;
import panda.dao.sql.expert.SqlExpert;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.lang.Types;

/**
 * !! thread-unsafe !!
 * @author yf.frank.wang@gmail.com
 */
public class SqlDao extends AbstractDao {
	private int transactionLevel = Connection.TRANSACTION_NONE;
	
	/**
	 * auto start count
	 */
	private int autoCount;
	
	private Connection connection;
	private JdbcSqlExecutor executor;
	
	public SqlDao(SqlDaoClient daoClient) {
		super(daoClient);
	}

	//-------------------------------------------------------------------------
	protected SqlDaoClient getSqlDaoClient() {
		return (SqlDaoClient)getDaoClient();
	}

	protected SqlExpert getSqlExpert() {
		return getSqlDaoClient().getSqlExpert();
	}

	//-------------------------------------------------------------------------
	@Override
	protected void autoStart() {
		if (autoCount < 1) {
			if (connection == null) {
				try {
					connection = getSqlDaoClient().getDataSource().getConnection();
					connection.setAutoCommit(false);
					if (transactionLevel != Connection.TRANSACTION_NONE) {
						connection.setTransactionIsolation(transactionLevel);
					}
				}
				catch (SQLException e) {
					Sqls.safeClose(connection);
					throw new DaoException("Failed to open connection", e);
				}
			}
			if (executor == null) {
				executor = getSqlDaoClient().getJdbcSqlExecutor();
			}
			else {
				executor.reset();
			}
			executor.setConnection(connection);
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
		if (autoCount == 0) {
			try {
				connection.close();
			}
			catch (SQLException e) {
				throw new DaoException("Failed to close connection", e);
			}
			finally {
				connection = null;
				executor.setConnection(null);
			}
		}
	}
	
	//-------------------------------------------------------------------------
	/**
	 * commit a transaction
	 */
	@Override
	public void commit() {
		if (connection != null) {
			try {
				connection.commit();
			}
			catch (SQLException e) {
				throw new DaoException("Failed to commit transaction", e);
			}
		}
	}
	
	/**
	 * rollback a transaction
	 */
	@Override
	public void rollback() {
		if (connection != null) {
			try {
				connection.rollback();
			}
			catch (SQLException e) {
				throw new DaoException("Failed to rollback transaction", e);
			}
		}
	}

	//-------------------------------------------------------------------------
	/**
	 * execute a transaction
	 */
	@Override
	public void exec(Transaction transaction) {
		exec(transaction, Connection.TRANSACTION_NONE);
	}
	
	/**
	 * execute a transaction
	 * @param level transaction level
	 */
	@Override
	public void exec(Transaction transaction, int level) {
		assertTransaction(transaction);

		transactionLevel = level;
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

	//-------------------------------------------------------------------------
	/**
	 * drop a table if exists
	 * 
	 * @param entity entity
	 */
	@Override
	public void drop(Entity<?> entity) {
		assertTable(entity);

		if (!getSqlExpert().isSupportDropIfExists()) {
			if (!exists(entity)) {
				return;
			}
		}
		
		List<String> sqls = getSqlExpert().drop(entity);

		autoStart();
		try {
			for (String sql : sqls) {
				executor.execute(sql);
			}
			autoCommit();
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to drop entity " + entity.getType() + ": " + Strings.join(sqls, ";\n"), e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * drop a table if exists
	 * 
	 * @param table table name
	 */
	@Override
	public void drop(String table) {
		assertTable(table);

		if (!getSqlExpert().isSupportDropIfExists()) {
			if (!exists(table)) {
				return;
			}
		}
		
		String sql = getSqlExpert().dropTable(table);

		autoStart();
		try {
			executor.execute(sql);
			autoCommit();
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to drop table " + table + ": " + sql, e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * create table
	 * 
	 * @param entity entity
	 */
	@Override
	public void create(Entity<?> entity) {
		assertTable(entity);

		List<String> sqls = getSqlExpert().create(entity);

		autoStart();
		try {
			for (String sql : sqls) {
				executor.execute(sql);
			}
			autoCommit();
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to create entity " + entity.getType() + ": " + Strings.join(sqls, ";\n"), e);
		}
		finally {
			autoClose();
		}
	}

	//-------------------------------------------------------------------------
	/**
	 * check a table exists in the data store.
	 * 
	 * @param table table name
	 * @return true if the record or the table exists in the data store
	 */
	@Override
	public boolean exists(String table) {
		assertTable(table);

		String sql = getSqlExpert().exists(table);

		autoStart();
		try {
			executor.execute(sql);
			return true;
		}
		catch (SQLException e) {
			// table does not exist
			return false;
		}
		finally {
			autoClose();
		}
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
			return exists(entity.getTableName());
		}

		Query query = new Query();
		queryPrimaryKey(entity, query, keys);
		includePrimaryKeys(entity, query);
		
		Object d = fetchByQuery(entity, query);
		return d != null;
	}

	/**
	 * check a record exists in the data store.
	 * if the query is not supplied, then check the table existence.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return true if the record or the table exists in the data store
	 */
	@Override
	protected boolean existsByQuery(Entity<?> entity, Query query) {
		assertEntity(entity);

		if (query == null || !query.hasConditions()) {
			return exists(entity.getTableName());
		}

		Object d = fetchByQuery(entity, query);
		return d != null;
	}

	/**
	 * get a record by the supplied query
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record
	 */
	@Override
	protected <T> T fetchByQuery(Entity<T> entity, Query query) {
		assertEntity(entity);

		query = cloneQuery(query);

		query.setLimit(1);
		Sql sql = getSqlExpert().select(entity, query);
		
		autoStart();
		try {
			return executor.fetch(sql.getSql(), sql.getParams(), entity.getType());
		}
		catch (SQLException e) {
			throw new DaoException("Failed to fetch entity " + entity.getViewName() + ": " + sql.getSql(), e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * get a record by the supplied query
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record
	 */
	@Override
	public Map fetch(String table, Query query) {
		assertTable(table);

		query = cloneQuery(query);

		query.setLimit(1);
		Sql sql = getSqlExpert().select(table, query);
		
		autoStart();
		try {
			return executor.fetch(sql.getSql(), sql.getParams(), HashMap.class);
		}
		catch (SQLException e) {
			throw new DaoException("Failed to fetch table " + table + ": " + sql.getSql(), e);
		}
		finally {
			autoClose();
		}
	}

	//-------------------------------------------------------------------------
	/**
	 * delete record by the supplied query
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	@Override
	public int deletes(Entity<?> entity, Query query) {
		assertTable(entity);

		Sql sql = getSqlExpert().delete(entity, query);
		
		autoStart();
		try {
			int cnt = executor.update(sql.getSql(), sql.getParams());
			autoCommit();
			return cnt;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to delete entity " + entity.getType() + ": " + sql.getSql(), e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * delete record by the supplied query
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	@Override
	public int deletes(String table, Query query) {
		assertTable(table);

		Sql sql = getSqlExpert().delete(table, query);
		
		autoStart();
		try {
			int cnt = executor.update(sql.getSql(), sql.getParams());
			autoCommit();
			return cnt;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to delete table " + table + ": " + sql.getSql(), e);
		}
		finally {
			autoClose();
		}
	}

	//-------------------------------------------------------------------------
	protected <T> T insert(Entity<?> entity, T obj) throws Exception {
		EntityField eid = entity.getIdentity();
		if (eid == null) {
			Sql sql = getSqlExpert().insert(entity, obj, false);
			int c = executor.update(sql.getSql(), sql.getParams());
			return c > 0 ? obj : null;
		}
		
		Object iid = eid.getValue(obj);
		if (isValidIdentity(iid)) {
			String s = getSqlExpert().identityInsertOn(entity);
			if (Strings.isNotEmpty(s)) {
				executor.execute(s);
			}
			
			Sql sql = getSqlExpert().insert(entity, obj, false);
			int c = executor.update(sql.getSql(), sql.getParams());
			
			s = getSqlExpert().identityInsertOff(entity);
			if (Strings.isNotEmpty(s)) {
				executor.execute(s);
			}
			
			return c > 0 ? obj : null;
		}
		
		if (eid.isAutoIncrement() && getSqlExpert().isSupportAutoIncrement()) {
			Sql sql = getSqlExpert().insert(entity, obj, true);
			return executor.insert(sql.getSql(), sql.getParams(), obj, eid.getName());
		}

		Map<String, String> m = new HashMap<String, String>();
		m.put("view", entity.getViewName());
		m.put("table", entity.getTableName());
		m.put("field", eid.getName());
		
		String prep = entity.getPrepSql(getSqlExpert().getDatabaseType());
		if (Strings.isEmpty(prep)) {
			prep = entity.getPrepSql(DB.GENERAL);
		}
		String post = entity.getPostSql(getSqlExpert().getDatabaseType());
		if (Strings.isEmpty(post)) {
			post = entity.getPostSql(DB.GENERAL);
		}
		
		if (Strings.isEmpty(prep) && Strings.isEmpty(post)) {
			prep = getSqlExpert().prepIdentity(entity);
			post = getSqlExpert().postIdentity(entity);
		}
		else {
			prep = Texts.translate(prep, m);
			post = Texts.translate(post, m);
		}

		if (Strings.isEmpty(prep) && Strings.isEmpty(post)) {
			throw new DaoException("Failed to get (" + getSqlExpert().getDatabaseType() + ") identity select sql for entity: " + entity.getType());
		}
		
		if (Strings.isNotEmpty(prep)) {
			iid = executor.fetch(prep, Types.getRawType(eid.getType()));
			if (!isValidIdentity(iid)) {
				throw new DaoException("Failed to get identity from prep sql: " + prep);
			}
			if (!eid.setValue(obj, iid)) {
				throw new DaoException("Failed to set identity to entity: " + entity.getType());
			}
		}
		
		Sql sql = getSqlExpert().insert(entity, obj, false);
		int c = executor.update(sql.getSql(), sql.getParams());
		if (c == 0) {
			return null;
		}

		if (Strings.isNotEmpty(post)) {
			iid = executor.fetch(post, Types.getRawType(eid.getType()));
			if (!isValidIdentity(iid)) {
				throw new DaoException("Failed to get identity from post sql: " + post);
			}
			if (!eid.setValue(obj, iid)) {
				throw new DaoException("Failed to set identity to entity: " + entity.getType());
			}
		}
		
		return obj;
	}

	//-------------------------------------------------------------------------
	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	protected int update(Entity<?> entity, Object obj, Query query) {
		query = cloneQuery(query);
		excludePrimaryKeys(entity, query);

		Sql sql = getSqlExpert().update(entity, obj, query);
		
		autoStart();
		try {
			int cnt = executor.update(sql.getSql(), sql.getParams());
			autoCommit();
			return cnt;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to update entity " + entity.getType() + ": " + sql, e);
		}
		finally {
			autoClose();
		}
	}

	private boolean isClientPaginate(Query query) {
		return (query != null && query.needsPaginate() && !getSqlExpert().isSupportPaginate());
	}
	
	//-------------------------------------------------------------------------
	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record list
	 */
	@Override
	public <T> List<T> select(Entity<T> entity, Query query) {
		assertEntity(entity);

		Sql sql = getSqlExpert().select(entity, query);
		
		autoStart();
		try {
			if (isClientPaginate(query)) {
				if (getSqlExpert().isSupportScroll()) {
					executor.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
				}
				List<T> list = executor.selectList(sql.getSql(), sql.getParams(), entity.getType(), query.getStart(), query.getLimit());
				if (getSqlExpert().isSupportScroll()) {
					executor.setResultSetType(ResultSet.FETCH_FORWARD);
				}
				return list;
			}
			return executor.selectList(sql.getSql(), sql.getParams(), entity.getType());
		}
		catch (SQLException e) {
			throw new DaoException("Failed to select entity " + entity.getViewName() + ": " + sql.getSql(), e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @return record(a map) list
	 */
	@Override
	public List<Map> select(String table, Query query) {
		assertTable(table);

		Sql sql = getSqlExpert().select(table, query);
		
		autoStart();
		try {
			if (isClientPaginate(query)) {
				return executor.selectList(sql.getSql(), sql.getParams(), Map.class, query.getStart(), query.getLimit());
			}
			return executor.selectList(sql.getSql(), sql.getParams(), Map.class);
		}
		catch (SQLException e) {
			throw new DaoException("Failed to select table " + table + ": " + sql.getSql(), e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * select records by the supplied query.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	@Override
	public <T> int select(Entity<T> entity, Query query, DataHandler<T> callback) {
		assertEntity(entity);
		assertCallback(callback);
		
		SqlResultSet<T> srs = null;
		Sql sql = getSqlExpert().select(entity, query);
		
		autoStart();
		try {
			srs = executor.selectResultSet(sql.getSql(), sql.getParams(), entity.getType());
			
			int count = 0;
			int max = Integer.MAX_VALUE;
			if (isClientPaginate(query)) {
				Sqls.skipResultSet(srs, query.getStart());
				if (query.getLimit() > 0) {
					max = query.getLimit();
				}
			}
			
			boolean next = true;
			while (srs.next() && next && count < max) {
				T data = srs.getResult();
				next = callback(callback, data, count);
				count++;
			}
			return count;
		}
		catch (SQLException e) {
			throw new DaoException("Failed to select entity " + entity.getViewName() + ": " + sql.getSql(), e);
		}
		finally {
			Sqls.safeClose(srs);
			autoClose();
		}
	}

	/**
	 * select records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	@Override
	public int select(String table, Query query, DataHandler<Map> callback) {
		assertTable(table);
		assertCallback(callback);

		SqlResultSet<Map> srs = null;
		Sql sql = getSqlExpert().select(table, query);

		autoStart();
		try {
			srs = executor.selectResultSet(sql.getSql(), sql.getParams(), Map.class);

			int count = 0;
			int max = Integer.MAX_VALUE;
			if (isClientPaginate(query)) {
				Sqls.skipResultSet(srs, query.getStart());
				if (query.getLimit() > 0) {
					max = query.getLimit();
				}
			}
			
			boolean next = true;
			while (srs.next() && next && count < max) {
				Map data = srs.getResult();
				next = callback(callback, data, count);
				count++;
			}
			return count;
		}
		catch (SQLException e) {
			throw new DaoException("Failed to select table " + table + ": " + sql.getSql(), e);
		}
		finally {
			Sqls.safeClose(srs);
			autoClose();
		}
	}

	//-------------------------------------------------------------------------
	/**
	 * count records by the supplied query.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record count
	 */
	@Override
	public int count(Entity<?> entity, Query query) {
		assertEntity(entity);

		Sql sql = getSqlExpert().count(entity, query);
		
		autoStart();
		try {
			int i = executor.fetch(sql.getSql(), sql.getParams(), int.class);
			return i;
		}
		catch (SQLException e) {
			throw new DaoException("Failed to count entity " + entity.getViewName() + ": " + sql.getSql(), e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * count records by the supplied query.
	 * 
	 * @param table table name
	 * @param query WHERE conditions
	 * @return record count
	 */
	@Override
	public int count(String table, Query query) {
		assertTable(table);

		Sql sql = getSqlExpert().count(table, query);
		
		autoStart();
		try {
			int i = executor.fetch(sql.getSql(), sql.getParams(), int.class);
			return i;
		}
		catch (SQLException e) {
			throw new DaoException("Failed to count table " + table + ": " + sql.getSql(), e);
		}
		finally {
			autoClose();
		}
	}

}
