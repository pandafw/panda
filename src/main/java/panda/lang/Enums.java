package panda.lang;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Utility library to provide helper methods for Java enums.
 * </p>
 * <p>
 * #ThreadSafe#
 * </p>
 */
public class Enums {

	private static final String NULL_ELEMENTS_NOT_PERMITTED = "null elements not permitted";
	private static final String CANNOT_STORE_S_S_VALUES_IN_S_BITS = "Cannot store %s %s values in %s bits";
	private static final String S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE = "%s does not seem to be an Enum type";
	private static final String ENUM_CLASS_MUST_BE_DEFINED = "EnumClass must be defined.";

	/**
	 * This constructor is public to permit tools that require a JavaBean instance to operate.
	 */
	public Enums() {
	}

	/**
	 * <p>
	 * Gets the {@code Map} of enums by name.
	 * </p>
	 * <p>
	 * This method is useful when you need a map of enums by name.
	 * </p>
	 * 
	 * @param <E> the type of the enumeration
	 * @param enumClass the class of the enum to query, not null
	 * @return the modifiable map of enum names to enums, never null
	 */
	public static <E extends Enum<E>> Map<String, E> getEnumMap(final Class<E> enumClass) {
		final Map<String, E> map = new LinkedHashMap<String, E>();
		for (final E e : enumClass.getEnumConstants()) {
			map.put(e.name(), e);
		}
		return map;
	}

	/**
	 * <p>
	 * Gets the {@code List} of enums.
	 * </p>
	 * <p>
	 * This method is useful when you need a list of enums rather than an array.
	 * </p>
	 * 
	 * @param <E> the type of the enumeration
	 * @param enumClass the class of the enum to query, not null
	 * @return the modifiable list of enums, never null
	 */
	public static <E extends Enum<E>> List<E> getEnumList(final Class<E> enumClass) {
		return new ArrayList<E>(Arrays.asList(enumClass.getEnumConstants()));
	}

	/**
	 * <p>
	 * Checks if the specified name is a valid enum for the class.
	 * </p>
	 * <p>
	 * This method differs from {@link Enum#valueOf} in that checks if the name is a valid enum
	 * without needing to catch the exception.
	 * </p>
	 * 
	 * @param <E> the type of the enumeration
	 * @param enumClass the class of the enum to query, not null
	 * @param enumName the enum name, null returns false
	 * @return true if the enum name is valid, otherwise false
	 */
	public static <E extends Enum<E>> boolean isValidEnum(final Class<E> enumClass, final String enumName) {
		if (enumName == null) {
			return false;
		}
		try {
			Enum.valueOf(enumClass, enumName);
			return true;
		}
		catch (final IllegalArgumentException ex) {
			return false;
		}
	}

