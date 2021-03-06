package panda.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.junit.Test;

import panda.lang.Strings;

/**
 */
public class Base64OutputStreamTest {

    private final static byte[] CRLF = {(byte) '\r', (byte) '\n'};

    private final static byte[] LF = {(byte) '\n'};

    private static final String STRING_FIXTURE = "Hello World";

    /**
     * Test the Base64OutputStream implementation against the special NPE inducing input
     * identified in the CODEC-98 bug.
     *
     * @throws Exception for some failure scenarios.
     */
    @Test
    public void testCodec98NPE() throws Exception {
        final byte[] codec98 = Strings.getBytesUtf8(Base64TestData.CODEC_98_NPE);
        final byte[] codec98_1024 = new byte[1024];
        System.arraycopy(codec98, 0, codec98_1024, 0, codec98.length);
        final ByteArrayOutputStream data = new ByteArrayOutputStream(1024);
        final Base64OutputStream stream = new Base64OutputStream(data, false);
        stream.write(codec98_1024, 0, 1024);
        stream.close();

        final byte[] decodedBytes = data.toByteArray();
        final String decoded = Strings.newStringUtf8(decodedBytes);
        assertEquals(
            "codec-98 NPE Base64OutputStream", Base64TestData.CODEC_98_NPE_DECODED, decoded
        );
    }


    /**
     * Test the Base64OutputStream implementation against empty input.
     *
     * @throws Exception
     *             for some failure scenarios.
     */
    @Test
    public void testBase64EmptyOutputStreamMimeChunkSize() throws Exception {
        testBase64EmptyOutputStream(BaseNCodec.MIME_CHUNK_SIZE);
    }

    /**
     * Test the Base64OutputStream implementation against empty input.
     *
     * @throws Exception
     *             for some failure scenarios.
     */
    @Test
    public void testBase64EmptyOutputStreamPemChunkSize() throws Exception {
        testBase64EmptyOutputStream(BaseNCodec.PEM_CHUNK_SIZE);
    }

    private void testBase64EmptyOutputStream(final int chunkSize) throws Exception {
        final byte[] emptyEncoded = new byte[0];
        final byte[] emptyDecoded = new byte[0];
        testByteByByte(emptyEncoded, emptyDecoded, chunkSize, CRLF);
        testByChunk(emptyEncoded, emptyDecoded, chunkSize, CRLF);
    }

    /**
     * Test the Base64OutputStream implementation
     *
     * @throws Exception
     *             for some failure scenarios.
     */
    @Test
    public void testBase64OutputStreamByChunk() throws Exception {
        // Hello World test.
        byte[] encoded = Strings.getBytesUtf8("SGVsbG8gV29ybGQ=\r\n");
        byte[] decoded = Strings.getBytesUtf8(STRING_FIXTURE);
        testByChunk(encoded, decoded, BaseNCodec.MIME_CHUNK_SIZE, CRLF);

        // Single Byte test.
        encoded = Strings.getBytesUtf8("AA==\r\n");
        decoded = new byte[]{(byte) 0};
        testByChunk(encoded, decoded, BaseNCodec.MIME_CHUNK_SIZE, CRLF);

        // OpenSSL interop test.
        encoded = Strings.getBytesUtf8(Base64TestData.ENCODED_64_CHARS_PER_LINE);
        decoded = Base64TestData.DECODED;
        testByChunk(encoded, decoded, BaseNCodec.PEM_CHUNK_SIZE, LF);

        // Single Line test.
        final String singleLine = Base64TestData.ENCODED_64_CHARS_PER_LINE.replaceAll("\n", "");
        encoded = Strings.getBytesUtf8(singleLine);
        decoded = Base64TestData.DECODED;
        testByChunk(encoded, decoded, 0, LF);

        // test random data of sizes 0 thru 150
        for (int i = 0; i <= 150; i++) {
            final byte[][] randomData = Base64TestData.randomData(i, false);
            encoded = randomData[1];
            decoded = randomData[0];
            testByChunk(encoded, decoded, 0, LF);
        }
    }

