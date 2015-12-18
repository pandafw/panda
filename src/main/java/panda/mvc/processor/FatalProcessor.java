package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.view.HttpStatusView;
import panda.servlet.HttpServlets;

@IocBean
public class FatalProcessor extends ViewProcessor {
	private static final Log log = Logs.getLog(FatalProcessor.class);

	public void process(ActionContext ac) {
		try {
			doNext(ac);
		}
		catch (Throwable e) {
			ac.setError(e);

			HttpServlets.logException(ac.getRequest(), e);

			View view = evalView(ac.getIoc(), ac.getInfo().getFatalView());
			if (view != null) {
				try {
					view.render(ac);
					return;
				}
				catch (Throwable e2) {
					log.error("Failed to render fatal view: " + view, e2);
				}
			}

			try {
				HttpServlets.saveException(ac.getRequest(), e);
				HttpStatusView.SERVER_ERROR.render(ac);
			}
			catch (Throwable e3) {
				log.error("Failed to send exception", e3);
			}
		}
	}
}
