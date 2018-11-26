package panda.mvc.view.tag.ui.theme.simple;

import panda.mvc.view.tag.ui.DateTimePicker;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class DateTimePickerRenderer extends AbstractDatePickerRenderer<DateTimePicker> {
	public DateTimePickerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected String getName() {
		return "datetimepicker";
	}

	@Override
	protected void addDatetimepickerOptions(Attributes attrs) {
		if (Boolean.FALSE.equals(tag.getPickSeconds())) {
			attrs.data("pickSeconds", "false");
		}
	}
}
