package panda.mvc.adaptor.multipart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

public class FileUploaderTest extends FileUploadTestCase {

	@Test
	public void testWithInvalidRequest() throws IOException {
		HttpServletRequest req = HttpServletRequestFactory.createInvalidHttpServletRequest();

		try {
			parseUpload(req);
			fail("testWithInvalidRequest: expected exception was not thrown");
		}
		catch (InvalidContentTypeException expected) {
			// this exception is expected
		}
	}

	@Test
	public void testWithNullContentType() throws IOException {
		HttpServletRequest req = HttpServletRequestFactory.createHttpServletRequestWithNullContentType();

		try {
			parseUpload(req);
			fail("testWithNullContentType: expected exception was not thrown");
		}
		catch (InvalidContentTypeException expected) {
			// this exception is expected
		}
	}

	@Test
	public void testFileUpload() throws IOException {
		List<TestFileItem> fileItems = parseUpload("-----1234\r\n"
				+ "Content-Disposition: form-data; name=\"file\"; filename=\"foo.tab\"\r\n"
				+ "Content-Type: text/whatever\r\n" + "\r\n" + "This is the content of the file\n" + "\r\n"
				+ "-----1234\r\n" + "Content-Disposition: form-data; name=\"field\"\r\n" + "\r\n" + "fieldValue\r\n"
				+ "-----1234\r\n" + "Content-Disposition: form-data; name=\"multi\"\r\n" + "\r\n" + "value1\r\n"
				+ "-----1234\r\n" + "Content-Disposition: form-data; name=\"multi\"\r\n" + "\r\n" + "value2\r\n"
				+ "-----1234--\r\n");
		assertEquals(4, fileItems.size());

		TestFileItem file = fileItems.get(0);
		assertEquals("file", file.getFieldName());
		assertFalse(file.isFormField());
		assertEquals("This is the content of the file\n", file.getString());
		assertEquals("text/whatever", file.getContentType());
		assertEquals("foo.tab", file.getName());

		TestFileItem field = fileItems.get(1);
		assertEquals("field", field.getFieldName());
		assertTrue(field.isFormField());
		assertEquals("fieldValue", field.getString());

		TestFileItem multi0 = fileItems.get(2);
		assertEquals("multi", multi0.getFieldName());
		assertTrue(multi0.isFormField());
		assertEquals("value1", multi0.getString());

		TestFileItem multi1 = fileItems.get(3);
		assertEquals("multi", multi1.getFieldName());
		assertTrue(multi1.isFormField());
		assertEquals("value2", multi1.getString());
	}

	@Test
	public void testFilenameCaseSensitivity() throws IOException {
		List<TestFileItem> fileItems = parseUpload("-----1234\r\n"
				+ "Content-Disposition: form-data; name=\"FiLe\"; filename=\"FOO.tab\"\r\n"
				+ "Content-Type: text/whatever\r\n" + "\r\n" + "This is the content of the file\n" + "\r\n"
				+ "-----1234--\r\n");
		assertEquals(1, fileItems.size());

		TestFileItem file = fileItems.get(0);
		assertEquals("FiLe", file.getFieldName());
		assertEquals("FOO.tab", file.getName());
	}

	/**
	 * This is what the browser does if you submit the form without choosing a file.
	 * @throws UnsupportedEncodingException UnsupportedEncodingException
	 * @throws IOException IOException
	 */
	@Test
	public void testEmptyFile() throws UnsupportedEncodingException, IOException {
		List<TestFileItem> fileItems = parseUpload("-----1234\r\n"
				+ "Content-Disposition: form-data; name=\"file\"; filename=\"\"\r\n" + "\r\n" + "\r\n"
				+ "-----1234--\r\n");
		assertEquals(1, fileItems.size());

		TestFileItem file = fileItems.get(0);
		assertFalse(file.isFormField());
		assertEquals("", file.getString());
		assertEquals("", file.getName());
	}

