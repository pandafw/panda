package panda.lang;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.TreeSet;

import panda.lang.builder.EqualsBuilder;
import panda.lang.builder.ToStringBuilder;
import panda.lang.builder.ToStringStyle;
import panda.lang.mutable.MutableInt;
import panda.lang.reflect.Types;

/**
 * utility class for array
 * 
 */
public abstract class Arrays {
	/**
	 * An empty immutable {@code Object} array.
	 */
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
	/**
	 * An empty immutable {@code Class} array.
	 */
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
	/**
	 * An empty immutable {@code String} array.
	 */
	public static final String[] EMPTY_STRING_ARRAY = new String[0];
	/**
	 * An empty immutable {@code long} array.
	 */
	public static final long[] EMPTY_LONG_ARRAY = new long[0];
	/**
	 * An empty immutable {@code Long} array.
	 */
	public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
	/**
	 * An empty immutable {@code int} array.
	 */
	public static final int[] EMPTY_INT_ARRAY = new int[0];
	/**
	 * An empty immutable {@code Integer} array.
	 */
	public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
	/**
	 * An empty immutable {@code short} array.
	 */
	public static final short[] EMPTY_SHORT_ARRAY = new short[0];
	/**
	 * An empty immutable {@code Short} array.
	 */
	public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
	/**
	 * An empty immutable {@code byte} array.
	 */
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	/**
	 * An empty immutable {@code Byte} array.
	 */
	public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
	/**
	 * An empty immutable {@code double} array.
	 */
	public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
	/**
	 * An empty immutable {@code Double} array.
	 */
	public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
	/**
	 * An empty immutable {@code float} array.
	 */
	public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
	/**
	 * An empty immutable {@code Float} array.
	 */
	public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
	/**
	 * An empty immutable {@code boolean} array.
	 */
	public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
	/**
	 * An empty immutable {@code Boolean} array.
	 */
	public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
	/**
	 * An empty immutable {@code char} array.
	 */
	public static final char[] EMPTY_CHAR_ARRAY = new char[0];
	/**
	 * An empty immutable {@code Character} array.
	 */
	public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

	/**
	 * The index value when an element is not found in a list or array: {@code -1}. This value is
	 * returned by methods in this class and can also be used in comparisons with values returned by
	 * various method from {@link java.util.List}.
	 */
	public static final int INDEX_NOT_FOUND = -1;

	// hashCode
	// -----------------------------------------------------------------------
	/**
	 * Returns a hash code based on the contents of the specified array. For any two <tt>long</tt>
	 * arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it is also the case
	 * that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is the same value that would be obtained by invoking the
	 * {@link List#hashCode() <tt>hashCode</tt>} method on a {@link List} containing a sequence of
	 * {@link Long} instances representing the elements of <tt>a</tt> in the same order. If
	 * <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose hash value to compute
	 * @return a content-based hash code for <tt>a</tt>
	 */
	public static int hashCode(long a[]) {
		return java.util.Arrays.hashCode(a);
	}

	/**
	 * Returns a hash code based on the contents of the specified array. For any two non-null
	 * <tt>int</tt> arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it is
	 * also the case that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is the same value that would be obtained by invoking the
	 * {@link List#hashCode() <tt>hashCode</tt>} method on a {@link List} containing a sequence of
	 * {@link Integer} instances representing the elements of <tt>a</tt> in the same order. If
	 * <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose hash value to compute
	 * @return a content-based hash code for <tt>a</tt>
	 */
	public static int hashCode(int a[]) {
		return java.util.Arrays.hashCode(a);
	}

	/**
	 * Returns a hash code based on the contents of the specified array. For any two <tt>short</tt>
	 * arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it is also the case
	 * that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is the same value that would be obtained by invoking the
	 * {@link List#hashCode() <tt>hashCode</tt>} method on a {@link List} containing a sequence of
	 * {@link Short} instances representing the elements of <tt>a</tt> in the same order. If
	 * <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose hash value to compute
	 * @return a content-based hash code for <tt>a</tt>
	 */
	public static int hashCode(short a[]) {
		return java.util.Arrays.hashCode(a);
	}

	/**
	 * Returns a hash code based on the contents of the specified array. For any two <tt>char</tt>
	 * arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it is also the case
	 * that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is the same value that would be obtained by invoking the
	 * {@link List#hashCode() <tt>hashCode</tt>} method on a {@link List} containing a sequence of
	 * {@link Character} instances representing the elements of <tt>a</tt> in the same order. If
	 * <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose hash value to compute
	 * @return a content-based hash code for <tt>a</tt>
	 */
	public static int hashCode(char a[]) {
		return java.util.Arrays.hashCode(a);
	}

	/**
	 * Returns a hash code based on the contents of the specified array. For any two <tt>byte</tt>
	 * arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it is also the case
	 * that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is the same value that would be obtained by invoking the
	 * {@link List#hashCode() <tt>hashCode</tt>} method on a {@link List} containing a sequence of
	 * {@link Byte} instances representing the elements of <tt>a</tt> in the same order. If
	 * <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose hash value to compute
	 * @return a content-based hash code for <tt>a</tt>
	 */
	public static int hashCode(byte a[]) {
		return java.util.Arrays.hashCode(a);
	}

	/**
	 * Returns a hash code based on the contents of the specified array. For any two
	 * <tt>boolean</tt> arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it
	 * is also the case that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is the same value that would be obtained by invoking the
	 * {@link List#hashCode() <tt>hashCode</tt>} method on a {@link List} containing a sequence of
	 * {@link Boolean} instances representing the elements of <tt>a</tt> in the same order. If
	 * <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose hash value to compute
	 * @return a content-based hash code for <tt>a</tt>
	 */
	public static int hashCode(boolean a[]) {
		return java.util.Arrays.hashCode(a);
	}

	/**
	 * Returns a hash code based on the contents of the specified array. For any two <tt>float</tt>
	 * arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it is also the case
	 * that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is the same value that would be obtained by invoking the
	 * {@link List#hashCode() <tt>hashCode</tt>} method on a {@link List} containing a sequence of
	 * {@link Float} instances representing the elements of <tt>a</tt> in the same order. If
	 * <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose hash value to compute
	 * @return a content-based hash code for <tt>a</tt>
	 */
	public static int hashCode(float a[]) {
		return java.util.Arrays.hashCode(a);
	}

	/**
	 * Returns a hash code based on the contents of the specified array. For any two <tt>double</tt>
	 * arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it is also the case
	 * that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is the same value that would be obtained by invoking the
	 * {@link List#hashCode() <tt>hashCode</tt>} method on a {@link List} containing a sequence of
	 * {@link Double} instances representing the elements of <tt>a</tt> in the same order. If
	 * <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose hash value to compute
	 * @return a content-based hash code for <tt>a</tt>
	 */
	public static int hashCode(double a[]) {
		return java.util.Arrays.hashCode(a);
	}

	/**
	 * Returns a hash code based on the contents of the specified array. If the array contains other
	 * arrays as elements, the hash code is based on their identities rather than their contents. It
	 * is therefore acceptable to invoke this method on an array that contains itself as an element,
	 * either directly or indirectly through one or more levels of arrays.
	 * <p>
	 * For any two arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it is
	 * also the case that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is equal to the value that would be returned by
	 * <tt>Arrays.asList(a).hashCode()</tt>, unless <tt>a</tt> is <tt>null</tt>, in which case
	 * <tt>0</tt> is returned.
	 * 
	 * @param a the array whose content-based hash code to compute
	 * @return a content-based hash code for <tt>a</tt>
	 * @see #deepHashCode(Object[])
	 */
	public static int hashCode(Object a[]) {
		return java.util.Arrays.hashCode(a);
	}

	/**
	 * Returns a hash code based on the contents of the specified array. If the array contains other
	 * arrays as elements, the hash code is based on their identities rather than their contents. It
	 * is therefore acceptable to invoke this method on an array that contains itself as an element,
	 * either directly or indirectly through one or more levels of arrays.
	 * <p>
	 * For any two arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.equals(a, b)</tt>, it is
	 * also the case that <tt>Arrays.hashCode(a) == Arrays.hashCode(b)</tt>.
	 * <p>
	 * The value returned by this method is equal to the value that would be returned by
	 * <tt>Arrays.asList(a).hashCode()</tt>, unless <tt>a</tt> is <tt>null</tt>, in which case
	 * <tt>0</tt> is returned.
	 * 
	 * @param a the array whose content-based hash code to compute
	 * @return a content-based hash code for <tt>a</tt>
	 * @see #deepHashCode(Object)
	 */
	public static int hashCode(Object a) {
		if (a == null) {
			return 0;
		}
		
		if (a.getClass().isArray()) {
			// 'Switch' on type of array, to dispatch to the correct handler
			// This handles multi dimensional arrays
			if (a instanceof long[]) {
				return hashCode((long[])a);
			}
			
			if (a instanceof int[]) {
				return hashCode((int[])a);
			}
			
			if (a instanceof short[]) {
				return hashCode((short[])a);
			}
			
			if (a instanceof char[]) {
				return hashCode((char[])a);
			}
			
			if (a instanceof byte[]) {
				return hashCode((byte[])a);
			}
			
			if (a instanceof double[]) {
				return hashCode((double[])a);
			}
			
			if (a instanceof float[]) {
				return hashCode((float[])a);
			}
			
			if (a instanceof boolean[]) {
				return hashCode((boolean[])a);
			}
			
			// Not an array of primitives
			return hashCode((Object[])a);
		}

		return Objects.hashCode(a);
	}

	/**
	 * Returns a hash code based on the "deep contents" of the specified array. If the array
	 * contains other arrays as elements, the hash code is based on their contents and so on, ad
	 * infinitum. It is therefore unacceptable to invoke this method on an array that contains
	 * itself as an element, either directly or indirectly through one or more levels of arrays. The
	 * behavior of such an invocation is undefined.
	 * <p>
	 * For any two arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.deepEquals(a, b)</tt>, it
	 * is also the case that <tt>Arrays.deepHashCode(a) == Arrays.deepHashCode(b)</tt>.
	 * <p>
	 * The computation of the value returned by this method is similar to that of the value returned
	 * by {@link List#hashCode()} on a list containing the same elements as <tt>a</tt> in the same
	 * order, with one difference: If an element <tt>e</tt> of <tt>a</tt> is itself an array, its
	 * hash code is computed not by calling <tt>e.hashCode()</tt>, but as by calling the appropriate
	 * overloading of <tt>Arrays.hashCode(e)</tt> if <tt>e</tt> is an array of a primitive type, or
	 * as by calling <tt>Arrays.deepHashCode(e)</tt> recursively if <tt>e</tt> is an array of a
	 * reference type. If <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose deep-content-based hash code to compute
	 * @return a deep-content-based hash code for <tt>a</tt>
	 * @see #hashCode(Object[])
	 */
	public static int deepHashCode(Object a[]) {
		return java.util.Arrays.deepHashCode(a);
	}

