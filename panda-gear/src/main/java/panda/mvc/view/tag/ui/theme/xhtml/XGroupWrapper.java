package panda.mvc.view.tag.ui.theme.xhtml;

import panda.mvc.view.tag.ui.Div;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class XGroupWrapper extends XInputWrapper<Div> {
	/**
	 * @param rc context
	 */
	public XGroupWrapper(RenderingContext rc) {
		super(rc);
	}

	@Override
	public void renderStart() throws Exception {
		renderHeader();
		writeBefore();
		setInGroup(true);
	}

	@Override
	public void renderEnd() throws Exception {
		writeAfter();
		renderFooter();
		setInGroup(false);
	}
}
