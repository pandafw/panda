package panda.dao.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.dao.entity.Entity;
import panda.lang.Objects;


@SuppressWarnings("unchecked")
public class EntityQuery<T, Q extends EntityQuery> implements Query<T> {
	protected GenericQuery<T> query;

	/**
	 * constructor
	 * @param table table
	 */
	public EntityQuery(Class<T> table) {
		this.query = new GenericQuery<T>(table);
	}

	/**
	 * @param query the query to set
	 */
	public EntityQuery(GenericQuery<T> query) {
		this.query = query;
	}

	/**
	 * @return the query
	 */
	protected GenericQuery<T> getQuery() {
		return query;
	}
	

	//-----------------------------------------------------------------------------------------
	// Query methods implement
	//
	@Override
	public Object getTarget() {
		return query.getTarget();
	}

	@Override
	public Entity<T> getEntity() {
		return query.getEntity();
	}

	@Override
	public Class<T> getType() {
		return query.getType();
	}

	@Override
	public String getTable() {
		return query.getTable();
	}

	@Override
	public Set<String> getIncludes() {
		return query.getIncludes();
	}

	@Override
	public Set<String> getExcludes() {
		return query.getExcludes();
	}

	@Override
	public boolean shouldInclude(String name) {
		return query.shouldInclude(name);
	}

	@Override
	public boolean shouldExclude(String name) {
		return query.shouldExclude(name);
	}

	@Override
	public boolean hasIncludes() {
		return query.hasIncludes();
	}

	@Override
	public boolean hasExcludes() {
		return query.hasExcludes();
	}

	@Override
	public boolean hasJoins() {
		return query.hasJoins();
	}

	@Override
	public List<Join> getJoins() {
		return query.getJoins();
	}

	@Override
	public boolean hasOrders() {
		return query.hasOrders();
	}

	@Override
	public Map<String, Order> getOrders() {
		return query.getOrders();
	}

	@Override
	public int getStart() {
		return query.getStart();
	}

	@Override
	public int getLimit() {
		return query.getLimit();
	}

	@Override
	public boolean needsPaginate() {
		return query.needsPaginate();
	}

	@Override
	public Operator getConjunction() {
		return query.getConjunction();
	}

	@Override
	public List<Expression> getExpressions() {
		return query.getExpressions();
	}

	@Override
	public boolean hasConditions() {
		return query.hasConditions();
	}

	//----------------------------------------------------------------------
	public Q clear() {
		query.clear();
		return (Q)this;
	}
	
	//----------------------------------------------------------------------
	// conjunction
	//----------------------------------------------------------------------
	/**
	 * append begin paren
	 * @return this
	 */
	public Q begin() {
		query.begin();
		return (Q)this;
	}

	/**
	 * append end paren
	 * @return this
	 */
	public Q end() {
		query.end();
		return (Q)this;
	}

	/**
	 * append AND
	 * @return this
	 */
	public Q and() {
		query.and();
		return (Q)this;
	}

	/**
	 * append OR
	 * @return this
	 */
	public Q or() {
		query.or();
		return (Q)this;
	}

	//----------------------------------------------------------------------
	// limit
	//----------------------------------------------------------------------
	/**
	 * set start
	 * @return this
	 */
	public Q start(int start) {
		query.start(start);
		return (Q)this;
	}

	/**
	 * set limit
	 * @return this
	 */
	public Q limit(int limit) {
		query.limit(limit);
		return (Q)this;
	}

	
	//----------------------------------------------------------------------
	// order
	//----------------------------------------------------------------------
	/**
	 * add ascend order
	 * @param column column
	 * @return this
	 */
	public Q orderBy(String column) {
		query.orderBy(column);
		return (Q)this;
	}

	/**
	 * add order
	 * @param name name
	 * @param order order direction
	 * @return this
	 */
	public Q orderBy(String name, Order order) {
		query.orderBy(name, order);
		return (Q)this;
	}

	/**
	 * add order
	 * @param name name
	 * @param order order direction
	 * @return this
	 */
	public Q orderBy(String name, String order) {
		query.orderBy(name, order);
		return (Q)this;
	}

	/**
	 * add order
	 * @param name name
	 * @param ascend direction
	 * @return this
	 */
	public Q orderBy(String name, boolean ascend) {
		query.orderBy(name, ascend);
		return (Q)this;
	}

	/**
	 * add ascend order
	 * @param name		name
	 * @return this
	 */
	public Q orderByAsc(String name) {
		query.orderByAsc(name);
		return (Q)this;
	}

	/**
	 * add descend order
	 * @param name		name
	 * @return this
	 */
	public Q orderByDesc(String name) {
		query.orderByDesc(name);
		return (Q)this;
	}
	
	//----------------------------------------------------------------------
	// join
	//----------------------------------------------------------------------
	/**
	 * add left join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q leftJoin(String table, String alias, String ... conditions) {
		query.leftJoin(table, alias, conditions);
		return (Q)this;
	}

	/**
	 * add left join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @param parameters join parameters
	 * @return this
	 */
	public Q leftJoin(String table, String alias, String[] conditions, Object[] parameters) {
		query.leftJoin(table, alias, conditions, parameters);
		return (Q)this;
	}

	/**
	 * add right join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q rightJoin(String table, String alias, String ... conditions) {
		query.rightJoin(table, alias, conditions);
		return (Q)this;
	}

	/**
	 * add right join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @param parameters join parameters
	 * @return this
	 */
	public Q rightJoin(String table, String alias, String[] conditions, Object[] parameters) {
		query.rightJoin(table, alias, conditions, parameters);
		return (Q)this;
	}

	/**
	 * add inner join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q innerJoin(String table, String alias, String ... conditions) {
		query.innerJoin(table, alias, conditions);
		return (Q)this;
	}

	/**
	 * add inner join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @param parameters join parameters
	 * @return this
	 */
	public Q innerJoin(String table, String alias, String[] conditions, Object[] parameters) {
		query.innerJoin(table, alias, conditions, parameters);
		return (Q)this;
	}

	/**
	 * add join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q join(String table, String alias, String ... conditions) {
		query.join(table, alias, conditions);
		return (Q)this;
	}

	/**
	 * add join
	 * @param type join type
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q join(String type, String table, String alias, String ... conditions) {
		query.join(type, table, alias, conditions);
		return (Q)this;
	}

	/**
	 * add join
	 * @param type join type
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @param parameters join parameters
	 * @return this
	 */
	public Q join(String type, String table, String alias, String[] conditions, Object[] parameters) {
		query.join(type, table, alias, conditions, parameters);
		return (Q)this;
	}

	//-------------------------------------------------------------------------------------------
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return query.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		EntityQuery rhs = (EntityQuery)obj;
		return query.equals(rhs.query);
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("query", query)
				.toString();
	}
}

