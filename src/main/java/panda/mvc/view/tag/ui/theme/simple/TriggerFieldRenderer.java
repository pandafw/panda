package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

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
		attr.add("type", "text")
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
		
		attr = new Attributes();
		attr.clear()
			.add("class", "input-group-addon p-triggerfield-icon")
			.addIfExists("onclick", tag.getOntrigger());
		stag("span", attr);
		write("<i class=\"fa fa-caret-down\"></i>");

		etag("span");
		write("</div>");
	}
}
