package panda.dao;

import java.util.Collection;

import panda.dao.sql.SqlConstants;

/**
 * @author yf.frank.wang@gmail.com
 */
public interface Conditions {
	/**
	 * AND = "AND";
	 */
	public static final String AND = SqlConstants.AND;
	
	/**
	 * OR = "OR";
	 */
	public static final String OR = SqlConstants.OR;
	
	/**
	 * @return conjunction
	 */
	String getConjunction();

	/**
	 * @param conjunction the conjunction to set
	 */
	void setConjunction(String conjunction);
	
	/**
	 * setConjunctionToAnd
	 */
	void setConjunctionToAnd();

	/**
	 * setConjunctionToOr
	 */
	void setConjunctionToOr();

	/**
	 * add AND expression 
	 * @return this
	 */
	Conditions and();
	
	/**
	 * add OR expression 
	 * @return this
	 */
	Conditions or();
	
	/**
	 * add open paren ( 
	 * @return this
	 */
	Conditions open();
	
	/**
	 * add close paren ) 
	 * @return this
	 */
	Conditions close();
	
	/**
	 * add "column IS NULL" expression
	 * @param column column 
	 * @return this
	 */
	Conditions isNull(String column);

	/**
	 * add "column IS NOT NULL" expression 
	 * @param column column 
	 * @return this
	 */
	Conditions isNotNull(String column);

	/**
	 * add "column = value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions equalTo(String column, Object value);

	/**
	 * add "column &lt;&gt; value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions notEqualTo(String column, Object value);

	/**
	 * add "column &gt; value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions greaterThan(String column, Object value);
	
	/**
	 * add "column %gt;= value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions greaterThanOrEqualTo(String column, Object value);
	
	/**
	 * add "column &lt; value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions lessThan(String column, Object value);
	
	/**
	 * add "column &lt;= value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions lessThanOrEqualTo(String column, Object value);

	/**
	 * add "column LIKE value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions like(String column, Object value);
	
	/**
	 * add "column LIKE %value%" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions match(String column, Object value);

	/**
	 * add "column LIKE value%" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions leftMatch(String column, Object value);

	/**
	 * add "column LIKE %value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions rightMatch(String column, Object value);

	/**
	 * add "column NOT LIKE value" expression
	 * @param column column 
	 * @param value value
	 * @return this
	 */
	Conditions notLike(String column, Object value);

	/**
	 * add "column = compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	Conditions equalToColumn(String column, String compareColumn);

	/**
	 * add "column &lt;&gt; compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	Conditions notEqualToColumn(String column, String compareColumn);

	/**
	 * add "column %gt; compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	Conditions greaterThanColumn(String column, String compareColumn);

	/**
	 * add "column &gt;= compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	Conditions greaterThanOrEqualToColumn(String column, String compareColumn);

	/**
	 * add "column &lt; compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	Conditions lessThanColumn(String column, String compareColumn);

	/**
	 * add "column %lt;= compareColumn" expression
	 * @param column column 
	 * @param compareColumn column to compare
	 * @return this
	 */
	Conditions lessThanOrEqualToColumn(String column, String compareColumn);

	/**
	 * add "column IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
	Conditions in(String column, Object[] values);

	/**
	 * add "column IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
	Conditions in(String column, Collection values);

	/**
	 * add "column NOT IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
	Conditions notIn(String column, Object[] values);

	/**
	 * add "column NOT IN (value1, value2 ...)" expression
	 * @param column column 
	 * @param values values
	 * @return this
	 */
	Conditions notIn(String column, Collection values);

	/**
	 * add "column BETWEEN (value1, value2)" expression
	 * @param column column 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	Conditions between(String column, Object value1, Object value2);

	/**
	 * add "column NOT BETWEEN (value1, value2)" expression
	 * @param column column 
	 * @param value1 value from
	 * @param value2 value to
	 * @return this
	 */
	Conditions notBetween(String column, Object value1, Object value2);
}
