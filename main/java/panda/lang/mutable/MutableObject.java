package panda.lang.mutable;

import java.io.Serializable;

/**
 * A mutable <code>Object</code> wrapper.
 * 
 */
public class MutableObject<T> implements Mutable<T>, Serializable {

	/**
	 * Required for serialization support.
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 86241875189L;

	/** The mutable value. */
	private T value;

	/**
	 * Constructs a new MutableObject with the default value of <code>null</code>.
	 */
	public MutableObject() {
		super();
	}

	/**
	 * Constructs a new MutableObject with the specified value.
	 * 
	 * @param value the initial value to store
	 */
	public MutableObject(T value) {
		super();
		this.value = value;
	}

	// -----------------------------------------------------------------------
	/**
	 * Gets the value.
	 * 
	 * @return the value, may be null
	 */
	public T getValue() {
		return this.value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Compares this object against the specified object. The result is <code>true</code> if and
	 * only if the argument is not <code>null</code> and is a <code>MutableObject</code> object that
	 * contains the same <code>T</code> value as this object.
	 * </p>
	 * 
	 * @param obj the object to compare with, <code>null</code> returns <code>false</code>
	 * @return <code>true</code> if the objects are the same; <code>true</code> if the objects have
	 *         equivalent <code>value</code> fields; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (this.getClass() == obj.getClass()) {
			MutableObject<?> that = (MutableObject<?>)obj;
			return this.value.equals(that.value);
		}
		else {
			return false;
		}
	}

	/**
	 * Returns the value's hash code or <code>0</code> if the value is <code>null</code>.
	 * 
	 * @return the value's hash code or <code>0</code> if the value is <code>null</code>.
	 */
	@Override
	public int hashCode() {
		return value == null ? 0 : value.hashCode();
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the String value of this mutable.
	 * 
	 * @return the mutable value as a string
	 */
	@Override
	public String toString() {
		return value == null ? "null" : value.toString();
	}

}
