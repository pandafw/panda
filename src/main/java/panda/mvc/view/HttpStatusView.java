package panda.mvc.view;

import panda.lang.Exceptions;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.net.http.HttpException;
import panda.net.http.HttpStatus;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * 返回特定的响应码
 * <p/>
 * <b>注意,400或以上,会调用resp.sendError,而非resp.setStatus.这样做的原因是
 * errorPage的配置,只有resp.sendError会触发,且绝大多数情况下,只会配置400或以上</b>
 */
public class HttpStatusView implements View {

	public static final View BAD_REQUEST = new HttpStatusView(HttpStatus.SC_BAD_REQUEST);
	public static final View FORBIDDEN = new HttpStatusView(HttpStatus.SC_FORBIDDEN);
	public static final View NOT_FOUND = new HttpStatusView(HttpStatus.SC_NOT_FOUND);
	public static final View SERVER_ERROR = new HttpStatusView(HttpStatus.SC_INTERNAL_SERVER_ERROR);
	public static final View BAD_GATEWAY = new HttpStatusView(HttpStatus.SC_BAD_GATEWAY);

	private int statusCode;

	public HttpStatusView(int statusCode) {
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
