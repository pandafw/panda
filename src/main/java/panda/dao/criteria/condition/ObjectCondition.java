package panda.dao.criteria.condition;



/**
 */
public class ObjectCondition<E extends ConditionQuery> extends AbstractCondition<E> {
	/**
	 * @param parent parent
	 * @param field field
	 */
	public ObjectCondition(E parent, String field) {
		super(parent, field);
	}

	/**
	 * @return parent
	 */
	public E isNull() {
		parent.getQuery().isNull(field);
		return parent;
	}

	/**
	 * @return parent
	 */
	public E isNotNull() {
		parent.getQuery().isNotNull(field);
		return parent;
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
