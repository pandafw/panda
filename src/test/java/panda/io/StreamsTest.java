package panda.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import panda.lang.Charsets;
import panda.lang.Strings;

/**
 * This is used to test Streams for correctness. The following checks are performed:
 * <ul>
 * <li>The return must not be null, must be the same type and equals() to the method's second arg</li>
 * <li>All bytes must have been read from the source (available() == 0)</li>
 * <li>The source and destination content must be identical (byte-wise comparison check)</li>
 * <li>The output stream must not have been closed (a byte/char is written to test this, and
 * subsequent size checked)</li>
 * </ul>
 * Due to interdependencies in Streams and StreamsTestlet, one bug may cause multiple tests to fail.
 */
public class StreamsTest extends FileBasedTestCase {

	private static final int FILE_SIZE = 1024 * 4 + 1;

	/** Determine if this is windows. */
	private static final boolean WINDOWS = File.separatorChar == '\\';
	/*
	 * Note: this is not particularly beautiful code. A better way to check for flush and close
	 * status would be to implement "trojan horse" wrapper implementations of the various stream
	 * classes, which set a flag when relevant methods are called. (JT)
	 */

	private char[] carr = null;

	private byte[] iarr = null;

	private File m_testFile;

	public StreamsTest(final String name) {
		super(name);
	}

	/** Assert that the contents of two byte arrays are the same. */
	private void assertEqualContent(final byte[] b0, final byte[] b1) {
		assertTrue("Content not equal according to java.util.Arrays#equals()", Arrays.equals(b0, b1));
	}

	@Override
	public void setUp() {
		try {
			getTestDirectory().mkdirs();
			m_testFile = new File(getTestDirectory(), "file2-test.txt");

			createFile(m_testFile, FILE_SIZE);
		}
		catch (final IOException ioe) {
			throw new RuntimeException("Can't run this test because the environment could not be built: "
					+ ioe.getMessage());
		}
		// Create and init a byte array as input data
		iarr = new byte[200];
		Arrays.fill(iarr, (byte)-1);
		for (int i = 0; i < 80; i++) {
			iarr[i] = (byte)i;
		}
		carr = new char[200];
		Arrays.fill(carr, (char)-1);
		for (int i = 0; i < 80; i++) {
			carr[i] = (char)i;
		}
	}

	@Override
	public void tearDown() {
		carr = null;
		iarr = null;
		try {
			Files.deleteDir(getTestDirectory());
		}
		catch (final IOException e) {
			throw new RuntimeException("Could not clear up " + getTestDirectory() + ": " + e);
		}
	}

	public void testCloseQuietly_CloseableIOException() {
		Streams.safeClose(new Closeable() {
			public void close() throws IOException {
				throw new IOException();
			}
		});
	}

	public void testCloseQuietly_Selector() {
		Selector selector = null;
		try {
			selector = Selector.open();
		}
		catch (final IOException e) {
		}
		finally {
			Streams.safeClose(selector);
		}
	}

	public void testCloseQuietly_SelectorNull() {
		final Selector selector = null;
		Streams.safeClose(selector);
	}

	public void testCloseQuietly_SelectorTwice() {
		Selector selector = null;
		try {
			selector = Selector.open();
		}
		catch (final IOException e) {
		}
		finally {
			Streams.safeClose(selector);
			Streams.safeClose(selector);
		}
	}

	public void testConstants() throws Exception {
		assertEquals("\n", Streams.LINE_SEPARATOR_UNIX);
		assertEquals("\r\n", Streams.LINE_SEPARATOR_WINDOWS);
		if (WINDOWS) {
			assertEquals("\r\n", Streams.LINE_SEPARATOR);
		}
		else {
			assertEquals("\n", Streams.LINE_SEPARATOR);
		}
	}

