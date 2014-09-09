package panda.mvc.impl.processor;

import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.View;
import panda.mvc.ViewMaker;
import panda.mvc.view.VoidView;

public class ViewProcessor extends AbstractProcessor {
	private static final Log log = Logs.getLog(ViewProcessor.class);

	protected View view;

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		// 需要特别提醒一下使用jsonView,但方法的返回值是String的!!
		if ("json".equals(ai.getOkView()) && String.class.equals(ai.getMethod().getReturnType())) {
			log.warn("Not a good idea : Return String ,and using JsonView!! (Using @Ok(\"raw\") or return map/list/pojo)--> "
					+ ai.getMethod().toString());
		}
		view = evalView(config, ai, ai.getOkView());
	}

	public void process(ActionContext ac) throws Throwable {
		Object re = ac.getResult();
		Object err = ac.getError();
		if (re != null && re instanceof View) {
			((View)re).render(ac, err);
		}
		else {
			view.render(ac, null == re ? err : re);
		}
		doNext(ac);
	}

	public static View evalView(MvcConfig config, ActionInfo ai, String viewType) {
		if (Strings.isBlank(viewType)) {
			return new VoidView();
		}

		String str = viewType;
		int pos = str.indexOf(':');
		String type, value;
		if (pos > 0) {
			type = Strings.trim(str.substring(0, pos).toLowerCase());
			value = Strings.trim(pos >= (str.length() - 1) ? null : str.substring(pos + 1));
		}
		else {
			type = str;
			value = null;
		}

		for (ViewMaker maker : ai.getViewMakers()) {
			View view = maker.make(config, ai, type, value);
			if (view != null) {
				return view;
			}
		}
		throw Exceptions.makeThrow("Can not eval %s(\"%s\") View for %s", viewType, str, ai.getMethod());
	}
}
