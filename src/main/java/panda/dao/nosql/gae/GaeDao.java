package panda.dao.nosql.gae;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;

import panda.dao.AbstractDao;
import panda.dao.DaoException;
import panda.dao.DataHandler;
import panda.dao.entity.Entity;
import panda.dao.entity.EntityField;
import panda.dao.query.Filter.ComboFilter;
import panda.dao.query.Filter.ReferFilter;
import panda.dao.query.Filter.SimpleFilter;
import panda.dao.query.Filter.ValueFilter;
import panda.dao.query.GenericQuery;
import panda.dao.query.Logical;
import panda.dao.query.Operator;
import panda.dao.query.Order;
import panda.dao.query.Query;
import panda.lang.Exceptions;
import panda.lang.reflect.Types;
import panda.log.Log;
import panda.log.Logs;

public class GaeDao extends AbstractDao {
	private static final Log log = Logs.getLog(GaeDao.class);
	
	private static final int MAX_SL = 500;
	//private static final int MAX_BL = 1048576; //1M
	//private static final int MAX_TL = 1048576; //1M

	private static final String ROOT = "_root_";

	private static Map<String, com.google.appengine.api.datastore.Entity> roots;

	private com.google.appengine.api.datastore.Entity rootEntity;

	/**
	 * auto start count
	 */
	private int autoCount;
	
	private DatastoreService service;
	private com.google.appengine.api.datastore.Transaction transaction;
	
	public GaeDao(GaeDaoClient daoClient, DatastoreService service) {
		super(daoClient);
		this.service = service;
	}

	//---------------------------------------------------------------------
	protected GaeDaoClient getGaeDaoClient() {
		return (GaeDaoClient)getDaoClient();
	}

	//---------------------------------------------------------------------
	@Override
	protected void autoStart() {
		if (transaction == null) {
			try {
				transaction = service.beginTransaction();
			}
			catch (Exception e) {
				throw new DaoException("Failed to open transaction", e);
			}
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
			transaction = null;
		}
	}

	//--------------------------------------------------------------------
	/**
	 * commit a transaction
	 */
	@Override
	public void commit() {
		if (transaction != null) {
			try {
				transaction.commit();
			}
			catch (Exception e) {
				throw new DaoException("Failed to commit transaction", e);
			}
			finally {
				transaction = null;
			}
		}
	}
	
