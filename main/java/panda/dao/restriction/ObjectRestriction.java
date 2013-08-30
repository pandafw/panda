package panda.dao.restriction;

import panda.dao.QueryParameter;


/**
 * @author yf.frank.wang@gmail.com
 */
public class ObjectRestriction<E extends QueryParameter> extends AbstractRestriction<E> {
	/**
	 * @param example example
	 * @param name name
	 */
	public ObjectRestriction(E example, String name) {
		super(example, name);
	}

	/**
	 * @param example example
	 * @param property property
	 * @param column column
	 * @param alias alias
	 */
	public ObjectRestriction(E example, String property, String column, String alias) {
		super(example, property, column, alias);
	}

	/**
	 * @return example
	 */
	public E isNull() {
		example.getConditions().isNull(column);
		return example;
	}

	/**
	 * @return example
	 */
	public E isNotNull() {
		example.getConditions().isNotNull(column);
		return example;
	}
}
