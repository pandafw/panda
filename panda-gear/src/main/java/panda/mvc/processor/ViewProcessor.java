package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.view.Views;

@IocBean
public class ViewProcessor extends AbstractProcessor {
	public void process(ActionContext ac) {
		View view = ac.getView();
		if (view == null) {
			view = Views.evalView(ac.getIoc(), ac.getConfig().getOkView());
		}

		if (view != null) {
			view.render(ac);
		}
		doNext(ac);
	}
}
