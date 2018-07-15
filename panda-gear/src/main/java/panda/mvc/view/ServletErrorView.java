package panda.mvc.view;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.View;
import panda.net.http.HttpException;
import panda.net.http.HttpStatus;
import panda.servlet.HttpServlets;

@IocBean(singleton=false)
public class ServletErrorView implements View {
	private static final Log log = Logs.getLog(ServletErrorView.class);
	
	public static final String TPL_SERVLET_ERROR = "/panda/mvc/view/servlet-error.ftl";
	
	@IocInject(value=MvcConstants.SERVLET_ERROR_VIEW, required=false)
	private String errorView = "sftl:" + TPL_SERVLET_ERROR;

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
		// set status code to request and response
		int sc = statusCode;

		Throwable err = ac.getError();
		if (err instanceof HttpException) {
			sc = ((HttpException)err).getStatus();
		}
		else {
			Object obj = ac.getResult();
			if (obj instanceof HttpException) {
				sc = ((HttpException)obj).getStatus();
			}
		}
		ac.getRequest().setAttribute(HttpServlets.SERVLET_ERROR_STATUS_CODE, sc);
		ac.getResponse().setStatus(sc);
		
		// set exception to request
		if (err == null) {
			Object obj = ac.getResult();
			if (obj instanceof Throwable) {
				err = (Throwable)obj;
			}
		}

		if (err != null) {
			ac.getRequest().setAttribute(HttpServlets.SERVLET_ERROR_EXCEPTION, err);
		}
		
		View view = Views.createView(ac, errorView);
		if (view == null) {
			log.error("Failed to create view: " + errorView);
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
