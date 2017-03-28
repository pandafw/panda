package panda.dao.query;




/**
 * @param <E> query target type
 */
public class ObjectCondition<E extends EntityQuery> extends AbstractCondition<E> {
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
	
	/**
	 * @param value column value
	 * @return parent
	 */
	public E column(String value) {
		parent.getQuery().column(field, value);
		return parent;
	}

	/**
	 * @return parent
	 */
	public E group() {
		parent.getQuery().groupBy(field);
		return parent;
	}
}
