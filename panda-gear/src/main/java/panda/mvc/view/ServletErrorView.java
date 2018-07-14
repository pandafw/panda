package panda.mvc.view;

import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.View;
import panda.net.http.HttpException;
import panda.net.http.HttpStatus;

@IocBean(singleton=false)
public class ServletErrorView implements View {
	private static final Log log = Logs.getLog(ServletErrorView.class);
	
	public static final String TPL = "/panda/mvc/view/servlet-error.ftl";

	public static final View BAD_REQUEST = new ServletErrorView(HttpStatus.SC_BAD_REQUEST);
	public static final View FORBIDDEN = new ServletErrorView(HttpStatus.SC_FORBIDDEN);
	public static final View NOT_FOUND = new ServletErrorView(HttpStatus.SC_NOT_FOUND);
	public static final View SERVER_ERROR = new ServletErrorView(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	public static final View BAD_GATEWAY = new ServletErrorView(HttpStatus.SC_BAD_GATEWAY);

	private int statusCode;

	public ServletErrorView() {
		this(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	}
	
	public ServletErrorView(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public void setArgument(String arg) {
		try {
			statusCode = Integer.parseInt(arg);
		}
		catch (NumberFormatException e) {
			log.warn("Invalid status code: " + arg);
		}
	}
	
	@Override
	public void render(ActionContext ac) {
		HttpServletResponse res = ac.getResponse();

		int code = this.statusCode;
		Object obj = ac.getResult();
		if (obj instanceof HttpException) {
			code = ((HttpException)obj).getStatus();
		}

		Object err = ac.getError();
		if (err instanceof HttpException) {
			code = ((HttpException)err).getStatus();
		}

		String viewer = ac.getIoc().getIfExists(String.class, MvcConstants.SERVLET_ERROR_VIEW);
		if (Strings.isEmpty(viewer)) {
			viewer = TPL;
		}
		
		res.setStatus(code);

		View view = Views.createView(ac, viewer);
		if (view == null) {
			log.error("Failed to create view: " + viewer);
		}
		else {
			view.render(ac);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + statusCode;
	}
}
