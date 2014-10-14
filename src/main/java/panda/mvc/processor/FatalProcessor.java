package panda.mvc.processor;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.RequestPath;

public class FatalProcessor extends ViewProcessor {

	private static final Log log = Logs.getLog(FatalProcessor.class);

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		view = evalView(config.getIoc(), ai.getFatalView());
	}

	public void process(ActionContext ac) throws Throwable {
		if (view == null) {
			doNext(ac);
			return;
		}
		
		try {
			doNext(ac);
		}
		catch (Throwable e) {
			ac.setError(e);

			if (log.isWarnEnabled()) {
				String uri = RequestPath.getRequestPath(ac.getRequest());
				log.warn(String.format("Error@[ %s ]:", uri), e);
			}

			view.render(ac);
		}
	}
}
