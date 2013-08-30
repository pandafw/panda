package panda.dao.restriction;

import panda.dao.QueryParameter;


/**
 * @author yf.frank.wang@gmail.com
 */
public class SortRestriction<E extends QueryParameter> extends AbstractRestriction<E> {
	/**
	 * @param example example
	 * @param name name
	 */
	public SortRestriction(E example, String name) {
		super(example, name);
	}

	/**
	 * @param example example
	 * @param property property
	 * @param column column
	 * @param alias alias
	 */
	public SortRestriction(E example, String property, String column, String alias) {
		super(example, property, column, alias);
	}

	/**
	 * @return example
	 */
	public E asc() {
		example.getOrders().addOrderAsc(alias);
		return example;
	}

	/**
	 * @return example
	 */
	public E desc() {
		example.getOrders().addOrderDesc(alias);
		return example;
	}
}
