package panda.mvc;

import javax.servlet.http.HttpServletRequest;

import panda.lang.Strings;
import panda.servlet.HttpServlets;

public class RequestPath {

	private String url;

	private String path;

	private String suffix;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public String toString() {
		return Strings.isBlank(suffix) ? path : path + "." + suffix;
	}

	//---------------------------------------
	/**
	 * 获取当前请求的路径，并去掉后缀
	 * 
	 * @param req HTTP 请求对象
	 */
	public static String getRequestPath(HttpServletRequest req) {
		return getRequestPathObject(req).getPath();
	}

	/**
	 * 获取当前请求的路径，并去掉后缀
	 * 
	 * @param req HTTP 请求对象
	 */
	public static RequestPath getRequestPathObject(HttpServletRequest req) {
		String url = HttpServlets.getServletURI(req);
		return getRequestPathObject(url);
	}

	/**
	 * 获取当前请求的路径，并去掉后缀
	 * 
	 * @param url 请求路径的URL
	 */
	public static RequestPath getRequestPathObject(String url) {
		RequestPath rr = new RequestPath();
		rr.setUrl(url);
		if (null != url) {
			int lio = 0;
			if (!url.endsWith("/")) {
				int ll = url.lastIndexOf('/');
				lio = url.lastIndexOf('.');
				if (lio < ll)
					lio = -1;
			}
			if (lio > 0) {
				rr.setPath(url.substring(0, lio));
				rr.setSuffix(url.substring(lio + 1));
			}
			else {
				rr.setPath(url);
				rr.setSuffix("");
			}
		}
		else {
			rr.setPath("");
			rr.setSuffix("");
		}
		return rr;
	}
}

