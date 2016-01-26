package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.lang.Strings;
import panda.mvc.view.tag.ui.TriggerField;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class TriggerFieldRenderer extends AbstractEndRenderer<TriggerField> {
	public TriggerFieldRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		write("<div class=\"input-group p-triggerfield\">");
		
		Attributes attr = new Attributes();
		if (tag.hasLeftTrigger()) {
			attr.clear()
				.add("class", "input-group-addon p-triggerfield-licon")
				.addIfExists("onclick", tag.getOnlclick());
			stag("div", attr);
			write(tag.getLtext());
			if (Strings.isNotEmpty(tag.getLicon())) {
				write(xicon(tag.getLicon()));
			}
			etag("div");
		}

		attr.clear()
			.add("type", "text")
			.id(tag)
			.name(tag)
			.css(this)
			.size(tag)
			.maxlength(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag)
			.formatValue(this, tag)
			.title(tag)
			.placeholder(tag)
			.mask(tag)
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		xtag("input", attr);
		
		if (tag.hasRightTrigger()) {
			attr = new Attributes();
			attr.clear()
				.add("class", "input-group-addon p-triggerfield-ricon")
				.addIfExists("onclick", tag.getOnrclick());
			stag("div", attr);
			write(tag.getRtext());
			if (Strings.isNotEmpty(tag.getRicon())) {
				write(xicon(tag.getRicon()));
			}
			etag("div");
		}

		write("</div>");
	}
}
