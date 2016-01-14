package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.lang.Strings;
import panda.mvc.view.tag.ui.Anchor;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class AnchorRenderer extends AbstractEndRenderer<Anchor> {

	public AnchorRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attr = new Attributes();

		String icon = tag.getIcon();
		String body = tag.getBody();
		String sicon = tag.getSicon();
		String btype = tag.getBtype();
		if (Strings.isNotEmpty(btype)) {
			btype = "btn btn-" + btype;
		}
		
		attr.id(tag)
			.href(tag)
			.target(tag)
			.cssClass(tag, btype)
			.disabled(tag)
			.tabindex(tag)
			.title(tag)
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		stag("a", attr);

		if (Strings.isNotEmpty(icon)) {
			write(xicon(icon));
			write(Strings.SPACE);
		}

		if (Strings.isNotEmpty(body)) {
			body(body, false);
		}
		else if (Strings.isNotEmpty(tag.getLabel())) {
			body(tag.getLabel(), true);
		}

		if (Strings.isNotEmpty(sicon)) {
			write(Strings.SPACE);
			write(xicon(sicon));
		}
		etag("a");
	}
}
