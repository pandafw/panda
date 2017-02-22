package panda.mvc.view.tag.ui.theme.xhtml;

import panda.mvc.view.tag.ui.Form;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RendererWrapper;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class XFormWrapper extends RendererWrapper<Form> {
	/**
	 * @param context context
	 */
	public XFormWrapper(RenderingContext context) {
		super(context);
	}

	@Override
	public void renderStart() throws Exception {
		super.renderStart();

		Attributes a = new Attributes();
		a.cssClass(tag, "p-xhtml")
		 .cssStyle(tag);

		stag("table", a);
		stag("tbody");
	}

	@Override
	public void renderEnd() throws Exception {
		etag("tbody");
		etag("table");
		super.renderEnd();
	}
}
