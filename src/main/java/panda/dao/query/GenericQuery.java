package panda.dao.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.dao.entity.Entity;
import panda.lang.Asserts;
import panda.lang.Collections;
import panda.lang.Objects;

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
	protected List<Expression> expressions;
	protected Map<String, Order> orders;
	protected Set<String> includes;
	protected Set<String> excludes;
	protected List<Join> joins;

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

		if (query.hasConditions()) {
			expressions = new ArrayList<Expression>();
			expressions.addAll(query.getExpressions());
		}
		
		if (query.hasOrders()) {
			orders = new LinkedHashMap<String, Order>();
			orders.putAll(query.getOrders());
		}

		if (query.hasIncludes()) {
			includes = new HashSet<String>();
			includes.addAll(query.getIncludes());
		}

		if (query.hasExcludes()) {
			excludes = new HashSet<String>();
			excludes.addAll(query.getExcludes());
		}

		if (query.hasJoins()) {
			joins = new ArrayList<Join>();
			joins.addAll(query.getJoins());
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
		if (expressions != null) {
			expressions.clear();
		}
		if (orders != null) {
			orders.clear();
		};
		if (includes != null) {
			includes.clear();
		};
		if (excludes != null) {
			excludes.clear();
		};
		if (joins != null) {
			joins.clear();
		}
	}

	//---------------------------------------------------------------
	// include & exclude
	//
	/**
	 * @return the includes
	 */
	public Set<String> getIncludes() {
		return includes;
	}

	/**
	 * @return the excludes
	 */
	public Set<String> getExcludes() {
		return excludes;
	}

	/**
	 * @param name field name
	 * @return true if the name should include
	 */
	public boolean shouldInclude(String name) {
		if (Collections.isNotEmpty(excludes) && excludes.contains(name)) {
			return false;
		}
		if (Collections.isNotEmpty(includes) && !includes.contains(name)) {
			return false;
		}
		return true;
	}

	/**
	 * @param name field name
	 * @return true if the name should exclude
	 */
	public boolean shouldExclude(String name) {
		if (Collections.isNotEmpty(excludes) && excludes.contains(name)) {
			return true;
		}
		if (Collections.isNotEmpty(includes) && !includes.contains(name)) {
			return true;
		}
		return false;
	}

	/**
	 * @return true if the includes is not empty
	 */
	public boolean hasIncludes() {
		return includes != null && !includes.isEmpty();
	}

	/**
	 * @return true if the excludes is not empty
	 */
	public boolean hasExcludes() {
		return excludes != null && !excludes.isEmpty();
	}

	/**
	 * @param name include name
	 * @return this
	 */
	public GenericQuery include(String name) {
		addInclude(name);
		removeExclude(name);
		return this;
	}

	/**
	 * @param name the field name to exclude
	 * @return this
	 */
	public GenericQuery exclude(String name) {
		removeInclude(name);
		addExclude(name);
		return this;
	}

	/**
	 * clearIncludes
	 * 
	 * @return this
	 */
	public GenericQuery clearIncludes() {
		if (includes != null) {
			includes.clear();
		}
		return this;
	}

	/**
	 * clearExcludes
	 * 
	 * @return this
	 */
	public GenericQuery clearExcludes() {
		if (excludes == null) {
			excludes.clear();
		}
		return this;
	}

	//---------------------------------------------------------------
	// include
	//
	/**
	 * @param includes the includes to set
	 */
	protected void setIncludes(Set<String> includes) {
		this.includes = includes;
	}

	/**
	 * @param name field name
	 * @return true if the name is included
	 */
	protected boolean isIncluded(String name) {
		return includes != null && includes.contains(name);
	}

	/**
	 * @param name the field name to include
	 * @return this
	 */
	protected GenericQuery addInclude(String name) {
		if (includes == null) {
			includes = new HashSet<String>();
		}
		includes.add(name);
		return this;
	}

	/**
	 * @param name field name
	 * @return this
	 */
	protected GenericQuery removeInclude(String name) {
		if (includes != null) {
			includes.remove(name);
		}
		return this;
	}

	//---------------------------------------------------------------
	// exclude
	//
	/**
	 * @param excludes the excludes to set
	 */
	protected void setExcludes(Set<String> excludes) {
		this.excludes = excludes;
	}

	/**
	 * @param name field name
	 * @return true if the name is excluded
	 */
	protected boolean isExcluded(String name) {
		return excludes != null && excludes.contains(name);
	}
	
	/**
	 * @param name the field name to exclude
	 * @return this
	 */
	protected GenericQuery addExclude(String name) {
		if (excludes == null) {
			excludes = new HashSet<String>();
		}
		excludes.add(name);
		return this;
	}

	/**
	 * @param name the field name
	 * @return this
	 */
	protected GenericQuery removeExclude(String name) {
		if (excludes != null) {
			excludes.remove(name);
		}
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
	public List<Join> getJoins() {
		return joins;
	}

	/**
	 * add left join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery leftJoin(String table, String alias, String ... conditions) {
		return join(Join.LEFT, table, alias, conditions);
	}

	/**
	 * add left join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @param parameters join parameters
	 * @return this
	 */
	public GenericQuery leftJoin(String table, String alias, String[] conditions, Object[] parameters) {
		return join(Join.LEFT, table, alias, conditions, parameters);
	}

	/**
	 * add right join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery rightJoin(String table, String alias, String ... conditions) {
		return join(Join.RIGHT, table, alias, conditions);
	}

	/**
	 * add right join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @param parameters join parameters
	 * @return this
	 */
	public GenericQuery rightJoin(String table, String alias, String[] conditions, Object[] parameters) {
		return join(Join.RIGHT, table, alias, conditions, parameters);
	}

	/**
	 * add inner join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery innerJoin(String table, String alias, String ... conditions) {
		return join(Join.INNER, table, alias, conditions);
	}

	/**
	 * add inner join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @param parameters join parameters
	 * @return this
	 */
	public GenericQuery innerJoin(String table, String alias, String[] conditions, Object[] parameters) {
		return join(Join.INNER, table, alias, conditions, parameters);
	}

	/**
	 * add join
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery join(String table, String alias, String ... conditions) {
		return join(null, table, alias, conditions);
	}

	/**
	 * add join
	 * @param type join type
	 * @param table join table name
	 * @param alias join table alias
	 * @param conditions join conditions
	 * @return this
	 */
	public GenericQuery join(String type, String table, String alias, String ... conditions) {
		return join(type, table, alias, conditions, (Object[])null);
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
	public GenericQuery join(String type, String table, String alias, String[] conditions, Object[] parameters) {
		if (joins == null) {
			joins = new ArrayList<Join>();
		}
		joins.add(new Join(type, table, alias, conditions, parameters));
		return this;
	}

	//---------------------------------------------------------------
	// order
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
	 * @param name		name
	 * @return this
	 */
	public GenericQuery orderByAsc(String name) {
		return orderBy(name, true);
	}

	/**
	 * add descend order
	 * @param name		name
	 * @return this
	 */
	public GenericQuery orderByDesc(String name) {
		return orderBy(name, false);
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
				.append(expressions)
				.append(orders)
				.append(includes)
				.append(excludes)
				.append(joins)
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
				.append(expressions, rhs.expressions)
				.append(orders, rhs.orders)
				.append(includes, rhs.includes)
				.append(excludes, rhs.excludes)
				.append(joins, rhs.joins)
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
				.append("expressions", expressions)
				.append("orders", orders)
				.append("includes", excludes)
				.append("excludes", excludes)
				.append("joins", joins)
				.toString();
	}
}