package panda.dao.query;


/**
 * @param <E> query target type
 */
@SuppressWarnings("rawtypes")
public class StringCondition<E extends EntityQuery> extends ComparableCondition<E, String> {
	/**
	 * @param parent parent
	 * @param field field 
	 */
	public StringCondition(E parent, String field) {
		super(parent, field);
	}

	/**
	 * add "field LIKE value" expression
	 * @param value value
	 * @return parent
	 */
	public E like(String value) {
		parent.getQuery().like(field, value);
		return parent;
	}

	/**
	 * add "field LIKE value" expression
	 * @param value value
	 * @param esc escape char
	 * @return parent
	 */
	public E like(String value, char esc) {
		parent.getQuery().like(field, value, esc);
		return parent;
	}

	/**
	 * add "field NOT LIKE value" expression
	 * @param value value
	 * @return this
	 */
	public E notLike(String value) {
		parent.getQuery().notLike(field, value);
		return parent;
	}

	/**
	 * add "field NOT LIKE value" expression
	 * @param value value
	 * @param esc escape char
	 * @return this
	 */
	public E notLike(String value, char esc) {
		parent.getQuery().notLike(field, value, esc);
		return parent;
	}

	/**
	 * add "field LIKE %value%" expression
	 * @param value value
	 * @return this
	 */
	public E match(String value) {
		parent.getQuery().match(field, value);
		return parent;
	}

	/**
	 * add "field NOT LIKE %value%" expression
	 * @param value value
	 * @return this
	 */
	public E notMatch(String value) {
		parent.getQuery().notMatch(field, value);
		return parent;
	}

	/**
	 * add "field LIKE value%" expression
	 * @param value value
	 * @return this
	 */
	public E leftMatch(String value) {
		parent.getQuery().leftMatch(field, value);
		return parent;
	}

	/**
	 * add "field NOT LIKE value%" expression
	 * @param value value
	 * @return this
	 */
	public E notLeftMatch(String value) {
		parent.getQuery().notLeftMatch(field, value);
		return parent;
	}

	/**
	 * add "field LIKE %value" expression
	 * @param value value
	 * @return this
	 */
	public E rightMatch(String value) {
		parent.getQuery().rightMatch(field, value);
		return parent;
	}

	/**
	 * add "field NOT LIKE %value" expression
	 * @param value value
	 * @return this
	 */
	public E notRightMatch(String value) {
		parent.getQuery().notRightMatch(field, value);
		return parent;
	}
}
