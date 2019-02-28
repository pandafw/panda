package panda.lang;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class for Number.
 */
public class Numbers {
	/** 1KB */
	public static final int KB = 1024;
	/** 1KB BigInteger */
	public static final BigInteger BI_KB = BigInteger.valueOf(KB);
	/** 1KB BigDecimal */
	public static final BigDecimal BD_KB = BigDecimal.valueOf(KB);
	/** 1MB */
	public static final int MB = KB * KB;
	/** 1MB BigInteger */
	public static final BigInteger BI_MB = BigInteger.valueOf(MB);
	/** 1MB BigDecimal */
	public static final BigDecimal BD_MB = BigDecimal.valueOf(MB);
	/** 1GB */
	public static final long GB = MB * 1024L;
	/** 1GB BigInteger */
	public static final BigInteger BI_GB = BigInteger.valueOf(GB);
	/** 1GB BigDecimal */
	public static final BigDecimal BD_GB = BigDecimal.valueOf(GB);
	/** 1TB */
	public static final long TB = GB * KB;
	/** 1TB BigInteger */
	public static final BigInteger BI_TB = BigInteger.valueOf(TB);
	/** 1TB BigDecimal */
	public static final BigDecimal BD_TB = BigDecimal.valueOf(TB);
	/** 1PB */
	public static final long PB = TB * KB;
	/** 1PB BigInteger */
	public static final BigInteger BI_PB = BigInteger.valueOf(PB);
	/** 1PB BigDecimal */
	public static final BigDecimal BD_PB = BigDecimal.valueOf(PB);
	/** 1EB */
	public static final long EB = PB * KB;
	/** 1EB BigInteger */
	public static final BigInteger BI_EB = BigInteger.valueOf(EB);
	/** 1EB BigDecimal */
	public static final BigDecimal BD_EB = BigDecimal.valueOf(EB);
	/** 1ZB BigInteger */
	public static final BigInteger BI_ZB = BigInteger.valueOf(KB).multiply(BI_EB);
	/** 1ZB BigDecimal */
	public static final BigDecimal BD_ZB = BigDecimal.valueOf(KB).multiply(BD_EB);
	/** 1YB BigInteger */
	public static final BigInteger BI_YB = BI_ZB.multiply(BI_KB);
	/** 1YB BigDecimal */
	public static final BigDecimal BD_YB = BD_ZB.multiply(BD_KB);

