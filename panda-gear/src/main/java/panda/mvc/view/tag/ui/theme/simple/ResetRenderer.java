package panda.mvc.view.tag.ui.theme.simple;

import panda.mvc.view.tag.ui.Reset;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ResetRenderer extends SubmitRenderer<Reset> {
	public ResetRenderer(RenderingContext context) {
		super(context);
		button = "reset";
	}
}

