package panda.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletFilter {
	/**
	 * process request filter
	 * @param req servlet request
	 * @param res servlet response
	 * @param sc servlet chain
	 * @return true if request has been processed
	 */
	boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc);
}
