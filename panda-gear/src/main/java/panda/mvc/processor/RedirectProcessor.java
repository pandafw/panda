package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.annotation.Redirect;
import panda.mvc.util.MvcURLBuilder;
import panda.servlet.HttpServlets;


/**
 * Redirect Processor
 * <p>
 * Samples：
 * <p>
 * '@Redirect("/pet/list")'
 * <p>
 * 
 * <p>
 * @Redirect("//anotherContext/some") -> redirect to: /anotherContext/some
 * @Redirect("//some") -> redirect to: /thisContext/some
 * @Redirect("+/some") -> redirect to: /thisContext + context.getPath() + /some
 * @Redirect("~/some") -> redirect to: /thisContext + context.getPath() + /../some
 */
@IocBean
public class RedirectProcessor extends AbstractProcessor {
	private static final Log log = Logs.getLog(RedirectProcessor.class);

	public void process(ActionContext ac) {
		Redirect r = ac.getMethod().getAnnotation(Redirect.class);
		if (r != null) {
			if (Strings.isNotEmpty(r.value())) {
				String url = MvcURLBuilder.buildPath(ac, r.value());
				if (log.isDebugEnabled()) {
					log.debug("Redirect to: " + url);
				}
				HttpServlets.sendRedirect(ac.getResponse(), url);
				return;
			}

			if (r.toslash()) {
				if (!Strings.endsWithChar(ac.getPath(), '/')) {
					String url = ac.getBase() + ac.getPath() + '/';
					if (log.isDebugEnabled()) {
						log.debug("Redirect to: " + url);
					}
					HttpServlets.sendRedirect(ac.getResponse(), url);
					return;
				}
			}
		}

		doNext(ac);
	}
}
