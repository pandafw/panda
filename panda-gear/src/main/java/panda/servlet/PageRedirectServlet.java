package panda.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.lang.Strings;

/**
 */
@SuppressWarnings("serial")
public class PageRedirectServlet extends HttpServlet {
	/**
	 * welcomeFile
	 */
	private String welcomeFile;

	/**
	 * redirectURL
	 */
	private String redirectURL;
	
	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		welcomeFile = servletConfig.getInitParameter("welcomeFile");
		redirectURL = servletConfig.getInitParameter("redirectURL");
	}

	private String getRedirectURL(String page) {
		String url = redirectURL;
		
		page = getServletContext().getRealPath(page);
		File file = new File(page);
		if (file.exists()) {
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader(file));
				String ln = br.readLine();
				if (Strings.isNotEmpty(ln)) {
					url = ln;
				}
			}
			catch (Exception ex) {
			}
		}
		
		return url;
	}
	
	/**
	 *
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String page = request.getRequestURI();
		if (page.endsWith("/")) {
			page += welcomeFile;
		}
		
		String url = this.getRedirectURL(page);
		
		HttpServlets.sendRedirect(response, url);
	}

	/**
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
	}
}
