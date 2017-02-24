package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.Div;
import panda.mvc.view.tag.ui.theme.AbstractTagRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class DivRenderer extends AbstractTagRenderer<Div> {

	public DivRenderer(RenderingContext context) {
		super(context);
	}

	public void renderStart() throws IOException {
		Attributes attr = new Attributes();

		attr.id(tag)
			.cssClass(tag)
			.disabled(tag)
			.tabindex(tag)
			.tooltip(tag)
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		stag("div", attr);
	}
	
	public void renderEnd() throws IOException {
		etag("div");
	}
}
