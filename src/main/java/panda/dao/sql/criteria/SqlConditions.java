package panda.dao.sql.criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.dao.criteria.Expression;
import panda.dao.criteria.Conditions;
import panda.dao.criteria.Operator;
import panda.dao.criteria.SimpleExpression;
import panda.dao.sql.SqlUtils;
import panda.lang.Objects;

/**
 * @author yf.frank.wang@gmail.com
 */
public class SqlConditions implements Conditions {

	private String conjunction = AND;
	
	private List<Expression> expressions;

	/**
	 * constructor
	 */
	public SqlConditions() {
		expressions = new ArrayList<Expression>();
	}

	/**
	 * @return conjunction
	 */
	public String getConjunction() {
		return conjunction;
	}

	/**
	 * @param conjunction the conjunction to set
	 */
	public void setConjunction(String conjunction) {
		if (conjunction == null) {
			throw new IllegalArgumentException(
					"the conjunction is required; it can not be null");
		}

		conjunction = conjunction.toUpperCase();
		if (!AND.equals(conjunction) && !OR.equals(conjunction)) {
			throw new IllegalArgumentException("Invalid conjunction ["
					+ conjunction + "], must be AND/OR");
		}

		this.conjunction = conjunction;
	}

	/**
	 * setConjunctionToAnd
	 */
	public void setConjunctionToAnd() {
		this.conjunction = AND;
	}

	/**
	 * setConjunctionToOr
	 */
	public void setConjunctionToOr() {
		this.conjunction = OR;
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
	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}

	/**
	 * isEmpty
	 * @return true/false
	 */
	public boolean isEmpty() {
		return expressions.isEmpty();
	}
	
	/**
	 * clear
	 */
	public void clear() {
		conjunction = AND;
		expressions.clear();
	}

