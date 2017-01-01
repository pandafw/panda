package panda.mvc.view.tag.ui.theme.simple;

import java.util.Collection;

import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ActionErrorRenderer extends AbstractMessageListRenderer {
	public ActionErrorRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected Collection<String> getMessages() {
		return context.getActionAlert().getErrors();
	}

	@Override
	protected String getDefaultULClass() {
		return "fa-ul";
	}

	@Override
	protected String getDefaultLIClass() {
		return "text-danger";
	}

	@Override
	protected String getDefaultIconClass() {
		return "fa-li fa fa-exclamation-circle";
	}
}
