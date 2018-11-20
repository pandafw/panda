package panda.lang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import org.junit.Test;

import panda.lang.time.StopWatch;

/**
 */
public class NumbersTest {
	// ---------------------------------------------------------------------
	/**
	 * Test for {@link Numbers#toInt(String)}.
	 */
	@Test
	public void testToIntString() {
		assertTrue("toInt(String) 1 failed", Numbers.toInt("12345") == 12345);
		assertTrue("toInt(String) 2 failed", Numbers.toInt("abc") == null);
		assertTrue("toInt(empty) failed", Numbers.toInt("") == null);
		assertTrue("toInt(null) failed", Numbers.toInt((String)null) == null);
	}

	/**
	 * Test for {@link Numbers#toInt(String, Integer)}.
	 */
	@Test
	public void testToIntStringI() {
		assertTrue("toInt(String,int) 1 failed", Numbers.toInt("12345", 5) == 12345);
		assertTrue("toInt(String,int) 2 failed", Numbers.toInt("1234.5", 5) == 5);
	}

	/**
	 * Test for {@link Numbers#toLong(String)}.
	 */
	@Test
	public void testToLongString() {
		assertTrue("toLong(String) 1 failed", Numbers.toLong("12345") == 12345l);
		assertTrue("toLong(String) 2 failed", Numbers.toLong("abc") == null);
		assertTrue("toLong(String) 3 failed", Numbers.toLong("1L") == null);
		assertTrue("toLong(String) 4 failed", Numbers.toLong("1l") == null);
		assertTrue("toLong(Long.MAX_VALUE) failed", Numbers.toLong(Long.MAX_VALUE + "") == Long.MAX_VALUE);
		assertTrue("toLong(Long.MIN_VALUE) failed", Numbers.toLong(Long.MIN_VALUE + "") == Long.MIN_VALUE);
		assertTrue("toLong(empty) failed", Numbers.toLong("") == null);
		assertTrue("toLong(null) failed", Numbers.toLong((String)null) == null);
	}

	/**
	 * Test for {@link Numbers#toLong(String, Long)}.
	 */
	@Test
	public void testToLongStringL() {
		assertTrue("toLong(String,long) 1 failed", Numbers.toLong("12345", 5l) == 12345l);
		assertTrue("toLong(String,long) 2 failed", Numbers.toLong("1234.5", 5l) == 5l);
	}

	/**
	 * Test for {@link Numbers#toFloat(String)}.
	 */
	@Test
	public void testToFloatString() {
		assertTrue("toFloat(String) 1 failed", Numbers.toFloat("-1.2345") == -1.2345f);
		assertTrue("toFloat(String) 2 failed", Numbers.toFloat("1.2345") == 1.2345f);
		assertTrue("toFloat(String) 3 failed", Numbers.toFloat("abc") == null);
		assertTrue("toFloat(Float.MAX_VALUE) failed", Numbers.toFloat(Float.MAX_VALUE + "") == Float.MAX_VALUE);
		assertTrue("toFloat(Float.MIN_VALUE) failed", Numbers.toFloat(Float.MIN_VALUE + "") == Float.MIN_VALUE);
		assertTrue("toFloat(empty) failed", Numbers.toFloat("") == null);
		assertTrue("toFloat(null) failed", Numbers.toFloat((String)null) == null);
	}

	/**
	 * Test for {@link Numbers#toFloat(String, Float)}.
	 */
	@Test
	public void testToFloatStringF() {
		assertTrue("toFloat(String,int) 1 failed", Numbers.toFloat("1.2345", 5.1f) == 1.2345f);
		assertTrue("toFloat(String,int) 2 failed", Numbers.toFloat("a", 5.0f) == 5.0f);
	}

	/**
	 * Test for {(@link Numbers#createNumber(String)}
	 */
	@Test
	public void testStringCreateNumberEnsureNoPrecisionLoss() {
		String shouldBeFloat = "1.23";
		String shouldBeDouble = "3.40282354e+38";
		String shouldBeBigDecimal = "1.797693134862315759e+308";

		assertTrue(Numbers.createNumber(shouldBeFloat) instanceof Float);
		assertTrue(Numbers.createNumber(shouldBeDouble) instanceof Double);
		assertTrue(Numbers.createNumber(shouldBeBigDecimal) instanceof BigDecimal);
	}

	/**
	 * Test for {@link Numbers#toDouble(String)}.
	 */
	@Test
	public void testStringToDoubleString() {
		assertTrue("toDouble(String) 1 failed", Numbers.toDouble("-1.2345") == -1.2345d);
		assertTrue("toDouble(String) 2 failed", Numbers.toDouble("1.2345") == 1.2345d);
		assertTrue("toDouble(String) 3 failed", Numbers.toDouble("abc") == null);
		assertTrue("toDouble(Double.MAX_VALUE) failed", Numbers.toDouble(Double.MAX_VALUE + "") == Double.MAX_VALUE);
		assertTrue("toDouble(Double.MIN_VALUE) failed", Numbers.toDouble(Double.MIN_VALUE + "") == Double.MIN_VALUE);
		assertTrue("toDouble(empty) failed", Numbers.toDouble("") == null);
		assertTrue("toDouble(null) failed", Numbers.toDouble((String)null) == null);
	}

	/**
	 * Test for {@link Numbers#toDouble(String, Double)}.
	 */
	@Test
	public void testStringToDoubleStringD() {
		assertTrue("toDouble(String,int) 1 failed", Numbers.toDouble("1.2345", 5.1d) == 1.2345d);
		assertTrue("toDouble(String,int) 2 failed", Numbers.toDouble("a", 5.0d) == 5.0d);
	}

	/**
	 * Test for {@link Numbers#toByte(String)}.
	 */
	@Test
	public void testToByteString() {
		assertTrue("toByte(String) 1 failed", Numbers.toByte("123") == 123);
		assertTrue("toByte(String) 2 failed", Numbers.toByte("abc") == null);
		assertTrue("toByte(empty) failed", Numbers.toByte("") == null);
		assertTrue("toByte(null) failed", Numbers.toByte(null) == null);
	}

	/**
	 * Test for {@link Numbers#toByte(String, Byte)}.
	 */
	@Test
	public void testToByteStringI() {
		assertTrue("toByte(String,byte) 1 failed", Numbers.toByte("123", (byte)5) == 123);
		assertTrue("toByte(String,byte) 2 failed", Numbers.toByte("12.3", (byte)5) == 5);
	}

	/**
	 * Test for {@link Numbers#toShort(String)}.
	 */
	@Test
	public void testToShortString() {
		assertTrue("toShort(String) 1 failed", Numbers.toShort("12345") == 12345);
		assertTrue("toShort(String) 2 failed", Numbers.toShort("abc") == null);
		assertTrue("toShort(empty) failed", Numbers.toShort("") == null);
		assertTrue("toShort(null) failed", Numbers.toShort(null) == null);
	}

	/**
	 * Test for {@link Numbers#toShort(String, Short)}.
	 */
	@Test
	public void testToShortStringI() {
		assertTrue("toShort(String,short) 1 failed", Numbers.toShort("12345", (short)5) == 12345);
		assertTrue("toShort(String,short) 2 failed", Numbers.toShort("1234.5", (short)5) == 5);
	}

	@Test
	public void testCreateNumber() {
		// a lot of things can go wrong
		assertEquals("createNumber(String) 1 failed", Float.valueOf("1234.5"), Numbers.createNumber("1234.5"));
		assertEquals("createNumber(String) 2 failed", Integer.valueOf("12345"), Numbers.createNumber("12345"));
		assertEquals("createNumber(String) 3 failed", Double.valueOf("1234.5"), Numbers.createNumber("1234.5D"));
		assertEquals("createNumber(String) 3 failed", Double.valueOf("1234.5"), Numbers.createNumber("1234.5d"));
		assertEquals("createNumber(String) 4 failed", Float.valueOf("1234.5"), Numbers.createNumber("1234.5F"));
		assertEquals("createNumber(String) 4 failed", Float.valueOf("1234.5"), Numbers.createNumber("1234.5f"));
		assertEquals("createNumber(String) 5 failed", Long.valueOf(Integer.MAX_VALUE + 1L),
			Numbers.createNumber("" + (Integer.MAX_VALUE + 1L)));
		assertEquals("createNumber(String) 6 failed", Long.valueOf(12345), Numbers.createNumber("12345L"));
		assertEquals("createNumber(String) 6 failed", Long.valueOf(12345), Numbers.createNumber("12345l"));
		assertEquals("createNumber(String) 7 failed", Float.valueOf("-1234.5"), Numbers.createNumber("-1234.5"));
		assertEquals("createNumber(String) 8 failed", Integer.valueOf("-12345"), Numbers.createNumber("-12345"));
		assertTrue("createNumber(String) 9a failed", 0xFADE == Numbers.createNumber("0xFADE").intValue());
		assertTrue("createNumber(String) 9b failed", 0xFADE == Numbers.createNumber("0Xfade").intValue());
		assertTrue("createNumber(String) 10a failed", -0xFADE == Numbers.createNumber("-0xFADE").intValue());
		assertTrue("createNumber(String) 10b failed", -0xFADE == Numbers.createNumber("-0Xfade").intValue());
		assertEquals("createNumber(String) 11 failed", Double.valueOf("1.1E200"), Numbers.createNumber("1.1E200"));
		assertEquals("createNumber(String) 12 failed", Float.valueOf("1.1E20"), Numbers.createNumber("1.1E20"));
		assertEquals("createNumber(String) 13 failed", Double.valueOf("-1.1E200"), Numbers.createNumber("-1.1E200"));
		assertEquals("createNumber(String) 14 failed", Double.valueOf("1.1E-200"), Numbers.createNumber("1.1E-200"));
		assertEquals("createNumber(null) failed", null, Numbers.createNumber(null));
		assertEquals("createNumber(String) failed", new BigInteger("12345678901234567890"),
			Numbers.createNumber("12345678901234567890L"));

		assertEquals("createNumber(String) 15 failed", new BigDecimal("1.1E-700"),
			Numbers.createNumber("1.1E-700F"));

		assertEquals("createNumber(String) 16 failed", Long.valueOf("10" + Integer.MAX_VALUE),
			Numbers.createNumber("10" + Integer.MAX_VALUE + "L"));
		assertEquals("createNumber(String) 17 failed", Long.valueOf("10" + Integer.MAX_VALUE),
			Numbers.createNumber("10" + Integer.MAX_VALUE));
		assertEquals("createNumber(String) 18 failed", new BigInteger("10" + Long.MAX_VALUE),
			Numbers.createNumber("10" + Long.MAX_VALUE));

		// LANG-521
		assertEquals("createNumber(String) LANG-521 failed", Float.valueOf("2."), Numbers.createNumber("2."));

		// LANG-638
		assertFalse("createNumber(String) succeeded", checkCreateNumber("1eE"));

		// LANG-693
		assertEquals("createNumber(String) LANG-693 failed", Double.valueOf(Double.MAX_VALUE),
			Numbers.createNumber("" + Double.MAX_VALUE));

		// LANG-822
		// ensure that the underlying negative number would create a BigDecimal
		final Number bigNum = Numbers.createNumber("-1.1E-700F");
		assertNotNull(bigNum);
		assertEquals(BigDecimal.class, bigNum.getClass());
	}

