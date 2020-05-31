package panda.lang.mutable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * JUnit tests.
 * 
 * @see MutableDouble
 */
public class MutableDoubleTest {

	// ----------------------------------------------------------------
	@Test
	public void testConstructors() {
		assertEquals(0d, new MutableDouble().doubleValue(), 0.0001d);

		assertEquals(1d, new MutableDouble(1d).doubleValue(), 0.0001d);

		assertEquals(2d, new MutableDouble(Double.valueOf(2d)).doubleValue(), 0.0001d);
		assertEquals(3d, new MutableDouble(new MutableDouble(3d)).doubleValue(), 0.0001d);

		assertEquals(2d, new MutableDouble("2.0").doubleValue(), 0.0001d);

	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNull() {
		new MutableDouble((Number)null);
	}

	@Test
	public void testGetSet() {
		final MutableDouble mutNum = new MutableDouble(0d);
		assertEquals(0d, new MutableDouble().doubleValue(), 0.0001d);
		assertEquals(Double.valueOf(0), new MutableDouble().getValue());

		mutNum.setValue(1);
		assertEquals(1d, mutNum.doubleValue(), 0.0001d);
		assertEquals(Double.valueOf(1d), mutNum.getValue());

		mutNum.setValue(Double.valueOf(2d));
		assertEquals(2d, mutNum.doubleValue(), 0.0001d);
		assertEquals(Double.valueOf(2d), mutNum.getValue());

		mutNum.setValue(new MutableDouble(3d));
		assertEquals(3d, mutNum.doubleValue(), 0.0001d);
		assertEquals(Double.valueOf(3d), mutNum.getValue());
	}

	@Test(expected = NullPointerException.class)
	public void testSetNull() {
		final MutableDouble mutNum = new MutableDouble(0d);
		mutNum.setValue(null);
	}

	@Test
	public void testNanInfinite() {
		MutableDouble mutNum = new MutableDouble(Double.NaN);
		assertTrue(mutNum.isNaN());

		mutNum = new MutableDouble(Double.POSITIVE_INFINITY);
		assertTrue(mutNum.isInfinite());

		mutNum = new MutableDouble(Double.NEGATIVE_INFINITY);
		assertTrue(mutNum.isInfinite());
	}

	@Test
	public void testEquals() {
		final MutableDouble mutNumA = new MutableDouble(0d);
		final MutableDouble mutNumB = new MutableDouble(0d);
		final MutableDouble mutNumC = new MutableDouble(1d);

		assertTrue(mutNumA.equals(mutNumA));
		assertTrue(mutNumA.equals(mutNumB));
		assertTrue(mutNumB.equals(mutNumA));
		assertTrue(mutNumB.equals(mutNumB));
		assertFalse(mutNumA.equals(mutNumC));
		assertFalse(mutNumB.equals(mutNumC));
		assertTrue(mutNumC.equals(mutNumC));
		assertFalse(mutNumA.equals(null));
		assertFalse(mutNumA.equals(Double.valueOf(0d)));
		assertFalse(mutNumA.equals("0"));
	}

	@Test
	public void testHashCode() {
		final MutableDouble mutNumA = new MutableDouble(0d);
		final MutableDouble mutNumB = new MutableDouble(0d);
		final MutableDouble mutNumC = new MutableDouble(1d);

		assertTrue(mutNumA.hashCode() == mutNumA.hashCode());
		assertTrue(mutNumA.hashCode() == mutNumB.hashCode());
		assertFalse(mutNumA.hashCode() == mutNumC.hashCode());
		assertTrue(mutNumA.hashCode() == Double.valueOf(0d).hashCode());
	}

	@Test
	public void testCompareTo() {
		final MutableDouble mutNum = new MutableDouble(0d);

		assertEquals(0, mutNum.compareTo(new MutableDouble(0d)));
		assertEquals(+1, mutNum.compareTo(new MutableDouble(-1d)));
		assertEquals(-1, mutNum.compareTo(new MutableDouble(1d)));
	}

	@Test(expected = NullPointerException.class)
	public void testCompareToNull() {
		final MutableDouble mutNum = new MutableDouble(0d);
		mutNum.compareTo(null);
	}

	@Test
	public void testPrimitiveValues() {
		final MutableDouble mutNum = new MutableDouble(1.7);

		assertEquals(1.7F, mutNum.floatValue(), 0);
		assertEquals(1.7, mutNum.doubleValue(), 0);
		assertEquals((byte)1, mutNum.byteValue());
		assertEquals((short)1, mutNum.shortValue());
		assertEquals(1, mutNum.intValue());
		assertEquals(1L, mutNum.longValue());
	}

	@Test
	public void testToDouble() {
		assertEquals(Double.valueOf(0d), new MutableDouble(0d).toDouble());
		assertEquals(Double.valueOf(12.3d), new MutableDouble(12.3d).toDouble());
	}

	@Test
	public void testIncrement() {
		final MutableDouble mutNum = new MutableDouble(1);
		mutNum.increment();

		assertEquals(2, mutNum.intValue());
		assertEquals(2L, mutNum.longValue());
	}

	@Test
	public void testDecrement() {
		final MutableDouble mutNum = new MutableDouble(1);
		mutNum.decrement();

		assertEquals(0, mutNum.intValue());
		assertEquals(0L, mutNum.longValue());
	}

	@Test
	public void testAddValuePrimitive() {
		final MutableDouble mutNum = new MutableDouble(1);
		mutNum.add(1.1d);

		assertEquals(2.1d, mutNum.doubleValue(), 0.01d);
	}

	@Test
	public void testAddValueObject() {
		final MutableDouble mutNum = new MutableDouble(1);
		mutNum.add(Double.valueOf(1.1d));

		assertEquals(2.1d, mutNum.doubleValue(), 0.01d);
	}

	@Test
	public void testSubtractValuePrimitive() {
		final MutableDouble mutNum = new MutableDouble(1);
		mutNum.subtract(0.9d);

		assertEquals(0.1d, mutNum.doubleValue(), 0.01d);
	}

	@Test
	public void testSubtractValueObject() {
		final MutableDouble mutNum = new MutableDouble(1);
		mutNum.subtract(Double.valueOf(0.9d));

		assertEquals(0.1d, mutNum.doubleValue(), 0.01d);
	}

	@Test
	public void testToString() {
		assertEquals("0.0", new MutableDouble(0d).toString());
		assertEquals("10.0", new MutableDouble(10d).toString());
		assertEquals("-123.0", new MutableDouble(-123d).toString());
	}

}
