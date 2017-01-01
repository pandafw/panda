package panda.dao.sql;

import panda.dao.AbstractDao;
import panda.dao.DB;
import panda.dao.DaoException;
import panda.dao.DataHandler;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.GenericQuery;
import panda.dao.query.Query;
import panda.dao.sql.executor.JdbcSqlExecutor;
import panda.dao.sql.expert.SqlExpert;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.lang.reflect.Types;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * !! thread-unsafe !!
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
	/**
	 * @return the transactionLevel
	 */
	public int getTransactionLevel() {
		return transactionLevel;
	}

	/**
	 * @param transactionLevel the transactionLevel to set
	 */
	public void setTransactionLevel(int transactionLevel) {
		this.transactionLevel = transactionLevel;
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
		autoStart(transactionLevel);
	}
	
	protected void autoStart(int transactionLevel) {
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
					throw new DaoException("Failed to open connection: " + e.getMessage(), e);
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
				Sqls.safeClose(connection);
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
				throw new DaoException("Failed to commit transaction: " + e.getMessage(), e);
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
				throw new DaoException("Failed to rollback transaction: " + e.getMessage(), e);
			}
		}
	}

	//-------------------------------------------------------------------------
	/**
	 * execute a transaction
	 */
	@Override
	public void exec(Runnable transaction) {
		exec(transaction, transactionLevel);
	}
	
	/**
	 * execute a transaction
	 * @param level transaction level
	 */
	@Override
	public void exec(Runnable transaction, int level) {
		assertTransaction(transaction);

		try {
			autoStart(level);
			transaction.run();
			autoCommit();
		}
		catch (Throwable e) {
			rollback();
			Exceptions.rethrowRuntime(e);
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
			if (!existsByTable(getTableName(entity))) {
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
			if (!existsByTable(table)) {
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

	/**
	 * create table ddl
	 * 
	 * @param entity entity
	 */
	@Override
	public String ddl(Entity<?> entity) {
		assertTable(entity);

		List<String> sqls = getSqlExpert().create(entity);
		
		StringBuilder sb = new StringBuilder();
		for (String s : sqls) {
			sb.append(s).append(';').append(Streams.LINE_SEPARATOR);
		}
		return sb.toString();
	}

	//-------------------------------------------------------------------------
	/**
	 * check a table exists in the data store.
	 * 
	 * @param table table name
	 * @return true if the record or the table exists in the data store
	 */
	@Override
	public boolean existsByTable(String table) {
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
	@SuppressWarnings("unchecked")
	protected boolean existsByKeys(Entity<?> entity, Object ... keys) {
		assertEntity(entity);

		if (keys == null || keys.length == 0) {
			return existsByTable(getTableName(entity));
		}

		GenericQuery query = createQuery(entity);
		queryPrimaryKey(query, keys);
		selectPrimaryKeys(query);
		
		Object d = fetchByQuery(query);
		return d != null;
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

		selectPrimaryKeys(query);
		Object d = fetchByQuery(query);
		return d != null;
	}

	/**
	 * get a record by the supplied query
	 * 
	 * @param query query
	 * @return record
	 */
	@Override
	protected <T> T fetchByQuery(GenericQuery<T> query) {
		query.setLimit(1);
		Sql sql = getSqlExpert().select(query);
		
		autoStart();
		try {
			try {
				return executor.fetch(sql.getSql(), sql.getParams(), query.getType());
			}
			catch (SQLException e) {
				throw new DaoException("Failed to fetch query " + getTableName(query) + ": " + sql.getSql() + " - " + e.getMessage(), e);
			}
		}
		finally {
			autoClose();
		}
	}

	//-------------------------------------------------------------------------
	/**
	 * count records by the supplied query.
	 * 
	 * @param query query
	 * @return record count
	 */
	@Override
	protected long countByQuery(Query<?> query) {
		Sql sql = getSqlExpert().count(query);
		
		autoStart();
		try {
			int i = executor.fetch(sql.getSql(), sql.getParams(), int.class);
			return i;
		}
		catch (SQLException e) {
			throw new DaoException("Failed to count query " + getTableName(query) + ": " + sql.getSql() + " - " + e.getMessage(), e);
		}
		finally {
			autoClose();
		}
	}
	
	//-------------------------------------------------------------------------
	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	 * @param query query
	 * @return record list
	 */
	@Override
	protected <T> List<T> selectByQuery(GenericQuery<T> query) {
		Sql sql = getSqlExpert().select(query);
		
		autoStart();
		try {
			if (isClientPaginate(query)) {
				if (getSqlExpert().isSupportScroll()) {
					executor.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
				}
				List<T> list = executor.selectList(sql.getSql(), sql.getParams(), query.getType(), query.getStart(), query.getLimit());
				if (getSqlExpert().isSupportScroll()) {
					executor.setResultSetType(ResultSet.TYPE_FORWARD_ONLY);
				}
				return list;
			}
			return executor.selectList(sql.getSql(), sql.getParams(), query.getType());
		}
		catch (SQLException e) {
			throw new DaoException("Failed to select query " + getTableName(query) + ": " + sql.getSql() + " - " + e.getMessage(), e);
		}
		finally {
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
	public <T> long selectByQuery(GenericQuery<T> query, DataHandler<T> callback) {
		assertCallback(callback);
		
		SqlResultSet<T> srs = null;
		Sql sql = getSqlExpert().select(query);
		
		autoStart();
		try {
			srs = executor.selectResultSet(sql.getSql(), sql.getParams(), query.getType());
			
			long count = 0;
			long max = Long.MAX_VALUE;
			if (isClientPaginate(query)) {
				if (getSqlExpert().isSupportScroll()) {
					executor.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
				}
				
				Sqls.skipResultSet(srs, query.getStart());

				if (getSqlExpert().isSupportScroll()) {
					executor.setResultSetType(ResultSet.TYPE_FORWARD_ONLY);
				}
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
			throw new DaoException("Failed to select query " + getTableName(query) + ": " + sql.getSql() + " - " + e.getMessage(), e);
		}
		finally {
			Sqls.safeClose(srs);
			autoClose();
		}
	}

	//-------------------------------------------------------------------------
	/**
	 * delete record by the supplied query
	 * 
	 * @param query query
	 * @return deleted count
	 */
	@Override
	protected int deletesByQuery(Query<?> query) {
		Sql sql = getSqlExpert().delete(query);
		
		autoStart();
		try {
			int cnt = executor.update(sql.getSql(), sql.getParams());
			autoCommit();
			return cnt;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to delete query " + getTableName(query) + ": " + sql.getSql() + " - " + e.getMessage(), e);
		}
		finally {
			autoClose();
		}
	}

	//-------------------------------------------------------------------------
	protected <T> T insert(Entity<T> entity, T obj) throws Exception {
		EntityField eid = entity.getIdentity();
		if (eid == null) {
			Sql sql = getSqlExpert().insert(entity, obj, false);
			int c = executor.update(sql.getSql(), sql.getParams());
			if (c != 1) {
				throw new DaoException("Failed to insert entity, update count: " + c + ", SQL: " + sql.getSql());
			}
			return obj;
		}
		
		Object iid = eid.getValue(obj);
		if (isValidIdentity(iid)) {
			if (eid.isNumberIdentity()) {
				String s = getSqlExpert().identityInsertOn(entity);
				if (Strings.isNotEmpty(s)) {
					executor.execute(s);
				}
			}
			
			Sql sql = getSqlExpert().insert(entity, obj, false);
			int c = executor.update(sql.getSql(), sql.getParams());

			if (eid.isNumberIdentity()) {
				String s = getSqlExpert().identityInsertOff(entity);
				if (Strings.isNotEmpty(s)) {
					executor.execute(s);
				}
			}
			
			if (c != 1) {
				throw new DaoException("Failed to insert entity, update count: " + c + ", SQL: " + sql.getSql());
			}

			return obj;
		}
		
		if (eid.isAutoIncrement() && getSqlExpert().isSupportAutoIncrement()) {
			Sql sql = getSqlExpert().insert(entity, obj, true);
			return executor.insert(sql.getSql(), sql.getParams(), obj, eid.getName());
		}

		if (eid.isAutoGenerate() && eid.isStringIdentity()) {
			String aid = Randoms.randUUID32();
			if (!eid.setValue(obj, aid)) {
				throw new DaoException("Failed to set identity to entity: " + entity.getType());
			}

			Sql sql = getSqlExpert().insert(entity, obj, false);
			int c = executor.update(sql.getSql(), sql.getParams());
			if (c != 1) {
				throw new DaoException("Failed to insert entity, update count: " + c + ", SQL: " + sql.getSql());
			}
			return obj;
		}
		
		Map<String, String> m = new HashMap<String, String>();
		m.put("view", getViewName(entity));
		m.put("table", getTableName(entity));
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
		if (c != 1) {
			throw new DaoException("Failed to insert entity, update count: " + c + ", SQL: " + sql.getSql());
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
	 * @param query query
	 * @return updated count
	 */
	@Override
	protected int updatesByQuery(Object obj, GenericQuery<?> query, int limit) {
		excludePrimaryKeys(query);

		Sql sql = getSqlExpert().update(obj, query);
		
		autoStart();
		try {
			int cnt = executor.update(sql.getSql(), sql.getParams());
			if (cnt > limit) {
				throw new SQLException("Too many (" + cnt + ") records updated.");
			}
			autoCommit();
			return cnt;
		}
		catch (SQLException e) {
			rollback();
			throw new DaoException("Failed to update query " + getTableName(query) + ": " + sql + " - " + e.getMessage(), e);
		}
		finally {
			autoClose();
		}
	}

	private boolean isClientPaginate(Query query) {
		return (query != null && query.needsPaginate() && !getSqlExpert().isSupportPaginate());
	}
}
