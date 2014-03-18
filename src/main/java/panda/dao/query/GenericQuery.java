package panda.dao.query;

import panda.dao.entity.Entity;
import panda.lang.Asserts;
import panda.lang.Collections;
import panda.lang.Objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> table
 */
public class GenericQuery<T> implements Query<T>, Cloneable {
	protected Object target;
	protected Operator conjunction = Operator.AND;
	protected int start;
	protected int limit;
	protected Map<String, String> columns;
	protected Map<String, Join> joins;
	protected List<Expression> expressions;
	protected Map<String, Order> orders;
	protected List<String> groups;
	
	protected static final int HAS_INCLUDES = 0x01;
	protected static final int HAS_EXCLUDES = 0x02;
	protected Integer flags;

	/**
	 * constructor
	 * @param type query type
	 */
	public GenericQuery(Class<T> type) {
		this.target = type;
	}

	/**
	 * constructor
	 * @param entity query entity
	 */
	public GenericQuery(Entity<T> entity) {
		this.target = entity;
	}

	/**
	 * @param target query target
	 */
	public GenericQuery(String target) {
		this.target = target;
	}
	
	/**
	 * @param query query to clone
	 */
	public GenericQuery(Query<T> query) {
		target = query.getTarget();
		conjunction = query.getConjunction();
		start = query.getStart();
		limit = query.getLimit();

		if (query.hasColumns()) {
			columns = new LinkedHashMap<String, String>();
			columns.putAll(query.getColumns());
		}

		if (query.hasJoins()) {
			joins = new LinkedHashMap<String, Join>();
			joins.putAll(query.getJoins());
		}

		if (query.hasConditions()) {
			expressions = new ArrayList<Expression>();
			expressions.addAll(query.getExpressions());
		}
		
		if (query.hasOrders()) {
			orders = new LinkedHashMap<String, Order>();
			orders.putAll(query.getOrders());
		}
		
		if (query.hasGroups()) {
			groups = new ArrayList<String>();
			groups.addAll(query.getGroups());
		}
	}

