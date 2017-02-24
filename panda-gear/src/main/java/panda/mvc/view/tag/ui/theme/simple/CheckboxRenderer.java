package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.lang.Strings;
import panda.mvc.view.tag.ui.Checkbox;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class CheckboxRenderer extends AbstractEndRenderer<Checkbox> {
	public CheckboxRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		write("<label class=\"checkbox-inline\">");

		Attributes attrs = new Attributes();
		attrs.add("type", "checkbox")
			.id(tag)
			.name(tag)
			.css(this, "p-checkbox")
			.add("value", tag.getFieldValue())
			.addIfTrue("checked", tag.getValue())
			.readonly(tag)
			.disabled(tag)
			.tabindex(tag)
			.tooltip(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		xtag("input", attrs);

		if (Strings.isNotEmpty(tag.getFieldLabel())) {
			body(tag.getFieldLabel());
		}
		write("&nbsp;</label>");
	}
}
