package panda.mvc.view.tag.ui.theme.simple;

import java.util.Collection;

import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ActionConfirmRenderer extends AbstractMessageListRenderer {
	public ActionConfirmRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected Collection<String> getMessages() {
		return context.getActionAware().getConfirms();
	}

	@Override
	protected String getDefaultULClass() {
		return "fa-ul";
	}

	@Override
	protected String getDefaultLIClass() {
		return "text-info";
	}

	@Override
	protected String getDefaultIconClass() {
		return "fa-li fa fa-question-circle";
	}
}
