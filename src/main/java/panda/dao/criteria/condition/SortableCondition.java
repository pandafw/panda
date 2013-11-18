package panda.dao.criteria.condition;

import panda.dao.criteria.QueryWrapper;



/**
 */
public class SortableCondition<E extends QueryWrapper> extends AbstractCondition<E> {
	/**
	 * @param parent parent
	 * @param field field
	 */
	public SortableCondition(E parent, String field) {
		super(parent, field);
	}

	/**
	 * @return parent
	 */
	public E asc() {
		parent.getQuery().orderByAsc(field);
		return parent;
	}

	/**
	 * @return parent
	 */
	public E desc() {
		parent.getQuery().orderByDesc(field);
		return parent;
	}
}
