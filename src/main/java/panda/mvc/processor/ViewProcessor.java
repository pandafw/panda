package panda.mvc.processor;

import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.View;
import panda.mvc.ViewMaker;
import panda.mvc.impl.DefaultViewMaker;

public class ViewProcessor extends AbstractProcessor {
	private static final Log log = Logs.getLog(ViewProcessor.class);

	protected View view;

	@Override
	public void init(MvcConfig config, ActionInfo ai) throws Throwable {
		// 需要特别提醒一下使用jsonView,但方法的返回值是String的!!
		if ("json".equals(ai.getOkView()) && String.class.equals(ai.getMethod().getReturnType())) {
			log.warn("Not a good idea : Return String for JsonView!! (Using @Ok(\"raw\") or return map/list/pojo)--> "
					+ ai.getMethod().toString());
		}
		view = evalView(config, ai, ai.getOkView());
	}

	public void process(ActionContext ac) throws Throwable {
		Object re = ac.getResult();
		if (re instanceof View) {
			((View)re).render(ac);
		}
		else if (view != null) {
			view.render(ac);
		}
		doNext(ac);
	}

	protected ViewMaker getViewMaker(MvcConfig config) {
		ViewMaker maker = config.getIoc().getIfExists(ViewMaker.class);
		if (maker == null) {
			maker = new DefaultViewMaker();
		}
		return maker;
	}
	
	protected View evalView(MvcConfig config, ActionInfo ai, String viewType) {
		if (Strings.isBlank(viewType)) {
			return null;
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

		View view = getViewMaker(config).make(config, ai, type, value);
		if (view != null) {
			return view;
		}

		throw Exceptions.makeThrow("Can not eval %s(\"%s\") View for %s", viewType, str, ai.getMethod());
	}
}
