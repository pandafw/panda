package panda.mvc.processor;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.servlet.HttpServlets;

public class FatalProcessor extends ViewProcessor {
	private static final Log log = Logs.getLog(FatalProcessor.class);

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		view = evalView(config.getIoc(), ai.getFatalView());
	}

	public void process(ActionContext ac) throws Throwable {
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
