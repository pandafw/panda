package panda.mvc.processor;

import panda.ioc.annotation.IocBean;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.view.Views;

@IocBean
public class ViewProcessor extends AbstractProcessor {
	private static final Log log = Logs.getLog(ViewProcessor.class);

	public void process(ActionContext ac) {
		if (log.isWarnEnabled()) {
			// 需要特别提醒一下使用jsonView,但方法的返回值是String的!!
			if (View.JSON.equals(ac.getInfo().getOkView()) 
					&& String.class.equals(ac.getInfo().getMethod().getReturnType())) {
				log.warn("Not a good idea : Return String for JsonView!! (Using @Ok(\"raw\") or return map/list/pojo)--> "
						+ ac.getInfo().getMethod().toString());
			}
		}
		
		View view = Views.evalView(ac.getIoc(), ac.getInfo().getOkView());

		Object re = ac.getResult();
		if (re instanceof View) {
			((View)re).render(ac);
		}
		else if (view != null) {
			view.render(ac);
		}
		doNext(ac);
	}
}
