package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.Token;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class TokenRenderer extends AbstractEndRenderer<Token> {
	public TokenRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes a = new Attributes();

		a.add("type", "hidden")
		 .id(tag)
		 .name(tag)
		 .cssClass(tag, "p-hidden")
		 .value(tag.getToken())
		 .disabled(tag)
		 .cssStyle(tag)
		 .dynamics(tag);
		xtag("input", a);
	}
}