    /**
     * Test the Base64OutputStream implementation
     *
     * @throws Exception
     *             for some failure scenarios.
     */
    @Test
    public void testBase64OutputStreamByteByByte() throws Exception {
        // Hello World test.
        byte[] encoded = Strings.getBytesUtf8("SGVsbG8gV29ybGQ=\r\n");
        byte[] decoded = Strings.getBytesUtf8(STRING_FIXTURE);
        testByteByByte(encoded, decoded, 76, CRLF);

        // Single Byte test.
        encoded = Strings.getBytesUtf8("AA==\r\n");
        decoded = new byte[]{(byte) 0};
        testByteByByte(encoded, decoded, 76, CRLF);

        // OpenSSL interop test.
        encoded = Strings.getBytesUtf8(Base64TestData.ENCODED_64_CHARS_PER_LINE);
        decoded = Base64TestData.DECODED;
        testByteByByte(encoded, decoded, 64, LF);

        // Single Line test.
        final String singleLine = Base64TestData.ENCODED_64_CHARS_PER_LINE.replaceAll("\n", "");
        encoded = Strings.getBytesUtf8(singleLine);
        decoded = Base64TestData.DECODED;
        testByteByByte(encoded, decoded, 0, LF);

        // test random data of sizes 0 thru 150
        for (int i = 0; i <= 150; i++) {
            final byte[][] randomData = Base64TestData.randomData(i, false);
            encoded = randomData[1];
            decoded = randomData[0];
            testByteByByte(encoded, decoded, 0, LF);
        }
    }

    /**
     * Test method does three tests on the supplied data: 1. encoded ---[DECODE]--> decoded 2. decoded ---[ENCODE]-->
     * encoded 3. decoded ---[WRAP-WRAP-WRAP-etc...] --> decoded
     * <p/>
     * By "[WRAP-WRAP-WRAP-etc...]" we mean situation where the Base64OutputStream wraps itself in encode and decode
     * mode over and over again.
     *
     * @param encoded
     *            base64 encoded data
     * @param decoded
     *            the data from above, but decoded
     * @param chunkSize
     *            chunk size (line-length) of the base64 encoded data.
     * @param seperator
     *            Line separator in the base64 encoded data.
     * @throws Exception
     *             Usually signifies a bug in the Base64 commons-codec implementation.
     */
    private void testByChunk(final byte[] encoded, final byte[] decoded, final int chunkSize, final byte[] seperator) throws Exception {

        // Start with encode.
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        OutputStream out = new Base64OutputStream(byteOut, true, chunkSize, seperator);
        out.write(decoded);
        out.close();
        byte[] output = byteOut.toByteArray();
        assertTrue("Streaming chunked base64 encode", Arrays.equals(output, encoded));

        // Now let's try decode.
        byteOut = new ByteArrayOutputStream();
        out = new Base64OutputStream(byteOut, false);
        out.write(encoded);
        out.close();
        output = byteOut.toByteArray();
        assertTrue("Streaming chunked base64 decode", Arrays.equals(output, decoded));

        // I always wanted to do this! (wrap encoder with decoder etc etc).
        byteOut = new ByteArrayOutputStream();
        out = byteOut;
        for (int i = 0; i < 10; i++) {
            out = new Base64OutputStream(out, false);
            out = new Base64OutputStream(out, true, chunkSize, seperator);
        }
        out.write(decoded);
        out.close();
        output = byteOut.toByteArray();

        assertTrue("Streaming chunked base64 wrap-wrap-wrap!", Arrays.equals(output, decoded));
    }

