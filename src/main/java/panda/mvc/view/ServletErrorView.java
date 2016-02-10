package panda.mvc.view;

import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.View;
import panda.mvc.processor.ViewProcessor;
import panda.net.http.HttpException;
import panda.net.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

public class ServletErrorView implements View {
	private static final Log log = Logs.getLog(ServletErrorView.class);
	
	public static final String TPL = "/panda/mvc/view/servlet-error.ftl";

	public static final View BAD_REQUEST = new ServletErrorView(HttpStatus.SC_BAD_REQUEST);
	public static final View FORBIDDEN = new ServletErrorView(HttpStatus.SC_FORBIDDEN);
	public static final View NOT_FOUND = new ServletErrorView(HttpStatus.SC_NOT_FOUND);
	public static final View SERVER_ERROR = new ServletErrorView(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	public static final View BAD_GATEWAY = new ServletErrorView(HttpStatus.SC_BAD_GATEWAY);

	private int statusCode;

	public ServletErrorView(int statusCode) {
		this.statusCode = statusCode;
	}

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

		String customView = ac.getIoc().getIfExists(String.class, MvcConstants.SERVLET_ERROR_VIEW);
		if (Strings.isEmpty(customView)) {
			customView = TPL;
		}
		
		res.setStatus(code);

		View view = ViewProcessor.evalView(ac.getIoc(), customView);
		if (view == null) {
			log.error("Failed to create view: " + customView);
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
