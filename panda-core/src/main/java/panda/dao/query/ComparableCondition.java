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
	 * @param value value
	 * @return parent
	 */
	public E eq(T value) {
		parent.getQuery().eq(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E eq2(String otherField) {
		parent.getQuery().eq2(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E ne(T value) {
		parent.getQuery().ne(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E ne2(String otherField) {
		parent.getQuery().ne2(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E gt(T value) {
		parent.getQuery().gt(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E gt2(String otherField) {
		parent.getQuery().gt2(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E ge(T value) {
		parent.getQuery().ge(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E ge2(String otherField) {
		parent.getQuery().ge2(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E lt(T value) {
		parent.getQuery().lt(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E lt2(String otherField) {
		parent.getQuery().lt2(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E le(T value) {
		parent.getQuery().le(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E le2(String otherField) {
		parent.getQuery().le2(field, otherField);
		return parent;
	}

	/**
	 * @param values value array
	 * @return parent
	 */
	public E in(T... values) {
		parent.getQuery().in(field, values);
		return parent;
	}

	/**
	 * @param values value list
	 * @return parent
	 */
	public E in(Collection<T> values) {
		parent.getQuery().in(field, values);
		return parent;
	}

	/**
	 * @param values value list
	 * @return parent
	 */
	public E nin(T[] values) {
		parent.getQuery().nin(field, values);
		return parent;
	}

	/**
	 * @param values value list
	 * @return parent
	 */
	public E nin(Collection<T> values) {
		parent.getQuery().nin(field, values);
		return parent;
	}

	/**
	 * @param minValue minValue
	 * @param maxValue maxValue
	 * @return parent
	 */
	public E between(T minValue, T maxValue) {
		parent.getQuery().between(field, minValue, maxValue);
		return parent;
	}

	/**
	 * @param minValue minValue
	 * @param maxValue maxValue
	 * @return parent
	 */
	public E nbetween(T minValue, T maxValue) {
		parent.getQuery().nbetween(field, minValue, maxValue);
		return parent;
	}
}