	/**
	 * @return the target
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * @return the entity
	 */
	@SuppressWarnings("unchecked")
	public Entity<T> getEntity() {
		return target instanceof Entity ? (Entity<T>)target : null;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(Entity<T> entity) {
		this.target = entity;
	}

	/**
	 * @return the type
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getType() {
		if (target instanceof Entity) {
			return ((Entity<T>)target).getType();
		}
		if (target instanceof Class) {
			return (Class<T>)target;
		}
		return (Class<T>)Map.class;
	}

	/**
	 * @return the table
	 */
	@SuppressWarnings("unchecked")
	public String getTable() {
		if (target instanceof Entity) {
			return ((Entity<T>)target).getViewName();
		}
		if (target instanceof String) {
			return (String)target;
		}
		return null;
	}

	/**
	 * clear
	 */
	public void clear() {
		conjunction = Operator.AND;
		start = 0;
		limit = 0;
		if (columns != null) {
			columns.clear();
		};
		if (joins != null) {
			joins.clear();
		}
		if (expressions != null) {
			expressions.clear();
		}
		if (orders != null) {
			orders.clear();
		};
		if (groups != null) {
			groups.clear();
		};
	}

	//---------------------------------------------------------------
	// columns
	//
	/**
	 * @return the columns
	 */
	public Map<String, String> getColumns() {
		return columns;
	}

	/**
	 * @return true if the columns is not empty
	 */
	public boolean hasColumns() {
		return columns != null && !columns.isEmpty();
	}

	protected int flags() {
		if (flags == null) {
			flags = 0;
			if (columns != null && !columns.isEmpty()) {
				Collection<String> vs = columns.values();
				if (Collections.contains(vs, "")) {
					flags |= HAS_INCLUDES;
				}
				if (Collections.contains(vs, null)) {
					flags |= HAS_EXCLUDES;
				}
			}
		}
		return flags;
	}
	
	/**
	 * @return true if has included columns
	 */
	protected boolean hasIncludes() {
		return (flags() & HAS_INCLUDES) != 0;
	}
	
	/**
	 * @return true if has excluded columns
	 */
	protected boolean hasExcludes() {
		return (flags() & HAS_EXCLUDES) != 0;
	}

	/**
	 * @param name column name
	 * @return column value
	 */
	public String getColumn(String name) {
		if (columns == null) {
			return null;
		}
		return columns.get(name);
	}

	/**
	 * @param name column name
	 * @param value column value
	 * @return this
	 */
	public GenericQuery column(String name, String value) {
		setColumn(name, value);
		flags = null;
		return this;
	}

	/**
	 * @param name include name
	 * @return this
	 */
	public GenericQuery include(String name) {
		setColumn(name, "");
		flags = null;
		return this;
	}

	/**
	 * @param name the field name to exclude
	 * @return this
	 */
	public GenericQuery exclude(String name) {
		setColumn(name, null);
		flags = null;
		return this;
	}

	/**
	 * @param name field name
	 * @return true if the name should include
	 */
	public boolean shouldInclude(String name) {
		return !hasIncludes() || columns.get(name) != null;
	}

	/**
	 * @param name field name
	 * @return true if the name should exclude
	 */
	public boolean shouldExclude(String name) {
		if (hasIncludes()) {
			return columns.get(name) == null;
		}
		if (hasExcludes()) {
			return columns.containsKey(name) && columns.get(name) == null;
		}
		return false;
	}

	/**
	 * clearColumns
	 * 
	 * @return this
	 */
	public GenericQuery clearColumns() {
		if (columns != null) {
			columns.clear();
		}
		return this;
	}

	/**
	 * @param columns the columns to set
	 */
	protected void setColumns(Map<String, String> columns) {
		this.columns = columns;
	}

	/**
	 * @param name the field name to include
	 * @param value the column value
	 * @return this
	 */
	protected GenericQuery setColumn(String name, String value) {
		if (columns == null) {
			columns = new LinkedHashMap<String, String>();
		}
		columns.put(name, value);
		return this;
	}

	//---------------------------------------------------------------
	// join
	//
	/**
	 * @return true if has orders
	 */
	public boolean hasJoins() {
		return joins != null && !joins.isEmpty();
	}

	/**
	 * @return joins
	 */
	public Map<String, Join> getJoins() {
		return joins;
	}

	/**
	 * add left join
	 * @param query join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery leftJoin(Query<?> query, String alias, String ... conditions) {
		return join(Join.LEFT, query, alias, conditions);
	}

	/**
	 * add right join
	 * @param query join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery rightJoin(Query<?> query, String alias, String ... conditions) {
		return join(Join.RIGHT, query, alias, conditions);
	}

	/**
	 * add inner join
	 * @param query join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery innerJoin(Query<?> query, String alias, String ... conditions) {
		return join(Join.INNER, query, alias, conditions);
	}

	/**
	 * add join
	 * @param query join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery join(Query<?> query, String alias, String ... conditions) {
		return join(null, query, alias, conditions);
	}

	/**
	 * add join
	 * @param type join type
	 * @param query join query
	 * @param alias join alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery join(String type, Query<?> query, String alias, String ... conditions) {
		Asserts.notEmpty(alias, "The parameter alias is empty");
		if (joins == null) {
			joins = new LinkedHashMap<String, Join>();
		}
		joins.put(alias, new Join(type, query, conditions));
		return this;
	}

	//---------------------------------------------------------------
	// orders
	//
	/**
	 * @return true if has orders
	 */
	public boolean hasOrders() {
		return orders != null && !orders.isEmpty();
	}

	/**
	 * @return orders
	 */
	public Map<String, Order> getOrders() {
		return orders;
	}

	/**
	 * add ascend order
	 * @param column column
	 * @return this
	 */
	public GenericQuery orderBy(String column) {
		return orderBy(column, true);
	}

	/**
	 * add order
	 * @param name name
	 * @param order order direction
	 * @return this
	 */
	public GenericQuery orderBy(String name, Order order) {
		Asserts.notEmpty(name, "The parameter name is empty");
		if (orders == null) {
			orders = new LinkedHashMap<String, Order>();
		}
		orders.put(name, order);
		return this;
	}

	/**
	 * add order
	 * @param name name
	 * @param order order direction
	 * @return this
	 */
	public GenericQuery orderBy(String name, String order) {
		return orderBy(name, Order.parse(order));
	}

	/**
	 * add order
	 * @param name name
	 * @param ascend direction
	 * @return this
	 */
	public GenericQuery orderBy(String name, boolean ascend) {
		return orderBy(name, ascend ? Order.ASC : Order.DESC);
	}

	/**
	 * add ascend order
	 * @param name name
	 * @return this
	 */
	public GenericQuery orderByAsc(String name) {
		return orderBy(name, true);
	}

	/**
	 * add descend order
	 * @param name name
	 * @return this
	 */
	public GenericQuery orderByDesc(String name) {
		return orderBy(name, false);
	}

	//---------------------------------------------------------------
	// groups
	//
	/**
	 * @return true if has groups
	 */
	public boolean hasGroups() {
		return groups != null && !groups.isEmpty();
	}

	/**
	 * @return groups
	 */
	public List<String> getGroups() {
		return groups;
	}

	/**
	 * add group
	 * @param column column
	 * @return this
	 */
	public GenericQuery groupBy(String ... column) {
		if (groups == null) {
			groups = new ArrayList<String>();
		}
		for (String s : column) {
			groups.add(s);
		}
		return this;
	}

	//---------------------------------------------------------------
	// start & limit
	//
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		Asserts.isTrue(start >= 0, "The start must >= 0");
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		Asserts.isTrue(limit >= 0, "The limit must >= 0");
		this.limit = limit;
	}

