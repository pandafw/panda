package panda.io.stream;

import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

/**
 * JUnit Test Case for {@link ProxyOutputStream}.
 */
public class ProxyOutputStreamTest extends TestCase {

	private ByteArrayOutputStream original;

	private OutputStream proxied;

	@Override
	protected void setUp() {
		original = new ByteArrayOutputStream() {
			@Override
			public void write(final byte[] ba) throws IOException {
				if (ba != null) {
					super.write(ba);
				}
			}
		};
		proxied = new ProxyOutputStream(original);
	}

	public void testWrite() throws Exception {
		proxied.write('y');
		assertEquals(1, original.size());
		assertEquals('y', original.toByteArray()[0]);
	}

	public void testWriteNullBaSucceeds() throws Exception {
		final byte[] ba = null;
		original.write(ba);
		proxied.write(ba);
	}
}
