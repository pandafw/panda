package panda.io.stream;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Arrays;

/**
 * JUnit Test Case for {@link CsvWriter}.
 */
public class XlsxWriterTest {

	@Test
	public void testNormal() throws IOException {
		List<String> a1 = Arrays.toList("a", "b\"c", "d\nef" );
		List<String> a2 = Arrays.toList("1", "\n23", "45\n\n6" );
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XlsxWriter xw = new XlsxWriter(baos);
		xw.setAllString(true);
		xw.writeList(a1);
		xw.writeList(a2);
		xw.close();
		
		XlsxReader xr = new XlsxReader(baos.toInputStream());
		List<Object> e1 = xr.readList();
		Assert.assertEquals(a1, e1);

		List<Object> e2 = xr.readList();
		Assert.assertEquals(a2, e2);
		
		xr.close();
	}
}