	/**
	 * rollback a transaction
	 */
	@Override
	public void rollback() {
		if (transaction != null) {
			try {
				transaction.rollback();
			}
			catch (Exception e) {
				throw new DaoException("Failed to rollback transaction", e);
			}
			finally {
				transaction = null;
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
	private com.google.appengine.api.datastore.Entity getRootEntity() {
		if (rootEntity == null) {
			if (roots == null) {
				synchronized (GaeDao.class) {
					roots = new HashMap<String, com.google.appengine.api.datastore.Entity>();
					com.google.appengine.api.datastore.Query q = new com.google.appengine.api.datastore.Query(ROOT);
					PreparedQuery pq = service.prepare(q);
					if (log.isDebugEnabled()) {
						log.debug("QUERY: " + q);
					}
					Iterator<com.google.appengine.api.datastore.Entity> it = pq.asIterator();
					while (it.hasNext()) {
						com.google.appengine.api.datastore.Entity en = it.next();
						if (log.isDebugEnabled()) {
							log.debug("GET ROOT: " + en);
						}
						roots.put(en.getKey().getName(), en);
					}
				}
			}

			rootEntity = roots.get(getDaoClient().getName());
			if (rootEntity == null) {
				synchronized (GaeDao.class) {
					com.google.appengine.api.datastore.Transaction transaction = service.beginTransaction();
					Key key = KeyFactory.createKey(ROOT, getDaoClient().getName());
					rootEntity = new com.google.appengine.api.datastore.Entity(key);
					if (log.isDebugEnabled()) {
						log.debug("PUT ROOT: " + rootEntity);
					}
					service.put(transaction, rootEntity);
					transaction.commit();
					roots.put(key.getName(), rootEntity);
				}
			}
		}
		return rootEntity;
	}

	private Key getRootKey() {
		return getRootEntity().getKey();
	}

	//-----------------------------------------------------------------------------
	private Key createKey(String table, Object id) {
		if (id instanceof Number) {
			return KeyFactory.createKey(getRootKey(), table, ((Number)id).longValue());
		}
		else {
			return KeyFactory.createKey(getRootKey(), table, id.toString());
		}
	}	
	
	private Key createKey(Entity<?> entity, Object id) {
		return createKey(entity.getTable(), id);
	}

	private Object getDataIdentity(Entity<?> en, Object data) {
		EntityField eid = en.getIdentity();
		if (eid == null) {
			return null;
		}

		Object id = eid.getValue(data);
		return id;
	}

	private void setDataIdentity(Entity<?> en, Object data, Key key) {
		EntityField eid = en.getIdentity();
		if (eid == null) {
			log.debug(en.getType() + " has no identity");
			return;
		}

		if (eid.isNumberIdentity()) {
			Object id = convertValueFromGae(eid, key.getId());
			eid.setValue(data, id);
		}
		else {
			Object id = convertValueFromGae(eid, key.getName());
			eid.setValue(data, id);
		}
	}

	private Object convertValueFromGae(EntityField ef, Object value) throws DaoException {
		if (value == null) {
			return value;
		}
		
		if (value instanceof Text) {
			value = ((Text)value).getValue();
		}
		else if (value instanceof Blob) {
			value = ((Blob)value).getBytes();
		}

		if (ef != null) {
			Type toType = ef.getType();
			if (!Types.isInstance(value, toType)) {
				value = getDaoClient().getCastors().cast(value, toType);
			}
		}

		return value;
	}

	private Object convertValueToGae(Object value) throws DaoException {
		if (value == null) {
			return value;
		}

		GaeConverter gc = getGaeDaoClient().findConverter(value.getClass());
		if (gc != null) {
			return gc.convert(value);
		}
		
		if (value instanceof CharSequence) {
			String s = value.toString();
			if (s.length() > MAX_SL) {
				value = new Text(s);
			}
			else {
				value = s;
			}
		}
		else if (value instanceof byte[]) {
			value = new Blob((byte[])value);
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
	private void convertEntityToData(com.google.appengine.api.datastore.Entity ge, Object data,
			Query<?> query) throws DaoException {

		Entity<?> entity = query.getEntity();
		if (entity == null) {
			Map map = (Map)data;
			Map<String, Object> eps = ge.getProperties();
			for (Entry<String, Object> e : eps.entrySet()) {
				String fn = e.getKey();
				if (query.shouldExclude(fn)) {
					continue;
				}

				Object v = e.getValue();
				v = convertValueFromGae(null, v);
				map.put(fn, v);
			}
		}
		else {
			Map<String, Object> eps = ge.getProperties();
			for (Entry<String, Object> e : eps.entrySet()) {
				String fn = e.getKey();
				EntityField ef = entity.getColumn(fn);
				if (ef == null || query.shouldExclude(ef.getName())) {
					continue;
				}
	
				Object v = e.getValue();
				v = convertValueFromGae(ef, v);
				ef.setValue(data, v);
			}
			setDataIdentity(entity, data, ge.getKey());
		}
	}
	
	private void convertDataToEntity(Query<?> query, Object data,
			com.google.appengine.api.datastore.Entity ge) throws DaoException {

		Entity<?> entity = query.getEntity();
		
		// reserve exclude properties
		Map<String, Object> eps = ge.getProperties();
		for (String k : eps.keySet()) {
			if (!query.shouldExclude(k)) {
				ge.removeProperty(k);
			}
		}
		
		// set properties except excludes
		for (EntityField ef : entity.getFields()) {
			if (ef.isReadonly() || query.shouldExclude(ef.getName())) {
				continue;
			}

			Object v = ef.getValue(data);
			v = convertValueToGae(v);

			String fn = ef.getColumn();
			ge.setProperty(fn, v);
		}
	}

	//-----------------------------------------------------------------------------
	/**
	 * is query for identity only? 
	 */
	private boolean isQueryIdentity(Query<?> query) {
		return getQueryIdentity(query) != null;
	}
	
	/**
	 * get identity value from query
	 */
	private Object getQueryIdentity(Query<?> query) {
		Entity<?> entity = query.getEntity();
		if (entity == null) {
			return null;
		}
		
		EntityField eid = entity.getIdentity();
		if (eid == null) {
			return null;
		}
		
		if (!query.hasFilters() || query.getFilters().getFilters().size() > 1) {
			return null;
		}
		
		Object f = query.getFilters().first();
		if (!(f instanceof ValueFilter) 
				|| !Operator.EQUAL.equals(((ValueFilter)f).getOperator())) {
			return null;
		}
		
		ValueFilter vf = (ValueFilter)f;
		if (vf.getValue() == null || !eid.getName().equals(vf.getField())) {
			return null;
		}
		
		return vf.getValue();
	}
	
	//-----------------------------------------------------------------------------
	private EntityField getEntityField(Entity<?> entity, String field, String name) {
		EntityField ef = entity.getField(field);
		if (ef == null) {
			throw new IllegalArgumentException("invalid " + name + " field '" + field + "' of entity " + entity.getType());
		}
		return ef;
	}
	
	private Filter where(Entity<?> entity, ComboFilter cf) {
		List<Filter> fs = new ArrayList<Filter>();
		for (Object f : cf.getFilters()) {
			if (f instanceof ValueFilter) {
				ValueFilter vf = (ValueFilter)f;
				if (entity == null) {
					fs.add(whereValueFilter(vf.getField(), vf));
				}
				else {
					EntityField ef = getEntityField(entity, vf.getField(), "where");
					fs.add(whereValueFilter(ef.getColumn(), vf));
				}
			}
			else if (f instanceof ReferFilter) {
				throw Exceptions.unsupported("Field compare is not supported by Google App Engine.");
			}
			else if (f instanceof SimpleFilter) {
				SimpleFilter sf = (SimpleFilter)f;
				if (entity == null) {
					fs.add(whereSimpleFilter(sf.getField(), sf));
				}
				else {
					EntityField ef = getEntityField(entity, sf.getField(), "simple");
					fs.add(whereSimpleFilter(ef.getColumn(), sf));
				}
			}
			else if (f instanceof ComboFilter) {
				fs.add(where(entity, (ComboFilter)f));
			}
		}
		if (fs.size() == 1) {
			return fs.get(0);
		}
		return new CompositeFilter(cf.getLogical() == Logical.OR ? CompositeFilterOperator.OR : CompositeFilterOperator.AND, fs);
	}

	private void where(com.google.appengine.api.datastore.Query gq, Query<?> query) {
		if (!query.hasFilters()) {
			return;
		}
		
		Entity<?> entity = query.getEntity();
		
		Filter filter = where(entity, query.getFilters());

		gq.setFilter(filter);
	}

	private FilterPredicate whereSimpleFilter(String column, SimpleFilter sf) {
		Operator op = sf.getOperator();
		if (op == Operator.IS_NULL) {
			return new FilterPredicate(column, FilterOperator.EQUAL, null);
		}
		else if (op == Operator.IS_NOT_NULL) {
			return new FilterPredicate(column, FilterOperator.NOT_EQUAL, null);
		}
		else {
			throw Exceptions.unsupported("Operator " + op + " is not supported by Google App Engine.");
		}
	}
	
	private Filter whereValueFilter(String column, ValueFilter evc) {
		Operator op = evc.getOperator();
		if (op == Operator.BETWEEN) {
			List<Filter> fs = new ArrayList<Filter>();
			fs.add(new FilterPredicate(column, FilterOperator.GREATER_THAN_OR_EQUAL, evc.getValue(0)));
			fs.add(new FilterPredicate(column, FilterOperator.LESS_THAN_OR_EQUAL, evc.getValue(1)));
			return new CompositeFilter(CompositeFilterOperator.AND, fs);
		}
		else if (op == Operator.NOT_BETWEEN) {
			List<Filter> fs = new ArrayList<Filter>();
			fs.add(new FilterPredicate(column, FilterOperator.LESS_THAN, evc.getValue(0)));
			fs.add(new FilterPredicate(column, FilterOperator.GREATER_THAN, evc.getValue(1)));
			return new CompositeFilter(CompositeFilterOperator.OR, fs);
		}
		else {
			FilterOperator fo = null;
			if (op == Operator.LESS_THAN) {
				fo = FilterOperator.LESS_THAN;
			}
			else if (op == Operator.LESS_EQUAL) {
				fo = FilterOperator.LESS_THAN_OR_EQUAL;
			}
			else if (op == Operator.GREATER_THAN) {
				fo = FilterOperator.GREATER_THAN;
			}
			else if (op == Operator.GREATER_EQUAL) {
				fo = FilterOperator.GREATER_THAN_OR_EQUAL;
			}
			else if (op == Operator.EQUAL) {
				fo = FilterOperator.EQUAL;
			}
			else if (op == Operator.NOT_EQUAL) {
				fo = FilterOperator.NOT_EQUAL;
			}
			else if (op == Operator.IN) {
				fo = FilterOperator.IN;
			}

			if (fo == null) {
				throw Exceptions.unsupported("Operator " + op + " is not supported by Google App Engine.");
			}
			return new FilterPredicate(column, fo, evc.getValue());
		}
	}

	//-----------------------------------------------------------------------------
	private SortDirection getSortDirection(Order order) {
		if (Order.DESC.equals(order)) {
			return SortDirection.DESCENDING;
		}
		else if (Order.ASC.equals(order)) {
			return SortDirection.ASCENDING;
		}
		else {
			throw new IllegalArgumentException("Illegal sort direction: " + order);
		}
	}

	private void order(com.google.appengine.api.datastore.Query gq, Query<?> query) {
		if (!query.hasOrders()) {
			return;
		}
		
		Entity<?> entity = query.getEntity();
		if (entity == null) {
			for (Entry<String, Order> en : query.getOrders().entrySet()) {
				gq.addSort(en.getKey(), getSortDirection(en.getValue()));
			}
		}
		else {
			for (Entry<String, Order> en : query.getOrders().entrySet()) {
				EntityField ef = getEntityField(entity, en.getKey(), "order");
				gq.addSort(ef.getColumn(), getSortDirection(en.getValue()));
			}
		}
	}
	
	//-----------------------------------------------------------------------------
	private PreparedQuery prepareQuery(Query<?> query) {
		return prepareQuery(query, false);
	}

	private PreparedQuery prepareQuery(Query<?> query, boolean keyOnly) {
		com.google.appengine.api.datastore.Query gq = new com.google.appengine.api.datastore.Query(query.getTable());

		if (keyOnly) {
			gq.setKeysOnly();
		}
		// @see https://developers.google.com/appengine/docs/java/datastore/projectionqueries#Java_Limitations_on_projections
//		else {
//			// excludes
//			for (EntityField ef : entity.getFields()) {
//				if (query != null && query.shouldExclude(ef.getName())) {
//					continue;
//				}
//				gq.addProjection(new PropertyProjection(ef.getColumn(), null));
//			}
//		}

		where(gq, query);
		order(gq, query);

		gq.setAncestor(getRootKey());
		if (log.isDebugEnabled()) {
			log.debug("QUERY: " + query);
		}
		return service.prepare(transaction, gq);
	}

	private FetchOptions getFetchOptions(Query q) {
		FetchOptions fo = FetchOptions.Builder.withDefaults();
		if (q.getStart() > 0) {
			fo.offset((int)q.getStart());
		}
		if (q.getLimit() > 0) {
			fo.limit((int)q.getLimit());
		}
		return fo;
	}

	private com.google.appengine.api.datastore.Entity fetchSingle(GenericQuery<?> query) {
		query.setLimit(1);
		PreparedQuery pq = prepareQuery(query);
		FetchOptions fo = getFetchOptions(query);
		Iterator<com.google.appengine.api.datastore.Entity> it = pq.asIterator(fo);
		while (it.hasNext()) {
			return it.next();
		}
		return null;
	}

	//-----------------------------------------------------------------------------
	private void saveEntity(com.google.appengine.api.datastore.Entity ge) throws DaoException {
		if (log.isDebugEnabled()) {
			log.debug("PUT: " + ge);
		}
		service.put(transaction, ge);
	}

	private com.google.appengine.api.datastore.Entity getEntity(Key key) throws DaoException {
		com.google.appengine.api.datastore.Entity ge = null;

		try {
			ge = service.get(transaction, key);
		}
		catch (EntityNotFoundException e) {
		}
		finally {
			if (log.isDebugEnabled()) {
				log.debug("GET: (" + key + ") - " + ge);
			}
		}
		
		return ge;
	}

	private void deleteEntity(Key key) throws DaoException {
		if (log.isDebugEnabled()) {
			log.debug("DELETE: (" + key + ")");
		}
		service.delete(transaction, key);
	}

	@SuppressWarnings("unused")
	private void deleteEntity(Iterable<Key> keys) throws DaoException {
		if (log.isDebugEnabled()) {
			log.debug("DELETE: [" + keys + "]");
		}
		service.delete(transaction, keys);
	}

	//-----------------------------------------------------------------------------
	/**
	 * drop a table if exists
	 * 
	 * @param entity entity
	 */
	@Override
	public void drop(Entity<?> entity) {
		// not support
	}

	/**
	 * drop a table if exists
	 * 
	 * @param table table name
	 */
	@Override
	public void drop(String table) {
		// not support
	}

	//--------------------------------------------------------------------
	/**
	 * create table
	 * 
	 * @param entity entity
	 */
	@Override
	public void create(Entity<?> entity) {
		// not need
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
			return existsByTable(entity.getTable());
		}

		GenericQuery<?> query = createQuery(entity);
		query.setLimit(1);
		queryPrimaryKey(query, keys);
		selectPrimaryKeys(query);
		
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
			return existsByTable(query.getTable());
		}

		autoStart();
		try {
			com.google.appengine.api.datastore.Entity ge = null;
			
			Object id = getQueryIdentity(query);
			if (id != null) {
				if (isValidIdentity(id)) {
					Key k = createKey(query.getEntity(), id);
					ge = getEntity(k);
				}
			}
			else {
				PreparedQuery pq = prepareQuery(query, true);
				ge = pq.asSingleEntity();
			}

			return (ge != null);
		}
		catch (Exception e) {
			throw new DaoException("Failed to fetch query " + query.getTable() + ": " + query, e);
		}
		finally {
			autoClose();
		}
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
		Entity<T> entity = query.getEntity();
		
		autoStart();
		try {
			com.google.appengine.api.datastore.Entity ge = null;
			
			Object id = getQueryIdentity(query);
			if (id != null) {
				if (isValidIdentity(id)) {
					Key k = createKey(entity, id);
					ge = getEntity(k);
				}
			}
			else {
				ge = fetchSingle(query);
			}

			if (ge != null) {
				T data = createEntityData(entity);
				convertEntityToData(ge, data, query);
				return data;
			}
			return null;
		}
		catch (Exception e) {
			throw new DaoException("Failed to fetch entity " + query.getTable() + ": " + query, e);
		}
		finally {
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
		autoStart();
		try {
			PreparedQuery pq = prepareQuery(query, true);
			FetchOptions fo = getFetchOptions(query);
			int cnt = pq.countEntities(fo);
			return cnt;
		}
		catch (Exception e) {
			throw new DaoException("Failed to count entity " + query.getTable() + ": " + query, e);
		}
		finally {
			autoClose();
		}
	}

	//--------------------------------------------------------------------
	/**
	 * select records by the supplied query.
	 * if query is null then select all records.
	 * 
	* @param query query
	 * @return record list
	 */
	@Override
	protected <T> List<T> selectByQuery(GenericQuery<T> query) {
		if (isQueryIdentity(query)) {
			T d = fetchByQuery(query);
			List<T> list = new ArrayList<T>();
			list.add(d);
			return list;
		}

		autoStart();
		try {
			Entity<T> entity = query.getEntity();
			PreparedQuery pq = prepareQuery(query);
			FetchOptions fo = getFetchOptions(query);
			Iterator<com.google.appengine.api.datastore.Entity> it = pq.asIterator(fo);
			List<T> list = new ArrayList<T>();
			while (it.hasNext()) {
				com.google.appengine.api.datastore.Entity ge = it.next();
				T data = createEntityData(entity);
				convertEntityToData(ge, data, query);
				list.add(data);
			}
			return list;
		}
		catch (Exception e) {
			throw new DaoException("Failed to select entity " + query.getTable() + ": " + query, e);
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
	protected <T> long selectByQuery(GenericQuery <T>query, DataHandler<T> callback) {
		if (isQueryIdentity(query)) {
			T d = fetchByQuery(query);
			callback(callback, d, 0);
			return 1;
		}

		autoStart();
		try {
			Entity<T> entity = query.getEntity();
			
			PreparedQuery pq = prepareQuery(query);
			FetchOptions fo = getFetchOptions(query);
			Iterator<com.google.appengine.api.datastore.Entity> it = pq.asIterator(fo);

			long count = 0;
			while (it.hasNext()) {
				com.google.appengine.api.datastore.Entity ge = it.next();
				T data = createEntityData(entity);
				convertEntityToData(ge, data, query);
				if (!callback(callback, data, count++)) {
					break;
				}
			}
			return count;
		}
		catch (Exception e) {
			throw new DaoException("Failed to select entity " + query.getTable() + ": " + query, e);
		}
		finally {
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
		autoStart();
		try {
			PreparedQuery pq = prepareQuery(query, true);
			FetchOptions fo = getFetchOptions(query);
			
			Iterator<com.google.appengine.api.datastore.Entity> it = pq.asIterator(fo);
			int cnt = 0;
			while (it.hasNext()) {
				deleteEntity(it.next().getKey());
				cnt++;
			}
			autoCommit();
			return cnt;
		}
		catch (Exception e) {
			rollback();
			throw new DaoException("Failed to delete entity " + query.getTable() + ": " + query, e);
		}
		finally {
			autoClose();
		}
	}

	//--------------------------------------------------------------------
	protected <T> T insert(Entity<T> entity, T data) throws Exception {
		com.google.appengine.api.datastore.Entity ge;
		Object kid = getDataIdentity(entity, data);
		if (isValidIdentity(kid)) {
			Key key = createKey(entity, kid);
			ge = new com.google.appengine.api.datastore.Entity(key); 
		}
		else {
			ge = new com.google.appengine.api.datastore.Entity(entity.getTable(), getRootKey());
			saveEntity(ge);
			setDataIdentity(entity, data, ge.getKey());
		}

		GenericQuery<?> query = createQuery(entity);
		convertDataToEntity(query, data, ge);
		saveEntity(ge);
		return data;
	}

	//--------------------------------------------------------------------
	/**
	 * update records by the supplied object and query
	 * 
	 * @param data sample object
	 * @param query where condition and update fields filter
	 * @return updated count
	 */
	protected int updatesByQuery(Object data, GenericQuery<?> query, int limit) {
		autoStart();
		try {
			int cnt = 0;
			
			Entity<?> entity = query.getEntity();

			Object id = getQueryIdentity(query);
			if (id != null) {
				if (isValidIdentity(id)) {
					Key k = createKey(entity, id);
					com.google.appengine.api.datastore.Entity ge = getEntity(k);
					if (ge != null) {
						excludePrimaryKeys(query);
						convertDataToEntity(query, data, ge);
						saveEntity(ge);
						cnt = 1;
					}
				}
			}
			else {
				PreparedQuery pq = prepareQuery(query);
				FetchOptions fo = getFetchOptions(query);
				Iterator<com.google.appengine.api.datastore.Entity> it = pq.asIterator(fo);

				excludePrimaryKeys(query);
				while (it.hasNext()) {
					com.google.appengine.api.datastore.Entity ge = it.next();
					convertDataToEntity(query, data, ge);
					saveEntity(ge);
					if (++cnt > limit) {
						throw new Exception("Too many (" + cnt + ") records updated.");
					}
				}
			}

			return cnt;
		}
		catch (Exception e) {
			rollback();
			throw new DaoException("Failed to update entity " + query.getTable() + ": " + query, e);
		}
		finally {
			autoClose();
		}
	}

}
