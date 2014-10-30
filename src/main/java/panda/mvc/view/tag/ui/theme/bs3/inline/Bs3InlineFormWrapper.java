package panda.mvc.view.tag.ui.theme.bs3.inline;

import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.theme.RendererWrapper;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class Bs3InlineFormWrapper extends RendererWrapper<Form> {
	/**
	 * @param context context
	 */
	public Bs3InlineFormWrapper(RenderingContext context) {
		super(context);
	}

	@Override
	public void renderStart() throws Exception {
		addCss("form-inline");
		super.renderStart();
	}
}
