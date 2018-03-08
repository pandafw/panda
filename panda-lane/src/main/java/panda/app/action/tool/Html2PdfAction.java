package panda.app.action.tool;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.util.pdf.Html2Pdf;
import panda.io.FileTypes;
import panda.io.MimeTypes;
import panda.io.stream.ByteArrayOutputStream;
import panda.ioc.annotation.IocInject;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.net.URLHelper;
import panda.net.http.HttpHeader;
import panda.servlet.HttpServletResponser;
import panda.servlet.ServletRequestHeaderMap;

@At("${super_path}/html2pdf")
@Auth(AUTH.SUPER)
public class Html2PdfAction extends AbstractAction {
	private static final Log log = Logs.getLog(Html2PdfAction.class);
	
	@IocInject
	private Html2Pdf html2pdf;

	private String getFileNameFromUrl(String url) {
		String fn = Strings.stripEnd(url, "/");
		int i = fn.lastIndexOf('/');
		if (i >= 0) {
			fn = fn.substring(i + 1);
		}
		i = fn.lastIndexOf('?');
		if (i >= 0) {
			fn = fn.substring(0, i);
		}
		i = fn.lastIndexOf(';');
		if (i >= 0) {
			fn = fn.substring(0, i);
		}
		return fn;
	}

	public static class Arg {
		private String url;
		private String charset;
		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}
		/**
		 * @param url the url to set
		 */
		public void setUrl(String url) {
			this.url = Strings.stripToNull(url);
			if (this.url != null) {
				int i = this.url.indexOf("://");
				if (i < 0) {
					this.url = "http://" + this.url;
				}
			}
		}
		/**
		 * @return the charset
		 */
		public String getCharset() {
			return charset;
		}
		/**
		 * @param charset the charset to set
		 */
		public void setCharset(String charset) {
			this.charset = Strings.stripToNull(charset);
		}
	}
	
	/**
	 * execute
	 * @param arg the input arguments
	 * @return view
	 * @throws Exception if an error occurs
	 */
	@At("")
	@To(View.RAW)
	public Object execute(@Param Arg arg) throws Exception {
		if (Strings.isEmpty(arg.url)) {
			arg.url = getText("url-default", "http://www.google.com");
			return Views.sftl(context);
		}

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			HttpServletRequest request = getRequest();
			HttpServletResponse response = getResponse();

			String domain = URLHelper.getURLDomain(arg.url);
			String server = request.getServerName();

			Map<String, Object> headers = null;
			if (domain.equalsIgnoreCase(server)) {
				headers = new HttpHeader();
				headers.putAll(new ServletRequestHeaderMap(request));
				headers.remove(HttpHeader.ACCEPT_ENCODING);
				headers.remove(HttpHeader.CONNECTION);
				headers.remove(HttpHeader.HOST);
			}

			html2pdf.setCharset(arg.charset);
			html2pdf.setHeaders(headers);
			html2pdf.setUrl(arg.url);
			html2pdf.process(baos);
			
			byte[] pdf = baos.toByteArray();

			HttpServletResponser hsr = new HttpServletResponser(request, response);
			hsr.setContentLength(Integer.valueOf(pdf.length));
			hsr.setContentType(MimeTypes.APP_PDF);
			hsr.setFileName(getFileNameFromUrl(arg.url) + '.' + FileTypes.PDF);
			hsr.setMaxAge(0);
			hsr.setBody(pdf);

			return hsr;
		}
		catch (Throwable e) {
			log.warn("html2pdf execute error", e);
			addActionError(Exceptions.getStackTrace(e));
			return Views.sftl(context);
		}
	}
}
