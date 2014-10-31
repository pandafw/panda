package panda.mvc.view;

import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;

public abstract class AbstractPathView extends AbstractView {

	public AbstractPathView(String path) {
		super(path);
	}
	
	protected String evalPath(ActionContext ac) {
		if (Strings.isEmpty(location)) {
			return null;
		}

		return Mvcs.translate(location, ac);
	}
}

