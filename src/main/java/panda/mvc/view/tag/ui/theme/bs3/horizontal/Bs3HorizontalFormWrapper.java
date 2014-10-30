package panda.mvc.view.tag.ui.theme.bs3.horizontal;

import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.theme.RendererWrapper;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class Bs3HorizontalFormWrapper extends RendererWrapper<Form> {
	/**
	 * @param context context
	 */
	public Bs3HorizontalFormWrapper(RenderingContext context) {
		super(context);
	}


	@Override
	public void renderStart() throws Exception {
		addCss("form-horizontal");
		super.renderStart();
	}
}
