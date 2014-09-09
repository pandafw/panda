package panda.mvc.impl.processor;

import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.RequestPath;

public class FailProcessor extends ViewProcessor {

	private static final Log log = Logs.getLog(FailProcessor.class);

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		view = evalView(config, ai, ai.getFailView());
	}

	public void process(ActionContext ac) throws Throwable {
		if (log.isWarnEnabled()) {
			String uri = RequestPath.getRequestPath(ac.getRequest());
			log.warn(String.format("Error@%s :", uri), ac.getError());
		}
		super.process(ac);
	}
}
