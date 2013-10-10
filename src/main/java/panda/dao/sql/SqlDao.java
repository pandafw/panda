package panda.dao.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.dao.Dao;
import panda.dao.DaoException;
import panda.dao.DataHandler;
import panda.dao.Transaction;
import panda.dao.criteria.Query;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.sql.expert.SqlExpert;
import panda.lang.Strings;

/**
 * !! thread-unsafe !!
 * @author yf.frank.wang@gmail.com
 */
public class SqlDao extends Dao {
	private int transactionLevel = Connection.TRANSACTION_NONE;
	
	/**
	 * auto start count
	 */
	private int autoCount;
	
	private Connection connection;
	private SqlExecutor executor;
	
	public SqlDao(SqlDaoClient daoClient) {
		super(daoClient);
	}

	protected SqlDaoClient getSqlDaoClient() {
		return (SqlDaoClient)getDaoClient();
	}

	protected SqlExpert getSqlExpert() {
		return getSqlDaoClient().getSqlExpert();
	}

	protected void autoStart() {
		if (connection == null) {
			try {
				connection = getSqlDaoClient().getDataSource().getConnection();
				connection.setAutoCommit(false);
				if (transactionLevel != Connection.TRANSACTION_NONE) {
					connection.setTransactionIsolation(transactionLevel);
				}
			}
			catch (SQLException e) {
				SqlUtils.safeClose(connection);
				throw new DaoException("Failed to open connection", e);
			}
		}
		if (executor == null) {
			executor = getSqlDaoClient().getSqlManager().getExecutor(connection);
		}
		else {
			executor.setConnection(connection);
		}
		autoCount++;
	}

