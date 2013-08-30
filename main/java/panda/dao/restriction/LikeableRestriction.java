package panda.dao.restriction;

import panda.dao.QueryParameter;


/**
 * @author yf.frank.wang@gmail.com
 */
public class LikeableRestriction<E extends QueryParameter, T> extends ComparableRestriction<E, T> {
	/**
	 * @param example example
	 * @param name name 
	 */
	public LikeableRestriction(E example, String name) {
		super(example, name);
	}

	/**
	 * @param example example
	 * @param property property
	 * @param column column
	 * @param alias alias
	 */
	public LikeableRestriction(E example, String property, String column, String alias) {
		super(example, property, column, alias);
	}

	/**
	 * @param value value
	 * @return example
	 */
	public E like(String value) {
		example.getConditions().like(column, value);
		return example;
	}

	/**
	 * @param value value
	 * @return example
	 */
	public E notLike(String value) {
		example.getConditions().notLike(column, value);
		return example;
	}
}
