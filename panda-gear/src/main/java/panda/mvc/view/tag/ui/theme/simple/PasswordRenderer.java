package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.Password;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class PasswordRenderer extends AbstractEndRenderer<Password> {

	public PasswordRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attrs = new Attributes();

		attrs.add("type", "password")
			.id(tag)
			.name(tag)
			.css(this, "p-password")
			.size(tag)
			.maxlength(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag)
			.title(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);

		if (tag.isShowPassword()) {
			attrs.formatValue(this, tag);
		}

		xtag("input", attrs);
	}
}
