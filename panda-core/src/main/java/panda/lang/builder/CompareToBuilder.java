package panda.lang.builder;

import java.util.Comparator;

import panda.lang.Objects;


/** 
 * Assists in implementing {@link java.lang.Comparable#compareTo(Object)} methods.
 *
 * It is consistent with <code>equals(Object)</code> and
 * <code>hashcode()</code> built with {@link EqualsBuilder} and
 * {@link Objects#hashCode}.</p>
 *
 * <p>Two Objects that compare equal using <code>equals(Object)</code> should normally
 * also compare equal using <code>compareTo(Object)</code>.</p>
 *
 * <p>All relevant fields should be included in the calculation of the
 * comparison. Derived fields may be ignored. The same fields, in the same
 * order, should be used in both <code>compareTo(Object)</code> and
 * <code>equals(Object)</code>.</p>
 *
 * <p>To use this class write code as follows:</p>
 *
 * <pre>
 * public class MyClass {
 *   String field1;
 *   int field2;
 *   boolean field3;
 *
 *   ...
 *
 *   public int compareTo(Object o) {
 *     MyClass myClass = (MyClass) o;
 *     return new CompareToBuilder()
 *       .appendSuper(super.compareTo(o)
 *       .append(this.field1, myClass.field1)
 *       .append(this.field2, myClass.field2)
 *       .append(this.field3, myClass.field3)
 *       .toComparison();
 *   }
 * }
 * </pre>
 *
 *
 * @see java.lang.Comparable
 * @see java.lang.Object#equals(Object)
 * @see java.lang.Object#hashCode()
 * @see EqualsBuilder
 * 
 */
public class CompareToBuilder implements Builder<Integer> {

	/**
	 * Current state of the comparison as appended fields are checked.
	 */
	private int comparison;

