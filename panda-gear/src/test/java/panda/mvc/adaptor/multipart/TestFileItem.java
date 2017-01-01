package panda.mvc.adaptor.multipart;

import java.io.IOException;

import panda.io.Streams;
import panda.io.stream.ByteArrayOutputStream;

public class TestFileItem {
	/**
	 * The file items content type.
	 */
	private final String contentType;

	/**
	 * The file items field name.
	 */
	private final String fieldName;

	/**
	 * The file items file name.
	 */
	private final String name;

	/**
	 * Whether the file item is a form field.
	 */
	private final boolean formField;

	/**
	 * The file items input stream.
	 */
	private final byte[] body;

	/**
	 * The headers, if any.
	 */
	private final FileItemHeaders headers;

	/**
	 * Creates a new instance.
	 * 
	 * @throws IOException Creating the file item failed.
	 */
	public TestFileItem(FileItemStream item) throws IOException {
		contentType = item.getContentType();
		fieldName = item.getFieldName();
		name = item.getName();
		formField = item.isFormField();
		headers = item.getHeaders();
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			Streams.copy(item.openStream(), os);
		}
		finally {
			Streams.safeClose(os);
		}
		body = os.toByteArray();
	}

	/**
	 * Returns the items content type, or null.
	 * 
	 * @return Content type, if known, or null.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Returns the items field name.
	 * 
	 * @return Field name.
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Returns the items file name.
	 * 
	 * @return File name, if known, or null.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns, whether this is a form field.
	 * 
	 * @return True, if the item is a form field, otherwise false.
	 */
	public boolean isFormField() {
		return formField;
	}

	/**
	 * Returns the file item headers.
	 * 
	 * @return The items header object
	 */
	public FileItemHeaders getHeaders() {
		return headers;
	}
	
	public byte[] getBody() {
		return body;
	}
	
	public String getString() {
		return new String(body);
	}
}