	/**
	 * Internet Explorer 5 for the Mac has a bug where the carriage return is missing on any
	 * boundary line immediately preceding an input with type=image. (type=submit does not have the
	 * bug.)
	 * @throws UnsupportedEncodingException UnsupportedEncodingException
	 * @throws IOException IOException
	 */
	@Test
	public void testIE5MacBug() throws UnsupportedEncodingException, IOException {
		List<TestFileItem> fileItems = parseUpload("-----1234\r\n" + "Content-Disposition: form-data; name=\"field1\"\r\n"
				+ "\r\n" + "fieldValue\r\n"
				+ "-----1234\n"
				+ // NOTE \r missing
				"Content-Disposition: form-data; name=\"submitName.x\"\r\n" + "\r\n" + "42\r\n"
				+ "-----1234\n"
				+ // NOTE \r missing
				"Content-Disposition: form-data; name=\"submitName.y\"\r\n" + "\r\n" + "21\r\n" + "-----1234\r\n"
				+ "Content-Disposition: form-data; name=\"field2\"\r\n" + "\r\n" + "fieldValue2\r\n"
				+ "-----1234--\r\n");

		assertEquals(4, fileItems.size());

		TestFileItem field1 = fileItems.get(0);
		assertEquals("field1", field1.getFieldName());
		assertTrue(field1.isFormField());
		assertEquals("fieldValue", field1.getString());

		TestFileItem submitX = fileItems.get(1);
		assertEquals("submitName.x", submitX.getFieldName());
		assertTrue(submitX.isFormField());
		assertEquals("42", submitX.getString());

		TestFileItem submitY = fileItems.get(2);
		assertEquals("submitName.y", submitY.getFieldName());
		assertTrue(submitY.isFormField());
		assertEquals("21", submitY.getString());

		TestFileItem field2 = fileItems.get(3);
		assertEquals("field2", field2.getFieldName());
		assertTrue(field2.isFormField());
		assertEquals("fieldValue2", field2.getString());
	}

	/**
	 * Test for <a href="http://issues.apache.org/jira/browse/FILEUPLOAD-62">FILEUPLOAD-62</a>
	 * @throws Exception Exception
	 */
	@Test
	public void testFILEUPLOAD62() throws Exception {
		final String contentType = "multipart/form-data; boundary=AaB03x";
		final String request = "--AaB03x\r\n" + "content-disposition: form-data; name=\"field1\"\r\n" + "\r\n"
				+ "Joe Blow\r\n" + "--AaB03x\r\n" + "content-disposition: form-data; name=\"pics\"\r\n"
				+ "Content-type: multipart/mixed; boundary=BbC04y\r\n" + "\r\n" + "--BbC04y\r\n"
				+ "Content-disposition: attachment; filename=\"file1.txt\"\r\n" + "Content-Type: text/plain\r\n"
				+ "\r\n" + "... contents of file1.txt ...\r\n" + "--BbC04y\r\n"
				+ "Content-disposition: attachment; filename=\"file2.gif\"\r\n" + "Content-type: image/gif\r\n"
				+ "Content-Transfer-Encoding: binary\r\n" + "\r\n" + "...contents of file2.gif...\r\n"
				+ "--BbC04y--\r\n" + "--AaB03x--";
		
		List<TestFileItem> fileItems = parseUpload(request.getBytes("US-ASCII"), contentType);
		assertEquals(3, fileItems.size());
		
		TestFileItem item0 = fileItems.get(0);
		assertEquals("field1", item0.getFieldName());
		assertNull(item0.getName());
		assertEquals("Joe Blow", new String(item0.getBody()));
		
		TestFileItem item1 = fileItems.get(1);
		assertEquals("pics", item1.getFieldName());
		assertEquals("file1.txt", item1.getName());
		assertEquals("... contents of file1.txt ...", new String(item1.getBody()));
		
		TestFileItem item2 = fileItems.get(2);
		assertEquals("pics", item2.getFieldName());
		assertEquals("file2.gif", item2.getName());
		assertEquals("...contents of file2.gif...", new String(item2.getBody()));
	}

