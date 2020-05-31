package panda.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests {@link EncoderException}.
 */
public class EncoderExceptionTest {

	private static final String MSG = "TEST";

	private static final Throwable t = new Exception();

	@Test
	public void testConstructor0() {
		final EncoderException e = new EncoderException();
		assertNull(e.getMessage());
		assertNull(e.getCause());
	}

	@Test
	public void testConstructorString() {
		final EncoderException e = new EncoderException(MSG);
		assertEquals(MSG, e.getMessage());
		assertNull(e.getCause());
	}

	@Test
	public void testConstructorStringThrowable() {
		final EncoderException e = new EncoderException(MSG, t);
		assertEquals(MSG, e.getMessage());
		assertEquals(t, e.getCause());
	}

	@Test
	public void testConstructorThrowable() {
		final EncoderException e = new EncoderException(t);
		assertEquals(t.getClass().getName(), e.getMessage());
		assertEquals(t, e.getCause());
	}

}
