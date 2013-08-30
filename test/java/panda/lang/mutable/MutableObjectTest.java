package panda.lang.mutable;

import org.junit.Test;

import panda.lang.mutable.MutableObject;
import panda.lang.mutable.MutableShort;
import static org.junit.Assert.*;

/**
 * JUnit tests.
 * 
 * @see MutableShort
 */
public class MutableObjectTest {

	// ----------------------------------------------------------------
	@Test
	public void testConstructors() {
		assertEquals(null, new MutableObject<String>().getValue());

		final Integer i = Integer.valueOf(6);
		assertSame(i, new MutableObject<Integer>(i).getValue());
		assertSame("HI", new MutableObject<String>("HI").getValue());
		assertSame(null, new MutableObject<Object>(null).getValue());
	}

	@Test
	public void testGetSet() {
		final MutableObject<String> mutNum = new MutableObject<String>();
		assertEquals(null, new MutableObject<Object>().getValue());

		mutNum.setValue("HELLO");
		assertSame("HELLO", mutNum.getValue());

		mutNum.setValue(null);
		assertSame(null, mutNum.getValue());
	}

	@Test
	public void testEquals() {
		final MutableObject<String> mutNumA = new MutableObject<String>("ALPHA");
		final MutableObject<String> mutNumB = new MutableObject<String>("ALPHA");
		final MutableObject<String> mutNumC = new MutableObject<String>("BETA");
		final MutableObject<String> mutNumD = new MutableObject<String>(null);

		assertTrue(mutNumA.equals(mutNumA));
		assertTrue(mutNumA.equals(mutNumB));
		assertTrue(mutNumB.equals(mutNumA));
		assertTrue(mutNumB.equals(mutNumB));
		assertFalse(mutNumA.equals(mutNumC));
		assertFalse(mutNumB.equals(mutNumC));
		assertTrue(mutNumC.equals(mutNumC));
		assertFalse(mutNumA.equals(mutNumD));
		assertTrue(mutNumD.equals(mutNumD));

		assertFalse(mutNumA.equals(null));
		assertFalse(mutNumA.equals(new Object()));
		assertFalse(mutNumA.equals("0"));
	}

	@Test
	public void testHashCode() {
		final MutableObject<String> mutNumA = new MutableObject<String>("ALPHA");
		final MutableObject<String> mutNumB = new MutableObject<String>("ALPHA");
		final MutableObject<String> mutNumC = new MutableObject<String>("BETA");
		final MutableObject<String> mutNumD = new MutableObject<String>(null);

		assertTrue(mutNumA.hashCode() == mutNumA.hashCode());
		assertTrue(mutNumA.hashCode() == mutNumB.hashCode());
		assertFalse(mutNumA.hashCode() == mutNumC.hashCode());
		assertFalse(mutNumA.hashCode() == mutNumD.hashCode());
		assertTrue(mutNumA.hashCode() == "ALPHA".hashCode());
		assertEquals(0, mutNumD.hashCode());
	}

	@Test
	public void testToString() {
		assertEquals("HI", new MutableObject<String>("HI").toString());
		assertEquals("10.0", new MutableObject<Double>(Double.valueOf(10)).toString());
		assertEquals("null", new MutableObject<Object>(null).toString());
	}

}
