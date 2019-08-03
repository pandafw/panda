package panda.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Mvc servlet filter chain
 */
public interface ServletChain {
	/**
	 * add a filter
	 * @param filter filter
	 */
	void addFilter(ServletFilter filter);

	/**
	 * process chain
	 * @param req servlet request
	 * @param res servlet response
	 * @return true if request has been handled
	 */
	boolean doChain(HttpServletRequest req, HttpServletResponse res);

	/**
	 * do next filter
	 * @param req servlet request
	 * @param res servlet response
	 * @return true if request has been handled
	 */
	boolean doNext(HttpServletRequest req, HttpServletResponse res);
}
