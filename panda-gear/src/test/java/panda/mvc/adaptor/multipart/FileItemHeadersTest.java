package panda.mvc.adaptor.multipart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

/**
 * Unit tests {@link FileItemHeaders}
 */
public class FileItemHeadersTest {

	/**
	 * @throws Exception
	 */
	@Test
	public void testFileItemHeaders() throws Exception {
		FileItemHeaders aMutableFileItemHeaders = new FileItemHeaders();
		aMutableFileItemHeaders.add("Content-Disposition", "form-data; name=\"FileItem\"; filename=\"file1.txt\"");
		aMutableFileItemHeaders.add("Content-Type", "text/plain");

		aMutableFileItemHeaders.add("TestHeader", "headerValue1");
		aMutableFileItemHeaders.add("TestHeader", "headerValue2");
		aMutableFileItemHeaders.add("TestHeader", "headerValue3");
		aMutableFileItemHeaders.add("testheader", "headerValue4");

		Iterator<String> headerNameEnumeration = aMutableFileItemHeaders.keySet().iterator();
		assertEquals("Content-Disposition", headerNameEnumeration.next());
		assertEquals("Content-Type", headerNameEnumeration.next());
		assertEquals("TestHeader", headerNameEnumeration.next());
		assertFalse(headerNameEnumeration.hasNext());

		assertEquals(aMutableFileItemHeaders.getString("Content-Disposition"),
			"form-data; name=\"FileItem\"; filename=\"file1.txt\"");
		assertEquals(aMutableFileItemHeaders.getString("Content-Type"), "text/plain");
		assertEquals(aMutableFileItemHeaders.getString("content-type"), "text/plain");
		assertEquals(aMutableFileItemHeaders.getString("TestHeader"), "headerValue1");

		assertNull(aMutableFileItemHeaders.getString("DummyHeader"));
		assertNull(aMutableFileItemHeaders.getStrings("DummyHeader"));

		Iterator<String> headerValueEnumeration;

		headerValueEnumeration = aMutableFileItemHeaders.getStrings("Content-Type").iterator();
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "text/plain");
		assertFalse(headerValueEnumeration.hasNext());

		headerValueEnumeration = aMutableFileItemHeaders.getStrings("content-type").iterator();
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "text/plain");
		assertFalse(headerValueEnumeration.hasNext());

		headerValueEnumeration = aMutableFileItemHeaders.getStrings("TestHeader").iterator();
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "headerValue1");
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "headerValue2");
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "headerValue3");
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "headerValue4");
		assertFalse(headerValueEnumeration.hasNext());

	}

}
