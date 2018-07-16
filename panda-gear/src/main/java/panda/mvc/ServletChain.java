package panda.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Mvc servlet filter chain
 */
public interface ServletChain {
	void addFilter(ServletFilter filter);
	
	boolean doChain(HttpServletRequest req, HttpServletResponse res);

	boolean doNext(HttpServletRequest req, HttpServletResponse res);
}
