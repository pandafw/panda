package panda.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <pre>
 * Set request's character encoding from filter init parameter.
 *
 * &lt;filter&gt;
 *  &lt;filter-name&gt;encoding-filter&lt;/filter-name&gt;
 *  &lt;filter-class&gt;panda.servlet.filter.CharacterEncodingFilter&lt;/filter-class&gt;
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
 */
public class CharacterEncodingFilter implements Filter {
	private String encoding;
	private boolean force = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding");
		force = "true".equalsIgnoreCase(config.getInitParameter("force"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {
		if (encoding != null && (force || req.getCharacterEncoding() == null)) {
			req.setCharacterEncoding(encoding);
		}

		chain.doFilter(req, res);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
	}
}
