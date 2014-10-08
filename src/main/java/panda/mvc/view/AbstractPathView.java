package panda.mvc.view;

import panda.el.ElTemplate;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.View;

public abstract class AbstractPathView implements View {

	private String dest;

	public AbstractPathView(String dest) {
		this.dest = dest;
	}

	protected String evalPath(ActionContext ac) {
		if (Strings.isEmpty(dest)) {
			return null;
		}

		return ElTemplate.evaluate(dest, ac);
	}
}
