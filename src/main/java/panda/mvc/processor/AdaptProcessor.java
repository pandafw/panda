package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.ParamAdaptor;
import panda.mvc.adaptor.DefaultParamAdaptor;

@IocBean
public class AdaptProcessor extends AbstractProcessor {
	@Override
	public void process(ActionContext ac) {
		ParamAdaptor adaptor = null;
		
		if (ac.getAdaptorType() != null) {
			adaptor = Mvcs.born(ac.getIoc(), ac.getAdaptorType());
		}
		if (adaptor == null) {
			adaptor = Mvcs.born(ac.getIoc(), DefaultParamAdaptor.class);
		}

		adaptor.adapt(ac);
		doNext(ac);
	}
}
