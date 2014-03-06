package panda.dao.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.dao.entity.Entity;

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
	// include & exclude
	//
	/**
	 * @return the includes
	 */
	public Set<String> getIncludes();

	/**
	 * @return the excludes
	 */
	public Set<String> getExcludes();

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

	/**
	 * @return true if the includes is not empty
	 */
	public boolean hasIncludes();

	/**
	 * @return true if the excludes is not empty
	 */
	public boolean hasExcludes();


	//---------------------------------------------------------------
	// join
	//
	/**
	 * @return true if has orders
	 */
	public boolean hasJoins();

	/**
	 * @return joins
	 */
	public List<Join> getJoins();

	//---------------------------------------------------------------
	// order
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