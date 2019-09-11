package panda.dao.query;

import java.util.Collection;

/**
 * @param <E> query target type
 * @param <T> the compare value type
 */
@SuppressWarnings("unchecked")
public class ComparableCondition<E extends EntityQuery, T> extends ObjectCondition<E> {
	/**
	 * @param parent parent
	 * @param field field
	 */
	public ComparableCondition(E parent, String field) {
		super(parent, field);
	}

	/**
	 * add "field = value" expression
	 * @param value value
	 * @return parent
	 */
	public E eq(T value) {
		parent.getQuery().eq(field, value);
		return parent;
	}

	/**
	 * add "field = otherField" expression
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E eqf(String otherField) {
		parent.getQuery().eqf(field, otherField);
		return parent;
	}

	/**
	 * add "field &lt;&gt; value" expression
	 * @param value value
	 * @return parent
	 */
	public E ne(T value) {
		parent.getQuery().ne(field, value);
		return parent;
	}

	/**
	 * add "field &lt;&gt; otherField" expression
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E nef(String otherField) {
		parent.getQuery().nef(field, otherField);
		return parent;
	}

	/**
	 * add "field &gt; value" expression
	 * @param value value
	 * @return parent
	 */
	public E gt(T value) {
		parent.getQuery().gt(field, value);
		return parent;
	}

	/**
	 * add "field &gt; otherField" expression
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E gtf(String otherField) {
		parent.getQuery().gtf(field, otherField);
		return parent;
	}

	/**
	 * add "field %gt;= value" expression
	 * @param value value
	 * @return parent
	 */
	public E ge(T value) {
		parent.getQuery().ge(field, value);
		return parent;
	}

	/**
	 * add "field %gt;= otherField" expression
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E gef(String otherField) {
		parent.getQuery().gef(field, otherField);
		return parent;
	}

	/**
	 * add "field &lt; value" expression
	 * @param value value
	 * @return parent
	 */
	public E lt(T value) {
		parent.getQuery().lt(field, value);
		return parent;
	}

	/**
	 * add "field &lt; otherField" expression
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E ltf(String otherField) {
		parent.getQuery().ltf(field, otherField);
		return parent;
	}

	/**
	 * add "field &lt;= value" expression
	 * @param value value
	 * @return parent
	 */
	public E le(T value) {
		parent.getQuery().le(field, value);
		return parent;
	}

	/**
	 * add "field &lt;= otherField" expression
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E lef(String otherField) {
		parent.getQuery().lef(field, otherField);
		return parent;
	}

	/**
	 * add "field IN (value1, value2 ...)" expression
	 * @param values value array
	 * @return parent
	 */
	public E in(T... values) {
		parent.getQuery().in(field, values);
		return parent;
	}

	/**
	 * add "field IN (value1, value2 ...)" expression
	 * @param values value list
	 * @return parent
	 */
	public E in(Collection<T> values) {
		parent.getQuery().in(field, values);
		return parent;
	}

	/**
	 * add "field NOT IN (value1, value2 ...)" expression
	 * @param values value list
	 * @return parent
	 */
	public E nin(T[] values) {
		parent.getQuery().nin(field, values);
		return parent;
	}

	/**
	 * add "field NOT IN (value1, value2 ...)" expression
	 * @param values value list
	 * @return parent
	 */
	public E nin(Collection<T> values) {
		parent.getQuery().nin(field, values);
		return parent;
	}

	/**
	 * add "field BETWEEN (value1, value2)" expression
	 * @param minValue minValue
	 * @param maxValue maxValue
	 * @return parent
	 */
	public E between(T minValue, T maxValue) {
		parent.getQuery().between(field, minValue, maxValue);
		return parent;
	}

	/**
	 * add "field NOT BETWEEN (value1, value2)" expression
	 * @param minValue minValue
	 * @param maxValue maxValue
	 * @return parent
	 */
	public E nbetween(T minValue, T maxValue) {
		parent.getQuery().nbetween(field, minValue, maxValue);
		return parent;
	}
}
