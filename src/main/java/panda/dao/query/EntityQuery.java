package panda.dao.query;

import java.util.List;
import java.util.Map;

import panda.dao.entity.Entity;
import panda.dao.query.Filter.ComboFilter;
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
	public boolean isDistinct() {
		return query.isDistinct();
	}
	
	@Override
	public Map<String, String> getColumns() {
		return query.getColumns();
	}

	@Override
	public boolean hasColumns() {
		return query.hasColumns();
	}

	/**
	 * @param name column name
	 * @return column value
	 */
	public String getColumn(String name) {
		return query.getColumn(name);
	}

	/**
	 * @param name column name
	 * @param value column value
	 * @return this
	 */
	public Q column(String name, String value) {
		query.column(name, value);
		return (Q)this;
	}

	/**
	 * @param name include name
	 * @return this
	 */
	public Q include(String name) {
		query.include(name);
		return (Q)this;
	}

	/**
	 * @param name the field name to exclude
	 * @return this
	 */
	public Q exclude(String name) {
		query.exclude(name);
		return (Q)this;
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
	public boolean hasJoins() {
		return query.hasJoins();
	}

	@Override
	public Map<String, Join> getJoins() {
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
	public ComboFilter getFilters() {
		return query.getFilters();
	}

	@Override
	public boolean hasFilters() {
		return query.hasFilters();
	}

	//----------------------------------------------------------------------
	public Q clear() {
		query.clear();
		return (Q)this;
	}
	
	//----------------------------------------------------------------------
	public Q distinct() {
		return distinct(true);
	}
	
	public Q distinct(boolean distinct) {
		query.setDistinct(distinct);
		return (Q)this;
	}

	//----------------------------------------------------------------------
	// conjunction
	//----------------------------------------------------------------------
	/**
	 * start with AND
	 * @return this
	 */
	public Q and() {
		query.and();
		return (Q)this;
	}

	/**
	 * start with OR
	 * @return this
	 */
	public Q or() {
		query.or();
		return (Q)this;
	}

	/**
	 * end with AND/OR
	 * @return this
	 */
	public Q end() {
		query.end();
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
	

	//---------------------------------------------------------------
	// groups
	//---------------------------------------------------------------
	/**
	 * @return true if has groups
	 */
	public boolean hasGroups() {
		return query.hasGroups();
	}

	/**
	 * @return groups
	 */
	public List<String> getGroups() {
		return query.getGroups();
	}

	/**
	 * add group
	 * @param column column
	 * @return this
	 */
	public Q groupBy(String ... column) {
		query.groupBy(column);
		return (Q)this;
	}

	//----------------------------------------------------------------------
	// join
	//----------------------------------------------------------------------
	/**
	 * add left join
	 * @param jquery join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q leftJoin(Query<?> jquery, String alias, String ... conditions) {
		query.leftJoin(jquery, alias, conditions);
		return (Q)this;
	}

	/**
	 * add right join
	 * @param jquery join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q rightJoin(Query<?> jquery, String alias, String ... conditions) {
		query.rightJoin(jquery, alias, conditions);
		return (Q)this;
	}

	/**
	 * add inner join
	 * @param jquery join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q innerJoin(Query<?> jquery, String alias, String ... conditions) {
		query.innerJoin(jquery, alias, conditions);
		return (Q)this;
	}

	/**
	 * add join
	 * @param jquery join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q join(Query<?> jquery, String alias, String ... conditions) {
		query.join(query, alias, conditions);
		return (Q)this;
	}

	/**
	 * add join
	 * @param type join type
	 * @param jquery join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public Q join(String type, Query<?> jquery, String alias, String ... conditions) {
		query.join(type, jquery, alias, conditions);
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