    /**
     * Test method does three tests on the supplied data: 1. encoded ---[DECODE]--> decoded 2. decoded ---[ENCODE]-->
     * encoded 3. decoded ---[WRAP-WRAP-WRAP-etc...] --> decoded
     * <p/>
     * By "[WRAP-WRAP-WRAP-etc...]" we mean situation where the Base64OutputStream wraps itself in encode and decode
     * mode over and over again.
     *
     * @param encoded
     *            base64 encoded data
     * @param decoded
     *            the data from above, but decoded
     * @param chunkSize
     *            chunk size (line-length) of the base64 encoded data.
     * @param seperator
     *            Line separator in the base64 encoded data.
     * @throws Exception
     *             Usually signifies a bug in the Base64 commons-codec implementation.
     */
    private void testByteByByte(final byte[] encoded, final byte[] decoded, final int chunkSize, final byte[] seperator) throws Exception {

        // Start with encode.
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        OutputStream out = new Base64OutputStream(byteOut, true, chunkSize, seperator);
        for (final byte element : decoded) {
            out.write(element);
        }
        out.close();
        byte[] output = byteOut.toByteArray();
        assertTrue("Streaming byte-by-byte base64 encode", Arrays.equals(output, encoded));

        // Now let's try decode.
        byteOut = new ByteArrayOutputStream();
        out = new Base64OutputStream(byteOut, false);
        for (final byte element : encoded) {
            out.write(element);
        }
        out.close();
        output = byteOut.toByteArray();
        assertTrue("Streaming byte-by-byte base64 decode", Arrays.equals(output, decoded));

        // Now let's try decode with tonnes of flushes.
        byteOut = new ByteArrayOutputStream();
        out = new Base64OutputStream(byteOut, false);
        for (final byte element : encoded) {
            out.write(element);
            out.flush();
        }
        out.close();
        output = byteOut.toByteArray();
        assertTrue("Streaming byte-by-byte flush() base64 decode", Arrays.equals(output, decoded));

        // I always wanted to do this! (wrap encoder with decoder etc etc).
        byteOut = new ByteArrayOutputStream();
        out = byteOut;
        for (int i = 0; i < 10; i++) {
            out = new Base64OutputStream(out, false);
            out = new Base64OutputStream(out, true, chunkSize, seperator);
        }
        for (final byte element : decoded) {
            out.write(element);
        }
        out.close();
        output = byteOut.toByteArray();

        assertTrue("Streaming byte-by-byte base64 wrap-wrap-wrap!", Arrays.equals(output, decoded));
    }

    /**
     * Tests Base64OutputStream.write for expected IndexOutOfBoundsException conditions.
     *
     * @throws Exception
     *             for some failure scenarios.
     */
    @Test
    public void testWriteOutOfBounds() throws Exception {
        final byte[] buf = new byte[1024];
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final Base64OutputStream out = new Base64OutputStream(bout);

        try {
            out.write(buf, -1, 1);
            fail("Expected Base64OutputStream.write(buf, -1, 1) to throw a IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException ioobe) {
            // Expected
        }

        try {
            out.write(buf, 1, -1);
            fail("Expected Base64OutputStream.write(buf, 1, -1) to throw a IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException ioobe) {
            // Expected
        }

        try {
            out.write(buf, buf.length + 1, 0);
            fail("Expected Base64OutputStream.write(buf, buf.length + 1, 0) to throw a IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException ioobe) {
            // Expected
        }

        try {
            out.write(buf, buf.length - 1, 2);
            fail("Expected Base64OutputStream.write(buf, buf.length - 1, 2) to throw a IndexOutOfBoundsException");
        } catch (final IndexOutOfBoundsException ioobe) {
            // Expected
        }
        out.close();
    }

    /**
     * Tests Base64OutputStream.write(null).
     *
     * @throws Exception
     *             for some failure scenarios.
     */
    @Test
    public void testWriteToNullCoverage() throws Exception {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final Base64OutputStream out = new Base64OutputStream(bout);
        try {
            out.write(null, 0, 0);
            fail("Expcted Base64OutputStream.write(null) to throw a NullPointerException");
        } catch (final NullPointerException e) {
            // Expected
        } finally {
            out.close();
        }
    }

}
