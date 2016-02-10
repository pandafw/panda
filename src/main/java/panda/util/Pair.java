package panda.util;

import panda.lang.Objects;

/**
 * 简便的名值对实现
 */
public class Pair<T> {
	private String name;
	private T value;

	public Pair() {
	}

	public Pair(String name, T value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getValue() {
		return value;
	}

	public String valueString() {
		return value == null ? null : value.toString();
	}

	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("name", name)
				.append("value", value)
				.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodes(name, value);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Pair rhs = (Pair)obj;
		return Objects.equalsBuilder()
				.append(name, rhs.name)
				.append(value, rhs.value)
				.isEquals();
	}

	/**
	 * Clone
	 * @return Clone Object
	 */
	public Pair<T> clone() {
		Pair<T> clone = new Pair<T>();

		clone.name = this.name;
		clone.value = this.value;
		
		return clone;
	}
}
