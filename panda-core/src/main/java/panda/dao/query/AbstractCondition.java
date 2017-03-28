package panda.dao.query;



/**
 * @param <E> query target type
 */
public abstract class AbstractCondition<E extends EntityQuery> {
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
