package panda.servlet.mock;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import panda.lang.Asserts;

/**
 * Mock implementation of the {@link javax.servlet.FilterConfig} interface.
 *
 * <p>Used for testing the web framework; also useful for testing
 * custom {@link javax.servlet.Filter} implementations.
 *
 * @see MockFilterConfig
 * @see PassThroughFilterChain
 */
public class MockFilterChain implements FilterChain {

	private ServletRequest request;

	private ServletResponse response;


	/**
	 * Records the request and response.
	 */
	public void doFilter(ServletRequest request, ServletResponse response) {
		Asserts.notNull(request, "Request must not be null");
		Asserts.notNull(response, "Response must not be null");
		if (this.request != null) {
			throw new IllegalStateException("This FilterChain has already been called!");
		}
		this.request = request;
		this.response = response;
	}

	/**
	 * Return the request that {@link #doFilter} has been called with.
	 */
	public ServletRequest getRequest() {
		return this.request;
	}

	/**
	 * Return the response that {@link #doFilter} has been called with.
	 */
	public ServletResponse getResponse() {
		return this.response;
	}

}
