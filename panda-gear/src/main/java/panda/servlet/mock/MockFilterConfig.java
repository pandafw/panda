package panda.servlet.mock;


import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

import panda.lang.Asserts;
import panda.lang.Collections;

/**
 * Mock implementation of the {@link javax.servlet.FilterConfig} interface.
 *
 * <p>Used for testing the web framework; also useful for testing
 * custom {@link javax.servlet.Filter} implementations.
 *
 * @see MockFilterChain
 * @see PassThroughFilterChain
 */
public class MockFilterConfig implements FilterConfig {

	private final ServletContext servletContext;

	private final String filterName;

	private final Map<String, String> initParameters = new LinkedHashMap<String, String>();


	/**
	 * Create a new MockFilterConfig with a default {@link MockServletContext}.
	 */
	public MockFilterConfig() {
		this(null, "");
	}

	/**
	 * Create a new MockFilterConfig with a default {@link MockServletContext}.
	 * @param filterName the name of the filter
	 */
	public MockFilterConfig(String filterName) {
		this(null, filterName);
	}

	/**
	 * Create a new MockFilterConfig.
	 * @param servletContext the ServletContext that the servlet runs in
	 */
	public MockFilterConfig(ServletContext servletContext) {
		this(servletContext, "");
	}

	/**
	 * Create a new MockFilterConfig.
	 * @param servletContext the ServletContext that the servlet runs in
	 * @param filterName the name of the filter
	 */
	public MockFilterConfig(ServletContext servletContext, String filterName) {
		this.servletContext = (servletContext != null ? servletContext : new MockServletContext());
		this.filterName = filterName;
	}


	public String getFilterName() {
		return filterName;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void addInitParameter(String name, String value) {
		Asserts.notNull(name, "Parameter name must not be null");
		this.initParameters.put(name, value);
	}

	public String getInitParameter(String name) {
		Asserts.notNull(name, "Parameter name must not be null");
		return this.initParameters.get(name);
	}

	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(this.initParameters.keySet());
	}

}
