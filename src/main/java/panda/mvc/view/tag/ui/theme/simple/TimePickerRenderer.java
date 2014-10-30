package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.TimePicker;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class TimePickerRenderer extends AbstractEndRenderer<TimePicker> {
	public TimePickerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		write("<div class=\"input-group p-tag\" data-spy=\"datetimepicker\" data-pick-date=\"false\">");
		
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
			.addIfExists("data-format", tag.getPattern())
			.commons(tag)
			.events(tag)
			.dynamics(tag);
		xtag("input", attr);
		
		write("<span class=\"input-group-addon p-tag-icon\">");
		write("<i class=\"fa fa-clock-o\"></i>");
		write("</span>");
		write("</div>");
	}
	
}