	public void testContentEquals_InputStream_InputStream() throws Exception {
		{
			final ByteArrayInputStream input1 = new ByteArrayInputStream("".getBytes(Charsets.UTF_8));
			assertTrue(Streams.contentEquals(input1, input1));
		}
		{
			final ByteArrayInputStream input1 = new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8));
			assertTrue(Streams.contentEquals(input1, input1));
		}
		assertTrue(Streams.contentEquals(new ByteArrayInputStream("".getBytes(Charsets.UTF_8)),
			new ByteArrayInputStream("".getBytes(Charsets.UTF_8))));
		assertTrue(Streams.contentEquals(
			new BufferedInputStream(new ByteArrayInputStream("".getBytes(Charsets.UTF_8))), new BufferedInputStream(
				new ByteArrayInputStream("".getBytes(Charsets.UTF_8)))));
		assertTrue(Streams.contentEquals(new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8)),
			new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8))));
		assertFalse(Streams.contentEquals(new ByteArrayInputStream("ABCD".getBytes(Charsets.UTF_8)),
			new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8))));
		assertFalse(Streams.contentEquals(new ByteArrayInputStream("ABC".getBytes(Charsets.UTF_8)),
			new ByteArrayInputStream("ABCD".getBytes(Charsets.UTF_8))));
	}

	public void testContentEquals_Reader_Reader() throws Exception {
		{
			final StringReader input1 = new StringReader("");
			assertTrue(Streams.contentEquals(input1, input1));
		}
		{
			final StringReader input1 = new StringReader("ABC");
			assertTrue(Streams.contentEquals(input1, input1));
		}
		assertTrue(Streams.contentEquals(new StringReader(""), new StringReader("")));
		assertTrue(Streams.contentEquals(new BufferedReader(new StringReader("")), new BufferedReader(new StringReader(
			""))));
		assertTrue(Streams.contentEquals(new StringReader("ABC"), new StringReader("ABC")));
		assertFalse(Streams.contentEquals(new StringReader("ABCD"), new StringReader("ABC")));
		assertFalse(Streams.contentEquals(new StringReader("ABC"), new StringReader("ABCD")));
	}

	public void testContentEqualsIgnoreEOL() throws Exception {
		{
			final Reader input1 = new CharArrayReader("".toCharArray());
			assertTrue(Streams.contentEqualsIgnoreEOL(input1, input1));
		}
		{
			final Reader input1 = new CharArrayReader("321\r\n".toCharArray());
			assertTrue(Streams.contentEqualsIgnoreEOL(input1, input1));
		}

		Reader r1;
		Reader r2;

		r1 = new CharArrayReader("".toCharArray());
		r2 = new CharArrayReader("".toCharArray());
		assertTrue(Streams.contentEqualsIgnoreEOL(r1, r2));

		r1 = new CharArrayReader("1".toCharArray());
		r2 = new CharArrayReader("1".toCharArray());
		assertTrue(Streams.contentEqualsIgnoreEOL(r1, r2));

		r1 = new CharArrayReader("1".toCharArray());
		r2 = new CharArrayReader("2".toCharArray());
		assertFalse(Streams.contentEqualsIgnoreEOL(r1, r2));

		r1 = new CharArrayReader("123\rabc".toCharArray());
		r2 = new CharArrayReader("123\nabc".toCharArray());
		assertTrue(Streams.contentEqualsIgnoreEOL(r1, r2));

		r1 = new CharArrayReader("321".toCharArray());
		r2 = new CharArrayReader("321\r\n".toCharArray());
		assertTrue(Streams.contentEqualsIgnoreEOL(r1, r2));
	}

	// testing deprecated method
	public void testCopy_ByteArray_OutputStream() throws Exception {
		final File destination = newFile("copy8.txt");
		final FileInputStream fin = new FileInputStream(m_testFile);
		byte[] in;
		try {
			// Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
			in = Streams.toByteArray(fin);
		}
		finally {
			fin.close();
		}

		final FileOutputStream fout = new FileOutputStream(destination);
		try {
			Streams.write(in, fout);

			fout.flush();

			checkFile(destination, m_testFile);
			checkWrite(fout);
		}
		finally {
			fout.close();
		}
		deleteFile(destination);
	}

	// testing deprecated method
	public void testCopy_ByteArray_Writer() throws Exception {
		final File destination = newFile("copy7.txt");
		final FileInputStream fin = new FileInputStream(m_testFile);
		byte[] in;
		try {
			// Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
			in = Streams.toByteArray(fin);
		}
		finally {
			fin.close();
		}

		final FileWriter fout = new FileWriter(destination);
		try {
			Streams.write(in, fout);
			fout.flush();
			checkFile(destination, m_testFile);
			checkWrite(fout);
		}
		finally {
			fout.close();
		}
		deleteFile(destination);
	}

	// testing deprecated method
	public void testCopy_String_Writer() throws Exception {
		final File destination = newFile("copy6.txt");
		final FileReader fin = new FileReader(m_testFile);
		String str;
		try {
			// Create our String. Rely on testReaderToString() to make sure this is valid.
			str = Streams.toString(fin);
		}
		finally {
			fin.close();
		}

		final FileWriter fout = new FileWriter(destination);
		try {
			Streams.write(str, fout);
			fout.flush();

			checkFile(destination, m_testFile);
			checkWrite(fout);
		}
		finally {
			fout.close();
		}
		deleteFile(destination);
	}

	public void testCopyLarge_CharExtraLength() throws IOException {
		CharArrayReader is = null;
		CharArrayWriter os = null;
		try {
			// Create streams
			is = new CharArrayReader(carr);
			os = new CharArrayWriter();

			// Test our copy method
			// for extra length, it reads till EOF
			assertEquals(200, Streams.copyLarge(is, os, 0, 2000));
			final char[] oarr = os.toCharArray();

			// check that output length is correct
			assertEquals(200, oarr.length);
			// check that output data corresponds to input data
			assertEquals(1, oarr[1]);
			assertEquals(79, oarr[79]);
			assertEquals((char)-1, oarr[80]);

		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testCopyLarge_CharFullLength() throws IOException {
		CharArrayReader is = null;
		CharArrayWriter os = null;
		try {
			// Create streams
			is = new CharArrayReader(carr);
			os = new CharArrayWriter();

			// Test our copy method
			assertEquals(200, Streams.copyLarge(is, os, 0, -1));
			final char[] oarr = os.toCharArray();

			// check that output length is correct
			assertEquals(200, oarr.length);
			// check that output data corresponds to input data
			assertEquals(1, oarr[1]);
			assertEquals(79, oarr[79]);
			assertEquals((char)-1, oarr[80]);

		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testCopyLarge_CharNoSkip() throws IOException {
		CharArrayReader is = null;
		CharArrayWriter os = null;
		try {
			// Create streams
			is = new CharArrayReader(carr);
			os = new CharArrayWriter();

			// Test our copy method
			assertEquals(100, Streams.copyLarge(is, os, 0, 100));
			final char[] oarr = os.toCharArray();

			// check that output length is correct
			assertEquals(100, oarr.length);
			// check that output data corresponds to input data
			assertEquals(1, oarr[1]);
			assertEquals(79, oarr[79]);
			assertEquals((char)-1, oarr[80]);

		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testCopyLarge_CharSkip() throws IOException {
		CharArrayReader is = null;
		CharArrayWriter os = null;
		try {
			// Create streams
			is = new CharArrayReader(carr);
			os = new CharArrayWriter();

			// Test our copy method
			assertEquals(100, Streams.copyLarge(is, os, 10, 100));
			final char[] oarr = os.toCharArray();

			// check that output length is correct
			assertEquals(100, oarr.length);
			// check that output data corresponds to input data
			assertEquals(11, oarr[1]);
			assertEquals(79, oarr[69]);
			assertEquals((char)-1, oarr[70]);

		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testCopyLarge_CharSkipInvalid() throws IOException {
		CharArrayReader is = null;
		CharArrayWriter os = null;
		try {
			// Create streams
			is = new CharArrayReader(carr);
			os = new CharArrayWriter();

			// Test our copy method
			Streams.copyLarge(is, os, 1000, 100);
			fail("Should have thrown EOFException");
		}
		catch (final EOFException eofe) {
		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testCopyLarge_ExtraLength() throws IOException {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			// Create streams
			is = new ByteArrayInputStream(iarr);
			os = new ByteArrayOutputStream();

			// Test our copy method
			// for extra length, it reads till EOF
			assertEquals(200, Streams.copyLarge(is, os, 0, 2000));
			final byte[] oarr = os.toByteArray();

			// check that output length is correct
			assertEquals(200, oarr.length);
			// check that output data corresponds to input data
			assertEquals(1, oarr[1]);
			assertEquals(79, oarr[79]);
			assertEquals(-1, oarr[80]);

		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testCopyLarge_FullLength() throws IOException {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			// Create streams
			is = new ByteArrayInputStream(iarr);
			os = new ByteArrayOutputStream();

			// Test our copy method
			assertEquals(200, Streams.copyLarge(is, os, 0, -1));
			final byte[] oarr = os.toByteArray();

			// check that output length is correct
			assertEquals(200, oarr.length);
			// check that output data corresponds to input data
			assertEquals(1, oarr[1]);
			assertEquals(79, oarr[79]);
			assertEquals(-1, oarr[80]);

		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testCopyLarge_NoSkip() throws IOException {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			// Create streams
			is = new ByteArrayInputStream(iarr);
			os = new ByteArrayOutputStream();

			// Test our copy method
			assertEquals(100, Streams.copyLarge(is, os, 0, 100));
			final byte[] oarr = os.toByteArray();

			// check that output length is correct
			assertEquals(100, oarr.length);
			// check that output data corresponds to input data
			assertEquals(1, oarr[1]);
			assertEquals(79, oarr[79]);
			assertEquals(-1, oarr[80]);

		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testCopyLarge_Skip() throws IOException {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			// Create streams
			is = new ByteArrayInputStream(iarr);
			os = new ByteArrayOutputStream();

			// Test our copy method
			assertEquals(100, Streams.copyLarge(is, os, 10, 100));
			final byte[] oarr = os.toByteArray();

			// check that output length is correct
			assertEquals(100, oarr.length);
			// check that output data corresponds to input data
			assertEquals(11, oarr[1]);
			assertEquals(79, oarr[69]);
			assertEquals(-1, oarr[70]);

		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testCopyLarge_SkipInvalid() throws IOException {
		ByteArrayInputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			// Create streams
			is = new ByteArrayInputStream(iarr);
			os = new ByteArrayOutputStream();

			// Test our copy method
			Streams.copyLarge(is, os, 1000, 100);
			fail("Should have thrown EOFException");
		}
		catch (final EOFException eofe) {
		}
		finally {
			Streams.safeClose(is);
			Streams.safeClose(os);
		}
	}

	public void testRead_ReadableByteChannel() throws Exception {
		final ByteBuffer buffer = ByteBuffer.allocate(FILE_SIZE);
		final FileInputStream fileInputStream = new FileInputStream(m_testFile);
		final FileChannel input = fileInputStream.getChannel();
		try {
			assertEquals(FILE_SIZE, Streams.read(input, buffer));
			assertEquals(0, Streams.read(input, buffer));
			assertEquals(0, buffer.remaining());
			assertEquals(0, input.read(buffer));
			buffer.clear();
			try {
				Streams.readFully(input, buffer);
				fail("Should have failed with EOFxception");
			}
			catch (final EOFException expected) {
				// expected
			}
		}
		finally {
			Streams.safeClose(input);
			Streams.safeClose(fileInputStream);
		}
	}

	public void testReadFully_InputStream_ByteArray() throws Exception {
		final int size = 1027;

		final byte[] buffer = new byte[size];

		final InputStream input = new ByteArrayInputStream(new byte[size]);
		try {
			Streams.readFully(input, buffer, 0, -1);
			fail("Should have failed with IllegalArgumentException");
		}
		catch (final IllegalArgumentException expected) {
			// expected
		}
		Streams.readFully(input, buffer, 0, 0);
		Streams.readFully(input, buffer, 0, size - 1);
		try {
			Streams.readFully(input, buffer, 0, 2);
			fail("Should have failed with EOFxception");
		}
		catch (final EOFException expected) {
			// expected
		}
		Streams.safeClose(input);

	}

	public void testReadFully_InputStream_Offset() throws Exception {
		final byte[] bytes = "abcd1234".getBytes("UTF-8");
		final ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		final byte[] buffer = "wx00000000".getBytes("UTF-8");
		Streams.readFully(stream, buffer, 2, 8);
		assertEquals("wxabcd1234", new String(buffer, 0, buffer.length, "UTF-8"));
		Streams.safeClose(stream);
	}

	public void testReadFully_ReadableByteChannel() throws Exception {
		final ByteBuffer buffer = ByteBuffer.allocate(FILE_SIZE);
		final FileInputStream fileInputStream = new FileInputStream(m_testFile);
		final FileChannel input = fileInputStream.getChannel();
		try {
			Streams.readFully(input, buffer);
			assertEquals(FILE_SIZE, buffer.position());
			assertEquals(0, buffer.remaining());
			assertEquals(0, input.read(buffer));
			Streams.readFully(input, buffer);
			assertEquals(FILE_SIZE, buffer.position());
			assertEquals(0, buffer.remaining());
			assertEquals(0, input.read(buffer));
			Streams.readFully(input, buffer);
			buffer.clear();
			try {
				Streams.readFully(input, buffer);
				fail("Should have failed with EOFxception");
			}
			catch (final EOFException expected) {
				// expected
			}
		}
		finally {
			Streams.safeClose(input);
			Streams.safeClose(fileInputStream);
		}
	}

	public void testReadFully_Reader() throws Exception {
		final int size = 1027;

		final char[] buffer = new char[size];

		final Reader input = new CharArrayReader(new char[size]);
		Streams.readFully(input, buffer, 0, 0);
		Streams.readFully(input, buffer, 0, size - 3);
		try {
			Streams.readFully(input, buffer, 0, -1);
			fail("Should have failed with IllegalArgumentException");
		}
		catch (final IllegalArgumentException expected) {
			// expected
		}
		try {
			Streams.readFully(input, buffer, 0, 5);
			fail("Should have failed with EOFException");
		}
		catch (final EOFException expected) {
			// expected
		}
		Streams.safeClose(input);
	}

	public void testReadFully_Reader_Offset() throws Exception {
		final Reader reader = new StringReader("abcd1234");
		final char[] buffer = "wx00000000".toCharArray();
		Streams.readFully(reader, buffer, 2, 8);
		assertEquals("wxabcd1234", new String(buffer));
		Streams.safeClose(reader);
	}

	public void testReadLines_InputStream() throws Exception {
		final File file = newFile("lines.txt");
		InputStream in = null;
		try {
			final String[] data = new String[] { "hello", "world", "", "this is", "some text" };
			createLineBasedFile(file, data);

			in = new FileInputStream(file);
			final List<String> lines = Streams.readLines(in);
			assertEquals(Arrays.asList(data), lines);
			assertEquals(-1, in.read());
		}
		finally {
			Streams.safeClose(in);
			deleteFile(file);
		}
	}

	public void testReadLines_InputStream_String() throws Exception {
		final File file = newFile("lines.txt");
		InputStream in = null;
		try {
			final String[] data = new String[] { "hello", "/u1234", "", "this is", "some text" };
			createLineBasedFile(file, data);

			in = new FileInputStream(file);
			final List<String> lines = Streams.readLines(in, "UTF-8");
			assertEquals(Arrays.asList(data), lines);
			assertEquals(-1, in.read());
		}
		finally {
			Streams.safeClose(in);
			deleteFile(file);
		}
	}

	public void testReadLines_Reader() throws Exception {
		final File file = newFile("lines.txt");
		Reader in = null;
		try {
			final String[] data = new String[] { "hello", "/u1234", "", "this is", "some text" };
			createLineBasedFile(file, data);

			in = new InputStreamReader(new FileInputStream(file));
			final List<String> lines = Streams.readLines(in);
			assertEquals(Arrays.asList(data), lines);
			assertEquals(-1, in.read());
		}
		finally {
			Streams.safeClose(in);
			deleteFile(file);
		}
	}

	public void testSkip_FileReader() throws Exception {
		final FileReader in = new FileReader(m_testFile);
		try {
			assertEquals(FILE_SIZE - 10, Streams.skip(in, FILE_SIZE - 10));
			assertEquals(10, Streams.skip(in, 20));
			assertEquals(0, Streams.skip(in, 10));
		}
		finally {
			in.close();
		}
	}

	public void testSkip_InputStream() throws Exception {
		final InputStream in = new FileInputStream(m_testFile);
		try {
			assertEquals(FILE_SIZE - 10, Streams.skip(in, FILE_SIZE - 10));
			assertEquals(10, Streams.skip(in, 20));
			assertEquals(0, Streams.skip(in, 10));
		}
		finally {
			in.close();
		}
	}

	public void testSkip_ReadableByteChannel() throws Exception {
		final FileInputStream fileInputStream = new FileInputStream(m_testFile);
		final FileChannel fileChannel = fileInputStream.getChannel();
		try {
			assertEquals(FILE_SIZE - 10, Streams.skip(fileChannel, FILE_SIZE - 10));
			assertEquals(10, Streams.skip(fileChannel, 20));
			assertEquals(0, Streams.skip(fileChannel, 10));
		}
		finally {
			Streams.safeClose(fileChannel);
			Streams.safeClose(fileInputStream);
		}
	}

	public void testSkipFully_InputStream() throws Exception {
		final int size = 1027;

		final InputStream input = new ByteArrayInputStream(new byte[size]);
		try {
			Streams.skipFully(input, -1);
			fail("Should have failed with IllegalArgumentException");
		}
		catch (final IllegalArgumentException expected) {
			// expected
		}
		Streams.skipFully(input, 0);
		Streams.skipFully(input, size - 1);
		try {
			Streams.skipFully(input, 2);
			fail("Should have failed with IOException");
		}
		catch (final IOException expected) {
			// expected
		}
		Streams.safeClose(input);

	}

	public void testSkipFully_ReadableByteChannel() throws Exception {
		final FileInputStream fileInputStream = new FileInputStream(m_testFile);
		final FileChannel fileChannel = fileInputStream.getChannel();
		try {
			try {
				Streams.skipFully(fileChannel, -1);
				fail("Should have failed with IllegalArgumentException");
			}
			catch (final IllegalArgumentException expected) {
				// expected
			}
			Streams.skipFully(fileChannel, 0);
			Streams.skipFully(fileChannel, FILE_SIZE - 1);
			try {
				Streams.skipFully(fileChannel, 2);
				fail("Should have failed with IOException");
			}
			catch (final IOException expected) {
				// expected
			}
		}
		finally {
			Streams.safeClose(fileChannel);
			Streams.safeClose(fileInputStream);
		}
	}

	public void testSkipFully_Reader() throws Exception {
		final int size = 1027;

		final Reader input = new CharArrayReader(new char[size]);
		Streams.skipFully(input, 0);
		Streams.skipFully(input, size - 3);
		try {
			Streams.skipFully(input, -1);
			fail("Should have failed with IllegalArgumentException");
		}
		catch (final IllegalArgumentException expected) {
			// expected
		}
		try {
			Streams.skipFully(input, 5);
			fail("Should have failed with IOException");
		}
		catch (final IOException expected) {
			// expected
		}
		Streams.safeClose(input);
	}

	// testing deprecated method
	public void testStringToOutputStream() throws Exception {
		final File destination = newFile("copy5.txt");
		final FileReader fin = new FileReader(m_testFile);
		String str;
		try {
			// Create our String. Rely on testReaderToString() to make sure this is valid.
			str = Streams.toString(fin);
		}
		finally {
			fin.close();
		}

		final FileOutputStream fout = new FileOutputStream(destination);
		try {
			Streams.write(str, fout);
			// Note: this method *does* flush. It is equivalent to:
			// OutputStreamWriter _out = new OutputStreamWriter(fout);
			// CopyUtils.copy( str, _out, 4096 ); // copy( Reader, Writer, int );
			// _out.flush();
			// out = fout;
			// note: we don't flush here; this Streams method does it for us

			checkFile(destination, m_testFile);
			checkWrite(fout);
		}
		finally {
			fout.close();
		}
		deleteFile(destination);
	}

	public void testToBufferedInputStream_InputStream() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);
		try {
			final InputStream in = Streams.toBufferedInputStream(fin);
			final byte[] out = Streams.toByteArray(in);
			assertNotNull(out);
			assertEquals("Not all bytes were read", 0, fin.available());
			assertEquals("Wrong output size", FILE_SIZE, out.length);
			assertEqualContent(out, m_testFile);
		}
		finally {
			fin.close();
		}
	}

	public void testToByteArray_InputStream() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);
		try {
			final byte[] out = Streams.toByteArray(fin);
			assertNotNull(out);
			assertEquals("Not all bytes were read", 0, fin.available());
			assertEquals("Wrong output size", FILE_SIZE, out.length);
			assertEqualContent(out, m_testFile);
		}
		finally {
			fin.close();
		}
	}

	public void testToByteArray_InputStream_NegativeSize() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);

		try {
			Streams.toByteArray(fin, -1);
			fail("IllegalArgumentException excepted");
		}
		catch (final IllegalArgumentException exc) {
			assertTrue("Exception message does not start with \"Size must be equal or greater than zero\"", exc
				.getMessage().startsWith("Size must be equal or greater than zero"));
		}
		finally {
			fin.close();
		}

	}

	public void testToByteArray_InputStream_Size() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);
		try {
			final byte[] out = Streams.toByteArray(fin, m_testFile.length());
			assertNotNull(out);
			assertEquals("Not all bytes were read", 0, fin.available());
			assertEquals("Wrong output size: out.length=" + out.length + "!=" + FILE_SIZE, FILE_SIZE, out.length);
			assertEqualContent(out, m_testFile);
		}
		finally {
			fin.close();
		}
	}

	public void testToByteArray_InputStream_SizeIllegal() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);

		try {
			Streams.toByteArray(fin, m_testFile.length() + 1);
			fail("IOException excepted");
		}
		catch (final IOException exc) {
			assertTrue("Exception message does not start with \"Unexpected readed size\"",
				exc.getMessage().startsWith("Unexpected readed size"));
		}
		finally {
			fin.close();
		}

	}

	public void testToByteArray_InputStream_SizeLong() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);

		try {
			Streams.toByteArray(fin, (long)Integer.MAX_VALUE + 1);
			fail("IOException excepted");
		}
		catch (final IllegalArgumentException exc) {
			assertTrue("Exception message does not start with \"Size cannot be greater than Integer max value\"", exc
				.getMessage().startsWith("Size cannot be greater than Integer max value"));
		}
		finally {
			fin.close();
		}

	}

	public void testToByteArray_InputStream_SizeZero() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);

		try {
			final byte[] out = Streams.toByteArray(fin, 0);
			assertNotNull("Out cannot be null", out);
			assertEquals("Out length must be 0", 0, out.length);
		}
		finally {
			fin.close();
		}
	}

	public void testToByteArray_Reader() throws IOException {
		final String charsetName = "UTF-8";
		final byte[] expecteds = charsetName.getBytes(charsetName);
		byte[] actuals = Streams.toByteArray(new InputStreamReader(new ByteArrayInputStream(expecteds)));
		Assert.assertArrayEquals(expecteds, actuals);
		actuals = Streams.toByteArray(new InputStreamReader(new ByteArrayInputStream(expecteds)), charsetName);
		Assert.assertArrayEquals(expecteds, actuals);
	}

	// testing deprecated method
	public void testToByteArray_String() throws Exception {
		final FileReader fin = new FileReader(m_testFile);
		try {
			// Create our String. Rely on testReaderToString() to make sure this is valid.
			final String str = Streams.toString(fin);

			final byte[] out = Strings.getBytes(str);
			assertEqualContent(str.getBytes(), out);
		}
		finally {
			fin.close();
		}
	}

	public void testToByteArray_URI() throws Exception {
		final URI url = m_testFile.toURI();
		final byte[] actual = Streams.toByteArray(url);
		Assert.assertEquals(FILE_SIZE, actual.length);
	}

	public void testToByteArray_URL() throws Exception {
		final URL url = m_testFile.toURI().toURL();
		final byte[] actual = Streams.toByteArray(url);
		Assert.assertEquals(FILE_SIZE, actual.length);
	}

	public void testToByteArray_URLConnection() throws Exception {
		final URLConnection urlConn = m_testFile.toURI().toURL().openConnection();
		byte[] actual;
		try {
			actual = Streams.toByteArray(urlConn);
		}
		finally {
			Streams.close(urlConn);
		}
		Assert.assertEquals(FILE_SIZE, actual.length);
	}

	public void testToCharArray_InputStream() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);
		try {
			final char[] out = Streams.toCharArray(fin);
			assertNotNull(out);
			assertEquals("Not all chars were read", 0, fin.available());
			assertEquals("Wrong output size", FILE_SIZE, out.length);
			assertEqualContent(out, m_testFile);
		}
		finally {
			fin.close();
		}
	}

	public void testToCharArray_InputStream_CharsetName() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);
		try {
			final char[] out = Streams.toCharArray(fin, "UTF-8");
			assertNotNull(out);
			assertEquals("Not all chars were read", 0, fin.available());
			assertEquals("Wrong output size", FILE_SIZE, out.length);
			assertEqualContent(out, m_testFile);
		}
		finally {
			fin.close();
		}
	}

	public void testToCharArray_Reader() throws Exception {
		final FileReader fr = new FileReader(m_testFile);
		try {
			final char[] out = Streams.toCharArray(fr);
			assertNotNull(out);
			assertEquals("Wrong output size", FILE_SIZE, out.length);
			assertEqualContent(out, m_testFile);
		}
		finally {
			fr.close();
		}
	}

	/**
	 * Test for {@link Streams#toInputStream(CharSequence)} and
	 * {@link Streams#toInputStream(CharSequence, String)}. Note, this test utilizes on
	 * {@link Streams#toByteArray(java.io.InputStream)} and so relies on
	 * {@link #testToByteArray_InputStream()} to ensure this method functions correctly.
	 * 
	 * @throws Exception on error
	 */
	public void testToInputStream_CharSequence() throws Exception {
		final CharSequence csq = new StringBuilder("Abc123Xyz!");
		InputStream inStream = Streams.toInputStream(csq); // deliberately testing deprecated method
		byte[] bytes = Streams.toByteArray(inStream);
		assertEqualContent(csq.toString().getBytes(), bytes);
		inStream = Streams.toInputStream(csq, (String)null);
		bytes = Streams.toByteArray(inStream);
		assertEqualContent(csq.toString().getBytes(), bytes);
		inStream = Streams.toInputStream(csq, "UTF-8");
		bytes = Streams.toByteArray(inStream);
		assertEqualContent(csq.toString().getBytes("UTF-8"), bytes);
	}

	// Tests from IO-305

	/**
	 * Test for {@link Streams#toInputStream(String)} and
	 * {@link Streams#toInputStream(String, String)}. Note, this test utilizes on
	 * {@link Streams#toByteArray(java.io.InputStream)} and so relies on
	 * {@link #testToByteArray_InputStream()} to ensure this method functions correctly.
	 * 
	 * @throws Exception on error
	 */
	@SuppressWarnings("javadoc")
	// deliberately testing deprecated method
	public void testToInputStream_String() throws Exception {
		final String str = "Abc123Xyz!";
		InputStream inStream = Streams.toInputStream(str);
		byte[] bytes = Streams.toByteArray(inStream);
		assertEqualContent(str.getBytes(), bytes);
		inStream = Streams.toInputStream(str, (String)null);
		bytes = Streams.toByteArray(inStream);
		assertEqualContent(str.getBytes(), bytes);
		inStream = Streams.toInputStream(str, "UTF-8");
		bytes = Streams.toByteArray(inStream);
		assertEqualContent(str.getBytes("UTF-8"), bytes);
	}

	// testing deprecated method
	public void testToString_ByteArray() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);
		try {
			final byte[] in = Streams.toByteArray(fin);
			// Create our byte[]. Rely on testInputStreamToByteArray() to make sure this is valid.
			final String str = Streams.toString(in);
			assertEqualContent(in, str.getBytes());
		}
		finally {
			fin.close();
		}
	}

	public void testToString_InputStream() throws Exception {
		final FileInputStream fin = new FileInputStream(m_testFile);
		try {
			final String out = Streams.toString(fin);
			assertNotNull(out);
			assertEquals("Not all bytes were read", 0, fin.available());
			assertEquals("Wrong output size", FILE_SIZE, out.length());
		}
		finally {
			fin.close();
		}
	}

	public void testToString_Reader() throws Exception {
		final FileReader fin = new FileReader(m_testFile);
		try {
			final String out = Streams.toString(fin);
			assertNotNull(out);
			assertEquals("Wrong output size", FILE_SIZE, out.length());
		}
		finally {
			fin.close();
		}
	}

	public void testToString_URI() throws Exception {
		final URI url = m_testFile.toURI();
		final String out = Streams.toString(url);
		assertNotNull(out);
		assertEquals("Wrong output size", FILE_SIZE, out.length());
	}

	private void testToString_URI(final String encoding) throws Exception {
		final URI uri = m_testFile.toURI();
		final String out = Streams.toString(uri, encoding);
		assertNotNull(out);
		assertEquals("Wrong output size", FILE_SIZE, out.length());
	}

	public void testToString_URI_CharsetName() throws Exception {
		testToString_URI("US-ASCII");
	}

	public void testToString_URI_CharsetNameNull() throws Exception {
		testToString_URI(null);
	}

	public void testToString_URL() throws Exception {
		final URL url = m_testFile.toURI().toURL();
		final String out = Streams.toString(url);
		assertNotNull(out);
		assertEquals("Wrong output size", FILE_SIZE, out.length());
	}

	private void testToString_URL(final String encoding) throws Exception {
		final URL url = m_testFile.toURI().toURL();
		final String out = Streams.toString(url, encoding);
		assertNotNull(out);
		assertEquals("Wrong output size", FILE_SIZE, out.length());
	}

	public void testToString_URL_CharsetName() throws Exception {
		testToString_URL("US-ASCII");
	}

	public void testToString_URL_CharsetNameNull() throws Exception {
		testToString_URL(null);
	}

	public void testAsBufferedNull() {
		try {
			Streams.buffer((InputStream)null);
			fail("Expected NullPointerException");
		}
		catch (NullPointerException npe) {
			// expected
		}
		try {
			Streams.buffer((OutputStream)null);
			fail("Expected NullPointerException");
		}
		catch (NullPointerException npe) {
			// expected
		}
		try {
			Streams.buffer((Reader)null);
			fail("Expected NullPointerException");
		}
		catch (NullPointerException npe) {
			// expected
		}
		try {
			Streams.buffer((Writer)null);
			fail("Expected NullPointerException");
		}
		catch (NullPointerException npe) {
			// expected
		}
	}

	public void testAsBufferedInputStream() {
		InputStream is = new InputStream() {
			@Override
			public int read() throws IOException {
				return 0;
			}
		};
		final BufferedInputStream bis = Streams.buffer(is);
		assertNotSame(is, bis);
		assertSame(bis, Streams.buffer(bis));
	}

	public void testAsBufferedOutputStream() {
		OutputStream is = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		};
		final BufferedOutputStream bis = Streams.buffer(is);
		assertNotSame(is, bis);
		assertSame(bis, Streams.buffer(bis));
	}

	public void testAsBufferedReader() {
		Reader is = new Reader() {
			@Override
			public int read(char[] cbuf, int off, int len) throws IOException {
				return 0;
			}

			@Override
			public void close() throws IOException {
			}
		};
		final BufferedReader bis = Streams.buffer(is);
		assertNotSame(is, bis);
		assertSame(bis, Streams.buffer(bis));
	}

	public void testAsBufferedWriter() {
		Writer is = new Writer() {
			@Override
			public void write(int b) throws IOException {
			}

			@Override
			public void write(char[] cbuf, int off, int len) throws IOException {
			}

			@Override
			public void flush() throws IOException {
			}

			@Override
			public void close() throws IOException {
			}
		};
		final BufferedWriter bis = Streams.buffer(is);
		assertNotSame(is, bis);
		assertSame(bis, Streams.buffer(bis));
	}
}
