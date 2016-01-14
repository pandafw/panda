package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.lang.Strings;
import panda.mvc.view.tag.ui.Button;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class ButtonRenderer extends AbstractEndRenderer<Button> {

	public ButtonRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attr = new Attributes();

		String icon = tag.getIcon();
		String sicon = tag.getSicon();
		String btype = defs(tag.getBtype(), "default");
		String body = tag.getBody();

		attr.id(tag)
			.cssClass(tag, "btn btn-" + btype)
			.disabled(tag)
			.tabindex(tag)
			.title(tag)
			.cssStyle(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		stag("button", attr);

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
		etag("button");
	}
}
