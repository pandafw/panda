package panda.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import panda.codec.DecoderException;

/**
 * Tests {@link DecoderException}.
 */
public class DecoderExceptionTest {

	private static final String MSG = "TEST";

	private static final Throwable t = new Exception();

	@Test
	public void testConstructor0() {
		final DecoderException e = new DecoderException();
		assertNull(e.getMessage());
		assertNull(e.getCause());
	}

	@Test
	public void testConstructorString() {
		final DecoderException e = new DecoderException(MSG);
		assertEquals(MSG, e.getMessage());
		assertNull(e.getCause());
	}

	@Test
	public void testConstructorStringThrowable() {
		final DecoderException e = new DecoderException(MSG, t);
		assertEquals(MSG, e.getMessage());
		assertEquals(t, e.getCause());
	}

	@Test
	public void testConstructorThrowable() {
		final DecoderException e = new DecoderException(t);
		assertEquals(t.getClass().getName(), e.getMessage());
		assertEquals(t, e.getCause());
	}

}
