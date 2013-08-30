package panda.dao;

import java.util.Map;

/**
 * @author yf.frank.wang@gmail.com
 */
public interface QueryParameter {
	/**
	 * @return the excludes
	 */
	Map<String, Boolean> getExcludes();

	/**
	 * @param excludes the excludes to set
	 */
	void setExcludes(Map<String, Boolean> excludes);

	/**
	 * @return true if the excludes is not empty
	 */
	boolean isHasExcludes();

	/**
	 * @param column exclude column
	 * @return this
	 */
	QueryParameter addExclude(String column);

	/**
	 * @param column exclude column
	 * @return this
	 */
	QueryParameter removeExclude(String column);

	/**
	 * clearExcludes
	 * 
	 * @return this
	 */
	QueryParameter clearExcludes();

	/**
	 * @return conditions
	 */
	Conditions getConditions();

	/**
	 * @param conditions the conditions to set
	 */
	void setConditions(Conditions conditions);

	/**
	 * @return orders
	 */
	Orders getOrders();

	/**
	 * @param orders the orders to set
	 */
	void setOrders(Orders orders);

	/**
	 * @return the start
	 */
	Integer getStart();

	/**
	 * @param start the start to set
	 */
	void setStart(Integer start);

	/**
	 * @return the limit
	 */
	Integer getLimit();

	/**
	 * @param limit the limit to set
	 */
	void setLimit(Integer limit);
}