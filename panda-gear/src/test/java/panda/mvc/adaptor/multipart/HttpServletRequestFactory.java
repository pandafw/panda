package panda.mvc.adaptor.multipart;

import javax.servlet.http.HttpServletRequest;

import panda.io.MimeType;

final class HttpServletRequestFactory {

	static public HttpServletRequest createHttpServletRequestWithNullContentType() {
		byte[] requestData = "foobar".getBytes();
		return new MockHttpServletRequest(requestData, null);
	}

	static public HttpServletRequest createValidHttpServletRequest(final String[] strFileNames) {
		// todo - provide a real implementation

		StringBuilder sbRequestData = new StringBuilder();

		for (String strFileName : strFileNames) {
			sbRequestData.append(strFileName);
		}

		byte[] requestData = null;
		requestData = sbRequestData.toString().getBytes();

		return new MockHttpServletRequest(requestData, MimeType.MULTIPART_FORM_DATA);
	}

	static public HttpServletRequest createInvalidHttpServletRequest() {
		byte[] requestData = "foobar".getBytes();
		return new MockHttpServletRequest(requestData, MimeType.MULTIPART_FORM_DATA);
	}

}
