package panda.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletFilter {
	boolean doFilter(HttpServletRequest req, HttpServletResponse res, ServletChain sc);
}