	/**
	 * @param start the start to set
	 */
	public GenericQuery start(int start) {
		setStart(start);
		return this;
	}

	/**
	 * @param limit the limit to set
	 */
	public GenericQuery limit(int limit) {
		setLimit(limit);
		return this;
	}

	/**
	 * is this query needs paginate
	 * @return true if start or limit > 0
	 */
	public boolean needsPaginate() {
		return start > 0 || limit > 0;
	}

	//---------------------------------------------------------------
	// conditions
	//
	/**
	 * @return conjunction
	 */
	public Operator getConjunction() {
		return conjunction;
	}

	/**
	 * @param conjunction the conjunction to set
	 */
	public void setConjunction(String conjunction) {
		setConjunction(Operator.parse(conjunction));
	}

	/**
	 * @param conjunction the conjunction to set
	 */
	public void setConjunction(Operator conjunction) {
		if (conjunction == null) {
			throw new IllegalArgumentException(
					"the conjunction is required; it can not be null");
		}

		if (Operator.AND != conjunction && Operator.OR != conjunction) {
			throw new IllegalArgumentException("Invalid conjunction ["
					+ conjunction + "], must be AND/OR");
		}

		this.conjunction = conjunction;
	}

	/**
	 * setConjunctionToAnd
	 */
	public void setConjunctionToAnd() {
		this.conjunction = Operator.AND;
	}

	/**
	 * setConjunctionToOr
	 */
	public void setConjunctionToOr() {
		this.conjunction = Operator.OR;
	}

	/**
	 * @return expressions
	 */
	public List<Expression> getExpressions() {
		return expressions;
	}