	/**
	 * <p>
	 * Constructor for CompareToBuilder.
	 * </p>
	 * <p>
	 * Starts off assuming that the objects are equal. Multiple calls are then made to the various
	 * append methods, followed by a call to {@link #toComparison} to get the result.
	 * </p>
	 */
	public CompareToBuilder() {
		super();
		comparison = 0;
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Appends to the <code>builder</code> the <code>compareTo(Object)</code> result of the
	 * superclass.
	 * </p>
	 * 
	 * @param superCompareTo result of calling <code>super.compareTo(Object)</code>
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder appendSuper(final int superCompareTo) {
		if (comparison != 0) {
			return this;
		}
		comparison = superCompareTo;
		return this;
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Appends to the <code>builder</code> the comparison of two <code>Object</code>s.
	 * </p>
	 * <ol>
	 * <li>Check if <code>lhs == rhs</code></li>
	 * <li>Check if either <code>lhs</code> or <code>rhs</code> is <code>null</code>, a
	 * <code>null</code> object is less than a non-<code>null</code> object</li>
	 * <li>Check the object contents</li>
	 * </ol>
	 * <p>
	 * <code>lhs</code> must either be an array or implement {@link Comparable}.
	 * </p>
	 * 
	 * @param lhs left-hand object
	 * @param rhs right-hand object
	 * @return this - used to chain append calls
	 * @throws ClassCastException if <code>rhs</code> is not assignment-compatible with
	 *             <code>lhs</code>
	 */
	public CompareToBuilder append(final Object lhs, final Object rhs) {
		return append(lhs, rhs, null);
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the comparison of two <code>Object</code>s.
	 * </p>
	 * <ol>
	 * <li>Check if <code>lhs == rhs</code></li>
	 * <li>Check if either <code>lhs</code> or <code>rhs</code> is <code>null</code>, a
	 * <code>null</code> object is less than a non-<code>null</code> object</li>
	 * <li>Check the object contents</li>
	 * </ol>
	 * <p>
	 * If <code>lhs</code> is an array, array comparison methods will be used. Otherwise
	 * <code>comparator</code> will be used to compare the objects. If <code>comparator</code> is
	 * <code>null</code>, <code>lhs</code> must implement {@link Comparable} instead.
	 * </p>
	 * 
	 * @param lhs left-hand object
	 * @param rhs right-hand object
	 * @param comparator <code>Comparator</code> used to compare the objects, <code>null</code>
	 *            means treat lhs as <code>Comparable</code>
	 * @return this - used to chain append calls
	 * @throws ClassCastException if <code>rhs</code> is not assignment-compatible with
	 *             <code>lhs</code>
	 */
	public CompareToBuilder append(final Object lhs, final Object rhs,
			final Comparator<?> comparator) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.getClass().isArray()) {
			// switch on type of array, to dispatch to the correct handler
			// handles multi dimensional arrays
			// throws a ClassCastException if rhs is not the correct array type
			if (lhs instanceof long[]) {
				append((long[])lhs, (long[])rhs);
			}
			else if (lhs instanceof int[]) {
				append((int[])lhs, (int[])rhs);
			}
			else if (lhs instanceof short[]) {
				append((short[])lhs, (short[])rhs);
			}
			else if (lhs instanceof char[]) {
				append((char[])lhs, (char[])rhs);
			}
			else if (lhs instanceof byte[]) {
				append((byte[])lhs, (byte[])rhs);
			}
			else if (lhs instanceof double[]) {
				append((double[])lhs, (double[])rhs);
			}
			else if (lhs instanceof float[]) {
				append((float[])lhs, (float[])rhs);
			}
			else if (lhs instanceof boolean[]) {
				append((boolean[])lhs, (boolean[])rhs);
			}
			else {
				// not an array of primitives
				// throws a ClassCastException if rhs is not an array
				append((Object[])lhs, (Object[])rhs, comparator);
			}
		}
		else {
			// the simple case, not an array, just test the element
			if (comparator == null) {
				@SuppressWarnings("unchecked")
				// assume this can be done; if not throw CCE as per Javadoc
				final Comparable<Object> comparable = (Comparable<Object>)lhs;
				comparison = comparable.compareTo(rhs);
			}
			else {
				@SuppressWarnings("unchecked")
				// assume this can be done; if not throw CCE as per Javadoc
				final Comparator<Object> comparator2 = (Comparator<Object>)comparator;
				comparison = comparator2.compare(lhs, rhs);
			}
		}
		return this;
	}

	// -------------------------------------------------------------------------
	/**
	 * Appends to the <code>builder</code> the comparison of two <code>long</code>s.
	 * 
	 * @param lhs left-hand value
	 * @param rhs right-hand value
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final long lhs, final long rhs) {
		if (comparison != 0) {
			return this;
		}
		comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
		return this;
	}

	/**
	 * Appends to the <code>builder</code> the comparison of two <code>int</code>s.
	 * 
	 * @param lhs left-hand value
	 * @param rhs right-hand value
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final int lhs, final int rhs) {
		if (comparison != 0) {
			return this;
		}
		comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
		return this;
	}

	/**
	 * Appends to the <code>builder</code> the comparison of two <code>short</code>s.
	 * 
	 * @param lhs left-hand value
	 * @param rhs right-hand value
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final short lhs, final short rhs) {
		if (comparison != 0) {
			return this;
		}
		comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
		return this;
	}

	/**
	 * Appends to the <code>builder</code> the comparison of two <code>char</code>s.
	 * 
	 * @param lhs left-hand value
	 * @param rhs right-hand value
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final char lhs, final char rhs) {
		if (comparison != 0) {
			return this;
		}
		comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
		return this;
	}

	/**
	 * Appends to the <code>builder</code> the comparison of two <code>byte</code>s.
	 * 
	 * @param lhs left-hand value
	 * @param rhs right-hand value
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final byte lhs, final byte rhs) {
		if (comparison != 0) {
			return this;
		}
		comparison = ((lhs < rhs) ? -1 : ((lhs > rhs) ? 1 : 0));
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the comparison of two <code>double</code>s.
	 * </p>
	 * <p>
	 * This handles NaNs, Infinities, and <code>-0.0</code>.
	 * </p>
	 * <p>
	 * It is compatible with the hash code generated by <code>HashCodeBuilder</code>.
	 * </p>
	 * 
	 * @param lhs left-hand value
	 * @param rhs right-hand value
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final double lhs, final double rhs) {
		if (comparison != 0) {
			return this;
		}
		comparison = Double.compare(lhs, rhs);
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the comparison of two <code>float</code>s.
	 * </p>
	 * <p>
	 * This handles NaNs, Infinities, and <code>-0.0</code>.
	 * </p>
	 * <p>
	 * It is compatible with the hash code generated by <code>HashCodeBuilder</code>.
	 * </p>
	 * 
	 * @param lhs left-hand value
	 * @param rhs right-hand value
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final float lhs, final float rhs) {
		if (comparison != 0) {
			return this;
		}
		comparison = Float.compare(lhs, rhs);
		return this;
	}

	/**
	 * Appends to the <code>builder</code> the comparison of two <code>booleans</code>s.
	 * 
	 * @param lhs left-hand value
	 * @param rhs right-hand value
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final boolean lhs, final boolean rhs) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == false) {
			comparison = -1;
		}
		else {
			comparison = +1;
		}
		return this;
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>Object</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a short length array is less than a long length array</li>
	 * <li>Check array contents element by element using {@link #append(Object, Object, Comparator)}
	 * </li>
	 * </ol>
	 * <p>
	 * This method will also will be called for the top level of multi-dimensional, ragged, and
	 * multi-typed arrays.
	 * </p>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @return this - used to chain append calls
	 * @throws ClassCastException if <code>rhs</code> is not assignment-compatible with
	 *             <code>lhs</code>
	 */
	public CompareToBuilder append(final Object[] lhs, final Object[] rhs) {
		return append(lhs, rhs, null);
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>Object</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a short length array is less than a long length array</li>
	 * <li>Check array contents element by element using {@link #append(Object, Object, Comparator)}
	 * </li>
	 * </ol>
	 * <p>
	 * This method will also will be called for the top level of multi-dimensional, ragged, and
	 * multi-typed arrays.
	 * </p>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @param comparator <code>Comparator</code> to use to compare the array elements,
	 *            <code>null</code> means to treat <code>lhs</code> elements as
	 *            <code>Comparable</code>.
	 * @return this - used to chain append calls
	 * @throws ClassCastException if <code>rhs</code> is not assignment-compatible with
	 *             <code>lhs</code>
	 */
	public CompareToBuilder append(final Object[] lhs, final Object[] rhs,
			final Comparator<?> comparator) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.length != rhs.length) {
			comparison = (lhs.length < rhs.length) ? -1 : +1;
			return this;
		}
		for (int i = 0; i < lhs.length && comparison == 0; i++) {
			append(lhs[i], rhs[i], comparator);
		}
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>long</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a shorter length array is less than a longer length array</li>
	 * <li>Check array contents element by element using {@link #append(long, long)}</li>
	 * </ol>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final long[] lhs, final long[] rhs) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.length != rhs.length) {
			comparison = (lhs.length < rhs.length) ? -1 : +1;
			return this;
		}
		for (int i = 0; i < lhs.length && comparison == 0; i++) {
			append(lhs[i], rhs[i]);
		}
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>int</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a shorter length array is less than a longer length array</li>
	 * <li>Check array contents element by element using {@link #append(int, int)}</li>
	 * </ol>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final int[] lhs, final int[] rhs) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.length != rhs.length) {
			comparison = (lhs.length < rhs.length) ? -1 : +1;
			return this;
		}
		for (int i = 0; i < lhs.length && comparison == 0; i++) {
			append(lhs[i], rhs[i]);
		}
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>short</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a shorter length array is less than a longer length array</li>
	 * <li>Check array contents element by element using {@link #append(short, short)}</li>
	 * </ol>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final short[] lhs, final short[] rhs) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.length != rhs.length) {
			comparison = (lhs.length < rhs.length) ? -1 : +1;
			return this;
		}
		for (int i = 0; i < lhs.length && comparison == 0; i++) {
			append(lhs[i], rhs[i]);
		}
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>char</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a shorter length array is less than a longer length array</li>
	 * <li>Check array contents element by element using {@link #append(char, char)}</li>
	 * </ol>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final char[] lhs, final char[] rhs) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.length != rhs.length) {
			comparison = (lhs.length < rhs.length) ? -1 : +1;
			return this;
		}
		for (int i = 0; i < lhs.length && comparison == 0; i++) {
			append(lhs[i], rhs[i]);
		}
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>byte</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a shorter length array is less than a longer length array</li>
	 * <li>Check array contents element by element using {@link #append(byte, byte)}</li>
	 * </ol>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final byte[] lhs, final byte[] rhs) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.length != rhs.length) {
			comparison = (lhs.length < rhs.length) ? -1 : +1;
			return this;
		}
		for (int i = 0; i < lhs.length && comparison == 0; i++) {
			append(lhs[i], rhs[i]);
		}
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>double</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a shorter length array is less than a longer length array</li>
	 * <li>Check array contents element by element using {@link #append(double, double)}</li>
	 * </ol>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final double[] lhs, final double[] rhs) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.length != rhs.length) {
			comparison = (lhs.length < rhs.length) ? -1 : +1;
			return this;
		}
		for (int i = 0; i < lhs.length && comparison == 0; i++) {
			append(lhs[i], rhs[i]);
		}
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>float</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a shorter length array is less than a longer length array</li>
	 * <li>Check array contents element by element using {@link #append(float, float)}</li>
	 * </ol>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final float[] lhs, final float[] rhs) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.length != rhs.length) {
			comparison = (lhs.length < rhs.length) ? -1 : +1;
			return this;
		}
		for (int i = 0; i < lhs.length && comparison == 0; i++) {
			append(lhs[i], rhs[i]);
		}
		return this;
	}

	/**
	 * <p>
	 * Appends to the <code>builder</code> the deep comparison of two <code>boolean</code> arrays.
	 * </p>
	 * <ol>
	 * <li>Check if arrays are the same using <code>==</code></li>
	 * <li>Check if for <code>null</code>, <code>null</code> is less than non-<code>null</code></li>
	 * <li>Check array length, a shorter length array is less than a longer length array</li>
	 * <li>Check array contents element by element using {@link #append(boolean, boolean)}</li>
	 * </ol>
	 * 
	 * @param lhs left-hand array
	 * @param rhs right-hand array
	 * @return this - used to chain append calls
	 */
	public CompareToBuilder append(final boolean[] lhs, final boolean[] rhs) {
		if (comparison != 0) {
			return this;
		}
		if (lhs == rhs) {
			return this;
		}
		if (lhs == null) {
			comparison = -1;
			return this;
		}
		if (rhs == null) {
			comparison = +1;
			return this;
		}
		if (lhs.length != rhs.length) {
			comparison = (lhs.length < rhs.length) ? -1 : +1;
			return this;
		}
		for (int i = 0; i < lhs.length && comparison == 0; i++) {
			append(lhs[i], rhs[i]);
		}
		return this;
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns a negative integer, a positive integer, or zero as the <code>builder</code> has
	 * judged the "left-hand" side as less than, greater than, or equal to the "right-hand" side.
	 * 
	 * @return final comparison result
	 */
	public int toComparison() {
		return comparison;
	}

	/**
	 * Returns a negative Integer, a positive Integer, or zero as the <code>builder</code> has
	 * judged the "left-hand" side as less than, greater than, or equal to the "right-hand" side.
	 * 
	 * @return final comparison result as an Integer
	 * @see #toComparison()
	 */
	public Integer build() {
		return Integer.valueOf(toComparison());
	}
}
