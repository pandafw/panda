package panda.mvc.view.tag.ui.theme.simple;

import panda.mvc.view.tag.ui.TextField;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class TextFieldRenderer extends AbstractTextFieldRenderer<TextField> {
	public TextFieldRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected String getName() {
		return "p-textfield";
	}
}
