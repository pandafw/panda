package panda.mock.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import panda.lang.Asserts;

/**
 * Implementation of the {@link javax.servlet.FilterConfig} interface which
 * simply passes the call through to a given Filter/FilterChain combination
 * (indicating the next Filter in the chain along with the FilterChain that it is
 * supposed to work on) or to a given Servlet (indicating the end of the chain).
 *
 * @see javax.servlet.Filter
 * @see javax.servlet.Servlet
 * @see MockFilterChain
 */
public class PassThroughFilterChain implements FilterChain {

	private Filter filter;

	private FilterChain nextFilterChain;

	private Servlet servlet;


	/**
	 * Create a new PassThroughFilterChain that delegates to the given Filter,
	 * calling it with the given FilterChain.
	 * @param filter the Filter to delegate to
	 * @param nextFilterChain the FilterChain to use for that next Filter
	 */
	public PassThroughFilterChain(Filter filter, FilterChain nextFilterChain) {
		Asserts.notNull(filter, "Filter must not be null");
		Asserts.notNull(nextFilterChain, "'FilterChain must not be null");
		this.filter = filter;
		this.nextFilterChain = nextFilterChain;
	}

	/**
	 * Create a new PassThroughFilterChain that delegates to the given Servlet.
	 * @param servlet the Servlet to delegate to
	 */
	public PassThroughFilterChain(Servlet servlet) {
		Asserts.notNull(servlet, "Servlet must not be null");
		this.servlet = servlet;
	}


	/**
	 * Pass the call on to the Filter/Servlet.
	 */
	public void doFilter(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		if (this.filter != null) {
			this.filter.doFilter(request, response, this.nextFilterChain);
		}
		else {
			this.servlet.service(request, response);
		}
	}

}
