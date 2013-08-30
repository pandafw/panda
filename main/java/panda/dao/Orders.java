package panda.dao;

import panda.dao.sql.SqlConstants;



/**
 * Orders
 * @author yf.frank.wang@gmail.com
 */
public interface Orders {
	/**
	 * ASC = "ASC";
	 */
	public static final String ASC = SqlConstants.ASC;
	
	/**
	 * DESC = "DESC";
	 */
	public static final String DESC = SqlConstants.DESC;

	/**
	 * addOrder
	 * @param column		column
	 * @return this
	 */
	Orders addOrder(String column);

	/**
	 * addOrder
	 * @param column		column
	 * @param direction		direction
	 * @return this
	 */
	Orders addOrder(String column, String direction);

	/**
	 * addOrderAsc
	 * @param column column
	 * @return this
	 */
	Orders addOrderAsc(String column);

	/**
	 * addOrderDesc
	 * @param column column
	 * @return this
	 */
	Orders addOrderDesc(String column);
}