	protected void autoCommit() {
		if (autoCount == 1) {
			commit();
		}
	}

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
			}
		}
	}

	/**
	 * drop a table if exists
	 * 
	 * @param entity entity
	 */
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
				executor.update(sql);
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
	 * @return true if drop successfully
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
			executor.update(sql);
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
	 * @param dropIfExists drop if table exists
	 */
	@Override
	public void create(Entity<?> entity, boolean dropIfExists) {
		assertTable(entity);

		List<String> sqls = getSqlExpert().create(entity);

		autoStart();
		try {
			if (dropIfExists) {
				drop(entity);
			}

			for (String sql : sqls) {
				executor.update(sql);
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
			// table does exists
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
	public boolean exists(Entity<?> entity, Object ... keys) {
		assertEntity(entity);

		if (keys == null || keys.length == 0) {
			return exists(entity.getTableName());
		}

		Query query = new Query();
		query.setLimit(1);
		queryPrimaryKey(entity, query, keys);
		includePrimaryKeys(entity, query);
		
		Object d = fetch(entity, query);
		return d != null;
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

		Query query = new Query();
		query.setLimit(1);
		queryPrimaryKey(entity, query, keys);

		return fetch(entity, query);
	}

	/**
	 * get a record by the supplied query
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record
	 */
	@Override
	public <T> T fetch(Entity<T> entity, Query query) {
		assertEntity(entity);

		if (query == null) {
			query = new Query();
		}

		query.setLimit(1);
		String sql = getSqlExpert().select(entity, query);
		
		autoStart();
		try {
			return executor.fetch(sql, query.getParams(), entity.getType());
		}
		catch (SQLException e) {
			throw new DaoException("Failed to fetch entity " + entity.getViewName() + ": " + sql, e);
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

		if (query == null) {
			query = new Query();
		}

		query.setLimit(1);
		String sql = getSqlExpert().select(table, query);
		
		autoStart();
		try {
			return executor.fetch(sql, query.getParams(), HashMap.class);
		}
		catch (SQLException e) {
			throw new DaoException("Failed to fetch table " + table + ": " + sql, e);
		}
		finally {
			autoClose();
		}
	}

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
		
		Query query = new Query();
		queryPrimaryKey(entity, query, obj);

		return delete(entity, query);
	}

	/**
	 * delete records by the supplied keys.
	 * if the supplied keys is null, all records will be deleted.
	 * 
	 * @param entity entity
	 * @param keys a record contains key property or composite keys
	 * @return deleted count
	 */
	@Override
	public <T> int delete(Entity<T> entity, Object ... keys) {
		assertTable(entity);

		if (keys == null || keys.length == 0) {
			return delete(entity.getTableName());
		}

		Query query = new Query();
		queryPrimaryKey(entity, query, keys);
		
		return delete(entity, query);
	}

	/**
	 * delete record by the supplied query
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return deleted count
	 */
	@Override
	public int delete(Entity<?> entity, Query query) {
		assertTable(entity);

		String sql = getSqlExpert().delete(entity, query);
		
		autoStart();
		try {
			int cnt = executor.update(sql, query == null ? null : query.getParams());
			autoCommit();
			return cnt;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to delete entity " + entity.getType() + ": " + sql, e);
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
	public int delete(String table, Query query) {
		assertTable(table);

		String sql = getSqlExpert().delete(table, query);
		
		autoStart();
		try {
			int cnt = executor.update(sql, query == null ? null : query.getParams());
			autoCommit();
			return cnt;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to delete table " + table + ": " + sql, e);
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
	 * @param obj the record to be inserted
	 * @return the inserted record
	 */
	@Override
	public <T> T insert(T obj) {
		assertObject(obj);

		Entity<?> entity = getEntity(obj.getClass());
		assertTable(entity);
		
		String sql = getSqlExpert().insert(entity);
		
		autoStart();
		try {
			T d = insert(entity, sql, obj);
			autoCommit();
			return d;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to insert entity " + entity.getType() + ": " + sql, e);
		}
		finally {
			autoClose();
		}
	}

	private <T> T insert(Entity<?> entity, String sql, T obj) throws SQLException {
		EntityField id = entity.getIdentity();
		if (id == null) {
			int i = executor.update(sql, obj);
			autoCommit();
			return i > 0 ? obj : null;
		}
		
		// TODO: @Prep @Post
		T d = executor.insert(sql, obj, id.getName());
		return d;
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
	public <T> Collection<T> inserts(Collection<T> col) {
		assertCollection(col);

		Entity<?> entity = null;
		String sql = null;

		autoStart();
		try {
			for (T obj : col) {
				if (obj == null) {
					continue;
				}
				if (entity == null) {
					entity = getEntity(obj.getClass());
					assertTable(entity);
					sql = getSqlExpert().insert(entity);
				}
				
				insert(entity, sql, obj);
			}

			autoCommit();
			return col;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to insert entity " + entity.getType() + ": " + sql, e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * update a record by the supplied object. 
	 * 
	 * @param obj sample object
	 * @return updated count
	 */
	@Override
	public int update(Object obj) {
		assertObject(obj);

		Entity<?> entity = getEntity(obj.getClass());
		assertTable(entity);
		
		Query query = new Query();
		queryPrimaryKey(entity, query, obj);

		return update(entity, obj, query);
	}


	/**
	 * update records by the supplied object collection. 
	 * 
	 * @param col record collection
	 * @return updated count
	 */
	@Override
	public <T> int updates(Collection<T> col) {
		assertCollection(col);

		Entity<?> entity = null;
		Query query = new Query();

		autoStart();
		try {
			int cnt = 0;
			for (T obj : col) {
				if (obj == null) {
					continue;
				}
				if (entity == null) {
					entity = getEntity(obj.getClass());
					assertTable(entity);
				}
				
				query.reset();
				queryPrimaryKey(entity, query, obj);
				excludePrimaryKeys(entity, query);

				String sql = getSqlExpert().update(entity, query);
				cnt += executor.update(sql, query.getParams());
			}

			autoCommit();
			return cnt;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to update entity " + entity.getType() + " collection", e);
		}
		finally {
			autoClose();
		}
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
		
		Query query = new Query();
		excludeNullProperties(entity, query, obj);

		return update(entity, obj, query);
	}

	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	@Override
	public int update(Object obj, Query query) {
		assertObject(obj);

		Entity<?> entity = getEntity(obj.getClass());
		assertTable(entity);

		return update(entity, obj, query);
	}

	/**
	 * update records by the supplied object and query. 
	 * the null properties will be ignored.
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	@Override
	public int updateIgnoreNull(Object obj, Query query) {
		assertObject(obj);

		Entity<?> entity = getEntity(obj.getClass());
		assertTable(entity);
		
		if (query == null) {
			query = new Query();
		}
		excludeNullProperties(entity, query, obj);

		return update(entity, obj, query);
	}

	/**
	 * update records by the supplied object and query
	 * 
	 * @param obj sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	private int update(Entity<?> entity, Object obj, Query query) {
		if (query == null) {
			query = new Query();
		}
		excludePrimaryKeys(entity, query);

		String sql = getSqlExpert().update(entity, query);
		
		autoStart();
		try {
			int cnt = executor.update(sql, query.getParams());
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

		String sql = getSqlExpert().select(entity, query);
		
		autoStart();
		try {
			if (isClientPaginate(query)) {
				return executor.selectList(sql, query == null ? null : query.getParams(), entity.getType(), query.getStart(), query.getLimit());
			}
			return executor.selectList(sql, query == null ? null : query.getParams(), entity.getType());
		}
		catch (SQLException e) {
			throw new DaoException("Failed to select entity " + entity.getViewName() + ": " + sql, e);
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

		String sql = getSqlExpert().select(table, query);
		
		autoStart();
		try {
			if (isClientPaginate(query)) {
				return executor.selectList(sql, query == null ? null : query.getParams(), Map.class, query.getStart(), query.getLimit());
			}
			return executor.selectList(sql, query == null ? null : query.getParams(), Map.class);
		}
		catch (SQLException e) {
			throw new DaoException("Failed to select table " + table + ": " + sql, e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * select records by the supplied query.
	 * 
	 * @param type record type
	 * @param query WHERE conditions, order, offset, limit and filters
	 * @param callback DataHandler callback
	 * @return callback processed count
	 */
	@Override
	public <T> int select(Entity<T> entity, Query query, DataHandler<T> callback) {
		assertEntity(entity);
		
		SqlResultSet<T> srs = null;
		String sql = getSqlExpert().select(entity, query);
		
		autoStart();
		try {
			srs = executor.selectResultSet(sql, query == null ? null : query.getParams(), entity.getType());
			
			int count = 0;
			int max = Integer.MAX_VALUE;
			if (isClientPaginate(query)) {
				SqlUtils.skipResultSet(srs, query.getStart());
				if (query.getLimit() > 0) {
					max = query.getLimit();
				}
			}
			
			boolean next = true;
			while (srs.next() && next && count < max) {
				T data = srs.getResult();
				try {
					next = callback.handleData(data);
				}
				catch (Throwable ex) {
					throw new DaoException("Data Handle Error [" + count + "]: " + data, ex);
				}
				count++;
			}
			return count;
		}
		catch (SQLException e) {
			throw new DaoException("Failed to select entity " + entity.getViewName() + ": " + sql, e);
		}
		finally {
			SqlUtils.safeClose(srs);
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

		SqlResultSet<Map> srs = null;
		String sql = getSqlExpert().select(table, query);

		autoStart();
		try {
			srs = executor.selectResultSet(sql, query == null ? null : query.getParams(), Map.class);

			int count = 0;
			int max = Integer.MAX_VALUE;
			if (isClientPaginate(query)) {
				SqlUtils.skipResultSet(srs, query.getStart());
				if (query.getLimit() > 0) {
					max = query.getLimit();
				}
			}
			
			boolean next = true;
			while (srs.next() && next && count < max) {
				Map data = srs.getResult();
				try {
					next = callback.handleData(data);
				}
				catch (Exception ex) {
					throw new DaoException("Data Handle Error [" + count + "]: " + data, ex);
				}
				count++;
			}
			return count;
		}
		catch (SQLException e) {
			throw new DaoException("Failed to select table " + table + ": " + sql, e);
		}
		finally {
			SqlUtils.safeClose(srs);
			autoClose();
		}
	}

	/**
	 * count records by the supplied query.
	 * 
	 * @param entity entity
	 * @param query WHERE conditions
	 * @return record count
	 */
	public int count(Entity<?> entity, Query query) {
		assertEntity(entity);

		String sql = getSqlExpert().count(entity, query);
		
		autoStart();
		try {
			int i = executor.fetch(sql, query == null ? null : query.getParams(), int.class);
			return i;
		}
		catch (SQLException e) {
			throw new DaoException("Failed to count entity " + entity.getViewName() + ": " + sql, e);
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

		String sql = getSqlExpert().count(table, query);
		
		autoStart();
		try {
			int i = executor.fetch(sql, query == null ? null : query.getParams(), int.class);
			return i;
		}
		catch (SQLException e) {
			throw new DaoException("Failed to count table " + table + ": " + sql, e);
		}
		finally {
			autoClose();
		}
	}

	/**
	 * execute a transaction
	 */
	public void exec(Transaction transaction) {
		exec(transaction, Connection.TRANSACTION_NONE);
	}
	
	/**
	 * execute a transaction
	 * @param level transaction level
	 */
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
		}
		finally {
			autoClose();
		}
	}
	
	/**
	 * commit a transaction
	 */
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
	public void rollback() {
		try {
			if (connection != null) {
				connection.rollback();
			}
		}
		catch (SQLException e) {
			throw new DaoException("Failed to commit transaction", e);
		}
	}

}