	/**
	 * Test for <a href="http://issues.apache.org/jira/browse/FILEUPLOAD-111">FILEUPLOAD-111</a>
	 * @throws IOException IOException
	 */
	@Test
	public void testFoldedHeaders() throws IOException {
		List<TestFileItem> fileItems = parseUpload("-----1234\r\n"
				+ "Content-Disposition: form-data; name=\"file\"; filename=\"foo.tab\"\r\n"
				+ "Content-Type: text/whatever\r\n" + "\r\n" + "This is the content of the file\n" + "\r\n"
				+ "-----1234\r\n" + "Content-Disposition: form-data; \r\n" + "\tname=\"field\"\r\n" + "\r\n"
				+ "fieldValue\r\n" + "-----1234\r\n" + "Content-Disposition: form-data;\r\n"
				+ "     name=\"multi\"\r\n" + "\r\n" + "value1\r\n" + "-----1234\r\n"
				+ "Content-Disposition: form-data; name=\"multi\"\r\n" + "\r\n" + "value2\r\n" + "-----1234--\r\n");
		assertEquals(4, fileItems.size());

		TestFileItem file = fileItems.get(0);
		assertEquals("file", file.getFieldName());
		assertFalse(file.isFormField());
		assertEquals("This is the content of the file\n", file.getString());
		assertEquals("text/whatever", file.getContentType());
		assertEquals("foo.tab", file.getName());

		TestFileItem field = fileItems.get(1);
		assertEquals("field", field.getFieldName());
		assertTrue(field.isFormField());
		assertEquals("fieldValue", field.getString());

		TestFileItem multi0 = fileItems.get(2);
		assertEquals("multi", multi0.getFieldName());
		assertTrue(multi0.isFormField());
		assertEquals("value1", multi0.getString());

		TestFileItem multi1 = fileItems.get(3);
		assertEquals("multi", multi1.getFieldName());
		assertTrue(multi1.isFormField());
		assertEquals("value2", multi1.getString());
	}

	/**
	 * Test case for <a href="http://issues.apache.org/jira/browse/FILEUPLOAD-130">
	 * @throws Exception Exception
	 */
	@Test
	public void testFileUpload130() throws Exception {
		final String[] headerNames = new String[] { "SomeHeader", "OtherHeader", "YetAnotherHeader", "WhatAHeader" };
		final String[] headerValues = new String[] { "present", "Is there", "Here", "Is That" };
		List<TestFileItem> fileItems = parseUpload("-----1234\r\n"
				+ "Content-Disposition: form-data; name=\"file\"; filename=\"foo.tab\"\r\n"
				+ "Content-Type: text/whatever\r\n"
				+ headerNames[0]
				+ ": "
				+ headerValues[0]
				+ "\r\n"
				+ "\r\n"
				+ "This is the content of the file\n"
				+ "\r\n"
				+ "-----1234\r\n"
				+ "Content-Disposition: form-data; \r\n"
				+ "\tname=\"field\"\r\n"
				+ headerNames[1]
				+ ": "
				+ headerValues[1]
				+ "\r\n"
				+ "\r\n"
				+ "fieldValue\r\n"
				+ "-----1234\r\n"
				+ "Content-Disposition: form-data;\r\n"
				+ "     name=\"multi\"\r\n"
				+ headerNames[2]
				+ ": "
				+ headerValues[2]
				+ "\r\n"
				+ "\r\n"
				+ "value1\r\n"
				+ "-----1234\r\n"
				+ "Content-Disposition: form-data; name=\"multi\"\r\n"
				+ headerNames[3]
				+ ": "
				+ headerValues[3]
				+ "\r\n" + "\r\n" + "value2\r\n" + "-----1234--\r\n");
		assertEquals(4, fileItems.size());

		TestFileItem file = fileItems.get(0);
		assertHeaders(headerNames, headerValues, file, 0);

		TestFileItem field = fileItems.get(1);
		assertHeaders(headerNames, headerValues, field, 1);

		TestFileItem multi0 = fileItems.get(2);
		assertHeaders(headerNames, headerValues, multi0, 2);

		TestFileItem multi1 = fileItems.get(3);
		assertHeaders(headerNames, headerValues, multi1, 3);
	}

	private void assertHeaders(String[] pHeaderNames, String[] pHeaderValues, TestFileItem pItem, int pIndex) {
		for (int i = 0; i < pHeaderNames.length; i++) {
			final String value = pItem.getHeaders().getString(pHeaderNames[i]);
			if (i == pIndex) {
				assertEquals(pHeaderValues[i], value);
			}
			else {
				assertNull(value);
			}
		}
	}

}
