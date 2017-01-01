package panda.net.io;

import java.io.IOException;
import java.io.StringReader;

import panda.net.io.DotTerminatedMessageReader;

import junit.framework.TestCase;

public class DotTerminatedMessageReaderTest extends TestCase {

	private DotTerminatedMessageReader reader;
	private final StringBuilder str = new StringBuilder();
	private final char[] buf = new char[64];
	private static final String CRLF = "\r\n";
	private static final String DOT = ".";
	private static final String EOM = CRLF + DOT + CRLF;

	public void testReadSimpleStringCrLfLineEnding() throws IOException {
		final String test = "Hello World!" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals("Hello World!" + CRLF, str.toString());
	}

	public void testReadSimpleStringLfLineEnding() throws IOException {
		final String test = "Hello World!" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals("Hello World!" + CRLF, str.toString());
	}

	public void testEmbeddedNewlines() throws IOException {
		final String test = "Hello" + CRLF + "World\nA\rB" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals(str.toString(), "Hello" + CRLF + "World\nA\rB" + CRLF);
	}

	public void testDoubleCrBeforeDot() throws IOException {
		final String test = "Hello World!\r" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals("Hello World!\r" + CRLF, str.toString());
	}

	public void testLeadingDot() throws IOException {
		final String test = "Hello World!" + CRLF + "..text" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals("Hello World!" + CRLF + ".text" + CRLF, str.toString());
	}

	public void testEmbeddedDot1() throws IOException {
		final String test = "Hello . World!" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals("Hello . World!" + CRLF, str.toString());
	}

	public void testEmbeddedDot2() throws IOException {
		final String test = "Hello .. World!" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals("Hello .. World!" + CRLF, str.toString());
	}

	public void testEmbeddedDot3() throws IOException {
		final String test = "Hello World." + CRLF + "more" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals("Hello World." + CRLF + "more" + CRLF, str.toString());
	}

	public void testEmbeddedDot4() throws IOException {
		final String test = "Hello World\r.\nmore" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals("Hello World\r.\nmore" + CRLF, str.toString());
	}

	public void testReadLine1() throws Exception {
		final String test = "Hello World" + CRLF + "more" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		String line;
		while ((line = reader.readLine()) != null) {
			str.append(line);
			str.append("#");
		}

		assertEquals("Hello World#more#", str.toString());

	}

	public void testReadLine2() throws Exception {
		final String test = "Hello World\r.\nmore" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		String line;
		while ((line = reader.readLine()) != null) {
			str.append(line);
			str.append("#");
		}

		assertEquals("Hello World\r.\nmore#", str.toString());

	}

	public void testSingleDotWithTrailingText() throws IOException {
		final String test = "Hello World!" + CRLF + ".text" + EOM;
		reader = new DotTerminatedMessageReader(new StringReader(test));

		int read = 0;
		while ((read = reader.read(buf)) != -1) {
			str.append(buf, 0, read);
		}

		assertEquals("Hello World!" + CRLF + ".text" + CRLF, str.toString());
	}

}
