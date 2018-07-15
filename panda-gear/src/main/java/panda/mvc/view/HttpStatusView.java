package panda.mvc.view;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.net.http.HttpException;
import panda.net.http.HttpStatus;

/**
 * use response.setStatus(xx) to set http status code
 * <p>
 * NOTE: we don not use response.sendError() even if the status code >= 400
 * </p>
 */
@IocBean(singleton=false)
public class HttpStatusView implements View {
	private int statusCode;

	public HttpStatusView() {
		this(HttpStatus.SC_OK);
	}

	public HttpStatusView(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public void setArgument(String arg) {
		statusCode = Integer.parseInt(arg);
	}

	@Override
	public void render(ActionContext ac) {
		int sc = statusCode;

		Object err = ac.getError();
		if (err instanceof HttpException) {
			sc = ((HttpException)err).getStatus();
		}
		else {
			Object obj = ac.getResult();
			if (obj instanceof HttpException) {
				sc = ((HttpException)obj).getStatus();
			}
		}

		ac.getResponse().setStatus(sc);
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + statusCode;
	}
}
