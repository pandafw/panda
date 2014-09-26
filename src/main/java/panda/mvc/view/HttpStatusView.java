package panda.mvc.view;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import panda.lang.Exceptions;
import panda.mvc.ActionContext;
import panda.mvc.View;

/**
 * 返回特定的响应码
 * <p/>
 * <b>注意,400或以上,会调用resp.sendError,而非resp.setStatus.这样做的原因是
 * errorPage的配置,只有resp.sendError会触发,且绝大多数情况下,只会配置400或以上</b>
 */
public class HttpStatusView implements View {

	public static final View HTTP_404 = new HttpStatusView(404);
	// public static final View HTTP_400 = new HttpStatusView(400);
	public static final View HTTP_500 = new HttpStatusView(500);
	public static final View HTTP_502 = new HttpStatusView(502);

	/**
	 * 这个异常用于，在某个入口函数,如果你声明了 `@Fail("http:500")` 但是你真正的返回值想根据运行时决定。 <br>
	 * 那么，你就直接抛这个异常好了
	 */
	public static class HttpStatusException extends RuntimeException {

		private static final long serialVersionUID = 4035188583429445028L;

		private int status;

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public HttpStatusException(int status) {
			this.status = status;
		}

		public HttpStatusException(int status, String fmt, Object... args) {
			super(String.format(fmt, args));
			this.status = status;
		}

	}

	private int statusCode;

	public HttpStatusView(int statusCode) {
		this.statusCode = statusCode;
	}

	public void render(ActionContext ac) {
		Object obj = ac.getResult();
		HttpServletResponse res = ac.getResponse();

		int code = this.statusCode;
		if (obj != null && obj instanceof HttpStatusException) {
			code = ((HttpStatusException)obj).getStatus();
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
}
