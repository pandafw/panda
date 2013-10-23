package panda.dao.criteria.condition;

import java.util.Collection;


/**
 */
public class ComparableCondition<E extends ConditionQuery> extends ObjectCondition<E> {
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
	public E equalTo(Object value) {
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
	public E notEqualTo(Object value) {
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
	public E greaterThan(Object value) {
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
	public E greaterThanOrEqualTo(Object value) {
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
	public E lessThan(Object value) {
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
	public E lessThanOrEqualTo(Object value) {
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
	public E in(Object[] values) {
		parent.getQuery().in(field, values);
		return parent;
	}

	/**
	 * @param values value list
	 * @return parent
	 */
	public E in(Collection<?> values) {
		parent.getQuery().in(field, values);
		return parent;
	}

	/**
	 * @param values value list
	 * @return parent
	 */
	public E notIn(Object[] values) {
		parent.getQuery().notIn(field, values);
		return parent;
	}

	/**
	 * @param values value list
	 * @return parent
	 */
	public E notIn(Collection<?> values) {
		parent.getQuery().notIn(field, values);
		return parent;
	}

	/**
	 * @param minValue minValue
	 * @param maxValue maxValue
	 * @return parent
	 */
	public E between(Object minValue, Object maxValue) {
		parent.getQuery().between(field, minValue, maxValue);
		return parent;
	}

	/**
	 * @param minValue minValue
	 * @param maxValue maxValue
	 * @return parent
	 */
	public E notBetween(Object minValue, Object maxValue) {
		parent.getQuery().notBetween(field, minValue, maxValue);
		return parent;
	}
}
