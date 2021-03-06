package panda.dao.query;




/**
 * @param <E> query target type
 */
@SuppressWarnings("rawtypes")
public class BooleanCondition<E extends EntityQuery> extends ObjectCondition<E> {
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
		parent.getQuery().eq(field, true);
		return parent;
	}

	/**
	 * @return parent
	 */
	public E isFalse() {
		parent.getQuery().eq(field, false);
		return parent;
	}
}
