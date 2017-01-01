package panda.mvc.view.tag.ui.theme.simple;

import panda.mvc.view.tag.ui.TriggerField;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class TriggerFieldRenderer extends AbstractTextFieldRenderer<TriggerField> {
	public TriggerFieldRenderer(RenderingContext context) {
		super(context);
	}

	protected String getName() {
		return "triggerfield";
	}
}
