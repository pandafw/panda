package panda.mvc.adaptor.multipart;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * Base class for deriving test cases.
 */
public abstract class FileUploadTestCase {

	protected static final String CONTENT_TYPE = "multipart/form-data; boundary=---1234";

	protected FileItemIterator parseUpload(int pLength, InputStream pStream) throws IOException {
		String contentType = "multipart/form-data; boundary=---1234";

		FileUploader upload = new FileUploader();
		HttpServletRequest request = new MockHttpServletRequest(pStream, pLength, contentType);

		return upload.getItemIterator(request);
	}


	protected List<TestFileItem> parseUpload(InputStream pStream, int pLength) throws IOException {
		String contentType = "multipart/form-data; boundary=---1234";

		FileUploader upload = new FileUploader();
		HttpServletRequest request = new MockHttpServletRequest(pStream, pLength, contentType);

		List<TestFileItem> fileItems = parseUpload(upload, request);
		return fileItems;
	}


	protected List<TestFileItem> parseUpload(byte[] bytes) throws IOException {
		return parseUpload(bytes, CONTENT_TYPE);
	}

	protected List<TestFileItem> parseUpload(byte[] bytes, String contentType) throws IOException {
		HttpServletRequest request = new MockHttpServletRequest(bytes, contentType);
		return parseUpload(request);
	}
	
	protected List<TestFileItem> parseUpload(HttpServletRequest request) throws IOException {
		FileUploader fu = new FileUploader();
		return parseUpload(fu, request);
	}

	protected List<TestFileItem> parseUpload(FileUploader fu, HttpServletRequest request) throws IOException {
		List<TestFileItem> fileItems = new ArrayList<TestFileItem>();
		
		FileItemIterator iter = fu.getItemIterator(request);
		while (iter.hasNext()) {
			fileItems.add(new TestFileItem(iter.next()));
		}

		return fileItems;
	}

	protected List<TestFileItem> parseUpload(String content) throws UnsupportedEncodingException, IOException {
		byte[] bytes = content.getBytes("US-ASCII");
		return parseUpload(bytes, CONTENT_TYPE);
	}

}