	@Test
	public void TestLang747() {
		assertEquals(Integer.valueOf(0x8000), Numbers.createNumber("0x8000"));
		assertEquals(Integer.valueOf(0x80000), Numbers.createNumber("0x80000"));
		assertEquals(Integer.valueOf(0x800000), Numbers.createNumber("0x800000"));
		assertEquals(Integer.valueOf(0x8000000), Numbers.createNumber("0x8000000"));
		assertEquals(Integer.valueOf(0x7FFFFFFF), Numbers.createNumber("0x7FFFFFFF"));
		assertEquals(Long.valueOf(0x80000000L), Numbers.createNumber("0x80000000"));
		assertEquals(Long.valueOf(0xFFFFFFFFL), Numbers.createNumber("0xFFFFFFFF"));

		// Leading zero tests
		assertEquals(Integer.valueOf(0x8000000), Numbers.createNumber("0x08000000"));
		assertEquals(Integer.valueOf(0x7FFFFFFF), Numbers.createNumber("0x007FFFFFFF"));
		assertEquals(Long.valueOf(0x80000000L), Numbers.createNumber("0x080000000"));
		assertEquals(Long.valueOf(0xFFFFFFFFL), Numbers.createNumber("0x00FFFFFFFF"));

		assertEquals(Long.valueOf(0x800000000L), Numbers.createNumber("0x800000000"));
		assertEquals(Long.valueOf(0x8000000000L), Numbers.createNumber("0x8000000000"));
		assertEquals(Long.valueOf(0x80000000000L), Numbers.createNumber("0x80000000000"));
		assertEquals(Long.valueOf(0x800000000000L), Numbers.createNumber("0x800000000000"));
		assertEquals(Long.valueOf(0x8000000000000L), Numbers.createNumber("0x8000000000000"));
		assertEquals(Long.valueOf(0x80000000000000L), Numbers.createNumber("0x80000000000000"));
		assertEquals(Long.valueOf(0x800000000000000L), Numbers.createNumber("0x800000000000000"));
		assertEquals(Long.valueOf(0x7FFFFFFFFFFFFFFFL), Numbers.createNumber("0x7FFFFFFFFFFFFFFF"));
		// N.B. Cannot use a hex constant such as 0x8000000000000000L here as that is interpreted as
		// a negative long
		assertEquals(new BigInteger("8000000000000000", 16), Numbers.createNumber("0x8000000000000000"));
		assertEquals(new BigInteger("FFFFFFFFFFFFFFFF", 16), Numbers.createNumber("0xFFFFFFFFFFFFFFFF"));

		// Leading zero tests
		assertEquals(Long.valueOf(0x80000000000000L), Numbers.createNumber("0x00080000000000000"));
		assertEquals(Long.valueOf(0x800000000000000L), Numbers.createNumber("0x0800000000000000"));
		assertEquals(Long.valueOf(0x7FFFFFFFFFFFFFFFL), Numbers.createNumber("0x07FFFFFFFFFFFFFFF"));
		// N.B. Cannot use a hex constant such as 0x8000000000000000L here as that is interpreted as
		// a negative long
		assertEquals(new BigInteger("8000000000000000", 16), Numbers.createNumber("0x00008000000000000000"));
		assertEquals(new BigInteger("FFFFFFFFFFFFFFFF", 16), Numbers.createNumber("0x0FFFFFFFFFFFFFFFF"));
	}

	@Test(expected = NumberFormatException.class)
	// Check that the code fails to create a valid number when preceeded by -- rather than -
	public void testCreateNumberFailure_1() {
		Numbers.createNumber("--1.1E-700F");
	}

	@Test(expected = NumberFormatException.class)
	// Check that the code fails to create a valid number when both e and E are present (with
	// decimal)
	public void testCreateNumberFailure_2() {
		Numbers.createNumber("-1.1E+0-7e00");
	}

	@Test(expected = NumberFormatException.class)
	// Check that the code fails to create a valid number when both e and E are present (no decimal)
	public void testCreateNumberFailure_3() {
		Numbers.createNumber("-11E+0-7e00");
	}

	@Test(expected = NumberFormatException.class)
	// Check that the code fails to create a valid number when both e and E are present (no decimal)
	public void testCreateNumberFailure_4() {
		Numbers.createNumber("1eE+00001");
	}

	// Tests to show when magnitude causes switch to next Number type
	// Will probably need to be adjusted if code is changed to check precision (LANG-693)
	@Test
	public void testCreateNumberMagnitude() {
		// Test Float.MAX_VALUE, and same with +1 in final digit to check conversion changes to next
		// Number type
		assertEquals(Float.valueOf(Float.MAX_VALUE), Numbers.createNumber("3.4028235e+38"));
		assertEquals(Double.valueOf(3.4028236e+38), Numbers.createNumber("3.4028236e+38"));

		// Test Double.MAX_VALUE
		assertEquals(Double.valueOf(Double.MAX_VALUE), Numbers.createNumber("1.7976931348623157e+308"));
		// Test with +2 in final digit (+1 does not cause roll-over to BigDecimal)
		assertEquals(new BigDecimal("1.7976931348623159e+308"), Numbers.createNumber("1.7976931348623159e+308"));

		assertEquals(Integer.valueOf(0x12345678), Numbers.createNumber("0x12345678"));
		assertEquals(Long.valueOf(0x123456789L), Numbers.createNumber("0x123456789"));

		assertEquals(Long.valueOf(0x7fffffffffffffffL), Numbers.createNumber("0x7fffffffffffffff"));
		// Does not appear to be a way to create a literal BigInteger of this magnitude
		assertEquals(new BigInteger("7fffffffffffffff0", 16), Numbers.createNumber("0x7fffffffffffffff0"));

		assertEquals(Long.valueOf(0x7fffffffffffffffL), Numbers.createNumber("#7fffffffffffffff"));
		assertEquals(new BigInteger("7fffffffffffffff0", 16), Numbers.createNumber("#7fffffffffffffff0"));

		assertEquals(Integer.valueOf(017777777777), Numbers.createNumber("017777777777")); // 31
																								// bits
		assertEquals(Long.valueOf(037777777777L), Numbers.createNumber("037777777777")); // 32
																								// bits

		assertEquals(Long.valueOf(0777777777777777777777L), Numbers.createNumber("0777777777777777777777")); // 63
																													// bits
		assertEquals(new BigInteger("1777777777777777777777", 8), Numbers.createNumber("01777777777777777777777"));// 64
																														// bits
	}

	@Test
	public void testCreateFloat() {
		assertEquals("createFloat(String) failed", Float.valueOf("1234.5"), Numbers.createFloat("1234.5"));
		assertEquals("createFloat(null) failed", null, Numbers.createFloat(null));
		this.testCreateFloatFailure("");
		this.testCreateFloatFailure(" ");
		this.testCreateFloatFailure("\b\t\n\f\r");
		// Funky whitespaces
		this.testCreateFloatFailure("\u00A0\uFEFF\u000B\u000C\u001C\u001D\u001E\u001F");
	}

	protected void testCreateFloatFailure(final String str) {
		try {
			final Float value = Numbers.createFloat(str);
			fail("createFloat(\"" + str + "\") should have failed: " + value);
		}
		catch (final NumberFormatException ex) {
			// empty
		}
	}

	@Test
	public void testCreateDouble() {
		assertEquals("createDouble(String) failed", Double.valueOf("1234.5"), Numbers.createDouble("1234.5"));
		assertEquals("createDouble(null) failed", null, Numbers.createDouble(null));
		this.testCreateDoubleFailure("");
		this.testCreateDoubleFailure(" ");
		this.testCreateDoubleFailure("\b\t\n\f\r");
		// Funky whitespaces
		this.testCreateDoubleFailure("\u00A0\uFEFF\u000B\u000C\u001C\u001D\u001E\u001F");
	}

