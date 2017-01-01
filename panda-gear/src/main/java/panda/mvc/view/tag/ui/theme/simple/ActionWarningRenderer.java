package panda.mvc.view.tag.ui.theme.simple;

import java.util.Collection;

import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ActionWarningRenderer extends AbstractMessageListRenderer {
	public ActionWarningRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected Collection<String> getMessages() {
		return context.getActionAlert().getWarnings();
	}

	@Override
	protected String getDefaultULClass() {
		return "fa-ul";
	}

	@Override
	protected String getDefaultLIClass() {
		return "text-warning";
	}

	@Override
	protected String getDefaultIconClass() {
		return "fa-li fa fa-exclamation-triangle";
	}
}
