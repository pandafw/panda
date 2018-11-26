package panda.mvc.view.tag.ui.theme.simple;

import panda.mvc.view.tag.ui.TimePicker;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class TimePickerRenderer extends AbstractDatePickerRenderer<TimePicker> {
	public TimePickerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected String getName() {
		return "timepicker";
	}
	
	@Override
	protected void addDatetimepickerOptions(Attributes attrs) {
		attrs.data("pickDate", "false");
		if (Boolean.FALSE.equals(tag.getPickSeconds())) {
			attrs.data("pickSeconds", "false");
		}
	}
}
