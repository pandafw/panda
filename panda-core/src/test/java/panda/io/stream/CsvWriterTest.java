package panda.io.stream;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * JUnit Test Case for {@link CsvWriter}.
 */
public class CsvWriterTest {

	@Test
	public void testAutoQuote() throws IOException {
		StringBuilder a = new StringBuilder();
		CsvWriter cw = new CsvWriter(a);
		cw.writeArray(new String[] { "a", "b\"c", "d\ref" });
		cw.writeArray(new String[] { "1", "\n23", "45\r\n6" });
		cw.close();
		Assert.assertEquals("a,\"b\"\"c\",\"d\ref\"\r\n1,\"\n23\",\"45\r\n6\"\r\n", a.toString());
	}

	@Test
	public void testUnQuote() throws IOException {
		StringBuilder a = new StringBuilder();
		CsvWriter cw = new CsvWriter(a);
		cw.writeArray(new String[] { "a", "bc", "def" });
		cw.writeArray(new String[] { "1", "23", "456" });
		cw.close();
		Assert.assertEquals("a,bc,def\r\n1,23,456\r\n", a.toString());
	}

	@Test
	public void testForceQuote() throws IOException {
		StringBuilder a = new StringBuilder();
		CsvWriter cw = new CsvWriter(a);
		cw.setForceQuote(true);
		cw.writeArray(new String[] { "a", "bc", "def" });
		cw.writeArray(new String[] { "1", "23", "456" });
		cw.close();
		Assert.assertEquals("\"a\",\"bc\",\"def\"\r\n\"1\",\"23\",\"456\"\r\n", a.toString());
	}

}
