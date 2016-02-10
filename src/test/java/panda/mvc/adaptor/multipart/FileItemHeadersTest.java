package panda.mvc.adaptor.multipart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

import panda.mvc.adaptor.multipart.FileItemHeaders;
import panda.mvc.adaptor.multipart.FileItemHeadersImpl;

/**
 * Unit tests {@link FileItemHeaders} and {@link FileItemHeadersImpl}.
 */
public class FileItemHeadersTest {

	/**
	 * @throws Exception
	 */
	@Test
	public void testFileItemHeaders() throws Exception {
		FileItemHeadersImpl aMutableFileItemHeaders = new FileItemHeadersImpl();
		aMutableFileItemHeaders
			.addHeader("Content-Disposition", "form-data; name=\"FileItem\"; filename=\"file1.txt\"");
		aMutableFileItemHeaders.addHeader("Content-Type", "text/plain");

		aMutableFileItemHeaders.addHeader("TestHeader", "headerValue1");
		aMutableFileItemHeaders.addHeader("TestHeader", "headerValue2");
		aMutableFileItemHeaders.addHeader("TestHeader", "headerValue3");
		aMutableFileItemHeaders.addHeader("testheader", "headerValue4");

		Iterator<String> headerNameEnumeration = aMutableFileItemHeaders.getHeaderNames();
		assertEquals("content-disposition", headerNameEnumeration.next());
		assertEquals("content-type", headerNameEnumeration.next());
		assertEquals("testheader", headerNameEnumeration.next());
		assertFalse(headerNameEnumeration.hasNext());

		assertEquals(aMutableFileItemHeaders.getHeader("Content-Disposition"),
			"form-data; name=\"FileItem\"; filename=\"file1.txt\"");
		assertEquals(aMutableFileItemHeaders.getHeader("Content-Type"), "text/plain");
		assertEquals(aMutableFileItemHeaders.getHeader("content-type"), "text/plain");
		assertEquals(aMutableFileItemHeaders.getHeader("TestHeader"), "headerValue1");
		assertNull(aMutableFileItemHeaders.getHeader("DummyHeader"));

		Iterator<String> headerValueEnumeration;

		headerValueEnumeration = aMutableFileItemHeaders.getHeaders("Content-Type");
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "text/plain");
		assertFalse(headerValueEnumeration.hasNext());

		headerValueEnumeration = aMutableFileItemHeaders.getHeaders("content-type");
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "text/plain");
		assertFalse(headerValueEnumeration.hasNext());

		headerValueEnumeration = aMutableFileItemHeaders.getHeaders("TestHeader");
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "headerValue1");
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "headerValue2");
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "headerValue3");
		assertTrue(headerValueEnumeration.hasNext());
		assertEquals(headerValueEnumeration.next(), "headerValue4");
		assertFalse(headerValueEnumeration.hasNext());

		headerValueEnumeration = aMutableFileItemHeaders.getHeaders("DummyHeader");
		assertFalse(headerValueEnumeration.hasNext());
	}

}
