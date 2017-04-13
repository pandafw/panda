package panda.mvc.view;

import panda.mvc.ActionContext;
import panda.mvc.util.MvcURLBuilder;
import panda.servlet.HttpServlets;

/**
 * RedirectView
 * <p>
 * Samples：
 * <p>
 * '@To("redirect:/pet/list.do")'
 * <p>
 * or：<br>
 * new RedirectView("/pet/list.do");
 * 
 * <p>
 * @To("//anotherContext/some") -> redirect to: /anotherContext/some
 * @To("//some") -> redirect to: /thisContext/some
 * @To("+/some") -> redirect to: /thisContext + context.getPath() + /some
 * @To("~/some") -> redirect to: /thisContext + context.getPath() + /../some
 */
public class RedirectView extends AbstractPathView {

	public RedirectView(String dest) {
		super(dest);
	}

	public void render(ActionContext ac) {
		String path = evalPath(ac);

		String url = MvcURLBuilder.buildPath(ac, path);
		
		HttpServlets.sendRedirect(ac.getResponse(), url);
	}
}