	protected void testCreateDoubleFailure(final String str) {
		try {
			final Double value = Numbers.createDouble(str);
			fail("createDouble(\"" + str + "\") should have failed: " + value);
		}
		catch (final NumberFormatException ex) {
			// empty
		}
	}

	@Test
	public void testCreateInteger() {
		assertEquals("createInteger(String) failed", Integer.valueOf("12345"), Numbers.createInteger("12345"));
		assertEquals("createInteger(null) failed", null, Numbers.createInteger(null));
		this.testCreateIntegerFailure("");
		this.testCreateIntegerFailure(" ");
		this.testCreateIntegerFailure("\b\t\n\f\r");
		// Funky whitespaces
		this.testCreateIntegerFailure("\u00A0\uFEFF\u000B\u000C\u001C\u001D\u001E\u001F");
	}

	protected void testCreateIntegerFailure(final String str) {
		try {
			final Integer value = Numbers.createInteger(str);
			fail("createInteger(\"" + str + "\") should have failed: " + value);
		}
		catch (final NumberFormatException ex) {
			// empty
		}
	}

	@Test
	public void testCreateLong() {
		assertEquals("createLong(String) failed", Long.valueOf("12345"), Numbers.createLong("12345"));
		assertEquals("createLong(null) failed", null, Numbers.createLong(null));
		this.testCreateLongFailure("");
		this.testCreateLongFailure(" ");
		this.testCreateLongFailure("\b\t\n\f\r");
		// Funky whitespaces
		this.testCreateLongFailure("\u00A0\uFEFF\u000B\u000C\u001C\u001D\u001E\u001F");
	}

	protected void testCreateLongFailure(final String str) {
		try {
			final Long value = Numbers.createLong(str);
			fail("createLong(\"" + str + "\") should have failed: " + value);
		}
		catch (final NumberFormatException ex) {
			// empty
		}
	}

	@Test
	public void testCreateBigInteger() {
		assertEquals("createBigInteger(String) failed", new BigInteger("12345"), Numbers.createBigInteger("12345"));
		assertEquals("createBigInteger(null) failed", null, Numbers.createBigInteger(null));
		this.testCreateBigIntegerFailure("");
		this.testCreateBigIntegerFailure(" ");
		this.testCreateBigIntegerFailure("\b\t\n\f\r");
		// Funky whitespaces
		this.testCreateBigIntegerFailure("\u00A0\uFEFF\u000B\u000C\u001C\u001D\u001E\u001F");
		assertEquals("createBigInteger(String) failed", new BigInteger("255"), Numbers.createBigInteger("0xff"));
		assertEquals("createBigInteger(String) failed", new BigInteger("255"), Numbers.createBigInteger("#ff"));
		assertEquals("createBigInteger(String) failed", new BigInteger("-255"), Numbers.createBigInteger("-0xff"));
		assertEquals("createBigInteger(String) failed", new BigInteger("255"), Numbers.createBigInteger("0377"));
		assertEquals("createBigInteger(String) failed", new BigInteger("-255"), Numbers.createBigInteger("-0377"));
		assertEquals("createBigInteger(String) failed", new BigInteger("-255"), Numbers.createBigInteger("-0377"));
		assertEquals("createBigInteger(String) failed", new BigInteger("-0"), Numbers.createBigInteger("-0"));
		assertEquals("createBigInteger(String) failed", new BigInteger("0"), Numbers.createBigInteger("0"));
		testCreateBigIntegerFailure("#");
		testCreateBigIntegerFailure("-#");
		testCreateBigIntegerFailure("0x");
		testCreateBigIntegerFailure("-0x");
	}

	protected void testCreateBigIntegerFailure(final String str) {
		try {
			final BigInteger value = Numbers.createBigInteger(str);
			fail("createBigInteger(\"" + str + "\") should have failed: " + value);
		}
		catch (final NumberFormatException ex) {
			// empty
		}
	}

	@Test
	public void testCreateBigDecimal() {
		assertEquals("createBigDecimal(String) failed", new BigDecimal("1234.5"),
			Numbers.createBigDecimal("1234.5"));
		assertEquals("createBigDecimal(null) failed", null, Numbers.createBigDecimal(null));
		this.testCreateBigDecimalFailure("");
		this.testCreateBigDecimalFailure(" ");
		this.testCreateBigDecimalFailure("\b\t\n\f\r");
		// Funky whitespaces
		this.testCreateBigDecimalFailure("\u00A0\uFEFF\u000B\u000C\u001C\u001D\u001E\u001F");
		this.testCreateBigDecimalFailure("-"); // sign alone not valid
		this.testCreateBigDecimalFailure("--"); // comment in Numbers suggests some
												// implementations may incorrectly allow this
		this.testCreateBigDecimalFailure("--0");
		this.testCreateBigDecimalFailure("+"); // sign alone not valid
		this.testCreateBigDecimalFailure("++"); // in case this was also allowed by some JVMs
		this.testCreateBigDecimalFailure("++0");
	}

	protected void testCreateBigDecimalFailure(final String str) {
		try {
			final BigDecimal value = Numbers.createBigDecimal(str);
			fail("createBigDecimal(\"" + str + "\") should have failed: " + value);
		}
		catch (final NumberFormatException ex) {
			// empty
		}
	}

