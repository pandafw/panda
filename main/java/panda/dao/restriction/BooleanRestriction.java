package panda.dao.restriction;

import panda.dao.criteria.Query;


/**
 * @author yf.frank.wang@gmail.com
 */
public class BooleanRestriction<E extends Query> extends ComparableRestriction<E, Boolean> {
	/**
	 * @param example example
	 * @param name name
	 */
	public BooleanRestriction(E example, String name) {
		super(example, name);
	}

	/**
	 * @param example example
	 * @param property property
	 * @param column column
	 * @param alias alias
	 */
	public BooleanRestriction(E example, String property, String column, String alias) {
		super(example, property, column, alias);
	}

	/**
	 * @return example
	 */
	public E isTrue() {
		example.getConditions().equalTo(column, true);
		return example;
	}

	/**
	 * @return example
	 */
	public E isFalse() {
		example.getConditions().equalTo(column, false);
		return example;
	}
}