	/**
	 * <p>
	 * Gets the enum for the class, returning {@code null} if not found.
	 * </p>
	 * <p>
	 * This method differs from {@link Enum#valueOf} in that it does not throw an exception for an
	 * invalid enum name.
	 * </p>
	 * 
	 * @param <E> the type of the enumeration
	 * @param enumClass the class of the enum to query, not null
	 * @param enumName the enum name, null returns null
	 * @return the enum, null if not found
	 */
	public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName) {
		if (enumName == null) {
			return null;
		}
		try {
			return Enum.valueOf(enumClass, enumName);
		}
		catch (final IllegalArgumentException ex) {
			return null;
		}
	}

	/**
	 * <p>
	 * Creates a long bit vector representation of the given subset of an Enum.
	 * </p>
	 * <p>
	 * This generates a value that is usable by {@link Enums#processBitVector}.
	 * </p>
	 * <p>
	 * Do not use this method if you have more than 64 values in your Enum, as this would create a
	 * value greater than a long can hold.
	 * </p>
	 * 
	 * @param enumClass the class of the enum we are working with, not {@code null}
	 * @param values the values we want to convert, not {@code null}, neither containing
	 *            {@code null}
	 * @param <E> the type of the enumeration
	 * @return a long whose value provides a binary representation of the given set of enum values.
	 * @throws NullPointerException if {@code enumClass} or {@code values} is {@code null}
	 * @throws IllegalArgumentException if {@code enumClass} is not an enum class or has more than
	 *             64 values, or if any {@code values} {@code null}
	 * @see #generateBitVectors(Class, Iterable)
	 */
	public static <E extends Enum<E>> long generateBitVector(final Class<E> enumClass,
			final Iterable<? extends E> values) {
		checkBitVectorable(enumClass);
		Asserts.notNull(values);
		long total = 0;
		for (final E constant : values) {
			Asserts.isTrue(constant != null, NULL_ELEMENTS_NOT_PERMITTED);
			total |= 1 << constant.ordinal();
		}
		return total;
	}

	/**
	 * <p>
	 * Creates a bit vector representation of the given subset of an Enum using as many {@code long}
	 * s as needed.
	 * </p>
	 * <p>
	 * This generates a value that is usable by {@link Enums#processBitVectors}.
	 * </p>
	 * <p>
	 * Use this method if you have more than 64 values in your Enum.
	 * </p>
	 * 
	 * @param enumClass the class of the enum we are working with, not {@code null}
	 * @param values the values we want to convert, not {@code null}, neither containing
	 *            {@code null}
	 * @param <E> the type of the enumeration
	 * @return a long[] whose values provide a binary representation of the given set of enum values
	 *         with least significant digits rightmost.
	 * @throws NullPointerException if {@code enumClass} or {@code values} is {@code null}
	 * @throws IllegalArgumentException if {@code enumClass} is not an enum class, or if any
	 *             {@code values} {@code null}
	 */
	public static <E extends Enum<E>> long[] generateBitVectors(final Class<E> enumClass,
			final Iterable<? extends E> values) {
		asEnum(enumClass);
		Asserts.notNull(values);
		final EnumSet<E> condensed = EnumSet.noneOf(enumClass);
		for (final E constant : values) {
			Asserts.isTrue(constant != null, NULL_ELEMENTS_NOT_PERMITTED);
			condensed.add(constant);
		}
		final long[] result = new long[(enumClass.getEnumConstants().length - 1) / Long.SIZE + 1];
		for (final E value : condensed) {
			result[value.ordinal() / Long.SIZE] |= 1 << (value.ordinal() % Long.SIZE);
		}
		Arrays.reverse(result);
		return result;
	}

	/**
	 * <p>
	 * Creates a long bit vector representation of the given array of Enum values.
	 * </p>
	 * <p>
	 * This generates a value that is usable by {@link Enums#processBitVector}.
	 * </p>
	 * <p>
	 * Do not use this method if you have more than 64 values in your Enum, as this would create a
	 * value greater than a long can hold.
	 * </p>
	 * 
	 * @param enumClass the class of the enum we are working with, not {@code null}
	 * @param values the values we want to convert, not {@code null}
	 * @param <E> the type of the enumeration
	 * @return a long whose value provides a binary representation of the given set of enum values.
	 * @throws NullPointerException if {@code enumClass} or {@code values} is {@code null}
	 * @throws IllegalArgumentException if {@code enumClass} is not an enum class or has more than
	 *             64 values
	 * @see #generateBitVectors(Class, Iterable)
	 */
	public static <E extends Enum<E>> long generateBitVector(final Class<E> enumClass, final E... values) {
		Asserts.noNullElements(values);
		return generateBitVector(enumClass, Arrays.<E> asList(values));
	}

	/**
	 * <p>
	 * Creates a bit vector representation of the given subset of an Enum using as many {@code long}
	 * s as needed.
	 * </p>
	 * <p>
	 * This generates a value that is usable by {@link Enums#processBitVectors}.
	 * </p>
	 * <p>
	 * Use this method if you have more than 64 values in your Enum.
	 * </p>
	 * 
	 * @param enumClass the class of the enum we are working with, not {@code null}
	 * @param values the values we want to convert, not {@code null}, neither containing
	 *            {@code null}
	 * @param <E> the type of the enumeration
	 * @return a long[] whose values provide a binary representation of the given set of enum values
	 *         with least significant digits rightmost.
	 * @throws NullPointerException if {@code enumClass} or {@code values} is {@code null}
	 * @throws IllegalArgumentException if {@code enumClass} is not an enum class, or if any
	 *             {@code values} {@code null}
	 */
	public static <E extends Enum<E>> long[] generateBitVectors(final Class<E> enumClass, final E... values) {
		asEnum(enumClass);
		Asserts.noNullElements(values);
		final EnumSet<E> condensed = EnumSet.noneOf(enumClass);
		Collections.addAll(condensed, values);
		final long[] result = new long[(enumClass.getEnumConstants().length - 1) / Long.SIZE + 1];
		for (final E value : condensed) {
			result[value.ordinal() / Long.SIZE] |= 1 << (value.ordinal() % Long.SIZE);
		}
		Arrays.reverse(result);
		return result;
	}

	/**
	 * <p>
	 * Convert a long value created by {@link Enums#generateBitVector} into the set of enum values
	 * that it represents.
	 * </p>
	 * <p>
	 * If you store this value, beware any changes to the enum that would affect ordinal values.
	 * </p>
	 * 
	 * @param enumClass the class of the enum we are working with, not {@code null}
	 * @param value the long value representation of a set of enum values
	 * @param <E> the type of the enumeration
	 * @return a set of enum values
	 * @throws NullPointerException if {@code enumClass} is {@code null}
	 * @throws IllegalArgumentException if {@code enumClass} is not an enum class or has more than
	 *             64 values
	 */
	public static <E extends Enum<E>> EnumSet<E> processBitVector(final Class<E> enumClass, final long value) {
		checkBitVectorable(enumClass).getEnumConstants();
		return processBitVectors(enumClass, value);
	}

	/**
	 * <p>
	 * Convert a {@code long[]} created by {@link Enums#generateBitVectors} into the set of enum
	 * values that it represents.
	 * </p>
	 * <p>
	 * If you store this value, beware any changes to the enum that would affect ordinal values.
	 * </p>
	 * 
	 * @param enumClass the class of the enum we are working with, not {@code null}
	 * @param values the long[] bearing the representation of a set of enum values, least
	 *            significant digits rightmost, not {@code null}
	 * @param <E> the type of the enumeration
	 * @return a set of enum values
	 * @throws NullPointerException if {@code enumClass} is {@code null}
	 * @throws IllegalArgumentException if {@code enumClass} is not an enum class
	 */
	public static <E extends Enum<E>> EnumSet<E> processBitVectors(final Class<E> enumClass, final long... values) {
		final EnumSet<E> results = EnumSet.noneOf(asEnum(enumClass));
		long[] lvalues = Arrays.clone(Asserts.notNull(values));
		Arrays.reverse(lvalues);
		for (final E constant : enumClass.getEnumConstants()) {
			final int block = constant.ordinal() / Long.SIZE;
			if (block < lvalues.length && (lvalues[block] & 1 << (constant.ordinal() % Long.SIZE)) != 0) {
				results.add(constant);
			}
		}
		return results;
	}

	/**
	 * Validate that {@code enumClass} is compatible with representation in a {@code long}.
	 * 
	 * @param <E> the type of the enumeration
	 * @param enumClass to check
	 * @return {@code enumClass}
	 * @throws NullPointerException if {@code enumClass} is {@code null}
	 * @throws IllegalArgumentException if {@code enumClass} is not an enum class or has more than
	 *             64 values
	 */
	private static <E extends Enum<E>> Class<E> checkBitVectorable(final Class<E> enumClass) {
		final E[] constants = asEnum(enumClass).getEnumConstants();
		Asserts.isTrue(constants.length <= Long.SIZE, CANNOT_STORE_S_S_VALUES_IN_S_BITS,
			Integer.valueOf(constants.length), enumClass.getSimpleName(), Integer.valueOf(Long.SIZE));

		return enumClass;
	}

	/**
	 * Validate {@code enumClass}.
	 * 
	 * @param <E> the type of the enumeration
	 * @param enumClass to check
	 * @return {@code enumClass}
	 * @throws NullPointerException if {@code enumClass} is {@code null}
	 * @throws IllegalArgumentException if {@code enumClass} is not an enum class
	 */
	private static <E extends Enum<E>> Class<E> asEnum(final Class<E> enumClass) {
		Asserts.notNull(enumClass, ENUM_CLASS_MUST_BE_DEFINED);
		Asserts.isTrue(enumClass.isEnum(), S_DOES_NOT_SEEM_TO_BE_AN_ENUM_TYPE, enumClass);
		return enumClass;
	}
}
