package panda.mvc.view;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;

/**
 * Servlet forward view
 * <p/>
 * Example:
 * <ul>
 * <li>'@Ok("forward")' => path: abc.cbc => /WEB-INF/abc.cbc
 * <li>'@Ok("forward:abc/cbc")' => /WEB-INF/abc/cbc
 * <li>'@Ok("forward:/abc/cbc")' => /abc/cbc
 * <li>'@Ok("forward:/abc/cbc.jsp")' => /abc/cbc.jsp
 * </ul>
 */
@IocBean(singleton=false)
public class ForwardView extends AbstractView {
	private static final Log log = Logs.getLog(ForwardView.class);

	public ForwardView() {
	}
	
	public ForwardView(String location) {
		setArgument(location);
	}
	
	/**
	 * override this method to customize
	 * 
	 * @return extension
	 */
	protected String getExtension() {
		return "";
	}
	
	/**
	 * override this method to customize
	 * 
	 * @return root path
	 */
	protected String getRootPath() {
		return "/WEB-INF";
	}

	public void render(ActionContext ac) {
		String path = argument;
		String args = "";
		if (Strings.isNotEmpty(path)) {
			int q = path.indexOf("?");
			if (q >= 0) {
				// split path and args
				args = path.substring(q);
				path = path.substring(0, q);
			}
		}

		String ext = getExtension();
		
		if (Strings.isEmpty(path)) {
			path = getRootPath() + ac.getPath();
		}
		else if (path.charAt(0) != '/') {
			path = getRootPath() + '/' + path;
		}

		if (!Strings.endsWithIgnoreCase(path, ext)) {
			path += ext;
		}

		// do forward
		forward(ac, path + args);
	}

	protected void forward(ActionContext ac, String path) {
		HttpServletRequest req = ac.getRequest();
		HttpServletResponse res = ac.getResponse();
		try {
			forward(req, res, path);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse res, String path) throws ServletException, IOException {
		if (log.isDebugEnabled()) {
			log.debug("Forward: " + path);
		}
		
		RequestDispatcher rd = req.getRequestDispatcher(path);
		if (rd == null) {
			throw new ServletException("Fail to find Forward " + path);
		}
		
		rd.forward(req, res);
	}

}
