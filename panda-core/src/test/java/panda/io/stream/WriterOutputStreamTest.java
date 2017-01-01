package panda.io.stream;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Random;

import junit.framework.TestCase;

@SuppressWarnings("resource")
public class WriterOutputStreamTest extends TestCase {
    private static final String TEST_STRING = "\u00e0 peine arriv\u00e9s nous entr\u00e2mes dans sa chambre";
    private static final String LARGE_TEST_STRING;
    
    static {
        StringBuilder buffer = new StringBuilder();
        for (int i=0; i<100; i++) {
            buffer.append(TEST_STRING);
        }
        LARGE_TEST_STRING = buffer.toString();
    }
    
    private Random random = new Random();
    
    private void testWithSingleByteWrite(String testString, String charsetName) throws IOException {
        byte[] bytes = testString.getBytes(charsetName);
        StringWriter writer = new StringWriter();
        WriterOutputStream out = new WriterOutputStream(writer, charsetName);
        for (byte b : bytes) {
            out.write(b);
        }
        out.close();
        assertEquals(testString, writer.toString());
    }
    
    private void testWithBufferedWrite(String testString, String charsetName) throws IOException {
        byte[] expected = testString.getBytes(charsetName);
        StringWriter writer = new StringWriter();
        WriterOutputStream out = new WriterOutputStream(writer, charsetName);
        int offset = 0;
        while (offset < expected.length) {
            int length = Math.min(random.nextInt(128), expected.length-offset);
            out.write(expected, offset, length);
            offset += length;
        }
        out.close();
        assertEquals(testString, writer.toString());
    }
    
    public void testUTF8WithSingleByteWrite() throws IOException {
        testWithSingleByteWrite(TEST_STRING, "UTF-8");
    }
    
    public void testLargeUTF8WithSingleByteWrite() throws IOException {
        testWithSingleByteWrite(LARGE_TEST_STRING, "UTF-8");
    }
    
    public void testUTF8WithBufferedWrite() throws IOException {
        testWithBufferedWrite(TEST_STRING, "UTF-8");
    }
    
    public void testLargeUTF8WithBufferedWrite() throws IOException {
        testWithBufferedWrite(LARGE_TEST_STRING, "UTF-8");
    }
    
    public void testUTF16WithSingleByteWrite() throws IOException {
        testWithSingleByteWrite(TEST_STRING, "UTF-16");
    }

    public void testUTF16WithBufferedWrite() throws IOException {
        testWithBufferedWrite(TEST_STRING, "UTF-16");
    }

    public void testUTF16BEWithSingleByteWrite() throws IOException {
        testWithSingleByteWrite(TEST_STRING, "UTF-16BE");
    }

    public void testUTF16BEWithBufferedWrite() throws IOException {
        testWithBufferedWrite(TEST_STRING, "UTF-16BE");
    }

    public void testUTF16LEWithSingleByteWrite() throws IOException {
        testWithSingleByteWrite(TEST_STRING, "UTF-16LE");
    }

    public void testUTF16LEWithBufferedWrite() throws IOException {
        testWithBufferedWrite(TEST_STRING, "UTF-16LE");
    }

    
	public void testFlush() throws IOException {
        StringWriter writer = new StringWriter();
        WriterOutputStream out = new WriterOutputStream(writer, "us-ascii", 1024, false);
        out.write("abc".getBytes("us-ascii"));
        assertEquals(0, writer.getBuffer().length());
        out.flush();
        assertEquals("abc", writer.toString());
    }
    
    public void testWriteImmediately() throws IOException {
        StringWriter writer = new StringWriter();
        WriterOutputStream out = new WriterOutputStream(writer, "us-ascii", 1024, true);
        out.write("abc".getBytes("us-ascii"));
        assertEquals("abc", writer.toString());
    }
}