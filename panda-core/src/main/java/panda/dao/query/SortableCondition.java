package panda.dao.query;




/**
 * @param <E> query target type
 */
public class SortableCondition<E extends EntityQuery> extends AbstractCondition<E> {
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