	// min/max tests
	// ----------------------------------------------------------------------
	@Test(expected = IllegalArgumentException.class)
	public void testMinLong_nullArray() {
		Numbers.min((long[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinLong_emptyArray() {
		Numbers.min(new long[0]);
	}

	@Test
	public void testMinLong() {
		assertEquals("min(long[]) failed for array length 1", 5, Numbers.min(new long[] { 5 }));

		assertEquals("min(long[]) failed for array length 2", 6, Numbers.min(new long[] { 6, 9 }));

		assertEquals(-10, Numbers.min(new long[] { -10, -5, 0, 5, 10 }));
		assertEquals(-10, Numbers.min(new long[] { -5, 0, -10, 5, 10 }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinInt_nullArray() {
		Numbers.min((int[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinInt_emptyArray() {
		Numbers.min(new int[0]);
	}

	@Test
	public void testMinInt() {
		assertEquals("min(int[]) failed for array length 1", 5, Numbers.min(new int[] { 5 }));

		assertEquals("min(int[]) failed for array length 2", 6, Numbers.min(new int[] { 6, 9 }));

		assertEquals(-10, Numbers.min(new int[] { -10, -5, 0, 5, 10 }));
		assertEquals(-10, Numbers.min(new int[] { -5, 0, -10, 5, 10 }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinShort_nullArray() {
		Numbers.min((short[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinShort_emptyArray() {
		Numbers.min(new short[0]);
	}

	@Test
	public void testMinShort() {
		assertEquals("min(short[]) failed for array length 1", 5, Numbers.min(new short[] { 5 }));

		assertEquals("min(short[]) failed for array length 2", 6, Numbers.min(new short[] { 6, 9 }));

		assertEquals(-10, Numbers.min(new short[] { -10, -5, 0, 5, 10 }));
		assertEquals(-10, Numbers.min(new short[] { -5, 0, -10, 5, 10 }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinByte_nullArray() {
		Numbers.min((byte[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinByte_emptyArray() {
		Numbers.min(new byte[0]);
	}

	@Test
	public void testMinByte() {
		assertEquals("min(byte[]) failed for array length 1", 5, Numbers.min(new byte[] { 5 }));

		assertEquals("min(byte[]) failed for array length 2", 6, Numbers.min(new byte[] { 6, 9 }));

		assertEquals(-10, Numbers.min(new byte[] { -10, -5, 0, 5, 10 }));
		assertEquals(-10, Numbers.min(new byte[] { -5, 0, -10, 5, 10 }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinDouble_nullArray() {
		Numbers.min((double[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinDouble_emptyArray() {
		Numbers.min(new double[0]);
	}

	@Test
	public void testMinDouble() {
		assertEquals("min(double[]) failed for array length 1", 5.12, Numbers.min(new double[] { 5.12 }), 0);

		assertEquals("min(double[]) failed for array length 2", 6.23, Numbers.min(new double[] { 6.23, 9.34 }), 0);

		assertEquals("min(double[]) failed for array length 5", -10.45,
			Numbers.min(new double[] { -10.45, -5.56, 0, 5.67, 10.78 }), 0);
		assertEquals(-10, Numbers.min(new double[] { -10, -5, 0, 5, 10 }), 0.0001);
		assertEquals(-10, Numbers.min(new double[] { -5, 0, -10, 5, 10 }), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinFloat_nullArray() {
		Numbers.min((float[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMinFloat_emptyArray() {
		Numbers.min(new float[0]);
	}

	@Test
	public void testMinFloat() {
		assertEquals("min(float[]) failed for array length 1", 5.9f, Numbers.min(new float[] { 5.9f }), 0);

		assertEquals("min(float[]) failed for array length 2", 6.8f, Numbers.min(new float[] { 6.8f, 9.7f }), 0);

		assertEquals("min(float[]) failed for array length 5", -10.6f,
			Numbers.min(new float[] { -10.6f, -5.5f, 0, 5.4f, 10.3f }), 0);
		assertEquals(-10, Numbers.min(new float[] { -10, -5, 0, 5, 10 }), 0.0001f);
		assertEquals(-10, Numbers.min(new float[] { -5, 0, -10, 5, 10 }), 0.0001f);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxLong_nullArray() {
		Numbers.max((long[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxLong_emptyArray() {
		Numbers.max(new long[0]);
	}

	@Test
	public void testMaxLong() {
		assertEquals("max(long[]) failed for array length 1", 5, Numbers.max(new long[] { 5 }));

		assertEquals("max(long[]) failed for array length 2", 9, Numbers.max(new long[] { 6, 9 }));

		assertEquals("max(long[]) failed for array length 5", 10, Numbers.max(new long[] { -10, -5, 0, 5, 10 }));
		assertEquals(10, Numbers.max(new long[] { -10, -5, 0, 5, 10 }));
		assertEquals(10, Numbers.max(new long[] { -5, 0, 10, 5, -10 }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxInt_nullArray() {
		Numbers.max((int[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxInt_emptyArray() {
		Numbers.max(new int[0]);
	}

	@Test
	public void testMaxInt() {
		assertEquals("max(int[]) failed for array length 1", 5, Numbers.max(new int[] { 5 }));

		assertEquals("max(int[]) failed for array length 2", 9, Numbers.max(new int[] { 6, 9 }));

		assertEquals("max(int[]) failed for array length 5", 10, Numbers.max(new int[] { -10, -5, 0, 5, 10 }));
		assertEquals(10, Numbers.max(new int[] { -10, -5, 0, 5, 10 }));
		assertEquals(10, Numbers.max(new int[] { -5, 0, 10, 5, -10 }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxShort_nullArray() {
		Numbers.max((short[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxShort_emptyArray() {
		Numbers.max(new short[0]);
	}

	@Test
	public void testMaxShort() {
		assertEquals("max(short[]) failed for array length 1", 5, Numbers.max(new short[] { 5 }));

		assertEquals("max(short[]) failed for array length 2", 9, Numbers.max(new short[] { 6, 9 }));

		assertEquals("max(short[]) failed for array length 5", 10, Numbers.max(new short[] { -10, -5, 0, 5, 10 }));
		assertEquals(10, Numbers.max(new short[] { -10, -5, 0, 5, 10 }));
		assertEquals(10, Numbers.max(new short[] { -5, 0, 10, 5, -10 }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxByte_nullArray() {
		Numbers.max((byte[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxByte_emptyArray() {
		Numbers.max(new byte[0]);
	}

	@Test
	public void testMaxByte() {
		assertEquals("max(byte[]) failed for array length 1", 5, Numbers.max(new byte[] { 5 }));

		assertEquals("max(byte[]) failed for array length 2", 9, Numbers.max(new byte[] { 6, 9 }));

		assertEquals("max(byte[]) failed for array length 5", 10, Numbers.max(new byte[] { -10, -5, 0, 5, 10 }));
		assertEquals(10, Numbers.max(new byte[] { -10, -5, 0, 5, 10 }));
		assertEquals(10, Numbers.max(new byte[] { -5, 0, 10, 5, -10 }));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxDouble_nullArray() {
		Numbers.max((double[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxDouble_emptyArray() {
		Numbers.max(new double[0]);
	}

	@Test
	public void testMaxDouble() {
		final double[] d = null;
		try {
			Numbers.max(d);
			fail("No exception was thrown for null input.");
		}
		catch (final IllegalArgumentException ex) {
		}

		try {
			Numbers.max(new double[0]);
			fail("No exception was thrown for empty input.");
		}
		catch (final IllegalArgumentException ex) {
		}

		assertEquals("max(double[]) failed for array length 1", 5.1f, Numbers.max(new double[] { 5.1f }), 0);

		assertEquals("max(double[]) failed for array length 2", 9.2f, Numbers.max(new double[] { 6.3f, 9.2f }), 0);

		assertEquals("max(double[]) failed for float length 5", 10.4f,
			Numbers.max(new double[] { -10.5f, -5.6f, 0, 5.7f, 10.4f }), 0);
		assertEquals(10, Numbers.max(new double[] { -10, -5, 0, 5, 10 }), 0.0001);
		assertEquals(10, Numbers.max(new double[] { -5, 0, 10, 5, -10 }), 0.0001);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxFloat_nullArray() {
		Numbers.max((float[])null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMaxFloat_emptyArray() {
		Numbers.max(new float[0]);
	}

	@Test
	public void testMaxFloat() {
		assertEquals("max(float[]) failed for array length 1", 5.1f, Numbers.max(new float[] { 5.1f }), 0);

		assertEquals("max(float[]) failed for array length 2", 9.2f, Numbers.max(new float[] { 6.3f, 9.2f }), 0);

		assertEquals("max(float[]) failed for float length 5", 10.4f,
			Numbers.max(new float[] { -10.5f, -5.6f, 0, 5.7f, 10.4f }), 0);
		assertEquals(10, Numbers.max(new float[] { -10, -5, 0, 5, 10 }), 0.0001f);
		assertEquals(10, Numbers.max(new float[] { -5, 0, 10, 5, -10 }), 0.0001f);
	}

	@Test
	public void testMinimumLong() {
		assertEquals("minimum(long,long,long) 1 failed", 12345L, Numbers.min(12345L, 12345L + 1L, 12345L + 2L));
		assertEquals("minimum(long,long,long) 2 failed", 12345L, Numbers.min(12345L + 1L, 12345L, 12345 + 2L));
		assertEquals("minimum(long,long,long) 3 failed", 12345L, Numbers.min(12345L + 1L, 12345L + 2L, 12345L));
		assertEquals("minimum(long,long,long) 4 failed", 12345L, Numbers.min(12345L + 1L, 12345L, 12345L));
		assertEquals("minimum(long,long,long) 5 failed", 12345L, Numbers.min(12345L, 12345L, 12345L));
	}

	@Test
	public void testMinimumInt() {
		assertEquals("minimum(int,int,int) 1 failed", 12345, Numbers.min(12345, 12345 + 1, 12345 + 2));
		assertEquals("minimum(int,int,int) 2 failed", 12345, Numbers.min(12345 + 1, 12345, 12345 + 2));
		assertEquals("minimum(int,int,int) 3 failed", 12345, Numbers.min(12345 + 1, 12345 + 2, 12345));
		assertEquals("minimum(int,int,int) 4 failed", 12345, Numbers.min(12345 + 1, 12345, 12345));
		assertEquals("minimum(int,int,int) 5 failed", 12345, Numbers.min(12345, 12345, 12345));
	}

	@Test
	public void testMinimumShort() {
		final short low = 1234;
		final short mid = 1234 + 1;
		final short high = 1234 + 2;
		assertEquals("minimum(short,short,short) 1 failed", low, Numbers.min(low, mid, high));
		assertEquals("minimum(short,short,short) 1 failed", low, Numbers.min(mid, low, high));
		assertEquals("minimum(short,short,short) 1 failed", low, Numbers.min(mid, high, low));
		assertEquals("minimum(short,short,short) 1 failed", low, Numbers.min(low, mid, low));
	}

	@Test
	public void testMinimumByte() {
		final byte low = 123;
		final byte mid = 123 + 1;
		final byte high = 123 + 2;
		assertEquals("minimum(byte,byte,byte) 1 failed", low, Numbers.min(low, mid, high));
		assertEquals("minimum(byte,byte,byte) 1 failed", low, Numbers.min(mid, low, high));
		assertEquals("minimum(byte,byte,byte) 1 failed", low, Numbers.min(mid, high, low));
		assertEquals("minimum(byte,byte,byte) 1 failed", low, Numbers.min(low, mid, low));
	}

	@Test
	public void testMinimumDouble() {
		final double low = 12.3;
		final double mid = 12.3 + 1;
		final double high = 12.3 + 2;
		assertEquals(low, Numbers.min(low, mid, high), 0.0001);
		assertEquals(low, Numbers.min(mid, low, high), 0.0001);
		assertEquals(low, Numbers.min(mid, high, low), 0.0001);
		assertEquals(low, Numbers.min(low, mid, low), 0.0001);
		assertEquals(mid, Numbers.min(high, mid, high), 0.0001);
	}

	@Test
	public void testMinimumFloat() {
		final float low = 12.3f;
		final float mid = 12.3f + 1;
		final float high = 12.3f + 2;
		assertEquals(low, Numbers.min(low, mid, high), 0.0001f);
		assertEquals(low, Numbers.min(mid, low, high), 0.0001f);
		assertEquals(low, Numbers.min(mid, high, low), 0.0001f);
		assertEquals(low, Numbers.min(low, mid, low), 0.0001f);
		assertEquals(mid, Numbers.min(high, mid, high), 0.0001f);
	}

	@Test
	public void testMaximumLong() {
		assertEquals("maximum(long,long,long) 1 failed", 12345L, Numbers.max(12345L, 12345L - 1L, 12345L - 2L));
		assertEquals("maximum(long,long,long) 2 failed", 12345L, Numbers.max(12345L - 1L, 12345L, 12345L - 2L));
		assertEquals("maximum(long,long,long) 3 failed", 12345L, Numbers.max(12345L - 1L, 12345L - 2L, 12345L));
		assertEquals("maximum(long,long,long) 4 failed", 12345L, Numbers.max(12345L - 1L, 12345L, 12345L));
		assertEquals("maximum(long,long,long) 5 failed", 12345L, Numbers.max(12345L, 12345L, 12345L));
	}

	@Test
	public void testMaximumInt() {
		assertEquals("maximum(int,int,int) 1 failed", 12345, Numbers.max(12345, 12345 - 1, 12345 - 2));
		assertEquals("maximum(int,int,int) 2 failed", 12345, Numbers.max(12345 - 1, 12345, 12345 - 2));
		assertEquals("maximum(int,int,int) 3 failed", 12345, Numbers.max(12345 - 1, 12345 - 2, 12345));
		assertEquals("maximum(int,int,int) 4 failed", 12345, Numbers.max(12345 - 1, 12345, 12345));
		assertEquals("maximum(int,int,int) 5 failed", 12345, Numbers.max(12345, 12345, 12345));
	}

	@Test
	public void testMaximumShort() {
		final short low = 1234;
		final short mid = 1234 + 1;
		final short high = 1234 + 2;
		assertEquals("maximum(short,short,short) 1 failed", high, Numbers.max(low, mid, high));
		assertEquals("maximum(short,short,short) 1 failed", high, Numbers.max(mid, low, high));
		assertEquals("maximum(short,short,short) 1 failed", high, Numbers.max(mid, high, low));
		assertEquals("maximum(short,short,short) 1 failed", high, Numbers.max(high, mid, high));
	}

	@Test
	public void testMaximumByte() {
		final byte low = 123;
		final byte mid = 123 + 1;
		final byte high = 123 + 2;
		assertEquals("maximum(byte,byte,byte) 1 failed", high, Numbers.max(low, mid, high));
		assertEquals("maximum(byte,byte,byte) 1 failed", high, Numbers.max(mid, low, high));
		assertEquals("maximum(byte,byte,byte) 1 failed", high, Numbers.max(mid, high, low));
		assertEquals("maximum(byte,byte,byte) 1 failed", high, Numbers.max(high, mid, high));
	}

	@Test
	public void testMaximumDouble() {
		final double low = 12.3;
		final double mid = 12.3 + 1;
		final double high = 12.3 + 2;
		assertEquals(high, Numbers.max(low, mid, high), 0.0001);
		assertEquals(high, Numbers.max(mid, low, high), 0.0001);
		assertEquals(high, Numbers.max(mid, high, low), 0.0001);
		assertEquals(mid, Numbers.max(low, mid, low), 0.0001);
		assertEquals(high, Numbers.max(high, mid, high), 0.0001);
	}

	@Test
	public void testMaximumFloat() {
		final float low = 12.3f;
		final float mid = 12.3f + 1;
		final float high = 12.3f + 2;
		assertEquals(high, Numbers.max(low, mid, high), 0.0001f);
		assertEquals(high, Numbers.max(mid, low, high), 0.0001f);
		assertEquals(high, Numbers.max(mid, high, low), 0.0001f);
		assertEquals(mid, Numbers.max(low, mid, low), 0.0001f);
		assertEquals(high, Numbers.max(high, mid, high), 0.0001f);
	}

	// Testing JDK against old Lang functionality
	@Test
	public void testCompareDouble() {
		assertTrue(Double.compare(Double.NaN, Double.NaN) == 0);
		assertTrue(Double.compare(Double.NaN, Double.POSITIVE_INFINITY) == +1);
		assertTrue(Double.compare(Double.NaN, Double.MAX_VALUE) == +1);
		assertTrue(Double.compare(Double.NaN, 1.2d) == +1);
		assertTrue(Double.compare(Double.NaN, 0.0d) == +1);
		assertTrue(Double.compare(Double.NaN, -0.0d) == +1);
		assertTrue(Double.compare(Double.NaN, -1.2d) == +1);
		assertTrue(Double.compare(Double.NaN, -Double.MAX_VALUE) == +1);
		assertTrue(Double.compare(Double.NaN, Double.NEGATIVE_INFINITY) == +1);

		assertTrue(Double.compare(Double.POSITIVE_INFINITY, Double.NaN) == -1);
		assertTrue(Double.compare(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY) == 0);
		assertTrue(Double.compare(Double.POSITIVE_INFINITY, Double.MAX_VALUE) == +1);
		assertTrue(Double.compare(Double.POSITIVE_INFINITY, 1.2d) == +1);
		assertTrue(Double.compare(Double.POSITIVE_INFINITY, 0.0d) == +1);
		assertTrue(Double.compare(Double.POSITIVE_INFINITY, -0.0d) == +1);
		assertTrue(Double.compare(Double.POSITIVE_INFINITY, -1.2d) == +1);
		assertTrue(Double.compare(Double.POSITIVE_INFINITY, -Double.MAX_VALUE) == +1);
		assertTrue(Double.compare(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY) == +1);

		assertTrue(Double.compare(Double.MAX_VALUE, Double.NaN) == -1);
		assertTrue(Double.compare(Double.MAX_VALUE, Double.POSITIVE_INFINITY) == -1);
		assertTrue(Double.compare(Double.MAX_VALUE, Double.MAX_VALUE) == 0);
		assertTrue(Double.compare(Double.MAX_VALUE, 1.2d) == +1);
		assertTrue(Double.compare(Double.MAX_VALUE, 0.0d) == +1);
		assertTrue(Double.compare(Double.MAX_VALUE, -0.0d) == +1);
		assertTrue(Double.compare(Double.MAX_VALUE, -1.2d) == +1);
		assertTrue(Double.compare(Double.MAX_VALUE, -Double.MAX_VALUE) == +1);
		assertTrue(Double.compare(Double.MAX_VALUE, Double.NEGATIVE_INFINITY) == +1);

		assertTrue(Double.compare(1.2d, Double.NaN) == -1);
		assertTrue(Double.compare(1.2d, Double.POSITIVE_INFINITY) == -1);
		assertTrue(Double.compare(1.2d, Double.MAX_VALUE) == -1);
		assertTrue(Double.compare(1.2d, 1.2d) == 0);
		assertTrue(Double.compare(1.2d, 0.0d) == +1);
		assertTrue(Double.compare(1.2d, -0.0d) == +1);
		assertTrue(Double.compare(1.2d, -1.2d) == +1);
		assertTrue(Double.compare(1.2d, -Double.MAX_VALUE) == +1);
		assertTrue(Double.compare(1.2d, Double.NEGATIVE_INFINITY) == +1);

		assertTrue(Double.compare(0.0d, Double.NaN) == -1);
		assertTrue(Double.compare(0.0d, Double.POSITIVE_INFINITY) == -1);
		assertTrue(Double.compare(0.0d, Double.MAX_VALUE) == -1);
		assertTrue(Double.compare(0.0d, 1.2d) == -1);
		assertTrue(Double.compare(0.0d, 0.0d) == 0);
		assertTrue(Double.compare(0.0d, -0.0d) == +1);
		assertTrue(Double.compare(0.0d, -1.2d) == +1);
		assertTrue(Double.compare(0.0d, -Double.MAX_VALUE) == +1);
		assertTrue(Double.compare(0.0d, Double.NEGATIVE_INFINITY) == +1);

		assertTrue(Double.compare(-0.0d, Double.NaN) == -1);
		assertTrue(Double.compare(-0.0d, Double.POSITIVE_INFINITY) == -1);
		assertTrue(Double.compare(-0.0d, Double.MAX_VALUE) == -1);
		assertTrue(Double.compare(-0.0d, 1.2d) == -1);
		assertTrue(Double.compare(-0.0d, 0.0d) == -1);
		assertTrue(Double.compare(-0.0d, -0.0d) == 0);
		assertTrue(Double.compare(-0.0d, -1.2d) == +1);
		assertTrue(Double.compare(-0.0d, -Double.MAX_VALUE) == +1);
		assertTrue(Double.compare(-0.0d, Double.NEGATIVE_INFINITY) == +1);

		assertTrue(Double.compare(-1.2d, Double.NaN) == -1);
		assertTrue(Double.compare(-1.2d, Double.POSITIVE_INFINITY) == -1);
		assertTrue(Double.compare(-1.2d, Double.MAX_VALUE) == -1);
		assertTrue(Double.compare(-1.2d, 1.2d) == -1);
		assertTrue(Double.compare(-1.2d, 0.0d) == -1);
		assertTrue(Double.compare(-1.2d, -0.0d) == -1);
		assertTrue(Double.compare(-1.2d, -1.2d) == 0);
		assertTrue(Double.compare(-1.2d, -Double.MAX_VALUE) == +1);
		assertTrue(Double.compare(-1.2d, Double.NEGATIVE_INFINITY) == +1);

		assertTrue(Double.compare(-Double.MAX_VALUE, Double.NaN) == -1);
		assertTrue(Double.compare(-Double.MAX_VALUE, Double.POSITIVE_INFINITY) == -1);
		assertTrue(Double.compare(-Double.MAX_VALUE, Double.MAX_VALUE) == -1);
		assertTrue(Double.compare(-Double.MAX_VALUE, 1.2d) == -1);
		assertTrue(Double.compare(-Double.MAX_VALUE, 0.0d) == -1);
		assertTrue(Double.compare(-Double.MAX_VALUE, -0.0d) == -1);
		assertTrue(Double.compare(-Double.MAX_VALUE, -1.2d) == -1);
		assertTrue(Double.compare(-Double.MAX_VALUE, -Double.MAX_VALUE) == 0);
		assertTrue(Double.compare(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY) == +1);

		assertTrue(Double.compare(Double.NEGATIVE_INFINITY, Double.NaN) == -1);
		assertTrue(Double.compare(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY) == -1);
		assertTrue(Double.compare(Double.NEGATIVE_INFINITY, Double.MAX_VALUE) == -1);
		assertTrue(Double.compare(Double.NEGATIVE_INFINITY, 1.2d) == -1);
		assertTrue(Double.compare(Double.NEGATIVE_INFINITY, 0.0d) == -1);
		assertTrue(Double.compare(Double.NEGATIVE_INFINITY, -0.0d) == -1);
		assertTrue(Double.compare(Double.NEGATIVE_INFINITY, -1.2d) == -1);
		assertTrue(Double.compare(Double.NEGATIVE_INFINITY, -Double.MAX_VALUE) == -1);
		assertTrue(Double.compare(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY) == 0);
	}

	@Test
	public void testCompareFloat() {
		assertTrue(Float.compare(Float.NaN, Float.NaN) == 0);
		assertTrue(Float.compare(Float.NaN, Float.POSITIVE_INFINITY) == +1);
		assertTrue(Float.compare(Float.NaN, Float.MAX_VALUE) == +1);
		assertTrue(Float.compare(Float.NaN, 1.2f) == +1);
		assertTrue(Float.compare(Float.NaN, 0.0f) == +1);
		assertTrue(Float.compare(Float.NaN, -0.0f) == +1);
		assertTrue(Float.compare(Float.NaN, -1.2f) == +1);
		assertTrue(Float.compare(Float.NaN, -Float.MAX_VALUE) == +1);
		assertTrue(Float.compare(Float.NaN, Float.NEGATIVE_INFINITY) == +1);

		assertTrue(Float.compare(Float.POSITIVE_INFINITY, Float.NaN) == -1);
		assertTrue(Float.compare(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY) == 0);
		assertTrue(Float.compare(Float.POSITIVE_INFINITY, Float.MAX_VALUE) == +1);
		assertTrue(Float.compare(Float.POSITIVE_INFINITY, 1.2f) == +1);
		assertTrue(Float.compare(Float.POSITIVE_INFINITY, 0.0f) == +1);
		assertTrue(Float.compare(Float.POSITIVE_INFINITY, -0.0f) == +1);
		assertTrue(Float.compare(Float.POSITIVE_INFINITY, -1.2f) == +1);
		assertTrue(Float.compare(Float.POSITIVE_INFINITY, -Float.MAX_VALUE) == +1);
		assertTrue(Float.compare(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY) == +1);

		assertTrue(Float.compare(Float.MAX_VALUE, Float.NaN) == -1);
		assertTrue(Float.compare(Float.MAX_VALUE, Float.POSITIVE_INFINITY) == -1);
		assertTrue(Float.compare(Float.MAX_VALUE, Float.MAX_VALUE) == 0);
		assertTrue(Float.compare(Float.MAX_VALUE, 1.2f) == +1);
		assertTrue(Float.compare(Float.MAX_VALUE, 0.0f) == +1);
		assertTrue(Float.compare(Float.MAX_VALUE, -0.0f) == +1);
		assertTrue(Float.compare(Float.MAX_VALUE, -1.2f) == +1);
		assertTrue(Float.compare(Float.MAX_VALUE, -Float.MAX_VALUE) == +1);
		assertTrue(Float.compare(Float.MAX_VALUE, Float.NEGATIVE_INFINITY) == +1);

		assertTrue(Float.compare(1.2f, Float.NaN) == -1);
		assertTrue(Float.compare(1.2f, Float.POSITIVE_INFINITY) == -1);
		assertTrue(Float.compare(1.2f, Float.MAX_VALUE) == -1);
		assertTrue(Float.compare(1.2f, 1.2f) == 0);
		assertTrue(Float.compare(1.2f, 0.0f) == +1);
		assertTrue(Float.compare(1.2f, -0.0f) == +1);
		assertTrue(Float.compare(1.2f, -1.2f) == +1);
		assertTrue(Float.compare(1.2f, -Float.MAX_VALUE) == +1);
		assertTrue(Float.compare(1.2f, Float.NEGATIVE_INFINITY) == +1);

		assertTrue(Float.compare(0.0f, Float.NaN) == -1);
		assertTrue(Float.compare(0.0f, Float.POSITIVE_INFINITY) == -1);
		assertTrue(Float.compare(0.0f, Float.MAX_VALUE) == -1);
		assertTrue(Float.compare(0.0f, 1.2f) == -1);
		assertTrue(Float.compare(0.0f, 0.0f) == 0);
		assertTrue(Float.compare(0.0f, -0.0f) == +1);
		assertTrue(Float.compare(0.0f, -1.2f) == +1);
		assertTrue(Float.compare(0.0f, -Float.MAX_VALUE) == +1);
		assertTrue(Float.compare(0.0f, Float.NEGATIVE_INFINITY) == +1);

		assertTrue(Float.compare(-0.0f, Float.NaN) == -1);
		assertTrue(Float.compare(-0.0f, Float.POSITIVE_INFINITY) == -1);
		assertTrue(Float.compare(-0.0f, Float.MAX_VALUE) == -1);
		assertTrue(Float.compare(-0.0f, 1.2f) == -1);
		assertTrue(Float.compare(-0.0f, 0.0f) == -1);
		assertTrue(Float.compare(-0.0f, -0.0f) == 0);
		assertTrue(Float.compare(-0.0f, -1.2f) == +1);
		assertTrue(Float.compare(-0.0f, -Float.MAX_VALUE) == +1);
		assertTrue(Float.compare(-0.0f, Float.NEGATIVE_INFINITY) == +1);

		assertTrue(Float.compare(-1.2f, Float.NaN) == -1);
		assertTrue(Float.compare(-1.2f, Float.POSITIVE_INFINITY) == -1);
		assertTrue(Float.compare(-1.2f, Float.MAX_VALUE) == -1);
		assertTrue(Float.compare(-1.2f, 1.2f) == -1);
		assertTrue(Float.compare(-1.2f, 0.0f) == -1);
		assertTrue(Float.compare(-1.2f, -0.0f) == -1);
		assertTrue(Float.compare(-1.2f, -1.2f) == 0);
		assertTrue(Float.compare(-1.2f, -Float.MAX_VALUE) == +1);
		assertTrue(Float.compare(-1.2f, Float.NEGATIVE_INFINITY) == +1);

		assertTrue(Float.compare(-Float.MAX_VALUE, Float.NaN) == -1);
		assertTrue(Float.compare(-Float.MAX_VALUE, Float.POSITIVE_INFINITY) == -1);
		assertTrue(Float.compare(-Float.MAX_VALUE, Float.MAX_VALUE) == -1);
		assertTrue(Float.compare(-Float.MAX_VALUE, 1.2f) == -1);
		assertTrue(Float.compare(-Float.MAX_VALUE, 0.0f) == -1);
		assertTrue(Float.compare(-Float.MAX_VALUE, -0.0f) == -1);
		assertTrue(Float.compare(-Float.MAX_VALUE, -1.2f) == -1);
		assertTrue(Float.compare(-Float.MAX_VALUE, -Float.MAX_VALUE) == 0);
		assertTrue(Float.compare(-Float.MAX_VALUE, Float.NEGATIVE_INFINITY) == +1);

		assertTrue(Float.compare(Float.NEGATIVE_INFINITY, Float.NaN) == -1);
		assertTrue(Float.compare(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY) == -1);
		assertTrue(Float.compare(Float.NEGATIVE_INFINITY, Float.MAX_VALUE) == -1);
		assertTrue(Float.compare(Float.NEGATIVE_INFINITY, 1.2f) == -1);
		assertTrue(Float.compare(Float.NEGATIVE_INFINITY, 0.0f) == -1);
		assertTrue(Float.compare(Float.NEGATIVE_INFINITY, -0.0f) == -1);
		assertTrue(Float.compare(Float.NEGATIVE_INFINITY, -1.2f) == -1);
		assertTrue(Float.compare(Float.NEGATIVE_INFINITY, -Float.MAX_VALUE) == -1);
		assertTrue(Float.compare(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY) == 0);
	}

	@Test
	public void testIsDigits() {
		assertFalse("isDigits(null) failed", Numbers.isDigits(null));
		assertFalse("isDigits('') failed", Numbers.isDigits(""));
		assertTrue("isDigits(String) failed", Numbers.isDigits("12345"));
		assertFalse("isDigits(String) neg 1 failed", Numbers.isDigits("1234.5"));
		assertFalse("isDigits(String) neg 3 failed", Numbers.isDigits("1ab"));
		assertFalse("isDigits(String) neg 4 failed", Numbers.isDigits("abc"));
	}

	/**
	 * Tests isNumber(String) and tests that createNumber(String) returns a valid number iff
	 * isNumber(String) returns false.
	 */
	@Test
	public void testIsNumber() {
		String val = "12345";
		assertTrue("isNumber(String) 1 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 1 failed", checkCreateNumber(val));
		val = "1234.5";
		assertTrue("isNumber(String) 2 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 2 failed", checkCreateNumber(val));
		val = ".12345";
		assertTrue("isNumber(String) 3 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 3 failed", checkCreateNumber(val));
		val = "1234E5";
		assertTrue("isNumber(String) 4 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 4 failed", checkCreateNumber(val));
		val = "1234E+5";
		assertTrue("isNumber(String) 5 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 5 failed", checkCreateNumber(val));
		val = "1234E-5";
		assertTrue("isNumber(String) 6 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 6 failed", checkCreateNumber(val));
		val = "123.4E5";
		assertTrue("isNumber(String) 7 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 7 failed", checkCreateNumber(val));
		val = "-1234";
		assertTrue("isNumber(String) 8 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 8 failed", checkCreateNumber(val));
		val = "-1234.5";
		assertTrue("isNumber(String) 9 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 9 failed", checkCreateNumber(val));
		val = "-.12345";
		assertTrue("isNumber(String) 10 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 10 failed", checkCreateNumber(val));
		val = "-1234E5";
		assertTrue("isNumber(String) 11 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 11 failed", checkCreateNumber(val));
		val = "0";
		assertTrue("isNumber(String) 12 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 12 failed", checkCreateNumber(val));
		val = "-0";
		assertTrue("isNumber(String) 13 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 13 failed", checkCreateNumber(val));
		val = "01234";
		assertTrue("isNumber(String) 14 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 14 failed", checkCreateNumber(val));
		val = "-01234";
		assertTrue("isNumber(String) 15 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 15 failed", checkCreateNumber(val));
		val = "0xABC123";
		assertTrue("isNumber(String) 16 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 16 failed", checkCreateNumber(val));
		val = "0x0";
		assertTrue("isNumber(String) 17 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 17 failed", checkCreateNumber(val));
		val = "123.4E21D";
		assertTrue("isNumber(String) 19 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 19 failed", checkCreateNumber(val));
		val = "-221.23F";
		assertTrue("isNumber(String) 20 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 20 failed", checkCreateNumber(val));
		val = "22338L";
		assertTrue("isNumber(String) 21 failed", Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 21 failed", checkCreateNumber(val));
		val = null;
		assertTrue("isNumber(String) 1 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 1 Neg failed", !checkCreateNumber(val));
		val = "";
		assertTrue("isNumber(String) 2 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 2 Neg failed", !checkCreateNumber(val));
		val = "--2.3";
		assertTrue("isNumber(String) 3 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 3 Neg failed", !checkCreateNumber(val));
		val = ".12.3";
		assertTrue("isNumber(String) 4 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 4 Neg failed", !checkCreateNumber(val));
		val = "-123E";
		assertTrue("isNumber(String) 5 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 5 Neg failed", !checkCreateNumber(val));
		val = "-123E+-212";
		assertTrue("isNumber(String) 6 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 6 Neg failed", !checkCreateNumber(val));
		val = "-123E2.12";
		assertTrue("isNumber(String) 7 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 7 Neg failed", !checkCreateNumber(val));
		val = "0xGF";
		assertTrue("isNumber(String) 8 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 8 Neg failed", !checkCreateNumber(val));
		val = "0xFAE-1";
		assertTrue("isNumber(String) 9 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 9 Neg failed", !checkCreateNumber(val));
		val = ".";
		assertTrue("isNumber(String) 10 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 10 Neg failed", !checkCreateNumber(val));
		val = "-0ABC123";
		assertTrue("isNumber(String) 11 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 11 Neg failed", !checkCreateNumber(val));
		val = "123.4E-D";
		assertTrue("isNumber(String) 12 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 12 Neg failed", !checkCreateNumber(val));
		val = "123.4ED";
		assertTrue("isNumber(String) 13 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 13 Neg failed", !checkCreateNumber(val));
		val = "1234E5l";
		assertTrue("isNumber(String) 14 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 14 Neg failed", !checkCreateNumber(val));
		val = "11a";
		assertTrue("isNumber(String) 15 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 15 Neg failed", !checkCreateNumber(val));
		val = "1a";
		assertTrue("isNumber(String) 16 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 16 Neg failed", !checkCreateNumber(val));
		val = "a";
		assertTrue("isNumber(String) 17 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 17 Neg failed", !checkCreateNumber(val));
		val = "11g";
		assertTrue("isNumber(String) 18 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 18 Neg failed", !checkCreateNumber(val));
		val = "11z";
		assertTrue("isNumber(String) 19 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 19 Neg failed", !checkCreateNumber(val));
		val = "11def";
		assertTrue("isNumber(String) 20 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 20 Neg failed", !checkCreateNumber(val));
		val = "11d11";
		assertTrue("isNumber(String) 21 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 21 Neg failed", !checkCreateNumber(val));
		val = "11 11";
		assertTrue("isNumber(String) 22 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 22 Neg failed", !checkCreateNumber(val));
		val = " 1111";
		assertTrue("isNumber(String) 23 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 23 Neg failed", !checkCreateNumber(val));
		val = "1111 ";
		assertTrue("isNumber(String) 24 Neg failed", !Numbers.isNumber(val));
		assertTrue("isNumber(String)/createNumber(String) 24 Neg failed", !checkCreateNumber(val));

		// LANG-521
		val = "2.";
		assertTrue("isNumber(String) LANG-521 failed", Numbers.isNumber(val));

		// LANG-664
		val = "1.1L";
		assertFalse("isNumber(String) LANG-664 failed", Numbers.isNumber(val));
	}

	private boolean checkCreateNumber(final String val) {
		try {
			final Object obj = Numbers.createNumber(val);
			if (obj == null) {
				return false;
			}
			return true;
		}
		catch (final NumberFormatException e) {
			return false;
		}
	}

	@SuppressWarnings("cast")
	// suppress instanceof warning check
	@Test
	public void testConstants() {
		assertTrue(Numbers.LONG_ZERO instanceof Long);
		assertTrue(Numbers.LONG_ONE instanceof Long);
		assertTrue(Numbers.LONG_MINUS_ONE instanceof Long);
		assertTrue(Numbers.INTEGER_ZERO instanceof Integer);
		assertTrue(Numbers.INTEGER_ONE instanceof Integer);
		assertTrue(Numbers.INTEGER_MINUS_ONE instanceof Integer);
		assertTrue(Numbers.SHORT_ZERO instanceof Short);
		assertTrue(Numbers.SHORT_ONE instanceof Short);
		assertTrue(Numbers.SHORT_MINUS_ONE instanceof Short);
		assertTrue(Numbers.BYTE_ZERO instanceof Byte);
		assertTrue(Numbers.BYTE_ONE instanceof Byte);
		assertTrue(Numbers.BYTE_MINUS_ONE instanceof Byte);
		assertTrue(Numbers.DOUBLE_ZERO instanceof Double);
		assertTrue(Numbers.DOUBLE_ONE instanceof Double);
		assertTrue(Numbers.DOUBLE_MINUS_ONE instanceof Double);
		assertTrue(Numbers.FLOAT_ZERO instanceof Float);
		assertTrue(Numbers.FLOAT_ONE instanceof Float);
		assertTrue(Numbers.FLOAT_MINUS_ONE instanceof Float);

		assertTrue(Numbers.LONG_ZERO.longValue() == 0);
		assertTrue(Numbers.LONG_ONE.longValue() == 1);
		assertTrue(Numbers.LONG_MINUS_ONE.longValue() == -1);
		assertTrue(Numbers.INTEGER_ZERO.intValue() == 0);
		assertTrue(Numbers.INTEGER_ONE.intValue() == 1);
		assertTrue(Numbers.INTEGER_MINUS_ONE.intValue() == -1);
		assertTrue(Numbers.SHORT_ZERO.shortValue() == 0);
		assertTrue(Numbers.SHORT_ONE.shortValue() == 1);
		assertTrue(Numbers.SHORT_MINUS_ONE.shortValue() == -1);
		assertTrue(Numbers.BYTE_ZERO.byteValue() == 0);
		assertTrue(Numbers.BYTE_ONE.byteValue() == 1);
		assertTrue(Numbers.BYTE_MINUS_ONE.byteValue() == -1);
		assertTrue(Numbers.DOUBLE_ZERO.doubleValue() == 0.0d);
		assertTrue(Numbers.DOUBLE_ONE.doubleValue() == 1.0d);
		assertTrue(Numbers.DOUBLE_MINUS_ONE.doubleValue() == -1.0d);
		assertTrue(Numbers.FLOAT_ZERO.floatValue() == 0.0f);
		assertTrue(Numbers.FLOAT_ONE.floatValue() == 1.0f);
		assertTrue(Numbers.FLOAT_MINUS_ONE.floatValue() == -1.0f);
	}

	@Test
	public void testLang300() {
		Numbers.createNumber("-1l");
		Numbers.createNumber("01l");
		Numbers.createNumber("1l");
	}

	@Test
	public void testLang381() {
		assertTrue(Double.isNaN(Numbers.min(1.2, 2.5, Double.NaN)));
		assertTrue(Double.isNaN(Numbers.max(1.2, 2.5, Double.NaN)));
		assertTrue(Float.isNaN(Numbers.min(1.2f, 2.5f, Float.NaN)));
		assertTrue(Float.isNaN(Numbers.max(1.2f, 2.5f, Float.NaN)));

		final double[] a = new double[] { 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
		assertTrue(Double.isNaN(Numbers.max(a)));
		assertTrue(Double.isNaN(Numbers.min(a)));

		final double[] b = new double[] { Double.NaN, 1.2, Double.NaN, 3.7, 27.0, 42.0, Double.NaN };
		assertTrue(Double.isNaN(Numbers.max(b)));
		assertTrue(Double.isNaN(Numbers.min(b)));

		final float[] aF = new float[] { 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };
		assertTrue(Float.isNaN(Numbers.max(aF)));

		final float[] bF = new float[] { Float.NaN, 1.2f, Float.NaN, 3.7f, 27.0f, 42.0f, Float.NaN };
		assertTrue(Float.isNaN(Numbers.max(bF)));
	}

	@Test
	public void testDecFormat() {
		assertEquals("100", Numbers.decFormat(100, 2));
		assertEquals("1", Numbers.decFormat(1.0012345, 2));
		assertEquals("1.1", Numbers.decFormat(1.102345, 2));
		assertEquals("1.01", Numbers.decFormat(1.012345, 2));
		assertEquals("1.23", Numbers.decFormat(1.2345, 2));
		assertEquals("1.235", Numbers.decFormat(1.2346, 3));
	}
	
	@Test
	public void testCutFormat1() {
		assertEquals("100", Numbers.cutFormat(100, 2));
		assertEquals("1", Numbers.cutFormat(1.0012345, 2));
		assertEquals("1.1", Numbers.cutFormat(1.102345, 2));
		assertEquals("1.01", Numbers.cutFormat(1.012345, 2));
		assertEquals("1.23", Numbers.cutFormat(1.2345, 2));
		assertEquals("1.234", Numbers.cutFormat(1.2346, 3));
	}

	private static String cutFormat2(double n, int frac) {
		String s = String.valueOf(n);
		int dot = s.indexOf('.');
		if (dot > 0) {
			int end = dot + frac;
			if (end < s.length()) {
				int i = end;
				for ( ; i >= dot; i--) {
					char c = s.charAt(i);
					if (c != '0' && c != '.') {
						break;
					}
				};
				s = s.substring(0, i + 1);
			}
		}
		
		return s;
	}

	@Test
	public void testCutFormat2() {
		assertEquals("100.0", cutFormat2(100, 2));
		assertEquals("1", cutFormat2(1.0012345, 2));
		assertEquals("1.1", cutFormat2(1.102345, 2));
		assertEquals("1.01", cutFormat2(1.012345, 2));
		assertEquals("1.23", cutFormat2(1.2345, 2));
		assertEquals("1.234", cutFormat2(1.2346, 3));
	}
	
	private static final DecimalFormat df2 = new DecimalFormat("#.##");
	private static final DecimalFormat df3 = new DecimalFormat("#.###");

	@Test
	public void testCutFormat3() {
		assertEquals("100", df2.format(100));
		assertEquals("1", df2.format(1.0012345));
		assertEquals("1.1", df2.format(1.102345));
		assertEquals("1.01", df2.format(1.012345));
		assertEquals("1.23", df2.format(1.2345));
		assertEquals("1.235", df3.format(1.2346));
	}
	
	@Test
	public void testSpeed() {
		final int c = 100000;
		StopWatch sw = new StopWatch();
		for (int i = 0; i < c; i++) {
			testDecFormat();
		}
		System.out.println("decFormat - " + sw);

		sw.restart();
		for (int i = 0; i < c; i++) {
			testCutFormat1();
		}
		System.out.println("cutFormat1 - " + sw);

		sw.restart();
		for (int i = 0; i < c; i++) {
			testCutFormat2();
		}
		System.out.println("cutFormat2 - " + sw);

		sw.restart();
		for (int i = 0; i < c; i++) {
			testCutFormat3();
		}
		System.out.println("DecimalFormat - " + sw);
	}

	
	@Test
	public void testSpeed2() {
		final int c = 100000;

		Numbers.format(c);
		StopWatch sw = new StopWatch();
		for (int i = 0; i < c; i++) {
			Numbers.format(i);
		}
		System.out.println("Thread Format - " + sw);

		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(340);
		sw.restart();
		for (int i = 0; i < c; i++) {
			df.format(i);
		}
		System.out.println("Static Format - " + sw);

		df.setMaximumFractionDigits(340);
		sw.restart();
		for (int i = 0; i < c; i++) {
			DecimalFormat df2 = new DecimalFormat("#");
			df2.format(i);
		}
		System.out.println("Dynamic Format - " + sw);
	}
	
	// -----------------------------------------------------------------------
	// 
	public void testFormatSize() {
		final BigInteger b1023 = BigInteger.valueOf(1023);
		final BigInteger b1025 = BigInteger.valueOf(1025);
		final BigInteger KB1 = BigInteger.valueOf(1024);
		final BigInteger MB1 = KB1.multiply(KB1);
		final BigInteger GB1 = MB1.multiply(KB1);
		final BigInteger GB2 = GB1.add(GB1);
		final BigInteger TB1 = GB1.multiply(KB1);
		final BigInteger PB1 = TB1.multiply(KB1);
		final BigInteger EB1 = PB1.multiply(KB1);
		assertEquals(Numbers.formatSize(BigInteger.ZERO), "0 bytes");
		assertEquals(Numbers.formatSize(BigInteger.ONE), "1 bytes");
		assertEquals(Numbers.formatSize(b1023), "1023 bytes");
		assertEquals(Numbers.formatSize(KB1), "1 KB");
		assertEquals(Numbers.formatSize(b1025), "1 KB");
		assertEquals(Numbers.formatSize(MB1.subtract(KB1)), "1023 KB");
		assertEquals(Numbers.formatSize(MB1), "1 MB");
		assertEquals(Numbers.formatSize(MB1.add(BigInteger.ONE)), "1 MB");
		assertEquals(Numbers.formatSize(GB1.subtract(KB1.multiply(BigInteger.valueOf(12)))), "1023.98 MB");
		assertEquals(Numbers.formatSize(GB1), "1 GB");
		assertEquals(Numbers.formatSize(GB1.add(BigInteger.ONE)), "1 GB");
		assertEquals(Numbers.formatSize(GB2), "2 GB");
		assertEquals(Numbers.formatSize(GB2.subtract(BigInteger.ONE)), "1.99 GB");
		assertEquals(Numbers.formatSize(TB1), "1 TB");
		assertEquals(Numbers.formatSize(PB1), "1 PB");
		assertEquals(Numbers.formatSize(EB1), "1 EB");
		assertEquals(Numbers.formatSize(Long.MAX_VALUE), "8 EB");
		// Other MAX_VALUEs
		assertEquals(Numbers.formatSize(BigInteger.valueOf(Character.MAX_VALUE)), "63.99 KB");
		assertEquals(Numbers.formatSize(BigInteger.valueOf(Short.MAX_VALUE)), "31.99 KB");
		assertEquals(Numbers.formatSize(BigInteger.valueOf(Integer.MAX_VALUE)), "1.99 GB");
	}

	public void testFormatSizeLong() {
		assertEquals(Numbers.formatSize(0), "0 bytes");
		assertEquals(Numbers.formatSize(1), "1 bytes");
		assertEquals(Numbers.formatSize(1023), "1023 bytes");
		assertEquals(Numbers.formatSize(1024), "1 KB");
		assertEquals(Numbers.formatSize(1025), "1 KB");
		assertEquals(Numbers.formatSize(1024 * 1023), "1023 KB");
		assertEquals(Numbers.formatSize(1024 * 1024), "1 MB");
		assertEquals(Numbers.formatSize(1024 * 1025), "1 MB");
		assertEquals(Numbers.formatSize(1024 * 1024 * 1023), "1023 MB");
		assertEquals(Numbers.formatSize(1024 * 1024 * 1024), "1 GB");
		assertEquals(Numbers.formatSize(1024 * 1024 * 1025), "1 GB");
		assertEquals(Numbers.formatSize(1024L * 1024 * 1024 * 2), "2 GB");
		assertEquals(Numbers.formatSize(1024 * 1024 * 1024 * 2 - 1), "1.99 GB");
		assertEquals(Numbers.formatSize(1024L * 1024 * 1024 * 1024), "1 TB");
		assertEquals(Numbers.formatSize(1024L * 1024 * 1024 * 1024 * 1024), "1 PB");
		assertEquals(Numbers.formatSize(1024L * 1024 * 1024 * 1024 * 1024 * 1024), "1 EB");
		assertEquals(Numbers.formatSize(Long.MAX_VALUE), "8 EB");
		// Other MAX_VALUEs
		assertEquals(Numbers.formatSize(Character.MAX_VALUE), "63.99 KB");
		assertEquals(Numbers.formatSize(Short.MAX_VALUE), "31.99 KB");
		assertEquals(Numbers.formatSize(Integer.MAX_VALUE), "1.99 GB");
	}

	// -----------------------------------------------------------------------
	// 
	public void testParseSize() {
		final BigDecimal b1023 = BigDecimal.valueOf(1023);
		final BigDecimal b1025 = BigDecimal.valueOf(1025);
		final BigDecimal KB1 = BigDecimal.valueOf(1024);
		final BigDecimal MB1 = KB1.multiply(KB1);
		final BigDecimal GB1 = MB1.multiply(KB1);
		final BigDecimal GB2 = GB1.add(GB1);
		final BigDecimal TB1 = GB1.multiply(KB1);
		final BigDecimal PB1 = TB1.multiply(KB1);
		final BigDecimal EB1 = PB1.multiply(KB1);
		assertEquals(Numbers.parseSize("0 bytes"), BigDecimal.ZERO);
		assertEquals(Numbers.parseSize("1 bytes"), BigDecimal.ONE);
		assertEquals(Numbers.parseSize("1023 bytes"), b1023);
		assertEquals(Numbers.parseSize("1 KB"), KB1);
		assertEquals(Numbers.parseSize("1025 B"), b1025);
		assertEquals(Numbers.parseSize("1023 KB"), MB1.subtract(KB1));
		assertEquals(Numbers.parseSize("1 MB"), MB1);
		assertEquals(Numbers.parseSize("1023 MB"), GB1.subtract(MB1));
		assertEquals(Numbers.parseSize("1 GB"), GB1);
		assertEquals(Numbers.parseSize("2 GB"), GB2);
		assertEquals(Numbers.parseSize("1 TB"), TB1);
		assertEquals(Numbers.parseSize("1 PB"), PB1);
		assertEquals(Numbers.parseSize("1 EB"), EB1);
		assertEquals(Numbers.parseSize("7 EB"), EB1.multiply(BigDecimal.valueOf(7)));
		// Other MAX_VALUEs
		assertEquals(Numbers.parseSize("64 KB"), BigDecimal.valueOf(Character.MAX_VALUE).add(BigDecimal.ONE));
		assertEquals(Numbers.parseSize("32 KB"), BigDecimal.valueOf(Short.MAX_VALUE).add(BigDecimal.ONE));
		assertEquals(Numbers.parseSize("2 GB"), BigDecimal.valueOf(Integer.MAX_VALUE).add(BigDecimal.ONE));
	}
}
