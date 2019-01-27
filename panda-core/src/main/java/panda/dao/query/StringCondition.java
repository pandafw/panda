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
	public E nlike(String value) {
		parent.getQuery().nlike(field, value);
		return parent;
	}

	/**
	 * add "field NOT LIKE value" expression
	 * @param value value
	 * @param esc escape char
	 * @return this
	 */
	public E nlike(String value, char esc) {
		parent.getQuery().nlike(field, value, esc);
		return parent;
	}

	/**
	 * add "field LIKE %value%" expression
	 * @param value value
	 * @return this
	 */
	public E contains(String value) {
		parent.getQuery().contains(field, value);
		return parent;
	}

	/**
	 * add "field NOT LIKE %value%" expression
	 * @param value value
	 * @return this
	 */
	public E ncontains(String value) {
		parent.getQuery().ncontains(field, value);
		return parent;
	}

	/**
	 * add "field LIKE value%" expression
	 * @param value value
	 * @return this
	 */
	public E startsWith(String value) {
		parent.getQuery().startsWith(field, value);
		return parent;
	}

	/**
	 * add "field NOT LIKE value%" expression
	 * @param value value
	 * @return this
	 */
	public E nstartsWith(String value) {
		parent.getQuery().nstartsWith(field, value);
		return parent;
	}

	/**
	 * add "field LIKE %value" expression
	 * @param value value
	 * @return this
	 */
	public E endsWith(String value) {
		parent.getQuery().endsWith(field, value);
		return parent;
	}

	/**
	 * add "field NOT LIKE %value" expression
	 * @param value value
	 * @return this
	 */
	public E nendsWith(String value) {
		parent.getQuery().nendsWith(field, value);
		return parent;
	}
}
