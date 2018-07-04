package panda.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.lang.Strings;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;
import panda.servlet.FilteredHttpServletRequestWrapper;

public class ContentDecodingFilter implements Filter {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		if (HttpMethod.POST.equalsIgnoreCase(request.getMethod())) {
			String encoding = request.getHeader(HttpHeader.CONTENT_ENCODING);
			if (encoding != null) {
				if (Strings.containsIgnoreCase(encoding, HttpHeader.CONTENT_ENCODING_GZIP)
						|| Strings.containsIgnoreCase(encoding, HttpHeader.CONTENT_ENCODING_DEFLATE)) {
					request = new FilteredHttpServletRequestWrapper(request);
				}
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
	}
}
