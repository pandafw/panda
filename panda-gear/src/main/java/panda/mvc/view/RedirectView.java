package panda.mvc.view;

import panda.ioc.annotation.IocBean;
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
@IocBean(singleton=false)
public class RedirectView extends AbstractView {

	public RedirectView() {
	}
	
	public RedirectView(String location) {
		setDescription(location);
	}

	public void render(ActionContext ac) {
		String url = MvcURLBuilder.buildPath(ac, description);
		
		HttpServlets.sendRedirect(ac.getResponse(), url);
	}
}
