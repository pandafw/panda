package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.mvc.view.tag.ui.DatePicker;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class DatePickerRenderer extends AbstractEndRenderer<DatePicker> {
	public DatePickerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		write("<div class=\"input-group p-datepicker\" data-spy=\"datetimepicker\" data-pick-time=\"false\">");
		
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
		
		write("<span class=\"input-group-addon p-datepicker-icon\">");
		write("<i class=\"fa fa-calendar\"></i>");
		write("</span>");
		write("</div>");
	}
}
