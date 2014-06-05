package panda.dao.query;

import panda.dao.entity.Entity;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 * @param <T> query target type
 */
public interface Query<T> {
	/**
	 * @return the target
	 */
	public Object getTarget();

	/**
	 * @return the entity
	 */
	public Entity<T> getEntity();

	/**
	 * @return the type
	 */
	public Class<T> getType();

	/**
	 * @return the table
	 */
	public String getTable();

	//---------------------------------------------------------------
	// distinct
	//
	public boolean isDistinct();
	
	//---------------------------------------------------------------
	// columns
	//
	/**
	 * @return true if has columns
	 */
	public boolean hasColumns();

	/**
	 * @return columns
	 */
	public Map<String, String> getColumns();

	/**
	 * @param name column name
	 * @return column value
	 */
	public String getColumn(String name);

	/**
	 * @param name field name
	 * @return true if the name should include
	 */
	public boolean shouldInclude(String name);

	/**
	 * @param name field name
	 * @return true if the name should exclude
	 */
	public boolean shouldExclude(String name);

	//---------------------------------------------------------------
	// join
	//
	/**
	 * @return true if has joins
	 */
	public boolean hasJoins();

	/**
	 * @return joins
	 */
	public Map<String, Join> getJoins();

	//---------------------------------------------------------------
	// orders
	//
	/**
	 * @return true if has orders
	 */
	public boolean hasOrders();

	/**
	 * @return orders
	 */
	public Map<String, Order> getOrders();

	//---------------------------------------------------------------
	// groups
	//
	/**
	 * @return true if has groups
	 */
	public boolean hasGroups();

	/**
	 * @return groups
	 */
	public List<String> getGroups();

	//---------------------------------------------------------------
	// start & limit
	//
	/**
	 * @return the start
	 */
	public int getStart();

	/**
	 * @return the limit
	 */
	public int getLimit();

	/**
	 * is this query needs paginate
	 * @return true if start or limit > 0
	 */
	public boolean needsPaginate();

	//---------------------------------------------------------------
	// conditions
	//
	/**
	 * @return conjunction
	 */
	public Operator getConjunction();

	/**
	 * @return expressions
	 */
	public List<Expression> getExpressions();

	/**
	 * @return true if has conditions
	 */
	public boolean hasConditions();
}