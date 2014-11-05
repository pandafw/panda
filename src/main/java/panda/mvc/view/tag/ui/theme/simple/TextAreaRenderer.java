package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.TextArea;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class TextAreaRenderer extends AbstractEndRenderer<TextArea> {
	public TextAreaRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attrs = new Attributes();

		attrs.id(tag)
			.name(tag)
			.css(this, "p-" + tag.getLayout() + "area")
			.cols(tag)
			.rows(tag)
			.wrap(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag)
			.maxlength(tag)
			.title(tag)
			.placeholder(tag)
			.addIfExists("layout", tag.getLayout())
			.commons(tag)
			.events(tag)
			.dynamics(tag);

		stag("tag", attrs);
		write(formatValue(tag));
		etag("tag");
	}
}