package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.Icon;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class IconRenderer extends AbstractEndRenderer<Icon> {

	public IconRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attr = new Attributes();

		String icon = tag.getIcon();
		
		attr.id(tag)
			.cssClass(tag, ticon(icon))
			.disabled(tag)
			.tabindex(tag)
			.title(tag)
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		stag("i", attr);
		etag("i");
	}
}
