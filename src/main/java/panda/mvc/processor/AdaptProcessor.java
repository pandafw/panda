package panda.mvc.processor;

import panda.lang.Classes;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.ParamAdaptor;
import panda.mvc.adaptor.DefaultParamAdaptor;

public class AdaptProcessor extends AbstractProcessor {

	private ParamAdaptor adaptor;

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		if (ai.getAdaptor() != null) {
			adaptor = config.getIoc().getIfExists(ai.getAdaptor());
			if (adaptor == null) {
				adaptor = Classes.born(ai.getAdaptor());
			}
		}
		if (adaptor == null) {
			adaptor = new DefaultParamAdaptor();
		}
	}

	public void process(ActionContext ac) throws Throwable {
		adaptor.adapt(ac);
		doNext(ac);
	}
}