	/**
	 * @param expressions the expressions to set
	 */
	protected void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}

	/**
	 * @return true if has conditions
	 */
	public boolean hasConditions() {
		return expressions != null && !expressions.isEmpty();
	}
	
	protected GenericQuery conjunction(Operator conjunction, boolean force) {
		if (expressions == null || expressions.isEmpty()) {
			if (force) {
				throw new IllegalArgumentException("Explicitly appending logical operator " + conjunction + " to empty filters is not allowed.");
			}
		}
		else {
			Expression last = expressions.get(expressions.size() - 1); 
			if (last instanceof Expression.Paren) {
				Expression.Paren pe = (Expression.Paren)last;
				if (Operator.END_PAREN == pe.getOperator()) {
					return addConjunctionExpression(conjunction);
				}
				else {
					if (force) {
						throw new IllegalArgumentException("Explicitly appending explicit logical operator " + conjunction + " after '" + pe.getOperator() + "' is not allowed.");
					}
				}
			}
			else if (last instanceof Expression.AndOr) {
				if (force) {
					throw new IllegalArgumentException("Explicitly appending logical operator " + conjunction + " after '" + last.getOperator() + "' is not allowed.");
				}
				// 'and' 'or' already appended, so skip
				return this;
			}
			else {
				return addConjunctionExpression(conjunction);
			}
		}
		return this;
	}

	private GenericQuery addParenExpression(Operator operator) {
		expressions().add(new Expression.Paren(operator));
		return this;
	}

	private GenericQuery addConjunctionExpression(Operator operator) {
		expressions().add(new Expression.AndOr(operator));
		return this;
	}

	private GenericQuery addSimpleExpression(String field, Operator operator) {
		conjunction();
		expressions().add(new Expression.Simple(field, operator));
		return this;
	}

	private GenericQuery addCompareValueExpression(String field, Operator operator, Object compareValue) {
		conjunction();
		expressions().add(new Expression.ValueCompare(field, operator, compareValue));
		return this;
	}

	private GenericQuery addCompareFieldExpression(String field, Operator operator, String compareField) {
		conjunction();
		expressions().add(new Expression.FieldCompare(field, operator, compareField));
		return this;
	}

	private GenericQuery addCompareCollectionExpression(String field, Operator operator, Object[] values) {
		conjunction();
		expressions().add(new Expression.ValueCompare(field, operator, values));
		return this;
	}

	private GenericQuery addCompareCollectionExpression(String field, Operator operator, Collection<?> values) {
		conjunction();
		expressions().add(new Expression.ValueCompare(field, operator, values));
		return this;
	}

	private GenericQuery addCompareRanageExpression(String field, Operator operator, Object minValue, Object maxValue) {
		conjunction();
		expressions().add(new Expression.ValueCompare(field, operator, new Object[] { minValue, maxValue }));
		return this;
	}

	/**
	 * add conjunction expression
	 * @return this
	 */
	private GenericQuery conjunction() {
		return conjunction(conjunction, false);
	}
	
	/**
	 * add conjunction expression
	 * @return this
	 */
	private List<Expression> expressions() {
		if (expressions == null) {
			expressions = new ArrayList<Expression>();
		}
		return expressions;
	}
	
	/**
	 * add AND expression 
	 * @return this
	 */
	public GenericQuery and() {
		return conjunction(Operator.AND, true);
	}
	
	/**
	 * add OR expression 
	 * @return this
	 */
	public GenericQuery or() {
		return conjunction(Operator.OR, true);
	}
	
	/**
	 * add open paren ( 
	 * @return this
	 */
	public GenericQuery begin() {
		conjunction();
		return addParenExpression(Operator.BEG_PAREN);
	}
	
	/**
	 * add close paren ) 
	 * @return this
	 */
	public GenericQuery end() {
		return addParenExpression(Operator.END_PAREN);
	}
	
	/**
	 * add "field IS NULL" expression
	 * @param field field 
	 * @return this
	 */
	public GenericQuery isNull(String field) {
		return addSimpleExpression(field, Operator.IS_NULL);
	}

	/**
	 * add "field IS NOT NULL" expression 
	 * @param field field 
	 * @return this
	 */
	public GenericQuery isNotNull(String field) {
		return addSimpleExpression(field, Operator.IS_NOT_NULL);
	}

	/**
	 * add "field = value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery equalTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.EQUAL, value);
	}

	/**
	 * add "field &lt;&gt; value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery notEqualTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.NOT_EQUAL, value);
	}

	/**
	 * add "field &gt; value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery greaterThan(String field, Object value) {
		return addCompareValueExpression(field, Operator.GREATER_THAN, value);
	}

	/**
	 * add "field %gt;= value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery greaterThanOrEqualTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.GREATER_EQUAL, value);
	}

	/**
	 * add "field &lt; value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery lessThan(String field, Object value) {
		return addCompareValueExpression(field, Operator.LESS_THAN, value);
	}

	/**
	 * add "field &lt;= value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery lessThanOrEqualTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.LESS_EQUAL, value);
	}

	/**
	 * add "field LIKE value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery like(String field, String value) {
		return addCompareValueExpression(field, Operator.LIKE, value);
	}

	/**
	 * add "field NOT LIKE value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery notLike(String field, String value) {
		return addCompareValueExpression(field, Operator.NOT_LIKE, value);
	}

	/**
	 * add "field LIKE %value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery match(String field, String value) {
		return addCompareValueExpression(field, Operator.MATCH, value);
	}

	/**
	 * add "field NOT LIKE %value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery notMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.NOT_MATCH, value);
	}

	/**
	 * add "field LIKE value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery leftMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.LEFT_MATCH, value);
	}

	/**
	 * add "field NOT LIKE value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery notLeftMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.NOT_LEFT_MATCH, value);
	}

	/**
	 * add "field LIKE %value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery rightMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.RIGHT_MATCH, value);
	}

	/**
	 * add "field NOT LIKE %value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public GenericQuery notRightMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.NOT_RIGHT_MATCH, value);
	}

	/**
	 * add "field = compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public GenericQuery equalToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.EQUAL, compareField);
	}

	/**
	 * add "field &lt;&gt; compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public GenericQuery notEqualToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.NOT_EQUAL, compareField);
	}

	/**
	 * add "field %gt; compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public GenericQuery greaterThanField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.GREATER_THAN, compareField);
	}

	/**
	 * add "field &gt;= compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public GenericQuery greaterThanOrEqualToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.GREATER_EQUAL, compareField);
	}

	/**
	 * add "field &lt; compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public GenericQuery lessThanField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.LESS_THAN, compareField);
	}

	/**
	 * add "field %lt;= compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public GenericQuery lessThanOrEqualToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.LESS_EQUAL, compareField);
	}

	/**
	 * add "field IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public GenericQuery in(String field, Object[] values) {
		return addCompareCollectionExpression(field, Operator.IN, values);
	}

	/**
	 * add "field IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public GenericQuery in(String field, Collection<?> values) {
		return addCompareCollectionExpression(field, Operator.IN, values);
	}

	/**
	 * add "field NOT IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public GenericQuery notIn(String field, Object[] values) {
		return addCompareCollectionExpression(field, Operator.NOT_IN, values);
	}

	/**
	 * add "field NOT IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public GenericQuery notIn(String field, Collection values) {
		return addCompareCollectionExpression(field, Operator.NOT_IN, values);
	}

	/**
	 * add "field BETWEEN (value1, value2)" expression
	 * @param field field 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	public GenericQuery between(String field, Object value1, Object value2) {
		return addCompareRanageExpression(field, Operator.BETWEEN, value1, value2);
	}

	/**
	 * add "field NOT BETWEEN (value1, value2)" expression
	 * @param field field 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	public GenericQuery notBetween(String field, Object value1, Object value2) {
		return addCompareRanageExpression(field, Operator.NOT_BETWEEN, value1, value2);
	}

	@Override
	public GenericQuery<T> clone() {
		return new GenericQuery<T>(this);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.append(target)
				.append(conjunction)
				.append(start)
				.append(limit)
				.append(columns)
				.append(joins)
				.append(expressions)
				.append(orders)
				.toHashCode();
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
		
		GenericQuery rhs = (GenericQuery) obj;
		return Objects.equalsBuilder()
				.append(target, rhs.target)
				.append(conjunction, rhs.conjunction)
				.append(start, rhs.start)
				.append(limit, rhs.limit)
				.append(columns, rhs.columns)
				.append(joins, rhs.joins)
				.append(expressions, rhs.expressions)
				.append(orders, rhs.orders)
				.isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("target", target)
				.append("conjunction", conjunction)
				.append("start", start)
				.append("limit", limit)
				.append("columns", columns)
				.append("joins", joins)
				.append("expressions", expressions)
				.append("orders", orders)
				.toString();
	}
}