	private SqlConditions conjunction(String conjunction, boolean force) {
		if (isEmpty()) {
			if (force) {
				throw new IllegalArgumentException("Explicitly appending logical operator " + conjunction + " to empty filters is not allowed.");
			}
		}
		else {
			Expression last = expressions.get(expressions.size() - 1); 
			if (last instanceof SimpleExpression) {
				SimpleExpression ce = (SimpleExpression)last;
				if (Operator.END_PAREN.equals(ce.getOperator())) {
					return addConjunctionExpression(conjunction);
				}
				else {
					if (force) {
						throw new IllegalArgumentException("Explicitly appending explicit logical operator " + conjunction + " after '" + ce.getOperator() + "' is not allowed.");
					}
				}
			}
			else if (last instanceof ConjunctionExpression) {
				if (force) {
					throw new IllegalArgumentException("Explicitly appending logical operator " + conjunction + " after '" + ((ConjunctionExpression)last).getOperator() + "' is not allowed.");
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

	private SqlConditions addParenExpression(String operator) {
		expressions.add(new SimpleExpression(operator));
		return this;
	}

	private SqlConditions addConjunctionExpression(String operator) {
		expressions.add(new ConjunctionExpression(operator));
		return this;
	}

	private SqlConditions addSimpleExpression(String column, String operator) {
		conjunction();
		expressions.add(new SimpleExpression(column, operator));
		return this;
	}

	private SqlConditions addCompareValueExpression(String column, String operator, Object compareValue) {
		conjunction();
		expressions.add(new CompareValueExpression(column, operator, compareValue));
		return this;
	}

	private SqlConditions addCompareColumnExpression(String column, String operator, String compareColumn) {
		conjunction();
		expressions.add(new CompareColumnExpression(column, operator, compareColumn));
		return this;
	}

	private SqlConditions addCompareCollectionExpression(String column, String operator, Object[] values) {
		conjunction();
		expressions.add(new CompareCollectionExpression(column, operator, values));
		return this;
	}

	private SqlConditions addCompareCollectionExpression(String column, String operator, Collection values) {
		conjunction();
		expressions.add(new CompareCollectionExpression(column, operator, values));
		return this;
	}

	private SqlConditions addCompareRanageExpression(String column, String operator, Object minValue, Object maxValue) {
		conjunction();
		expressions.add(new CompareRangeExpression(column, operator, minValue, maxValue));
		return this;
	}

	/**
	 * add conjunction expression
	 * @return this
	 */
	private SqlConditions conjunction() {
		return conjunction(conjunction, false);
	}
	
	/**
	 * add AND expression 
	 * @return this
	 */
	public SqlConditions and() {
		return conjunction(AND, true);
	}
	
	/**
	 * add OR expression 
	 * @return this
	 */
	public SqlConditions or() {
		return conjunction(OR, true);
	}
	
	/**
	 * add open paren ( 
	 * @return this
	 */
	public SqlConditions open() {
		conjunction();
		return addParenExpression(Operator.BEG_PAREN);
	}
	
	/**
	 * add close paren ) 
	 * @return this
	 */
	public SqlConditions close() {
		return addParenExpression(Operator.END_PAREN);
	}
	
	/**
	 * add "column IS NULL" expression
	 * @param column column 
	 * @return this
	 */
	public SqlConditions isNull(String column) {
		return addSimpleExpression(column, Operator.IS_NULL);
	}

	/**
	 * add "column IS NOT NULL" expression 
	 * @param column column 
	 * @return this
	 */
	public SqlConditions isNotNull(String column) {
		return addSimpleExpression(column, Operator.IS_NOT_NULL);
	}

	/**
	 * add "column = value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions equalTo(String column, Object value) {
		return addCompareValueExpression(column, Operator.EQUAL, value);
	}

	/**
	 * add "column &lt;&gt; value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions notEqualTo(String column, Object value) {
		return addCompareValueExpression(column, Operator.NOT_EQUAL, value);
	}

	/**
	 * add "column &gt; value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions greaterThan(String column, Object value) {
		return addCompareValueExpression(column, Operator.GREAT_THAN, value);
	}

	/**
	 * add "column %gt;= value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions greaterThanOrEqualTo(String column, Object value) {
		return addCompareValueExpression(column, Operator.GREAT_EQUAL, value);
	}

	/**
	 * add "column &lt; value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions lessThan(String column, Object value) {
		return addCompareValueExpression(column, Operator.LESS_THAN, value);
	}

	/**
	 * add "column &lt;= value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions lessThanOrEqualTo(String column, Object value) {
		return addCompareValueExpression(column, Operator.LESS_EQUAL, value);
	}

	/**
	 * add "column LIKE value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions like(String column, Object value) {
		return addCompareValueExpression(column, Operator.LIKE, value);
	}

	/**
	 * add "column LIKE %value%" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions match(String column, Object value) {
		return like(column, SqlUtils.stringLike(value.toString()));
	}

	/**
	 * add "column LIKE value%" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions leftMatch(String column, Object value) {
		return like(column, SqlUtils.startsLike(value.toString()));
	}

	/**
	 * add "column LIKE %value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions rightMatch(String column, Object value) {
		return like(column, SqlUtils.endsLike(value.toString()));
	}

	/**
	 * add "column NOT LIKE value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	public SqlConditions notLike(String column, Object value) {
		return addCompareValueExpression(column, Operator.NOT_LIKE, value);
	}

	/**
	 * add "column = compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	public SqlConditions equalToColumn(String column, String compareColumn) {
		return addCompareColumnExpression(column, Operator.EQUAL, compareColumn);
	}

	/**
	 * add "column &lt;&gt; compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	public SqlConditions notEqualToColumn(String column, String compareColumn) {
		return addCompareColumnExpression(column, Operator.NOT_EQUAL, compareColumn);
	}

	/**
	 * add "column %gt; compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	public SqlConditions greaterThanColumn(String column, String compareColumn) {
		return addCompareColumnExpression(column, Operator.GREAT_THAN, compareColumn);
	}

	/**
	 * add "column &gt;= compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	public SqlConditions greaterThanOrEqualToColumn(String column, String compareColumn) {
		return addCompareColumnExpression(column, Operator.GREAT_EQUAL, compareColumn);
	}

	/**
	 * add "column &lt; compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	public SqlConditions lessThanColumn(String column, String compareColumn) {
		return addCompareColumnExpression(column, Operator.LESS_THAN, compareColumn);
	}

	/**
	 * add "column %lt;= compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	public SqlConditions lessThanOrEqualToColumn(String column, String compareColumn) {
		return addCompareColumnExpression(column, Operator.LESS_EQUAL, compareColumn);
	}

	/**
	 * add "column IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
	public SqlConditions in(String column, Object[] values) {
		return addCompareCollectionExpression(column, Operator.IN, values);
	}

	/**
	 * add "column IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
	public SqlConditions in(String column, Collection values) {
		return addCompareCollectionExpression(column, Operator.IN, values);
	}

	/**
	 * add "column NOT IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
	public SqlConditions notIn(String column, Object[] values) {
		return addCompareCollectionExpression(column, Operator.NOT_IN, values);
	}

	/**
	 * add "column NOT IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
	public SqlConditions notIn(String column, Collection values) {
		return addCompareCollectionExpression(column, Operator.NOT_IN, values);
	}

	/**
	 * add "column BETWEEN (value1, value2)" expression
	 * @param column column 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	public SqlConditions between(String column, Object value1, Object value2) {
		return addCompareRanageExpression(column, Operator.BETWEEN, value1, value2);
	}

	/**
	 * add "column NOT BETWEEN (value1, value2)" expression
	 * @param column column 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	public SqlConditions notBetween(String column, Object value1, Object value2) {
		return addCompareRanageExpression(column, Operator.NOT_BETWEEN, value1, value2);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodeBuilder().append(conjunction).append(expressions).toHashCode();
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

		SqlConditions rhs = (SqlConditions) obj;
		return Objects.equalsBuilder()
				.append(conjunction, rhs.conjunction)
				.append(expressions, rhs.expressions)
				.isEquals();
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("conjunction", conjunction)
				.append("expressions", expressions)
				.toString();
	}

}
