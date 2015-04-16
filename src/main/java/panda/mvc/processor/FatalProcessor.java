package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.servlet.HttpServlets;

@IocBean
public class FatalProcessor extends ViewProcessor {
	private static final Log log = Logs.getLog(FatalProcessor.class);

	public void process(ActionContext ac) {
		View view = evalView(ac.getIoc(), ac.getInfo().getFatalView());
		
		try {
			doNext(ac);
		}
		catch (Throwable e) {
			ac.setError(e);

			HttpServlets.logException(ac.getRequest(), e);

			if (view != null) {
				try {
					view.render(ac);
				}
				catch (Throwable e2) {
					log.error("Failed to render fatal view: " + view, e2);
				}
			}
		}
	}
}