	/**
	 * Returns a hash code based on the "deep contents" of the specified array. If the array
	 * contains other arrays as elements, the hash code is based on their contents and so on, ad
	 * infinitum. It is therefore unacceptable to invoke this method on an array that contains
	 * itself as an element, either directly or indirectly through one or more levels of arrays. The
	 * behavior of such an invocation is undefined.
	 * <p>
	 * For any two arrays <tt>a</tt> and <tt>b</tt> such that <tt>Arrays.deepEquals(a, b)</tt>, it
	 * is also the case that <tt>Arrays.deepHashCode(a) == Arrays.deepHashCode(b)</tt>.
	 * <p>
	 * The computation of the value returned by this method is similar to that of the value returned
	 * by {@link List#hashCode()} on a list containing the same elements as <tt>a</tt> in the same
	 * order, with one difference: If an element <tt>e</tt> of <tt>a</tt> is itself an array, its
	 * hash code is computed not by calling <tt>e.hashCode()</tt>, but as by calling the appropriate
	 * overloading of <tt>Arrays.hashCode(e)</tt> if <tt>e</tt> is an array of a primitive type, or
	 * as by calling <tt>Arrays.deepHashCode(e)</tt> recursively if <tt>e</tt> is an array of a
	 * reference type. If <tt>a</tt> is <tt>null</tt>, this method returns 0.
	 * 
	 * @param a the array whose deep-content-based hash code to compute
	 * @return a deep-content-based hash code for <tt>a</tt>
	 * @see #hashCode(Object)
	 */
	public static int deepHashCode(Object a) {
		if (a == null) {
			return 0;
		}
		
		if (a.getClass().isArray()) {
			// 'Switch' on type of array, to dispatch to the correct handler
			// This handles multi dimensional arrays
			if (a instanceof long[]) {
				return deepHashCode((long[])a);
			}
			
			if (a instanceof int[]) {
				return deepHashCode((int[])a);
			}
			
			if (a instanceof short[]) {
				return deepHashCode((short[])a);
			}
			
			if (a instanceof char[]) {
				return deepHashCode((char[])a);
			}
			
			if (a instanceof byte[]) {
				return deepHashCode((byte[])a);
			}
			
			if (a instanceof double[]) {
				return deepHashCode((double[])a);
			}
			
			if (a instanceof float[]) {
				return deepHashCode((float[])a);
			}
			
			if (a instanceof boolean[]) {
				return deepHashCode((boolean[])a);
			}
			
			// Not an array of primitives
			return deepHashCode((Object[])a);
		}

		return Objects.hashCode(a);
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays are <i>deeply equal</i> to one another.
	 * Unlike the {@link #equals(Object[],Object[])} method, this method is appropriate for use with
	 * nested arrays of arbitrary depth.
	 * <p>
	 * Two array references are considered deeply equal if both are <tt>null</tt>, or if they refer
	 * to arrays that contain the same number of elements and all corresponding pairs of elements in
	 * the two arrays are deeply equal.
	 * <p>
	 * Two possibly <tt>null</tt> elements <tt>e1</tt> and <tt>e2</tt> are deeply equal if any of
	 * the following conditions hold:
	 * <ul>
	 * <li> <tt>e1</tt> and <tt>e2</tt> are both arrays of object reference types, and
	 * <tt>Arrays.deepEquals(e1, e2) would return true</tt>
	 * <li> <tt>e1</tt> and <tt>e2</tt> are arrays of the same primitive type, and the appropriate
	 * overloading of <tt>Arrays.equals(e1, e2)</tt> would return true.
	 * <li> <tt>e1 == e2</tt>
	 * <li> <tt>e1.equals(e2)</tt> would return true.
	 * </ul>
	 * Note that this definition permits <tt>null</tt> elements at any depth.
	 * <p>
	 * If either of the specified arrays contain themselves as elements either directly or
	 * indirectly through one or more levels of arrays, the behavior of this method is undefined.
	 * 
	 * @param a1 one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 * @see #equals(Object[],Object[])
	 */
	public static boolean deepEquals(Object[] a1, Object[] a2) {
		return java.util.Arrays.deepEquals(a1, a2);
	}

	// Generic array
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Create a type-safe generic array.
	 * </p>
	 * <p>
	 * The Java language does not allow an array to be created from a generic type:
	 * </p>
	 * 
	 * <pre>
	 * public static &lt;T&gt; T[] createAnArray(int size) {
	 * 	return new T[size]; // compiler error here
	 * }
	 * 
	 * public static &lt;T&gt; T[] createAnArray(int size) {
	 * 	return (T[])new Object[size]; // ClassCastException at runtime
	 * }
	 * </pre>
	 * <p>
	 * Therefore new arrays of generic types can be created with this method. For example, an array
	 * of Strings can be created:
	 * </p>
	 * 
	 * <pre>
	 * String[] array = Arrays.toArray(&quot;1&quot;, &quot;2&quot;);
	 * String[] emptyArray = Arrays.&lt;String&gt; toArray();
	 * </pre>
	 * <p>
	 * The method is typically used in scenarios, where the caller itself uses generic types that
	 * have to be combined into an array.
	 * </p>
	 * <p>
	 * Note, this method makes only sense to provide arguments of the same type so that the compiler
	 * can deduce the type of the array itself. While it is possible to select the type explicitly
	 * like in
	 * <code>Number[] array = Arrays.&lt;Number&gt;toArray(Integer.valueOf(42), Double.valueOf(Math.PI))</code>
	 * , there is no real advantage when compared to
	 * <code>new Number[] {Integer.valueOf(42), Double.valueOf(Math.PI)}</code>.
	 * </p>
	 * 
	 * @param <T> the array's element type
	 * @param items the varargs array items, null allowed
	 * @return the array, not null unless a null array is passed in
	 */
	public static <T> T[] toArray(final T... items) {
		return items;
	}

	// Clone
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Shallow clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * The objects in the array are not cloned, thus there is no special handling for
	 * multi-dimensional arrays.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array to shallow clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static <T> T[] clone(final T[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static long[] clone(final long[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static int[] clone(final int[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static short[] clone(final short[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static char[] clone(final char[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static byte[] clone(final byte[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static double[] clone(final double[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static float[] clone(final float[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Clones an array returning a typecast result and handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static boolean[] clone(final boolean[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	// nullToEmpty
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static Object[] nullToEmpty(final Object[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 * @since 3.2
	 */
	public static Class<?>[] nullToEmpty(final Class<?>[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_CLASS_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static String[] nullToEmpty(final String[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_STRING_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static long[] nullToEmpty(final long[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_LONG_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static int[] nullToEmpty(final int[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_INT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static short[] nullToEmpty(final short[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_SHORT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static char[] nullToEmpty(final char[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_CHAR_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static byte[] nullToEmpty(final byte[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_BYTE_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static double[] nullToEmpty(final double[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_DOUBLE_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static float[] nullToEmpty(final float[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_FLOAT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static boolean[] nullToEmpty(final boolean[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_BOOLEAN_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static Long[] nullToEmpty(final Long[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_LONG_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static Integer[] nullToEmpty(final Integer[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_INTEGER_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static Short[] nullToEmpty(final Short[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_SHORT_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static Character[] nullToEmpty(final Character[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_CHARACTER_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static Byte[] nullToEmpty(final Byte[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_BYTE_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static Double[] nullToEmpty(final Double[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_DOUBLE_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static Float[] nullToEmpty(final Float[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_FLOAT_OBJECT_ARRAY;
		}
		return array;
	}

	/**
	 * <p>
	 * Defensive programming technique to change a {@code null} reference to an empty one.
	 * </p>
	 * <p>
	 * This method returns an empty array for a {@code null} input array.
	 * </p>
	 * <p>
	 * As a memory optimizing technique an empty array passed in will be overridden with the empty
	 * {@code public static} references in this class.
	 * </p>
	 * 
	 * @param array the array to check for {@code null} or empty
	 * @return the same array, {@code public static} empty array if {@code null} or empty input
	 */
	public static Boolean[] nullToEmpty(final Boolean[] array) {
		if (array == null || array.length == 0) {
			return EMPTY_BOOLEAN_OBJECT_ARRAY;
		}
		return array;
	}

	// Subarrays
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Produces a new array containing the elements between the start and end indices.
	 * </p>
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces null output.
	 * </p>
	 * <p>
	 * The component type of the subarray is always the same as that of the input array. Thus, if
	 * the input is an array of type {@code Date}, the following usage is envisaged:
	 * </p>
	 * 
	 * <pre>
	 * Date[] someDates = (Date[])Arrays.subarray(allDates, 2, 5);
	 * </pre>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned subarray.
	 *            Undervalue (&lt; startIndex) produces empty array, overvalue (&gt;array.length) is
	 *            demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 */
	public static <T> T[] subarray(final T[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		final Class<?> type = array.getClass().getComponentType();
		if (newSize <= 0) {
			@SuppressWarnings("unchecked")
			// OK, because array is of type T
			final T[] emptyArray = (T[])Array.newInstance(type, 0);
			return emptyArray;
		}
		@SuppressWarnings("unchecked")
		// OK, because array is of type T
		final T[] subarray = (T[])Array.newInstance(type, newSize);
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new {@code long} array containing the elements between the start and end indices.
	 * </p>
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned subarray.
	 *            Undervalue (&lt; startIndex) produces empty array, overvalue (&gt;array.length) is
	 *            demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 */
	public static long[] subarray(final long[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_LONG_ARRAY;
		}

		final long[] subarray = new long[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new {@code int} array containing the elements between the start and end indices.
	 * </p>
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned subarray.
	 *            Undervalue (&lt; startIndex) produces empty array, overvalue (&gt;array.length) is
	 *            demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 */
	public static int[] subarray(final int[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_INT_ARRAY;
		}

		final int[] subarray = new int[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new {@code short} array containing the elements between the start and end indices.
	 * </p>
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned subarray.
	 *            Undervalue (&lt; startIndex) produces empty array, overvalue (&gt;array.length) is
	 *            demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 */
	public static short[] subarray(final short[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_SHORT_ARRAY;
		}

		final short[] subarray = new short[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new {@code char} array containing the elements between the start and end indices.
	 * </p>
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned subarray.
	 *            Undervalue (&lt; startIndex) produces empty array, overvalue (&gt;array.length) is
	 *            demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 */
	public static char[] subarray(final char[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_CHAR_ARRAY;
		}

		final char[] subarray = new char[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new {@code byte} array containing the elements between the start and end indices.
	 * </p>
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned subarray.
	 *            Undervalue (&lt; startIndex) produces empty array, overvalue (&gt;array.length) is
	 *            demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 */
	public static byte[] subarray(final byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_BYTE_ARRAY;
		}

		final byte[] subarray = new byte[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new {@code double} array containing the elements between the start and end
	 * indices.
	 * </p>
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned subarray.
	 *            Undervalue (&lt; startIndex) produces empty array, overvalue (&gt;array.length) is
	 *            demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 */
	public static double[] subarray(final double[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_DOUBLE_ARRAY;
		}

		final double[] subarray = new double[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new {@code float} array containing the elements between the start and end indices.
	 * </p>
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned subarray.
	 *            Undervalue (&lt; startIndex) produces empty array, overvalue (&gt;array.length) is
	 *            demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 */
	public static float[] subarray(final float[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_FLOAT_ARRAY;
		}

		final float[] subarray = new float[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	/**
	 * <p>
	 * Produces a new {@code boolean} array containing the elements between the start and end
	 * indices.
	 * </p>
	 * <p>
	 * The start index is inclusive, the end index exclusive. Null array input produces null output.
	 * </p>
	 * 
	 * @param array the array
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in an empty array.
	 * @param endIndexExclusive elements up to endIndex-1 are present in the returned subarray.
	 *            Undervalue (&lt; startIndex) produces empty array, overvalue (&gt;array.length) is
	 *            demoted to array length.
	 * @return a new array containing the elements between the start and end indices.
	 */
	public static boolean[] subarray(final boolean[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return null;
		}
		if (startIndexInclusive < 0) {
			startIndexInclusive = 0;
		}
		if (endIndexExclusive > array.length) {
			endIndexExclusive = array.length;
		}
		final int newSize = endIndexExclusive - startIndexInclusive;
		if (newSize <= 0) {
			return EMPTY_BOOLEAN_ARRAY;
		}

		final boolean[] subarray = new boolean[newSize];
		System.arraycopy(array, startIndexInclusive, subarray, 0, newSize);
		return subarray;
	}

	// Is same length
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating {@code null} arrays as length
	 * {@code 0}.
	 * <p>
	 * Any multi-dimensional aspects of the arrays are ignored.
	 * </p>
	 * 
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
	 */
	public static boolean isSameLength(final Object[] array1, final Object[] array2) {
		if ((array1 == null && array2 != null && array2.length > 0)
				|| (array2 == null && array1 != null && array1.length > 0)
				|| (array1 != null && array2 != null && array1.length != array2.length)) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating {@code null} arrays as length
	 * {@code 0}.
	 * </p>
	 * 
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
	 */
	public static boolean isSameLength(final long[] array1, final long[] array2) {
		if ((array1 == null && array2 != null && array2.length > 0)
				|| (array2 == null && array1 != null && array1.length > 0)
				|| (array1 != null && array2 != null && array1.length != array2.length)) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating {@code null} arrays as length
	 * {@code 0}.
	 * </p>
	 * 
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
	 */
	public static boolean isSameLength(final int[] array1, final int[] array2) {
		if ((array1 == null && array2 != null && array2.length > 0)
				|| (array2 == null && array1 != null && array1.length > 0)
				|| (array1 != null && array2 != null && array1.length != array2.length)) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating {@code null} arrays as length
	 * {@code 0}.
	 * </p>
	 * 
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
	 */
	public static boolean isSameLength(final short[] array1, final short[] array2) {
		if ((array1 == null && array2 != null && array2.length > 0)
				|| (array2 == null && array1 != null && array1.length > 0)
				|| (array1 != null && array2 != null && array1.length != array2.length)) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating {@code null} arrays as length
	 * {@code 0}.
	 * </p>
	 * 
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
	 */
	public static boolean isSameLength(final char[] array1, final char[] array2) {
		if ((array1 == null && array2 != null && array2.length > 0)
				|| (array2 == null && array1 != null && array1.length > 0)
				|| (array1 != null && array2 != null && array1.length != array2.length)) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating {@code null} arrays as length
	 * {@code 0}.
	 * </p>
	 * 
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
	 */
	public static boolean isSameLength(final byte[] array1, final byte[] array2) {
		if ((array1 == null && array2 != null && array2.length > 0)
				|| (array2 == null && array1 != null && array1.length > 0)
				|| (array1 != null && array2 != null && array1.length != array2.length)) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating {@code null} arrays as length
	 * {@code 0}.
	 * </p>
	 * 
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
	 */
	public static boolean isSameLength(final double[] array1, final double[] array2) {
		if ((array1 == null && array2 != null && array2.length > 0)
				|| (array2 == null && array1 != null && array1.length > 0)
				|| (array1 != null && array2 != null && array1.length != array2.length)) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating {@code null} arrays as length
	 * {@code 0}.
	 * </p>
	 * 
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
	 */
	public static boolean isSameLength(final float[] array1, final float[] array2) {
		if ((array1 == null && array2 != null && array2.length > 0)
				|| (array2 == null && array1 != null && array1.length > 0)
				|| (array1 != null && array2 != null && array1.length != array2.length)) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same length, treating {@code null} arrays as length
	 * {@code 0}.
	 * </p>
	 * 
	 * @param array1 the first array, may be {@code null}
	 * @param array2 the second array, may be {@code null}
	 * @return {@code true} if length of arrays matches, treating {@code null} as an empty array
	 */
	public static boolean isSameLength(final boolean[] array1, final boolean[] array2) {
		if ((array1 == null && array2 != null && array2.length > 0)
				|| (array2 == null && array1 != null && array1.length > 0)
				|| (array1 != null && array2 != null && array1.length != array2.length)) {
			return false;
		}
		return true;
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Returns the length of the specified array. This method can deal with {@code Object} arrays
	 * and with primitive arrays.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, {@code 0} is returned.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.getLength(null)            = 0
	 * Arrays.getLength([])              = 0
	 * Arrays.getLength([null])          = 1
	 * Arrays.getLength([true, false])   = 2
	 * Arrays.getLength([1, 2, 3])       = 3
	 * Arrays.getLength(["a", "b", "c"]) = 3
	 * </pre>
	 * 
	 * @param array the array to retrieve the length from, may be null
	 * @return The length of the array, or {@code 0} if the array is {@code null}
	 * @throws IllegalArgumentException if the object arguement is not an array.
	 */
	public static int getLength(final Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}

	/**
	 * <p>
	 * Checks whether two arrays are the same type taking into account multi-dimensional arrays.
	 * </p>
	 * 
	 * @param array1 the first array, must not be {@code null}
	 * @param array2 the second array, must not be {@code null}
	 * @return {@code true} if type of arrays matches
	 * @throws IllegalArgumentException if either array is {@code null}
	 */
	public static boolean isSameType(final Object array1, final Object array2) {
		if (array1 == null || array2 == null) {
			throw new IllegalArgumentException("The Array must not be null");
		}
		return array1.getClass().getName().equals(array2.getClass().getName());
	}

	// Reverse
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * <p>
	 * There is no special handling for multi-dimensional arrays.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(final Object[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(final long[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(final int[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(final short[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(final char[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(final byte[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(final double[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(final float[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * <p>
	 * Reverses the order of the given array.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 */
	public static void reverse(final boolean[] array) {
		if (array == null) {
			return;
		}
		reverse(array, 0, array.length);
	}

	/**
	 * <p>
	 * Reverses the order of the given array in the given range.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue (&gt;array.length) is demoted
	 *            to array length.
	 */
	public static void reverse(final boolean[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		boolean tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array in the given range.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue (&gt;array.length) is demoted
	 *            to array length.
	 */
	public static void reverse(final byte[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		byte tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array in the given range.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue (&gt;array.length) is demoted
	 *            to array length.
	 */
	public static void reverse(final char[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		char tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array in the given range.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue (&gt;array.length) is demoted
	 *            to array length.
	 */
	public static void reverse(final double[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		double tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array in the given range.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue (&gt;array.length) is demoted
	 *            to array length.
	 */
	public static void reverse(final float[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		float tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array in the given range.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue (&gt;array.length) is demoted
	 *            to array length.
	 */
	public static void reverse(final int[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		int tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array in the given range.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue (&gt;array.length) is demoted
	 *            to array length.
	 */
	public static void reverse(final long[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		long tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array in the given range.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue (&gt;array.length) is demoted
	 *            to array length.
	 */
	public static void reverse(final Object[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		Object tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	/**
	 * <p>
	 * Reverses the order of the given array in the given range.
	 * </p>
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to reverse, may be {@code null}
	 * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue (&gt;array.length) is demoted
	 *            to array length.
	 */
	public static void reverse(final short[] array, int startIndexInclusive, int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		short tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}

	// IndexOf search
	// ----------------------------------------------------------------------

	// Object IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given object in the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @return the index of the object within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if
	 *         not found or {@code null} array input
	 */
	public static int indexOf(final Object[] array, final Object objectToFind) {
		return indexOf(array, objectToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given object in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @param startIndex the index to start searching at
	 * @return the index of the object within the array starting at the index,
	 *         {@link #INDEX_NOT_FOUND} ({@code -1}) if not found or {@code null} array input
	 */
	public static int indexOf(final Object[] array, final Object objectToFind, int startIndex) {
		if (array == null || array.length == 0) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (objectToFind == null) {
			for (int i = startIndex; i < array.length; i++) {
				if (array[i] == null) {
					return i;
				}
			}
		}
		else if (array.getClass().getComponentType().isInstance(objectToFind)) {
			for (int i = startIndex; i < array.length; i++) {
				if (objectToFind.equals(array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given object within the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @return the last index of the object within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final Object[] array, final Object objectToFind) {
		return lastIndexOf(array, objectToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given object in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param objectToFind the object to find, may be {@code null}
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the object within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final Object[] array, final Object objectToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		if (objectToFind == null) {
			for (int i = startIndex; i >= 0; i--) {
				if (array[i] == null) {
					return i;
				}
			}
		}
		else if (array.getClass().getComponentType().isInstance(objectToFind)) {
			for (int i = startIndex; i >= 0; i--) {
				if (objectToFind.equals(array[i])) {
					return i;
				}
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the object is in the given array.
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param objectToFind the object to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(final Object[] array, final Object objectToFind) {
		return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
	}

	// long IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final long[] array, final long valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final long[] array, final long valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final long[] array, final long valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final long[] array, final long valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(final long[] array, final long valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// int IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final int[] array, final int valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final int[] array, final int valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final int[] array, final int valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final int[] array, final int valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(final int[] array, final int valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// short IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final short[] array, final short valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final short[] array, final short valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	public static int indexOf(final byte[] array, final byte[] target) {
		return indexOf(array, target, 0);
	}

	public static int indexOf(final byte[] array, final byte[] target, int fromIndex) {
		return indexOf(array, 0, array.length, target, 0, target.length, fromIndex);
	}

	public static int indexOf(byte[] source, int sourceOffset, int sourceCount, byte[] target, int targetOffset,
			int targetCount, int fromIndex) {
		if (fromIndex >= sourceCount) {
			return (targetCount == 0 ? sourceCount : -1);
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (targetCount == 0) {
			return fromIndex;
		}

		byte first = target[targetOffset];
		int max = sourceOffset + (sourceCount - targetCount);

		for (int i = sourceOffset + fromIndex; i <= max; i++) {
			/* Look for first character. */
			if (source[i] != first) {
				while (++i <= max && source[i] != first)
					;
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;
				for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++)
					;

				if (j == end) {
					/* Found whole string. */
					return i - sourceOffset;
				}
			}
		}
		return -1;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final short[] array, final short valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final short[] array, final short valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(final short[] array, final short valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// char IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final char[] array, final char valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final char[] array, final char valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final char[] array, final char valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final char[] array, final char valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(final char[] array, final char valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// byte IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final byte[] array, final byte valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final byte[] array, final byte valueToFind, int startIndex) {
		return indexOf(array, valueToFind, startIndex, -1);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @param endIndex the index to end searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final byte[] array, final byte valueToFind, int startIndex, int endIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		if (endIndex < 0) {
			endIndex = array.length;
		}
		for (int i = startIndex; i < endIndex; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final byte[] array, final byte valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final byte[] array, final byte valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(final byte[] array, final byte valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// double IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final double[] array, final double valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value within a given tolerance in the array. This method will
	 * return the index of the first value which falls between the region defined by valueToFind -
	 * tolerance and valueToFind + tolerance.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param tolerance tolerance of the search
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final double[] array, final double valueToFind, final double tolerance) {
		return indexOf(array, valueToFind, 0, tolerance);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final double[] array, final double valueToFind, int startIndex) {
		if (Arrays.isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index. This method will
	 * return the index of the first value which falls between the region defined by valueToFind -
	 * tolerance and valueToFind + tolerance.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @param tolerance tolerance of the search
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final double[] array, final double valueToFind, int startIndex, double tolerance) {
		if (Arrays.isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		final double min = valueToFind - tolerance;
		final double max = valueToFind + tolerance;
		for (int i = startIndex; i < array.length; i++) {
			if (array[i] >= min && array[i] <= max) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final double[] array, final double valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value within a given tolerance in the array. This method
	 * will return the index of the last value which falls between the region defined by valueToFind
	 * - tolerance and valueToFind + tolerance.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param tolerance tolerance of the search
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int lastIndexOf(final double[] array, final double valueToFind, final double tolerance) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE, tolerance);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final double[] array, final double valueToFind, int startIndex) {
		if (Arrays.isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index. This method
	 * will return the index of the last value which falls between the region defined by valueToFind
	 * - tolerance and valueToFind + tolerance.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @param tolerance search for value within plus/minus this amount
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final double[] array, final double valueToFind, int startIndex, double tolerance) {
		if (Arrays.isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		final double min = valueToFind - tolerance;
		final double max = valueToFind + tolerance;
		for (int i = startIndex; i >= 0; i--) {
			if (array[i] >= min && array[i] <= max) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(final double[] array, final double valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if a value falling within the given tolerance is in the given array. If the array
	 * contains a value within the inclusive range defined by (value - tolerance) to (value +
	 * tolerance).
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search
	 * @param valueToFind the value to find
	 * @param tolerance the array contains the tolerance of the search
	 * @return true if value falling within tolerance is in array
	 */
	public static boolean contains(final double[] array, final double valueToFind, final double tolerance) {
		return indexOf(array, valueToFind, 0, tolerance) != INDEX_NOT_FOUND;
	}

	// float IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final float[] array, final float valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final float[] array, final float valueToFind, int startIndex) {
		if (Arrays.isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final float[] array, final float valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final float[] array, final float valueToFind, int startIndex) {
		if (Arrays.isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(final float[] array, final float valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// boolean IndexOf
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Finds the index of the given value in the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final boolean[] array, final boolean valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	/**
	 * <p>
	 * Finds the index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex is treated as zero. A startIndex larger than the array length will
	 * return {@link #INDEX_NOT_FOUND} ({@code -1}).
	 * </p>
	 * 
	 * @param array the array to search through for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the index to start searching at
	 * @return the index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1}) if not
	 *         found or {@code null} array input
	 */
	public static int indexOf(final boolean[] array, final boolean valueToFind, int startIndex) {
		if (Arrays.isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Finds the last index of the given value within the array.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) if {@code null} array input.
	 * </p>
	 * 
	 * @param array the array to travers backwords looking for the object, may be {@code null}
	 * @param valueToFind the object to find
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final boolean[] array, final boolean valueToFind) {
		return lastIndexOf(array, valueToFind, Integer.MAX_VALUE);
	}

	/**
	 * <p>
	 * Finds the last index of the given value in the array starting at the given index.
	 * </p>
	 * <p>
	 * This method returns {@link #INDEX_NOT_FOUND} ({@code -1}) for a {@code null} input array.
	 * </p>
	 * <p>
	 * A negative startIndex will return {@link #INDEX_NOT_FOUND} ({@code -1}). A startIndex larger
	 * than the array length will search from the end of the array.
	 * </p>
	 * 
	 * @param array the array to traverse for looking for the object, may be {@code null}
	 * @param valueToFind the value to find
	 * @param startIndex the start index to travers backwards from
	 * @return the last index of the value within the array, {@link #INDEX_NOT_FOUND} ({@code -1})
	 *         if not found or {@code null} array input
	 */
	public static int lastIndexOf(final boolean[] array, final boolean valueToFind, int startIndex) {
		if (Arrays.isEmpty(array)) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			return INDEX_NOT_FOUND;
		}
		else if (startIndex >= array.length) {
			startIndex = array.length - 1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (valueToFind == array[i]) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * <p>
	 * Checks if the value is in the given array.
	 * </p>
	 * <p>
	 * The method returns {@code false} if a {@code null} array is passed in.
	 * </p>
	 * 
	 * @param array the array to search through
	 * @param valueToFind the value to find
	 * @return {@code true} if the array contains the object
	 */
	public static boolean contains(final boolean[] array, final boolean valueToFind) {
		return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
	}

	// Primitive/Object array converters
	// ----------------------------------------------------------------------

	// Character array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Characters to primitives.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Character} array, may be {@code null}
	 * @return a {@code char} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static char[] toPrimitive(final Character[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_CHAR_ARRAY;
		}
		final char[] result = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].charValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Character to primitives handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Character} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code char} array, {@code null} if null array input
	 */
	public static char[] toPrimitive(final Character[] array, final char valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_CHAR_ARRAY;
		}
		final char[] result = new char[array.length];
		for (int i = 0; i < array.length; i++) {
			final Character b = array[i];
			result[i] = (b == null ? valueForNull : b.charValue());
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive chars to objects.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code char} array
	 * @return a {@code Character} array, {@code null} if null array input
	 */
	public static Character[] toObject(final char[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_CHARACTER_OBJECT_ARRAY;
		}
		final Character[] result = new Character[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Character.valueOf(array[i]);
		}
		return result;
	}

	// Long array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Longs to primitives.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Long} array, may be {@code null}
	 * @return a {@code long} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static long[] toPrimitive(final Long[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_LONG_ARRAY;
		}
		final long[] result = new long[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].longValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Long to primitives handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Long} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code long} array, {@code null} if null array input
	 */
	public static long[] toPrimitive(final Long[] array, final long valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_LONG_ARRAY;
		}
		final long[] result = new long[array.length];
		for (int i = 0; i < array.length; i++) {
			Long b = array[i];
			result[i] = (b == null ? valueForNull : b.longValue());
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive longs to objects.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code long} array
	 * @return a {@code Long} array, {@code null} if null array input
	 */
	public static Long[] toObject(final long[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_LONG_OBJECT_ARRAY;
		}
		final Long[] result = new Long[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Long.valueOf(array[i]);
		}
		return result;
	}

	// Int array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Integers to primitives.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Integer} array, may be {@code null}
	 * @return an {@code int} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static int[] toPrimitive(final Integer[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_INT_ARRAY;
		}
		final int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].intValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Integer to primitives handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Integer} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return an {@code int} array, {@code null} if null array input
	 */
	public static int[] toPrimitive(final Integer[] array, final int valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_INT_ARRAY;
		}
		final int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			final Integer b = array[i];
			result[i] = (b == null ? valueForNull : b.intValue());
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive ints to objects.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array an {@code int} array
	 * @return an {@code Integer} array, {@code null} if null array input
	 */
	public static Integer[] toObject(final int[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_INTEGER_OBJECT_ARRAY;
		}
		final Integer[] result = new Integer[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Integer.valueOf(array[i]);
		}
		return result;
	}

	// Short array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Shorts to primitives.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Short} array, may be {@code null}
	 * @return a {@code byte} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static short[] toPrimitive(final Short[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_SHORT_ARRAY;
		}
		final short[] result = new short[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].shortValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Short to primitives handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Short} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code byte} array, {@code null} if null array input
	 */
	public static short[] toPrimitive(final Short[] array, final short valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_SHORT_ARRAY;
		}
		final short[] result = new short[array.length];
		for (int i = 0; i < array.length; i++) {
			final Short b = array[i];
			result[i] = (b == null ? valueForNull : b.shortValue());
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive shorts to objects.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code short} array
	 * @return a {@code Short} array, {@code null} if null array input
	 */
	public static Short[] toObject(final short[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_SHORT_OBJECT_ARRAY;
		}
		final Short[] result = new Short[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Short.valueOf(array[i]);
		}
		return result;
	}

	// Byte array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Bytes to primitives.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Byte} array, may be {@code null}
	 * @return a {@code byte} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static byte[] toPrimitive(final Byte[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BYTE_ARRAY;
		}
		final byte[] result = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].byteValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Bytes to primitives handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Byte} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code byte} array, {@code null} if null array input
	 */
	public static byte[] toPrimitive(final Byte[] array, final byte valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BYTE_ARRAY;
		}
		final byte[] result = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			final Byte b = array[i];
			result[i] = (b == null ? valueForNull : b.byteValue());
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive bytes to objects.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code byte} array
	 * @return a {@code Byte} array, {@code null} if null array input
	 */
	public static Byte[] toObject(final byte[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BYTE_OBJECT_ARRAY;
		}
		final Byte[] result = new Byte[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Byte.valueOf(array[i]);
		}
		return result;
	}

	// Double array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Doubles to primitives.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Double} array, may be {@code null}
	 * @return a {@code double} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static double[] toPrimitive(final Double[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_DOUBLE_ARRAY;
		}
		final double[] result = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].doubleValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Doubles to primitives handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Double} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code double} array, {@code null} if null array input
	 */
	public static double[] toPrimitive(final Double[] array, final double valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_DOUBLE_ARRAY;
		}
		final double[] result = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			final Double b = array[i];
			result[i] = (b == null ? valueForNull : b.doubleValue());
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive doubles to objects.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code double} array
	 * @return a {@code Double} array, {@code null} if null array input
	 */
	public static Double[] toObject(final double[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_DOUBLE_OBJECT_ARRAY;
		}
		final Double[] result = new Double[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Double.valueOf(array[i]);
		}
		return result;
	}

	// Float array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Floats to primitives.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Float} array, may be {@code null}
	 * @return a {@code float} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static float[] toPrimitive(final Float[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_FLOAT_ARRAY;
		}
		final float[] result = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].floatValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Floats to primitives handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Float} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code float} array, {@code null} if null array input
	 */
	public static float[] toPrimitive(final Float[] array, final float valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_FLOAT_ARRAY;
		}
		final float[] result = new float[array.length];
		for (int i = 0; i < array.length; i++) {
			final Float b = array[i];
			result[i] = (b == null ? valueForNull : b.floatValue());
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive floats to objects.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code float} array
	 * @return a {@code Float} array, {@code null} if null array input
	 */
	public static Float[] toObject(final float[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_FLOAT_OBJECT_ARRAY;
		}
		final Float[] result = new Float[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = Float.valueOf(array[i]);
		}
		return result;
	}

	// Boolean array converters
	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Converts an array of object Booleans to primitives.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Boolean} array, may be {@code null}
	 * @return a {@code boolean} array, {@code null} if null array input
	 * @throws NullPointerException if array content is {@code null}
	 */
	public static boolean[] toPrimitive(final Boolean[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BOOLEAN_ARRAY;
		}
		final boolean[] result = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = array[i].booleanValue();
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of object Booleans to primitives handling {@code null}.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code Boolean} array, may be {@code null}
	 * @param valueForNull the value to insert if {@code null} found
	 * @return a {@code boolean} array, {@code null} if null array input
	 */
	public static boolean[] toPrimitive(final Boolean[] array, final boolean valueForNull) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BOOLEAN_ARRAY;
		}
		final boolean[] result = new boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			final Boolean b = array[i];
			result[i] = (b == null ? valueForNull : b.booleanValue());
		}
		return result;
	}

	/**
	 * <p>
	 * Converts an array of primitive booleans to objects.
	 * </p>
	 * <p>
	 * This method returns {@code null} for a {@code null} input array.
	 * </p>
	 * 
	 * @param array a {@code boolean} array
	 * @return a {@code Boolean} array, {@code null} if null array input
	 */
	public static Boolean[] toObject(final boolean[] array) {
		if (array == null) {
			return null;
		}
		else if (array.length == 0) {
			return EMPTY_BOOLEAN_OBJECT_ARRAY;
		}
		final Boolean[] result = new Boolean[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = (array[i] ? Boolean.TRUE : Boolean.FALSE);
		}
		return result;
	}

	/**
	 * Convert the given array (which may be a primitive array) to an object array (if necessary of
	 * primitive wrapper objects).
	 * <p>
	 * A <code>null</code> source value will be converted to an empty Object array.
	 * 
	 * @param source the (potentially primitive) array
	 * @return the corresponding object array (never <code>null</code>)
	 * @throws IllegalArgumentException if the parameter is not an array
	 */
	public static Object[] toObjectArray(Object source) {
		if (source instanceof Object[]) {
			return (Object[])source;
		}
		if (source == null) {
			return new Object[0];
		}
		if (!source.getClass().isArray()) {
			throw new IllegalArgumentException("Source is not an array: " + source);
		}
		int length = Array.getLength(source);
		if (length == 0) {
			return new Object[0];
		}
		Class wrapperType = Array.get(source, 0).getClass();
		Object[] newArray = (Object[])Array.newInstance(wrapperType, length);
		for (int i = 0; i < length; i++) {
			newArray[i] = Array.get(source, i);
		}
		return newArray;
	}

	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if an array of Objects is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive longs is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final long[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive ints is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final int[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive shorts is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final short[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive chars is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final char[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive bytes is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final byte[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive doubles is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final double[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive floats is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final float[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * <p>
	 * Checks if an array of primitive booleans is empty or {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final boolean[] array) {
		return array == null || array.length == 0;
	}

	// ----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if an array of Objects is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static <T> boolean isNotEmpty(final T[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive longs is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(final long[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive ints is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(final int[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive shorts is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(final short[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive chars is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(final char[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive bytes is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(final byte[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive doubles is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(final double[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive floats is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(final float[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Checks if an array of primitive booleans is not empty or not {@code null}.
	 * </p>
	 * 
	 * @param array the array to test
	 * @return {@code true} if the array is not empty or not {@code null}
	 */
	public static boolean isNotEmpty(final boolean[] array) {
		return (array != null && array.length != 0);
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements
	 * {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.addAll(null, null)     = null
	 * Arrays.addAll(array1, null)   = cloned copy of array1
	 * Arrays.addAll(null, array2)   = cloned copy of array2
	 * Arrays.addAll([], [])         = []
	 * Arrays.addAll([null], [null]) = [null, null]
	 * Arrays.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
	 * </pre>
	 * 
	 * @param <T> the component type of the array
	 * @param array1 the first array whose elements are added to the new array, may be {@code null}
	 * @param array2 the second array whose elements are added to the new array, may be {@code null}
	 * @return The new array, {@code null} if both arrays are {@code null}. The type of the new
	 *         array is the type of the first array, unless the first array is null, in which case
	 *         the type is the same as the second array.
	 * @throws IllegalArgumentException if the array types are incompatible
	 */
	public static <T> T[] addAll(final T[] array1, final T... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final Class<?> type1 = array1.getClass().getComponentType();
		@SuppressWarnings("unchecked")
		// OK, because array is of type T
		final T[] joinedArray = (T[])Array.newInstance(type1, array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		try {
			System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		}
		catch (final ArrayStoreException ase) {
			// Check if problem was due to incompatible types
			/*
			 * We do this here, rather than before the copy because: - it would be a wasted check
			 * most of the time - safer, in case check turns out to be too strict
			 */
			final Class<?> type2 = array2.getClass().getComponentType();
			if (!type1.isAssignableFrom(type2)) {
				throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of "
						+ type1.getName(), ase);
			}
			throw ase; // No, so rethrow original
		}
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements
	 * {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.addAll(array1, null)   = cloned copy of array1
	 * Arrays.addAll(null, array2)   = cloned copy of array2
	 * Arrays.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new boolean[] array.
	 */
	public static boolean[] addAll(final boolean[] array1, final boolean... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final boolean[] joinedArray = new boolean[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements
	 * {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.addAll(array1, null)   = cloned copy of array1
	 * Arrays.addAll(null, array2)   = cloned copy of array2
	 * Arrays.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new char[] array.
	 */
	public static char[] addAll(final char[] array1, final char... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final char[] joinedArray = new char[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements
	 * {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.addAll(array1, null)   = cloned copy of array1
	 * Arrays.addAll(null, array2)   = cloned copy of array2
	 * Arrays.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new byte[] array.
	 */
	public static byte[] addAll(final byte[] array1, final byte... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final byte[] joinedArray = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements
	 * {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.addAll(array1, null)   = cloned copy of array1
	 * Arrays.addAll(null, array2)   = cloned copy of array2
	 * Arrays.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new short[] array.
	 */
	public static short[] addAll(final short[] array1, final short... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final short[] joinedArray = new short[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements
	 * {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.addAll(array1, null)   = cloned copy of array1
	 * Arrays.addAll(null, array2)   = cloned copy of array2
	 * Arrays.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new int[] array.
	 */
	public static int[] addAll(final int[] array1, final int... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final int[] joinedArray = new int[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements
	 * {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.addAll(array1, null)   = cloned copy of array1
	 * Arrays.addAll(null, array2)   = cloned copy of array2
	 * Arrays.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new long[] array.
	 */
	public static long[] addAll(final long[] array1, final long... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final long[] joinedArray = new long[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements
	 * {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.addAll(array1, null)   = cloned copy of array1
	 * Arrays.addAll(null, array2)   = cloned copy of array2
	 * Arrays.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new float[] array.
	 */
	public static float[] addAll(final float[] array1, final float... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final float[] joinedArray = new float[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * </p>
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the elements
	 * {@code array2}. When an array is returned, it is always a new array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.addAll(array1, null)   = cloned copy of array1
	 * Arrays.addAll(null, array2)   = cloned copy of array2
	 * Arrays.addAll([], [])         = []
	 * </pre>
	 * 
	 * @param array1 the first array whose elements are added to the new array.
	 * @param array2 the second array whose elements are added to the new array.
	 * @return The new double[] array.
	 */
	public static double[] addAll(final double[] array1, final double... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final double[] joinedArray = new double[array1.length + array2.length];
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		return joinedArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the
	 * last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element, unless the element itself is null, in which case the return type
	 * is Object[]
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, null)      = [null]
	 * Arrays.add(null, "a")       = ["a"]
	 * Arrays.add(["a"], null)     = ["a", null]
	 * Arrays.add(["a"], "b")      = ["a", "b"]
	 * Arrays.add(["a", "b"], "c") = ["a", "b", "c"]
	 * </pre>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array to "add" the element to, may be {@code null}
	 * @param element the object to add, may be {@code null}
	 * @return A new array containing the existing elements plus the new element The returned array
	 *         type will be that of the input array (unless null), in which case it will have the
	 *         same type as the element. If both are null, an IllegalArgumentException is thrown
	 * @throws IllegalArgumentException if both arguments are null
	 */
	public static <T> T[] add(final T[] array, final T element) {
		Class<?> type;
		if (array != null) {
			type = array.getClass();
		}
		else if (element != null) {
			type = element.getClass();
		}
		else {
			throw new IllegalArgumentException("Arguments cannot both be null");
		}
		@SuppressWarnings("unchecked")
		// type must be T
		final T[] newArray = (T[])copyArrayGrow1(array, type);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the
	 * last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, true)          = [true]
	 * Arrays.add([true], false)       = [true, false]
	 * Arrays.add([true, false], true) = [true, false, true]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 */
	public static boolean[] add(final boolean[] array, final boolean element) {
		final boolean[] newArray = (boolean[])copyArrayGrow1(array, Boolean.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the
	 * last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, 0)   = [0]
	 * Arrays.add([1], 0)    = [1, 0]
	 * Arrays.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 */
	public static byte[] add(final byte[] array, final byte element) {
		final byte[] newArray = (byte[])copyArrayGrow1(array, Byte.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the
	 * last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, '0')       = ['0']
	 * Arrays.add(['1'], '0')      = ['1', '0']
	 * Arrays.add(['1', '0'], '1') = ['1', '0', '1']
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 */
	public static char[] add(final char[] array, final char element) {
		final char[] newArray = (char[])copyArrayGrow1(array, Character.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the
	 * last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, 0)   = [0]
	 * Arrays.add([1], 0)    = [1, 0]
	 * Arrays.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 */
	public static double[] add(final double[] array, final double element) {
		final double[] newArray = (double[])copyArrayGrow1(array, Double.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the
	 * last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, 0)   = [0]
	 * Arrays.add([1], 0)    = [1, 0]
	 * Arrays.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 */
	public static float[] add(final float[] array, final float element) {
		final float[] newArray = (float[])copyArrayGrow1(array, Float.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the
	 * last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, 0)   = [0]
	 * Arrays.add([1], 0)    = [1, 0]
	 * Arrays.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 */
	public static int[] add(final int[] array, final int element) {
		final int[] newArray = (int[])copyArrayGrow1(array, Integer.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the
	 * last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, 0)   = [0]
	 * Arrays.add([1], 0)    = [1, 0]
	 * Arrays.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 */
	public static long[] add(final long[] array, final long element) {
		final long[] newArray = (long[])copyArrayGrow1(array, Long.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * <p>
	 * Copies the given array and adds the given element at the end of the new array.
	 * </p>
	 * <p>
	 * The new array contains the same elements of the input array plus the given element in the
	 * last position. The component type of the new array is the same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, 0)   = [0]
	 * Arrays.add([1], 0)    = [1, 0]
	 * Arrays.add([1, 0], 1) = [1, 0, 1]
	 * </pre>
	 * 
	 * @param array the array to copy and add the element to, may be {@code null}
	 * @param element the object to add at the last index of the new array
	 * @return A new array containing the existing elements plus the new element
	 */
	public static short[] add(final short[] array, final short element) {
		final short[] newArray = (short[])copyArrayGrow1(array, Short.TYPE);
		newArray[newArray.length - 1] = element;
		return newArray;
	}

	/**
	 * Returns a copy of the given array of size 1 greater than the argument. The last value of the
	 * array is left to the default value.
	 * 
	 * @param array The array to copy, must not be {@code null}.
	 * @param newArrayComponentType If {@code array} is {@code null}, create a size 1 array of this
	 *            type.
	 * @return A new copy of the array of size 1 greater than the input.
	 */
	private static Object copyArrayGrow1(final Object array, final Class<?> newArrayComponentType) {
		if (array != null) {
			final int arrayLength = Array.getLength(array);
			final Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
			System.arraycopy(array, 0, newArray, 0, arrayLength);
			return newArray;
		}
		return Array.newInstance(newArrayComponentType, 1);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given
	 * element on the specified position. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, 0, null)      = [null]
	 * Arrays.add(null, 0, "a")       = ["a"]
	 * Arrays.add(["a"], 1, null)     = ["a", null]
	 * Arrays.add(["a"], 1, "b")      = ["a", "b"]
	 * Arrays.add(["a", "b"], 3, "c") = ["a", "b", "c"]
	 * </pre>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >
	 *             array.length).
	 * @throws IllegalArgumentException if both array and element are null
	 */
	public static <T> T[] add(final T[] array, final int index, final T element) {
		Class<?> clss = null;
		if (array != null) {
			clss = array.getClass().getComponentType();
		}
		else if (element != null) {
			clss = element.getClass();
		}
		else {
			throw new IllegalArgumentException("Array and element cannot both be null");
		}
		@SuppressWarnings("unchecked")
		// the add method creates an array of type clss, which is type T
		final T[] newArray = (T[])add(array, index, element, clss);
		return newArray;
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given
	 * element on the specified position. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, 0, true)          = [true]
	 * Arrays.add([true], 0, false)       = [false, true]
	 * Arrays.add([false], 1, true)       = [false, true]
	 * Arrays.add([true, false], 1, true) = [true, true, false]
	 * </pre>
	 * 
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >
	 *             array.length).
	 */
	public static boolean[] add(final boolean[] array, final int index, final boolean element) {
		return (boolean[])add(array, index, Boolean.valueOf(element), Boolean.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given
	 * element on the specified position. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add(null, 0, 'a')            = ['a']
	 * Arrays.add(['a'], 0, 'b')           = ['b', 'a']
	 * Arrays.add(['a', 'b'], 0, 'c')      = ['c', 'a', 'b']
	 * Arrays.add(['a', 'b'], 1, 'k')      = ['a', 'k', 'b']
	 * Arrays.add(['a', 'b', 'c'], 1, 't') = ['a', 't', 'b', 'c']
	 * </pre>
	 * 
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >
	 *             array.length).
	 */
	public static char[] add(final char[] array, final int index, final char element) {
		return (char[])add(array, index, Character.valueOf(element), Character.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given
	 * element on the specified position. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add([1], 0, 2)         = [2, 1]
	 * Arrays.add([2, 6], 2, 3)      = [2, 6, 3]
	 * Arrays.add([2, 6], 0, 1)      = [1, 2, 6]
	 * Arrays.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
	 * </pre>
	 * 
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >
	 *             array.length).
	 */
	public static byte[] add(final byte[] array, final int index, final byte element) {
		return (byte[])add(array, index, Byte.valueOf(element), Byte.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given
	 * element on the specified position. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add([1], 0, 2)         = [2, 1]
	 * Arrays.add([2, 6], 2, 10)     = [2, 6, 10]
	 * Arrays.add([2, 6], 0, -4)     = [-4, 2, 6]
	 * Arrays.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
	 * </pre>
	 * 
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >
	 *             array.length).
	 */
	public static short[] add(final short[] array, final int index, final short element) {
		return (short[])add(array, index, Short.valueOf(element), Short.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given
	 * element on the specified position. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add([1], 0, 2)         = [2, 1]
	 * Arrays.add([2, 6], 2, 10)     = [2, 6, 10]
	 * Arrays.add([2, 6], 0, -4)     = [-4, 2, 6]
	 * Arrays.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
	 * </pre>
	 * 
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >
	 *             array.length).
	 */
	public static int[] add(final int[] array, final int index, final int element) {
		return (int[])add(array, index, Integer.valueOf(element), Integer.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given
	 * element on the specified position. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add([1L], 0, 2L)           = [2L, 1L]
	 * Arrays.add([2L, 6L], 2, 10L)      = [2L, 6L, 10L]
	 * Arrays.add([2L, 6L], 0, -4L)      = [-4L, 2L, 6L]
	 * Arrays.add([2L, 6L, 3L], 2, 1L)   = [2L, 6L, 1L, 3L]
	 * </pre>
	 * 
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >
	 *             array.length).
	 */
	public static long[] add(final long[] array, final int index, final long element) {
		return (long[])add(array, index, Long.valueOf(element), Long.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given
	 * element on the specified position. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add([1.1f], 0, 2.2f)               = [2.2f, 1.1f]
	 * Arrays.add([2.3f, 6.4f], 2, 10.5f)        = [2.3f, 6.4f, 10.5f]
	 * Arrays.add([2.6f, 6.7f], 0, -4.8f)        = [-4.8f, 2.6f, 6.7f]
	 * Arrays.add([2.9f, 6.0f, 0.3f], 2, 1.0f)   = [2.9f, 6.0f, 1.0f, 0.3f]
	 * </pre>
	 * 
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >
	 *             array.length).
	 */
	public static float[] add(final float[] array, final int index, final float element) {
		return (float[])add(array, index, Float.valueOf(element), Float.TYPE);
	}

	/**
	 * <p>
	 * Inserts the specified element at the specified position in the array. Shifts the element
	 * currently at that position (if any) and any subsequent elements to the right (adds one to
	 * their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array plus the given
	 * element on the specified position. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, a new one element array is returned whose component type
	 * is the same as the element.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.add([1.1], 0, 2.2)              = [2.2, 1.1]
	 * Arrays.add([2.3, 6.4], 2, 10.5)        = [2.3, 6.4, 10.5]
	 * Arrays.add([2.6, 6.7], 0, -4.8)        = [-4.8, 2.6, 6.7]
	 * Arrays.add([2.9, 6.0, 0.3], 2, 1.0)    = [2.9, 6.0, 1.0, 0.3]
	 * </pre>
	 * 
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @return A new array containing the existing elements and the new element
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >
	 *             array.length).
	 */
	public static double[] add(final double[] array, final int index, final double element) {
		return (double[])add(array, index, Double.valueOf(element), Double.TYPE);
	}

	/**
	 * Underlying implementation of add(array, index, element) methods. The last parameter is the
	 * class, which may not equal element.getClass for primitives.
	 * 
	 * @param array the array to add the element to, may be {@code null}
	 * @param index the position of the new object
	 * @param element the object to add
	 * @param clss the type of the element being added
	 * @return A new array containing the existing elements and the new element
	 */
	private static Object add(final Object array, final int index, final Object element, final Class<?> clss) {
		if (array == null) {
			if (index != 0) {
				throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
			}
			final Object joinedArray = Array.newInstance(clss, 1);
			Array.set(joinedArray, 0, element);
			return joinedArray;
		}
		final int length = Array.getLength(array);
		if (index > length || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}
		final Object result = Array.newInstance(clss, length + 1);
		System.arraycopy(array, 0, result, 0, index);
		Array.set(result, index, element);
		if (index < length) {
			System.arraycopy(array, index, result, index + 1, length - index);
		}
		return result;
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.remove(["a"], 0)           = []
	 * Arrays.remove(["a", "b"], 0)      = ["b"]
	 * Arrays.remove(["a", "b"], 1)      = ["a"]
	 * Arrays.remove(["a", "b", "c"], 1) = ["a", "c"]
	 * </pre>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	@SuppressWarnings("unchecked")
	// remove() always creates an array of the same type as its input
	public static <T> T[] remove(final T[] array, final int index) {
		return (T[])remove((Object)array, index);
	}

	/**
	 * <p>
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If the array
	 * doesn't contains such an element, no elements are removed from the array.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the first
	 * occurrence of the specified element. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElement(null, "a")            = null
	 * Arrays.removeElement([], "a")              = []
	 * Arrays.removeElement(["a"], "b")           = ["a"]
	 * Arrays.removeElement(["a", "b"], "a")      = ["b"]
	 * Arrays.removeElement(["a", "b", "a"], "a") = ["b", "a"]
	 * </pre>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of the
	 *         specified element.
	 */
	public static <T> T[] removeElement(final T[] array, final Object element) {
		final int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.remove([true], 0)              = []
	 * Arrays.remove([true, false], 0)       = [false]
	 * Arrays.remove([true, false], 1)       = [true]
	 * Arrays.remove([true, true, false], 1) = [true, false]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static boolean[] remove(final boolean[] array, final int index) {
		return (boolean[])remove((Object)array, index);
	}

	/**
	 * <p>
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If the array
	 * doesn't contains such an element, no elements are removed from the array.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the first
	 * occurrence of the specified element. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElement(null, true)                = null
	 * Arrays.removeElement([], true)                  = []
	 * Arrays.removeElement([true], false)             = [true]
	 * Arrays.removeElement([true, false], false)      = [true]
	 * Arrays.removeElement([true, false, true], true) = [false, true]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of the
	 *         specified element.
	 */
	public static boolean[] removeElement(final boolean[] array, final boolean element) {
		final int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.remove([1], 0)          = []
	 * Arrays.remove([1, 0], 0)       = [0]
	 * Arrays.remove([1, 0], 1)       = [1]
	 * Arrays.remove([1, 0, 1], 1)    = [1, 1]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static byte[] remove(final byte[] array, final int index) {
		return (byte[])remove((Object)array, index);
	}

	/**
	 * <p>
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If the array
	 * doesn't contains such an element, no elements are removed from the array.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the first
	 * occurrence of the specified element. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElement(null, 1)        = null
	 * Arrays.removeElement([], 1)          = []
	 * Arrays.removeElement([1], 0)         = [1]
	 * Arrays.removeElement([1, 0], 0)      = [1]
	 * Arrays.removeElement([1, 0, 1], 1)   = [0, 1]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of the
	 *         specified element.
	 */
	public static byte[] removeElement(final byte[] array, final byte element) {
		final int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.remove(['a'], 0)           = []
	 * Arrays.remove(['a', 'b'], 0)      = ['b']
	 * Arrays.remove(['a', 'b'], 1)      = ['a']
	 * Arrays.remove(['a', 'b', 'c'], 1) = ['a', 'c']
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static char[] remove(final char[] array, final int index) {
		return (char[])remove((Object)array, index);
	}

	/**
	 * <p>
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If the array
	 * doesn't contains such an element, no elements are removed from the array.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the first
	 * occurrence of the specified element. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElement(null, 'a')            = null
	 * Arrays.removeElement([], 'a')              = []
	 * Arrays.removeElement(['a'], 'b')           = ['a']
	 * Arrays.removeElement(['a', 'b'], 'a')      = ['b']
	 * Arrays.removeElement(['a', 'b', 'a'], 'a') = ['b', 'a']
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of the
	 *         specified element.
	 */
	public static char[] removeElement(final char[] array, final char element) {
		final int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.remove([1.1], 0)           = []
	 * Arrays.remove([2.5, 6.0], 0)      = [6.0]
	 * Arrays.remove([2.5, 6.0], 1)      = [2.5]
	 * Arrays.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static double[] remove(final double[] array, final int index) {
		return (double[])remove((Object)array, index);
	}

	/**
	 * <p>
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If the array
	 * doesn't contains such an element, no elements are removed from the array.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the first
	 * occurrence of the specified element. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElement(null, 1.1)            = null
	 * Arrays.removeElement([], 1.1)              = []
	 * Arrays.removeElement([1.1], 1.2)           = [1.1]
	 * Arrays.removeElement([1.1, 2.3], 1.1)      = [2.3]
	 * Arrays.removeElement([1.1, 2.3, 1.1], 1.1) = [2.3, 1.1]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of the
	 *         specified element.
	 */
	public static double[] removeElement(final double[] array, final double element) {
		final int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.remove([1.1], 0)           = []
	 * Arrays.remove([2.5, 6.0], 0)      = [6.0]
	 * Arrays.remove([2.5, 6.0], 1)      = [2.5]
	 * Arrays.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static float[] remove(final float[] array, final int index) {
		return (float[])remove((Object)array, index);
	}

	/**
	 * <p>
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If the array
	 * doesn't contains such an element, no elements are removed from the array.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the first
	 * occurrence of the specified element. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElement(null, 1.1)            = null
	 * Arrays.removeElement([], 1.1)              = []
	 * Arrays.removeElement([1.1], 1.2)           = [1.1]
	 * Arrays.removeElement([1.1, 2.3], 1.1)      = [2.3]
	 * Arrays.removeElement([1.1, 2.3, 1.1], 1.1) = [2.3, 1.1]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of the
	 *         specified element.
	 */
	public static float[] removeElement(final float[] array, final float element) {
		final int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.remove([1], 0)         = []
	 * Arrays.remove([2, 6], 0)      = [6]
	 * Arrays.remove([2, 6], 1)      = [2]
	 * Arrays.remove([2, 6, 3], 1)   = [2, 3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static int[] remove(final int[] array, final int index) {
		return (int[])remove((Object)array, index);
	}

	/**
	 * <p>
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If the array
	 * doesn't contains such an element, no elements are removed from the array.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the first
	 * occurrence of the specified element. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElement(null, 1)      = null
	 * Arrays.removeElement([], 1)        = []
	 * Arrays.removeElement([1], 2)       = [1]
	 * Arrays.removeElement([1, 3], 1)    = [3]
	 * Arrays.removeElement([1, 3, 1], 1) = [3, 1]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of the
	 *         specified element.
	 */
	public static int[] removeElement(final int[] array, final int element) {
		final int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.remove([1], 0)         = []
	 * Arrays.remove([2, 6], 0)      = [6]
	 * Arrays.remove([2, 6], 1)      = [2]
	 * Arrays.remove([2, 6, 3], 1)   = [2, 3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static long[] remove(final long[] array, final int index) {
		return (long[])remove((Object)array, index);
	}

	/**
	 * <p>
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If the array
	 * doesn't contains such an element, no elements are removed from the array.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the first
	 * occurrence of the specified element. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElement(null, 1)      = null
	 * Arrays.removeElement([], 1)        = []
	 * Arrays.removeElement([1], 2)       = [1]
	 * Arrays.removeElement([1, 3], 1)    = [3]
	 * Arrays.removeElement([1, 3, 1], 1) = [3, 1]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of the
	 *         specified element.
	 */
	public static long[] removeElement(final long[] array, final long element) {
		final int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.remove([1], 0)         = []
	 * Arrays.remove([2, 6], 0)      = [6]
	 * Arrays.remove([2, 6], 1)      = [2]
	 * Arrays.remove([2, 6, 3], 1)   = [2, 3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static short[] remove(final short[] array, final int index) {
		return (short[])remove((Object)array, index);
	}

	/**
	 * <p>
	 * Removes the first occurrence of the specified element from the specified array. All
	 * subsequent elements are shifted to the left (subtracts one from their indices). If the array
	 * doesn't contains such an element, no elements are removed from the array.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the first
	 * occurrence of the specified element. The component type of the returned array is always the
	 * same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElement(null, 1)      = null
	 * Arrays.removeElement([], 1)        = []
	 * Arrays.removeElement([1], 2)       = [1]
	 * Arrays.removeElement([1, 3], 1)    = [3]
	 * Arrays.removeElement([1, 3, 1], 1) = [3, 1]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param element the element to be removed
	 * @return A new array containing the existing elements except the first occurrence of the
	 *         specified element.
	 */
	public static short[] removeElement(final short[] array, final short element) {
		final int index = indexOf(array, element);
		if (index == INDEX_NOT_FOUND) {
			return clone(array);
		}
		return remove(array, index);
	}

	/**
	 * <p>
	 * Removes the element at the specified position from the specified array. All subsequent
	 * elements are shifted to the left (subtracts one from their indices).
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except the element
	 * on the specified position. The component type of the returned array is always the same as
	 * that of the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param index the position of the element to be removed
	 * @return A new array containing the existing elements except the element at the specified
	 *         position.
	 * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	private static Object remove(final Object array, final int index) {
		final int length = getLength(array);
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		}

		final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, result, 0, index);
		if (index < length - 1) {
			System.arraycopy(array, index + 1, result, index, length - index - 1);
		}

		return result;
	}

	/**
	 * <p>
	 * Removes the elements at the specified positions from the specified array. All remaining
	 * elements are shifted to the left.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except those at the
	 * specified positions. The component type of the returned array is always the same as that of
	 * the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeAll(["a", "b", "c"], 0, 2) = ["b"]
	 * Arrays.removeAll(["a", "b", "c"], 1, 2) = ["a"]
	 * </pre>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	@SuppressWarnings("unchecked")
	// removeAll() always creates an array of the same type as its input
	public static <T> T[] removeAll(final T[] array, final int... indices) {
		return (T[])removeAll((Object)array, clone(indices));
	}

	/**
	 * <p>
	 * Removes occurrences of specified elements, in specified quantities, from the specified array.
	 * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
	 * quantities than contained in the original array, no change occurs beyond the removal of the
	 * existing matching items.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except for the
	 * earliest-encountered occurrences of the specified elements. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElements(null, "a", "b")            = null
	 * Arrays.removeElements([], "a", "b")              = []
	 * Arrays.removeElements(["a"], "b", "c")           = ["a"]
	 * Arrays.removeElements(["a", "b"], "a", "c")      = ["b"]
	 * Arrays.removeElements(["a", "b", "a"], "a")      = ["b", "a"]
	 * Arrays.removeElements(["a", "b", "a"], "a", "a") = ["b"]
	 * </pre>
	 * 
	 * @param <T> the component type of the array
	 * @param array the array to remove the element from, may be {@code null}
	 * @param values the elements to be removed
	 * @return A new array containing the existing elements except the earliest-encountered
	 *         occurrences of the specified elements.
	 */
	public static <T> T[] removeElements(final T[] array, final T... values) {
		if (isEmpty(array) || isEmpty(values)) {
			return clone(array);
		}
		final HashMap<T, MutableInt> occurrences = new HashMap<T, MutableInt>(values.length);
		for (final T v : values) {
			final MutableInt count = occurrences.get(v);
			if (count == null) {
				occurrences.put(v, new MutableInt(1));
			}
			else {
				count.increment();
			}
		}
		final BitSet toRemove = new BitSet();
		for (final Map.Entry<T, MutableInt> e : occurrences.entrySet()) {
			final T v = e.getKey();
			int found = 0;
			for (int i = 0, ct = e.getValue().intValue(); i < ct; i++) {
				found = indexOf(array, v, found);
				if (found < 0) {
					break;
				}
				toRemove.set(found++);
			}
		}
		@SuppressWarnings("unchecked")
		// removeAll() always creates an array of the same type as its input
		final T[] result = (T[])removeAll(array, toRemove);
		return result;
	}

	/**
	 * <p>
	 * Removes the elements at the specified positions from the specified array. All remaining
	 * elements are shifted to the left.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except those at the
	 * specified positions. The component type of the returned array is always the same as that of
	 * the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeAll([1], 0)             = []
	 * Arrays.removeAll([2, 6], 0)          = [6]
	 * Arrays.removeAll([2, 6], 0, 1)       = []
	 * Arrays.removeAll([2, 6, 3], 1, 2)    = [2]
	 * Arrays.removeAll([2, 6, 3], 0, 2)    = [6]
	 * Arrays.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static byte[] removeAll(final byte[] array, final int... indices) {
		return (byte[])removeAll((Object)array, clone(indices));
	}

	/**
	 * <p>
	 * Removes occurrences of specified elements, in specified quantities, from the specified array.
	 * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
	 * quantities than contained in the original array, no change occurs beyond the removal of the
	 * existing matching items.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except for the
	 * earliest-encountered occurrences of the specified elements. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElements(null, 1, 2)      = null
	 * Arrays.removeElements([], 1, 2)        = []
	 * Arrays.removeElements([1], 2, 3)       = [1]
	 * Arrays.removeElements([1, 3], 1, 2)    = [3]
	 * Arrays.removeElements([1, 3, 1], 1)    = [3, 1]
	 * Arrays.removeElements([1, 3, 1], 1, 1) = [3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param values the elements to be removed
	 * @return A new array containing the existing elements except the earliest-encountered
	 *         occurrences of the specified elements.
	 */
	public static byte[] removeElements(final byte[] array, final byte... values) {
		if (isEmpty(array) || isEmpty(values)) {
			return clone(array);
		}
		final HashMap<Byte, MutableInt> occurrences = new HashMap<Byte, MutableInt>(values.length);
		for (final byte v : values) {
			final Byte boxed = Byte.valueOf(v);
			final MutableInt count = occurrences.get(boxed);
			if (count == null) {
				occurrences.put(boxed, new MutableInt(1));
			}
			else {
				count.increment();
			}
		}
		final BitSet toRemove = new BitSet();
		for (final Map.Entry<Byte, MutableInt> e : occurrences.entrySet()) {
			final Byte v = e.getKey();
			int found = 0;
			for (int i = 0, ct = e.getValue().intValue(); i < ct; i++) {
				found = indexOf(array, v.byteValue(), found);
				if (found < 0) {
					break;
				}
				toRemove.set(found++);
			}
		}
		return (byte[])removeAll(array, toRemove);
	}

	/**
	 * <p>
	 * Removes the elements at the specified positions from the specified array. All remaining
	 * elements are shifted to the left.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except those at the
	 * specified positions. The component type of the returned array is always the same as that of
	 * the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeAll([1], 0)             = []
	 * Arrays.removeAll([2, 6], 0)          = [6]
	 * Arrays.removeAll([2, 6], 0, 1)       = []
	 * Arrays.removeAll([2, 6, 3], 1, 2)    = [2]
	 * Arrays.removeAll([2, 6, 3], 0, 2)    = [6]
	 * Arrays.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static short[] removeAll(final short[] array, final int... indices) {
		return (short[])removeAll((Object)array, clone(indices));
	}

	/**
	 * <p>
	 * Removes occurrences of specified elements, in specified quantities, from the specified array.
	 * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
	 * quantities than contained in the original array, no change occurs beyond the removal of the
	 * existing matching items.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except for the
	 * earliest-encountered occurrences of the specified elements. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElements(null, 1, 2)      = null
	 * Arrays.removeElements([], 1, 2)        = []
	 * Arrays.removeElements([1], 2, 3)       = [1]
	 * Arrays.removeElements([1, 3], 1, 2)    = [3]
	 * Arrays.removeElements([1, 3, 1], 1)    = [3, 1]
	 * Arrays.removeElements([1, 3, 1], 1, 1) = [3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param values the elements to be removed
	 * @return A new array containing the existing elements except the earliest-encountered
	 *         occurrences of the specified elements.
	 */
	public static short[] removeElements(final short[] array, final short... values) {
		if (isEmpty(array) || isEmpty(values)) {
			return clone(array);
		}
		final HashMap<Short, MutableInt> occurrences = new HashMap<Short, MutableInt>(values.length);
		for (final short v : values) {
			final Short boxed = Short.valueOf(v);
			final MutableInt count = occurrences.get(boxed);
			if (count == null) {
				occurrences.put(boxed, new MutableInt(1));
			}
			else {
				count.increment();
			}
		}
		final BitSet toRemove = new BitSet();
		for (final Map.Entry<Short, MutableInt> e : occurrences.entrySet()) {
			final Short v = e.getKey();
			int found = 0;
			for (int i = 0, ct = e.getValue().intValue(); i < ct; i++) {
				found = indexOf(array, v.shortValue(), found);
				if (found < 0) {
					break;
				}
				toRemove.set(found++);
			}
		}
		return (short[])removeAll(array, toRemove);
	}

	/**
	 * <p>
	 * Removes the elements at the specified positions from the specified array. All remaining
	 * elements are shifted to the left.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except those at the
	 * specified positions. The component type of the returned array is always the same as that of
	 * the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeAll([1], 0)             = []
	 * Arrays.removeAll([2, 6], 0)          = [6]
	 * Arrays.removeAll([2, 6], 0, 1)       = []
	 * Arrays.removeAll([2, 6, 3], 1, 2)    = [2]
	 * Arrays.removeAll([2, 6, 3], 0, 2)    = [6]
	 * Arrays.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static int[] removeAll(final int[] array, final int... indices) {
		return (int[])removeAll((Object)array, clone(indices));
	}

	/**
	 * <p>
	 * Removes occurrences of specified elements, in specified quantities, from the specified array.
	 * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
	 * quantities than contained in the original array, no change occurs beyond the removal of the
	 * existing matching items.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except for the
	 * earliest-encountered occurrences of the specified elements. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElements(null, 1, 2)      = null
	 * Arrays.removeElements([], 1, 2)        = []
	 * Arrays.removeElements([1], 2, 3)       = [1]
	 * Arrays.removeElements([1, 3], 1, 2)    = [3]
	 * Arrays.removeElements([1, 3, 1], 1)    = [3, 1]
	 * Arrays.removeElements([1, 3, 1], 1, 1) = [3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param values the elements to be removed
	 * @return A new array containing the existing elements except the earliest-encountered
	 *         occurrences of the specified elements.
	 */
	public static int[] removeElements(final int[] array, final int... values) {
		if (isEmpty(array) || isEmpty(values)) {
			return clone(array);
		}
		final HashMap<Integer, MutableInt> occurrences = new HashMap<Integer, MutableInt>(values.length);
		for (final int v : values) {
			final Integer boxed = Integer.valueOf(v);
			final MutableInt count = occurrences.get(boxed);
			if (count == null) {
				occurrences.put(boxed, new MutableInt(1));
			}
			else {
				count.increment();
			}
		}
		final BitSet toRemove = new BitSet();
		for (final Map.Entry<Integer, MutableInt> e : occurrences.entrySet()) {
			final Integer v = e.getKey();
			int found = 0;
			for (int i = 0, ct = e.getValue().intValue(); i < ct; i++) {
				found = indexOf(array, v.intValue(), found);
				if (found < 0) {
					break;
				}
				toRemove.set(found++);
			}
		}
		return (int[])removeAll(array, toRemove);
	}

	/**
	 * <p>
	 * Removes the elements at the specified positions from the specified array. All remaining
	 * elements are shifted to the left.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except those at the
	 * specified positions. The component type of the returned array is always the same as that of
	 * the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeAll([1], 0)             = []
	 * Arrays.removeAll([2, 6], 0)          = [6]
	 * Arrays.removeAll([2, 6], 0, 1)       = []
	 * Arrays.removeAll([2, 6, 3], 1, 2)    = [2]
	 * Arrays.removeAll([2, 6, 3], 0, 2)    = [6]
	 * Arrays.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static char[] removeAll(final char[] array, final int... indices) {
		return (char[])removeAll((Object)array, clone(indices));
	}

	/**
	 * <p>
	 * Removes occurrences of specified elements, in specified quantities, from the specified array.
	 * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
	 * quantities than contained in the original array, no change occurs beyond the removal of the
	 * existing matching items.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except for the
	 * earliest-encountered occurrences of the specified elements. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElements(null, 1, 2)      = null
	 * Arrays.removeElements([], 1, 2)        = []
	 * Arrays.removeElements([1], 2, 3)       = [1]
	 * Arrays.removeElements([1, 3], 1, 2)    = [3]
	 * Arrays.removeElements([1, 3, 1], 1)    = [3, 1]
	 * Arrays.removeElements([1, 3, 1], 1, 1) = [3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param values the elements to be removed
	 * @return A new array containing the existing elements except the earliest-encountered
	 *         occurrences of the specified elements.
	 */
	public static char[] removeElements(final char[] array, final char... values) {
		if (isEmpty(array) || isEmpty(values)) {
			return clone(array);
		}
		final HashMap<Character, MutableInt> occurrences = new HashMap<Character, MutableInt>(values.length);
		for (final char v : values) {
			final Character boxed = Character.valueOf(v);
			final MutableInt count = occurrences.get(boxed);
			if (count == null) {
				occurrences.put(boxed, new MutableInt(1));
			}
			else {
				count.increment();
			}
		}
		final BitSet toRemove = new BitSet();
		for (Map.Entry<Character, MutableInt> e : occurrences.entrySet()) {
			Character v = e.getKey();
			int found = 0;
			for (int i = 0, ct = e.getValue().intValue(); i < ct; i++) {
				found = indexOf(array, v.charValue(), found);
				if (found < 0) {
					break;
				}
				toRemove.set(found++);
			}
		}
		return (char[])removeAll(array, toRemove);
	}

	/**
	 * <p>
	 * Removes the elements at the specified positions from the specified array. All remaining
	 * elements are shifted to the left.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except those at the
	 * specified positions. The component type of the returned array is always the same as that of
	 * the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeAll([1], 0)             = []
	 * Arrays.removeAll([2, 6], 0)          = [6]
	 * Arrays.removeAll([2, 6], 0, 1)       = []
	 * Arrays.removeAll([2, 6, 3], 1, 2)    = [2]
	 * Arrays.removeAll([2, 6, 3], 0, 2)    = [6]
	 * Arrays.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static long[] removeAll(final long[] array, final int... indices) {
		return (long[])removeAll((Object)array, clone(indices));
	}

	/**
	 * <p>
	 * Removes occurrences of specified elements, in specified quantities, from the specified array.
	 * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
	 * quantities than contained in the original array, no change occurs beyond the removal of the
	 * existing matching items.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except for the
	 * earliest-encountered occurrences of the specified elements. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElements(null, 1, 2)      = null
	 * Arrays.removeElements([], 1, 2)        = []
	 * Arrays.removeElements([1], 2, 3)       = [1]
	 * Arrays.removeElements([1, 3], 1, 2)    = [3]
	 * Arrays.removeElements([1, 3, 1], 1)    = [3, 1]
	 * Arrays.removeElements([1, 3, 1], 1, 1) = [3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param values the elements to be removed
	 * @return A new array containing the existing elements except the earliest-encountered
	 *         occurrences of the specified elements.
	 */
	public static long[] removeElements(final long[] array, long... values) {
		if (isEmpty(array) || isEmpty(values)) {
			return clone(array);
		}
		final HashMap<Long, MutableInt> occurrences = new HashMap<Long, MutableInt>(values.length);
		for (final long v : values) {
			final Long boxed = Long.valueOf(v);
			final MutableInt count = occurrences.get(boxed);
			if (count == null) {
				occurrences.put(boxed, new MutableInt(1));
			}
			else {
				count.increment();
			}
		}
		final BitSet toRemove = new BitSet();
		for (final Map.Entry<Long, MutableInt> e : occurrences.entrySet()) {
			final Long v = e.getKey();
			int found = 0;
			for (int i = 0, ct = e.getValue().intValue(); i < ct; i++) {
				found = indexOf(array, v.longValue(), found);
				if (found < 0) {
					break;
				}
				toRemove.set(found++);
			}
		}
		return (long[])removeAll(array, toRemove);
	}

	/**
	 * <p>
	 * Removes the elements at the specified positions from the specified array. All remaining
	 * elements are shifted to the left.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except those at the
	 * specified positions. The component type of the returned array is always the same as that of
	 * the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeAll([1], 0)             = []
	 * Arrays.removeAll([2, 6], 0)          = [6]
	 * Arrays.removeAll([2, 6], 0, 1)       = []
	 * Arrays.removeAll([2, 6, 3], 1, 2)    = [2]
	 * Arrays.removeAll([2, 6, 3], 0, 2)    = [6]
	 * Arrays.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static float[] removeAll(final float[] array, final int... indices) {
		return (float[])removeAll((Object)array, clone(indices));
	}

	/**
	 * <p>
	 * Removes occurrences of specified elements, in specified quantities, from the specified array.
	 * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
	 * quantities than contained in the original array, no change occurs beyond the removal of the
	 * existing matching items.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except for the
	 * earliest-encountered occurrences of the specified elements. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElements(null, 1, 2)      = null
	 * Arrays.removeElements([], 1, 2)        = []
	 * Arrays.removeElements([1], 2, 3)       = [1]
	 * Arrays.removeElements([1, 3], 1, 2)    = [3]
	 * Arrays.removeElements([1, 3, 1], 1)    = [3, 1]
	 * Arrays.removeElements([1, 3, 1], 1, 1) = [3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param values the elements to be removed
	 * @return A new array containing the existing elements except the earliest-encountered
	 *         occurrences of the specified elements.
	 */
	public static float[] removeElements(final float[] array, final float... values) {
		if (isEmpty(array) || isEmpty(values)) {
			return clone(array);
		}
		final HashMap<Float, MutableInt> occurrences = new HashMap<Float, MutableInt>(values.length);
		for (final float v : values) {
			final Float boxed = Float.valueOf(v);
			final MutableInt count = occurrences.get(boxed);
			if (count == null) {
				occurrences.put(boxed, new MutableInt(1));
			}
			else {
				count.increment();
			}
		}
		final BitSet toRemove = new BitSet();
		for (final Map.Entry<Float, MutableInt> e : occurrences.entrySet()) {
			final Float v = e.getKey();
			int found = 0;
			for (int i = 0, ct = e.getValue().intValue(); i < ct; i++) {
				found = indexOf(array, v.floatValue(), found);
				if (found < 0) {
					break;
				}
				toRemove.set(found++);
			}
		}
		return (float[])removeAll(array, toRemove);
	}

	/**
	 * <p>
	 * Removes the elements at the specified positions from the specified array. All remaining
	 * elements are shifted to the left.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except those at the
	 * specified positions. The component type of the returned array is always the same as that of
	 * the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeAll([1], 0)             = []
	 * Arrays.removeAll([2, 6], 0)          = [6]
	 * Arrays.removeAll([2, 6], 0, 1)       = []
	 * Arrays.removeAll([2, 6, 3], 1, 2)    = [2]
	 * Arrays.removeAll([2, 6, 3], 0, 2)    = [6]
	 * Arrays.removeAll([2, 6, 3], 0, 1, 2) = []
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static double[] removeAll(final double[] array, final int... indices) {
		return (double[])removeAll((Object)array, clone(indices));
	}

	/**
	 * <p>
	 * Removes occurrences of specified elements, in specified quantities, from the specified array.
	 * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
	 * quantities than contained in the original array, no change occurs beyond the removal of the
	 * existing matching items.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except for the
	 * earliest-encountered occurrences of the specified elements. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElements(null, 1, 2)      = null
	 * Arrays.removeElements([], 1, 2)        = []
	 * Arrays.removeElements([1], 2, 3)       = [1]
	 * Arrays.removeElements([1, 3], 1, 2)    = [3]
	 * Arrays.removeElements([1, 3, 1], 1)    = [3, 1]
	 * Arrays.removeElements([1, 3, 1], 1, 1) = [3]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param values the elements to be removed
	 * @return A new array containing the existing elements except the earliest-encountered
	 *         occurrences of the specified elements.
	 */
	public static double[] removeElements(final double[] array, final double... values) {
		if (isEmpty(array) || isEmpty(values)) {
			return clone(array);
		}
		final HashMap<Double, MutableInt> occurrences = new HashMap<Double, MutableInt>(values.length);
		for (final double v : values) {
			final Double boxed = Double.valueOf(v);
			final MutableInt count = occurrences.get(boxed);
			if (count == null) {
				occurrences.put(boxed, new MutableInt(1));
			}
			else {
				count.increment();
			}
		}
		final BitSet toRemove = new BitSet();
		for (final Map.Entry<Double, MutableInt> e : occurrences.entrySet()) {
			final Double v = e.getKey();
			int found = 0;
			for (int i = 0, ct = e.getValue().intValue(); i < ct; i++) {
				found = indexOf(array, v.doubleValue(), found);
				if (found < 0) {
					break;
				}
				toRemove.set(found++);
			}
		}
		return (double[])removeAll(array, toRemove);
	}

	/**
	 * <p>
	 * Removes the elements at the specified positions from the specified array. All remaining
	 * elements are shifted to the left.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except those at the
	 * specified positions. The component type of the returned array is always the same as that of
	 * the input array.
	 * </p>
	 * <p>
	 * If the input array is {@code null}, an IndexOutOfBoundsException will be thrown, because in
	 * that case no valid index can be specified.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeAll([true, false, true], 0, 2) = [false]
	 * Arrays.removeAll([true, false, true], 1, 2) = [true]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may not be {@code null}
	 * @param indices the positions of the elements to be removed
	 * @return A new array containing the existing elements except those at the specified positions.
	 * @throws IndexOutOfBoundsException if any index is out of range (index < 0 || index >=
	 *             array.length), or if the array is {@code null}.
	 */
	public static boolean[] removeAll(final boolean[] array, final int... indices) {
		return (boolean[])removeAll((Object)array, clone(indices));
	}

	/**
	 * <p>
	 * Removes occurrences of specified elements, in specified quantities, from the specified array.
	 * All subsequent elements are shifted left. For any element-to-be-removed specified in greater
	 * quantities than contained in the original array, no change occurs beyond the removal of the
	 * existing matching items.
	 * </p>
	 * <p>
	 * This method returns a new array with the same elements of the input array except for the
	 * earliest-encountered occurrences of the specified elements. The component type of the
	 * returned array is always the same as that of the input array.
	 * </p>
	 * 
	 * <pre>
	 * Arrays.removeElements(null, true, false)               = null
	 * Arrays.removeElements([], true, false)                 = []
	 * Arrays.removeElements([true], false, false)            = [true]
	 * Arrays.removeElements([true, false], true, true)       = [false]
	 * Arrays.removeElements([true, false, true], true)       = [false, true]
	 * Arrays.removeElements([true, false, true], true, true) = [false]
	 * </pre>
	 * 
	 * @param array the array to remove the element from, may be {@code null}
	 * @param values the elements to be removed
	 * @return A new array containing the existing elements except the earliest-encountered
	 *         occurrences of the specified elements.
	 */
	public static boolean[] removeElements(final boolean[] array, final boolean... values) {
		if (isEmpty(array) || isEmpty(values)) {
			return clone(array);
		}
		final HashMap<Boolean, MutableInt> occurrences = new HashMap<Boolean, MutableInt>(2); // only
																								// two
																								// possible
																								// values
																								// here
		for (final boolean v : values) {
			final Boolean boxed = Boolean.valueOf(v);
			final MutableInt count = occurrences.get(boxed);
			if (count == null) {
				occurrences.put(boxed, new MutableInt(1));
			}
			else {
				count.increment();
			}
		}
		final BitSet toRemove = new BitSet();
		for (final Map.Entry<Boolean, MutableInt> e : occurrences.entrySet()) {
			final Boolean v = e.getKey();
			int found = 0;
			for (int i = 0, ct = e.getValue().intValue(); i < ct; i++) {
				found = indexOf(array, v.booleanValue(), found);
				if (found < 0) {
					break;
				}
				toRemove.set(found++);
			}
		}
		return (boolean[])removeAll(array, toRemove);
	}

	/**
	 * Removes multiple array elements specified by index.
	 * 
	 * @param array source
	 * @param indices to remove, WILL BE SORTED--so only clones of user-owned arrays!
	 * @return new array of same type minus elements specified by unique values of {@code indices}
	 */
	static Object removeAll(final Object array, final int... indices) {
		final int length = getLength(array);
		int diff = 0;

		if (isNotEmpty(indices)) {
			java.util.Arrays.sort(indices);

			int i = indices.length;
			int prevIndex = length;
			while (--i >= 0) {
				final int index = indices[i];
				if (index < 0 || index >= length) {
					throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
				}
				if (index >= prevIndex) {
					continue;
				}
				diff++;
				prevIndex = index;
			}
		}
		final Object result = Array.newInstance(array.getClass().getComponentType(), length - diff);
		if (diff < length) {
			int end = length;
			int dest = length - diff;
			for (int i = indices.length - 1; i >= 0; i--) {
				final int index = indices[i];
				if (end - index > 1) { // same as (cp > 0)
					final int cp = end - index - 1;
					dest -= cp;
					System.arraycopy(array, index + 1, result, dest, cp);
				}
				end = index;
			}
			if (end > 0) {
				System.arraycopy(array, 0, result, 0, end);
			}
		}
		return result;
	}

	/**
	 * Removes multiple array elements specified by indices.
	 * 
	 * @param array source
	 * @param indices to remove
	 * @return new array of same type minus elements specified by the set bits in {@code indices}
	 */
	// package protected for access by unit tests
	static Object removeAll(final Object array, final BitSet indices) {
		final int srcLength = getLength(array);
		// No need to check maxIndex here, because method only currently called from
		// removeElements()
		// which guarantee to generate on;y valid bit entries.
		// final int maxIndex = indices.length();
		// if (maxIndex > srcLength) {
		// throw new IndexOutOfBoundsException("Index: " + (maxIndex-1) + ", Length: " + srcLength);
		// }
		final int removals = indices.cardinality(); // true bits are items to remove
		final Object result = Array.newInstance(array.getClass().getComponentType(), srcLength - removals);
		int srcIndex = 0;
		int destIndex = 0;
		int count;
		int set;
		while ((set = indices.nextSetBit(srcIndex)) != -1) {
			count = set - srcIndex;
			if (count > 0) {
				System.arraycopy(array, srcIndex, result, destIndex, count);
				destIndex += count;
			}
			srcIndex = indices.nextClearBit(set);
		}
		count = srcLength - srcIndex;
		if (count > 0) {
			System.arraycopy(array, srcIndex, result, destIndex, count);
		}
		return result;
	}

	public static Object newInstance(Type componentType, int size) {
		Class componentClazz = Types.getRawType(componentType);
		return Array.newInstance(componentClazz, size);
	}

	/**
	 * swap array element
	 * 
	 * @param array the array to swap
	 * @param i element index
	 * @param j element index
	 */
	public static <T> void swap(T[] array, int i, int j) {
		T t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	/**
	 * swap array element
	 * 
	 * @param array the array to swap
	 * @param i element index
	 * @param j element index
	 */
	public static void swap(boolean[] array, int i, int j) {
		boolean t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	/**
	 * swap array element
	 * 
	 * @param array the array to swap
	 * @param i element index
	 * @param j element index
	 */
	public static void swap(byte[] array, int i, int j) {
		byte t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	/**
	 * swap array element
	 * 
	 * @param array the array to swap
	 * @param i element index
	 * @param j element index
	 */
	public static void swap(char[] array, int i, int j) {
		char t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	/**
	 * swap array element
	 * 
	 * @param array the array to swap
	 * @param i element index
	 * @param j element index
	 */
	public static void swap(double[] array, int i, int j) {
		double t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	/**
	 * swap array element
	 * 
	 * @param array the array to swap
	 * @param i element index
	 * @param j element index
	 */
	public static void swap(float[] array, int i, int j) {
		float t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	/**
	 * swap array element
	 * 
	 * @param array the array to swap
	 * @param i element index
	 * @param j element index
	 */
	public static void swap(int[] array, int i, int j) {
		int t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	/**
	 * swap array element
	 * 
	 * @param array the array to swap
	 * @param i element index
	 * @param j element index
	 */
	public static void swap(long[] array, int i, int j) {
		long t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	/**
	 * swap array element
	 * 
	 * @param array the array to swap
	 * @param i element index
	 * @param j element index
	 */
	public static void swap(short[] array, int i, int j) {
		short t = array[i];
		array[i] = array[j];
		array[j] = t;
	}

	/**
	 * Remove duplicate elements from the given array. Also sorts the array, as it uses a TreeSet.
	 * 
	 * @param array the sorted array
	 * @return an array without duplicates, in natural sort order
	 */
	public static Object[] removeDuplicate(Object[] array) {
		if (Arrays.isEmpty(array)) {
			return array;
		}
		Set<Object> set = new TreeSet<Object>();
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return set.toArray((Object[])Array.newInstance(array.getClass(), set.size()));
	}

	/**
	 * Merge the given String arrays into one, with overlapping array elements only included once.
	 * <p>
	 * The order of elements in the original arrays is preserved (with the exception of overlapping
	 * elements, which are only included on their first occurence).
	 * 
	 * @param array1 the first array (can be <code>null</code>)
	 * @param array2 the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
	 */
	public static String[] merge(String[] array1, String[] array2) {
		if (isEmpty(array1)) {
			return array2;
		}
		if (isEmpty(array2)) {
			return array1;
		}
		List<String> result = toList(array1);
		for (int i = 0; i < array2.length; i++) {
			String str = array2[i];
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return result.toArray(new String[result.size()]);
	}

	// ----------------------------------------------------------------------------
	// @see java.util.Arrays

	/**
	 * Sorts the specified array of longs into ascending numerical order.
	 * 
	 * @param a the array to be sorted
	 */
	public static void sort(long[] a) {
		java.util.Arrays.sort(a);
	}

	/**
	 * Sorts the specified range of the specified array of longs into ascending numerical order. The
	 * range to be sorted extends from index <tt>fromIndex</tt>, inclusive, to index
	 * <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be sorted is
	 * empty.)
	 * 
	 * @param a the array to be sorted
	 * @param fromIndex the index of the first element (inclusive) to be sorted
	 * @param toIndex the index of the last element (exclusive) to be sorted
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void sort(long[] a, int fromIndex, int toIndex) {
		java.util.Arrays.sort(a, fromIndex, toIndex);
	}

	/**
	 * Sorts the specified array of ints into ascending numerical order.
	 * 
	 * @param a the array to be sorted
	 */
	public static void sort(int[] a) {
		java.util.Arrays.sort(a);
	}

	/**
	 * Sorts the specified range of the specified array of ints into ascending numerical order. The
	 * range to be sorted extends from index <tt>fromIndex</tt>, inclusive, to index
	 * <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be sorted is
	 * empty.)
	 * <p>
	 * 
	 * @param a the array to be sorted
	 * @param fromIndex the index of the first element (inclusive) to be sorted
	 * @param toIndex the index of the last element (exclusive) to be sorted
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void sort(int[] a, int fromIndex, int toIndex) {
		java.util.Arrays.sort(a, fromIndex, toIndex);
	}

	/**
	 * Sorts the specified array of shorts into ascending numerical order.
	 * 
	 * @param a the array to be sorted
	 */
	public static void sort(short[] a) {
		java.util.Arrays.sort(a);
	}

	/**
	 * Sorts the specified range of the specified array of shorts into ascending numerical order.
	 * The range to be sorted extends from index <tt>fromIndex</tt>, inclusive, to index
	 * <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be sorted is
	 * empty.)
	 * <p>
	 * 
	 * @param a the array to be sorted
	 * @param fromIndex the index of the first element (inclusive) to be sorted
	 * @param toIndex the index of the last element (exclusive) to be sorted
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void sort(short[] a, int fromIndex, int toIndex) {
		java.util.Arrays.sort(a, fromIndex, toIndex);
	}

	/**
	 * Sorts the specified array of chars into ascending numerical order.
	 * 
	 * @param a the array to be sorted
	 */
	public static void sort(char[] a) {
		java.util.Arrays.sort(a);
	}

	/**
	 * Sorts the specified range of the specified array of chars into ascending numerical order. The
	 * range to be sorted extends from index <tt>fromIndex</tt>, inclusive, to index
	 * <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be sorted is
	 * empty.)
	 * <p>
	 * 
	 * @param a the array to be sorted
	 * @param fromIndex the index of the first element (inclusive) to be sorted
	 * @param toIndex the index of the last element (exclusive) to be sorted
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void sort(char[] a, int fromIndex, int toIndex) {
		java.util.Arrays.sort(a, fromIndex, toIndex);
	}

	/**
	 * Sorts the specified array of bytes into ascending numerical order.
	 * 
	 * @param a the array to be sorted
	 */
	public static void sort(byte[] a) {
		java.util.Arrays.sort(a);
	}

	/**
	 * Sorts the specified range of the specified array of bytes into ascending numerical order. The
	 * range to be sorted extends from index <tt>fromIndex</tt>, inclusive, to index
	 * <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be sorted is
	 * empty.)
	 * <p>
	 * 
	 * @param a the array to be sorted
	 * @param fromIndex the index of the first element (inclusive) to be sorted
	 * @param toIndex the index of the last element (exclusive) to be sorted
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void sort(byte[] a, int fromIndex, int toIndex) {
		java.util.Arrays.sort(a, fromIndex, toIndex);
	}

	/**
	 * Sorts the specified array of doubles into ascending numerical order.
	 * 
	 * @param a the array to be sorted
	 */
	public static void sort(double[] a) {
		java.util.Arrays.sort(a);
	}

	/**
	 * Sorts the specified range of the specified array of doubles into ascending numerical order.
	 * The range to be sorted extends from index <tt>fromIndex</tt>, inclusive, to index
	 * <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be sorted is
	 * empty.)
	 * 
	 * @param a the array to be sorted
	 * @param fromIndex the index of the first element (inclusive) to be sorted
	 * @param toIndex the index of the last element (exclusive) to be sorted
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void sort(double[] a, int fromIndex, int toIndex) {
		java.util.Arrays.sort(a, fromIndex, toIndex);
	}

	/**
	 * Sorts the specified array of floats into ascending numerical order.
	 * 
	 * @param a the array to be sorted
	 */
	public static void sort(float[] a) {
		java.util.Arrays.sort(a);
	}

	/**
	 * Sorts the specified range of the specified array of floats into ascending numerical order.
	 * The range to be sorted extends from index <tt>fromIndex</tt>, inclusive, to index
	 * <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be sorted is
	 * empty.)
	 * 
	 * @param a the array to be sorted
	 * @param fromIndex the index of the first element (inclusive) to be sorted
	 * @param toIndex the index of the last element (exclusive) to be sorted
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void sort(float[] a, int fromIndex, int toIndex) {
		java.util.Arrays.sort(a, fromIndex, toIndex);
	}

	/**
	 * Sorts the specified array of objects into ascending order, according to the
	 * {@linkplain Comparable natural ordering} of its elements. All elements in the array must
	 * implement the {@link Comparable} interface. Furthermore, all elements in the array must be
	 * <i>mutually comparable</i> (that is, <tt>e1.compareTo(e2)</tt> must not throw a
	 * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and <tt>e2</tt> in the array).
	 * <p>
	 * This sort is guaranteed to be <i>stable</i>: equal elements will not be reordered as a result
	 * of the sort.
	 * <p>
	 * 
	 * @param a the array to be sorted
	 * @throws ClassCastException if the array contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers).
	 */
	public static void sort(Object[] a) {
		java.util.Arrays.sort(a);
	}

	/**
	 * Sorts the specified range of the specified array of objects into ascending order, according
	 * to the {@linkplain Comparable natural ordering} of its elements. The range to be sorted
	 * extends from index <tt>fromIndex</tt>, inclusive, to index <tt>toIndex</tt>, exclusive. (If
	 * <tt>fromIndex==toIndex</tt>, the range to be sorted is empty.) All elements in this range
	 * must implement the {@link Comparable} interface. Furthermore, all elements in this range must
	 * be <i>mutually comparable</i> (that is, <tt>e1.compareTo(e2)</tt> must not throw a
	 * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and <tt>e2</tt> in the array).
	 * <p>
	 * This sort is guaranteed to be <i>stable</i>: equal elements will not be reordered as a result
	 * of the sort.
	 * <p>
	 * 
	 * @param a the array to be sorted
	 * @param fromIndex the index of the first element (inclusive) to be sorted
	 * @param toIndex the index of the last element (exclusive) to be sorted
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 * @throws ClassCastException if the array contains elements that are not <i>mutually
	 *             comparable</i> (for example, strings and integers).
	 */
	public static void sort(Object[] a, int fromIndex, int toIndex) {
		java.util.Arrays.sort(a, fromIndex, toIndex);
	}

	/**
	 * Sorts the specified array of objects according to the order induced by the specified
	 * comparator. All elements in the array must be <i>mutually comparable</i> by the specified
	 * comparator (that is, <tt>c.compare(e1, e2)</tt> must not throw a <tt>ClassCastException</tt>
	 * for any elements <tt>e1</tt> and <tt>e2</tt> in the array).
	 * <p>
	 * This sort is guaranteed to be <i>stable</i>: equal elements will not be reordered as a result
	 * of the sort.
	 * <p>
	 * 
	 * @param a the array to be sorted
	 * @param c the comparator to determine the order of the array. A <tt>null</tt> value indicates
	 *            that the elements' {@linkplain Comparable natural ordering} should be used.
	 * @throws ClassCastException if the array contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator.
	 */
	public static <T> void sort(T[] a, Comparator<? super T> c) {
		java.util.Arrays.sort(a, c);
	}

	/**
	 * Sorts the specified range of the specified array of objects according to the order induced by
	 * the specified comparator. The range to be sorted extends from index <tt>fromIndex</tt>,
	 * inclusive, to index <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range
	 * to be sorted is empty.) All elements in the range must be <i>mutually comparable</i> by the
	 * specified comparator (that is, <tt>c.compare(e1, e2)</tt> must not throw a
	 * <tt>ClassCastException</tt> for any elements <tt>e1</tt> and <tt>e2</tt> in the range).
	 * <p>
	 * This sort is guaranteed to be <i>stable</i>: equal elements will not be reordered as a result
	 * of the sort.
	 * <p>
	 * 
	 * @param a the array to be sorted
	 * @param fromIndex the index of the first element (inclusive) to be sorted
	 * @param toIndex the index of the last element (exclusive) to be sorted
	 * @param c the comparator to determine the order of the array. A <tt>null</tt> value indicates
	 *            that the elements' {@linkplain Comparable natural ordering} should be used.
	 * @throws ClassCastException if the array contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator.
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static <T> void sort(T[] a, int fromIndex, int toIndex, Comparator<? super T> c) {
		java.util.Arrays.sort(a, fromIndex, toIndex);
	}

	// Searching

	/**
	 * Searches the specified array of longs for the specified value using the binary search
	 * algorithm. The array must be sorted (as by the {@link #sort(long[])} method) prior to making
	 * this call. If it is not sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will be found.
	 * 
	 * @param a the array to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the array: the index of the first
	 *         element greater than the key, or <tt>a.length</tt> if all elements in the array are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 */
	public static int binarySearch(long[] a, long key) {
		return java.util.Arrays.binarySearch(a, key);
	}

	/**
	 * Searches a range of the specified array of longs for the specified value using the binary
	 * search algorithm. The range must be sorted (as by the {@link #sort(long[], int, int)} method)
	 * prior to making this call. If it is not sorted, the results are undefined. If the range
	 * contains multiple elements with the specified value, there is no guarantee which one will be
	 * found.
	 * 
	 * @param a the array to be searched
	 * @param fromIndex the index of the first element (inclusive) to be searched
	 * @param toIndex the index of the last element (exclusive) to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array within the specified range;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is
	 *         defined as the point at which the key would be inserted into the array: the index of
	 *         the first element in the range greater than the key, or <tt>toIndex</tt> if all
	 *         elements in the range are less than the specified key. Note that this guarantees that
	 *         the return value will be &gt;= 0 if and only if the key is found.
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0 or toIndex > a.length}
	 */
	public static int binarySearch(long[] a, int fromIndex, int toIndex, long key) {
		return java.util.Arrays.binarySearch(a, fromIndex, toIndex, key);
	}

	/**
	 * Searches the specified array of ints for the specified value using the binary search
	 * algorithm. The array must be sorted (as by the {@link #sort(int[])} method) prior to making
	 * this call. If it is not sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will be found.
	 * 
	 * @param a the array to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the array: the index of the first
	 *         element greater than the key, or <tt>a.length</tt> if all elements in the array are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 */
	public static int binarySearch(int[] a, int key) {
		return java.util.Arrays.binarySearch(a, key);
	}

	/**
	 * Searches a range of the specified array of ints for the specified value using the binary
	 * search algorithm. The range must be sorted (as by the {@link #sort(int[], int, int)} method)
	 * prior to making this call. If it is not sorted, the results are undefined. If the range
	 * contains multiple elements with the specified value, there is no guarantee which one will be
	 * found.
	 * 
	 * @param a the array to be searched
	 * @param fromIndex the index of the first element (inclusive) to be searched
	 * @param toIndex the index of the last element (exclusive) to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array within the specified range;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is
	 *         defined as the point at which the key would be inserted into the array: the index of
	 *         the first element in the range greater than the key, or <tt>toIndex</tt> if all
	 *         elements in the range are less than the specified key. Note that this guarantees that
	 *         the return value will be &gt;= 0 if and only if the key is found.
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0 or toIndex > a.length}
	 */
	public static int binarySearch(int[] a, int fromIndex, int toIndex, int key) {
		return java.util.Arrays.binarySearch(a, fromIndex, toIndex, key);
	}

	/**
	 * Searches the specified array of shorts for the specified value using the binary search
	 * algorithm. The array must be sorted (as by the {@link #sort(short[])} method) prior to making
	 * this call. If it is not sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will be found.
	 * 
	 * @param a the array to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the array: the index of the first
	 *         element greater than the key, or <tt>a.length</tt> if all elements in the array are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 */
	public static int binarySearch(short[] a, short key) {
		return java.util.Arrays.binarySearch(a, key);
	}

	/**
	 * Searches a range of the specified array of shorts for the specified value using the binary
	 * search algorithm. The range must be sorted (as by the {@link #sort(short[], int, int)}
	 * method) prior to making this call. If it is not sorted, the results are undefined. If the
	 * range contains multiple elements with the specified value, there is no guarantee which one
	 * will be found.
	 * 
	 * @param a the array to be searched
	 * @param fromIndex the index of the first element (inclusive) to be searched
	 * @param toIndex the index of the last element (exclusive) to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array within the specified range;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is
	 *         defined as the point at which the key would be inserted into the array: the index of
	 *         the first element in the range greater than the key, or <tt>toIndex</tt> if all
	 *         elements in the range are less than the specified key. Note that this guarantees that
	 *         the return value will be &gt;= 0 if and only if the key is found.
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0 or toIndex > a.length}
	 */
	public static int binarySearch(short[] a, int fromIndex, int toIndex, short key) {
		return java.util.Arrays.binarySearch(a, fromIndex, toIndex, key);
	}

	/**
	 * Searches the specified array of chars for the specified value using the binary search
	 * algorithm. The array must be sorted (as by the {@link #sort(char[])} method) prior to making
	 * this call. If it is not sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will be found.
	 * 
	 * @param a the array to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the array: the index of the first
	 *         element greater than the key, or <tt>a.length</tt> if all elements in the array are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 */
	public static int binarySearch(char[] a, char key) {
		return java.util.Arrays.binarySearch(a, key);
	}

	/**
	 * Searches a range of the specified array of chars for the specified value using the binary
	 * search algorithm. The range must be sorted (as by the {@link #sort(char[], int, int)} method)
	 * prior to making this call. If it is not sorted, the results are undefined. If the range
	 * contains multiple elements with the specified value, there is no guarantee which one will be
	 * found.
	 * 
	 * @param a the array to be searched
	 * @param fromIndex the index of the first element (inclusive) to be searched
	 * @param toIndex the index of the last element (exclusive) to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array within the specified range;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is
	 *         defined as the point at which the key would be inserted into the array: the index of
	 *         the first element in the range greater than the key, or <tt>toIndex</tt> if all
	 *         elements in the range are less than the specified key. Note that this guarantees that
	 *         the return value will be &gt;= 0 if and only if the key is found.
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0 or toIndex > a.length}
	 */
	public static int binarySearch(char[] a, int fromIndex, int toIndex, char key) {
		return java.util.Arrays.binarySearch(a, fromIndex, toIndex, key);
	}

	/**
	 * Searches the specified array of bytes for the specified value using the binary search
	 * algorithm. The array must be sorted (as by the {@link #sort(byte[])} method) prior to making
	 * this call. If it is not sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will be found.
	 * 
	 * @param a the array to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the array: the index of the first
	 *         element greater than the key, or <tt>a.length</tt> if all elements in the array are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 */
	public static int binarySearch(byte[] a, byte key) {
		return java.util.Arrays.binarySearch(a, key);
	}

	/**
	 * Searches a range of the specified array of bytes for the specified value using the binary
	 * search algorithm. The range must be sorted (as by the {@link #sort(byte[], int, int)} method)
	 * prior to making this call. If it is not sorted, the results are undefined. If the range
	 * contains multiple elements with the specified value, there is no guarantee which one will be
	 * found.
	 * 
	 * @param a the array to be searched
	 * @param fromIndex the index of the first element (inclusive) to be searched
	 * @param toIndex the index of the last element (exclusive) to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array within the specified range;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is
	 *         defined as the point at which the key would be inserted into the array: the index of
	 *         the first element in the range greater than the key, or <tt>toIndex</tt> if all
	 *         elements in the range are less than the specified key. Note that this guarantees that
	 *         the return value will be &gt;= 0 if and only if the key is found.
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0 or toIndex > a.length}
	 */
	public static int binarySearch(byte[] a, int fromIndex, int toIndex, byte key) {
		return java.util.Arrays.binarySearch(a, fromIndex, toIndex, key);
	}

	/**
	 * Searches the specified array of doubles for the specified value using the binary search
	 * algorithm. The array must be sorted (as by the {@link #sort(double[])} method) prior to
	 * making this call. If it is not sorted, the results are undefined. If the array contains
	 * multiple elements with the specified value, there is no guarantee which one will be found.
	 * This method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a the array to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the array: the index of the first
	 *         element greater than the key, or <tt>a.length</tt> if all elements in the array are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 */
	public static int binarySearch(double[] a, double key) {
		return java.util.Arrays.binarySearch(a, key);
	}

	/**
	 * Searches a range of the specified array of doubles for the specified value using the binary
	 * search algorithm. The range must be sorted (as by the {@link #sort(double[], int, int)}
	 * method) prior to making this call. If it is not sorted, the results are undefined. If the
	 * range contains multiple elements with the specified value, there is no guarantee which one
	 * will be found. This method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a the array to be searched
	 * @param fromIndex the index of the first element (inclusive) to be searched
	 * @param toIndex the index of the last element (exclusive) to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array within the specified range;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is
	 *         defined as the point at which the key would be inserted into the array: the index of
	 *         the first element in the range greater than the key, or <tt>toIndex</tt> if all
	 *         elements in the range are less than the specified key. Note that this guarantees that
	 *         the return value will be &gt;= 0 if and only if the key is found.
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0 or toIndex > a.length}
	 */
	public static int binarySearch(double[] a, int fromIndex, int toIndex, double key) {
		return java.util.Arrays.binarySearch(a, fromIndex, toIndex, key);
	}

	/**
	 * Searches the specified array of floats for the specified value using the binary search
	 * algorithm. The array must be sorted (as by the {@link #sort(float[])} method) prior to making
	 * this call. If it is not sorted, the results are undefined. If the array contains multiple
	 * elements with the specified value, there is no guarantee which one will be found. This method
	 * considers all NaN values to be equivalent and equal.
	 * 
	 * @param a the array to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the array: the index of the first
	 *         element greater than the key, or <tt>a.length</tt> if all elements in the array are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 */
	public static int binarySearch(float[] a, float key) {
		return java.util.Arrays.binarySearch(a, key);
	}

	/**
	 * Searches a range of the specified array of floats for the specified value using the binary
	 * search algorithm. The range must be sorted (as by the {@link #sort(float[], int, int)}
	 * method) prior to making this call. If it is not sorted, the results are undefined. If the
	 * range contains multiple elements with the specified value, there is no guarantee which one
	 * will be found. This method considers all NaN values to be equivalent and equal.
	 * 
	 * @param a the array to be searched
	 * @param fromIndex the index of the first element (inclusive) to be searched
	 * @param toIndex the index of the last element (exclusive) to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array within the specified range;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is
	 *         defined as the point at which the key would be inserted into the array: the index of
	 *         the first element in the range greater than the key, or <tt>toIndex</tt> if all
	 *         elements in the range are less than the specified key. Note that this guarantees that
	 *         the return value will be &gt;= 0 if and only if the key is found.
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0 or toIndex > a.length}
	 */
	public static int binarySearch(float[] a, int fromIndex, int toIndex, float key) {
		return java.util.Arrays.binarySearch(a, fromIndex, toIndex, key);
	}

	/**
	 * Searches the specified array for the specified object using the binary search algorithm. The
	 * array must be sorted into ascending order according to the {@linkplain Comparable natural
	 * ordering} of its elements (as by the {@link #sort(Object[])} method) prior to making this
	 * call. If it is not sorted, the results are undefined. (If the array contains elements that
	 * are not mutually comparable (for example, strings and integers), it <i>cannot</i> be sorted
	 * according to the natural ordering of its elements, hence results are undefined.) If the array
	 * contains multiple elements equal to the specified object, there is no guarantee which one
	 * will be found.
	 * 
	 * @param a the array to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the array: the index of the first
	 *         element greater than the key, or <tt>a.length</tt> if all elements in the array are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 * @throws ClassCastException if the search key is not comparable to the elements of the array.
	 */
	public static int binarySearch(Object[] a, Object key) {
		return java.util.Arrays.binarySearch(a, key);
	}

	/**
	 * Searches a range of the specified array for the specified object using the binary search
	 * algorithm. The range must be sorted into ascending order according to the
	 * {@linkplain Comparable natural ordering} of its elements (as by the
	 * {@link #sort(Object[], int, int)} method) prior to making this call. If it is not sorted, the
	 * results are undefined. (If the range contains elements that are not mutually comparable (for
	 * example, strings and integers), it <i>cannot</i> be sorted according to the natural ordering
	 * of its elements, hence results are undefined.) If the range contains multiple elements equal
	 * to the specified object, there is no guarantee which one will be found.
	 * 
	 * @param a the array to be searched
	 * @param fromIndex the index of the first element (inclusive) to be searched
	 * @param toIndex the index of the last element (exclusive) to be searched
	 * @param key the value to be searched for
	 * @return index of the search key, if it is contained in the array within the specified range;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is
	 *         defined as the point at which the key would be inserted into the array: the index of
	 *         the first element in the range greater than the key, or <tt>toIndex</tt> if all
	 *         elements in the range are less than the specified key. Note that this guarantees that
	 *         the return value will be &gt;= 0 if and only if the key is found.
	 * @throws ClassCastException if the search key is not comparable to the elements of the array
	 *             within the specified range.
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0 or toIndex > a.length}
	 */
	public static int binarySearch(Object[] a, int fromIndex, int toIndex, Object key) {
		return java.util.Arrays.binarySearch(a, fromIndex, toIndex, key);
	}

	/**
	 * Searches the specified array for the specified object using the binary search algorithm. The
	 * array must be sorted into ascending order according to the specified comparator (as by the
	 * {@link #sort(Object[], Comparator) sort(T[], Comparator)} method) prior to making this call.
	 * If it is not sorted, the results are undefined. If the array contains multiple elements equal
	 * to the specified object, there is no guarantee which one will be found.
	 * 
	 * @param a the array to be searched
	 * @param key the value to be searched for
	 * @param c the comparator by which the array is ordered. A <tt>null</tt> value indicates that
	 *            the elements' {@linkplain Comparable natural ordering} should be used.
	 * @return index of the search key, if it is contained in the array; otherwise,
	 *         <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is defined as
	 *         the point at which the key would be inserted into the array: the index of the first
	 *         element greater than the key, or <tt>a.length</tt> if all elements in the array are
	 *         less than the specified key. Note that this guarantees that the return value will be
	 *         &gt;= 0 if and only if the key is found.
	 * @throws ClassCastException if the array contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator, or the search key is not
	 *             comparable to the elements of the array using this comparator.
	 */
	public static <T> int binarySearch(T[] a, T key, Comparator<? super T> c) {
		return java.util.Arrays.binarySearch(a, key, c);
	}

	/**
	 * Searches a range of the specified array for the specified object using the binary search
	 * algorithm. The range must be sorted into ascending order according to the specified
	 * comparator (as by the {@link #sort(Object[], int, int, Comparator) sort(T[], int, int,
	 * Comparator)} method) prior to making this call. If it is not sorted, the results are
	 * undefined. If the range contains multiple elements equal to the specified object, there is no
	 * guarantee which one will be found.
	 * 
	 * @param a the array to be searched
	 * @param fromIndex the index of the first element (inclusive) to be searched
	 * @param toIndex the index of the last element (exclusive) to be searched
	 * @param key the value to be searched for
	 * @param c the comparator by which the array is ordered. A <tt>null</tt> value indicates that
	 *            the elements' {@linkplain Comparable natural ordering} should be used.
	 * @return index of the search key, if it is contained in the array within the specified range;
	 *         otherwise, <tt>(-(<i>insertion point</i>) - 1)</tt>. The <i>insertion point</i> is
	 *         defined as the point at which the key would be inserted into the array: the index of
	 *         the first element in the range greater than the key, or <tt>toIndex</tt> if all
	 *         elements in the range are less than the specified key. Note that this guarantees that
	 *         the return value will be &gt;= 0 if and only if the key is found.
	 * @throws ClassCastException if the range contains elements that are not <i>mutually
	 *             comparable</i> using the specified comparator, or the search key is not
	 *             comparable to the elements in the range using this comparator.
	 * @throws IllegalArgumentException if {@code fromIndex > toIndex}
	 * @throws ArrayIndexOutOfBoundsException if {@code fromIndex < 0 or toIndex > a.length}
	 */
	public static <T> int binarySearch(T[] a, int fromIndex, int toIndex, T key, Comparator<? super T> c) {
		return java.util.Arrays.binarySearch(a, fromIndex, toIndex, key, c);
	}

	// Equality Testing

	/**
	 * Returns <tt>true</tt> if the two specified arrays of longs are <i>equal</i> to one another.
	 * Two arrays are considered equal if both arrays contain the same number of elements, and all
	 * corresponding pairs of elements in the two arrays are equal. In other words, two arrays are
	 * equal if they contain the same elements in the same order. Also, two array references are
	 * considered equal if both are <tt>null</tt>.
	 * <p>
	 * 
	 * @param a one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 */
	public static boolean equals(long[] a, long[] a2) {
		return java.util.Arrays.equals(a, a2);
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of ints are <i>equal</i> to one another.
	 * Two arrays are considered equal if both arrays contain the same number of elements, and all
	 * corresponding pairs of elements in the two arrays are equal. In other words, two arrays are
	 * equal if they contain the same elements in the same order. Also, two array references are
	 * considered equal if both are <tt>null</tt>.
	 * <p>
	 * 
	 * @param a one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 */
	public static boolean equals(int[] a, int[] a2) {
		return java.util.Arrays.equals(a, a2);
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of shorts are <i>equal</i> to one another.
	 * Two arrays are considered equal if both arrays contain the same number of elements, and all
	 * corresponding pairs of elements in the two arrays are equal. In other words, two arrays are
	 * equal if they contain the same elements in the same order. Also, two array references are
	 * considered equal if both are <tt>null</tt>.
	 * <p>
	 * 
	 * @param a one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 */
	public static boolean equals(short[] a, short a2[]) {
		return java.util.Arrays.equals(a, a2);
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of chars are <i>equal</i> to one another.
	 * Two arrays are considered equal if both arrays contain the same number of elements, and all
	 * corresponding pairs of elements in the two arrays are equal. In other words, two arrays are
	 * equal if they contain the same elements in the same order. Also, two array references are
	 * considered equal if both are <tt>null</tt>.
	 * <p>
	 * 
	 * @param a one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 */
	public static boolean equals(char[] a, char[] a2) {
		return java.util.Arrays.equals(a, a2);
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of bytes are <i>equal</i> to one another.
	 * Two arrays are considered equal if both arrays contain the same number of elements, and all
	 * corresponding pairs of elements in the two arrays are equal. In other words, two arrays are
	 * equal if they contain the same elements in the same order. Also, two array references are
	 * considered equal if both are <tt>null</tt>.
	 * <p>
	 * 
	 * @param a one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 */
	public static boolean equals(byte[] a, byte[] a2) {
		return java.util.Arrays.equals(a, a2);
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of booleans are <i>equal</i> to one
	 * another. Two arrays are considered equal if both arrays contain the same number of elements,
	 * and all corresponding pairs of elements in the two arrays are equal. In other words, two
	 * arrays are equal if they contain the same elements in the same order. Also, two array
	 * references are considered equal if both are <tt>null</tt>.
	 * <p>
	 * 
	 * @param a one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 */
	public static boolean equals(boolean[] a, boolean[] a2) {
		return java.util.Arrays.equals(a, a2);
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of doubles are <i>equal</i> to one another.
	 * Two arrays are considered equal if both arrays contain the same number of elements, and all
	 * corresponding pairs of elements in the two arrays are equal. In other words, two arrays are
	 * equal if they contain the same elements in the same order. Also, two array references are
	 * considered equal if both are <tt>null</tt>.
	 * <p>
	 * Two doubles <tt>d1</tt> and <tt>d2</tt> are considered equal if:
	 * 
	 * <pre>
	 * <tt>new Double(d1).equals(new Double(d2))</tt>
	 * </pre>
	 * 
	 * (Unlike the <tt>==</tt> operator, this method considers <tt>NaN</tt> equals to itself, and
	 * 0.0d unequal to -0.0d.)
	 * 
	 * @param a one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 * @see Double#equals(Object)
	 */
	public static boolean equals(double[] a, double[] a2) {
		return java.util.Arrays.equals(a, a2);
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of floats are <i>equal</i> to one another.
	 * Two arrays are considered equal if both arrays contain the same number of elements, and all
	 * corresponding pairs of elements in the two arrays are equal. In other words, two arrays are
	 * equal if they contain the same elements in the same order. Also, two array references are
	 * considered equal if both are <tt>null</tt>.
	 * <p>
	 * Two floats <tt>f1</tt> and <tt>f2</tt> are considered equal if:
	 * 
	 * <pre>
	 * <tt>new Float(f1).equals(new Float(f2))</tt>
	 * </pre>
	 * 
	 * (Unlike the <tt>==</tt> operator, this method considers <tt>NaN</tt> equals to itself, and
	 * 0.0f unequal to -0.0f.)
	 * 
	 * @param a one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 * @see Float#equals(Object)
	 */
	public static boolean equals(float[] a, float[] a2) {
		return java.util.Arrays.equals(a, a2);
	}

	/**
	 * Returns <tt>true</tt> if the two specified arrays of Objects are <i>equal</i> to one another.
	 * The two arrays are considered equal if both arrays contain the same number of elements, and
	 * all corresponding pairs of elements in the two arrays are equal. Two objects <tt>e1</tt> and
	 * <tt>e2</tt> are considered <i>equal</i> if <tt>(e1==null ? e2==null
	 * : e1.equals(e2))</tt>. In other words, the two arrays are equal if they contain the same
	 * elements in the same order. Also, two array references are considered equal if both are
	 * <tt>null</tt>.
	 * <p>
	 * 
	 * @param a one array to be tested for equality
	 * @param a2 the other array to be tested for equality
	 * @return <tt>true</tt> if the two arrays are equal
	 */
	public static boolean equals(Object[] a, Object[] a2) {
		return java.util.Arrays.equals(a, a2);
	}

	/**
	 * <p>
	 * Compares two arrays, using equals(), handling multi-dimensional arrays correctly.
	 * </p>
	 * <p>
	 * Multi-dimensional primitive arrays are also handled correctly by this method.
	 * </p>
	 * 
	 * @param a the left hand array to compare, may be {@code null}
	 * @param a2 the right hand array to compare, may be {@code null}
	 * @return {@code true} if the arrays are equal
	 */
	public static boolean equals(Object a, Object a2) {
		return new EqualsBuilder().append(a, a2).isEquals();
	}

	// Filling

	/**
	 * Assigns the specified long value to each element of the specified array of longs.
	 * 
	 * @param a the array to be filled
	 * @param val the value to be stored in all elements of the array
	 */
	public static void fill(long[] a, long val) {
		java.util.Arrays.fill(a, val);
	}

	/**
	 * Assigns the specified long value to each element of the specified range of the specified
	 * array of longs. The range to be filled extends from index <tt>fromIndex</tt>, inclusive, to
	 * index <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be filled is
	 * empty.)
	 * 
	 * @param a the array to be filled
	 * @param fromIndex the index of the first element (inclusive) to be filled with the specified
	 *            value
	 * @param toIndex the index of the last element (exclusive) to be filled with the specified
	 *            value
	 * @param val the value to be stored in all elements of the array
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void fill(long[] a, int fromIndex, int toIndex, long val) {
		java.util.Arrays.fill(a, fromIndex, toIndex, val);
	}

	/**
	 * Assigns the specified int value to each element of the specified array of ints.
	 * 
	 * @param a the array to be filled
	 * @param val the value to be stored in all elements of the array
	 */
	public static void fill(int[] a, int val) {
		java.util.Arrays.fill(a, val);
	}

	/**
	 * Assigns the specified int value to each element of the specified range of the specified array
	 * of ints. The range to be filled extends from index <tt>fromIndex</tt>, inclusive, to index
	 * <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be filled is
	 * empty.)
	 * 
	 * @param a the array to be filled
	 * @param fromIndex the index of the first element (inclusive) to be filled with the specified
	 *            value
	 * @param toIndex the index of the last element (exclusive) to be filled with the specified
	 *            value
	 * @param val the value to be stored in all elements of the array
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void fill(int[] a, int fromIndex, int toIndex, int val) {
		java.util.Arrays.fill(a, fromIndex, toIndex, val);
	}

	/**
	 * Assigns the specified short value to each element of the specified array of shorts.
	 * 
	 * @param a the array to be filled
	 * @param val the value to be stored in all elements of the array
	 */
	public static void fill(short[] a, short val) {
		java.util.Arrays.fill(a, val);
	}

	/**
	 * Assigns the specified short value to each element of the specified range of the specified
	 * array of shorts. The range to be filled extends from index <tt>fromIndex</tt>, inclusive, to
	 * index <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be filled is
	 * empty.)
	 * 
	 * @param a the array to be filled
	 * @param fromIndex the index of the first element (inclusive) to be filled with the specified
	 *            value
	 * @param toIndex the index of the last element (exclusive) to be filled with the specified
	 *            value
	 * @param val the value to be stored in all elements of the array
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void fill(short[] a, int fromIndex, int toIndex, short val) {
		java.util.Arrays.fill(a, fromIndex, toIndex, val);
	}

	/**
	 * Assigns the specified char value to each element of the specified array of chars.
	 * 
	 * @param a the array to be filled
	 * @param val the value to be stored in all elements of the array
	 */
	public static void fill(char[] a, char val) {
		java.util.Arrays.fill(a, val);
	}

	/**
	 * Assigns the specified char value to each element of the specified range of the specified
	 * array of chars. The range to be filled extends from index <tt>fromIndex</tt>, inclusive, to
	 * index <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be filled is
	 * empty.)
	 * 
	 * @param a the array to be filled
	 * @param fromIndex the index of the first element (inclusive) to be filled with the specified
	 *            value
	 * @param toIndex the index of the last element (exclusive) to be filled with the specified
	 *            value
	 * @param val the value to be stored in all elements of the array
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void fill(char[] a, int fromIndex, int toIndex, char val) {
		java.util.Arrays.fill(a, fromIndex, toIndex, val);
	}

	/**
	 * Assigns the specified byte value to each element of the specified array of bytes.
	 * 
	 * @param a the array to be filled
	 * @param val the value to be stored in all elements of the array
	 */
	public static void fill(byte[] a, byte val) {
		java.util.Arrays.fill(a, val);
	}

	/**
	 * Assigns the specified byte value to each element of the specified range of the specified
	 * array of bytes. The range to be filled extends from index <tt>fromIndex</tt>, inclusive, to
	 * index <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be filled is
	 * empty.)
	 * 
	 * @param a the array to be filled
	 * @param fromIndex the index of the first element (inclusive) to be filled with the specified
	 *            value
	 * @param toIndex the index of the last element (exclusive) to be filled with the specified
	 *            value
	 * @param val the value to be stored in all elements of the array
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void fill(byte[] a, int fromIndex, int toIndex, byte val) {
		java.util.Arrays.fill(a, fromIndex, toIndex, val);
	}

	/**
	 * Assigns the specified boolean value to each element of the specified array of booleans.
	 * 
	 * @param a the array to be filled
	 * @param val the value to be stored in all elements of the array
	 */
	public static void fill(boolean[] a, boolean val) {
		java.util.Arrays.fill(a, val);
	}

	/**
	 * Assigns the specified boolean value to each element of the specified range of the specified
	 * array of booleans. The range to be filled extends from index <tt>fromIndex</tt>, inclusive,
	 * to index <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be filled
	 * is empty.)
	 * 
	 * @param a the array to be filled
	 * @param fromIndex the index of the first element (inclusive) to be filled with the specified
	 *            value
	 * @param toIndex the index of the last element (exclusive) to be filled with the specified
	 *            value
	 * @param val the value to be stored in all elements of the array
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void fill(boolean[] a, int fromIndex, int toIndex, boolean val) {
		java.util.Arrays.fill(a, fromIndex, toIndex, val);
	}

	/**
	 * Assigns the specified double value to each element of the specified array of doubles.
	 * 
	 * @param a the array to be filled
	 * @param val the value to be stored in all elements of the array
	 */
	public static void fill(double[] a, double val) {
		java.util.Arrays.fill(a, val);
	}

	/**
	 * Assigns the specified double value to each element of the specified range of the specified
	 * array of doubles. The range to be filled extends from index <tt>fromIndex</tt>, inclusive, to
	 * index <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be filled is
	 * empty.)
	 * 
	 * @param a the array to be filled
	 * @param fromIndex the index of the first element (inclusive) to be filled with the specified
	 *            value
	 * @param toIndex the index of the last element (exclusive) to be filled with the specified
	 *            value
	 * @param val the value to be stored in all elements of the array
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void fill(double[] a, int fromIndex, int toIndex, double val) {
		java.util.Arrays.fill(a, fromIndex, toIndex, val);
	}

	/**
	 * Assigns the specified float value to each element of the specified array of floats.
	 * 
	 * @param a the array to be filled
	 * @param val the value to be stored in all elements of the array
	 */
	public static void fill(float[] a, float val) {
		fill(a, 0, a.length, val);
	}

	/**
	 * Assigns the specified float value to each element of the specified range of the specified
	 * array of floats. The range to be filled extends from index <tt>fromIndex</tt>, inclusive, to
	 * index <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range to be filled is
	 * empty.)
	 * 
	 * @param a the array to be filled
	 * @param fromIndex the index of the first element (inclusive) to be filled with the specified
	 *            value
	 * @param toIndex the index of the last element (exclusive) to be filled with the specified
	 *            value
	 * @param val the value to be stored in all elements of the array
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 */
	public static void fill(float[] a, int fromIndex, int toIndex, float val) {
		java.util.Arrays.fill(a, fromIndex, toIndex, val);
	}

	/**
	 * Assigns the specified Object reference to each element of the specified array of Objects.
	 * 
	 * @param a the array to be filled
	 * @param val the value to be stored in all elements of the array
	 * @throws ArrayStoreException if the specified value is not of a runtime type that can be
	 *             stored in the specified array
	 */
	public static void fill(Object[] a, Object val) {
		fill(a, 0, a.length, val);
	}

	/**
	 * Assigns the specified Object reference to each element of the specified range of the
	 * specified array of Objects. The range to be filled extends from index <tt>fromIndex</tt>,
	 * inclusive, to index <tt>toIndex</tt>, exclusive. (If <tt>fromIndex==toIndex</tt>, the range
	 * to be filled is empty.)
	 * 
	 * @param a the array to be filled
	 * @param fromIndex the index of the first element (inclusive) to be filled with the specified
	 *            value
	 * @param toIndex the index of the last element (exclusive) to be filled with the specified
	 *            value
	 * @param val the value to be stored in all elements of the array
	 * @throws IllegalArgumentException if <tt>fromIndex &gt; toIndex</tt>
	 * @throws ArrayIndexOutOfBoundsException if <tt>fromIndex &lt; 0</tt> or
	 *             <tt>toIndex &gt; a.length</tt>
	 * @throws ArrayStoreException if the specified value is not of a runtime type that can be
	 *             stored in the specified array
	 */
	public static void fill(Object[] a, int fromIndex, int toIndex, Object val) {
		java.util.Arrays.fill(a, fromIndex, toIndex, val);
	}

	// Cloning
	/**
	 * Copies the specified array, truncating or padding with nulls (if necessary) so the copy has
	 * the specified length. For all indices that are valid in both the original array and the copy,
	 * the two arrays will contain identical values. For any indices that are valid in the copy but
	 * not the original, the copy will contain <tt>null</tt>. Such indices will exist if and only if
	 * the specified length is greater than that of the original array. The resulting array is of
	 * exactly the same class as the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with nulls to obtain the specified
	 *         length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static <T> T[] copyOf(T[] original, int newLength) {
		return java.util.Arrays.copyOf(original, newLength);
	}

	/**
	 * Copies the specified array, truncating or padding with nulls (if necessary) so the copy has
	 * the specified length. For all indices that are valid in both the original array and the copy,
	 * the two arrays will contain identical values. For any indices that are valid in the copy but
	 * not the original, the copy will contain <tt>null</tt>. Such indices will exist if and only if
	 * the specified length is greater than that of the original array. The resulting array is of
	 * the class <tt>newType</tt>.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @param newType the class of the copy to be returned
	 * @return a copy of the original array, truncated or padded with nulls to obtain the specified
	 *         length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 * @throws ArrayStoreException if an element copied from <tt>original</tt> is not of a runtime
	 *             type that can be stored in an array of class <tt>newType</tt>
	 */
	public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
		return java.util.Arrays.copyOf(original, newLength, newType);
	}

	/**
	 * Copies the specified array, truncating or padding with zeros (if necessary) so the copy has
	 * the specified length. For all indices that are valid in both the original array and the copy,
	 * the two arrays will contain identical values. For any indices that are valid in the copy but
	 * not the original, the copy will contain <tt>(byte)0</tt>. Such indices will exist if and only
	 * if the specified length is greater than that of the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with zeros to obtain the specified
	 *         length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static byte[] copyOf(byte[] original, int newLength) {
		return java.util.Arrays.copyOf(original, newLength);
	}

	/**
	 * Copies the specified array, truncating or padding with zeros (if necessary) so the copy has
	 * the specified length. For all indices that are valid in both the original array and the copy,
	 * the two arrays will contain identical values. For any indices that are valid in the copy but
	 * not the original, the copy will contain <tt>(short)0</tt>. Such indices will exist if and
	 * only if the specified length is greater than that of the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with zeros to obtain the specified
	 *         length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static short[] copyOf(short[] original, int newLength) {
		return java.util.Arrays.copyOf(original, newLength);
	}

	/**
	 * Copies the specified array, truncating or padding with zeros (if necessary) so the copy has
	 * the specified length. For all indices that are valid in both the original array and the copy,
	 * the two arrays will contain identical values. For any indices that are valid in the copy but
	 * not the original, the copy will contain <tt>0</tt>. Such indices will exist if and only if
	 * the specified length is greater than that of the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with zeros to obtain the specified
	 *         length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static int[] copyOf(int[] original, int newLength) {
		return java.util.Arrays.copyOf(original, newLength);
	}

	/**
	 * Copies the specified array, truncating or padding with zeros (if necessary) so the copy has
	 * the specified length. For all indices that are valid in both the original array and the copy,
	 * the two arrays will contain identical values. For any indices that are valid in the copy but
	 * not the original, the copy will contain <tt>0L</tt>. Such indices will exist if and only if
	 * the specified length is greater than that of the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with zeros to obtain the specified
	 *         length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static long[] copyOf(long[] original, int newLength) {
		return java.util.Arrays.copyOf(original, newLength);
	}

	/**
	 * Copies the specified array, truncating or padding with null characters (if necessary) so the
	 * copy has the specified length. For all indices that are valid in both the original array and
	 * the copy, the two arrays will contain identical values. For any indices that are valid in the
	 * copy but not the original, the copy will contain <tt>'\\u000'</tt>. Such indices will exist
	 * if and only if the specified length is greater than that of the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with null characters to obtain the
	 *         specified length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static char[] copyOf(char[] original, int newLength) {
		return java.util.Arrays.copyOf(original, newLength);
	}

	/**
	 * Copies the specified array, truncating or padding with zeros (if necessary) so the copy has
	 * the specified length. For all indices that are valid in both the original array and the copy,
	 * the two arrays will contain identical values. For any indices that are valid in the copy but
	 * not the original, the copy will contain <tt>0f</tt>. Such indices will exist if and only if
	 * the specified length is greater than that of the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with zeros to obtain the specified
	 *         length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static float[] copyOf(float[] original, int newLength) {
		return java.util.Arrays.copyOf(original, newLength);
	}

	/**
	 * Copies the specified array, truncating or padding with zeros (if necessary) so the copy has
	 * the specified length. For all indices that are valid in both the original array and the copy,
	 * the two arrays will contain identical values. For any indices that are valid in the copy but
	 * not the original, the copy will contain <tt>0d</tt>. Such indices will exist if and only if
	 * the specified length is greater than that of the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with zeros to obtain the specified
	 *         length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static double[] copyOf(double[] original, int newLength) {
		return java.util.Arrays.copyOf(original, newLength);
	}

	/**
	 * Copies the specified array, truncating or padding with <tt>false</tt> (if necessary) so the
	 * copy has the specified length. For all indices that are valid in both the original array and
	 * the copy, the two arrays will contain identical values. For any indices that are valid in the
	 * copy but not the original, the copy will contain <tt>false</tt>. Such indices will exist if
	 * and only if the specified length is greater than that of the original array.
	 * 
	 * @param original the array to be copied
	 * @param newLength the length of the copy to be returned
	 * @return a copy of the original array, truncated or padded with false elements to obtain the
	 *         specified length
	 * @throws NegativeArraySizeException if <tt>newLength</tt> is negative
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static boolean[] copyOf(boolean[] original, int newLength) {
		return java.util.Arrays.copyOf(original, newLength);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>null</tt> is placed in all elements of the
	 * copy whose index is greater than or equal to <tt>original.length - from</tt>. The length of
	 * the returned array will be <tt>to - from</tt>.
	 * <p>
	 * The resulting array is of exactly the same class as the original array.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with nulls to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static <T> T[] copyOfRange(T[] original, int from, int to) {
		return java.util.Arrays.copyOfRange(original, from, to);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>null</tt> is placed in all elements of the
	 * copy whose index is greater than or equal to <tt>original.length - from</tt>. The length of
	 * the returned array will be <tt>to - from</tt>. The resulting array is of the class
	 * <tt>newType</tt>.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @param newType the class of the copy to be returned
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with nulls to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 * @throws ArrayStoreException if an element copied from <tt>original</tt> is not of a runtime
	 *             type that can be stored in an array of class <tt>newType</tt>.
	 */
	public static <T, U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType) {
		return java.util.Arrays.copyOfRange(original, from, to, newType);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>(byte)0</tt> is placed in all elements of
	 * the copy whose index is greater than or equal to <tt>original.length - from</tt>. The length
	 * of the returned array will be <tt>to - from</tt>.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with zeros to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static byte[] copyOfRange(byte[] original, int from, int to) {
		return java.util.Arrays.copyOfRange(original, from, to);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>(short)0</tt> is placed in all elements of
	 * the copy whose index is greater than or equal to <tt>original.length - from</tt>. The length
	 * of the returned array will be <tt>to - from</tt>.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with zeros to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static short[] copyOfRange(short[] original, int from, int to) {
		return java.util.Arrays.copyOfRange(original, from, to);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>0</tt> is placed in all elements of the copy
	 * whose index is greater than or equal to <tt>original.length - from</tt>. The length of the
	 * returned array will be <tt>to - from</tt>.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with zeros to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static int[] copyOfRange(int[] original, int from, int to) {
		return java.util.Arrays.copyOfRange(original, from, to);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>0L</tt> is placed in all elements of the
	 * copy whose index is greater than or equal to <tt>original.length - from</tt>. The length of
	 * the returned array will be <tt>to - from</tt>.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with zeros to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static long[] copyOfRange(long[] original, int from, int to) {
		return java.util.Arrays.copyOfRange(original, from, to);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>'\\u000'</tt> is placed in all elements of
	 * the copy whose index is greater than or equal to <tt>original.length - from</tt>. The length
	 * of the returned array will be <tt>to - from</tt>.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with null characters to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static char[] copyOfRange(char[] original, int from, int to) {
		return java.util.Arrays.copyOfRange(original, from, to);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>0f</tt> is placed in all elements of the
	 * copy whose index is greater than or equal to <tt>original.length - from</tt>. The length of
	 * the returned array will be <tt>to - from</tt>.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with zeros to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static float[] copyOfRange(float[] original, int from, int to) {
		return java.util.Arrays.copyOfRange(original, from, to);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>0d</tt> is placed in all elements of the
	 * copy whose index is greater than or equal to <tt>original.length - from</tt>. The length of
	 * the returned array will be <tt>to - from</tt>.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with zeros to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static double[] copyOfRange(double[] original, int from, int to) {
		return java.util.Arrays.copyOfRange(original, from, to);
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the
	 * range (<tt>from</tt>) must lie between zero and <tt>original.length</tt>, inclusive. The
	 * value at <tt>original[from]</tt> is placed into the initial element of the copy (unless
	 * <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in
	 * the original array are placed into subsequent elements in the copy. The final index of the
	 * range (<tt>to</tt>), which must be greater than or equal to <tt>from</tt>, may be greater
	 * than <tt>original.length</tt>, in which case <tt>false</tt> is placed in all elements of the
	 * copy whose index is greater than or equal to <tt>original.length - from</tt>. The length of
	 * the returned array will be <tt>to - from</tt>.
	 * 
	 * @param original the array from which a range is to be copied
	 * @param from the initial index of the range to be copied, inclusive
	 * @param to the final index of the range to be copied, exclusive. (This index may lie outside
	 *            the array.)
	 * @return a new array containing the specified range from the original array, truncated or
	 *         padded with false elements to obtain the required length
	 * @throws ArrayIndexOutOfBoundsException if <tt>from &lt; 0</tt> or
	 *             <tt>from &gt; original.length()</tt>
	 * @throws IllegalArgumentException if <tt>from &gt; to</tt>
	 * @throws NullPointerException if <tt>original</tt> is null
	 */
	public static boolean[] copyOfRange(boolean[] original, int from, int to) {
		return java.util.Arrays.copyOfRange(original, from, to);
	}

	// Misc

	/**
	 * Returns a fixed-size list backed by the specified array. (Changes to the returned list
	 * "write through" to the array.) This method acts as bridge between array-based and
	 * collection-based APIs, in combination with {@link Collection#toArray}. The returned list is
	 * serializable and implements {@link RandomAccess}.
	 * <p>
	 * This method also provides a convenient way to create a fixed-size list initialized to contain
	 * several elements:
	 * 
	 * <pre>
	 * List&lt;String&gt; stooges = java.util.Arrays.asList(&quot;Larry&quot;, &quot;Moe&quot;, &quot;Curly&quot;);
	 * </pre>
	 * 
	 * @param a the array by which the list will be backed
	 * @return a list view of the specified array
	 */
	public static <T> List<T> asList(T... a) {
		if (a == null) {
			return null;
		}
		return java.util.Arrays.asList(a);
	}

	/**
	 * Returns a new list contains all elements of the specified array.
	 * 
	 * @param a the array
	 * @return a list
	 */
	public static <T> List<T> toList(T... a) {
		if (a == null) {
			return null;
		}

		List<T> list = new ArrayList<T>(a.length);
		for (T e : a) {
			list.add(e);
		}
		return list;
	}

	/**
	 * Returns a new set contains all elements of the specified array.
	 * 
	 * @param a the array
	 * @return a set
	 */
	public static <T> Set<T> toSet(T... a) {
		if (a == null) {
			return null;
		}

		Set<T> set = new HashSet<T>(a.length);
		for (T e : a) {
			set.add(e);
		}
		return set;
	}

	/**
	 * Returns a new map contains all elements of the specified array.
	 * Map m = Arrays.toMap("key", "value");
	 * 
	 * @param a the array
	 * @return a map
	 */
	public static <T> Map<T, T> toMap(T... a) {
		if (a == null) {
			return null;
		}

		if (a.length % 2 != 0) {
			throw new IllegalArgumentException("The length is incorrect: " + a.length);
		}

		Map<T, T> map = new HashMap<T, T>(a.length / 2);
		for (int i = 0; i < a.length; i += 2) {
			map.put(a[i], a[i + 1]);
		}
		return map;
	}

	/**
	 * Returns a new map contains all elements of the specified array.
	 * 
	 * @param a the array
	 * @return a set
	 */
	public static <T> LinkedHashMap<T, T> toLinkedMap(T... a) {
		if (a == null) {
			return null;
		}

		if (a.length % 2 != 0) {
			throw new IllegalArgumentException("The length is incorrect: " + a.length);
		}
		
		LinkedHashMap<T, T> map = new LinkedHashMap<T, T>(a.length / 2);
		for (int i = 0; i < a.length; i += 2) {
			map.put(a[i], a[i + 1]);
		}
		return map;
	}

	/**
	 * <p>
	 * Outputs an array as a String, treating {@code null} as an empty array.
	 * </p>
	 * <p>
	 * Multi-dimensional arrays are handled correctly, including multi-dimensional primitive arrays.
	 * </p>
	 * <p>
	 * The format is that of Java source code, for example <code>{a,b}</code>.
	 * </p>
	 * 
	 * @param array the array to get a toString for, may be {@code null}
	 * @return a String representation of the array, '[]' if null array input
	 */
	public static String toString(Object array) {
		return toString(array, "null");
	}

	/**
	 * <p>
	 * Outputs an array as a String handling {@code null}s.
	 * </p>
	 * <p>
	 * Multi-dimensional arrays are handled correctly, including multi-dimensional primitive arrays.
	 * </p>
	 * <p>
	 * The format is that of Java source code, for example <code>{a,b}</code>.
	 * </p>
	 * 
	 * @param array the array to get a toString for, may be {@code null}
	 * @param stringIfNull the String to return if the array is {@code null}
	 * @return a String representation of the array
	 */
	public static String toString(Object array, String stringIfNull) {
		if (array == null) {
			return stringIfNull;
		}
		return new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE).append(array).toString();
	}
}
