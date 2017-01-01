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
		a.add("class", join("p-xhtml", tag.getCssClass()))
		 .cssStyle(tag);

		stag("table", a);
	}

	@Override
	public void renderEnd() throws Exception {
		etag("table");
		super.renderEnd();
	}
}
