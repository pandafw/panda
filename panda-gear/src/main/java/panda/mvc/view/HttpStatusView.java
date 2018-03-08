package panda.mvc.view;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.net.http.HttpException;
import panda.net.http.HttpStatus;

/**
 * 返回特定的响应码
 * <p/>
 * <b>注意,400或以上,会调用resp.sendError,而非resp.setStatus.这样做的原因是
 * errorPage的配置,只有resp.sendError会触发,且绝大多数情况下,只会配置400或以上</b>
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

		if (code >= 400) {
			try {
				res.sendError(code);
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		else {
			res.setStatus(code);
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + statusCode;
	}
}
