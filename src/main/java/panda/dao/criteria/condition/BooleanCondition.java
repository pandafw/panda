package panda.dao.criteria.condition;

import panda.dao.criteria.QueryWrapper;



/**
 */
public class BooleanCondition<E extends QueryWrapper> extends ObjectCondition<E> {
	/**
	 * @param parent parent
	 * @param field field
	 */
	public BooleanCondition(E parent, String field) {
		super(parent, field);
	}

	/**
	 * @return parent
	 */
	public E isTrue() {
		parent.getQuery().equalTo(field, true);
		return parent;
	}

	/**
	 * @return parent
	 */
	public E isFalse() {
		parent.getQuery().equalTo(field, false);
		return parent;
	}
}
