package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.File;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class FileRenderer extends AbstractEndRenderer<File> {

	public FileRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes a = new Attributes();

		a.add("type", "file")
		 .id(tag)
		 .name(tag)
		 .css(this)
		 .size(tag)
		 .value(tag)
		 .disabled(tag)
		 .accept(tag)
		 .tabindex(tag)
		 .title(tag)
		 .commons(tag)
		 .events(tag)
		 .dynamics(tag);
		xtag("input", a);
	}
}
