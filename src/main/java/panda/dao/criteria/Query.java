package panda.dao.criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.lang.Asserts;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class Query implements Cloneable {
	protected Operator conjunction = Operator.AND;
	protected int start;
	protected int limit;
	protected List<Expression> expressions;
	protected Map<String, Order> orders;
	protected Set<String> includes;
	protected Set<String> excludes;

	public static Query create() {
		return new Query();
	}

	/**
	 * constructor
	 */
	public Query() {
	}

	/**
	 * reset
	 */
	public void reset() {
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
	public Query include(String name) {
		addInclude(name);
		removeExclude(name);
		return this;
	}

	/**
	 * @param name the field name to exclude
	 * @return this
	 */
	public Query exclude(String name) {
		removeInclude(name);
		addExclude(name);
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
	protected Query addInclude(String name) {
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
	protected Query removeInclude(String name) {
		if (includes != null) {
			includes.remove(name);
		}
		return this;
	}

	/**
	 * clearIncludes
	 * 
	 * @return this
	 */
	protected Query clearIncludes() {
		if (includes != null) {
			includes.clear();
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
	protected Query addExclude(String name) {
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
	protected Query removeExclude(String name) {
		if (excludes == null) {
			excludes.remove(name);
		}
		return this;
	}

	/**
	 * clearExcludes
	 * 
	 * @return this
	 */
	protected Query clearExcludes() {
		if (excludes == null) {
			excludes.clear();
		}
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
	public Query orderBy(String column) {
		return orderBy(column, true);
	}

	/**
	 * add order
	 * @param name name
	 * @param order order direction
	 * @return this
	 */
	public Query orderBy(String name, Order order) {
		if (Strings.isNotEmpty(name)) {
			if (orders == null) {
				orders = new LinkedHashMap<String, Order>();
			}
			orders.put(name, order);
		}
		return this;
	}

	/**
	 * add order
	 * @param name name
	 * @param order order direction
	 * @return this
	 */
	public Query orderBy(String name, String order) {
		return orderBy(name, Order.parse(order));
	}

	/**
	 * add order
	 * @param name name
	 * @param ascend direction
	 * @return this
	 */
	public Query orderBy(String name, boolean ascend) {
		return orderBy(name, ascend ? Order.ASC : Order.DESC);
	}

	/**
	 * add ascend order
	 * @param name		name
	 * @return this
	 */
	public Query orderByAsc(String name) {
		return orderBy(name, true);
	}

	/**
	 * add descend order
	 * @param name		name
	 * @return this
	 */
	public Query orderByDesc(String name) {
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
	public Query start(int start) {
		setStart(start);
		return this;
	}

	/**
	 * @param limit the limit to set
	 */
	public Query limit(int limit) {
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
	
	/**
	 * clear
	 */
	public void clear() {
		conjunction = Operator.AND;
		expressions.clear();
	}

	protected Query conjunction(Operator conjunction, boolean force) {
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

	private Query addParenExpression(Operator operator) {
		expressions().add(new Expression.Paren(operator));
		return this;
	}

	private Query addConjunctionExpression(Operator operator) {
		expressions().add(new Expression.AndOr(operator));
		return this;
	}

	private Query addSimpleExpression(String field, Operator operator) {
		conjunction();
		expressions().add(new Expression.Simple(field, operator));
		return this;
	}

	private Query addCompareValueExpression(String field, Operator operator, Object compareValue) {
		conjunction();
		expressions().add(new Expression.ValueCompare(field, operator, compareValue));
		return this;
	}

	private Query addCompareFieldExpression(String field, Operator operator, String compareField) {
		conjunction();
		expressions().add(new Expression.FieldCompare(field, operator, compareField));
		return this;
	}

	private Query addCompareCollectionExpression(String field, Operator operator, Object[] values) {
		conjunction();
		expressions().add(new Expression.ValueCompare(field, operator, values));
		return this;
	}

	private Query addCompareCollectionExpression(String field, Operator operator, Collection<?> values) {
		conjunction();
		expressions().add(new Expression.ValueCompare(field, operator, values));
		return this;
	}

	private Query addCompareRanageExpression(String field, Operator operator, Object minValue, Object maxValue) {
		conjunction();
		expressions().add(new Expression.ValueCompare(field, operator, new Object[] { minValue, maxValue }));
		return this;
	}

	/**
	 * add conjunction expression
	 * @return this
	 */
	private Query conjunction() {
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
	public Query and() {
		return conjunction(Operator.AND, true);
	}
	
	/**
	 * add OR expression 
	 * @return this
	 */
	public Query or() {
		return conjunction(Operator.OR, true);
	}
	
	/**
	 * add open paren ( 
	 * @return this
	 */
	public Query begin() {
		conjunction();
		return addParenExpression(Operator.BEG_PAREN);
	}
	
	/**
	 * add close paren ) 
	 * @return this
	 */
	public Query end() {
		return addParenExpression(Operator.END_PAREN);
	}
	
	/**
	 * add "field IS NULL" expression
	 * @param field field 
	 * @return this
	 */
	public Query isNull(String field) {
		return addSimpleExpression(field, Operator.IS_NULL);
	}

	/**
	 * add "field IS NOT NULL" expression 
	 * @param field field 
	 * @return this
	 */
	public Query isNotNull(String field) {
		return addSimpleExpression(field, Operator.IS_NOT_NULL);
	}

	/**
	 * add "field = value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query equalTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.EQUAL, value);
	}

	/**
	 * add "field &lt;&gt; value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query notEqualTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.NOT_EQUAL, value);
	}

	/**
	 * add "field &gt; value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query greaterThan(String field, Object value) {
		return addCompareValueExpression(field, Operator.GREATER_THAN, value);
	}

	/**
	 * add "field %gt;= value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query greaterThanOrEqualTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.GREATER_EQUAL, value);
	}

	/**
	 * add "field &lt; value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query lessThan(String field, Object value) {
		return addCompareValueExpression(field, Operator.LESS_THAN, value);
	}

	/**
	 * add "field &lt;= value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query lessThanOrEqualTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.LESS_EQUAL, value);
	}

	/**
	 * add "field LIKE value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query like(String field, String value) {
		return addCompareValueExpression(field, Operator.LIKE, value);
	}

	/**
	 * add "field NOT LIKE value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query notLike(String field, String value) {
		return addCompareValueExpression(field, Operator.NOT_LIKE, value);
	}

	/**
	 * add "field LIKE %value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query match(String field, String value) {
		return addCompareValueExpression(field, Operator.MATCH, value);
	}

	/**
	 * add "field NOT LIKE %value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query notMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.NOT_MATCH, value);
	}

	/**
	 * add "field LIKE value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query leftMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.LEFT_MATCH, value);
	}

	/**
	 * add "field NOT LIKE value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query notLeftMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.NOT_LEFT_MATCH, value);
	}

	/**
	 * add "field LIKE %value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query rightMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.RIGHT_MATCH, value);
	}

	/**
	 * add "field NOT LIKE %value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Query notRightMatch(String field, String value) {
		return addCompareValueExpression(field, Operator.NOT_RIGHT_MATCH, value);
	}

	/**
	 * add "field = compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Query equalToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.EQUAL, compareField);
	}

	/**
	 * add "field &lt;&gt; compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Query notEqualToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.NOT_EQUAL, compareField);
	}

	/**
	 * add "field %gt; compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Query greaterThanField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.GREATER_THAN, compareField);
	}

	/**
	 * add "field &gt;= compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Query greaterThanOrEqualToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.GREATER_EQUAL, compareField);
	}

	/**
	 * add "field &lt; compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Query lessThanField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.LESS_THAN, compareField);
	}

	/**
	 * add "field %lt;= compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Query lessThanOrEqualToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.LESS_EQUAL, compareField);
	}

	/**
	 * add "field IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public Query in(String field, Object[] values) {
		return addCompareCollectionExpression(field, Operator.IN, values);
	}

	/**
	 * add "field IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public Query in(String field, Collection<?> values) {
		return addCompareCollectionExpression(field, Operator.IN, values);
	}

	/**
	 * add "field NOT IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public Query notIn(String field, Object[] values) {
		return addCompareCollectionExpression(field, Operator.NOT_IN, values);
	}

	/**
	 * add "field NOT IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public Query notIn(String field, Collection values) {
		return addCompareCollectionExpression(field, Operator.NOT_IN, values);
	}

	/**
	 * add "field BETWEEN (value1, value2)" expression
	 * @param field field 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	public Query between(String field, Object value1, Object value2) {
		return addCompareRanageExpression(field, Operator.BETWEEN, value1, value2);
	}

	/**
	 * add "field NOT BETWEEN (value1, value2)" expression
	 * @param field field 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	public Query notBetween(String field, Object value1, Object value2) {
		return addCompareRanageExpression(field, Operator.NOT_BETWEEN, value1, value2);
	}

	@Override
	public Query clone() {
		Query q = new Query();

		q.conjunction = conjunction;
		q.start = start;
		q.limit = limit;

		if (Collections.isNotEmpty(expressions)) {
			q.expressions = new ArrayList<Expression>();
			q.expressions.addAll(expressions);
		}
		
		if (orders != null && !orders.isEmpty()) {
			q.orders = new LinkedHashMap<String, Order>();
			q.orders.putAll(orders);
		}

		if (Collections.isNotEmpty(includes)) {
			q.includes = new HashSet<String>();
			q.includes.addAll(includes);
		}

		if (Collections.isNotEmpty(excludes)) {
			q.excludes = new HashSet<String>();
			q.excludes.addAll(excludes);
		}
		
		return q;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder()
				.append(conjunction)
				.append(start)
				.append(limit)
				.append(expressions)
				.append(orders)
				.append(includes)
				.append(excludes)
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
		
		Query rhs = (Query) obj;
		return Objects.equalsBuilder()
				.append(conjunction, rhs.conjunction)
				.append(start, rhs.start)
				.append(limit, rhs.limit)
				.append(expressions, rhs.expressions)
				.append(orders, rhs.orders)
				.append(includes, rhs.includes)
				.append(excludes, rhs.excludes)
				.isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("conjunction", conjunction)
				.append("start", start)
				.append("limit", limit)
				.append("expressions", expressions)
				.append("orders", orders)
				.append("includes", excludes)
				.append("excludes", excludes)
				.toString();
	}
}