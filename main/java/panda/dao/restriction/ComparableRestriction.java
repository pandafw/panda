package panda.dao.restriction;

import java.util.Collection;

import panda.dao.QueryParameter;


/**
 * @author yf.frank.wang@gmail.com
 */
public class ComparableRestriction<E extends QueryParameter, T> extends ObjectRestriction<E> {
	/**
	 * @param example example
	 * @param name name
	 */
	public ComparableRestriction(E example, String name) {
		super(example, name);
	}

	/**
	 * @param example example
	 * @param property property
	 * @param column column
	 * @param alias alias
	 */
	public ComparableRestriction(E example, String property, String column, String alias) {
		super(example, property, column, alias);
	}

	/**
	 * @param value value
	 * @return example
	 */
	public E equalTo(T value) {
		example.getConditions().equalTo(column, value);
		return example;
	}

	/**
	 * @param aColumn the column to compare
	 * @return example
	 */
	public E equalToColumn(String aColumn) {
		example.getConditions().equalToColumn(column, aColumn);
		return example;
	}

	/**
	 * @param value value
	 * @return example
	 */
	public E notEqualTo(T value) {
		example.getConditions().notEqualTo(column, value);
		return example;
	}

	/**
	 * @param aColumn the column to compare
	 * @return example
	 */
	public E notEqualToColumn(String aColumn) {
		example.getConditions().notEqualToColumn(column, aColumn);
		return example;
	}

	/**
	 * @param value value
	 * @return example
	 */
	public E greaterThan(T value) {
		example.getConditions().greaterThan(column, value);
		return example;
	}

	/**
	 * @param aColumn the column to compare
	 * @return example
	 */
	public E greaterThanColumn(String aColumn) {
		example.getConditions().greaterThanColumn(column, aColumn);
		return example;
	}

	/**
	 * @param value value
	 * @return example
	 */
	public E greaterThanOrEqualTo(T value) {
		example.getConditions().greaterThanOrEqualTo(column, value);
		return example;
	}

	/**
	 * @param aColumn the column to compare
	 * @return example
	 */
	public E greaterThanOrEqualToColumn(String aColumn) {
		example.getConditions().greaterThanOrEqualToColumn(column, aColumn);
		return example;
	}

	/**
	 * @param value value
	 * @return example
	 */
	public E lessThan(T value) {
		example.getConditions().lessThan(column, value);
		return example;
	}

	/**
	 * @param aColumn the column to compare
	 * @return example
	 */
	public E lessThanColumn(String aColumn) {
		example.getConditions().lessThanColumn(column, aColumn);
		return example;
	}

	/**
	 * @param value value
	 * @return example
	 */
	public E lessThanOrEqualTo(T value) {
		example.getConditions().lessThanOrEqualTo(column, value);
		return example;
	}

	/**
	 * @param aColumn the column to compare
	 * @return example
	 */
	public E lessThanOrEqualToColumn(String aColumn) {
		example.getConditions().lessThanOrEqualToColumn(column, aColumn);
		return example;
	}

	/**
	 * @param values value array
	 * @return example
	 */
	public E in(T[] values) {
		example.getConditions().in(column, values);
		return example;
	}

	/**
	 * @param values value list
	 * @return example
	 */
	public E in(Collection<T> values) {
		example.getConditions().in(column, values);
		return example;
	}

	/**
	 * @param values value list
	 * @return example
	 */
	public E notIn(T[] values) {
		example.getConditions().notIn(column, values);
		return example;
	}

	/**
	 * @param values value list
	 * @return example
	 */
	public E notIn(Collection<T> values) {
		example.getConditions().notIn(column, values);
		return example;
	}

	/**
	 * @param minValue minValue
	 * @param maxValue maxValue
	 * @return example
	 */
	public E between(T minValue, T maxValue) {
		example.getConditions().between(column, minValue, maxValue);
		return example;
	}

	/**
	 * @param minValue minValue
	 * @param maxValue maxValue
	 * @return example
	 */
	public E notBetween(T minValue, T maxValue) {
		example.getConditions().notBetween(column, minValue, maxValue);
		return example;
	}

	/**
	 * @return example
	 */
	public E asc() {
		example.getOrders().addOrderAsc(column);
		return example;
	}

	/**
	 * @return example
	 */
	public E desc() {
		example.getOrders().addOrderDesc(column);
		return example;
	}
}
