package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.view.Views;
import panda.servlet.HttpServlets;

@IocBean
public class FatalProcessor extends AbstractProcessor {
	private static final Log log = Logs.getLog(FatalProcessor.class);

	@Override
	public void process(ActionContext ac) {
		try {
			doNext(ac);
		}
		catch (Throwable e) {
			ac.setError(e);

			// log exception
			Log elog = Logs.getLog(e.getClass());
			if (HttpServlets.isClientAbortError(e)) {
				if (elog.isWarnEnabled()) {
					String s = HttpServlets.dumpException(ac.getRequest(), e, false);
					elog.warn(s);
				}
				return;
			}

			if (elog.isErrorEnabled()) {
				String s = HttpServlets.dumpException(ac.getRequest(), e, true);
				elog.error(s, e);
			}

			// process error view
			View view = Views.createView(ac, ac.getConfig().getFatalView());
			if (view != null) {
				try {
					HttpServlets.safeReset(ac.getResponse());
					view.render(ac);
					return;
				}
				catch (Throwable e2) {
					log.error("Failed to render fatal view: " + view, e2);
				}
			}

			HttpServlets.safeReset(ac.getResponse());
			try {
				HttpServlets.sendException(ac.getRequest(), ac.getResponse(), e);
			}
			catch (Throwable e3) {
				log.warn("Failed to send exception", e3);
			}
		}
	}
}
