package panda.servlet.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * RequestEncodingFilter
 *
 * <pre>
 * Set request's character encoding from filter init parameter.
 *
 * &lt;filter&gt;
 *  &lt;filter-name&gt;encoding-filter&lt;/filter-name&gt;
 *  &lt;filter-class&gt;panda.servlet.filters.RequestEncodingFilter&lt;/filter-class&gt;
 *  &lt;init-param&gt;            
 *    &lt;param-name&gt;encoding&lt;/param-name&gt;            
 *    &lt;param-value&gt;UTF-8&lt;/param-value&gt;        
 *  &lt;/init-param&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *  &lt;filter-name&gt;encoding-filter&lt;/filter-name&gt;
 *  &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 *
 * </pre>
 * 
 * @author yf.frank.wang@gmail.com
 */
public class RequestEncodingFilter implements Filter {
	private String encoding;
	private boolean forceEncoding = false;

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding");
		forceEncoding = Boolean.TRUE.equals(config.getInitParameter("forceEncoding"));
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		if (encoding != null && (forceEncoding || req.getCharacterEncoding() == null)) {
			req.setCharacterEncoding(encoding);
		}

		chain.doFilter(req, res);
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
}
