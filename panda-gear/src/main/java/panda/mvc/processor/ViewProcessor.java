package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.View;

@IocBean
public class ViewProcessor extends AbstractProcessor {
	public void process(ActionContext ac) {
		View view = ac.getView();
		if (view == null) {
			view = Mvcs.createView(ac, ac.getConfig().getOkView());
		}

		if (view != null) {
			view.render(ac);
		}
		doNext(ac);
	}
}
