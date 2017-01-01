package panda.mvc.view.tag.ui.theme.simple;

import java.util.Collection;

import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ActionMessageRenderer extends AbstractMessageListRenderer {
	public ActionMessageRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected Collection<String> getMessages() {
		return context.getActionAlert().getMessages();
	}

	@Override
	protected String getDefaultULClass() {
		return "fa-ul";
	}

	@Override
	protected String getDefaultLIClass() {
		return "text-success";
	}

	@Override
	protected String getDefaultIconClass() {
		return "fa-li fa fa-info-circle";
	}
}
