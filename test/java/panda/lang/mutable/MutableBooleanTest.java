package panda.lang.mutable;

import org.junit.Test;

import panda.lang.mutable.MutableBoolean;
import static org.junit.Assert.*;

/**
 * JUnit tests.
 * 
 * @see MutableBoolean
 */
public class MutableBooleanTest {

	@Test
	public void testCompareTo() {
		final MutableBoolean mutBool = new MutableBoolean(false);

		assertEquals(0, mutBool.compareTo(new MutableBoolean(false)));
		assertEquals(-1, mutBool.compareTo(new MutableBoolean(true)));
		mutBool.setValue(true);
		assertEquals(+1, mutBool.compareTo(new MutableBoolean(false)));
		assertEquals(0, mutBool.compareTo(new MutableBoolean(true)));
	}

	@Test(expected = NullPointerException.class)
	public void testCompareToNull() {
		final MutableBoolean mutBool = new MutableBoolean(false);
		mutBool.compareTo(null);
	}

	// ----------------------------------------------------------------
	@Test
	public void testConstructors() {
		assertFalse(new MutableBoolean().booleanValue());

		assertTrue(new MutableBoolean(true).booleanValue());
		assertFalse(new MutableBoolean(false).booleanValue());

		assertTrue(new MutableBoolean(Boolean.TRUE).booleanValue());
		assertFalse(new MutableBoolean(Boolean.FALSE).booleanValue());

	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNull() {
		new MutableBoolean(null);
	}

	@Test
	public void testEquals() {
		final MutableBoolean mutBoolA = new MutableBoolean(false);
		final MutableBoolean mutBoolB = new MutableBoolean(false);
		final MutableBoolean mutBoolC = new MutableBoolean(true);

		assertTrue(mutBoolA.equals(mutBoolA));
		assertTrue(mutBoolA.equals(mutBoolB));
		assertTrue(mutBoolB.equals(mutBoolA));
		assertTrue(mutBoolB.equals(mutBoolB));
		assertFalse(mutBoolA.equals(mutBoolC));
		assertFalse(mutBoolB.equals(mutBoolC));
		assertTrue(mutBoolC.equals(mutBoolC));
		assertFalse(mutBoolA.equals(null));
		assertFalse(mutBoolA.equals(Boolean.FALSE));
		assertFalse(mutBoolA.equals("false"));
	}

	@Test
	public void testGetSet() {
		assertFalse(new MutableBoolean().booleanValue());
		assertEquals(Boolean.FALSE, new MutableBoolean().getValue());

		final MutableBoolean mutBool = new MutableBoolean(false);
		assertEquals(Boolean.FALSE, mutBool.toBoolean());
		assertFalse(mutBool.booleanValue());
		assertTrue(mutBool.isFalse());
		assertFalse(mutBool.isTrue());

		mutBool.setValue(Boolean.TRUE);
		assertEquals(Boolean.TRUE, mutBool.toBoolean());
		assertTrue(mutBool.booleanValue());
		assertFalse(mutBool.isFalse());
		assertTrue(mutBool.isTrue());

		mutBool.setValue(false);
		assertFalse(mutBool.booleanValue());

		mutBool.setValue(true);
		assertTrue(mutBool.booleanValue());

	}

	@Test(expected = NullPointerException.class)
	public void testSetNull() {
		final MutableBoolean mutBool = new MutableBoolean(false);
		mutBool.setValue(null);
	}

	@Test
	public void testHashCode() {
		final MutableBoolean mutBoolA = new MutableBoolean(false);
		final MutableBoolean mutBoolB = new MutableBoolean(false);
		final MutableBoolean mutBoolC = new MutableBoolean(true);

		assertEquals(mutBoolA.hashCode(), mutBoolA.hashCode());
		assertEquals(mutBoolA.hashCode(), mutBoolB.hashCode());
		assertFalse(mutBoolA.hashCode() == mutBoolC.hashCode());
		assertEquals(mutBoolA.hashCode(), Boolean.FALSE.hashCode());
		assertEquals(mutBoolC.hashCode(), Boolean.TRUE.hashCode());
	}

	@Test
	public void testToString() {
		assertEquals(Boolean.FALSE.toString(), new MutableBoolean(false).toString());
		assertEquals(Boolean.TRUE.toString(), new MutableBoolean(true).toString());
	}

}
