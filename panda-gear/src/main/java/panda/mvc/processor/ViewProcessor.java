package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.view.Views;

@IocBean
public class ViewProcessor extends AbstractProcessor {
	@Override
	public void process(ActionContext ac) {
		View view = ac.getView();
		if (view == null) {
			view = Views.createDefaultView(ac);
		}

		if (view != null) {
			view.render(ac);
		}
		doNext(ac);
	}
}
