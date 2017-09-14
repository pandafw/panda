package panda.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;

import panda.io.stream.BOMInputStream;
import panda.io.stream.ByteArrayOutputStream;
import panda.io.stream.ClosedInputStream;
import panda.io.stream.ClosedOutputStream;
import panda.io.stream.NullOutputStream;
import panda.io.stream.StringBuilderWriter;
import panda.io.stream.WriterOutputStream;
import panda.lang.Arrays;
import panda.lang.CharSequences;
import panda.lang.Charsets;
import panda.lang.ClassLoaders;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.lang.Threads;

/**
 * I/O Utilities class.
 */
public class Streams {
	/**
	 * End of Stream
	 */
	public static final int EOF = -1;

	/**
	 * The Unix line separator string.
	 */
	public static final String LINE_SEPARATOR_UNIX = Strings.LF;

	/**
	 * The Windows line separator string.
	 */
	public static final String LINE_SEPARATOR_WINDOWS = Strings.CRLF;

	/**
	 * The system line separator string.
	 */
	public static final String LINE_SEPARATOR = Systems.LINE_SEPARATOR;

	/**
	 * The default buffer size ({@value} ) to use for {@link #copyLarge(InputStream, OutputStream)}
	 * and {@link #copyLarge(Reader, Appendable)}
	 */
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	/**
	 * The default buffer size to use for the skip() methods.
	 */
	private static final int SKIP_BUFFER_SIZE = 2048;

	// Allocated in the relevant skip method if necessary.
	/*
	 * N.B. no need to synchronize these because: - we don't care if the buffer is created multiple
	 * times (the data is ignored) - we always use the same size buffer, so if it it is recreated it
	 * will still be OK (if the buffer size were variable, we would need to synch. to ensure some
	 * other thread did not create a smaller one)
	 */
	private static char[] SKIP_CHAR_BUFFER;
	private static byte[] SKIP_BYTE_BUFFER;

	/**
	 * Copy the contents of the given input File to the given output File.
	 * 
	 * @param in the file to copy from
	 * @param os the output stream to copy to
	 * @return the number of bytes copied
	 * @throws IOException in case of I/O errors
	 */
	public static int copy(File in, OutputStream os) throws IOException {
		InputStream is = null;

		try {
			is = new FileInputStream(in);
			return copy(is, os);
		}
		finally {
			safeClose(is);
		}
	}

	/**
	 * Returns a copy of the specified stream. If the specified stream supports marking, it will be
	 * reset after the copy.
	 * 
	 * @param sourceStream the stream to copy
	 * @return a copy of the stream
	 * @throws IOException if an IO error occurred
	 */
	public static InputStream copy(InputStream sourceStream) throws IOException {
		if (sourceStream.markSupported())
			sourceStream.mark(Integer.MAX_VALUE);
		byte[] sourceData = toByteArray(sourceStream);
		if (sourceStream.markSupported())
			sourceStream.reset();
		return new ByteArrayInputStream(sourceData);
	}

	/**
	 * Returns a copy of the specified reader. If the specified reader supports marking, it will be
	 * reset after the copy.
	 * 
	 * @param sourceReader the stream to reader
	 * @return a copy of the reader
	 * @throws IOException if an IO error occurred
	 */
	public static Reader copy(Reader sourceReader) throws IOException {
		if (sourceReader.markSupported())
			sourceReader.mark(Integer.MAX_VALUE);
		String sourceData = toString(sourceReader);
		if (sourceReader.markSupported())
			sourceReader.reset();
		return new StringReader(sourceData);
	}

	/**
	 * Write the contents of the given byte array to the given output File.
	 * 
	 * @param in the byte array to copy from
	 * @param out the file to copy to
	 * @throws IOException in case of I/O errors
	 */
	public static void write(byte[] in, File out) throws IOException {
		OutputStream os = null;

		try {
			os = new FileOutputStream(out);
			write(in, os);
		}
		finally {
			safeClose(os);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Closes a URLConnection.
	 * 
	 * @param conn the connection to close.
	 */
	public static void close(final URLConnection conn) {
		if (conn instanceof HttpURLConnection) {
			((HttpURLConnection)conn).disconnect();
		}
	}

	/**
	 * Unconditionally close a <code>Closeable</code>.
	 * <p>
	 * Equivalent to {@link Closeable#close()}, except any exceptions will be ignored. This is
	 * typically used in finally blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * Closeable closeable = null;
	 * try {
	 * 	closeable = new FileReader(&quot;foo.txt&quot;);
	 * 	// process closeable
	 * 	closeable.close();
	 * }
	 * catch (Exception e) {
	 * 	// error handling
	 * }
	 * finally {
	 * 	Streams.safeClose(closeable);
	 * }
	 * </pre>
	 * 
	 * @param closeable the object to close, may be null or already closed
	 */
	public static void safeClose(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		}
		catch (IOException ioe) {
			// ignore
		}
	}

	public static void safeClose(AutoCloseable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		}
		catch (Exception ioe) {
			// ignore
		}
	}

	/**
	 * Closes a <code>Selector</code> unconditionally.
	 * <p>
	 * Equivalent to {@link Selector#close()}, except any exceptions will be ignored. This is
	 * typically used in finally blocks.
	 * <p>
	 * Example code:
	 * 
	 * <pre>
	 * Selector selector = null;
	 * try {
	 * 	selector = Selector.open();
	 * 	// process socket
	 * 
	 * }
	 * catch (Exception e) {
	 * 	// error handling
	 * }
	 * finally {
	 * 	Streams.safeClose(selector);
	 * }
	 * </pre>
	 * 
	 * @param selector the Selector to close, may be null or already closed
	 */
	public static void safeClose(final Selector selector) {
		if (selector != null) {
			try {
				selector.close();
			}
			catch (final IOException ioe) {
				// ignored
			}
		}
	}

	/**
	 * safe flush
	 * 
	 * @param flushable a flushable object
	 */
	public static void safeFlush(Flushable flushable) {
		if (null != flushable) {
			try {
				flushable.flush();
			}
			catch (IOException e) {
			}
		}
	}

	/**
	 * Fetches entire contents of an <code>InputStream</code> and represent same data as result
	 * InputStream.
	 * <p>
	 * This method is useful where,
	 * <ul>
	 * <li>Source InputStream is slow.</li>
	 * <li>It has network resources associated, so we cannot keep it open for long time.</li>
	 * <li>It has network timeout associated.</li>
	 * </ul>
	 * It can be used in favor of {@link #toByteArray(InputStream)}, since it avoids unnecessary
	 * allocation and copy of byte[].<br>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input Stream to be fully buffered.
	 * @return A fully buffered stream.
	 * @throws IOException if an I/O error occurs
	 */
	public static InputStream toBufferedInputStream(final InputStream input) throws IOException {
		return ByteArrayOutputStream.toBufferedInputStream(input);
	}

	/**
	 * Returns the given reader if it is a {@link BufferedReader}, otherwise creates a
	 * toBufferedReader for the given reader.
	 * 
	 * @param reader the reader to wrap or return
	 * @return the given reader or a new {@link BufferedReader} for the given reader
	 */
	public static BufferedReader toBufferedReader(final Reader reader) {
		return reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
	}

	/**
	 * Returns the given reader if it is already a {@link BufferedReader}, otherwise creates a
	 * BufferedReader from the given reader.
	 * 
	 * @param reader the reader to wrap or return (not null)
	 * @return the given reader or a new {@link BufferedReader} for the given reader
	 * @throws NullPointerException if the input parameter is null
	 */
	public static BufferedReader buffer(final Reader reader) {
		return reader instanceof BufferedReader ? (BufferedReader)reader : new BufferedReader(reader);
	}

	/**
	 * Returns the given Writer if it is already a {@link BufferedWriter}, otherwise creates a
	 * BufferedWriter from the given Writer.
	 * 
	 * @param writer the Writer to wrap or return (not null)
	 * @return the given Writer or a new {@link BufferedWriter} for the given Writer
	 * @throws NullPointerException if the input parameter is null
	 */
	public static BufferedWriter buffer(final Writer writer) {
		return writer instanceof BufferedWriter ? (BufferedWriter)writer : new BufferedWriter(writer);
	}

	/**
	 * Returns the given OutputStream if it is already a {@link BufferedOutputStream}, otherwise
	 * creates a BufferedOutputStream from the given OutputStream.
	 * 
	 * @param outputStream the OutputStream to wrap or return (not null)
	 * @return the given OutputStream or a new {@link BufferedOutputStream} for the given
	 *         OutputStream
	 * @throws NullPointerException if the input parameter is null
	 */
	public static BufferedOutputStream buffer(final OutputStream outputStream) {
		// reject null early on rather than waiting for IO operation to fail
		if (outputStream == null) { // not checked by BufferedOutputStream
			throw new NullPointerException();
		}
		return outputStream instanceof BufferedOutputStream ? (BufferedOutputStream)outputStream : new BufferedOutputStream(
			outputStream);
	}

