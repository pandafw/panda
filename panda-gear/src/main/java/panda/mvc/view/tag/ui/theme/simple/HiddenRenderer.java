package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.Hidden;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class HiddenRenderer extends AbstractEndRenderer<Hidden> {
	public HiddenRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes a = new Attributes();

		a.add("type", "hidden")
		 .id(tag)
		 .name(tag)
		 .cssClass(tag, "p-hidden")
		 .formatValue(this, tag)
		 .disabled(tag)
		 .cssStyle(tag)
		 .dynamics(tag);
		xtag("input", a);
	}
}
