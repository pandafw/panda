package panda.io.stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

/**
 * Test case for {@link CharSequenceReader}.
 * 
 */
public class CharSequenceReaderTest {
	private static final char NONE = (new char[1])[0];

	/** Test {@link Reader#close()}. */
	@Test
	public void testClose() throws IOException {
		final Reader reader = new CharSequenceReader("FooBar");
		checkRead(reader, "Foo");
		reader.close();
		checkRead(reader, "Foo");
	}

	/** Test {@link Reader#markSupported()}. */
	@Test
	public void testMarkSupported() throws Exception {
		final Reader reader = new CharSequenceReader("FooBar");
		assertTrue(reader.markSupported());
		reader.close();
	}

	/** Test {@link Reader#mark(int)}. */
	@Test
	public void testMark() throws IOException {
		final Reader reader = new CharSequenceReader("FooBar");
		checkRead(reader, "Foo");
		reader.mark(0);
		checkRead(reader, "Bar");
		reader.reset();
		checkRead(reader, "Bar");
		reader.close();
		checkRead(reader, "Foo");
		reader.reset();
		checkRead(reader, "Foo");
	}

	/** Test {@link Reader#skip(long)}. */
	@Test
	public void testSkip() throws IOException {
		final Reader reader = new CharSequenceReader("FooBar");
		assertEquals(3, reader.skip(3));
		checkRead(reader, "Bar");
		assertEquals(-1, reader.skip(3));
		reader.reset();
		assertEquals(2, reader.skip(2));
		assertEquals(4, reader.skip(10));
		assertEquals(-1, reader.skip(1));
		reader.close();
		assertEquals(6, reader.skip(20));
		assertEquals(-1, reader.read());
	}

	/** Test {@link Reader#read()}. */
	@Test
	public void testRead() throws IOException {
		final Reader reader = new CharSequenceReader("Foo");
		assertEquals('F', reader.read());
		assertEquals('o', reader.read());
		assertEquals('o', reader.read());
		assertEquals(-1, reader.read());
		assertEquals(-1, reader.read());
		reader.close();
	}

	/** Test {@link Reader#read(char[])}. */
	@Test
	public void testReadCharArray() throws IOException {
		final Reader reader = new CharSequenceReader("FooBar");
		char[] chars = new char[2];
		assertEquals(2, reader.read(chars));
		checkArray(new char[] { 'F', 'o' }, chars);
		chars = new char[3];
		assertEquals(3, reader.read(chars));
		checkArray(new char[] { 'o', 'B', 'a' }, chars);
		chars = new char[3];
		assertEquals(1, reader.read(chars));
		checkArray(new char[] { 'r', NONE, NONE }, chars);
		assertEquals(-1, reader.read(chars));
		reader.close();
	}

	/** Test {@link Reader#read(char[], int, int)}. */
	@Test
	public void testReadCharArrayPortion() throws IOException {
		final char[] chars = new char[10];
		final Reader reader = new CharSequenceReader("FooBar");
		assertEquals(3, reader.read(chars, 3, 3));
		checkArray(new char[] { NONE, NONE, NONE, 'F', 'o', 'o' }, chars);
		assertEquals(3, reader.read(chars, 0, 3));
		checkArray(new char[] { 'B', 'a', 'r', 'F', 'o', 'o', NONE }, chars);
		assertEquals(-1, reader.read(chars));
		reader.close();
	}

	private void checkRead(final Reader reader, final String expected) throws IOException {
		for (int i = 0; i < expected.length(); i++) {
			assertEquals("Read[" + i + "] of '" + expected + "'", expected.charAt(i), (char)reader.read());
		}
	}

	private void checkArray(final char[] expected, final char[] actual) {
		for (int i = 0; i < expected.length; i++) {
			assertEquals("Compare[" + i + "]", expected[i], actual[i]);
		}
	}
}
