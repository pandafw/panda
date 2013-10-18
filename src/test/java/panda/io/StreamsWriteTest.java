package panda.io;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import panda.io.stream.ByteArrayOutputStream;

/**
 * JUnit tests for Streams write methods.
 * 
 * @see Streams
 */
public class StreamsWriteTest extends FileBasedTestCase {

	private static final int FILE_SIZE = 1024 * 4 + 1;

	private final byte[] inData = generateTestData(FILE_SIZE);

	public StreamsWriteTest(final String testName) {
		super(testName);
	}

	// ----------------------------------------------------------------
	// Setup
	// ----------------------------------------------------------------

	@Override
	public void setUp() throws Exception {
	}

	@Override
	public void tearDown() throws Exception {
	}

	// ----------------------------------------------------------------
	// Tests
	// ----------------------------------------------------------------

	// -----------------------------------------------------------------------
	public void testWrite_byteArrayToOutputStream() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(inData, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	public void testWrite_byteArrayToOutputStream_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write((byte[])null, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_byteArrayToOutputStream_nullStream() throws Exception {
		try {
			Streams.write(inData, (OutputStream)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testWrite_byteArrayToWriter() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write(inData, writer);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	public void testWrite_byteArrayToWriter_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write((byte[])null, writer);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_byteArrayToWriter_nullWriter() throws Exception {
		try {
			Streams.write(inData, (Writer)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testWrite_byteArrayToWriter_Encoding() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write(inData, writer, "UTF8");
		out.off();
		writer.flush();

		byte[] bytes = baout.toByteArray();
		bytes = new String(bytes, "UTF8").getBytes("US-ASCII");
		assertTrue("Content differs", Arrays.equals(inData, bytes));
	}

	public void testWrite_byteArrayToWriter_Encoding_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write((byte[])null, writer, "UTF8");
		out.off();
		writer.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_byteArrayToWriter_Encoding_nullWriter() throws Exception {
		try {
			Streams.write(inData, (Writer)null, "UTF8");
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	public void testWrite_byteArrayToWriter_Encoding_nullEncoding() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write(inData, writer, (String)null);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	// -----------------------------------------------------------------------
	public void testWrite_charSequenceToOutputStream() throws Exception {
		final CharSequence csq = new StringBuilder(new String(inData, "US-ASCII"));

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(csq, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	public void testWrite_charSequenceToOutputStream_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write((CharSequence)null, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_charSequenceToOutputStream_nullStream() throws Exception {
		final CharSequence csq = new StringBuilder(new String(inData, "US-ASCII"));
		try {
			Streams.write(csq, (OutputStream)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testWrite_charSequenceToOutputStream_Encoding() throws Exception {
		final CharSequence csq = new StringBuilder(new String(inData, "US-ASCII"));

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(csq, out, "UTF16");
		out.off();
		out.flush();

		byte[] bytes = baout.toByteArray();
		bytes = new String(bytes, "UTF16").getBytes("US-ASCII");
		assertTrue("Content differs", Arrays.equals(inData, bytes));
	}

	public void testWrite_charSequenceToOutputStream_Encoding_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write((CharSequence)null, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_charSequenceToOutputStream_Encoding_nullStream() throws Exception {
		final CharSequence csq = new StringBuilder(new String(inData, "US-ASCII"));
		try {
			Streams.write(csq, (OutputStream)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	public void testWrite_charSequenceToOutputStream_nullEncoding() throws Exception {
		final CharSequence csq = new StringBuilder(new String(inData, "US-ASCII"));

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(csq, out, (String)null);
		out.off();
		out.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	// -----------------------------------------------------------------------
	public void testWrite_charSequenceToWriter() throws Exception {
		final CharSequence csq = new StringBuilder(new String(inData, "US-ASCII"));

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write(csq, writer);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	public void testWrite_charSequenceToWriter_Encoding_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write((CharSequence)null, writer);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_charSequenceToWriter_Encoding_nullStream() throws Exception {
		final CharSequence csq = new StringBuilder(new String(inData, "US-ASCII"));
		try {
			Streams.write(csq, (Writer)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testWrite_stringToOutputStream() throws Exception {
		final String str = new String(inData, "US-ASCII");

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(str, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	public void testWrite_stringToOutputStream_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write((String)null, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_stringToOutputStream_nullStream() throws Exception {
		final String str = new String(inData, "US-ASCII");
		try {
			Streams.write(str, (OutputStream)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testWrite_stringToOutputStream_Encoding() throws Exception {
		final String str = new String(inData, "US-ASCII");

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(str, out, "UTF16");
		out.off();
		out.flush();

		byte[] bytes = baout.toByteArray();
		bytes = new String(bytes, "UTF16").getBytes("US-ASCII");
		assertTrue("Content differs", Arrays.equals(inData, bytes));
	}

	public void testWrite_stringToOutputStream_Encoding_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write((String)null, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_stringToOutputStream_Encoding_nullStream() throws Exception {
		final String str = new String(inData, "US-ASCII");
		try {
			Streams.write(str, (OutputStream)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	public void testWrite_stringToOutputStream_nullEncoding() throws Exception {
		final String str = new String(inData, "US-ASCII");

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(str, out, (String)null);
		out.off();
		out.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	// -----------------------------------------------------------------------
	public void testWrite_stringToWriter() throws Exception {
		final String str = new String(inData, "US-ASCII");

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write(str, writer);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	public void testWrite_stringToWriter_Encoding_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write((String)null, writer);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_stringToWriter_Encoding_nullStream() throws Exception {
		final String str = new String(inData, "US-ASCII");
		try {
			Streams.write(str, (Writer)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testWrite_charArrayToOutputStream() throws Exception {
		final String str = new String(inData, "US-ASCII");

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(str.toCharArray(), out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	public void testWrite_charArrayToOutputStream_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write((char[])null, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_charArrayToOutputStream_nullStream() throws Exception {
		final String str = new String(inData, "US-ASCII");
		try {
			Streams.write(str.toCharArray(), (OutputStream)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testWrite_charArrayToOutputStream_Encoding() throws Exception {
		final String str = new String(inData, "US-ASCII");

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(str.toCharArray(), out, "UTF16");
		out.off();
		out.flush();

		byte[] bytes = baout.toByteArray();
		bytes = new String(bytes, "UTF16").getBytes("US-ASCII");
		assertTrue("Content differs", Arrays.equals(inData, bytes));
	}

	public void testWrite_charArrayToOutputStream_Encoding_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write((char[])null, out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_charArrayToOutputStream_Encoding_nullStream() throws Exception {
		final String str = new String(inData, "US-ASCII");
		try {
			Streams.write(str.toCharArray(), (OutputStream)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	public void testWrite_charArrayToOutputStream_nullEncoding() throws Exception {
		final String str = new String(inData, "US-ASCII");

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);

		Streams.write(str.toCharArray(), out, (String)null);
		out.off();
		out.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	// -----------------------------------------------------------------------
	public void testWrite_charArrayToWriter() throws Exception {
		final String str = new String(inData, "US-ASCII");

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write(str.toCharArray(), writer);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", inData.length, baout.size());
		assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
	}

	public void testWrite_charArrayToWriter_Encoding_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.write((char[])null, writer);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWrite_charArrayToWriter_Encoding_nullStream() throws Exception {
		final String str = new String(inData, "US-ASCII");
		try {
			Streams.write(str.toCharArray(), (Writer)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testWriteLines_OutputStream() throws Exception {
		final Object[] data = new Object[] { "hello", new StringBuffer("world"), "", "this is", null, "some text" };
		final List<Object> list = Arrays.asList(data);

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

		Streams.writeLines(list, "*", out);

		out.off();
		out.flush();

		final String expected = "hello*world**this is**some text*";
		final String actual = baout.toString();
		assertEquals(expected, actual);
	}

	public void testWriteLines_OutputStream_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

		Streams.writeLines((List<?>)null, "*", out);
		out.off();
		out.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWriteLines_OutputStream_nullSeparator() throws Exception {
		final Object[] data = new Object[] { "hello", "world" };
		final List<Object> list = Arrays.asList(data);

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

		Streams.writeLines(list, (String)null, out);
		out.off();
		out.flush();

		final String expected = "hello" + Streams.LINE_SEPARATOR + "world" + Streams.LINE_SEPARATOR;
		final String actual = baout.toString();
		assertEquals(expected, actual);
	}

	public void testWriteLines_OutputStream_nullStream() throws Exception {
		final Object[] data = new Object[] { "hello", "world" };
		final List<Object> list = Arrays.asList(data);
		try {
			Streams.writeLines(list, "*", (OutputStream)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	// -----------------------------------------------------------------------
	public void testWriteLines_OutputStream_Encoding() throws Exception {
		final Object[] data = new Object[] { "hello\u8364", new StringBuffer("world"), "", "this is", null, "some text" };
		final List<Object> list = Arrays.asList(data);

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

		Streams.writeLines(list, "*", out, "UTF-8");

		out.off();
		out.flush();

		final String expected = "hello\u8364*world**this is**some text*";
		final String actual = baout.toString("UTF-8");
		assertEquals(expected, actual);
	}

	public void testWriteLines_OutputStream_Encoding_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

		Streams.writeLines((List<?>)null, "*", out, "US-ASCII");
		out.off();
		out.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWriteLines_OutputStream_Encoding_nullSeparator() throws Exception {
		final Object[] data = new Object[] { "hello", "world" };
		final List<Object> list = Arrays.asList(data);

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

		Streams.writeLines(list, (String)null, out, "US-ASCII");
		out.off();
		out.flush();

		final String expected = "hello" + Streams.LINE_SEPARATOR + "world" + Streams.LINE_SEPARATOR;
		final String actual = baout.toString();
		assertEquals(expected, actual);
	}

	public void testWriteLines_OutputStream_Encoding_nullStream() throws Exception {
		final Object[] data = new Object[] { "hello", "world" };
		final List<Object> list = Arrays.asList(data);
		try {
			Streams.writeLines(list, "*", (OutputStream)null, "US-ASCII");
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

	public void testWriteLines_OutputStream_Encoding_nullEncoding() throws Exception {
		final Object[] data = new Object[] { "hello", new StringBuffer("world"), "", "this is", null, "some text" };
		final List<Object> list = Arrays.asList(data);

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

		Streams.writeLines(list, "*", out, (String)null);

		out.off();
		out.flush();

		final String expected = "hello*world**this is**some text*";
		final String actual = baout.toString();
		assertEquals(expected, actual);
	}

	// -----------------------------------------------------------------------
	public void testWriteLines_Writer() throws Exception {
		final Object[] data = new Object[] { "hello", new StringBuffer("world"), "", "this is", null, "some text" };
		final List<Object> list = Arrays.asList(data);

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.writeLines(list, "*", writer);

		out.off();
		writer.flush();

		final String expected = "hello*world**this is**some text*";
		final String actual = baout.toString();
		assertEquals(expected, actual);
	}

	public void testWriteLines_Writer_nullData() throws Exception {
		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.writeLines((List<?>)null, "*", writer);
		out.off();
		writer.flush();

		assertEquals("Sizes differ", 0, baout.size());
	}

	public void testWriteLines_Writer_nullSeparator() throws Exception {
		final Object[] data = new Object[] { "hello", "world" };
		final List<Object> list = Arrays.asList(data);

		final ByteArrayOutputStream baout = new ByteArrayOutputStream();
		@SuppressWarnings("resource")
		// deliberately not closed
		final YellOnFlushAndCloseOutputStream out = new YellOnFlushAndCloseOutputStream(baout, true, true);
		final Writer writer = new OutputStreamWriter(baout, "US-ASCII");

		Streams.writeLines(list, (String)null, writer);
		out.off();
		writer.flush();

		final String expected = "hello" + Streams.LINE_SEPARATOR + "world" + Streams.LINE_SEPARATOR;
		final String actual = baout.toString();
		assertEquals(expected, actual);
	}

	public void testWriteLines_Writer_nullStream() throws Exception {
		final Object[] data = new Object[] { "hello", "world" };
		final List<Object> list = Arrays.asList(data);
		try {
			Streams.writeLines(list, "*", (Writer)null);
			fail();
		}
		catch (final NullPointerException ex) {
		}
	}

}
