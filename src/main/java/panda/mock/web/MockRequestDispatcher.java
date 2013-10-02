package panda.mock.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import panda.lang.Asserts;
import panda.log.Log;
import panda.log.Logs;


/**
 * Mock implementation of the {@link javax.servlet.RequestDispatcher} interface.
 *
 * <p>Used for testing the web framework; typically not necessary for
 * testing application controllers.
 *
 */
public class MockRequestDispatcher implements RequestDispatcher {

	private final Log logger = Logs.getLog(getClass());

	private final String url;


	/**
	 * Create a new MockRequestDispatcher for the given URL.
	 * @param url the URL to dispatch to.
	 */
	public MockRequestDispatcher(String url) {
		Asserts.notNull(url, "URL must not be null");
		this.url = url;
	}


	public void forward(ServletRequest request, ServletResponse response) {
		Asserts.notNull(request, "Request must not be null");
		Asserts.notNull(response, "Response must not be null");
		if (response.isCommitted()) {
			throw new IllegalStateException("Cannot perform forward - response is already committed");
		}
		getMockHttpServletResponse(response).setForwardedUrl(this.url);
		if (logger.isDebugEnabled()) {
			logger.debug("MockRequestDispatcher: forwarding to URL [" + this.url + "]");
		}
	}

	public void include(ServletRequest request, ServletResponse response) {
		Asserts.notNull(request, "Request must not be null");
		Asserts.notNull(response, "Response must not be null");
		getMockHttpServletResponse(response).addIncludedUrl(this.url);
		if (logger.isDebugEnabled()) {
			logger.debug("MockRequestDispatcher: including URL [" + this.url + "]");
		}
	}

	/**
	 * Obtain the underlying MockHttpServletResponse,
	 * unwrapping {@link HttpServletResponseWrapper} decorators if necessary.
	 */
	protected MockHttpServletResponse getMockHttpServletResponse(ServletResponse response) {
		if (response instanceof MockHttpServletResponse) {
			return (MockHttpServletResponse) response;
		}
		if (response instanceof HttpServletResponseWrapper) {
			return getMockHttpServletResponse(((HttpServletResponseWrapper) response).getResponse());
		}
		throw new IllegalArgumentException("MockRequestDispatcher requires MockHttpServletResponse");
	}

}