	/**
	 * Returns the given InputStream if it is already a {@link BufferedInputStream}, otherwise
	 * creates a BufferedInputStream from the given InputStream.
	 * 
	 * @param inputStream the InputStream to wrap or return (not null)
	 * @return the given InputStream or a new {@link BufferedInputStream} for the given InputStream
	 * @throws NullPointerException if the input parameter is null
	 */
	public static BufferedInputStream buffer(final InputStream inputStream) {
		// reject null early on rather than waiting for IO operation to fail
		if (inputStream == null) { // not checked by BufferedInputStream
			throw new NullPointerException();
		}
		return inputStream instanceof BufferedInputStream ? (BufferedInputStream)inputStream : new BufferedInputStream(
			inputStream);
	}

	// -----------------------------------------------------------------------
	/**
	 * Returns the given OutputStream if it is already a {@link GZIPOutputStream}, otherwise
	 * creates a GZIPOutputStream from the given OutputStream.
	 * 
	 * @param outputStream the OutputStream to wrap or return (not null)
	 * @return the given OutputStream or a new {@link GZIPOutputStream} for the given
	 *         OutputStream
	 * @throws IOException If an I/O error has occurred
	 * @throws NullPointerException if the input parameter is null
	 */
	public static GZIPOutputStream gzip(final OutputStream outputStream) throws IOException {
		// reject null early on rather than waiting for IO operation to fail
		if (outputStream == null) {
			throw new NullPointerException();
		}
		return outputStream instanceof GZIPOutputStream ? (GZIPOutputStream)outputStream : new GZIPOutputStream(
			outputStream);
	}

	/**
	 * Returns the given InputStream if it is already a {@link GZIPInputStream}, otherwise
	 * creates a GZIPInputStream from the given InputStream.
	 * 
	 * @param inputStream the InputStream to wrap or return (not null)
	 * @return the given InputStream or a new {@link GZIPInputStream} for the given InputStream
	 * @throws IOException If an I/O error has occurred
	 * @throws NullPointerException if the input parameter is null
	 */
	public static GZIPInputStream gzip(final InputStream inputStream) throws IOException {
		// reject null early on rather than waiting for IO operation to fail
		if (inputStream == null) {
			throw new NullPointerException();
		}
		return inputStream instanceof GZIPInputStream ? (GZIPInputStream)inputStream : new GZIPInputStream(
			inputStream);
	}

	/**
	 * Returns the given InputStream if it is already a {@link ZipInputStream}, otherwise
	 * creates a ZipInputStream from the given InputStream.
	 * 
	 * @param inputStream the InputStream to wrap or return (not null)
	 * @return the given InputStream or a new {@link ZipInputStream} for the given InputStream
	 * @throws IOException If an I/O error has occurred
	 * @throws NullPointerException if the input parameter is null
	 */
	public static ZipInputStream zip(final InputStream inputStream) throws IOException {
		// reject null early on rather than waiting for IO operation to fail
		if (inputStream == null) {
			throw new NullPointerException();
		}
		return inputStream instanceof ZipInputStream ? (ZipInputStream)inputStream : new ZipInputStream(
			inputStream);
	}

