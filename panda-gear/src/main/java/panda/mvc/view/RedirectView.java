package panda.mvc.view;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.util.MvcURLBuilder;
import panda.servlet.HttpServlets;

/**
 * RedirectView
 * <p>
 * Samples：
 * <p>
 * &#64;To("redirect:/pet/list.do")
 * <p>
 * or：<br>
 * new RedirectView("/pet/list.do");
 * 
 * <p>
 * &#64;To(">>://anotherContext/some") -> redirect to: /anotherContext/some
 * &#64;To(">>:/some") -> redirect to: /thisContext/some
 * &#64;To(">>:+/some") -> redirect to: /thisContext + context.getPath() + /some
 * &#64;To(">>:~/some") -> redirect to: /thisContext + context.getPath() + /../some
 */
@IocBean(singleton=false)
public class RedirectView extends AbstractView {

	public RedirectView() {
	}
	
	public RedirectView(String location) {
		setArgument(location);
	}

	public void render(ActionContext ac) {
		String a = argument;
		if (Strings.isEmpty(a)) {
			Object r = ac.getResult();
			if (r instanceof CharSequence) {
				a = r.toString();
			}
		}

		if (Strings.isEmpty(a)) {
			a = "/";
		}

		String url = MvcURLBuilder.buildPath(ac, a);
		HttpServlets.sendRedirect(ac.getResponse(), url);
	}
}
