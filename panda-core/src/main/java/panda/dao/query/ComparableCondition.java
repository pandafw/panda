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
	public E equalTo(T value) {
		parent.getQuery().equalTo(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E equalToField(String otherField) {
		parent.getQuery().equalToField(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E notEqualTo(T value) {
		parent.getQuery().notEqualTo(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E notEqualToField(String otherField) {
		parent.getQuery().notEqualToField(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E greaterThan(T value) {
		parent.getQuery().greaterThan(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E greaterThanField(String otherField) {
		parent.getQuery().greaterThanField(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E greaterThanOrEqualTo(T value) {
		parent.getQuery().greaterThanOrEqualTo(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E greaterThanOrEqualToField(String otherField) {
		parent.getQuery().greaterThanOrEqualToField(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E lessThan(T value) {
		parent.getQuery().lessThan(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E lessThanField(String otherField) {
		parent.getQuery().lessThanField(field, otherField);
		return parent;
	}

	/**
	 * @param value value
	 * @return parent
	 */
	public E lessThanOrEqualTo(T value) {
		parent.getQuery().lessThanOrEqualTo(field, value);
		return parent;
	}

	/**
	 * @param otherField the field to compare
	 * @return parent
	 */
	public E lessThanOrEqualToField(String otherField) {
		parent.getQuery().lessThanOrEqualToField(field, otherField);
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
	public E notIn(T[] values) {
		parent.getQuery().notIn(field, values);
		return parent;
	}

	/**
	 * @param values value list
	 * @return parent
	 */
	public E notIn(Collection<T> values) {
		parent.getQuery().notIn(field, values);
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
	public E notBetween(T minValue, T maxValue) {
		parent.getQuery().notBetween(field, minValue, maxValue);
		return parent;
	}
}
