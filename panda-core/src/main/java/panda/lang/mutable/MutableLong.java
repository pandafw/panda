package panda.lang.mutable;

/**
 * A mutable <code>long</code> wrapper.
 * <p>
 * Note that as MutableLong does not extend Long, it is not treated by String.format as a Long parameter. 
 * 
 */
public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {

	/**
	 * Required for serialization support.
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 62986528375L;

	/** The mutable value. */
	private long value;

	/**
	 * Constructs a new MutableLong with the default value of zero.
	 */
	public MutableLong() {
		super();
	}

	/**
	 * Constructs a new MutableLong with the specified value.
	 * 
	 * @param value the initial value to store
	 */
	public MutableLong(long value) {
		super();
		this.value = value;
	}

	/**
	 * Constructs a new MutableLong with the specified value.
	 * 
	 * @param value the initial value to store, not null
	 * @throws NullPointerException if the object is null
	 */
	public MutableLong(Number value) {
		super();
		this.value = value.longValue();
	}

	/**
	 * Constructs a new MutableLong parsing the given string.
	 * 
	 * @param value the string to parse, not null
	 * @throws NumberFormatException if the string cannot be parsed into a long
	 */
	public MutableLong(String value) throws NumberFormatException {
		super();
		this.value = Long.parseLong(value);
	}

	// -----------------------------------------------------------------------
	/**
	 * Gets the value as a Long instance.
	 * 
	 * @return the value as a Long, never null
	 */
	public Long getValue() {
		return Long.valueOf(this.value);
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the value to set
	 */
	public void setValue(long value) {
		this.value = value;
	}

	/**
	 * Sets the value from any Number instance.
	 * 
	 * @param value the value to set, not null
	 * @throws NullPointerException if the object is null
	 */
	public void setValue(Number value) {
		this.value = value.longValue();
	}

	// -----------------------------------------------------------------------
	/**
	 * Increments the value.
	 */
	public void increment() {
		value++;
	}

	/**
	 * Decrements the value.
	 */
	public void decrement() {
		value--;
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a value to the value of this instance.
	 * 
	 * @param operand the value to add, not null
	 */
	public void add(long operand) {
		this.value += operand;
	}

	/**
	 * Adds a value to the value of this instance.
	 * 
	 * @param operand the value to add, not null
	 * @throws NullPointerException if the object is null
	 */
	public void add(Number operand) {
		this.value += operand.longValue();
	}

	/**
	 * Subtracts a value from the value of this instance.
	 * 
	 * @param operand the value to subtract, not null
	 */
	public void subtract(long operand) {
		this.value -= operand;
	}

	/**
	 * Subtracts a value from the value of this instance.
	 * 
	 * @param operand the value to subtract, not null
	 * @throws NullPointerException if the object is null
	 */
	public void subtract(Number operand) {
		this.value -= operand.longValue();
	}

	// -----------------------------------------------------------------------
	// shortValue and byteValue rely on Number implementation
	/**
	 * Returns the value of this MutableLong as an int.
	 * 
	 * @return the numeric value represented by this object after conversion to type int.
	 */
	@Override
	public int intValue() {
		return (int)value;
	}

	/**
	 * Returns the value of this MutableLong as a long.
	 * 
	 * @return the numeric value represented by this object after conversion to type long.
	 */
	@Override
	public long longValue() {
		return value;
	}

	/**
	 * Returns the value of this MutableLong as a float.
	 * 
	 * @return the numeric value represented by this object after conversion to type float.
	 */
	@Override
	public float floatValue() {
		return value;
	}

	/**
	 * Returns the value of this MutableLong as a double.
	 * 
	 * @return the numeric value represented by this object after conversion to type double.
	 */
	@Override
	public double doubleValue() {
		return value;
	}

	// -----------------------------------------------------------------------
	/**
	 * Gets this mutable as an instance of Long.
	 * 
	 * @return a Long instance containing the value from this mutable, never null
	 */
	public Long toLong() {
		return Long.valueOf(longValue());
	}

	// -----------------------------------------------------------------------
	/**
	 * Compares this object to the specified object. The result is <code>true</code> if and only if
	 * the argument is not <code>null</code> and is a <code>MutableLong</code> object that contains
	 * the same <code>long</code> value as this object.
	 * 
	 * @param obj the object to compare with, null returns false
	 * @return <code>true</code> if the objects are the same; <code>false</code> otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MutableLong) {
			return value == ((MutableLong)obj).longValue();
		}
		return false;
	}

	/**
	 * Returns a suitable hash code for this mutable.
	 * 
	 * @return a suitable hash code
	 */
	@Override
	public int hashCode() {
		return (int)(value ^ (value >>> 32));
	}

	// -----------------------------------------------------------------------
	/**
	 * Compares this mutable to another in ascending order.
	 * 
	 * @param other the other mutable to compare to, not null
	 * @return negative if this is less, zero if equal, positive if greater
	 */
	public int compareTo(MutableLong other) {
		long anotherVal = other.value;
		return value < anotherVal ? -1 : (value == anotherVal ? 0 : 1);
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the String value of this mutable.
	 * 
	 * @return the mutable value as a string
	 */
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
