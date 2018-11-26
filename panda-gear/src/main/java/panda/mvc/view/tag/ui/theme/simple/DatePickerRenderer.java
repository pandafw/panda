package panda.mvc.view.tag.ui.theme.simple;

import panda.mvc.view.tag.ui.DatePicker;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class DatePickerRenderer extends AbstractDatePickerRenderer<DatePicker> {
	public DatePickerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected String getName() {
		return "datepicker";
	}
	
	@Override
	protected void addDatetimepickerOptions(Attributes attrs) {
		attrs.data("pickTime", "false");
	}
}
