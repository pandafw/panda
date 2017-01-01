package panda.mvc.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.mvc.ActionContext;
import panda.servlet.HttpServlets;

/**
 * 重定向视图
 * <p>
 * 在入口函数上声明：
 * <p>
 * '@Ok("redirect:/pet/list.nut")'
 * <p>
 * 实际上相当于：<br>
 * new ServerRedirectView("/pet/list.nut");
 * 
 */
public class RedirectView extends AbstractPathView {

	public RedirectView(String dest) {
		super(dest);
	}

	public void render(ActionContext ac) {
		HttpServletRequest req = ac.getRequest();
		HttpServletResponse res = ac.getResponse();

		String path = evalPath(ac);

		// This site
		if (path.startsWith("//")) {
			path = path.substring(1);
		}
		// Absolute path, add the context path for it
		else if (path.length() > 0 && path.charAt(0) == '/') {
			path = req.getContextPath() + path;
		}
		
		HttpServlets.sendRedirect(res, path);
	}
}
