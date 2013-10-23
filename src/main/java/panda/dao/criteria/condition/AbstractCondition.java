package panda.dao.criteria.condition;



/**
 */
public abstract class AbstractCondition<E extends ConditionQuery> {
	protected E parent;
	protected String field;

	/**
	 * @param parent parent
	 * @param field field
	 */
	public AbstractCondition(E parent, String field) {
		this.parent = parent;
		this.field = field;
	}

	/**
	 * @return parent
	 */
	public E exclude() {
		parent.getQuery().exclude(field);
		return parent;
	}

	/**
	 * @return parent
	 */
	public E include() {
		parent.getQuery().include(field);
		return parent;
	}
}
