package panda.mvc.processor;

import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.ParamAdaptor;
import panda.mvc.adaptor.DefaultParamAdaptor;

public class AdaptorProcessor extends AbstractProcessor {

	private ParamAdaptor adaptor;

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		adaptor = evalObj(config, ai.getAdaptorInfo());
		if (adaptor == null) {
			adaptor = new DefaultParamAdaptor();
		}
		adaptor.init(config, ai);
	}

	public void process(ActionContext ac) throws Throwable {
		Object[] args = adaptor.adapt(ac);
		ac.setArgs(args);
		doNext(ac);
	}
}
