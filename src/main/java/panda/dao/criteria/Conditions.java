package panda.dao.criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import panda.lang.Objects;

/**
 * @author yf.frank.wang@gmail.com
 */
public class Conditions {

	private Operator conjunction = Operator.AND;

	private List<Expression> expressions;


	/**
	 * constructor
	 */
	public Conditions() {
		expressions = new ArrayList<Expression>();
	}

	/**
	 * @return conjunction
	 */
	public Operator getConjunction() {
		return conjunction;
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
		conjunction = Operator.AND;
		expressions.clear();
	}

	private Conditions conjunction(Operator conjunction, boolean force) {
		if (isEmpty()) {
			if (force) {
				throw new IllegalArgumentException("Explicitly appending logical operator " + conjunction + " to empty filters is not allowed.");
			}
		}
		else {
			Expression last = expressions.get(expressions.size() - 1); 
			if (last instanceof Expression.Paren) {
				Expression.Paren pe = (Expression.Paren)last;
				if (Operator.CLOSE_PAREN == pe.getOperator()) {
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

	private Conditions addParenExpression(Operator operator) {
		expressions.add(new Expression.Paren(operator));
		return this;
	}

	private Conditions addConjunctionExpression(Operator operator) {
		expressions.add(new Expression.AndOr(operator));
		return this;
	}

	private Conditions addSimpleExpression(String field, Operator operator) {
		conjunction();
		expressions.add(new Expression.Simple(field, operator));
		return this;
	}

	private Conditions addCompareValueExpression(String field, Operator operator, Object compareValue) {
		conjunction();
		expressions.add(new Expression.ValueCompare(field, operator, compareValue));
		return this;
	}

	private Conditions addCompareFieldExpression(String field, Operator operator, String compareField) {
		conjunction();
		expressions.add(new Expression.FieldCompare(field, operator, compareField));
		return this;
	}

	private Conditions addCompareCollectionExpression(String field, Operator operator, Object[] values) {
		conjunction();
		expressions.add(new Expression.ValueCompare(field, operator, values));
		return this;
	}

	private Conditions addCompareCollectionExpression(String field, Operator operator, Collection values) {
		conjunction();
		expressions.add(new Expression.ValueCompare(field, operator, values));
		return this;
	}

	private Conditions addCompareRanageExpression(String field, Operator operator, Object minValue, Object maxValue) {
		conjunction();
		expressions.add(new Expression.ValueCompare(field, operator, new Object[] { minValue, maxValue }));
		return this;
	}

	/**
	 * add conjunction expression
	 * @return this
	 */
	private Conditions conjunction() {
		return conjunction(conjunction, false);
	}
	
	/**
	 * add AND expression 
	 * @return this
	 */
	public Conditions and() {
		return conjunction(Operator.AND, true);
	}
	
	/**
	 * add OR expression 
	 * @return this
	 */
	public Conditions or() {
		return conjunction(Operator.OR, true);
	}
	
	/**
	 * add open paren ( 
	 * @return this
	 */
	public Conditions open() {
		conjunction();
		return addParenExpression(Operator.OPEN_PAREN);
	}
	
	/**
	 * add close paren ) 
	 * @return this
	 */
	public Conditions close() {
		return addParenExpression(Operator.CLOSE_PAREN);
	}
	
	/**
	 * add "field IS NULL" expression
	 * @param field field 
	 * @return this
	 */
	public Conditions isNull(String field) {
		return addSimpleExpression(field, Operator.IS_NULL);
	}

	/**
	 * add "field IS NOT NULL" expression 
	 * @param field field 
	 * @return this
	 */
	public Conditions isNotNull(String field) {
		return addSimpleExpression(field, Operator.IS_NOT_NULL);
	}

	/**
	 * add "field = value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions equalTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.EQUAL, value);
	}

	/**
	 * add "field &lt;&gt; value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions notEqualTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.NOT_EQUAL, value);
	}

	/**
	 * add "field &gt; value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions greaterThan(String field, Object value) {
		return addCompareValueExpression(field, Operator.GREAT_THAN, value);
	}

	/**
	 * add "field %gt;= value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions greaterThanOrEqualTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.GREAT_EQUAL, value);
	}

	/**
	 * add "field &lt; value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions lessThan(String field, Object value) {
		return addCompareValueExpression(field, Operator.LESS_THAN, value);
	}

	/**
	 * add "field &lt;= value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions lessThanOrEqualTo(String field, Object value) {
		return addCompareValueExpression(field, Operator.LESS_EQUAL, value);
	}

	/**
	 * add "field LIKE value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions like(String field, Object value) {
		return addCompareValueExpression(field, Operator.LIKE, value);
	}

	/**
	 * add "field LIKE %value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions match(String field, Object value) {
		return addCompareValueExpression(field, Operator.MATCH, value);
	}

	/**
	 * add "field LIKE value%" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions leftMatch(String field, Object value) {
		return addCompareValueExpression(field, Operator.LEFT_MATCH, value);
	}

	/**
	 * add "field LIKE %value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions rightMatch(String field, Object value) {
		return addCompareValueExpression(field, Operator.RIGHT_MATCH, value);
	}

	/**
	 * add "field NOT LIKE value" expression
	 * @param field field 
	 * @param value value
	 * @return this
	 */
	public Conditions notLike(String field, Object value) {
		return addCompareValueExpression(field, Operator.NOT_LIKE, value);
	}

	/**
	 * add "field = compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Conditions equalToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.EQUAL, compareField);
	}

	/**
	 * add "field &lt;&gt; compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Conditions notEqualToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.NOT_EQUAL, compareField);
	}

	/**
	 * add "field %gt; compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Conditions greaterThanField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.GREAT_THAN, compareField);
	}

	/**
	 * add "field &gt;= compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Conditions greaterThanOrEqualToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.GREAT_EQUAL, compareField);
	}

	/**
	 * add "field &lt; compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Conditions lessThanField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.LESS_THAN, compareField);
	}

	/**
	 * add "field %lt;= compareField" expression
	 * @param field field 
	 * @param compareField field to compare
	 * @return this
	 */
	public Conditions lessThanOrEqualToField(String field, String compareField) {
		return addCompareFieldExpression(field, Operator.LESS_EQUAL, compareField);
	}

	/**
	 * add "field IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public Conditions in(String field, Object[] values) {
		return addCompareCollectionExpression(field, Operator.IN, values);
	}

	/**
	 * add "field IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public Conditions in(String field, Collection values) {
		return addCompareCollectionExpression(field, Operator.IN, values);
	}

	/**
	 * add "field NOT IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public Conditions notIn(String field, Object[] values) {
		return addCompareCollectionExpression(field, Operator.NOT_IN, values);
	}

	/**
	 * add "field NOT IN (value1, value2 ...)" expression
	 * @param field field 
	 * @param values values
	 * @return this
	 */
	public Conditions notIn(String field, Collection values) {
		return addCompareCollectionExpression(field, Operator.NOT_IN, values);
	}

	/**
	 * add "field BETWEEN (value1, value2)" expression
	 * @param field field 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	public Conditions between(String field, Object value1, Object value2) {
		return addCompareRanageExpression(field, Operator.BETWEEN, value1, value2);
	}

	/**
	 * add "field NOT BETWEEN (value1, value2)" expression
	 * @param field field 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	public Conditions notBetween(String field, Object value1, Object value2) {
		return addCompareRanageExpression(field, Operator.NOT_BETWEEN, value1, value2);
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

		Conditions rhs = (Conditions) obj;
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