    /** Reusable Long constant for zero. */
	public static final Long LONG_ZERO = Long.valueOf(0L);
	/** Reusable Long constant for one. */
	public static final Long LONG_ONE = Long.valueOf(1L);
	/** Reusable Long constant for minus one. */
	public static final Long LONG_MINUS_ONE = Long.valueOf(-1L);
	/** Reusable Integer constant for zero. */
	public static final Integer INTEGER_ZERO = Integer.valueOf(0);
	/** Reusable Integer constant for one. */
	public static final Integer INTEGER_ONE = Integer.valueOf(1);
	/** Reusable Integer constant for minus one. */
	public static final Integer INTEGER_MINUS_ONE = Integer.valueOf(-1);
	/** Reusable Short constant for zero. */
	public static final Short SHORT_ZERO = Short.valueOf((short) 0);
	/** Reusable Short constant for one. */
	public static final Short SHORT_ONE = Short.valueOf((short) 1);
	/** Reusable Short constant for minus one. */
	public static final Short SHORT_MINUS_ONE = Short.valueOf((short) -1);
	/** Reusable Byte constant for zero. */
	public static final Byte BYTE_ZERO = Byte.valueOf((byte) 0);
	/** Reusable Byte constant for one. */
	public static final Byte BYTE_ONE = Byte.valueOf((byte) 1);
	/** Reusable Byte constant for minus one. */
	public static final Byte BYTE_MINUS_ONE = Byte.valueOf((byte) -1);
	/** Reusable Double constant for zero. */
	public static final Double DOUBLE_ZERO = Double.valueOf(0.0d);
	/** Reusable Double constant for one. */
	public static final Double DOUBLE_ONE = Double.valueOf(1.0d);
	/** Reusable Double constant for minus one. */
	public static final Double DOUBLE_MINUS_ONE = Double.valueOf(-1.0d);
	/** Reusable Float constant for zero. */
	public static final Float FLOAT_ZERO = Float.valueOf(0.0f);
	/** Reusable Float constant for one. */
	public static final Float FLOAT_ONE = Float.valueOf(1.0f);
	/** Reusable Float constant for minus one. */
	public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0f);

	public static DecimalFormat COMMA_FORMAT = new DecimalFormat("#,###");
	
	//----------------------------------------------------------------
	public static String toCommaString(Number n) {
		if (n == null) {
			return null;
		}
		return COMMA_FORMAT.format(n);
	}

	public static String toCommaString(short n) {
		return COMMA_FORMAT.format(n);
	}

	public static String toCommaString(int n) {
		return COMMA_FORMAT.format(n);
	}
	
	public static String toCommaString(long n) {
		return COMMA_FORMAT.format(n);
	}
	
	//----------------------------------------------------------------
	public static byte defaultByte(Number n) {
		return n == null ? 0 : n.byteValue();
	}
	
	public static byte defaultByte(Number n, byte d) {
		return n == null ? d : n.byteValue();
	}
	
	public static short defaultShort(Number n) {
		return n == null ? 0 : n.shortValue();
	}
	
	public static short defaultShort(Number n, short d) {
		return n == null ? d : n.shortValue();
	}
	
	public static int defaultInt(Number n) {
		return n == null ? 0 : n.intValue();
	}
	
	public static int defaultInt(Number n, int d) {
		return n == null ? d : n.intValue();
	}
	
	public static long defaultLong(Number n) {
		return n == null ? 0 : n.longValue();
	}
	
	public static long defaultLong(Number n, long d) {
		return n == null ? d : n.longValue();
	}
	
	public static float defaultFloat(Number n) {
		return n == null ? 0 : n.floatValue();
	}
	
	public static float defaultFloat(Number n, float f) {
		return n == null ? f : n.floatValue();
	}
	
	public static double defaultDouble(Number n) {
		return n == null ? 0 : n.doubleValue();
	}
	
	public static double defaultDouble(Number n, double d) {
		return n == null ? d : n.doubleValue();
	}
	
	//----------------------------------------------------------------
	/**
	 * <p>
	 * Checks if the value can safely be converted to a byte primitive.
	 * </p>
	 * 
	 * @param value The value validation is being performed on.
	 * @return true if the value can be converted to a Byte.
	 */
	public static boolean isByte(String value) {
		return (toByte(value) != null);
	}

	/**
	 * <p>
	 * Checks if the value can safely be converted to a short primitive.
	 * </p>
	 * 
	 * @param value The value validation is being performed on.
	 * @return true if the value can be converted to a Short.
	 */
	public static boolean isShort(String value) {
		return (toShort(value) != null);
	}

	/**
	 * <p>
	 * Checks if the value can safely be converted to a int primitive.
	 * </p>
	 * 
	 * @param value The value validation is being performed on.
	 * @return true if the value can be converted to an Integer.
	 */
	public static boolean isInt(String value) {
		return (toInt(value) != null);
	}

	/**
	 * <p>
	 * Checks if the value can safely be converted to a long primitive.
	 * </p>
	 * 
	 * @param value The value validation is being performed on.
	 * @return true if the value can be converted to a Long.
	 */
	public static boolean isLong(String value) {
		return (toLong(value) != null);
	}

	/**
	 * <p>
	 * Checks if the value can safely be converted to a float primitive.
	 * </p>
	 * 
	 * @param value The value validation is being performed on.
	 * @return true if the value can be converted to a Float.
	 */
	public static boolean isFloat(String value) {
		return (toFloat(value) != null);
	}

	/**
	 * <p>
	 * Checks if the value can safely be converted to a double primitive.
	 * </p>
	 * 
	 * @param value The value validation is being performed on.
	 * @return true if the value can be converted to a Double.
	 */
	public static boolean isDouble(String value) {
		return (toDouble(value) != null);
	}

	public static Integer toInt(Number num) {
		return (num == null ? null : num.intValue());
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to an <code>int</code>, returning <code>zero</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toInt(null) = null
	 *   Numbers.toInt("")   = null
	 *   Numbers.toInt("1")  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @return the int represented by the string, or <code>null</code> if conversion fails
	 */
	public static Integer toInt(String str) {
		return toInt(str, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to an <code>int</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toInt(null, 1) = 1
	 *   Numbers.toInt("", 1)   = 1
	 *   Numbers.toInt("1", 0)  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the int represented by the string, or the default if conversion fails
	 */
	public static Integer toInt(String str, Integer defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return createInteger(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	public static Long toLong(Number num) {
		return (num == null ? null : num.longValue());
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>long</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toLong(null) = null
	 *   Numbers.toLong("")   = null
	 *   Numbers.toLong("1")  = 1L
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @return the long represented by the string, or <code>null</code> if conversion fails
	 */
	public static Long toLong(String str) {
		return toLong(str, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>long</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toLong(null, 1L) = 1L
	 *   Numbers.toLong("", 1L)   = 1L
	 *   Numbers.toLong("1", 0L)  = 1L
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the long represented by the string, or the default if conversion fails
	 */
	public static Long toLong(String str, Long defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return createLong(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	public static Float toFloat(Number num) {
		return (num == null ? null : num.floatValue());
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>float</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toFloat(null)   = null
	 *   Numbers.toFloat("")     = null
	 *   Numbers.toFloat("1.5")  = 1.5f
	 * </pre>
	 * 
	 * @param str the string to convert, may be <code>null</code>
	 * @return the float represented by the string, or <code>null</code> if conversion fails
	 */
	public static Float toFloat(String str) {
		return toFloat(str, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>float</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string <code>str</code> is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toFloat(null, 1.1f)   = 1.0f
	 *   Numbers.toFloat("", 1.1f)     = 1.1f
	 *   Numbers.toFloat("1.5", 0.0f)  = 1.5f
	 * </pre>
	 * 
	 * @param str the string to convert, may be <code>null</code>
	 * @param defaultValue the default value
	 * @return the float represented by the string, or defaultValue if conversion fails
	 */
	public static Float toFloat(String str, Float defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return createFloat(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	public static Double toDouble(Number num) {
		return (num == null ? null : num.doubleValue());
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>double</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toDouble(null)   = null
	 *   Numbers.toDouble("")     = null
	 *   Numbers.toDouble("1.5")  = 1.5d
	 * </pre>
	 * 
	 * @param str the string to convert, may be <code>null</code>
	 * @return the double represented by the string, or <code>null</code> if conversion fails
	 */
	public static Double toDouble(String str) {
		return toDouble(str, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>double</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string <code>str</code> is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toDouble(null, 1.1d)   = 1.1d
	 *   Numbers.toDouble("", 1.1d)     = 1.1d
	 *   Numbers.toDouble("1.5", 0.0d)  = 1.5d
	 * </pre>
	 * 
	 * @param str the string to convert, may be <code>null</code>
	 * @param defaultValue the default value
	 * @return the double represented by the string, or defaultValue if conversion fails
	 */
	public static Double toDouble(String str, Double defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return createDouble(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}


	public static BigDecimal toBigDecimal(Number num) {
		return (num == null ? null : (num instanceof BigDecimal ? (BigDecimal)num : BigDecimal.valueOf(num.doubleValue())));
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigDecimal</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toBigDecimal(null)   = null
	 *   Numbers.toBigDecimal("")     = null
	 *   Numbers.toBigDecimal("1.5")  = 1.5d
	 * </pre>
	 * 
	 * @param str the string to convert, may be <code>null</code>
	 * @return the double represented by the string, or <code>null</code> if conversion fails
	 */
	public static BigDecimal toBigDecimal(String str) {
		return toBigDecimal(str, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigDecimal</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string <code>str</code> is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toBigDecimal(null, 1.1d)   = 1.1d
	 *   Numbers.toBigDecimal("", 1.1d)     = 1.1d
	 *   Numbers.toBigDecimal("1.5", 0.0d)  = 1.5d
	 * </pre>
	 * 
	 * @param str the string to convert, may be <code>null</code>
	 * @param defaultValue the default value
	 * @return the double represented by the string, or defaultValue if conversion fails
	 */
	public static BigDecimal toBigDecimal(String str, BigDecimal defaultValue) {
		if (Strings.isEmpty(str)) {
			return defaultValue;
		}
		try {
			return new BigDecimal(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigDecimal</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toBigDecimal(null)   = null
	 *   Numbers.toBigDecimal("")     = null
	 *   Numbers.toBigDecimal("1.5")  = 1.5d
	 * </pre>
	 * 
	 * @param num the double number to convert, may be <code>null</code>
	 * @return the double represented by the string, or <code>null</code> if conversion fails
	 */
	public static BigDecimal toBigDecimal(double num) {
		return toBigDecimal(num, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigDecimal</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string <code>str</code> is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toBigDecimal(null, 1.1d)   = 1.1d
	 *   Numbers.toBigDecimal("", 1.1d)     = 1.1d
	 *   Numbers.toBigDecimal("1.5", 0.0d)  = 1.5d
	 * </pre>
	 * 
	 * @param num the double number to convert, may be <code>null</code>
	 * @param defaultValue the default value
	 * @return the double represented by the string, or defaultValue if conversion fails
	 */
	public static BigDecimal toBigDecimal(double num, BigDecimal defaultValue) {
		if (num == Double.NaN) {
			return defaultValue;
		}
		try {
			return BigDecimal.valueOf(num);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	public static BigInteger toBigInteger(Number num) {
		return (num == null ? null : (num instanceof BigInteger ? (BigInteger)num : BigInteger.valueOf(num.longValue())));
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigInteger</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toBigInteger(null)   = null
	 *   Numbers.toBigInteger("")     = null
	 *   Numbers.toBigInteger("15")  = 15L
	 * </pre>
	 * 
	 * @param str the string to convert, may be <code>null</code>
	 * @return the double represented by the string, or <code>null</code> if conversion fails
	 */
	public static BigInteger toBigInteger(String str) {
		return toBigInteger(str, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigInteger</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string <code>str</code> is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toBigInteger(null, 1.1d)   = 1.1d
	 *   Numbers.toBigInteger("", 1.1d)     = 1.1d
	 *   Numbers.toBigInteger("1.5", 0.0d)  = 1.5d
	 * </pre>
	 * 
	 * @param str the string to convert, may be <code>null</code>
	 * @param defaultValue the default value
	 * @return the double represented by the string, or defaultValue if conversion fails
	 */
	public static BigInteger toBigInteger(String str, BigInteger defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return createBigInteger(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigInteger</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toBigInteger(null)   = null
	 *   Numbers.toBigInteger("")     = null
	 *   Numbers.toBigInteger("15")  = 15L
	 * </pre>
	 * 
	 * @param num the double number to convert, may be <code>null</code>
	 * @return the double represented by the string, or <code>null</code> if conversion fails
	 */
	public static BigInteger toBigInteger(long num) {
		return toBigInteger(num, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigInteger</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string <code>str</code> is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toBigInteger(null, 11L)   = 11L
	 *   Numbers.toBigInteger("", 11L)     = 11L
	 *   Numbers.toBigInteger("15", 0L)  = 15L
	 * </pre>
	 * 
	 * @param num the double number to convert, may be <code>null</code>
	 * @param defaultValue the default value
	 * @return the double represented by the string, or defaultValue if conversion fails
	 */
	public static BigInteger toBigInteger(long num, BigInteger defaultValue) {
		if (num == Double.NaN) {
			return defaultValue;
		}
		try {
			return BigInteger.valueOf(num);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>byte</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toByte(null) = null
	 *   Numbers.toByte("")   = null
	 *   Numbers.toByte("1")  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @return the byte represented by the string, or <code>null</code> if conversion fails
	 */
	public static Byte toByte(String str) {
		return toByte(str, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>byte</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toByte(null, 1) = 1
	 *   Numbers.toByte("", 1)   = 1
	 *   Numbers.toByte("1", 0)  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the byte represented by the string, or the default if conversion fails
	 */
	public static Byte toByte(String str, Byte defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return Byte.parseByte(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>short</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toShort(null) = null
	 *   Numbers.toShort("")   = null
	 *   Numbers.toShort("1")  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @return the short represented by the string, or <code>null</code> if conversion fails
	 */
	public static Short toShort(String str) {
		return toShort(str, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to an <code>short</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toShort(null, 1) = 1
	 *   Numbers.toShort("", 1)   = 1
	 *   Numbers.toShort("1", 0)  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the short represented by the string, or the default if conversion fails
	 */
	public static Short toShort(String str, Short defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return createShort(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>Number</code>, returning <code>null</code> if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, <code>null</code> is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toNumber(null) = null
	 *   Numbers.toNumber("")   = null
	 *   Numbers.toNumber("1")  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @return the short represented by the string, or <code>null</code> if conversion fails
	 */
	public static Number toNumber(String str) {
		return toNumber(str, null);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to an <code>Number</code>, returning a default value if the
	 * conversion fails.
	 * </p>
	 * <p>
	 * If the string is <code>null</code>, the default value is returned.
	 * </p>
	 * 
	 * <pre>
	 *   Numbers.toNumber(null, 1) = 1
	 *   Numbers.toNumber("", 1)   = 1
	 *   Numbers.toNumber("1", 0)  = 1
	 * </pre>
	 * 
	 * @param str the string to convert, may be null
	 * @param defaultValue the default value
	 * @return the short represented by the string, or the default if conversion fails
	 */
	public static Number toNumber(String str, Number defaultValue) {
		if (str == null) {
			return defaultValue;
		}
		try {
			return createNumber(str);
		}
		catch (NumberFormatException nfe) {
			return defaultValue;
		}
	}

	// -----------------------------------------------------------------------
	// must handle Long, Float, Integer, Float, Short,
	// BigDecimal, BigInteger and Byte
	// useful methods:
	// Byte.decode(String)
	// Byte.valueOf(String,int radix)
	// Byte.valueOf(String)
	// Double.valueOf(String)
	// Float.valueOf(String)
	// Float.valueOf(String)
	// Integer.valueOf(String,int radix)
	// Integer.valueOf(String)
	// Integer.decode(String)
	// Integer.getInteger(String)
	// Integer.getInteger(String,int val)
	// Integer.getInteger(String,Integer val)
	// Integer.valueOf(String)
	// Double.valueOf(String)
	// new Byte(String)
	// Long.valueOf(String)
	// Long.getLong(String)
	// Long.getLong(String,int)
	// Long.getLong(String,Integer)
	// Long.valueOf(String,int)
	// Long.valueOf(String)
	// Short.valueOf(String)
	// Short.decode(String)
	// Short.valueOf(String,int)
	// Short.valueOf(String)
	// new BigDecimal(String)
	// new BigInteger(String)
	// new BigInteger(String,int radix)
	// Possible inputs:
	// 45 45.5 45E7 4.5E7 Hex Oct Binary xxxF xxxD xxxf xxxd
	// plus minus everything. Prolly more. A lot are not separable.

	/**
	 * <p>
	 * Turns a string value into a java.lang.Number.
	 * </p>
	 * 
	 * <p>
	 * First, the value is examined for a type qualifier on the end (
	 * <code>'f','F','d','D','l','L'</code>). If it is found, it starts trying
	 * to create successively larger types from the type specified until one is
	 * found that can represent the value.
	 * </p>
	 * 
	 * <p>
	 * If a type specifier is not found, it will check for a decimal point and
	 * then try successively larger types from <code>Integer</code> to
	 * <code>BigInteger</code> and from <code>Float</code> to
	 * <code>BigDecimal</code>.
	 * </p>
	 * 
	 * <p>
	 * If the string starts with <code>0x</code> or <code>-0x</code> (lower or
	 * upper case), it will be interpreted as a hexadecimal integer. Values with
	 * leading <code>0</code>'s will not be interpreted as octal.
	 * </p>
	 * 
	 * <p>
	 * Returns <code>null</code> if the string is <code>null</code>.
	 * </p>
	 * 
	 * <p>
	 * This method does not trim the input string, i.e., strings with leading or
	 * trailing spaces will generate NumberFormatExceptions.
	 * </p>
	 * 
	 * @param str
	 *            String containing a number, may be null
	 * @return Number created from the string (or null if the input is null)
	 * @throws NumberFormatException
	 *             if the value cannot be converted
	 */
	public static Number createNumber(String str) throws NumberFormatException {
		if (str == null) {
			return null;
		}
		if (Strings.isBlank(str)) {
			throw new NumberFormatException("A blank string is not a valid number");
		}
		// Need to deal with all possible hex prefixes here
		final String[] hex_prefixes = { "0x", "0X", "-0x", "-0X", "#", "-#" };
		int pfxLen = 0;
		for (final String pfx : hex_prefixes) {
			if (str.startsWith(pfx)) {
				pfxLen += pfx.length();
				break;
			}
		}
		if (pfxLen > 0) { // we have a hex number
			char firstSigDigit = 0; // strip leading zeroes
			for (int i = pfxLen; i < str.length(); i++) {
				firstSigDigit = str.charAt(i);
				if (firstSigDigit == '0') { // count leading zeroes
					pfxLen++;
				}
				else {
					break;
				}
			}
			final int hexDigits = str.length() - pfxLen;
			if (hexDigits > 16 || (hexDigits == 16 && firstSigDigit > '7')) { // too many for Long
				return createBigInteger(str);
			}
			if (hexDigits > 8 || (hexDigits == 8 && firstSigDigit > '7')) { // too many for an int
				return createLong(str);
			}
			return createInteger(str);
		}
		final char lastChar = str.charAt(str.length() - 1);
		String mant;
		String dec;
		String exp;
		final int decPos = str.indexOf('.');
		final int expPos = str.indexOf('e') + str.indexOf('E') + 1; // assumes both not present
		// if both e and E are present, this is caught by the checks on expPos (which prevent IOOBE)
		// and the parsing which will detect if e or E appear in a number due to using the wrong
		// offset

		int numDecimals = 0; // Check required precision (LANG-693)
		if (decPos > -1) { // there is a decimal point

			if (expPos > -1) { // there is an exponent
				if (expPos < decPos || expPos > str.length()) { // prevents double exponent causing
																// IOOBE
					throw new NumberFormatException(str + " is not a valid number.");
				}
				dec = str.substring(decPos + 1, expPos);
			}
			else {
				dec = str.substring(decPos + 1);
			}
			mant = str.substring(0, decPos);
			numDecimals = dec.length(); // gets number of digits past the decimal to ensure no loss
										// of precision for floating point numbers.
		}
		else {
			if (expPos > -1) {
				if (expPos > str.length()) { // prevents double exponent causing IOOBE
					throw new NumberFormatException(str + " is not a valid number.");
				}
				mant = str.substring(0, expPos);
			}
			else {
				mant = str;
			}
			dec = null;
		}
		if (!Character.isDigit(lastChar) && lastChar != '.') {
			if (expPos > -1 && expPos < str.length() - 1) {
				exp = str.substring(expPos + 1, str.length() - 1);
			}
			else {
				exp = null;
			}
			// Requesting a specific type..
			final String numeric = str.substring(0, str.length() - 1);
			final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
			switch (lastChar) {
			case 'l':
			case 'L':
				if (dec == null && exp == null
						&& (numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {
					try {
						return createLong(numeric);
					}
					catch (final NumberFormatException nfe) { // NOPMD
						// Too big for a long
					}
					return createBigInteger(numeric);

				}
				throw new NumberFormatException(str + " is not a valid number.");
			case 'f':
			case 'F':
				try {
					final Float f = createFloat(numeric);
					if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
						// If it's too big for a float or the float value = 0 and the string
						// has non-zeros in it, then float does not have the precision we want
						return f;
					}

				}
				catch (final NumberFormatException nfe) { // NOPMD
					// ignore the bad number
				}
				//$FALL-THROUGH$
			case 'd':
			case 'D':
				try {
					final Double d = createDouble(numeric);
					if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
						return d;
					}
				}
				catch (final NumberFormatException nfe) { // NOPMD
					// ignore the bad number
				}
				try {
					return createBigDecimal(numeric);
				}
				catch (final NumberFormatException e) { // NOPMD
					// ignore the bad number
				}
				//$FALL-THROUGH$
			default:
				throw new NumberFormatException(str + " is not a valid number.");

			}
		}
		// User doesn't have a preference on the return type, so let's start
		// small and go from there...
		if (expPos > -1 && expPos < str.length() - 1) {
			exp = str.substring(expPos + 1, str.length());
		}
		else {
			exp = null;
		}
		if (dec == null && exp == null) { // no decimal point and no exponent
			// Must be an Integer, Long, Biginteger
			try {
				return createInteger(str);
			}
			catch (final NumberFormatException nfe) { // NOPMD
				// ignore the bad number
			}
			try {
				return createLong(str);
			}
			catch (final NumberFormatException nfe) { // NOPMD
				// ignore the bad number
			}
			return createBigInteger(str);
		}

		// Must be a Float, Double, BigDecimal
		final boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
		try {
			if (numDecimals <= 7) {// If number has 7 or fewer digits past the decimal point then
									// make it a float
				final Float f = createFloat(str);
				if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
					return f;
				}
			}
		}
		catch (final NumberFormatException nfe) { // NOPMD
			// ignore the bad number
		}
		try {
			if (numDecimals <= 16) {// If number has between 8 and 16 digits past the decimal point
									// then make it a double
				final Double d = createDouble(str);
				if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
					return d;
				}
			}
		}
		catch (final NumberFormatException nfe) { // NOPMD
			// ignore the bad number
		}

		return createBigDecimal(str);
	}

	/**
	 * <p>
	 * Returns <code>true</code> if s is <code>null</code>.
	 * </p>
	 * 
	 * @param str the String to check
	 * @return if it is all zeros or <code>null</code>
	 */
	public static boolean isAllZeros(String str) {
		if (str == null) {
			return true;
		}
		for (int i = str.length() - 1; i >= 0; i--) {
			if (str.charAt(i) != '0') {
				return false;
			}
		}
		return str.length() > 0;
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>Float</code>.
	 * </p>
	 * 
	 * <p>
	 * Returns <code>null</code> if the string is <code>null</code>.
	 * </p>
	 * 
	 * @param str
	 *            a <code>String</code> to convert, may be null
	 * @return converted <code>Float</code>
	 * @throws NumberFormatException
	 *             if the value cannot be converted
	 */
	public static Float createFloat(String str) {
		if (str == null) {
			return null;
		}
		return Float.valueOf(str);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>Double</code>.
	 * </p>
	 * 
	 * <p>
	 * Returns <code>null</code> if the string is <code>null</code>.
	 * </p>
	 * 
	 * @param str
	 *            a <code>String</code> to convert, may be null
	 * @return converted <code>Double</code>
	 * @throws NumberFormatException
	 *             if the value cannot be converted
	 */
	public static Double createDouble(String str) {
		if (str == null) {
			return null;
		}
		return Double.valueOf(str);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>Byte</code>, handling hex and
	 * octal notations.
	 * </p>
	 * 
	 * <p>
	 * Returns <code>null</code> if the string is <code>null</code>.
	 * </p>
	 * 
	 * @param str
	 *            a <code>String</code> to convert, may be null
	 * @return converted <code>Integer</code>
	 * @throws NumberFormatException
	 *             if the value cannot be converted
	 */
	public static Byte createByte(String str) {
		if (str == null) {
			return null;
		}
		// decode() handles 0xAABD and 0777 (hex and octal) as well.
		return Byte.decode(str);
	}


	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>Integer</code>, handling hex and
	 * octal notations.
	 * </p>
	 * 
	 * <p>
	 * Returns <code>null</code> if the string is <code>null</code>.
	 * </p>
	 * 
	 * @param str
	 *            a <code>String</code> to convert, may be null
	 * @return converted <code>Integer</code>
	 * @throws NumberFormatException
	 *             if the value cannot be converted
	 */
	public static Short createShort(String str) {
		if (str == null) {
			return null;
		}
		// decode() handles 0xAABD and 0777 (hex and octal) as well.
		return Short.decode(str);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>Integer</code>, handling hex and
	 * octal notations.
	 * </p>
	 * 
	 * <p>
	 * Returns <code>null</code> if the string is <code>null</code>.
	 * </p>
	 * 
	 * @param str
	 *            a <code>String</code> to convert, may be null
	 * @return converted <code>Integer</code>
	 * @throws NumberFormatException
	 *             if the value cannot be converted
	 */
	public static Integer createInteger(String str) {
		if (str == null) {
			return null;
		}
		// decode() handles 0xAABD and 0777 (hex and octal) as well.
		return Integer.decode(str);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>Long</code>; since 3.1 it
	 * handles hex and octal notations.
	 * </p>
	 * 
	 * <p>
	 * Returns <code>null</code> if the string is <code>null</code>.
	 * </p>
	 * 
	 * @param str
	 *            a <code>String</code> to convert, may be null
	 * @return converted <code>Long</code>
	 * @throws NumberFormatException
	 *             if the value cannot be converted
	 */
	public static Long createLong(String str) {
		if (str == null) {
			return null;
		}
		return Long.decode(str);
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigInteger</code>.
	 * </p>
	 * 
	 * <p>
	 * Returns <code>null</code> if the string is <code>null</code>.
	 * </p>
	 * 
	 * @param str
	 *            a <code>String</code> to convert, may be null
	 * @return converted <code>BigInteger</code>
	 * @throws NumberFormatException
	 *             if the value cannot be converted
	 */
	public static BigInteger createBigInteger(String str) {
		if (str == null) {
			return null;
		}
		int pos = 0; // offset within string
		int radix = 10;
		boolean negate = false; // need to negate later?
		if (str.startsWith("-")) {
			negate = true;
			pos = 1;
		}
		if (str.startsWith("0x", pos) || str.startsWith("0x", pos)) { // hex
			radix = 16;
			pos += 2;
		}
		else if (str.startsWith("#", pos)) { // alternative hex (allowed by Long/Integer)
			radix = 16;
			pos++;
		}
		else if (str.startsWith("0", pos) && str.length() > pos + 1) { // octal; so long as there
																		// are additional digits
			radix = 8;
			pos++;
		} // default is to treat as decimal

		final BigInteger value = new BigInteger(str.substring(pos), radix);
		return negate ? value.negate() : value;
	}

	/**
	 * <p>
	 * Convert a <code>String</code> to a <code>BigDecimal</code>.
	 * </p>
	 * 
	 * <p>
	 * Returns <code>null</code> if the string is <code>null</code>.
	 * </p>
	 * 
	 * @param str
	 *            a <code>String</code> to convert, may be null
	 * @return converted <code>BigDecimal</code>
	 * @throws NumberFormatException
	 *             if the value cannot be converted
	 */
	public static BigDecimal createBigDecimal(String str) {
		if (str == null) {
			return null;
		}
		// handle JDK1.3.1 bug where "" throws IndexOutOfBoundsException
		if (Strings.isBlank(str)) {
			throw new NumberFormatException(
					"A blank string is not a valid number");
		}
		return new BigDecimal(str);
	}

	/**
	 * Checks if the specified array is neither null nor empty.
	 *
	 * @param array the array to check
	 * @throws IllegalArgumentException if {@code array} is either {@code null} or empty
	 */
	private static void validateArray(final Object array) {
		if (array == null) {
			throw new IllegalArgumentException("The Array must not be null");
		}
		if (Array.getLength(array) == 0) {
			throw new IllegalArgumentException("Array cannot be empty.");
		}
	}

	// Min in array
	// --------------------------------------------------------------------
	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static long min(final long... array) {
		validateArray(array);

		// Finds and returns min
		long min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static int min(final int... array) {
		validateArray(array);

		// Finds and returns min
		int min = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] < min) {
				min = array[j];
			}
		}

		return min;
	}

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static short min(final short... array) {
		validateArray(array);

		// Finds and returns min
		short min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte min(final byte ... array) {
		validateArray(array);

		// Finds and returns min
		byte min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static double min(final double... array) {
		validateArray(array);

		// Finds and returns min
		double min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (Double.isNaN(array[i])) {
				return Double.NaN;
			}
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

	/**
	 * <p>
	 * Returns the minimum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static float min(final float... array) {
		validateArray(array);

		// Finds and returns min
		float min = array[0];
		for (int i = 1; i < array.length; i++) {
			if (Float.isNaN(array[i])) {
				return Float.NaN;
			}
			if (array[i] < min) {
				min = array[i];
			}
		}

		return min;
	}

	// Max in array
	// --------------------------------------------------------------------
	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static long max(final long... array) {
		validateArray(array);

		// Finds and returns max
		long max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] > max) {
				max = array[j];
			}
		}

		return max;
	}

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static int max(final int... array) {
		validateArray(array);

		// Finds and returns max
		int max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (array[j] > max) {
				max = array[j];
			}
		}

		return max;
	}

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static short max(final short... array) {
		validateArray(array);

		// Finds and returns max
		short max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}

		return max;
	}

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static byte max(final byte... array) {
		validateArray(array);

		// Finds and returns max
		byte max = array[0];
		for (int i = 1; i < array.length; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}

		return max;
	}

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static double max(final double... array) {
		validateArray(array);

		// Finds and returns max
		double max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (Double.isNaN(array[j])) {
				return Double.NaN;
			}
			if (array[j] > max) {
				max = array[j];
			}
		}

		return max;
	}

	/**
	 * <p>
	 * Returns the maximum value in an array.
	 * </p>
	 * 
	 * @param array an array, must not be null or empty
	 * @return the minimum value in the array
	 * @throws IllegalArgumentException if <code>array</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>array</code> is empty
	 */
	public static float max(final float... array) {
		validateArray(array);

		// Finds and returns max
		float max = array[0];
		for (int j = 1; j < array.length; j++) {
			if (Float.isNaN(array[j])) {
				return Float.NaN;
			}
			if (array[j] > max) {
				max = array[j];
			}
		}

		return max;
	}

	// 3 param min
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the minimum of three <code>long</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static long min(long a, long b, long c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>int</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static int min(int a, int b, int c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>short</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static short min(short a, short b, short c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>byte</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static byte min(byte a, byte b, byte c) {
		if (b < a) {
			a = b;
		}
		if (c < a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>double</code> values.
	 * </p>
	 * <p>
	 * If any value is <code>NaN</code>, <code>NaN</code> is returned. Infinity is handled.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static double min(double a, double b, double c) {
		return Math.min(Math.min(a, b), c);
	}

	/**
	 * <p>
	 * Gets the minimum of three <code>float</code> values.
	 * </p>
	 * <p>
	 * If any value is <code>NaN</code>, <code>NaN</code> is returned. Infinity is handled.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the smallest of the values
	 */
	public static float min(float a, float b, float c) {
		return Math.min(Math.min(a, b), c);
	}

	// 3 param max
	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Gets the maximum of three <code>long</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static long max(long a, long b, long c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>int</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static int max(int a, int b, int c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>short</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static short max(short a, short b, short c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>byte</code> values.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static byte max(byte a, byte b, byte c) {
		if (b > a) {
			a = b;
		}
		if (c > a) {
			a = c;
		}
		return a;
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>double</code> values.
	 * </p>
	 * <p>
	 * If any value is <code>NaN</code>, <code>NaN</code> is returned. Infinity is handled.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static double max(double a, double b, double c) {
		return Math.max(Math.max(a, b), c);
	}

	/**
	 * <p>
	 * Gets the maximum of three <code>float</code> values.
	 * </p>
	 * <p>
	 * If any value is <code>NaN</code>, <code>NaN</code> is returned. Infinity is handled.
	 * </p>
	 * 
	 * @param a value 1
	 * @param b value 2
	 * @param c value 3
	 * @return the largest of the values
	 */
	public static float max(float a, float b, float c) {
		return Math.max(Math.max(a, b), c);
	}

	// -----------------------------------------------------------------------
	public static int compare(Number n1, Number n2) {
		return compare(n1, n2, false);
	}
	
	public static int compare(Number n1, Number n2, boolean nullGreater) {
		if (n1 == n2) {
			return 0;
		}
		
		if (n1 == null) {
			return nullGreater ? 1 : -1;
		}
		
		if (n2 == null) {
			return nullGreater ? -1 : 1;
		}
		return compare(n1.doubleValue(), n2.doubleValue());
	}

	/**
	 * <p>
	 * Compares two <code>doubles</code> for order.
	 * </p>
	 * <p>
	 * This method is more comprehensive than the standard Java greater than, less than and equals
	 * operators.
	 * </p>
	 * <ul>
	 * <li>It returns <code>-1</code> if the first value is less than the second.</li>
	 * <li>It returns <code>+1</code> if the first value is greater than the second.</li>
	 * <li>It returns <code>0</code> if the values are equal.</li>
	 * </ul>
	 * <p>
	 * The ordering is as follows, largest to smallest:
	 * <ul>
	 * <li>NaN
	 * <li>Positive infinity
	 * <li>Maximum double
	 * <li>Normal positive numbers
	 * <li>+0.0
	 * <li>-0.0
	 * <li>Normal negative numbers
	 * <li>Minimum double (<code>-Double.MAX_VALUE</code>)
	 * <li>Negative infinity
	 * </ul>
	 * </p>
	 * <p>
	 * Comparing <code>NaN</code> with <code>NaN</code> will return <code>0</code>.
	 * </p>
	 * 
	 * @param lhs the first <code>double</code>
	 * @param rhs the second <code>double</code>
	 * @return <code>-1</code> if lhs is less, <code>+1</code> if greater, <code>0</code> if equal
	 *         to rhs
	 */
	public static int compare(double lhs, double rhs) {
		if (lhs < rhs) {
			return -1;
		}
		if (lhs > rhs) {
			return +1;
		}
		// Need to compare bits to handle 0.0 == -0.0 being true
		// compare should put -0.0 < +0.0
		// Two NaNs are also == for compare purposes
		// where NaN == NaN is false
		long lhsBits = Double.doubleToLongBits(lhs);
		long rhsBits = Double.doubleToLongBits(rhs);
		if (lhsBits == rhsBits) {
			return 0;
		}
		// Something exotic! A comparison to NaN or 0.0 vs -0.0
		// Fortunately NaN's long is > than everything else
		// Also negzeros bits < poszero
		// NAN: 9221120237041090560
		// MAX: 9218868437227405311
		// NEGZERO: -9223372036854775808
		if (lhsBits < rhsBits) {
			return -1;
		}
		else {
			return +1;
		}
	}

	/**
	 * <p>
	 * Compares two floats for order.
	 * </p>
	 * <p>
	 * This method is more comprehensive than the standard Java greater than, less than and equals
	 * operators.
	 * </p>
	 * <ul>
	 * <li>It returns <code>-1</code> if the first value is less than the second.
	 * <li>It returns <code>+1</code> if the first value is greater than the second.
	 * <li>It returns <code>0</code> if the values are equal.
	 * </ul>
	 * <p>
	 * The ordering is as follows, largest to smallest:
	 * <ul>
	 * <li>NaN
	 * <li>Positive infinity
	 * <li>Maximum float
	 * <li>Normal positive numbers
	 * <li>+0.0
	 * <li>-0.0
	 * <li>Normal negative numbers
	 * <li>Minimum float (<code>-Float.MAX_VALUE</code>)
	 * <li>Negative infinity
	 * </ul>
	 * <p>
	 * Comparing <code>NaN</code> with <code>NaN</code> will return <code>0</code>.
	 * </p>
	 * 
	 * @param lhs the first <code>float</code>
	 * @param rhs the second <code>float</code>
	 * @return <code>-1</code> if lhs is less, <code>+1</code> if greater, <code>0</code> if equal
	 *         to rhs
	 */
	public static int compare(float lhs, float rhs) {
		if (lhs < rhs) {
			return -1;
		}
		if (lhs > rhs) {
			return +1;
		}
		// Need to compare bits to handle 0.0 == -0.0 being true
		// compare should put -0.0 < +0.0
		// Two NaNs are also == for compare purposes
		// where NaN == NaN is false
		int lhsBits = Float.floatToIntBits(lhs);
		int rhsBits = Float.floatToIntBits(rhs);
		if (lhsBits == rhsBits) {
			return 0;
		}
		// Something exotic! A comparison to NaN or 0.0 vs -0.0
		// Fortunately NaN's int is > than everything else
		// Also negzeros bits < poszero
		// NAN: 2143289344
		// MAX: 2139095039
		// NEGZERO: -2147483648
		if (lhsBits < rhsBits) {
			return -1;
		}
		else {
			return +1;
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks whether the <code>String</code> contains only digit characters.
	 * </p>
	 * <p>
	 * <code>Null</code> and empty String will return <code>false</code>.
	 * </p>
	 * 
	 * @param str the <code>String</code> to check
	 * @return <code>true</code> if str contains only unicode numeric
	 */
	public static boolean isDigits(String str) {
		if (Strings.isEmpty(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks whether the String a valid Java number.
	 * </p>
	 * <p>
	 * Valid numbers include hexadecimal marked with the <code>0x</code> qualifier, scientific
	 * notation and numbers marked with a type qualifier (e.g. 123L).
	 * </p>
	 * <p>
	 * <code>Null</code> and empty String will return <code>false</code>.
	 * </p>
	 * 
	 * @param str the <code>String</code> to check
	 * @return <code>true</code> if the string is a correctly formatted number
	 */
	public static boolean isNumber(String str) {
		if (Strings.isEmpty(str)) {
			return false;
		}
		char[] chars = str.toCharArray();
		int sz = chars.length;
		boolean hasExp = false;
		boolean hasDecPoint = false;
		boolean allowSigns = false;
		boolean foundDigit = false;
		// deal with any possible sign up front
		int start = (chars[0] == '-') ? 1 : 0;
		if (sz > start + 1) {
			if (chars[start] == '0' && chars[start + 1] == 'x') {
				int i = start + 2;
				if (i == sz) {
					return false; // str == "0x"
				}
				// checking hex (it can't be anything else)
				for (; i < chars.length; i++) {
					if ((chars[i] < '0' || chars[i] > '9') && (chars[i] < 'a' || chars[i] > 'f')
							&& (chars[i] < 'A' || chars[i] > 'F')) {
						return false;
					}
				}
				return true;
			}
		}
		sz--; // don't want to loop to the last char, check it afterwords
				// for type qualifiers
		int i = start;
		// loop to the next to last char or to the last char if we need another digit to
		// make a valid number (e.g. chars[0..5] = "1234E")
		while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				foundDigit = true;
				allowSigns = false;

			}
			else if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				hasDecPoint = true;
			}
			else if (chars[i] == 'e' || chars[i] == 'E') {
				// we've already taken care of hex.
				if (hasExp) {
					// two E's
					return false;
				}
				if (!foundDigit) {
					return false;
				}
				hasExp = true;
				allowSigns = true;
			}
			else if (chars[i] == '+' || chars[i] == '-') {
				if (!allowSigns) {
					return false;
				}
				allowSigns = false;
				foundDigit = false; // we need a digit after the E
			}
			else {
				return false;
			}
			i++;
		}
		if (i < chars.length) {
			if (chars[i] >= '0' && chars[i] <= '9') {
				// no type qualifier, OK
				return true;
			}
			if (chars[i] == 'e' || chars[i] == 'E') {
				// can't have an E at the last byte
				return false;
			}
			if (chars[i] == '.') {
				if (hasDecPoint || hasExp) {
					// two decimal points or dec in exponent
					return false;
				}
				// single trailing decimal point after non-exponent is ok
				return foundDigit;
			}
			if (!allowSigns
					&& (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
				return foundDigit;
			}
			if (chars[i] == 'l' || chars[i] == 'L') {
				// not allowing L with an exponent or decimal point
				return foundDigit && !hasExp && !hasDecPoint;
			}
			// last character is illegal
			return false;
		}
		// allowSigns is true iff the val ends in 'E'
		// found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
		return !allowSigns && foundDigit;
	}

	// -----------------------------------------------------------------------
	private static final ThreadLocal<DecimalFormat> DEFAULT_FORMAT = new ThreadLocal<DecimalFormat>() {
		@Override
		protected DecimalFormat initialValue() {
			DecimalFormat df = new DecimalFormat("#", new DecimalFormatSymbols(Locale.ENGLISH));
			df.setMaximumFractionDigits(340);
			return df;
		}
	};

	/**
	 * @param n the number to format
	 * @return formatted number string
	 */
	public static String format(Number n) {
		return DEFAULT_FORMAT.get().format(n);
	}

	/**
	 * decFormat(1.235, 2) -> 1.23
	 * decFormat(1.236, 2) -> 1.24
	 * 
	 * @param n the number to format
	 * @param frac maximum fraction digits
	 * @return formatted number string
	 */
	public static String decFormat(Number n, int frac) {
		DecimalFormat df = null;
		df = new DecimalFormat("#", new DecimalFormatSymbols(Locale.ENGLISH));
		df.setMaximumFractionDigits(frac);
		return df.format(n);
	}

	/**
	 * decFormat(1.235, 2) -> 1.23
	 * decFormat(1.236, 2) -> 1.23
	 * 
	 * @param n the number to format
	 * @param frac maximum fraction digits
	 * @return formatted number string
	 */
	public static String cutFormat(double n, int frac) {
		boolean minus = false;
		if (n < 0) {
			minus = true;
			n = -n;
		}

		long i = (long)n;
		double d = n - i;
		double p = Math.pow(10, frac);
		double dp = d * p;
		long f = (long)(dp);

		if (f <= 0) {
			return String.valueOf(i);
		}

		StringBuilder sb = new StringBuilder();
		if (minus) {
			sb.append('-');
		};
		sb.append(i)
		  .append('.')
		  .append(Strings.stripEnd(Strings.leftPad(String.valueOf(f), frac, '0'), '0'));
		
		return sb.toString();
	}

	// -----------------------------------------------------------------------
	private static String _formatSize(final double size, int frac) {
		return cutFormat(size, frac);
	}
	
	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final BigInteger size) {
		return formatSize(size, 2);
	}

	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @param frac maximum fraction digits
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final BigInteger size, int frac) {
		if (size == null) {
			return Strings.EMPTY;
		}

		return formatSize(new BigDecimal(size), frac);
	}
	
	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final BigDecimal size) {
		return formatSize(size, 2);
	}
	
	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @param frac maximum fraction digits
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final BigDecimal size, int frac) {
		if (size == null) {
			return Strings.EMPTY;
		}

		String sz;
		if (size.compareTo(Numbers.BD_YB) > 0) {
			sz = _formatSize(size.divide(BD_YB).doubleValue(), frac) + " YB";
		}
		else if (size.compareTo(BD_ZB) > 0) {
			sz = _formatSize(size.divide(BD_ZB).doubleValue(), frac) + " ZB";
		}
		else {
			sz = formatSize(size.longValue(), frac);
		}
		return sz;
	}

	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final Number size) {
		return formatSize(size, 2);
	}
	
	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @param frac maximum fraction digits
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final Number size, int frac) {
		if (size == null) {
			return Strings.EMPTY;
		}

		return formatSize(size.doubleValue(), frac);
	}

	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final long size) {
		return formatSize(size, 2);
	}
	
	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @param frac maximum fraction digits
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final long size, int frac) {
		return formatSize((double)size, frac);
	}

	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final double size) {
		return formatSize(size, 2);
	}

	/**
	 * Returns a human-readable version of the file size, where the input represents a specific
	 * number of bytes.
	 * <p>
	 * If the size is over 1GB, the size is returned as the number of whole GB, i.e. the size is
	 * rounded down to the nearest GB boundary.
	 * </p>
	 * <p>
	 * Similarly for the 1MB and 1KB boundaries.
	 * </p>
	 * 
	 * @param size the number of bytes
	 * @return a human-readable display value (includes units - EB, PB, TB, GB, MB, KB or bytes)
	 */
	public static String formatSize(final double size, int frac) {
		String sz;
		if (size >= EB) {
			sz = _formatSize(size / EB, frac) + " EB";
		}
		else if (size >= PB) {
			sz = _formatSize(size / PB, frac) + " PB";
		}
		else if (size >= TB) {
			sz = _formatSize(size / TB, frac) + " TB";
		}
		else if (size >= GB) {
			sz = _formatSize(size / GB, frac) + " GB";
		}
		else if (size >= MB) {
			sz = _formatSize(size / MB, frac) + " MB";
		}
		else if (size >= KB) {
			sz = _formatSize(size / KB, frac) + " KB";
		}
		else {
			sz = _formatSize(size, frac) + " bytes";
		}
		return sz;
	}

	/**
	 * parse display size to number.
	 * return null if the input string is not a valid display size string.
	 * 
	 * @param str display size string
	 * @return number
	 */
	public static BigDecimal parseSize(final String str) {
		if (Strings.isEmpty(str)) {
			return null;
		}

		int i = 0;
		while (i < str.length() && !Character.isLetter(str.charAt(i))) {
			i++;
		}

		BigDecimal n = toBigDecimal(Strings.strip(str.substring(0, i)));
		if (n == null) {
			return null;
		}

		if (i == str.length()) {
			return n;
		}

		char unit = str.charAt(i);
		switch (unit) {
		case 'Y':
		case 'y':
			n = n.multiply(BD_YB);
			break;
		case 'Z':
		case 'z':
			n = n.multiply(BD_ZB);
			break;
		case 'E':
		case 'e':
			n = n.multiply(BD_EB);
			break;
		case 'P':
		case 'p':
			n = n.multiply(BD_PB);
			break;
		case 'T':
		case 't':
			n = n.multiply(BD_TB);
			break;
		case 'G':
		case 'g':
			n = n.multiply(BD_GB);
			break;
		case 'M':
		case 'm':
			n = n.multiply(BD_MB);
			break;
		case 'K':
		case 'k':
			n = n.multiply(BD_KB);
			break;
		}
		return n;
	}
}