	// read toByteArray
	// -----------------------------------------------------------------------
	/**
	 * read file content to byte array
	 * 
	 * @param file file
	 * @return byte array
	 * @throws FileNotFoundException if file not found
	 * @throws IOException in case of I/O errors
	 */
	public static byte[] toByteArray(File file) throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(file);
		try {
			byte[] b = toByteArray(fis);
			return b;
		}
		finally {
			safeClose(fis);
		}
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested byte array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] toByteArray(final InputStream input) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	/**
	 * Get contents of an <code>InputStream</code> as a <code>byte[]</code>. Use this method instead
	 * of <code>toByteArray(InputStream)</code> when <code>InputStream</code> size is known.
	 * <b>NOTE:</b> the method checks that the length can safely be cast to an int without
	 * truncation before using {@link Streams#toByteArray(java.io.InputStream, int)} to read into
	 * the byte array. (Arrays can have no more than Integer.MAX_VALUE entries anyway)
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param size the size of <code>InputStream</code>
	 * @return the requested byte array
	 * @throws IOException if an I/O error occurs or <code>InputStream</code> size differ from
	 *             parameter size
	 * @throws IllegalArgumentException if size is less than zero or size is greater than
	 *             Integer.MAX_VALUE
	 * @see Streams#toByteArray(java.io.InputStream, int)
	 */
	public static byte[] toByteArray(final InputStream input, final long size) throws IOException {

		if (size > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
		}

		return toByteArray(input, (int)size);
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a <code>byte[]</code>. Use this method
	 * instead of <code>toByteArray(InputStream)</code> when <code>InputStream</code> size is known
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param size the size of <code>InputStream</code>
	 * @return the requested byte array
	 * @throws IOException if an I/O error occurs or <code>InputStream</code> size differ from
	 *             parameter size
	 * @throws IllegalArgumentException if size is less than zero
	 */
	public static byte[] toByteArray(final InputStream input, final int size) throws IOException {

		if (size < 0) {
			throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
		}

		if (size == 0) {
			return Arrays.EMPTY_BYTE_ARRAY;
		}

		final byte[] data = new byte[size];
		int offset = 0;
		int readed;

		while (offset < size && (readed = input.read(data, offset, size - offset)) != EOF) {
			offset += readed;
		}

		if (offset != size) {
			throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
		}

		return data;
	}

	/**
	 * Get the contents of a <code>Reader</code> as a <code>byte[]</code> using the default
	 * character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @return the requested byte array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] toByteArray(Reader input) throws IOException {
		return toByteArray(input, Charset.defaultCharset());
	}

	/**
	 * Get the contents of a <code>Reader</code> as a <code>byte[]</code> using the specified
	 * character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested byte array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static byte[] toByteArray(final Reader input, final Charset encoding) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output, encoding);
		return output.toByteArray();
	}

	/**
	 * Gets the contents of a <code>Reader</code> as a <code>byte[]</code> using the specified
	 * character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested byte array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 * @throws UnsupportedCharsetException thrown instead of {@link UnsupportedEncodingException} in
	 *             version 2.2 if the encoding is not supported.
	 */
	public static byte[] toByteArray(final Reader input, final String encoding) throws IOException {
		return toByteArray(input, Charsets.toCharset(encoding));
	}

	/**
	 * Gets the contents of a <code>URI</code> as a <code>byte[]</code>.
	 * 
	 * @param uri the <code>URI</code> to read
	 * @return the requested byte array
	 * @throws NullPointerException if the uri is null
	 * @throws IOException if an I/O exception occurs
	 */
	public static byte[] toByteArray(final URI uri) throws IOException {
		return toByteArray(uri.toURL());
	}

	/**
	 * Gets the contents of a <code>URL</code> as a <code>byte[]</code>.
	 * 
	 * @param url the <code>URL</code> to read
	 * @return the requested byte array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O exception occurs
	 */
	public static byte[] toByteArray(final URL url) throws IOException {
		final URLConnection conn = url.openConnection();
		try {
			return toByteArray(conn);
		}
		finally {
			close(conn);
		}
	}

	/**
	 * Gets the contents of a <code>URLConnection</code> as a <code>byte[]</code>.
	 * 
	 * @param urlConn the <code>URLConnection</code> to read
	 * @return the requested byte array
	 * @throws NullPointerException if the urlConn is null
	 * @throws IOException if an I/O exception occurs
	 */
	public static byte[] toByteArray(final URLConnection urlConn) throws IOException {
		final InputStream inputStream = urlConn.getInputStream();
		try {
			return toByteArray(inputStream);
		}
		finally {
			inputStream.close();
		}
	}

	// read char[]
	// -----------------------------------------------------------------------
	/**
	 * Get the contents of an <code>InputStream</code> as a character array using the detected
	 * character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested character array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static char[] toCharArray(final InputStream input) throws IOException {
		BOMInputStream bis = toBOMInputStream(input);
		Charset cs = bis.hasBOM() ? bis.getBOMCharset() : Charset.defaultCharset();
		return toCharArray(bis, cs);
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a character array using the specified
	 * character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param is the <code>InputStream</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested character array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static char[] toCharArray(final InputStream is, final Charset encoding) throws IOException {
		final CharArrayWriter output = new CharArrayWriter();
		copy(is, output, encoding);
		return output.toCharArray();
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a character array using the specified
	 * character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param is the <code>InputStream</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested character array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static char[] toCharArray(final InputStream is, final String encoding) throws IOException {
		return toCharArray(is, Charsets.toCharset(encoding));
	}

	/**
	 * Get the contents of a <code>Reader</code> as a character array.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @return the requested character array
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static char[] toCharArray(final Reader input) throws IOException {
		final CharArrayWriter sw = new CharArrayWriter();
		copy(input, sw);
		return sw.toCharArray();
	}

	// read toString
	// -----------------------------------------------------------------------
	/**
	 * Get the contents of an <code>InputStream</code> as a String using the detected character
	 * encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static String toStringBom(InputStream input) throws IOException {
		BOMInputStream bis = toBOMInputStream(input);
		Charset cs = bis.hasBOM() ? bis.getBOMCharset() : Charset.defaultCharset();
		return toString(bis, cs);
	}

	/**
	 * Convert input stream to BOMInputStream
	 * @param input the input stream
	 * @return a new BOMInputStream
	 */
	public static BOMInputStream toBOMInputStream(InputStream input) {
		if (input instanceof BOMInputStream) {
			return ((BOMInputStream)input);
		}
		return new BOMInputStream(input);
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a String using the default character
	 * encoding.
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static String toString(final InputStream input) throws IOException {
		final StringBuilderWriter sw = new StringBuilderWriter();
		copy(input, sw, Charset.defaultCharset());
		return sw.toString();
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a String using the specified character
	 * encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static String toString(final InputStream input, final Charset encoding) throws IOException {
		final StringBuilderWriter sw = new StringBuilderWriter();
		copy(input, sw, encoding);
		return sw.toString();
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a String using the specified character
	 * encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static String toString(final InputStream input, final String encoding) throws IOException {
		return toString(input, Charsets.toCharset(encoding));
	}

	/**
	 * Get the contents of a <code>Reader</code> as a String.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static String toString(final Reader input) throws IOException {
		final StringBuilderWriter sw = new StringBuilderWriter();
		copy(input, sw);
		return sw.toString();
	}

	/**
	 * Gets the contents at the given URI.
	 * 
	 * @param uri The URI source.
	 * @return The contents of the URL as a String.
	 * @throws IOException if an I/O exception occurs.
	 */
	public static String toString(final URI uri) throws IOException {
		return toString(uri.toURL());
	}

	/**
	 * Gets the contents at the given URI.
	 * 
	 * @param uri The URI source.
	 * @param encoding The encoding name for the URL contents.
	 * @return The contents of the URL as a String.
	 * @throws IOException if an I/O exception occurs.
	 */
	public static String toString(final URI uri, final Charset encoding) throws IOException {
		return toString(uri.toURL(), Charsets.toCharset(encoding));
	}

	/**
	 * Gets the contents at the given URI.
	 * 
	 * @param uri The URI source.
	 * @param encoding The encoding name for the URL contents.
	 * @return The contents of the URL as a String.
	 * @throws IOException if an I/O exception occurs.
	 */
	public static String toString(final URI uri, final String encoding) throws IOException {
		return toString(uri.toURL(), Charsets.toCharset(encoding));
	}

	/**
	 * Gets the contents at the given URL.
	 * 
	 * @param url The URL source.
	 * @return The contents of the URL as a String.
	 * @throws IOException if an I/O exception occurs.
	 */
	public static String toString(final URL url) throws IOException {
		URLConnection urlc = url.openConnection();
		urlc.connect();

		String charset = getCharsetFromContentTypeString(urlc.getHeaderField("Content-Type"));
		if (charset == null) {
			charset = Charsets.UTF_8;
		}

		InputStream is = urlc.getInputStream();
		try {
			return toString(is, charset);
		}
		finally {
			is.close();
		}
	}

	public static String getCharsetFromContentTypeString(String contentType) {
		if (contentType != null) {
			int position = Strings.indexOfIgnoreCase(contentType, "charset=");
			if (position > 0) {
				return Strings.trim(contentType.substring(position + 8));
			}
		}
		return null;
	}

	/**
	 * Gets the contents at the given URL.
	 * 
	 * @param url The URL source.
	 * @param encoding The encoding name for the URL contents.
	 * @return The contents of the URL as a String.
	 * @throws IOException if an I/O exception occurs.
	 */
	public static String toString(final URL url, final Charset encoding) throws IOException {
		final InputStream inputStream = url.openStream();
		try {
			return toString(inputStream, encoding);
		}
		finally {
			inputStream.close();
		}
	}

	/**
	 * Gets the contents at the given URL.
	 * 
	 * @param url The URL source.
	 * @param encoding The encoding name for the URL contents.
	 * @return The contents of the URL as a String.
	 * @throws IOException if an I/O exception occurs.
	 */
	public static String toString(final URL url, final String encoding) throws IOException {
		return toString(url, Charsets.toCharset(encoding));
	}

	/**
	 * Gets the contents of a <code>byte[]</code> as a String using the default character encoding
	 * of the platform.
	 * 
	 * @param input the byte array to read from
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs (never occurs)
	 */
	public static String toString(final byte[] input) throws IOException {
		// make explicit the use of the default charset
		return new String(input);
	}

	/**
	 * Gets the contents of a <code>byte[]</code> as a String using the specified character
	 * encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * 
	 * @param input the byte array to read from
	 * @param encoding the encoding to use, null means platform default
	 * @return the requested String
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs (never occurs)
	 */
	public static String toString(final byte[] input, final String encoding) throws IOException {
		return new String(input, Charsets.toCharset(encoding));
	}

	// readLines
	// -----------------------------------------------------------------------
	/**
	 * Get the contents of an <code>InputStream</code> as a list of Strings, one entry per line,
	 * using the default character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from, not null
	 * @return the list of Strings, never null
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static List<String> readLines(final InputStream input) throws IOException {
		return readLines(input, Charset.defaultCharset());
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a list of Strings, one entry per line,
	 * using the specified character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from, not null
	 * @param encoding the encoding to use, null means platform default
	 * @return the list of Strings, never null
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static List<String> readLines(final InputStream input, final Charset encoding) throws IOException {
		final InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(encoding));
		return readLines(reader);
	}

	/**
	 * Get the contents of an <code>InputStream</code> as a list of Strings, one entry per line,
	 * using the specified character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * 
	 * @param input the <code>InputStream</code> to read from, not null
	 * @param encoding the encoding to use, null means platform default
	 * @return the list of Strings, never null
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static List<String> readLines(final InputStream input, final String encoding) throws IOException {
		return readLines(input, Charsets.toCharset(encoding));
	}

	/**
	 * Get the contents of a <code>Reader</code> as a list of Strings, one entry per line.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * 
	 * @param input the <code>Reader</code> to read from, not null
	 * @return the list of Strings, never null
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static List<String> readLines(final Reader input) throws IOException {
		final BufferedReader reader = toBufferedReader(input);
		final List<String> list = new ArrayList<String>();
		String line = reader.readLine();
		while (line != null) {
			list.add(line);
			line = reader.readLine();
		}
		return list;
	}

	// lineIterator
	// -----------------------------------------------------------------------
	/**
	 * Return an Iterator for the lines in a <code>Reader</code>.
	 * <p>
	 * <code>LineIterator</code> holds a reference to the open <code>Reader</code> specified here.
	 * When you have finished with the iterator you should close the reader to free internal
	 * resources. This can be done by closing the reader directly, or by calling
	 * {@link LineIterator#close()}.
	 * <p>
	 * The recommended usage pattern is:
	 * 
	 * <pre>
	 * try {
	 * 	LineIterator it = Streams.lineIterator(reader);
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// / do something with line
	 * 	}
	 * }
	 * finally {
	 * 	Streams.safeClose(reader);
	 * }
	 * </pre>
	 * 
	 * @param reader the <code>Reader</code> to read from, not null
	 * @return an Iterator of the lines in the reader, never null
	 * @throws IllegalArgumentException if the reader is null
	 */
	public static LineIterator lineIterator(final Reader reader) {
		return new LineIterator(reader);
	}

	/**
	 * Return an Iterator for the lines in an <code>InputStream</code>, using the character encoding
	 * specified (or default encoding if null).
	 * <p>
	 * <code>LineIterator</code> holds a reference to the open <code>InputStream</code> specified
	 * here. When you have finished with the iterator you should close the stream to free internal
	 * resources. This can be done by closing the stream directly, or by calling
	 * {@link LineIterator#close()}.
	 * <p>
	 * The recommended usage pattern is:
	 * 
	 * <pre>
	 * try {
	 * 	LineIterator it = Streams.lineIterator(stream, &quot;UTF-8&quot;);
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// / do something with line
	 * 	}
	 * }
	 * finally {
	 * 	Streams.safeClose(stream);
	 * }
	 * </pre>
	 * 
	 * @param input the <code>InputStream</code> to read from, not null
	 * @param encoding the encoding to use, null means platform default
	 * @return an Iterator of the lines in the reader, never null
	 * @throws IllegalArgumentException if the input is null
	 * @throws IOException if an I/O error occurs, such as if the encoding is invalid
	 */
	public static LineIterator lineIterator(final InputStream input, final Charset encoding) throws IOException {
		return new LineIterator(new InputStreamReader(input, Charsets.toCharset(encoding)));
	}

	/**
	 * Returns an Iterator for the lines in an <code>InputStream</code>, using the character
	 * encoding specified (or default encoding if null).
	 * <p>
	 * <code>LineIterator</code> holds a reference to the open <code>InputStream</code> specified
	 * here. When you have finished with the iterator you should close the stream to free internal
	 * resources. This can be done by closing the stream directly, or by calling
	 * {@link LineIterator#close()}.
	 * <p>
	 * The recommended usage pattern is:
	 * 
	 * <pre>
	 * try {
	 * 	LineIterator it = Streams.lineIterator(stream, &quot;UTF-8&quot;);
	 * 	while (it.hasNext()) {
	 * 		String line = it.nextLine();
	 * 		// / do something with line
	 * 	}
	 * }
	 * finally {
	 * 	Streams.safeClose(stream);
	 * }
	 * </pre>
	 * 
	 * @param input the <code>InputStream</code> to read from, not null
	 * @param encoding the encoding to use, null means platform default
	 * @return an Iterator of the lines in the reader, never null
	 * @throws IllegalArgumentException if the input is null
	 * @throws IOException if an I/O error occurs, such as if the encoding is invalid
	 * @throws UnsupportedCharsetException thrown instead of {@link UnsupportedEncodingException} in
	 *             version 2.2 if the encoding is not supported.
	 */
	public static LineIterator lineIterator(final InputStream input, final String encoding) throws IOException {
		return lineIterator(input, Charsets.toCharset(encoding));
	}

	// -----------------------------------------------------------------------
	/**
	 * Converts the specified CharSequence to an input stream, encoded as bytes using the default
	 * character encoding of the platform.
	 * 
	 * @param input the CharSequence to convert
	 * @return an input stream
	 */
	public static InputStream toInputStream(final CharSequence input) {
		return toInputStream(input, Charset.defaultCharset());
	}

	/**
	 * Converts the specified CharSequence to an input stream, encoded as bytes using the specified
	 * character encoding.
	 * 
	 * @param input the CharSequence to convert
	 * @param encoding the encoding to use, null means platform default
	 * @return an input stream
	 */
	public static InputStream toInputStream(final CharSequence input, final Charset encoding) {
		return toInputStream(input.toString(), encoding);
	}

	/**
	 * Convert the specified CharSequence to an input stream, encoded as bytes using the specified
	 * character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * 
	 * @param input the CharSequence to convert
	 * @param encoding the encoding to use, null means platform default
	 * @return an input stream
	 */
	public static InputStream toInputStream(final CharSequence input, final String encoding) {
		return toInputStream(input, Charsets.toCharset(encoding));
	}

	// -----------------------------------------------------------------------
	/**
	 * Load file or class name to InputStream.
	 * 
	 * @param path the file name or class name to load
	 * @return an input stream
     * @exception  FileNotFoundException  if the file does not exist,
     *                   is a directory rather than a regular file,
     *                   or for some other reason cannot be opened for
     *                   reading.
	 */
	public static InputStream getStream(final String path) throws FileNotFoundException {
		InputStream is = null;
		if (Files.isFile(path)) {
			is = new FileInputStream(path);
		}
		else {
			is = ClassLoaders.getResourceAsStream(path);
		}
		if (is == null) {
			throw new FileNotFoundException("Failed to find file: " + path);
		}
		return is;
	}

	// -----------------------------------------------------------------------
	/**
	 * Convert the specified data to an input stream.
	 * 
	 * @param input the data to convert
	 * @return an input stream
	 */
	public static InputStream toInputStream(final byte[] input) {
		return new ByteArrayInputStream(input);
	}

	/**
	 * Convert the specified string to an input stream, encoded as bytes using the default character
	 * encoding of the platform.
	 * 
	 * @param input the string to convert
	 * @return an input stream
	 */
	public static InputStream toInputStream(final String input) {
		return toInputStream(input, Charset.defaultCharset());
	}

	/**
	 * Converts the specified string to an input stream, encoded as bytes using the specified
	 * character encoding.
	 * 
	 * @param input the string to convert
	 * @param encoding the encoding to use, null means platform default
	 * @return an input stream
	 */
	public static InputStream toInputStream(final String input, final Charset encoding) {
		return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(encoding)));
	}

	/**
	 * Converts the specified string to an input stream, encoded as bytes using the specified
	 * character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * 
	 * @param input the string to convert
	 * @param encoding the encoding to use, null means platform default
	 * @return an input stream
	 */
	public static InputStream toInputStream(final String input, final String encoding) {
		final byte[] bytes = input.getBytes(Charsets.toCharset(encoding));
		return new ByteArrayInputStream(bytes);
	}

	// -----------------------------------------------------------------------
	public static Reader toReader(final InputStream input, final String encoding) {
		try {
			return new InputStreamReader(input, encoding);
		}
		catch (UnsupportedEncodingException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	// write byte[]
	// -----------------------------------------------------------------------
	/**
	 * Writes bytes from a <code>byte[]</code> to an <code>OutputStream</code>.
	 * 
	 * @param data the byte array to write, do not modify during output, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(final byte[] data, final OutputStream output) throws IOException {
		if (data != null) {
			output.write(data);
		}
	}

	/**
	 * Writes bytes from a <code>byte[]</code> to an <code>OutputStream</code> using chunked writes.
	 * This is intended for writing very large byte arrays which might otherwise cause excessive
	 * memory usage if the native code has to allocate a copy.
	 * 
	 * @param data the byte array to write, do not modify during output, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void writeChunked(final byte[] data, final OutputStream output) throws IOException {
		if (data != null) {
			int bytes = data.length;
			int offset = 0;
			while (bytes > 0) {
				int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE);
				output.write(data, offset, chunk);
				bytes -= chunk;
				offset += chunk;
			}
		}
	}

	/**
	 * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code> using the default
	 * character encoding of the platform.
	 * <p>
	 * This method uses {@link String#String(byte[])}.
	 * 
	 * @param data the byte array to write, do not modify during output, null ignored
	 * @param output the <code>Writer</code> to write to
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(byte[] data, Appendable output) throws IOException {
		if (data != null) {
			output.append(new String(data));
		}
	}

	/**
	 * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code> using the specified
	 * character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link String#String(byte[], String)}.
	 * 
	 * @param data the byte array to write, do not modify during output, null ignored
	 * @param output the <code>Writer</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(byte[] data, Appendable output, Charset encoding) throws IOException {
		if (data != null) {
			output.append(new String(data, Charsets.toCharset(encoding)));
		}
	}

	/**
	 * Writes bytes from a <code>byte[]</code> to chars on a <code>Writer</code> using the specified
	 * character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link String#String(byte[], String)}.
	 * 
	 * @param data the byte array to write, do not modify during output, null ignored
	 * @param output the <code>Writer</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 * @throws UnsupportedCharsetException thrown instead of {@link UnsupportedEncodingException} in
	 *             version 2.2 if the encoding is not supported.
	 */
	public static void write(final byte[] data, final Writer output, final String encoding) throws IOException {
		write(data, output, Charsets.toCharset(encoding));
	}

	// write char[]
	// -----------------------------------------------------------------------
	/**
	 * Writes chars from a <code>char[]</code> to a <code>Writer</code> using the default character
	 * encoding of the platform.
	 * 
	 * @param data the char array to write, do not modify during output, null ignored
	 * @param output the <code>Writer</code> to write to
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(final char[] data, final Appendable output) throws IOException {
		if (data != null) {
			CharSequence cs = CharSequences.toCharSequence(data);
			output.append(cs);
		}
	}

	/**
	 * Writes chars from a <code>char[]</code> to a <code>Writer</code> using chunked writes. This
	 * is intended for writing very large byte arrays which might otherwise cause excessive memory
	 * usage if the native code has to allocate a copy.
	 * 
	 * @param data the char array to write, do not modify during output, null ignored
	 * @param output the <code>Writer</code> to write to
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void writeChunked(final char[] data, final Appendable output) throws IOException {
		if (data != null) {
			int bytes = data.length;
			int offset = 0;
			while (bytes > 0) {
				int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE);
				CharSequence cs = CharSequences.toCharSequence(data, offset, chunk);
				output.append(cs);
				bytes -= chunk;
				offset += chunk;
			}
		}
	}

	/**
	 * Writes chars from a <code>char[]</code> to bytes on an <code>OutputStream</code>.
	 * <p>
	 * This method uses {@link String#String(char[])} and {@link String#getBytes()}.
	 * 
	 * @param data the char array to write, do not modify during output, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(final char[] data, final OutputStream output) throws IOException {
		write(data, output, Charset.defaultCharset());
	}

	/**
	 * Writes chars from a <code>char[]</code> to bytes on an <code>OutputStream</code> using the
	 * specified character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link String#String(char[])} and {@link String#getBytes(String)}.
	 * 
	 * @param data the char array to write, do not modify during output, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(final char[] data, final OutputStream output, final Charset encoding) throws IOException {
		if (data != null) {
			output.write(new String(data).getBytes(Charsets.toCharset(encoding)));
		}
	}

	/**
	 * Writes chars from a <code>char[]</code> to bytes on an <code>OutputStream</code> using the
	 * specified character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link String#String(char[])} and {@link String#getBytes(String)}.
	 * 
	 * @param data the char array to write, do not modify during output, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 * @throws UnsupportedCharsetException thrown instead of {@link UnsupportedEncodingException} in
	 *             version 2.2 if the encoding is not supported.
	 */
	public static void write(final char[] data, final OutputStream output, final String encoding) throws IOException {
		write(data, output, Charsets.toCharset(encoding));
	}

	// write CharSequence
	// -----------------------------------------------------------------------
	/**
	 * Writes chars from a <code>CharSequence</code> to a <code>Writer</code>.
	 * 
	 * @param data the <code>CharSequence</code> to write, null ignored
	 * @param output the <code>Writer</code> to write to
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(final CharSequence data, final Appendable output) throws IOException {
		if (data != null) {
			output.append(data);
		}
	}

	/**
	 * Writes chars from a <code>CharSequence</code> to bytes on an <code>OutputStream</code> using
	 * the default character encoding of the platform.
	 * <p>
	 * This method uses {@link String#getBytes()}.
	 * 
	 * @param data the <code>CharSequence</code> to write, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(final CharSequence data, final OutputStream output) throws IOException {
		write(data, output, Charset.defaultCharset());
	}

	/**
	 * Writes chars from a <code>CharSequence</code> to bytes on an <code>OutputStream</code> using
	 * the specified character encoding.
	 * <p>
	 * This method uses {@link String#getBytes(String)}.
	 * 
	 * @param data the <code>CharSequence</code> to write, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(final CharSequence data, final OutputStream output, final Charset encoding)
			throws IOException {
		if (data != null) {
			output.write(data.toString().getBytes(Charsets.toCharset(encoding)));
		}
	}

	/**
	 * Writes chars from a <code>CharSequence</code> to bytes on an <code>OutputStream</code> using
	 * the specified character encoding.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link String#getBytes(String)}.
	 * 
	 * @param data the <code>CharSequence</code> to write, null ignored
	 * @param output the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void write(final CharSequence data, final OutputStream output, final String encoding)
			throws IOException {
		write(data, output, Charsets.toCharset(encoding));
	}

	// writeLines
	// -----------------------------------------------------------------------
	/**
	 * Writes the <code>toString()</code> value of each item in a collection to an
	 * <code>OutputStream</code> line by line, using the default character encoding of the platform
	 * and the specified line ending.
	 * 
	 * @param lines the lines to write, null entries produce blank lines
	 * @param lineEnding the line separator to use, null is system default
	 * @param output the <code>OutputStream</code> to write to, not null, not closed
	 * @throws NullPointerException if the output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void writeLines(final Collection<?> lines, final String lineEnding, final OutputStream output)
			throws IOException {
		writeLines(lines, lineEnding, output, Charset.defaultCharset());
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to an
	 * <code>OutputStream</code> line by line, using the specified character encoding and the
	 * specified line ending.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * 
	 * @param lines the lines to write, null entries produce blank lines
	 * @param lineEnding the line separator to use, null is system default
	 * @param output the <code>OutputStream</code> to write to, not null, not closed
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if the output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void writeLines(final Collection<?> lines, String lineEnding, final OutputStream output,
			final Charset encoding) throws IOException {
		if (lines == null) {
			return;
		}
		if (lineEnding == null) {
			lineEnding = LINE_SEPARATOR;
		}
		final Charset cs = Charsets.toCharset(encoding);
		for (final Object line : lines) {
			if (line != null) {
				output.write(line.toString().getBytes(cs));
			}
			output.write(lineEnding.getBytes(cs));
		}
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to an
	 * <code>OutputStream</code> line by line, using the specified character encoding and the
	 * specified line ending.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * 
	 * @param lines the lines to write, null entries produce blank lines
	 * @param lineEnding the line separator to use, null is system default
	 * @param output the <code>OutputStream</code> to write to, not null, not closed
	 * @param encoding the encoding to use, null means platform default
	 * @throws NullPointerException if the output is null
	 * @throws IOException if an I/O error occurs
	 * @throws UnsupportedCharsetException thrown instead of {@link UnsupportedEncodingException} in
	 *             version 2.2 if the encoding is not supported.
	 */
	public static void writeLines(final Collection<?> lines, final String lineEnding, final OutputStream output,
			final String encoding) throws IOException {
		writeLines(lines, lineEnding, output, Charsets.toCharset(encoding));
	}

	/**
	 * Writes the <code>toString()</code> value of each item in a collection to a
	 * <code>Writer</code> line by line, using the specified line ending.
	 * 
	 * @param lines the lines to write, null entries produce blank lines
	 * @param lineEnding the line separator to use, null is system default
	 * @param writer the <code>Writer</code> to write to, not null, not closed
	 * @throws NullPointerException if the input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static void writeLines(Collection<?> lines, String lineEnding, Appendable writer) throws IOException {
		if (lines == null) {
			return;
		}
		if (lineEnding == null) {
			lineEnding = LINE_SEPARATOR;
		}
		for (Object line : lines) {
			if (line != null) {
				writer.append(line.toString());
			}
			writer.append(lineEnding);
		}
	}

	// copy from InputStream
	// -----------------------------------------------------------------------
	/**
	 * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * Large streams (over 2GB) will return a bytes copied value of <code>-1</code> after the copy
	 * has completed since the correct number of bytes cannot be returned as an int. For large
	 * streams use the <code>copyLarge(InputStream, OutputStream)</code> method.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static int copy(final InputStream input, final OutputStream output) throws IOException {
		final long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int)count;
	}

	/**
	 * Copies bytes from an <code>InputStream</code> to an <code>OutputStream</code> using an
	 * internal buffer of the given size.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @param bufferSize the bufferSize used to copy from the input to the output
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copy(final InputStream input, final OutputStream output, final int bufferSize)
			throws IOException {
		return copyLarge(input, output, new byte[bufferSize]);
	}

	public static long copyUntil(final InputStream input, final OutputStream output, final int end)
			throws IOException {
		int ch;
		int count = 0;
		while (EOF != (ch = input.read())) {
			if (end == ch) {
				output.flush();
				return count;
			}
			output.write(ch);
			count++;
		}
		throw new IOException("Unexpected EOF: " + (char)end + " is expected!");
	}

	/**
	 * Copies bytes from a large (over 2GB) <code>InputStream</code> to an <code>OutputStream</code>
	 * .
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
	}

	/**
	 * Copy bytes from a large (over 2GB) <code>InputStream</code> to an <code>OutputStream</code>.
	 * <p>
	 * This method uses the provided buffer, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @param buffer the buffer to use for the copy
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer)
			throws IOException {
		long count = 0;
		int n = 0;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	/**
	 * Copy some or all bytes from a large (over 2GB) <code>InputStream</code> to an
	 * <code>OutputStream</code>, optionally skipping input bytes.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @param inputOffset : number of bytes to skip from input before copying -ve values are ignored
	 * @param length : number of bytes to copy. -ve means all
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset,
			final long length) throws IOException {
		return copyLarge(input, output, inputOffset, length, new byte[DEFAULT_BUFFER_SIZE]);
	}

	/**
	 * Copy some or all bytes from a large (over 2GB) <code>InputStream</code> to an
	 * <code>OutputStream</code>, optionally skipping input bytes.
	 * <p>
	 * This method uses the provided buffer, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @param inputOffset : number of bytes to skip from input before copying -ve values are ignored
	 * @param length : number of bytes to copy. -ve means all
	 * @param buffer the buffer to use for the copy
	 * @return the number of bytes copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset,
			final long length, final byte[] buffer) throws IOException {
		if (inputOffset > 0) {
			skipFully(input, inputOffset);
		}
		if (length == 0) {
			return 0;
		}
		final int bufferLength = buffer.length;
		int bytesToRead = bufferLength;
		if (length > 0 && length < bufferLength) {
			bytesToRead = (int)length;
		}
		int read;
		long totalRead = 0;
		while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
			output.write(buffer, 0, read);
			totalRead += read;
			if (length > 0) { // only adjust length if not reading to the end
				// Note the cast must work because buffer.length is an integer
				bytesToRead = (int)Math.min(length - totalRead, bufferLength);
			}
		}
		return totalRead;
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to chars on a <code>Writer</code> using the
	 * default character encoding of the platform.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * This method uses {@link InputStreamReader}.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static int copy(final InputStream input, final Appendable output) throws IOException {
		return copy(input, output, Charset.defaultCharset());
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to chars on a <code>Writer</code> using the
	 * specified character encoding.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link InputStreamReader}.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static int copy(final InputStream input, final Appendable output, final Charset encoding)
			throws IOException {
		final InputStreamReader in = new InputStreamReader(input, Charsets.toCharset(encoding));
		return copy(in, output);
	}

	public static long copyUntil(final InputStream input, final Appendable output, final int ch, final Charset encoding)
			throws IOException {
		final WriterOutputStream ou = new WriterOutputStream(output, Charsets.toCharset(encoding));
		return copyUntil(input, ou, ch);
	}

	/**
	 * Copy bytes from an <code>InputStream</code> to chars on a <code>Writer</code> using the
	 * specified character encoding.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedInputStream</code>.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * This method uses {@link InputStreamReader}.
	 * 
	 * @param input the <code>InputStream</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static int copy(final InputStream input, final Appendable output, final String encoding)
			throws IOException {
		return copy(input, output, Charsets.toCharset(encoding));
	}

	public static long copyUntil(final InputStream input, final Appendable output, int end, final String encoding)
			throws IOException {
		return copyUntil(input, output, end, Charsets.toCharset(encoding));
	}

	public static long copyUntil(final InputStream input, final Appendable output, final int end)
			throws IOException {
		return copyUntil(input, output, end, Charsets.CS_UTF_8);
	}

	// copy from Reader
	// -----------------------------------------------------------------------
	/**
	 * Copy chars from a <code>Reader</code> to a <code>Writer</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * <p>
	 * Large streams (over 2GB) will return a chars copied value of <code>-1</code> after the copy
	 * has completed since the correct number of chars cannot be returned as an int. For large
	 * streams use the <code>copyLarge(Reader, Writer)</code> method.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static int copy(final Reader input, final Appendable output) throws IOException {
		final long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int)count;
	}

	/**
	 * Copy chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * <p>
	 * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @return the number of characters copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copyLarge(final Reader input, final Appendable output) throws IOException {
		return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
	}

	/**
	 * Copy chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
	 * <p>
	 * This method uses the provided buffer, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * <p>
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @param buffer the buffer to be used for the copy
	 * @return the number of characters copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copyLarge(final Reader input, final Appendable output, final char[] buffer) throws IOException {
		long count = 0;
		int n = 0;
		while (EOF != (n = input.read(buffer))) {
			CharSequence cs = CharSequences.toCharSequence(buffer, 0, n);
			output.append(cs);
			count += n;
		}
		return count;
	}

	/**
	 * Copy some or all chars from a large (over 2GB) <code>InputStream</code> to an
	 * <code>OutputStream</code>, optionally skipping input chars.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * <p>
	 * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @param inputOffset : number of chars to skip from input before copying -ve values are ignored
	 * @param length : number of chars to copy. -ve means all
	 * @return the number of chars copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copyLarge(final Reader input, final Appendable output, final long inputOffset, final long length)
			throws IOException {
		return copyLarge(input, output, inputOffset, length, new char[DEFAULT_BUFFER_SIZE]);
	}

	/**
	 * Copy some or all chars from a large (over 2GB) <code>InputStream</code> to an
	 * <code>OutputStream</code>, optionally skipping input chars.
	 * <p>
	 * This method uses the provided buffer, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * <p>
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>Writer</code> to write to
	 * @param inputOffset : number of chars to skip from input before copying -ve values are ignored
	 * @param length : number of chars to copy. -ve means all
	 * @param buffer the buffer to be used for the copy
	 * @return the number of chars copied
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static long copyLarge(final Reader input, final Appendable output, final long inputOffset,
			final long length, final char[] buffer) throws IOException {
		if (inputOffset > 0) {
			skipFully(input, inputOffset);
		}
		if (length == 0) {
			return 0;
		}
		int bytesToRead = buffer.length;
		if (length > 0 && length < buffer.length) {
			bytesToRead = (int)length;
		}
		int read;
		long totalRead = 0;
		while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
			CharSequence cs = CharSequences.toCharSequence(buffer, 0, read);
			output.append(cs);
			totalRead += read;
			if (length > 0) { // only adjust length if not reading to the end
				// Note the cast must work because buffer.length is an integer
				bytesToRead = (int)Math.min(length - totalRead, buffer.length);
			}
		}
		return totalRead;
	}

	/**
	 * Copy chars from a <code>Reader</code> to bytes on an <code>OutputStream</code> using the
	 * default character encoding of the platform, and calling flush.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * <p>
	 * Due to the implementation of OutputStreamWriter, this method performs a flush.
	 * <p>
	 * This method uses {@link OutputStreamWriter}.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static int copy(final Reader input, final OutputStream output) throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(output);
		
		int cnt = copy(input, out);
		// XXX Unless anyone is planning on rewriting OutputStreamWriter, we
		// have to flush here.
		out.flush();
		
		return cnt;
	}

	/**
	 * Copy chars from a <code>Reader</code> to bytes on an <code>OutputStream</code> using the
	 * specified character encoding, and calling flush.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * Due to the implementation of OutputStreamWriter, this method performs a flush.
	 * <p>
	 * This method uses {@link OutputStreamWriter}.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use, null means platform default
	 * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static int copy(final Reader input, final OutputStream output, final Charset encoding)
			throws IOException {
		final OutputStreamWriter out = new OutputStreamWriter(output, Charsets.toCharset(encoding));
		
		int cnt = copy(input, out);

		// XXX Unless anyone is planning on rewriting OutputStreamWriter,
		// we have to flush here.
		out.flush();
		
		return cnt;
	}

	/**
	 * Copies chars from a <code>Reader</code> to bytes on an <code>OutputStream</code> using the
	 * specified character encoding, and calling flush.
	 * <p>
	 * This method buffers the input internally, so there is no need to use a
	 * <code>BufferedReader</code>.
	 * <p>
	 * Character encoding names can be found at <a
	 * href="http://www.iana.org/assignments/character-sets">IANA</a>.
	 * <p>
	 * Due to the implementation of OutputStreamWriter, this method performs a flush.
	 * <p>
	 * This method uses {@link OutputStreamWriter}.
	 * 
	 * @param input the <code>Reader</code> to read from
	 * @param output the <code>OutputStream</code> to write to
	 * @param encoding the encoding to use for the OutputStream, null means platform default
	 * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
	 * @throws NullPointerException if the input or output is null
	 * @throws IOException if an I/O error occurs
	 */
	public static int copy(final Reader input, final OutputStream output, final String encoding)
			throws IOException {
		return copy(input, output, Charsets.toCharset(encoding));
	}

	// content equals
	// -----------------------------------------------------------------------
	/**
	 * Compare the contents of two Streams to determine if they are equal or not.
	 * <p>
	 * This method buffers the input internally using <code>BufferedInputStream</code> if they are
	 * not already buffered.
	 * 
	 * @param input1 the first stream
	 * @param input2 the second stream
	 * @return true if the content of the streams are equal or they both don't exist, false
	 *         otherwise
	 * @throws NullPointerException if either input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
		if (input1 == input2) {
			return true;
		}
		if (!(input1 instanceof BufferedInputStream)) {
			input1 = new BufferedInputStream(input1);
		}
		if (!(input2 instanceof BufferedInputStream)) {
			input2 = new BufferedInputStream(input2);
		}

		int ch = input1.read();
		while (EOF != ch) {
			final int ch2 = input2.read();
			if (ch != ch2) {
				return false;
			}
			ch = input1.read();
		}

		final int ch2 = input2.read();
		return ch2 == EOF;
	}

	/**
	 * Compare the contents of two Readers to determine if they are equal or not.
	 * <p>
	 * This method buffers the input internally using <code>BufferedReader</code> if they are not
	 * already buffered.
	 * 
	 * @param input1 the first reader
	 * @param input2 the second reader
	 * @return true if the content of the readers are equal or they both don't exist, false
	 *         otherwise
	 * @throws NullPointerException if either input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
		if (input1 == input2) {
			return true;
		}

		input1 = toBufferedReader(input1);
		input2 = toBufferedReader(input2);

		int ch = input1.read();
		while (EOF != ch) {
			final int ch2 = input2.read();
			if (ch != ch2) {
				return false;
			}
			ch = input1.read();
		}

		final int ch2 = input2.read();
		return ch2 == EOF;
	}

	/**
	 * Compare the contents of two Readers to determine if they are equal or not, ignoring EOL
	 * characters.
	 * <p>
	 * This method buffers the input internally using <code>BufferedReader</code> if they are not
	 * already buffered.
	 * 
	 * @param input1 the first reader
	 * @param input2 the second reader
	 * @return true if the content of the readers are equal (ignoring EOL differences), false
	 *         otherwise
	 * @throws NullPointerException if either input is null
	 * @throws IOException if an I/O error occurs
	 */
	public static boolean contentEqualsIgnoreEOL(final Reader input1, final Reader input2) throws IOException {
		if (input1 == input2) {
			return true;
		}
		final BufferedReader br1 = toBufferedReader(input1);
		final BufferedReader br2 = toBufferedReader(input2);

		String line1 = br1.readLine();
		String line2 = br2.readLine();
		while (line1 != null && line2 != null && line1.equals(line2)) {
			line1 = br1.readLine();
			line2 = br2.readLine();
		}
		return line1 == null ? line2 == null ? true : false : line1.equals(line2);
	}

	public static long available(InputStream input) throws IOException {
		return input == null ? 0 : input.available();
	}

	public static long available(Reader reader) throws IOException {
		if (reader.markSupported()) {
			reader.mark(Integer.MAX_VALUE);
			long len = reader.skip(Integer.MAX_VALUE);
			reader.reset();
			return len;
		}
		return -1;
	}

	/**
	 * Drain an <code>InputStream</code>.
	 * 
	 * @param input the input stream to drain
	 */
	public static void safeDrain(InputStream input) {
		try {
			if (input != null) {
				drain(input);
			}
		}
		catch (IOException ioe) {
			// ignore
		}
	}
	
	/**
	 * Drain an <code>InputStream</code>.
	 * 
	 * @param input the input stream to drain
	 * @param timeout the timeout to stop drain (milliseconds)
	 */
	public static void safeDrain(InputStream input, long timeout) {
		try {
			if (input != null) {
				drain(input, timeout);
			}
		}
		catch (IOException ioe) {
			// ignore
		}
	}
	
	/**
	 * Drain an <code>InputStream</code>.
	 * 
	 * @param input the input stream to drain
	 * @return the number of bytes read
	 * @throws IOException in case of I/O errors
	 */
	public static long drain(InputStream input) throws IOException {
		/*
		 * N.B. no need to synchronize this because: - we don't care if the buffer is created
		 * multiple times (the data is ignored) - we always use the same size buffer, so if it it is
		 * recreated it will still be OK (if the buffer size were variable, we would need to synch.
		 * to ensure some other thread did not create a smaller one)
		 */
		if (SKIP_BYTE_BUFFER == null) {
			SKIP_BYTE_BUFFER = new byte[SKIP_BUFFER_SIZE];
		}
		long read = 0;
		while (true) {
			final long n = input.read(SKIP_BYTE_BUFFER, 0, SKIP_BUFFER_SIZE);
			if (n < 0) { // EOF
				break;
			}
			read += n;
		}
		return read;
	}
	
	/**
	 * Drain an <code>InputStream</code>.
	 * 
	 * @param input the input stream to drain
	 * @param timeout the timeout to stop drain (milliseconds)
	 * @return the number of bytes read
	 * @throws IOException in case of I/O errors
	 */
	public static long drain(InputStream input, long timeout) throws IOException {
		if (timeout <= 0) {
			return drain(input);
		}

		/*
		 * N.B. no need to synchronize this because: - we don't care if the buffer is created
		 * multiple times (the data is ignored) - we always use the same size buffer, so if it it is
		 * recreated it will still be OK (if the buffer size were variable, we would need to synch.
		 * to ensure some other thread did not create a smaller one)
		 */
		if (SKIP_BYTE_BUFFER == null) {
			SKIP_BYTE_BUFFER = new byte[SKIP_BUFFER_SIZE];
		}

		long start = System.currentTimeMillis();
		long read = 0;
		while (true) {
			if (input.available() <= 0) {
				Threads.safeSleep(10);
			}
			if (System.currentTimeMillis() - start > timeout) {
				break;
			}
			final long n = input.read(SKIP_BYTE_BUFFER, 0, SKIP_BUFFER_SIZE);
			if (n < 0) { // EOF
				break;
			}
			read += n;
		}
		return read;
	}
	
	/**
	 * Skip bytes from an input byte stream. This implementation guarantees that it will read as
	 * many bytes as possible before giving up; this may not always be the case for subclasses of
	 * {@link Reader}.
	 * 
	 * @param input byte stream to skip
	 * @param toSkip number of bytes to skip.
	 * @return number of bytes actually skipped.
	 * @see InputStream#skip(long)
	 * @throws IOException if there is a problem reading the file
	 * @throws IllegalArgumentException if toSkip is negative
	 */
	public static long skip(final InputStream input, final long toSkip) throws IOException {
		if (toSkip < 0) {
			throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
		}
		/*
		 * N.B. no need to synchronize this because: - we don't care if the buffer is created
		 * multiple times (the data is ignored) - we always use the same size buffer, so if it it is
		 * recreated it will still be OK (if the buffer size were variable, we would need to synch.
		 * to ensure some other thread did not create a smaller one)
		 */
		if (SKIP_BYTE_BUFFER == null) {
			SKIP_BYTE_BUFFER = new byte[SKIP_BUFFER_SIZE];
		}
		long remain = toSkip;
		while (remain > 0) {
			final long n = input.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(remain, SKIP_BUFFER_SIZE));
			if (n < 0) { // EOF
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	/**
	 * Skips bytes from a ReadableByteChannel. This implementation guarantees that it will read as
	 * many bytes as possible before giving up.
	 * 
	 * @param input ReadableByteChannel to skip
	 * @param toSkip number of bytes to skip.
	 * @return number of bytes actually skipped.
	 * @throws IOException if there is a problem reading the ReadableByteChannel
	 * @throws IllegalArgumentException if toSkip is negative
	 */
	public static long skip(final ReadableByteChannel input, final long toSkip) throws IOException {
		if (toSkip < 0) {
			throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
		}
		final ByteBuffer skipByteBuffer = ByteBuffer.allocate((int)Math.min(toSkip, SKIP_BUFFER_SIZE));
		long remain = toSkip;
		while (remain > 0) {
			skipByteBuffer.position(0);
			skipByteBuffer.limit((int)Math.min(remain, SKIP_BUFFER_SIZE));
			final int n = input.read(skipByteBuffer);
			if (n == EOF) {
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	/**
	 * Skips characters from an input character stream. This implementation guarantees that it will
	 * read as many characters as possible before giving up; this may not always be the case for
	 * skip() implementations in subclasses of {@link Reader}.
	 * <p>
	 * Note that the implementation uses {@link Reader#read(char[], int, int)} rather than
	 * delegating to {@link Reader#skip(long)}. This means that the method may be considerably less
	 * efficient than using the actual skip implementation, this is done to guarantee that the
	 * correct number of characters are skipped.
	 * </p>
	 * 
	 * @param input character stream to skip
	 * @param toSkip number of characters to skip.
	 * @return number of characters actually skipped.
	 * @see Reader#skip(long)
	 * @see <a href="https://issues.apache.org/jira/browse/IO-203">IO-203 - Add skipFully() method
	 *      for InputStreams</a>
	 * @throws IOException if there is a problem reading the file
	 * @throws IllegalArgumentException if toSkip is negative
	 */
	public static long skip(final Reader input, final long toSkip) throws IOException {
		if (toSkip < 0) {
			throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
		}
		/*
		 * N.B. no need to synchronize this because: - we don't care if the buffer is created
		 * multiple times (the data is ignored) - we always use the same size buffer, so if it it is
		 * recreated it will still be OK (if the buffer size were variable, we would need to synch.
		 * to ensure some other thread did not create a smaller one)
		 */
		if (SKIP_CHAR_BUFFER == null) {
			SKIP_CHAR_BUFFER = new char[SKIP_BUFFER_SIZE];
		}
		long remain = toSkip;
		while (remain > 0) {
			final long n = input.read(SKIP_CHAR_BUFFER, 0, (int)Math.min(remain, SKIP_BUFFER_SIZE));
			if (n < 0) { // EOF
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	/**
	 * Skip the requested number of bytes or fail if there are not enough left.
	 * <p>
	 * This allows for the possibility that {@link InputStream#skip(long)} may not skip as many
	 * bytes as requested (most likely because of reaching EOF).
	 * 
	 * @param input stream to skip
	 * @param toSkip the number of bytes to skip
	 * @see InputStream#skip(long)
	 * @throws IOException if there is a problem reading the file
	 * @throws IllegalArgumentException if toSkip is negative
	 * @throws EOFException if the number of bytes skipped was incorrect
	 */
	public static void skipFully(final InputStream input, final long toSkip) throws IOException {
		if (toSkip < 0) {
			throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
		}
		final long skipped = skip(input, toSkip);
		if (skipped != toSkip) {
			throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
		}
	}

	/**
	 * Skips the requested number of bytes or fail if there are not enough left.
	 * 
	 * @param input ReadableByteChannel to skip
	 * @param toSkip the number of bytes to skip
	 * @throws IOException if there is a problem reading the ReadableByteChannel
	 * @throws IllegalArgumentException if toSkip is negative
	 * @throws EOFException if the number of bytes skipped was incorrect
	 */
	public static void skipFully(final ReadableByteChannel input, final long toSkip) throws IOException {
		if (toSkip < 0) {
			throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
		}
		final long skipped = skip(input, toSkip);
		if (skipped != toSkip) {
			throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
		}
	}

	/**
	 * Skips the requested number of characters or fail if there are not enough left.
	 * <p>
	 * This allows for the possibility that {@link Reader#skip(long)} may not skip as many
	 * characters as requested (most likely because of reaching EOF).
	 * <p>
	 * Note that the implementation uses {@link #skip(Reader, long)}. This means that the method may
	 * be considerably less efficient than using the actual skip implementation, this is done to
	 * guarantee that the correct number of characters are skipped.
	 * </p>
	 * 
	 * @param input stream to skip
	 * @param toSkip the number of characters to skip
	 * @see Reader#skip(long)
	 * @throws IOException if there is a problem reading the file
	 * @throws IllegalArgumentException if toSkip is negative
	 * @throws EOFException if the number of characters skipped was incorrect
	 */
	public static void skipFully(final Reader input, final long toSkip) throws IOException {
		final long skipped = skip(input, toSkip);
		if (skipped != toSkip) {
			throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
		}
	}

	public static long skipTo(InputStream input, int value) throws IOException {
		long i = 0;
		int v = input.read();
		while (v != EOF) {
			i++;
			if (v == value) {
				return i;
			}
			v = input.read();
		}
		return -1L;
	}
	
	/**
	 * Read characters from an input character stream. This implementation guarantees that it will
	 * read as many characters as possible before giving up; this may not always be the case for
	 * subclasses of {@link Reader}.
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @param offset inital offset into buffer
	 * @param length length to read, must be >= 0
	 * @return actual length read; may be less than requested if EOF was reached
	 * @throws IOException if a read error occurs
	 */
	public static int read(final Reader input, final char[] buffer, final int offset, final int length)
			throws IOException {
		if (length < 0) {
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0) {
			final int location = length - remaining;
			final int count = input.read(buffer, offset + location, remaining);
			if (EOF == count) { // EOF
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	/**
	 * Read characters from an input character stream. This implementation guarantees that it will
	 * read as many characters as possible before giving up; this may not always be the case for
	 * subclasses of {@link Reader}.
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @return actual length read; may be less than requested if EOF was reached
	 * @throws IOException if a read error occurs
	 */
	public static int read(final Reader input, final char[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	/**
	 * Read bytes from an input stream. This implementation guarantees that it will read as many
	 * bytes as possible before giving up; this may not always be the case for subclasses of
	 * {@link InputStream}.
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @param offset inital offset into buffer
	 * @param length length to read, must be >= 0
	 * @return actual length read; may be less than requested if EOF was reached
	 * @throws IOException if a read error occurs
	 */
	public static int read(final InputStream input, final byte[] buffer, final int offset, final int length)
			throws IOException {
		if (length < 0) {
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0) {
			final int location = length - remaining;
			final int count = input.read(buffer, offset + location, remaining);
			if (EOF == count) { // EOF
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	/**
	 * Read bytes from an input stream. This implementation guarantees that it will read as many
	 * bytes as possible before giving up; this may not always be the case for subclasses of
	 * {@link InputStream}.
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @return actual length read; may be less than requested if EOF was reached
	 * @throws IOException if a read error occurs
	 */
	public static int read(final InputStream input, final byte[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	/**
	 * Reads bytes from a ReadableByteChannel.
	 * <p>
	 * This implementation guarantees that it will read as many bytes as possible before giving up;
	 * this may not always be the case for subclasses of {@link ReadableByteChannel}.
	 * 
	 * @param input the byte channel to read
	 * @param buffer byte buffer destination
	 * @return the actual length read; may be less than requested if EOF was reached
	 * @throws IOException if a read error occurs
	 */
	public static int read(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
		final int length = buffer.remaining();
		while (buffer.remaining() > 0) {
			final int count = input.read(buffer);
			if (EOF == count) { // EOF
				break;
			}
		}
		return length - buffer.remaining();
	}

	/**
	 * Read the requested number of characters or fail if there are not enough left.
	 * <p>
	 * This allows for the possibility that {@link Reader#read(char[], int, int)} may not read as
	 * many characters as requested (most likely because of reaching EOF).
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @param offset inital offset into buffer
	 * @param length length to read, must be >= 0
	 * @throws IOException if there is a problem reading the file
	 * @throws IllegalArgumentException if length is negative
	 * @throws EOFException if the number of characters read was incorrect
	 */
	public static void readFully(final Reader input, final char[] buffer, final int offset, final int length)
			throws IOException {
		final int actual = read(input, buffer, offset, length);
		if (actual != length) {
			throw new EOFException("Length to read: " + length + " actual: " + actual);
		}
	}

	/**
	 * Read the requested number of characters or fail if there are not enough left.
	 * <p>
	 * This allows for the possibility that {@link Reader#read(char[], int, int)} may not read as
	 * many characters as requested (most likely because of reaching EOF).
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @throws IOException if there is a problem reading the file
	 * @throws IllegalArgumentException if length is negative
	 * @throws EOFException if the number of characters read was incorrect
	 */
	public static void readFully(final Reader input, final char[] buffer) throws IOException {
		readFully(input, buffer, 0, buffer.length);
	}

	/**
	 * Read the requested number of bytes or fail if there are not enough left.
	 * <p>
	 * This allows for the possibility that {@link InputStream#read(byte[], int, int)} may not read
	 * as many bytes as requested (most likely because of reaching EOF).
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @param offset initial offset into buffer
	 * @param length length to read, must be >= 0
	 * @throws IOException if there is a problem reading the file
	 * @throws IllegalArgumentException if length is negative
	 * @throws EOFException if the number of bytes read was incorrect
	 */
	public static void readFully(final InputStream input, final byte[] buffer, final int offset, final int length)
			throws IOException {
		final int actual = read(input, buffer, offset, length);
		if (actual != length) {
			throw new EOFException("Length to read: " + length + " actual: " + actual);
		}
	}

	/**
	 * Read the requested number of bytes or fail if there are not enough left.
	 * <p>
	 * This allows for the possibility that {@link InputStream#read(byte[], int, int)} may not read
	 * as many bytes as requested (most likely because of reaching EOF).
	 * 
	 * @param input where to read input from
	 * @param buffer destination
	 * @throws IOException if there is a problem reading the file
	 * @throws IllegalArgumentException if length is negative
	 * @throws EOFException if the number of bytes read was incorrect
	 */
	public static void readFully(final InputStream input, final byte[] buffer) throws IOException {
		readFully(input, buffer, 0, buffer.length);
	}

	/**
	 * Reads the requested number of bytes or fail if there are not enough left.
	 * <p>
	 * This allows for the possibility that {@link ReadableByteChannel#read(ByteBuffer)} may not
	 * read as many bytes as requested (most likely because of reaching EOF).
	 * 
	 * @param input the byte channel to read
	 * @param buffer byte buffer destination
	 * @throws IOException if there is a problem reading the file
	 * @throws EOFException if the number of bytes read was incorrect
	 */
	public static void readFully(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
		final int expected = buffer.remaining();
		final int actual = read(input, buffer);
		if (actual != expected) {
			throw new EOFException("Length to read: " + expected + " actual: " + actual);
		}
	}

	//---------------------------------------------------------------------------
	public static InputStream closedInputStream() {
		return ClosedInputStream.INSTANCE;
	}
	
	public static OutputStream closedOutputStream() {
		return ClosedOutputStream.INSTANCE;
	}
	
	public static OutputStream nullOutputStream() {
		return NullOutputStream.INSTANCE;
	}
}
